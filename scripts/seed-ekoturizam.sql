-- ============================================
-- Seed podaci za Avanture i Vodici servise
-- Baza: ekoturizam_db
-- ============================================

CREATE DATABASE IF NOT EXISTS ekoturizam_db;
USE ekoturizam_db;

-- Avanture - kategorije
INSERT IGNORE INTO adventure_category (id, name, description) VALUES
(1, 'Planinarenje', 'Avanture u planinskom terenu'),
(2, 'Rafting', 'Avanture na rekama i brzacima'),
(3, 'Speleologija', 'Istrazivanje pecina i podzemnih sistema'),
(4, 'Paraglajding', 'Letenje paraglajderom'),
(5, 'Biciklizam', 'Avanture na biciklu u prirodi');

-- Avanture
INSERT IGNORE INTO adventure (id, title, description, difficulty, base_price, category_id) VALUES
(1, 'Planinarenje na Kopaoniku', 'Jednodnevni pohod na vrh Kopaonika', 'MEDIUM', 3500.00, 1),
(2, 'Rafting na Tari', 'Uzbudljivi rafting kroz kanjon Tare', 'HARD', 5000.00, 2),
(3, 'Istrazivanje Resavske pecine', 'Vodjena tura kroz Resavsku pecinu', 'EASY', 1500.00, 3),
(4, 'Paraglajding sa Rtnja', 'Let paraglajderom sa vrha Rtnja', 'EXTREME', 8000.00, 4),
(5, 'MTB Zlatibor', 'Mountainbiking na Zlatiboru', 'MEDIUM', 2500.00, 5);

-- Time slot-ovi
INSERT IGNORE INTO time_slot (id, adventure_id, start_time, end_time, max_participants, current_participants) VALUES
(1, 1, '2025-08-15 08:00:00', '2025-08-15 18:00:00', 15, 3),
(2, 1, '2025-09-01 08:00:00', '2025-09-01 18:00:00', 15, 0),
(3, 2, '2025-07-20 07:00:00', '2025-07-20 16:00:00', 10, 5),
(4, 3, '2025-08-10 10:00:00', '2025-08-10 14:00:00', 20, 8),
(5, 4, '2025-09-15 09:00:00', '2025-09-15 13:00:00', 6, 1);

-- Oprema (gear requirements)
INSERT IGNORE INTO gear_requirement (id, adventure_id, name, description, is_mandatory) VALUES
(1, 1, 'Planinarske cipele', 'Cipele sa dobrim drzanjem', true),
(2, 1, 'Ruksak', 'Ruksak od 20-30L', true),
(3, 1, 'Kabanica', 'Zastitna oprema od kise', false),
(4, 2, 'Prsluk za spasavanje', 'Obavezan sigurnosni prsluk', true),
(5, 2, 'Helm', 'Zastitna kaciga', true),
(6, 3, 'Baterijska lampa', 'Punjiva lampa za tamne prolaze', true),
(7, 4, 'Padobranska oprema', 'Profesionalni paraglajder', true);

-- Recenzije
INSERT IGNORE INTO adventure_review (id, adventure_id, rating, comment, review_date, reviewer_name) VALUES
(1, 1, 5, 'Odlicna avantura, preporucujem svima!', '2025-06-01', 'Petar Petrovic'),
(2, 1, 4, 'Lepo organizovano, vodic je bio sjajan', '2025-06-10', 'Ana Jovic'),
(3, 2, 5, 'Nezaboravno iskustvo na Tari!', '2025-05-20', 'Marko Nikolic'),
(4, 3, 4, 'Interesantno, ali kratko', '2025-06-15', 'Jelena Stojanovic'),
(5, 4, 5, 'Neverovatno! Zivotno iskustvo!', '2025-05-30', 'Nikola Savic');

-- Jezici
INSERT IGNORE INTO language (id, language_name, language_code) VALUES
(1, 'Srpski', 'SR'),
(2, 'Engleski', 'EN'),
(3, 'Nemacki', 'DE'),
(4, 'Francuski', 'FR'),
(5, 'Spanski', 'ES');

-- Vodici (Guides)
INSERT IGNORE INTO guide (id, first_name, last_name, bio, rating) VALUES
(1, 'Marko', 'Petrovic', 'Iskusni planinski vodic sa 10 godina iskustva', 4.8),
(2, 'Ana', 'Jovanovic', 'Sertifikovani vodic za vode i planine', 4.5),
(3, 'Nikola', 'Stojanovic', 'Specijalizovan za rafting i ekstremne sportove', 4.9),
(4, 'Jelena', 'Nikolic', 'Vodic za kulturne ture i planinarenje', 3.8),
(5, 'Stefan', 'Popovic', 'Pocetni vodic, entuzijasta prirode', 3.2);

-- Veza vodic-jezik
INSERT IGNORE INTO guide_language (guide_id, language_id) VALUES
(1, 1), (1, 2),
(2, 1), (2, 2), (2, 3),
(3, 1), (3, 2),
(4, 1), (4, 4),
(5, 1);

-- Sertifikati
INSERT IGNORE INTO certification (id, guide_id, name, issuing_body, expiry_date) VALUES
(1, 1, 'Planinarski vodic nivo 3', 'Planinski savez Srbije', '2027-06-30'),
(2, 1, 'Prva pomoc', 'Crveni krst', '2026-12-31'),
(3, 2, 'Vodeni sportovi', 'Kajakaski savez', '2026-09-30'),
(4, 2, 'Planinarski vodic nivo 2', 'Planinski savez Srbije', '2025-12-31'),
(5, 3, 'Rafting instruktor', 'ECA - European Canoe Association', '2028-03-31'),
(6, 3, 'Spasavanje u vodi', 'Vaterpolski savez Srbije', '2027-01-31');
