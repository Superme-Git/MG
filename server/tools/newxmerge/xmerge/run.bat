@echo off
echo ��ʼʱ����%time:~0,2%��%time:~3,2%��%time:~6,2%��
set time1=%time:~0,2%%time:~3,2%%time:~6,2%

java -jar xmerge_run.jar -conf xmerge.xml -srcdb "F:\\xmergetest\\src_db" -destdb "F:\\xmergetest\\dest_db" -resultdb "F:\\xmergetest\\result_db" 

echo ����ʱ����%time:~0,2%��%time:~3,2%��%time:~6,2%��
set time2=%time:~0,2%%time:~3,2%%time:~6,2%
set /a time3=%time2%-%time1%
echo ���%time3%��
pause