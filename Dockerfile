FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /source
COPY src ./src
COPY pom.xml .

#required BUILD_KIT to be enabled
RUN --mount=type=cache,target=~/.m2 mvn clean package -DskipTests

FROM openjdk:17-alpine

WORKDIR /app
COPY --from=build /source/target/word-spreads*.jar word-spreads.jar
# COPY target/word-spreads*.jar word-spreads.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "word-spreads.jar"]