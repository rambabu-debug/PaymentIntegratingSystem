# Run Payment Integration System - Frontend
# This script starts the React frontend

Write-Host "================================"
Write-Host "Payment Integration System"
Write-Host "Starting Frontend..."
Write-Host "================================"
Write-Host ""
Write-Host "Frontend will run on: http://localhost:5001"
Write-Host "Backend should already be running on: http://localhost:7070"
Write-Host ""

# Add Node.js to PATH
$env:PATH += ";C:\Program Files\nodejs"

cd 'C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem\frontend'

Write-Host "Checking dependencies..."
if (!(Test-Path "node_modules")) {
    Write-Host "Installing npm dependencies..."
    npm install
}

Write-Host ""
Write-Host "Starting development server..."
Write-Host ""

npm run dev

Read-Host "Press Enter to exit"

