package ru.neoflex.neosudy.deal.controller.impl;

import lombok.RequiredArgsConstructor;
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
    public List<LoanOfferDTO> calculateOffers(LoanApplicationRequestDTO request) {
        return service.getOffers(request);
    }

    @Override
    public void selectOffer(LoanOfferDTO request) {
        service.selectOffer(request);
    }

    @Override
    public void finishRegistration(String applicationId, FinishRegistrationRequestDTO request) {
        service.finishRegistration(applicationId, request);
    }
}
