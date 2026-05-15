@echo off
REM Master script to run both backend and frontend
REM This will open two separate windows for backend and frontend

echo ========================================
echo Payment Integration System - Full Stack
echo ========================================
echo.
echo This will start both Backend and Frontend
echo.

REM Set up environment
set PATH=%PATH%;C:\Program Files\nodejs

REM Start Backend in a new window
echo Starting Backend on port 7070...
start "Payment System - Backend" cmd /k "cd /d C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem && call gradlew.bat bootRun"

REM Wait a bit for backend to start
echo Waiting for backend to initialize...
timeout /t 5 /nobreak

REM Start Frontend in a new window
echo Starting Frontend on port 5000...
start "Payment System - Frontend" cmd /k "cd /d C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem\frontend && (if not exist node_modules call npm install) && call npm run dev"

echo.
echo ========================================
echo Both services are starting...
echo ========================================
echo.
echo Backend will be available at: http://localhost:7070
echo Frontend will be available at: http://localhost:5000
echo.
echo Two new windows will open - minimize but do NOT close them!
echo.
pause

