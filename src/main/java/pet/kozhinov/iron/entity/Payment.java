package pet.kozhinov.iron.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table(name = "payment")
@Entity
public class Payment {

    @GeneratedValue
    @Id
    private Long id;

    @Column
    private int orderNumber;

    @Column
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

    @Column
    private boolean paidOut;
}
