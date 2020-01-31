-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE account
(
    account_id         UUID           NOT NULL PRIMARY KEY DEFAULT public.uuid_generate_v4(),
    balance            NUMERIC(50, 2) NOT NULL,
    meta_active        BOOLEAN        NOT NULL             DEFAULT true,
    meta_creation_date TIMESTAMP      NOT NULL,
    meta_modified_date TIMESTAMP      NOT NULL
);

CREATE TABLE transaction
(
    transaction_id     UUID           NOT NULL PRIMARY KEY DEFAULT public.uuid_generate_v4(),
    account_id         UUID           NOT NULL REFERENCES account (account_id),
    related_account_id UUID           NOT NULL,
    amount             NUMERIC(50, 2) NOT NULL,
    timestamp          TIMESTAMP      NOT NULL,
    meta_active        BOOLEAN        NOT NULL,
    meta_creation_date TIMESTAMP      NOT NULL,
    meta_modified_date TIMESTAMP      NOT NULL
);
