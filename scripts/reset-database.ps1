# Reset Database PowerShell Script
Write-Host "Database Reset Script" -ForegroundColor Cyan
Write-Host "This will clear and repopulate the database data" -ForegroundColor Yellow

# Confirm action
$confirm = Read-Host "Are you sure you want to reset the database? (y/n)"
if ($confirm -ne "y") {
    Write-Host "Operation cancelled." -ForegroundColor Yellow
    exit
}

# Navigate to database directory
Set-Location -Path "$PSScriptRoot\..\database"

# Check if MySQL is running
$mysqlRunning = docker ps | Select-String "zhixuanche_mysql"
if (-not $mysqlRunning) {
    Write-Host "Starting MySQL service..." -ForegroundColor Yellow
    docker-compose up -d mysql
    Start-Sleep -Seconds 10
}

# Check again
$mysqlRunning = docker ps | Select-String "zhixuanche_mysql"
if (-not $mysqlRunning) {
    Write-Host "Failed to start MySQL service." -ForegroundColor Red
    exit
}

# Clear data
Write-Host "Step 1: Clearing data..." -ForegroundColor Yellow
docker exec -i zhixuanche_mysql mysql -uroot -pzhixuanche123 < mysql/init/clear-data.sql

if ($LASTEXITCODE -eq 0) {
    Write-Host "Data cleared successfully!" -ForegroundColor Green
}
else {
    Write-Host "Failed to clear data." -ForegroundColor Red
    exit
}

# Populate data
Write-Host "Step 2: Populating data..." -ForegroundColor Yellow
docker exec -i zhixuanche_mysql mysql -uroot -pzhixuanche123 < mysql/init/seed-data.sql

if ($LASTEXITCODE -eq 0) {
    Write-Host "Data populated successfully!" -ForegroundColor Green
    Write-Host "Database reset complete!" -ForegroundColor Green
    
    # Show tables
    Write-Host "Database Tables:" -ForegroundColor Cyan
    docker exec zhixuanche_mysql mysql -uroot -pzhixuanche123 -e "USE zhixuanche; SHOW TABLES;"
}
else {
    Write-Host "Failed to populate data." -ForegroundColor Red
} 