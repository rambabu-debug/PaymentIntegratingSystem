@echo off
REM Master startup script - Kills all existing processes and starts fresh

echo.
echo ========================================
echo PAYMENT INTEGRATION SYSTEM - FULL STACK
echo ========================================
echo.
echo Step 1: Killing any existing processes...
echo.

REM Kill any existing Java processes
taskkill /IM java.exe /F 2>nul

REM Kill any existing Node processes
taskkill /IM node.exe /F 2>nul

timeout /t 3 /nobreak

echo.
echo Step 2: Starting Backend on Port 7070...
echo.

REM Set Node.js path
set PATH=%PATH%;C:\Program Files\nodejs

REM Start Backend in new window
start "Payment System Backend" cmd /k "cd /d C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem && call gradlew.bat bootRun"

echo Waiting for backend to initialize...
timeout /t 8 /nobreak

echo.
echo Step 3: Starting Frontend on Port 5000...
echo.

REM Start Frontend in new window
start "Payment System Frontend" cmd /k "cd /d C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem\frontend && (if not exist node_modules call npm install) && call npm run dev"

echo.
echo ========================================
echo Both services are starting!
echo ========================================
echo.
echo Backend:  http://localhost:7070
echo Frontend: http://localhost:5000
echo.
echo Two new windows will open. Do NOT close them!
echo Open your browser to: http://localhost:5000
echo.
pause

