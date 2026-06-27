# Project Backlog & Roadmap

## 1. Bugs & Hotfixes (Sofortige Priorität)
- [ ] **Material Upload Fehleranzeige:** Frontend zeigt keine Fehlermeldungen an, wenn der Backend-Upload wegen fehlender Datei oder fehlendem Titel fehlschlägt. [High]

## 2. Security & Architektur (Muss vor dem Release sitzen)
- [ ] **JWT zu HTTPOnlyCookies:** Kritisch gegen XSS-Angriffe. [High]
- [ ] **Streaming für Materialien:** Umstellung von byte[] auf Streams (verhindert OutOfMemoryErrors bei großen PDFs). [High]
- [ ] **Refresh Token Flow:** Verhindert, dass User nach Ablauf des Access Tokens ausgeloggt werden. [Medium]
- [ ] **Admin-Rolle einführen:** Rollenkonzept erweitern (Backend-Guards anpassen). [Medium]

## 3. Refactoring & Code Quality (Technische Schulden)
- [ ] **useCourseDetails aufteilen:** In 3 spezialisierte Hooks trennen (Frontend-Clean-Code). [Medium]
- [ ] **Backend Mocking für Frontend Dev:** Ermöglicht paralleles Arbeiten ohne laufendes Java-Backend. [Low]
- [ ] **CSV-Import Fehlerbehandlung:** Fehler pro Zeile loggen statt lautlos verschlucken. Option prüfen, fehlgeschlagene Zeilen im Response-Body zu sammeln, damit der User Feedback erhält. [Medium]

## 4. Neue Features & UI-Optimierungen

### Course / Lehrveranstaltungen (LV)
- [ ] **Suche:** LV per Titel suchen (Endpunkt im Backend + Suchleiste im Frontend). [High]
- [ ] **Kurs-Abmeldung:** Feature "Vom Kurs abmelden" (Controller-Methode + UI-Button). [High]
- [ ] **Kurs-Sichtbarkeit:** Feld für "Nicht Auffindbar" (Archiviert/Privat-Flag in Course-Entity). [Medium]
- [ ] **CourseList & Courses:** Sortierung einbauen (z. B. nach Semester oder Alphabet). [Medium]
- [ ] **Teilnehmerliste:** Namen in CourseDetails anklickbar machen (Verlinkung auf Profile). [Low]

### User Experience & Feedback
- [ ] **Rückmeldung bei Einschreiben:** Visuelles Feedback (Erfolgs-Screen oder Modal). [High]
- [ ] **Toastify:** Globale Benachrichtigungen für Fehler/Erfolge integrieren. [Medium]
- [ ] **PDF Viewer:** Materialien direkt im Browser anzeigen anstatt Download-Zwang. [Medium]
- [ ] **Profile-UX:** Cursor/Mouse-Zeiger bei Kursliste im eigenen Profil anpassen. [Low]
- [ ] **Dark Mode:** Design-Erweiterung. [Low]

## TICKET: Infra - Schema-Migration mit Flyway & DB-Sequence für Produktion absichern

### Beschreibung
Aktuell läuft die Anwendung lokal auf 'create-drop' mit einer 'data.sql'. Für die Concurrency-Sicherheit der Matrikelnummern wurde das Design im `UserService` auf eine native DB-Sequence umgestellt. Diese Sequence existiert aktuell nur temporär im lokalen Speicher. Für den Produktionsbetrieb muss eine versionierte Schema-Migration eingeführt werden.

### Akzeptanzkriterien (Definition of Done)
1. [ ] 'flyway-core' Dependency ist in der `pom.xml` integriert.
2. [ ] Die erste Migrationsdatei `V1__init_schema.sql` (bzw. Basisschema) ist unter `src/main/resources/db/migration/` angelegt.
3. [ ] Die Datei `V2__add_matriculation_sequence.sql` ist angelegt und enthält den Befehl:
   `CREATE SEQUENCE IF NOT EXISTS student_matriculation_seq START WITH 10000000 INCREMENT BY 1;`
4. [ ] Die Profile sind getrennt: 
   - `application-dev.yml` nutzt weiterhin `ddl-auto: create-drop` und führt `data.sql` aus.
   - `application-prod.yml` nutzt `ddl-auto: validate` und aktiviert Flyway (`spring.flyway.enabled: true`).
5. [ ] Ein lokaler Test-Build läuft mit aktiviertem Flyway-Profil fehlerfrei gegen eine Test-DB durch.