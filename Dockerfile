FROM infotechsoft/maven:3.9.6-openjdk-17 AS build
# Ustaw katalog roboczy
WORKDIR /app
# Kopiuj pliki projektu
COPY . .
# Budowanie projektu (bez testów)
RUN mvn clean package -DskipTests
# Etap 2: Uruchamianie
FROM openjdk:19-jdk-slim
# Katalog roboczy
WORKDIR /app
# Skopiuj zbudowany plik JAR
COPY --from=build /app/target/*.jar app.jar
# Otwórz port
EXPOSE 8080
# Start aplikacji
ENTRYPOINT ["java", "-jar", "app.jar"]
