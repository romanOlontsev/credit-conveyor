package ru.neoflex.neostudy.dossier.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class CreditDTO {
    @JsonProperty(value = "amount")
    private BigDecimal amount;
    @JsonProperty(value = "term")
    private Integer term;
    @JsonProperty(value = "monthly_payment")
    private BigDecimal monthlyPayment;
    @JsonProperty(value = "rate")
    private BigDecimal rate;
    @JsonProperty(value = "psk")
    private BigDecimal psk;
    @JsonProperty(value = "is_insurance_enabled")
    private Boolean isInsuranceEnabled;
    @JsonProperty(value = "is_salary_client")
    private Boolean isSalaryClient;
    @JsonProperty(value = "payment_schedule")
    private List<PaymentScheduleElement> paymentSchedule;
}
