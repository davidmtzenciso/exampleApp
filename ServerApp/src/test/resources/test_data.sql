INSERT INTO account (account_id, pin, first_name, last_name, account_holders_id, balance) VALUES (1, 1234, 'david', 'martinez', '12345678', 1000.0);

INSERT INTO account (account_id, pin, first_name, last_name, account_holders_id, balance) VALUES (2, 1234, 'david', 'martinez', '12345678', 10000.0);

INSERT INTO transaction (id, date_, type_, amount, description, account_id ) VALUES (1, '2019-01-12', 1, 1000.0, 'Santander payroll', 1);
INSERT INTO transaction (id, date_, type_, amount, description, account_id ) VALUES (2, '2019-01-13', 2, 1500.0, 'ATM av. americas', 1);
INSERT INTO transaction (id, date_, type_, amount, description, account_id ) VALUES (3, '2019-01-13', 3, 100.0, 'programed payment banamex', 1);
INSERT INTO transaction (id, date_, type_, amount, description, account_id ) VALUES (4, '2019-01-13', 3, 200.0, 'programed payment banamex', 1);
INSERT INTO transaction (id, date_, type_, amount, description, account_id ) VALUES (5, '2019-01-14', 4, 1000.0, 'paypal transfer', 1);

insert into oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) values ('oauth2-jwt-client','resource-server-rest-api','$2a$08$qvrzQZ7jJ7oy2p/msL4M0.l83Cd0jNsX6AJUitbgRXGzge4j035ha','read','password,authorization_code,refresh_token,client_credentials,implicit','https://www.getpostman.com/oauth2/callback','USER',10800,2592000,null,null);
insert into authority(name) values ('ADMIN');
insert into user_(account_expired, account_locked, credentials_expired, enabled, password, user_name) values (0,0,0,1,'$2a$08$qvrzQZ7jJ7oy2p/msL4M0.l83Cd0jNsX6AJUitbgRXGzge4j035ha','admin');
insert into user_authority (authority_id, user_id) values (1,1);