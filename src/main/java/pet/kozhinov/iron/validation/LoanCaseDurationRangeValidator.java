package pet.kozhinov.iron.validation;

import pet.kozhinov.iron.entity.Loan;
import pet.kozhinov.iron.entity.Case;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoanCaseDurationRangeValidator implements ConstraintValidator<ValidLoanCaseDurationRange, Case> {

    @Override
    public boolean isValid(Case value, ConstraintValidatorContext context) {
        Loan loan = value.getLoan();
        if (loan == null) {
            return false;
        }

        return value.getDurationMonths() >= loan.getMinDurationMonths() &&
                value.getDurationMonths() <= loan.getMaxDurationMonths();
    }
}
