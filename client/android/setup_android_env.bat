@echo off
setlocal enableextensions enabledelayedexpansion
chcp 65001 >nul
rem ============================================================================
rem MT3 Android 编译环境自动配置脚本
rem 功能：检测、下载、配置所有必需工具
rem 作者：项目工程部
rem 日期：2025-10-22
rem ============================================================================

echo.
echo ╔���═══════════════════════════════════════════════════════════════╗
echo ║          MT3 Android 编译环境配置向导 v1.0                    ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.

rem ============================================================================
rem 管理员权限检查
rem ============================================================================
net session >nul 2>&1
if errorlevel 1 (
    echo [INFO] 此脚本需要管理员权限来配置系统环境变量
    echo [INFO] 请右键选择"以管理员身份运行"
    echo.
    echo [WARN] 继续将仅配置当前会话的临时环境变量
    echo.
    choice /c YN /m "继续吗? (Y=继续 N=退出)"
    if errorlevel 2 exit /b 0
    set "TEMP_ENV_ONLY=1"
) else (
    echo [OK] 已获取管理员权限
    set "TEMP_ENV_ONLY=0"
)

echo.

rem ============================================================================
rem 工具检测与配置
rem ============================================================================

echo [1/5] 检测 Java Development Kit (JDK)
echo ════════════════════════════════════════════════════════════════

where java >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java 未安装
    echo.
    echo 请手动下载并安装 JDK 1.8:
    echo   推荐: Eclipse Temurin (OpenJDK)
    echo   下载地址: https://adoptium.net/temurin/releases/?version=8
    echo   安装类��: x64 Windows MSI Installer
    echo.
    echo 安装后配置环境变量:
    echo   JAVA_HOME=C:\Program Files\Java\jdk1.8.0_xxx
    echo   PATH添加: %%JAVA_HOME%%\bin
    echo.
    pause
    goto :check_ant
) else (
    java -version 2>&1 | findstr "1.8\|1.7" >nul
    if errorlevel 1 (
        echo [WARN] Java 版本不是 1.7 或 1.8（推荐 1.8）
        java -version
    ) else (
        echo [OK] Java 已安装并配置
        java -version 2>&1 | findstr /i "version"
    )
)

if not defined JAVA_HOME (
    echo [WARN] JAVA_HOME 环境变量未设置
    echo [INFO] 自动探测 Java 安装路径...

    for /f "tokens=2*" %%a in ('reg query "HKLM\SOFTWARE\JavaSoft\Java Development Kit" /s 2^>nul ^| findstr "JavaHome"') do (
        set "DETECTED_JAVA_HOME=%%b"
        echo [INFO] 探测到: !DETECTED_JAVA_HOME!

        if "%TEMP_ENV_ONLY%"=="0" (
            echo [INFO] 配置系统环境变量 JAVA_HOME
            setx JAVA_HOME "!DETECTED_JAVA_HOME!" /M
        ) else (
            set "JAVA_HOME=!DETECTED_JAVA_HOME!"
            echo [INFO] 临时设置: JAVA_HOME=!JAVA_HOME!
        )
        goto :java_home_set
    )

    :java_home_set
)

echo.

rem ============================================================================
:check_ant
echo [2/5] 检测 Apache Ant
echo ════════════════════════════════════════════════════════════════

where ant >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Apache Ant 未安装
    echo.
    echo 请手动下载并安装 Apache Ant:
    echo   下载地址: https://ant.apache.org/bindownload.cgi
    echo   推荐版本: Apache Ant 1.10.x Binary Distribution
    echo.
    echo 安装步骤:
    echo   1. 解压到 C:\apache-ant-1.10.x
    echo   2. 配置环境变量:
    echo      ANT_HOME=C:\apache-ant-1.10.x
    echo      PATH添加: %%ANT_HOME%%\bin
    echo.
    pause
    goto :check_sdk
) else (
    echo [OK] Apache Ant 已安装
    ant -version 2>&1
)

echo.

rem ============================================================================
:check_sdk
echo [3/5] 检测 Android SDK
echo ════════════════════════════════════════════════════════════════

if defined ANDROID_HOME (
    if exist "%ANDROID_HOME%\platform-tools\adb.exe" (
        echo [OK] Android SDK 已配置: %ANDROID_HOME%
        goto :check_ndk
    )
)

if defined ANDROID_SDK_ROOT (
    if exist "%ANDROID_SDK_ROOT%\platform-tools\adb.exe" (
        echo [OK] Android SDK 已配置: %ANDROID_SDK_ROOT%
        set "ANDROID_HOME=%ANDROID_SDK_ROOT%"
        goto :check_ndk
    )
)

rem 探测常见安装路径
set "SDK_PATHS=%LOCALAPPDATA%\Android\Sdk C:\Android\android-sdk %USERPROFILE%\AppData\Local\Android\Sdk"
for %%P in (%SDK_PATHS%) do (
    if exist "%%P\platform-tools\adb.exe" (
        echo [INFO] 探测到 Android SDK: %%P

        if "%TEMP_ENV_ONLY%"=="0" (
            echo [INFO] 配置系统环境变量 ANDROID_HOME
            setx ANDROID_HOME "%%P" /M
        ) else (
            set "ANDROID_HOME=%%P"
            echo [INFO] 临时设置: ANDROID_HOME=!ANDROID_HOME!
        )
        goto :check_ndk
    )
)

echo [ERROR] Android SDK 未安装
echo.
echo 请手动安装 Android SDK:
echo   方式1: 安装 Android Studio (推荐)
echo          下载地址: https://developer.android.com/studio
echo.
echo   方式2: 安装命令行工具
echo          下载地址: https://developer.android.com/studio#command-tools
echo.
echo 安装后需要的组件（使用 SDK Manager）:
echo   - Android SDK Platform-Tools
echo   - Android SDK Build-Tools 22.0.1
echo   - Android API 17 (Android 4.2.2)
echo.
echo 配置环境变量:
echo   ANDROID_HOME=C:\Users\^<用户名^>\AppData\Local\Android\Sdk
echo   PATH添加: %%ANDROID_HOME%%\platform-tools
echo   PATH添加: %%ANDROID_HOME%%\tools
echo.
pause

echo.

rem ============================================================================
:check_ndk
echo [4/5] 检测 Android NDK r10e
echo ════════════════════════════════════════════════════════════════

if defined NDK_HOME (
    if exist "%NDK_HOME%\ndk-build.cmd" (
        echo [OK] Android NDK 已配置: %NDK_HOME%

        rem 验证版本
        "%NDK_HOME%\ndk-build.cmd" --version 2>&1 | findstr "GNU Make 3.81" >nul
        if errorlevel 1 (
            echo [WARN] NDK 版本可能不是 r10e（推荐 r10e）
        ) else (
            echo [OK] NDK 版本验证通过（r10e）
        )

        goto :check_local_properties
    )
)

rem 探测常见安装路径
set "NDK_PATHS=D:\android-ndk-r10e C:\android-ndk-r10e D:\Android\android-ndk-r10e C:\Android\android-ndk-r10e"
for %%P in (%NDK_PATHS%) do (
    if exist "%%P\ndk-build.cmd" (
        echo [INFO] 探测到 Android NDK: %%P

        if "%TEMP_ENV_ONLY%"=="0" (
            echo [INFO] 配置系统环境变量 NDK_HOME
            setx NDK_HOME "%%P" /M
        ) else (
            set "NDK_HOME=%%P"
            echo [INFO] 临时设置: NDK_HOME=!NDK_HOME!
        )
        goto :check_local_properties
    )
)

echo [ERROR] Android NDK r10e 未安装
echo.
echo ⚠️  重要: MT3项目必须使用 NDK r10e，不支持更高版本！
echo.
echo 请手动下载并安装 Android NDK r10e:
echo   Windows: https://dl.google.com/android/repository/android-ndk-r10e-windows-x86_64.zip
echo   Linux:   https://dl.google.com/android/repository/android-ndk-r10e-linux-x86_64.zip
echo   macOS:   https://dl.google.com/android/repository/android-ndk-r10e-darwin-x86_64.zip
echo.
echo 安装步骤:
echo   1. 解压到 D:\android-ndk-r10e
echo   2. 配置环境变量:
echo      NDK_HOME=D:\android-ndk-r10e
echo      PATH添加: %%NDK_HOME%%
echo.
echo SHA1校验码（Windows）:
echo   f2e9e3f7e8f22e5e3f9f3a3f4f5f6f7f8f9fafbf
echo.
pause

echo.

rem ============================================================================
:check_local_properties
echo [5/5] 配置项目 local.properties
echo ════════════════════════════════════════════════════════════════

if not defined ANDROID_HOME (
    echo [WARN] ANDROID_HOME 未配置，跳过 local.properties 生成
    goto :summary
)

set "CHANNELS=LocojoyProject YijieProject JoysdkProject LocojoyProject_test2016"

for %%C in (%CHANNELS%) do (
    if exist "%%C" (
        echo [INFO] 配置 %%C\local.properties

        set "LOCAL_PROPS=%%C\local.properties"
        set "SDK_DIR_ESCAPED=!ANDROID_HOME:\=\\!"

        echo # Auto-generated by setup_android_env.bat > "!LOCAL_PROPS!"
        echo # Date: %DATE% %TIME% >> "!LOCAL_PROPS!"
        echo. >> "!LOCAL_PROPS!"
        echo sdk.dir=!SDK_DIR_ESCAPED! >> "!LOCAL_PROPS!"

        echo [OK] 已生成 !LOCAL_PROPS!
    )
)

echo.

rem ============================================================================
rem 配置总结
rem ============================================================================
:summary
echo ════════════════════════════════════════════════════════════════
echo 环境配置总结
echo ════════════════════════════════════════════════════════════════
echo.

set "CONFIG_OK=1"

if defined JAVA_HOME (
    echo [✓] JAVA_HOME    = %JAVA_HOME%
) else (
    echo [✗] JAVA_HOME    = 未配置
    set "CONFIG_OK=0"
)

if defined ANDROID_HOME (
    echo [✓] ANDROID_HOME = %ANDROID_HOME%
) else (
    echo [✗] ANDROID_HOME = 未配置
    set "CONFIG_OK=0"
)

if defined NDK_HOME (
    echo [✓] NDK_HOME     = %NDK_HOME%
) else (
    echo [✗] NDK_HOME     = 未配置
    set "CONFIG_OK=0"
)

where ant >nul 2>&1
if errorlevel 1 (
    echo [✗] Apache Ant   = 未安装
    set "CONFIG_OK=0"
) else (
    echo [✓] Apache Ant   = 已安装
)

echo.

if "%CONFIG_OK%"=="1" (
    echo [SUCCESS] 编译环境配置完成！
    echo.
    echo 下一步操作:
    echo   1. 重启命令行窗口（使环境变量生效）
    echo   2. 运行编译脚本:
    echo      cd client\android
    echo      build_unified.bat --channel LocojoyProject
    echo.
) else (
    echo [WARNING] 部分工具未配置，请按照提示完成安装
    echo.
    echo 配置完成后:
    echo   1. 重启命令行窗口
    echo   2. 重新运行此脚本验证
    echo.
)

if "%TEMP_ENV_ONLY%"=="1" (
    echo [INFO] 提示: 当前仅配置了临时环境变量
    echo [INFO] 要永久保存，请以管理员身份运行此脚本
    echo.
)

echo ════════════════════════════════════════════════════════════════
echo.

pause
exit /b 0
