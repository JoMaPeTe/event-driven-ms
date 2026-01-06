servicio de Pedidos (Puerto 8082): Actuará como el "cliente".
Cuando alguien crea un pedido, este servicio llamará al de Inventario para ver si el producto existe
```markdown
# 🚀 Order Service

Proof of concept.
This service is the entry point of the system. You can a post call to the endpoint from postman.
The body should be something like this:
    {"satelliteName": "Falcon-10",
    "action": "PERFORM_DELTA_V_BURN, Pitch 14, Roll 5."
    }
It handles HTTP requests from operators and orchestrates the process.

## Key Features

* **REST Controller:** Exposes the `/api/orders` endpoint.
* **Saga Orchestrator:** Initiates the transaction by publishing events to RabbitMQ.
* **Compensation Logic:** Listens for failure events to trigger rollbacks (e.g., cancelling a maneuver if fuel is low).
* **Idempotency Key:** Generates unique UUIDs for every request to ensure traceability.

## Configuration
Runs on port **8082**. Connects to RabbitMQ and Eureka.
## 🔐 Configuration (Environment Variables)

Secrets and configuration are decoupled from the code.

To run the application, you must set the following environment variables.

### Required Variables

| Variable          | Description                               | Example |
| `RABBITMQ_URI`    | Connection string for the Message Broker  | `amqps://user:pass@host.cloudamqp.com/vhost` |
| `EUREKA_URI`      | Location of the Service Registry          | `http://localhost:8761/eureka` (Default) |

### How to set them

**Option A: Using Docker (Recommended)**
Create a `.env` file in the root directory:
```properties
RABBITMQ_URI=amqps://tu_usuario:tu_password@...
```
**Option B: Using IntelliJ IDEA**
Go to Run/Debug Configurations -> Environment variables and add them there.