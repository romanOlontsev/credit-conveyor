--liquibase formatted sql

--changeset admin:1
CREATE SCHEMA IF NOT EXISTS credit_app;
CREATE TABLE IF NOT EXISTS credit_app.application
(
    application_id bigint NOT NULL PRIMARY KEY,
    client_id      bigint NOT NULL,
    status         varchar NOT NULL ,
    creation_date  timestamp DEFAULT now(),
    applied_offer  jsonb,
    sign_date      timestamp DEFAULT now(),
    status_history jsonb
);
