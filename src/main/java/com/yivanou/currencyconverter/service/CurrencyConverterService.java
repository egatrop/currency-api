package com.yivanou.currencyconverter.service;

import com.yivanou.currencyconverter.data.CurrencyDataSource;
import com.yivanou.currencyconverter.service.dto.ChartLinkDto;
import com.yivanou.currencyconverter.service.dto.ConvertResultDto;
import com.yivanou.currencyconverter.service.dto.RateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

@Service
public class CurrencyConverterService {

    @Autowired
    private CurrencyDataSource dataSource;

    @Autowired
    private CurrencyConverter converter;

    @Autowired
    private ChartLinksService linksService;

    private final Map<String, LongAdder> counters = new ConcurrentHashMap<>();

    public RateDto getReferenceRate(String currency) {
        if (dataSource.getValidCurrencies().contains(currency)) {
            incrementCounter(currency);
            return new RateDto("EUR", currency, dataSource.getCurrencyToRates().get(currency));
        }
        throw new IllegalArgumentException("Currency " + currency + " not found");
    }

    public RateDto getExchangeRate(String fromCurrency, String toCurrency) {
        if (dataSource.getValidCurrencies().containsAll(Arrays.asList(fromCurrency, toCurrency))) {
            incrementCounter(fromCurrency);
            incrementCounter(toCurrency);
            return new RateDto(fromCurrency, toCurrency, converter.calculateExchangeRate(fromCurrency, toCurrency));
        }
        throw new IllegalArgumentException("Currencies not found");
    }

    void incrementCounter(String currency) {
        counters.computeIfAbsent(currency, k -> new LongAdder()).increment();
    }

    public ConvertResultDto convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (dataSource.getValidCurrencies().containsAll(Arrays.asList(fromCurrency, toCurrency))) {
            return new ConvertResultDto(fromCurrency, toCurrency, amount, converter.convert(amount, fromCurrency, toCurrency));
        }
        throw new IllegalArgumentException("Currencies not found");
    }

    public ChartLinkDto getChartLink(String fromCurrency, String toCurrency) {
        return new ChartLinkDto(fromCurrency, toCurrency, linksService.getLink(fromCurrency, toCurrency));
    }

    public Map<String, Long> getCounters() {
        return dataSource.getValidCurrencies().stream()
                .collect(Collectors.toMap(
                        c -> c,
                        c -> counters.get(c) != null ? counters.get(c).longValue() : 0L)
                );
    }
}
