package ru.neoflex.neostudy.dossier.model.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmploymentStatus {
    UNEMPLOYED("Unemployed"),
    SELF_EMPLOYED("Self employed"),
    EMPLOYED("Employed"),
    BUSINESS_OWNER("Business owner");

    private final String text;
}
