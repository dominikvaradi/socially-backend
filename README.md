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

## Environment Variables

| ENVIRONMENT VARIABLE          | Description                                                         |
|-------------------------------|---------------------------------------------------------------------|
| **DATABASE_URL**              | The URL of the database. (string)                                   |
| **DATABASE_USERNAME**         | The username of the database user. (string)                         |
| **DATABASE_PASSWORD**         | The password of the database user. (string)                         |
| **TESTING_ENDPOINTS_ENABLED** | Whether the testing endpoints are enabled or not. (boolean)         |
| **JWT_SECRET_IN_BASE64**      | The secret used for JWT token generation. (base64 formatted string) |

## Endpoint documentation

There is an openapi generation configured in Spring, which you can download from [http://localhost:8080/api-docs](http://localhost:8080/api-docs) if you are running the application.

You can see the REST Api documentation via Swagger UI, on [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) if you are running the application.

## Test data generation

You can enable test data generation endpoint by changing the `testing-TESTING_ENDPOINTS_ENABLED-enabled` environment variable, or edit `application.yaml` locally:

```
application:
  environment:
    testing-endpoints-enabled: true
```

Make sure you set the `testing-endpoints-enabled` environment variable to `false` if you're making a production ready build.

After setting the `testing-endpoints-enabled` environment variable to `true`, you will have access for the following endpoint, which will reset the database and fill it up with some test data:

```
POST /api/test/reset-db

no parameters, or request body required.
```
