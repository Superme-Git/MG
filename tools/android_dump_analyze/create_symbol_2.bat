@echo off

setlocal enabledelayedexpansion

echo -
echo -
echo ******************************************
echo move symbol file to it's path
echo ******************************************
echo -
echo -

md .\sym\symbols\libgame.so\512BBEFD57BA534123C48FB74B07EFBF0
move .\sym\libgame.so.sym .\sym\symbols\libgame.so\512BBEFD57BA534123C48FB74B07EFBF0

echo done!
pause>nul
