#include "FileDownloader.h"
//#include "GlobalNotification.h"

#include "../GlobalFunction_Common.cpp"
#include "FileInterface.h"
#include "UpdateInterface.h"

//#include <pthread.h>
//pthread_t g_tipstid;
//BOOL tipsloop = NO;
//void* threadLoop(void *param)
//{
//    while (tipsloop == YES) {
//        std::string utfstring = SHARE_Wstring2String(GlobalGetText(rand()%51+900));
//        GlobalNotification::GetInstance().onNotifyTips([NSString stringWithUTF8String:utfstring.c_str()]);
//        sleep(5);
//    }
//    return NULL;
//}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
int NeedToStringUpdate(std::wstring latest, std::wstring laststrong)
{
	Platform::String^ newest = ref new Platform::String(latest.c_str());
	Platform::String^ strong = ref new Platform::String(laststrong.c_str());

	//local >= newest
	if (PhoneDirect3DXamlAppComponent::UpdateInterface::callCompareWithLocalVersion(newest) <= 0)
	{
		return 1;
	}

	//local >= strong
	if (PhoneDirect3DXamlAppComponent::UpdateInterface::callCompareWithLocalVersion(strong) <= 0)
	{
		return 2;
	}
	else
	{
		//strong
		return 3;
	}
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

bool IsDirectoryExisting( const wchar_t * lpszDirName )
{
	Platform::String^ dirName = ref new Platform::String(lpszDirName);
	return PhoneDirect3DXamlAppComponent::FileInterface::callIsDirectoryExisting(dirName);
}

void CreateDir(const std::wstring& dir)
{
    Platform::String^ dirName = ref new Platform::String(dir.c_str());
	PhoneDirect3DXamlAppComponent::FileInterface::callCreateDir(dirName);
}

void RemoveDir(const std::wstring& dir)
{
	Platform::String^ dirName = ref new Platform::String(dir.c_str());
	PhoneDirect3DXamlAppComponent::FileInterface::callRemoveDir(dirName,true);
}

void CopyDirectoryWithNotify(const std::string& src, const std::string& dst)
{
	std::wstring srcW = SHARE_String2Wstring(src);
	std::wstring dstW = SHARE_String2Wstring(dst);
	GlobalNotify_TextID( 36 );
	Platform::String^ srcDir = ref new Platform::String(srcW.c_str());
	Platform::String^ dstDir = ref new Platform::String(dstW.c_str());
	PhoneDirect3DXamlAppComponent::FileInterface::callCopyDirectoryWithNotify(srcDir, dstDir);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void GlobalNotify(const wchar_t* format, ...)
{
    
    va_list ap;
    va_start(ap,format);
    std::wstring instring;
    wchar_t buffer[1024] = {0};
    
    int numforwrite = 0;
    
    numforwrite = vsnwprintf_x( buffer,1024,format,ap);
    
    if ( numforwrite > 0)
    {
        std::wstring str(buffer);
        instring = str;
    }
    
    else
        instring = L"";
    va_end(ap);

	Platform::String^ msg = ref new Platform::String(instring.c_str());
	PhoneDirect3DXamlAppComponent::UpdateInterface::callonNotifyMsg(msg);

}

void GlobalNotifyMsgAndStep(const std::wstring& msg)
{
	Platform::String^ str = ref new Platform::String(msg.c_str());
	PhoneDirect3DXamlAppComponent::UpdateInterface::callonNotifyStart(str);
}

void GlobalNotifyEnd()
{
	PhoneDirect3DXamlAppComponent::UpdateInterface::callonNotifyEnd();
}

void GlobalNotifyFail()
{
	PhoneDirect3DXamlAppComponent::UpdateInterface::callonNotifyFail();
}

void GlobalNotifyStep(int step)
{
	PhoneDirect3DXamlAppComponent::UpdateInterface::callonNotifyStep(step);
}

void GlobalNotifyMsg(const std::wstring& msg)
{
	Platform::String^ str = ref new Platform::String(msg.c_str());
	PhoneDirect3DXamlAppComponent::UpdateInterface::callonNotifyMsg(str);
}
void GlobalNotify_TextID(int textID)
{
    GlobalNotify(GlobalGetText(textID).c_str()) ;
}

void GlobalNotifyLocalVersion(const std::wstring& localVersion)
{
	Platform::String^ str = ref new Platform::String(localVersion.c_str());
	PhoneDirect3DXamlAppComponent::UpdateInterface::callonNotifyLocalVersion(str);
}

void GlobalNotifyNewVersion(const std::wstring& newVersion)
{
	Platform::String^ str = ref new Platform::String(newVersion.c_str());
	PhoneDirect3DXamlAppComponent::UpdateInterface::callonNotifyLatestVersion(str);
}

void GlobalNotifyDownLoadSize(LJFM::fsize_t downloaded , LJFM::fsize_t total)
{
	std::wstring sizeString(L""); 
	FormatString(sizeString, L"%.1fkb/%.1fkb", downloaded/1024.0f, total/1024.0f);
	Platform::String^ str = ref new Platform::String(sizeString.c_str());
	PhoneDirect3DXamlAppComponent::UpdateInterface::callonNotifyDownLoadSize(str);
}

void GlobalNotifyDownLoadEnd()
{
	PhoneDirect3DXamlAppComponent::UpdateInterface::callonNotifyDownLoadEnd();
}

void GlobalNotifyDownLoadSizeTooLarge(unsigned long long total)
{
	PhoneDirect3DXamlAppComponent::UpdateInterface::callonNotifyDownLoadSizeTooLarge(total);
}

void GlobalNotifyAppUpdate(int ret, const std::wstring& downloadurl)
{
	Platform::String^ str = ref new Platform::String(downloadurl.c_str());
	PhoneDirect3DXamlAppComponent::UpdateInterface::callonNotifyAppUpdate(ret, str);
}

void GlobalNotifyTips()
{
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

bool needToSelectUpdateUrl()
{
#ifdef IOS_LARGE_RESOURCE_UPDATE
	std::string path =  CFileUtil::GetCacheDir() + "/res/updatetype";
	if (!IsDirectoryExisting(SHARE_String2Wstring(path).c_str()))
	{
		return true;
	}

	Platform::String^ str = ref new Platform::String(SHARE_String2Wstring(path).c_str());
	Platform::String^ content = PhoneDirect3DXamlAppComponent::FileInterface::callReadFile(str);
	std::wstring contents = content->Data();
	if (contents.compare(L"yes") == 0)
	{
		return false;
	}
	else
	{
		return true;
	}
#endif
	return false;
}

void saveHaveSelectUpdateUrl()
{
#ifdef IOS_LARGE_RESOURCE_UPDATE
	std::string path =  CFileUtil::GetCacheDir() + "/res/updatetype";
	Platform::String^ pathStr = ref new Platform::String(SHARE_String2Wstring(path).c_str());
	Platform::String^ content = ref new Platform::String(L"yes");
	PhoneDirect3DXamlAppComponent::FileInterface::callWriteFile(pathStr, content);
#endif
}

void CheckCopy()
{
	PhoneDirect3DXamlAppComponent::UpdateInterface::callCheckCopy();
}

void CopyPatcher()
{
	PhoneDirect3DXamlAppComponent::UpdateInterface::callCopyPatcher();
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
