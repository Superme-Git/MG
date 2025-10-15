@echo off
setlocal EnableExtensions
chcp 65001 >nul
REM ===========================
REM LJFilePack - 获取版本号
REM 说明：自动切到脚本目录，定位可执行文件后调用
REM ===========================

pushd "%~dp0"

set "EXE=%~dp0LJFilePack.exe"
if not exist "%EXE%" (
  echo [错误] 未找到工具：%EXE%
  echo 请确认 LJFilePack.exe 与本 bat 位于同一目录。
  goto :PAUSE_END
)

"%EXE%" getversionnum
set "RC=%ERRORLEVEL%"
if not "%RC%"=="0" echo [错误] 获取版本号失败，错误码 %RC%.

popd
pause
exit /b %RC%

:PAUSE_END
pause
popd
exit /b 1