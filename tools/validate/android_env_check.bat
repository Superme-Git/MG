@echo off
setlocal EnableExtensions EnableDelayedExpansion

rem ============================================================
rem Android 环境快速验证脚本（批处理版）
rem 用法示例：
rem   tools\validate\android_env_check.bat SDK="C:\Progra~2\Android\android-sdk" NDK="D:\android-ndk-r10e" ANT="C:\apache-ant-1.9.7" JDK="C:\Program Files\Java\jdk1.8.0_144" BUILD_TOOLS=22.0.1 PLATFORM=android-22
rem 说明：
rem - 支持 key=value 形式参数（可带引号）
rem - 未传参时使用默认值（见“默认参数”）
rem - 返回码：0=全部通过，非0=存在缺失/错误项
rem ============================================================

rem 默认参数（可被 key=value 覆盖）
set "SDK=C:\Progra~2\Android\android-sdk"
set "NDK=D:\android-ndk-r10e"
set "ANT=C:\apache-ant-1.9.7"
set "JDK=C:\Program Files\Java\jdk1.8.0_144"
set "BUILD_TOOLS=22.0.1"
set "PLATFORM=android-22"

rem 解析 key=value 形式的命令行参数
for %%A in (%*) do (
  for /f "tokens=1* delims==" %%B in ("%%~A") do (
    if /I "%%B"=="SDK" set "SDK=%%C"
    if /I "%%B"=="NDK" set "NDK=%%C"
    if /I "%%B"=="ANT" set "ANT=%%C"
    if /I "%%B"=="JDK" set "JDK=%%C"
    if /I "%%B"=="BUILD_TOOLS" set "BUILD_TOOLS=%%C"
    if /I "%%B"=="PLATFORM" set "PLATFORM=%%C"
  )
)

rem 去除可能的包裹引号
call set "SDK=%%SDK:"=%%"
call set "NDK=%%NDK:"=%%"
call set "ANT=%%ANT:"=%%"
call set "JDK=%%JDK:"=%%"
call set "BUILD_TOOLS=%%BUILD_TOOLS:"=%%"
call set "PLATFORM=%%PLATFORM:"=%%"

set "AAPT=%SDK%\build-tools\%BUILD_TOOLS%\aapt.exe"
set "DXBAT=%SDK%\build-tools\%BUILD_TOOLS%\dx.bat"
set "ZIPALIGN=%SDK%\build-tools\%BUILD_TOOLS%\zipalign.exe"
set "ANDROIDJAR=%SDK%\platforms\%PLATFORM%\android.jar"
set "ADB=%SDK%\platform-tools\adb.exe"
set "NDKBUILD=%NDK%\ndk-build.cmd"
set "ANTBAT=%ANT%\bin\ant.bat"
set "JAVA=%JDK%\bin\java.exe"
set "JAVAC=%JDK%\bin\javac.exe"
set "JARSIGNER=%JDK%\bin\jarsigner.exe"

echo ============================================================
echo [Android Env Check] 参数:
echo  SDK="%SDK%"
echo  NDK="%NDK%"
echo  ANT="%ANT%"
echo  JDK="%JDK%"
echo  BUILD_TOOLS="%BUILD_TOOLS%"
echo  PLATFORM="%PLATFORM%"
echo ------------------------------------------------------------
set /a ERR=0

rem --- aapt ---
if exist "%AAPT%" (
  "%AAPT%" version 1>nul 2>nul
  if errorlevel 1 (
    echo [WARN] aapt 存在但 version 调用返回非零："%AAPT%"
  ) else (
    echo [OK]   aapt: "%AAPT%"
  )
) else (
  echo [X]    aapt 缺失："%AAPT%"
  set /a ERR+=1
)

rem --- dx ---
if exist "%DXBAT%" (
  call "%DXBAT%" --version 1>nul 2>nul
  if errorlevel 1 (
    echo [WARN] dx 存在但 --version 返回非零："%DXBAT%"
  ) else (
    echo [OK]   dx: "%DXBAT%"
  )
) else (
  echo [X]    dx 缺失："%DXBAT%"
  set /a ERR+=1
)

rem --- zipalign ---
if exist "%ZIPALIGN%" (
  "%ZIPALIGN%" -h 1>nul 2>nul
  if errorlevel 1 (
    echo [WARN] zipalign 存在但 -h 返回非零："%ZIPALIGN%"
  ) else (
    echo [OK]   zipalign: "%ZIPALIGN%"
  )
) else (
  echo [X]    zipalign 缺失："%ZIPALIGN%"
  set /a ERR+=1
)

rem --- android.jar ---
if exist "%ANDROIDJAR%" (
  echo [OK]   android.jar: "%ANDROIDJAR%"
) else (
  echo [X]    android.jar 缺失："%ANDROIDJAR%"
  set /a ERR+=1
)

rem --- adb ---
if exist "%ADB%" (
  "%ADB%" version 1>nul 2>nul
  if errorlevel 1 (
    echo [WARN] adb 存在但 version 返回非零："%ADB%"
  ) else (
    echo [OK]   adb: "%ADB%"
  )
) else (
  echo [X]    adb 缺失："%ADB%"
  set /a ERR+=1
)

rem --- ndk-build ---
if exist "%NDKBUILD%" (
  call "%NDKBUILD%" --help 1>nul 2>nul
  if errorlevel 1 (
    echo [WARN] ndk-build 存在但 --help 返回非零："%NDKBUILD%"
  ) else (
    echo [OK]   ndk-build: "%NDKBUILD%"
  )
) else (
  echo [X]    ndk-build 缺失："%NDKBUILD%"
  set /a ERR+=1
)

rem --- ant ---
if exist "%ANTBAT%" (
  call "%ANTBAT%" -version 1>nul 2>nul
  if errorlevel 1 (
    echo [WARN] ant 存在但 -version 返回非零："%ANTBAT%"
  ) else (
    echo [OK]   ant: "%ANTBAT%"
  )
) else (
  echo [X]    ant 缺失："%ANTBAT%"
  set /a ERR+=1
)

rem --- JDK: java/javac/jarsigner ---
if exist "%JAVA%" (
  "%JAVA%" -version 1>nul 2>nul
  echo [OK]   java: "%JAVA%"
) else (
  echo [X]    java 缺失："%JAVA%"
  set /a ERR+=1
)

if exist "%JAVAC%" (
  "%JAVAC%" -version 1>nul 2>nul
  echo [OK]   javac: "%JAVAC%"
) else (
  echo [X]    javac 缺失："%JAVAC%"
  set /a ERR+=1
)

if exist "%JARSIGNER%" (
  "%JARSIGNER%" -help 1>nul 2>nul
  echo [OK]   jarsigner: "%JARSIGNER%"
) else (
  echo [X]    jarsigner 缺失："%JARSIGNER%"
  set /a ERR+=1
)

echo ------------------------------------------------------------
if %ERR% NEQ 0 (
  echo [SUMMARY] 验证完成：存在 %ERR% 项缺失/异常（请检查上方 [X]/[WARN]）
  echo [HINT] 建议使用短路径（C:\Progra~2）和正确版本（Build-Tools=%BUILD_TOOLS%、Platform=%PLATFORM%）
  exit /b %ERR%
) else (
  echo [SUMMARY] 验证完成：所有检查通过
  exit /b 0
)