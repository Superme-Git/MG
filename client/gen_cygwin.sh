#!/bin/bash

# gen authc protocols for c++
cd ../common/cauthc
chmod +x genauth.sh
./genauth.sh 
cd ../../client


# gen client protocols for c++
cd FireClient/Application
chmod +x genprotocols.sh
./genprotocols.sh $1

chmod +x genluaforproto.sh
./genluaforproto.sh 
rm -Rf ../../resource/res/script/protodef
mv ./protolua ../../resource/res/script/protodef

# gen LuaFireClient.cpp
cd ../../tolua++-pkgs
chmod +x lua5.1.dll
chmod +x tolua++WP8.exe
ant genapp_tolua_cygwin

# gen LuaEngine.cpp
cd ../../engine/tolua++-pkgs
chmod +x lua5.1.dll
chmod +x tolua++WP8.exe
./tolua++WP8.exe -o ../../client/FireClient/Application/Framework/LuaEngine.cpp -L custom_functions.lua engine.pkg

# gen lua_CEGUI.cpp
cd ../../dependencies/cegui/CEGUI/src/ScriptingModules/LuaScriptModule/package
chmod +x toluappcegui.exe
./toluappcegui.exe -o ../lua_CEGUI.cpp -L exceptions.lua CEGUI.pkg

