@echo off
echo 开始时间是%time:~0,2%点%time:~3,2%分%time:~6,2%秒
set time1=%time:~0,2%%time:~3,2%%time:~6,2%

java -jar xmerge_run.jar -conf xmerge.xml -srcdb "F:\\xmergetest\\src_db" -destdb "F:\\xmergetest\\dest_db" -resultdb "F:\\xmergetest\\result_db" 

echo 结束时间是%time:~0,2%点%time:~3,2%分%time:~6,2%秒
set time2=%time:~0,2%%time:~3,2%%time:~6,2%
set /a time3=%time2%-%time1%
echo 间隔%time3%秒
pause