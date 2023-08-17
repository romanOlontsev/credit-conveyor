package ru.neoflex.neostudy.conveyor.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.conveyor.config.AppConfig;
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
@RequiredArgsConstructor
public class ScoringRateService {
    private final AppConfig config;

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
        BigDecimal salariesCount = BigDecimal.valueOf(config.salariesLessLoanAmount());
        boolean isAmountMoreThanTwentySalaries = BigDecimalRoughComparison.check(
                amount,
                BigDecimalRoughComparison.Operator.GREATER_THAN,
                salary.multiply(salariesCount));
        if (isAmountMoreThanTwentySalaries) {
            String exceptionMessage = "Credit denial: the credit amount is more than " + salariesCount + " salaries";
            throw new ValidationException(exceptionMessage);
        }
    }

    private void checkAge(ScoringDataDTO scoringRequest) {
        int age = Period.between(scoringRequest.getBirthdate(), LocalDate.now())
                        .getYears();
        int maxAge = config.creditMaxAge();
        int minAge = config.creditMinAge();
        if (age < minAge || age > maxAge) {
            String exceptionMessage = "Credit denial: credit is issued from " + minAge + " to " + maxAge + " years";
            throw new ValidationException(exceptionMessage);
        }
    }

    private void checkExperience(ScoringDataDTO scoringRequest) {
        Integer workExperienceTotal = scoringRequest.getEmployment()
                                                    .getWorkExperienceTotal();
        Integer workExperienceCurrent = scoringRequest.getEmployment()
                                                      .getWorkExperienceCurrent();
        if (workExperienceTotal < config.minWorkExperienceTotal() ||
                workExperienceCurrent < config.minWorkExperienceCurrent()) {
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
                BigDecimal selfEmployedRate = BigDecimal.valueOf(config.rate()
                                                                       .selfEmployedIncrease());
                return zero.add(selfEmployedRate);
            }
            case BUSINESS_OWNER -> {
                BigDecimal businessOwnerRate = BigDecimal.valueOf(config.rate()
                                                                        .businessOwnerIncrease());
                return zero.add(businessOwnerRate);
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
                BigDecimal middleManagerRate = BigDecimal.valueOf(config.rate()
                                                                        .middleManagerReduction());
                return zero.subtract(middleManagerRate);
            }
            case TOP_MANAGER -> {
                BigDecimal topManagerRate = BigDecimal.valueOf(config.rate()
                                                                     .topManagerReduction());
                return zero.subtract(topManagerRate);
            }
            default -> throw new BadRequestException("Unknown employee position");
        }
    }

    private BigDecimal getMaritalStatusRate(ScoringDataDTO scoringRequest) {
        BigDecimal zero = BigDecimal.ZERO;
        MaritalStatus maritalStatus = scoringRequest.getMaritalStatus();
        switch (maritalStatus) {
            case MARRIED -> {
                BigDecimal marriedRate = BigDecimal.valueOf(config.rate()
                                                                  .marriedReduction());
                return zero.subtract(marriedRate);
            }
            case DIVORCED -> {
                BigDecimal divorcedRate = BigDecimal.valueOf(config.rate()
                                                                   .divorcedIncrease());
                return zero.add(divorcedRate);
            }
            default -> throw new BadRequestException("Unknown marital status");
        }
    }

    private BigDecimal getDependentAmountRate(ScoringDataDTO scoringRequest) {
        BigDecimal zero = BigDecimal.ZERO;
        Integer dependentAmount = scoringRequest.getDependentAmount();
        BigDecimal dependentAmountRate = BigDecimal.valueOf(config.rate()
                                                                  .dependentAmountIncrease());
        return dependentAmount > 1 ? zero.add(dependentAmountRate) : zero;
    }

    private BigDecimal getGenderRate(ScoringDataDTO scoringRequest) {
        BigDecimal zero = BigDecimal.ZERO;
        Gender gender = scoringRequest.getGender();
        int age = Period.between(scoringRequest.getBirthdate(), LocalDate.now())
                        .getYears();
        switch (gender) {
            case FEMALE -> {
                int womanMinAge = config.womanMinAgeChangingRate();
                int womanMaxAge = config.womanMaxAgeChangingRate();
                if (age > womanMinAge && age < womanMaxAge) {
                    BigDecimal womanAgeRate = BigDecimal.valueOf(config.rate()
                                                                       .womanAgeReduction());
                    return zero.subtract(womanAgeRate);
                } else {
                    return zero;
                }
            }
            case MALE -> {
                int manMinAge = config.manMinAgeChangingRate();
                int manMaxAge = config.manMaxAgeChangingRate();
                if (age > manMinAge && age < manMaxAge) {
                    BigDecimal manAgeRate = BigDecimal.valueOf(config.rate()
                                                                     .manAgeReduction());
                    return zero.subtract(manAgeRate);
                } else {
                    return zero;
                }
            }
            case NON_BINARY -> {
                BigDecimal nonBinaryRate = BigDecimal.valueOf(config.rate()
                                                                    .nonBinaryAgeIncrease());
                return zero.add(nonBinaryRate);
            }
            default -> throw new BadRequestException("Unknown gender");
        }
    }
}
