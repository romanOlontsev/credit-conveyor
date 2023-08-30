package ru.neoflex.neostudy.deal.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class PassportDTO {
    @JsonProperty(value = "series")
    private String series;
    @JsonProperty(value = "number")
    private String number;
    @JsonProperty(value = "issue_branch")
    private String issueBranch;
    @JsonProperty(value = "issued_date")
    private LocalDate issueDate;
}
