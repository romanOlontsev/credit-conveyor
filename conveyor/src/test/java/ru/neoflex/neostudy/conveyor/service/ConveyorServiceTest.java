package ru.neoflex.neostudy.conveyor.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.neostudy.conveyor.model.dto.*;
import ru.neoflex.neostudy.conveyor.model.types.EmploymentStatus;
import ru.neoflex.neostudy.conveyor.model.types.Gender;
import ru.neoflex.neostudy.conveyor.model.types.MaritalStatus;
import ru.neoflex.neostudy.conveyor.model.types.Position;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConveyorServiceTest {
    @InjectMocks
    private ConveyorService service;
    @Mock
    private ScoringService scoringService;

    @Test
    void generateOffers_shouldReturnList() {
        LoanApplicationRequestDTO request = LoanApplicationRequestDTO.builder()
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

        BigDecimal monthlyPayment = BigDecimal.valueOf(10000);
        BigDecimal firstPreRate = BigDecimal.valueOf(25);
        BigDecimal secondPreRate = BigDecimal.valueOf(27);
        BigDecimal totalAmount = BigDecimal.valueOf(120000);
        when(scoringService.calculatePreRate(anyBoolean(), anyBoolean()))
                .thenReturn(firstPreRate, secondPreRate, firstPreRate, secondPreRate);
        when(scoringService.calculateMonthlyPayment(any(), anyInt(), any())).thenReturn(monthlyPayment);
        when(scoringService.evaluateTotalAmount(anyInt(), any())).thenReturn(totalAmount);

        List<LoanOfferDTO> offers = service.generateOffers(request);

        assertAll(
                () -> assertThat(offers).isNotNull()
                                        .isNotEmpty()
                                        .hasSize(4),
                () -> assertThat(offers.get(1)).extracting(LoanOfferDTO::getMonthlyPayment)
                                               .isEqualTo(monthlyPayment),
                () -> assertThat(offers.get(1)).extracting(LoanOfferDTO::getRate)
                                               .isEqualTo(secondPreRate),
                () -> assertThat(offers.get(2)).extracting(LoanOfferDTO::getRate)
                                               .isEqualTo(firstPreRate),
                () -> verify(scoringService, times(4)).calculatePreRate(anyBoolean(), anyBoolean()),
                () -> verify(scoringService, times(4)).evaluateTotalAmount(anyInt(), any())
        );


    }

    @Test
    void calculateCredit_shouldReturnCreditDto() {
        EmploymentDTO employment = EmploymentDTO.builder()
                                                .status(EmploymentStatus.SELF_EMPLOYED)
                                                .employerInn("321123")
                                                .salary(BigDecimal.valueOf(50000))
                                                .position(Position.MIDDLE_MANAGER)
                                                .workExperienceTotal(15)
                                                .workExperienceCurrent(5)
                                                .build();
        ScoringDataDTO scoringRequest = ScoringDataDTO.builder()
                                                      .amount(BigDecimal.valueOf(100000))
                                                      .term(6)
                                                      .firstName("test")
                                                      .lastName("test")
                                                      .middleName("test")
                                                      .gender(Gender.FEMALE)
                                                      .birthDate(LocalDate.now()
                                                                          .minusYears(30))
                                                      .passportSeries("1234")
                                                      .passportNumber("123456")
                                                      .passportIssueDate(LocalDate.now()
                                                                                  .minusYears(2))
                                                      .passportIssueBranch("test")
                                                      .maritalStatus(MaritalStatus.MARRIED)
                                                      .dependentAmount(1)
                                                      .employment(employment)
                                                      .account("abc")
                                                      .isInsuranceEnabled(false)
                                                      .isSalaryClient(false)
                                                      .build();

        BigDecimal creditRate = BigDecimal.valueOf(25);
        BigDecimal monthlyPayment = BigDecimal.valueOf(9000);
        BigDecimal totalAmount = BigDecimal.valueOf(120000);
        when(scoringService.calculateCreditRate(any())).thenReturn(creditRate);
        when(scoringService.calculateMonthlyPayment(any(), anyInt(), any())).thenReturn(monthlyPayment);
        when(scoringService.evaluateTotalAmount(anyInt(), any())).thenReturn(totalAmount);
        when(scoringService.createMonthlyPaymentSchedule(any(), any(), any(), anyInt())).thenReturn(new ArrayList<>());
        CreditDTO credit = service.calculateCredit(scoringRequest);

        assertAll(
                () -> assertThat(credit).extracting(CreditDTO::getRate)
                                        .isEqualTo(creditRate),
                () -> assertThat(credit).extracting(CreditDTO::getMonthlyPayment)
                                        .isEqualTo(monthlyPayment),
                () -> assertThat(credit).extracting(CreditDTO::getAmount)
                                        .isEqualTo(scoringRequest.getAmount()),
                () -> verify(scoringService, times(1))
                        .createMonthlyPaymentSchedule(any(), any(), any(), anyInt())
        );
    }
}