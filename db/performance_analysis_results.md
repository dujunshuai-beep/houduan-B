# SQL性能分析报告

## 1. 全表扫描查询
```sql
EXPLAIN SELECT * FROM sales;
```

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
|----|-------------|-------|------------|------|---------------|-----|---------|-----|------|----------|-------|
| 1 | SIMPLE | sales | NULL | ALL | NULL | NULL | NULL | NULL | 1000 | 100.00 | NULL |

**分析**：
- type = ALL 表示全表扫描，没有使用索引
- rows = 1000 表示扫描了1000行数据
- Extra = NULL 表示没有额外信息

## 2. 主键查询
```sql
EXPLAIN SELECT * FROM sales WHERE id = 1;
```

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
|----|-------------|-------|------------|------|---------------|-----|---------|-----|------|----------|-------|
| 1 | SIMPLE | sales | NULL | const | PRIMARY | PRIMARY | 4 | const | 1 | 100.00 | NULL |

**分析**：
- type = const 表示通过主键进行等值查询，效率最高
- key = PRIMARY 使用了主键索引
- rows = 1 只扫描了一行数据

## 3. 日期索引查询
```sql
EXPLAIN SELECT * FROM sales WHERE sale_date = '2025-01-01';
```

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
|----|-------------|-------|------------|------|---------------|-----|---------|-----|------|----------|-------|
| 1 | SIMPLE | sales | NULL | ref | idx_sale_date | idx_sale_date | 3 | const | 4 | 100.00 | NULL |

**分析**：
- type = ref 表示使用了非唯一索引
- key = idx_sale_date 使用了日期索引
- rows = 4 表示只扫描了4行数据

## 4. 产品索引查询
```sql
EXPLAIN SELECT * FROM sales WHERE product = '产品A';
```

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
|----|-------------|-------|------------|------|---------------|-----|---------|-----|------|----------|-------|
| 1 | SIMPLE | sales | NULL | ref | idx_product | idx_product | 1022 | const | 1 | 100.00 | NULL |

**分析**：
- type = ref 表示使用了非唯一索引
- key = idx_product 使用了产品索引
- rows = 1 表示只扫描了1行数据

## 5. 区域索引查询
```sql
EXPLAIN SELECT * FROM sales WHERE region = '华东';
```

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
|----|-------------|-------|------------|------|---------------|-----|---------|-----|------|----------|-------|
| 1 | SIMPLE | sales | NULL | ref | idx_region | idx_region | 1022 | const | 1 | 100.00 | NULL |

**分析**：
- type = ref 表示使用了非唯一索引
- key = idx_region 使用了区域索引
- rows = 1 表示只扫描了1行数据

## 6. 聚合查询（无条件）
```sql
EXPLAIN SELECT DATE_FORMAT(sale_date, '%Y-%m-%d') as date, product, region, SUM(amount) as totalAmount, COUNT(*) as count FROM sales GROUP BY date, product, region ORDER BY date DESC, totalAmount DESC;
```

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
|----|-------------|-------|------------|------|---------------|-----|---------|-----|------|----------|-------|
| 1 | SIMPLE | sales | NULL | ALL | NULL | NULL | NULL | NULL | 1000 | 100.00 | Using temporary; Using filesort |

**分析**：
- type = ALL 表示全表扫描
- rows = 1000 表示扫描了全部1000行数据
- Extra = Using temporary; Using filesort 表示使用了临时表和文件排序，性能较差

## 7. 聚合查询（带条件）
```sql
EXPLAIN SELECT DATE_FORMAT(sale_date, '%Y-%m-%d') as date, product, region, SUM(amount) as totalAmount, COUNT(*) as count FROM sales WHERE sale_date = '2025-01-01' AND product = '产品A' AND region = '华东' GROUP BY date, product, region ORDER BY date DESC, totalAmount DESC;
```

| id | select_type | table | partitions | type | possible_keys | key | key_len | ref | rows | filtered | Extra |
|----|-------------|-------|------------|------|---------------|-----|---------|-----|------|----------|-------|
| 1 | SIMPLE | sales | NULL | ref | idx_sale_date,idx_product,idx_region | idx_product | 1022 | const | 1 | 5.00 | Using where; Using temporary; Using filesort |

**分析**：
- type = ref 表示使用了非唯一索引
- key = idx_product 使用了产品索引
- rows = 1 表示只扫描了1行数据
- Extra = Using where; Using temporary; Using filesort 表示仍然使用了临时表和文件排序