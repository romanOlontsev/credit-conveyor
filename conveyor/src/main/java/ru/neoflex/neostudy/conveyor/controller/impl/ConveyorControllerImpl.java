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
        if (accept != null && accept.contains("application/json")) {
            List<LoanOfferDTO> offers = service.generateOffers(prescoringRequest);
            return new ResponseEntity<>(offers, HttpStatus.OK);
        }
        throw new BadRequestException("Bad request");
    }

    @Override
    public ResponseEntity<CreditDTO> calculateCredit(ScoringDataDTO scoringRequest) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            return new ResponseEntity<>(CreditDTO.builder()
                                                 .term(12)
                                                 .build(), HttpStatus.OK);
        }
        throw new BadRequestException("Bad request");
    }
}
