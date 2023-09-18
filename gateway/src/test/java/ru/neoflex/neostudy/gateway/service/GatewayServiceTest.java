package ru.neoflex.neostudy.gateway.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.neostudy.gateway.model.dto.FinishRegistrationRequestDTO;
import ru.neoflex.neostudy.gateway.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.gateway.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.gateway.webclient.ApplicationClient;
import ru.neoflex.neostudy.gateway.webclient.DealClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GatewayServiceTest {

    @InjectMocks
    private GatewayService service;
    @Mock
    private ApplicationClient applicationClient;
    @Mock
    private DealClient dealClient;

    @Test
    void createApplication_shouldReturnExpectedList() {
        LoanApplicationRequestDTO mock = Mockito.mock(LoanApplicationRequestDTO.class);
        List<LoanOfferDTO> expectedList = List.of(LoanOfferDTO.builder()
                                                              .term(10)
                                                              .build());

        when(applicationClient.createApplication(any())).thenReturn(expectedList);
        List<LoanOfferDTO> response = service.createApplication(mock);

        assertAll(
                () -> verify(applicationClient, times(1)).createApplication(any()),
                () -> assertThat(response).isNotNull()
                                          .isNotEmpty()
                                          .hasSize(expectedList.size()),
                () -> assertThat(response.get(0)).extracting(LoanOfferDTO::getTerm)
                                                 .isEqualTo(expectedList.get(0)
                                                                        .getTerm())

        );
    }

    @Test
    void applyOffer_shouldCalledOnce() {
        LoanOfferDTO mock = mock(LoanOfferDTO.class);

        service.applyOffer(mock);

        verify(applicationClient, times(1)).applyOffer(any());
    }

    @Test
    void finishRegistration_shouldCalledOnce() {
        FinishRegistrationRequestDTO mock = mock(FinishRegistrationRequestDTO.class);

        service.finishRegistration(1L, mock);

        verify(dealClient, times(1)).finishRegistration(any(), any());
    }

    @Test
    void createDocumentsRequest_shouldCalledOnce() {
        service.createDocumentsRequest(1L);

        verify(dealClient, times(1)).sendDocuments(any());
    }

    @Test
    void signDocumentRequest_shouldCalledOnce() {
        service.signDocumentRequest(1L);

        verify(dealClient, times(1)).signDocuments(any());
    }

    @Test
    void verifySesCodeRequest_shouldCalledOnce() {
        service.verifySesCodeRequest(1L);

        verify(dealClient, times(1)).verifySesCode(any());
    }
}