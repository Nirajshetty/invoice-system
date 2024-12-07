FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/invoice-system-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
