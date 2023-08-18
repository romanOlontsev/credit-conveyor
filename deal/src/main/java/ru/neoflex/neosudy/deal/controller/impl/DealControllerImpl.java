package ru.neoflex.neosudy.deal.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.neosudy.deal.controller.DealController;
import ru.neoflex.neosudy.deal.model.dto.FinishRegistrationRequestDTO;
import ru.neoflex.neosudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neosudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neosudy.deal.service.DealService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DealControllerImpl implements DealController {
    private final DealService service;

    @Override
    public ResponseEntity<List<LoanOfferDTO>> calculateOffers(LoanApplicationRequestDTO request) {
        List<LoanOfferDTO> offers = service.getOffers(request);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> selectOffer(LoanOfferDTO request) {
        service.selectOffer(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> finishRegistration(String applicationId, FinishRegistrationRequestDTO request) {
        service.finishRegistration(applicationId, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
