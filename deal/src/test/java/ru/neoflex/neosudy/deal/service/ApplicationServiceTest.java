package ru.neoflex.neosudy.deal.service;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.neosudy.deal.config.IntegrationEnvironment;
import ru.neoflex.neosudy.deal.config.TestDbConfiguration;
import ru.neoflex.neosudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neosudy.deal.model.entity.Application;
import ru.neoflex.neosudy.deal.model.entity.Client;
import ru.neoflex.neosudy.deal.model.entity.Passport;
import ru.neoflex.neosudy.deal.model.jsonb.StatusHistory;
import ru.neoflex.neosudy.deal.model.types.ApplicationStatus;
import ru.neoflex.neosudy.deal.model.types.ChangeType;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
                () -> assertThat(response.getStatusHistory()
                                         .get(0)
                                         .getChangeType()).isNotNull()
                                                          .isEqualTo(ChangeType.AUTOMATIC),
                () -> assertTrue(Duration.between(response.getCreationDate(), creationDate)
                                         .getSeconds() < 1)
        );
    }

    @Test
    void findApplicationById() {
    }

    @Test
    void changeStatus() {
    }
}