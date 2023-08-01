package ru.neoflex.neostudy.conveyor.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class PaymentScheduleElement {
    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;
    private BigDecimal debtPayment;
    private BigDecimal interestPayment;
    private BigDecimal remainingDebt;
}
