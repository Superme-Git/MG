#!/bin/bash
#Filename: xmerge.sh

function usage()
{
	echo "-->Usage: xmerge.sh mylogic.jar -conf xmerge.xml [src_dir] [dest_dir] [foreign_dir]"
	exit
}

if [ "$#" -lt 3 ]; then
	usage
else
	if [ ! -f "$1" ]; then
		echo "-->$1 not exist!"
		exit
	else
		logic_jar=$1	
	fi
	if [ ! "$2" = "-conf" ]; then
		usage
	else			
		if [ ! -f $3 ]; then
			echo "-->$3 not exist!"
			exit
		else
			xmergeconf=$3
		fi
	fi
	src_dir=$4
	dest_dir=$5
	foreign_dir=$6
fi
			
if [ ! -f "xmerge.jar" ]; then
	echo "-->xmerge.jar not exist"
	exit
else
	java -Xss4m -Xbootclasspath/a:../bin/xdb.jar:../bin/jio.jar:$logic_jar -jar ../bin/xmerge.jar -conf $xmergeconf -srcdb $src_dir -destdb $dest_dir
fi
