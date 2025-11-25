-- 性能分析SQL语句
-- 使用EXPLAIN命令分析主要查询的性能

-- 1. 分析findAll查询
EXPLAIN SELECT * FROM sales;

-- 2. 分析findById查询
EXPLAIN SELECT * FROM sales WHERE id = 1;

-- 3. 分析aggregateSales查询（不带条件）
EXPLAIN 
SELECT
    DATE_FORMAT(sale_date, '%Y-%m-%d') as date,
    product,
    region,
    SUM(amount) as totalAmount,
    COUNT(*) as count
FROM sales
GROUP BY date, product, region
ORDER BY date DESC, totalAmount DESC;

-- 4. 分析aggregateSales查询（带条件）
EXPLAIN 
SELECT
    DATE_FORMAT(sale_date, '%Y-%m-%d') as date,
    product,
    region,
    SUM(amount) as totalAmount,
    COUNT(*) as count
FROM sales
WHERE sale_date = '2025-01-01'
  AND product = '产品A'
  AND region = '华东'
GROUP BY date, product, region
ORDER BY date DESC, totalAmount DESC;

-- 5. 分析带索引的查询
EXPLAIN SELECT * FROM sales WHERE sale_date = '2025-01-01';
EXPLAIN SELECT * FROM sales WHERE product = '产品A';
EXPLAIN SELECT * FROM sales WHERE region = '华东';