package ru.neoflex.neosudy.deal.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import ru.neoflex.neosudy.deal.model.jsonb.AppliedOffer;
import ru.neoflex.neosudy.deal.model.jsonb.StatusHistory;
import ru.neoflex.neosudy.deal.model.types.ApplicationStatus;

import java.time.OffsetDateTime;

@Entity
@Table(name = "application", schema = "credit_app")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client clientId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id")
    private Credit creditId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus status;
    @Column(name = "creation_date")
    private OffsetDateTime creationDate;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applied_offer")
    private AppliedOffer appliedOffer;
    @Column(name = "sign_date")
    private OffsetDateTime signDate;
    @UuidGenerator
    @Column(name = "ses_code", columnDefinition = "VARCHAR")
    private String sesCode;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "status_history")
    private StatusHistory statusHistory;
}
