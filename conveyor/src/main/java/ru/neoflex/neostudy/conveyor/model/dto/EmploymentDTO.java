package ru.neoflex.neostudy.conveyor.model.dto;

import lombok.Data;
import ru.neoflex.neostudy.conveyor.model.types.EmploymentStatus;
import ru.neoflex.neostudy.conveyor.model.types.Position;

import java.math.BigDecimal;

@Data
public class EmploymentDTO {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
