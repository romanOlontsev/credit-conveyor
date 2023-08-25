package ru.neoflex.neostudy.deal.service;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.neostudy.deal.config.IntegrationEnvironment;
import ru.neoflex.neostudy.deal.config.TestDbConfiguration;
import ru.neoflex.neostudy.deal.exception.DataNotFoundException;
import ru.neoflex.neostudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.deal.model.entity.Application;
import ru.neoflex.neostudy.deal.model.entity.Client;
import ru.neoflex.neostudy.deal.model.entity.Passport;
import ru.neoflex.neostudy.deal.model.jsonb.StatusHistory;
import ru.neoflex.neostudy.deal.model.types.ChangeType;
import ru.neoflex.neostudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.deal.model.types.ApplicationStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ApplicationServiceTest extends IntegrationEnvironment {
    @Autowired
    private TestDbConfiguration.DbInfo info;
    @Autowired
    private ApplicationService service;

    @BeforeEach
    void setUp() {
        try (Connection connection = DriverManager.getConnection(info.getUrl(), info.getUsername(), info.getPassword());
             Database database = DatabaseFactory.getInstance()
                                                .findCorrectDatabaseImplementation(new JdbcConnection(connection))) {
            Liquibase liquibase = new liquibase.Liquibase("master.xml",
                    new DirectoryResourceAccessor(new File("").toPath()
                                                              .toAbsolutePath()
                                                              .getParent()
                                                              .resolve("migrations")), database
            );
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException | SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createApplication_shouldReturnExpectedApplication() {
//        given
        LoanApplicationRequestDTO request = LoanApplicationRequestDTO.builder()
                                                                     .firstName("test")
                                                                     .lastName("test")
                                                                     .middleName("test")
                                                                     .email("test@test.com")
                                                                     .birthDate(LocalDate.of(2000, 1, 1))
                                                                     .passportSeries("1234")
                                                                     .passportNumber("123456")
                                                                     .build();
        Client client = Client.builder()
                              .clientId(3L)
                              .firstName("test")
                              .lastName("test")
                              .middleName("test")
                              .birthDate(LocalDate.of(2000, 1, 1))
                              .email("test@test.com")
                              .passport(Passport.builder()
                                                .passportId(3L)
                                                .series("1234")
                                                .number("123456")
                                                .build())
                              .build();
        Application expectedApplication = Application.builder()
                                                     .applicationId(3L)
                                                     .clientId(client)
                                                     .build();
        ApplicationStatus status = ApplicationStatus.PREAPPROVAL;
        LocalDateTime creationDate = LocalDateTime.now();
        StatusHistory statusHistory = StatusHistory.builder()
                                                   .status(status)
                                                   .time(creationDate)
                                                   .changeType(ChangeType.AUTOMATIC)
                                                   .build();
        expectedApplication.setStatus(status);
        expectedApplication.setCreationDate(creationDate);
        expectedApplication.setStatusHistory(List.of(statusHistory));
//        when
        Application response = service.createApplication(request);
//        then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getApplicationId()).isNotNull()
                                                             .isEqualTo(expectedApplication.getApplicationId()),
                () -> assertThat(response.getClientId()).isNotNull()
                                                        .isEqualTo(client),
                () -> assertThat(response.getStatus()).isNotNull()
                                                      .isEqualTo(status),
                () -> assertThat(response.getSesCode()).isNotNull(),
                () -> assertThat(response.getStatusHistory()
                                         .get(0)
                                         .getStatus()).isNotNull()
                                                      .isEqualTo(statusHistory.getStatus()),
                () -> Assertions.assertThat(response.getStatusHistory()
                                                    .get(0)
                                                    .getChangeType()).isNotNull()
                                .isEqualTo(ChangeType.AUTOMATIC),
                () -> assertTrue(Duration.between(response.getCreationDate(), creationDate)
                                         .getSeconds() < 1)
        );
    }

    @Test
    void findApplicationById_shouldReturnExpectedApplication() {
//        given
        LoanOfferDTO expectedLoanOfferDTO = LoanOfferDTO.builder()
                                                        .applicationId(6L)
                                                        .requestedAmount(BigDecimal.valueOf(125000))
                                                        .totalAmount(BigDecimal.valueOf(146254.5))
                                                        .term(14)
                                                        .monthlyPayment(BigDecimal.valueOf(10446.75))
                                                        .rate(BigDecimal.valueOf(26))
                                                        .isInsuranceEnabled(true)
                                                        .isSalaryClient(true)
                                                        .build();
        StatusHistory expectedHistory = StatusHistory.builder()
                                                     .time(LocalDateTime.parse("2023-08-11T19:39:55.2076183"))
                                                     .status(ApplicationStatus.PREAPPROVAL)
                                                     .changeType(ChangeType.AUTOMATIC)
                                                     .build();
        Application expectedApplication = Application.builder()
                                                     .applicationId(1L)
                                                     .status(ApplicationStatus.PREPARE_DOCUMENTS)
                                                     .creationDate(LocalDateTime.parse("2023-08-11T19:32:17.3441307"))
                                                     .appliedOffer(expectedLoanOfferDTO)
                                                     .sesCode("1234-1323-3212-6543")
                                                     .statusHistory(List.of(expectedHistory))
                                                     .build();
//        when
        Application response = service.findApplicationById(1L);
//        then
        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getApplicationId()).isEqualTo(expectedApplication.getApplicationId()),
                () -> assertThat(response.getSesCode()).isEqualTo(expectedApplication.getSesCode()),
                () -> Assertions.assertThat(response.getStatusHistory()).isEqualTo(expectedApplication.getStatusHistory()),
                () -> assertThat(response.getAppliedOffer()).isEqualTo(expectedApplication.getAppliedOffer())
        );
    }

    @Test
    void findApplicationById_shouldThrowDataNotFoundException() {
        assertAll(
                () -> assertThatThrownBy(() -> service.findApplicationById(-999L))
                        .isInstanceOf(DataNotFoundException.class)
                        .hasMessage("Application with id=-999 not found")
        );
    }

    @Test
    void changeStatus_shouldChangeStatusAndCreateHistory() {
        Application application = Application.builder()
                                             .build();
        ApplicationStatus status = ApplicationStatus.CC_DENIED;

        service.changeStatus(application, status);

        assertAll(
                () -> assertThat(application.getStatus()).isNotNull()
                                                         .isEqualTo(status),
                () -> assertTrue(Duration.between(application.getCreationDate(), LocalDateTime.now())
                                         .getSeconds() < 1),
                () -> Assertions.assertThat(application.getStatusHistory()
                                                       .get(0)
                                                       .getChangeType()).isEqualTo(ChangeType.AUTOMATIC)
        );
    }

    @Test
    void changeStatus_shouldChangeStatusAndAddToHistory() {
        Application application = Application.builder()
                                             .statusHistory(new ArrayList<>())
                                             .build();
        ApplicationStatus status = ApplicationStatus.CC_DENIED;

        service.changeStatus(application, status);

        assertAll(
                () -> assertThat(application.getStatus()).isNotNull()
                                                         .isEqualTo(status),
                () -> assertTrue(Duration.between(application.getCreationDate(), LocalDateTime.now())
                                         .getSeconds() < 1),
                () -> Assertions.assertThat(application.getStatusHistory()
                                                       .get(0)
                                                       .getChangeType()).isEqualTo(ChangeType.AUTOMATIC)
        );
    }
}