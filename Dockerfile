FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jdk-alpine AS runtime

WORKDIR /app

COPY --from=build /app/data.sql .

COPY --from=build /app/hotel-booking-app-web/target/hotel-booking-app-web-0.0.1-SNAPSHOT.jar hotel-booking-app.jar

ENTRYPOINT ["java", "-jar", "hotel-booking-app.jar"]