package ru.neoflex.neostudy.conveyor.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.neostudy.conveyor.controller.ConveyorController;
import ru.neoflex.neostudy.conveyor.model.dto.CreditDTO;
import ru.neoflex.neostudy.conveyor.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.conveyor.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.conveyor.model.dto.ScoringDataDTO;
import ru.neoflex.neostudy.conveyor.service.ConveyorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConveyorControllerImpl implements ConveyorController {
    private final ConveyorService service;

    @Override
    public List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO prescoringRequest) {
        return service.generateOffers(prescoringRequest);
    }

    @Override
    public CreditDTO calculateCredit(ScoringDataDTO scoringRequest) {
        return service.calculateCredit(scoringRequest);
    }
}