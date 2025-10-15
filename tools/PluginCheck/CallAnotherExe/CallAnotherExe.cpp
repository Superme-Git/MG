#include <stdio.h>
#include <Windows.h>
#include <tchar.h>

void main()
{
	STARTUPINFO startupInfo;
	ZeroMemory(&startupInfo, sizeof(startupInfo));
	startupInfo.cb = sizeof(startupInfo);

	PROCESS_INFORMATION processInfo;
	if (CreateProcess(_T("dummyWinApp.exe"), NULL, NULL, NULL, FALSE, 0/*CREATE_SUSPENDED*/, NULL, NULL, &startupInfo, &processInfo))
	{
		WaitForSingleObject(processInfo.hProcess, 10000);
		//PostThreadMessage(processInfo.dwThreadId, WM_QUIT, 1, 0);
		//WaitForSingleObject(processInfo.hProcess, 3000);

		//TerminateProcess(processInfo.hProcess, 2);
	}
}