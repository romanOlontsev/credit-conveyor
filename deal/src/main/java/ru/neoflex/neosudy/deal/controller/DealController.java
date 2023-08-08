package ru.neoflex.neosudy.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.neoflex.neosudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neosudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neosudy.deal.model.response.ApiErrorResponse;

import java.util.List;

@Validated
@RequestMapping("/deal")
public interface DealController {
    @Operation(summary = "Calculation of possible credit conditions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Calculation of possible credit conditions is successful",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(
                                            implementation = LoanOfferDTO.class)))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))})
    @PostMapping(value = "/application",
            produces = {"application/json"},
            consumes = {"application/json"})
    ResponseEntity<List<LoanOfferDTO>> generateOffers(
            @Parameter(in = ParameterIn.DEFAULT,
                    required = true)
            @Valid
            @RequestBody LoanApplicationRequestDTO prescoringRequest
    );
}
