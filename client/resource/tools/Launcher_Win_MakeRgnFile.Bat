@echo off
setlocal EnableExtensions
chcp 65001 >nul
REM ===========================
REM Launcher - 生成 RGN 区域文件
REM 用途：从指定 PNG 生成 .rgn 掩码
REM 注意：参数中的竖线 | 需使用 ^| 转义
REM ===========================

pushd "%~dp0"

set "LAUNCHER=..\Launcher.exe"
if not exist "%LAUNCHER%" (
  echo [错误] 未找到启动器：%LAUNCHER%
  echo 请确认路径正确（相对脚本上级目录）。
  goto :PAUSE_END
)

set "ARGS=designmode makergnfile:bin/release/LauncherImageBG.png^|bin/release/LauncherImageBG.rgn^|10^|20^|10"
start "" /d ".." "Launcher.exe" %ARGS%
set "RC=%ERRORLEVEL%"

popd
pause
exit /b %RC%

:PAUSE_END
pause
popd
exit /b 1