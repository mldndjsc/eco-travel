FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY shared-module/pom.xml ./shared-module/pom.xml
COPY shared-module/src ./shared-module/src
RUN mvn -f shared-module/pom.xml clean install -DskipTests

COPY vodici-service/pom.xml .

RUN mvn dependency:go-offline -B

COPY vodici-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV SERVER_PORT=8089
EXPOSE 8089

ENTRYPOINT ["java", "-jar", "app.jar"]
