#/bin/bash
cd ../../dependencies/cegui/CEGUI/src/ScriptingModules/LuaScriptModule/package/
chmod +x ./make.bat
chmod +x ./tolua++cegui
./make.bat
cd ../../../../../../../client/tolua++-pkgs
