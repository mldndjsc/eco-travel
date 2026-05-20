# Stage 1: Build faza (Koristimo Maven i JDK)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Kopiramo pom.xml, source kod i shared config repository
COPY config-service/pom.xml .
COPY config-service/src ./src
COPY config_repo ./config_repo

# Kompajliramo i pakujemo (preskačemo testove radi brzine demoa)
RUN mvn clean package -DskipTests

# Stage 2: Runtime faza (Minimalni JRE)
FROM container-registry.oracle.com/java/openjdk:24-oraclelinux9
WORKDIR /app

# Kopiramo gotov .jar i konfiguracione fajlove koje Config Server servira
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/config_repo ./config_repo

# Eksternalizacija porta (Faktor III - Config)
ENV SERVER_PORT=8888

# Dokumentujemo port
EXPOSE 8888

# Komanda za pokretanje
ENTRYPOINT ["java", "-jar", "app.jar"]
