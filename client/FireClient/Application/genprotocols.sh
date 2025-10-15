
#!/bin/bash
if [ "$1" != "debug" ]
then
rm -Rf ./ProtoDef/*
fi
echo "clean protocols done =========================================="

echo "gen protocols:"

java -jar rpcgen.jar  -modules modules.xml -debug -validateMarshal -validateUnmarshal -aio client.xml
echo "gen client protocol code success!"
