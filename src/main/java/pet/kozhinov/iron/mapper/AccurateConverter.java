package pet.kozhinov.iron.mapper;

import org.springframework.stereotype.Component;
import pet.kozhinov.iron.utils.AccurateNumber;

import java.math.BigDecimal;

@Component
public class AccurateConverter {
    public BigDecimal convert1(AccurateNumber number) {
        if (number == null) {
            return null;
        }

        return number.getValue();
    }

    public AccurateNumber convert2(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return null;
        }

        return new AccurateNumber(bigDecimal);
    }
}
