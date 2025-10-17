@echo off
setlocal EnableExtensions EnableDelayedExpansion
rem Force ASCII-safe output (avoid Unicode BOM issues in cmd)
chcp 65001 >nul

rem ========================================
rem Android Build Environment Check (v7a only)
rem Matches [build.xml](client/android/LocojoyProject/build/build.xml) and [mt3_build.bat](client/android/LocojoyProject/mt3_build.bat)
rem ========================================

echo === Android Build Env Check (v7a only) ===
echo.

set MISS=0
set WARN=0

echo --- Environment Variables ---
echo JAVA_HOME=%JAVA_HOME%
echo ANDROID_HOME=%ANDROID_HOME%
echo ANDROID_SDK_ROOT=%ANDROID_SDK_ROOT%
echo NDK_HOME=%NDK_HOME%
echo.

rem 1) JDK check: javac / jarsigner
echo [1/7] Check JDK...
if not defined JAVA_HOME (
  echo [MISS] JAVA_HOME is not set
  set MISS=1
) else (
  if exist "%JAVA_HOME%\bin\javac.exe" (
    echo [OK] javac: "%JAVA_HOME%\bin\javac.exe"
    for /f "tokens=* delims=" %%V in ('"%JAVA_HOME%\bin\javac.exe" -version 2^>^&1') do echo [JDK] %%V
  ) else (
    echo [MISS] javac not found: "%JAVA_HOME%\bin\javac.exe"
    set MISS=1
  )
  if exist "%JAVA_HOME%\bin\jarsigner.exe" (
    echo [OK] jarsigner: "%JAVA_HOME%\bin\jarsigner.exe"
  ) else (
    echo [MISS] jarsigner not found: "%JAVA_HOME%\bin\jarsigner.exe"
    set MISS=1
  )
)
echo.

rem 2) Ant check
echo [2/7] Check Ant...
where ant >nul 2>nul
if errorlevel 1 (
  echo [MISS] ant not in PATH or cannot execute
  set MISS=1
) else (
  for /f "tokens=* delims=" %%A in ('ant -version 2^>^&1') do echo [ANT] %%A
)
echo.

rem 3) SDK check (Build-Tools=22.0.1, Platform=android-22)
echo [3/7] Check Android SDK...
set "SDK_CANDIDATE="
if defined ANDROID_HOME set "SDK_CANDIDATE=%ANDROID_HOME%"
if not defined SDK_CANDIDATE if defined ANDROID_SDK_ROOT set "SDK_CANDIDATE=%ANDROID_SDK_ROOT%"
if not defined SDK_CANDIDATE if exist "C:\Program Files (x86)\Android\android-sdk" set "SDK_CANDIDATE=C:\Program Files (x86)\Android\android-sdk"
if not defined SDK_CANDIDATE if exist "D:\android-sdk_r24.1.2-windows\android-sdk-windows" set "SDK_CANDIDATE=D:\android-sdk_r24.1.2-windows\android-sdk-windows"

if not defined SDK_CANDIDATE (
  echo [MISS] Android SDK root not detected (ANDROID_HOME / ANDROID_SDK_ROOT not set and common paths missing)
  set MISS=1
) else (
  echo [SDK] Root: "%SDK_CANDIDATE%"
  if exist "%SDK_CANDIDATE%\build-tools\22.0.1\aapt.exe" (
    echo [OK] aapt 22.0.1: "%SDK_CANDIDATE%\build-tools\22.0.1\aapt.exe"
  ) else (
    echo [MISS] aapt 22.0.1 missing: "%SDK_CANDIDATE%\build-tools\22.0.1\aapt.exe"
    set MISS=1
  )
  if exist "%SDK_CANDIDATE%\build-tools\22.0.1\dx.bat" (
    echo [OK] dx 22.0.1: "%SDK_CANDIDATE%\build-tools\22.0.1\dx.bat"
  ) else (
    echo [MISS] dx 22.0.1 missing: "%SDK_CANDIDATE%\build-tools\22.0.1\dx.bat"
    set MISS=1
  )
  if exist "%SDK_CANDIDATE%\platforms\android-22\android.jar" (
    echo [OK] android-22 android.jar: "%SDK_CANDIDATE%\platforms\android-22\android.jar"
  ) else (
    echo [MISS] android-22 android.jar missing: "%SDK_CANDIDATE%\platforms\android-22\android.jar"
    set MISS=1
  )
)
echo.

rem 4) NDK check
echo [4/7] Check NDK...
if not defined NDK_HOME (
  echo [MISS] NDK_HOME is not set (should point to r10e, e.g. D:\android-ndk-r10e)
  set MISS=1
) else (
  if exist "%NDK_HOME%\ndk-build.cmd" (
    echo [OK] ndk-build: "%NDK_HOME%\ndk-build.cmd"
  ) else (
    echo [MISS] ndk-build not found: "%NDK_HOME%\ndk-build.cmd"
    set MISS=1
  )
)
echo.

rem 5) Keystore check (signing)
echo [5/7] Check Keystore...
if exist "client\android\LocojoyProject\build\mt3.keystore" (
  echo [OK] keystore: "client\android\LocojoyProject\build\mt3.keystore"
) else (
  echo [MISS] keystore missing: "client\android\LocojoyProject\build\mt3.keystore"
  set MISS=1
)
echo.

rem 6) Project config snapshot & consistency (v7a only)
echo [6/7] Project config snapshot and consistency...
if exist "client\android\LocojoyProject\jni\Application.mk" (
  echo [SNAPSHOT] client\android\LocojoyProject\jni\Application.mk
  echo    APP_ABI line:
  type "client\android\LocojoyProject\jni\Application.mk" | findstr /R /C:"APP_ABI"
  echo    APP_PLATFORM line:
  type "client\android\LocojoyProject\jni\Application.mk" | findstr /R /C:"APP_PLATFORM"
  echo    NDK_TOOLCHAIN_VERSION line:
  type "client\android\LocojoyProject\jni\Application.mk" | findstr /R /C:"NDK_TOOLCHAIN_VERSION"

  rem v7a-only strict checks
  type "client\android\LocojoyProject\jni\Application.mk" | findstr /R /C:"APP_ABI *:= *armeabi-v7a" >nul
  if errorlevel 1 (
    echo [MISS] APP_ABI is not v7a (please set: APP_ABI := armeabi-v7a)
    set MISS=1
  ) else (
    echo [OK] APP_ABI = armeabi-v7a
  )
  type "client\android\LocojoyProject\jni\Application.mk" | findstr /R /C:"APP_PLATFORM *:= *android-11" >nul
  if errorlevel 1 (
    echo [WARN] APP_PLATFORM is not android-11 (recommended to match manifest minSdkVersion 11)
    set WARN=1
  ) else (
    echo [OK] APP_PLATFORM = android-11
  )
) else (
  echo [MISS] Application.mk not found
  set MISS=1
)
echo.

rem 7) Script expectations (align with build.xml)
echo [7/7] Script expectations (v7a only)...
echo [build.xml] Uses ANDROID_HOME / ANDROID_SDK_ROOT; fallback: D:\android-sdk_r24.1.2-windows\android-sdk-windows
echo [build.xml] Requires Build-Tools=22.0.1 and Platform=android-22 (verified above)
echo [mt3_build.bat] Needs NDK_HOME=r10e and ndk-build.cmd; builds with NDK_DEBUG=0 -j4
echo [v7a only] Do NOT add any arm64-v8a build/copy steps (project does not support 64-bit)
echo.

echo === Summary ===
if %MISS%==0 (
  echo [PASS] Environment is ready for v7a build
  if %WARN%==1 echo [WARN] Non-blocking recommendations detected
  exit /b 0
) else (
  echo [FAIL] Missing items detected. Please fix the [MISS] entries above and retry.
  exit /b 1
)