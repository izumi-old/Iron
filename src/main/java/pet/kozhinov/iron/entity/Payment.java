package pet.kozhinov.iron.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Table
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int orderNumber;

    private LocalDate date;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private BigDecimal loanRepaymentAmount;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private BigDecimal interestRepaymentAmount;

    private boolean paidOut;
}
