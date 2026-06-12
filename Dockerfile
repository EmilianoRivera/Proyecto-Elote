FROM eclipse-temurin:21-jdk-alpine
LABEL authors = "darkdestiny"
ARG JAR-FILE=target/Eskilokos-0.0.1-SNAPSHOT.jar
COPY ${JAR-FILE} app_eskilokos.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app_eskilokos.jar"]