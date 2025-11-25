package com.salesanalysis.service;

import com.salesanalysis.model.Sales;
import com.salesanalysis.model.SalesSummary;

import java.util.List;

public interface SalesService {
    // 查询所有销售记录
    List<Sales> findAllSales();

    // 根据ID查询销售记录
    Sales findSalesById(Integer id);

    // 新增销售记录
    Sales saveSales(Sales sales);

    // 更新销售记录
    Sales updateSales(Sales sales);

    // 删除销售记录
    boolean deleteSales(Integer id);

    // 聚合查询销售数据
    List<SalesSummary> aggregateSales(String date, String product, String region);

    // 批量导入销售数据
    boolean batchImportSales(List<Sales> salesList);
}
