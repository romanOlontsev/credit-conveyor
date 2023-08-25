package ru.neoflex.neostudy.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.application.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.application.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.application.webclient.DealClient;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final DealClient client;

    public List<LoanOfferDTO> createApplication(LoanApplicationRequestDTO prescoringRequest) {
        log.info("Input application: {}", prescoringRequest);
        List<LoanOfferDTO> offers = client.calculateOffers(prescoringRequest);
        log.info("Response offers: {}", offers);
        return offers;
    }

    public void applyOffer(LoanOfferDTO request) {
        client.selectOffer(request);
        log.info("Offer selected: {}", request);
    }
}
