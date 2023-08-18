package ru.neoflex.neostudy.conveyor.model.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.pl.NIP;
import ru.neoflex.neostudy.conveyor.annotation.UserNameConstraint;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LoanApplicationRequestDTO {
    @NotNull
    @DecimalMin(value = "10000", message = "The amount must be greater than or equal to 10000")
    private BigDecimal amount;

    @NotNull
    @DecimalMin(value = "6", message = "The term must be greater than or equal to 6")
    private Integer term;

    @UserNameConstraint(message = "The firstname must contain from 2 to 30 latin characters")
    private String firstName;

    @UserNameConstraint(message = "The lastname must contain from 2 to 30 latin characters")
    private String lastName;

    @UserNameConstraint(required = true,
            message = "The middle name must be empty or contain from 2 to 30 Latin characters")
    private String middleName;

    @NotNull
    @Pattern(regexp = "[\\w\\.]{2,50}@[\\w\\.]{2,20}", message = "Email must be: example@dog.con")
    private String email;

    private LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must contain 4 numbers")
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "^\\d{6}$", message = "Passport number must contain 6 numbers")
    private String passportNumber;
}
