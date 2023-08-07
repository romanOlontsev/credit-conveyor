package ru.neoflex.neosudy.deal.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neoflex.neosudy.deal.model.jsonb.Employment;
import ru.neoflex.neosudy.deal.model.jsonb.Passport;
import ru.neoflex.neosudy.deal.model.types.Gender;
import ru.neoflex.neosudy.deal.model.types.MaritalStatus;

import java.time.LocalDate;

@Entity
@Table(name = "client", schema = "credit_app")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "birth_date")
    private LocalDate birth_date;
    @Column(name = "email")
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;
    @Column(name = "dependent_amount")
    private Integer dependentAmount;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "passport")
    private Passport passport;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "employment")
    private Employment employment;
    @Column(name = "account")
    private String account;
}
