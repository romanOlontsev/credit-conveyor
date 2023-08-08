--liquibase formatted sql

--changeset admin:10
CREATE SCHEMA IF NOT EXISTS credit_app;
CREATE TABLE IF NOT EXISTS credit_app.credit
(
    credit_id        bigint      NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    amount           decimal     NOT NULL,
    term             int         NOT NULL,
    monthly_payment  decimal     NOT NULL,
    rate             decimal     NOT NULL,
    psk              decimal     NOT NULL,
    payment_schedule jsonb       NOT NULL,
    insurance_enable boolean     NOT NULL DEFAULT false,
    salary_client    boolean     NOT NULL DEFAULT false,
    credit_status    varchar(12) NOT NULL
);
CREATE TABLE IF NOT EXISTS credit_app.passport
(
    passport_id  bigint       NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    series       varchar(4)   NOT NULL,
    number       varchar(6)   NOT NULL,
    issue_branch varchar(255) NOT NULL,
    issue_date   date         NOT NULL
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
    client_id        bigint      NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1) PRIMARY KEY,
    first_name       varchar     NOT NULL,
    last_name        varchar     NOT NULL,
    middle_name      varchar,
    birth_date       date        NOT NULL,
    email            varchar(56) NOT NULL,
    gender           varchar(12) NOT NULL,
    marital_status   varchar(16) NOT NULL,
    dependent_amount int         NOT NULL,
    passport_id      bigint      NOT NULL REFERENCES credit_app.passport ON DELETE CASCADE,
    employment_id    bigint      NOT NULL REFERENCES credit_app.employment ON DELETE CASCADE,
    account          varchar     NOT NULL
);
CREATE TABLE IF NOT EXISTS credit_app.application
(
    application_id bigint      NOT NULL NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
    client_id      bigint      NOT NULL REFERENCES credit_app.client ON DELETE CASCADE,
    credit_id      bigint      NOT NULL REFERENCES credit_app.credit ON DELETE CASCADE,
    status         varchar(24) NOT NULL,
    creation_date  timestamp DEFAULT now(),
    applied_offer  jsonb       NOT NULL,
    sign_date      timestamp DEFAULT now(),
    ses_code       varchar     NOT NULL,
    status_history jsonb       NOT NULL
);
