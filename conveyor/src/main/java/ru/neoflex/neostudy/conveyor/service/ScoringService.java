package ru.neoflex.neostudy.conveyor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.conveyor.config.AppConfig;
import ru.neoflex.neostudy.conveyor.model.dto.PaymentScheduleElement;
import ru.neoflex.neostudy.conveyor.model.dto.ScoringDataDTO;
import ru.neoflex.neostudy.conveyor.utils.BigDecimalRoughComparison;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringService {
    private final AppConfig config;
    private final ScoringRateService scoringRateService;
    private final static int CALCULATED_SCALE = 8;
    private final static int PRINTED_SCALE = 2;

    public BigDecimal evaluateTotalAmount(Integer term, BigDecimal monthlyPayment) {
        return monthlyPayment.multiply(BigDecimal.valueOf(term))
                             .setScale(PRINTED_SCALE, RoundingMode.HALF_UP);

    }

    public BigDecimal calculatePreRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        double currentRate = config.baseRate();
        if (isInsuranceEnabled) {
            currentRate -= config.rate()
                                 .isInsuranceEnabledReduction();
        }
        if (isSalaryClient) {
            currentRate -= config.rate()
                                 .isSalaryClientReduction();
        }
        return BigDecimal.valueOf(currentRate);
    }

    public BigDecimal calculateCreditRate(ScoringDataDTO scoringRequest) {
        BigDecimal preRate = calculatePreRate(scoringRequest.getIsInsuranceEnabled(),
                scoringRequest.getIsSalaryClient());
        BigDecimal scoringRate = scoringRateService.calculateScoringRate(scoringRequest);
        return preRate.add(scoringRate);
    }

    public BigDecimal calculateMonthlyPayment(BigDecimal rate, Integer term, BigDecimal amount) {
        BigDecimal numberMonthsInYear = BigDecimal.valueOf(12);
        BigDecimal oneHundredPercent = BigDecimal.valueOf(100);
        BigDecimal monthlyRate = rate.divide(numberMonthsInYear, CALCULATED_SCALE, RoundingMode.HALF_UP)
                                     .divide(oneHundredPercent, CALCULATED_SCALE, RoundingMode.HALF_UP);
        BigDecimal annuityRatio = monthlyRate
                .multiply(((monthlyRate.add(BigDecimal.ONE)).pow(term))
                        .divide((monthlyRate.add(BigDecimal.ONE)).pow(term)
                                                                 .subtract(BigDecimal.ONE), CALCULATED_SCALE,
                                RoundingMode.HALF_UP));
        return annuityRatio.multiply(amount)
                           .setScale(PRINTED_SCALE, RoundingMode.HALF_UP);
    }

    public List<PaymentScheduleElement> createMonthlyPaymentSchedule(BigDecimal monthlyPayment,
                                                                     BigDecimal rate,
                                                                     BigDecimal totalAmount,
                                                                     Integer term) {
        List<PaymentScheduleElement> elements = new ArrayList<>();
        LocalDate creditDate = LocalDate.now();
        BigDecimal remainingDebt = totalAmount;
        for (int i = 1; i <= term; i++) {
            LocalDate currentPaymentDate = creditDate.plusMonths(i);
            BigDecimal interestPayment = calculateInterestPayment(rate, currentPaymentDate, remainingDebt);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

            boolean remainLessDebt = BigDecimalRoughComparison.check(remainingDebt,
                    BigDecimalRoughComparison.Operator.LESS_THAN,
                    debtPayment);
            if (remainLessDebt) {
                debtPayment = debtPayment.add(remainingDebt);
                monthlyPayment = monthlyPayment.add(remainingDebt);
                remainingDebt = BigDecimal.ZERO;
            }

            PaymentScheduleElement element = PaymentScheduleElement.builder()
                                                                   .number(i)
                                                                   .date(currentPaymentDate)
                                                                   .totalPayment(monthlyPayment)
                                                                   .debtPayment(debtPayment)
                                                                   .interestPayment(interestPayment)
                                                                   .remainingDebt(remainingDebt)
                                                                   .build();
            elements.add(element);
        }
        return elements;
    }

    private BigDecimal calculateInterestPayment(BigDecimal rate, LocalDate currentPaymentDate, BigDecimal remainingDebt) {
        int daysPerYear = currentPaymentDate.lengthOfYear();
        int daysPerMonth = currentPaymentDate.lengthOfMonth();
        BigDecimal oneHundredPercent = BigDecimal.valueOf(100);

        return remainingDebt.multiply(rate)
                            .multiply(BigDecimal.valueOf(daysPerMonth))
                            .divide(BigDecimal.valueOf(daysPerYear), CALCULATED_SCALE, RoundingMode.HALF_UP)
                            .divide(oneHundredPercent, PRINTED_SCALE, RoundingMode.HALF_UP);
    }
}
