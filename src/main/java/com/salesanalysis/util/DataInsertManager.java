package com.salesanalysis.util;

import com.salesanalysis.mapper.SalesMapper;
import com.salesanalysis.model.Sales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class DataInsertManager {
    private final SalesMapper salesMapper;
    private static final int BATCH_SIZE = 100; // 每批次插入的记录数
    
    @Autowired
    public DataInsertManager(SalesMapper salesMapper) {
        this.salesMapper = salesMapper;
    }
    
    /**
     * 批量插入销售数据
     * @param salesList 销售数据列表
     * @return 插入成功的记录数
     */
    @Transactional
    public int insertSalesDataBatch(List<Sales> salesList) {
        if (salesList == null || salesList.isEmpty()) {
            System.out.println("没有数据需要插入");
            return 0;
        }
        
        int totalInserted = 0;
        int totalSize = salesList.size();
        
        System.out.println("开始批量插入" + totalSize + "条销售数据...");
        
        // 分批插入，避免单次插入过多数据导致数据库压力过大
        for (int i = 0; i < totalSize; i += BATCH_SIZE) {
            int endIndex = Math.min(i + BATCH_SIZE, totalSize);
            List<Sales> batchList = salesList.subList(i, endIndex);
            
            int inserted = salesMapper.batchInsert(batchList);
            totalInserted += inserted;
            
            System.out.println("已插入第" + (i + 1) + "-" + endIndex + "条数据，当前进度：" + endIndex + "/" + totalSize);
        }
        
        System.out.println("批量插入完成，共成功插入" + totalInserted + "条数据");
        return totalInserted;
    }
    
    /**
     * 单独插入一条销售数据（用于测试）
     * @param sales 销售数据
     * @return 插入是否成功
     */
    public boolean insertSalesData(Sales sales) {
        int result = salesMapper.insert(sales);
        return result > 0;
    }
    
    /**
     * 获取数据库中销售记录的总数
     * @return 销售记录总数
     */
    public int getSalesCount() {
        List<Sales> allSales = salesMapper.findAll();
        return allSales != null ? allSales.size() : 0;
    }
}