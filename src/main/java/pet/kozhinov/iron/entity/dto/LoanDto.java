package pet.kozhinov.iron.entity.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LoanDto {
    private String id;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Integer minDurationMonths;
    private Integer maxDurationMonths;
    private Float interestRate;
}
