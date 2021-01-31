package com.yivanou.currencyconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "spring")
public class AppProperties {

    private Set<String> currencies;
    private String ecbUrl;
    private String xeUrl;

    public void setCurrencies(Set<String> currencies) {
        this.currencies = currencies;
    }

    public void setEcbUrl(String ecbUrl) {
        this.ecbUrl = ecbUrl;
    }

    public String getEcbUrl() {
        return ecbUrl;
    }

    public String getXeUrl() {
        return xeUrl;
    }

    public void setXeUrl(String xeUrl) {
        this.xeUrl = xeUrl;
    }
}
