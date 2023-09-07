package ru.neoflex.neostudy.gateway.webclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.neostudy.gateway.model.dto.FinishRegistrationRequestDTO;

@FeignClient(value = "deal", url = "${deal.client.base-url}", configuration = CustomErrorDecoder.class)
public interface DealClient {

    @PutMapping(value = "/calculate/{applicationId}",
            consumes = {"application/json"})
    void finishRegistration(@PathVariable(value = "applicationId") Long applicationId, @RequestBody FinishRegistrationRequestDTO request);

    @PutMapping(value = "/document/{applicationId}/send")
    void sendDocuments(@PathVariable(value = "applicationId") Long applicationId);

    @PutMapping(value = "/document/{applicationId}/sign")
    void signDocuments(@PathVariable(value = "applicationId") Long applicationId);

    @PutMapping(value = "/document/{applicationId}/code")
    void verifySesCode(@PathVariable(value = "applicationId") Long applicationId);
}
