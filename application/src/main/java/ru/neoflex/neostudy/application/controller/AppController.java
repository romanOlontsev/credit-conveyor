package ru.neoflex.neostudy.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.neostudy.application.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.application.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.application.model.response.ApiErrorResponse;

import java.util.List;

@Validated
@RequestMapping("/application")
public interface AppController {
    @Operation(summary = "Create application")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Create application is successful",
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
    @ResponseBody
    @PostMapping(value = "",
            produces = {"application/json"},
            consumes = {"application/json"})
    List<LoanOfferDTO> createApplication(
            @Parameter(in = ParameterIn.DEFAULT,
                    required = true)
            @Valid
            @RequestBody LoanApplicationRequestDTO prescoringRequest);

    @Operation(summary = "Selecting one of the offers")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Offer selection was successful"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping(value = "/offer",
            consumes = {"application/json"})
    void applyOffer(
            @Parameter(in = ParameterIn.DEFAULT,
                    required = true)
            @RequestBody LoanOfferDTO request);
}
