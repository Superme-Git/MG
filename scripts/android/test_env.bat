@echo off
setlocal enableextensions enabledelayedexpansion

echo ============================================================
echo MT3 Android Build Environment Quick Test
echo ============================================================
echo.

rem Test 1: NDK
echo [TEST 1] Android NDK r10e
set "NDK_FOUND=0"
if defined NDK_HOME (
    if exist "%NDK_HOME%\ndk-build.cmd" (
        echo   [OK] NDK_HOME = %NDK_HOME%
        set "NDK_FOUND=1"
    )
)
if "!NDK_FOUND!"=="0" (
    if exist "D:\android-ndk-r10e\ndk-build.cmd" (
        echo   [OK] Found at D:\android-ndk-r10e
        set "NDK_FOUND=1"
    )
)
if "!NDK_FOUND!"=="0" (
    echo   [FAIL] NDK r10e not found
    echo   [HINT] Install NDK r10e or set NDK_HOME
)
echo.

rem Test 2: Ant
echo [TEST 2] Apache Ant
where ant >nul 2>nul
if !errorlevel! == 0 (
    echo   [OK] Ant found in PATH
    for /f "delims=" %%A in ('ant -version 2^>^&1 ^| findstr "version"') do echo   %%A
) else (
    echo   [FAIL] Ant not found in PATH
    echo   [HINT] Download Ant from https://ant.apache.org/
)
echo.

rem Test 3: Java
echo [TEST 3] Java JDK
where java >nul 2>nul
if !errorlevel! == 0 (
    echo   [OK] Java found in PATH
    for /f "delims=" %%A in ('java -version 2^>^&1 ^| findstr "version"') do echo   %%A
    if defined JAVA_HOME (
        echo   [OK] JAVA_HOME = %JAVA_HOME%
    ) else (
        echo   [WARN] JAVA_HOME not set
    )
) else (
    echo   [FAIL] Java not found in PATH
    echo   [HINT] Install JDK 7 or 8
)
echo.

rem Test 4: Prebuilt libs
echo [TEST 4] Prebuilt third-party libraries
set "LIB_COUNT=0"
if exist "E:\MT3\dependencies\zlib\prebuilt\android\armeabi-v7a\libz.a" set /a "LIB_COUNT+=1"
if exist "E:\MT3\dependencies\jpeg\prebuilt\android\armeabi-v7a\libjpeg.a" set /a "LIB_COUNT+=1"
if exist "E:\MT3\dependencies\png\prebuilt\android\armeabi-v7a\libpng.a" set /a "LIB_COUNT+=1"

if !LIB_COUNT! == 3 (
    echo   [OK] All required libraries found ^(!LIB_COUNT!/3^)
) else (
    echo   [WARN] Only !LIB_COUNT!/3 libraries found
)
echo.

rem Test 5: Android project structure
echo [TEST 5] Android project structure
if exist "E:\MT3\client\android\LocojoyProject\jni\Android.mk" (
    echo   [OK] Android project found
    if exist "E:\MT3\client\android\LocojoyProject\mt3_build.bat" (
        echo   [OK] Build script found
    ) else (
        echo   [WARN] mt3_build.bat not found
    )
) else (
    echo   [FAIL] Android project not found
)
echo.

echo ============================================================
echo Test completed. Check results above.
echo ============================================================
echo.
echo Next steps:
echo   1. Fix any [FAIL] items
echo   2. Run: cd E:\MT3\client\android\LocojoyProject
echo   3. Run: mt3_build.bat
echo   4. Run: mt3_apk.bat
echo ============================================================

endlocal
pause
