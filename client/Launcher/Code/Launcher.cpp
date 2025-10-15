#include "stdafx.h"
#include "Launcher.h"
#include "LauncherTools.h"
#include "LauncherUI.h"
#include "LauncherData.h"
#include "Download/DownloadManager.h"
#include "Utils/FileUtil.h"
#include "Update/UpdateEngine.h"
#include "WebBrowser/WebBrowser.h"
#include "CrashDump.h"

map<wstring, wstring> g_StringMap;
static ULONG_PTR gs_GPToken = 0;
int APIENTRY _tWinMain(_In_ HINSTANCE hInstance, _In_opt_ HINSTANCE hPrevInstance, _In_ LPTSTR lpCmdLine, _In_ int nCmdShow)
{
	HANDLE hMutexOneInstance = CreateMutex(NULL, TRUE, L"MT3Launcher");
	if (GetLastError() == ERROR_ALREADY_EXISTS)
	{
		MessageBox(0, L"Launcher已经在运行,请关闭之前的程序再次尝试运行", L"", MB_OK);
		return FALSE;
	}
	if (hMutexOneInstance)
	{
		ReleaseMutex(hMutexOneInstance);
	}

	GdiplusStartupInput GPSI;
	GdiplusStartup(&gs_GPToken, &GPSI, NULL);
	setlocale(LC_ALL, "");
	_wsetlocale(LC_ALL, L"");
	
	UNREFERENCED_PARAMETER(hPrevInstance);
	UNREFERENCED_PARAMETER(lpCmdLine);
	int iResult = 0;

	SYSTEMTIME st;
	GetSystemTime(&st);
	wchar_t wsDmpFileName[MAX_PATH];
	wsprintf(wsDmpFileName, L"%d_%d_%d_%d.dmp", st.wDay, st.wHour, st.wMinute, st.wSecond);
	MHSD_CrashDump::CrashDump_Init(wsDmpFileName, L"");

	iResult = LauncherTools::CheckCmdLine(lpCmdLine);
	if (iResult == 0)
	{
		return TRUE;
	}
	else if (iResult <= 0)
	{
		return FALSE;
	}

	gs_hInstance = hInstance;
	iResult = LauncherData::InitData();
	if (iResult != 0)
	{
		return FALSE;
	}
	iResult = LauncherUI::InitUI();
	if (iResult != 0)
	{
		return FALSE;
	}
	
	UpdateEngine UE;
	UE.Initialize();

	if (gs_iDesignMode == 0)
	{
		UE.Run();
	}

	HACCEL hAccelTable;
	hAccelTable = LoadAccelerators(hInstance, MAKEINTRESOURCE(IDC_LAUNCHER));
	MSG msg;
	while (GetMessage(&msg, NULL, 0, 0))
	{
		if (!TranslateAccelerator(msg.hwnd, hAccelTable, &msg))
		{
			TranslateMessage(&msg);
			DispatchMessage(&msg);
			GetDUIWindowManager()->Run();
		}
	}

	ReleaseDownloadManager();
	LauncherUI::UnInitUI();
	GdiplusShutdown(gs_GPToken);
	MHSD_CrashDump::CrashDump_Clean();

	return (int) msg.wParam;
}