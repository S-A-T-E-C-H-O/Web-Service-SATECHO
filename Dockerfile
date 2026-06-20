# ── Stage 1: Build ────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src src
RUN mvn package -Dmaven.test.skip=true -q

# ── Stage 2: Runtime ──────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S agrosafe && adduser -S agrosafe -G agrosafe

COPY --from=builder /app/target/satecho-agrosafe-backend-0.0.1-SNAPSHOT.jar app.jar

USER agrosafe
EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
