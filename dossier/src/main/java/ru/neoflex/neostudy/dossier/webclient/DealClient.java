package ru.neoflex.neostudy.dossier.webclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import ru.neoflex.neostudy.dossier.model.dto.ApplicationDTO;

@FeignClient(value = "deal", url = "${deal.client.base-url}")
public interface DealClient {

    @PutMapping(value = "/application/{applicationId}/status",
            produces = "application/json")
    ApplicationDTO updateApplicationStatus(@PathVariable("applicationId") Long applicationId);
}
