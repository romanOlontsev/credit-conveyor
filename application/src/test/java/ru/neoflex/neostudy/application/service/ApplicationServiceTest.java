package ru.neoflex.neostudy.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.neostudy.application.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.application.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.application.webclient.DealClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    @InjectMocks
    private ApplicationService service;
    @Mock
    private DealClient client;

    @Test
    void createApplication_shouldReturnExpectedList() {
        LoanApplicationRequestDTO request = LoanApplicationRequestDTO.builder()
                                                                     .build();
        List<LoanOfferDTO> expetedList = List.of(LoanOfferDTO.builder()
                                                             .term(-1)
                                                             .build());

        when(client.calculateOffers(any())).thenReturn(expetedList);

        List<LoanOfferDTO> response = service.createApplication(request);
        assertAll(
                () -> assertThat(response).isNotNull()
                                          .element(0)
                                          .extracting(LoanOfferDTO::getTerm)
                                          .isEqualTo(expetedList.get(0)
                                                                .getTerm()),
                () -> verify(client, times(1)).calculateOffers(any())
                 );
    }

    @Test
    void applyOffer_selectOfferShouldCalledOnce() {
        LoanOfferDTO request = LoanOfferDTO.builder()
                                           .build();

        doNothing().when(client)
                   .selectOffer(any());

        service.applyOffer(request);
        assertAll(
                () -> verify(client, times(1)).selectOffer(any())
                 );
    }
}