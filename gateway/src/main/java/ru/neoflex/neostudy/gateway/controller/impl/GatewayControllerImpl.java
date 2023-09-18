package ru.neoflex.neostudy.gateway.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.neostudy.gateway.controller.GatewayController;
import ru.neoflex.neostudy.gateway.model.dto.FinishRegistrationRequestDTO;
import ru.neoflex.neostudy.gateway.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.gateway.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.gateway.service.GatewayService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GatewayControllerImpl implements GatewayController {

    private final GatewayService service;

    @Override
    public List<LoanOfferDTO> createApplication(LoanApplicationRequestDTO prescoringRequest) {
        return service.createApplication(prescoringRequest);
    }

    @Override
    public void applyOffer(LoanOfferDTO request) {
        service.applyOffer(request);
    }

    @Override
    public void finishRegistration(Long applicationId, FinishRegistrationRequestDTO request) {
        service.finishRegistration(applicationId, request);
    }

    @Override
    public void createDocumentsRequest(Long applicationId) {
        service.createDocumentsRequest(applicationId);
    }

    @Override
    public void signDocumentsRequest(Long applicationId) {
        service.signDocumentRequest(applicationId);
    }

    @Override
    public void verifySesCodeRequest(Long applicationId) {
        service.verifySesCodeRequest(applicationId);
    }
}
