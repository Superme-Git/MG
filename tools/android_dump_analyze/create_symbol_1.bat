@echo off

setlocal enabledelayedexpansion

echo -
echo -
echo ******************************************
echo clear work path in phone
echo ******************************************
echo -
echo -

adb shell /data/local/tmp/clear.sh
echo done!

echo -
echo -
echo ******************************************
echo push libgame.so to work path
echo ******************************************
echo -
echo -

adb push .\so\libgame.so /sdcard/data/dumptest
echo done!

echo -
echo -
echo ******************************************
echo create symbol file in path "./sym"
echo ******************************************
echo -
echo -

adb shell /data/local/tmp/dump_syms /sdcard/data/dumptest/libgame.so > .\sym\libgame.so.sym
echo done!

echo -
echo -
echo ******************************************
echo delete libgame.so in work path
echo ******************************************
echo -
echo -

adb shell rm /sdcard/data/dumptest/libgame.so
echo done!

pause>nul
