@echo off

setlocal enabledelayedexpansion

echo -
echo -
echo ******************************************
echo push tools to phone, and prepare for job
echo ******************************************
echo -
echo -

adb push .\tools /data/local/tmp
adb shell chmod 0777 /data/local/tmp/init.sh
adb shell /data/local/tmp/init.sh
adb shell /data/local/tmp/clear.sh

echo -
echo -
echo done!

pause>nul
