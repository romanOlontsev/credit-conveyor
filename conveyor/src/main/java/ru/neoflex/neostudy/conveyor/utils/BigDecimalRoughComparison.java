package ru.neoflex.neostudy.conveyor.utils;

import java.math.BigDecimal;

public class BigDecimalRoughComparison {
    public static boolean check(BigDecimal firstNum, Operator operator, BigDecimal secondNum) {
        return switch (operator) {
            case EQUALS -> firstNum.compareTo(secondNum) == 0;
            case LESS_THAN -> firstNum.compareTo(secondNum) < 0;
            case LESS_THAN_OR_EQUALS -> firstNum.compareTo(secondNum) <= 0;
            case GREATER_THAN -> firstNum.compareTo(secondNum) > 0;
            case GREATER_THAN_OR_EQUALS -> firstNum.compareTo(secondNum) >= 0;
        };
    }

    public enum Operator {
        LESS_THAN,
        LESS_THAN_OR_EQUALS,
        GREATER_THAN,
        GREATER_THAN_OR_EQUALS,
        EQUALS
    }
}
