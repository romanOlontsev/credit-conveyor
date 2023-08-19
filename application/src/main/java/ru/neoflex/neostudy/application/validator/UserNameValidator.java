package ru.neoflex.neostudy.application.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.neoflex.neostudy.application.annotation.UserNameConstraint;

public class UserNameValidator implements ConstraintValidator<UserNameConstraint, String> {
    private boolean required;

    @Override
    public void initialize(UserNameConstraint constraintAnnotation) {
        required = constraintAnnotation.required();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        String regex = "^[a-zA-Z]{2,30}$";
        if (required) {
            return value.matches(regex);
        } else {
            return value.isEmpty() || value.matches(regex);
        }
    }
}
