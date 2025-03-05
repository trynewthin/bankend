# Start Database PowerShell Script
Write-Host "Starting database services..." -ForegroundColor Yellow

# Navigate to database directory
Set-Location -Path "$PSScriptRoot\..\database"

# Start services
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host "Database services started successfully!" -ForegroundColor Green
    Write-Host "Connection Info:" -ForegroundColor Cyan
    Write-Host "- Host: localhost" -ForegroundColor White
    Write-Host "- Port: 3307" -ForegroundColor White
    Write-Host "- Database: zhixuanche" -ForegroundColor White
    Write-Host "- Username: zhixuanche_user (root privileges)" -ForegroundColor White
    Write-Host "- Password: zhixuanche_pass" -ForegroundColor White
    
    # Check if MySQL is ready
    Write-Host "Waiting for MySQL to be ready..." -ForegroundColor Yellow
    $retry = 0
    $maxRetry = 5
    
    do {
        Start-Sleep -Seconds 2
        $retry++
        docker exec zhixuanche_mysql mysqladmin ping -h localhost -u root -pzhixuanche123 2>$null
        if ($LASTEXITCODE -eq 0) {
            Write-Host "MySQL is ready!" -ForegroundColor Green
            break
        }
        Write-Host "Waiting for MySQL... ($retry/$maxRetry)" -ForegroundColor Yellow
    } while ($retry -lt $maxRetry)
    
    # Show container status
    docker-compose ps
}
else {
    Write-Host "Failed to start database services." -ForegroundColor Red
} 