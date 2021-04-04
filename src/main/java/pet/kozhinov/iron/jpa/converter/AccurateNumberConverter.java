package pet.kozhinov.iron.jpa.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pet.kozhinov.iron.mapper.AccurateConverter;
import pet.kozhinov.iron.utils.AccurateNumber;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Converter
public class AccurateNumberConverter implements AttributeConverter<AccurateNumber, BigDecimal> {
    private final AccurateConverter converter;

    @Override
    public BigDecimal convertToDatabaseColumn(AccurateNumber attribute) {
        return converter.convert1(attribute);
    }

    @Override
    public AccurateNumber convertToEntityAttribute(BigDecimal dbData) {
        return converter.convert2(dbData);
    }
}
