# Microservice Book Store

This is a **Microservice-based Book Store Application** built using **Java 21**, **Spring Boot**, and **Docker**. It demonstrates a modern microservices architecture with independent services, an API Gateway, customer and admin web frontends.

## 🚀 Architecture

The application consists of the following microservices:

*   **`catalog-service`**: Manages book catalog (CRUD operations).
*   **`order-service`**: Handles customer orders.
*   **`cart-service`**: Manages user shopping carts.
*   **`payment-service`**: Handles payment processing (PayPal sandbox integration).
*   **`search-service`**: Advanced full-text search functionality for books (powered by Elasticsearch).
*   **`notification-service`**: Listens to events (e.g., order placed) and sends email notifications.
*   **`api-gateway`**: Central entry point (port `8989`) for routing requests to all backend services.
*   **`web-app`**: Customer-facing server-side rendered frontend (Spring MVC + Thymeleaf).
*   **`admin-web-app`**: Admin panel for managing products, orders, and users (served under `/admin`).

### Infrastructure & Tools

*   **RabbitMQ**: Message broker for asynchronous communication between services.
*   **Keycloak**: Identity and Access Management (running on port `9191`). Handles SSO login/logout for both `web-app` and `admin-web-app`.
*   **Redis**: In-memory data store used by Cart Service.
*   **Elasticsearch**: Search and analytics engine used by Search Service.
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
./mvnw clean package -DskipTests
```

### 3. Run the Infrastructure

Start the supporting services (RabbitMQ, Keycloak, Redis, Elasticsearch, etc.).

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

### 5. Environment Variables

All service configuration is managed via `code/deployment/docker-compose/docker-compose.env`. Key variables:

| Variable | Default | Description |
|---|---|---|
| `API_GATEWAY_URL` | `http://api-gateway:8989` | Internal Docker URL used by services to call the gateway |
| `PUBLIC_URL` | `http://localhost:8989` | **Browser-accessible** URL — used as the post-logout redirect URI sent to Keycloak |
| `OAUTH2_SERVER_URL` | `http://keycloak:9191` | Internal Keycloak URL for server-side calls (token, JWK, userinfo) |
| `PUBLIC_OAUTH2_SERVER_URL` | `http://localhost:9191` | **Browser-accessible** Keycloak URL — used for the OAuth2 login redirect (authorization URI) |
| `TUNNEL_TOKEN` | *(empty)* | Cloudflare Tunnel token for public internet exposure |

> **Note**: `API_GATEWAY_URL` and `PUBLIC_URL` serve different purposes. `API_GATEWAY_URL` is the internal Docker hostname used for service-to-service calls. `PUBLIC_URL` is the address that the browser navigates to after logging out — it must be reachable by the user's browser.

### 6. Exposing to the Internet (Cloudflare Tunnel)

This project includes a **Cloudflare Tunnel** container inside `infra.yml` to securely expose the local application to the Internet without opening ports.

1. Obtain a Tunnel Token from the [Cloudflare Zero Trust Dashboard](https://one.dash.cloudflare.com).
2. Add your token to `code/deployment/docker-compose/docker-compose.env`:
   ```env
   TUNNEL_TOKEN=your_token_here
   ```
3. Set `PUBLIC_URL` to your public domain (e.g., `https://yourdomain.com`):
   ```env
   PUBLIC_URL=https://yourdomain.com
   ```
4. Add a Public Hostname in the Cloudflare Dashboard pointing to `http://api-gateway:8989`.
5. Start the infrastructure (`task start_infra`).
6. **Keycloak Fix**: To allow OAuth2 login via the public domain, open the Keycloak Admin UI (`http://localhost:9191/admin`), go to **Clients** > **bookstore-webapp**, and add your domain to **Valid redirect URIs**, **Valid post-logout redirect URIs**, and **Web origins**.

## 🌐 Applications

All services are accessible through the API Gateway at `http://localhost:8989`.

| Application | Direct Port | Gateway Path | Description |
|---|---|---|---|
| **Web App** (Customer) | `8080` | `http://localhost:8989/` | Customer-facing storefront |
| **Admin Web App** | `8086` | `http://localhost:8989/admin` | Admin panel (product/order management) |
| **API Gateway** | `8989` | — | Central router + Swagger UI at `/swagger-ui.html` |
| Catalog Service | — | `/catalog/**` | Book catalog REST API |
| Order Service | `8082` | `/orders/**` | Order management REST API |
| Cart Service | `8081` | `/cart/**` | Shopping cart REST API |
| Payment Service | `8084` | `/payments/**` | Payment REST API |
| Search Service | `8085` | `/search/**` | Full-text search REST API |

### Default Credentials

| Service | Username | Password |
|---|---|---|
| Web App / Admin (SSO) | `minh` | `minh` |
| Web App / Admin (SSO) | `ply` | *(see Keycloak)* |
| Keycloak Admin UI | `admin` | `admin1234` |

## 📊 Monitoring

| Tool | Port | Credentials |
|---|---|---|
| **Keycloak** | `9191` | `admin` / `admin1234` |
| **RabbitMQ Management** | `15672` | `guest` / `guest` |
| **Grafana** | `3000` | `admin` / `admin123` |
| **Prometheus** | `9096` | — |
| **Loki** | `3100` | — |
| **Tempo** | `3200` | — |

Grafana is pre-configured with a Spring Boot 3.x dashboard tailored for Docker Compose.

## 🔐 Authentication (SSO)

Both `web-app` and `admin-web-app` use **Keycloak OIDC** for Single Sign-On. Logout is handled via Keycloak's end-session endpoint (`/protocol/openid-connect/logout`) with an `id_token_hint` and `post_logout_redirect_uri`, which fully invalidates the browser's Keycloak session and forces a credential prompt on the next login.

The `issuer-uri` is intentionally **not** configured (to avoid internal Docker network resolution issues during startup). Instead, all OAuth2 provider endpoints are explicitly set in `application.properties`.

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
│   ├── admin-web-app/        # Admin panel UI (served under /admin)
│   ├── api-gateway/          # Gatekeeper for backend services
│   ├── cart-service/         # Shopping cart domain
│   ├── catalog-service/      # Book catalog domain
│   ├── deployment/           # Docker Compose configs & Keycloak realm
│   │   └── docker-compose/
│   │       ├── apps.yml      # Application services
│   │       ├── infra.yml     # Infrastructure services
│   │       └── docker-compose.env  # Environment variables
│   ├── notification-service/ # Email/Notification handler
│   ├── order-service/        # Order management domain
│   ├── payment-service/      # Payment processing domain
│   ├── search-service/       # Book search domain (Elasticsearch)
│   ├── web-app/              # Customer frontend UI
│   ├── Taskfile.yml          # Task runner definitions
│   └── pom.xml               # Parent POM
├── README.md                 # Project documentation
└── ...
```
