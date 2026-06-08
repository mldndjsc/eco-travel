INSERT IGNORE INTO language (id, language_name, language_code) VALUES
(1, 'Srpski', 'SR'),
(2, 'Engleski', 'EN'),
(3, 'Nemacki', 'DE'),
(4, 'Francuski', 'FR'),
(5, 'Spanski', 'ES');

INSERT IGNORE INTO guide (id, first_name, last_name, bio, rating) VALUES
(1, 'Marko', 'Petrovic', 'Iskusni planinski vodic sa 10 godina iskustva', 4.8),
(2, 'Ana', 'Jovanovic', 'Sertifikovani vodic za vode i planine', 4.5),
(3, 'Nikola', 'Stojanovic', 'Specijalizovan za rafting i ekstremne sportove', 4.9),
(4, 'Jelena', 'Nikolic', 'Vodic za kulturne ture i planinarenje', 3.8),
(5, 'Stefan', 'Popovic', 'Pocetni vodic, entuzijasta prirode', 3.2);

INSERT IGNORE INTO guide_language (guide_id, language_id) VALUES
(1, 1), (1, 2),
(2, 1), (2, 2), (2, 3),
(3, 1), (3, 2),
(4, 1), (4, 4),
(5, 1);

INSERT IGNORE INTO certification (id, guide_id, name, issuing_body, expiry_date) VALUES
(1, 1, 'Planinarski vodic nivo 3', 'Planinski savez Srbije', '2027-06-30'),
(2, 1, 'Prva pomoc', 'Crveni krst', '2026-12-31'),
(3, 2, 'Vodeni sportovi', 'Kajakaski savez', '2026-09-30'),
(4, 2, 'Planinarski vodic nivo 2', 'Planinski savez Srbije', '2025-12-31'),
(5, 3, 'Rafting instruktor', 'ECA - European Canoe Association', '2028-03-31'),
(6, 3, 'Spasavanje u vodi', 'Vaterpolski savez Srbije', '2027-01-31');
