package ru.neoflex.neostudy.gateway.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.neoflex.neostudy.gateway.model.types.Gender;
import ru.neoflex.neostudy.gateway.model.types.MaritalStatus;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FinishRegistrationRequestDTO {
    @JsonProperty(value = "gender")
    private Gender gender;
    @JsonProperty(value = "marital_status")
    private MaritalStatus maritalStatus;
    @JsonProperty(value = "dependent_amount")
    private Integer dependentAmount;
    @JsonProperty(value = "passport_issue_date")
    private LocalDate passportIssueDate;
    @JsonProperty(value = "passport_issue_branch")
    private String passportIssueBranch;
    @JsonProperty(value = "employment")
    private EmploymentDTO employment;
    @JsonProperty(value = "account")
    private String account;

}