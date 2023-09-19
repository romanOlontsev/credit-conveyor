## Credit-conveyor

The project is a prototype of a bank that issues loans to clients.

### Stack

- Java 
- SpringBoot
- PostgreSQL 
- JPA 
- Swagger 
- Kafka 
- JUnit
- Mockito
- TestContainers

### Requirements

The application can be run locally, the requirements for setup are listed below.

#### Local

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.3/apache-maven-3.9.3-bin.zip)

#### Docker

- [Docker](https://www.docker.com/get-docker)

#### Properties

1. Application will run by default on port `8081`.
   Configure the port by changing `server.port` in `application.properties`.

2. This MS calls the **Conveyor MS** endpoints.
   By default, base URL is `conveyor.client.base-url=http://localhost:8082/conveyor`.

3. [PostgreSQL](https://www.postgresql.org/docs/) is used as db by default.
    - [Schema](https://github.com/romanOlontsev/credit-conveyor/blob/develop/migrations/credit-app-schema.sql)
    - Properties:
        - `spring.datasource.url=jdbc:postgresql://localhost:8032/credit_app_db`
        - `spring.datasource.username=root`
        - `spring.datasource.password=root`
4. Kafka bootstrap servers by default on port `29092`.

### Quick Start

#### Run Docker

```sh
$ docker-compose -f docker-compose.projectup.yml up
```

Application will run by default on port `8080`

Configure the port by changing services.api.ports in **docker-compose.projectup.yml**. 
Port 8080 was used by default so the value is easy to identify and change in the configuration file.

### API

Interaction with the project occurs in the **Gateway MS**. 
The list of endpoints can be viewed via the link: [http://localhost:8080/swagger-ui](http://localhost:8080/swagger-ui)

#### Endpoints

- `POST` `/application`

Submits a loan application. 
Four loan offers with different conditions are returned (without insurance, with insurance, with a salary client, 
with insurance and a salary client) or refusal.

Body:

```json
{
   "amount": 0,
   "term": 0,
   "first_name": "string",
   "last_name": "string",
   "middle_name": "string",
   "email": "string",
   "birth_date": "2000-01-01",
   "passport_series": "string",
   "passport_number": "string"
}
```
Response:

```json
[
  {
    "application_id": 0,
    "requested_amount": 0,
    "total_amount": 0,
    "term": 0,
    "monthly_payment": 0,
    "rate": 0,
    "is_insurance_enabled": true,
    "is_salary_client": true
  }
]
```

- `POST` `/application/apply`

Selects one of the offers. An email is sent to the client with a link to complete the registration.

Body:

```json
{
  "application_id": 0,
  "requested_amount": 0,
  "total_amount": 0,
  "term": 0,
  "monthly_payment": 0,
  "rate": 0,
  "is_insurance_enabled": true,
  "is_salary_client": true
}
```

- `POST` `/application/registration/{applicationId}`

A request is sent with the client’s complete information about the employer and registration. Data scoring occurs and 
all loan data is calculated. Afterwards, an email is sent to the client with approval or refusal. 

ApplicationId is passed as a parameter.

Body:

```json
{
  "gender": "MALE",
  "marital_status": "MARRIED",
  "dependent_amount": 0,
  "passport_issue_date": "2000-01-01",
  "passport_issue_branch": "string",
  "employment": {
    "status": "SELF_EMPLOYED",
    "employer_inn": "string",
    "salary": 0,
    "position": "WORKER",
    "work_experience_total": 0,
    "work_experience_current": 0
  },
  "account": "string"
}
```

- `POST` `/application/document/{applicationId}`

Sends a request to generate documents. In response, the client is sent by email documents for signing as a pdf file and 
a link to a request to agree to the terms.

ApplicationId is passed as a parameter.

- `POST` `/application/document/{applicationId}/sign`

Sends a code and a link to sign documents by email, where the client must send the received code.

- `POST` `/application/document/{applicationId}/sign/code`

If the received code matches the one sent, it issues a credit and sends a notification by email.