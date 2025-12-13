# Restart Gateway Only
# Usage: Run this to fix 404 Error
Write-Host "Killing process on port 3081..."
$p = Get-NetTCPConnection -LocalPort 3081 -ErrorAction SilentlyContinue
if ($p) { 
    Write-Host "Killing PID $($p.OwningProcess)"
    Stop-Process -Id $p.OwningProcess -Force 
} else {
    Write-Host "Port 3081 is free."
}

Write-Host "Starting Gateway Service..."
cd gateway-service
.\mvnw.cmd spring-boot:run
