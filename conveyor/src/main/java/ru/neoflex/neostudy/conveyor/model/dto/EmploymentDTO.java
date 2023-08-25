package ru.neoflex.neostudy.conveyor.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(value = "status")
    private EmploymentStatus status;
    @JsonProperty(value = "employer_inn")
    private String employerInn;
    @JsonProperty(value = "salary")
    private BigDecimal salary;
    @JsonProperty(value = "position")
    private Position position;
    @JsonProperty(value = "work_experience_total")
    private Integer workExperienceTotal;
    @JsonProperty(value = "work_experience_current")
    private Integer workExperienceCurrent;
}
