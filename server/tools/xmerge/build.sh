#!/bin/bash
#Filename:build.sh

if [ ! -f build-ml.xml ]; then
	echo "-->build-ml.xml not exist"
else
	if [ ! -f build.xml ]; then
		echo "-->build.xml not exist"
	else
		if [ ! -f build-validator.xml ]; then
			echo "-->build-validator.xml not exist"
		else
			ant -f build-ml.xml xgen
			ant -f build-ml.xml install
			ant -f build.xml install
			ant -f build-validator.xml install
			echo "-->mylogic.jar|xmerge.jar|validator.jar are distributed to ../bin"
		fi
	fi
fi
