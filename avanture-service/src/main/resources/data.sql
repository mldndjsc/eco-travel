INSERT INTO adventure_category (name, description) VALUES
('Planinarenje', 'Avanture u planinskom terenu'),
('Rafting', 'Avanture na rekama i brzacima'),
('Speleologija', 'Istrazivanje pecina i podzemnih sistema'),
('Paraglajding', 'Letenje paraglajderom'),
('Biciklizam', 'Avanture na biciklu u prirodi')
ON CONFLICT DO NOTHING;

INSERT INTO adventure (title, description, difficulty, base_price, category_id)
SELECT 'Planinarenje na Kopaoniku', 'Jednodnevni pohod na vrh Kopaonika', 'MEDIUM', 3500.00, id
FROM adventure_category WHERE name = 'Planinarenje'
AND NOT EXISTS (SELECT 1 FROM adventure WHERE title = 'Planinarenje na Kopaoniku');

INSERT INTO adventure (title, description, difficulty, base_price, category_id)
SELECT 'Rafting na Tari', 'Uzbudljivi rafting kroz kanjon Tare', 'HARD', 5000.00, id
FROM adventure_category WHERE name = 'Rafting'
AND NOT EXISTS (SELECT 1 FROM adventure WHERE title = 'Rafting na Tari');

INSERT INTO adventure (title, description, difficulty, base_price, category_id)
SELECT 'Istrazivanje Resavske pecine', 'Vodjena tura kroz Resavsku pecinu', 'EASY', 1500.00, id
FROM adventure_category WHERE name = 'Speleologija'
AND NOT EXISTS (SELECT 1 FROM adventure WHERE title = 'Istrazivanje Resavske pecine');

INSERT INTO adventure (title, description, difficulty, base_price, category_id)
SELECT 'Paraglajding sa Rtnja', 'Let paraglajderom sa vrha Rtnja', 'EXTREME', 8000.00, id
FROM adventure_category WHERE name = 'Paraglajding'
AND NOT EXISTS (SELECT 1 FROM adventure WHERE title = 'Paraglajding sa Rtnja');

INSERT INTO adventure (title, description, difficulty, base_price, category_id)
SELECT 'MTB Zlatibor', 'Mountainbiking na Zlatiboru', 'MEDIUM', 2500.00, id
FROM adventure_category WHERE name = 'Biciklizam'
AND NOT EXISTS (SELECT 1 FROM adventure WHERE title = 'MTB Zlatibor');

INSERT INTO time_slot (adventure_id, start_time, end_time, max_participants, current_participants)
SELECT id, '2025-08-15 08:00:00', '2025-08-15 18:00:00', 15, 3
FROM adventure WHERE title = 'Planinarenje na Kopaoniku'
AND NOT EXISTS (SELECT 1 FROM time_slot ts JOIN adventure a ON ts.adventure_id = a.id WHERE a.title = 'Planinarenje na Kopaoniku' AND ts.start_time = '2025-08-15 08:00:00');

INSERT INTO time_slot (adventure_id, start_time, end_time, max_participants, current_participants)
SELECT id, '2025-09-01 08:00:00', '2025-09-01 18:00:00', 15, 0
FROM adventure WHERE title = 'Planinarenje na Kopaoniku'
AND NOT EXISTS (SELECT 1 FROM time_slot ts JOIN adventure a ON ts.adventure_id = a.id WHERE a.title = 'Planinarenje na Kopaoniku' AND ts.start_time = '2025-09-01 08:00:00');

INSERT INTO time_slot (adventure_id, start_time, end_time, max_participants, current_participants)
SELECT id, '2025-07-20 07:00:00', '2025-07-20 16:00:00', 10, 5
FROM adventure WHERE title = 'Rafting na Tari'
AND NOT EXISTS (SELECT 1 FROM time_slot ts JOIN adventure a ON ts.adventure_id = a.id WHERE a.title = 'Rafting na Tari' AND ts.start_time = '2025-07-20 07:00:00');

INSERT INTO time_slot (adventure_id, start_time, end_time, max_participants, current_participants)
SELECT id, '2025-08-10 10:00:00', '2025-08-10 14:00:00', 20, 8
FROM adventure WHERE title = 'Istrazivanje Resavske pecine'
AND NOT EXISTS (SELECT 1 FROM time_slot ts JOIN adventure a ON ts.adventure_id = a.id WHERE a.title = 'Istrazivanje Resavske pecine' AND ts.start_time = '2025-08-10 10:00:00');

INSERT INTO time_slot (adventure_id, start_time, end_time, max_participants, current_participants)
SELECT id, '2025-09-15 09:00:00', '2025-09-15 13:00:00', 6, 1
FROM adventure WHERE title = 'Paraglajding sa Rtnja'
AND NOT EXISTS (SELECT 1 FROM time_slot ts JOIN adventure a ON ts.adventure_id = a.id WHERE a.title = 'Paraglajding sa Rtnja' AND ts.start_time = '2025-09-15 09:00:00');

INSERT INTO gear_requirement (adventure_id, name, description, is_mandatory)
SELECT id, 'Planinarske cipele', 'Cipele sa dobrim drzanjem', true FROM adventure WHERE title = 'Planinarenje na Kopaoniku'
AND NOT EXISTS (SELECT 1 FROM gear_requirement gr JOIN adventure a ON gr.adventure_id = a.id WHERE a.title = 'Planinarenje na Kopaoniku' AND gr.name = 'Planinarske cipele');

INSERT INTO gear_requirement (adventure_id, name, description, is_mandatory)
SELECT id, 'Ruksak', 'Ruksak od 20-30L', true FROM adventure WHERE title = 'Planinarenje na Kopaoniku'
AND NOT EXISTS (SELECT 1 FROM gear_requirement gr JOIN adventure a ON gr.adventure_id = a.id WHERE a.title = 'Planinarenje na Kopaoniku' AND gr.name = 'Ruksak');

INSERT INTO gear_requirement (adventure_id, name, description, is_mandatory)
SELECT id, 'Kabanica', 'Zastitna oprema od kise', false FROM adventure WHERE title = 'Planinarenje na Kopaoniku'
AND NOT EXISTS (SELECT 1 FROM gear_requirement gr JOIN adventure a ON gr.adventure_id = a.id WHERE a.title = 'Planinarenje na Kopaoniku' AND gr.name = 'Kabanica');

INSERT INTO gear_requirement (adventure_id, name, description, is_mandatory)
SELECT id, 'Prsluk za spasavanje', 'Obavezan sigurnosni prsluk', true FROM adventure WHERE title = 'Rafting na Tari'
AND NOT EXISTS (SELECT 1 FROM gear_requirement gr JOIN adventure a ON gr.adventure_id = a.id WHERE a.title = 'Rafting na Tari' AND gr.name = 'Prsluk za spasavanje');

INSERT INTO gear_requirement (adventure_id, name, description, is_mandatory)
SELECT id, 'Helm', 'Zastitna kaciga', true FROM adventure WHERE title = 'Rafting na Tari'
AND NOT EXISTS (SELECT 1 FROM gear_requirement gr JOIN adventure a ON gr.adventure_id = a.id WHERE a.title = 'Rafting na Tari' AND gr.name = 'Helm');

INSERT INTO gear_requirement (adventure_id, name, description, is_mandatory)
SELECT id, 'Baterijska lampa', 'Punjiva lampa za tamne prolaze', true FROM adventure WHERE title = 'Istrazivanje Resavske pecine'
AND NOT EXISTS (SELECT 1 FROM gear_requirement gr JOIN adventure a ON gr.adventure_id = a.id WHERE a.title = 'Istrazivanje Resavske pecine' AND gr.name = 'Baterijska lampa');

INSERT INTO gear_requirement (adventure_id, name, description, is_mandatory)
SELECT id, 'Padobranska oprema', 'Profesionalni paraglajder', true FROM adventure WHERE title = 'Paraglajding sa Rtnja'
AND NOT EXISTS (SELECT 1 FROM gear_requirement gr JOIN adventure a ON gr.adventure_id = a.id WHERE a.title = 'Paraglajding sa Rtnja' AND gr.name = 'Padobranska oprema');

INSERT INTO adventure_review (adventure_id, rating, comment, review_date, reviewer_name)
SELECT id, 5, 'Odlicna avantura, preporucujem svima!', '2025-06-01', 'Petar Petrovic' FROM adventure WHERE title = 'Planinarenje na Kopaoniku'
AND NOT EXISTS (SELECT 1 FROM adventure_review ar JOIN adventure a ON ar.adventure_id = a.id WHERE a.title = 'Planinarenje na Kopaoniku' AND ar.reviewer_name = 'Petar Petrovic');

INSERT INTO adventure_review (adventure_id, rating, comment, review_date, reviewer_name)
SELECT id, 4, 'Lepo organizovano, vodic je bio sjajan', '2025-06-10', 'Ana Jovic' FROM adventure WHERE title = 'Planinarenje na Kopaoniku'
AND NOT EXISTS (SELECT 1 FROM adventure_review ar JOIN adventure a ON ar.adventure_id = a.id WHERE a.title = 'Planinarenje na Kopaoniku' AND ar.reviewer_name = 'Ana Jovic');

INSERT INTO adventure_review (adventure_id, rating, comment, review_date, reviewer_name)
SELECT id, 5, 'Nezaboravno iskustvo na Tari!', '2025-05-20', 'Marko Nikolic' FROM adventure WHERE title = 'Rafting na Tari'
AND NOT EXISTS (SELECT 1 FROM adventure_review ar JOIN adventure a ON ar.adventure_id = a.id WHERE a.title = 'Rafting na Tari' AND ar.reviewer_name = 'Marko Nikolic');

INSERT INTO adventure_review (adventure_id, rating, comment, review_date, reviewer_name)
SELECT id, 4, 'Interesantno, ali kratko', '2025-06-15', 'Jelena Stojanovic' FROM adventure WHERE title = 'Istrazivanje Resavske pecine'
AND NOT EXISTS (SELECT 1 FROM adventure_review ar JOIN adventure a ON ar.adventure_id = a.id WHERE a.title = 'Istrazivanje Resavske pecine' AND ar.reviewer_name = 'Jelena Stojanovic');

INSERT INTO adventure_review (adventure_id, rating, comment, review_date, reviewer_name)
SELECT id, 5, 'Neverovatno! Zivotno iskustvo!', '2025-05-30', 'Nikola Savic' FROM adventure WHERE title = 'Paraglajding sa Rtnja'
AND NOT EXISTS (SELECT 1 FROM adventure_review ar JOIN adventure a ON ar.adventure_id = a.id WHERE a.title = 'Paraglajding sa Rtnja' AND ar.reviewer_name = 'Nikola Savic');
