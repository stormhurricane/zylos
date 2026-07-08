# --- Build Stage ---
# Use a lightweight Maven image with JDK 21 for building the application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Optimize dependency caching: Copy only pom.xml first to download plugins and dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the actual source code after dependencies are cached
COPY src ./src

# Build the production-ready JAR file and skip running tests during the docker build
RUN mvn clean package -DskipTests

# --- Run Stage ---
# Use a lightweight JRE image for running the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Security Best Practice: Create a non-root user and group to run the application safely
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built JAR from the builder stage (avoids wildcard mapping issues)
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Performance-optimized entrypoint for container environments using G1 Garbage Collector
ENTRYPOINT ["java", "-XX:+UseG1GC", "-jar", "app.jar"]