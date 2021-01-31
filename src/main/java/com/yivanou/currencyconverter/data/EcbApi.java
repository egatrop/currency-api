package com.yivanou.currencyconverter.data;

import com.yivanou.currencyconverter.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class EcbApi {

    private static final Logger logger = LoggerFactory.getLogger(EcbApi.class);

    @Autowired
    private AppProperties properties;

    @Autowired
    private RestTemplate restTemplate;

    public String getHtmlPageAsString() {
        final String url = properties.getEcbUrl();

        try {
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientException ex) {
            logger.error("Unable to get data from ECB website", ex);
        }

        return "";
    }
}
