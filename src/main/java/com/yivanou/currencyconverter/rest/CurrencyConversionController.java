package com.yivanou.currencyconverter.rest;

import com.yivanou.currencyconverter.service.CurrencyConverterService;
import com.yivanou.currencyconverter.service.dto.ChartLinkDto;
import com.yivanou.currencyconverter.service.dto.ConvertResultDto;
import com.yivanou.currencyconverter.service.dto.RateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/currencies")
public class CurrencyConversionController {

    private final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

    @Autowired
    private CurrencyConverterService service;

    @GetMapping
    public Map<String, Long> getStatistic() {
        return service.getCounters();
    }

    @GetMapping("reference-rate")
    public RateDto getReferenceRate(@RequestParam(name = "cur") String currency) {
        return service.getReferenceRate(currency);
    }

    @GetMapping("exchange-rate")
    public RateDto getExchangeRate(
            @RequestParam(name = "from") String fromCurrency,
            @RequestParam(name = "to") String toCurrency
    ) {
        return service.getExchangeRate(fromCurrency, toCurrency);
    }

    @GetMapping("chart")
    public ChartLinkDto getChartLink(
            @RequestParam(name = "from") String fromCurrency,
            @RequestParam(name = "to") String toCurrency
    ) {
        return service.getChartLink(fromCurrency, toCurrency);
    }

    @GetMapping("convert")
    public ConvertResultDto convert(
            @RequestParam(name = "from") String fromCurrency,
            @RequestParam(name = "to") String toCurrency,
            @RequestParam(name = "amount") BigDecimal amount
    ) {
        return service.convert(amount, fromCurrency, toCurrency);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleException(Throwable ex, WebRequest request) {
        logger.error("Application error: " + request.getDescription(false), ex);

        Object errMsg = new HashMap<String, String>(){{ put("error_message", ex.getMessage()); }};

        if (ex instanceof IllegalArgumentException || ex instanceof IllegalStateException) {
            return new ResponseEntity<>(errMsg, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
