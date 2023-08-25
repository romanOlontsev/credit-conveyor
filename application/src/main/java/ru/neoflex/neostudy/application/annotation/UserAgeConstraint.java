package ru.neoflex.neostudy.application.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.neoflex.neostudy.application.validator.UserAgeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserAgeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAgeConstraint {
    String message() default "The middle name must be empty or hava between 2 and 30 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
