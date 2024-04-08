FROM openjdk:19-alpine

COPY .env .

COPY application.properties .

COPY data.sql .

COPY hotel-booking-app-web/target/hotel-booking-app-web-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]