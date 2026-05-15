use emile_bank;
CREATE TABLE emile_bank.Customers (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  pass VARCHAR(50) NOT NULL,
  first_name VARCHAR(50) NOT NULL,
  middle_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  gender VARCHAR(25) NOT NULL,
  DOB DATE NOT NULL,
  email_address VARCHAR(50) DEFAULT NULL UNIQUE,
  country VARCHAR(50) NOT NULL,
  street VARCHAR(50) NOT NULL,
  city VARCHAR(50) NOT NULL,
  state VARCHAR(50) NOT NULL,
  zipcode VARCHAR(10) NOT NULL,
  ssn VARCHAR(25) NOT NULL UNIQUE,
  PRIMARY KEY (id)
); 

use emile_bank_accounts;
CREATE TABLE emile_bank_accounts.Accounts (
  id INT NOT NULL,
  balance FLOAT NOT NULL,
  routing_number VARCHAR(25) NOT NULL,
  account_number VARCHAR(25) NOT NULL,
  account_type varchar(10),
  date_time TIMESTAMP NOT NULL,
  eod TIMESTAMP DEFAULT ((CURRENT_TIMESTAMP + INTERVAL '1' DAY) - INTERVAL '1' SECOND),
  atm_debit_number VARCHAR(25) DEFAULT NULL,
  pin VARCHAR(5) DEFAULT NULL,
  exp_date VARCHAR(10) DEFAULT NULL,
  security_code VARCHAR(5) DEFAULT NULL,
  freeze BOOLEAN DEFAULT FALSE,
  missing BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (routing_number,account_number),
  CHECK(account_type='checking' or account_type = 'savings')
);

ALTER TABLE emile_bank_accounts.Accounts
ADD COLUMN vendor_id VARCHAR(50) AFTER missing;

ALTER TABLE emile_bank_accounts.Accounts
ADD COLUMN auto_pay BOOLEAN AFTER vendor_id;

ALTER TABLE emile_bank_accounts.Accounts
ADD COLUMN deposit FLOAT AFTER auto_pay;

ALTER TABLE emile_bank_accounts.Accounts
ADD COLUMN withdrawal FLOAT AFTER deposit;

ALTER TABLE emile_bank_accounts.Accounts
ADD COLUMN ipv6 VARCHAR(50) AFTER withdrawal;

ALTER TABLE emile_bank_accounts.Accounts
ADD COLUMN ipv4 VARCHAR(15) AFTER ipv6;

ALTER TABLE emile_bank_accounts.Accounts
DROP COLUMN vendor_id;

ALTER TABLE emile_bank_accounts.Accounts
DROP COLUMN auto_pay;

ALTER TABLE emile_bank_accounts.Accounts
DROP COLUMN deposit;

ALTER TABLE emile_bank_accounts.Accounts
DROP COLUMN withdrawal;

ALTER TABLE emile_bank_accounts.Accounts
DROP COLUMN ipv6;

ALTER TABLE emile_bank_accounts.Accounts
DROP COLUMN ipv4;

UPDATE emile_bank_accounts.Accounts SET freeze = false WHERE freeze = ' ';
UPDATE emile_bank_accounts.Accounts SET missing = false WHERE missing = ' ';
UPDATE emile_bank_accounts.Accounts SET freeze = NULL WHERE account_type = 'savings';
UPDATE emile_bank_accounts.Accounts SET missing = NULL WHERE account_type = 'savings';
UPDATE emile_bank_accounts.Accounts SET atm_debit_number = NULL WHERE account_type = 'savings';
UPDATE emile_bank_accounts.Accounts SET pin = NULL WHERE account_type = 'savings';
UPDATE emile_bank_accounts.Accounts SET exp_date = NULL WHERE account_type = 'savings';
UPDATE emile_bank_accounts.Accounts SET security_code = NULL WHERE account_type = 'savings';
UPDATE emile_bank_accounts.Accounts SET eod = DATE(date_time) + INTERVAL 1 DAY - INTERVAL 1 SECOND WHERE id IS NOT NULL;

use emile_bank_transactions;
CREATE TABLE emile_bank_transactions.Transactions (
  routing_number VARCHAR(25) NOT NULL,
  account_number VARCHAR(25) NOT NULL,
  vendor_id VARCHAR(50) DEFAULT NULL,
  auto_pay BOOLEAN DEFAULT NULL,
  deposit FLOAT DEFAULT NULL,
  withdrawal FLOAT DEFAULT NULL,
  ipv6 VARCHAR(50) DEFAULT NULL,
  ipv4 VARCHAR(15) DEFAULT NULL,
  date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  freeze BOOLEAN DEFAULT NULL,
  missing BOOLEAN DEFAULT NULL,
  PRIMARY KEY (routing_number,account_number)
);

ALTER TABLE emile_bank_transactions.Transactions
ADD COLUMN id TEXT AFTER missing;

ALTER TABLE emile_bank_transactions.Transactions
ADD COLUMN balance TEXT AFTER id;

ALTER TABLE emile_bank_transactions.Transactions
ADD COLUMN account_type TEXT AFTER balance;

ALTER TABLE emile_bank_transactions.Transactions
ADD COLUMN atm_debit_number TEXT AFTER account_type;

ALTER TABLE emile_bank_transactions.Transactions
ADD COLUMN pin TEXT AFTER atm_debit_number;

ALTER TABLE emile_bank_transactions.Transactions
ADD COLUMN exp_date TEXT AFTER pin;

ALTER TABLE emile_bank_transactions.Transactions
ADD COLUMN security_code TEXT AFTER exp_date;

ALTER TABLE emile_bank_transactions.Transactions
DROP COLUMN id;

ALTER TABLE emile_bank_transactions.Transactions
DROP COLUMN balance;

ALTER TABLE emile_bank_transactions.Transactions
DROP COLUMN account_type;

ALTER TABLE emile_bank_transactions.Transactions
DROP COLUMN atm_debit_number;

ALTER TABLE emile_bank_transactions.Transactions
DROP COLUMN pin;

ALTER TABLE emile_bank_transactions.Transactions
DROP COLUMN exp_date;

ALTER TABLE emile_bank_transactions.Transactions
DROP COLUMN security_code;

UPDATE emile_bank_transactions.Transactions SET freeze = false WHERE freeze = 'false';
UPDATE emile_bank_transactions.Transactions SET freeze = true WHERE freeze = 'true';
UPDATE emile_bank_transactions.Transactions SET freeze = false WHERE freeze = ' ';

UPDATE emile_bank_transactions.Transactions SET missing = false WHERE missing = 'false';
UPDATE emile_bank_transactions.Transactions SET missing = true WHERE missing = 'true';
UPDATE emile_bank_transactions.Transactions SET missing = false WHERE missing = ' ';

UPDATE emile_bank_transactions.Transactions SET auto_pay = false WHERE auto_pay = 'false';
UPDATE emile_bank_transactions.Transactions SET auto_pay = true WHERE auto_pay = 'true';
UPDATE emile_bank_transactions.Transactions SET auto_pay = false WHERE auto_pay = ' ';

UPDATE emile_bank_transactions.Transactions SET freeze = NULL WHERE account_type = 'savings';
UPDATE emile_bank_transactions.Transactions SET missing = NULL WHERE account_type = 'savings';
UPDATE emile_bank_transactions.Transactions SET auto_pay = NULL WHERE account_type = 'savings';
UPDATE emile_bank_transactions.Transactions SET vendor_id = NULL WHERE account_type = 'savings';

UPDATE emile_bank_transactions.Transactions SET deposit = NULL WHERE withdrawal != ' ';
UPDATE emile_bank_transactions.Transactions SET withdrawal = NULL WHERE deposit != ' ';

CREATE VIEW emile_bank.customer_info
 AS 
	SELECT c.id, c.username, c.pass, c.first_name, 
	c.middle_name, c.last_name, c.gender, c.DOB, 
	c.email_address, c.country, c.street, c.city, 
	c.state, c.zipcode, c.ssn, a.balance, 
	a.routing_number, a.account_number, 
	a.account_type, a.date_time, a.eod, 
	a.atm_debit_number, a.pin, a.exp_date, 
	a.security_code, a.freeze, a.missing, 
	t.vendor_id, t.auto_pay, t.deposit, 
	t.withdrawal, t.ipv6, t.ipv4
	FROM emile_bank_accounts.Accounts a 
	JOIN emile_bank.Customers c
	USING (id)
	JOIN emile_bank_transactions.Transactions t 
	USING (routing_number,account_number); 
    
    SELECT * FROM customer_info ORDER BY id ASC;