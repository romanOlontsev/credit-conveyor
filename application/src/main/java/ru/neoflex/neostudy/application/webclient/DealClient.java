package ru.neoflex.neostudy.application.webclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.neostudy.application.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.application.model.dto.LoanOfferDTO;

import java.util.List;

@FeignClient(value = "deal", url = "${deal.client.base-url}")
public interface DealClient {

    @PostMapping(value = "/application",
            produces = {"application/json"},
            consumes = {"application/json"})
    List<LoanOfferDTO> calculateOffers(@RequestBody LoanApplicationRequestDTO request);

    @PutMapping(value = "/offer",
            consumes = {"application/json"})
    void selectOffer(@RequestBody LoanOfferDTO request);
}
