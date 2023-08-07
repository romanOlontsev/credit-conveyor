--liquibase formatted sql

--changeset admin:6
CREATE SCHEMA IF NOT EXISTS credit_app;
CREATE TABLE IF NOT EXISTS credit_app.credit
(
    credit_id        bigint  NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    amount           decimal NOT NULL,
    term             int     NOT NULL,
    monthly_payment  decimal NOT NULL,
    rate             decimal NOT NULL,
    psk              decimal NOT NULL,
    payment_schedule jsonb   NOT NULL,
    insurance_enable boolean NOT NULL DEFAULT false,
    salary_client    boolean NOT NULL DEFAULT false,
    credit_status    varchar NOT NULL
);
CREATE TABLE IF NOT EXISTS credit_app.client
(
    client_id        bigint  NOT NULL PRIMARY KEY,
    first_name       varchar NOT NULL,
    last_name        varchar NOT NULL,
    middle_name      varchar NOT NULL,
    birth_date       date DEFAULT '1999-01-08',
    email            varchar NOT NULL,
    gender           varchar NOT NULL,
    marital_status   varchar NOT NULL,
    dependent_amount int     NOT NULL,
    passport_id      jsonb   NOT NULL,
    employment_id    jsonb   NOT NULL,
    account          varchar NOT NULL
);
CREATE TABLE IF NOT EXISTS credit_app.application
(
    application_id bigint  NOT NULL PRIMARY KEY,
    client_id      bigint  NOT NULL REFERENCES credit_app.client ON DELETE CASCADE,
    credit_id      bigint  NOT NULL REFERENCES credit_app.credit ON DELETE CASCADE,
    status         varchar NOT NULL,
    creation_date  timestamp DEFAULT now(),
    applied_offer  jsonb   NOT NULL,
    sign_date      timestamp DEFAULT now(),
    ses_code       varchar NOT NULL,
    status_history jsonb   NOT NULL
);

