package com.salesanalysis.service.impl;

import com.salesanalysis.mapper.SalesMapper;
import com.salesanalysis.model.Sales;
import com.salesanalysis.model.SalesSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalesServiceImplTest {

    @Mock
    private SalesMapper salesMapper;

    @InjectMocks
    private SalesServiceImpl salesService;

    private Sales sales;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 创建测试数据
        sales = new Sales();
        sales.setId(1);
        sales.setSaleDate(new Date());
        sales.setProduct("产品A");
        sales.setRegion("华东");
        sales.setAmount(new BigDecimal(1299.00));
    }

    @Test
    void testFindAllSales() {
        // 准备模拟数据
        List<Sales> salesList = Arrays.asList(sales);
        when(salesMapper.findAll()).thenReturn(salesList);
        
        // 调用方法
        List<Sales> result = salesService.findAllSales();
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(salesMapper, times(1)).findAll();
    }

    @Test
    void testFindSalesById() {
        // 准备模拟数据
        when(salesMapper.findById(1)).thenReturn(sales);
        
        // 调用方法
        Sales result = salesService.findSalesById(1);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(salesMapper, times(1)).findById(1);
    }

    @Test
    void testFindSalesById_NullId() {
        // 测试空ID参数
        assertThrows(IllegalArgumentException.class, () -> {
            salesService.findSalesById(null);
        });
    }

    @Test
    void testSaveSales() {
        // 准备模拟数据
        when(salesMapper.insert(sales)).thenReturn(1);
        
        // 调用方法
        Sales result = salesService.saveSales(sales);
        
        // 验证结果
        assertNotNull(result);
        verify(salesMapper, times(1)).insert(sales);
    }

    @Test
    void testSaveSales_InvalidData() {
        // 测试无效数据
        Sales invalidSales = new Sales();
        // 缺少必填字段
        
        assertThrows(IllegalArgumentException.class, () -> {
            salesService.saveSales(invalidSales);
        });
    }

    @Test
    void testUpdateSales() {
        // 准备模拟数据
        when(salesMapper.findById(1)).thenReturn(sales);
        when(salesMapper.update(sales)).thenReturn(1);
        
        // 修改数据
        sales.setProduct("产品B");
        
        // 调用方法
        Sales result = salesService.updateSales(sales);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("产品B", result.getProduct());
        verify(salesMapper, times(1)).update(sales);
    }

    @Test
    void testUpdateSales_NotFound() {
        // 测试更新不存在的记录
        when(salesMapper.findById(100)).thenReturn(null);
        sales.setId(100);
        
        assertThrows(RuntimeException.class, () -> {
            salesService.updateSales(sales);
        });
    }

    @Test
    void testDeleteSales() {
        // 准备模拟数据
        when(salesMapper.findById(1)).thenReturn(sales);
        when(salesMapper.delete(1)).thenReturn(1);
        
        // 调用方法
        boolean result = salesService.deleteSales(1);
        
        // 验证结果
        assertTrue(result);
        verify(salesMapper, times(1)).delete(1);
    }

    @Test
    void testDeleteSales_NotFound() {
        // 测试删除不存在的记录
        when(salesMapper.findById(100)).thenReturn(null);
        
        boolean result = salesService.deleteSales(100);
        assertFalse(result);
    }

    @Test
    void testAggregateSales() {
        // 准备模拟数据
        SalesSummary summary = new SalesSummary();
        summary.setDate("2023-01-01");
        summary.setProduct("产品A");
        summary.setRegion("华东");
        summary.setTotalAmount(new BigDecimal(1299.00));
        summary.setCount(1);
        
        List<SalesSummary> summaryList = Arrays.asList(summary);
        when(salesMapper.aggregateSales("2023-01-01", "产品A", "华东")).thenReturn(summaryList);
        
        // 调用方法
        List<SalesSummary> result = salesService.aggregateSales("2023-01-01", "产品A", "华东");
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(salesMapper, times(1)).aggregateSales("2023-01-01", "产品A", "华东");
    }

    @Test
    void testBatchImportSales() {
        // 准备模拟数据
        List<Sales> salesList = Arrays.asList(sales);
        when(salesMapper.batchInsert(salesList)).thenReturn(1);
        
        // 调用方法
        boolean result = salesService.batchImportSales(salesList);
        
        // 验证结果
        assertTrue(result);
        verify(salesMapper, times(1)).batchInsert(salesList);
    }

    @Test
    void testBatchImportSales_EmptyList() {
        // 测试空列表
        List<Sales> emptyList = new ArrayList<>();
        
        assertThrows(IllegalArgumentException.class, () -> {
            salesService.batchImportSales(emptyList);
        });
    }
}
