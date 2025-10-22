@echo off
setlocal enableextensions enabledelayedexpansion
rem ============================================================================
rem MT3 Android Quick Build Script
rem Purpose: Simplified build with automatic path detection and fixes
rem Author: Build Engineering Team
rem Date: 2025-10-23
rem ============================================================================

echo.
echo ========================================================================
echo MT3 Android Quick Build Tool v1.0
echo ========================================================================
echo.

rem ============================================================================
rem Auto-detect and set NDK_HOME
rem ============================================================================
echo [1/5] Detecting Build Tools...
echo.

rem Check and set NDK_HOME
if not defined NDK_HOME (
    echo [INFO] NDK_HOME not set, auto-detecting...

    if exist "D:\android-ndk-r10e\ndk-build.cmd" (
        set "NDK_HOME=D:\android-ndk-r10e"
        echo [OK] NDK found: !NDK_HOME!
    ) else if exist "C:\android-ndk-r10e\ndk-build.cmd" (
        set "NDK_HOME=C:\android-ndk-r10e"
        echo [OK] NDK found: !NDK_HOME!
    ) else (
        echo [ERROR] NDK r10e not found!
        echo [HINT] Please install NDK r10e to D:\android-ndk-r10e
        pause
        exit /b 1
    )
) else (
    echo [OK] NDK_HOME: %NDK_HOME%
)

rem Verify JAVA_HOME
if not defined JAVA_HOME (
    echo [WARN] JAVA_HOME not set
    where java >nul 2>&1
    if errorlevel 1 (
        echo [ERROR] Java not found in PATH
        pause
        exit /b 1
    ) else (
        echo [OK] Java found in PATH
    )
) else (
    echo [OK] JAVA_HOME: %JAVA_HOME%
)

rem Verify ANT
where ant >nul 2>&1
if errorlevel 1 (
    if defined ANT_HOME (
        echo [OK] ANT_HOME: %ANT_HOME%
    ) else (
        echo [ERROR] Apache Ant not found
        pause
        exit /b 1
    )
) else (
    echo [OK] Apache Ant found in PATH
)

rem Verify ANDROID_HOME
if not defined ANDROID_HOME (
    if defined ANDROID_SDK_ROOT (
        set "ANDROID_HOME=%ANDROID_SDK_ROOT%"
        echo [OK] Using ANDROID_SDK_ROOT: %ANDROID_HOME%
    ) else (
        echo [ERROR] ANDROID_HOME not set
        pause
        exit /b 1
    )
) else (
    echo [OK] ANDROID_HOME: %ANDROID_HOME%
)

echo.
echo [2/5] Environment Check Passed
echo.

rem ============================================================================
rem Parse Arguments
rem ============================================================================
set "BUILD_CHANNEL=LocojoyProject"
set "CLEAN_BUILD=0"

:parse_args
if "%~1"=="" goto :args_done
if /i "%~1"=="--channel" (
    set "BUILD_CHANNEL=%~2"
    shift
    shift
    goto :parse_args
)
if /i "%~1"=="-c" (
    set "BUILD_CHANNEL=%~2"
    shift
    shift
    goto :parse_args
)
if /i "%~1"=="--clean" (
    set "CLEAN_BUILD=1"
    shift
    goto :parse_args
)
shift
goto :parse_args

:args_done

echo [3/5] Build Configuration
echo   Target Channel: %BUILD_CHANNEL%
echo   Clean Build: %CLEAN_BUILD%
echo.

rem ============================================================================
rem Verify Channel Exists
rem ============================================================================
if not exist "%BUILD_CHANNEL%\build_with_log.bat" (
    echo [ERROR] Channel not found: %BUILD_CHANNEL%
    echo.
    echo Available channels:
    for /d %%D in (*Project*) do (
        if exist "%%D\build_with_log.bat" (
            echo   - %%D
        )
    )
    pause
    exit /b 1
)

cd /d "%BUILD_CHANNEL%"

rem ============================================================================
rem Clean Build (Optional)
rem ============================================================================
if "%CLEAN_BUILD%"=="1" (
    echo [4/5] Cleaning old build artifacts...
    rd /s /q obj 2>nul
    rd /s /q libs 2>nul
    rd /s /q bin 2>nul
    rd /s /q gen 2>nul
    echo [OK] Clean completed
    echo.
) else (
    echo [4/5] Skipping clean (use --clean to enable)
    echo.
)

rem ============================================================================
rem Build
rem ============================================================================
echo [5/5] Building APK...
echo ========================================================================
echo.

call build_with_log.bat

if errorlevel 1 (
    echo.
    echo ========================================================================
    echo [ERROR] Build Failed!
    echo ========================================================================
    echo.
    echo Check logs at:
    echo   %CD%\build_logs\
    echo.
    cd ..
    pause
    exit /b 1
)

echo.
echo ========================================================================
echo [SUCCESS] Build Completed!
echo ========================================================================
echo.

rem ============================================================================
rem Find and display APK info
rem ============================================================================
set "APK_FILE="
for /f "delims=" %%F in ('dir /b /a:-d /o:-d "bin\*.apk" 2^>nul') do (
    set "APK_FILE=bin\%%F"
    goto :found_apk
)

:found_apk
if defined APK_FILE (
    echo APK Location: %CD%\!APK_FILE!

    for %%A in ("!APK_FILE!") do set "APK_SIZE=%%~zA"
    set /a "APK_SIZE_MB=!APK_SIZE! / 1048576"
    echo APK Size: !APK_SIZE_MB! MB
    echo.

    echo Next Steps:
    echo   1. Install on device: adb install -r "!APK_FILE!"
    echo   2. View logs: type build_logs\android_build_*.log
) else (
    echo [WARN] APK file not found in bin directory
)

echo.
cd ..
pause
exit /b 0
