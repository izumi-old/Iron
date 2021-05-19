package pet.kozhinov.iron.entity;

import lombok.Data;
import pet.kozhinov.iron.jpa.converter.AccurateNumberConverter;
import pet.kozhinov.iron.utils.AccurateNumber;
import pet.kozhinov.iron.validation.Positive;
import pet.kozhinov.iron.validation.ValidLoanCaseAmountRange;
import pet.kozhinov.iron.validation.ValidLoanCaseDurationRange;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

@ValidLoanCaseDurationRange
@ValidLoanCaseAmountRange
@Data
@Table
@Entity
public class LoanCase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @OneToOne(optional = false)
    private Person client;

    @NotNull
    @ManyToOne(optional = false)
    private Loan loan;

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

    private LocalDate confirmationDate;

    private boolean closed;
}
