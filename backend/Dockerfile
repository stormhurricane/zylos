# Multi-stage build for Spring Boot Backend

# --- Build Stage ---
# Use a Maven image with JDK 21 for building the application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

# Set working directory inside the container
WORKDIR /app

# Copy the Maven project files
# Copy pom.xml first to leverage Docker cache if dependencies don't change
COPY pom.xml .
# Copy source code
COPY src ./src

# Build the JAR using Maven
# -DskipTests skips running tests during the Docker build, as tests should ideally run in CI
RUN mvn clean package -DskipTests

# --- Run Stage ---
# Use a JRE image for running the application (smaller than JDK)
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]