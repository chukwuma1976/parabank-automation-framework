-- seed.sql
INSERT INTO customers
    (first_name, last_name, username, ssn)
VALUES
    ('Calvin', 'Ellis', 'calvin.test', '123-45-6789'),
    ('Jane', 'Doe', 'jane.test', '987-65-4321');

INSERT INTO accounts
    (customer_id, type, balance)
VALUES
    (1, 'CHECKING', 15000.00),
    (1, 'SAVINGS', 5000.00),
    (2, 'CHECKING', 250.50);

INSERT INTO transactions
    (account_id, type, amount, description)
VALUES
    (1, 'Credit', 100.00, 'Deposit via Web Service'),
    (1, 'Debit', 200.00, 'Withdraw via Web Service'),
    (2, 'Debit', 500.00, 'Funds Transfer Sent');