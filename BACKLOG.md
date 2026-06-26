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