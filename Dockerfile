FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY . /app

RUN ./mvnw clean package -DskipTests

EXPOSE 8081

CMD ["java", "-jar", "target/BackEnd_INGSW-0.0.1-SNAPSHOT.jar"]
