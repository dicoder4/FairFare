# FairFare 💸

> A robust, microservices-based bill splitting platform built with Spring Cloud.

## 📖 Overview
FairFare is a distributed application designed to simplify shared expense management. Built on a scalable **Spring Boot** microservices architecture, it handles everything from user management to complex debt calculation and bill splitting.

Similar to enterprise-grade systems, FairFare decouples its logic into specialized services to ensure scalability and maintainability.

## 🚀 Key Features
*   **Microservices Architecture**: Fully decoupled services using **Netflix Eureka** for discovery and **Spring Cloud Gateway** for routing.
*   **Smart Splitting Engine**: Supports itemized splitting calculations to determine exactly who owes what based on consumption.
*   **Debt Tracking**: automatically calculates net balances ("Who owes who") and tracks settlement status.
*   **Visual Analytics**: Dynamic visualization of cost distribution among group members.
*   **Experimental OCR**: Basic support for scanning receipt images to auto-populate bill items *(currently optimized for specific receipt formats)*.
*   **Developer Experience**: Automated PowerShell scripts for one-click startup and testing.

## 🛠️ Technology Stack
*   **Core**: Java 17, Spring Boot 3.x
*   **Cloud Native**: Spring Cloud Netflix Eureka, Spring Cloud Gateway
*   **Database**: MongoDB
*   **Frontend**: Vanilla JavaScript (ES6+), CSS3 (Glassmorphism design)
*   **Testing**: JUnit 5, Mockito

## 📂 Project Structure
```bash
FairFare/
├── discovery-service/     # Service Registry (Eureka)
├── gateway-service/       # API Gateway & UI Static Resources
├── user-service/          # User & Group Management
├── billing-service/       # Bill Persistence & Management
├── splitter-service/      # Splitting Logic & Debt Calculation
└── setup.ps1              # ⚡ Automation Script
```

## 🏁 Getting Started

### Prerequisites
*   Java 17+
*   MongoDB (running locally on port 27017 or configured via `.env`)

### ⚡ Quick Start (The Easy Way)
We provide a powerful automation script to handle the entire lifecycle of the application.

1.  **Start All Services**:
    ```powershell
    .\setup.ps1 -Run
    ```
    This will:
    *   Load environment variables from `.env`.
    *   Launch Eureka, User, Billing, Splitter, and Gateway services in the correct order.
    *   Wait for port readiness.

2.  **Access the App**:
    Open [http://localhost:3081](http://localhost:3081) in your browser.

3.  **Stop Services**:
    ```powershell
    .\setup.ps1 -Stop
    ```

### 🧪 Running Tests
To run the full suite of unit and integration tests across all microservices:
```powershell
.\run_tests.ps1
```

