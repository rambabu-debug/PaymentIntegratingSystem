@echo off
REM Kill any processes on ports 5000 and 7070 first
echo Cleaning up any existing processes...

for /f "tokens=5" %%a in ('netstat -ano ^| findstr :5000') do taskkill /PID %%a /F 2>nul
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :7070') do taskkill /PID %%a /F 2>nul

REM Wait a moment for cleanup
timeout /t 2 /nobreak

REM Set Node.js path
set PATH=%PATH%;C:\Program Files\nodejs

echo.
echo ========================================
echo PAYMENT INTEGRATION SYSTEM
echo ========================================
echo.
echo Starting Backend...
echo.

cd /d "C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem"

REM Start backend
call gradlew.bat bootRun

pause

