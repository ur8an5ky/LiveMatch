# LiveMatch - Panel Wyników Piłkarskich na Żywo

Interaktywna platforma webowa do śledzenia wyników i zdarzeń meczów piłkarskich w czasie rzeczywistym. Projekt zaliczeniowy z przedmiotu **Zaawansowane Technologie Internetowe (ZTI)** - kierunek Informatyka Stosowana, AGH 2025/2026.

Autor: **Jakub Urbański**

---

## Spis treści

- [Stos technologiczny](#stos-technologiczny)
- [Wymagania](#wymagania)
- [Szybki start](#szybki-start)
- [Konfiguracja](#konfiguracja)
- [Struktura projektu](#struktura-projektu)
- [Domyślne dane logowania](#domyślne-dane-logowania)
- [Testy](#testy)
- [Wdrożenie na maszynie AGH](#wdrożenie-na-maszynie-agh)
- [Dokumentacja](#dokumentacja)

---

## Stos technologiczny

### Backend
- **Java 21**, **Spring Boot 3.5.14**
- **Spring Data JPA** + Hibernate (ORM)
- **Spring Security** + **JWT** (autentykacja stateless)
- **Spring WebSocket** + STOMP (komunikacja real-time)
- **Spring AOP** (audyt operacji)
- **PostgreSQL 16** (baza danych)
- **Maven** (build)
- **JUnit 5** + **Mockito** (testy)

### Frontend
- **React 19** + **Vite 8**
- **Tailwind CSS** + **shadcn/ui** (komponenty)
- **react-router-dom** (routing)
- **axios** (HTTP)
- **@stomp/stompjs** (WebSocket)
- **i18next** (internacjonalizacja PL/EN)
- **sonner** (toasty), **next-themes** (tryb ciemny)

### Infrastruktura
- **Docker** + **Docker Compose** (konteneryzacja)
- **nginx** (serwer statyczny + reverse proxy)

---

## Wymagania

Do uruchomienia z Dockerem wystarczy:
- **Docker** (≥ 20.10)
- **Docker Compose** (v1 lub v2)

Do uruchomienia w trybie deweloperskim (bez Dockera):
- **JDK 21**
- **Node.js 20+**
- **PostgreSQL 16** (lokalnie lub w kontenerze)
- **Maven 3.9+**

---

## Szybki start

### Wariant 1 - pełny stack lokalnie w Docker (rekomendowany)

```bash
git clone https://github.com/ur8an5ky/LiveMatch.git
cd LiveMatch
docker compose up -d --build
```

Po zakończeniu buildu aplikacja będzie dostępna pod adresem:

```
http://localhost
```

Aby zatrzymać:
```bash
docker compose down          # zatrzymuje kontenery
docker compose down -v       # zatrzymuje i usuwa wolumeny (np. bazę)
```

### Wariant 2 - tryb deweloperski (hot reload)

**Backend:**
```bash
# Wymaga uruchomionego PostgreSQL na localhost:5432 z bazą `livematch` (user: livematch, hasło: livematch_dev_pass)
mvn spring-boot:run
# Backend dostępny pod http://localhost:8081
```

**Frontend (osobny terminal):**
```bash
cd client
npm install
npm run dev
# Frontend dostępny pod http://localhost:5173 z hot reloadem
```

---

## Konfiguracja

### Plik kompozycji Docker

W repozytorium znajduje się plik **`docker-compose.yml`** skonfigurowany pod wdrożenie na maszynie wirtualnej AGH. Definiuje dwa serwisy: `app` (backend Spring Boot) oraz `web` (frontend + nginx). Baza danych nie jest uruchamiana w kontenerze - wykorzystywany jest zewnętrzny serwer PostgreSQL AGH (`pascal.fis.agh.edu.pl`), gdzie każdy student posiada własną instancję bazy o nazwie odpowiadającej loginowi.

### Zmienne środowiskowe backendu

Skonfigurowane w `docker-compose.yml` (sekcja `environment` serwisu `app`), z domyślnymi wartościami w `src/main/resources/application.properties`:

| Zmienna | Wartość (AGH) | Opis |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://pascal.fis.agh.edu.pl:5432/u0urbanski` | URL bazy danych AGH |
| `SPRING_DATASOURCE_USERNAME` | `u0urbanski` | login (analogiczny dla każdego studenta) |
| `SPRING_DATASOURCE_PASSWORD` | `0urbanski` | hasło (konwencja AGH dla grupy ZTI) |
| `APP_JWT_SECRET` | (zakodowany w properties) | sekret BASE64 do podpisywania tokenów JWT |
| `APP_JWT_EXPIRATION_MS` | `86400000` (24h) | czas życia tokenu w milisekundach |

> Dane logowania do bazy są **jawne i wspólne dla całej grupy ZTI** - każdy student posiada analogiczne, oparte na własnym loginie AGH.

### Zmienne środowiskowe frontendu

W trybie dev odczytywane z `client/.env.development`:

```bash
VITE_API_URL=http://localhost:8081
VITE_WS_URL=ws://localhost:8081/ws
```

W produkcji (Docker) frontend używa **względnych ścieżek** (`/api/...`, `/ws`) - nginx pełni rolę reverse proxy.

---

## Struktura projektu

```
LiveMatch/
├── src/main/java/pl/ur8an5ky/livematch/
│   ├── aop/                  # programowanie aspektowe (Auditable, AuditAspect)
│   ├── config/               # DataInitializer, WebConfig, WebSocketConfig
│   ├── controller/           # REST controllers (Auth, Match, MatchEvent, Team)
│   ├── domain/               # encje JPA (Match, Team, MatchEvent, User, AuditLog…)
│   ├── dto/                  # Data Transfer Objects (record)
│   ├── exception/            # custom exceptions + GlobalExceptionHandler
│   ├── mapper/               # MapStruct-style mappery
│   ├── repository/           # interfejsy Spring Data JPA
│   ├── security/             # JWT (JwtUtil, JwtAuthFilter, SecurityConfig)
│   └── service/              # logika biznesowa
├── src/main/resources/
│   ├── application.properties
│   └── data.sql              # seed: Mistrzostwa Świata 2026
├── src/test/                 # testy JUnit
├── client/                   # frontend React + Vite
│   ├── src/
│   │   ├── components/       # komponenty React (w tym shadcn/ui)
│   │   ├── pages/            # widoki (Login, MatchList, MatchDetails, Admin…)
│   │   ├── services/         # warstwa API (axios)
│   │   ├── hooks/            # custom hooks (useMatchUpdates, useDateFormat…)
│   │   ├── contexts/         # React Context (Auth, Theme)
│   │   ├── locales/          # tłumaczenia PL/EN (i18next)
│   │   └── lib/              # narzędzia pomocnicze
│   ├── Dockerfile            # multi-stage build (node → nginx)
│   ├── nginx.conf            # reverse proxy + SPA fallback
│   └── package.json
├── docs/                     # dokumentacja LaTeX, diagramy
├── Dockerfile                # backend multi-stage build (maven → jre)
├── docker-compose.yml        # lokalna kompozycja
├── docker-compose.agh.yml    # kompozycja na maszynę AGH
└── README.md
```

---

## Domyślne dane logowania

Przy pierwszym uruchomieniu aplikacja automatycznie tworzy konto administratora przez `DataInitializer`:

```
login:    admin
hasło:    admin123
rola:     ROLE_ADMIN
```

> ⚠️ W środowisku produkcyjnym należy te dane natychmiast zmienić.

---

## Testy

Uruchomienie testów backendowych:
```bash
mvn test
```

Aktualny stan: **20/20 testów przechodzi**. Testy obejmują:
- Logikę biznesową (`MatchService`, `MatchEventService`, `TeamService`, `AuthService`)
- Walidację reguł domeny (np. zakaz meczu drużyny z samą sobą)
- Mapowanie encji do DTO
- Filtry bezpieczeństwa

---

## Wdrożenie na maszynie AGH

Aplikacja została wdrożona na udostępnionej maszynie wirtualnej AGH.

### Adres maszyny

Maszyna dostępna z sieci wewnętrznej AGH przez serwer dostępowy **Taurus**:

```
adres maszyny:    172.20.41.52
port aplikacji:   8090
```

Pełny URL po połączeniu z Taurusem:
```
http://172.20.41.52:8090
```

### Procedura wdrożenia

1. Połączenie z maszyną przez RDP (przez Taurus)
2. Klonowanie repozytorium:
   ```bash
   git clone https://github.com/ur8an5ky/LiveMatch.git
   cd LiveMatch
   ```
3. Uruchomienie:
   ```bash
   docker-compose -f docker-compose.agh.yml up -d --build
   ```

Maszyna współdzieli infrastrukturę grupy ZTI (kontener `zti01_haproxy_1` zajmuje port 80), dlatego serwis web wystawiony jest na port **8090**.

---

## Dokumentacja

Pełna dokumentacja projektu (LaTeX, PDF) znajduje się w katalogu `docs/`:

- `docs/dokumentacja.pdf` - dokumentacja końcowa
- `docs/diagrams/` - diagramy UML (PlantUML + PNG)
- `docs/screens/` - zrzuty ekranu aplikacji