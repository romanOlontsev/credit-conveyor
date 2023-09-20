package ru.neoflex.neostudy.conveyor.service;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.neostudy.conveyor.model.dto.EmploymentDTO;
import ru.neoflex.neostudy.conveyor.model.dto.ScoringDataDTO;
import ru.neoflex.neostudy.conveyor.model.types.EmploymentStatus;
import ru.neoflex.neostudy.conveyor.model.types.Gender;
import ru.neoflex.neostudy.conveyor.model.types.MaritalStatus;
import ru.neoflex.neostudy.conveyor.model.types.Position;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


@SpringBootTest
class ScoringRateServiceTest {
    @Autowired
    private ScoringRateService service;
    private ScoringDataDTO scoringDataDTO;

    @BeforeEach
    void setUp() {
        EmploymentDTO employment = EmploymentDTO.builder()
                                                .status(EmploymentStatus.BUSINESS_OWNER)
                                                .employerInn("1234")
                                                .salary(BigDecimal.valueOf(50000))
                                                .position(Position.MIDDLE_MANAGER)
                                                .workExperienceTotal(13)
                                                .workExperienceCurrent(4)
                                                .build();
        scoringDataDTO = ScoringDataDTO.builder()
                                       .amount(BigDecimal.valueOf(10000))
                                       .term(6)
                                       .firstName("test")
                                       .lastName("test")
                                       .middleName("test")
                                       .gender(Gender.MALE)
                                       .birthDate(LocalDate.now()
                                                           .minusYears(25))
                                       .passportSeries("1234")
                                       .passportNumber("123456")
                                       .passportIssueDate(LocalDate.now()
                                                                   .minusYears(5))
                                       .passportIssueBranch("test")
                                       .maritalStatus(MaritalStatus.DIVORCED)
                                       .dependentAmount(1)
                                       .employment(employment)
                                       .account("test")
                                       .isInsuranceEnabled(false)
                                       .isSalaryClient(false)
                                       .build();
    }

    @Test
    void calculateScoringRate_shouldReturnRate() {
        BigDecimal scoringRate = service.calculateScoringRate(scoringDataDTO);

        BigDecimal expectedRate = BigDecimal.valueOf(2.0);
        assertThat(scoringRate).isNotNull()
                               .isEqualTo(expectedRate);
    }

    @Test
    void calculateScoringRate_shouldThrowValidatedExceptionBySalary() {
        scoringDataDTO.getEmployment()
                      .setSalary(BigDecimal.TEN);

        String exceptionMessage = "Credit denial: the credit amount is more than 20 salaries";
        assertAll(
                () -> assertThatThrownBy(() -> service.calculateScoringRate(scoringDataDTO))
                        .isInstanceOf(ValidationException.class)
                        .hasMessage(exceptionMessage)

        );
    }

    @Test
    void calculateScoringRate_shouldThrowValidatedExceptionByAge() {
        scoringDataDTO.setBirthDate(LocalDate.now());
        ScoringDataDTO youngAge = scoringDataDTO;
        scoringDataDTO.setBirthDate(LocalDate.now()
                                             .minusYears(100L));
        ScoringDataDTO oldAge = scoringDataDTO;

        String exceptionMessage = "Credit denial: credit is issued from 20 to 60 years";
        assertAll(
                () -> assertThatThrownBy(() -> service.calculateScoringRate(youngAge))
                        .isInstanceOf(ValidationException.class)
                        .hasMessage(exceptionMessage),
                () -> assertThatThrownBy(() -> service.calculateScoringRate(oldAge))
                        .isInstanceOf(ValidationException.class)
                        .hasMessage(exceptionMessage)
        );
    }

    @Test
    void calculateScoringRate_shouldThrowValidatedExceptionByExperience() {
        scoringDataDTO.getEmployment()
                      .setWorkExperienceCurrent(2);
        ScoringDataDTO smallCurrentExp = scoringDataDTO;
        scoringDataDTO.getEmployment()
                      .setWorkExperienceTotal(5);
        ScoringDataDTO smallTotalExp = scoringDataDTO;

        String exceptionMessage = "Credit denial: insufficient experience";
        assertAll(
                () -> assertThatThrownBy(() -> service.calculateScoringRate(smallCurrentExp))
                        .isInstanceOf(ValidationException.class)
                        .hasMessage(exceptionMessage),
                () -> assertThatThrownBy(() -> service.calculateScoringRate(smallTotalExp))
                        .isInstanceOf(ValidationException.class)
                        .hasMessage(exceptionMessage)
        );
    }

    @Test
    void calculateScoringRate_shouldReturnRate_employmentStatus_selfEmployed() {
        scoringDataDTO.getEmployment()
                      .setStatus(EmploymentStatus.SELF_EMPLOYED);
        ScoringDataDTO selfEmployedStatus = scoringDataDTO;

        BigDecimal scoringRate = service.calculateScoringRate(selfEmployedStatus);
        BigDecimal expectedRate = BigDecimal.valueOf(0.0);
        assertThat(scoringRate).isNotNull()
                               .isEqualTo(expectedRate);
    }

    @Test
    void calculateScoringRate_shouldThrowValidatedExceptionByEmploymentStatus() {
        scoringDataDTO.getEmployment()
                      .setStatus(EmploymentStatus.UNEMPLOYED);
        ScoringDataDTO unemployedStatus = scoringDataDTO;

        String exceptionMessage = "Credit denial: employment status - unemployed";
        assertAll(
                () -> assertThatThrownBy(() -> service.calculateScoringRate(unemployedStatus))
                        .isInstanceOf(ValidationException.class)
                        .hasMessage(exceptionMessage)
        );
    }

    @Test
    void calculateScoringRate_shouldReturnRate_position_topManager() {
        scoringDataDTO.getEmployment()
                      .setPosition(Position.TOP_MANAGER);
        ScoringDataDTO topManagerPosition = scoringDataDTO;

        BigDecimal scoringRate = service.calculateScoringRate(topManagerPosition);

        BigDecimal expectedRate = BigDecimal.valueOf(0.0);
        assertThat(scoringRate).isNotNull()
                               .isEqualTo(expectedRate);
    }

    @Test
    void calculateScoringRate_shouldReturnRate_maritalStatus_married() {
        scoringDataDTO.setMaritalStatus(MaritalStatus.MARRIED);
        ScoringDataDTO marriedStatus = scoringDataDTO;

        BigDecimal scoringRate = service.calculateScoringRate(marriedStatus);

        BigDecimal expectedRate = BigDecimal.valueOf(-2.0);
        assertThat(scoringRate).isNotNull()
                               .isEqualTo(expectedRate);
    }

    @Test
    void calculateScoringRate_shouldReturnRate_dependentAmount_moreThanOne() {
        scoringDataDTO.setDependentAmount(2);
        ScoringDataDTO dependent = scoringDataDTO;

        BigDecimal scoringRate = service.calculateScoringRate(dependent);

        BigDecimal expectedRate = BigDecimal.valueOf(3.0);
        assertThat(scoringRate).isNotNull()
                               .isEqualTo(expectedRate);
    }

    @Test
    void calculateScoringRate_shouldReturnRate_gender_male_age_40() {
        scoringDataDTO.setGender(Gender.MALE);
        scoringDataDTO.setBirthDate(LocalDate.now()
                                             .minusYears(40));
        ScoringDataDTO male40 = scoringDataDTO;

        BigDecimal scoringRate = service.calculateScoringRate(male40);
        BigDecimal expectedRate = BigDecimal.valueOf(-1.0);
        assertThat(scoringRate).isNotNull()
                               .isEqualTo(expectedRate);
    }

    @Test
    void calculateScoringRate_shouldReturnRate_gender_female_age_40() {
        scoringDataDTO.setGender(Gender.FEMALE);
        scoringDataDTO.setBirthDate(LocalDate.now()
                                             .minusYears(40));
        ScoringDataDTO female40 = scoringDataDTO;

        BigDecimal scoringRate = service.calculateScoringRate(female40);

        BigDecimal expectedRate = BigDecimal.valueOf(-1.0);
        assertThat(scoringRate).isNotNull()
                               .isEqualTo(expectedRate);
    }

    @Test
    void calculateScoringRate_shouldReturnRate_gender_female_age_20() {
        scoringDataDTO.setGender(Gender.FEMALE);
        scoringDataDTO.setBirthDate(LocalDate.now()
                                             .minusYears(20));
        ScoringDataDTO female40 = scoringDataDTO;

        BigDecimal scoringRate = service.calculateScoringRate(female40);

        BigDecimal expectedRate = BigDecimal.valueOf(2.0);
        assertThat(scoringRate).isNotNull()
                               .isEqualTo(expectedRate);
    }

    @Test
    void calculateScoringRate_shouldReturnRate_gender_noneBinary() {
        scoringDataDTO.setGender(Gender.NON_BINARY);
        ScoringDataDTO nonBinary = scoringDataDTO;

        BigDecimal scoringRate = service.calculateScoringRate(nonBinary);

        BigDecimal expectedRate = BigDecimal.valueOf(5.0);
        assertThat(scoringRate).isNotNull()
                               .isEqualTo(expectedRate);
    }
}