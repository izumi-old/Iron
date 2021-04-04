package pet.kozhinov.iron.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.LoanCase;
import pet.kozhinov.iron.entity.Payment;
import pet.kozhinov.iron.entity.Status;
import pet.kozhinov.iron.entity.dto.LoanCaseDto;
import pet.kozhinov.iron.exception.NotFoundException;
import pet.kozhinov.iron.mapper.LoanCaseMapper;
import pet.kozhinov.iron.repository.LoanCaseRepository;
import pet.kozhinov.iron.service.LoanCaseService;
import pet.kozhinov.iron.utils.AccurateNumber;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static pet.kozhinov.iron.utils.Constants.ANNUITY_COEFFICIENT;
import static pet.kozhinov.iron.utils.Constants.MAX_PERCENTS;
import static pet.kozhinov.iron.utils.Constants.MONTHS_IN_YEAR;

@RequiredArgsConstructor
@Service
public class LoanCaseServiceImpl implements LoanCaseService {
    private final LoanCaseRepository repository;
    private final LoanCaseMapper mapper;

    @Override
    public Collection<LoanCaseDto> getAllPending() {
        Collection<LoanCaseDto> result = new LinkedList<>();
        result.addAll(repository.findAllByStatusBankSideAndStatusClientSideAndClosed(
                        Status.PENDING, Status.APPROVED, false).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList()));
        result.addAll(repository.findAllByStatusBankSideAndStatusClientSideAndClosed(
                        Status.APPROVED, Status.PENDING, false).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList()));
        return result;
    }

    @Override
    public Collection<LoanCaseDto> getAllAcceptedInProgress() {
        return repository.findAllByStatusBankSideAndStatusClientSideAndClosed(
                Status.APPROVED, Status.APPROVED, false).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<LoanCaseDto> getAllPendingForClient(String clientId) {
        UUID id = UUID.fromString(clientId);
        Collection<LoanCaseDto> result = new LinkedList<>();
        result.addAll(repository.findAllByClientIdAndStatusBankSideAndStatusClientSide(
                id, Status.PENDING, Status.APPROVED).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList()));
        result.addAll(repository.findAllByClientIdAndStatusBankSideAndStatusClientSide(
                id, Status.APPROVED, Status.PENDING).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList()));
        return result;
    }

    @Override
    public Collection<LoanCaseDto> getAllAcceptedForClient(String clientId) {
        return repository.findAllByClientIdAndStatusBankSideAndStatusClientSide(
                UUID.fromString(clientId), Status.APPROVED, Status.APPROVED).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LoanCaseDto> getById(String id) {
        return repository.findById(UUID.fromString(id)).map(mapper::toDto);
    }

    @Override
    public LoanCaseDto update(String id, LoanCaseDto loanCase) {
        UUID uId = UUID.fromString(id);
        LoanCase toUpdate = repository.findById(uId)
                .orElseThrow(NotFoundException::new);

        if (loanCase.getStatusBankSide() != null) {
            toUpdate.setStatusBankSide(Status.valueOf(loanCase.getStatusBankSide()));
        }
        if (loanCase.getStatusClientSide() != null) {
            toUpdate.setStatusClientSide(Status.valueOf(loanCase.getStatusClientSide()));
        }
        if (toUpdate.getStatusBankSide() == Status.APPROVED && toUpdate.getStatusClientSide() == Status.APPROVED) {
            LocalDate now = LocalDate.now();
            toUpdate.setConfirmationDate(now);
            toUpdate.getPayments().forEach(payment -> payment.setDate(addMonths(now, payment.getOrderNumber())));
        }

        return mapper.toDto(repository.save(toUpdate));
    }

    @Override
    public LoanCaseDto save(LoanCaseDto loanOffer) {
        LoanCase toSave = mapper.fromDto(loanOffer);
        toSave.setPayments(schedule(toSave));
        return mapper.toDto(repository.save(toSave));
    }

    @Override
    public void delete(String id) {
        repository.deleteById(UUID.fromString(id));
    }

    /*
    formulas:

    1) P = S * (i + i / ((1+i)^n - 1));

    y = (1+i)^n -1;
    x = i / y;
    P = S * (i + x);

    where
    P - month payment amount
    S - loan amount
    i - month interest rate
    n - duration in months

    2) I(n) = S(n) * i;

    where
    I(n) - percents amount
    S(n) - left loan amount

    3) B(n) = P - I(n);

    where
    B - body amount
    */
    private static Collection<Payment> schedule(LoanCase loanCase) {
        AccurateNumber monthInterestRate = new AccurateNumber(loanCase.getLoan().getInterestRate())
                .divide(MAX_PERCENTS)
                .divide(MONTHS_IN_YEAR);

        AccurateNumber y = ((new AccurateNumber(ANNUITY_COEFFICIENT)
                .add(monthInterestRate))
                .pow(loanCase.getDurationMonths()))
                .subtract(ANNUITY_COEFFICIENT);
        AccurateNumber x = monthInterestRate.divide(y);

        AccurateNumber annuityCoefficient = monthInterestRate.add(x);

        AccurateNumber monthPayment = loanCase.getAmount().multiply(annuityCoefficient);
        AccurateNumber left = loanCase.getAmount();

        Collection<Payment> result = new LinkedList<>();
        for (int i = 1; i <= loanCase.getDurationMonths(); i++) {
            AccurateNumber percents = left.multiply(monthInterestRate);
            AccurateNumber body = monthPayment.subtract(percents);

            Payment payment = new Payment();
            payment.setOrderNumber(i);
            payment.setAmount(monthPayment.getValue());
            payment.setInterestRepaymentAmount(percents.getValue());
            payment.setLoanRepaymentAmount(body.getValue());

            left = left.subtract(body);
            result.add(payment);
        }

        return result;
    }

    private static LocalDate addMonths(LocalDate to, int howMuch) {
        return ChronoUnit.MONTHS.addTo(to, howMuch);
    }
}
