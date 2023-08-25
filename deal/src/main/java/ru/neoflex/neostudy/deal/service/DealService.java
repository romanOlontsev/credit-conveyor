package ru.neoflex.neostudy.deal.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.deal.model.dto.*;
import ru.neoflex.neostudy.deal.model.entity.Application;
import ru.neoflex.neostudy.deal.model.entity.Client;
import ru.neoflex.neostudy.deal.model.entity.Credit;
import ru.neoflex.neostudy.deal.model.response.EmailMessage;
import ru.neoflex.neostudy.deal.model.types.ApplicationStatus;
import ru.neoflex.neostudy.deal.model.types.Theme;
import ru.neoflex.neostudy.deal.webclient.CreditConveyorClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {
    private final CreditConveyorClient creditConveyorClient;
    private final ApplicationService applicationService;
    private final CreditService creditService;
    private final ClientService clientService;
    private final KafkaService kafkaService;

    @Transactional
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO request) {
        Application createdApplication = applicationService.createApplication(request);
        log.info("Application saved: {}", createdApplication);

        List<LoanOfferDTO> loanOfferDTOS = creditConveyorClient.calculateOffers(request);
        loanOfferDTOS.forEach(it -> it.setApplicationId(createdApplication.getApplicationId()));
        log.info("Received loans from /conveyor/offers: {}", loanOfferDTOS);
        return loanOfferDTOS;
    }


    @Transactional
    public void selectOffer(LoanOfferDTO request) {
        log.info("LoanOfferDTO input: {}", request);
        Application foundApplication = applicationService.findApplicationById(request.getApplicationId());
        ApplicationStatus status = ApplicationStatus.APPROVED;
        applicationService.changeStatus(foundApplication, status);

        Credit credit = creditService.getCreditFromLoanOfferDTO(request);
        foundApplication.setAppliedOffer(request);
        foundApplication.setCreditId(credit);
        log.info("Application changed: {}", foundApplication);

        String email = foundApplication.getClientId()
                                       .getEmail();
        Theme finishRegistrationTheme = Theme.FINISH_REGISTRATION;
        EmailMessage message = EmailMessage.builder()
                                           .applicationId(foundApplication.getApplicationId())
                                           .address(email)
                                           .theme(finishRegistrationTheme)
                                           .build();
        kafkaService.sendEmailMessage(finishRegistrationTheme, message);
    }

    @Transactional
    public void finishRegistration(String applicationId, FinishRegistrationRequestDTO request) {
        log.info("Input data: {}, ApplicationID={}", request, applicationId);

        Application foundApplication = applicationService.findApplicationById(Long.parseLong(applicationId));
        ApplicationStatus status = ApplicationStatus.CC_APPROVED;
        applicationService.changeStatus(foundApplication, status);
        log.info("Application changed: {}, ", foundApplication);

        Client client = foundApplication.getClientId();
        clientService.updateClientFromFinishRegistrationRequestDTO(client, request);
        log.info("Client changed: {}, ", client);

        ScoringDataDTO scoringData = clientService.getScoringDataDTOFromClient(client);
        Credit credit = foundApplication.getCreditId();
        creditService.updateScoringDataDTOFromCredit(scoringData, credit);
        CreditDTO calculatedFinalCredit = creditConveyorClient.calculateCredit(scoringData);
        creditService.updateCreditFromCreditDto(credit, calculatedFinalCredit);
        log.info("Credit calculated from /conveyor/calculation. Credit changed: {}, ", credit);

        Theme createDocumentTheme = Theme.CREATE_DOCUMENTS;
        EmailMessage message = EmailMessage.builder()
                                           .applicationId(Long.valueOf(applicationId))
                                           .address(client.getEmail())
                                           .theme(createDocumentTheme)
                                           .build();
        kafkaService.sendEmailMessage(createDocumentTheme, message);
    }

    public void sendDocuments(String applicationId) {
        Theme sendDocumentsTheme = Theme.SEND_DOCUMENTS;
        sendEmailMessage(applicationId, sendDocumentsTheme);

    }

    public void signDocuments(String applicationId) {
        Theme signDocumentsTheme = Theme.SEND_SES;
        sendEmailMessage(applicationId, signDocumentsTheme);
    }

    public void verifySesCode(String applicationId) {
        Theme creditIssuedTheme = Theme.CREDIT_ISSUED;
        sendEmailMessage(applicationId, creditIssuedTheme);
    }

    private void sendEmailMessage(String applicationId, Theme theme) {
        Application foundApplication = applicationService.findApplicationById(Long.valueOf(applicationId));
        String email = foundApplication.getClientId()
                                       .getEmail();

        EmailMessage message = EmailMessage.builder()
                                           .applicationId(Long.valueOf(applicationId))
                                           .address(email)
                                           .theme(theme)
                                           .build();
        kafkaService.sendEmailMessage(theme, message);
    }
}
