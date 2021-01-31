package com.yivanou.currencyconverter.service.dto;

import org.springframework.util.StringUtils;

import java.util.Objects;

public class ChartLinkDto {

    public String from;
    public String to;
    public String link;

    public ChartLinkDto(String from, String to, String link) {
        if (StringUtils.isEmpty(from) || StringUtils.isEmpty(to) || StringUtils.isEmpty(link)) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        this.from = from;
        this.to = to;
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChartLinkDto that = (ChartLinkDto) o;
        return Objects.equals(from, that.from) && Objects.equals(link, that.link) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, link, to);
    }
}
