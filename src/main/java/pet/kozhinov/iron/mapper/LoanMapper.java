package pet.kozhinov.iron.mapper;

import org.springframework.stereotype.Component;
import pet.kozhinov.iron.entity.Loan;
import pet.kozhinov.iron.entity.dto.LoanDto;
import pet.kozhinov.iron.utils.AccurateNumber;

@Component
public class LoanMapper {
    public Loan fromDto(LoanDto dto) {
        Loan loan = new Loan();
        loan.setId(Long.parseLong(dto.getId()));
        if (dto.getInterestRate() != null) {
            loan.setInterestRate(dto.getInterestRate());
        }
        if (dto.getMinDurationMonths() != null) {
            loan.setMinDurationMonths(dto.getMinDurationMonths());
        }
        if (dto.getMaxDurationMonths() != null) {
            loan.setMaxDurationMonths(dto.getMaxDurationMonths());
        }
        loan.setMinAmount(dto.getMinAmount() != null ? new AccurateNumber(dto.getMinAmount()) : null);
        loan.setMaxAmount(dto.getMaxAmount() != null ? new AccurateNumber(dto.getMaxAmount()) : null);
        return loan;
    }

    public LoanDto toDto(Loan loan) {
        LoanDto dto = new LoanDto();
        dto.setId(loan.getId().toString());
        dto.setInterestRate(loan.getInterestRate());
        dto.setMinAmount(loan.getMinAmount().getValue());
        dto.setMaxAmount(loan.getMaxAmount().getValue());
        dto.setMinDurationMonths(loan.getMinDurationMonths());
        dto.setMaxDurationMonths(loan.getMaxDurationMonths());
        return dto;
    }
}
