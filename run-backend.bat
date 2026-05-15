@echo off
REM Start Backend Spring Boot Application
cd /d "C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem"

echo ========================================
echo Starting Payment Integration Backend...
echo ========================================
echo.
echo Backend will run on: http://localhost:7070
echo.

REM Kill any existing Java processes on port 7070 (optional safety measure)
netstat -ano | findstr :7070 >nul
if %errorlevel% equ 0 (
    echo Warning: Port 7070 is already in use. Attempting to use alternate behavior...
)

REM Run the backend
call gradlew.bat bootRun

pause

