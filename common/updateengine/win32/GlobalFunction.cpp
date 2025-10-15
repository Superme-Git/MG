#include <string>


////////////////////////////////////////////////////////////////////////////////////////////////////////////////
int NeedToStringUpdate(std::wstring latest, std::wstring laststrong)
{
	return 0;
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

bool IsDirectoryExisting(const wchar_t * lpszDirName)
{
	return true;
}

void CreateDir(const std::wstring& dir)
{
}

void RemoveDir(const std::wstring& dir)
{
}

void CopyDirectoryWithNotify(const std::string& src, const std::string& dst)
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void GlobalNotify(const wchar_t* format, ...)
{

}

void GlobalNotifyMsgAndStep(const std::wstring& msg)
{
}

void GlobalNotifyEnd()
{
}

void GlobalNotifyFail()
{
}

void GlobalNotifyStep(int step)
{
}

void GlobalNotifyMsg(const std::wstring& msg)
{
}
void GlobalNotify_TextID(int textID)
{
}

void GlobalNotifyLocalVersion(const std::wstring& localVersion)
{
}

void GlobalNotifyNewVersion(const std::wstring& newVersion)
{
}

void GlobalNotifyDownLoadEnd()
{
}

void GlobalNotifyDownLoadSizeTooLarge(unsigned long long total)
{
}

void GlobalNotifyAppUpdate(int ret, const std::wstring& downloadurl)
{
}

void GlobalNotifyTips()
{
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

bool needToSelectUpdateUrl()
{
	return true;
}

void saveHaveSelectUpdateUrl()
{
}

void CheckCopy()
{
}

void CopyPatcher()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
