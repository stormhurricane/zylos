# TODO: Projekt-Refactoring und Migration

## Überblick
Dieses TODO-Dokument beschreibt die Schritte zur Migration des Projekts:
- Backend: Gradle → Maven (Java 21), Spring Boot 3 Migration
- Frontend: JavaFX → JavaScript (SPA)
- Dockerization für beide Teile

**Status:** Phase 1 & 2 erfolgreich abgeschlossen. Fokus liegt nun auf Architektur & Refactoring.

## 1. Aktuellen Stand analysieren und lauffähig machen
- [X] Gradle-Build testen: `./gradlew clean build` im backend/ und frontend/
- [X] JARs ausführen: backend-v3.01-SNAPSHOT.jar (Port 8080) und frontend-1.0-SNAPSHOT.jar
- [X] Datenbank prüfen: H2-Datenbank in backend/ (falls gefüllt, behalten; sonst leer starten) → Leer starten (keine DB-Dateien vorhanden)
- [X] Lokale MySQL-Datenbank einrichten: Docker-Container für MySQL starten (für realistischere Tests)
- [X] Backend-Dockerfile erstellen: Single-Stage Build für Spring Boot
- [X] docker-compose.yml erstellen: Backend + MySQL als Services
- [X] Abhängigkeiten dokumentieren: Alle Gradle-Dependencies aus build.gradle extrahieren
- [X] Tests laufen lassen: `./gradlew test` – alle grün?
- [X] API-Endpunkte identifizieren: Welche REST-APIs gibt es? (z.B. via Logs oder Code-Review)

**Warum zuerst?** Sicherstellen, dass das Projekt funktioniert, bevor Änderungen. Vermeidet "Broken Window"-Effekt.

## 2. Maven-Migration & Java 21 Upgrade (Backend)
- [X] pom.xml erstellen: Umstellung auf **Java 21** und **Spring Boot 3.2.x**
- [X] MySQL-Dependency: Wechsel von `mysql-connector-java` zu `com.mysql:mysql-connector-j`
- [X] **Jakarta EE Namespace Migration**:
    - [X] Suchen & Ersetzen: `javax.persistence.*` -> `jakarta.persistence.*`
    - [X] Suchen & Ersetzen: `javax.validation.*` -> `jakarta.validation.*`
    - [X] Suchen & Ersetzen: `javax.servlet.*` -> `jakarta.servlet.*`
    - [X] Mail-Versand: `javax.mail.*` -> `jakarta.mail.*`
- [X] Dependencies aufräumen: Manuelle `javax.mail` und `activation` entfernen (jetzt in Starter enthalten)
- [X] **Gradle-Altlasten entfernen**: build.gradle, gradlew und build/ Ordner löschen
- [X] Verzeichnisstruktur anpassen: src/main/java, src/test/java, etc.
- [X] **Umgebung prüfen**: Sicherstellen, dass `mvn -version` Java 21 nutzt
- [X] **Jakarta EE Namespace Migration**: Abgeschlossen (javax -> jakarta)
- [X] **Circular Reference beheben**: `TeilnehmerService` auf Constructor Injection mit `@Lazy` umgestellt
- [X] Build testen: `mvn clean compile` (Erfolgreich), `mvn test` (JUnit 5 sicherstellen)
- [X] Build testen: `mvn clean package` (JAR-Erstellung in /target)
- [X] JAR laufen lassen: `java -jar target/backend-v3.01-SNAPSHOT.jar` (Konfiguration auf localhost angepasst)
- [X] **Datenbank-Anbindung**: MySQL Docker-Container angebunden
- [X] **Umgebungs-Trennung**: Gelöst via src/test/resources und Docker-Env-Overrides

## 3. API-Stabilisierung (Der "Vertrag")
- [ ] **Java 21 Records**: Erstelle Records als DTOs für alle `Map`-basierten Endpunkte
- [ ] **SpringDoc**: Dependency `springdoc-openapi-starter-webmvc-ui` hinzufügen
- [ ] **Swagger-UI**: API unter `/swagger-ui.html` verifizieren
- [ ] **Contract-Check**: Alle Endpunkte aus `API-Endpoints.md` prüfen


## 4. H2-Testdaten und Tests verbessern
- [ ] Test-Konfiguration: src/test/resources/application.properties (H2 in-memory)
- [ ] Schema und Daten: schema.sql und data.sql in test/resources
- [ ] Integrationstests schreiben: @SpringBootTest mit Testdaten
- [ ] Unit-Tests erweitern: Mockito für Services/Repos

## 5. Backend-Refactor und Verbesserungen
- [ ] Layered Architecture: Controller → Service → Repository
- [ ] Restliche DTOs einführen: Konsistente Datenmodelle für alle Endpunkte
- [ ] Exception Handling: Global @ControllerAdvice
- [ ] Spring Security: Falls vorhanden, auf Lambda-basierte Konfiguration (Security 6) umstellen
- [ ] Tests an neue Architektur anpassen: Mockito (Version 5+) für neue Service-Layer
- [ ] Security: JWT oder Session-Management
- [ ] Monitoring: Spring Actuator (health, metrics)

## 6. Frontend in JavaScript umschreiben
- [ ] Framework wählen: React/Vue/Svelte + TypeScript
- [ ] Scaffold erstellen: `npm create vite@latest frontend --template react-ts`
- [ ] API-Integration: Axios/Fetch für Backend-APIs
- [ ] Komponenten portieren: Schrittweise (Login, Kursliste, etc.)
- [ ] UI/UX: Web-native Design (nicht 1:1 JavaFX)

## 7. Dockerization
- [ ] Backend-Dockerfile: Multistage (Maven build + **JRE 21** run)
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