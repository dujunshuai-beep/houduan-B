package com.salesanalysis.model;

import java.math.BigDecimal;

public class SalesSummary {
    private String date;
    private String product;
    private String region;
    private BigDecimal totalAmount;
    private Integer count;

    // getter and setter methods
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "SalesSummary{" +
                "date='" + date + '\'' +
                ", product='" + product + '\'' +
                ", region='" + region + '\'' +
                ", totalAmount=" + totalAmount +
                ", count=" + count +
                '}';
    }
}
