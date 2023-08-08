package ru.neoflex.neostudy.conveyor.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class PaymentScheduleElement {
    @JsonProperty(value = "number")
    private Integer number;
    @JsonProperty(value = "date")
    private LocalDate date;
    @JsonProperty(value = "total_payment")
    private BigDecimal totalPayment;
    @JsonProperty(value = "debt_payment")
    private BigDecimal debtPayment;
    @JsonProperty(value = "interest_payment")
    private BigDecimal interestPayment;
    @JsonProperty(value = "remaining_debt")
    private BigDecimal remainingDebt;
}
