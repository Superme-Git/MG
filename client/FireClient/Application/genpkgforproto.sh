
#!/bin/bash
rm -Rf ./ProtoPkg/*
echo "clean protocols pkgs done =========================================="

echo "gen protocol pkg:"

java -jar rpcgen.jar  -debug -validateMarshal -validateUnmarshal -luapkg -aio pkg_client.xml
echo "gen client protocol pkg code success!"
