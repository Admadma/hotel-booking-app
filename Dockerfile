FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY hotel-booking-app-web/target/hotel-booking-app-web-0.0.1-SNAPSHOT.jar hotel-booking-app.jar

ENTRYPOINT ["java", "-jar", "hotel-booking-app.jar"]