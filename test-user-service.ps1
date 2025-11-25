# Test script to run user-service with environment variables
Write-Host "🔧 Testing User Service with Environment Variables..." -ForegroundColor Blue

# Set environment variables
$env:MONGODB_USERNAME = "1ms22cs051"
$env:MONGODB_PASSWORD = "gyVy64EIEDLCvPrS"
$env:MONGODB_CLUSTER = "cluster0.ak4k3cl.mongodb.net"
$env:USER_SERVICE_PORT = "3001"
$env:EUREKA_SERVER_URL = "http://localhost:3000/eureka"

Write-Host "Environment variables set:" -ForegroundColor Green
Write-Host "  MONGODB_USERNAME: $env:MONGODB_USERNAME" -ForegroundColor Cyan
Write-Host "  MONGODB_CLUSTER: $env:MONGODB_CLUSTER" -ForegroundColor Cyan
Write-Host "  USER_SERVICE_PORT: $env:USER_SERVICE_PORT" -ForegroundColor Cyan

Write-Host "`n🚀 Starting User Service..." -ForegroundColor Yellow
Set-Location "c:\Users\Diya\FairFare\user-service"

# Run the service
.\mvnw.cmd spring-boot:run
