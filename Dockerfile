FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21
WORKDIR /app

# Copia il JAR finale dal primo stage
COPY --from=build /app/target/DietiEstates2025-1.0-SNAPSHOT.jar app.jar

RUN apt-get update && apt-get install -y xvfb

CMD ["xvfb-run", "java", "-jar", "app.jar"]
