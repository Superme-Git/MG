#include "main.h"
#include "CrashDump.h"
#include "CCEGLView.h"

#include "GameApplication.h"
#include "Resource.h"
#include "../../platform/ini/IniFile.h"

#include "io.h"
#include "utils\FileUtil.h"
//#define USE_WIN32_VLD	// uncomment, open visual leak detector in win32 debug mode

#if (defined(_DEBUG) && defined(USE_WIN32_VLD) && defined(WIN32))
	#include "vld.h"
#endif
#include "LoginManager.h"

USING_NS_CC;

int ReplaceLauncher()
{
	remove("../../Launcher.exe.old");
	std::ifstream FSNew("Launcher.exe.new", std::ios_base::binary);
	if (FSNew.is_open())
	{
		FSNew.seekg(0, std::ios_base::end);
		int iNewSize = FSNew.tellg();
		FSNew.close();
		int iOldSize = -1;
		std::ifstream FSOld("../../Launcher.exe", std::ios_base::binary);
		if (FSOld.is_open())
		{
			FSOld.seekg(0, std::ios_base::end);
			iOldSize = FSOld.tellg();
			FSOld.close();
		}
		if (iNewSize != iOldSize)
		{
			rename("../../Launcher.exe", "../../Launcher.exe.old");
			CFileUtil::CopyFileByFS(L"Launcher.exe.new", L"../../Launcher.exe");
			remove("../../Launcher.exe.old");
		}
	}
	return 0;
}

int APIENTRY _tWinMain(HINSTANCE hInstance,
                       HINSTANCE hPrevInstance,
                       LPTSTR    lpCmdLine,
                       int       nCmdShow)

{
    UNREFERENCED_PARAMETER(hPrevInstance);
    UNREFERENCED_PARAMETER(lpCmdLine);

	ReplaceLauncher();

	int iRunMax = 3;
	std::string sRunMaxMsgTitle = "Limit";
	std::string sRunMaxMsgData = "Client Limit 3";
	std::ifstream FS("StartLimit.txt");//std::ios_base::binary
	if (FS.is_open())
	{
		FS.seekg(0, std::ios_base::end);
		unsigned int uiLen = FS.tellg();
		FS.seekg(0, std::ios_base::beg);
		std::string sData;
		sData.resize(uiLen);
		FS.read((char*)sData.c_str(), uiLen);
		size_t FindStart = 0;
		size_t FindPos = 0;
		FindPos = sData.find_first_of("|", FindStart);
		if (FindPos != std::string::npos && FindPos != FindStart)
		{
			std::string sRunMax = sData.substr(0, FindPos);
			iRunMax = atoi(sRunMax.c_str());
			FindStart = FindPos + 1;
			FindPos = sData.find_first_of("|", FindStart);
			if (FindPos != std::string::npos && FindPos != FindStart)
			{
				sRunMaxMsgTitle = sData.substr(FindStart, FindPos - FindStart);
				sRunMaxMsgData = sData.substr(FindPos + 1);
			}
		}
		FS.close();
	}
	HANDLE hSemaphoreOneInstance = OpenSemaphore(SEMAPHORE_ALL_ACCESS, FALSE, L"MT3Client");
	if (!hSemaphoreOneInstance)
	{
		hSemaphoreOneInstance = CreateSemaphore(NULL, iRunMax, iRunMax, L"MT3Client");
	}
	DWORD dwResult = WaitForSingleObject(hSemaphoreOneInstance, 100);
	if (dwResult == WAIT_TIMEOUT)
	{
		MessageBoxA(0, sRunMaxMsgData.c_str(), sRunMaxMsgTitle.c_str(), MB_OK);
		return FALSE;
	}

	std::wstring strCmdLine = lpCmdLine;
	std::wstring shortcutLaunchKey = L"shortcutLaunch=";
	size_t shortcutLaunchPos = strCmdLine.find(shortcutLaunchKey);
	if (shortcutLaunchPos != std::wstring::npos)
	{
		size_t valPos = shortcutLaunchPos + shortcutLaunchKey.size();
		size_t blankPos = strCmdLine.find(L" ", valPos);
		if (blankPos == std::wstring::npos)
		{
			blankPos = strCmdLine.size();
		}
		size_t valLen = blankPos - valPos;
		std::wstring strVal = strCmdLine.substr(valPos, valLen);
		eShortcutItem shortcutItem = (eShortcutItem)_wtoi(strVal.c_str());
		LoginManager::NewInstance()->setShortcutItemLaunchedBy(shortcutItem);
	}

    //create the application instance
	int w = 800;
	int h = 600;
	std::FILE *fp = std::fopen("frameSize.txt", "r");
	if (fp)
	{
		fseek(fp, 0, SEEK_END);
		long size = ftell(fp);
		fseek(fp, 0, SEEK_SET);
		char *buffer = new char[size+1];
		memset(buffer, 0, size + 1);
		size = fread(buffer, sizeof(char), size, fp);
		char *p = strtok(buffer, " ");
		w = atoi(p);
		p = strtok(NULL, " ");
		h = atoi(p);
		delete buffer;	// yeqing 2015-10-20
		fclose(fp);
	}

	int isPointServer = IniFile::read_profile_int("ClientSetting", "bIsPointVersion", 0, "clientsetting_win.ini");
	bool bIsPoint = isPointServer > 0 ? true : false;

    CCEGLView* eglView = CCEGLView::sharedOpenGLView();
    eglView->setViewName("Welcome to MT3");
    eglView->setFrameSize(w, h);
	if (!bIsPoint)
	{
		HICON icon_dk = LoadIcon(hInstance, MAKEINTRESOURCE(IDI_ICON1));
		SendMessage(eglView->getHWnd(), WM_SETICON, TRUE, (LPARAM)icon_dk);//大图标
	}
	else
	{
		HICON icon_free = LoadIcon(hInstance, MAKEINTRESOURCE(IDI_ICON2));
		SendMessage(eglView->getHWnd(), WM_SETICON, TRUE, (LPARAM)icon_free);//大图标
	}
    // set the design resolution screen size, if you want to use Design Resoulution scaled to current screen, please uncomment next line.
    // eglView.setDesignResolutionSize(1136, 640);
	SYSTEMTIME st;
	GetSystemTime(&st);
	wchar_t wsDmpFileName[MAX_PATH];
	wsprintf(wsDmpFileName, L"%d_%d_%d_%d.dmp", st.wDay, st.wHour, st.wMinute, st.wSecond);
	MHSD_CrashDump::CrashDump_Init(wsDmpFileName, L"");
	int retNumber = gRunGameApplication();
	MHSD_CrashDump::CrashDump_Clean();
	if (hSemaphoreOneInstance)
	{
		ReleaseSemaphore(hSemaphoreOneInstance, 1, 0);
	}
	return retNumber;
}
