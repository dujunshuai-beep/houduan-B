#!/bin/bash

# 设置环境变量
export JAVA_HOME=$(/usr/libexec/java_home)
export PATH=$JAVA_HOME/bin:$PATH

echo "开始运行Mockaroo数据加载器..."

# 检查是否已编译项目
if [ ! -d "target/classes" ]; then
    echo "项目尚未编译，正在执行编译..."
    mvn clean compile
    if [ $? -ne 0 ]; then
        echo "编译失败，请检查代码错误"
        exit 1
    fi
fi

# 构建classpath
CLASSPATH=".:target/classes"

# 添加Maven依赖到classpath
dependency_jars=$(find ~/.m2/repository -name "*.jar" | grep -E 'mysql|mybatis|json|spring')
for jar in $dependency_jars; do
    CLASSPATH="$CLASSPATH:$jar"
done

# 运行MockarooDataLoader
echo "正在运行MockarooDataLoader..."
java -cp "$CLASSPATH" com.salesanalysis.util.MockarooDataLoader

echo "Mockaroo数据加载完成！"
