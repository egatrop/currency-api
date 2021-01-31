package com.yivanou.currencyconverter.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataInitializingJob {

    private final Logger logger = LoggerFactory.getLogger(DataInitializingJob.class);

    @Autowired
    private CurrencyDataSource dataSource;

    @Scheduled(cron = "0 0 0 * * *")
    public void runJob() {
        logger.info("Run scheduled data initialization...");
        dataSource.init();
    }
}
