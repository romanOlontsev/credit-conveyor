package ru.neoflex.neostudy.deal.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neoflex.neostudy.deal.model.jsonb.PaymentScheduleElement;
import ru.neoflex.neostudy.deal.model.types.CreditStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credit credit = (Credit) o;
        return Objects.equals(amount, credit.amount) && Objects.equals(term, credit.term) && Objects.equals(monthlyPayment, credit.monthlyPayment) && Objects.equals(rate, credit.rate) && Objects.equals(psk, credit.psk) && Objects.equals(paymentSchedule, credit.paymentSchedule) && Objects.equals(insuranceEnable, credit.insuranceEnable) && Objects.equals(salaryClient, credit.salaryClient) && creditStatus == credit.creditStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, term, monthlyPayment, rate, psk, paymentSchedule, insuranceEnable, salaryClient, creditStatus);
    }
}
