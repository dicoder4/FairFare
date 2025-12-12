# FairFare 💸
> **Smart Bill Splitting & Expense Management System**  
> *Built with Spring Cloud Microservices & AI-Powered OCR*

## 🚀 Overview
**FairFare** is a scalable, distributed web application designed to simplify group expenses. Unlike basic split apps, FairFare leverages **AI** to scan receipts, **Microservices** for scalability, and a **Modern UI** for a seamless user experience.

## 🛠️ Technology Stack
- **Framework**: Spring Boot 3.2, Spring Cloud 2023
- **Infrastructure**: Netflix Eureka (Discovery), Spring Cloud Gateway
- **Persistence**: MongoDB (NoSQL)
- **Frontend**: Vanilla JS (ES6+), CSS3 Variables, Glassmorphism
- **AI/ML**: Tesseract.js (Optical Character Recognition)
- **Testing**: JUnit 5, Mockito

## 🏗️ Architecture
The system consists of five fully decoupled microservices:

| Service | Port | Description |
| :--- | :--- | :--- |
| **Discovery Service** | `8761` | Service Registry (Eureka Server) |
| **Gateway Service** | `3081` | Central API Gateway & UI Host |
| **User Service** | `3001` | Manages Users & Groups |
| **Billing Service** | `3003` | Handles Bill Creation & Items |
| **Splitter Service** | `3002` | Logic for Shares, Debts & Settlements |

## ✨ Key Features
*   **🤖 AI Receipt Scanner**: Upload a receipt image, and our OCR engine automatically extracts items and prices.
*   **📊 Smart Splitting**: Detects "Equal" vs "Item-Based" splits and visualizes the distribution with a color-coded bar.
*   **💸 Debt Settlement**: Tracks who paid, who owes, and allows users to "Mark as Paid" to settle debts.
*   **🌍 Multi-Currency**: Native support for INR (₹), USD ($), EUR (€), and GBP (£).
*   **🔒 Resilient**: Self-healing architecture with dynamic service discovery.

## � Project Structure
```bash
FairFare/
├── discovery-service/      # Eureka Server
├── gateway-service/        # API Gateway + Static UI
├── user-service/           # User/Group Logic
├── billing-service/        # Bill/Item Logic
├── splitter-service/       # Settlement Logic
├── setup.ps1               # One-Click Run Script
└── run_tests.ps1           # Automated Test Suite
```

## ⚡ Getting Started
**Prerequisites**: Java 17+, Maven, MongoDB (running on port 27017).

### 1. Run the App
We provide a unified PowerShell script to build and launch all services in order:
```powershell
.\setup.ps1 -Run
```

### 2. Access the UI
Once started, open your browser:
👉 **http://localhost:3081**

### 3. Run Tests
To verify system integrity:
```powershell
.\run_tests.ps1
```
