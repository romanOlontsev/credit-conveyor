package ru.neoflex.neosudy.deal.model.jsonb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Passport implements Serializable {
    @JsonProperty(value = "passport_id")
    private Long passportId;
    @JsonProperty(value = "series")
    private String series;
    @JsonProperty(value = "number")
    private String number;
    @JsonProperty(value = "issue_branch")
    private String issueBranch;
    @JsonProperty(value = "issue_date")
    private OffsetDateTime issueDate;
}
