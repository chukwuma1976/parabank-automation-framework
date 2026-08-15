-- schema.sql
CREATE TABLE customers
(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    ssn VARCHAR(20)
);

CREATE TABLE accounts
(
    id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES customers(id),
    type VARCHAR(20) NOT NULL,
    balance DECIMAL(12,2) NOT NULL DEFAULT 0.00
);

CREATE TABLE transactions
(
    id SERIAL PRIMARY KEY,
    account_id INTEGER REFERENCES accounts(id),
    type VARCHAR(20) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    description VARCHAR(255),
    transaction_date TIMESTAMP DEFAULT NOW()
);