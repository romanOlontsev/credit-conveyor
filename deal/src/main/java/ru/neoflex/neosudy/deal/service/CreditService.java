package ru.neoflex.neosudy.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.neosudy.deal.mapper.CreditMapper;
import ru.neoflex.neosudy.deal.model.dto.CreditDTO;
import ru.neoflex.neosudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neosudy.deal.model.dto.ScoringDataDTO;
import ru.neoflex.neosudy.deal.model.entity.Credit;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditMapper creditMapper;

    public Credit getCreditFromLoanOfferDTO(LoanOfferDTO request) {
        return creditMapper.loanOfferToCredit(request);

    }

    public void updateScoringDataDTOFromCredit(ScoringDataDTO scoringData, Credit credit) {
        creditMapper.updateScoringData(scoringData, credit);
    }

    public void updateCreditFromCreditDto(Credit credit, CreditDTO creditDTO) {
        creditMapper.updateCredit(credit, creditDTO);
    }
}
