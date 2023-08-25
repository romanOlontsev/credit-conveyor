package ru.neoflex.neosudy.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.neoflex.neosudy.deal.model.dto.FinishRegistrationRequestDTO;
import ru.neoflex.neosudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neosudy.deal.model.dto.ScoringDataDTO;
import ru.neoflex.neosudy.deal.model.entity.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    @Mapping(source = "passportSeries", target = "passport.series")
    @Mapping(source = "passportNumber", target = "passport.number")
    Client loanApplicationRequestToClient(LoanApplicationRequestDTO loanApplicationRequestDTO);

    @Mapping(source = "passportIssueBranch", target = "passport.issueBranch")
    @Mapping(source = "passportIssueDate", target = "passport.issueDate")
    void updateClient(@MappingTarget Client client, FinishRegistrationRequestDTO finishRegistrationRequestDTO);

    @Mapping(source = "passport.series", target = "passportSeries")
    @Mapping(source = "passport.number", target = "passportNumber")
    @Mapping(source = "passport.issueBranch", target = "passportIssueBranch")
    @Mapping(source = "passport.issueDate", target = "passportIssueDate")
    @Mapping(source = "employment.status", target = "employment.status")
    ScoringDataDTO clientToScoringData(Client client);
}
