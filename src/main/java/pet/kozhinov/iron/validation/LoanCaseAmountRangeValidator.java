package pet.kozhinov.iron.validation;

import pet.kozhinov.iron.entity.Loan;
import pet.kozhinov.iron.entity.LoanCase;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoanCaseAmountRangeValidator implements ConstraintValidator<ValidLoanCaseAmountRange, LoanCase> {

    @Override
    public boolean isValid(LoanCase value, ConstraintValidatorContext context) {
        Loan loan = value.getLoan();
        if (loan == null) {
            return false;
        }

        return value.getAmount().compareTo(loan.getMinAmount()) >= 0 &&
                value.getAmount().compareTo(loan.getMaxAmount()) <= 0 ;
    }
}
