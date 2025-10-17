@echo off
setlocal enableextensions enabledelayedexpansion

rem Clean previous build outputs (ignore errors)
rd /s /q ".\obj\local" 2>nul

rem Resolve NDK root from env vars or fallback to known path
set "NDK_CANDIDATE="
if defined NDK_HOME set "NDK_CANDIDATE=%NDK_HOME%"
if not defined NDK_CANDIDATE if defined ANDROID_NDK_HOME set "NDK_CANDIDATE=%ANDROID_NDK_HOME%"
if not defined NDK_CANDIDATE if defined ANDROID_NDK_ROOT set "NDK_CANDIDATE=%ANDROID_NDK_ROOT%"
if not defined NDK_CANDIDATE set "NDK_CANDIDATE=D:\android-ndk-r10e"

rem sanitize NDK path (remove embedded quotes, trim leading/trailing spaces)
set "NDK_CLEAN=!NDK_CANDIDATE:"=!"
for /f "tokens=* delims= " %%A in ("!NDK_CLEAN!") do set "NDK_CLEAN=%%A"
rem trim trailing spaces
set "NDK_TRIM=!NDK_CLEAN!"
:ndk_trim_tail
if "!NDK_TRIM:~-1!"==" " (
  set "NDK_TRIM=!NDK_TRIM:~0,-1!"
  goto :ndk_trim_tail
)
set "NDK_CLEAN=!NDK_TRIM!"

echo [NDK] Using NDK at: "!NDK_CLEAN!"

if defined NDK_CLEAN if exist "!NDK_CLEAN!\ndk-build.cmd" (
  call "!NDK_CLEAN!\ndk-build.cmd" NDK_DEBUG=0 -j4
  goto :done
) else (
  echo [WARN] "!NDK_CLEAN!\ndk-build.cmd" not found or NDK_CLEAN undefined. Falling back to PATH.
)

rem Try ndk-build from PATH as a fallback
where ndk-build.cmd >nul 2>nul && (
  call ndk-build.cmd NDK_DEBUG=0 -j4
  goto :done
)

where ndk-build >nul 2>nul && (
  call ndk-build NDK_DEBUG=0 -j4
  goto :done
)

rem Last-chance probe common install locations (non-recursive glob)
for /d %%D in (D:\android-ndk-* C:\android-ndk-* D:\Android\android-ndk-* C:\Android\android-ndk-*) do (
  if exist "%%D\ndk-build.cmd" (
    echo [INFO] Auto-detected NDK at: "%%D"
    set "NDK_CLEAN=%%D"
    call "%%D\ndk-build.cmd" NDK_DEBUG=0 -j4
    goto :done
  )
)

echo [ERROR] Could not find ndk-build(.cmd). Please install NDK r10e and ensure ndk-build is available.
echo [HINT] Set NDK_HOME to your NDK root (e.g. D:\android-ndk-r10e), or add ndk-build(.cmd) to PATH.
echo [HINT] PowerShell: $env:NDK_HOME="D:\android-ndk-r10e"
echo [HINT] CMD      : set "NDK_HOME=D:\android-ndk-r10e"
exit /b 1

:done
endlocal
if not defined MT3_NO_PAUSE pause