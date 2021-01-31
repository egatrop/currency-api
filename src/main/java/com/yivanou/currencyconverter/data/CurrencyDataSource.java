package com.yivanou.currencyconverter.data;

import com.yivanou.currencyconverter.core.CurrencyExtractor;
import com.yivanou.currencyconverter.service.CurrencyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CurrencyDataSource {

    private final Logger logger = LoggerFactory.getLogger(CurrencyDataSource.class);

    @Autowired
    private EcbApi api;

    @Autowired
    private CurrencyExtractor extractor;

    private final Map<String, BigDecimal> currencyToRates = new HashMap<>();
    private final Set<String> validCurrencies = new HashSet<>();

    public void init() {
        logger.info("Initializing data...");

        final String html = api.getHtmlPageAsString();
        currencyToRates.clear();
        currencyToRates.putAll(extractor.extract(html));
        currencyToRates.put(CurrencyConverter.BASE_CURRENCY, BigDecimal.ONE);

        validCurrencies.clear();
        validCurrencies.addAll(
                currencyToRates.entrySet().stream()
                        .filter(e -> e.getValue().compareTo(BigDecimal.ZERO) != 0)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet())
        );
        validCurrencies.add(CurrencyConverter.BASE_CURRENCY);

        logger.info("Data was initialized");
    }

    public Map<String, BigDecimal> getCurrencyToRates() {
        return new HashMap<>(currencyToRates);
    }

    public Set<String> getValidCurrencies() {
        return new HashSet<>(validCurrencies);
    }
}
