package pet.kozhinov.iron.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PaymentDto {
    private String id;
    private LocalDate date;
    private BigDecimal amount;
    private BigDecimal loanRepaymentAmount;
    private BigDecimal interestRepaymentAmount;
    private Boolean paidOut;
}
