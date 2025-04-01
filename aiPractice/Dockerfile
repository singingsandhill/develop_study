FROM openjdk:17
WORKDIR /app
COPY build/libs/aiPractice-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources /app/resources
ENTRYPOINT ["java", "-jar", "app.jar"]