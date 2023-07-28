package ru.neoflex.neostudy.conveyor.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.neoflex.neostudy.conveyor.annotation.UserAgeConstraint;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class UserAgeValidator implements ConstraintValidator<UserAgeConstraint, LocalDate> {
    private static final Pattern pattern = Pattern.compile("^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$");

    @Override
    public void initialize(UserAgeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if (pattern.matcher(value.toString())
                   .matches()) {
            int years = Period.between(value, LocalDate.now())
                              .getYears();
            return years >= 18;
        }
        return false;
    }
}
