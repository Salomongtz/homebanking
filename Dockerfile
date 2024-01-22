FROM gradle:8.5.0-jdk17-alpine
COPY . .
RUN gradle build
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/home/gradle/src/build/libs/homebanking-0.0.1-SNAPSHOT.jar"]