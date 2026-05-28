-- 1. Unos adresa
INSERT IGNORE INTO addresses (id, city, zip_code, street_address) VALUES (1, 'Zlatibor', '31315', 'Kraljeve Vode bb');
INSERT IGNORE INTO addresses (id, city, zip_code, street_address) VALUES (2, 'Kopaonik', '36354', 'Sunčani vrhovi 1');
INSERT IGNORE INTO addresses (id, city, zip_code, street_address) VALUES (3, 'Tara', '31250', 'Mitrovac bb');

-- 2. Unos sadržaja (facilities)
INSERT IGNORE INTO facilities (id, name, extra_cost) VALUES (1, 'WiFi', 0.0);
INSERT IGNORE INTO facilities (id, name, extra_cost) VALUES (2, 'Bazen', 25.0);
INSERT IGNORE INTO facilities (id, name, extra_cost) VALUES (3, 'Sauna', 15.0);
INSERT IGNORE INTO facilities (id, name, extra_cost) VALUES (4, 'Doručak', 10.0);

-- 3. Unos smeštajnih jedinica (povezivanje sa address_id)
INSERT IGNORE INTO units (id, name, unit_type, base_price_per_night, address_id) VALUES (1, 'Planinska Idila', 'CABIN', 120.0, 1);
INSERT IGNORE INTO units (id, name, unit_type, base_price_per_night, address_id) VALUES (2, 'Grand Apartman', 'APARTMENT', 200.0, 2);
INSERT IGNORE INTO units (id, name, unit_type, base_price_per_night, address_id) VALUES (3, 'Šumska Kuća', 'HOUSE', 90.0, 3);

-- 4. Povezivanje smeštaja i sadržaja (unit_facilities spojna tabela)
INSERT IGNORE INTO unit_facilities (unit_id, facility_id) VALUES (1, 1);
INSERT IGNORE INTO unit_facilities (unit_id, facility_id) VALUES (1, 3);
INSERT IGNORE INTO unit_facilities (unit_id, facility_id) VALUES (2, 1);
INSERT IGNORE INTO unit_facilities (unit_id, facility_id) VALUES (2, 2);
INSERT IGNORE INTO unit_facilities (unit_id, facility_id) VALUES (2, 4);

-- Unos u kalendar
INSERT IGNORE INTO availability_calendar (id, unit_id, start_date, end_date, reason) VALUES (1, 1, '2026-03-01', '2026-03-31', 'Godišnji odmor');
INSERT IGNORE INTO availability_calendar (id, unit_id, start_date, end_date, reason) VALUES (2, 1, '2026-06-15', '2026-06-25', 'Rezervacija #456');

-- 5. Unos sezonskih cena (price_tiers)
INSERT IGNORE INTO price_tiers (id, name, multiplier, start_date, end_date, unit_id) VALUES (1, 'Zimska sezona', 1.5, '2026-12-01', '2027-02-28', 1);
INSERT IGNORE INTO price_tiers (id, name, multiplier, start_date, end_date, unit_id) VALUES (2, 'Zimska sezona', 1.8, '2026-12-01', '2027-02-28', 2);