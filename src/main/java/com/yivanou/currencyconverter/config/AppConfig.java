package com.yivanou.currencyconverter.config;

import com.yivanou.currencyconverter.data.CurrencyDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean(initMethod = "init")
    public CurrencyDataSource getCurrencyDataSource() {
        return new CurrencyDataSource();
    }
}
