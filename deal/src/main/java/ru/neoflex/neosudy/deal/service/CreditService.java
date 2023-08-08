package ru.neoflex.neosudy.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.neosudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neosudy.deal.model.entity.Credit;
import ru.neoflex.neosudy.deal.repository.CreditRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditRepository creditRepository;

    public List<LoanOfferDTO> getOffers() {
        List<Credit> credits = creditRepository.findAll();
        List<LoanOfferDTO> collect = credits.stream()
                                            .map(it -> LoanOfferDTO.builder()
                                                                   .applicationId(66L)
                                                                   .requestedAmount(it.getAmount())
                                                                   .totalAmount(it.getPsk())
                                                                   .term(it.getTerm())
                                                                   .monthlyPayment(it.getMonthlyPayment())
                                                                   .rate(it.getRate())
                                                                   .isInsuranceEnabled(it.getInsuranceEnable())
                                                                   .isSalaryClient(it.getSalaryClient())
                                                                   .build())
                                            .collect(Collectors.toList());

        return collect;
    }
}
