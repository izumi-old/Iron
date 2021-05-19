package pet.kozhinov.iron.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.LoanCase;
import pet.kozhinov.iron.entity.Status;
import pet.kozhinov.iron.entity.dto.LoanCaseDto;
import pet.kozhinov.iron.exception.NotFoundException;
import pet.kozhinov.iron.mapper.LoanCaseMapper;
import pet.kozhinov.iron.repository.LoanCaseRepository;
import pet.kozhinov.iron.service.LoanCaseService;
import pet.kozhinov.iron.service.PaymentsService;
import pet.kozhinov.iron.utils.ValidationUtils;

import javax.validation.constraints.NotBlank;
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
    private final PaymentsService paymentsService;

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
    public Collection<LoanCaseDto> getAllPendingForClient(@NotBlank String clientId) {
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
    public Collection<LoanCaseDto> getAllAcceptedForClient(@NotBlank String clientId) {
        return repository.findAllByClientIdAndStatusBankSideAndStatusClientSide(
                UUID.fromString(clientId), Status.APPROVED, Status.APPROVED).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LoanCaseDto> getById(@NotBlank String id) {
        return repository.findById(UUID.fromString(id)).map(mapper::toDto);
    }

    @Override
    public LoanCaseDto update(@NotBlank String id, LoanCaseDto loanCase) {
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

        ValidationUtils.validate(toUpdate);

        return mapper.toDto(repository.save(toUpdate));
    }

    @Override
    public LoanCaseDto save(LoanCaseDto loanOffer) {
        LoanCase toSave = mapper.fromDto(loanOffer);
        toSave.setPayments(paymentsService.schedule(toSave));
        ValidationUtils.validate(toSave);
        return mapper.toDto(repository.save(toSave));
    }

    @Override
    public void delete(@NotBlank String id) {
        repository.deleteById(UUID.fromString(id));
    }

    private static LocalDate addMonths(LocalDate to, int howMuch) {
        return ChronoUnit.MONTHS.addTo(to, howMuch);
    }
}
