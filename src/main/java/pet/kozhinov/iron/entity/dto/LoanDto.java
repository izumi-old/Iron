package pet.kozhinov.iron.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class LoanDto {
    private String id;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Integer minDurationMonths;
    private Integer maxDurationMonths;
    private Float interestRate;
}
