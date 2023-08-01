package ru.neoflex.neostudy.conveyor.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.neostudy.conveyor.controller.ConveyorController;
import ru.neoflex.neostudy.conveyor.exception.BadRequestException;
import ru.neoflex.neostudy.conveyor.model.dto.CreditDTO;
import ru.neoflex.neostudy.conveyor.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.conveyor.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.conveyor.model.dto.ScoringDataDTO;
import ru.neoflex.neostudy.conveyor.service.ConveyorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConveyorControllerImpl implements ConveyorController {
    private final HttpServletRequest request;
    private final ConveyorService service;

    @Override
    public ResponseEntity<List<LoanOfferDTO>> generateOffers(LoanApplicationRequestDTO prescoringRequest) {
        String accept = request.getHeader("Accept");
        if (accept != null) {
            List<LoanOfferDTO> offers = service.generateOffers(prescoringRequest);
            return new ResponseEntity<>(offers, HttpStatus.OK);
        }
        throw new BadRequestException("Bad request: Accept=" + accept);
    }

    @Override
    public ResponseEntity<CreditDTO> calculateCredit(ScoringDataDTO scoringRequest) {
        String accept = request.getHeader("Accept");
        if (accept != null) {
            CreditDTO credit = service.calculateCredit(scoringRequest);
            return new ResponseEntity<>(credit, HttpStatus.OK);
        }
        throw new BadRequestException("Bad request: Accept=" + accept);
    }
}
