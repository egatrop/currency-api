package com.yivanou.currencyconverter.service;

import com.yivanou.currencyconverter.config.AppProperties;
import com.yivanou.currencyconverter.data.CurrencyDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {ChartLinksService.class, AppProperties.class, CurrencyDataSource.class})
public class ChartLinksServiceTest {

    @MockBean
    private AppProperties properties;

    @MockBean
    private CurrencyDataSource dataSource;

    @Autowired
    @InjectMocks
    private ChartLinksService service;

    @BeforeEach()
    public void setUp() {
        when(properties.getXeUrl()).thenReturn("https://www.xe.com/currencycharts/?from=<CUR_FROM>&to=<CUR_TO>");
        when(dataSource.getValidCurrencies()).thenReturn(new HashSet<>(Arrays.asList("USD", "EUR")));
    }

    @Test
    public void shouldReturnProperLink() {
        final String fromCurrency = "USD";
        final String toCurrency = "EUR";
        final String link = service.getLink(fromCurrency, toCurrency);
        assertThat(link).isEqualTo("https://www.xe.com/currencycharts/?from=USD&to=EUR");
    }

    @Test
    public void shouldFailForUnknownCurrency() {
        final String fromCurrency = "USD";
        final String toCurrency = "XXX";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.getLink(fromCurrency, toCurrency));
        assertThat(exception).hasMessage("Invalid currency");
    }
}
