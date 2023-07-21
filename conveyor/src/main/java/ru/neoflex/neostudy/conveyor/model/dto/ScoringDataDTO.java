package ru.neoflex.neostudy.conveyor.model.dto;

import lombok.Data;
import ru.neoflex.neostudy.conveyor.model.types.Gender;
import ru.neoflex.neostudy.conveyor.model.types.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ScoringDataDTO {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private EmploymentDTO employment;
    private String account;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
