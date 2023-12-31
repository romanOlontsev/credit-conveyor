package ru.neoflex.neostudy.conveyor.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
    @Test
    void check_shouldReturnFalse() {
        BigDecimal first = BigDecimal.valueOf(155);
        BigDecimal second = BigDecimal.valueOf(155);
        BigDecimal third = BigDecimal.valueOf(200);
        BigDecimal fourth = BigDecimal.valueOf(120);
        assertFalse(BigDecimalRoughComparison.check(first, BigDecimalRoughComparison.Operator.EQUALS, third));
        assertFalse(BigDecimalRoughComparison.check(first, BigDecimalRoughComparison.Operator.LESS_THAN, second));
        assertFalse(BigDecimalRoughComparison.check(second, BigDecimalRoughComparison.Operator.LESS_THAN_OR_EQUALS, fourth));
        assertFalse(BigDecimalRoughComparison.check(second, BigDecimalRoughComparison.Operator.GREATER_THAN, third));
        assertFalse(BigDecimalRoughComparison.check(fourth, BigDecimalRoughComparison.Operator.GREATER_THAN_OR_EQUALS, second));
    }
}