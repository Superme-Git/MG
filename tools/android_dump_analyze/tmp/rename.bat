@echo off

setlocal enabledelayedexpansion

set n=1
for /r %%a in (*.txt) do (
    set a=%%a
    echo "!a! -> !n!.dump"
    ren !a! "!n!.dump"
    set /a n+=1
)

pause

