package com.salesanalysis.service.impl;

import com.salesanalysis.mapper.SalesMapper;
import com.salesanalysis.model.Sales;
import com.salesanalysis.model.SalesSummary;
import com.salesanalysis.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SalesServiceImpl implements SalesService {

    private final SalesMapper salesMapper;

    @Autowired
    public SalesServiceImpl(SalesMapper salesMapper) {
        this.salesMapper = salesMapper;
    }

    @Override
    public List<Sales> findAllSales() {
        return salesMapper.findAll();
    }

    @Override
    public Sales findSalesById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return salesMapper.findById(id);
    }

    @Override
    @Transactional
    public Sales saveSales(Sales sales) {
        validateSales(sales);
        salesMapper.insert(sales);
        return sales;
    }

    @Override
    @Transactional
    public Sales updateSales(Sales sales) {
        if (sales.getId() == null) {
            throw new IllegalArgumentException("Sales ID cannot be null for update");
        }
        validateSales(sales);
        
        // 检查记录是否存在
        Sales existingSales = salesMapper.findById(sales.getId());
        if (existingSales == null) {
            throw new RuntimeException("Sales record not found with id: " + sales.getId());
        }
        
        salesMapper.update(sales);
        return sales;
    }

    @Override
    @Transactional
    public boolean deleteSales(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        
        // 检查记录是否存在
        Sales existingSales = salesMapper.findById(id);
        if (existingSales == null) {
            return false;
        }
        
        int result = salesMapper.delete(id);
        return result > 0;
    }

    @Override
    public List<SalesSummary> aggregateSales(String date, String product, String region) {
        return salesMapper.aggregateSales(date, product, region);
    }

    @Override
    @Transactional
    public boolean batchImportSales(List<Sales> salesList) {
        if (salesList == null || salesList.isEmpty()) {
            throw new IllegalArgumentException("Sales list cannot be empty");
        }
        
        // 验证所有销售记录
        for (Sales sales : salesList) {
            validateSales(sales);
        }
        
        int result = salesMapper.batchInsert(salesList);
        return result == salesList.size();
    }

    /**
     * 验证销售记录的有效性
     */
    private void validateSales(Sales sales) {
        if (sales == null) {
            throw new IllegalArgumentException("Sales cannot be null");
        }
        if (sales.getSaleDate() == null) {
            throw new IllegalArgumentException("Sale date cannot be null");
        }
        if (sales.getProduct() == null || sales.getProduct().trim().isEmpty()) {
            throw new IllegalArgumentException("Product cannot be null or empty");
        }
        if (sales.getRegion() == null || sales.getRegion().trim().isEmpty()) {
            throw new IllegalArgumentException("Region cannot be null or empty");
        }
        if (sales.getAmount() == null || sales.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
