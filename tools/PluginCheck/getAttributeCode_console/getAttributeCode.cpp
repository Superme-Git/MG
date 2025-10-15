#include <stdio.h>
#include <windows.h>
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

#define CAT_PROTECT_STRING(protect, sz, testProtect)\
	if(protect & testProtect)\
	{\
		if(sz[0] != '\0')\
		{\
			_tcscat(sz, _T("|"));\
		}\
		_tcscat(sz, _T(#testProtect));\
	}

void pageProtectToString(DWORD protect, TCHAR* sz)
{
	CAT_PROTECT_STRING(protect, sz, PAGE_EXECUTE);
	CAT_PROTECT_STRING(protect, sz, PAGE_EXECUTE_READ);
	CAT_PROTECT_STRING(protect, sz, PAGE_EXECUTE_READWRITE);
	CAT_PROTECT_STRING(protect, sz, PAGE_EXECUTE_WRITECOPY);
	CAT_PROTECT_STRING(protect, sz, PAGE_NOACCESS);
	CAT_PROTECT_STRING(protect, sz, PAGE_READONLY);
	CAT_PROTECT_STRING(protect, sz, PAGE_READWRITE);
	CAT_PROTECT_STRING(protect, sz, PAGE_WRITECOPY);
	CAT_PROTECT_STRING(protect, sz, PAGE_GUARD);
	CAT_PROTECT_STRING(protect, sz, PAGE_NOCACHE);
	CAT_PROTECT_STRING(protect, sz, PAGE_WRITECOMBINE);
}

void main()
{
	printf("enter target process id:");
	DWORD processId = 0;
	scanf("%u", &processId);

	HANDLE hProcess = OpenProcess(PROCESS_ALL_ACCESS, FALSE, processId);
	if (hProcess)
	{
		//DWORD startAddr = 0x00400000;
		//DWORD endAddr = 0x7FFFFFFF;
		DWORD startAddr = 0x00000000;
		DWORD endAddr = 0xFFFFFFFF;

		const DWORD pageSize = 4096;

		DWORD testAddr = startAddr;
		log(_T("process memory blocks:\n"));
		while (testAddr < endAddr)
		{
			MEMORY_BASIC_INFORMATION mbi;
			VirtualQueryEx(hProcess, (LPCVOID)testAddr, &mbi, sizeof(mbi));
			log(_T("--------------------------\n"));
			log(_T("addr: %p"), mbi.BaseAddress);
			log(_T("(%s)\n"), (mbi.BaseAddress == (LPCVOID)testAddr ? _T("equal to test addr") : _T("not equal to test addr")));
			log(_T("size: %u\n"), mbi.RegionSize);

			TCHAR szState[32] = { 0 };
			if (MEM_COMMIT == mbi.State)
			{
				_tcscpy(szState, _T("MEM_COMMIT"));
			}
			else if (MEM_FREE == mbi.State)
			{
				_tcscpy(szState, _T("MEM_FREE"));
			}
			else if (MEM_RESERVE == mbi.State)
			{
				_tcscpy(szState, _T("MEM_RESERVE"));
			}
			log(_T("state: %s\n"), szState);
			
			TCHAR szType[32] = { 0 };
			if (MEM_IMAGE == mbi.Type)
			{
				_tcscpy(szType, _T("MEM_IMAGE"));
			}
			else if (MEM_MAPPED == mbi.Type)
			{
				_tcscpy(szType, _T("MEM_MAPPED"));
			}
			else if (MEM_PRIVATE == mbi.Type)
			{
				_tcscpy(szType, _T("MEM_PRIVATE"));
			}
			log(_T("type: %s\n"), szType);

			TCHAR szProtect[1024] = { 0 };
			pageProtectToString(mbi.Protect, szProtect);
			log(_T("protect: %s\n"), szProtect);

			PBYTE tmpBuffer = new BYTE[mbi.RegionSize];
			if (tmpBuffer)
			{
				SIZE_T readBytes = 0;
				if (ReadProcessMemory(hProcess, mbi.BaseAddress, tmpBuffer, mbi.RegionSize, &readBytes))
				{
					log(_T("ReadProcessMemory success! addr=0x%p, readBytes=%u\n"), mbi.BaseAddress, readBytes);
				}
				else
				{
					//log(_T("ReadProcessMemory failed! addr=0x%p, readBytes=%u\n"), mbi.BaseAddress, readBytes);

					if (mbi.State == MEM_COMMIT)
					{
						DWORD oldProtect;
						DWORD newProtect = PAGE_READONLY;

						VirtualProtectEx(hProcess, mbi.BaseAddress, mbi.RegionSize, newProtect, &oldProtect);

						if (ReadProcessMemory(hProcess, mbi.BaseAddress, tmpBuffer, mbi.RegionSize, &readBytes))
						{
							log(_T("ReadProcessMemory success! addr=0x%p, readBytes=%u\n"), mbi.BaseAddress, readBytes);
						}
						else
						{
							log(_T("ReadProcessMemory failed! addr=0x%p, readBytes=%u\n"), mbi.BaseAddress, readBytes);
						}

						VirtualProtectEx(hProcess, mbi.BaseAddress, mbi.RegionSize, oldProtect, &newProtect);
					}
					else
					{
						log(_T("ReadProcessMemory failed! addr=0x%p, readBytes=%u\n"), mbi.BaseAddress, readBytes);
					}
				}

				delete[] tmpBuffer;
			}

			if (endAddr - testAddr > mbi.RegionSize)
			{
				testAddr += mbi.RegionSize;
			}
			else
			{
				break;
			}
		}

		log(_T("\n"));
		log(_T("ReadProcessMemory:\n"));

		DWORD readAddr = startAddr;
		while (readAddr < endAddr)
		{
			BYTE page[pageSize];
			SIZE_T readBytes = 0;
			if (ReadProcessMemory(hProcess, (LPCVOID)readAddr, page, pageSize, &readBytes))
			{
				log(_T("ReadProcessMemory success! addr=0x%p, readBytes=%u\n"), (LPCVOID)readAddr, readBytes);
			}
			else
			{
				log(_T("ReadProcessMemory failed! addr=0x%p, readBytes=%u\n"), (LPCVOID)readAddr, readBytes);
			}

			if (endAddr - readAddr > pageSize)
			{
				readAddr += pageSize;
			}
			else
			{
				break;
			}
		}

		CloseHandle(hProcess);
		hProcess = NULL;
	}
}