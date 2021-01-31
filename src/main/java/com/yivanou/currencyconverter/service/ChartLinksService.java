package com.yivanou.currencyconverter.service;

import com.yivanou.currencyconverter.config.AppProperties;
import com.yivanou.currencyconverter.data.CurrencyDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

@Component
public class ChartLinksService {

    @Autowired
    private AppProperties properties;

    @Autowired
    private CurrencyDataSource dataSource;

    public String getLink(String fromCurrency, String toCurrency) {
        Set<String> currencies = dataSource.getValidCurrencies();
        if (currencies.containsAll(Arrays.asList(fromCurrency, toCurrency))) {
            return properties.getXeUrl().replace("<CUR_FROM>", fromCurrency).replace("<CUR_TO>", toCurrency);
        } else {
            throw new IllegalArgumentException("Invalid currency");
        }
    }
}
