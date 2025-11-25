# FairFare Service Startup Test Script
Write-Host "🎉 FairFare - Testing All Fixed Services" -ForegroundColor Blue
Write-Host "=========================================" -ForegroundColor Blue

$services = @(
    @{Name="Discovery Service"; Dir="discovery-service"; Port=3000; Path=""},
    @{Name="User Service"; Dir="user-service"; Port=3001; Path="/users"},
    @{Name="Billing Service"; Dir="billing-service"; Port=3003; Path="/bills"},
    @{Name="Splitter Service"; Dir="splitter-service"; Port=3002; Path="/splits"},
    @{Name="Gateway Service"; Dir="gateway-service"; Port=3081; Path="/users"}
)

function Test-ServiceBuild {
    param($ServiceDir)
    Write-Host "🔨 Testing $ServiceDir build..." -ForegroundColor Yellow
    Push-Location $ServiceDir
    $result = & .\mvnw.cmd clean compile 2>&1
    Pop-Location
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  ✅ $ServiceDir builds successfully" -ForegroundColor Green
        return $true
    } else {
        Write-Host "  ❌ $ServiceDir build failed" -ForegroundColor Red
        return $false
    }
}

function Start-ServiceForTest {
    param($ServiceDir, $ServiceName)
    Write-Host "🚀 Starting $ServiceName for test..." -ForegroundColor Yellow
    Push-Location $ServiceDir
    $process = Start-Process -FilePath ".\mvnw.cmd" -ArgumentList "spring-boot:run" -PassThru -WindowStyle Minimized
    Pop-Location
    return $process
}

function Test-ServiceEndpoint {
    param($Port, $Path, $ServiceName)
    Write-Host "🌐 Testing $ServiceName endpoint..." -ForegroundColor Yellow
    
    # Wait for service to start
    Start-Sleep 20
    
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:$Port$Path" -Method GET -TimeoutSec 10
        if ($response.StatusCode -eq 200) {
            Write-Host "  ✅ $ServiceName is responding (Status: $($response.StatusCode))" -ForegroundColor Green
            return $true
        }
    } catch {
        Write-Host "  ❌ $ServiceName is not responding: $_" -ForegroundColor Red
    }
    return $false
}

Write-Host "`n📋 Testing All Service Builds..." -ForegroundColor Cyan
$buildResults = @{}
foreach ($service in $services) {
    $buildResults[$service.Name] = Test-ServiceBuild $service.Dir
}

Write-Host "`n📊 Build Results:" -ForegroundColor Cyan
foreach ($result in $buildResults.GetEnumerator()) {
    $status = if ($result.Value) { "✅ PASS" } else { "❌ FAIL" }
    $color = if ($result.Value) { "Green" } else { "Red" }
    Write-Host "  $($result.Key): $status" -ForegroundColor $color
}

$successfulBuilds = ($buildResults.Values | Where-Object { $_ }).Count
$totalBuilds = $buildResults.Count

Write-Host "`n🎯 Summary:" -ForegroundColor Blue
Write-Host "  Built: $successfulBuilds/$totalBuilds services" -ForegroundColor Cyan
if ($successfulBuilds -eq $totalBuilds) {
    Write-Host "  🎉 ALL SERVICES BUILD SUCCESSFULLY!" -ForegroundColor Green
    Write-Host "`n✨ Your FairFare application is ready to run!" -ForegroundColor Green
    Write-Host "`n🚀 To start all services:" -ForegroundColor Yellow
    Write-Host "   .\setup.ps1 -Run" -ForegroundColor Cyan
} else {
    Write-Host "  ⚠️  Some services failed to build. Check the errors above." -ForegroundColor Red
}

Write-Host "`n🔗 Service Endpoints (when running):" -ForegroundColor Blue
foreach ($service in $services) {
    Write-Host "  $($service.Name): http://localhost:$($service.Port)$($service.Path)" -ForegroundColor Cyan
}
