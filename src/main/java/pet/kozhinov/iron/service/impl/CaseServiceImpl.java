package pet.kozhinov.iron.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.Case;
import pet.kozhinov.iron.entity.Status;
import pet.kozhinov.iron.entity.dto.CaseDto;
import pet.kozhinov.iron.entity.notification.email.CaseConfirmedNotification;
import pet.kozhinov.iron.entity.notification.email.Email;
import pet.kozhinov.iron.entity.notification.email.NewLoanCaseNotification;
import pet.kozhinov.iron.exception.NotFoundException;
import pet.kozhinov.iron.mapper.CaseMapper;
import pet.kozhinov.iron.repository.CaseRepository;
import pet.kozhinov.iron.service.CaseService;
import pet.kozhinov.iron.service.NotificationService;
import pet.kozhinov.iron.service.PaymentsService;
import pet.kozhinov.iron.service.PersonService;
import pet.kozhinov.iron.utils.AccurateNumber;
import pet.kozhinov.iron.utils.ValidationUtils;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

import static pet.kozhinov.iron.entity.Status.APPROVED;
import static pet.kozhinov.iron.utils.Utils.toPage;

@RequiredArgsConstructor
@Service(CaseServiceImpl.NAME)
public class CaseServiceImpl implements CaseService {
    public static final String NAME = "iron_CaseServiceImpl";
    private final CaseRepository repository;
    private final CaseMapper mapper;
    private final PaymentsService paymentsService;
    private final NotificationService<Email> emailNotificationService;
    private final PersonService personService;

    private static LocalDate addMonths(LocalDate to, int howMuch) {
        return ChronoUnit.MONTHS.addTo(to, howMuch);
    }

    @Override
    public Collection<CaseDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::map1)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CaseDto> getAllPending() {
        Collection<CaseDto> result = new LinkedList<>();
        result.addAll(repository.findAllByStatusBankSideAndStatusClientSideAndClosed(
                Status.PENDING, APPROVED, false).stream()
                .map(mapper::map1)
                .collect(Collectors.toList()));
        result.addAll(repository.findAllByStatusBankSideAndStatusClientSideAndClosed(
                APPROVED, Status.PENDING, false).stream()
                .map(mapper::map1)
                .collect(Collectors.toList()));

        return result;
    }

    @Override
    public Collection<CaseDto> getAllAcceptedInProgress() {
        return repository.findAllByStatusBankSideAndStatusClientSideAndClosed(
                APPROVED, APPROVED, false).stream()
                .map(mapper::map1)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<CaseDto> getAllPendingForClient(@NotBlank String clientId) {
        Long id = Long.parseLong(clientId);
        Collection<CaseDto> result = new LinkedList<>();
        result.addAll(repository.findAllByClientIdAndStatusBankSideAndStatusClientSide(
                id, Status.PENDING, APPROVED).stream()
                .map(mapper::map1)
                .collect(Collectors.toList()));
        result.addAll(repository.findAllByClientIdAndStatusBankSideAndStatusClientSide(
                id, APPROVED, Status.PENDING).stream()
                .map(mapper::map1)
                .collect(Collectors.toList()));
        return result;
    }

    @Override
    public Collection<CaseDto> getAllAcceptedForClient(@NotBlank String clientId) {
        return repository.findAllByClientIdAndStatusBankSideAndStatusClientSide(
                Long.parseLong(clientId), APPROVED, APPROVED).stream()
                .map(mapper::map1)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CaseDto> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size))
                .map(mapper::map1);
    }

    @Override
    public Page<CaseDto> getAllPending(int page, int size) {
        return toPage(getAllPending(), page, size);
    }

    @Override
    public Page<CaseDto> getAllAcceptedInProgress(int page, int size) {
        return toPage(getAllAcceptedInProgress(), page, size);
    }

    @Override
    public Page<CaseDto> getAllPendingForClient(int page, int size, String clientId) {
        return toPage(getAllPendingForClient(clientId), page, size);
    }

    @Override
    public Page<CaseDto> getAllAcceptedForClient(int page, int size, String clientId) {
        return toPage(getAllAcceptedForClient(clientId), page, size);
    }

    @Override
    public Optional<CaseDto> getById(String id) {
        return repository.findById(Long.parseLong(id))
                .map(mapper::map1);
    }

    @Override
    public CaseDto update(CaseDto toUpdate) {
        Long uId = Long.parseLong(toUpdate.getId());
        Case fromDb = repository.findById(uId)
                .orElseThrow(NotFoundException::new);

        boolean statusUpdated = false;
        boolean confirmed = fromDb.getConfirmationDate() != null;
        if (!confirmed) {
            updateFields(fromDb, toUpdate);
            if (toUpdate.getStatusBankSide() != null) {
                fromDb.setStatusBankSide(Status.valueOf(toUpdate.getStatusBankSide()));
                statusUpdated = true;
            }
            if (toUpdate.getStatusClientSide() != null) {
                fromDb.setStatusClientSide(Status.valueOf(toUpdate.getStatusClientSide()));
                statusUpdated = true;
            }

            confirmed = statusUpdated && fromDb.getStatusBankSide() == APPROVED
                    && fromDb.getStatusClientSide() == APPROVED;
            if (confirmed) {
                LocalDate now = LocalDate.now();
                fromDb.setConfirmationDate(now);
                fromDb.getPayments().forEach(payment -> payment.setDate(addMonths(now, payment.getOrderNumber())));
            }
        }

        ValidationUtils.validate(fromDb);

        CaseDto updated = mapper.map1(repository.save(fromDb));
        if (confirmed) {
            String clientEmail = fromDb.getClient().getEmail();
            if (clientEmail != null) {
                emailNotificationService.sendAsynchronous(new CaseConfirmedNotification(clientEmail));
            }
        }

        return updated;
    }

    @Override
    public CaseDto save(CaseDto loanOffer) {
        Case toSave = mapper.map2(loanOffer);
        toSave.setPayments(paymentsService.schedule(toSave));
        toSave.setStatusBankSide(Status.PENDING);
        toSave.setStatusClientSide(Status.PENDING);
        ValidationUtils.validate(toSave);
        CaseDto saved = mapper.map1(repository.save(toSave));

        personService.getById(loanOffer.getClientId())
            .ifPresent(personDto -> {
                String clientEmail = personDto.getEmail();
                emailNotificationService.sendAsynchronous(new NewLoanCaseNotification(clientEmail));
            });
        return saved;
    }

    @Override
    public void delete(String id) {
        repository.deleteById(Long.parseLong(id));
    }

    private void updateFields(Case fromDb, CaseDto toUpdate) {
        if (toUpdate.getAmount() != null) {
            fromDb.setAmount(new AccurateNumber(toUpdate.getAmount()));
        }
        if (toUpdate.getClosed() != null) {
            fromDb.setClosed(toUpdate.getClosed());
        }
        if (toUpdate.getDurationMonths() != null) {
            fromDb.setDurationMonths(toUpdate.getDurationMonths());
        }
    }
}
