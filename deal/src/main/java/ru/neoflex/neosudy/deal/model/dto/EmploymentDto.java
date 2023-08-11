package ru.neoflex.neosudy.deal.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import ru.neoflex.neosudy.deal.model.types.EmploymentStatus;
import ru.neoflex.neosudy.deal.model.types.Position;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmploymentDto {
//    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "status")
    private EmploymentStatus status;
    @JsonProperty(value = "employer_inn")
    private String employerInn;
    @JsonProperty(value = "salary")
    private BigDecimal salary;
    //    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "position")
    private Position position;
    @JsonProperty(value = "work_experience_total")
    private Integer workExperienceTotal;
    @JsonProperty(value = "work_experience_current")
    private Integer workExperienceCurrent;
}