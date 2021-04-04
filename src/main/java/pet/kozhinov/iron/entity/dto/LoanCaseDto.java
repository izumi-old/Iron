package pet.kozhinov.iron.entity.dto;

import lombok.Data;
import pet.kozhinov.iron.entity.Payment;
import pet.kozhinov.iron.entity.Person;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

@Data
public class LoanCaseDto {
    private String id;
    private String clientId;
    private String loanId;
    private BigDecimal amount;
    private Integer durationMonths;
    private String statusBankSide;
    private String statusClientSide;
    private LocalDate confirmationDate;

    private Person client;
    private LoanDto loan;
    private Boolean closed;
    private Collection<Payment> payments;
}
