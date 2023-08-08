package ru.neoflex.neosudy.deal.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import ru.neoflex.neosudy.deal.model.types.EmploymentStatus;
import ru.neoflex.neosudy.deal.model.types.Position;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "employment", schema = "credit_app")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "employment_id")
    private Long employmentId;
    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "status")
    private EmploymentStatus status;
    @JsonProperty(value = "employer_inn")
    private String employerInn;
    @JsonProperty(value = "salary")
    private BigDecimal salary;
    @Enumerated(EnumType.STRING)
    @JsonProperty(value = "position")
    private Position position;
    @JsonProperty(value = "work_experience_total")
    private Integer workExperienceTotal;
    @JsonProperty(value = "work_experience_current")
    private Integer workExperienceCurrent;
}
