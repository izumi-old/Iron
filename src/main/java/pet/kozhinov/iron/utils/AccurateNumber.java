package pet.kozhinov.iron.utils;

import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AccurateNumber implements Comparable<AccurateNumber> {
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final int SCALE = 8;
    public static final AccurateNumber ZERO = new AccurateNumber(BigDecimal.ZERO);

    private final BigDecimal value;

    public AccurateNumber(String value) {
        this(new BigDecimal(value));
    }

    public AccurateNumber(BigDecimal value) {
        this.value = value.setScale(SCALE, ROUNDING_MODE);
    }

    public AccurateNumber(float value) {
        this(BigDecimal.valueOf(value));
    }

    public AccurateNumber add(AccurateNumber augend) {
        return new AccurateNumber(this.value.add(augend.value));
    }

    public AccurateNumber subtract(int subtrahend) {
        return new AccurateNumber(this.value.subtract(new BigDecimal(subtrahend)));
    }

    public AccurateNumber subtract(AccurateNumber subtrahend) {
        return new AccurateNumber(this.value.subtract(subtrahend.value));
    }

    public AccurateNumber multiply(AccurateNumber multiplicand) {
        return new AccurateNumber(this.value.multiply(multiplicand.value));
    }

    public AccurateNumber divide(int divisor) {
        return new AccurateNumber(this.value.divide(new BigDecimal(divisor), SCALE, ROUNDING_MODE));
    }

    public AccurateNumber divide(AccurateNumber divisor) {
        return new AccurateNumber(this.value.divide(divisor.value, SCALE, ROUNDING_MODE));
    }

    public AccurateNumber pow(int n) {
        return new AccurateNumber(this.value.pow(n));
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public int compareTo(AccurateNumber accurateNumber) {
        return this.value.compareTo(accurateNumber.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        if (obj.getClass() == AccurateNumber.class) {
            return value.equals(((AccurateNumber)obj).value);
        } else {
            return value.equals(obj);
        }
    }
}
