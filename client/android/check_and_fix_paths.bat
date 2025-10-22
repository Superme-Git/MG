@echo off
setlocal enableextensions enabledelayedexpansion
chcp 65001 >nul
rem ============================================================================
rem MT3 Build Environment Path Checker and Fixer
rem Purpose: Verify and fix all build tool paths
rem Author: Build Engineering Team
rem Date: 2025-10-23
rem ============================================================================

echo.
echo ========================================================================
echo MT3 Build Environment Path Checker v1.0
echo ========================================================================
echo.

set "ALL_OK=1"
set "FIXES_NEEDED=0"

rem ============================================================================
rem Check 1: Java
rem ============================================================================
echo [Check 1/4] Java JDK
echo ------------------------------------------------------------------------

if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\java.exe" (
        echo [OK] JAVA_HOME: %JAVA_HOME%
        "%JAVA_HOME%\bin\java.exe" -version 2>&1 | findstr "1.8\|1.7" >nul
        if errorlevel 1 (
            echo [WARN] Java version may not be 1.7 or 1.8 (recommended: 1.8)
        )
    ) else (
        echo [ERROR] JAVA_HOME points to invalid location: %JAVA_HOME%
        set "ALL_OK=0"
    )
) else (
    echo [WARN] JAVA_HOME not set
    where java >nul 2>&1
    if errorlevel 1 (
        echo [ERROR] Java not found in PATH
        set "ALL_OK=0"
        set "FIXES_NEEDED=1"
    ) else (
        echo [INFO] Java found in PATH (but JAVA_HOME not set)
        for /f "tokens=*" %%i in ('where java') do (
            echo [INFO] Java location: %%i
        )
    )
)
echo.

rem ============================================================================
rem Check 2: Apache Ant
rem ============================================================================
echo [Check 2/4] Apache Ant
echo ------------------------------------------------------------------------

if defined ANT_HOME (
    if exist "%ANT_HOME%\bin\ant.bat" (
        echo [OK] ANT_HOME: %ANT_HOME%
    ) else (
        echo [ERROR] ANT_HOME points to invalid location: %ANT_HOME%
        set "ALL_OK=0"
    )
) else (
    echo [WARN] ANT_HOME not set
)

where ant >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Apache Ant not found in PATH
    set "ALL_OK=0"
    set "FIXES_NEEDED=1"
) else (
    echo [OK] Apache Ant found in PATH
    for /f "tokens=*" %%i in ('where ant') do (
        echo [INFO] Ant location: %%i
    )
)
echo.

rem ============================================================================
rem Check 3: Android SDK
rem ============================================================================
echo [Check 3/4] Android SDK
echo ------------------------------------------------------------------------

set "SDK_OK=0"
if defined ANDROID_HOME (
    if exist "%ANDROID_HOME%\platform-tools\adb.exe" (
        echo [OK] ANDROID_HOME: %ANDROID_HOME%
        set "SDK_OK=1"
    ) else (
        echo [ERROR] ANDROID_HOME points to invalid SDK: %ANDROID_HOME%
        set "ALL_OK=0"
    )
) else if defined ANDROID_SDK_ROOT (
    if exist "%ANDROID_SDK_ROOT%\platform-tools\adb.exe" (
        echo [OK] ANDROID_SDK_ROOT: %ANDROID_SDK_ROOT%
        echo [INFO] Setting ANDROID_HOME from ANDROID_SDK_ROOT
        set "ANDROID_HOME=%ANDROID_SDK_ROOT%"
        set "SDK_OK=1"
    ) else (
        echo [ERROR] ANDROID_SDK_ROOT points to invalid SDK: %ANDROID_SDK_ROOT%
        set "ALL_OK=0"
    )
) else (
    echo [WARN] ANDROID_HOME not set, checking common locations...

    set "COMMON_PATHS=%LOCALAPPDATA%\Android\Sdk C:\Program Files (x86)\Android\android-sdk"
    for %%P in (%COMMON_PATHS%) do (
        if exist "%%P\platform-tools\adb.exe" (
            echo [INFO] Found SDK at: %%P
            echo [FIX] Run: setx ANDROID_HOME "%%P" /M
            set "SDK_OK=1"
            set "FIXES_NEEDED=1"
        )
    )

    if "!SDK_OK!"=="0" (
        echo [ERROR] Android SDK not found
        set "ALL_OK=0"
    )
)
echo.

rem ============================================================================
rem Check 4: Android NDK r10e
rem ============================================================================
echo [Check 4/4] Android NDK r10e
echo ------------------------------------------------------------------------

set "NDK_OK=0"
if defined NDK_HOME (
    if exist "%NDK_HOME%\ndk-build.cmd" (
        echo [OK] NDK_HOME: %NDK_HOME%

        rem Verify version
        "%NDK_HOME%\ndk-build.cmd" --version 2>&1 | findstr "GNU Make 3.81" >nul
        if errorlevel 1 (
            echo [WARN] NDK version may not be r10e (expected: GNU Make 3.81)
        ) else (
            echo [OK] NDK version verified (r10e)
        )
        set "NDK_OK=1"
    ) else (
        echo [ERROR] NDK_HOME points to invalid NDK: %NDK_HOME%
        set "ALL_OK=0"
    )
) else (
    echo [WARN] NDK_HOME not set, checking common locations...

    set "NDK_PATHS=D:\android-ndk-r10e C:\android-ndk-r10e D:\Android\android-ndk-r10e C:\Android\android-ndk-r10e"
    for %%P in (%NDK_PATHS%) do (
        if exist "%%P\ndk-build.cmd" (
            echo [INFO] Found NDK at: %%P
            echo [FIX] Run: setx NDK_HOME "%%P" /M
            set "NDK_OK=1"
            set "FIXES_NEEDED=1"
        )
    )

    if "!NDK_OK!"=="0" (
        echo [ERROR] Android NDK r10e not found
        echo [HINT] Download from: https://dl.google.com/android/repository/android-ndk-r10e-windows-x86_64.zip
        echo [HINT] Extract to: D:\android-ndk-r10e
        set "ALL_OK=0"
    )
)
echo.

rem ============================================================================
rem Generate local.properties
rem ============================================================================
echo [Additional Check] local.properties files
echo ------------------------------------------------------------------------

if defined ANDROID_HOME (
    set "SDK_DIR_ESCAPED=%ANDROID_HOME:\=\\%"

    for /d %%C in (LocojoyProject YijieProject JoysdkProject LocojoyProject_test2016) do (
        if exist "%%C" (
            if not exist "%%C\local.properties" (
                echo [INFO] Creating %%C\local.properties
                echo # Auto-generated by check_and_fix_paths.bat > "%%C\local.properties"
                echo sdk.dir=!SDK_DIR_ESCAPED! >> "%%C\local.properties"
                echo [OK] Created %%C\local.properties
            ) else (
                echo [OK] %%C\local.properties exists
            )
        )
    )
) else (
    echo [SKIP] Cannot create local.properties (ANDROID_HOME not set)
)
echo.

rem ============================================================================
rem Summary and Recommendations
rem ============================================================================
echo ========================================================================
echo Summary
echo ========================================================================
echo.

if "%ALL_OK%"=="1" (
    echo [SUCCESS] All required tools are properly configured!
    echo.
    echo You can now build the project:
    echo   cd E:\MT3\client\android
    echo   build_unified.bat --channel LocojoyProject
    echo.
    echo Or use the quick build script:
    echo   quick_build.bat --channel LocojoyProject
) else (
    echo [ERROR] Some tools are missing or misconfigured
    echo.

    if "%FIXES_NEEDED%"=="1" (
        echo Recommended Fixes:
        echo ------------------------------------------------------------------------
        echo 1. Run this script as Administrator to apply automatic fixes
        echo 2. Restart your command prompt after fixes
        echo 3. Or manually set the environment variables shown above
        echo.
        echo To set environment variables manually:
        echo   - Open: System Properties ^> Advanced ^> Environment Variables
        echo   - Add the variables shown above to System Variables
        echo   - Restart your command prompt
    )
)

echo.
echo ========================================================================
pause
exit /b 0
