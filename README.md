# z-customer-data-service

## Overview

z-customer-data-service is a Spring Boot–based microservice that provides customer profile data in a GenAI multi-agent system.

It is designed as **Agent B**, responsible for serving structured customer data to other services such as:

- Agent A (orchestrator)
- Agent C (compliance review)

This service exposes REST APIs and integrates with PostgreSQL using JPA, supporting pagination and filtering.

---

## Key Responsibilities

- Provide customer profile data via REST APIs
- Support lookup by customerId
- Support pagination and filtering
- Persist and manage customer data
- Act as a reliable data source for compliance evaluation

---

## Architecture Role

```text
z-wealth-knowledge-rag (Agent A)
        |
        v
z-compliance-review-service (Agent C)
        |
        v
z-customer-data-service (Agent B)
        |
        v
PostgreSQL
```

---

## Tech Stack

- Java 25
- Spring Boot 4.1.0
- Spring AI 2.0.0 dependency management
- springdoc-openapi 3.0.2
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Redis
- Kafka client
- Spring Actuator
- JUnit 5 + Mockito + AssertJ
- JaCoCo

---

## Service Port

```text
http://localhost:8082
```

## Runtime Dependencies

- PostgreSQL: localhost:5432/appdb
- Redis: localhost:6379
- Kafka: localhost:9092

---

## Database Schema

```sql
CREATE TABLE customer_profile (
    customer_id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INTEGER NOT NULL,
    annual_income DOUBLE PRECISION NOT NULL,
    risk_level VARCHAR(64),
    investment_objective VARCHAR(128),
    kyc_status VARCHAR(64)
);
```

---

## API

Interactive OpenAPI documentation is available while the service is running:

```text
http://localhost:8082/swagger-ui.html
```

The generated OpenAPI specification is available at:

```text
http://localhost:8082/v3/api-docs
```

### Get Customer Profile

```http
GET /customers/{customerId}
```

```json
{
  "customerId": "C1001",
  "name": "Alex Kim",
  "age": 35,
  "annualIncome": 125000.0,
  "riskLevel": "HIGH",
  "investmentObjective": "GROWTH",
  "kycStatus": "VERIFIED"
}
```

Fallback mock exists for `C9999`.

---

### Search Customers

```http
POST /customers/search
```

```json
{
  "riskLevel": "HIGH",
  "kycStatus": "VERIFIED",
  "minIncome": 100000,
  "investmentObjective": "GROWTH",
  "page": 0,
  "size": 10
}
```

Rules:

- AND filters
- case-insensitive
- size capped at 50
- sorted by customerId

---

## Health

```http
GET /actuator/health
```

Checks:

- DB connection
- Redis
- Kafka

---

## Filters & Interceptors

- RequestLoggingFilter (correlation id + logging)
- SimpleRateLimiterFilter (60 req/min/IP)
- RequestTimingInterceptor (latency tracking)

---

## Security

- API key placeholder (not enforced)
- All endpoints currently open
- Designed for future security extension

---

## Run

JDK 25 is required for local builds and execution.

```bash
./mvnw spring-boot:run
```

---

## Build

```bash
./mvnw package
java -jar target/z-customer-data-service-0.0.1-SNAPSHOT.jar
```

---

## Docker

The multi-stage Docker build compiles the service with Eclipse Temurin JDK 25 and runs it on the smaller Java 25 JRE image as a non-root user.

```bash
docker build -t z-customer-data-service:jdk25 .
docker run --rm -p 8082:8082 z-customer-data-service:jdk25
```

The configured PostgreSQL, Redis, and Kafka addresses must be reachable from the container. Override the corresponding Spring properties for your Docker environment as needed.

---

## Tests

```bash
./mvnw test
./mvnw verify
```

JaCoCo:

```text
target/site/jacoco/index.html
```

---

## Design Principles

- Stateless service
- DB as source of truth
- Clean layered architecture
- Specification-based dynamic queries
- Pagination for scalability

---

## Failure Handling

- DB failure → fail fast
- Invalid input → 400
- Internal error → 500

---
