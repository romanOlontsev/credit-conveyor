package ru.neoflex.neostudy.deal.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@DynamicInsert
@Table(name = "passport", schema = "credit_app")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passport_id")
    private Long passportId;

    @Column(name = "series")
    private String series;

    @Column(name = "number")
    private String number;

    @Column(name = "issue_branch")
    private String issueBranch;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passport passport = (Passport) o;
        return Objects.equals(series, passport.series) && Objects.equals(number, passport.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(series, number);
    }
}
