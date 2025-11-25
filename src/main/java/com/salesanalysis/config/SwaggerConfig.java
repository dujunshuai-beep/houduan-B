package com.salesanalysis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("销售数据分析系统 API")
                        .version("1.0.0")
                        .description("销售数据分析系统的RESTful API文档，提供销售数据的查询、聚合等功能。"));
    }

}
