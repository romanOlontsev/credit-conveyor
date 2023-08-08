package ru.neoflex.neosudy.deal.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neoflex.neosudy.deal.model.jsonb.PaymentScheduleElement;
import ru.neoflex.neosudy.deal.model.types.CreditStatus;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "credit", schema = "credit_app")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_id")
    private Long creditId;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "term")
    private Integer term;
    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "psk")
    private BigDecimal psk;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payment_schedule")
    private List<PaymentScheduleElement> paymentSchedule;
    @Column(name = "insurance_enable")
    private Boolean insuranceEnable;
    @Column(name = "salary_client")
    private Boolean salaryClient;
    @Enumerated(EnumType.STRING)
    @Column(name = "credit_status")
    private CreditStatus creditStatus;
}
