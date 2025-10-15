#!/bin/bash
java -jar ./rpcgen.jar -aio ./authc.xml
java -jar ./rpcgen.jar -aio ./gnet.xml
echo "gen authc protocol success!"
