package pet.kozhinov.iron.entity;

import lombok.Data;
import pet.kozhinov.iron.jpa.converter.AccurateNumberConverter;
import pet.kozhinov.iron.utils.AccurateNumber;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Table
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(nullable = false)
    @Convert(converter = AccurateNumberConverter.class)
    private AccurateNumber minAmount;

    @NotNull
    @Column(nullable = false)
    @Convert(converter = AccurateNumberConverter.class)
    private AccurateNumber maxAmount;

    @Column(nullable = false)
    private int minDurationMonths;

    @Column(nullable = false)
    private int maxDurationMonths;

    @Column(nullable = false)
    private float interestRate;
}
