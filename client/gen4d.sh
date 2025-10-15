#!/bin/bash
MODE=$1
#gen authc protocols for c++
cd ../common/cauthc
chmod +x genauth.sh
./genauth.sh 
cd ../..


#gen client protocols for c++
cd client/FireClient/Application
chmod +x genprotocols.sh
./genprotocols.sh $1

chmod +x genluaforproto.sh
./genluaforproto.sh 
rm -Rf ../../resource/res/script/protodef
mv ./protolua ../../resource/res/script/protodef

#gen client beans for c++
#cd beans/make
#chmod u+x make.sh
#./make.sh $1

cd ../../tolua++-pkgs
chmod u+x ./FireClient_tolua++.sh
#ant    ###########commend by liugeng, replace by following command, TODO: move ant to /usr/local
/Users/mtg/Documents/apache-ant-1.9.4/bin/ant
#./FireClient_tolua++.sh
chmod +x ./cegui_tolua++.sh
./cegui_tolua++.sh

chmod +x ./engine_tolua++.sh
./engine_tolua++.sh

#gen lua++ code for lua


# clean luadebug

cd ../
#rm resource/res/script/sdluadebug.lua
#touch resource/res/script/sdluadebug.lua

rsync -r --exclude=".svn" ../dependencies/luadebug/ resource/res/script/

vidir=`pwd`/resource/res/script/
vidir=${vidir//\//\\\/}
echo $vidir

#sed -i -e "s/scriptdir/${vidir}/g" resource/res/script/sdluadebug.lua
#rm resource/res/script/sdluadebug.lua-e
