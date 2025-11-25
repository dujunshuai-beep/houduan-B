package com.salesanalysis.util;

import com.salesanalysis.model.Sales;
import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockarooDataGenerator {
    private final MockarooClient mockarooClient;
    private final SimpleDateFormat dateFormat;
    
    public MockarooDataGenerator(String apiKey) {
        this.mockarooClient = new MockarooClient(apiKey);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
    }
    
    /**
     * 生成指定数量的销售数据
     * @param count 需要生成的数据条数
     * @return List<Sales> 销售数据列表
     * @throws Exception 可能的异常
     */
    public List<Sales> generateSalesList(int count) throws Exception {
        List<Sales> salesList = new ArrayList<>(count);
        
        // 从Mockaroo获取数据
        JSONArray dataArray = mockarooClient.generateSalesData(count);
        
        // 转换JSON数据为Sales对象
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject jsonObject = dataArray.getJSONObject(i);
            Sales sales = new Sales();
            
            // 设置销售日期
            String saleDateStr = jsonObject.getString("sale_date");
            // 处理日期格式 "2025-05-15T14:22:17+00:00"
            Date saleDate = dateFormat.parse(saleDateStr);
            sales.setSaleDate(saleDate);
            
            // 设置产品名称
            sales.setProduct(jsonObject.getString("product"));
            
            // 设置销售区域
            sales.setRegion(jsonObject.getString("region"));
            
            // 设置销售金额
            double amountDouble = jsonObject.getDouble("amount");
            BigDecimal amount = BigDecimal.valueOf(amountDouble);
            sales.setAmount(amount.setScale(2, BigDecimal.ROUND_HALF_UP));
            
            salesList.add(sales);
        }
        
        return salesList;
    }
    
    /**
     * 生成1000条测试数据
     * @return List<Sales> 包含1000条销售数据的列表
     * @throws Exception 可能的异常
     */
    public List<Sales> generate1000TestData() throws Exception {
        System.out.println("开始从Mockaroo生成1000条测试数据...");
        List<Sales> salesList = generateSalesList(1000);
        System.out.println("成功生成1000条测试数据");
        return salesList;
    }
}