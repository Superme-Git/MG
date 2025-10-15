@echo off

setlocal enabledelayedexpansion

echo -
echo -
echo ******************************************
echo translate dump file format from DOS to UNIX
echo ******************************************
echo -
echo -

cd .\files
for /r %%a in (*.dmp) do (
    set a=%%a
    ..\removeReturn.exe !a!
)

echo done!
ping -n 3 127.1>nul

echo -
echo -
echo ******************************************
echo clear work path in phone
echo ******************************************
echo -
echo -

cd ..
adb shell /data/local/tmp/clear.sh

echo done!
ping -n 3 127.1>nul

echo -
echo -
echo ******************************************
echo push symbol file to work path
echo ******************************************
echo -
echo -

adb push .\sym /sdcard/data/dumptest

echo done!
ping -n 3 127.1>nul

echo -
echo -
echo ******************************************
echo push dump file to work path
echo ******************************************
echo -
echo -

adb push .\files /sdcard/data/dumptest

echo done!
ping -n 3 127.1>nul

echo -
echo -
echo ******************************************
echo analyze dump file in work path
echo ******************************************
echo -
echo -

adb shell /data/local/tmp/analyze.sh

echo done!
ping -n 3 127.1>nul

echo -
echo -
echo ******************************************
echo pull back txt file from work path
echo ******************************************
echo -
echo -

adb pull /sdcard/data/dumptest .\files

echo done!
pause>nul