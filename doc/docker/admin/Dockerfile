FROM amazoncorretto:17

LABEL maintainer="YT"
LABEL version="1.0.0"
LABEL description="yt-message管理后端"
LABEL author="YT"
LABEL email="1479767311@qq.com"


#EXPOSE
WORKDIR /app
#设置部署环境
ENV APP_ENV="--spring.profiles.active=docker"

# 设置时区为东八区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 设置 JAVA_OPTS 环境变量，可通过 docker run -e "JAVA_OPTS=" 进行覆盖
#ENV JAVA_OPTS="-Xms512m -Xmx512m"
ENV JAVA_OPTS=""


# 将jar包复制到容器中
ADD ./yt-message-admin.jar ./app.jar

# 执行启动命令
#正确写法
ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS app.jar ${APP_ENV}"]
#错误写法
#ENTRYPOINT ["sh","-c","java", "$JAVA_OPTS", "-jar", "app.jar","${APP_ENV}"]


