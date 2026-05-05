INSERT INTO cancellation_policies (policy_name, refund_percentage) VALUES ( 'FREE_48H', 100);
INSERT INTO cancellation_policies (policy_name, refund_percentage) VALUES ( 'HALF_REFUND_24H', 50);
INSERT INTO cancellation_policies (policy_name, refund_percentage) VALUES ( 'NO_REFUND', 0);

INSERT INTO guests ( first_name, last_name, email) VALUES ( 'Marko', 'Markovic', 'marko.markovic@gmail.com');
INSERT INTO guests ( first_name, last_name, email) VALUES ('Jelena', 'Jovanovic', 'jelena.jovanovic@gmail.com');
INSERT INTO guests ( first_name, last_name, email) VALUES ( 'Nikola', 'Nikolic', 'nikola.nikolic@gmail.com');
INSERT INTO guests ( first_name, last_name, email) VALUES ( 'Ana', 'Andric', 'ana.andric@gmail.com');
INSERT INTO guests ( first_name, last_name, email) VALUES ( 'Petar', 'Petrovic', 'petar.petrovic@gmail.com');

INSERT INTO bookings ( unit_id, guest_id, policy_id, start_date, end_date, total_price, status) VALUES ( 101, 1, 1, '2025-07-10', '2025-07-15', 250.00, 'CONFIRMED');
INSERT INTO bookings ( unit_id, guest_id, policy_id, start_date, end_date, total_price, status) VALUES ( 102, 2, 2, '2025-07-12', '2025-07-14', 120.00, 'PENDING');
INSERT INTO bookings ( unit_id, guest_id, policy_id, start_date, end_date, total_price, status) VALUES ( 103, 3, 3, '2025-08-01', '2025-08-05', 300.00, 'CONFIRMED');
INSERT INTO bookings ( unit_id, guest_id, policy_id, start_date, end_date, total_price, status) VALUES ( 104, 4, 1, '2025-08-10', '2025-08-12', 140.00, 'CANCELLED');
INSERT INTO bookings ( unit_id, guest_id, policy_id, start_date, end_date, total_price, status) VALUES ( 105, 5, 2, '2025-09-01', '2025-09-06', 400.00, 'COMPLETED');

INSERT INTO add_on_items ( booking_id, service_name, price) VALUES ( 1, 'Breakfast', 25.00);
INSERT INTO add_on_items ( booking_id, service_name, price) VALUES ( 1, 'Parking', 15.00);
INSERT INTO add_on_items ( booking_id, service_name, price) VALUES ( 2, 'Late checkout', 20.00);
INSERT INTO add_on_items ( booking_id, service_name, price) VALUES ( 3, 'Airport transfer', 35.00);
INSERT INTO add_on_items ( booking_id, service_name, price) VALUES ( 3, 'Breakfast', 20.00);
INSERT INTO add_on_items ( booking_id, service_name, price) VALUES ( 5, 'Guided eco tour', 50.00);

INSERT INTO booking_status_logs ( booking_id, old_status, new_status, changed_at) VALUES ( 1, 'PENDING', 'CONFIRMED', '2025-06-20 10:15:00');
INSERT INTO booking_status_logs ( booking_id, old_status, new_status, changed_at) VALUES ( 2, 'CREATED', 'PENDING', '2025-06-25 12:35:00');
INSERT INTO booking_status_logs ( booking_id, old_status, new_status, changed_at) VALUES ( 3, 'PENDING', 'CONFIRMED', '2025-07-01 09:30:00');
INSERT INTO booking_status_logs ( booking_id, old_status, new_status, changed_at) VALUES ( 4, 'PENDING', 'CONFIRMED', '2025-07-15 14:10:00');
INSERT INTO booking_status_logs ( booking_id, old_status, new_status, changed_at) VALUES ( 4, 'CONFIRMED', 'CANCELLED', '2025-08-08 11:00:00');
INSERT INTO booking_status_logs ( booking_id, old_status, new_status, changed_at) VALUES ( 5, 'PENDING', 'CONFIRMED', '2025-08-01 17:00:00');
INSERT INTO booking_status_logs ( booking_id, old_status, new_status, changed_at) VALUES ( 5, 'CONFIRMED', 'COMPLETED', '2025-09-06 12:00:00');