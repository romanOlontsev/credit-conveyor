package ru.neoflex.neostudy.conveyor.model.dto;

import lombok.*;
import ru.neoflex.neostudy.conveyor.model.types.EmploymentStatus;
import ru.neoflex.neostudy.conveyor.model.types.Position;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EmploymentDTO {
    private EmploymentStatus employmentStatus;
    private String employerInn;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
