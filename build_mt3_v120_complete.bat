@echo off
setlocal enabledelayedexpansion

REM ===================================================================
REM MT3项目 v120 (VS2013) 完整编译脚本
REM 
REM 用途: 使用v120工具集完整编译MT3项目及其所有依赖库
REM 适用于: Visual Studio 2013 (v120) 或已安装v140工具集的VS2015+
REM 
REM 使用方法:
REM   build_mt3_v120_complete.bat
REM 
REM 注意事项:
REM   1. 需要预先安装Visual Studio 2013或Visual Studio 2015+
REM   2. 如果使用VS2015+，需要安装v120工具集
REM   3. 编译过程约需要30-40分钟
REM   4. 建议先运行编译前检查清单
REM ===================================================================

echo.
echo ========================================
echo MT3项目 v120 (VS2013) 完整编译脚本
echo ========================================
echo.

REM 设置环境变量
set "PROJECT_ROOT=%~dp0"
set "MSBUILD_PATH=C:\Program Files (x86)\MSBuild\12.0\Bin\MSBuild.exe"
set "VSWHERE_PATH=C:\Program Files (x86)\Microsoft Visual Studio\Installer\vswhere.exe"

REM 检查MSBuild路径
if not exist "%MSBUILD_PATH%" (
    echo 尝试查找VS2015+的MSBuild...
    if exist "%VSWHERE_PATH%" (
        for /f "usebackq tokens=*" %%i in (`"%VSWHERE_PATH%" -latest -products * -requires Microsoft.VisualStudio.Component.VC.Tools.x86.x64 -property installationPath`) do (
            set "VS_INSTALL_DIR=%%i"
        )
        set "MSBUILD_PATH=!VS_INSTALL_DIR!\MSBuild\15.0\Bin\MSBuild.exe"
        if not exist "!MSBUILD_PATH!" (
            set "MSBUILD_PATH=!VS_INSTALL_DIR!\MSBuild\Current\Bin\MSBuild.exe"
        )
    )
)

if not exist "%MSBUILD_PATH%" (
    echo 错误: 找不到MSBuild.exe
    echo 请确保已安装Visual Studio 2013或更高版本
    pause
    exit /b 1
)

echo 使用MSBuild: %MSBUILD_PATH%
echo.

REM 检查项目文件是否存在
if not exist "%PROJECT_ROOT%client\FireClient\FireClient.sln" (
    echo 错误: 找不到主解决方案文件
    echo 期望路径: %PROJECT_ROOT%client\FireClient\FireClient.sln
    pause
    exit /b 1
)

echo 开始编译MT3项目...
echo.

REM 记录开始时间
set "START_TIME=%date% %time%"

REM 第一步: 编译基础库
echo [1/7] 编译基础库...
echo.

REM 1.1 编译liblua
echo [1.1/7] 编译liblua...
"%MSBUILD_PATH%" "%PROJECT_ROOT%common\lua\lua.win32.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: liblua编译失败
    pause
    exit /b 1
)

REM 1.2 编译platform
echo [1.2/7] 编译platform...
"%MSBUILD_PATH%" "%PROJECT_ROOT%common\platform\platform.win32.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: platform编译失败
    pause
    exit /b 1
)

REM 1.3 编译ljfm
echo [1.3/7] 编译ljfm...
"%MSBUILD_PATH%" "%PROJECT_ROOT%common\ljfm\ljfm.win32.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: ljfm编译失败
    pause
    exit /b 1
)

REM 1.4 编译cauthc
echo [1.4/7] 编译cauthc...
"%MSBUILD_PATH%" "%PROJECT_ROOT%common\cauthc\projects\windows\cauthc.win32.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: cauthc编译失败
    pause
    exit /b 1
)

REM 第二步: 编译UI和图形库
echo [2/7] 编译UI和图形库...
echo.

REM 2.1 编译pcre
echo [2.1/7] 编译pcre...
"%MSBUILD_PATH%" "%PROJECT_ROOT%dependencies\pcre-8.31\pcre-8.31.win32.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: pcre编译失败
    pause
    exit /b 1
)

REM 2.2 编译SILLY
echo [2.2/7] 编译SILLY...
"%MSBUILD_PATH%" "%PROJECT_ROOT%dependencies\SILLY-0.1.0\SILLY-0.1.0.win32.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: SILLY编译失败
    pause
    exit /b 1
)

REM 2.3 编译cegui
echo [2.3/7] 编译cegui...
"%MSBUILD_PATH%" "%PROJECT_ROOT%dependencies\cegui\project\win32\cegui.win32.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: cegui编译失败
    pause
    exit /b 1
)

REM 第三步: 编译游戏引擎
echo [3/7] 编译游戏引擎...
echo.

REM 3.1 编译Cocos2d-x
echo [3.1/7] 编译Cocos2d-x...
"%MSBUILD_PATH%" "%PROJECT_ROOT%cocos2d-2.0-rc2-x-2.0.1\cocos2dx\proj.win32\cocos2d-win32.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: Cocos2d-x编译失败
    pause
    exit /b 1
)

REM 3.2 编译CocosDenshion
echo [3.2/7] 编译CocosDenshion...
"%MSBUILD_PATH%" "%PROJECT_ROOT%cocos2d-2.0-rc2-x-2.0.1\CocosDenshion\proj.win32\CocosDenshion.win32.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: CocosDenshion编译失败
    pause
    exit /b 1
)

REM 3.3 编译libSpine
echo [3.3/7] 编译libSpine...
"%MSBUILD_PATH%" "%PROJECT_ROOT%cocos2d-2.0-rc2-x-2.0.1\extensions\spine\proj.win32\libSpine.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: libSpine编译失败
    pause
    exit /b 1
)

REM 第四步: 编译游戏引擎扩展
echo [4/7] 编译游戏引擎扩展...
echo.

REM 4.1 编译engine
echo [4.1/7] 编译engine...
"%MSBUILD_PATH%" "%PROJECT_ROOT%engine\engine.win32.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: engine编译失败
    pause
    exit /b 1
)

REM 第五步: 编译客户端库
echo [5/7] 编译客户端库...
echo.

REM 5.1 编译FireClient
echo [5.1/7] 编译FireClient...
"%MSBUILD_PATH%" "%PROJECT_ROOT%client\FireClient\FireClient.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: FireClient编译失败
    pause
    exit /b 1
)

REM 第六步: 编译主应用程序
echo [6/7] 编译主应用程序...
echo.

REM 6.1 编译MT3.exe
echo [6.1/7] 编译MT3.exe...
"%MSBUILD_PATH%" "%PROJECT_ROOT%client\MT3Win32App\mt3.win32.vcxproj" /t:Rebuild /p:Configuration=Release /p:Platform=Win32 /p:PlatformToolset=v120 /m /nologo /v:minimal
if !errorlevel! neq 0 (
    echo 错误: MT3.exe编译失败
    pause
    exit /b 1
)

REM 第七步: 复制运行时文件
echo [7/7] 复制运行时文件...
echo.

REM 7.1 复制运行时DLL
echo [7.1/7] 复制运行时DLL...
if exist "%PROJECT_ROOT%copy_runtime_dlls.bat" (
    call "%PROJECT_ROOT%copy_runtime_dlls.bat"
) else (
    echo 警告: 找不到copy_runtime_dlls.bat脚本，跳过DLL复制
)

REM 记录结束时间
set "END_TIME=%date% %time%"

REM 编译完成
echo.
echo ========================================
echo 编译完成!
echo ========================================
echo.
echo 开始时间: %START_TIME%
echo 结束时间: %END_TIME%
echo.
echo 输出文件:
echo   MT3.exe: %PROJECT_ROOT%client\MT3Win32App\Release.win32\MT3.exe
echo   FireClient.lib: %PROJECT_ROOT%client\FireClient\Release.win32\FireClient.lib
echo   engine.lib: %PROJECT_ROOT%engine\Release.win32\engine.lib
echo   libcocos2d.lib: %PROJECT_ROOT%cocos2d-2.0-rc2-x-2.0.1\Release.win32\libcocos2d.lib
echo.
echo 如果编译成功，可以运行以下命令启动游戏:
echo   cd "%PROJECT_ROOT%client\MT3Win32App\Release.win32"
echo   MT3.exe
echo.

pause