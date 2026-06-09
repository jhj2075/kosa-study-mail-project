FROM eclipse-temurin:21-jre

ARG JAR_FILE
WORKDIR /app
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
