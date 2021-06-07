package pet.kozhinov.iron.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import pet.kozhinov.iron.validation.ValidLoanCaseAmountRange;
import pet.kozhinov.iron.validation.ValidLoanCaseDurationRange;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

@ValidLoanCaseAmountRange
@ValidLoanCaseDurationRange
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CaseDto {

    private String id;

    @NotBlank
    private String clientId;

    @NotBlank
    private String creatorId;

    @NotBlank
    private String loanId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @JsonIgnoreProperties(allowGetters = true)
    private BigDecimal paid;

    @Positive
    private Integer durationMonths;

    private String statusBankSide;
    private String statusClientSide;
    private LocalDate confirmationDate;

    private LoanDto loan;
    private Boolean closed;
    private Collection<PaymentDto> payments;

    public void setLoan(LoanDto loan) {
        if (loan != null) {
            loanId = loan.getId();
        }
        this.loan = loan;
    }
}
