@echo off
set /p var_apktype=请选择要出的APK包类型(免费服:1,点卡服:2):

if "%var_apktype%" == "1" (
cd .\build 
ant -buildfile build.xml release
)

if "%var_apktype%" == "2" (
cd .\build 
ant -buildfile build_monthpayment.xml release
) else ( echo input error )


cd ..
pause