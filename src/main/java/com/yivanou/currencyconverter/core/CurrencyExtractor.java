package com.yivanou.currencyconverter.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class CurrencyExtractor {

    private final Logger logger = LoggerFactory.getLogger(CurrencyExtractor.class);

    public Map<String, BigDecimal> extract(final String src) {
        return Jsoup.parse(src).select("table.forextable tbody tr").stream()
                .filter(containsEnoughData())
                .collect(Collectors.toMap(this::extractCurrencyName, this::extractCurrencyRate));
    }

    private BigDecimal extractCurrencyRate(Element tr) {
        final String value = tr.selectFirst("span.rate").text();
        try {
            return new BigDecimal(value).setScale(CurrencyConverterHelper.SCALE, CurrencyConverterHelper.ROUNDING_MODE);
        } catch (NumberFormatException ex) {
            logger.error("Unable to parse rate out of: " + value, ex);
        }

        return BigDecimal.ZERO;
    }

    private String extractCurrencyName(Element tr) {
        return tr.selectFirst(".currency").id();
    }

    private Predicate<Element> containsEnoughData() {
        return tr -> tr.selectFirst("td.currency") != null && tr.selectFirst("span.rate") != null;
    }
}
