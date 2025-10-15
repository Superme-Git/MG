
RCOUNT = 1000

all:
	java -jar ../bin/xgen.jar xdb.xml
	ant
	cp ../jnidb/libdb_amd64.so .
	#cp ../jnidb/libdb_i386.so .

clean:
	ant clean

run:
	java -Xmx2048m -cp ../bin/jio.jar:../bin/xdb.jar:classes bench.Memory ${RCOUNT}
