# Single-Stage Build für Spring Boot Backend (einfach für Tests)
FROM eclipse-temurin:8-jdk-alpine

# Arbeitsverzeichnis
WORKDIR /app

# Alle Dateien kopieren
COPY . .

# Alpine benötigt ggf. CA-Zertifikate für HTTPS, und wir erzwingen TLS 1.2
RUN apk add --no-cache ca-certificates && update-ca-certificates
ENV GRADLE_OPTS="-Dhttps.protocols=TLSv1.2"
RUN chmod +x gradlew

# Build der JAR (ohne Tests)
RUN ./gradlew bootJar -x test --no-daemon

# Port freigeben
EXPOSE 8080

# JAR starten
CMD ["java", "-jar", "build/libs/backend-v3.01-SNAPSHOT.jar"]