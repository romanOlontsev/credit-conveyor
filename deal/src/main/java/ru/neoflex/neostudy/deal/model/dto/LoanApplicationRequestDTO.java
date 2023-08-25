package ru.neoflex.neostudy.deal.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoanApplicationRequestDTO {
    @JsonProperty(value = "amount")
    private BigDecimal amount;
    @JsonProperty(value = "term")
    private Integer term;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;
    @JsonProperty(value = "middle_name")
    private String middleName;
    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "birth_date")
    private LocalDate birthDate;
    @JsonProperty(value = "passport_series")
    private String passportSeries;
    @JsonProperty(value = "passport_number")
    private String passportNumber;
}
