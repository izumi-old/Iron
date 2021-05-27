CREATE TABLE role (
    id BIGINT PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE person (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    patronymic VARCHAR(64) NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    phone_number CHAR(16) NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    passport_number_and_series CHAR(11) NOT NULL UNIQUE
);

CREATE TABLE person_role (
    id BIGINT PRIMARY KEY,
    person_id BIGINT REFERENCES person(id),
    role_id BIGINT REFERENCES role(id),
    UNIQUE(person_id, role_id)
);

CREATE TABLE loan (
    id BIGINT PRIMARY KEY,
    min_amount DECIMAL(19, 4) NOT NULL,
    max_amount DECIMAL(19, 4) NOT NULL,
    min_duration_months SMALLINT NOT NULL,
    max_duration_months SMALLINT NOT NULL,
    interest_rate REAL CHECK(interest_rate >= 0 AND interest_rate <= 100)
);

CREATE TABLE loan_case (
    id BIGINT PRIMARY KEY,
    client_id BIGINT REFERENCES person(id),
    loan_id BIGINT REFERENCES loan(id),
    amount DECIMAL(19, 4) NOT NULL CHECK (amount > 0),
    duration_months SMALLINT NOT NULL CHECK (duration_months > 0),
    status_bank_side VARCHAR(8) DEFAULT 'PENDING',
    status_client_side VARCHAR(8) DEFAULT 'PENDING',
    confirmation_date DATE NULL,
    closed BOOLEAN DEFAULT false
);

CREATE TABLE payment (
    id BIGINT PRIMARY KEY,
    loan_case_id BIGINT REFERENCES loan_case(id),
    order_number SMALLINT NOT NULL CHECK (order_number > 0),
    date DATE NULL,
    amount DECIMAL(19, 4) NOT NULL CHECK (amount > 0),
    loan_repayment_amount DECIMAL(19, 4) NOT NULL CHECK (loan_repayment_amount >= 0),
    interest_repayment_amount DECIMAL(19, 4) NOT NULL CHECK (interest_repayment_amount >= 0),
    paid_out BOOLEAN DEFAULT false
);
