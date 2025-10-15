@echo off
setlocal EnableExtensions
chcp 65001 >nul
REM ===========================
REM LJFilePack - 资源 Unpack
REM 用途：对指定包进行 unpack
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
  echo [示例] %~nx0 ..\res\pack.ljpk
  set "_PAUSE=1"
  goto :END
)

"%EXE%" unpack:"%~1"
set "RC=%ERRORLEVEL%"
if not "%RC%"=="0" echo [错误] 解包失败，错误码 %RC%.

:END
popd
if defined _PAUSE pause
exit /b %RC%

:PAUSE_END
pause
popd
exit /b 1