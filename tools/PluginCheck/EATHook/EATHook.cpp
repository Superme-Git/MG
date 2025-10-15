#include <stdlib.h>
#include <windows.h>
#include <DbgHelp.h>
#include <tchar.h>

bool EATHook(const TCHAR* szDllName, const char* szFuncName, DWORD newFunc)
{
	bool bFound = false;
	DWORD index = 0;

	HMODULE hMod = LoadLibrary(szDllName);
	PIMAGE_DOS_HEADER pDosHeader = (PIMAGE_DOS_HEADER)hMod;
	PIMAGE_OPTIONAL_HEADER pOptionalHeader = (PIMAGE_OPTIONAL_HEADER)((DWORD)hMod + pDosHeader->e_lfanew + 24);
	PIMAGE_EXPORT_DIRECTORY pExport = (PIMAGE_EXPORT_DIRECTORY)((PBYTE)pDosHeader + pOptionalHeader->DataDirectory[IMAGE_DIRECTORY_ENTRY_EXPORT].VirtualAddress);
	PULONG pFuncAddrs = (PULONG)((PBYTE)hMod + pExport->AddressOfFunctions);
	PULONG pNameAddrs = (PULONG)((PBYTE)hMod + pExport->AddressOfNames);
	PUSHORT pNameOrdinalAddrs = (PUSHORT)((PBYTE)hMod + pExport->AddressOfNameOrdinals);
	for (DWORD i = 0; i < pExport->NumberOfNames; ++i)
	{
		char* pFuncName = (char*)((PBYTE)hMod + pNameAddrs[i]);
		if (_stricmp(pFuncName, szFuncName) == 0)
		{
			index = pNameOrdinalAddrs[i];
			bFound = true;
			break;
		}
	}

	if (bFound)
	{
		DWORD protect = 0x1000;
		DWORD oldProtect = 0;

		VirtualProtect(&pFuncAddrs[index], protect, PAGE_READWRITE, &oldProtect);

		pFuncAddrs[index] = (DWORD)newFunc - (DWORD)hMod;
		
		VirtualProtect(&pFuncAddrs[index], 0x1000, oldProtect, &protect);
	}

	return true;
}

int test_function()
{
	MessageBox(NULL, _T("hook_entry function"), _T("hook"), MB_OK);
	return 0;
}

void main()
{
	DWORD pid = GetCurrentProcessId();
	EATHook(_T("kernel32.dll"), "GetCurrentProcessId", (DWORD)&test_function);

	pid = GetCurrentProcessId();
}