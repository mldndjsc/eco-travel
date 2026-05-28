INSERT IGNORE INTO workers (id, first_name, last_name, specialty) VALUES (1, 'Jovan', 'Jovanović', 'Električar');
INSERT IGNORE INTO workers (id, first_name, last_name, specialty) VALUES (2, 'Petar', 'Petrović', 'Vodoinstalater');
INSERT IGNORE INTO workers (id, first_name, last_name, specialty) VALUES (3, 'Ana', 'Anić', 'Higijeničar');

INSERT IGNORE INTO maintenance_tasks (id, task_name, estimated_duration_hours) VALUES (1, 'Popravka klime', 3);
INSERT IGNORE INTO maintenance_tasks (id, task_name, estimated_duration_hours) VALUES (2, 'Generalno čišćenje', 4);
INSERT IGNORE INTO maintenance_tasks (id, task_name, estimated_duration_hours) VALUES (3, 'Zavrtanje ventila', 1);

INSERT IGNORE INTO work_orders (id, unit_id, worker_id, task_id, status, scheduled_for) VALUES (1, 1, 1, 1, 'PENDING', '2026-04-10 10:00:00');
INSERT IGNORE INTO work_orders (id, unit_id, worker_id, task_id, status, scheduled_for) VALUES (2, 2, 3, 2, 'IN_PROGRESS', '2026-04-12 08:00:00');