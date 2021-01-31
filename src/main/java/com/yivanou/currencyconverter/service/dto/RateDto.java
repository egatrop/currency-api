package com.yivanou.currencyconverter.service.dto;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class RateDto {

    public String from;
    public String to;
    public BigDecimal rate;

    public RateDto(String from, String to, BigDecimal rate) {
        if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to) || rate == null || BigDecimal.ZERO.compareTo(rate) == 0) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RateDto rateDto = (RateDto) o;
        return from.equals(rateDto.from) && to.equals(rateDto.to) && rate.compareTo(rateDto.rate) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, rate);
    }
}
