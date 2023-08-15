package ru.neoflex.neosudy.deal.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class LoanOfferDTO {
    @JsonProperty(value = "application_id")
    private Long applicationId;
    @JsonProperty(value = "requested_amount")
    private BigDecimal requestedAmount;
    @JsonProperty(value = "total_amount")
    private BigDecimal totalAmount;
    @JsonProperty(value = "term")
    private Integer term;
    @JsonProperty(value = "monthly_payment")
    private BigDecimal monthlyPayment;
    @JsonProperty(value = "rate")
    private BigDecimal rate;
    @JsonProperty(value = "is_insurance_enabled")
    private Boolean isInsuranceEnabled;
    @JsonProperty(value = "is_salary_client")
    private Boolean isSalaryClient;
}
