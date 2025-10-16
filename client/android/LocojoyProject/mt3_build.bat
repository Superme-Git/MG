@echo off
setlocal EnableExtensions
chcp 65001 >nul
REM ===========================
REM NDK 构建入口（支持通过环境变量 NDK_HOME 指定路径）
REM 说明：
REM - 若未设置 NDK_HOME，则默认尝试 D:\android-ndk-r10e
REM - 构建输出将生成各 ABI 的本地库（见 [APP_ABI](client/android/LocojoyProject/jni/Application.mk:3)）
REM ===========================

REM 解析 NDK_HOME
if not defined NDK_HOME (
  set "NDK_HOME=D:\android-ndk-r10e"
)
REM 规范化 NDK_HOME，去除可能的尾随空格并转换为绝对路径
for %%i in ("%NDK_HOME%") do set "NDK_HOME=%%~fi"

if not exist "%NDK_HOME%\ndk-build.cmd" (
  echo [错误] 未找到 "%NDK_HOME%\ndk-build.cmd"
  echo 请设置环境变量 NDK_HOME 指向 Android NDK 根目录（例如 D:\android-ndk-r10e）
  goto :PAUSE_END
)

echo [NDK] 使用 NDK_HOME=%NDK_HOME%

REM 清理旧 obj
if exist ".\obj\local" rd /s /q ".\obj\local"

REM 执行构建（Release）
"%NDK_HOME%\ndk-build.cmd" NDK_DEBUG=0 -j4
set "RC=%ERRORLEVEL%"
if not "%RC%"=="0" (
  echo [错误] ndk-build 失败，错误码 %RC%
  goto :PAUSE_END
) else (
  echo [NDK] 本地库构建完成
)

:PAUSE_END
pause
exit /b %RC%