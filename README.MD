# 🛰️ Event Driven Microservices Proof of Concept

A distributed microservices system designed for  **Space Traffic Management**, handling calculations asynchronously.

##  Architecture Design

The system implements an **Event-Driven Architecture** to ensure high decoupling and scalability:

* **Order Service (Producer):** REST API entry point for satellite operators. Initiates the Saga.
* **Inventory/Worker Service (Consumer):** Performs CPU-intensive orbit calculations.
* **Discovery Server (Eureka):** Service Registry for dynamic service location.
* **RabbitMQ (Cloud):** Message broker handling the asynchronous communication.

## 🚀 Tech Stack

* **Java 21** & **Spring Boot 3**
* **Spring Cloud** (Netflix Eureka)
* **RabbitMQ** (CloudAMQP)
* **Docker** & **Docker Compose**
* **Saga Pattern** (Choreography-based for distributed transactions)

## 🛠️ How to Run 

### 1. Prerequisites
* **Java 21** installed.
* **Docker** installed (for MongoDB).
* **Maven** installed (or use the wrapper `mvnw`).
* **CloudAMQP** account (or local RabbitMQ).

### 2. Quick Start (Docker Compose) 🐳
The easiest way to run the full system (Discovery, Order, Inventory, and MongoDB).
*This will start Discovery, Order, Mongo and Inventory services.*
```bash
docker-compose up --build
````
### 3. Manual Run (IntelliJ / Dev Mode)

#### A. Start a local MongoDB instance using Docker:
```bash
docker run -d -p 27017:27017 --name mongo-db mongo:latest
```
or 
```bash
* docker compose up -d mongo-db
```

#### B.  **You can clone the repo :**
```bash
    git clone https://github.com/JoMaPeTe/event-driven-ms.git
```
### Environment Variables(Configuration).
#### Manual Run (IntelliJ): 
IMPORTANT: This project follows the 12-Factor App methodology. Configuration is injected via Environment Variables. 
* You must set these variables in your IDE (IntelliJ Run Configurations)

#### Global Variables (For all services)

| Variable | Description | Example Value |
| :--- | :--- | :--- |
| `RABBITMQ_URI` | **[SECRET]** Connection string for RabbitMQ. | `amqps://user:pass@host/vhost` |
| `EUREKA_URI` | URL where clients register. | `http://localhost:8761/eureka` |

#### Service-Specific Variables

| Service | Variable | Value |
| :--- | :--- | :--- |
| **Order Service** | `SPRING_DATA_MONGODB_DATABASE` | `order-db` |
| **Inventory Service** | `SPRING_DATA_MONGODB_DATABASE` | `inventory-db` |

**C. Execution Order**

Start the applications in this specific order to ensure correct registration:

1. `DiscoveryServerApplication` (Wait for it to start fully)
2. `InventoryServiceApplication`
3. `OrderServiceApplication`

## 🧪 Testing the API

**Scenario:** Trigger a Collision Avoidance Maneuver.


Once everything is up, you can test the Happy Path and the Rollback Mechanism:

POST http://localhost:8082/api/orders

Scenario A: Success
```bash
curl -X POST http://localhost:8082/api/orders \
   -H "Content-Type: application/json" \
   -d '{"satelliteName": "Sentinel-6", "action": "EXECUTE_COLLISION_AVOIDANCE"}'
```
or use postman and send the following in the body
```json
{
"satelliteName": "Falcon-9",
"action": "CALCULATE_ORBIT"
}
```
Result: Order status becomes COMPLETED.

Scenario B: Trigger Rollback (Failure)
```json
{
"satelliteName": "ERROR",
"action": "CALCULATE_ORBIT"
}
```
