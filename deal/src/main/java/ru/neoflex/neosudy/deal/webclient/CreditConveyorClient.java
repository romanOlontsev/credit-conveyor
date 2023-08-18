package ru.neoflex.neosudy.deal.webclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.neosudy.deal.model.dto.CreditDTO;
import ru.neoflex.neosudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neosudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neosudy.deal.model.dto.ScoringDataDTO;

import java.util.List;

@FeignClient(value = "conveyor", url = "${conveyor.client.base-url}")
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
