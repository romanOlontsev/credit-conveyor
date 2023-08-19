package ru.neoflex.neostudy.application.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.neostudy.application.controller.AppController;
import ru.neoflex.neostudy.application.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.application.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.application.service.ApplicationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppControllerImpl implements AppController {
    private final ApplicationService service;


    @Override
    public List<LoanOfferDTO> createApplication(LoanApplicationRequestDTO prescoringRequest) {
        return service.createApplication(prescoringRequest);
    }

    @Override
    public void applyOffer(LoanOfferDTO request) {
        service.applyOffer(request);
    }
}
