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
    public void finishRegistration(Long applicationId, FinishRegistrationRequestDTO request) {
        log.info("Input data: {}, ApplicationID={}", request, applicationId);

        Application foundApplication = applicationService.findApplicationById(applicationId);
        ApplicationStatus status = ApplicationStatus.CC_APPROVED;
        applicationService.changeStatus(foundApplication, status);

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
    }

    @Transactional
    public void sendDocuments(Long applicationId) {
        Application foundApplication = applicationService.findApplicationById(applicationId);
        applicationService.changeStatus(foundApplication, ApplicationStatus.PREPARE_DOCUMENTS);
        Theme sendDocumentsTheme = Theme.SEND_DOCUMENTS;
        sendEmailMessage(applicationId, sendDocumentsTheme);
    }

    @Transactional
    public void signDocuments(Long applicationId) {
        Application foundApplication = applicationService.findApplicationById(applicationId);
        applicationService.generateSesCode(foundApplication);
        Theme signDocumentsTheme = Theme.SEND_SES;
        sendEmailMessage(applicationId, signDocumentsTheme);
    }

    @Transactional
    public void verifySesCode(Long applicationId) {
        Theme creditIssuedTheme = Theme.CREDIT_ISSUED;
        Application foundApplication = applicationService.findApplicationById(applicationId);
        foundApplication.setSignDate(LocalDateTime.now());
        applicationService.changeStatus(foundApplication, ApplicationStatus.DOCUMENT_SIGNED);
        applicationService.changeStatus(foundApplication, ApplicationStatus.CREDIT_ISSUED);
        sendEmailMessage(applicationId, creditIssuedTheme);
    }

    @Transactional
    public void updateApplicationStatus(Long applicationId) {
        Application foundApplication = applicationService.findApplicationById(applicationId);
        applicationService.changeStatus(foundApplication, ApplicationStatus.DOCUMENT_CREATED);
    }

    public ApplicationDTO getApplicationById(Long id) {
        Application foundApplication = applicationService.findApplicationById(id);
        return applicationService.getApplicationDTOFromApplication(foundApplication);
    }

    public List<ApplicationDTO> getAllApplications() {
        List<Application> allApplications = applicationService.findAllApplications();
        return applicationService.getApplicationDTOListFromApplicationList(allApplications);

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
}
