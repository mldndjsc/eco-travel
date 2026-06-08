***

# 💰 Finansije Mikroservis (Finansije Service)


## 1. Tehničke specifikacije i opis poslovne logike

### Tehnološki stek
* **Framework:** Spring Boot (Java)
* **Baza podataka:** H2 (In-Memory relacionalna baza)
* **ORM:** Spring Data JPA / Hibernate
* **Sinhrona komunikacija:** Spring Cloud OpenFeign
* **Otpornost sistema (Fault Tolerance):** Resilience4j (Circuit Breaker, Rate Limiter, Retry)
* **Asinhrona komunikacija:** RabbitMQ (Spring AMQP)
* **Dokumentacija API-ja:** Swagger / OpenAPI 3


---

## 2. Mrežni port

Aplikacija je konfigurisana da operiše na portu:
* **Port:** `8083`

---

## 3. Prikaz svih implementiranih endpointa

### 📄 Invoices (Fakture)
| HTTP Metoda | Endpoint                           | Opis funkcionalnosti |
| :--- |:-----------------------------------| :--- |
| `POST` | `/api/finansije/invoices/add`      | Kreira novu fakturu uz proveru validnosti podataka i komunicira sa Bookings servisom. |
| `PUT` | `/api/finansije/invoices/update/{id}`        | Ažurira postojeću fakturu (ili je kreira ako ne postoji). |
| `GET` | `/api/finansije/invoices/{id}`               | Vraća detalje specifične fakture na osnovu ID-a. |
| `GET` | `/api/finansije/invoices/all`                | Vraća listu svih faktura u sistemu. |
| `DELETE` | `/api/finansije/invoices/delete/{id}`        | Briše fakturu iz baze podataka. |
| `PUT` | `/api/finansije/invoices/{id}/cancel-unpaid` | Stornira fakturu i automatski otkazuje vezanu rezervaciju (Saga pattern). |

### 💳 Transactions (Uplate i Transakcije)
| HTTP Metoda | Endpoint | Opis funkcionalnosti |
| :--- | :--- | :--- |
| `POST` | `/api/finansije/transactions/add` | Ručno dodavanje zapisa o transakciji. |
| `POST` | `/api/finansije/transactions/pay` | **Glavni endpoint za plaćanje:** Beleži uplatu, sabira ukupno uplaćen novac, menja status fakture u `PAID` i javlja Bookings servisu da potvrdi rezervaciju. |
| `GET` | `/api/finansije/transactions/search` | Pretraga transakcija na osnovu vremenskog opsega (od-do) i metode plaćanja. |
| `PUT` | `/api/finansije/transactions/update/{id}` | Ažuriranje podataka o transakciji. |
| `GET` | `/api/finansije/transactions/{id}` | Prikaz jedne transakcije na osnovu ID-a. |
| `GET` | `/api/finansije/transactions/all` | Prikaz svih zabeleženih transakcija. |
| `DELETE`| `/api/finansije/transactions/delete/{id}` | Brisanje transakcije iz sistema. |

### 🏦 Payment Methods (Načini plaćanja)
| HTTP Metoda | Endpoint | Opis funkcionalnosti |
| :--- | :--- | :--- |
| `POST` | `/api/finansije/paymentmethods/add` | Dodavanje novog načina plaćanja (npr. Kartica, Keš). |
| `PUT` | `/api/finansije/paymentmethods/update/{id}` | Ažuriranje naziva ili statusa aktivnosti načina plaćanja. |
| `GET` | `/api/finansije/paymentmethods/{id}` | Dohvatanje specifičnog načina plaćanja. |
| `GET` | `/api/finansije/paymentmethods/all` | Prikaz svih dostupnih načina plaćanja. |
| `DELETE`| `/api/finansije/paymentmethods/delete/{id}` | Brisanje načina plaćanja. |

### 📊 Tax Rates (Poreske stope)
| HTTP Metoda | Endpoint | Opis funkcionalnosti |
| :--- | :--- | :--- |
| `POST` | `/api/finansije/taxrates/add` | Definisanje nove poreske stope u sistemu. |
| `PUT` | `/api/finansije/taxrates/update/{id}` | Ažuriranje postojeće poreske stope i njenog procenta. |
| `GET` | `/api/finansije/taxrates/{id}` | Dohvatanje specifične poreske stope. |
| `GET` | `/api/finansije/taxrates/all` | Prikaz svih poreskih stopa u bazi. |
| `DELETE`| `/api/finansije/taxrates/delete/{id}`| Brisanje poreske stope. |

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
* **URL:** `http://localhost:8083/h2-console`
* **JDBC URL:** `jdbc:h2:mem:finansijedb`
* **Username:** `md`
* **Password:** *(ostaviti prazno)*

### Testiranje API-ja (Swagger)
Za lakše testiranje i vizuelni pregled endpointa, konfigurisan je Swagger UI. Nakon pokretanja aplikacije, otvorite browser i idite na:
* **Swagger interfejs:** `http://localhost:8083/swagger-ui.html`

### Praćenje zdravlja sistema i Osigurača (Actuator)
Za nadzor stanja `Resilience4j` osigurača i celokupnog mikroservisa, dostupni su sledeći endpointi:
* Health Check: `http://localhost:8083/actuator/health`
* Status osigurača: `http://localhost:8083/actuator/circuitbreakers`

---

## 5. Dokerizacija i konfiguracija (Zadatak 1)

### Multi-stage Dockerfile

Servis koristi **multi-stage Docker build** koji razdvaja fazu kompajliranja od runtime okruženja:

* **Build faza** —   Prvo se instalira `shared-module` (zajednička biblioteka sa event klasama), a zatim se `pom.xml` kopira odvojeno kako bi Docker keširao dependency layer i ubrzao ponovne buildove.

Rezultat je produkciona slika koja ne sadrži Maven, JDK, source kod niti privremene kompajlirne fajlove.

### Eksternalizacija konfiguracije

Port i adresa Config servisa nisu hardkodovani, već se preuzimaju iz environment varijabli:

```
SERVER_PORT=8083
CONFIG_SERVER_URL=http://config-service:8888
```

Aplikacija dinamički učitava svu preostalu konfiguraciju (baza, RabbitMQ, Eureka, Loki, tracing, Resilience4j parametri) sa **Spring Cloud Config Servera** pri pokretanju. Lokalna `application.properties` sadrži samo ime servisa i `spring.config.import` direktivu.

### Docker Compose i zavisnosti

Servis se podiže kao deo zajedničke Docker Compose orksetracije. Konfigurisan je `depends_on` sa `healthcheck` uslovom koji garantuje da aplikativni kontejner ne stupa u operativno stanje pre nego što baza podataka potvrdi svoju punu dostupnost.

---

## 6. Asinhrona komunikacija (Zadatak 2)

### RabbitMQ — asinhroni tokovi poruka

Servis je integrisan sa RabbitMQ brokerom i učestvuje u sledećim asinhronim tokovima:

#### 1. Slanje obaveštenja o kreiranoj transakciji (ka Rezervacije servisu)

| Komponenta | Vrednost |
| :--- | :--- |
| Queue | `rezervacije.finance-events.queue` |
| Exchange | `hotel.events.exchange` (Topic) |
| Routing Key | `finance.transaction.created` |
| Publisher | `TransactionEventPublisher` |
| Event | `TransactionCreatedEvent` |

Kada se izvrši `TransactionsService.save()`, servis objavljuje `TransactionCreatedEvent` sa `transactionId`, `bookingId`, `invoiceNumber`, `amount` i `paymentMethod`. Rezervacije servis prima ovu poruku i menja status odgovarajuće rezervacije u `PAYMENT_RECORDED`.

Tok za `processPayment()` je isti — transakcija se evidentira i event šalje, a nakon toga se sinhronim Feign pozivom rezervacija potvrđuje ako je iznos u potpunosti plaćen.

#### 2. Primanje feedback-a od Rezervacije servisa

| Komponenta | Vrednost |
| :--- | :--- |
| Queue | `finansije.reservation-feedback.queue` |
| Exchange | `hotel.feedback.exchange` (Topic) |
| Routing Key | `reservation.payment-recorded.feedback` |
| Listener | `ReservationFeedbackListener` |
| Event | `ReservationPaymentRecordedFeedbackEvent` |

Nakon što Rezervacije servis evidentira uplatu, šalje povratni događaj. `ReservationFeedbackListener` prima poruku i poziva `invoicesService.markReservationFeedbackReceived(invoiceNumber)`, čime se faktura ažurira na status `RESERVATION_PAYMENT_RECORDED`.


### Konfiguracija exhange i queue-a

| Exchange | Tip | Opis |
| :--- | :--- | :--- |
| `hotel.events.exchange` | Topic | Transakcioni događaji prema Rezervacije MS |
| `hotel.feedback.exchange` | Topic | Feedback od Rezervacije MS |

---

## 7. Observabilnost i distribuirano praćenje (Zadatak 3)

### W3C Traceparent propagacija

Konfiguracija preuzeta sa Config Servera (`application.properties`) aktivira W3C format:

```properties
management.tracing.propagation.type=w3c
management.tracing.sampling.probability=1.0
management.zipkin.tracing.endpoint=http://zipkin:9411/api/v2/spans
```


### Uniformni format logova

Konfigurisano putem Config Servera:

```properties
logging.pattern.level=%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]
```

Svaka log linija sadrži ime servisa (`finansije-service`), `traceId` i `spanId`, što omogućava filtriranje i korelaciju u Grafana/Loki dashboardu.

### Slanje logova u Loki

```properties
loki.url=http://loki:3100/loki/api/v1/push
```

Logovi se šalju u centralizovani Loki server obogaćeni metapodacima (`app`, `instance`). Grafana ih vizuelizuje i omogućava pretragu po `traceId`-u.


### Horizontalno skaliranje

Servis je dizajniran kao **stateless** — ne drži nikakvo stanje između zahteva u memoriji izvan baze podataka. Eureka service discovery omogućava registraciju više instanci, a API Gateway vrši **round-robin load balancing** između njih automatski na osnovu Eureka registra.

***
