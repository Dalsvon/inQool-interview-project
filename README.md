# Tennis reservation project (inQool interview project)

This is a server application that allows users to reserve tennis courts at arbitrary lengths. Application also
supports management of users, courts, reservations and surface types by administrators.

Project is created using Spring Boot and exposes its API at /api endpoints described later.

## Technologies
* Maven 3.9.1
* Java 21
* Spring Boot 3.4.4
* H2 in-memory database
* JWT token authentication
* Liquibase

## How to run
Application can be built from root by calling `mvn clean install` which will also run unit tests.

Server can be started by running `mvn spring-boot:run`. This will start the application on http://localhost:8080/.

## Endpoints
All endpoints are documented on http://localhost:8080/swagger-ui/index.html. Here you can find OpenApi
documentation of all endpoints, and you can also test them.

All endpoints except /api/auth/* are protected and need to be accessed with a valid JWT token, which can be
obtained by logging in by calling /api/auth/login.

Not all endpoints are accessible to normal users. Some can be accessed only by an administrator and will result in 403 code if called by users.

## External config and seeding
External configuration of the app can be changed in application.yml. You can change database access, port and jwt
configurations.

Seeding and admin account initialization can be also managed here by adding or removing contexts from spring.liquibase.contexts.

