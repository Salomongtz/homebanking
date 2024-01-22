FROM gradle:8.5.0-jdk17-alpine
COPY . .
EXPOSE 8080
RUN chmod +x gradlew
RUN gradle build
ENTRYPOINT ["java", "-jar", "build/libs/homebanking-0.0.1-SNAPSHOT.jar"]