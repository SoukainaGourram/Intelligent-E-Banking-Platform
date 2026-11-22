# 🏦 Intelligent E-Banking Platform

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.1.4-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

**A microservices-based e-banking platform built with Spring Boot & Spring Cloud**

*Author: Soukaina Gourram*

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Microservices](#-microservices)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [API Endpoints](#-api-endpoints)
- [Project Structure](#-project-structure)

---

## 🌟 Overview

The **Intelligent E-Banking Platform** is a cloud-native application built using a microservices architecture. It enables customers to manage their bank accounts through a set of independent, loosely-coupled services orchestrated via Spring Cloud.

Each service is independently deployable, scalable, and communicates through REST APIs — with service discovery handled by **Netflix Eureka** and cross-service calls managed by **OpenFeign** with **Resilience4j** circuit breakers.

---

## 🏗️ Architecture

```
                        ┌─────────────────────────────┐
                        │     API Gateway (8888)       │
                        │   Spring Cloud Gateway       │
                        └──────────┬──────────────┬────┘
                                   │              │
               ┌───────────────────▼──┐  ┌────────▼────────────┐
               │  customer-service    │  │   ebank-service      │
               │     (port 8056)      │  │    (port 8057)       │
               │  - Customer CRUD     │  │  - BankAccount CRUD  │
               │  - H2 Database       │  │  - Feign → customer  │
               │  - Swagger UI        │  │  - Circuit Breaker   │
               └──────────────────────┘  └─────────────────────┘
                        │                          │
               ┌────────▼──────────────────────────▼────┐
               │        discovery-service (8761)         │
               │        Netflix Eureka Server            │
               └─────────────────────────────────────────┘
```

---

## 🔧 Microservices

### 1. 🗺️ `discovery-service` — Eureka Server (port **8761**)
Central service registry. All microservices register here on startup, enabling dynamic discovery.

| Config | Value |
|--------|-------|
| Port | `8761` |
| Eureka Dashboard | `http://localhost:8761` |

---

### 2. 👥 `customer-service` — Customer Management (port **8056**)
Manages bank customers. Exposes a REST API consumed by the `ebank-service` via Feign.

| Config | Value |
|--------|-------|
| Port | `8056` |
| Database | H2 in-memory (`customer-db`) |
| Swagger UI | `http://localhost:8056/swagger-ui.html` |
| H2 Console | `http://localhost:8056/h2-console` |

**Preloaded test data:** `Soukaina`, `Hiba`, `Doha`

---

### 3. 🏦 `ebank-service` — Bank Account Management (port **8057**)
Manages bank accounts. Communicates with `customer-service` to enrich account data with customer info. Includes a **Resilience4j Circuit Breaker** — if `customer-service` is down, a fallback response is returned automatically.

| Config | Value |
|--------|-------|
| Port | `8057` |
| Database | H2 in-memory (`accounts-db`) |
| Swagger UI | `http://localhost:8057/swagger-ui.html` |
| H2 Console | `http://localhost:8057/h2-console` |

**Preloaded test data:** 15 random bank accounts (CURRENT-ACCOUNT / SAVING-ACCOUNT)

---

### 4. 🚪 `gateway-service` — API Gateway (port **8888**)
Single entry point for all client requests. Routes are **dynamically resolved via Eureka** — no hardcoded URLs needed.

| Config | Value |
|--------|-------|
| Port | `8888` |
| Routing | Dynamic via Eureka discovery |

---

## 💻 Tech Stack

| Technology | Usage |
|------------|-------|
| **Java 21** | Programming language |
| **Spring Boot 4.1.2** | Application framework |
| **Spring Cloud 2025.1.4** | Microservices ecosystem |
| **Netflix Eureka** | Service discovery & registration |
| **Spring Cloud Gateway** | API Gateway (WebFlux) |
| **OpenFeign** | Declarative HTTP client between services |
| **Resilience4j** | Circuit breaker / fault tolerance |
| **Spring Data JPA** | Database persistence layer |
| **H2 Database** | In-memory database (dev/test) |
| **Springdoc OpenAPI** | Swagger UI / API documentation |
| **Spring Actuator** | Health checks & metrics |
| **Lombok** | Boilerplate reduction |

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- Git

### Clone the repository

```bash
git clone https://github.com/SoukainaGourram/Intelligent-E-Banking-Platform.git
cd Intelligent-E-Banking-Platform
```

### ⚡ Start order (important!)

> Services **must** be started in this exact order:

**Step 1 — Start Eureka (Discovery Service)**
```bash
cd discovery-service
./mvnw spring-boot:run
```
Wait until `http://localhost:8761` is accessible.

**Step 2 — Start Customer Service**
```bash
cd customer-service
./mvnw spring-boot:run
```

**Step 3 — Start Ebank Service**
```bash
cd ebank-service
./mvnw spring-boot:run
```

**Step 4 — Start Gateway Service**
```bash
cd gateway-service
./mvnw spring-boot:run
```

---

## 📡 API Endpoints

### Via API Gateway (recommended — port 8888)

#### Customer Service
| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/customer-service/customers` | Get all customers |
| `GET` | `/customer-service/customers/{id}` | Get customer by ID |
| `POST` | `/customer-service/customers` | Create a new customer |

#### Ebank Service
| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/ebank-service/accounts` | Get all bank accounts |
| `GET` | `/ebank-service/accounts/{id}` | Get account by ID (with customer info) |
| `POST` | `/ebank-service/accounts` | Create a new bank account |

### Direct access (without gateway)

```
http://localhost:8056/customers        # Customer Service direct
http://localhost:8057/accounts         # Ebank Service direct
```

### Example requests

```bash
# Get all customers
curl http://localhost:8888/customer-service/customers

# Get customer by ID
curl http://localhost:8888/customer-service/customers/1

# Create a customer
curl -X POST http://localhost:8888/customer-service/customers \
  -H "Content-Type: application/json" \
  -d '{"name": "Fatima", "email": "fatima@gmail.com"}'

# Get all bank accounts (with customer details)
curl http://localhost:8888/ebank-service/accounts

# Get account by ID
curl http://localhost:8888/ebank-service/accounts/{uuid}
```

---

## 📁 Project Structure

```
Intelligent-E-Banking-Platform/
│
├── discovery-service/                          # Eureka Server
│   └── src/main/java/net/gourram/discoveryservice/
│       └── DiscoveryServiceApplication.java    # @EnableEurekaServer
│
├── customer-service/                           # Customer microservice
│   └── src/main/java/net/gourram/customerservice/
│       ├── controllers/CustomerRestController.java
│       ├── entities/Customer.java
│       ├── repository/CustomerRepository.java
│       └── service/CustomerService.java
│
├── ebank-service/                              # Bank account microservice
│   └── src/main/java/net/gourram/ebankservice/
│       ├── controllers/EbankRestController.java
│       ├── entities/BankAccount.java
│       ├── feign/CustomerRestClients.java       # Feign + Circuit Breaker
│       ├── model/Customer.java                  # DTO from customer-service
│       ├── repository/BankAccountRepository.java
│       └── service/EbankService.java
│
├── gateway-service/                            # API Gateway
│   └── src/main/java/net/gourram/gatewayservice/
│       └── GatewayServiceApplication.java      # Dynamic routing via Eureka
│
└── src/                                        # Root module
    └── main/java/net/gourram/ebankapp/
        └── EbankAppApplication.java
```

---

## 🔗 Useful URLs

| Service | URL |
|---------|-----|
| Eureka Dashboard | http://localhost:8761 |
| Gateway | http://localhost:8888 |
| Customer Swagger UI | http://localhost:8056/swagger-ui.html |
| Customer H2 Console | http://localhost:8056/h2-console |
| Ebank Swagger UI | http://localhost:8057/swagger-ui.html |
| Ebank H2 Console | http://localhost:8057/h2-console |

---

<div align="center">

 by **Soukaina Gourram**  
[GitHub](https://github.com/SoukainaGourram/Intelligent-E-Banking-Platform)

</div>
