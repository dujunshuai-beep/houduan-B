-- 使用已存在的数据库
USE taskdb;

-- 创建销售表
CREATE TABLE IF NOT EXISTS sales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sale_date DATE NOT NULL,
    product VARCHAR(255) NOT NULL,
    region VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    INDEX idx_sale_date (sale_date),
    INDEX idx_product (product),
    INDEX idx_region (region)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 添加注释
ALTER TABLE sales COMMENT = '销售记录表';
ALTER TABLE sales MODIFY COLUMN sale_date DATE NOT NULL COMMENT '销售日期';
ALTER TABLE sales MODIFY COLUMN product VARCHAR(255) NOT NULL COMMENT '产品名称';
ALTER TABLE sales MODIFY COLUMN region VARCHAR(255) NOT NULL COMMENT '销售区域';
ALTER TABLE sales MODIFY COLUMN amount DECIMAL(10, 2) NOT NULL COMMENT '销售金额';

-- 显示创建的表信息
SHOW CREATE TABLE sales;

-- 显示索引信息
SHOW INDEX FROM sales;
