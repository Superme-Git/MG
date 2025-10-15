#include <windows.h>
#include <tchar.h>
#include <stdio.h>

typedef void(*SetHookFunc)(DWORD);

void main()
{
	HINSTANCE hDll = LoadLibrary(_T("WaiGuaDll.dll"));
	if (hDll)
	{
		SetHookFunc func = (SetHookFunc)GetProcAddress(hDll, "setHook");
		if (func)
		{
			DWORD processId = 0;

			printf("请输入目标进程Id：\n");
			while (1 != scanf("%u", &processId));

			func(processId);

			HANDLE hProcess = OpenProcess(PROCESS_ALL_ACCESS, false, processId);
			WaitForSingleObject((HANDLE)hProcess, INFINITE);
			CloseHandle(hProcess);
		}

		FreeLibrary(hDll);
	}
}