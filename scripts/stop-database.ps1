# Stop Database PowerShell Script
Write-Host "Stopping database services..." -ForegroundColor Yellow

# Navigate to database directory
Set-Location -Path "$PSScriptRoot\..\database"

# Check if containers are running
$mysqlRunning = docker ps | Select-String "zhixuanche_mysql"
$nginxRunning = docker ps | Select-String "zhixuanche_nginx"

if (-not $mysqlRunning -and -not $nginxRunning) {
    Write-Host "Database services are already stopped." -ForegroundColor Yellow
    exit
}

# Stop services
docker-compose down

if ($LASTEXITCODE -eq 0) {
    Write-Host "Database services stopped successfully!" -ForegroundColor Green
    docker-compose ps
}
else {
    Write-Host "Failed to stop database services. Attempting force stop..." -ForegroundColor Red
    docker stop zhixuanche_mysql zhixuanche_nginx 2>$null
    docker-compose ps
} 