package ru.neoflex.neostudy.dossier.model.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ThymeleafAttribute {
    private String title;
    private String message;
    private String buttonName;
    private String url;
    private boolean isHiddenButton;
}
