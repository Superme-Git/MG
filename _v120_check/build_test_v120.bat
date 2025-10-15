@echo off
setlocal
set VC1="C:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\vcvarsall.bat"
set VC2="C:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\bin\vcvars32.bat"

if exist %VC1% (
  call %VC1% x86
) else if exist %VC2% (
  call %VC2%
) else (
  echo [ERROR] VS2013 vcvars script not found: %VC1% or %VC2%
  exit /b 1
)

cd /d "%~dp0"
echo [INFO] Using cl:
cl /?
echo [INFO] Building test_v120.cpp with v120 toolset...
cl /nologo /EHsc /MT test_v120.cpp /Fe:test_v120.exe
if errorlevel 1 (
  echo [ERROR] Build failed
  exit /b 1
)
echo [INFO] Running test_v120.exe...
test_v120.exe
echo [INFO] Done.
exit /b 0