package pet.kozhinov.iron.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.Case;
import pet.kozhinov.iron.entity.Status;
import pet.kozhinov.iron.entity.dto.CaseDto;
import pet.kozhinov.iron.exception.NotFoundException;
import pet.kozhinov.iron.mapper.CaseMapper;
import pet.kozhinov.iron.repository.CaseRepository;
import pet.kozhinov.iron.service.CaseService;
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

@RequiredArgsConstructor
@Service
public class CaseServiceImpl implements CaseService {
    private final CaseRepository repository;
    private final CaseMapper mapper;
    private final PaymentsService paymentsService;

    @Override
    public Collection<CaseDto> getAllPending() {
        Collection<CaseDto> result = new LinkedList<>();
        result.addAll(repository.findAllByStatusBankSideAndStatusClientSideAndClosed(
                        Status.PENDING, Status.APPROVED, false).stream()
                .map(mapper::map1)
                .collect(Collectors.toList()));
        result.addAll(repository.findAllByStatusBankSideAndStatusClientSideAndClosed(
                        Status.APPROVED, Status.PENDING, false).stream()
                .map(mapper::map1)
                .collect(Collectors.toList()));
        return result;
    }

    @Override
    public Collection<CaseDto> getAllAcceptedInProgress() {
        return repository.findAllByStatusBankSideAndStatusClientSideAndClosed(
                Status.APPROVED, Status.APPROVED, false).stream()
                .map(mapper::map1)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CaseDto> getAllPendingForClient(@NotBlank String clientId) {
        Long id = Long.parseLong(clientId);
        Collection<CaseDto> result = new LinkedList<>();
        result.addAll(repository.findAllByClientIdAndStatusBankSideAndStatusClientSide(
                id, Status.PENDING, Status.APPROVED).stream()
                .map(mapper::map1)
                .collect(Collectors.toList()));
        result.addAll(repository.findAllByClientIdAndStatusBankSideAndStatusClientSide(
                id, Status.APPROVED, Status.PENDING).stream()
                .map(mapper::map1)
                .collect(Collectors.toList()));
        return result;
    }

    @Override
    public Collection<CaseDto> getAllAcceptedForClient(@NotBlank String clientId) {
        return repository.findAllByClientIdAndStatusBankSideAndStatusClientSide(
                Long.parseLong(clientId), Status.APPROVED, Status.APPROVED).stream()
                .map(mapper::map1)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CaseDto> getById(@NotBlank String id) {
        return repository.findById(Long.parseLong(id)).map(mapper::map1);
    }

    @Override
    public CaseDto update(@NotBlank String id, CaseDto loanCase) {
        Long uId = Long.parseLong(id);
        Case toUpdate = repository.findById(uId)
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

        return mapper.map1(repository.save(toUpdate));
    }

    @Override
    public CaseDto save(CaseDto loanOffer) {
        Case toSave = mapper.map2(loanOffer);
        toSave.setPayments(paymentsService.schedule(toSave));
        ValidationUtils.validate(toSave);
        return mapper.map1(repository.save(toSave));
    }

    @Override
    public void delete(@NotBlank String id) {
        repository.deleteById(Long.parseLong(id));
    }

    private static LocalDate addMonths(LocalDate to, int howMuch) {
        return ChronoUnit.MONTHS.addTo(to, howMuch);
    }
}
