## Gateway MS

Gateway MS is an interface for interacting with the credit-conveyor project.

### Requirements

The application can be run locally, the requirements for setup are listed below.

#### Local

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.3/apache-maven-3.9.3-bin.zip)

#### Properties

1. Application will run by default on port `8079`
Configure the port by changing `server.port` in `application.properties`
2. This ms calls the **Application MS** and **Deal MS** endpoints. 
By default, base URLs are:
- `application.client.base-url=http://localhost:8080/application`
- `deal.client.base-url=http://localhost:8081/deal`

### Quick Start

#### Run Local

```sh
$ mvn spring-boot:run
```
