@echo off
setlocal enableextensions enabledelayedexpansion
chcp 65001 >nul
rem ============================================================================
rem Android APK 打包脚本 - 增强版（含详细日志记录）
rem 功能：自动记录NDK构建 + Ant打包全过程日志，便于排错和追溯
rem 用法：双击运行或从命令行调用
rem ============================================================================

rem 生成时间戳日志文件名（支持自定义日志目录 MT3_LOG_DIR）
rem 计算脚本所在目录，作为项目根
set "SCRIPT_DIR=%~dp0"
rem 默认日志目录为项目根下的 build_logs，可通过 MT3_LOG_DIR 覆盖
set "LOG_DIR=%MT3_LOG_DIR%"
if not defined LOG_DIR set "LOG_DIR=%SCRIPT_DIR%build_logs"

rem 优先用 PowerShell 生成 yyyyMMdd_HHmmss，失败则回退到本地 date/time 解析
powershell -NoProfile -Command "$d=(Get-Date).ToString('yyyyMMdd_HHmmss'); Write-Output $d" > "%TEMP%\mt3_ts.txt" 2>nul
set /p "TS=" < "%TEMP%\mt3_ts.txt"
del "%TEMP%\mt3_ts.txt" 2>nul
if not defined TS (
    for /f "tokens=1-3 delims=/ " %%a in ('date /t') do set "DATESTAMP=%%c%%a%%b"
    for /f "tokens=1-2 delims=: " %%a in ('time /t') do set "TIMESTAMP=%%a%%b"
    set "TS=%DATESTAMP%_%TIMESTAMP%"
)
rem 去除 LOG_DIR 末尾空格
set "LOG_DIR_RAW=%LOG_DIR%"
:trim_logdir
if "%LOG_DIR_RAW:~-1%"==" " (
    set "LOG_DIR_RAW=%LOG_DIR_RAW:~0,-1%"
    goto :trim_logdir
)
set "LOG_DIR=%LOG_DIR_RAW%"

set "LOG_FILE=%LOG_DIR%\android_build_%TS%.log"

rem 确保日志目录存在
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

echo ============================================================ > "%LOG_FILE%"
echo Android APK 打包日志 >> "%LOG_FILE%"
echo 开始时间: %DATE% %TIME% >> "%LOG_FILE%"
echo ============================================================ >> "%LOG_FILE%"
echo. >> "%LOG_FILE%"

echo [INFO] 日志文件: %LOG_FILE%
echo [INFO] 开始打包流程...
echo.

rem ============================================================================
rem 第1步：环境变量检查与记录
rem ============================================================================
echo ============================================================ >> "%LOG_FILE%"
echo 第1步：环境变量检查 >> "%LOG_FILE%"
echo ============================================================ >> "%LOG_FILE%"

rem 若未显式设置 NDK_HOME，尝试使用常见默认路径作为回退
if not defined NDK_HOME (
    if exist "D:\android-ndk-r10e\ndk-build.cmd" (
        set "NDK_HOME=D:\android-ndk-r10e"
        echo [INFO] 未检测到NDK_HOME，使用回退路径: %NDK_HOME% >> "%LOG_FILE%"
    )
)

echo [环境变量] >> "%LOG_FILE%"
echo JAVA_HOME=%JAVA_HOME% >> "%LOG_FILE%"
echo ANDROID_HOME=%ANDROID_HOME% >> "%LOG_FILE%"
echo ANDROID_SDK_ROOT=%ANDROID_SDK_ROOT% >> "%LOG_FILE%"
echo NDK_HOME=%NDK_HOME% >> "%LOG_FILE%"
echo ANT_HOME=%ANT_HOME% >> "%LOG_FILE%"
echo. >> "%LOG_FILE%"

echo [工具版本检查] >> "%LOG_FILE%"
java -version 2>> "%LOG_FILE%"
echo. >> "%LOG_FILE%"
call ant -version >> "%LOG_FILE%" 2>&1
echo. >> "%LOG_FILE%"

echo [检查关键工具] >> "%LOG_FILE%"
where javac >> "%LOG_FILE%" 2>&1
where ant >> "%LOG_FILE%" 2>&1

rem 将 NDK_HOME 注入 PATH（若存在）
if defined NDK_HOME set "PATH=%NDK_HOME%;%PATH%"
echo PATH=%PATH% >> "%LOG_FILE%"
ver >> "%LOG_FILE%"
if exist "local.properties" (
    echo [local.properties] >> "%LOG_FILE%"
    type "local.properties" >> "%LOG_FILE%"
)
echo. >> "%LOG_FILE%"

echo [1/4] 环境检查完成
echo.

rem ============================================================================
rem 第2步：NDK Native 构建
rem ============================================================================
echo ============================================================ >> "%LOG_FILE%"
echo 第2步：NDK Native 构建 (libgame.so) >> "%LOG_FILE%"
echo ============================================================ >> "%LOG_FILE%"
echo 工作目录: %CD% >> "%LOG_FILE%"
echo 目标目录: %SCRIPT_DIR% >> "%LOG_FILE%"
echo. >> "%LOG_FILE%"

echo [2/4] 开始NDK构建...
cd /d "%SCRIPT_DIR%"

rem 可选：若外部已定义MT3_NO_PAUSE则脚本结尾将不暂停
if defined NDK_HOME set "PATH=%NDK_HOME%;%PATH%"

rem 再次输出有效的 NDK 路径以便诊断
echo [NDK_HOME=%NDK_HOME%] >> "%LOG_FILE%"

rem 执行NDK构建并记录输出
echo [NDK路径探测] >> "%LOG_FILE%"
where ndk-build.cmd >> "%LOG_FILE%" 2>&1
where ndk-build >> "%LOG_FILE%" 2>&1

echo [NDK构建输出] >> "%LOG_FILE%"
call mt3_build.bat >> "%LOG_FILE%" 2>&1

if errorlevel 1 (
    echo [ERROR] NDK构建失败! >> "%LOG_FILE%"
    echo [ERROR] NDK构建失败! 请查看日志: %LOG_FILE%
    goto :end_with_error
)

echo [SUCCESS] NDK构建成功 >> "%LOG_FILE%"
echo. >> "%LOG_FILE%"

rem 检查生成的so文件
echo [检查生成的Native库] >> "%LOG_FILE%"
if exist "libs\armeabi-v7a\libgame.so" (
    echo [OK] libs\armeabi-v7a\libgame.so 已生成 >> "%LOG_FILE%"
    dir "libs\armeabi-v7a\libgame.so" >> "%LOG_FILE%"
) else (
    echo [ERROR] libs\armeabi-v7a\libgame.so 未找到! >> "%LOG_FILE%"
    goto :end_with_error
)
echo. >> "%LOG_FILE%"

echo [2/4] NDK构建完成
echo.

rem ============================================================================
rem 第3步：Ant Java 编译与打包
rem ============================================================================
echo ============================================================ >> "%LOG_FILE%"
echo 第3步：Ant Java 编译与APK打包 >> "%LOG_FILE%"
echo ============================================================ >> "%LOG_FILE%"
echo 工作目录: %SCRIPT_DIR% >> "%LOG_FILE%"
echo 构建脚本: build.xml >> "%LOG_FILE%"
echo 目标任务: release >> "%LOG_FILE%"
echo. >> "%LOG_FILE%"

echo [3/4] 开始Ant打包...
cd /d "%SCRIPT_DIR%"

rem 执行Ant构建（verbose模式）并记录输出
echo [Ant构建输出] >> "%LOG_FILE%"
call ant -buildfile build.xml -v release >> "%LOG_FILE%" 2>&1

if errorlevel 1 (
    echo [ERROR] Ant打包失败! >> "%LOG_FILE%"
    echo [ERROR] Ant打包失败! 请查看日志: %LOG_FILE%
    goto :end_with_error
)

echo [SUCCESS] Ant打包成功 >> "%LOG_FILE%"
echo. >> "%LOG_FILE%"

echo [3/4] Ant打包完成
echo.

rem ============================================================================
rem 第4步：验证APK产物
rem ============================================================================
echo ============================================================ >> "%LOG_FILE%"
echo 第4步：验证APK产物 >> "%LOG_FILE%"
echo ============================================================ >> "%LOG_FILE%"

echo [4/4] 验证APK文件...

if exist "bin\mt3_locojoy.apk" (
    set "APK_FILE=bin\mt3_locojoy.apk"
    echo [OK] APK文件已生成: %APK_FILE% >> "%LOG_FILE%"
    dir "%APK_FILE%" >> "%LOG_FILE%"
    echo. >> "%LOG_FILE%"
) else (
    set "APK_DIR=bin"
    for /f "delims=" %%F in ('dir /b /a:-d /o:-d "%APK_DIR%\*.apk" 2^>nul') do (
        set "APK_FILE=%APK_DIR%\%%F"
        goto :found_apk
    )
    echo [ERROR] APK文件未找到! >> "%LOG_FILE%"
    goto :end_with_error
)
:found_apk
echo [APK基本信息] >> "%LOG_FILE%"
if exist "%ANDROID_HOME%\build-tools\22.0.1\aapt.exe" (
    "%ANDROID_HOME%\build-tools\22.0.1\aapt.exe" dump badging "%APK_FILE%" >> "%LOG_FILE%" 2>&1
) else if exist "C:\Progra~2\Android\android-sdk\build-tools\22.0.1\aapt.exe" (
    "C:\Progra~2\Android\android-sdk\build-tools\22.0.1\aapt.exe" dump badging "%APK_FILE%" >> "%LOG_FILE%" 2>&1
) else (
    for /f "delims=" %%A in ('dir /b /s "%ANDROID_HOME%\build-tools\*\aapt.exe" 2^>nul') do (
        set "AAPT=%%A"
        goto :have_aapt
    )
    for /f "delims=" %%A in ('dir /b /s "C:\Progra~2\Android\android-sdk\build-tools\*\aapt.exe" 2^>nul') do (
        set "AAPT=%%A"
        goto :have_aapt
    )
    echo [WARN] 未找到aapt.exe，跳过badging信息 >> "%LOG_FILE%"
    goto :after_aapt
)
:have_aapt
"%AAPT%" dump badging "%APK_FILE%" >> "%LOG_FILE%" 2>&1
:after_aapt
echo. >> "%LOG_FILE%"

echo [4/4] APK验证完成
echo.

rem ============================================================================
rem 构建成功总结
rem ============================================================================
echo ============================================================ >> "%LOG_FILE%"
echo 构建成功! >> "%LOG_FILE%"
echo 完成时间: %DATE% %TIME% >> "%LOG_FILE%"
echo ============================================================ >> "%LOG_FILE%"
echo. >> "%LOG_FILE%"
echo [产物位置] >> "%LOG_FILE%"
echo - Native库: %SCRIPT_DIR%libs\armeabi-v7a\libgame.so >> "%LOG_FILE%"
echo - APK文件: %SCRIPT_DIR%%APK_FILE% >> "%LOG_FILE%"
echo. >> "%LOG_FILE%"

echo ============================================================
echo 构建成功!
echo ============================================================
echo.
echo 产物位置:
echo - APK: %SCRIPT_DIR%%APK_FILE%
echo - 日志: %LOG_FILE%
echo.
echo 下一步操作:
echo 1. 安装APK: adb install -r "%APK_FILE%"
echo 2. 查看日志: type "%LOG_FILE%"
echo.
if not defined MT3_NO_PAUSE pause
exit /b 0

:end_with_error
echo. >> "%LOG_FILE%"
echo ============================================================ >> "%LOG_FILE%"
echo 构建失败! >> "%LOG_FILE%"
echo 失败时间: %DATE% %TIME% >> "%LOG_FILE%"
echo ============================================================ >> "%LOG_FILE%"
echo.
echo ============================================================
echo 构建失败! 请检查日志文件:
echo %LOG_FILE%
echo ============================================================
echo.
if not defined MT3_NO_PAUSE pause
exit /b 1
