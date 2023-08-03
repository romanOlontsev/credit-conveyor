--liquibase formatted sql

--changeset admin:1
-- INSERT INTO credit_app.application (application_id, client_id, status, applied_offer, status_history)
-- VALUES (1, 1, "NONE",);