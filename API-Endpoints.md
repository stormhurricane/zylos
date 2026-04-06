# API Dokumentation - Doodle Frontend

Diese Übersicht listet alle REST-Endpunkte auf, die im Interface `NutzerEndpoint.java` für die Kommunikation mit dem Backend definiert sind. Die APIs sind nach Versionen (v1, v2, v3) und Funktionalität gruppiert.

## 1. Nutzerverwaltung & Profile (v1)
| Methode | Pfad | Beschreibung |
| :--- | :--- | :--- |
| POST | `api/v1/nutzer/register?nutzer=student` | Registriert einen neuen Studenten. |
| POST | `api/v1/nutzer/register?nutzer=lehrender` | Registriert einen neuen Lehrenden. |
| GET | `api/v1/nutzer/view/{id}` | Ruft das Profil eines Nutzers ab. |
| PUT | `api/v1/nutzer/update/{id}` | Aktualisiert die Profildaten eines Nutzers. |
| POST | `api/v1/nutzer/findStudent` | Sucht nach Studenten-IDs basierend auf Suchkriterien. |
| POST | `api/v1/nutzer/login` | Führt den Login durch (gibt ID oder -1 zurück). |
| POST | `api/v1/nutzer/verify/{id}` | Verifiziert den Login mit einem generierten Code. |
| GET | `api/v1/nutzer/all` | Listet alle registrierten Nutzer auf (Testmethode). |

## 2. Lehrveranstaltungen & Materialien (v1)
| Methode | Pfad | Beschreibung |
| :--- | :--- | :--- |
| GET | `api/v1/lehrveranstaltung/view{id}` | Lädt Details einer Lehrveranstaltung. |
| GET | `api/v1/lehrveranstaltung/all` | Listet alle verfügbaren Lehrveranstaltungen auf. |
| POST | `api/v1/lehrveranstaltung/find` | Sucht nach einer LV via Map-Kriterien. |
| GET | `api/v1/lehrveranstaltung/find/{id}` | Sucht eine spezifische LV per ID. |
| GET | `api/v1/lehrveranstaltungmaterial/find/{id}` | Lädt Lehrmaterialien einer LV. |
| POST | `api/v1/lehrveranstaltungsmaterial/hinzufugen` | Erstellt ein neues Lehrmaterial. |
| POST | `api/v1/lehrveranstaltung/create` | Erstellt eine neue Lehrveranstaltung. |

## 3. Teilnehmerliste & Projektgruppen (v1)
| Methode | Pfad | Beschreibung |
| :--- | :--- | :--- |
| POST | `api/v1/teilnehmerliste/check/{id}` | Prüft die Teilnahme eines Nutzers an einer PG. |
| POST | `api/v1/teilnehmerliste/join` | Tritt einer Veranstaltung bei. |
| GET | `api/v1/teilnehmerliste/lv/{id}` | Lädt die Teilnehmerliste einer LV. |
| GET | `api/v1/teilnehmerliste/nutzer/{id}` | Lädt alle LVs, an denen ein Nutzer teilnimmt. |
| POST | `api/v1/teilnehmerliste/studentOf/{id}` | Prüft, ob ein Student Teilnehmer bei einem Lehrenden ist. |
| POST | `api/v1/teilnehmerliste/addStudent` | Fügt einen Studenten einer Projektgruppe hinzu. |

## 4. Freundschaftssystem & Chats (v2)
| Methode | Pfad | Beschreibung |
| :--- | :--- | :--- |
| GET | `api/v2/friends/show/{id}` | Zeigt die Freundesliste eines Nutzers. |
| POST | `api/v2/friends/sendRequest/{id}` | Sendet eine Freundschaftsanfrage. |
| POST | `api/v2/friends/respond` | Beantwortet eine Freundschaftsanfrage. |
| GET | `api/v2/friends/openRequests/{id}` | Zeigt offene Anfragen eines Nutzers. |
| POST | `api/v2/privatechat/send` | Sendet eine private Nachricht. |
| POST | `api/v2/privatechat/with/{id}` | Lädt den Chatverlauf mit einem Nutzer. |
| GET | `api/v2/privatechat/show/{id}` | Listet alle aktiven Chats eines Nutzers auf. |

## 5. Projektgruppen-Interaktion (v1)
| Methode | Pfad | Beschreibung |
| :--- | :--- | :--- |
| GET | `api/v1/lehrveranstaltung/chat/{id}` | Lädt den Chatverlauf einer Projektgruppe. |
| POST | `api/v1/lehrveranstaltung/chat/post` | Sendet eine Nachricht in den Gruppenchat. |
| GET | `api/v1/lehrveranstaltung/todo/{id}` | Zeigt die ToDo-Liste einer Gruppe. |
| POST | `api/v1/lehrveranstaltung/todo/add` | Fügt ein neues ToDo hinzu. |
| PUT | `api/v1/lehrveranstaltung/todo/done` | Markiert ein ToDo als erledigt. |
| PUT | `api/v1/lehrveranstaltung/todo/change/{id}` | Ändert den Inhalt eines ToDos. |
}


## 6.  & Quiz (v1 & v2)
| Methode | Pfad | Beschreibung |
| :--- | :--- | :--- |
| POST | `api/v1/lehrveranstaltung/lernkartenThema/{id}` | Erstellt ein neues Thema für Lernkarten. |
| GET | `api/v1/lehrveranstaltung/lernkartenThemaListe/{id}` | Listet Themen für Lernkarten einer LV auf. |
| POST | `api/v1/lehrveranstaltung/lernkarte` | Erstellt eine neue Lernkarte. |
| GET | `api/v1/lehrveranstaltung/lernkarten/{id}` | Lädt Lernkarten zu einem Thema. |
| GET | `api/v2/quiz/showFrom/{id}` | Zeigt alle Quizze einer LV. |
| POST | `api/v2/quiz/forceSemester` | Erzwingt eine Bestehensprüfung. |
| POST | `api/v2/quiz/new` | Erstellt einen neuen Quiz. |
| GET | `api/v2/quiz/findeFrageMitId/{id}` | Sucht eine spezifische Frage per ID. |
| POST | `api/v2/quiz/feedback` | Erstellt ein neues Feedback |
| POST | `api/v2/quiz/feedbackVersuch` | Erstellt Feedback für einen neuen Versuch |
| GET | `api/v2/quiz/showFeedback/{id}` | Lädt Feedback für einen Versuch. |
| POST | `api/v2/quiz/versuch` | Erstellt einen neuen Versuch. |
| PUT | `api/v2/quiz/pruefeVersuch` | Prüft einen Versuch |
| GET | `api/v2/quiz/statistik/{id}` | Zeigt Statistiken für einen Test. |


## 7. Kalender & Termine (v2)
| Methode | Pfad | Beschreibung |
| :--- | :--- | :--- |
| POST | `api/v2/calender/termineEinesDatums/{id}` | Lädt Termine für ein bestimmtes Datum. |
| POST | `api/v2/calender/reminderFuerPopUpSchicken/{id}` | Lädt fällige Erinnerungen für Popups. |
| POST | `api/v2/calender/erstelleTermin` | Erstellt einen neuen Termin. |
| POST | `api/v2/calender/reminderEinesTermins` | Erstellt eine neue Erinnerung für einen Termin. |
| GET | `api/v2/calender/terminVonReminder/{id}` | Ruft den zugehörigen Termin einer Erinnerung ab. |

## 8. Evaluation & Themenangebote (v3)
| Methode | Pfad | Beschreibung |
| :--- | :--- | :--- |
| GET | `api/v3/topic/showAllTopics/{id}` | Zeigt alle Arbeitsthemen eines Lehrenden. |
| POST | `api/v3/topic/addTopic` | Erstellt eine neue Arbeitsthema. |
| GET | `api/v3/lvBewertung/showQuestions/{id}` | Lädt die Fragen für eine LV-Bewertung. |
| POST | `api/v3/lvBewertung/create` | Erstellt eine neue LV-Bewertung. |
| POST | `api/v3/lvBewertung/check/{id}` | Erstellt einen neuen Bewertungsversuch. |
| POST | `api/v3/lvBewertung/createFeedback` | Speichert das Feedback für einen Versuch. |
| POST | `api/v3/lvBewertung/createFeedbackStatistics/{id}` | Erstellt Statistiken für eine Evaluation. |
| POST | `api/v3/lvBewertung/checkParticipation/{id}` | Prüft die Teilnahme eines Studenten an der Bewertung | 

---
*Hinweis: Diese Dokumentation wurde automatisch aus dem Retrofit-Interface generiert.*