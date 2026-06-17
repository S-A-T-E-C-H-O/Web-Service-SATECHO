# ── Stage 1: Build ────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline -q

COPY src src
RUN ./mvnw package -Dmaven.test.skip=true -q

# ── Stage 2: Runtime ──────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S agrosafe && adduser -S agrosafe -G agrosafe

COPY --from=builder /app/target/satecho-agrosafe-backend-0.0.1-SNAPSHOT.jar app.jar

USER agrosafe
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
