@echo off
REM 复制运行时DLL到MT3.exe目录
REM 用于解决运行时缺少DLL的问题

setlocal enabledelayedexpansion

echo ========================================
echo MT3 运行时DLL复制脚本
echo ========================================

REM 设置项目根目录
set PROJECT_ROOT=%~dp0
set MT3_EXE_DIR=%PROJECT_ROOT%client\MT3Win32App\bin\win32
set DEPS_DIR=%PROJECT_ROOT%dependencies

REM 检查MT3.exe目录是否存在
if not exist "%MT3_EXE_DIR%" (
    echo 错误: 找不到MT3.exe目录: %MT3_EXE_DIR%
    echo 请先编译MT3项目
    pause
    exit /b 1
)

REM 检查依赖库目录是否存在
if not exist "%DEPS_DIR%" (
    echo 错误: 找不到依赖库目录: %DEPS_DIR%
    pause
    exit /b 1
)

echo 目标目录: %MT3_EXE_DIR%
echo 依赖库目录: %DEPS_DIR%

REM 需要复制的DLL列表
set DLL_COUNT=0

REM 1. Visual C++ Redistributable DLLs
echo.
echo [1/9] 复制Visual C++ Redistributable DLLs...
set "VCREDIST_PATH=C:\Windows\System32"
if exist "%VCREDIST_PATH%\msvcp140.dll" (
    copy "%VCREDIST_PATH%\msvcp140.dll" "%MT3_EXE_DIR%\" >nul
    if !errorlevel! equ 0 (
        echo    已复制: msvcp140.dll
        set /a DLL_COUNT+=1
    ) else (
        echo    警告: 无法复制msvcp140.dll
    )
) else (
    echo    警告: 找不到msvcp140.dll，请安装Visual C++ Redistributable 2015
)

if exist "%VCREDIST_PATH%\vcruntime140.dll" (
    copy "%VCREDIST_PATH%\vcruntime140.dll" "%MT3_EXE_DIR%\" >nul
    if !errorlevel! equ 0 (
        echo    已复制: vcruntime140.dll
        set /a DLL_COUNT+=1
    ) else (
        echo    警告: 无法复制vcruntime140.dll
    )
) else (
    echo    警告: 找不到vcruntime140.dll，请安装Visual C++ Redistributable 2015
)

REM 2. Cocos2d-x相关DLL
echo.
echo [2/9] 复制Cocos2d-x相关DLLs...
set "COCOS_DLL_DIR=%PROJECT_ROOT%cocos2d-2.0-rc2-x-2.0.1\prebuilt\win32"
if exist "%COCOS_DLL_DIR%" (
    if exist "%COCOS_DLL_DIR%\libcocos2d.dll" (
        copy "%COCOS_DLL_DIR%\libcocos2d.dll" "%MT3_EXE_DIR%\" >nul
        if !errorlevel! equ 0 (
            echo    已复制: libcocos2d.dll
            set /a DLL_COUNT+=1
        ) else (
            echo    警告: 无法复制libcocos2d.dll
        )
    )
    
    if exist "%COCOS_DLL_DIR%\libCocosDenshion.dll" (
        copy "%COCOS_DLL_DIR%\libCocosDenshion.dll" "%MT3_EXE_DIR%\" >nul
        if !errorlevel! equ 0 (
            echo    已复制: libCocosDenshion.dll
            set /a DLL_COUNT+=1
        ) else (
            echo    警告: 无法复制libCocosDenshion.dll
        )
    )
) else (
    echo    警告: 找不到Cocos2d-x DLL目录: %COCOS_DLL_DIR%
)

REM 3. CEGUI相关DLL
echo.
echo [3/9] 复制CEGUI相关DLLs...
set "CEGUI_DLL_DIR=%PROJECT_ROOT%dependencies\cegui\bin"
if exist "%CEGUI_DLL_DIR%" (
    if exist "%CEGUI_DLL_DIR%\CEGUIBase.dll" (
        copy "%CEGUI_DLL_DIR%\CEGUIBase.dll" "%MT3_EXE_DIR%\" >nul
        if !errorlevel! equ 0 (
            echo    已复制: CEGUIBase.dll
            set /a DLL_COUNT+=1
        ) else (
            echo    警告: 无法复制CEGUIBase.dll
        )
    )
) else (
    echo    警告: 找不到CEGUI DLL目录: %CEGUI_DLL_DIR%
)

REM 4. FreeType相关DLL
echo.
echo [4/9] 复制FreeType相关DLLs...
set "FREETYPE_DLL_DIR=%PROJECT_ROOT%dependencies\freetype\bin"
if exist "%FREETYPE_DLL_DIR%" (
    if exist "%FREETYPE_DLL_DIR%\freetype.dll" (
        copy "%FREETYPE_DLL_DIR%\freetype.dll" "%MT3_EXE_DIR%\" >nul
        if !errorlevel! equ 0 (
            echo    已复制: freetype.dll
            set /a DLL_COUNT+=1
        ) else (
            echo    警告: 无法复制freetype.dll
        )
    )
) else (
    echo    警告: 找不到FreeType DLL目录: %FREETYPE_DLL_DIR%
)

REM 5. PNG相关DLL
echo.
echo [5/9] 复制PNG相关DLLs...
set "PNG_DLL_DIR=%PROJECT_ROOT%dependencies\png\bin"
if exist "%PNG_DLL_DIR%" (
    if exist "%PNG_DLL_DIR%\libpng.dll" (
        copy "%PNG_DLL_DIR%\libpng.dll" "%MT3_EXE_DIR%\" >nul
        if !errorlevel! equ 0 (
            echo    已复制: libpng.dll
            set /a DLL_COUNT+=1
        ) else (
            echo    警告: 无法复制libpng.dll
        )
    )
) else (
    echo    警告: 找不到PNG DLL目录: %PNG_DLL_DIR%
)

REM 6. JPEG相关DLL
echo.
echo [6/9] 复制JPEG相关DLLs...
set "JPEG_DLL_DIR=%PROJECT_ROOT%dependencies\jpeg\bin"
if exist "%JPEG_DLL_DIR%" (
    if exist "%JPEG_DLL_DIR%\jpeg.dll" (
        copy "%JPEG_DLL_DIR%\jpeg.dll" "%MT3_EXE_DIR%\" >nul
        if !errorlevel! equ 0 (
            echo    已复制: jpeg.dll
            set /a DLL_COUNT+=1
        ) else (
            echo    警告: 无法复制jpeg.dll
        )
    )
) else (
    echo    警告: 找不到JPEG DLL目录: %JPEG_DLL_DIR%
)

REM 7. ZLIB相关DLL
echo.
echo [7/9] 复制ZLIB相关DLLs...
set "ZLIB_DLL_DIR=%PROJECT_ROOT%dependencies\zlib\bin"
if exist "%ZLIB_DLL_DIR%" (
    if exist "%ZLIB_DLL_DIR%\zlib.dll" (
        copy "%ZLIB_DLL_DIR%\zlib.dll" "%MT3_EXE_DIR%\" >nul
        if !errorlevel! equ 0 (
            echo    已复制: zlib.dll
            set /a DLL_COUNT+=1
        ) else (
            echo    警告: 无法复制zlib.dll
        )
    )
) else (
    echo    警告: 找不到ZLIB DLL目录: %ZLIB_DLL_DIR%
)

REM 8. OGG/VORBIS相关DLL
echo.
echo [8/9] 复制OGG/VORBIS相关DLLs...
set "OGG_DLL_DIR=%PROJECT_ROOT%dependencies\ogg\bin"
if exist "%OGG_DLL_DIR%" (
    if exist "%OGG_DLL_DIR%\libogg.dll" (
        copy "%OGG_DLL_DIR%\libogg.dll" "%MT3_EXE_DIR%\" >nul
        if !errorlevel! equ 0 (
            echo    已复制: libogg.dll
            set /a DLL_COUNT+=1
        ) else (
            echo    警告: 无法复制libogg.dll
        )
    )
) else (
    echo    警告: 找不到OGG DLL目录: %OGG_DLL_DIR%
)

set "VORBIS_DLL_DIR=%PROJECT_ROOT%dependencies\vorbis\bin"
if exist "%VORBIS_DLL_DIR%" (
    if exist "%VORBIS_DLL_DIR%\libvorbis.dll" (
        copy "%VORBIS_DLL_DIR%\libvorbis.dll" "%MT3_EXE_DIR%\" >nul
        if !errorlevel! equ 0 (
            echo    已复制: libvorbis.dll
            set /a DLL_COUNT+=1
        ) else (
            echo    警告: 无法复制libvorbis.dll
        )
    )
) else (
    echo    警告: 找不到VORBIS DLL目录: %VORBIS_DLL_DIR%
)

REM 9. LUA相关DLL
echo.
echo [9/9] 复制LUA相关DLLs...
set "LUA_DLL_DIR=%PROJECT_ROOT%common\lua\bin"
if exist "%LUA_DLL_DIR%" (
    if exist "%LUA_DLL_DIR%\lua.dll" (
        copy "%LUA_DLL_DIR%\lua.dll" "%MT3_EXE_DIR%\" >nul
        if !errorlevel! equ 0 (
            echo    已复制: lua.dll
            set /a DLL_COUNT+=1
        ) else (
            echo    警告: 无法复制lua.dll
        )
    )
) else (
    echo    警告: 找不到LUA DLL目录: %LUA_DLL_DIR%
)

REM 显示复制结果
echo.
echo ========================================
echo 复制完成
echo ========================================
echo 已复制 %DLL_COUNT% 个DLL文件到: %MT3_EXE_DIR%

REM 检查MT3.exe是否存在
if exist "%MT3_EXE_DIR%\MT3.exe" (
    echo.
    echo MT3.exe已就绪，可以运行游戏
    echo.
    echo 如果仍然遇到DLL缺失问题，请：
    echo 1. 安装Visual C++ Redistributable 2015 (x86)
    echo 2. 检查Windows事件查看器中的详细错误信息
    echo 3. 使用Dependency Walker检查MT3.exe的依赖项
) else (
    echo.
    echo 警告: 在目标目录中找不到MT3.exe
    echo 请确保已成功编译MT3项目
)

echo.
echo 按任意键退出...
pause >nul
endlocal