package com.yivanou.currencyconverter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring")
public class AppProperties {

    private String ecbUrl;
    private String xeUrl;

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
