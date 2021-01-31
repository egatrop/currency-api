package com.yivanou.currencyconverter.core;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyConverterHelperTest {

    private final CurrencyConverterHelper converterHelper = new CurrencyConverterHelper();

    @Test
    public void shouldReturnMultiplicativeInverse() {
        final BigDecimal r = new BigDecimal("35714.285714286");
        assertThat(converterHelper.inverseRatio(r)).isEqualTo(new BigDecimal("0.000028").setScale(6, RoundingMode.HALF_EVEN));
    }

    @Test
    public void shouldReturnNewRatio() {
        final BigDecimal r1 = new BigDecimal("16994.16").setScale(6, RoundingMode.HALF_EVEN);
        final BigDecimal r2 = new BigDecimal("0.88383").setScale(6, RoundingMode.HALF_EVEN);
        assertThat(converterHelper.calculateRatio(r1, r2)).isEqualTo(new BigDecimal("0.000052").setScale(6, RoundingMode.HALF_EVEN));
    }

    @Test
    public void shouldConvert() {
        final BigDecimal originAmount = BigDecimal.valueOf(150);
        final BigDecimal ratio = new BigDecimal("0.000052").setScale(6, RoundingMode.HALF_EVEN);
        assertThat(converterHelper.convert(originAmount, ratio)).isEqualTo(new BigDecimal("0.0078").setScale(6, RoundingMode.HALF_EVEN));
    }
}
