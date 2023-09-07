package ru.neoflex.neostudy.dossier.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.neoflex.neostudy.dossier.model.types.EmploymentStatus;
import ru.neoflex.neostudy.dossier.model.types.Position;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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