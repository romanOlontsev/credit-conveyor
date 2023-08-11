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
    @Column(name = "employment_id")
    private Long employmentId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EmploymentStatus status;
    @Column(name = "employer_inn")
    private String employerInn;
    @Column(name = "salary")
    private BigDecimal salary;
    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;
    @Column(name = "work_experience_total")
    private Integer workExperienceTotal;
    @Column(name = "work_experience_current")
    private Integer workExperienceCurrent;
}
