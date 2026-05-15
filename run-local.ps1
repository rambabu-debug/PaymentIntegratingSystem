param(
    [string]$stripeSecret = '',  # Read from environment variable APP_STRIPE_SECRET_KEY
    [string]$stripeWebhook = '',  # Read from environment variable APP_STRIPE_WEBHOOK_SECRET
    [string]$jwtSecret = '',  # Read from environment variable APP_JWT_SECRET
    [string]$jwtExpirationMs = '3600000',
    [string]$firebaseProjectId = '',  # Read from environment variable FIREBASE_PROJECT_ID
    [string]$firebaseCredPath = '',
    [string]$serverPort = '',  # leave empty to auto-detect available port
    [switch]$findAvailablePort  # if true, find first available port
)

Write-Host "Preparing environment and starting application..."

# Use environment variables if parameters not provided
if ([string]::IsNullOrEmpty($stripeSecret)) { $stripeSecret = $env:APP_STRIPE_SECRET_KEY }
if ([string]::IsNullOrEmpty($stripeWebhook)) { $stripeWebhook = $env:APP_STRIPE_WEBHOOK_SECRET }
if ([string]::IsNullOrEmpty($jwtSecret)) { $jwtSecret = $env:APP_JWT_SECRET }
if ([string]::IsNullOrEmpty($firebaseProjectId)) { $firebaseProjectId = $env:FIREBASE_PROJECT_ID }

# Validate required secrets are set
if ([string]::IsNullOrEmpty($stripeSecret)) {
    Write-Host "⚠️  WARNING: APP_STRIPE_SECRET_KEY not set. Set environment variable:" -ForegroundColor Yellow
    Write-Host '    $env:APP_STRIPE_SECRET_KEY = "sk_test_..."' -ForegroundColor Yellow
}
if ([string]::IsNullOrEmpty($stripeWebhook)) {
    Write-Host "⚠️  WARNING: APP_STRIPE_WEBHOOK_SECRET not set. Set environment variable:" -ForegroundColor Yellow
    Write-Host '    $env:APP_STRIPE_WEBHOOK_SECRET = "whsec_..."' -ForegroundColor Yellow
}
if ([string]::IsNullOrEmpty($jwtSecret)) {
    Write-Host "⚠️  WARNING: APP_JWT_SECRET not set. Set environment variable:" -ForegroundColor Yellow
    Write-Host '    $env:APP_JWT_SECRET = "your-secret-key"' -ForegroundColor Yellow
}
if ($findAvailablePort -or $serverPort -eq '') {
    Write-Host "Finding available port..." -ForegroundColor Yellow

    $availablePort = $null
    for ($port = 8080; $port -le 9000; $port++) {
        $connection = $null
        try {
            $connection = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
        } catch {
            # No connection on this port
        }

        if ($null -eq $connection) {
            $availablePort = $port
            break
        }
    }

    if ($availablePort) {
        $serverPort = $availablePort.ToString()
        Write-Host "✓ Found available port: $serverPort" -ForegroundColor Green
    } else {
        Write-Host "✗ No available ports found between 8080-9000. Please manually specify a port or stop other processes." -ForegroundColor Red
        exit 1
    }
}

$env:APP_STRIPE_SECRET_KEY = $stripeSecret
$env:APP_STRIPE_WEBHOOK_SECRET = $stripeWebhook
$env:APP_JWT_SECRET = $jwtSecret
$env:APP_JWT_EXPIRATION_MS = $jwtExpirationMs
$env:FIREBASE_PROJECT_ID = $firebaseProjectId
$env:SERVER_PORT = $serverPort

Write-Host "Environment variables set:" -ForegroundColor Green
Write-Host " APP_STRIPE_SECRET_KEY= $env:APP_STRIPE_SECRET_KEY"
Write-Host " APP_STRIPE_WEBHOOK_SECRET= $env:APP_STRIPE_WEBHOOK_SECRET"
Write-Host " APP_JWT_SECRET= (hidden)"
Write-Host " FIREBASE_PROJECT_ID= $env:FIREBASE_PROJECT_ID"
Write-Host " SERVER_PORT= $env:SERVER_PORT"

Push-Location -Path "$PSScriptRoot"
try {
    Write-Host "Running Gradle bootRun... (this will stream logs)"
    & .\gradlew.bat clean bootRun --no-daemon
} finally {
    Pop-Location
}

Write-Host "Application stopped or Gradle exited." -ForegroundColor Cyan

