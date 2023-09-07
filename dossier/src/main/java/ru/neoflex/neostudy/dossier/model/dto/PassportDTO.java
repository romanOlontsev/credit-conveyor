package ru.neoflex.neostudy.dossier.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
