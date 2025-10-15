/*************************************************************************
	> File Name: UpdateCommon.h
	> Author:sunwenming
	> Mail: ma6174@163.com 
	> Created Time: 五  6/21 11:37:41 2013
 ************************************************************************/

#ifdef ANDROID
#include <android/log.h>

#define  LOG_TAG    "updateengine"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define LOGD
#endif
