package ru.neoflex.neosudy.deal.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.neoflex.neosudy.deal.model.types.EmploymentStatus;
import ru.neoflex.neosudy.deal.model.types.Position;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employment that = (Employment) o;
        return status == that.status && Objects.equals(employerInn, that.employerInn) && Objects.equals(salary, that.salary) && position == that.position && Objects.equals(workExperienceTotal, that.workExperienceTotal) && Objects.equals(workExperienceCurrent, that.workExperienceCurrent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, employerInn, salary, position, workExperienceTotal, workExperienceCurrent);
    }
}
