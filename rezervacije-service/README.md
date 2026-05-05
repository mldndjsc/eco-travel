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

***
