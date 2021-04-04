CREATE TABLE role (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE person (
    id UUID PRIMARY KEY,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    patronymic VARCHAR(64) NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    phone_number CHAR(16) NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    passport_number_and_series CHAR(11) NOT NULL UNIQUE
);

CREATE TABLE person_role (
    id SMALLINT PRIMARY KEY,
    person_id UUID REFERENCES person(id),
    role_id SMALLINT REFERENCES role(id),
    UNIQUE(person_id, role_id)
);

CREATE TABLE loan (
    id UUID PRIMARY KEY,
    min_amount DECIMAL(19, 4) NOT NULL CHECK (min_amount > 0),
    max_amount DECIMAL(19, 4) NOT NULL CHECK (max_amount > loan.min_amount),
    min_duration_months SMALLINT NOT NULL CHECK (min_duration_months > 0),
    max_duration_months SMALLINT NOT NULL CHECK (max_duration_months > loan.min_duration_months),
    interest_rate REAL CHECK(interest_rate >= 0 AND interest_rate <= 100)
);

CREATE TABLE loan_case (
    id UUID PRIMARY KEY,
    client_id UUID REFERENCES person(id),
    loan_id UUID REFERENCES loan(id),
    amount DECIMAL(19, 4) NOT NULL CHECK (amount > 0),
    duration_months SMALLINT NOT NULL CHECK (duration_months > 0),
    status_bank_side VARCHAR(8) DEFAULT 'PENDING',
    status_client_side VARCHAR(8) DEFAULT 'PENDING',
    confirmation_date DATE NULL,
    closed BOOLEAN DEFAULT false
);

CREATE TABLE payment (
    id UUID PRIMARY KEY,
    loan_case_id UUID REFERENCES loan_case(id),
    order_number SMALLINT NOT NULL CHECK (order_number > 0),
    date DATE NULL,
    amount DECIMAL(19, 4) NOT NULL CHECK (amount > 0),
    loan_repayment_amount DECIMAL(19, 4) NOT NULL CHECK (loan_repayment_amount >= 0),
    interest_repayment_amount DECIMAL(19, 4) NOT NULL CHECK (interest_repayment_amount >= 0),
    paid_out BOOLEAN DEFAULT false
);
