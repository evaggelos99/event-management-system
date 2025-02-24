FROM eclipse-temurin:17.0.14_7-jre-ubi9-minimal
ADD target/organizer-service-0.0.1-SNAPSHOT-jar-with-dependencies.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]