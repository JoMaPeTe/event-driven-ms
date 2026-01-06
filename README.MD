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

1.  **Prerequisites:** Java 21, Maven, Docker (optional).
2.  **You can clone the repo:**
    ```bash
    git clone https://github.com/JoMaPeTe/event-driven-ms.git
    ```
3.  **Or you can run with Docker Compose** (Recommended):
    ```bash
    docker-compose up --build
    ```
    *This will start Discovery, Order, Mongo and Inventory services.*

4.  **Manual Run (IntelliJ):**
    * Start `DiscoveryServerApplication`
    * Start `InventoryServiceApplication`
    * Start `OrderServiceApplication`

## 🧪 Testing the API

**Scenario:** Trigger a Collision Avoidance Maneuver.

```bash
curl -X POST http://localhost:8082/api/orders \
   -H "Content-Type: application/json" \
   -d '{"satelliteName": "Sentinel-6", "action": "EXECUTE_COLLISION_AVOIDANCE"}'