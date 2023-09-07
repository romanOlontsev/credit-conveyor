package ru.neoflex.neostudy.conveyor.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private EmploymentStatus status;
    @JsonProperty(value = "employer_inn")
    @NotNull
    private String employerInn;
    @JsonProperty(value = "salary")
    @NotNull
    private BigDecimal salary;
    @JsonProperty(value = "position")
    @NotNull
    private Position position;
    @JsonProperty(value = "work_experience_total")
    @NotNull
    private Integer workExperienceTotal;
    @JsonProperty(value = "work_experience_current")
    @NotNull
    private Integer workExperienceCurrent;
}
