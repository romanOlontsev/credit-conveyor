package ru.neoflex.neostudy.conveyor.model.dto;

import lombok.Data;
import ru.neoflex.neostudy.conveyor.model.types.Gender;
import ru.neoflex.neostudy.conveyor.model.types.MaritalStatus;

import java.time.LocalDate;

@Data
public class FinishRegistrationRequestDTO {
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private EmploymentDTO employment;
    private String account;
}
