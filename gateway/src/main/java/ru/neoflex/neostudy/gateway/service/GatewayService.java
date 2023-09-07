package ru.neoflex.neostudy.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.gateway.model.dto.FinishRegistrationRequestDTO;
import ru.neoflex.neostudy.gateway.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.gateway.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.gateway.webclient.ApplicationClient;
import ru.neoflex.neostudy.gateway.webclient.DealClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayService {

    private final ApplicationClient applicationClient;
    private final DealClient dealClient;

    public List<LoanOfferDTO> createApplication(LoanApplicationRequestDTO prescoringRequest) {
        log.info("Input data: {}", prescoringRequest);
        return applicationClient.createApplication(prescoringRequest);
    }

    public void applyOffer(LoanOfferDTO request) {
        log.info("Offer: {} applied", request);
        applicationClient.applyOffer(request);
    }

    public void finishRegistration(Long applicationId, FinishRegistrationRequestDTO request) {
        log.info("Input {} for applicationId={}", request, applicationId);
        dealClient.finishRegistration(applicationId, request);
    }

    public void createDocumentsRequest(Long applicationId) {
        log.info("Request to create document for applicationId={}", applicationId);
        dealClient.sendDocuments(applicationId);
    }

    public void signDocumentRequest(Long applicationId) {
        log.info("Request to sign document for applicationId={}", applicationId);
        dealClient.signDocuments(applicationId);
    }

    public void verifySesCodeRequest(Long applicationId) {
        log.info("Request to verify ses code for applicationId={}", applicationId);
        dealClient.verifySesCode(applicationId);
    }
}
