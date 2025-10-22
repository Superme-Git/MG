@echo off
setlocal enableextensions enabledelayedexpansion
chcp 65001 >nul
rem ============================================================================
rem MT3 APK 验证脚本
rem 功能：完整性检查、签名验证、内容分析
rem ============================================================================

if "%~1"=="" (
    echo 用法: verify_apk.bat ^<APK文件路径^>
    echo 示例: verify_apk.bat LocojoyProject\bin\mt3_locojoy.apk
    exit /b 1
)

set "APK_FILE=%~1"

if not exist "%APK_FILE%" (
    echo [ERROR] APK文件不存在: %APK_FILE%
    exit /b 1
)

echo ════════════════════════════════════════════════════════════════
echo APK 验证报告
echo ════════════════════════════════════════════════════════════════
echo APK路径: %APK_FILE%
echo.

rem 检查APK大小
for %%A in ("%APK_FILE%") do set "APK_SIZE=%%~zA"
set /a "APK_SIZE_MB=!APK_SIZE! / 1048576"
echo [INFO] APK大小: !APK_SIZE_MB! MB (!APK_SIZE! bytes)

if !APK_SIZE_MB! LSS 10 (
    echo [WARN] APK大小异常小（正常应为50-80MB）
)

echo.

rem 查找aapt工具
set "AAPT="
if defined ANDROID_HOME (
    for /f "delims=" %%A in ('dir /b /s "%ANDROID_HOME%\build-tools\*\aapt.exe" 2^>nul') do (
        set "AAPT=%%A"
        goto :found_aapt
    )
)

:found_aapt
if not defined AAPT (
    echo [WARN] aapt.exe 未找到，跳过详细分析
    echo [HINT] 请配置 ANDROID_HOME 环境变量
    goto :check_signature
)

echo [INFO] 使用工具: !AAPT!
echo.

rem APK基本信息
echo ────────────────────────────────────────────────────────────────
echo 包信息:
echo ────────────────────────────────────────────────────────────────
"!AAPT!" dump badging "%APK_FILE%" | findstr "package: sdkVersion: targetSdkVersion:"
echo.

rem 检查关键so库
echo ────────────────────────────────────────────────────────────────
echo Native库检查:
echo ────────────────────────────────────────────────────────────────

set "REQUIRED_LIBS=libgame.so locSDK6a.so libdu.so"
set "MISSING_LIBS="

for %%L in (%REQUIRED_LIBS%) do (
    "!AAPT!" list "%APK_FILE%" | findstr "%%L" >nul
    if errorlevel 1 (
        echo [ERROR] 缺失: %%L
        set "MISSING_LIBS=!MISSING_LIBS! %%L"
    ) else (
        echo [OK] 存在: %%L
    )
)

if defined MISSING_LIBS (
    echo.
    echo [ERROR] APK缺少必需的Native库:!MISSING_LIBS!
    set "VERIFICATION_FAILED=1"
)

echo.

rem 检查权限
echo ────────────────────────────────────────────────────────────────
echo 权限列表:
echo ────────────────────────────────────────────────────────────────
"!AAPT!" dump badging "%APK_FILE%" | findstr "uses-permission:"
echo.

:check_signature
rem 签名验证
echo ────────────────────────────────────────────────────────────────
echo 签名验证:
echo ────────────────────────────────────────────────────────────────

where jarsigner >nul 2>&1
if errorlevel 1 (
    echo [WARN] jarsigner 未找到，跳过签名验证
) else (
    jarsigner -verify -verbose -certs "%APK_FILE%" 2>&1 | findstr "jar verified"
    if errorlevel 1 (
        echo [WARN] APK签名验证失败或未签名
    ) else (
        echo [OK] APK签名有效
    )
)

echo.

rem 总结
echo ════════════════════════════════════════════════════════════════
if defined VERIFICATION_FAILED (
    echo [ERROR] APK验证失败
    echo ════════════════════════════════════════════════════════════
    exit /b 1
) else (
    echo [SUCCESS] APK验证通过
    echo ════════════════════════════════════════════════════════════
    exit /b 0
)
