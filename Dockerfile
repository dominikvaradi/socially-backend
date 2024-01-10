FROM maven:3.9.6-amazoncorretto-11-al2023 AS builder

WORKDIR /app

COPY pom.xml .

RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]

COPY src ./src

RUN ["mvn", "package", "-DskipTests"]

FROM amazoncorretto:11-al2023 AS runner

WORKDIR /app

COPY --from=builder /app/target/spring-boot-application.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "spring-boot-application.jar"]