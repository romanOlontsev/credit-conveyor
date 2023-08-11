package ru.neoflex.neostudy.conveyor.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(value = "amount")
    private BigDecimal amount;
    @NotNull
    @DecimalMin(value = "6", message = "The term must be greater than or equal to 6")
    @JsonProperty(value = "term")
    private Integer term;
    @UserNameConstraint(message = "The firstname must contain from 2 to 30 latin characters")
    @JsonProperty(value = "first_name")
    private String firstName;
    @UserNameConstraint(message = "The lastname must contain from 2 to 30 latin characters")
    @JsonProperty(value = "last_name")
    private String lastName;
    @UserNameConstraint(required = false,
            message = "The middle name must be empty or contain from 2 to 30 Latin characters")
    @JsonProperty(value = "middle_name")
    private String middleName;
    @NotNull
    @JsonProperty(value = "gender")
    private Gender gender;
    @UserAgeConstraint(message = "User must be over 18 years of age")
    @JsonProperty(value = "birthdate")
    private LocalDate birthdate;
    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must contain 4 numbers")
    @JsonProperty(value = "passport_series")
    private String passportSeries;
    @NotNull
    @Pattern(regexp = "^\\d{6}$", message = "Passport number must contain 6 numbers")
    @JsonProperty(value = "passport_number")
    private String passportNumber;
    @NotNull
    @JsonProperty(value = "passport_issue_date")
    private LocalDate passportIssueDate;
    @NotNull
    @JsonProperty(value = "passport_issue_branch")
    private String passportIssueBranch;
    @NotNull
    @JsonProperty(value = "marital_status")
    private MaritalStatus maritalStatus;
    @NotNull
    @JsonProperty(value = "dependent_amount")
    private Integer dependentAmount;
    @NotNull
    @JsonProperty(value = "employment")
    private EmploymentDTO employment;
    @NotNull
    @JsonProperty(value = "account")
    private String account;
    @NotNull
    @JsonProperty(value = "is_insurance_enabled")
    private Boolean isInsuranceEnabled;
    @NotNull
    @JsonProperty(value = "is_salary_client")
    private Boolean isSalaryClient;
}
