package pet.kozhinov.iron.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pet.kozhinov.iron.entity.Case;
import pet.kozhinov.iron.entity.Status;
import pet.kozhinov.iron.entity.dto.CaseDto;
import pet.kozhinov.iron.entity.notification.email.CaseConfirmedNotification;
import pet.kozhinov.iron.entity.notification.email.Email;
import pet.kozhinov.iron.entity.notification.email.NewLoanCaseNotification;
import pet.kozhinov.iron.exception.ForbiddenException;
import pet.kozhinov.iron.exception.NotFoundException;
import pet.kozhinov.iron.mapper.CaseMapper;
import pet.kozhinov.iron.repository.CaseRepository;
import pet.kozhinov.iron.service.CaseService;
import pet.kozhinov.iron.service.NotificationService;
import pet.kozhinov.iron.service.PaymentsService;
import pet.kozhinov.iron.service.PersonService;
import pet.kozhinov.iron.utils.AccurateNumber;
import pet.kozhinov.iron.utils.ValidationUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static pet.kozhinov.iron.entity.Status.APPROVED;

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
    public Page<CaseDto> getCases(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::map1);
    }

    @Override
    public Page<CaseDto> getCasesByPersonId(Pageable pageable, Long clientId) {
        return repository.findAllByClientId(pageable, clientId)
                .map(mapper::map1);
    }

    @Override
    public Page<CaseDto> getCasesByStatusesAndPersonId(Pageable pageable, Status bankStatus, Status clientStatus) {
        return repository.findAllByStatusBankSideAndStatusClientSideAndClosed(
                pageable, bankStatus, clientStatus, false)
                .map(mapper::map1);
    }

    @Override
    public Page<CaseDto> getCasesByStatusesAndPersonId(Pageable pageable,
                                                       Long personId,
                                                       Status bankStatus,
                                                       Status clientStatus) {
        return repository.findAllByClientIdAndStatusBankSideAndStatusClientSide(
                pageable, personId, bankStatus, clientStatus)
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
        if (loanOffer.getCreatorId() != null && loanOffer.getCreatorId().equals(loanOffer.getClientId())) {
            throw new ForbiddenException();
        }

        Case toSave = mapper.map2(loanOffer);
        toSave.setPayments(paymentsService.schedule(toSave));
        toSave.setStatusBankSide(Status.PENDING);
        toSave.setStatusClientSide(Status.PENDING);
        ValidationUtils.validate(toSave);
        CaseDto saved = mapper.map1(repository.save(toSave));

        personService.getPersonById(loanOffer.getClientId())
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
