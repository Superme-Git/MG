@echo off
setlocal EnableExtensions
chcp 65001 >nul
REM ===========================
REM LJFilePack - LJPI -> XML 转换
REM 用途：将 .ljpi 转为 .xml
REM 注意：
REM 1) 已切换到脚本所在目录运行（支持路径含空格）
REM 2) 自动定位 LJFilePack.exe
REM 3) 双击无参数会显示用法并暂停，便于查看错误信息
REM ===========================

pushd "%~dp0"

set "EXE=%~dp0LJFilePack.exe"
if not exist "%EXE%" (
  echo [错误] 未找到工具：%EXE%
  echo 请确认 LJFilePack.exe 与本 bat 位于同一目录。
  goto :PAUSE_END
)

if "%~1"=="" (
  echo [用法] %~nx0 ^<输入文件或目录^>
  echo [示例] %~nx0 ..\res\fl.ljpi
  set "_PAUSE=1"
  goto :END
)

REM 关键调用：若工具/资源在其他磁盘或路径含空格，均已加引号保护
"%EXE%" ljpi2xml:"%~1"
set "RC=%ERRORLEVEL%"
if not "%RC%"=="0" echo [错误] 转换失败，错误码 %RC%.

:END
popd
if defined _PAUSE pause
exit /b %RC%

:PAUSE_END
pause
popd
exit /b 1