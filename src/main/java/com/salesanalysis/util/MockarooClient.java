package com.salesanalysis.util;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MockarooClient {
    private static final String MOCKAROO_API_URL = "https://api.mockaroo.com/api/generate.json";
    private final String apiKey;
    
    public MockarooClient(String apiKey) {
        this.apiKey = apiKey;
    }
    
    /**
     * 从Mockaroo API获取销售数据
     * @param count 需要生成的数据条数
     * @return JSONArray 包含生成的销售数据
     * @throws Exception 可能的异常
     */
    public JSONArray generateSalesData(int count) throws Exception {
        // 构建API请求URL
        StringBuilder urlBuilder = new StringBuilder(MOCKAROO_API_URL);
        urlBuilder.append("?key=").append(apiKey);
        urlBuilder.append("&count=").append(count);
        
        // 添加字段定义 - 使用JSON数组格式发送请求体
        JSONArray fields = new JSONArray();
        
        // 销售日期字段
        JSONObject dateField = new JSONObject();
        dateField.put("name", "sale_date");
        dateField.put("type", "Datetime");
        fields.put(dateField);
        
        // 产品名称字段
        JSONObject productField = new JSONObject();
        productField.put("name", "product");
        productField.put("type", "Custom List");
        productField.put("values", new JSONArray(new String[]{
            "产品A", "产品B", "产品C", "产品D", "产品E", 
            "产品F", "产品G", "产品H", "产品I", "产品J"
        }));
        fields.put(productField);
        
        // 销售区域字段
        JSONObject regionField = new JSONObject();
        regionField.put("name", "region");
        regionField.put("type", "Custom List");
        regionField.put("values", new JSONArray(new String[]{
            "华东", "华北", "华南", "华中", "西南", "西北", "东北"
        }));
        fields.put(regionField);
        
        // 销售金额字段
        JSONObject amountField = new JSONObject();
        amountField.put("name", "amount");
        amountField.put("type", "Number");
        amountField.put("min", 100);
        amountField.put("max", 10000);
        amountField.put("decimals", 2);
        fields.put(amountField);
        
        // 创建HTTP连接
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        
        // 发送请求体
        String jsonString = fields.toString();
        conn.getOutputStream().write(jsonString.getBytes(StandardCharsets.UTF_8));
        
        // 检查响应状态
        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(
                conn.getErrorStream(), StandardCharsets.UTF_8));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            conn.disconnect();
            throw new RuntimeException("Failed : HTTP error code : " + responseCode + ", Response: " + errorResponse.toString());
        }
        
        // 读取响应内容
        BufferedReader br = new BufferedReader(new InputStreamReader(
            (conn.getInputStream()), StandardCharsets.UTF_8));
        
        StringBuilder response = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            response.append(output);
        }
        
        conn.disconnect();
        
        // 解析JSON响应
        return new JSONArray(response.toString());
    }
}