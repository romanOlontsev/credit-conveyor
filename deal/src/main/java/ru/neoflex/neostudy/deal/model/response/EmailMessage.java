package ru.neoflex.neostudy.deal.model.response;

import lombok.*;
import ru.neoflex.neostudy.deal.model.types.Theme;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EmailMessage {
    private String address;
    private Theme theme;
    private Long applicationId;
}
