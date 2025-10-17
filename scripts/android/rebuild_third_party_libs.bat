@echo off
setlocal enableextensions enabledelayedexpansion

rem ================================================================================
rem MT3 Android third-party static libs rebuild script (NDK r10e, armeabi-v7a)
rem Rebuilds zlib, libpng, libjpeg, libtiff, OpenSSL (crypto/ssl), curl and installs
rem to the project's expected prebuilt locations for linkage.
rem ================================================================================
rem Layout expectation:
rem   E:\MT3\third_party\zlib\jni\Android.mk               (module: zlib)
rem   E:\MT3\third_party\libpng\jni\Android.mk             (module: png)
rem   E:\MT3\third_party\libjpeg\jni\Android.mk            (module: jpeg)
rem   E:\MT3\third_party\tiff\jni\Android.mk               (module: tiff)
rem   E:\MT3\third_party\openssl\jni\Android.mk            (modules: crypto, ssl)
rem   E:\MT3\third_party\curl\jni\Android.mk               (module: curl)
rem
rem Output copy mapping:
rem   zlib    -> dependencies\zlib\prebuilt\android\armeabi-v7a\libz.a
rem   libpng  -> dependencies\png\prebuilt\android\armeabi-v7a\libpng.a
rem   libjpeg -> dependencies\jpeg\prebuilt\android\armeabi-v7a\libjpeg.a
rem   libtiff -> cocos2d-2.0-rc2-x-2.0.1\external\tiff\prebuilt\android\armeabi-v7a\libtiff.a
rem   openssl -> cocos2d-2.0-rc2-x-2.0.1\external\curl\prebuilt\android\armeabi-v7a\libcrypto.a / libssl.a
rem   curl    -> cocos2d-2.0-rc2-x-2.0.1\external\curl\prebuilt\android\armeabi-v7a\libcurl.a
rem ================================================================================

rem Resolve project root (two levels up from scripts\android\)
set "SCRIPT_DIR=%~dp0"
for %%A in ("%SCRIPT_DIR%..") do set "ROOT=%%~fA\.."
rem Normalize ROOT (ensure trailing backslash)
if not "%ROOT:~-1%"=="\" set "ROOT=%ROOT%\"

rem NDK path
if not defined NDK_HOME (
  if exist "D:\android-ndk-r10e\ndk-build.cmd" (
    set "NDK_HOME=D:\android-ndk-r10e"
  ) else (
    echo [ERROR] NDK_HOME 未设置且默认路径 D:\android-ndk-r10e 不存在
    echo [HINT ] 请先安装 NDK r10e 或设置环境变量：set NDK_HOME=D:\android-ndk-r10e
    exit /b 1
  )
)
set "NDK_BUILD=%NDK_HOME%\ndk-build.cmd"

if not exist "%NDK_BUILD%" (
  echo [ERROR] 未找到 ndk-build.cmd: "%NDK_BUILD%"
  exit /b 1
)

set "ABI=armeabi-v7a"
set "JOBS=4"

rem ----------------------------------------------------------------------------
rem Utilities
rem ----------------------------------------------------------------------------
set "LOG_DIR=%ROOT%build_logs"
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"
rem 使用简化的时间戳（避免 PowerShell 兼容性问题）
set "TS=%RANDOM%_%RANDOM%"
set "LOG_FILE=%LOG_DIR%\third_party_rebuild_%TS%.log"

echo ============================================================ > "%LOG_FILE%"
echo 第三方静态库重建日志 (NDK r10e, ABI=%ABI%) >> "%LOG_FILE%"
echo 开始时间: %DATE% %TIME% >> "%LOG_FILE%"
echo ROOT=%ROOT% >> "%LOG_FILE%"
echo NDK_HOME=%NDK_HOME% >> "%LOG_FILE%"
echo ============================================================ >> "%LOG_FILE%"
echo. >> "%LOG_FILE%"

rem :buildone & :copyone helpers
rem %1: friendly name, %2: module (output libname), %3: project subdir under third_party
:buildone
set "NAME=%~1"
set "MODULE=%~2"
set "SUBDIR=%~3"
set "TP_DIR=%ROOT%third_party\%SUBDIR%"
if not exist "%TP_DIR%\jni\Android.mk" (
  echo [SKIP] %NAME% 缺少 jni/Android.mk，目录: %TP_DIR% >> "%LOG_FILE%"
  goto :eof
)
echo [BUILD] %NAME% (%MODULE%) at %TP_DIR% >> "%LOG_FILE%"
pushd "%TP_DIR%"
call "%NDK_BUILD%" NDK_DEBUG=0 -j%JOBS% >> "%LOG_FILE%" 2>&1
if errorlevel 1 (
  echo [ERROR] 构建 %NAME% 失败 >> "%LOG_FILE%"
  popd
  goto :eof
)
popd
echo [OK] %NAME% 构建完成 >> "%LOG_FILE%"
goto :eof

rem %1: friendly name, %2: built lib path (relative from third_party subdir), %3: destination absolute path
:copyone
set "C_NAME=%~1"
set "SRC_REL=%~2"
set "DST_ABS=%~3"
rem Resolve absolute source using prior SUBDIR context if present
set "SRC_ABS=%SRC_REL%"
if not exist "%SRC_ABS%" (
  rem try common obj path: third_party\SUBDIR\obj\local\ABI\lib*.a
  echo [WARN] 源文件不存在: %SRC_ABS% >> "%LOG_FILE%"
  goto :eof
)
for %%D in ("%DST_ABS%") do set "DST_DIR=%%~dpD"
if not exist "%DST_DIR%" mkdir "%DST_DIR%"
copy /y "%SRC_ABS%" "%DST_ABS%" >nul
if errorlevel 1 (
  echo [ERROR] 复制 %C_NAME% 失败: "%SRC_ABS%" -^> "%DST_ABS%" >> "%LOG_FILE%"
) else (
  echo [INSTALL] %C_NAME% -^> %DST_ABS% >> "%LOG_FILE%"
)
goto :eof

rem ----------------------------------------------------------------------------
rem Build sequence
rem ----------------------------------------------------------------------------
set "OBJ_BASE=%ROOT%third_party"

rem zlib
call :buildone "zlib" "zlib" "zlib"
set "Z_SRC=%OBJ_BASE%\zlib\obj\local\%ABI%\libz.a"
set "Z_DST=%ROOT%dependencies\zlib\prebuilt\android\%ABI%\libz.a"
call :copyone "zlib" "%Z_SRC%" "%Z_DST%"

rem libpng
call :buildone "libpng" "png" "libpng"
set "PNG_SRC=%OBJ_BASE%\libpng\obj\local\%ABI%\libpng.a"
set "PNG_DST=%ROOT%dependencies\png\prebuilt\android\%ABI%\libpng.a"
call :copyone "libpng" "%PNG_SRC%" "%PNG_DST%"

rem libjpeg
call :buildone "libjpeg" "jpeg" "libjpeg"
set "JPEG_SRC=%OBJ_BASE%\libjpeg\obj\local\%ABI%\libjpeg.a"
set "JPEG_DST=%ROOT%dependencies\jpeg\prebuilt\android\%ABI%\libjpeg.a"
call :copyone "libjpeg" "%JPEG_SRC%" "%JPEG_DST%"

rem libtiff
call :buildone "libtiff" "tiff" "tiff"
set "TIFF_SRC=%OBJ_BASE%\tiff\obj\local\%ABI%\libtiff.a"
set "TIFF_DST=%ROOT%cocos2d-2.0-rc2-x-2.0.1\external\tiff\prebuilt\android\%ABI%\libtiff.a"
call :copyone "libtiff" "%TIFF_SRC%" "%TIFF_DST%"

rem OpenSSL (crypto/ssl)
call :buildone "OpenSSL(crypto)" "crypto" "openssl"
set "CRYPTO_SRC=%OBJ_BASE%\openssl\obj\local\%ABI%\libcrypto.a"
set "CRYPTO_DST=%ROOT%cocos2d-2.0-rc2-x-2.0.1\external\curl\prebuilt\android\%ABI%\libcrypto.a"
call :copyone "libcrypto" "%CRYPTO_SRC%" "%CRYPTO_DST%"

call :buildone "OpenSSL(ssl)" "ssl" "openssl"
set "SSL_SRC=%OBJ_BASE%\openssl\obj\local\%ABI%\libssl.a"
set "SSL_DST=%ROOT%cocos2d-2.0-rc2-x-2.0.1\external\curl\prebuilt\android\%ABI%\libssl.a"
call :copyone "libssl" "%SSL_SRC%" "%SSL_DST%"

rem curl
call :buildone "curl" "curl" "curl"
set "CURL_SRC=%OBJ_BASE%\curl\obj\local\%ABI%\libcurl.a"
set "CURL_DST=%ROOT%cocos2d-2.0-rc2-x-2.0.1\external\curl\prebuilt\android\%ABI%\libcurl.a"
call :copyone "libcurl" "%CURL_SRC%" "%CURL_DST%"

echo. >> "%LOG_FILE%"
echo ============================================================ >> "%LOG_FILE%"
echo 第三方库重建流程完成 >> "%LOG_FILE%"
echo 完成时间: %DATE% %TIME% >> "%LOG_FILE%"
echo ============================================================ >> "%LOG_FILE%"
echo.
echo ============================================================
echo 第三方库重建流程完成
echo 日志: %LOG_FILE%
echo 预编译库已安装到项目预期路径（如 dependencies\*\prebuilt\android\%ABI% 或 cocos2d\external\*\prebuilt\android\%ABI%）
echo ============================================================

endlocal
exit /b 0