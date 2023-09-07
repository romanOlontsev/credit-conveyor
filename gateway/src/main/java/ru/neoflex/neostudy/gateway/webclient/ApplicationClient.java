package ru.neoflex.neostudy.gateway.webclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.neostudy.gateway.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.gateway.model.dto.LoanOfferDTO;

import java.util.List;

@FeignClient(value = "application", url = "${application.client.base-url}", configuration = CustomErrorDecoder.class)
public interface ApplicationClient {

    @PostMapping(value = "",
            produces = {"application/json"},
            consumes = {"application/json"})
    List<LoanOfferDTO> createApplication(@RequestBody LoanApplicationRequestDTO request);

    @PutMapping(value = "/offer",
            consumes = {"application/json"})
    void applyOffer(@RequestBody LoanOfferDTO request);
}
