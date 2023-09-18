## Dossier MS

Dossier MS sends email to users depending on the topic.

### Requirements

The application can be run locally, the requirements for setup are listed below.

#### Local

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.3/apache-maven-3.9.3-bin.zip)

#### Properties

1. To set links to the application form, you must change `gateway.client.base-url` in `application.properties`
2. This MS calls the **Deal MS** endpoint to get application info and to update application status.
   By default, base URL is `deal.client.base-url=http://localhost:8081/deal/admin`.
3. Kafka bootstrap servers by default on port `29092`.
4. Gmail is used as the smtp server.
   Properties:
    - `spring.mail.host=smtp.gmail.com`
    - `spring.mail.port=587`
    - `spring.mail.username` <-- must be set
    - `spring.mail.password` <-- must be set

### Quick Start

#### Run Local

```sh
$ mvn spring-boot:run
```
