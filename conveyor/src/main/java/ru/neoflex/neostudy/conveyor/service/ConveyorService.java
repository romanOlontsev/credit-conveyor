package ru.neoflex.neostudy.conveyor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.conveyor.model.dto.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConveyorService {
    private final ScoringService scoringService;

    public List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO prescoringRequest) {
        log.info("Prescoring input: {}", prescoringRequest);
        return List.of(createOffer(1L, false, false, prescoringRequest),
                createOffer(2L, false, true, prescoringRequest),
                createOffer(3L, true, false, prescoringRequest),
                createOffer(4L, true, true, prescoringRequest)
        );
    }

    public CreditDTO calculateCredit(ScoringDataDTO scoringRequest) {
        log.info("Scoring input: {}", scoringRequest);
        Integer term = scoringRequest.getTerm();
        BigDecimal amount = scoringRequest.getAmount();
        Boolean isInsuranceEnabled = scoringRequest.getIsInsuranceEnabled();

        BigDecimal rate = scoringService.calculateCreditRate(scoringRequest);
        BigDecimal monthlyPayment = scoringService.calculateMonthlyPayment(rate, term, amount);
        BigDecimal totalAmount =
                scoringService.evaluateTotalAmount(term, monthlyPayment);

        List<PaymentScheduleElement> paymentScheduleElements =
                scoringService.createMonthlyPaymentSchedule(monthlyPayment, rate, amount, term);

        CreditDTO credit = CreditDTO.builder()
                .amount(amount)
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(totalAmount)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(scoringRequest.getIsSalaryClient())
                .paymentSchedule(paymentScheduleElements)
                .build();
        log.info("Credit:{}", credit);
        return credit;
    }

    private LoanOfferDTO createOffer(Long offerId,
                                     Boolean isInsuranceEnabled,
                                     Boolean isSalaryClient,
                                     LoanApplicationRequestDTO prescoringRequest) {

        BigDecimal rate = scoringService.calculatePreRate(isInsuranceEnabled, isSalaryClient);
        Integer term = prescoringRequest.getTerm();
        BigDecimal amount = prescoringRequest.getAmount();

        BigDecimal monthlyPayment = scoringService.calculateMonthlyPayment(rate, term, amount);
        BigDecimal totalAmount = scoringService.evaluateTotalAmount(term, monthlyPayment);
        LoanOfferDTO offer = LoanOfferDTO.builder()
                .applicationId(offerId)
                .requestedAmount(amount)
                .totalAmount(totalAmount)
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
        log.info("Offer:{}", offer);
        return offer;
    }
}
