package pet.kozhinov.iron.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import pet.kozhinov.iron.entity.Payment;
import pet.kozhinov.iron.entity.Person;
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
    private String loanId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @Positive
    private Integer durationMonths;

    private String statusBankSide;
    private String statusClientSide;
    private LocalDate confirmationDate;

    private Person client;
    private LoanDto loan;
    private Boolean closed;
    private Collection<Payment> payments;

    public void setClient(Person client) {
        if (client != null) {
            clientId = client.getId().toString();
        }
        this.client = client;
    }

    public void setLoan(LoanDto loan) {
        if (loan != null) {
            loanId = loan.getId().toString();
        }
        this.loan = loan;
    }
}
