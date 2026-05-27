# Faza 1: Build faza (Koristimo Maven i JDK)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Prvo kopiramo i instaliramo shared-module u Docker
COPY shared-module/pom.xml ./shared-module/pom.xml
COPY shared-module/src ./shared-module/src
RUN mvn -f shared-module/pom.xml clean install -DskipTests

# 2. Zatim kopiramo pom.xml za tvoj servis
COPY maintenance-service/pom.xml .

# Preuzimamo zavisnosti
RUN mvn dependency:go-offline -B

# 3. Kopiramo source kod tvog servisa i pakujemo ga
COPY maintenance-service/src ./src
RUN mvn clean package -DskipTests

# Faza 2: Runtime faza
FROM eclipse-temurin:21-jre
WORKDIR /app

# Kopiramo samo gotov .jar iz prve faze
COPY --from=build /app/target/*.jar app.jar

# Eksternalizacija porta
ENV SERVER_PORT=8085
EXPOSE 8085

# Komanda za pokretanje
ENTRYPOINT ["java", "-jar", "app.jar"]