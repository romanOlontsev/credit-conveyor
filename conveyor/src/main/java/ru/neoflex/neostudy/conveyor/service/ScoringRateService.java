package ru.neoflex.neostudy.conveyor.service;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.conveyor.exception.BadRequestException;
import ru.neoflex.neostudy.conveyor.model.dto.ScoringDataDTO;
import ru.neoflex.neostudy.conveyor.model.types.EmploymentStatus;
import ru.neoflex.neostudy.conveyor.model.types.Gender;
import ru.neoflex.neostudy.conveyor.model.types.MaritalStatus;
import ru.neoflex.neostudy.conveyor.model.types.Position;
import ru.neoflex.neostudy.conveyor.utils.BigDecimalRoughComparison;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
public class ScoringRateService {

    public BigDecimal calculateScoringRate(ScoringDataDTO scoringRequest) {
        checkSalary(scoringRequest);
        checkAge(scoringRequest);
        checkExperience(scoringRequest);
        BigDecimal employmentStatusRate = getEmploymentStatusRate(scoringRequest);
        BigDecimal employeePositionRate = getEmployeePositionRate(scoringRequest);
        BigDecimal maritalStatusRate = getMaritalStatusRate(scoringRequest);
        BigDecimal dependentAmountRate = getDependentAmountRate(scoringRequest);
        BigDecimal genderRate = getGenderRate(scoringRequest);
        return employmentStatusRate.add(employeePositionRate)
                                   .add(maritalStatusRate)
                                   .add(dependentAmountRate)
                                   .add(genderRate);
    }

    private void checkSalary(ScoringDataDTO scoringRequest) {
        BigDecimal amount = scoringRequest.getAmount();
        BigDecimal salary = scoringRequest.getEmployment()
                                          .getSalary();
        boolean isAmountMoreThanTwentySalaries = BigDecimalRoughComparison.check(
                amount,
                BigDecimalRoughComparison.Operator.GREATER_THAN,
                salary.multiply(BigDecimal.valueOf(20)));
        if (isAmountMoreThanTwentySalaries) {
            String exceptionMessage = "Credit denial: the credit amount is more than 20 salaries";
            throw new ValidationException(exceptionMessage);
        }
    }

    private void checkAge(ScoringDataDTO scoringRequest) {
        int age = Period.between(scoringRequest.getBirthDate(), LocalDate.now())
                        .getYears();
        if (age < 20 || age > 60) {
            String exceptionMessage = "Credit denial: credit is issued from 20 to 60 years";
            throw new ValidationException(exceptionMessage);
        }
    }

    private void checkExperience(ScoringDataDTO scoringRequest) {
        Integer workExperienceTotal = scoringRequest.getEmployment()
                                                    .getWorkExperienceTotal();
        Integer workExperienceCurrent = scoringRequest.getEmployment()
                                                      .getWorkExperienceCurrent();
        if (workExperienceTotal < 12 || workExperienceCurrent < 3) {
            String exceptionMessage = "Credit denial: insufficient experience";
            throw new ValidationException(exceptionMessage);
        }
    }

    private BigDecimal getEmploymentStatusRate(ScoringDataDTO scoringRequest) {
        BigDecimal zero = BigDecimal.ZERO;
        EmploymentStatus employmentStatus = scoringRequest.getEmployment()
                                                          .getEmploymentStatus();
        switch (employmentStatus) {
            case UNEMPLOYED -> throw new ValidationException("Credit denial: employment status - unemployed");
            case SELF_EMPLOYED -> {
                return zero.add(BigDecimal.ONE);
            }
            case BUSINESS_OWNER -> {
                return zero.add(BigDecimal.valueOf(3));
            }case EMPLOYED -> {
                return zero;
            }
            default -> throw new BadRequestException("Unknown employment status");
        }
    }

    private BigDecimal getEmployeePositionRate(ScoringDataDTO scoringRequest) {
        BigDecimal zero = BigDecimal.ZERO;
        Position position = scoringRequest.getEmployment()
                                          .getPosition();
        switch (position) {
            case MIDDLE_MANAGER -> {
                return zero.subtract(BigDecimal.valueOf(2));
            }
            case TOP_MANAGER -> {
                return zero.subtract(BigDecimal.valueOf(4));
            }
            default -> throw new BadRequestException("Unknown employee position");
        }
    }

    private BigDecimal getMaritalStatusRate(ScoringDataDTO scoringRequest) {
        BigDecimal zero = BigDecimal.ZERO;
        MaritalStatus maritalStatus = scoringRequest.getMaritalStatus();
        switch (maritalStatus) {
            case MARRIED -> {
                return zero.subtract(BigDecimal.valueOf(3));
            }
            case DIVORCED -> {
                return zero.add(BigDecimal.valueOf(1));
            }
            default -> throw new BadRequestException("Unknown marital status");
        }
    }

    private BigDecimal getDependentAmountRate(ScoringDataDTO scoringRequest) {
        BigDecimal zero = BigDecimal.ZERO;
        Integer dependentAmount = scoringRequest.getDependentAmount();
        return dependentAmount > 1 ? zero.add(BigDecimal.ONE) : zero;
    }

    private BigDecimal getGenderRate(ScoringDataDTO scoringRequest) {
        BigDecimal zero = BigDecimal.ZERO;
        Gender gender = scoringRequest.getGender();
        int age = Period.between(scoringRequest.getBirthDate(), LocalDate.now())
                        .getYears();
        switch (gender) {
            case FEMALE -> {
                if (age > 35 && age < 60) {
                    return zero.subtract(BigDecimal.valueOf(3));
                } else {
                    return zero;
                }
            }
            case MALE -> {
                if (age > 30 && age < 55) {
                    return zero.subtract(BigDecimal.valueOf(3));
                } else {
                    return zero;
                }
            }
            case NON_BINARY -> {
                return zero.add(BigDecimal.valueOf(3));
            }
            default -> throw new BadRequestException("Unknown gender");
        }
    }
}
