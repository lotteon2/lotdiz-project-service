FROM openjdk:11-slim-buster

WORKDIR /app

ARG ORIGINAL_JAR_FILE=./build/libs/project-service-1.0.0.jar

COPY ${ORIGINAL_JAR_FILE} project-service.jar

CMD ["java", "-jar", "/app/project-service.jar"]
