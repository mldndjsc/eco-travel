# Avanture Service

Mikroservis zadužen za upravljanje avanturističkim turama u okviru **Eco-Travel & Adventure Management System**.

**Port:** `8085` | **Baza:** H2 in-memory (`avanturedb`) | **Autor:** Vojislav Popović

---

## Opis servisa

Avanture Service omogućuje:
- Kreiranje i upravljanje avanturističkim turama sa kategorijama, težinom i opremom
- Upravljanje vremenskim slotovima (termini tura)
- Dodelu vodiča turama i selekciju po strategiji (Standard/Expert)
- Recenzije avantura od strane korisnika
- Statistike po turama
- Integraciju sa vodici-service, smestaj-service i booking-service

---

## Zadatak 1 — Entiteti i endpointi

### Model podataka

```
Adventure (avantura)
 ├── AdventureCategory (ManyToOne)
 ├── List<GearRequirement> (OneToMany)
 ├── List<TimeSlot> (OneToMany)
 ├── List<AdventureReview> (OneToMany)
 └── Difficulty (enum: EASY, MEDIUM, HARD, EXTREME)
```

### Endpointi

#### Avanture (`/api/adventures`)

| Metoda | URL | Opis |
|--------|-----|------|
| POST | `/api/adventures` | Kreiranje nove avanture |
| GET | `/api/adventures` | Lista svih avantura |
| GET | `/api/adventures/{id}` | Preuzimanje avanture po ID-u |
| PUT | `/api/adventures/{id}` | Ažuriranje avanture |
| DELETE | `/api/adventures/{id}` | Brisanje avanture |
| GET | `/api/adventures/{id}/stats` | Statistike avanture |
| POST | `/api/adventures/{id}/reviews` | Dodavanje recenzije |
| GET | `/api/adventures/{id}/reviews` | Lista recenzija |

#### Kategorije (`/api/adventure-categories`)

| Metoda | URL | Opis |
|--------|-----|------|
| POST | `/api/adventure-categories` | Kreiranje kategorije |
| GET | `/api/adventure-categories` | Lista kategorija |
| DELETE | `/api/adventure-categories/{id}` | Brisanje kategorije |

#### Vodiči za avanture (`/api/adventures/{id}/guides`)

| Metoda | URL | Opis |
|--------|-----|------|
| GET | `/api/adventures/{id}/guides` | Lista vodiča za avantu (Feign → vodici-service) |
| GET | `/api/adventures/{id}/guides/best` | Najbolji vodič po strategiji |

---

## Zadatak 2 — Dizajn patern, Feign, Resilience4j

### Strategy Pattern — Selekcija vodiča

Implementiran **Strategy** dizajn patern za odabir vodiča za avanturu:

```
GuideSelectionStrategy (interfejs)
 ├── StandardGuideStrategy  — prvi dostupni vodič
 └── ExpertGuideStrategy    — vodič sa najvećim brojem sertifikata
```

### Factory Pattern — Fallback kreatori

Implementiran **Factory** dizajn patern za kreiranje fallback odgovora:

```
FallbackResponseCreator (interfejs)
 ├── GuideFallbackCreator
 ├── GuideListFallbackCreator
 ├── CertificationListFallbackCreator
 ├── UnitListFallbackCreator
 └── BookingListFallbackCreator
```

### OpenFeign integracija

| Klijent | Servis | Port |
|---------|--------|------|
| `VodiciClient` | vodici-service | 8086 |
| `SmestajClient` | smestaj-service | 8081 |
| `BookingClient` | booking-service | 8083 |

### Resilience4j — Circuit Breaker i Retry

Konfigurisano za 3 CB instance: `vodici-client`, `smestaj-client`, `booking-client`
- failureRateThreshold: 50%, slidingWindowSize: 10, maxAttempts: 3

---

## Zadatak 3 — TraceId, Feign integracija

### TraceIdFilter

Automatski generiše ili prosleđuje `X-Trace-ID` header i smešta u MDC.

### FeignHeaderInterceptor

Prosleđuje `X-API-KEY`, `X-Trace-ID`, `X-User-Role`, `X-Authenticated-User` na sve Feign pozive.

### FeignErrorDecoder

Mapira HTTP 400/404/503 na odgovarajuće RuntimeException-e.

---

## Pokretanje

```bash
cd avanture-service
JAVA_HOME=/Users/aleksandar/Library/Java/JavaVirtualMachines/corretto-21.0.8/Contents/Home mvn spring-boot:run
```

H2 baza se inicijalizuje automatski iz `schema.sql` i `data.sql`.

H2 konzola: `http://localhost:8085/h2-console` (JDBC: `jdbc:h2:mem:avanturedb`, user: `sa`)

---

## Swagger UI

```
http://localhost:8085/swagger-ui.html
http://localhost:8085/api-docs
```
