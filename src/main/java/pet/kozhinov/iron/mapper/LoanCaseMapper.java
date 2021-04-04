package pet.kozhinov.iron.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pet.kozhinov.iron.entity.LoanCase;
import pet.kozhinov.iron.entity.Status;
import pet.kozhinov.iron.entity.dto.LoanCaseDto;
import pet.kozhinov.iron.exception.InternalServerErrorException;
import pet.kozhinov.iron.service.LoanService;
import pet.kozhinov.iron.service.PersonService;

import java.util.LinkedList;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class LoanCaseMapper {
    private final PersonService personService;
    private final LoanService loanService;
    private final LoanMapper loanMapper;
    private final AccurateConverter accurateNumberConverter;

    public LoanCase fromDto(LoanCaseDto dto) {
        LoanCase loanCase = new LoanCase();
        loanCase.setId(UUID.fromString(dto.getLoanId()));
        loanCase.setClient(personService.getById(dto.getClientId())
                .orElseThrow(InternalServerErrorException::new));
        loanCase.setLoan(loanService.getById(dto.getLoanId())
                .orElseThrow(InternalServerErrorException::new));
        loanCase.setPayments(new LinkedList<>());
        loanCase.setAmount(accurateNumberConverter.convert2(dto.getAmount()));
        loanCase.setDurationMonths(dto.getDurationMonths());
        loanCase.setStatusBankSide(Status.valueOf(dto.getStatusBankSide().toUpperCase()));
        loanCase.setStatusClientSide(Status.valueOf(dto.getStatusClientSide().toUpperCase()));
        loanCase.setConfirmationDate(dto.getConfirmationDate());
        return loanCase;
    }

    public LoanCaseDto toDto(LoanCase loanCase) {
        LoanCaseDto dto = new LoanCaseDto();
        dto.setId(loanCase.getId().toString());
        dto.setClientId(loanCase.getClient().getId().toString());
        dto.setLoanId(loanCase.getLoan().getId().toString());
        dto.setAmount(accurateNumberConverter.convert1(loanCase.getAmount()));
        dto.setDurationMonths(loanCase.getDurationMonths());
        dto.setStatusBankSide(loanCase.getStatusBankSide().toString());
        dto.setStatusClientSide(loanCase.getStatusClientSide().toString());
        dto.setConfirmationDate(loanCase.getConfirmationDate());
        dto.setClosed(loanCase.isClosed());

        dto.setClient(loanCase.getClient());
        dto.setLoan(loanMapper.toDto(loanCase.getLoan()));
        dto.setPayments(loanCase.getPayments());
        return dto;
    }
}
