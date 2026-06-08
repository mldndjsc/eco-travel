INSERT INTO tax_rates (tax_name, percentage)
SELECT 'PDV', 20.00
WHERE NOT EXISTS (SELECT 1 FROM tax_rates WHERE tax_name = 'PDV');

INSERT INTO tax_rates (tax_name, percentage)
SELECT 'BORAVISNA_TAKSA', 5.00
WHERE NOT EXISTS (SELECT 1 FROM tax_rates WHERE tax_name = 'BORAVISNA_TAKSA');

INSERT INTO tax_rates (tax_name, percentage)
SELECT 'REDUCED_TAX', 10.00
WHERE NOT EXISTS (SELECT 1 FROM tax_rates WHERE tax_name = 'REDUCED_TAX');

INSERT INTO payment_methods (method_name, is_active)
SELECT 'CARD', true
WHERE NOT EXISTS (SELECT 1 FROM payment_methods WHERE method_name = 'CARD');

INSERT INTO payment_methods (method_name, is_active)
SELECT 'CASH', true
WHERE NOT EXISTS (SELECT 1 FROM payment_methods WHERE method_name = 'CASH');

INSERT INTO payment_methods (method_name, is_active)
SELECT 'BANK_TRANSFER', true
WHERE NOT EXISTS (SELECT 1 FROM payment_methods WHERE method_name = 'BANK_TRANSFER');

INSERT INTO payment_methods (method_name, is_active)
SELECT 'CRYPTO', false
WHERE NOT EXISTS (SELECT 1 FROM payment_methods WHERE method_name = 'CRYPTO');

INSERT INTO invoices (invoice_number, subtotal, total_amount, status, booking_id, tax_rates_id)
SELECT 'RCN1', 250.00, 300.00, 'PAID', 1, tax_rates.id
FROM tax_rates
WHERE tax_rates.tax_name = 'PDV'
  AND NOT EXISTS (SELECT 1 FROM invoices WHERE invoice_number = 'RCN1');

INSERT INTO invoices (invoice_number, subtotal, total_amount, status, booking_id, tax_rates_id)
SELECT 'RCN2', 120.00, 126.00, 'UNPAID', 2, tax_rates.id
FROM tax_rates
WHERE tax_rates.tax_name = 'BORAVISNA_TAKSA'
  AND NOT EXISTS (SELECT 1 FROM invoices WHERE invoice_number = 'RCN2');

INSERT INTO invoices (invoice_number, subtotal, total_amount, status, booking_id, tax_rates_id)
SELECT 'RCN3', 300.00, 360.00, 'PARTIALLY_PAID', 3, tax_rates.id
FROM tax_rates
WHERE tax_rates.tax_name = 'PDV'
  AND NOT EXISTS (SELECT 1 FROM invoices WHERE invoice_number = 'RCN3');

INSERT INTO invoices (invoice_number, subtotal, total_amount, status, booking_id, tax_rates_id)
SELECT 'RCN4', 140.00, 147.00, 'CANCELLED', 4, tax_rates.id
FROM tax_rates
WHERE tax_rates.tax_name = 'BORAVISNA_TAKSA'
  AND NOT EXISTS (SELECT 1 FROM invoices WHERE invoice_number = 'RCN4');

INSERT INTO invoices (invoice_number, subtotal, total_amount, status, booking_id, tax_rates_id)
SELECT 'RCN5', 400.00, 480.00, 'PAID', 5, tax_rates.id
FROM tax_rates
WHERE tax_rates.tax_name = 'PDV'
  AND NOT EXISTS (SELECT 1 FROM invoices WHERE invoice_number = 'RCN5');

INSERT INTO transactions (amount, date, payment_method_id, invoice_id)
SELECT 300.00, '2025-06-22 13:20:00', payment_methods.id, invoices.id
FROM payment_methods
JOIN invoices ON invoices.invoice_number = 'RCN1'
WHERE payment_methods.method_name = 'CARD'
  AND NOT EXISTS (
      SELECT 1 FROM transactions
      WHERE amount = 300.00
        AND date = '2025-06-22 13:20:00'
        AND invoice_id = invoices.id
  );

INSERT INTO transactions (amount, date, payment_method_id, invoice_id)
SELECT 150.00, '2025-07-05 15:00:00', payment_methods.id, invoices.id
FROM payment_methods
JOIN invoices ON invoices.invoice_number = 'RCN3'
WHERE payment_methods.method_name = 'BANK_TRANSFER'
  AND NOT EXISTS (
      SELECT 1 FROM transactions
      WHERE amount = 150.00
        AND date = '2025-07-05 15:00:00'
        AND invoice_id = invoices.id
  );

INSERT INTO transactions (amount, date, payment_method_id, invoice_id)
SELECT 480.00, '2025-08-05 18:45:00', payment_methods.id, invoices.id
FROM payment_methods
JOIN invoices ON invoices.invoice_number = 'RCN5'
WHERE payment_methods.method_name = 'CASH'
  AND NOT EXISTS (
      SELECT 1 FROM transactions
      WHERE amount = 480.00
        AND date = '2025-08-05 18:45:00'
        AND invoice_id = invoices.id
  );
