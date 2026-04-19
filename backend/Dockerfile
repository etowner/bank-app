# syntax=docker/dockerfile:1

# ---- Base build stage ----
FROM maven:3.9-eclipse-temurin-25 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ---- Development stage ----
FROM eclipse-temurin:25-jre-alpine AS development

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

# ---- Production stage ----
FROM eclipse-temurin:25-jre-alpine AS production

RUN adduser --system --group appuser

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]