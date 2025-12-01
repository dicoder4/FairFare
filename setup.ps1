# FairFare Setup and Run Script
# This script sets up the environment and runs all services in the correct order

param(
    [switch]$Setup,
    [Alias("start")]
    [switch]$Run,
    [switch]$Stop,
    [switch]$Clean
)

$services = @(
    @{Name="discovery-service"; Port=3000; Order=1},
    @{Name="user-service"; Port=3001; Order=2},
    @{Name="billing-service"; Port=3003; Order=3},
    @{Name="splitter-service"; Port=3002; Order=4},
    @{Name="gateway-service"; Port=3081; Order=5}
)

function Test-EnvFile {
    if (-not (Test-Path ".env")) {
        Write-Host "[ERROR] .env file not found!" -ForegroundColor Red
        Write-Host "Please copy .env.example to .env and configure your MongoDB credentials" -ForegroundColor Yellow
        Write-Host "cp .env.example .env" -ForegroundColor Green
        exit 1
    }
    Write-Host "[SUCCESS] .env file found" -ForegroundColor Green
}

function Load-EnvFile {
    if (Test-Path ".env") {
        Write-Host "[Loading] environment variables from .env file..." -ForegroundColor Yellow
        Get-Content ".env" | ForEach-Object {
            if ($_ -match '^([^=#]+)=(.*)$' -and -not $_.StartsWith('#')) {
                $name = $Matches[1].Trim()
                $value = $Matches[2].Trim()
                [Environment]::SetEnvironmentVariable($name, $value, "Process")
                Write-Host "  [Set] $name" -ForegroundColor Green
            }
        }
        Write-Host "[SUCCESS] Environment variables loaded" -ForegroundColor Green
    } else {
        Write-Host "[ERROR] .env file not found! Please create it from .env.example" -ForegroundColor Red
        exit 1
    }
}

function Test-Port {
    param($Port)
    try {
        $connection = New-Object System.Net.Sockets.TcpClient
        $connection.Connect("localhost", $Port)
        $connection.Close()
        return $true
    } catch {
        return $false
    }
}

function Wait-ForService {
    param($Name, $Port, $Timeout = 60)
    Write-Host "[Waiting] for $Name on port $Port..." -ForegroundColor Yellow
    $elapsed = 0
    while ($elapsed -lt $Timeout) {
        if (Test-Port $Port) {
            Write-Host "[SUCCESS] $Name is ready!" -ForegroundColor Green
            return $true
        }
        Start-Sleep 2
        $elapsed += 2
    }
    Write-Host "[ERROR] $Name failed to start within $Timeout seconds" -ForegroundColor Red
    return $false
}

function Start-Service {
    param($ServiceName)
    Write-Host "[Starting] $ServiceName..." -ForegroundColor Blue
    
    if (-not (Test-Path $ServiceName)) {
        Write-Host "[ERROR] Service directory $ServiceName not found!" -ForegroundColor Red
        return
    }
    
    Push-Location $ServiceName
    try {
        # Use the batch file which sets environment variables
        if (Test-Path "start.bat") {
            Write-Host "   Using start.bat (with environment variables)" -ForegroundColor Cyan
            Start-Process -FilePath "cmd" -ArgumentList "/c", "start.bat" -WindowStyle Normal
        } else {
            Write-Host "   Using mvnw directly" -ForegroundColor Cyan
            Start-Process -FilePath "cmd" -ArgumentList "/c", ".\mvnw.cmd spring-boot:run" -WindowStyle Normal
        }
        Write-Host "[SUCCESS] $ServiceName started in new window" -ForegroundColor Green
    } catch {
        Write-Host "[ERROR] Failed to start $ServiceName`: $_" -ForegroundColor Red
    } finally {
        Pop-Location
    }
}

function Stop-AllServices {
    Write-Host "[Stopping] all services..." -ForegroundColor Red
    Get-Process | Where-Object {$_.ProcessName -like "*java*" -and $_.CommandLine -like "*spring-boot*"} | Stop-Process -Force -ErrorAction SilentlyContinue
    Write-Host "[SUCCESS] All services stopped" -ForegroundColor Green
}

function Clean-Services {
    Write-Host "[Cleaning] all services..." -ForegroundColor Yellow
    $services | ForEach-Object {
        if (Test-Path $_.Name) {
            Push-Location $_.Name
            & .\mvnw.cmd clean
            Pop-Location
        }
    }
    Write-Host "[SUCCESS] All services cleaned" -ForegroundColor Green
}

function Setup-Environment {
    Write-Host "[Setup] Setting up FairFare environment..." -ForegroundColor Blue
    
    # Check if .env exists, if not copy from example
    if (-not (Test-Path ".env")) {
        if (Test-Path ".env.example") {
            Copy-Item ".env.example" ".env"
            Write-Host "[Created] .env from .env.example" -ForegroundColor Green
            Write-Host "[Warning] Please edit .env file with your MongoDB credentials before running services" -ForegroundColor Yellow
        } else {
            Write-Host "[ERROR] Neither .env nor .env.example found!" -ForegroundColor Red
            return
        }
    }
    
    # Compile all services
    Write-Host "[Compiling] all services..." -ForegroundColor Blue
    $services | ForEach-Object {
        Write-Host "Compiling $($_.Name)..." -ForegroundColor Yellow
        Push-Location $_.Name
        & .\mvnw.cmd clean compile
        if ($LASTEXITCODE -eq 0) {
            Write-Host "[SUCCESS] $($_.Name) compiled successfully" -ForegroundColor Green
        } else {
            Write-Host "[ERROR] $($_.Name) compilation failed" -ForegroundColor Red
        }
        Pop-Location
    }
}

function Run-Services {
    Test-EnvFile
    Load-EnvFile
    
    Write-Host "[Starting] FairFare services..." -ForegroundColor Blue
    
    # Sort services numerically by the 'Order' property.
    $sortedServices = $services | Sort-Object { [int]$_.Order }
    
    # Verification step: Print the planned execution order
    Write-Host "[INFO] Services will be started in the following order:" -ForegroundColor Cyan
    $sortedServices | ForEach-Object { Write-Host "  -> $($_.Name) (Order: $($_.Order))" -ForegroundColor Cyan }

    foreach ($service in $sortedServices) {
        Start-Service $service.Name
        
        # Wait for the service to be ready before starting the next one
        if ($service.Order -lt ($services.Count)) { # Don't wait for the last service
            if (Wait-ForService $service.Name $service.Port) {
                Start-Sleep 3 # Additional buffer time
            } else {
                Write-Host "[FATAL] Service $($service.Name) failed to start. Aborting." -ForegroundColor Red
                Stop-AllServices
                return
            }
        }
    }

    Write-Host ""
    Write-Host "*** All services started! Access points:" -ForegroundColor Green
    Write-Host "   [Eureka] Dashboard: http://localhost:3000" -ForegroundColor Cyan
    Write-Host "   [User] Service: http://localhost:3001/users" -ForegroundColor Cyan
    Write-Host "   [Billing] Service: http://localhost:3003/bills" -ForegroundColor Cyan
    Write-Host "   [Splitter] Service: http://localhost:3002/splits" -ForegroundColor Cyan
    Write-Host "   [Gateway] All services: http://localhost:3081" -ForegroundColor Cyan
}

# Main script logic
if ($Setup) {
    Setup-Environment
} elseif ($Run) {
    Run-Services
} elseif ($Stop) {
    Stop-AllServices
} elseif ($Clean) {
    Clean-Services
} else {
    Write-Host @"
FairFare Management Script
=========================

Usage:
  .\setup.ps1 [command]

Available Commands:
  -Setup        Sets up the environment, creating the .env file and compiling all services.
  -Run, -start  Loads environment variables and runs all microservices in the correct order.
  -Stop         Stops all running Java processes related to the services.
  -Clean        Cleans the Maven build artifacts for all services.

Examples:
  .\setup.ps1 -Setup
  .\setup.ps1 -Run
"@
}
