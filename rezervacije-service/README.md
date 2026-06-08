***

# 🏨 Rezervacije Mikroservis (Rezervacije Service)


## 1. Tehničke specifikacije i opis poslovne logike

### Tehnološki stek
* **Framework:** Spring Boot (Java)
* **Baza podataka:** H2 (In-Memory relacionalna baza)
* **ORM:** Spring Data JPA / Hibernate
* **Arhitekturni šablon:** Event-Driven Architecture (Observer Pattern)
* **Dokumentacija API-ja:** Swagger / OpenAPI 3


---

## 2. Mrežni port

Aplikacija je konfigurisana da operiše na portu:
* **Port:** `8082`

---

## 3. Prikaz svih implementiranih endpointa

### 📅 Bookings (Rezervacije)
| HTTP Metoda | Endpoint                                | Opis funkcionalnosti |
| :--- |:----------------------------------------| :--- |
| `POST` | `/api/rezervacije/bookings/add`         | Kreira novu rezervaciju uz striktnu `Fail-Fast` validaciju (provera gostiju, datuma i iznosa). |
| `PUT` | `/api/rezervacije/bookings/update/{id}` | Ažurira podatke postojeće rezervacije. |
| `POST` | `/api/rezervacije/bookings/{id}/cancel`             | Otkazuje rezervaciju i **vraća izračunati iznos za povraćaj (Refund)** na osnovu politike otkazivanja. |
| `PUT` | `/api/rezervacije/bookings/{id}/status`             | Ručna izmena statusa rezervacije (npr. u `CONFIRMED`). |
| `PUT` | `/api/rezervacije/bookings/{id}/cancel-unpaid`      | Saga Endpoint: Otkazuje rezervaciju usled neplaćanja (poziva ga Finansijski MS). |
| `GET` | `/api/rezervacije/bookings/search`                  | Pametna pretraga rezervacija. Parametri: `guestName` (ime gosta) i `status`. |
| `GET` | `/api/rezervacije/bookings/{id}`                    | Vraća detalje jedne rezervacije. |
| `GET` | `/api/rezervacije/bookings/all`                     | Vraća sve rezervacije u sistemu. |
| `DELETE` | `/api/rezervacije/bookings/delete/{id}`             | Briše rezervaciju iz baze. |

### 👤 Guests (Gosti)
| HTTP Metoda | Endpoint | Opis funkcionalnosti |
| :--- | :--- | :--- |
| `POST` | `/api/rezervacije/guests/add` | Dodavanje novog gosta u bazu. |
| `PUT` | `/api/rezervacije/guests/update/{id}` | Ažuriranje ličnih podataka gosta (ime, prezime, email). |
| `GET` | `/api/rezervacije/guests/{id}` | Dohvatanje podataka o specifičnom gostu. |
| `GET` | `/api/rezervacije/guests/all` | Prikaz svih registrovanih gostiju. |
| `DELETE` | `/api/rezervacije/guests/delete/{id}`| Brisanje gosta iz sistema. |

### 📜 Cancellation Policies (Politike otkazivanja)
| HTTP Metoda | Endpoint | Opis funkcionalnosti |
| :--- | :--- | :--- |
| `POST` | `/api/rezervacije/cancellationpolicies/add` | Definisanje nove politike (npr. *Strict*, *Flexible*) i procenta povraćaja. |
| `PUT` | `/api/rezervacije/cancellationpolicies/update/{id}`| Ažuriranje naziva politike ili izmena procenta povraćaja novca. |
| `GET` | `/api/rezervacije/cancellationpolicies/{id}` | Prikaz detalja određene politike. |
| `GET` | `/api/rezervacije/cancellationpolicies/all` | Prikaz svih postojećih politika otkazivanja. |
| `DELETE` | `/api/rezervacije/cancellationpolicies/delete/{id}`| Brisanje politike otkazivanja. |

### 📝 Booking Status Logs (Istorija promene statusa)

| HTTP Metoda | Endpoint | Opis funkcionalnosti |
| :--- | :--- | :--- |
| `POST` | `/api/rezervacije/bookingstatuslogs/add` | Ručno dodavanje loga o promeni statusa. |
| `PUT` | `/api/rezervacije/bookingstatuslogs/update/{id}`| Ažuriranje postojećeg loga. |
| `GET` | `/api/rezervacije/bookingstatuslogs/{id}` | Dohvatanje specifičnog loga na osnovu ID-a. |
| `GET` | `/api/rezervacije/bookingstatuslogs/all` | Prikaz celokupne istorije promena statusa rezervacija. |
| `DELETE` | `/api/rezervacije/bookingstatuslogs/delete/{id}`| Brisanje specifičnog loga iz baze. |

### ➕ Add-On Items (Dodatne usluge)
| HTTP Metoda | Endpoint | Opis funkcionalnosti |
| :--- | :--- | :--- |
| `POST` | `/api/rezervacije/addonitems/add` | Dodavanje usluge (npr. Doručak, Spa) na postojeću rezervaciju. |
| `PUT` | `/api/rezervacije/addonitems/update/{id}` | Ažuriranje cene ili naziva dodatne usluge. |
| `GET` | `/api/rezervacije/addonitems/{id}` | Prikaz jedne dodatne usluge. |
| `GET` | `/api/rezervacije/addonitems/all` | Prikaz svih dodeljenih dodatnih usluga. |
| `DELETE` | `/api/rezervacije/addonitems/delete/{id}` | Uklanjanje usluge sa rezervacije. |

---

## 4. Instrukcije za lokalno pokretanje i testiranje

### Preduslovi
* Java 17 (ili novija)
* Maven

### Pokretanje aplikacije
Aplikaciju možete pokrenuti direktno iz vašeg IDE-a (IntelliJ IDEA / Eclipse) pokretanjem glavne klase, ili putem terminala koristeći komandu:
Napomena: Prvo morate pokrenuti Config servis
```bash
mvn spring-boot:run
```

### Baza podataka (H2)
Aplikacija koristi H2 in-memory bazu koja se kreira pri svakom pokretanju (`create-drop`).
Bazi možete pristupiti putem ugrađene web konzole:
* **URL:** `http://localhost:8082/h2-console`
* **JDBC URL:** `jdbc:h2:mem:rezervacijedb`
* **Username:** `md`
* **Password:** *(ostaviti prazno)*

### Testiranje API-ja (Swagger)
Za lakše testiranje, vizuelni pregled endpointa i testiranje kalkulacije povraćaja novca, konfigurisan je Swagger UI. Nakon pokretanja aplikacije, otvorite browser i idite na:
* **Swagger interfejs:** `http://localhost:8082/swagger-ui.html`

---

## 5. Dokerizacija i konfiguracija (Zadatak 1)

### Multi-stage Dockerfile

Servis koristi **multi-stage Docker build** koji razdvaja fazu kompajliranja od runtime okruženja:

* **Build faza** —  Prvo se instalira `shared-module` (zajednička biblioteka sa event klasama), a zatim se `pom.xml` kopira odvojeno kako bi Docker keširao dependency layer i ubrzao ponovne buildove.

Rezultat je produkciona slika koja ne sadrži Maven, JDK, source kod niti privremene kompajlirne fajlove.

### Eksternalizacija konfiguracije

Port i adresa Config servisa nisu hardkodovani, već se preuzimaju iz environment varijabli:

```
SERVER_PORT=8082
CONFIG_SERVER_URL=http://config-service:8888
```

Aplikacija dinamički učitava svu preostalu konfiguraciju (baza, RabbitMQ, Eureka, Loki, tracing) sa **Spring Cloud Config Servera** pri pokretanju. Lokalna `application.properties` sadrži samo ime servisa i `spring.config.import` direktivu.

### Docker Compose i zavisnosti

Servis se podiže kao deo zajedničke Docker Compose orksetracije. Konfigurisan je `depends_on` sa `healthcheck` uslovom koji garantuje da aplikativni kontejner ne stupa u operativno stanje pre nego što baza podataka potvrdi svoju punu dostupnost.

---

## 6. Asinhrona komunikacija (Zadatak 2)

### Observer pattern (lokalni događaji)

`BookingsService` implementira interfejs `BookingPublisher` i održava listu `BookingSubscriber` objekata. Kada se status rezervacije promeni (npr. nakon evidentirane uplate), metoda `notifyObservers()` poziva sve pretplatnike.

`BookingStatusLogSubscriber` je jedini registrovani pretplatnik — automatski zapisuje promenu statusa u `BookingStatusLogs` tabelu bez direktnog poziva iz servisa.

```
BookingsService.markPaymentRecorded()
    └─► notifyObservers(bookingId, "PENDING", "PAYMENT_RECORDED")
            └─► BookingStatusLogSubscriber.update()
                    └─► BookingStatusLogsService.logStatusChange()
```

### RabbitMQ — asinhroni tokovi poruka

Servis je integrisan sa RabbitMQ brokerom i učestvuje u sledećim asinhronim tokovima:

#### 1. Primanje obaveštenja o transakciji (od Finansije servisa)

| Komponenta | Vrednost |
| :--- | :--- |
| Queue | `rezervacije.finance-events.queue` |
| Exchange | `hotel.events.exchange` (Topic) |
| Routing Key | `finance.transaction.created` |
| Listener | `TransactionCreatedEventListener` |
| Event | `TransactionCreatedEvent` |

Kada Finansije servis kreira transakciju, šalje `TransactionCreatedEvent`. Ovaj servis prima poruku i poziva `bookingsService.markPaymentRecorded(bookingId)`, čime se status rezervacije menja u `PAYMENT_RECORDED` i pokreće Observer chain koji to loguje.

#### 2. Slanje feedback-a Finansije servisu (nakon evidencije uplate)

| Komponenta | Vrednost |
| :--- | :--- |
| Queue | `finansije.reservation-feedback.queue` |
| Exchange | `hotel.feedback.exchange` (Topic) |
| Routing Key | `reservation.payment-recorded.feedback` |
| Publisher | `ReservationFeedbackPublisher` |
| Event | `ReservationPaymentRecordedFeedbackEvent` |

Odmah nakon što evidentira uplatu, servis šalje povratni događaj Finansije servisu kako bi mu potvrdio obradu.

#### 3. Obaveštavanje Maintenance servisa o novoj rezervaciji

| Komponenta | Vrednost |
| :--- | :--- |
| Queue | `maintenance.booking-events.queue` |
| Exchange | `maintenance.events.exchange` (Topic) |
| Routing Key | `reservation.booking.created` |
| Publisher | `BookingMaintenancePublisher` |
| Event | `BookingCreatedMaintenanceEvent` |

Svaki put kada se kreira nova rezervacija (`BookingsService.save()`), servis automatski šalje `BookingCreatedMaintenanceEvent` sa `bookingId`, `unitId`, datumima i statusom. Maintenance servis to koristi za kreiranje naloga za održavanje smeštajne jedinice.

#### 4. Primanje feedback-a od Maintenance servisa

| Komponenta | Vrednost |
| :--- | :--- |
| Queue | `rezervacije.maintenance-feedback.queue` |
| Exchange | `maintenance.feedback.exchange` (Topic) |
| Routing Key | `maintenance.work-order.created.feedback` |
| Listener | `MaintenanceFeedbackListener` |
| Event | `MaintenanceWorkOrderCreatedFeedbackEvent` |

Kada Maintenance servis kreira radni nalog, šalje povratni događaj. Ovaj servis prima poruku i upisuje status `MAINTENANCE_WORK_ORDER_CREATED` u `BookingStatusLogs` za odgovarajuću rezervaciju.

### Otpornost sistema

* **Durable queues** — sve RabbitMQ redove deklarisane su kao `durable: true`, što garantuje da poruke nisu izgubljene ni u slučaju restarta brokera.
* **AmqpException handling** — u svim publisherima postoji `try/catch` blok koji hvata `AmqpException`. U slučaju nedostupnosti brokera, lokalna promena stanja se čuva, a greška se loguje bez bacanja iznimke prema korisniku.
* `RabbitTemplate` ima uključen `observationEnabled(true)` koji propagira trace kontekst kroz broker.

### Konfiguracija exchange i queue-a

| Exchange | Tip | Opis |
| :--- | :--- | :--- |
| `hotel.events.exchange` | Topic | Transakcioni događaji od Finansije MS |
| `hotel.feedback.exchange` | Topic | Feedback Finansije MS-u |
| `maintenance.events.exchange` | Topic | Događaji prema Maintenance MS |
| `maintenance.feedback.exchange` | Topic | Feedback od Maintenance MS |

---

## 7. Observabilnost i distribuirano praćenje (Zadatak 3)

### W3C Traceparent propagacija

Konfiguracija preuzeta sa Config Servera (`application.properties`) aktivira W3C format:

```properties
management.tracing.propagation.type=w3c
management.tracing.sampling.probability=1.0
management.zipkin.tracing.endpoint=http://zipkin:9411/api/v2/spans
```

Svaki zahtev koji prolazi kroz sistem dobija jedinstveni `traceId` koji se propagira kroz sve mikroservise, RabbitMQ poruke i Feign pozive.


### Uniformni format logova

Konfigurisano putem Config Servera:

```properties
logging.pattern.level=%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]
```

Svaka log linija sadrži ime servisa (`rezervacije-service`), `traceId` i `spanId`, što omogućava filtriranje i korelaciju u Grafana/Loki dashboardu.

### Slanje logova u Loki

```properties
loki.url=http://loki:3100/loki/api/v1/push
```

Logovi se šalju u centralizovani Loki server obogaćeni metapodacima (`app`, `instance`). Grafana ih vizuelizuje i omogućava pretragu po `traceId`-u.


### Horizontalno skaliranje

Servis je dizajniran kao **stateless** — ne drži nikakvo stanje između zahteva u memoriji izvan baze podataka. Eureka service discovery omogućava registraciju više instanci, a API Gateway vrši **round-robin load balancing** između njih automatski na osnovu Eureka registra.

***
