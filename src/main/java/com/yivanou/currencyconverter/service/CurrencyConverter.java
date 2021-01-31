package com.yivanou.currencyconverter.service;

import com.yivanou.currencyconverter.core.CurrencyConverterHelper;
import com.yivanou.currencyconverter.data.CurrencyDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CurrencyConverter {

    public static final String BASE_CURRENCY = "EUR";

    @Autowired
    private CurrencyDataSource dataSource;

    @Autowired
    private CurrencyConverterHelper helper;

    public BigDecimal convert(BigDecimal originalAmount, String from, String to) {
        return helper.convert(originalAmount, calculateExchangeRate(from, to));
    }

    public BigDecimal calculateExchangeRate(String from, String to) {
        if (BASE_CURRENCY.equals(from)) {
            final BigDecimal referenceRatio = dataSource.getCurrencyToRates().get(to);
            if (referenceRatio != null && referenceRatio.compareTo(BigDecimal.ZERO) != 0) {
                return referenceRatio;
            } else {
                throw new IllegalStateException("Ratio for " + from + " and " + to + " is wrong: " + referenceRatio);
            }
        } else if (BASE_CURRENCY.equals(to)) {
            final BigDecimal referenceRatio = dataSource.getCurrencyToRates().get(from);
            if (referenceRatio != null && referenceRatio.compareTo(BigDecimal.ZERO) != 0) {
                return helper.inverseRatio(referenceRatio);
            } else {
                throw new IllegalStateException("Ratio for " + to + " and " + from + " is wrong: " + referenceRatio);
            }
        } else {
            final BigDecimal ratioFrom = dataSource.getCurrencyToRates().get(from);
            final BigDecimal ratioTo = dataSource.getCurrencyToRates().get(to);

            if (ratioFrom != null
                    && ratioTo != null
                    && ratioFrom.compareTo(BigDecimal.ZERO) != 0
                    && ratioTo.compareTo(BigDecimal.ZERO) != 0)
            {
                return helper.calculateRatio(ratioFrom, ratioTo);
            } else {
                throw new IllegalStateException("Ratio for " + from + " and " + to + " is wrong");
            }
        }
    }
}
