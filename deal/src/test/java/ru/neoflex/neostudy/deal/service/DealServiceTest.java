package ru.neoflex.neostudy.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.neostudy.deal.model.dto.*;
import ru.neoflex.neostudy.deal.model.entity.Application;
import ru.neoflex.neostudy.deal.model.entity.Client;
import ru.neoflex.neostudy.deal.model.entity.Credit;
import ru.neoflex.neostudy.deal.model.types.MaritalStatus;
import ru.neoflex.neostudy.deal.webclient.CreditConveyorClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {
    @InjectMocks
    private DealService service;
    @Mock
    private CreditConveyorClient creditConveyorClient;
    @Mock
    private ApplicationService applicationService;
    @Mock
    private CreditService creditService;
    @Mock
    private ClientService clientService;

    @Test
    void getOffers_shouldReturnExpectedList() {
        List<LoanOfferDTO> expectedList = List.of(LoanOfferDTO.builder()
                                                              .build(), LoanOfferDTO.builder()
                                                                                    .build());
        Application application = Application.builder()
                                             .applicationId(-231L)
                                             .build();

        when(applicationService.createApplication(any())).thenReturn(application);
        when(creditConveyorClient.calculateOffers(any())).thenReturn(expectedList);

        LoanApplicationRequestDTO request = LoanApplicationRequestDTO.builder()
                                                                     .build();
        List<LoanOfferDTO> response = service.getOffers(request);
        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isNotEmpty()
                                          .size()
                                          .isEqualTo(expectedList.size()),
                () -> assertThat(response.get(0)).extracting(LoanOfferDTO::getApplicationId)
                                                 .isEqualTo(application.getApplicationId()),
                () -> assertThat(response.get(1)).extracting(LoanOfferDTO::getApplicationId)
                                                 .isEqualTo(application.getApplicationId())
        );
    }

    @Test
    void selectOffer_shouldUpdateApplication() {
        Application application = Application.builder()
                                             .sesCode("test")
                                             .build();
        Credit credit = Credit.builder()
                              .term(-200)
                              .build();
        LoanOfferDTO offer = LoanOfferDTO.builder()
                                         .isSalaryClient(true)
                                         .build();

        when(applicationService.findApplicationById(any())).thenReturn(application);
        doNothing().when(applicationService)
                   .changeStatus(any(), any());
        when(creditService.getCreditFromLoanOfferDTO(any())).thenReturn(credit);

        service.selectOffer(offer);
        assertAll(
                () -> assertThat(application).extracting(Application::getCreditId)
                                             .isEqualTo(credit),
                () -> assertThat(application).extracting(Application::getAppliedOffer)
                                             .isEqualTo(offer),
                () -> verify(applicationService, times(1)).findApplicationById(any()),
                () -> verify(applicationService, times(1)).changeStatus(any(), any()),
                () -> verify(creditService, times(1)).getCreditFromLoanOfferDTO(any())
        );
    }

    @Test
    void finishRegistration_shouldUpdateApplicationClientCredit() {
        Client client = Client.builder()
                              .clientId(-3L)
                              .build();
        Credit credit = Credit.builder()
                              .creditId(-2L)
                              .term(-200)
                              .build();
        Application application = Application.builder()
                                             .clientId(client)
                                             .creditId(credit)
                                             .sesCode("test")
                                             .build();
        FinishRegistrationRequestDTO offer = FinishRegistrationRequestDTO.builder()
                                                                         .maritalStatus(MaritalStatus.SINGLE)
                                                                         .build();
        ScoringDataDTO scoringData = ScoringDataDTO.builder()
                                                   .build();
        CreditDTO finalCredit = CreditDTO.builder()
                                         .build();

        when(applicationService.findApplicationById(any())).thenReturn(application);
        doNothing().when(applicationService)
                   .changeStatus(any(), any());
        doNothing().when(clientService)
                   .updateClientFromFinishRegistrationRequestDTO(any(), any());
        when(clientService.getScoringDataDTOFromClient(any())).thenReturn(scoringData);
        doNothing().when(creditService)
                   .updateScoringDataDTOFromCredit(any(), any());
        when(creditConveyorClient.calculateCredit(any())).thenReturn(finalCredit);
        doNothing().when(creditService)
                   .updateCreditFromCreditDto(any(), any());


        service.finishRegistration("-1", offer);
        assertAll(
                () -> verify(applicationService, times(1)).findApplicationById(any()),
                () -> verify(applicationService, times(1)).changeStatus(any(), any()),
                () -> verify(clientService, times(1))
                        .updateClientFromFinishRegistrationRequestDTO(any(), any()),
                () -> verify(clientService, times(1))
                        .getScoringDataDTOFromClient(any()),
                () -> verify(creditService, times(1)).updateScoringDataDTOFromCredit(any(), any()),
                () -> verify(creditService, times(1)).updateCreditFromCreditDto(any(), any())
        );
    }
}