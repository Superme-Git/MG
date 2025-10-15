@echo off
setlocal EnableExtensions

set "CHECK_DIR=%~dp0"
cd /d "%CHECK_DIR%"

echo [INFO] Starting VS2013 v120 toolset validation...

rem Known VS2013 paths
set "VS2013_VCVARSALL=C:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\vcvarsall.bat"
set "VS2013_VCVARS32=C:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\bin\vcvars32.bat"

set "FOUND_VCVARS="
set "FOUND_ARG="
if exist "%VS2013_VCVARSALL%" (
  set "FOUND_VCVARS=%VS2013_VCVARSALL%"
  set "FOUND_ARG=x86"
)
if not defined FOUND_VCVARS (
  if exist "%VS2013_VCVARS32%" (
    set "FOUND_VCVARS=%VS2013_VCVARS32%"
  )
)

if defined FOUND_VCVARS (
  echo [INFO] Found VS2013 vcvars: "%FOUND_VCVARS%"
  call "%FOUND_VCVARS%" %FOUND_ARG%
  goto build
)

rem Try VSwhere and newer VS versions
set "VSWHERE=C:\Program Files (x86)\Microsoft Visual Studio\Installer\vswhere.exe"
if exist "%VSWHERE%" (
  for /f "usebackq delims=" %%I in (`"C:\Program Files (x86)\Microsoft Visual Studio\Installer\vswhere.exe" -latest -products * -requires Microsoft.VisualStudio.Component.VC.Tools.x86.x64 -property installationPath`) do (
    set "VSROOT=%%~I"
  )
)

if defined VSROOT (
  set "NEW_VCVARS=%VSROOT%\VC\Auxiliary\Build\vcvarsall.bat"
  if exist "%NEW_VCVARS%" (
    echo [INFO] Found newer VS vcvars: "%NEW_VCVARS%"
    echo [INFO] Trying to activate v120 toolset via -vcvars_ver=12.0...
    call "%NEW_VCVARS%" x86 -vcvars_ver=12.0
    rem Check if cl is available after attempting v120
    where cl >nul 2>&1
    if errorlevel 1 (
      echo [WARN] v120 activation failed or not installed. Falling back to default toolset.
      call "%NEW_VCVARS%" x86
      set "V120_MISSING=1"
    ) else (
      set "V120_MISSING="
    )
    goto build
  )
)

echo [ERROR] No VS toolset environment found. Please install VS2013 or add "VC++ 2013 v120 toolset".
exit /b 1

:build
rem Verify that cl is available
where cl >nul 2>&1
if errorlevel 1 (
  echo [ERROR] 'cl' compiler not found in PATH after vcvars. Aborting.
  exit /b 1
)

if not exist "test_v120.cpp" (
  > test_v120.cpp echo #include ^<iostream^>
  >> test_v120.cpp echo int main(){ std::cout ^<^< "v120 toolset compile OK" ^<^< std::endl; return 0; }
)

echo [INFO] Compiling test_v120.cpp ...
cl /nologo /EHsc /MT test_v120.cpp /Fe:test_v120.exe
if errorlevel 1 (
  echo [ERROR] cl compile failed.
  exit /b 1
)

echo [INFO] Running test_v120.exe ...
test_v120.exe

if defined V120_MISSING (
  echo [RESULT] Toolset active but v120 is NOT installed. Please install VC++ 2013 v120 toolset.
  exit /b 2
) else (
  echo [RESULT] VS2013 v120 toolset validated successfully.
  exit /b 0
)

endlocal