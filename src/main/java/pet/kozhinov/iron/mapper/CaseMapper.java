package pet.kozhinov.iron.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pet.kozhinov.iron.entity.Case;
import pet.kozhinov.iron.entity.Payment;
import pet.kozhinov.iron.entity.Status;
import pet.kozhinov.iron.entity.dto.CaseDto;
import pet.kozhinov.iron.exception.BadRequestException;
import pet.kozhinov.iron.repository.CaseRepository;
import pet.kozhinov.iron.repository.LoanRepository;
import pet.kozhinov.iron.repository.PersonRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static pet.kozhinov.iron.utils.ValidationUtils.validateId;

@RequiredArgsConstructor
@Component(CaseMapper.NAME)
public class CaseMapper implements Mapper<Case, CaseDto> {
    public static final String NAME = "iron_CaseMapper";
    private final LoanMapper loanMapper;
    private final PaymentMapper paymentMapper;
    private final AccurateConverter accurateNumberConverter;

    private final CaseRepository caseRepository;
    private final PersonRepository personRepository;
    private final LoanRepository loanRepository;

    @Override
    public CaseDto map1(Case aCase) {
        CaseDto dto = new CaseDto();
        dto.setId(aCase.getId().toString());
        dto.setClientId(aCase.getClient().getId().toString());
        dto.setLoanId(aCase.getLoan().getId().toString());
        dto.setAmount(accurateNumberConverter.convert1(aCase.getAmount()));
        dto.setDurationMonths(aCase.getDurationMonths());
        dto.setStatusBankSide(aCase.getStatusBankSide().toString());
        dto.setStatusClientSide(aCase.getStatusClientSide().toString());
        dto.setConfirmationDate(aCase.getConfirmationDate());
        dto.setClosed(aCase.isClosed());

        dto.setLoan(loanMapper.map1(aCase.getLoan()));
        dto.setPayments(aCase.getPayments().stream()
            .map(paymentMapper::map1)
            .collect(Collectors.toSet()));

        dto.setPaid(countPaid(aCase));

        return dto;
    }

    @Override
    public Case map2(CaseDto dto) {
        Case aCase;
        if (dto.getId() == null) {
            aCase = createNewOne(dto);
        } else {
            String id = dto.getId();
            validateId(id);
            aCase = caseRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new BadRequestException("A case with given id wasn't found"));
        }

        aCase.setAmount(accurateNumberConverter.convert2(dto.getAmount()));
        aCase.setDurationMonths(dto.getDurationMonths());

        if (dto.getStatusBankSide() != null) {
            aCase.setStatusBankSide(Status.valueOf(dto.getStatusBankSide().toUpperCase()));
        }

        if (dto.getStatusClientSide() != null) {
            aCase.setStatusClientSide(Status.valueOf(dto.getStatusClientSide().toUpperCase()));
        }

        aCase.setConfirmationDate(dto.getConfirmationDate());
        return aCase;
    }

    private Case createNewOne(CaseDto dto) {
        Case aCase = new Case();
        String clientId = dto.getClientId();
        String loanId = dto.getLoanId();
        if (clientId == null) {
            throw new BadRequestException("Client is not specified");
        }
        if (loanId == null) {
            throw new BadRequestException("Loan is not specified");
        }

        validateId(clientId);
        validateId(loanId);
        aCase.setClient(personRepository.findById(Long.parseLong(clientId))
                .orElseThrow(() -> new BadRequestException("A client with given id wasn't found")));
        aCase.setLoan(loanRepository.findById(Long.parseLong(loanId))
                .orElseThrow(() -> new BadRequestException("A loan with given id wasn't found")));
        aCase.setPayments(new LinkedList<>());

        return aCase;
    }

    private BigDecimal countPaid(Case aCase) {
        BigDecimal paid = BigDecimal.ZERO;
        Collection<Payment> paidPayments = aCase.getPayments().stream()
                .filter(Payment::isPaidOut)
                .collect(Collectors.toList());
        for (Payment payment : paidPayments) {
            paid = paid.add(payment.getAmount());
        }
        return paid;
    }
}
