# 使用多阶段构建优化Docker镜像大小

# 阶段一：构建应用
FROM maven:3-jdk-8 AS builder

# 设置工作目录
WORKDIR /app

# 首先复制pom.xml文件并下载依赖（利用Docker缓存层）
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 然后复制源代码并构建
COPY src ./src
RUN mvn clean package -DskipTests

# 阶段二：创建运行环境
FROM openjdk:8-jre-slim

# 设置工作目录
WORKDIR /app

# 从构建阶段复制构建好的jar文件
COPY --from=builder /app/target/sales-analysis-1.0.0.jar app.jar

# 设置时区为亚洲/上海（中国时区）
RUN apk --no-cache add tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

# 暴露应用端口
EXPOSE 8080

# 设置启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]

# 设置JVM参数优化
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m -Dfile.encoding=UTF-8"
