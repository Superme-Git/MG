
#ifndef __FireClient__GlobalNotification__
#define __FireClient__GlobalNotification__

#include <string>
#include <jni.h>

extern "C"
{
	extern void onNotifyMsg(const std::wstring& msg);
    
	extern void onNotifyStart(const std::wstring& msg);
    
	extern void onNotifyEnd();

	extern void onNotifyFail();
    
	extern void onNotifyStep(int step);
	extern void onNotifyStep2(int spro, int epro, int sec);
    
	extern void onNotifyLocalVersion(const std::wstring& localVersion );
    
	extern void onNotifyLatestVersion(const std::wstring& newVersion);
    
	extern void onNotifyDownLoadSize(const std::wstring& sizeString);
    
	extern void onNotifyDownLoadEnd();
    
	extern void onNotifyDownLoadSizeTooLarge(unsigned long long total);
    
	extern void onNotifyAppUpdate(int ret);
    
    extern void onNotifyShowForm(int FormType, int DownloadSize, std::wstring AppURL);
}


#endif /* defined(__FireClient__GlobalNotification__) */
