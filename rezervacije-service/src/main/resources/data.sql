INSERT INTO cancellation_policies (policy_name, refund_percentage)
SELECT 'FREE_48H', 100
WHERE NOT EXISTS (SELECT 1 FROM cancellation_policies WHERE policy_name = 'FREE_48H');

INSERT INTO cancellation_policies (policy_name, refund_percentage)
SELECT 'HALF_REFUND_24H', 50
WHERE NOT EXISTS (SELECT 1 FROM cancellation_policies WHERE policy_name = 'HALF_REFUND_24H');

INSERT INTO cancellation_policies (policy_name, refund_percentage)
SELECT 'NO_REFUND', 0
WHERE NOT EXISTS (SELECT 1 FROM cancellation_policies WHERE policy_name = 'NO_REFUND');

INSERT INTO guests (first_name, last_name, email)
SELECT 'Marko', 'Markovic', 'marko.markovic@gmail.com'
WHERE NOT EXISTS (SELECT 1 FROM guests WHERE email = 'marko.markovic@gmail.com');

INSERT INTO guests (first_name, last_name, email)
SELECT 'Jelena', 'Jovanovic', 'jelena.jovanovic@gmail.com'
WHERE NOT EXISTS (SELECT 1 FROM guests WHERE email = 'jelena.jovanovic@gmail.com');

INSERT INTO guests (first_name, last_name, email)
SELECT 'Nikola', 'Nikolic', 'nikola.nikolic@gmail.com'
WHERE NOT EXISTS (SELECT 1 FROM guests WHERE email = 'nikola.nikolic@gmail.com');

INSERT INTO guests (first_name, last_name, email)
SELECT 'Ana', 'Andric', 'ana.andric@gmail.com'
WHERE NOT EXISTS (SELECT 1 FROM guests WHERE email = 'ana.andric@gmail.com');

INSERT INTO guests (first_name, last_name, email)
SELECT 'Petar', 'Petrovic', 'petar.petrovic@gmail.com'
WHERE NOT EXISTS (SELECT 1 FROM guests WHERE email = 'petar.petrovic@gmail.com');

INSERT INTO bookings (unit_id, guest_id, policy_id, start_date, end_date, total_price, status)
SELECT 101, guests.id, cancellation_policies.id, '2025-07-10', '2025-07-15', 250.00, 'CONFIRMED'
FROM guests
JOIN cancellation_policies ON cancellation_policies.policy_name = 'FREE_48H'
WHERE guests.email = 'marko.markovic@gmail.com'
  AND NOT EXISTS (
      SELECT 1 FROM bookings
      WHERE unit_id = 101
        AND guest_id = guests.id
        AND start_date = '2025-07-10'
        AND end_date = '2025-07-15'
  );

INSERT INTO bookings (unit_id, guest_id, policy_id, start_date, end_date, total_price, status)
SELECT 102, guests.id, cancellation_policies.id, '2025-07-12', '2025-07-14', 120.00, 'PENDING'
FROM guests
JOIN cancellation_policies ON cancellation_policies.policy_name = 'HALF_REFUND_24H'
WHERE guests.email = 'jelena.jovanovic@gmail.com'
  AND NOT EXISTS (
      SELECT 1 FROM bookings
      WHERE unit_id = 102
        AND guest_id = guests.id
        AND start_date = '2025-07-12'
        AND end_date = '2025-07-14'
  );

INSERT INTO bookings (unit_id, guest_id, policy_id, start_date, end_date, total_price, status)
SELECT 103, guests.id, cancellation_policies.id, '2025-08-01', '2025-08-05', 300.00, 'CONFIRMED'
FROM guests
JOIN cancellation_policies ON cancellation_policies.policy_name = 'NO_REFUND'
WHERE guests.email = 'nikola.nikolic@gmail.com'
  AND NOT EXISTS (
      SELECT 1 FROM bookings
      WHERE unit_id = 103
        AND guest_id = guests.id
        AND start_date = '2025-08-01'
        AND end_date = '2025-08-05'
  );

INSERT INTO bookings (unit_id, guest_id, policy_id, start_date, end_date, total_price, status)
SELECT 104, guests.id, cancellation_policies.id, '2025-08-10', '2025-08-12', 140.00, 'CANCELLED'
FROM guests
JOIN cancellation_policies ON cancellation_policies.policy_name = 'FREE_48H'
WHERE guests.email = 'ana.andric@gmail.com'
  AND NOT EXISTS (
      SELECT 1 FROM bookings
      WHERE unit_id = 104
        AND guest_id = guests.id
        AND start_date = '2025-08-10'
        AND end_date = '2025-08-12'
  );

INSERT INTO bookings (unit_id, guest_id, policy_id, start_date, end_date, total_price, status)
SELECT 105, guests.id, cancellation_policies.id, '2025-09-01', '2025-09-06', 400.00, 'COMPLETED'
FROM guests
JOIN cancellation_policies ON cancellation_policies.policy_name = 'HALF_REFUND_24H'
WHERE guests.email = 'petar.petrovic@gmail.com'
  AND NOT EXISTS (
      SELECT 1 FROM bookings
      WHERE unit_id = 105
        AND guest_id = guests.id
        AND start_date = '2025-09-01'
        AND end_date = '2025-09-06'
  );

INSERT INTO add_on_items (booking_id, service_name, price)
SELECT bookings.id, 'Breakfast', 25.00
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'marko.markovic@gmail.com'
  AND bookings.unit_id = 101
  AND bookings.start_date = '2025-07-10'
  AND bookings.end_date = '2025-07-15'
  AND NOT EXISTS (
      SELECT 1 FROM add_on_items
      WHERE booking_id = bookings.id
        AND service_name = 'Breakfast'
        AND price = 25.00
  );

INSERT INTO add_on_items (booking_id, service_name, price)
SELECT bookings.id, 'Parking', 15.00
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'marko.markovic@gmail.com'
  AND bookings.unit_id = 101
  AND bookings.start_date = '2025-07-10'
  AND bookings.end_date = '2025-07-15'
  AND NOT EXISTS (
      SELECT 1 FROM add_on_items
      WHERE booking_id = bookings.id
        AND service_name = 'Parking'
        AND price = 15.00
  );

INSERT INTO add_on_items (booking_id, service_name, price)
SELECT bookings.id, 'Late checkout', 20.00
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'jelena.jovanovic@gmail.com'
  AND bookings.unit_id = 102
  AND bookings.start_date = '2025-07-12'
  AND bookings.end_date = '2025-07-14'
  AND NOT EXISTS (
      SELECT 1 FROM add_on_items
      WHERE booking_id = bookings.id
        AND service_name = 'Late checkout'
        AND price = 20.00
  );

INSERT INTO add_on_items (booking_id, service_name, price)
SELECT bookings.id, 'Airport transfer', 35.00
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'nikola.nikolic@gmail.com'
  AND bookings.unit_id = 103
  AND bookings.start_date = '2025-08-01'
  AND bookings.end_date = '2025-08-05'
  AND NOT EXISTS (
      SELECT 1 FROM add_on_items
      WHERE booking_id = bookings.id
        AND service_name = 'Airport transfer'
        AND price = 35.00
  );

INSERT INTO add_on_items (booking_id, service_name, price)
SELECT bookings.id, 'Breakfast', 20.00
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'nikola.nikolic@gmail.com'
  AND bookings.unit_id = 103
  AND bookings.start_date = '2025-08-01'
  AND bookings.end_date = '2025-08-05'
  AND NOT EXISTS (
      SELECT 1 FROM add_on_items
      WHERE booking_id = bookings.id
        AND service_name = 'Breakfast'
        AND price = 20.00
  );

INSERT INTO add_on_items (booking_id, service_name, price)
SELECT bookings.id, 'Guided eco tour', 50.00
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'petar.petrovic@gmail.com'
  AND bookings.unit_id = 105
  AND bookings.start_date = '2025-09-01'
  AND bookings.end_date = '2025-09-06'
  AND NOT EXISTS (
      SELECT 1 FROM add_on_items
      WHERE booking_id = bookings.id
        AND service_name = 'Guided eco tour'
        AND price = 50.00
  );

INSERT INTO booking_status_logs (booking_id, old_status, new_status, changed_at)
SELECT bookings.id, 'PENDING', 'CONFIRMED', '2025-06-20 10:15:00'
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'marko.markovic@gmail.com'
  AND bookings.unit_id = 101
  AND bookings.start_date = '2025-07-10'
  AND bookings.end_date = '2025-07-15'
  AND NOT EXISTS (
      SELECT 1 FROM booking_status_logs
      WHERE booking_id = bookings.id
        AND changed_at = '2025-06-20 10:15:00'
  );

INSERT INTO booking_status_logs (booking_id, old_status, new_status, changed_at)
SELECT bookings.id, 'CREATED', 'PENDING', '2025-06-25 12:35:00'
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'jelena.jovanovic@gmail.com'
  AND bookings.unit_id = 102
  AND bookings.start_date = '2025-07-12'
  AND bookings.end_date = '2025-07-14'
  AND NOT EXISTS (
      SELECT 1 FROM booking_status_logs
      WHERE booking_id = bookings.id
        AND changed_at = '2025-06-25 12:35:00'
  );

INSERT INTO booking_status_logs (booking_id, old_status, new_status, changed_at)
SELECT bookings.id, 'PENDING', 'CONFIRMED', '2025-07-01 09:30:00'
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'nikola.nikolic@gmail.com'
  AND bookings.unit_id = 103
  AND bookings.start_date = '2025-08-01'
  AND bookings.end_date = '2025-08-05'
  AND NOT EXISTS (
      SELECT 1 FROM booking_status_logs
      WHERE booking_id = bookings.id
        AND changed_at = '2025-07-01 09:30:00'
  );

INSERT INTO booking_status_logs (booking_id, old_status, new_status, changed_at)
SELECT bookings.id, 'PENDING', 'CONFIRMED', '2025-07-15 14:10:00'
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'ana.andric@gmail.com'
  AND bookings.unit_id = 104
  AND bookings.start_date = '2025-08-10'
  AND bookings.end_date = '2025-08-12'
  AND NOT EXISTS (
      SELECT 1 FROM booking_status_logs
      WHERE booking_id = bookings.id
        AND changed_at = '2025-07-15 14:10:00'
  );

INSERT INTO booking_status_logs (booking_id, old_status, new_status, changed_at)
SELECT bookings.id, 'CONFIRMED', 'CANCELLED', '2025-08-08 11:00:00'
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'ana.andric@gmail.com'
  AND bookings.unit_id = 104
  AND bookings.start_date = '2025-08-10'
  AND bookings.end_date = '2025-08-12'
  AND NOT EXISTS (
      SELECT 1 FROM booking_status_logs
      WHERE booking_id = bookings.id
        AND changed_at = '2025-08-08 11:00:00'
  );

INSERT INTO booking_status_logs (booking_id, old_status, new_status, changed_at)
SELECT bookings.id, 'PENDING', 'CONFIRMED', '2025-08-01 17:00:00'
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'petar.petrovic@gmail.com'
  AND bookings.unit_id = 105
  AND bookings.start_date = '2025-09-01'
  AND bookings.end_date = '2025-09-06'
  AND NOT EXISTS (
      SELECT 1 FROM booking_status_logs
      WHERE booking_id = bookings.id
        AND changed_at = '2025-08-01 17:00:00'
  );

INSERT INTO booking_status_logs (booking_id, old_status, new_status, changed_at)
SELECT bookings.id, 'CONFIRMED', 'COMPLETED', '2025-09-06 12:00:00'
FROM bookings
JOIN guests ON guests.id = bookings.guest_id
WHERE guests.email = 'petar.petrovic@gmail.com'
  AND bookings.unit_id = 105
  AND bookings.start_date = '2025-09-01'
  AND bookings.end_date = '2025-09-06'
  AND NOT EXISTS (
      SELECT 1 FROM booking_status_logs
      WHERE booking_id = bookings.id
        AND changed_at = '2025-09-06 12:00:00'
  );
