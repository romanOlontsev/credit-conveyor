package ru.neoflex.neostudy.dossier.model.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MaritalStatus {
    MARRIED("Married"),
    DIVORCED("Divorced"),
    SINGLE("Single"),
    WIDOW_WIDOWER("Widow/Widower");

    private final String text;
}
