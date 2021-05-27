package pet.kozhinov.iron.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pet.kozhinov.iron.entity.Case;
import pet.kozhinov.iron.entity.Status;
import pet.kozhinov.iron.entity.dto.CaseDto;
import pet.kozhinov.iron.exception.InternalServerErrorException;
import pet.kozhinov.iron.repository.PersonRepository;
import pet.kozhinov.iron.service.LoanService;

import java.util.LinkedList;

@RequiredArgsConstructor
@Component
public class CaseMapper implements Mapper<Case, CaseDto> {
    private final PersonRepository personRepository;
    private final LoanService loanService;
    private final LoanMapper loanMapper;
    private final AccurateConverter accurateNumberConverter;

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

        dto.setClient(aCase.getClient());
        dto.setLoan(loanMapper.toDto(aCase.getLoan()));
        dto.setPayments(aCase.getPayments());
        return dto;
    }

    @Override
    public Case map2(CaseDto dto) {
        Case aCase = new Case();
        aCase.setId(Long.parseLong(dto.getLoanId()));
        aCase.setClient(personRepository.findById(Long.parseLong(dto.getClientId()))
                .orElseThrow(InternalServerErrorException::new));
        aCase.setLoan(loanService.getById(dto.getLoanId())
                .orElseThrow(InternalServerErrorException::new));
        aCase.setPayments(new LinkedList<>());
        aCase.setAmount(accurateNumberConverter.convert2(dto.getAmount()));
        aCase.setDurationMonths(dto.getDurationMonths());
        aCase.setStatusBankSide(Status.valueOf(dto.getStatusBankSide().toUpperCase()));
        aCase.setStatusClientSide(Status.valueOf(dto.getStatusClientSide().toUpperCase()));
        aCase.setConfirmationDate(dto.getConfirmationDate());
        return aCase;
    }
}
