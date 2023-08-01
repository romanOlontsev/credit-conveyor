package ru.neoflex.neostudy.conveyor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.neostudy.conveyor.config.AppConfig;
import ru.neoflex.neostudy.conveyor.model.dto.PaymentScheduleElement;
import ru.neoflex.neostudy.conveyor.model.dto.ScoringDataDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoringServiceTest {
    @InjectMocks
    private ScoringService service;
    @Mock
    private AppConfig config;
    @Mock
    private ScoringRateService scoringRateService;

    @Test
    void evaluateTotalAmount_shouldReturnBigDecimal() {
        BigDecimal monthlyPayment = BigDecimal.valueOf(1000);
        int term = 12;

        BigDecimal totalAmount = service.evaluateTotalAmount(term, monthlyPayment);

        BigDecimal expected = BigDecimal.valueOf(12000)
                                        .setScale(2, RoundingMode.HALF_UP);
        assertThat(totalAmount).isNotNull()
                               .isEqualTo(expected);
    }

    @Test
    void calculatePreRate_shouldReturnBigDecimal_isInsuranceEnabledTrue_isSalaryClientFalse() {
        when(config.baseRate()).thenReturn(10.0);
        BigDecimal preRate = service.calculatePreRate(true, false);

        BigDecimal expected = BigDecimal.valueOf(7.0);
        assertThat(preRate).isNotNull()
                           .isEqualTo(expected);
    }

    @Test
    void calculatePreRate_shouldReturnBigDecimal_isInsuranceEnabledFalse_isSalaryClientTrue() {
        when(config.baseRate()).thenReturn(10.0);
        BigDecimal preRate = service.calculatePreRate(false, true);

        BigDecimal expected = BigDecimal.valueOf(9.0);
        assertThat(preRate).isNotNull()
                           .isEqualTo(expected);
    }


    @Test
    void calculateCreditRate_shouldReturnBigDecimal() {
        ScoringDataDTO dataDTO = ScoringDataDTO.builder()
                                               .isInsuranceEnabled(true)
                                               .isSalaryClient(true)
                                               .build();

        BigDecimal scoringRate = BigDecimal.ONE;
        when(config.baseRate()).thenReturn(10.0);
        when(scoringRateService.calculateScoringRate(any())).thenReturn(scoringRate);
        BigDecimal creditRate = service.calculateCreditRate(dataDTO);

        assertThat(creditRate).isNotNull()
                              .isEqualTo(BigDecimal.valueOf(7.0));

    }

    @Test
    void calculateMonthlyPayment_shouldReturnBigDecimal() {
        BigDecimal amount = BigDecimal.valueOf(100000);
        BigDecimal rate = BigDecimal.valueOf(25);
        int term = 12;

        BigDecimal monthlyPayment = service.calculateMonthlyPayment(rate, term, amount);

        BigDecimal expected = BigDecimal.valueOf(9504.42);
        assertThat(monthlyPayment).isNotNull()
                                  .isEqualTo(expected);
    }

    @Test
    void createMonthlyPaymentSchedule_shouldReturnList() {
        BigDecimal monthlyPayment = BigDecimal.valueOf(9504.42);
        BigDecimal rate = BigDecimal.valueOf(25);
        BigDecimal totalAmount = BigDecimal.valueOf(100000);
        int term = 12;

        List<PaymentScheduleElement> monthlyPaymentSchedule =
                service.createMonthlyPaymentSchedule(monthlyPayment, rate, totalAmount, term);

        int expectedNumber = 3;
        LocalDate expectedDate = LocalDate.now()
                                          .plusMonths(8);
        BigDecimal expectedRemainingDebt = BigDecimal.valueOf(0);
        BigDecimal expectedDebtPayment = BigDecimal.valueOf(8943.86);
        BigDecimal expectedInterestPayment = BigDecimal.valueOf(389.86);

        assertAll(
                () -> assertThat(monthlyPaymentSchedule).isNotNull()
                                                        .isNotEmpty()
                                                        .hasSize(12),
                () -> assertThat(monthlyPaymentSchedule.get(2)).extracting(PaymentScheduleElement::getNumber)
                                                               .isEqualTo(expectedNumber),
                () -> assertThat(monthlyPaymentSchedule.get(5)).extracting(PaymentScheduleElement::getTotalPayment)
                                                               .isEqualTo(monthlyPayment),
                () -> assertThat(monthlyPaymentSchedule.get(7)).extracting(PaymentScheduleElement::getDate)
                                                               .isEqualTo(expectedDate),
                () -> assertThat(monthlyPaymentSchedule.get(9)).extracting(PaymentScheduleElement::getDebtPayment)
                                                               .isEqualTo(expectedDebtPayment),
                () -> assertThat(monthlyPaymentSchedule.get(10)).extracting(PaymentScheduleElement::getInterestPayment)
                                                                .isEqualTo(expectedInterestPayment),
                () -> assertThat(monthlyPaymentSchedule.get(11)).extracting(PaymentScheduleElement::getRemainingDebt)
                                                                .isEqualTo(expectedRemainingDebt)
        );
    }
}