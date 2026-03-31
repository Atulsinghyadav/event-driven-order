# Event Driven Order Processing System

## Overview
This project is a backend event-driven order processing system built with Spring Boot, Kafka, PostgreSQL, and Flyway.

It demonstrates:
- Order creation through a REST API
- Transactional outbox pattern for reliable event publishing
- Kafka-based asynchronous communication
- Idempotent event consumption in inventory-service
- Separate database ownership per service

## Services

### order-service
Responsible for:
- accepting order creation requests
- validating input
- persisting orders and order items
- writing outbox events
- publishing events to Kafka

### inventory-service
Responsible for:
- consuming `order-events` from Kafka
- preventing duplicate processing using `processed_events`
- creating inventory reservations
- exposing reservation lookup by `orderId`

## Tech Stack
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Kafka
- PostgreSQL
- Flyway
- Docker Compose
- Maven

## Architecture Flow
1. Client sends `POST /orders` to `order-service`
2. `order-service` validates and stores order data
3. `order-service` writes an outbox event in the same DB transaction
4. Outbox publisher publishes event to Kafka topic `order-events`
5. `inventory-service` consumes the event
6. `inventory-service` checks idempotency using `processed_events`
7. If not already processed, it creates inventory reservations
8. Reservations can be queried using `GET /inventory/reservations/{orderId}`

## Run Locally

### 1. Start infrastructure
```bash
docker compose -f docker/docker-compose.yml up -d
```


### 2. Run order-service
cd services/order-service
./mvnw spring-boot:run -Dspring-boot.run.profiles=local


### 3. Run inventory-service
cd services/inventory-service
./services/order-service/mvnw -f services/inventory-service/pom.xml spring-boot:run -Dspring-boot.run.profiles=local


### Sample API Usage

POST http://localhost:8080/orders

{
  "customerId": "cust-101",
  "currency": "INR",
  "totalAmount": 1200.50,
  "items": [
    {
      "sku": "SKU-1",
      "quantity": 2,
      "unitPrice": 600.25
    }
  ]
}

Get Inventory Reservations
GET http://localhost:8081/inventory/reservations/{orderId}

Reliability Features
Transactional outbox pattern
Retry-aware outbox publishing
Idempotent consumer using processed_events
Separate DB ownership for each service
Flyway-managed schema versioning

