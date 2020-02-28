# 基础镜像
FROM registry.cn-beijing.aliyuncs.com/o-w-o/api-starter:1.0

# 对应 pom.xml 文件中的 dockerfile-maven-plugin 插件 buildArgs 配置项 JAR_FILE 的值
ARG JAR_FILE

# 复制打包完成后的jar文件到/opt目录下
COPY ${JAR_FILE} /opt/app.jar

# 启动容器时执行
  ## 使用 "-Dkey=value" 覆盖 spring boot 配置文件参数
  ## 使用 "-Djava.security.egd=file:/dev/./urandom" 可以加快 SecureRandom 随机数产生过程
    ### —— SecureRandom 在 java 各种组件中使用广泛，可以可靠的产生随机数。但在大量产生随机数的场景下，性能会较低。
ENTRYPOINT java \
    -Dspring.profiles.active=production \
    -Djava.security.egd=file:/dev/./urandom \
    -jar /opt/app.jar

# 使用端口 8080
EXPOSE 8080
