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

import java.time.LocalDateTime;
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
        log.info("Application status with applicationId={} changed to {}", foundApplication.getApplicationId(),
                foundApplication.getStatus());

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
        log.info("Email sent to topic: {}", finishRegistrationTheme.getTopicName());
    }

    @Transactional
    public void finishRegistration(Long applicationId, FinishRegistrationRequestDTO request) {
        log.info("Input data: {}, ApplicationID={}", request, applicationId);

        Application foundApplication = applicationService.findApplicationById(applicationId);
        ApplicationStatus status = ApplicationStatus.CC_APPROVED;
        applicationService.changeStatus(foundApplication, status);
        log.info("Application status with applicationId={} changed to {}", applicationId, foundApplication.getStatus());

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
                                           .applicationId(applicationId)
                                           .address(client.getEmail())
                                           .theme(createDocumentTheme)
                                           .build();
        kafkaService.sendEmailMessage(createDocumentTheme, message);
        log.info("Email sent to topic: {}", createDocumentTheme.getTopicName());
    }

    @Transactional
    public void sendDocuments(Long applicationId) {
        Application foundApplication = applicationService.findApplicationById(applicationId);
        applicationService.changeStatus(foundApplication, ApplicationStatus.PREPARE_DOCUMENTS);
        log.info("Application status with applicationId={} changed to {}", applicationId, foundApplication.getStatus());
        Theme sendDocumentsTheme = Theme.SEND_DOCUMENTS;
        sendEmailMessage(applicationId, sendDocumentsTheme);
        log.info("Email sent to topic: {}", sendDocumentsTheme.getTopicName());
    }

    @Transactional
    public void signDocuments(Long applicationId) {
        Application foundApplication = applicationService.findApplicationById(applicationId);
        applicationService.generateSesCode(foundApplication);
        log.info("Ses code generated for applicationId={}", applicationId);
        Theme signDocumentsTheme = Theme.SEND_SES;
        sendEmailMessage(applicationId, signDocumentsTheme);
        log.info("Email sent to topic: {}", signDocumentsTheme.getTopicName());
    }

    @Transactional
    public void verifySesCode(Long applicationId) {
        Theme creditIssuedTheme = Theme.CREDIT_ISSUED;
        Application foundApplication = applicationService.findApplicationById(applicationId);
        foundApplication.setSignDate(LocalDateTime.now());
        applicationService.changeStatus(foundApplication, ApplicationStatus.DOCUMENT_SIGNED);
        log.info("Application status with applicationId={} changed to {}", applicationId, foundApplication.getStatus());
        applicationService.changeStatus(foundApplication, ApplicationStatus.CREDIT_ISSUED);
        log.info("Application status with applicationId={} changed to {}", applicationId, foundApplication.getStatus());
        sendEmailMessage(applicationId, creditIssuedTheme);
        log.info("Email sent to topic: {}", creditIssuedTheme.getTopicName());
    }

    @Transactional
    public ApplicationDTO updateApplicationStatus(Long applicationId) {
        Application foundApplication = applicationService.findApplicationById(applicationId);
        applicationService.changeStatus(foundApplication, ApplicationStatus.DOCUMENT_CREATED);
        log.info("Application changed: {}, ", foundApplication);
        return applicationService.getApplicationInfoFromApplication(foundApplication);
    }

    private void sendEmailMessage(Long applicationId, Theme theme) {
        Application foundApplication = applicationService.findApplicationById(applicationId);
        String email = foundApplication.getClientId()
                                       .getEmail();

        EmailMessage message = EmailMessage.builder()
                                           .applicationId(applicationId)
                                           .address(email)
                                           .theme(theme)
                                           .build();
        kafkaService.sendEmailMessage(theme, message);
    }

    public void test(Long id) {
        kafkaService.sendEmailMessage(Theme.APPLICATION_DENIED, EmailMessage.builder()
                                                                            .applicationId(id)
                                                                            .build());
    }
}
