package ru.neoflex.neosudy.deal.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.neoflex.neosudy.deal.model.entity.Employment;
import ru.neoflex.neosudy.deal.model.entity.Passport;
import ru.neoflex.neosudy.deal.model.types.Gender;
import ru.neoflex.neosudy.deal.model.types.MaritalStatus;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ClientDto {
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;
    @JsonProperty(value = "middle_name")
    private String middleName;
    @JsonProperty(value = "birth_date")
    private LocalDate birthDate;
    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "gender")
    private Gender gender;
    @JsonProperty(value = "marital_status")
    private MaritalStatus maritalStatus;
    @JsonProperty(value = "dependent_amount")
    private Integer dependentAmount;
    @JsonProperty(value = "passport")
    private Passport passport;
    @JsonProperty(value = "employment")
    private Employment employment;
    @JsonProperty(value = "account")
    private String account;
}
