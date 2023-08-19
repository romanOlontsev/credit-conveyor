package ru.neoflex.neostudy.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.neostudy.application.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.application.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.application.webclient.DealClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final DealClient client;

    public List<LoanOfferDTO> createApplication(LoanApplicationRequestDTO prescoringRequest) {
        return client.calculateOffers(prescoringRequest);
    }

    public void applyOffer(LoanOfferDTO request) {
        client.selectOffer(request);
    }
}
