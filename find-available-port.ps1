# Find first available port starting from a given port
# Usage: .\find-available-port.ps1 -startPort 8080
param(
    [int]$startPort = 8080,
    [int]$endPort = 9000
)

Write-Host "Scanning for available ports between $startPort and $endPort..." -ForegroundColor Cyan

for ($port = $startPort; $port -le $endPort; $port++) {
    $connection = $null
    try {
        $connection = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
    } catch {
        # No connection on this port
    }

    if ($null -eq $connection) {
        Write-Host "✓ Port $port is available!" -ForegroundColor Green
        return $port
    }
}

Write-Host "✗ No available ports found between $startPort and $endPort" -ForegroundColor Red
exit 1

