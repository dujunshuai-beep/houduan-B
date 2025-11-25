package com.salesanalysis.util;

import com.salesanalysis.mapper.SalesMapper;
import com.salesanalysis.model.Sales;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MockarooDataLoader {
    private static final String API_KEY = "86296520"; // 用户提供的Mockaroo API密钥
    
    public static void main(String[] args) {
        System.out.println("开始执行Mockaroo数据加载程序...");
        
        try {
            // 1. 从Mockaroo生成1000条测试数据
            MockarooDataGenerator generator = new MockarooDataGenerator(API_KEY);
            List<Sales> salesList = generator.generate1000TestData();
            
            // 2. 显示部分生成的数据，用于验证
            System.out.println("\n部分生成的数据样本：");
            for (int i = 0; i < Math.min(5, salesList.size()); i++) {
                System.out.println(salesList.get(i));
            }
            
            // 3. 通过MyBatis直接插入数据到数据库
            int insertedCount = insertDataDirectly(salesList);
            
            System.out.println("\n数据加载程序执行完成！");
            System.out.println("总共生成：" + salesList.size() + "条数据");
            System.out.println("成功插入数据库：" + insertedCount + "条数据");
            
        } catch (Exception e) {
            System.err.println("执行过程中出现错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 直接使用MyBatis插入数据，不依赖Spring上下文
     * @param salesList 销售数据列表
     * @return 插入成功的记录数
     * @throws IOException 配置文件读取异常
     */
    private static int insertDataDirectly(List<Sales> salesList) throws IOException {
        // 读取MyBatis配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        
        int insertedCount = 0;
        SqlSession sqlSession = null;
        
        try {
            sqlSession = sqlSessionFactory.openSession(false); // 不自动提交
            SalesMapper salesMapper = sqlSession.getMapper(SalesMapper.class);
            
            // 分批次插入数据
            int batchSize = 100;
            int totalSize = salesList.size();
            
            System.out.println("\n开始将数据插入到数据库...");
            
            for (int i = 0; i < totalSize; i += batchSize) {
                int endIndex = Math.min(i + batchSize, totalSize);
                List<Sales> batchList = salesList.subList(i, endIndex);
                
                int batchInserted = salesMapper.batchInsert(batchList);
                insertedCount += batchInserted;
                
                // 每批次提交一次
                sqlSession.commit();
                
                System.out.println("已插入第" + (i + 1) + "-" + endIndex + "条数据，当前进度：" + endIndex + "/" + totalSize);
            }
            
            System.out.println("数据插入完成！");
            
        } catch (Exception e) {
            if (sqlSession != null) {
                sqlSession.rollback();
            }
            System.err.println("数据插入过程中发生错误：" + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
        
        return insertedCount;
    }
    
    /**
     * 创建一个简单的数据源，用于连接数据库
     * @return DriverManagerDataSource
     */
    private static DriverManagerDataSource createDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/taskdb?useSSL=false&serverTimezone=UTC&characterEncoding=utf-8");
        dataSource.setUsername("user");
        dataSource.setPassword("password");
        return dataSource;
    }
}