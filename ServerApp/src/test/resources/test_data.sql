INSERT INTO account (account_id, pin, first_name, last_name, account_holders_id, balance) VALUES (1, 1234, 'david', 'martinez', '12345678', 1000.0);

INSERT INTO account (account_id, pin, first_name, last_name, account_holders_id, balance) VALUES (2, 1234, 'david', 'martinez', '12345678', 10000.0);

INSERT INTO transaction (id, date_, type_, amount, description, account_id ) VALUES (1, '2019-01-12', 1, 1000.0, 'Santander payroll', 1);
INSERT INTO transaction (id, date_, type_, amount, description, account_id ) VALUES (2, '2019-01-13', 2, 1500.0, 'ATM av. americas', 1);
INSERT INTO transaction (id, date_, type_, amount, description, account_id ) VALUES (3, '2019-01-13', 3, 100.0, 'programed payment banamex', 1);
INSERT INTO transaction (id, date_, type_, amount, description, account_id ) VALUES (4, '2019-01-13', 3, 200.0, 'programed payment banamex', 1);
INSERT INTO transaction (id, date_, type_, amount, description, account_id ) VALUES (5, '2019-01-14', 4, 1000.0, 'paypal transfer', 1);