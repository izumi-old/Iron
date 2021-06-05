package pet.kozhinov.iron.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pet.kozhinov.iron.entity.Loan;
import pet.kozhinov.iron.entity.dto.LoanDto;
import pet.kozhinov.iron.exception.BadRequestException;
import pet.kozhinov.iron.repository.LoanRepository;
import pet.kozhinov.iron.utils.AccurateNumber;

@RequiredArgsConstructor
@Component(LoanMapper.NAME)
public class LoanMapper implements Mapper<Loan, LoanDto> {
    public static final String NAME = "iron_LoanMapper";

    private final LoanRepository loanRepository;

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
        Loan loan;
        if (dto.getId() == null) {
            loan = new Loan();
        } else {
            String id = dto.getId();
            loan = loanRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new BadRequestException("A loan with given id wasn't found"));
        }

        loan.setInterestRate(dto.getInterestRate());
        loan.setMinDurationMonths(dto.getMinDurationMonths());
        loan.setMaxDurationMonths(dto.getMaxDurationMonths());
        loan.setMinAmount(dto.getMinAmount() != null ? new AccurateNumber(dto.getMinAmount()) : null);
        loan.setMaxAmount(dto.getMaxAmount() != null ? new AccurateNumber(dto.getMaxAmount()) : null);
        return loan;
    }
}
