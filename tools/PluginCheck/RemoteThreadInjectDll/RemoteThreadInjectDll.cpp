#include <stdio.h>
#include <windows.h>
#include <tchar.h>
#include <Strsafe.h>

void main()
{
	printf("enter process Id:\n");

	int processId = 0;
	while (1 != scanf("%d", &processId));

	if (processId != 0)
	{
		HANDLE hProcess = OpenProcess(PROCESS_ALL_ACCESS, false, processId);
		if (hProcess != INVALID_HANDLE_VALUE)
		{
			HMODULE hKernel32 = GetModuleHandle(_T("kernel32"));

#ifdef UNICODE
			PTHREAD_START_ROUTINE loadLibFuncAddr = (PTHREAD_START_ROUTINE)GetProcAddress(hKernel32, "LoadLibraryW");
#else
			PTHREAD_START_ROUTINE loadLibFuncAddr = (PTHREAD_START_ROUTINE)GetProcAddress(hKernel32, "LoadLibraryA");
#endif

			TCHAR curDir[MAX_PATH];
			GetCurrentDirectory(MAX_PATH, curDir);

			TCHAR szDllLocalFilename[MAX_PATH] = { 0 };
			_tcscpy(szDllLocalFilename, curDir);
			_tcscat(szDllLocalFilename, _T("\\WaiGuaDll.dll"));

			int dllFilenameBytesSize = (_tcslen(szDllLocalFilename) + 1) * sizeof(TCHAR);

			TCHAR* szDllRemoteFilename = (TCHAR*)VirtualAllocEx(hProcess, NULL, dllFilenameBytesSize, MEM_COMMIT, PAGE_READWRITE);
			if (szDllRemoteFilename)
			{
				DWORD newProtect = PAGE_READWRITE;
				DWORD oldProtect = 0;
				BOOL bRet = true;
				VirtualProtectEx(hProcess, szDllRemoteFilename, dllFilenameBytesSize, PAGE_READWRITE, &oldProtect);

				bRet = WriteProcessMemory(hProcess, szDllRemoteFilename, szDllLocalFilename, dllFilenameBytesSize, NULL);

				bRet = VirtualProtectEx(hProcess, szDllRemoteFilename, dllFilenameBytesSize, oldProtect, &newProtect);

				HANDLE hThread = CreateRemoteThread(hProcess, NULL, 0, loadLibFuncAddr, szDllRemoteFilename, 0, NULL);
				if (!hThread || hThread == INVALID_HANDLE_VALUE)
				{
					DWORD dw = GetLastError();
					_tprintf(_T("CreateRemoteThread error. err code: %u\n"), dw);
				}
			}

			CloseHandle(hProcess);
		}
	}
}