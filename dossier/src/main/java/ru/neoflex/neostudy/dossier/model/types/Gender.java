package ru.neoflex.neostudy.dossier.model.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    NON_BINARY("Non binary");

    private final String text;
}
