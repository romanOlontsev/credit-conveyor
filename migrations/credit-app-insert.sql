--liquibase formatted sql

--changeset admin:6
INSERT INTO credit_app.credit (amount, term, monthly_payment, rate, psk, payment_schedule, credit_status)
VALUES (10000.00, 12, 921.57, 19.00, 11058.84,
'[
    {
      "number": 1,
      "date": "2023-09-07",
      "totalPayment": 921.57,
      "debtPayment": 765.41,
      "interestPayment": 156.16,
      "remainingDebt": 9234.59
    },
    {
      "number": 2,
      "date": "2023-10-07",
      "totalPayment": 921.57,
      "debtPayment": 772.55,
      "interestPayment": 149.02,
      "remainingDebt": 8462.04
    },
    {
      "number": 3,
      "date": "2023-11-07",
      "totalPayment": 921.57,
      "debtPayment": 789.42,
      "interestPayment": 132.15,
      "remainingDebt": 7672.62
    },
    {
      "number": 4,
      "date": "2023-12-07",
      "totalPayment": 921.57,
      "debtPayment": 797.76,
      "interestPayment": 123.81,
      "remainingDebt": 6874.86
    },
    {
      "number": 5,
      "date": "2024-01-07",
      "totalPayment": 921.57,
      "debtPayment": 810.93,
      "interestPayment": 110.64,
      "remainingDebt": 6063.93
    },
    {
      "number": 6,
      "date": "2024-02-07",
      "totalPayment": 921.57,
      "debtPayment": 830.28,
      "interestPayment": 91.29,
      "remainingDebt": 5233.65
    },
    {
      "number": 7,
      "date": "2024-03-07",
      "totalPayment": 921.57,
      "debtPayment": 837.35,
      "interestPayment": 84.22,
      "remainingDebt": 4396.3
    },
    {
      "number": 8,
      "date": "2024-04-07",
      "totalPayment": 921.57,
      "debtPayment": 853.1,
      "interestPayment": 68.47,
      "remainingDebt": 3543.2
    },
    {
      "number": 9,
      "date": "2024-05-07",
      "totalPayment": 921.57,
      "debtPayment": 864.55,
      "interestPayment": 57.02,
      "remainingDebt": 2678.65
    },
    {
      "number": 10,
      "date": "2024-06-07",
      "totalPayment": 921.57,
      "debtPayment": 879.85,
      "interestPayment": 41.72,
      "remainingDebt": 1798.8
    },
    {
      "number": 11,
      "date": "2024-07-07",
      "totalPayment": 921.57,
      "debtPayment": 892.62,
      "interestPayment": 28.95,
      "remainingDebt": 906.18
    },
    {
      "number": 12,
      "date": "2024-08-07",
      "totalPayment": 920.76,
      "debtPayment": 906.18,
      "interestPayment": 14.58,
      "remainingDebt": 0
    }
]',
        'CALCULATED');