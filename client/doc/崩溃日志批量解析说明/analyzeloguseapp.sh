#!/bin/bash
export DEVELOPER_DIR=/Applications/Xcode.app/Contents/Developer 
mkdir output
filelist=`ls crashfile/`

for file in $filelist
do

echo $file

#{$file//_} replace space to _

./symbolicatecrash "crashfile/"$file mt3.app > "output/"$file

done


#for file in $(find crashfile -name "*.crash")
#do
#echo $file
#./symbolicatecrash $file mt3.app.dSYM > $file".log"
#done

