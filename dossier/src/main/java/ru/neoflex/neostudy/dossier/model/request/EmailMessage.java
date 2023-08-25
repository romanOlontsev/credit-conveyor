package ru.neoflex.neostudy.dossier.model.request;

import lombok.*;
import ru.neoflex.neostudy.dossier.model.types.Theme;

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
