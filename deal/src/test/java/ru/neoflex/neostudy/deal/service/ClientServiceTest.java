package ru.neoflex.neostudy.deal.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.neoflex.neostudy.deal.model.dto.EmploymentDTO;
import ru.neoflex.neostudy.deal.model.entity.Client;
import ru.neoflex.neostudy.deal.model.entity.Employment;
import ru.neoflex.neostudy.deal.model.entity.Passport;
import ru.neoflex.neostudy.deal.model.types.EmploymentStatus;
import ru.neoflex.neostudy.deal.model.types.Gender;
import ru.neoflex.neostudy.deal.model.types.MaritalStatus;
import ru.neoflex.neostudy.deal.model.types.Position;
import ru.neoflex.neostudy.deal.mapper.ClientMapperImpl;
import ru.neoflex.neostudy.deal.model.dto.FinishRegistrationRequestDTO;
import ru.neoflex.neostudy.deal.model.dto.LoanApplicationRequestDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class ClientServiceTest {
    private static ClientService service;

    @BeforeAll
    static void beforeAll() {
        service = new ClientService(new ClientMapperImpl());
    }

    @Test
    void getClientFromLoanApplicationRequestDTO_shouldReturnExpectedClient() {
        LoanApplicationRequestDTO request = LoanApplicationRequestDTO.builder()
                                                                     .firstName("test")
                                                                     .lastName("test")
                                                                     .middleName("test")
                                                                     .email("test@test.con")
                                                                     .passportNumber("123456")
                                                                     .passportSeries("1234")
                                                                     .build();
        Client expectedClient = Client.builder()
                                      .firstName(request.getFirstName())
                                      .lastName(request.getLastName())
                                      .middleName(request.getMiddleName())
                                      .email(request.getEmail())
                                      .passport(Passport.builder()
                                                        .number(request.getPassportNumber())
                                                        .series(request.getPassportSeries())
                                                        .build())
                                      .build();

        Client response = service.getClientFromLoanApplicationRequestDTO(request);

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isEqualTo(expectedClient),
                () -> assertThat(response).extracting(Client::getEmail)
                                          .isEqualTo(expectedClient.getEmail()),
                () -> assertThat(response).extracting(Client::getFirstName)
                                          .isEqualTo(expectedClient.getFirstName()),
                () -> assertThat(response).extracting(Client::getLastName)
                                          .isEqualTo(expectedClient.getLastName()),
                () -> assertThat(response).extracting(Client::getMiddleName)
                                          .isEqualTo(expectedClient.getMiddleName())
        );
    }

    @Test
    void updateClientFromFinishRegistrationRequestDTO_shouldUpdateClient() {
        EmploymentDTO employmentDto = EmploymentDTO.builder()
                                                   .status(EmploymentStatus.EMPLOYED)
                                                   .employerInn("1234")
                                                   .salary(BigDecimal.ONE)
                                                   .position(Position.WORKER)
                                                   .workExperienceTotal(12)
                                                   .workExperienceCurrent(6)
                                                   .build();
        FinishRegistrationRequestDTO request =
                FinishRegistrationRequestDTO.builder()
                                            .gender(Gender.FEMALE)
                                            .maritalStatus(MaritalStatus.SINGLE)
                                            .passportIssueDate(LocalDate.of(2000, 1, 1))
                                            .passportIssueBranch("test")
                                            .employment(employmentDto)
                                            .account("test")
                                            .build();
        Client expectedClient = Client.builder()
                                      .gender(request.getGender())
                                      .maritalStatus(request.getMaritalStatus())
                                      .passport(Passport.builder()
                                                        .issueBranch(request.getPassportIssueBranch())
                                                        .issueDate(request.getPassportIssueDate())
                                                        .build())
                                      .employment(Employment.builder()
                                                            .status(employmentDto.getStatus())
                                                            .employerInn(employmentDto.getEmployerInn())
                                                            .salary(employmentDto.getSalary())
                                                            .position(employmentDto.getPosition())
                                                            .workExperienceTotal(employmentDto.getWorkExperienceTotal())
                                                            .workExperienceCurrent(employmentDto.getWorkExperienceCurrent())
                                                            .build())
                                      .build();
        Client client = Client.builder()
                              .build();

        service.updateClientFromFinishRegistrationRequestDTO(client, request);

        assertAll(
                () -> assertThat(client).extracting(Client::getGender)
                                        .isEqualTo(expectedClient.getGender()),
                () -> assertThat(client).extracting(Client::getMaritalStatus)
                                        .isEqualTo(expectedClient.getMaritalStatus()),
                () -> assertThat(client).extracting(Client::getEmployment)
                                        .isEqualTo(expectedClient.getEmployment()),
                () -> assertThat(client).extracting(Client::getPassport)
                                        .extracting(Passport::getIssueBranch)
                                        .isEqualTo(expectedClient.getPassport()
                                                                 .getIssueBranch()),
                () -> assertThat(client).extracting(Client::getPassport)
                                        .extracting(Passport::getIssueDate)
                                        .isEqualTo(expectedClient.getPassport()
                                                                 .getIssueDate())
        );
    }
}