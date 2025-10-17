<#
.SYNOPSIS
    PowerShell wrapper for rebuild_third_party_libs.bat

.DESCRIPTION
    This script provides a PowerShell-friendly way to execute the batch file
    that rebuilds Android third-party static libraries. It ensures proper
    environment handling and provides better error reporting.

.EXAMPLE
    .\rebuild_third_party_libs.ps1
#>

param(
    [string]$NdkHome = "D:\android-ndk-r10e"
)

$ErrorActionPreference = "Stop"

# Get script directory and resolve project root
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectRoot = (Resolve-Path (Join-Path $ScriptDir "..\..")).Path

Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "MT3 Android Third-Party Libraries Rebuild (PowerShell Wrapper)" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "Project Root: $ProjectRoot" -ForegroundColor Gray
Write-Host "NDK Home: $NdkHome" -ForegroundColor Gray
Write-Host ""

# Verify NDK exists
if (-not (Test-Path "$NdkHome\ndk-build.cmd")) {
    Write-Host "[ERROR] NDK not found at: $NdkHome" -ForegroundColor Red
    Write-Host "[HINT] Please install NDK r10e or set the -NdkHome parameter" -ForegroundColor Yellow
    exit 1
}

# Set environment variable for the batch script
$env:NDK_HOME = $NdkHome

# Path to the batch script
$BatchScript = Join-Path $ScriptDir "rebuild_third_party_libs.bat"

if (-not (Test-Path $BatchScript)) {
    Write-Host "[ERROR] Batch script not found: $BatchScript" -ForegroundColor Red
    exit 1
}

Write-Host "[INFO] Executing batch script in CMD environment..." -ForegroundColor Green
Write-Host "------------------------------------------------------------" -ForegroundColor Gray

# Execute the batch script using cmd.exe
$Process = Start-Process -FilePath "cmd.exe" `
    -ArgumentList "/c", "`"cd /d $ProjectRoot && scripts\android\rebuild_third_party_libs.bat`"" `
    -NoNewWindow `
    -PassThru `
    -Wait

Write-Host "------------------------------------------------------------" -ForegroundColor Gray

if ($Process.ExitCode -eq 0) {
    Write-Host ""
    Write-Host "============================================================" -ForegroundColor Green
    Write-Host "Third-party libraries rebuild completed successfully!" -ForegroundColor Green
    Write-Host "============================================================" -ForegroundColor Green

    # Show log file location
    $LogDir = Join-Path $ProjectRoot "build_logs"
    if (Test-Path $LogDir) {
        $LatestLog = Get-ChildItem $LogDir -Filter "third_party_rebuild_*.log" |
                     Sort-Object LastWriteTime -Descending |
                     Select-Object -First 1
        if ($LatestLog) {
            Write-Host "Log file: $($LatestLog.FullName)" -ForegroundColor Gray
        }
    }
} else {
    Write-Host ""
    Write-Host "============================================================" -ForegroundColor Red
    Write-Host "Build failed with exit code: $($Process.ExitCode)" -ForegroundColor Red
    Write-Host "============================================================" -ForegroundColor Red

    # Show log file location for debugging
    $LogDir = Join-Path $ProjectRoot "build_logs"
    if (Test-Path $LogDir) {
        $LatestLog = Get-ChildItem $LogDir -Filter "third_party_rebuild_*.log" |
                     Sort-Object LastWriteTime -Descending |
                     Select-Object -First 1
        if ($LatestLog) {
            Write-Host "Check log file for details: $($LatestLog.FullName)" -ForegroundColor Yellow

            # Show last 20 lines of the log
            Write-Host ""
            Write-Host "Last 20 lines of log:" -ForegroundColor Yellow
            Get-Content $LatestLog.FullName -Tail 20 | ForEach-Object { Write-Host "  $_" -ForegroundColor Gray }
        }
    }

    exit $Process.ExitCode
}
