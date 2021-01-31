package com.yivanou.currencyconverter.core;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CurrencyConverterHelper {

    public static final int SCALE = 6;
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    public BigDecimal inverseRatio(BigDecimal from) {
        return new BigDecimal(1).divide(from, SCALE, ROUNDING_MODE);
    }

    /**
     * Given EUR/GBP and EUR/IRD calculates GBP/IRD
     *
     * @param ratio1 first ratio
     * @param ratio2 second ratio
     * @return a result of division the second ratio by the first one
     */
    public BigDecimal calculateRatio(BigDecimal ratio1, BigDecimal ratio2) {
        return ratio2.divide(ratio1, SCALE, ROUNDING_MODE);
    }

    public BigDecimal convert(BigDecimal originalAmount, BigDecimal ratio) {
        return originalAmount.multiply(ratio).setScale(SCALE, ROUNDING_MODE);
    }
}
