#include <stdio.h>
#include <windows.h>
#include <DbgHelp.h>
#include <tchar.h>

int test_function()
{
	MessageBox(NULL, _T("hook_entry function"), _T("hook"), MB_OK);
	return 0;
}

class IATHook
{
private:
	char mModuleName[256];
	char mProcName[256];
	PROC* mNewFuncAddr;

	HMODULE mhModule;
	bool mbNeedFree;
	PROC mFuncAddr;

public:
	IATHook::IATHook(const char* szModuleName, const char* szProcName, PROC* proc)
		: mNewFuncAddr(proc)
		, mbNeedFree(false)
		, mFuncAddr(NULL)
	{
		strcpy(mModuleName, szModuleName);
		strcpy(mProcName, szProcName);

		mhModule = GetModuleHandleA(mModuleName);
		if (!mhModule)
		{
			mhModule = LoadLibraryA(szModuleName);
			mbNeedFree = true;
		}
		if (mhModule)
		{
			mFuncAddr = GetProcAddress(mhModule, mProcName);
		}
	}

	bool hook()
	{
		bool bSuccess = false;

		ULONG importDescSize = 0;
		HMODULE hModule = GetModuleHandle(NULL);
		PIMAGE_IMPORT_DESCRIPTOR pImportDesc = (PIMAGE_IMPORT_DESCRIPTOR)ImageDirectoryEntryToData(hModule, TRUE, IMAGE_DIRECTORY_ENTRY_IMPORT, &importDescSize);
		while (pImportDesc && pImportDesc->Name)
		{
			PSTR szModName = (PSTR)((PBYTE)hModule + pImportDesc->Name);
			if (_stricmp(szModName, mModuleName) == 0)
			{
				break;
			}

			++pImportDesc;
		}

		if (pImportDesc && pImportDesc->Name)
		{
			PIMAGE_THUNK_DATA pThunk = (PIMAGE_THUNK_DATA)((PBYTE)hModule + pImportDesc->FirstThunk);
			while (pThunk->u1.Function)
			{
				PROC* ppfn = (PROC*)&pThunk->u1.Function;
				if (*ppfn == mFuncAddr)
				{
					MEMORY_BASIC_INFORMATION mbi;
					VirtualQuery(ppfn, &mbi, sizeof(mbi));
					VirtualProtect(mbi.BaseAddress, mbi.RegionSize, PAGE_READWRITE, &mbi.Protect);

					*ppfn = *mNewFuncAddr;

					DWORD oldProtect;
					VirtualProtect(mbi.BaseAddress, mbi.RegionSize, mbi.Protect, &oldProtect);

					bSuccess = true;

					break;
				}

				++pThunk;
			}
		}

		return bSuccess;
	}
};

void main()
{
	PROC stub = (PROC)&test_function;

	IATHook ihook("kernel32.dll", "GetCurrentProcessId", &stub);
	ihook.hook();

	DWORD pid = GetCurrentProcessId();
	printf("pid=%u\n", pid);
}