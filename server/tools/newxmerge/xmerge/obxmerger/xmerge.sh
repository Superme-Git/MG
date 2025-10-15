#!/bin/bash
cd `dirname $0`

logic_jar=mylogic.jar
xmergeconf=xmerge.xml
xdb_lib=lib/xdb.jar			
jio_lib=lib/jio.jar
xbean_lib=lib/gsxdb.jar

SRC1_DIR=$1
SRC2_DIR=$2
DEST_DIR=$3
if [ $# != 3 ];then
	echo "error operation: usage is not correct!"
	echo "USAGE : $0 source1_dir source2_dir result_dir"
	exit 1
fi

if [ ! -d ${SRC1_DIR} ];then
	echo "error!"
	echo "source 1 xdb dir is not exist!"
	exit 1
fi

if [ ! -d ${SRC2_DIR} ];then
	echo "error!"
	echo "source 2 xdb dir is not exist!"
	exit 1
fi

if [ ${SRC1_DIR} -ef ${SRC2_DIR} ];then
	echo "error!"
	echo "source 1 is same as source 2!"
	exit 1
fi

if [ ${SRC1_DIR} -ef ${DEST_DIR} ];then
	echo "error!"
	echo "source 1 is same as dest!"
	exit 1
fi

if [ ${SRC2_DIR} -ef ${DEST_DIR} ];then
	echo "error!"
	echo "source 2 is same as dest!"
	exit 1
fi


mkdir -p ${DEST_DIR}
java -Xss4m -Xbootclasspath/a:$xdb_lib:$jio_lib:$xbean_lib:$logic_jar:xmerge.jar -jar xmerge.jar -conf $xmergeconf -srcdb $SRC1_DIR -destdb $SRC2_DIR -resultdb $DEST_DIR
