# FairFare �
> A Microservices-based Bill Splitting Application.

**FairFare** is a distributed system designed to simplify group expense management. Built using **Spring Boot**, **Spring Cloud**, and **MongoDB**, it demonstrates a modern microservices architecture with service discovery, client-side load balancing, and an API Gateway.

## 🚀 Key Features
- **Microservices Architecture**: Completely decoupled services for Users, Billing, and Splitting logic.
- **Service Discovery**: Uses **Netflix Eureka** for dynamic service registration.
- **API Gateway**: Unified entry point (Port 3081) routing traffic to backend clusters.
- **Smart Splitting**: Calculates individual shares based on item consumption, including proportional tax and tip distribution.
- **Debt Settlement**: Tracks who paid and who owes, with "Mark as Paid" functionality.
- **Modern UI**: Clean, responsive frontend built with Vanilla JS and CSS variables.

## 🛠️ Tech Stack
- **Languages**: Java, JavaScript, PowerShell
- **Frameworks**: Spring Boot, Spring Cloud (Gateway, Eureka)
- **Database**: MongoDB (NoSQL)
- **Testing**: JUnit 5, Mockito

## ⚙️ How to Run
**Prerequisites:** Java 11+, Maven, MongoDB (running on default port 27017).

### 1. Automated Setup
We have included an automated script to build and launch the entire stack:
```powershell
.\setup.ps1
```
*This verifies prerequisites, compiles all services, and launches them in the correct order.*

### 2. Access the Application
Once the script completes, the Gateway is active at:
👉 **[http://localhost:3081](http://localhost:3081)**

## 🧪 Testing
To run the full suite of Unit and Mock tests:
```powershell
.\run_tests.ps1
```

## 📐 Architecture Overview
1.  **Discovery Service**: (Port 8761) Service Registry.
2.  **Gateway Service**: (Port 3081) Entry point & Load Balancer.
3.  **User Service**: (Port 3001) Manages Users & Groups.
4.  **Billing Service**: (Port 3000) Manages Bills & Items.
5.  **Splitter Service**: (Port 3002) Calculates Debts & Settlements.
