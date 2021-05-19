package pet.kozhinov.iron.validation;

import pet.kozhinov.iron.entity.Loan;
import pet.kozhinov.iron.entity.LoanCase;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoanCaseDurationRangeValidator implements ConstraintValidator<ValidLoanCaseDurationRange, LoanCase> {

    @Override
    public boolean isValid(LoanCase value, ConstraintValidatorContext context) {
        Loan loan = value.getLoan();
        if (loan == null) {
            return false;
        }

        return value.getDurationMonths() >= loan.getMinDurationMonths() &&
                value.getDurationMonths() <= loan.getMaxDurationMonths();
    }
}
