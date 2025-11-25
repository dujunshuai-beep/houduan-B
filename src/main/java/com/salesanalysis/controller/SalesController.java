package com.salesanalysis.controller;

import com.salesanalysis.model.Sales;
import com.salesanalysis.model.SalesSummary;
import com.salesanalysis.service.SalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@Tag(name = "销售数据管理", description = "销售数据的CRUD操作和聚合查询")
public class SalesController {

    private final SalesService salesService;

    @Autowired
    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @Operation(summary = "获取所有销售记录")
    @GetMapping
    public ResponseEntity<List<Sales>> getAllSales() {
        List<Sales> salesList = salesService.findAllSales();
        return ResponseEntity.ok(salesList);
    }

    @Operation(summary = "根据ID获取销售记录")
    @GetMapping("/{id}")
    public ResponseEntity<Sales> getSalesById(
            @Parameter(description = "销售记录ID") @PathVariable Integer id) {
        Sales sales = salesService.findSalesById(id);
        if (sales == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sales);
    }

    @Operation(summary = "新增销售记录")
    @PostMapping
    public ResponseEntity<Sales> createSales(@RequestBody Sales sales) {
        Sales createdSales = salesService.saveSales(sales);
        return new ResponseEntity<>(createdSales, HttpStatus.CREATED);
    }

    @Operation(summary = "更新销售记录")
    @PutMapping("/{id}")
    public ResponseEntity<Sales> updateSales(
            @Parameter(description = "销售记录ID") @PathVariable Integer id,
            @RequestBody Sales sales) {
        // 确保请求的ID与实体中的ID一致
        sales.setId(id);
        Sales updatedSales = salesService.updateSales(sales);
        return ResponseEntity.ok(updatedSales);
    }

    @Operation(summary = "删除销售记录")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSales(
            @Parameter(description = "销售记录ID") @PathVariable Integer id) {
        boolean deleted = salesService.deleteSales(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "聚合查询销售数据")
    @GetMapping("/summary")
    public ResponseEntity<List<SalesSummary>> getSalesSummary(
            @Parameter(description = "销售日期，格式：YYYY-MM-DD") @RequestParam(required = false) String date,
            @Parameter(description = "产品名称") @RequestParam(required = false) String product,
            @Parameter(description = "区域") @RequestParam(required = false) String region) {
        List<SalesSummary> summaryList = salesService.aggregateSales(date, product, region);
        return ResponseEntity.ok(summaryList);
    }

    @Operation(summary = "批量导入销售数据")
    @PostMapping("/batch")
    public ResponseEntity<String> batchImportSales(@RequestBody List<Sales> salesList) {
        boolean success = salesService.batchImportSales(salesList);
        if (success) {
            return ResponseEntity.ok("Batch import successful. Imported " + salesList.size() + " records.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Batch import failed");
        }
    }

    // 全局异常处理
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
