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
- [X] **Datenbank-Cleanup**: Unnötige `data.sql` (Billionaires-Beispiel) entfernen oder ersetzen
- [X] Verzeichnisstruktur anpassen: src/main/java, src/test/java, etc.
- [X] **Umgebung prüfen**: Sicherstellen, dass `mvn -version` Java 21 nutzt
- [X] **Jakarta EE Namespace Migration**: Abgeschlossen (javax -> jakarta)
- [X] **Circular Reference beheben**: `TeilnehmerService` auf Constructor Injection mit `@Lazy` umgestellt
- [X] Build testen: `mvn clean compile` (Erfolgreich), `mvn test` (JUnit 5 sicherstellen)
- [X] Build testen: `mvn clean package` (JAR-Erstellung in /target)
- [X] JAR laufen lassen: `java -jar target/backend-v3.01-SNAPSHOT.jar` (Konfiguration auf localhost angepasst)
- [X] **Datenbank-Anbindung**: MySQL Docker-Container angebunden
- [X] **Umgebungs-Trennung**: Gelöst via src/test/resources und Docker-Env-Overrides

## 3. Architektur-Vorbereitung
- [ ] **DTO-Standard**: Festlegen von Java 21 Records für konsistente API-Antworten (Weg von `Map<String, String>`)
- [ ] **SpringDoc**: Dependency `springdoc-openapi-starter-webmvc-ui` hinzufügen
- [ ] **Swagger-UI**: API unter `/swagger-ui.html` verifizieren
- [X] **Frontend-Setup**: Basis-Projekt mit **Vite + React + TypeScript** aufsetzen
- [ ] **Global Exception Handling**: `@ControllerAdvice` im Backend für saubere Fehler-Responses

## 4. Vertikale Migration (Feature-Slices)
*Vorgehen pro Modul: Backend Refactor (Service/Repo) → API/DTO Design → Frontend Integration*

- [X] **Slice 1: Nutzerverwaltung & Auth**
    - [X] Backend: Login-Logik von IDs auf JWT/Session umstellen
    - [X] API: `/api/v1/nutzer/login` und `/register` stabilisieren
    - [X] Frontend: Login-Seite und Registrierung
- [ ] **Slice 2: Lehrveranstaltungen & Materialien**
    - Backend: Suche und Kurslisten-Logik refactoren
    - API: `GET /lehrveranstaltung/all` und `/view/{id}`
    - Frontend: Dashboard und Kurs-Detailansicht
- [ ] **Slice 3: Freundschaftssystem & Kommunikation**
    - Backend: `FreundschaftService` aufräumen (Weg mit "dirty" Logik)
    - API: V2 Endpunkte für Requests und Chat
    - Frontend: Freundesliste und Messenger-UI
- [ ] **Slice 4: Projektgruppen & Aufgaben**
    - Backend: ToDo-Logik und Teilnehmerlisten
    - API: V1 Endpunkte `/lehrveranstaltung/todo/*`
    - Frontend: Gruppen-Ansicht mit Task-Board
- [ ] **Slice 5: Lernsystem (Quiz & Kalender)**
    - Backend: Quiz-Logik und Termin-Erinnerungen
    - API: V2 Kalender und Quiz-Endpoints
    - Frontend: Kalender-Widget und Quiz-Interface

## 5. H2-Testdaten und Tests (begleitend)
- [ ] Test-Konfiguration: src/test/resources/application.properties (H2 in-memory)
- [ ] Integrationstests: Pro Slice einen `@SpringBootTest` mit Testdaten

## 6. Dockerization
- [ ] Backend-Dockerfile: Multistage (Maven build + **JRE 21** run)
- [ ] Frontend-Dockerfile: Node build + NGINX serve
- [ ] docker-compose.yml: Services für backend, frontend, db (H2 oder Postgres)
- [ ] **Environment-Config**: `.env` Dateien für Frontend-API-URL und Backend-DB-Secrets finalisieren
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

Aktualisiert: 11. April 2026