package ru.neoflex.neostudy.conveyor.controller.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.neostudy.conveyor.controller.ConveyorController;
import ru.neoflex.neostudy.conveyor.exception.BadRequestException;
import ru.neoflex.neostudy.conveyor.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.conveyor.model.dto.LoanOfferDTO;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConveyorControllerImpl implements ConveyorController {
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<List<LoanOfferDTO>> calculatePrescoring(LoanApplicationRequestDTO prescoringRequest) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            List<LoanOfferDTO> response = List.of(
                    LoanOfferDTO.builder()
                                .applicationId(1L)
                                .requestedAmount(BigDecimal.ONE)
                                .totalAmount(BigDecimal.ONE)
                                .term(1)
                                .monthlyPayment(BigDecimal.ONE)
                                .rate(BigDecimal.TEN)
                                .isInsuranceEnabled(false)
                                .isSalaryClient(true)
                                .build()
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new BadRequestException("Bad request");
    }
}
