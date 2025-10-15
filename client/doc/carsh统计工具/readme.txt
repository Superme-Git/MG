android
	把.dump文件全都放在files文件夹里，运行analyse_android.py，会生成result.txt
	如果生成失败，可能是.dump文件里包含中文，可以用python安装时自带的IDLE打开analyse_android.py再运行，找到出错的.dump文件手动修改一下再运行工具
	
	
ios
	ios的crash文件有两级目录，files下面还有带output的目录，files的路径可以通过修改analyse_ios.py里的dumpPath来更改