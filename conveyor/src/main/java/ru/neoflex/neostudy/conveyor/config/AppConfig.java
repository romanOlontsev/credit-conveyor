package ru.neoflex.neostudy.conveyor.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.conveyor", ignoreUnknownFields = false)
public record AppConfig(@NotNull double baseRate,
                        @NotNull int creditMinAge,
                        @NotNull int creditMaxAge,
                        @NotNull int minWorkExperienceTotal,
                        @NotNull int minWorkExperienceCurrent,
                        @NotNull int salariesLessLoanAmount,
                        @NotNull int womanMinAgeChangingRate,
                        @NotNull int womanMaxAgeChangingRate,
                        @NotNull int manMinAgeChangingRate,
                        @NotNull int manMaxAgeChangingRate,
                        @NotNull Rate rate) {
    public record Rate(@NotNull double isInsuranceEnabledReduction,
                       @NotNull double isSalaryClientReduction,
                       @NotNull double selfEmployedIncrease,
                       @NotNull double businessOwnerIncrease,
                       @NotNull double middleManagerReduction,
                       @NotNull double topManagerReduction,
                       @NotNull double marriedReduction,
                       @NotNull double divorcedIncrease,
                       @NotNull double dependentAmountIncrease,
                       @NotNull double womanAgeReduction,
                       @NotNull double manAgeReduction,
                       @NotNull double nonBinaryAgeIncrease) {
    }

}
