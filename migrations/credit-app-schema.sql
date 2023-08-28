--liquibase formatted sql

--changeset admin:26
CREATE SCHEMA IF NOT EXISTS credit_app;
CREATE TABLE IF NOT EXISTS credit_app.credit
(
    credit_id        bigint  NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    amount           decimal NOT NULL,
    term             int     NOT NULL,
    monthly_payment  decimal,
    rate             decimal,
    psk              decimal,
    payment_schedule jsonb,
    insurance_enable boolean DEFAULT false,
    salary_client    boolean DEFAULT false,
    credit_status    varchar(12)
);
CREATE TABLE IF NOT EXISTS credit_app.passport
(
    passport_id  bigint     NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    series       varchar(4) NOT NULL,
    number       varchar(6) NOT NULL,
    issue_branch varchar(255),
    issue_date   date
);
CREATE TABLE IF NOT EXISTS credit_app.employment
(
    employment_id           bigint      NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    status                  varchar(24) NOT NULL,
    employer_inn            varchar(12) NOT NULL,
    salary                  decimal     NOT NULL,
    position                varchar(16) NOT NULL,
    work_experience_total   int         NOT NULL,
    work_experience_current int         NOT NULL
);


CREATE TABLE IF NOT EXISTS credit_app.client
(
    client_id        bigint       NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    first_name       varchar(255) NOT NULL,
    last_name        varchar(255) NOT NULL,
    middle_name      varchar(255),
    birth_date       date         NOT NULL,
    email            varchar(72)  NOT NULL,
    gender           varchar(12),
    marital_status   varchar(16),
    dependent_amount int,
    passport_id      bigint REFERENCES credit_app.passport ON DELETE CASCADE,
    employment_id    bigint REFERENCES credit_app.employment ON DELETE CASCADE,
    account          varchar(255)
);
CREATE TABLE IF NOT EXISTS credit_app.application
(
    application_id bigint      NOT NULL NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    client_id      bigint      NOT NULL REFERENCES credit_app.client ON DELETE CASCADE,
    credit_id      bigint REFERENCES credit_app.credit ON DELETE CASCADE,
    status         varchar(24) NOT NULL,
    creation_date  timestamp(6) WITH TIME ZONE DEFAULT now(),
    applied_offer  jsonb,
    sign_date      timestamp(6) WITH TIME ZONE,
    ses_code       varchar(64) NOT NULL,
    status_history jsonb       NOT NULL
);
