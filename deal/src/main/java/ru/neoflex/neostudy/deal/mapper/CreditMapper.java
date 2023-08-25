package ru.neoflex.neostudy.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.neoflex.neostudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.deal.model.dto.ScoringDataDTO;
import ru.neoflex.neostudy.deal.model.dto.CreditDTO;
import ru.neoflex.neostudy.deal.model.entity.Credit;

@Mapper(componentModel = "spring")
public interface CreditMapper {
    @Mapping(source = "requestedAmount", target = "amount")
    @Mapping(source = "isInsuranceEnabled", target = "insuranceEnable")
    @Mapping(source = "isSalaryClient", target = "salaryClient")
    Credit loanOfferToCredit(LoanOfferDTO loanOfferDTO);

    @Mapping(source = "insuranceEnable", target = "isInsuranceEnabled")
    @Mapping(source = "salaryClient", target = "isSalaryClient")
    void updateScoringData(@MappingTarget ScoringDataDTO scoringData, Credit credit);

    @Mapping(target = "creditStatus", constant = "CALCULATED")
    void updateCredit(@MappingTarget Credit credit, CreditDTO creditDTO);
}
