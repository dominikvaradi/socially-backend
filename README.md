# Socially backend

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=dominikvaradi%3Asocially&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=dominikvaradi%3Asocially)

## Requirements

For building and running the application you need:

- JDK 11
- Maven 3


## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `hu.dominikvaradi.sociallybackend.SociallyBackendApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Endpoints

There is an openapi generation configured in Spring, which you can download from [http://localhost:8080/api-docs](http://localhost:8080/api-docs) if you are running the application.

You can see the REST Api documentation via Swagger UI, on [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) if you are running the application.
