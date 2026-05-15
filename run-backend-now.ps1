# Run Payment Integration System - Backend
# This script starts the Spring Boot backend

Write-Host "================================"
Write-Host "Payment Integration System"
Write-Host "Starting Backend..."
Write-Host "================================"
Write-Host ""
Write-Host "Backend will run on: http://localhost:7070"
Write-Host "Frontend expected on: http://localhost:5001"
Write-Host ""

cd 'C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem'

Write-Host "Starting Gradle build and run..."
Write-Host ""

# Run the backend
& .\gradlew.bat bootRun

Read-Host "Press Enter to exit"

