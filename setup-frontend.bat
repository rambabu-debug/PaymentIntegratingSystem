@echo off
REM Add Node.js to PATH temporarily
set PATH=%PATH%;C:\Program Files\nodejs

REM Verify npm
npm --version

REM Navigate to frontend
cd /d "C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem\frontend"

REM Install dependencies
call npm install

REM Start dev server
call npm run dev

pause

