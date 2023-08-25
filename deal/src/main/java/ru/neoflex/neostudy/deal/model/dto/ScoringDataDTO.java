package ru.neoflex.neostudy.deal.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.neoflex.neostudy.deal.model.types.Gender;
import ru.neoflex.neostudy.deal.model.types.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ScoringDataDTO {
    @JsonProperty(value = "amount")
    private BigDecimal amount;
    @JsonProperty(value = "term")
    private Integer term;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;
    @JsonProperty(value = "middle_name")
    private String middleName;
    @JsonProperty(value = "gender")
    private Gender gender;
    @JsonProperty(value = "birthdate")
    private LocalDate birthDate;
    @JsonProperty(value = "passport_series")
    private String passportSeries;
    @JsonProperty(value = "passport_number")
    private String passportNumber;
    @JsonProperty(value = "passport_issue_date")
    private LocalDate passportIssueDate;
    @JsonProperty(value = "passport_issue_branch")
    private String passportIssueBranch;
    @JsonProperty(value = "marital_status")
    private MaritalStatus maritalStatus;
    @JsonProperty(value = "dependent_amount")
    private Integer dependentAmount;
    @JsonProperty(value = "employment")
    private EmploymentDto employment;
    @JsonProperty(value = "account")
    private String account;
    @JsonProperty(value = "is_insurance_enabled")
    private Boolean isInsuranceEnabled;
    @JsonProperty(value = "is_salary_client")
    private Boolean isSalaryClient;
}
