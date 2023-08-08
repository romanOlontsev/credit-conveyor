package ru.neoflex.neosudy.deal.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.neosudy.deal.controller.DealController;
import ru.neoflex.neosudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neosudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neosudy.deal.service.CreditService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DealControllerImpl implements DealController {
    private final CreditService service;

    @Override
    public ResponseEntity<List<LoanOfferDTO>> generateOffers(LoanApplicationRequestDTO prescoringRequest) {
        List<LoanOfferDTO> offers = service.getOffers();
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }
}
