INSERT INTO tax_rates (tax_name, percentage) VALUES ( 'PDV', 20.00);
INSERT INTO tax_rates (tax_name, percentage) VALUES ( 'BORAVISNA_TAKSA', 5.00);
INSERT INTO tax_rates (tax_name, percentage) VALUES ( 'REDUCED_TAX', 10.00);

INSERT INTO payment_methods (method_name, is_active) VALUES ('CARD', true);
INSERT INTO payment_methods (method_name, is_active) VALUES ( 'CASH', true);
INSERT INTO payment_methods (method_name, is_active) VALUES ( 'BANK_TRANSFER', true);
INSERT INTO payment_methods (method_name, is_active) VALUES ( 'CRYPTO', false);

INSERT INTO invoices (invoice_number, subtotal, total_amount, status, booking_id, tax_rates_id) VALUES ( 'RCN1',250.00, 300.00, 'PAID', 1, 1);
INSERT INTO invoices (invoice_number, subtotal, total_amount, status, booking_id, tax_rates_id) VALUES ( 'RCN2',120.00, 126.00, 'UNPAID', 2, 2);
INSERT INTO invoices (invoice_number, subtotal, total_amount, status, booking_id, tax_rates_id) VALUES ( 'RCN3',300.00, 360.00, 'PARTIALLY_PAID', 3, 1);
INSERT INTO invoices (invoice_number, subtotal, total_amount, status, booking_id, tax_rates_id) VALUES ( 'RCN4',140.00, 147.00, 'CANCELLED', 4, 2);
INSERT INTO invoices (invoice_number, subtotal, total_amount, status, booking_id, tax_rates_id) VALUES ( 'RCN5',400.00, 480.00, 'PAID', 5, 1);

INSERT INTO transactions (amount, date, payment_method_id, invoice_id) VALUES ( 300.00, '2025-06-22 13:20:00', 1, 1);
INSERT INTO transactions (amount, date, payment_method_id, invoice_id) VALUES ( 150.00, '2025-07-05 15:00:00', 3, 3);
INSERT INTO transactions (amount, date, payment_method_id, invoice_id) VALUES ( 480.00, '2025-08-05 18:45:00', 2, 5);