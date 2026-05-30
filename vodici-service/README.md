# Vodici Service

Mikroservis zadužen za upravljanje licenciranim vodičima u okviru **Eco-Travel & Adventure Management System**.

**Port:** `8086` | **Baza:** H2 in-memory (`vodicidb`) | **Autor:** Vojislav Popović

---

## Opis servisa

Vodici Service omogućuje:
- Kreiranje i upravljanje profilima vodiča
- Upravljanje jezicima koje vodič govori
- Upravljanje sertifikatima vodiča
- Pretragu vodiča po specijalizaciji i jezicima

---

## Zadatak 1 — Entiteti i endpointi

### Model podataka

```
Guide (vodič)
 ├── List<GuideLanguage> (ManyToMany — guide_language)
 └── List<Certification> (OneToMany)

Language (jezik)
GuideLanguage (join entitet sa dodatnim podacima)
Certification (sertifikat)
```

### Endpointi

#### Vodiči (`/api/guides`)

| Metoda | URL | Opis |
|--------|-----|------|
| POST | `/api/guides` | Kreiranje novog vodiča |
| GET | `/api/guides` | Lista svih vodiča |
| GET | `/api/guides/{id}` | Preuzimanje vodiča po ID-u |
| PUT | `/api/guides/{id}` | Ažuriranje podataka vodiča |
| DELETE | `/api/guides/{id}` | Brisanje vodiča |
| POST | `/api/guides/{id}/certifications` | Dodavanje sertifikata vodiču |
| DELETE | `/api/guides/{id}/certifications/{certId}` | Uklanjanje sertifikata |
| POST | `/api/guides/{id}/languages` | Dodavanje jezika vodiču |
| GET | `/api/guides/{id}/languages` | Lista jezika vodiča |

#### Jezici (`/api/languages`)

| Metoda | URL | Opis |
|--------|-----|------|
| POST | `/api/languages` | Kreiranje novog jezika |
| GET | `/api/languages` | Lista svih jezika |
| DELETE | `/api/languages/{id}` | Brisanje jezika |

---

## Zadatak 3 — TraceId i upravljanje greškama

### TraceIdFilter

Automatski generiše ili prosleđuje `X-Trace-ID` header i smešta u MDC za praćenje zahteva kroz sistem.

### Uniformni format grešaka

```json
{
  "timestamp": "2025-04-15T18:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Vodič nije pronađen",
  "traceId": "a1b2c3d4-..."
}
```

---

## Pokretanje

```bash
cd vodici-service
JAVA_HOME=/Users/aleksandar/Library/Java/JavaVirtualMachines/corretto-21.0.8/Contents/Home mvn spring-boot:run
```

H2 baza se inicijalizuje automatski iz `schema.sql` i `data.sql`.

H2 konzola: `http://localhost:8086/h2-console` (JDBC: `jdbc:h2:mem:vodicidb`, user: `sa`)

---

## Swagger UI

```
http://localhost:8086/swagger-ui.html
http://localhost:8086/api-docs
```
