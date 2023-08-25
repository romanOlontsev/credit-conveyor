package ru.neoflex.neostudy.deal.webclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.neostudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.deal.model.dto.ScoringDataDTO;
import ru.neoflex.neostudy.deal.model.dto.CreditDTO;
import ru.neoflex.neostudy.deal.model.dto.LoanApplicationRequestDTO;

import java.util.List;

@FeignClient(value = "conveyor", url = "${conveyor.client.base-url}", configuration = CustomErrorDecoder.class)
public interface CreditConveyorClient {

    @PostMapping(value = "/offers",
            produces = {"application/json"},
            consumes = {"application/json"})
    List<LoanOfferDTO> calculateOffers(@RequestBody LoanApplicationRequestDTO prescoringRequest);

    @PostMapping(value = "/calculation",
            produces = {"application/json"},
            consumes = {"application/json"})
    CreditDTO calculateCredit(@RequestBody ScoringDataDTO scoringRequest);
}
