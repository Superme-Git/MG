#Q/bin/sh
REPORT_FILE="testCompressReport.txt"
declare -i min_filesize=2000000000
declare -i max_filesize=0
declare -i successes=0
declare -i failures=0

echo "==========================="  > ${REPORT_FILE} 
echo "====== Test   Start =======" >> ${REPORT_FILE} 
echo "===========================" >> ${REPORT_FILE} 

for files in `find $1 *`; do
	if [ -f $files ]; then
		# 统计文件大小
		declare -i filesize=$(wc -c ${files} | cut -d ' ' -f 1)
		#echo $filesize
		if [ $max_filesize -le $filesize ]; then
			declare -i max_filesize=$filesize
		fi
		if [ $min_filesize -ge $filesize ]; then
			declare -i min_filesize=$filesize
		fi
	
		# 检查压缩解压算法
		if [$(diff file1 file2 | wc -l | cut -d ' ' -f 1) -eq 0]
		if [ $(./testool < ${files} 2>&1  | grep "Assert" | wc -l | cut -d ' ' -f 1) -eq 0 ]; then
			echo "[Test File] ${files} : Success" | tee -a ${REPORT_FILE}
			successes=$[ $successes + 1 ]
		else	
			echo "[Test File] ${files} : Failure" | tee -a ${REPORT_FILE}
			failures=$[ $failures + 1 ]
		fi
		./testool < ${files} | tee -a ${REPORT_FILE}
	fi
done

echo "==========================" >> ${REPORT_FILE}
echo "====== Test  Result  =====" >> ${REPORT_FILE}
echo "==========================" >> ${REPORT_FILE}

#echo "Total Test Number:$(grep "Testing:" ${REPORT_FILE} | wc -l | cut -d ' ' -f 1)" >> ${REPORT_FILE} 
#echo "Failure Number:   $(grep "Assert"   ${REPORT_FILE} | wc -l | cut -d ' ' -f 1)" >> ${REPORT_FILE} 
echo "Success Number:    $successes" | tee -a ${REPORT_FILE} 
echo "Failure Number:    $failures " | tee -a ${REPORT_FILE} 
echo "Max File Size :    $max_filesize" | tee -a ${REPORT_FILE} 
echo "Min File Size :    $min_filesize" | tee -a ${REPORT_FILE} 

