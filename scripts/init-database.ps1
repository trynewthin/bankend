# Initialize Database PowerShell Script
Write-Host "Database Initialization Script" -ForegroundColor Cyan
Write-Host "WARNING: This will delete and rebuild the entire database structure!" -ForegroundColor Red

# Confirm action
$confirm = Read-Host "Are you sure you want to initialize the database? (y/n)"
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

# Create temp SQL file
Write-Host "Step 1: Recreating database and user..." -ForegroundColor Yellow
@"
DROP DATABASE IF EXISTS zhixuanche;
CREATE DATABASE zhixuanche CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
DROP USER IF EXISTS 'zhixuanche_user'@'%';
CREATE USER 'zhixuanche_user'@'%' IDENTIFIED BY 'zhixuanche_pass';
GRANT ALL PRIVILEGES ON *.* TO 'zhixuanche_user'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
"@ | Out-File -FilePath "temp_init.sql" -Encoding utf8

# Execute SQL
docker exec -i zhixuanche_mysql mysql -uroot -pzhixuanche123 < temp_init.sql
Remove-Item -Path "temp_init.sql"

if ($LASTEXITCODE -eq 0) {
    Write-Host "Database and user recreated successfully!" -ForegroundColor Green
}
else {
    Write-Host "Failed to recreate database and user." -ForegroundColor Red
    exit
}

# Create table structure
Write-Host "Step 2: Creating table structure..." -ForegroundColor Yellow
docker exec -i zhixuanche_mysql mysql -uroot -pzhixuanche123 < mysql/init/schema.sql

if ($LASTEXITCODE -eq 0) {
    Write-Host "Table structure created successfully!" -ForegroundColor Green
}
else {
    Write-Host "Failed to create table structure." -ForegroundColor Red
    exit
}

# Ask to populate data
$fillData = Read-Host "Do you want to populate initial data? (y/n)"
if ($fillData -eq "y") {
    Write-Host "Step 3: Populating initial data..." -ForegroundColor Yellow
    docker exec -i zhixuanche_mysql mysql -uroot -pzhixuanche123 < mysql/init/seed-data.sql
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Data populated successfully!" -ForegroundColor Green
    }
    else {
        Write-Host "Failed to populate data." -ForegroundColor Red
        exit
    }
}

Write-Host "Database initialization complete!" -ForegroundColor Green

# Show configuration
Write-Host "Database Configuration:" -ForegroundColor Cyan
Write-Host "- Host: localhost" -ForegroundColor White
Write-Host "- Port: 3307" -ForegroundColor White
Write-Host "- Database: zhixuanche" -ForegroundColor White
Write-Host "- Username: zhixuanche_user (with root privileges)" -ForegroundColor White
Write-Host "- Password: zhixuanche_pass" -ForegroundColor White

# Show tables
Write-Host "Database Tables:" -ForegroundColor Cyan
docker exec zhixuanche_mysql mysql -uroot -pzhixuanche123 -e "USE zhixuanche; SHOW TABLES;" 