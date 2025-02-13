FROM gradle:8.12.1-jdk23 as builder
WORKDIR /app
COPY . .
RUN gradle clean build --no-daemon

FROM openjdk:23-jdk as prod
COPY --from=builder /app/build /app
ENTRYPOINT ["java", "-jar", "app/libs/auth-service-0.0.1-SNAPSHOT.jar"]