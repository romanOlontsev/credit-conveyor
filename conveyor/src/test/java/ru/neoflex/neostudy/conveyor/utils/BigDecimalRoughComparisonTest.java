package ru.neoflex.neostudy.conveyor.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BigDecimalRoughComparisonTest {

    @Test
    void check_shouldReturnTrue() {
        BigDecimal first = BigDecimal.valueOf(155);
        BigDecimal second = BigDecimal.valueOf(155);
        BigDecimal third = BigDecimal.valueOf(200);
        BigDecimal fourth = BigDecimal.valueOf(120);
        assertTrue(BigDecimalRoughComparison.check(first, BigDecimalRoughComparison.Operator.EQUALS, second));
        assertTrue(BigDecimalRoughComparison.check(first, BigDecimalRoughComparison.Operator.LESS_THAN, third));
        assertTrue(BigDecimalRoughComparison.check(first, BigDecimalRoughComparison.Operator.LESS_THAN_OR_EQUALS, second));
        assertTrue(BigDecimalRoughComparison.check(first, BigDecimalRoughComparison.Operator.GREATER_THAN, fourth));
        assertTrue(BigDecimalRoughComparison.check(second, BigDecimalRoughComparison.Operator.GREATER_THAN_OR_EQUALS, fourth));
    }
}