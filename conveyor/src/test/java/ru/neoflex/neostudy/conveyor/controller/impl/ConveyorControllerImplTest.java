package ru.neoflex.neostudy.conveyor.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.neoflex.neostudy.conveyor.exception.BadRequestException;
import ru.neoflex.neostudy.conveyor.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.conveyor.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.conveyor.model.response.ApiErrorResponse;
import ru.neoflex.neostudy.conveyor.service.ConveyorService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ConveyorControllerImpl.class)
class ConveyorControllerImplTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ConveyorService service;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void generateOffers_shouldReturnLoanList() throws Exception {
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
        List<LoanOfferDTO> offers = List.of(LoanOfferDTO.builder()
                                                        .term(6)
                                                        .build());


        when(service.generateOffers(any())).thenReturn(offers);

        mockMvc.perform(post("/conveyor/offers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDTO))
                                .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].term", is(offers.get(0)
                                                         .getTerm())));


    }
@Test
    void generateOffers_shouldThrowBadRequestException_notContainAccept() throws Exception {
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

    MockHttpServletResponse response =
            mockMvc.perform(post("/conveyor/offers")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(requestDTO)))
                   .andExpect(status().isBadRequest())
                   .andExpect(result -> assertTrue(
                           result.getResolvedException() instanceof BadRequestException))
                   .andReturn()
                   .getResponse();
    ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
    assertThat(readValue).isNotNull()
                         .extracting(ApiErrorResponse::getDescription)
                         .isEqualTo("Bad request: Accept=null");


    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_amountIsNull() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
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

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("amount: must not be null");

    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_amountIsLessThan10000() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(9999))
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

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("amount: The amount must be greater than or equal to 10000");

    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_termIsNull() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .firstName("test")
                                                                        .lastName("test")
                                                                        .middleName("test")
                                                                        .email("man@dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("term: must not be null");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_termIsLessThan6() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(5)
                                                                        .firstName("test")
                                                                        .lastName("test")
                                                                        .middleName("test")
                                                                        .email("man@dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("term: The term must be greater than or equal to 6");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_firstNameIsNull() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .lastName("test")
                                                                        .middleName("test")
                                                                        .email("man@dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("firstName: The firstname must contain from 2 to 30 latin characters");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_firstNameIsNotInRegex() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("a")
                                                                        .lastName("test")
                                                                        .middleName("test")
                                                                        .email("man@dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("firstName: The firstname must contain from 2 to 30 latin characters");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_lastNameIsNull() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("test")
                                                                        .middleName("test")
                                                                        .email("man@dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("lastName: The lastname must contain from 2 to 30 latin characters");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_lastNameIsNotInRegex() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("test")
                                                                        .lastName("a")
                                                                        .middleName("test")
                                                                        .email("man@dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("lastName: The lastname must contain from 2 to 30 latin characters");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_middleNameIsNull() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("test")
                                                                        .lastName("test")
                                                                        .email("man@dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("middleName: The middle name must be empty or contain from 2 to 30 " +
                                                "Latin characters");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_middleNameIsNotInRegex() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("test")
                                                                        .lastName("test")
                                                                        .middleName("a")
                                                                        .email("man@dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("middleName: The middle name must be empty or contain from 2 to 30 " +
                                                "Latin characters");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_emailIsNull() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("test")
                                                                        .lastName("test")
                                                                        .middleName("test")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("email: must not be null");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_emailDoesNotMatchPattern() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("test")
                                                                        .lastName("test")
                                                                        .middleName("test")
                                                                        .email("test#dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("email: Email must be: example@dog.con");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_birthdateIsNull() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("test")
                                                                        .lastName("test")
                                                                        .middleName("test")
                                                                        .email("man@dog.con")
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("birthdate: User must be over 18 years of age");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_userUnder18() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("test")
                                                                        .lastName("test")
                                                                        .middleName("test")
                                                                        .email("man@dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(17))
                                                                        .passportSeries("1234")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("birthdate: User must be over 18 years of age");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_passportSeriesIsNull() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("test")
                                                                        .lastName("test")
                                                                        .middleName("test")
                                                                        .email("man@dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("passportSeries: must not be null");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_passportSeriesDoesNotContainFourNumbers() throws Exception {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                                                                        .amount(BigDecimal.valueOf(100000))
                                                                        .term(6)
                                                                        .firstName("test")
                                                                        .lastName("test")
                                                                        .middleName("test")
                                                                        .email("man@dog.con")
                                                                        .birthdate(LocalDate.now()
                                                                                            .minusYears(25))
                                                                        .passportSeries("123")
                                                                        .passportNumber("123456")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("passportSeries: Passport series must contain 4 numbers");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_passportNumberIsNull() throws Exception {
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
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("passportNumber: must not be null");
    }

    @Test
    void generateOffers_shouldThrowMethodArgumentNotValidException_passportNumberDoesNotContainSixNumbers() throws Exception {
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
                                                                        .passportNumber("12345")
                                                                        .build();

        MockHttpServletResponse response =
                mockMvc.perform(post("/conveyor/offers")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(requestDTO))
                                        .accept(MediaType.APPLICATION_JSON))
                       .andExpect(status().isBadRequest())
                       .andExpect(result -> assertTrue(
                               result.getResolvedException() instanceof MethodArgumentNotValidException))
                       .andReturn()
                       .getResponse();
        ApiErrorResponse readValue = objectMapper.readValue(response.getContentAsString(), ApiErrorResponse.class);
        assertThat(readValue).isNotNull()
                             .extracting(ApiErrorResponse::getDescription)
                             .isEqualTo("passportNumber: Passport number must contain 6 numbers");
    }


    @Test
    void calculateCredit() {
    }
}