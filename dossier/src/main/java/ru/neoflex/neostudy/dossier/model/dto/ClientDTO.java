package ru.neoflex.neostudy.dossier.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.*;
import ru.neoflex.neostudy.dossier.model.types.Gender;
import ru.neoflex.neostudy.dossier.model.types.MaritalStatus;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ClientDTO {
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;
    @JsonProperty(value = "middle_name")
    private String middleName;
    @JsonDeserialize(using = LocalDateDeserializer.class)
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
    private PassportDTO passport;
    @JsonProperty(value = "employment")
    private EmploymentDTO employment;
    @JsonProperty(value = "account")
    private String account;
}
