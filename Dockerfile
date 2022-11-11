# FROM maven:3.8.5-openjdk-17-slim AS build
# WORKDIR /source
# COPY src .
# COPY pom.xml .
# RUN --mount=type=cache,target=~/.m2 mvn clean package -DskipTests

FROM openjdk:17-alpine
WORKDIR /app
# COPY --from=build /source/target/word-spreads*.jar word-spreads.jar
COPY target/word-spreads*.jar word-spreads.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "word-spreads.jar"]