package ru.neoflex.neostudy.conveyor.test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Main {
    public static void main(String[] args) {
        System.out.println(calculateMonthlyPayment(BigDecimal.valueOf(10), 24, BigDecimal.valueOf(100000)));
    }
    public static BigDecimal calculateMonthlyPayment(BigDecimal rate, Integer term, BigDecimal amount) {
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                                     .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        System.out.println(monthlyRate);
        BigDecimal annuityRatio = monthlyRate
                .multiply(((monthlyRate.add(BigDecimal.ONE)).pow(term))
                        .divide((monthlyRate.add(BigDecimal.ONE)).pow(term)
                                                                 .subtract(BigDecimal.ONE), 10,
                                RoundingMode.HALF_UP));
        System.out.println(annuityRatio);
        return annuityRatio.multiply(amount)
                           .setScale(2, RoundingMode.HALF_UP);
    }
}
