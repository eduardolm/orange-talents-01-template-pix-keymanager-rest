FROM openjdk:11-jdk-slim
MAINTAINER Eduardo lodi Marzano <lodi001@uol.com.br>
ARG JAR_FILE=build/libs/*-all.jar
ADD ${JAR_FILE} app.jar
ENV APP_NAME keymanager-rest
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080