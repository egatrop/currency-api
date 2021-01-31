package com.yivanou.currencyconverter.service;

import com.yivanou.currencyconverter.data.CurrencyDataSource;
import com.yivanou.currencyconverter.service.dto.ChartLinkDto;
import com.yivanou.currencyconverter.service.dto.ConvertResultDto;
import com.yivanou.currencyconverter.service.dto.RateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CurrencyConverterServiceConfiguration.class})
public class CurrencyConverterServiceTest {

    @MockBean
    private CurrencyDataSource dataSource;

    @MockBean
    private CurrencyConverter converter;

    @MockBean
    private ChartLinksService linksService;

    @InjectMocks
    @Resource
    private CurrencyConverterService service;

    @BeforeEach
    public void setUp() {
        when(dataSource.getCurrencyToRates()).thenReturn(new HashMap<String, BigDecimal>() {{
            put("USD", BigDecimal.valueOf(1));
            put("GBP", BigDecimal.valueOf(2));
            put("IDR", BigDecimal.valueOf(3));
            put("RUR", BigDecimal.valueOf(0));
        }});

        when(dataSource.getValidCurrencies()).thenReturn(new HashSet<String>() {{
            add("USD");
            add("GBP");
            add("IDR");
            add("EUR");
        }});

        Mockito.reset(service);
    }

    @Test
    public void shouldReturnReferenceRate() {
        final RateDto dto = new RateDto("EUR", "USD", BigDecimal.valueOf(1));
        assertThat(service.getReferenceRate("USD")).isEqualTo(dto);
        verify(service).incrementCounter("USD");
    }

    @Test
    public void shouldFailIfReferenceRateNotFound() {
        final Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getReferenceRate("XXX"));
        assertThat(exception).hasMessage("Currency XXX not found");
    }

    @Test
    public void shouldFailIfReferenceRateIsZero() {
        final Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getReferenceRate("RUR"));
        assertThat(exception).hasMessage("Currency RUR not found");
        verify(service, times(0)).incrementCounter("RUR");
    }

    @Test
    public void shouldReturnExchangeRate() {
        when(converter.calculateExchangeRate("USD", "GBP")).thenReturn(BigDecimal.valueOf(2));

        final RateDto dto = new RateDto("USD", "GBP", BigDecimal.valueOf(2));
        assertThat(service.getExchangeRate("USD", "GBP")).isEqualTo(dto);
        verify(service).incrementCounter("USD");
        verify(service).incrementCounter("GBP");
    }

    @Test
    public void shouldFailIfCurrencyNotFound() {
        final Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getExchangeRate("RUR", "USD"));
        assertThat(exception).hasMessage("Currencies not found");
        verify(service, times(0)).incrementCounter("RUR");
        verify(service, times(0)).incrementCounter("USD");
    }

    @Test
    public void shouldConvert() {
        when(converter.convert(new BigDecimal("4"), "IDR", "EUR"))
                .thenReturn(new BigDecimal("1.333333"));

        final ConvertResultDto dto = new ConvertResultDto("IDR", "EUR", new BigDecimal("4"), new BigDecimal("1.333333"));
        assertThat(service.convert(new BigDecimal("4"), "IDR", "EUR")).isEqualTo(dto);
        verify(service, times(0)).incrementCounter("IDR");
        verify(service, times(0)).incrementCounter("EUR");
    }

    @Test
    public void shouldReturnProperChartLink() {
        when(linksService.getLink("USD", "EUR")).thenReturn("https://www.xe.com/currencycharts/?from=USD&to=EUR");
        final ChartLinkDto dto = new ChartLinkDto("USD", "EUR", "https://www.xe.com/currencycharts/?from=USD&to=EUR");

        assertThat(service.getChartLink("USD", "EUR")).isEqualTo(dto);
        verify(service, times(0)).incrementCounter("EUR");
        verify(service, times(0)).incrementCounter("USD");
    }
}

@TestConfiguration
class CurrencyConverterServiceConfiguration {
    @Bean
    @Primary
    public CurrencyConverterService getService(CurrencyConverterService service) {
        return Mockito.spy(service);
    }
}
