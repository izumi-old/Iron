package pet.kozhinov.iron.validation;

import pet.kozhinov.iron.utils.AccurateNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccurateNumberPositiveValidator implements ConstraintValidator<AccurateNumberPositive, AccurateNumber> {

    @Override
    public boolean isValid(AccurateNumber value, ConstraintValidatorContext context) {
        return value != null && value.compareTo(AccurateNumber.ZERO) > 0;
    }
}
