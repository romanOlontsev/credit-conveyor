package ru.neoflex.neosudy.deal.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.neosudy.deal.exception.DataNotFoundException;
import ru.neoflex.neosudy.deal.mapper.ClientMapper;
import ru.neoflex.neosudy.deal.mapper.CreditMapper;
import ru.neoflex.neosudy.deal.model.dto.*;
import ru.neoflex.neosudy.deal.model.entity.Application;
import ru.neoflex.neosudy.deal.model.entity.Client;
import ru.neoflex.neosudy.deal.model.entity.Credit;
import ru.neoflex.neosudy.deal.model.entity.Passport;
import ru.neoflex.neosudy.deal.model.jsonb.StatusHistory;
import ru.neoflex.neosudy.deal.model.types.ApplicationStatus;
import ru.neoflex.neosudy.deal.model.types.ChangeType;
import ru.neoflex.neosudy.deal.repository.ApplicationRepository;
import ru.neoflex.neosudy.deal.webclient.CreditConveyorClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {
    private final ClientMapper clientMapper;
    private final CreditMapper creditMapper;
    private final ApplicationRepository applicationRepository;
    private final CreditConveyorClient creditConveyorClient;
    private final CreditConveyorClient feignClient;

    //    TODO
//      refactoring + check if exists
    @Transactional
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO request) {
        Client client = clientMapper.loanApplicationRequestToClient(request);
        Passport passport = Passport.builder()
                                    .series(request.getPassportSeries())
                                    .number(request.getPassportNumber())
                                    .build();
        client.setPassport(passport);

        ApplicationStatus status = ApplicationStatus.PREAPPROVAL;
        LocalDateTime creationDate = LocalDateTime.now();
        StatusHistory statusHistory = StatusHistory.builder()
                                                   .status(status)
                                                   .time(creationDate)
                                                   .changeType(ChangeType.AUTOMATIC)
                                                   .build();
        Application application = Application.builder()
                                             .clientId(client)
                                             .status(status)
                                             .creationDate(creationDate)
                                             .statusHistory(List.of(statusHistory))
                                             .build();
        Application savedApp = applicationRepository.save(application);

        List<LoanOfferDTO> loanOfferDTOS = creditConveyorClient.calculateOffers(request);
        loanOfferDTOS.forEach(it -> it.setApplicationId(savedApp.getApplicationId()));
        return loanOfferDTOS;
    }


    @Transactional
    public void selectOffer(LoanOfferDTO request) {
        Long applicationId = request.getApplicationId();
        Application foundApplication = applicationRepository.findById(applicationId)
                                                            .orElseThrow(() -> new DataNotFoundException("Application with id=" +
                                                                    applicationId + " not found"));

        ApplicationStatus status = ApplicationStatus.APPROVED;
        LocalDateTime creationDate = LocalDateTime.now();
        StatusHistory statusHistory = StatusHistory.builder()
                                                   .status(status)
                                                   .time(creationDate)
                                                   .changeType(ChangeType.AUTOMATIC)
                                                   .build();
        Credit preliminaryOffer = creditMapper.loanOfferToCredit(request);

        foundApplication.setStatus(status);
        foundApplication.setAppliedOffer(request);
        foundApplication.getStatusHistory()
                        .add(statusHistory);
        foundApplication.setCreditId(preliminaryOffer);
    }

    // TODO
    //  not end
    @Transactional
    public void finishRegistration(String applicationId, FinishRegistrationRequestDTO request) {
        Long parsedLong = Long.parseLong(applicationId);
        Application foundApplication = applicationRepository.findById(parsedLong)
                                                            .orElseThrow(() -> new DataNotFoundException("Application with id=" +
                                                                    applicationId + " not found"));
        Client client = foundApplication.getClientId();
        clientMapper.updateClient(client, request);
        System.out.println(client);

        Credit credit = foundApplication.getCreditId();
        ScoringDataDTO scoringData = clientMapper.clientToScoringData(client);
//        Passport foundPassport = client.getPassport();
//        Passport updatedPassport = passportMapper.updatePassport(foundPassport, request);
        creditMapper.updateScoringData(scoringData, credit);

        CreditDTO calculatedCredit = creditConveyorClient.calculateCredit(scoringData);
        creditMapper.updateCredit(credit, calculatedCredit);
    }
}
