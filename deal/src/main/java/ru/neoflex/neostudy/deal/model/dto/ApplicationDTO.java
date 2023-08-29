package ru.neoflex.neostudy.deal.model.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ApplicationDTO {
    private ClientDTO client;
    private PassportDTO passport;
    private EmploymentDTO employment;
    private CreditDTO credit;
}
