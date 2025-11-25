package com.salesanalysis.model;

import java.math.BigDecimal;
import java.util.Date;

public class Sales {
    private Integer id;
    private Date saleDate;
    private String product;
    private String region;
    private BigDecimal amount;

    // getter and setter methods
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Sales{" +
                "id=" + id +
                ", saleDate=" + saleDate +
                ", product='" + product + '\'' +
                ", region='" + region + '\'' +
                ", amount=" + amount +
                '}';
    }
}
