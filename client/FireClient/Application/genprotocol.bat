echo y | rd ProtoDef /s
java -jar rpcgen.jar -modules modules.xml -debug -validateMarshal -validateUnmarshal -aio client.xml

echo y | rd ProtoPkg /s
echo y | del ..\..\resource\res\script\manager\protocolhandlermanager.lua
echo y | rd ..\..\tolua++-pkgs\FireClient\ProtoDef /s
java -jar rpcgen.jar -debug -validateMarshal -validateUnmarshal -luapkg -aio pkg_client.xml
move ProtoPkg\protocolhandlermanager.lua ..\..\resource\res\script\manager
move ProtoPkg ..\..\tolua++-pkgs\FireClient\ProtoDef

echo y | rd protolua /s
echo y | rd ..\..\resource\res\script\protodef /s
java -jar rpcgen.jar -debug -validateMarshal -validateUnmarshal -lua -aio lua_client.xml
move protolua ..\..\resource\res\script\protodef

pause