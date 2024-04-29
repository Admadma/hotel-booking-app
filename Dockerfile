FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY hotel-booking-app-persistence .

COPY hotel-booking-app-service .

COPY hotel-booking-app-web .

COPY pom.xml .

COPY lombok.config .

RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jdk-alpine AS runtime

WORKDIR /app

RUN mkdir images

RUN mkdir demo_images

COPY images ./demo_images

COPY copy_files.sh .

RUN chmod +x copy_files.sh

COPY data.sql .

COPY --from=build /app/hotel-booking-app-web/target/hotel-booking-app-web-0.0.1-SNAPSHOT.jar hotel-booking-app.jar

ENTRYPOINT ["java", "-jar", "hotel-booking-app.jar"]