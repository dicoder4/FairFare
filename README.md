# FairFare �
> A Microservices-based Bill Splitting Application.

**FairFare** is a distributed system designed to simplify group expense management. Built using **Spring Boot**, **Spring Cloud**, and **MongoDB**, it demonstrates a modern microservices architecture with service discovery, client-side load balancing, and an API Gateway.

## 🚀 Key Features
- **Microservices Architecture**: Completely decoupled services for Users, Billing, and Splitting logic.
- **Service Discovery**: Uses **Netflix Eureka** for dynamic service registration.
- **API Gateway**: Unified entry point (Port 3081) routing traffic to backend clusters.
- **Smart Splitting**: Calculates individual shares based on item consumption, including proportional tax and tip distribution.
- **✨ AI Receipt Scanner**: Automatically extracts items and prices from receipt images using **Tesseract.js** OCR.
- **📊 Visual Split Analysis**: Color-coded distribution bar showing percentage breakdown of costs per user.
- **💱 Multi-Currency Support**: Supports INR (₹), USD ($), EUR (€), and GBP (£) with instant UI updates.
- **Debt Settlement**: Tracks who paid and who owes, with "Mark as Paid" functionality.
- **Modern UI**: Clean, responsive frontend built with Vanilla JS, CSS variables, and Glassmorphism effects.

## 🛠️ Tech Stack
- **Frontend**: Vanilla HTML5, CSS3 (Glassmorphism), JavaScript (ES6+), Tesseract.js
- **Backend**: Java 17, Spring Boot 3.x
- **Microservices**: Spring Cloud Gateway, Netflix Eureka
- **Database**: MongoDB 5.0+
- **DevOps**: PowerShell Automation, Docker (Ready)

## ⚙️ How to Run
**Prerequisites:** Java 11+, Maven, MongoDB (running on default port 27017).

### 1. Automated Setup
We have included an automated script to build and launch the entire stack:
```powershell
.\setup.ps1 -Run
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

## 📐 Project Structure
```
FairFare/
├── discovery-service/       # Netflix Eureka Server (Service Registry)
├── gateway-service/         # Spring Cloud Gateway (Entry Point & UI)
│   └── src/main/resources   # Frontend Assets (HTML/JS/CSS)
├── user-service/           # User & Group Management
├── billing-service/        # Bill Creation & Persistence
├── splitter-service/       # Split Logic & Debt Calculation
├── setup.ps1               # Orchestration Script
└── run_tests.ps1           # Automated Test Suite
```
