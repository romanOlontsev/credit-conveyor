--liquibase formatted sql

--changeset admin:24
INSERT INTO credit_app.credit (amount, term, monthly_payment, rate, psk, payment_schedule, insurance_enable,
                               salary_client, credit_status)
VALUES (10000.00, 12, 921.57, 19.00, 11058.84,
        '[
          {
            "number": 1,
            "date": "2023-09-08",
            "total_payment": 926.35,
            "debt_payment": 761.97,
            "interest_payment": 164.38,
            "remaining_debt": 9238.03
          },
          {
            "number": 2,
            "date": "2023-10-08",
            "total_payment": 926.35,
            "debt_payment": 769.43,
            "interest_payment": 156.92,
            "remaining_debt": 8468.6
          },
          {
            "number": 3,
            "date": "2023-11-08",
            "total_payment": 926.35,
            "debt_payment": 787.14,
            "interest_payment": 139.21,
            "remaining_debt": 7681.46
          },
          {
            "number": 4,
            "date": "2023-12-08",
            "total_payment": 926.35,
            "debt_payment": 795.87,
            "interest_payment": 130.48,
            "remaining_debt": 6885.59
          },
          {
            "number": 5,
            "date": "2024-01-08",
            "total_payment": 926.35,
            "debt_payment": 809.71,
            "interest_payment": 116.64,
            "remaining_debt": 6075.88
          },
          {
            "number": 6,
            "date": "2024-02-08",
            "total_payment": 926.35,
            "debt_payment": 830.07,
            "interest_payment": 96.28,
            "remaining_debt": 5245.81
          },
          {
            "number": 7,
            "date": "2024-03-08",
            "total_payment": 926.35,
            "debt_payment": 837.49,
            "interest_payment": 88.86,
            "remaining_debt": 4408.32
          },
          {
            "number": 8,
            "date": "2024-04-08",
            "total_payment": 926.35,
            "debt_payment": 854.08,
            "interest_payment": 72.27,
            "remaining_debt": 3554.24
          },
          {
            "number": 9,
            "date": "2024-05-08",
            "total_payment": 926.35,
            "debt_payment": 866.14,
            "interest_payment": 60.21,
            "remaining_debt": 2688.1
          },
          {
            "number": 10,
            "date": "2024-06-08",
            "total_payment": 926.35,
            "debt_payment": 882.28,
            "interest_payment": 44.07,
            "remaining_debt": 1805.82
          },
          {
            "number": 11,
            "date": "2024-07-08",
            "total_payment": 926.35,
            "debt_payment": 895.76,
            "interest_payment": 30.59,
            "remaining_debt": 910.06
          },
          {
            "number": 12,
            "date": "2024-08-08",
            "total_payment": 925.48,
            "debt_payment": 910.06,
            "interest_payment": 15.42,
            "remaining_debt": 0
          }
        ]', true, true, 'CALCULATED');
INSERT INTO credit_app.credit (amount, term, monthly_payment, rate, psk, payment_schedule, insurance_enable,
                               salary_client, credit_status)
VALUES (20000.00, 6, 3540.45, 21.00, 21242.70,
        '[
          {
            "number": 1,
            "date": "2023-09-08",
            "total_payment": 3540.45,
            "debt_payment": 3195.24,
            "interest_payment": 345.21,
            "remaining_debt": 16804.76
          },
          {
            "number": 2,
            "date": "2023-10-08",
            "total_payment": 3540.45,
            "debt_payment": 3240.73,
            "interest_payment": 299.72,
            "remaining_debt": 13564.03
          },
          {
            "number": 3,
            "date": "2023-11-08",
            "total_payment": 3540.45,
            "debt_payment": 3306.33,
            "interest_payment": 234.12,
            "remaining_debt": 10257.7
          },
          {
            "number": 4,
            "date": "2023-12-08",
            "total_payment": 3540.45,
            "debt_payment": 3357.5,
            "interest_payment": 182.95,
            "remaining_debt": 6900.2
          },
          {
            "number": 5,
            "date": "2024-01-08",
            "total_payment": 3540.45,
            "debt_payment": 3417.72,
            "interest_payment": 122.73,
            "remaining_debt": 3482.48
          },
          {
            "number": 6,
            "date": "2024-02-08",
            "total_payment": 3540.43,
            "debt_payment": 3482.48,
            "interest_payment": 57.95,
            "remaining_debt": 0
          }
        ]', false, false, 'CALCULATED');

INSERT INTO credit_app.passport(series, number, issue_branch, issue_date)
VALUES (1234, 212334, 'UFMS RUSSIA 5', '2005-01-01');
INSERT INTO credit_app.passport(series, number, issue_branch, issue_date)
VALUES (3221, 214235, 'UFMS RUSSIA 3', '2009-06-21');

INSERT INTO credit_app.employment(status, employer_inn, salary, position, work_experience_total,
                                  work_experience_current)
VALUES ('SELF_EMPLOYED', '234322112287', 56232.20, 'MIDDLE_MANAGER', 12, 6);
INSERT INTO credit_app.employment(status, employer_inn, salary, position, work_experience_total,
                                  work_experience_current)
VALUES ('BUSINESS_OWNER', '543322112765', 78233.60, 'TOP_MANAGER', 24, 12);

INSERT INTO credit_app.client(first_name, last_name, middle_name, birth_date, email, gender, marital_status,
                              dependent_amount, passport_id, employment_id, account)
VALUES ('Malfurion', 'Fury', 'Olen', '2023-08-11T19:32:17.3441307', 'malfurion@olen.dog', 'MALE', 'MARRIED', 1, 1, 1, '1234 Tinkoff');
INSERT INTO credit_app.client(first_name, last_name, birth_date, email, gender, marital_status,
                              dependent_amount, passport_id, employment_id, account)
VALUES ('Arthas', 'Menethil', '2023-08-11T19:32:17.3441307', 'lich@king.com', 'MALE', 'DIVORCED', 0, 2, 2, '4324 Sber');

INSERT INTO credit_app.application(client_id, credit_id, status, creation_date, applied_offer, ses_code,
                                   status_history)
VALUES (1, 1, 'PREPARE_DOCUMENTS', '2023-08-11T19:32:17.3441307',
'{
  "application_id": 6,
  "requested_amount": 125000,
  "total_amount": 146254.5,
  "term": 14,
  "monthly_payment": 10446.75,
  "rate": 26,
  "is_insurance_enabled": true,
  "is_salary_client": true
}', '1234-1323-3212-6543',
'[{
    "time": "2023-08-11T19:39:55.2076183",
    "status": "PREAPPROVAL",
    "change_type": "AUTOMATIC"
}]');
INSERT INTO credit_app.application(client_id, credit_id, status, creation_date, applied_offer, ses_code,
                                   status_history)
VALUES (2, 2, 'DOCUMENT_CREATED', '2023-08-11T19:32:17.3441307', '{
  "application_id": 6,
  "requested_amount": 125000,
  "total_amount": 149688.98,
  "term": 14,
  "monthly_payment": 10692.07,
  "rate": 30,
  "is_insurance_enabled": false,
  "is_salary_client": false
}', '9877-1323-1235-8765',
        '[{
          "time": "2023-08-11T19:39:55.2076183",
          "status": "PREAPPROVAL",
          "change_type": "AUTOMATIC"
        }]');