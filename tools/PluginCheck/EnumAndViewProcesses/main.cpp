#include <windows.h>
#include <TlHelp32.h>
#include <stdio.h>
#include <tchar.h>

void log(const TCHAR* szFmt, ...)
{
	const int maxLen = 1024;
	TCHAR sz[maxLen];

	va_list vl;
	va_start(vl, szFmt);
	_vsntprintf(sz, maxLen, szFmt, vl);
	va_end(vl);

	_tprintf(sz);
	OutputDebugString(sz);
}

bool ListProcessModules(DWORD processId)
{
	HANDLE hModuleSnap = CreateToolhelp32Snapshot(TH32CS_SNAPMODULE, processId);
	if (hModuleSnap != INVALID_HANDLE_VALUE)
	{
		MODULEENTRY32 me;
		me.dwSize = sizeof(me);

		if (Module32First(hModuleSnap, &me))
		{
			do 
			{
				log(_T("\n\n"));
				log(_T("module name: %s\n"), me.szModule);
				log(_T("executable: %s\n"), me.szExePath);
				log(_T("process Id: %u\n"), me.th32ProcessID);
				log(_T("global ref count: %d\n"), me.GlblcntUsage);
				log(_T("process ref count: %d\n"), me.ProccntUsage);
				log(_T("base address: 0x%08X\n"), (DWORD)me.modBaseAddr);
				log(_T("base size: %u\n"), me.modBaseSize);

			} while (Module32Next(hModuleSnap, &me));
		}

		CloseHandle(hModuleSnap);

		return true;
	}

	return false;
}

bool ListProcessThreads(DWORD processId)
{
	HANDLE hSnap = CreateToolhelp32Snapshot(TH32CS_SNAPTHREAD, processId);
	if (hSnap != INVALID_HANDLE_VALUE)
	{
		THREADENTRY32 te;
		te.dwSize = sizeof(te);

		if (Thread32First(hSnap, &te))
		{
			do 
			{
				if (te.th32OwnerProcessID == processId)
				{
					log(_T("\n\n"));
					log(_T("thread id: %u\n"), te.th32ThreadID);
					log(_T("base priority: %d\n"), te.tpBasePri);
					log(_T("base priority: %d\n"), te.tpDeltaPri);
					log(_T("ref count: %u\n"), te.cntUsage);
				}

			} while (Thread32Next(hSnap, &te));
		}

		CloseHandle(hSnap);
		return true;
	}
	return false;
}

void main()
{
	HANDLE hProcessSnap = CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, 0);
	if (hProcessSnap != INVALID_HANDLE_VALUE)
	{
		PROCESSENTRY32 pe;
		pe.dwSize = sizeof(pe);

		if (Process32First(hProcessSnap, &pe))
		{
			do
			{
				log(_T("\n\n"));
				log(_T("==============================\n"));
				log(_T("process name: %s\n"), pe.szExeFile);
				log(_T("---------------------------------------\n"));

				log(_T("process Id: %u\n"), pe.th32ProcessID);
				log(_T("parent process Id: %u\n"), pe.th32ParentProcessID);
				log(_T("thread count: %u\n"), pe.cntThreads);

				ListProcessModules(pe.th32ProcessID);
				//ListProcessThreads(pe.th32ProcessID);

			} while (Process32Next(hProcessSnap, &pe));
		}

		CloseHandle(hProcessSnap);
	}	
}