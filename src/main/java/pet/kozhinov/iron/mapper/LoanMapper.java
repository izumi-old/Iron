package pet.kozhinov.iron.mapper;

import org.springframework.stereotype.Component;
import pet.kozhinov.iron.entity.Loan;
import pet.kozhinov.iron.entity.dto.LoanDto;
import pet.kozhinov.iron.utils.AccurateNumber;

@Component(LoanMapper.NAME)
public class LoanMapper implements Mapper<Loan, LoanDto> {
    public static final String NAME = "iron_LoanMapper";

    @Override
    public LoanDto map1(Loan loan) {
        LoanDto dto = new LoanDto();
        dto.setId(loan.getId().toString());
        dto.setInterestRate(loan.getInterestRate());
        dto.setMinAmount(loan.getMinAmount().getValue());
        dto.setMaxAmount(loan.getMaxAmount().getValue());
        dto.setMinDurationMonths(loan.getMinDurationMonths());
        dto.setMaxDurationMonths(loan.getMaxDurationMonths());
        return dto;
    }

    @Override
    public Loan map2(LoanDto dto) {
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
}
