package ru.neoflex.neostudy.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.neoflex.neostudy.deal.model.dto.ApplicationDTO;
import ru.neoflex.neostudy.deal.model.entity.Application;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(source = "clientId.firstName", target = "client.firstName")
    @Mapping(source = "clientId.lastName", target = "client.lastName")
    @Mapping(source = "clientId.middleName", target = "client.middleName")
    @Mapping(source = "clientId.birthDate", target = "client.birthDate")
    @Mapping(source = "clientId.email", target = "client.email")
    @Mapping(source = "clientId.gender", target = "client.gender")
    @Mapping(source = "clientId.maritalStatus", target = "client.maritalStatus")
    @Mapping(source = "clientId.dependentAmount", target = "client.dependentAmount")
    @Mapping(source = "clientId.account", target = "client.account")
    @Mapping(source = "clientId.passport.series", target = "client.passport.series")
    @Mapping(source = "clientId.passport.number", target = "client.passport.number")
    @Mapping(source = "clientId.passport.issueBranch", target = "client.passport.issueBranch")
    @Mapping(source = "clientId.passport.issueDate", target = "client.passport.issueDate")
    @Mapping(source = "clientId.employment.status", target = "client.employment.status")
    @Mapping(source = "clientId.employment.employerInn", target = "client.employment.employerInn")
    @Mapping(source = "clientId.employment.salary", target = "client.employment.salary")
    @Mapping(source = "clientId.employment.position", target = "client.employment.position")
    @Mapping(source = "clientId.employment.workExperienceTotal", target = "client.employment.workExperienceTotal")
    @Mapping(source = "clientId.employment.workExperienceCurrent", target = "client.employment.workExperienceCurrent")
    @Mapping(source = "creditId.amount", target = "credit.amount")
    @Mapping(source = "creditId.term", target = "credit.term")
    @Mapping(source = "creditId.monthlyPayment", target = "credit.monthlyPayment")
    @Mapping(source = "creditId.rate", target = "credit.rate")
    @Mapping(source = "creditId.psk", target = "credit.psk")
    @Mapping(source = "creditId.insuranceEnable", target = "credit.isInsuranceEnabled")
    @Mapping(source = "creditId.salaryClient", target = "credit.isSalaryClient")
    @Mapping(source = "creditId.paymentSchedule", target = "credit.paymentSchedule")
    ApplicationDTO applicationToApplicationDTO(Application application);

    List<ApplicationDTO> applicationListToApplicationDTOList(List<Application> applications);
}
