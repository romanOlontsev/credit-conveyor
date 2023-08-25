package ru.neoflex.neosudy.deal.model.response;

import lombok.*;
import ru.neoflex.neosudy.deal.model.types.Theme;

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
