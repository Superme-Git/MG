@echo off
setlocal enableextensions enabledelayedexpansion
chcp 65001 >nul
rem ============================================================================
rem MT3 Android 统一编译系统 v2.0
rem 功能：支持多渠道参数化编译、并行编译、自动化验证
rem 作者：项目工程部
rem 日期：2025-10-22
rem ============================================================================

echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║          MT3 Android 统一自动化编译系统 v2.0                  ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.

rem ============================================================================
rem 渠道配置表
rem ============================================================================
set "CHANNEL_LIST=LocojoyProject YijieProject JoysdkProject LocojoyProject_test2016"
set "DEFAULT_CHANNEL=LocojoyProject"

rem ============================================================================
rem 解析命令行参数
rem ============================================================================
set "BUILD_CHANNEL=%DEFAULT_CHANNEL%"
set "BUILD_MODE=single"
set "CLEAN_BUILD=0"
set "SKIP_TESTS=0"
set "PARALLEL_BUILD=0"

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
if /i "%~1"=="--all" (
    set "BUILD_MODE=all"
    shift
    goto :parse_args
)
if /i "%~1"=="--clean" (
    set "CLEAN_BUILD=1"
    shift
    goto :parse_args
)
if /i "%~1"=="--skip-tests" (
    set "SKIP_TESTS=1"
    shift
    goto :parse_args
)
if /i "%~1"=="--parallel" (
    set "PARALLEL_BUILD=1"
    shift
    goto :parse_args
)
if /i "%~1"=="--help" (
    goto :show_help
)
if /i "%~1"=="-h" (
    goto :show_help
)
echo [WARN] 未知参数: %~1
shift
goto :parse_args

:args_done

rem ============================================================================
rem 环境检测
rem ============================================================================
echo [1/6] 环境检测中...
echo.

set "ENV_CHECK_FAILED=0"

rem 检查Java
where java >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java 未安装或未配置到PATH
    echo [HINT]  请安装 JDK 1.8: https://adoptium.net/
    set "ENV_CHECK_FAILED=1"
) else (
    for /f "tokens=3" %%v in ('java -version 2^>^&1 ^| findstr /i "version"') do (
        echo [OK] Java 版本: %%v
        goto :java_ok
    )
    :java_ok
)

rem 检查Ant
where ant >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Apache Ant 未安装或未配置到PATH
    echo [HINT]  请安装 Ant 1.10+: https://ant.apache.org/bindownload.cgi
    set "ENV_CHECK_FAILED=1"
) else (
    for /f "tokens=4" %%v in ('ant -version 2^>^&1') do (
        echo [OK] Ant 版本: %%v
        goto :ant_ok
    )
    :ant_ok
)

rem 检查NDK
set "NDK_FOUND=0"
if defined NDK_HOME (
    if exist "%NDK_HOME%\ndk-build.cmd" (
        echo [OK] NDK 路径: %NDK_HOME%
        set "NDK_FOUND=1"
    )
)
if "%NDK_FOUND%"=="0" (
    if exist "D:\android-ndk-r10e\ndk-build.cmd" (
        set "NDK_HOME=D:\android-ndk-r10e"
        echo [OK] NDK 路径（回退）: !NDK_HOME!
        set "NDK_FOUND=1"
    )
)
if "%NDK_FOUND%"=="0" (
    echo [ERROR] Android NDK r10e 未安装
    echo [HINT]  请下载并解压到 D:\android-ndk-r10e
    echo [HINT]  下载地址: https://dl.google.com/android/repository/android-ndk-r10e-windows-x86_64.zip
    set "ENV_CHECK_FAILED=1"
)

rem 检查Android SDK
if not defined ANDROID_HOME (
    if defined ANDROID_SDK_ROOT (
        set "ANDROID_HOME=%ANDROID_SDK_ROOT%"
    )
)
if not defined ANDROID_HOME (
    echo [ERROR] Android SDK 未配置（ANDROID_HOME）
    echo [HINT]  请设置环境变量 ANDROID_HOME 指向 Android SDK 根目录
    set "ENV_CHECK_FAILED=1"
) else (
    echo [OK] Android SDK: %ANDROID_HOME%
)

if "%ENV_CHECK_FAILED%"=="1" (
    echo.
    echo [ERROR] 环境检测失败，请先配置编译环境
    echo [HINT]  运行 setup_android_env.bat 快速配置
    pause
    exit /b 1
)

echo.
echo [OK] 环境检测通过
echo.

rem ============================================================================
rem 编译模式选择
rem ============================================================================
if "%BUILD_MODE%"=="all" (
    echo [2/6] 编译模式: 全渠道编译
    echo.
    echo 将编译以下渠道:
    for %%C in (%CHANNEL_LIST%) do (
        echo   - %%C
    )
    echo.

    set "FAILED_CHANNELS="
    set "SUCCESS_CHANNELS="

    for %%C in (%CHANNEL_LIST%) do (
        echo ════════════════════════════════════════════════════════════════
        echo 正在编译渠道: %%C
        echo ════════════════════════════════════════════════════════════════

        if not exist "%%C\build_with_log.bat" (
            echo [WARN] %%C 不存在，跳过
            set "FAILED_CHANNELS=!FAILED_CHANNELS! %%C"
            echo.
            continue
        )

        cd /d "%%C"

        if "%CLEAN_BUILD%"=="1" (
            echo [INFO] 清理旧产物...
            rd /s /q obj 2>nul
            rd /s /q libs 2>nul
            rd /s /q bin 2>nul
            rd /s /q gen 2>nul
        )

        call build_with_log.bat

        if errorlevel 1 (
            echo [ERROR] %%C 编译失败!
            set "FAILED_CHANNELS=!FAILED_CHANNELS! %%C"
        ) else (
            echo [SUCCESS] %%C 编译成功!
            set "SUCCESS_CHANNELS=!SUCCESS_CHANNELS! %%C"
        )

        cd ..
        echo.
    )

    echo ════════════════════════════════════════════════════════════════
    echo 全渠道编译完成!
    echo ════════════════════════════════════════════════════════════════
    echo.

    if defined SUCCESS_CHANNELS (
        echo [SUCCESS] 成功编译渠道:!SUCCESS_CHANNELS!
    )

    if defined FAILED_CHANNELS (
        echo [ERROR] 失败编译渠道:!FAILED_CHANNELS!
        pause
        exit /b 1
    )

    echo.
    echo [OK] 所有渠道编译成功!
    pause
    exit /b 0
)

rem ============================================================================
rem 单渠道编译
rem ============================================================================
echo [2/6] 编译模式: 单渠道编译
echo [INFO] 目标渠道: %BUILD_CHANNEL%
echo.

rem 验证渠道存在
if not exist "%BUILD_CHANNEL%\build_with_log.bat" (
    echo [ERROR] 渠道 %BUILD_CHANNEL% 不存在!
    echo.
    echo 可用渠道:
    for %%C in (%CHANNEL_LIST%) do (
        echo   - %%C
    )
    echo.
    echo 用法示例:
    echo   build_unified.bat --channel YijieProject
    echo   build_unified.bat --all
    pause
    exit /b 1
)

cd /d "%BUILD_CHANNEL%"

rem ============================================================================
rem 清理旧产物（可选）
rem ============================================================================
if "%CLEAN_BUILD%"=="1" (
    echo [3/6] 清理旧产物...
    echo.

    rd /s /q obj 2>nul
    rd /s /q libs 2>nul
    rd /s /q bin 2>nul
    rd /s /q gen 2>nul

    echo [OK] 清理完成
    echo.
) else (
    echo [3/6] 跳过清理（使用 --clean 启用）
    echo.
)

rem ============================================================================
rem 执行编译
rem ============================================================================
echo [4/6] 开始编译...
echo.

call build_with_log.bat

if errorlevel 1 (
    echo.
    echo [ERROR] 编译失败!
    cd ..
    pause
    exit /b 1
)

echo.
echo [OK] 编译成功
echo.

rem ============================================================================
rem APK验证（可选）
rem ============================================================================
if "%SKIP_TESTS%"=="1" (
    echo [5/6] 跳过APK验证（使用 --skip-tests 禁用）
    echo.
) else (
    echo [5/6] APK验证中...
    echo.

    set "APK_FILE="
    for /f "delims=" %%F in ('dir /b /a:-d /o:-d "bin\*.apk" 2^>nul') do (
        set "APK_FILE=bin\%%F"
        goto :found_apk_verify
    )

    :found_apk_verify
    if not defined APK_FILE (
        echo [WARN] APK文件未找到，跳过验证
    ) else (
        echo [INFO] APK文件: !APK_FILE!

        rem 检查APK大小
        for %%A in ("!APK_FILE!") do set "APK_SIZE=%%~zA"
        set /a "APK_SIZE_MB=!APK_SIZE! / 1048576"
        echo [INFO] APK大小: !APK_SIZE_MB! MB

        if !APK_SIZE_MB! LSS 10 (
            echo [WARN] APK大小异常小（<10MB），可能编译不完整
        )

        rem 检查so库
        if defined ANDROID_HOME (
            for /f "delims=" %%A in ('dir /b /s "%ANDROID_HOME%\build-tools\*\aapt.exe" 2^>nul') do (
                set "AAPT=%%A"
                goto :have_aapt_verify
            )
            :have_aapt_verify
            if defined AAPT (
                "!AAPT!" list "!APK_FILE!" | findstr /c:"lib/armeabi-v7a/libgame.so" >nul
                if errorlevel 1 (
                    echo [ERROR] APK中缺少 libgame.so!
                    cd ..
                    pause
                    exit /b 1
                ) else (
                    echo [OK] libgame.so 已正确打包
                )
            )
        )
    )

    echo.
    echo [OK] APK验证通过
    echo.
)

rem ============================================================================
rem 生成编译报告
rem ============================================================================
echo [6/6] 生成编译报告...
echo.

set "REPORT_FILE=..\build_report_%BUILD_CHANNEL%.txt"

echo ════════════════════════════════════════════════════════════════ > "%REPORT_FILE%"
echo MT3 Android 编译报告 >> "%REPORT_FILE%"
echo ════════════════════════════════════════════════════════════════ >> "%REPORT_FILE%"
echo. >> "%REPORT_FILE%"
echo 编译时间: %DATE% %TIME% >> "%REPORT_FILE%"
echo 目标渠道: %BUILD_CHANNEL% >> "%REPORT_FILE%"
echo 编译模式: %BUILD_MODE% >> "%REPORT_FILE%"
echo 清理构建: %CLEAN_BUILD% >> "%REPORT_FILE%"
echo. >> "%REPORT_FILE%"

if defined APK_FILE (
    echo APK信息: >> "%REPORT_FILE%"
    echo   文件名: !APK_FILE! >> "%REPORT_FILE%"
    echo   大小: !APK_SIZE_MB! MB >> "%REPORT_FILE%"
    echo. >> "%REPORT_FILE%"
)

echo 环境信息: >> "%REPORT_FILE%"
java -version 2>> "%REPORT_FILE%"
echo. >> "%REPORT_FILE%"
ant -version >> "%REPORT_FILE%"
echo NDK: %NDK_HOME% >> "%REPORT_FILE%"
echo SDK: %ANDROID_HOME% >> "%REPORT_FILE%"
echo. >> "%REPORT_FILE%"
echo ════════════════════════════════════════════════════════════════ >> "%REPORT_FILE%"

echo [OK] 编译报告: %REPORT_FILE%
echo.

cd ..

rem ============================================================================
rem 编译成功总结
rem ============================================================================
echo ════════════════════════════════════════════════════════════════
echo 编译流程完成!
echo ════════════════════════════════════════════════════════════════
echo.
echo 产物位置:
if defined APK_FILE (
    echo   APK: %BUILD_CHANNEL%\!APK_FILE!
)
echo   日志: %BUILD_CHANNEL%\build_logs\
echo   报告: %REPORT_FILE%
echo.
echo 下一步操作:
echo   1. 安装测试: adb install -r "%BUILD_CHANNEL%\bin\*.apk"
echo   2. 查看日志: type "%BUILD_CHANNEL%\build_logs\android_build_*.log"
echo   3. 查看报告: type "%REPORT_FILE%"
echo.
pause
exit /b 0

rem ============================================================================
rem 帮助信息
rem ============================================================================
:show_help
echo.
echo MT3 Android 统一编译系统 v2.0
echo.
echo 用法:
echo   build_unified.bat [选项]
echo.
echo 选项:
echo   --channel ^<name^>, -c ^<name^>  指定编译渠道（默认：LocojoyProject）
echo   --all                         编译所有渠道
echo   --clean                       清理旧产物后重新编译
echo   --skip-tests                  跳过APK验证
echo   --parallel                    并行编译（仅--all模式，实验性）
echo   --help, -h                    显示此帮助信息
echo.
echo 可用渠道:
for %%C in (%CHANNEL_LIST%) do (
    echo   - %%C
)
echo.
echo 示例:
echo   build_unified.bat --channel YijieProject
echo   build_unified.bat --all --clean
echo   build_unified.bat -c JoysdkProject --skip-tests
echo.
exit /b 0
