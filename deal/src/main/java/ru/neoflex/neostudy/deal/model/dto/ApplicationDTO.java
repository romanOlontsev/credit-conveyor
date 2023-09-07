package ru.neoflex.neostudy.deal.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.neoflex.neostudy.deal.model.jsonb.StatusHistory;
import ru.neoflex.neostudy.deal.model.types.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ApplicationDTO {
    @JsonProperty(value = "application_id")
    private Long applicationId;

    @JsonProperty(value = "client")
    private ClientDTO client;

    @JsonProperty(value = "credit")
    private CreditDTO credit;

    @JsonProperty(value = "status")
    private ApplicationStatus status;

    @JsonProperty(value = "creation_date")
    private LocalDateTime creationDate;

    @JsonProperty(value = "applied_offer")
    private LoanOfferDTO appliedOffer;

    @JsonProperty(value = "sign_date")
    private LocalDateTime signDate;

    @JsonProperty(value = "ses_code")
    private String sesCode;

    @JsonProperty(value = "status_history")
    private List<StatusHistory> statusHistory;
}
