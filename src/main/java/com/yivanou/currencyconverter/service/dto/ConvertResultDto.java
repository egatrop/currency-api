package com.yivanou.currencyconverter.service.dto;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class ConvertResultDto {

    public String from;
    public String to;
    public BigDecimal originalAmount;
    public BigDecimal convertedAmount;

    public ConvertResultDto(String from, String to, BigDecimal originalAmount, BigDecimal convertedAmount) {
        if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to)
                || originalAmount == null || BigDecimal.ZERO.compareTo(originalAmount) == 0
                || convertedAmount == null || BigDecimal.ZERO.compareTo(convertedAmount) == 0) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        this.from = from;
        this.to = to;
        this.originalAmount = originalAmount;
        this.convertedAmount = convertedAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConvertResultDto that = (ConvertResultDto) o;
        return from.equals(that.from) && to.equals(that.to) && originalAmount.compareTo(that.originalAmount) == 0 && convertedAmount.compareTo(that.convertedAmount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, originalAmount, convertedAmount);
    }
}
