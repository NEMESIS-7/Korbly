FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/*.jar app.jar
COPY .env /app/.env

ENV JAVA_OPTS="-Xmx384m -Xms256m"


EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$PORT -jar app.jar"]