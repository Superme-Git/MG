#/bin/bash
cd ../../engine/tolua++-pkgs/
chmod +x ./tolua++.exe
./tolua++.exe -o ../../client/FireClient/Application/Framework/LuaEngine.cpp -L custom_functions.lua engine.pkg
cd ../../client/tolua++-pkgs
