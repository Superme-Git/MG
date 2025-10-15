#include "BugReport.h"
#include <dbghelp.h>


#pragma comment(lib,"dbghelp.lib")



/*
GetDumpInfo_ReadMemory ���ڶ�ȡdump�ļ�ָ����ַ�����ڴ�������Ϣ����ģ��������ջ����
����1��lpMappedAddr dump�ļ�ӳ���ַ
����2��dwEbp ��Ҫ��ȡ�����Ϣ��ebp��ַ
����3��dwSize ��ȡ�ڴ�ĳ���
����4��lpBuff ���
����ֵ��0.��ȡdump�ļ��������δ����������ڴ�����   1.�ɹ���ȡ   2.��Ҫ��ȡ���ڴ泤�ȳ������ƣ���ģ�鲻������������
*/
DWORD WINAPI GetDumpInfo_ReadMemory(PVOID lpMappedAddr,DWORD dwEbp,DWORD dwSize,PVOID lpBuff)
{
	PVOID lpStream;
	DWORD dwNumber = 0;
	DWORD dwEnd = 0;
	DWORD dwStart = 0;
	DWORD dwRemain = 0;
	DWORD dwRtn = 0;
	DWORD dwSourceAddr = 0;
	DWORD dwMappedAddr;
	LPVOID lpSourceAddr;
	PMINIDUMP_MEMORY_DESCRIPTOR pMd = 0;
	RtlCopyMemory(&dwMappedAddr,&lpMappedAddr,4);
	if(!MiniDumpReadDumpStream(lpMappedAddr,MemoryListStream,0,&lpStream,0))
	{
		Log(_TEXT("GetDumpInfo_ReadMemory at Ebp: %08x, Error: %d\r\n"),dwEbp,GetLastError());
		return dwRtn;
	}
	dwNumber = ((PMINIDUMP_MEMORY_LIST)lpStream)->NumberOfMemoryRanges;
	pMd = &(((PMINIDUMP_MEMORY_LIST)lpStream)->MemoryRanges[0]);
	while(dwNumber)
	{
		dwNumber--;
		dwStart = (DWORD)pMd->StartOfMemoryRange;
		dwEnd = pMd->Memory.DataSize + dwStart;
		if((dwEbp <= dwEnd) && (dwEbp >= dwStart))
		{
			dwRemain = dwEnd - dwEbp;
			if(dwRemain > dwSize)
			{
				dwSourceAddr = dwMappedAddr + pMd->Memory.Rva + dwEbp - dwStart; 
				RtlCopyMemory(&lpSourceAddr,&dwSourceAddr,4);
				RtlCopyMemory(lpBuff,lpSourceAddr,dwSize);
				dwRtn = 1;
			}
			else
				dwRtn = 2;
			break;
		}
		pMd += 1;
	}
	return dwRtn;
}

/*
GetDumpInfo_GetAddressModule ��ȡ��ǰ��ַ���ڵ�ģ����Ϣ
����1��lpMappedAddr dump�ļ�ӳ���ַ
����2��dwRetAddr   ��Ҫ��ȡģ����Ϣ�ĵ�ַ
����3��pdwOffset    �����ģ���ڲ�ƫ��
����ֵ��0.��ȡdump�ļ�ʧ�� other:��Ӧģ���PMINIDUMP_STRING 
*/
PMINIDUMP_STRING WINAPI GetDumpInfo_GetAddressModule(PVOID lpMappedAddr,DWORD dwRetAddr,DWORD *pdwOffset)
{
	PMINIDUMP_DIRECTORY stMd = 0;
	PVOID lpStream = 0;
	DWORD dwStreamSize = 0;
	DWORD dwNumber = 0;
	BOOL bFound = 0;
	PMINIDUMP_MODULE pM = 0;
	PMINIDUMP_STRING lpFileName = 0;
	DWORD dwMappedAddr,dwFileName;
	RtlCopyMemory(&dwMappedAddr,&lpMappedAddr,4);
	//LPWSTR lpRetAddr = 0;
	if(!MiniDumpReadDumpStream(lpMappedAddr,ModuleListStream,&stMd,&lpStream,&dwStreamSize))
	{
		Log(_TEXT("GetDumpInfo_GetAddressModule:Read DumpFile Error: %d\r\n"),GetLastError());
		return lpFileName;
	}
	dwNumber = ((PMINIDUMP_MODULE_LIST)lpStream)->NumberOfModules;
	pM = &(((PMINIDUMP_MODULE_LIST)lpStream)->Modules[0]);
	while(dwNumber)
	{
		dwNumber--;
		if((dwRetAddr <= pM->BaseOfImage + pM->SizeOfImage)&&(dwRetAddr >= pM->BaseOfImage))
		{
			if(pdwOffset) //Ҫ�󷵻�ƫ�Ƶ�ַ
				*pdwOffset = dwRetAddr - (DWORD)pM->BaseOfImage;
			dwFileName = pM->ModuleNameRva + dwMappedAddr;
			RtlCopyMemory(&lpFileName,&dwFileName,4);
			bFound = TRUE;
			break;
		}
		pM += 1;
	}
	return lpFileName;
}

BOOL WINAPI GetDumpInfo_SystemDummyInfo(LPBYTE lpBuffer, PartInfo* pPartArray)
{
	DWORD dwNumber = 3;

	OSVERSIONINFO version = {0};
	version.dwOSVersionInfoSize = sizeof( version);
	BOOL bRet = GetVersionEx( &version );	
	if( bRet )
	{
		dwNumber = swprintf_s((wchar_t *)lpBuffer,100,L"%d.%d.%d.%ws",
			version.dwMajorVersion,version.dwMinorVersion,version.dwBuildNumber,version.szCSDVersion);
		pPartArray[OSVersion].lpContent = (LPWSTR)lpBuffer;
		pPartArray[OSVersion].dwInfoLen = dwNumber*2;
	}
	else
	{
		wcscpy_s((wchar_t*)lpBuffer, dwNumber+1, L"N/A");
		pPartArray[OSVersion].lpContent = (LPWSTR)lpBuffer;
		pPartArray[OSVersion].dwInfoLen = dwNumber*2;
	}
	return TRUE;
}

BOOL WINAPI GetDumpInfo_DummyInfo(PartInfo* pPartArray)
{
	DWORD dwNumber = 3;
	LPBYTE lpBuffer = (LPBYTE)HeapAlloc(GetProcessHeap(), HEAP_ZERO_MEMORY, 256);

	wcscpy_s((wchar_t*)lpBuffer, dwNumber+1, L"N/A");
	pPartArray[ExceptionCode].dwInfoLen = dwNumber*2;
	pPartArray[ExceptionCode].lpContent = (LPWSTR)lpBuffer;
	lpBuffer += dwNumber*2;
	
	wcscpy_s((wchar_t*)lpBuffer, dwNumber+1, L"N/A");
	pPartArray[ExceptionAddress].dwInfoLen = dwNumber*2;
	pPartArray[ExceptionAddress].lpContent = (LPWSTR)lpBuffer;
	lpBuffer += dwNumber*2;

	wcscpy_s((wchar_t*)lpBuffer, dwNumber+1, L"N/A");
	pPartArray[Register].dwInfoLen = dwNumber*2;
	pPartArray[Register].lpContent = (LPWSTR)lpBuffer;
	lpBuffer += dwNumber*2;

	wcscpy_s((wchar_t*)lpBuffer, dwNumber+1, L"N/A");
	pPartArray[StackInfo].lpContent = (LPWSTR)lpBuffer;
	pPartArray[StackInfo].dwInfoLen = dwNumber*2;
	lpBuffer += dwNumber*2;

	GetDumpInfo_SystemDummyInfo( lpBuffer, pPartArray);

	Log(_TEXT("OSVersion set into Array\r\n"));
	return TRUE;
}



/*
GetDumpInfo_GetInfo GetDumpInfo�����ӿڣ���postupģ������Գ�ʼ��partinfo�����ж�Ӧ������
����:1 IN lpMappedFile ӳ�䵽�ڴ��е�dmp�ļ���ַ��2 IN pPartArray ��Ҫ��ʼ��������Ϣ������
����: TRUE or FALSE
*/
DWORD WINAPI GetDumpInfo_GetInfo(PVOID lpMappedFile,PartInfo* pPartArray)
{
	PMINIDUMP_DIRECTORY pstMd = 0;
	PVOID lpStream = 0;
	DWORD dwStreamSize = 0,dwNumber = 0,dwPerNumber = 0,dwLastEbp = 0,dwEbp = 0,dwReturnAddr = 0;
	DWORD dwRtn = 0,dwOffset = 0,dwModuleLen = 0;
	DWORD dwMajorVersion = 0,dwMinorVersion = 0,dwBuildNumber = 0,dwBuffSize = 0,dwCSD = 0;
	
	LPWSTR lpszCSDVersion = 0;
	LPSTR lpMultiCSD = 0;
	PMINIDUMP_STRING lpModule = 0;
	LPWSTR lpszModuleName = 0;
	WCHAR wcSlash = '\\';
	LPBYTE lpBuffer = 0;
	//LPSTR lpMultiModule = 0;
	BOOL bDefaultUsed = 0;

	//��������ת��
	DWORD dwMappedFile;
	DWORD dwRva;
	RtlCopyMemory(&dwMappedFile,&lpMappedFile,4);
	//����ѿռ䣬���ڴ����������

	lpBuffer = (LPBYTE)HeapAlloc(GetProcessHeap(), HEAP_ZERO_MEMORY, 8192);
	
	if(!MiniDumpReadDumpStream(lpMappedFile,ExceptionStream,&pstMd,&lpStream,&dwStreamSize)) //�Ȼ�ȡ�쳣�����Ϣ
	{
		Log(_TEXT("GetDumpInfo_GetInfo:Read DumpFile Error: %d\r\n"),GetLastError());
		return -1;
	}
	//����쳣��Ϣ�������������ݣ�
	DWORD dwExceptionCode = ((PMINIDUMP_EXCEPTION_STREAM)lpStream)->ExceptionRecord.ExceptionCode; //�ֶ�1
	dwNumber = swprintf_s((wchar_t *)lpBuffer,16,L"%08x",dwExceptionCode);
	pPartArray[ExceptionCode].dwInfoLen = dwNumber*2;
	pPartArray[ExceptionCode].lpContent = (LPWSTR)lpBuffer;
	lpBuffer = lpBuffer + dwNumber*2;
	Log(_TEXT("ExceptionCode set into Array\r\n"));

	DWORD dwExceptionAddress = (DWORD)((PMINIDUMP_EXCEPTION_STREAM)lpStream)->ExceptionRecord.ExceptionAddress;//�ֶ�2
	dwNumber = swprintf_s((wchar_t *)lpBuffer,16,L"%08x",dwExceptionAddress);
	pPartArray[ExceptionAddress].dwInfoLen = dwNumber*2;
	pPartArray[ExceptionAddress].lpContent = (LPWSTR)lpBuffer;
	lpBuffer = lpBuffer + dwNumber*2;
	Log(_TEXT("ExceptionAddress set into Array\r\n"));

	// ����쳣����ʱ�Ĵ�����ֵ
	PCONTEXT pCon;
	dwRva = ((PMINIDUMP_EXCEPTION_STREAM)lpStream)->ThreadContext.Rva + dwMappedFile;
	RtlCopyMemory(&pCon,&dwRva,4);
	//pCon->Eax,ebx,ecx.....
	dwNumber = swprintf_s( (wchar_t*)lpBuffer, 
				1024, 
				L"EAX=%08X EBX=%08X ECX=%08X EDX=%08X ESI=%08X EDI=%08X EIP=%08X ESP=%08X EBP=%08X EFLAGS=%08X",
				pCon->Eax, pCon->Ebx, pCon->Ecx, pCon->Edx, pCon->Esi, pCon->Edi, 
				pCon->Eip, pCon->Esp, pCon->Ebp, pCon->EFlags);
	dwNumber++;
	pPartArray[Register].dwInfoLen = dwNumber*2;
	pPartArray[Register].lpContent = (LPWSTR)lpBuffer;
	lpBuffer = lpBuffer + dwNumber*2;

	//����쳣������ջ��Ϣ
	//�ֶ�3
	dwEbp = pCon->Ebp;
	dwNumber = 0;
	pPartArray[StackInfo].lpContent = (LPWSTR)lpBuffer;
	while(dwEbp) //ջ����
	{
		if( dwNumber*2 > 7168)
		{
			break;
		}
		if(!GetDumpInfo_ReadMemory(lpMappedFile,dwEbp,4,&dwLastEbp))
		{
			Log(_TEXT("Stack reverse end dwEbp:%08x\r\n"),dwEbp);
			break;
		}
		if(!GetDumpInfo_ReadMemory(lpMappedFile,dwEbp+4,4,&dwReturnAddr))
		{
			Log(_TEXT("Stack reverse find rtnAddr Error:%08x\r\n"),dwEbp+4);
			break;
		}
		lpModule = GetDumpInfo_GetAddressModule(lpMappedFile,dwReturnAddr,&dwOffset);
		if(lpModule)
		{
			//���dwReturnAddr,lpszModuleName,dwOffset
			lpszModuleName = wcsrchr(lpModule->Buffer,wcSlash)+1;
			dwModuleLen = (DWORD)wcslen(lpszModuleName)*2;
			//lpMultiModule = (LPSTR)malloc(dwModuleLen);
			//WideCharToMultiByte(CP_ACP,WC_NO_BEST_FIT_CHARS,lpszModuleName,dwModuleLen/2+1,lpMultiModule,dwModuleLen,"\xff",&bDefaultUsed);
			dwPerNumber = swprintf_s((wchar_t *)lpBuffer,800,L"%08x %ws+%08x\r\n",dwReturnAddr,lpszModuleName,dwOffset);
			//free(lpMultiModule);
			dwNumber += dwPerNumber;
			lpBuffer = lpBuffer + dwPerNumber*2;
		}
		else
		{
			//���dwReturnAddr,NotFound
			dwPerNumber = swprintf_s((wchar_t *)lpBuffer,800,L"%08x Module Not Found\r\n",dwReturnAddr);
			dwNumber += dwPerNumber;
			lpBuffer = lpBuffer + dwPerNumber*2;
			break;
		}
		dwEbp = dwLastEbp;
	}
	pPartArray[StackInfo].dwInfoLen = dwNumber*2;
	Log(_TEXT("StackInfo set into Array\r\n"));


	//�������ϵͳ�汾
	if(!MiniDumpReadDumpStream(lpMappedFile,SystemInfoStream,&pstMd,&lpStream,&dwStreamSize)) //�Ȼ�ȡ�쳣�����Ϣ
	{
		GetDumpInfo_SystemDummyInfo( lpBuffer, pPartArray);
		return 0;
	}
	dwMajorVersion = ((PMINIDUMP_SYSTEM_INFO)lpStream)->MajorVersion;
	dwMinorVersion = ((PMINIDUMP_SYSTEM_INFO)lpStream)->MinorVersion;
	dwBuildNumber = ((PMINIDUMP_SYSTEM_INFO)lpStream)->BuildNumber;
	dwRva = ((PMINIDUMP_SYSTEM_INFO)lpStream)->CSDVersionRva + dwMappedFile + 4;
	RtlCopyMemory(&lpszCSDVersion,&dwRva,4);
	/*dwCSD = (DWORD)wcslen(lpszCSDVersion);
	lpMultiCSD = (LPSTR)malloc(dwCSD+1);
	WideCharToMultiByte(CP_ACP,WC_NO_BEST_FIT_CHARS,lpszCSDVersion,dwCSD+1,\
					lpMultiCSD,dwCSD+1,"\xff",&bDefaultUsed);*/
	dwNumber = swprintf_s((wchar_t *)lpBuffer,100,L"%d.%d.%d.%ws",dwMajorVersion,dwMinorVersion,dwBuildNumber,lpszCSDVersion);
	pPartArray[OSVersion].lpContent = (LPWSTR)lpBuffer;
	pPartArray[OSVersion].dwInfoLen = dwNumber*2;
	Log(_TEXT("OSVersion set into Array\r\n"));

	return 0;
}

