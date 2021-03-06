# Room Reservations

This is a recruitment assignment application.


### Prerequisites
Have Java 8 and Docker installed on your localhost.

### Getting started
To start the app with a dockerized Postgres database execute

1. `docker run -p 5432:5432 -d postgres:11.2-alpine` to pull Postgres on Alpine docker image and run it, then
2. `./mvnw clean spring-boot:run` to start the app.

Database gets its tables created and populated with sample data upon startup with the use of Liquibase. See [seed-rooms.csv](src/main/resources/db/changelog/seed-rooms.csv) for list of rooms initially available. 

To play with the api go to http://localhost:8080/swagger-ui.html.

To run all tests execute `./mvnw clean integration-test` in project root directory.

Have fun!
