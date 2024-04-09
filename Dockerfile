FROM openjdk:19-alpine

WORKDIR /app

COPY data.sql .

COPY hotel-booking-app-web/target/hotel-booking-app-web-0.0.1-SNAPSHOT.jar hotel-booking-app.jar

ENTRYPOINT ["java", "-jar", "hotel-booking-app.jar"]