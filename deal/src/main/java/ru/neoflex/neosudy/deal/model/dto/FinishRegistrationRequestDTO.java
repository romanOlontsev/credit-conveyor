package ru.neoflex.neosudy.deal.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.neoflex.neosudy.deal.model.entity.Employment;
import ru.neoflex.neosudy.deal.model.types.Gender;
import ru.neoflex.neosudy.deal.model.types.MaritalStatus;

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
    private EmploymentDto employment;
    @JsonProperty(value = "account")
    private String account;

}