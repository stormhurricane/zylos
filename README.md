# Zylos - Lernplattform

![CI Status](https://github.com/stormhurricane/zylos/actions/workflows/ci.yml/badge.svg)
![Code Coverage](https://codecov.io/gh/stormhurricane/zylos/branch/main/graph/badge.svg)

Zylos ist eine moderne Fullstack-Webanwendung zur Organisation von Lehrveranstaltungen, Projektgruppen und Lernmaterialien. Das Projekt wird aktuell von einer JavaFX-Desktop-Anwendung zu einer zeitgemäßen Web-Architektur migriert.

## 🚀 Tech Stack
- **Backend:** Java 21, Spring Boot 3.2, Maven, MySQL
- **Frontend:** React 18, TypeScript, Vite, Axios
- **Infrastruktur:** Docker, GitHub Actions (CI/CD)

## 📋 Voraussetzungen
Bevor du startest, stelle sicher, dass folgende Tools installiert sind:
- **Java JDK 21** (z.B. Temurin)
- **Node.js 20.x** & npm
- **Docker & Docker Compose**

## 🛠️ Entwicklungsumgebung starten

| Service | URL | Beschreibung |
| :--- | :--- | :--- |
| **Frontend** | `http://localhost:5173` | Vite Dev Server |
| **Backend** | `http://localhost:8080` | Spring Boot API |
| **Swagger UI** | `http://localhost:8080/swagger-ui/index.html` | API Dokumentation |
| **MySQL** | `localhost:3306` | Datenbank (User: root/root) |

### 1. Datenbank (Docker)
```bash
docker compose up -d mysql
```

### 2. Backend
```bash
cd backend
mvn spring-boot:run
```

### 3. Frontend
Erstelle eine `.env` Datei im `/frontend` Verzeichnis:
`VITE_API_URL=http://localhost:8080/api/v1`

```bash
cd frontend
npm install
npm run dev
```


## 🏗️ Projektstruktur

### Frontend (`/frontend`)
```text
src/
├── assets/     # Statische Dateien (Bilder, globale CSS-Themes)
├── api/        # Zentralisierte Axios-Clients & TypeScript-Interfaces
├── components/ # Atomare, wiederverwendbare UI-Komponenten
├── context/    # Globaler State Management (z.B. AuthContext)
├── hooks/      # Custom React Hooks (z.B. useAuth, useFetch)
├── pages/      # Vertikale Slices / Hauptansichten (Login, Dashboard)
├── utils/      # Hilfsfunktionen (Validierung, Datumsformatierung)
└── App.tsx     # Zentrales Routing und Provider-Setup
```

### Backend (`/backend`)
```text
src/main/java/.../
├── config/     # Security, CORS, Swagger/OpenAPI Setup
├── controller/ # REST-Schnittstellen (v1, v2)
├── exception/  # GlobalExceptionHandler (@ControllerAdvice)
├── model/      # Datenmodelle (dto/ für API, entity/ für DB)
├── repository/ # Spring Data JPA Interfaces
├── security/   # JWT-Filter und Auth-Provider
└── service/    # Geschäftslogik und Transaktionsmanagement
```

## 🧪 Testing & Qualität

**Lokal ausführen:**
```bash
# Backend Tests
cd backend && mvn test

# Frontend Tests & Linting
cd frontend && npm test && npm run lint
```

Die **GitHub Actions** Pipeline validiert jeden Push auf `main` und alle Pull Requests.

## 📅 Migrations-Status
Der Fortschritt wird detailliert in der `todo.md` gepflegt.

*   **Slice 1:** Nutzerverwaltung & Authentifizierung ✅
*   **Slice 2:** Lehrveranstaltungen & Materialien 🔄

---
*Hinweis: Architektur-Entscheidungen orientieren sich an modernen Cloud-Native Standards.*
