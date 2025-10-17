<#
.SYNOPSIS
    Android build environment checker for MT3 project

.DESCRIPTION
    Verifies that all required tools and directories are in place before building
#>

$ErrorActionPreference = "Continue"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectRoot = (Resolve-Path (Join-Path $ScriptDir "..\..")).Path

Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "MT3 Android Build Environment Checker" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan
Write-Host ""

$AllOk = $true

# Check 1: NDK
Write-Host "[CHECK] Android NDK r10e..." -NoNewline
$NdkPaths = @(
    "D:\android-ndk-r10e",
    $env:NDK_HOME,
    $env:ANDROID_NDK_HOME
) | Where-Object { $_ }

$NdkFound = $false
foreach ($Path in $NdkPaths) {
    if (Test-Path "$Path\ndk-build.cmd") {
        Write-Host " OK" -ForegroundColor Green
        Write-Host "  Location: $Path" -ForegroundColor Gray
        $NdkFound = $true
        break
    }
}

if (-not $NdkFound) {
    Write-Host " FAILED" -ForegroundColor Red
    Write-Host "  NDK r10e not found in:" -ForegroundColor Yellow
    Write-Host "    - D:\android-ndk-r10e" -ForegroundColor Gray
    Write-Host "    - %NDK_HOME%" -ForegroundColor Gray
    Write-Host "    - %ANDROID_NDK_HOME%" -ForegroundColor Gray
    $AllOk = $false
}

# Check 2: Third-party source directories
Write-Host ""
Write-Host "[CHECK] Third-party source directories..." -ForegroundColor White

$ThirdPartyLibs = @(
    @{Name="zlib"; Path="third_party\zlib\jni\Android.mk"},
    @{Name="libpng"; Path="third_party\libpng\jni\Android.mk"},
    @{Name="libjpeg"; Path="third_party\libjpeg\jni\Android.mk"},
    @{Name="libtiff"; Path="third_party\tiff\jni\Android.mk"},
    @{Name="openssl"; Path="third_party\openssl\jni\Android.mk"},
    @{Name="curl"; Path="third_party\curl\jni\Android.mk"}
)

foreach ($Lib in $ThirdPartyLibs) {
    $FullPath = Join-Path $ProjectRoot $Lib.Path
    Write-Host "  $($Lib.Name.PadRight(10)) " -NoNewline
    if (Test-Path $FullPath) {
        Write-Host "OK" -ForegroundColor Green
    } else {
        Write-Host "MISSING" -ForegroundColor Red
        Write-Host "    Expected: $FullPath" -ForegroundColor Gray
        $AllOk = $false
    }
}

# Check 3: Output directories writable
Write-Host ""
Write-Host "[CHECK] Output directories..." -ForegroundColor White

$OutputDirs = @(
    "dependencies\zlib\prebuilt\android\armeabi-v7a",
    "dependencies\png\prebuilt\android\armeabi-v7a",
    "dependencies\jpeg\prebuilt\android\armeabi-v7a",
    "cocos2d-2.0-rc2-x-2.0.1\external\tiff\prebuilt\android\armeabi-v7a",
    "cocos2d-2.0-rc2-x-2.0.1\external\curl\prebuilt\android\armeabi-v7a"
)

foreach ($Dir in $OutputDirs) {
    $FullPath = Join-Path $ProjectRoot $Dir
    Write-Host "  Checking: $Dir" -ForegroundColor Gray
    try {
        if (-not (Test-Path $FullPath)) {
            New-Item -ItemType Directory -Path $FullPath -Force | Out-Null
            Write-Host "    Created" -ForegroundColor Yellow
        } else {
            Write-Host "    Exists" -ForegroundColor Green
        }
    } catch {
        Write-Host "    FAILED: $_" -ForegroundColor Red
        $AllOk = $false
    }
}

# Summary
Write-Host ""
Write-Host "============================================================" -ForegroundColor Cyan
if ($AllOk) {
    Write-Host "Environment check PASSED" -ForegroundColor Green
    Write-Host "You can now run: .\rebuild_third_party_libs.ps1" -ForegroundColor White
} else {
    Write-Host "Environment check FAILED" -ForegroundColor Red
    Write-Host "Please fix the issues above before building" -ForegroundColor Yellow
}
Write-Host "============================================================" -ForegroundColor Cyan

exit $(if ($AllOk) { 0 } else { 1 })
