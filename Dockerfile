#FROM maven:3.6.3-jdk-8-slim as builder
#
## 将本地代码复制到容器映像
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#
## 构建发布项目
#RUN mvn package -DskipTests
#
## 在容器启动时运行 Web 服务
#CMD ["java","-jar","/app/target/TeamBlogBackend-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]



# 设置JAVA版本
FROM openjdk:8-jdk-alpine
# 指定存储卷, 任何向/tmp写入的信息都不会记录到容器存储层
VOLUME /tmp
# 拷贝运行JAR包
ADD TeamBlogBackend-0.0.1-SNAPSHOT.jar TeamBlog.jar
# 设置JVM运行参数， 这里限定下内存大小，减少开销
ENV JAVA_OPTS="\
-server \
-Xms256m \
-Xmx512m \
-XX:MetaspaceSize=256m \
-XX:MaxMetaspaceSize=512m"
#空参数，方便创建容器时传参
ENV PARAMS=""
# 入口点， 执行JAVA运行命令
ENTRYPOINT ["java -jar $JAVA_OPTS TeamBlog.jar $PARAMS"]
