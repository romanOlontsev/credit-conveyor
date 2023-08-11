package ru.neoflex.neosudy.deal.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ru.neoflex.neosudy.deal.annotation.UserAgeConstraint;
import ru.neoflex.neosudy.deal.annotation.UserNameConstraint;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class LoanApplicationRequestDTO {
    @NotNull
    @DecimalMin(value = "10000", message = "The amount must be greater than or equal to 10000")
    @JsonProperty(value = "amount")
    private BigDecimal amount;

    @NotNull
    @DecimalMin(value = "6", message = "The term must be greater than or equal to 6")
    @JsonProperty(value = "term")
    private Integer term;

    @UserNameConstraint(message = "The firstname must contain from 2 to 30 latin characters")
    @JsonProperty(value = "first_name")
    private String firstName;

    @UserNameConstraint(message = "The lastname must contain from 2 to 30 latin characters")
    @JsonProperty(value = "last_name")
    private String lastName;

    @UserNameConstraint(required = false,
            message = "The middle name must be null or contain from 2 to 30 latin characters")
    @JsonProperty(value = "middle_name")
    private String middleName;

    @NotNull
    @Pattern(regexp = "^[\\w\\.]{2,50}@[\\w\\.]{2,20}$", message = "Email must be: example@dog.con")
    @JsonProperty(value = "email")
    private String email;

    @UserAgeConstraint(message = "User must be over 18 years of age")
    @JsonProperty(value = "birth_date")
    private LocalDate birthDate;

    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must contain 4 numbers")
    @JsonProperty(value = "passport_series")
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "^\\d{6}$", message = "Passport number must contain 6 numbers")
    @JsonProperty(value = "passport_number")
    private String passportNumber;
}
