@echo off
REM Start Frontend React Application
set PATH=%PATH%;C:\Program Files\nodejs

cd /d "C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem\frontend"

echo ========================================
echo Starting Payment Integration Frontend...
echo ========================================
echo.
echo Frontend will run on: http://localhost:5000
echo API Proxy to Backend: http://localhost:7070
echo.
echo Make sure the backend is running on port 7070!
echo.

REM Check if node_modules exists, if not install dependencies
if not exist "node_modules" (
    echo Installing dependencies...
    call npm install
    echo.
)

echo Starting development server...
call npm run dev

pause

