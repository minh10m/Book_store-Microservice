# Requires PowerShell 5.1 or later
$ErrorActionPreference = "Stop"

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Building Spring Boot Microservices..." -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
.\mvnw.cmd clean package -DskipTests

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "Building and Starting Docker Containers..." -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan
docker compose -f deployment/docker-compose/apps.yml up --build -d

Write-Host "=========================================" -ForegroundColor Green
Write-Host "Application Started Successfully!" -ForegroundColor Green
Write-Host "API Gateway: http://localhost:8989" -ForegroundColor Green
Write-Host "Web App: http://localhost:8080" -ForegroundColor Green
Write-Host "=========================================" -ForegroundColor Green
