function doWithDump() {
	for file in ` ls /sdcard/data/dumptest/*.dmp `
	do
			/data/local/tmp/minidump_stackwalk $file /sdcard/data/dumptest/symbols > $file".txt"
	done
}

doWithDump