
#!/bin/bash
rm -Rf ./protolua/*
echo "clean protocols lua done =========================================="

echo "gen protocol lua:"

java -jar rpcgen.jar  -debug -validateMarshal -validateUnmarshal -lua -aio lua_client.xml
echo "gen client protocol lua code success!"
