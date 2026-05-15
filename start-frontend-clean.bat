@echo off
REM Kill any processes on port 5000 first
echo Cleaning up any existing frontend processes...

for /f "tokens=5" %%a in ('netstat -ano ^| findstr :5000') do taskkill /PID %%a /F 2>nul

REM Wait a moment for cleanup
timeout /t 2 /nobreak

REM Set Node.js path
set PATH=%PATH%;C:\Program Files\nodejs

echo.
echo ========================================
echo PAYMENT INTEGRATION SYSTEM - FRONTEND
echo ========================================
echo.
echo Starting Frontend...
echo Port: http://localhost:5000
echo.

cd /d "C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem\frontend"

REM Check if node_modules exists
if not exist "node_modules" (
    echo Installing dependencies first...
    call npm install
    echo.
)

REM Start frontend
call npm run dev

pause

