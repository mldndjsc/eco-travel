***

# 💰 Finansije Mikroservis (Finansije Service)


## 1. Tehničke specifikacije i opis poslovne logike

### Tehnološki stek
* **Framework:** Spring Boot (Java)
* **Baza podataka:** H2 (In-Memory relacionalna baza)
* **ORM:** Spring Data JPA / Hibernate
* **Sinhrona komunikacija:** Spring Cloud OpenFeign
* **Otpornost sistema (Fault Tolerance):** Resilience4j (Circuit Breaker)
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

***
