# TODO: Projekt-Refactoring und Migration

## Überblick
Dieses TODO-Dokument beschreibt die Schritte zur Migration des Projekts:
- Backend: Gradle → Maven, Refactor, Verbesserungen
- Frontend: JavaFX → JavaScript (SPA)
- Dockerization für beide Teile

**Priorität:** Zuerst den aktuellen Stand sichern und lauffähig machen, bevor migriert wird.

## 1. Aktuellen Stand analysieren und lauffähig machen
- [ ] Gradle-Build testen: `./gradlew clean build` im backend/ und frontend/
- [ ] JARs ausführen: backend-v3.01-SNAPSHOT.jar (Port 8080) und frontend-1.0-SNAPSHOT.jar
- [ ] Datenbank prüfen: H2-Datenbank in backend/ (falls gefüllt, behalten; sonst leer starten)
- [ ] Abhängigkeiten dokumentieren: Alle Gradle-Dependencies aus build.gradle extrahieren
- [ ] Tests laufen lassen: `./gradlew test` – alle grün?
- [ ] API-Endpunkte identifizieren: Welche REST-APIs gibt es? (z.B. via Logs oder Code-Review)

**Warum zuerst?** Sicherstellen, dass das Projekt funktioniert, bevor Änderungen. Vermeidet "Broken Window"-Effekt.

## 2. Maven-Migration für Backend
- [ ] pom.xml erstellen: Basierend auf build.gradle (Spring Boot 2.4.5, Java 8, Dependencies: web, jdbc, h2, jpa, mail, etc.)
- [ ] Verzeichnisstruktur anpassen: src/main/java, src/test/java, etc.
- [ ] Build testen: `mvn clean compile`, `mvn test`, `mvn package`
- [ ] JAR laufen lassen: `java -jar target/backend-3.01.jar`
- [ ] Profile einrichten: dev, prod, test (application.properties anpassen)

## 3. OpenAPI/Swagger einrichten
- [ ] Dependency hinzufügen: springdoc-openapi (Maven)
- [ ] API-Dokumentation: Endpunkte annotieren (@Operation, @ApiResponse)
- [ ] Swagger-UI aktivieren: /swagger-ui.html
- [ ] openapi.yaml exportieren: Für Frontend-Entwicklung

## 4. H2-Testdaten und Tests verbessern
- [ ] Test-Konfiguration: src/test/resources/application.properties (H2 in-memory)
- [ ] Schema und Daten: schema.sql und data.sql in test/resources
- [ ] Integrationstests schreiben: @SpringBootTest mit Testdaten
- [ ] Unit-Tests erweitern: Mockito für Services/Repos

## 5. Backend-Refactor und Verbesserungen
- [ ] Layered Architecture: Controller → Service → Repository
- [ ] DTOs einführen: Request/Response-Objekte statt Maps
- [ ] Exception Handling: Global @ControllerAdvice
- [ ] Security: JWT oder Session-Management
- [ ] Monitoring: Spring Actuator (health, metrics)

## 6. Frontend in JavaScript umschreiben
- [ ] Framework wählen: React/Vue/Svelte + TypeScript
- [ ] Scaffold erstellen: `npm create vite@latest frontend --template react-ts`
- [ ] API-Integration: Axios/Fetch für Backend-APIs
- [ ] Komponenten portieren: Schrittweise (Login, Kursliste, etc.)
- [ ] UI/UX: Web-native Design (nicht 1:1 JavaFX)

## 7. Dockerization
- [ ] Backend-Dockerfile: Multistage (Maven build + JRE run)
- [ ] Frontend-Dockerfile: Node build + NGINX serve
- [ ] docker-compose.yml: Services für backend, frontend, db (H2 oder Postgres)
- [ ] Test: `docker compose up` und API/UI checken

## 8. Finale Schritte
- [ ] CI/CD einrichten: GitHub Actions oder ähnlich (build, test, deploy)
- [ ] Dokumentation: README.md erweitern mit Setup, API-Docs
- [ ] Testsuite vollständig: >80% Coverage
- [ ] Deployment: Container in Cloud (z.B. Heroku, AWS)

## Notizen
- **Risiko-Management:** Nach jedem Schritt Tests laufen lassen.
- **Parallel-Arbeit:** Frontend-Scaffold kann parallel zu Backend-Refactor starten.
- **Zeitplan:** Schätze 4-6 Wochen für alles, je nach Komplexität.
- **Abbruchpunkte:** Wenn Maven-Migration scheitert, Gradle behalten und nur refactoren.

Aktualisiert: 3. April 2026