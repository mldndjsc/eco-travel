# Faza 1: Build (Kompajliranje)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# Preuzimanje zavisnosti (ubrzava ponovne build-ove)
RUN mvn dependency:go-offline -B
COPY src ./src
# Pakovanje aplikacije uz preskakanje testova
RUN mvn clean package -DskipTests

# Faza 2: Runtime (Pokretanje)
FROM eclipse-temurin:21-jre
WORKDIR /app
# Kopiranje samo generisanog JAR fajla iz prethodne faze
COPY --from=build /app/target/*.jar app.jar

# Definisanje porta (8084 je za accommodation)
ENV SERVER_PORT=8084
EXPOSE 8084

# Pokretanje aplikacije
ENTRYPOINT ["java", "-jar", "app.jar"]