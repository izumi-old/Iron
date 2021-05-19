package pet.kozhinov.iron.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AccurateNumberPositiveValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccurateNumberPositive {
    String message() default "Number is not positive";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
