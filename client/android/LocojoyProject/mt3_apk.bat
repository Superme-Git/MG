@echo off
set /p var_apktype=��ѡ��Ҫ����APK������(��ѷ�:1,�㿨��:2):

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