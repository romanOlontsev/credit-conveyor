package ru.neoflex.neostudy.conveyor.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.neostudy.conveyor.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.conveyor.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.conveyor.service.ConveyorService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = ConveyorControllerImpl.class)
class ConveyorControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HttpServletRequest request;

    @MockBean
    private ConveyorService service;

    @Test
    void generateOffers_shouldReturnLoanList() {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("test")
                                                                        .lastName("test")
                                                                        .middleName("test")
                                                                        .email("man@dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();
        List<LoanOfferDTO> offers = List.of(new LoanOfferDTO());

        when(service.generateOffers(any())).thenReturn(offers);
        when(request.getHeader(any())).thenReturn("application/json");
//          TODO
//        mockMvc.perform(post("/conveyor/offers").contentType(MediaType.APPLICATION_JSON));


    }

    @Test
    void calculateCredit() {
    }
}