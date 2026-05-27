# Faza 1: Build faza (Koristimo Maven i JDK)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Prvo kopiramo i instaliramo shared-module u Docker
COPY shared-module/pom.xml ./shared-module/pom.xml
COPY shared-module/src ./shared-module/src
RUN mvn -f shared-module/pom.xml clean install -DskipTests

# 2. Zatim kopiramo pom.xml za accommodation-service
COPY accommodation-service/pom.xml .

# Preuzimamo zavisnosti
RUN mvn dependency:go-offline -B

# 3. Kopiramo source kod i pakujemo accommodation-service
COPY accommodation-service/src ./src
RUN mvn clean package -DskipTests

# Faza 2: Runtime faza
FROM eclipse-temurin:21-jre
WORKDIR /app

# Kopiramo samo gotov .jar iz prve faze
COPY --from=build /app/target/*.jar app.jar

# Eksternalizacija porta za accommodation
ENV SERVER_PORT=8084
EXPOSE 8084

# Komanda za pokretanje
ENTRYPOINT ["java", "-jar", "app.jar"]