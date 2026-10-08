INSERT INTO java.products (account_number, balance, product_type, user_id)
SELECT 'ACC-001', 1000.00, 'ACCOUNT', u.id FROM java.users u WHERE u.username = 'maria'
ON CONFLICT (account_number) DO NOTHING;

INSERT INTO java.products (account_number, balance, product_type, user_id)
SELECT 'CRD-001', 500.50, 'CARD', u.id FROM java.users u WHERE u.username = 'maria'
ON CONFLICT (account_number) DO NOTHING;

INSERT INTO java.products (account_number, balance, product_type, user_id)
SELECT 'ACC-002', 2500.00, 'ACCOUNT', u.id FROM java.users u WHERE u.username = 'ivan'
ON CONFLICT (account_number) DO NOTHING;

INSERT INTO java.products (account_number, balance, product_type, user_id)
SELECT 'CRD-002', 100.00, 'CARD', u.id FROM java.users u WHERE u.username = 'olga'
ON CONFLICT (account_number) DO NOTHING;