## Deal MS

- submits a request to **Conveyor MS** to calculate offers and calculate credit
- connects to database to work with credit data
- sends messages to **Dossier MS** for mail sending

### Requirements

The application can be run locally, the requirements for setup are listed below.

#### Local

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.3/apache-maven-3.9.3-bin.zip)

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

#### Run Local

```sh
$ mvn spring-boot:run
```
