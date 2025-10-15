#pragma once

/*
  原bsdiff.cpp和bspatch.cpp中均含有err、errx等函数
  所以全放到这个公共文件中
**/



template<class T1, class T2>
void err(int i, const char* str, T1 arg1, T2 arg2) {
	char lastErrorTxt[1024];
	FormatMessageA(FORMAT_MESSAGE_FROM_SYSTEM|FORMAT_MESSAGE_IGNORE_INSERTS,NULL,GetLastError(),0,lastErrorTxt,1024,NULL);
	printf(("%s"),lastErrorTxt);
	printf(str, arg1, arg2);
	exit(i);
}
template<class T>
void err(int i, const char* str, T arg) {
	char lastErrorTxt[1024];
	FormatMessageA(FORMAT_MESSAGE_FROM_SYSTEM|FORMAT_MESSAGE_IGNORE_INSERTS,NULL,GetLastError(),0,lastErrorTxt,1024,NULL);
	printf(("%s"),lastErrorTxt);
	printf(str, arg);
	exit(i);
}
inline void err(int i, const char* str) {
	char lastErrorTxt[1024];
	FormatMessageA(FORMAT_MESSAGE_FROM_SYSTEM|FORMAT_MESSAGE_IGNORE_INSERTS,NULL,GetLastError(),0,lastErrorTxt,1024,NULL);
	printf(("%s"),lastErrorTxt);
	if (str!=NULL) {
		printf(("%s"),str);
	}
	exit(i);
}
template<class T>
void errx(int i, const char* str, T arg) {
	printf(str, arg);
	exit(i);
}
inline void errx(int i, const char* str) {
	printf(("%s"),str);
	exit(i);
}
