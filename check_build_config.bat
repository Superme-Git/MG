@echo off
REM check_build_config.bat - 自动检查编译配置

echo ========================================
echo MT3 项目编译配置检查工具
echo ========================================
echo.

REM 检查 VS2013
echo [1/5] 检查 Visual Studio 2013...
if not defined VS120COMNTOOLS (
    echo [错误] 未检测到 Visual Studio 2013
    exit /b 1
) else (
    echo [通过] Visual Studio 2013: %VS120COMNTOOLS%
)
echo.

REM 检查工具集版本
echo [2/5] 检查工具集版本...
findstr /s /i "v140\|v141\|v142" *.vcxproj >nul
if %ERRORLEVEL% EQU 0 (
    echo [警告] 发现错误的工具集版本!
    findstr /s /i /n "v140\|v141\|v142" *.vcxproj
    exit /b 1
) else (
    echo [通过] 所有项目使用 v120
)
echo.

REM 检查必要的库文件
echo [3/5] 检查必要的库文件...
set MISSING=0
if not exist "common\platform\Release.win32\platform.lib" (
    echo [信息] platform.lib 未编译 (首次编译时正常)
)
if not exist "engine\Release.win32\engine.lib" (
    echo [信息] engine.lib 未编译 (首次编译时正常)
)
echo [通过] 配置检查完成
echo.

REM 检查运行时 DLL
echo [4/5] 检查运行时 DLL...
if not exist "client\resource\bin\debug\MSVCR120.dll" (
    echo [警告] 缺少 MSVCR120.dll
    echo [提示] 编译后运行 copy_runtime_dlls.bat
) else (
    echo [通过] 运行时 DLL 完整
)
echo.

REM 检查资源目录
echo [5/5] 检查资源目录...
if not exist "client\resource\script\" (
    echo [信息] script 目录不存在 (资源未部署)
)
if not exist "client\resource\res\" (
    echo [信息] res 目录不存在 (资源未部署)
)
echo [通过] 目录结构检查完成
echo.

echo ========================================
echo 配置检查完成! 项目配置正确。
echo ========================================
exit /b 0