package ru.neoflex.neostudy.dossier.model.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Position {
    WORKER("Worker"),
    MIDDLE_MANAGER("Middle manager"),
    TOP_MANAGER("Top manager"),
    OWNER("Owner");

    private final String text;
}
