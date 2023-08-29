package ru.neoflex.neostudy.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.neostudy.deal.model.dto.FinishRegistrationRequestDTO;
import ru.neoflex.neostudy.deal.model.dto.LoanApplicationRequestDTO;
import ru.neoflex.neostudy.deal.model.dto.LoanOfferDTO;
import ru.neoflex.neostudy.deal.model.response.ApiErrorResponse;
import ru.neoflex.neostudy.deal.model.dto.ApplicationDTO;

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
    @ResponseBody
    @PostMapping(value = "/application",
            produces = {"application/json"},
            consumes = {"application/json"})
    List<LoanOfferDTO> calculateOffers(
            @Parameter(in = ParameterIn.DEFAULT,
                    required = true)
            @RequestBody LoanApplicationRequestDTO request
    );

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
    void selectOffer(
            @Parameter(in = ParameterIn.DEFAULT,
                    required = true)
            @RequestBody LoanOfferDTO request
    );

    @Operation(summary = "Successful registration")
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
    @PutMapping(value = "/calculate/{applicationId}",
            consumes = {"application/json"})
    void finishRegistration(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable String applicationId,
            @Parameter(in = ParameterIn.DEFAULT,
                    required = true)
            @RequestBody FinishRegistrationRequestDTO request);

    @Operation(summary = "Send documents to the client")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Documents sent successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping(value = "/document/{applicationId}/send")
    void sendDocuments(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable String applicationId);

    @Operation(summary = "Sign documents")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Documents signed successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping(value = "/document/{applicationId}/sign")
    void signDocuments(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable String applicationId);

    @Operation(summary = "Verify Ses code")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ses cod verified successfully"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping(value = "/document/{applicationId}/code")
    void verifySesCode(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable String applicationId);

    @Operation(summary = "Update application status")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ApplicationDTO.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Application not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PutMapping(value = "admin/application/{applicationId}/status",
            produces = "application/json")
    ApplicationDTO updateApplicationStatus(
            @Parameter(in = ParameterIn.PATH,
                    required = true)
            @PathVariable String applicationId);


    @GetMapping(value = "/test/{id}")
    void test(@PathVariable Long id);
}
