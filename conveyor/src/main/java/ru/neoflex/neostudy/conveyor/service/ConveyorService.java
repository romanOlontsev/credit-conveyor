package ru.neoflex.neostudy.conveyor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.conveyor.config.AppConfig;
import ru.neoflex.neostudy.conveyor.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.conveyor.model.dto.LoanOfferDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConveyorService {

    private final AppConfig config;

    public List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO prescoringRequest) {
//        List<LoanOfferDTO> blank = IntStream.range(1, 5)
//                                            .boxed()
//                                            .map(it -> LoanOfferDTO.builder()
//                                                                   .applicationId(it.longValue())
//                                                                   .requestedAmount(prescoringRequest.getAmount())
//                                                                   .term(prescoringRequest.getTerm())
//                                                                   .isInsuranceEnabled(false)
//                                                                   .isSalaryClient(false)
//                                                                   .build())
//                                            .toList();
        List<LoanOfferDTO> blank = new ArrayList<>();
        LoanOfferDTO noInsuranceNoSalaryOffer = LoanOfferDTO.builder()
                                                            .applicationId(1L)
                                                            .requestedAmount(prescoringRequest.getAmount())
                                                            .term(prescoringRequest.getTerm())
                                                            .rate(BigDecimal.valueOf(config.baseRate()))
                                                            .isInsuranceEnabled(false)
                                                            .isSalaryClient(false)
                                                            .build();
        BigDecimal rate = noInsuranceNoSalaryOffer.getRate();
        Integer term = noInsuranceNoSalaryOffer.getTerm();
        BigDecimal requestedAmount = noInsuranceNoSalaryOffer.getRequestedAmount();

        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(term), 6, RoundingMode.HALF_DOWN)
                                     .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_DOWN);
        BigDecimal annuityRatio = monthlyRate
                .multiply(((monthlyRate.add(BigDecimal.ONE)).pow(term))
                        .divide((monthlyRate.add(BigDecimal.ONE)).pow(term)
                                                                 .subtract(BigDecimal.ONE), 6, RoundingMode.HALF_DOWN));
        BigDecimal monthlyPayment = annuityRatio.multiply(requestedAmount)
                                            .setScale(2, RoundingMode.HALF_DOWN);
        noInsuranceNoSalaryOffer.setMonthlyPayment(monthlyPayment);
        blank.add(noInsuranceNoSalaryOffer);

        return blank;
    }
}
