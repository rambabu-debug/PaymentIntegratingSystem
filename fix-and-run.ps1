#!/usr/bin/env pwsh

Write-Host "🔧 Fixing Java 21 / Gradle Issue" -ForegroundColor Cyan
Write-Host "=================================" -ForegroundColor Cyan

# Kill all Java processes
Write-Host "`n1. Killing all Java processes..." -ForegroundColor Yellow
Get-Process java -ErrorAction SilentlyContinue | ForEach-Object {
    Stop-Process -Id $_.Id -Force -ErrorAction SilentlyContinue
    Write-Host "   Killed PID: $($_.Id)" -ForegroundColor Green
}

# Wait
Write-Host "`n2. Waiting 3 seconds..." -ForegroundColor Yellow
Start-Sleep -Seconds 3

# Clear Gradle cache
Write-Host "`n3. Clearing Gradle cache..." -ForegroundColor Yellow
$projectPath = 'C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem'
Push-Location $projectPath

if (Test-Path '.gradle') {
    Remove-Item -Recurse -Force -Path '.gradle' -ErrorAction SilentlyContinue
    Write-Host "   Removed .gradle directory" -ForegroundColor Green
}

if (Test-Path 'build') {
    Remove-Item -Recurse -Force -Path 'build' -ErrorAction SilentlyContinue
    Write-Host "   Removed build directory" -ForegroundColor Green
}

# Clean build
Write-Host "`n4. Running clean build..." -ForegroundColor Yellow
& .\gradlew.bat clean build --no-daemon
if ($LASTEXITCODE -eq 0) {
    Write-Host "`n✅ Build successful!" -ForegroundColor Green
} else {
    Write-Host "`n❌ Build failed. Try running again." -ForegroundColor Red
}

# Start application
Write-Host "`n5. Starting application on port 9090..." -ForegroundColor Yellow
& .\gradlew.bat bootRun --no-daemon

Pop-Location

