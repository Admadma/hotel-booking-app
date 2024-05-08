FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jdk-alpine AS runtime

WORKDIR /app

RUN mkdir images

RUN mkdir demo_images

COPY images ./demo_images

COPY copy_files.sh .

RUN chmod +x copy_files.sh

COPY data.sql .

COPY --from=build /app/hotel-booking-app-web/target/*.jar hotel-booking-app.jar

ENTRYPOINT ["java", "-jar", "hotel-booking-app.jar"]