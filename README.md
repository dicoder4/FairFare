# FairFare 💰

A microservices-based bill splitting application that allows groups of users to fairly split expenses from restaurant bills and other shared costs. Built with Spring Boot, MongoDB, and Netflix Eureka for service discovery.

## 🏗️ Architecture Overview

FairFare uses a distributed microservices architecture with the following components:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Service  │    │ Billing Service │    │Splitter Service │
│    Port: 3001   │    │    Port: 3081   │    │    Port: 3002   │
│                 │    │                 │    │                 │
│ • User Management│    │ • Bill Creation │    │ • Cost Calculation│
│ • Group Management│   │ • Item Management│   │ • Fair Splitting │
│ • Authentication │    │ • Bill Storage   │   │ • Split Results  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │Discovery Service│
                    │    Port: 3000   │
                    │                 │
                    │ • Eureka Server │
                    │ • Service Registry│
                    │ • Load Balancing │
                    └─────────────────┘
                                 │
                    ┌─────────────────┐
                    │ Gateway Service │
                    │    Port: 3081   │
                    │                 │
                    │ • API Gateway   │
                    │ • Request Routing│
                    │ • Load Balancing │
                    └─────────────────┘
```

## 🚀 Features

### 👥 User & Group Management
- Create and manage user accounts
- Form groups for shared expenses
- Add/remove members from groups
- User authentication and authorization

### 🧾 Bill Management
- Create detailed bills with multiple items
- Add items with quantities, unit prices, and total costs
- Apply taxes, service charges, and tips
- Assign specific items to specific group members
- Real-time bill calculations

### ⚖️ Smart Bill Splitting
- Automatic fair splitting based on item assignments
- Proportional distribution of taxes, tips, and service charges
- Individual user share calculations
- Persistent split results for future reference
- Recalculate splits when bills are updated

### 🔄 Microservices Benefits
- Independent service scaling
- Fault isolation and resilience
- Technology diversity
- Easy maintenance and updates

## 🛠️ Technology Stack

- **Backend Framework**: Spring Boot 2.5.3
- **Database**: MongoDB Atlas (Cloud)
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Communication**: REST APIs with RestTemplate
- **Build Tool**: Maven
- **Java Version**: 8+

## 📋 Prerequisites

- Java 8 or higher
- Maven 3.6+
- MongoDB Atlas account (or local MongoDB)
- Internet connection for Maven dependencies

## 🏃‍♂️ Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd FairFare
```

### 2. Setup Environment Variables
```powershell
# Copy the example environment file
Copy-Item .env.example .env

# Edit .env file with your MongoDB credentials
notepad .env
```

**Required Environment Variables:**
- `MONGODB_USERNAME` - Your MongoDB Atlas username
- `MONGODB_PASSWORD` - Your MongoDB Atlas password  
- `MONGODB_CLUSTER` - Your MongoDB Atlas cluster URL

### 3. Automated Setup (Recommended)

#### Option A: Using PowerShell Script
```powershell
# Setup environment and compile all services
.\setup.ps1 -Setup

# Run all services in correct order
.\setup.ps1 -Run

# Stop all services
.\setup.ps1 -Stop

# Clean all services
.\setup.ps1 -Clean
```

#### Option B: Manual Setup

##### Step 1: Setup Environment
```powershell
# Ensure .env file is configured with your MongoDB credentials
```

##### Step 2: Start Services in Order (Each in separate terminal)

**Terminal 1 - Discovery Service:**
```powershell
cd discovery-service
.\mvnw spring-boot:run
```
*Wait for service to start completely (check http://localhost:3000)*

**Terminal 2 - User Service:**
```powershell
cd user-service  
.\mvnw spring-boot:run
```

**Terminal 3 - Billing Service:**
```powershell
cd billing-service
.\mvnw spring-boot:run  
```

**Terminal 4 - Splitter Service:**
```powershell
cd splitter-service
.\mvnw spring-boot:run
```

**Terminal 5 - Gateway Service:**
```powershell
cd gateway-service
.\mvnw spring-boot:run
```

### 4. Access the Application

- **Eureka Dashboard**: http://localhost:3000
- **User Service Direct**: http://localhost:3001
- **Billing Service Direct**: http://localhost:3003/bills  
- **Splitter Service Direct**: http://localhost:3002/splits
- **Gateway (All Services)**: http://localhost:3081

### 5. Service Health Checks

Once all services are running, verify they're registered with Eureka:
1. Visit http://localhost:3000
2. You should see all 4 services listed:
   - USER-SERVICE
   - BILLING-SERVICE  
   - SPLITTER-SERVICE
   - GATEWAY-SERVICE

## 📖 API Documentation

### User Service Endpoints

#### Users
- `GET /users` - Get all users
- `POST /users` - Create a new user
- `GET /users/{id}` - Get user by ID

#### Groups  
- `GET /groups` - Get all groups
- `POST /groups` - Create a new group
- `GET /groups/{id}` - Get group by ID
- `POST /groups/{id}/members` - Add member to group

### Billing Service Endpoints

#### Bills
- `POST /bills` - Create a new bill
- `GET /bills/{billId}` - Get bill by ID
- `POST /bills/{billId}/items` - Add items to a bill

### Splitter Service Endpoints

#### Splits
- `GET /splits/{billId}` - Get split calculation for a bill
- `POST /splits/{billId}/recalculate` - Force recalculation of split

### Gateway Routes

All services are accessible through the gateway at `http://localhost:3081`:
- `/users/**` → User Service (port 3001)
- `/groups/**` → User Service (port 3001) 
- `/bills/**` → Billing Service (port 3003)
- `/splits/**` → Splitter Service (port 3002)

## 💡 Usage Example

### 1. Create Users
```bash
# Create users
curl -X POST http://localhost:3001/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com"}'

curl -X POST http://localhost:3001/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Bob","email":"bob@example.com"}'
```

### 2. Create a Group
```bash
curl -X POST http://localhost:3001/groups \
  -H "Content-Type: application/json" \
  -d '{"name":"Dinner Group","description":"Friday dinner friends","memberIds":["user_id_1","user_id_2"]}'
```

### 3. Create a Bill
```bash
curl -X POST http://localhost:3003/bills \
  -H "Content-Type: application/json" \
  -d '{
    "groupId": "group_id_here",
    "createdByUserId": "user_id_1",
    "tax": 8.50,
    "serviceCharge": 5.00,
    "tip": 12.00,
    "items": [
      {
        "name": "Pizza",
        "quantity": 2,
        "unitPrice": 15.99,
        "assignedUserIds": ["user_id_1", "user_id_2"]
      },
      {
        "name": "Coke",
        "quantity": 1,
        "unitPrice": 2.99,
        "assignedUserIds": ["user_id_1"]
      }
    ]
  }'
```

### 4. Add Items to Existing Bill
```bash
curl -X POST http://localhost:3003/bills/{billId}/items \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "name": "Dessert",
        "quantity": 1,
        "unitPrice": 6.99,
        "assignedUserIds": ["user_id_1", "user_id_2"]
      }
    ]
  }'
```

### 5. Get Split Calculation
```bash
curl http://localhost:3002/splits/{billId}
```

## 🗄️ Database Schema

### Users Collection
```javascript
{
  "_id": "ObjectId",
  "name": "String",
  "email": "String"
}
```

### Groups Collection
```javascript
{
  "_id": "ObjectId", 
  "name": "String",
  "description": "String",
  "memberIds": ["user_id_1", "user_id_2"]
}
```

### Bills Collection
```javascript
{
  "_id": "ObjectId",
  "groupId": "String",
  "createdByUserId": "String",
  "subtotal": "Double",
  "tax": "Double", 
  "serviceCharge": "Double",
  "tip": "Double",
  "total": "Double",
  "items": [
    {
      "name": "String",
      "quantity": "Integer",
      "unitPrice": "Double", 
      "totalPrice": "Double",
      "assignedUserIds": ["user_id_1"]
    }
  ]
}
```

### Splits Collection
```javascript
{
  "_id": "ObjectId",
  "billId": "String",
  "groupId": "String", 
  "userShares": [
    {
      "userId": "String",
      "itemsSubtotal": "Double",
      "taxShare": "Double",
      "serviceChargeShare": "Double", 
      "tipShare": "Double",
      "totalOwed": "Double"
    }
  ]
}
```

## 🔒 Security & Environment Variables

### Environment Configuration
All sensitive configuration is stored in environment variables:

- **MongoDB credentials** are externalized to `.env` file
- **Service ports** can be configured via environment variables  
- **Eureka server URL** is configurable

### Files to Keep Secure
- `.env` - Contains actual MongoDB credentials (never commit this)
- `.env.example` - Template file (safe to commit)

### Default Environment Variables
```bash
MONGODB_USERNAME=your_username
MONGODB_PASSWORD=your_password  
MONGODB_CLUSTER=your_cluster_url
DISCOVERY_SERVICE_PORT=3000
USER_SERVICE_PORT=3001
SPLITTER_SERVICE_PORT=3002
BILLING_SERVICE_PORT=3003
GATEWAY_SERVICE_PORT=3081
EUREKA_SERVER_URL=http://localhost:3000/eureka
```

## 🔧 Configuration

### Service Ports
- Discovery Service: 3000
- User Service: 3001  
- Splitter Service: 3002
- Billing Service: 3003
- Gateway Service: 3081

### MongoDB Configuration
Each service connects to its own database:
- `user-service-db`
- `billing-service-db` 
- `splitter-service-db`

## 🧪 Development & Testing

### Build All Services
```powershell
# From root directory
Get-ChildItem -Directory | ForEach-Object { 
  Set-Location $_.FullName
  if (Test-Path "pom.xml") { 
    .\mvnw clean compile 
  }
  Set-Location ..
}
```

### Run Tests
```powershell
# In each service directory
.\mvnw test
```

## 🚀 Deployment

The application is containerizable and can be deployed using:
- Docker containers
- Kubernetes clusters  
- Cloud platforms (AWS, Azure, GCP)
- Traditional application servers

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Diya D Shah** - [GitHub Profile](https://github.com/diyashah)

---

## 🔗 Related Documentation

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Cloud Gateway](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
- [Netflix Eureka](https://github.com/Netflix/eureka)
- [MongoDB Documentation](https://docs.mongodb.com/)

---

*FairFare - Making bill splitting fair and simple! 💰✨*