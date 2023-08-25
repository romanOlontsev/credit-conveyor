package ru.neoflex.neostudy.deal.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.neostudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.deal.model.dto.FinishRegistrationRequestDTO;
import ru.neoflex.neostudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.deal.service.DealService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DealControllerImpl.class)
class DealControllerImplTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DealService service;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void calculateOffers_shouldReturnLoanOfferDTOList() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("test")
                                                                        .lastName("test")
                                                                        .middleName("test")
                                                                        .email("man@dog.con")
                                                                        .birthDate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();
        List<LoanOfferDTO> offers = List.of(LoanOfferDTO.builder()
                                                        .term(6)
                                                        .build());

        when(service.getOffers(any())).thenReturn(offers);

        mockMvc.perform(post("/deal/application")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(requestDTO))
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].term", is(offers.get(0)
                                                         .getTerm())));
    }

    @Test
    void selectOffer_shouldCallSelectOfferOnce() throws Exception {
        LoanOfferDTO request = LoanOfferDTO.builder()
                                           .build();

        mockMvc.perform(put("/deal/offer")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
        verify(service, times(1)).selectOffer(any());
    }

    @Test
    void finishRegistration_shouldCallFinishRegistrationOnce() throws Exception {
        FinishRegistrationRequestDTO request = FinishRegistrationRequestDTO.builder()
                                                                           .build();

        mockMvc.perform(put("/deal/calculate/{applicationId}", 1)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
                       .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
        verify(service, times(1)).finishRegistration(any(), any());
    }
}