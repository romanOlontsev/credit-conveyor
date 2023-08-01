package ru.neoflex.neostudy.conveyor.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ru.neoflex.neostudy.conveyor.annotation.UserAgeConstraint;
import ru.neoflex.neostudy.conveyor.annotation.UserNameConstraint;
import ru.neoflex.neostudy.conveyor.model.types.Gender;
import ru.neoflex.neostudy.conveyor.model.types.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ScoringDataDTO {
    @NotNull
    @DecimalMin(value = "10000", message = "The amount must be greater than or equal to 10000")
    private BigDecimal amount;
    @NotNull
    @DecimalMin(value = "6", message = "The term must be greater than or equal to 6")
    private Integer term;
    @UserNameConstraint(message = "The firstname must contain from 2 to 30 latin characters")
    private String firstName;
    @UserNameConstraint(message = "The lastname must contain from 2 to 30 latin characters")
    private String lastName;
    @UserNameConstraint(required = false,
            message = "The middle name must be empty or contain from 2 to 30 Latin characters")
    private String middleName;
    @NotNull
    private Gender gender;
    @UserAgeConstraint(message = "User must be over 18 years of age")
    private LocalDate birthdate;
    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must contain 4 numbers")
    private String passportSeries;
    @NotNull
    @Pattern(regexp = "^\\d{6}$", message = "Passport number must contain 6 numbers")
    private String passportNumber;
    @NotNull
    private LocalDate passportIssueDate;
    @NotNull
    private String passportIssueBranch;
    @NotNull
    private MaritalStatus maritalStatus;
    @NotNull
    private Integer dependentAmount;
    @NotNull
    private EmploymentDTO employment;
    @NotNull
    private String account;
    @NotNull
    private Boolean isInsuranceEnabled;
    @NotNull
    private Boolean isSalaryClient;
}
