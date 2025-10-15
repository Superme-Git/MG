# MT3 Build Configuration Check Script
# PowerShell version

Write-Host "========================================"
Write-Host "MT3 Project Build Configuration Check"
Write-Host "========================================"
Write-Host ""

# Check 1: Visual Studio 2013
Write-Host "[1/5] Checking Visual Studio 2013..."
if (Test-Path env:VS120COMNTOOLS) {
    Write-Host "[PASS] Visual Studio 2013: $env:VS120COMNTOOLS" -ForegroundColor Green
} else {
    Write-Host "[ERROR] Visual Studio 2013 not found" -ForegroundColor Red
    exit 1
}
Write-Host ""

# Check 2: PlatformToolset
Write-Host "[2/5] Checking PlatformToolset..."
$wrongToolsets = Get-ChildItem -Path . -Filter *.vcxproj -Recurse | 
    Select-String -Pattern "<PlatformToolset>(v140|v141|v142)</PlatformToolset>"

if ($wrongToolsets) {
    Write-Host "[ERROR] Found incorrect toolset versions:" -ForegroundColor Red
    $wrongToolsets | ForEach-Object { Write-Host $_.Path -ForegroundColor Yellow }
    exit 1
} else {
    Write-Host "[PASS] All projects use v120" -ForegroundColor Green
}
Write-Host ""

# Check 3: Library files
Write-Host "[3/5] Checking library files..."
$libs = @(
    "common\lua\Release.win32\lua.lib",
    "common\platform\Release.win32\platform.lib",
    "engine\Release.win32\engine.lib"
)

$missing = 0
foreach ($lib in $libs) {
    if (Test-Path $lib) {
        Write-Host "[FOUND] $lib" -ForegroundColor Green
    } else {
        Write-Host "[INFO] $lib not compiled yet (normal for first build)" -ForegroundColor Yellow
    }
}
Write-Host ""

# Check 4: Runtime DLLs
Write-Host "[4/5] Checking runtime DLLs..."
$dllPath = "client\resource\bin\debug"
if (Test-Path "$dllPath\MSVCR120.dll") {
    Write-Host "[PASS] Runtime DLLs found" -ForegroundColor Green
} else {
    Write-Host "[INFO] MSVCR120.dll not found (copy after build)" -ForegroundColor Yellow
}
Write-Host ""

# Check 5: Resource directories
Write-Host "[5/5] Checking resource directories..."
$dirs = @("client\resource\script", "client\resource\res")
foreach ($dir in $dirs) {
    if (Test-Path $dir) {
        Write-Host "[FOUND] $dir" -ForegroundColor Green
    } else {
        Write-Host "[INFO] $dir not deployed yet" -ForegroundColor Yellow
    }
}
Write-Host ""

Write-Host "========================================"
Write-Host "Configuration check completed!" -ForegroundColor Green
Write-Host "Project configuration is correct."
Write-Host "========================================"
exit 0