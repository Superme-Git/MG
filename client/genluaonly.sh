#!/bin/bash


cd FireClient/Application
chmod u+x genluaforproto.sh
./genluaforproto.sh
rm -Rf ../../resource/res/script/protodef
mv ./protolua ../../resource/res/script/protodef



