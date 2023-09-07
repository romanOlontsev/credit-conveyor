package ru.neoflex.neostudy.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.neoflex.neostudy.gateway.model.dto.FinishRegistrationRequestDTO;
import ru.neoflex.neostudy.gateway.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.gateway.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.gateway.model.response.ApiErrorResponse;

import java.util.List;

public interface GatewayController {
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
    @PostMapping(value = "/application",
            produces = {"application/json"},
            consumes = {"application/json"})
    List<LoanOfferDTO> createApplication(
            @Parameter(in = ParameterIn.DEFAULT,
                    required = true)
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
    @PostMapping(value = "/application/apply",
            consumes = {"application/json"})
    void applyOffer(
            @Parameter(in = ParameterIn.DEFAULT,
                    required = true)
            @RequestBody LoanOfferDTO request);


    @Operation(summary = "Finish registration")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registration completed successfully"),
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
    @PostMapping(value = "/application/registration/{applicationId}",
            consumes = {"application/json"})
    void finishRegistration(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable Long applicationId,
            @Parameter(in = ParameterIn.DEFAULT,
                    required = true)
            @RequestBody FinishRegistrationRequestDTO request);

    @Operation(summary = "Request to send documents")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Request for sending documents has been sent"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(value = "/document/{applicationId}")
    void createDocumentsRequest(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable Long applicationId);

    @Operation(summary = "Request to sign documents")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Request for signing documents has been sent"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(value = "/document/{applicationId}/sign")
    void signDocumentsRequest(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable Long applicationId);

    @Operation(summary = "Request to verify ses code")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Request for verifying ses code has been sent"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(value = "/document/{applicationId}/sign/code")
    void verifySesCodeRequest(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable Long applicationId);

}
