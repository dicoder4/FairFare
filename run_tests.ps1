Write-Host "============================================" -ForegroundColor Cyan
Write-Host "      FairFare Automated Test Runner        " -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan

$services = @("user-service", "billing-service", "splitter-service")
$globalSuccess = $true

foreach ($service in $services) {
    Write-Host "`n--------------------------------------------" -ForegroundColor Yellow
    Write-Host "Testing $service..." -ForegroundColor Yellow
    Write-Host "--------------------------------------------" -ForegroundColor Yellow

    Push-Location $service
    
    # Run Maven Test
    # -B: Batch mode (no colors/progress bars to clutter logs)
    # -q: Quiet (only errors/warnings) - actually let's keep it visible but maybe less verbose?
    # Let's just run it standard so they can see success.
    
    try {
        $testProcess = Start-Process -FilePath ".\mvnw.cmd" -ArgumentList "clean", "test" -NoNewWindow -PassThru -Wait
        
        if ($testProcess.ExitCode -eq 0) {
            Write-Host "✅ $service TESTS PASSED" -ForegroundColor Green
        } else {
            Write-Host "❌ $service TESTS FAILED" -ForegroundColor Red
            $globalSuccess = $false
        }
    } catch {
        Write-Host "❌ $service FAILED TO EXECUTE" -ForegroundColor Red
        Write-Error $_
        $globalSuccess = $false
    }
    
    Pop-Location
}

Write-Host "`n============================================" -ForegroundColor Cyan
if ($globalSuccess) {
    Write-Host "        ALL SYSTEMS GO! (PASSED)            " -ForegroundColor Green
} else {
    Write-Host "        SOME TESTS FAILED                   " -ForegroundColor Red
}
Write-Host "============================================" -ForegroundColor Cyan
