package ru.neoflex.neosudy.deal.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.neoflex.neosudy.deal.mapper.CreditMapperImpl;
import ru.neoflex.neosudy.deal.model.dto.CreditDTO;
import ru.neoflex.neosudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neosudy.deal.model.dto.ScoringDataDTO;
import ru.neoflex.neosudy.deal.model.entity.Credit;
import ru.neoflex.neosudy.deal.model.types.CreditStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CreditServiceTest {

    private static CreditService service;

    @BeforeAll
    static void beforeAll() {
        service = new CreditService(new CreditMapperImpl());
    }

    @Test
    void getCreditFromLoanOfferDTO_shouldReturnCredit() {
        LoanOfferDTO request = LoanOfferDTO.builder()
                                           .requestedAmount(BigDecimal.ONE)
                                           .isInsuranceEnabled(false)
                                           .isSalaryClient(false)
                                           .term(6)
                                           .monthlyPayment(BigDecimal.TEN)
                                           .rate(BigDecimal.ONE)
                                           .build();
        Credit expectedCredit = Credit.builder()
                                      .amount(request.getRequestedAmount())
                                      .insuranceEnable(request.getIsInsuranceEnabled())
                                      .salaryClient(request.getIsSalaryClient())
                                      .term(request.getTerm())
                                      .monthlyPayment(request.getMonthlyPayment())
                                      .rate(request.getRate())
                                      .build();

        Credit response = service.getCreditFromLoanOfferDTO(request);

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isEqualTo(expectedCredit)
        );
    }

    @Test
    void updateScoringDataDTOFromCredit_shouldUpdateScoringData() {
        ScoringDataDTO scoringData = ScoringDataDTO.builder()
                                                   .build();
        Credit credit = Credit.builder()
                              .insuranceEnable(false)
                              .salaryClient(true)
                              .amount(BigDecimal.ONE)
                              .term(-1)
                              .build();

        service.updateScoringDataDTOFromCredit(scoringData, credit);

        assertAll(
                () -> assertThat(scoringData).extracting(ScoringDataDTO::getAmount)
                                             .isEqualTo(credit.getAmount()),
                () -> assertThat(scoringData).extracting(ScoringDataDTO::getTerm)
                                             .isEqualTo(credit.getTerm()),
                () -> assertThat(scoringData).extracting(ScoringDataDTO::getIsSalaryClient)
                                             .isEqualTo(credit.getSalaryClient()),
                () -> assertThat(scoringData).extracting(ScoringDataDTO::getIsInsuranceEnabled)
                                             .isEqualTo(credit.getInsuranceEnable())
        );
    }

    @Test
    void updateCreditFromCreditDto_shouldUpdateCredit() {
        CreditDTO creditDTO = CreditDTO.builder()
                                       .amount(BigDecimal.ONE)
                                       .term(-1)
                                       .monthlyPayment(BigDecimal.TEN)
                                       .rate(BigDecimal.ONE)
                                       .psk(BigDecimal.ZERO)
                                       .paymentSchedule(List.of())
                                       .build();
        Credit expectedCredit = Credit.builder()
                                      .amount(creditDTO.getAmount())
                                      .term(creditDTO.getTerm())
                                      .monthlyPayment(creditDTO.getMonthlyPayment())
                                      .rate(creditDTO.getRate())
                                      .psk(creditDTO.getPsk())
                                      .paymentSchedule(List.of())
                                      .creditStatus(CreditStatus.CALCULATED)
                                      .build();
        Credit credit = Credit.builder()
                              .creditStatus(CreditStatus.CALCULATED)
                              .build();

        service.updateCreditFromCreditDto(credit, creditDTO);

        assertAll(
                () -> assertThat(credit).isNotNull()
                                        .isEqualTo(expectedCredit)
        );
    }
}