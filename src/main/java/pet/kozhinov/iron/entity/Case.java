package pet.kozhinov.iron.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import pet.kozhinov.iron.jpa.converter.AccurateNumberConverter;
import pet.kozhinov.iron.utils.AccurateNumber;
import pet.kozhinov.iron.validation.Positive;
import pet.kozhinov.iron.validation.ValidLoanCaseAmountRange;
import pet.kozhinov.iron.validation.ValidLoanCaseDurationRange;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;

import static javax.persistence.GenerationType.IDENTITY;

@ValidLoanCaseDurationRange
@ValidLoanCaseAmountRange
@Data
@Table(name = "loan_case")
@Entity
public class Case {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @NotNull
    @OneToOne(optional = false)
    private Person client;

    @NotNull
    @OneToOne(optional = false)
    private Person creator;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    @ManyToOne(optional = false)
    private Loan loan;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @NotNull
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "loan_case_id", nullable = false)
    private Collection<Payment> payments;

    @Positive
    @NotNull
    @Column(nullable = false)
    @Convert(converter = AccurateNumberConverter.class)
    private AccurateNumber amount;

    @javax.validation.constraints.Positive
    @NotNull
    @Column(nullable = false)
    private int durationMonths;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status statusBankSide = Status.PENDING;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status statusClientSide = Status.PENDING;

    @Column
    private LocalDate confirmationDate;

    @Column
    private boolean closed;
}
