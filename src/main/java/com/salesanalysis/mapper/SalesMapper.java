package com.salesanalysis.mapper;

import com.salesanalysis.model.Sales;
import com.salesanalysis.model.SalesSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesMapper {
    // 查询所有销售记录
    List<Sales> findAll();

    // 根据ID查询销售记录
    Sales findById(Integer id);

    // 新增销售记录
    int insert(Sales sales);

    // 更新销售记录
    int update(Sales sales);

    // 删除销售记录
    int delete(Integer id);

    // 聚合查询销售数据
    List<SalesSummary> aggregateSales(
            @Param("date") String date,
            @Param("product") String product,
            @Param("region") String region);

    // 批量插入销售数据
    int batchInsert(@Param("salesList") List<Sales> salesList);
}
