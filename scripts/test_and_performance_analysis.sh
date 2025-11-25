#!/bin/bash

# 销售分析系统 - API功能测试和性能分析脚本
# 本脚本用于测试Spring Boot应用的所有RESTful API接口并进行MySQL性能分析

echo "========================================="
echo "销售分析系统 - API功能测试和性能分析脚本"
echo "========================================="

# 定义API基础URL
API_BASE_URL="http://localhost:8080/api/sales"

# 定义MySQL连接信息
MYSQL_HOST="localhost"
MYSQL_PORT="3306"
MYSQL_USER="root"
MYSQL_PASSWORD="rootpassword"
MYSQL_DATABASE="salesdb"

# 颜色定义
GREEN="\033[0;32m"
RED="\033[0;31m"
YELLOW="\033[0;33m"
NC="\033[0m" # No Color

# 检查服务是否运行
check_service_running() {
    echo -e "${YELLOW}检查服务是否在运行...${NC}"
    if curl -s "${API_BASE_URL}" > /dev/null; then
        echo -e "${GREEN}服务正在运行！${NC}"
        return 0
    else
        echo -e "${RED}错误: 服务似乎未运行，请先启动服务。${NC}"
        return 1
    fi
}

# 测试获取所有销售数据
test_get_all_sales() {
    echo -e "\n${YELLOW}测试1: 获取所有销售数据${NC}"
    echo "调用: GET ${API_BASE_URL}"
    curl -s -X GET "${API_BASE_URL}" | jq .
    echo -e "${GREEN}测试1完成${NC}"
}

# 测试按ID获取销售数据
test_get_sales_by_id() {
    echo -e "\n${YELLOW}测试2: 按ID获取销售数据${NC}"
    echo "调用: GET ${API_BASE_URL}/1"
    curl -s -X GET "${API_BASE_URL}/1" | jq .
    echo -e "${GREEN}测试2完成${NC}"
}

# 测试创建新销售记录
test_create_sales() {
    echo -e "\n${YELLOW}测试3: 创建新销售记录${NC}"
    echo "调用: POST ${API_BASE_URL}"
    
    # 创建测试数据
    TEST_DATA='{ \
        "saleDate": "2023-06-15", \
        "product": "测试产品", \
        "region": "测试地区", \
        "amount": 999.99 \
    }'
    
    curl -s -X POST "${API_BASE_URL}" \
         -H "Content-Type: application/json" \
         -d "${TEST_DATA}" | jq .
    echo -e "${GREEN}测试3完成${NC}"
}

# 测试更新销售记录
test_update_sales() {
    echo -e "\n${YELLOW}测试4: 更新销售记录${NC}"
    echo "调用: PUT ${API_BASE_URL}/1"
    
    # 创建更新数据
    UPDATE_DATA='{ \
        "id": 1, \
        "saleDate": "2023-06-16", \
        "product": "更新产品", \
        "region": "更新地区", \
        "amount": 1299.99 \
    }'
    
    curl -s -X PUT "${API_BASE_URL}/1" \
         -H "Content-Type: application/json" \
         -d "${UPDATE_DATA}" | jq .
    echo -e "${GREEN}测试4完成${NC}"
}

# 测试获取销售汇总数据
test_get_sales_summary() {
    echo -e "\n${YELLOW}测试5: 获取销售汇总数据${NC}"
    echo "调用: GET ${API_BASE_URL}/summary?date=2023-06-15&product=产品A&region=华东"
    
    curl -s -X GET "${API_BASE_URL}/summary?date=2023-06-15&product=产品A&region=华东" | jq .
    echo -e "${GREEN}测试5完成${NC}"
}

# 测试批量导入销售数据
test_batch_import_sales() {
    echo -e "\n${YELLOW}测试6: 批量导入销售数据${NC}"
    echo "调用: POST ${API_BASE_URL}/batch"
    
    # 创建批量导入数据
    BATCH_DATA='[ \
        { \
            "saleDate": "2023-06-20", \
            "product": "批量产品1", \
            "region": "批量地区1", \
            "amount": 599.99 \
        }, \
        { \
            "saleDate": "2023-06-21", \
            "product": "批量产品2", \
            "region": "批量地区2", \
            "amount": 799.99 \
        } \
    ]'
    
    curl -s -X POST "${API_BASE_URL}/batch" \
         -H "Content-Type: application/json" \
         -d "${BATCH_DATA}" | jq .
    echo -e "${GREEN}测试6完成${NC}"
}

# 执行MySQL性能分析
execute_mysql_performance_analysis() {
    echo -e "\n${YELLOW}执行MySQL性能分析${NC}"
    echo "========================================="
    
    # 创建性能分析SQL文件
    cat > /tmp/performance_analysis.sql << EOF
-- 连接到数据库
USE ${MYSQL_DATABASE};

-- 查看表结构
echo '表结构:';
SHOW CREATE TABLE sales;

-- 分析聚合查询的性能
echo '\n聚合查询性能分析:';
EXPLAIN SELECT 
    DATE_FORMAT(sale_date, '%Y-%m-%d') as date, 
    product, 
    region, 
    SUM(amount) as totalAmount,
    COUNT(*) as count
FROM sales
GROUP BY date, product, region;

-- 分析有筛选条件的聚合查询性能
echo '\n有筛选条件的聚合查询性能分析:';
EXPLAIN SELECT 
    DATE_FORMAT(sale_date, '%Y-%m-%d') as date, 
    product, 
    region, 
    SUM(amount) as totalAmount,
    COUNT(*) as count
FROM sales
WHERE sale_date >= '2023-01-01' AND product = '产品A'
GROUP BY date, product, region;

-- 分析索引使用情况
echo '\n索引使用情况:';
SHOW INDEX FROM sales;

-- 查看表统计信息
echo '\n表统计信息:';
SHOW TABLE STATUS LIKE 'sales';
EOF
    
    echo -e "${GREEN}执行SQL性能分析...${NC}"
    mysql -h ${MYSQL_HOST} -P ${MYSQL_PORT} -u ${MYSQL_USER} -p${MYSQL_PASSWORD} < /tmp/performance_analysis.sql
    
    # 清理临时文件
    rm /tmp/performance_analysis.sql
    
    echo -e "${GREEN}MySQL性能分析完成${NC}"
}

# 执行API功能测试
run_api_tests() {
    echo -e "\n${YELLOW}开始API功能测试${NC}"
    echo "========================================="
    
    test_get_all_sales
    test_get_sales_by_id
    test_create_sales
    test_update_sales
    test_get_sales_summary
    test_batch_import_sales
    
    echo -e "\n${GREEN}所有API测试完成${NC}"
}

# 主函数
main() {
    # 检查服务是否运行
    if ! check_service_running; then
        echo -e "${YELLOW}提示: 请确保服务已启动，可以使用以下命令启动服务:${NC}"
        echo -e "${YELLOW}docker-compose up -d${NC}"
        echo -e "${YELLOW}或者在项目目录中运行:${NC}"
        echo -e "${YELLOW}mvn spring-boot:run${NC}"
        exit 1
    fi
    
    # 运行API功能测试
    run_api_tests
    
    # 执行MySQL性能分析
    execute_mysql_performance_analysis
    
    echo -e "\n${GREEN}=========================================${NC}"
    echo -e "${GREEN}所有测试和性能分析完成！${NC}"
    echo -e "${GREEN}=========================================${NC}"
    echo -e "${YELLOW}性能分析说明:${NC}"
    echo -e "${YELLOW}1. 查看EXPLAIN结果中的'type'列，'ALL'表示全表扫描，'range'或'index'表示使用索引${NC}"
    echo -e "${YELLOW}2. 'rows'列表示MySQL估计需要检查的行数，值越小性能越好${NC}"
    echo -e "${YELLOW}3. 'Extra'列显示'Using index'表示使用了覆盖索引，性能较好${NC}"
    echo -e "${YELLOW}4. 确保所有查询都正确使用了索引，避免全表扫描${NC}"
}

# 运行主函数
main
