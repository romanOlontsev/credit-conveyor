package ru.neoflex.neostudy.conveyor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.conveyor.model.dto.*;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConveyorService {
    private final ScoringService scoringService;

    public List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO prescoringRequest) {
        return List.of(createOffer(false, false, prescoringRequest),
                createOffer(false, true, prescoringRequest),
                createOffer(true, false, prescoringRequest),
                createOffer(true, true, prescoringRequest)
        );
    }

    private LoanOfferDTO createOffer(Boolean isInsuranceEnabled,
                                     Boolean isSalaryClient,
                                     LoanApplicationRequestDTO prescoringRequest) {

        BigDecimal rate = scoringService.calculatePreRate(isInsuranceEnabled, isSalaryClient);
        Integer term = prescoringRequest.getTerm();
        BigDecimal amount = prescoringRequest.getAmount();

        BigDecimal monthlyPayment = scoringService.calculateMonthlyPayment(rate, term, amount);
        BigDecimal totalAmount = scoringService.evaluateTotalAmount(term, monthlyPayment);

        return LoanOfferDTO.builder()
                           .requestedAmount(amount)
                           .totalAmount(totalAmount)
                           .term(term)
                           .monthlyPayment(monthlyPayment)
                           .rate(rate)
                           .isInsuranceEnabled(isInsuranceEnabled)
                           .isSalaryClient(isSalaryClient)
                           .build();
    }

    public CreditDTO calculateCredit(ScoringDataDTO scoringRequest) {
        Integer term = scoringRequest.getTerm();
        BigDecimal amount = scoringRequest.getAmount();
        Boolean isInsuranceEnabled = scoringRequest.getIsInsuranceEnabled();

        BigDecimal rate = scoringService.calculateCreditRate(scoringRequest);
        BigDecimal monthlyPayment = scoringService.calculateMonthlyPayment(rate, term, amount);
        BigDecimal totalAmount =
                scoringService.evaluateTotalAmount(term, monthlyPayment);

        List<PaymentScheduleElement> paymentScheduleElements =
                scoringService.createMonthlyPaymentSchedule(monthlyPayment, rate, amount, term);

        return CreditDTO.builder()
                        .amount(amount)
                        .term(term)
                        .monthlyPayment(monthlyPayment)
                        .rate(rate)
                        .psk(totalAmount)
                        .isInsuranceEnabled(isInsuranceEnabled)
                        .isSalaryClient(scoringRequest.getIsSalaryClient())
                        .paymentSchedule(paymentScheduleElements)
                        .build();
    }
}
