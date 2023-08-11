package ru.neoflex.neosudy.deal.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.neoflex.neosudy.deal.annotation.UserNameConstraint;

public class UserNameValidator implements ConstraintValidator<UserNameConstraint, String> {
    private boolean required;

    @Override
    public void initialize(UserNameConstraint constraintAnnotation) {
        required = constraintAnnotation.required();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String regex = "^[a-zA-Z]{2,30}$";
        if (required) {
            if (value == null) {
                return false;
            }
        } else {
            if (value == null) {
                return true;
            }
        }
        return value.matches(regex);
    }
}
