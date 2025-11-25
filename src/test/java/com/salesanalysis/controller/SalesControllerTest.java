package com.salesanalysis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesanalysis.model.Sales;
import com.salesanalysis.model.SalesSummary;
import com.salesanalysis.service.SalesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
class SalesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalesService salesService;

    @Autowired
    private ObjectMapper objectMapper;

    private Sales sales;
    private SalesSummary salesSummary;

    @BeforeEach
    void setUp() {
        // 创建测试数据
        sales = new Sales();
        sales.setId(1);
        sales.setSaleDate(new Date());
        sales.setProduct("产品A");
        sales.setRegion("华东");
        sales.setAmount(new BigDecimal(1299.00));

        // 创建销售汇总数据
        salesSummary = new SalesSummary();
        salesSummary.setDate("2023-01-01");
        salesSummary.setProduct("产品A");
        salesSummary.setRegion("华东");
        salesSummary.setTotalAmount(new BigDecimal(1299.00));
        salesSummary.setCount(1);
    }

    @Test
    void testGetAllSales() throws Exception {
        // 准备模拟数据
        List<Sales> salesList = Arrays.asList(sales);
        when(salesService.findAllSales()).thenReturn(salesList);

        // 执行测试
        mockMvc.perform(get("/api/sales"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].product").value("产品A"));

        // 验证服务方法被调用
        verify(salesService, times(1)).findAllSales();
    }

    @Test
    void testGetSalesById() throws Exception {
        // 准备模拟数据
        when(salesService.findSalesById(1)).thenReturn(sales);

        // 执行测试
        mockMvc.perform(get("/api/sales/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.product").value("产品A"));

        // 验证服务方法被调用
        verify(salesService, times(1)).findSalesById(1);
    }

    @Test
    void testGetSalesById_NotFound() throws Exception {
        // 准备模拟数据 - 返回null表示找不到
        when(salesService.findSalesById(100)).thenReturn(null);

        // 执行测试
        mockMvc.perform(get("/api/sales/100"))
                .andExpect(status().isNotFound());

        // 验证服务方法被调用
        verify(salesService, times(1)).findSalesById(100);
    }

    @Test
    void testCreateSales() throws Exception {
        // 准备模拟数据
        when(salesService.saveSales(any(Sales.class))).thenReturn(sales);

        // 执行测试
        mockMvc.perform(post("/api/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sales)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.product").value("产品A"));

        // 验证服务方法被调用
        verify(salesService, times(1)).saveSales(any(Sales.class));
    }

    @Test
    void testUpdateSales() throws Exception {
        // 准备模拟数据
        Sales updatedSales = new Sales();
        updatedSales.setId(1);
        updatedSales.setSaleDate(new Date());
        updatedSales.setProduct("产品B");
        updatedSales.setRegion("华北");
        updatedSales.setAmount(new BigDecimal(1599.00));

        when(salesService.updateSales(any(Sales.class))).thenReturn(updatedSales);

        // 执行测试
        mockMvc.perform(put("/api/sales/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedSales)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.product").value("产品B"));

        // 验证服务方法被调用
        verify(salesService, times(1)).updateSales(any(Sales.class));
    }

    @Test
    void testDeleteSales() throws Exception {
        // 准备模拟数据
        when(salesService.deleteSales(1)).thenReturn(true);

        // 执行测试
        mockMvc.perform(delete("/api/sales/1"))
                .andExpect(status().isNoContent());

        // 验证服务方法被调用
        verify(salesService, times(1)).deleteSales(1);
    }

    @Test
    void testDeleteSales_NotFound() throws Exception {
        // 准备模拟数据
        when(salesService.deleteSales(100)).thenReturn(false);

        // 执行测试
        mockMvc.perform(delete("/api/sales/100"))
                .andExpect(status().isNotFound());

        // 验证服务方法被调用
        verify(salesService, times(1)).deleteSales(100);
    }

    @Test
    void testGetSalesSummary() throws Exception {
        // 准备模拟数据
        List<SalesSummary> summaryList = Arrays.asList(salesSummary);
        when(salesService.aggregateSales("2023-01-01", "产品A", "华东")).thenReturn(summaryList);

        // 执行测试
        mockMvc.perform(get("/api/sales/summary")
                .param("date", "2023-01-01")
                .param("product", "产品A")
                .param("region", "华东"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].date").value("2023-01-01"))
                .andExpect(jsonPath("$[0].product").value("产品A"))
                .andExpect(jsonPath("$[0].region").value("华东"))
                .andExpect(jsonPath("$[0].totalAmount").value(1299.00))
                .andExpect(jsonPath("$[0].count").value(1));

        // 验证服务方法被调用
        verify(salesService, times(1)).aggregateSales("2023-01-01", "产品A", "华东");
    }

    @Test
    void testBatchImportSales() throws Exception {
        // 准备模拟数据
        List<Sales> salesList = Arrays.asList(sales);
        when(salesService.batchImportSales(any(List.class))).thenReturn(true);

        // 执行测试
        mockMvc.perform(post("/api/sales/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salesList)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Batch import successful")));

        // 验证服务方法被调用
        verify(salesService, times(1)).batchImportSales(any(List.class));
    }

    @Test
    void testHandleIllegalArgumentException() throws Exception {
        // 准备模拟数据 - 抛出IllegalArgumentException
        when(salesService.findSalesById(anyInt())).thenThrow(new IllegalArgumentException("Invalid ID"));

        // 执行测试
        mockMvc.perform(get("/api/sales/999"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid ID"));
    }

    @Test
    void testHandleRuntimeException() throws Exception {
        // 准备模拟数据 - 抛出RuntimeException
        when(salesService.findSalesById(anyInt())).thenThrow(new RuntimeException("Internal error"));

        // 执行测试
        mockMvc.perform(get("/api/sales/999"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal error"));
    }
}
