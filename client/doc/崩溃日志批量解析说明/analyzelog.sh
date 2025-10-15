#!/bin/bash
export DEVELOPER_DIR=/Applications/Xcode.app/Contents/Developer 

filedatename=0128

filelist=`ls $filedatename`

for filetype in $filelist
do

#filetype=1
echo $filetype

outputfile=$filedatename"/""output"$filetype
mkdir $outputfile

filelist2=`ls $filedatename$"/"$filetype/`

#=======================
for filecrash in $filelist2
do

#echo $filecrash
targetfile=$filedatename"/"$filetype"/"$filecrash
echo $targetfile

targetfileoutput=$outputfile"/"$filecrash
echo $targetfileoutput

./symbolicatecrash $targetfile mt3 > $targetfileoutput

done
#=======================

done


#for file in $(find crashfile -name "*.crash")
#do
#echo $file
#done


#for filecrash in $filelist2
#do
#echo $filecrash


#targetfile=$filedatename"/"$filetype"/"$filecrash
#echo $targetfile

#targetfileoutput=$outputfile"/"$filecrash
#echo $targetfileoutput

#./symbolicatecrash $targetfile mt3 > $targetfileoutput

#done

