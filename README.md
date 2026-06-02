# Microservice Book Store

This is a **Microservice-based Book Store Application** built using **Java 21**, **Spring Boot**, and **Docker**. It demonstrates a modern microservices architecture with independent services, an API Gateway, and a Web Application frontend.

## 🚀 Architecture

The application consists of the following microservices:

*   **`catalog-service`**: Manages book catalog (CRUD operations).
*   **`order-service`**: Handles customer orders.
*   **`cart-service`**: Manages user shopping carts.
*   **`search-service`**: Advanced search functionality for books.
*   **`notification-service`**: Listens to events (e.g., order placed) and sends notifications.
*   **`api-gateway`**: Central entry point for routing requests to backend services.
*   **`web-app`**: A server-side rendered frontend (Spring MVC/Thymeleaf) interacting with the microservices.

### Infrastructure & Tools

*   **RabbitMQ**: Message broker for asynchronous communication between services.
*   **Keycloak**: Identity and Access Management (running on port `9191`).
*   **Redis**: In-memory data store used by Cart Service.
*   **Elasticsearch**: Search and analytics engine used by Search Service.
*   **MailHog**: Email testing tool (captures emails sent by notification service).
*   **Prometheus**: Monitoring and alerting toolkit.
*   **Grafana**: Visualization platform for metrics, logs, and traces.
*   **Loki**: Log aggregation system.
*   **Tempo**: Distributed tracing backend.
*   **Cloudflare Tunnel**: Securely exposes local applications to the internet.
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

### 5. Exposing to the Internet (Cloudflare Tunnel)

This project includes a **Cloudflare Tunnel** container inside `infra.yml` to securely expose the local application to the Internet without opening ports.

1. Obtain a Tunnel Token from the [Cloudflare Zero Trust Dashboard](https://one.dash.cloudflare.com).
2. Add your token to `code/deployment/docker-compose/docker-compose.env`:
   ```env
   TUNNEL_TOKEN=your_token_here
   ```
3. Add a Public Hostname in the Cloudflare Dashboard pointing to `http://web-app:8080`.
4. Start the infrastructure (`task start_infra`).
5. **Keycloak Fix**: To allow OAuth2 login via the public domain, open the Keycloak Admin UI (`http://localhost:9191/admin`), go to **Clients** > **bookstore-webapp**, and add `*` to **Valid redirect URIs** and **Web origins**. *(Note: `web-app` is already pre-configured with `server.forward-headers-strategy=framework` to generate HTTPS redirects).*

## 🌐 Applications

*   **Web App**: Port `8080`
*   **API Gateway**: Port `8989` (Swagger UI: `/swagger-ui.html`)
*   **Catalog Service**: Port `8081`
*   **Order Service**: Port `8082`
*   **Cart Service**: Port `8084`
*   **Search Service**: Port `8085`

## 📊 Monitoring & Testing

*   **Keycloak**: Port `9191` (Admin: `admin`/`admin1234`)
*   **RabbitMQ Management**: Port `15672` (User: `guest`/`guest`)
*   **MailHog**: Port `8025`
*   **Grafana**: Port `3000` (User: `admin`/`admin123`) - *Pre-configured with Spring Boot 3.x dashboard tailored for Docker Compose.*
*   **Prometheus**: Port `9096`
*   **Loki**: Port `3100`
*   **Tempo**: Port `3200`

## 💻 Development

### Running Tests
To execute all unit and integration tests across all microservices, use the `test` task. This command automatically formats the code and then runs Maven's `verify` phase.

**Using Task:**
```bash
task test
```

**Using Maven directly:**
```bash
./mvnw clean verify
```

### Code Formatting
This project uses **Spotless** to enforce code formatting.

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
│   ├── cart-service/         # Shopping cart domain
│   ├── catalog-service/      # Book catalog domains
│   ├── deployment/           # Docker compose and k8s configs
│   ├── notification-service/ # Email/Notification handler
│   ├── order-service/        # Order management domain
│   ├── search-service/       # Book search domain
│   ├── web-app/              # Frontend UI
│   ├── Taskfile.yml          # Task runner definitions
│   └── pom.xml               # Parent POM
├── README.md                 # Project documentation
└── ...
```
