FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
RUN apk add curl
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]