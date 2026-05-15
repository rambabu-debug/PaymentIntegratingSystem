@echo off
echo Killing all Java processes...
taskkill /F /IM java.exe 2>nul

echo Waiting 3 seconds...
timeout /t 3 /nobreak

echo Clearing Gradle cache...
cd /d "C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem"
rmdir /s /q .gradle 2>nul
rmdir /s /q build 2>nul

echo.
echo Running clean build...
call gradlew.bat clean build --no-daemon

echo.
echo Starting application...
call gradlew.bat bootRun --no-daemon -PserverPort=9090

pause

