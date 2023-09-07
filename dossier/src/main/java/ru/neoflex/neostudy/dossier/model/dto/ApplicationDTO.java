package ru.neoflex.neostudy.dossier.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ApplicationDTO {

    @JsonProperty(value = "client")
    private ClientDTO client;

    @JsonProperty(value = "credit")
    private CreditDTO credit;
}
