#/bin/bash
cd FireClient

#protocols
cd ../../FireClient/Application/
chmod +x ./genpkgforproto.sh
./genpkgforproto.sh
cd ../../tolua++-pkgs/FireClient
rm -Rf ProtoDef/
mkdir ProtoDef
cp -Rf ../../FireClient/Application/ProtoPkg/* ./ProtoDef/
mv ProtoDef/protocolhandlermanager.lua ../../resource/res/script/manager/


#../tolua++.exe -tFireClient -o ../../FireClient/Application/Framework/LuaFireClient.cpp FireClient.pkg
chmod +x ../tolua++.exe
../tolua++.exe -tFireClient -L ./custom_functions.lua -o ../../FireClient/Application/Framework/LuaFireClient.cpp FireClient.pkg
cd ..
