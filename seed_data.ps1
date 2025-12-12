# seed_data.ps1
$ErrorActionPreference = "Stop"

function Invoke-Api {
    param($Url, $Method="GET", $Body=$null)
    Write-Host "Calling $Method $Url" -ForegroundColor Cyan
    try {
        $params = @{
            Uri = $Url
            Method = $Method
            ContentType = "application/json"
        }
        if ($Body) { $params.Body = $Body | ConvertTo-Json -Depth 10 }
        
        $response = Invoke-RestMethod @params
        Write-Host "Success!" -ForegroundColor Green
        return $response
    } catch {
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
             $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
             Write-Host "Response Body: $($reader.ReadToEnd())" -ForegroundColor Red
        }
        return $null
    }
}

Write-Host "🌱 Seeding Database with Fresh Data..." -ForegroundColor Yellow

# 1. Create Users
Write-Host "`n1. Creating Users..." -ForegroundColor Yellow
$alice = Invoke-Api -Url "http://localhost:3001/users" -Method "POST" -Body @{name="Alice"; email="alice@test.com"; phone="555-0101"}
$bob = Invoke-Api -Url "http://localhost:3001/users" -Method "POST" -Body @{name="Bob"; email="bob@test.com"; phone="555-0102"}
$charlie = Invoke-Api -Url "http://localhost:3001/users" -Method "POST" -Body @{name="Charlie"; email="charlie@test.com"; phone="555-0103"}

if ($alice -and $bob -and $charlie) {
    Write-Host "Created Users:"
    Write-Host " - Alice ($($alice.id))"
    Write-Host " - Bob ($($bob.id))"
    Write-Host " - Charlie ($($charlie.id))"

    # 2. Create Groups
    Write-Host "`n2. Creating Groups..." -ForegroundColor Yellow
    
    # Group 1: Apartment
    $aptGroup = Invoke-Api -Url "http://localhost:3001/groups" -Method "POST" -Body @{
        name="Apartment 4B"
        description="Shared expenses for the flat"
        memberIds=@($alice.id, $bob.id, $charlie.id)
    }

    # Group 2: Trip
    $tripGroup = Invoke-Api -Url "http://localhost:3001/groups" -Method "POST" -Body @{
        name="Road Trip"
        description="Weekend getaway"
        memberIds=@($alice.id, $bob.id)
    }

    if ($aptGroup -and $tripGroup) {
        Write-Host "Created Groups:"
        Write-Host " - Apartment 4B ($($aptGroup.id))"
        Write-Host " - Road Trip ($($tripGroup.id))"
    }
}

Write-Host "`n✅ Seeding Complete! Refresh the UI." -ForegroundColor Green
