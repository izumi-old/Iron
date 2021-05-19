package pet.kozhinov.iron.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LoanCaseAmountRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLoanCaseAmountRange {
    String message() default "Wrong loan case amount range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
