package com.yivanou.currencyconverter.service;

import com.yivanou.currencyconverter.core.CurrencyConverterHelper;
import com.yivanou.currencyconverter.data.CurrencyDataSource;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CurrencyConverter.class, CurrencyConverterHelper.class, CurrencyDataSource.class})
public class CurrencyConverterTest {

    @MockBean
    private CurrencyConverterHelper converterHelper;

    @MockBean
    private CurrencyDataSource dataSource;

    @InjectMocks
    @Autowired
    private CurrencyConverter converter;

    @Test
    public void shouldReturnExchangeRate() {
        when(dataSource.getCurrencyToRates()).thenReturn(new HashMap<String, BigDecimal>() {{
            put("GBP", new BigDecimal("0.88383"));
            put("IDR", new BigDecimal("16994.16"));
        }});
        when(converterHelper.calculateRatio(new BigDecimal("0.88383"), new BigDecimal("16994.16"))).thenReturn(new BigDecimal("19227.86056142"));

        assertThat(converter.calculateExchangeRate("GBP", "IDR")).isEqualTo(new BigDecimal("19227.86056142"));
    }

    @Test
    public void shouldConvertFromEuroToAnotherCurrency() {
        final BigDecimal originAmount = BigDecimal.valueOf(150);
        when(dataSource.getCurrencyToRates()).thenReturn(new HashMap<String, BigDecimal>() {{
            put("USD", new BigDecimal("1.2136"));
        }});
        when(converterHelper.convert(BigDecimal.valueOf(150), new BigDecimal("1.2136"))).thenReturn(new BigDecimal("182.04"));

        assertThat(converter.convert(originAmount, "EUR", "USD")).isEqualTo(new BigDecimal("182.04"));
    }

    @Test
    public void shouldFailWhenRatioIsZeroAndOneCurrencyIsEuro() {
        when(dataSource.getCurrencyToRates()).thenReturn(new HashMap<String, BigDecimal>() {{
            put("USD", new BigDecimal("0.00"));
        }});

        final BigDecimal originAmount = BigDecimal.valueOf(150);
        Exception exception = assertThrows(IllegalStateException.class, () -> converter.convert(originAmount, "EUR", "USD"));
        assertThat(exception).hasMessage("Ratio for EUR and USD is wrong: 0.00");

        exception = assertThrows(IllegalStateException.class, () -> converter.convert(originAmount, "USD", "EUR"));
        assertThat(exception).hasMessage("Ratio for EUR and USD is wrong: 0.00");
    }


    @Test
    public void shouldConvertFromAnyCurrencyToEuro() {
        final BigDecimal originAmount = BigDecimal.valueOf(150);
        when(dataSource.getCurrencyToRates()).thenReturn(new HashMap<String, BigDecimal>() {{
            put("USD", new BigDecimal("1.2136"));
        }});
        when(converterHelper.inverseRatio(new BigDecimal("1.2136"))).thenReturn(new BigDecimal("0.823994"));
        when(converterHelper.convert(originAmount, new BigDecimal("0.823994"))).thenReturn(new BigDecimal("123.599208"));

        assertThat(converter.convert(originAmount, "USD", "EUR")).isEqualTo(new BigDecimal("123.599208"));
    }

    @Test
    public void shouldConvertFromAnyCurrencyToAnyCurrency() {
        final BigDecimal originAmount = BigDecimal.valueOf(150);
        when(dataSource.getCurrencyToRates()).thenReturn(new HashMap<String, BigDecimal>() {{
            put("GBP", new BigDecimal("0.88383"));
            put("IDR", new BigDecimal("16994.16"));
        }});
        when(converterHelper.calculateRatio(new BigDecimal("0.88383"), new BigDecimal("16994.16"))).thenReturn(new BigDecimal("19227.86056142"));
        when(converterHelper.convert(originAmount, new BigDecimal("19227.86056142"))).thenReturn(new BigDecimal("288417.9084213"));

        assertThat(converter.convert(originAmount, "GBP", "IDR")).isEqualTo(new BigDecimal("288417.9084213"));
    }

    @Test
    public void shouldFailWhenOneOfRatiosIsZeroAndNoEuro() {
        when(dataSource.getCurrencyToRates()).thenReturn(new HashMap<String, BigDecimal>() {{
            put("USD", new BigDecimal("0.00"));
            put("IRD", new BigDecimal("1.23"));
        }});

        final BigDecimal originAmount = BigDecimal.valueOf(150);
        Exception exception = assertThrows(IllegalStateException.class, () -> converter.convert(originAmount, "USD", "IRD"));
        assertThat(exception).hasMessage("Ratio for USD and IRD is wrong");
    }
}
