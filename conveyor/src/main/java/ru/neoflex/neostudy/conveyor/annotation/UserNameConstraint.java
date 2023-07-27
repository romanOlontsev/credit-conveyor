package ru.neoflex.neostudy.conveyor.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.neoflex.neostudy.conveyor.validator.UserNameValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserNameConstraint {
    String message() default "The middle name must be empty or hava between 2 and 30 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean required() default false;
}
