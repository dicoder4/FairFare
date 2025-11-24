# FairFare Quick Start Guide

## 🚀 Getting Started in 3 Steps

### Step 1: Configure Environment
1. Copy the environment template:
   ```
   Copy-Item .env.example .env
   ```

2. Edit `.env` file with your MongoDB Atlas credentials:
   ```
   MONGODB_USERNAME=your_atlas_username
   MONGODB_PASSWORD=your_atlas_password
   MONGODB_CLUSTER=your_cluster_url
   ```

### Step 2: Setup & Compile
```powershell
.\setup.ps1 -Setup
```

### Step 3: Run All Services  
```powershell
.\setup.ps1 -Run
```

## 🔗 Access Points
- **Eureka Dashboard**: http://localhost:3000
- **Gateway (All APIs)**: http://localhost:3081
- **Direct Service Access**:
  - Users: http://localhost:3001/users
  - Bills: http://localhost:3003/bills
  - Splits: http://localhost:3002/splits

## 🛑 Management
- **Stop all services**: `.\setup.ps1 -Stop`
- **Clean build files**: `.\setup.ps1 -Clean`

## ❗ Troubleshooting
- If PowerShell execution policy prevents running scripts:
  ```powershell
  Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
  ```
- If services fail to start, check the .env file configuration
- Services start in order automatically with health checks
