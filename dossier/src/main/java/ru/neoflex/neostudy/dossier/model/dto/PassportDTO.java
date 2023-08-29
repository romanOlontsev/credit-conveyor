package ru.neoflex.neostudy.dossier.model.dto;

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
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}
