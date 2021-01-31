package com.yivanou.currencyconverter.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyExtractorTest {

    private final CurrencyExtractor extractor = new CurrencyExtractor();

    private static String HTML;

    @BeforeAll
    public static void setUp() throws IOException {
        HTML = new String(Files.readAllBytes(
                Paths.get("src/test/resources/data/index.html")));
    }

    @Test
    public void shouldExtractCurrencyDataFromHtmlString() {
        Map<String, BigDecimal> currencyToRate = extractor.extract(HTML);
        assertThat(currencyToRate).isNotEmpty();
        assertThat(currencyToRate).hasSize(32);
        assertThat(currencyToRate.get("USD")).isEqualTo(new BigDecimal("1.2136").setScale(6, RoundingMode.HALF_EVEN));
        assertThat(currencyToRate.get("JPY")).isEqualTo(new BigDecimal("127.05").setScale(6, RoundingMode.HALF_EVEN));
        assertThat(currencyToRate.get("CAD")).isEqualTo(BigDecimal.ZERO);
    }
}
