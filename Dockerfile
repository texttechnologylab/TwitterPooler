FROM maven:3.8.6-openjdk-11-slim as build

#WORKDIR /app

COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY src .

RUN mvn clean quarkus:build

FROM openjdk:11-jre-slim-buster

COPY --from=build target/quarkus-app/ /quarkus-app
WORKDIR /quarkus-app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]