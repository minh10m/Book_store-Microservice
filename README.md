# Microservice Book Store

This is a **Microservice-based Book Store Application** built using **Java 21**, **Spring Boot**, and **Docker**. It demonstrates a modern microservices architecture with independent services, an API Gateway, and a Web Application frontend.

## 🚀 Architecture

The application consists of the following microservices:

*   **`catalog-service`**: Manages book catalog (CRUD operations).
*   **`order-service`**: Handles customer orders.
*   **`notification-service`**: Listens to events (e.g., order placed) and sends notifications.
*   **`api-gateway`**: Central entry point for routing requests to backend services.
*   **`web-app`**: A server-side rendered frontend (Spring MVC/Thymeleaf) interacting with the microservices.

### Infrastructure & Tools

*   **RabbitMQ**: Message broker for asynchronous communication between services.
*   **Keycloak**: Identity and Access Management (running on port `9191`).
*   **MailHog**: Email testing tool (captures emails sent by notification service).
*   **Prometheus**: Monitoring and alerting toolkit.
*   **Maven**: Build automation tool.
*   **Task**: Simple build tool/task runner.

## 🛠 Prerequisites

*   **Java 21**
*   **Docker** & **Docker Compose**
*   **Maven** (Optional, if using the included `mvnw` wrapper)
*   **Task** (Optional, but recommended for running commands readily)

## 🏁 Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd "microservice book store"
```

### 2. Build the Application

You can use the provided `Taskfile` to simplify commands.

**Using Task:**
```bash
task build
```

**Using Maven directly:**
```bash
./mvnw clean compile jib:dockerBuild
```

### 3. Run the Infrastructure

Start the supporting services (RabbitMQ, Keycloak, MailHog, etc.).

**Using Task:**
```bash
task start_infra
```

**Using Docker Compose:**
```bash
docker compose -f code/deployment/docker-compose/infra.yml up -d
```

### 4. Run the Microservices

Once the infrastructure is up, you can start the application services.

**Using Task:**
```bash
task start
```

**Using Docker Compose:**
```bash
docker compose -f code/deployment/docker-compose/infra.yml -f code/deployment/docker-compose/apps.yml up -d
```

## 🌐 Applications

*   **Web App**: Port `8080`
*   **API Gateway**: Port `8989` (Swagger UI: `/swagger-ui.html`)
*   **Catalog Service**: Port `8081`
*   **Order Service**: Port `8082`

## 📊 Monitoring & Testing

*   **Keycloak**: Port `9191` (Admin: `admin`/`admin1234`)
*   **RabbitMQ Management**: Port `15672` (User: `guest`/`guest`)
*   **MailHog**: Port `8025`
*   **Prometheus**: Port `9090`

## 💻 Development

### Code Formatting
This project uses **Spotless** to enforce code formatting.

**Check format:**
```bash
task test
```

**Apply format:**
```bash
task format
```
(or `./mvnw spotless:apply`)

## 📂 Project Structure

```text
microservice-book-store/
├── code/
│   ├── api-gateway/          # Gatekeeper for backend services
│   ├── catalog-service/      # Book catalog domains
│   ├── deployment/           # Docker compose and k8s configs
│   ├── notification-service/ # Email/Notification handler
│   ├── order-service/        # Order management domain
│   ├── web-app/              # Frontend UI
│   ├── Taskfile.yml          # Task runner definitions
│   └── pom.xml               # Parent POM
├── README.md                 # Project documentation
└── ...
```
