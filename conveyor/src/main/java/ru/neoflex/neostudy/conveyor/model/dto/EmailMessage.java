package ru.neoflex.neostudy.conveyor.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EmailMessage {
    private String address;
    private Enum theme;
    private Long applicationId;
}
