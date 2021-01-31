package com.yivanou.currencyconverter.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CurrencyDataSourceTest {

    @Autowired
    private CurrencyDataSource source;

    @Test
    @Timeout(10)
    public void currencyDataSourceShouldBeInitialized() {
        assertThat(source.getCurrencyToRates()).isNotEmpty();
    }
}
