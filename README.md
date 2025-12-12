# FairFare

**FairFare** is a scalable, decoupled microservices architecture for managing shared expenses, bill splitting, and debt visualization. The system uses Spring Boot microservices with MongoDB for data persistence, Eureka for service discovery, and an API Gateway for centralized request routing.

## Architecture

The system consists of five main microservices:

1.  **Eureka Server** (Port 8761) - Service Registry & Discovery.
2.  **API Gateway** (Port 3081) - Centralized Entry Point & Request Routing.
3.  **User Service** (Port 3001) - Manages User Profiles and Groups.
4.  **Billing Service** (Port 3003) - Handles Bill Creation and Persistence.
5.  **Splitter Service** (Port 3002) - Calculates Debt Shares, "Paid By" Logic, and Split Distributions.

## Technology Stack

-   **Spring Boot 3.3.x** - Application framework
-   **Spring Cloud 2023.x** - Microservices framework
-   **Netflix Eureka** - Service discovery
-   **Spring Cloud Gateway** - API Gateway
-   **Spring Data MongoDB** - Data persistence
-   **OpenFeign** - Inter-service communication
-   **Maven** - Build tool
-   **Vanilla JS / CSS** - Responsive Frontend & Visualization

## Project Structure

```bash
FairFare/
 ├── eureka-server/    # Service Registry
 ├── gateway-service/  # API Gateway & Static UI
 ├── user-service/     # User & Group Management
 ├── billing-service/  # Bill Management
 ├── splitter-service/ # Split Logic & Debt Calculation
 └── setup.ps1         # Orchestration Script
```

## Key Features

-   **🎯 Microservices Architecture**: Independently deployable services for separate concerns.
-   **🔄 Service Discovery**: Dynamic registration of services via Eureka.
-   **🚪 Centralized API Gateway**: Single entry point for all frontend requests.
-   **💾 Independent Data Stores**: Each service manages its own MongoDB collection context.
-   **📊 Smart Split Logic**: Algorithms to handle "Item-Based" calculation versus simple equal splitting.
-   **🎨 Visual Data Representation**: Frontend visualizers for debt distribution and payment status.
-   **🧾 Receipt Parsing Utility**: Basic text extraction for standard receipt formats to accelerate data entry.

## Getting Started

### Prerequisites

-   **Java 17+**
-   **Maven**
-   **MongoDB** (Local or Atlas)

### Installation & Running

1.  **Configure Environment**:
    Ensure your `.env` file lists your MongoDB credentials and port configurations.

2.  **Start Services**:
    Use the provided PowerShell script to build and launch all services in the correct order.
    ```powershell
    .\setup.ps1 -Run
    ```

3.  **Access the Application**:
    Navigate to `http://localhost:3081`

## API Endpoints

| Service | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **User** | GET | `/users` | List all users |
| **User** | POST | `/groups` | Create a new group |
| **Billing** | POST | `/bills` | Create a new bill with item details |
| **Splitter** | GET | `/splits/{billId}` | Get calculated split result |
| **Splitter** | POST | `/splits/{billId}/settle/{userId}` | Mark a user's share as paid |

## License

This project is licensed under the MIT License - see the LICENSE file for details.
