#include "stdafx.h"
#include <windows.h>
#include <stdio.h>
#include <TCHAR.H>
#include "Cross.h"
#include <tlhelp32.h> // Kernel32.lib.
#include "VMProtectSDK.h"

// #define  USE_SELF_UPDATE

bool Cross_Is64Bit=false;

typedef  BOOL (WINAPI * CreateProcessInternalW)(HANDLE hToken,
					   LPCWSTR lpApplicationName,
					   LPWSTR lpCommandLine,
					   LPSECURITY_ATTRIBUTES lpProcessAttributes,
					   LPSECURITY_ATTRIBUTES lpThreadAttributes,
					   BOOL bInheritHandles,
					   DWORD dwCreationFlags,
					   LPVOID lpEnvironment,
					   LPCWSTR lpCurrentDirectory,
					   LPSTARTUPINFOW lpStartupInfo,
					   LPPROCESS_INFORMATION lpProcessInformation,
					   PHANDLE hNewToken);

typedef  BOOL (WINAPI * CreateProcessInternalA)(HANDLE hToken,
												LPCSTR lpApplicationName,
												LPSTR lpCommandLine,
												LPSECURITY_ATTRIBUTES lpProcessAttributes,
												LPSECURITY_ATTRIBUTES lpThreadAttributes,
												BOOL bInheritHandles,
												DWORD dwCreationFlags,
												LPVOID lpEnvironment,
												LPCSTR lpCurrentDirectory,
												LPSTARTUPINFO lpStartupInfo,
												LPPROCESS_INFORMATION lpProcessInformation,
												PHANDLE hNewToken);

//���������
DWORD WINAPI GetRandom()
{
	DWORD dwRandom = 0;
	DWORD dwTick = 0;
	FILETIME fileTime;
	VMProtectBeginVirtualization
	GetSystemTimeAsFileTime(&fileTime);
	dwRandom = dwRandom ^ fileTime.dwHighDateTime;
	dwRandom = dwRandom ^ fileTime.dwLowDateTime;

	if(Cross_Is64Bit==false)
		GetCount(dwTick)
	else
		dwTick=rand();

	dwRandom = dwRandom ^ dwTick;
	dwRandom = dwRandom ^ dwMagic3;
	VMProtectEnd
	return dwRandom;
}

BYTE WINAPI CheckSum(PBYTE bArray,DWORD dwArrSize)
{
	BYTE bCheck = bArray[0];
	DWORD _dwSize = dwArrSize;
	VMProtectBeginVirtualization
	while(_dwSize>0)
	{
		_dwSize--;
		bCheck ^= bArray[_dwSize];
		bCheck += bArray[dwArrSize-_dwSize-1];
	}
	VMProtectEnd
	return bCheck;
}


//�����������ݣ��洢��pbuffָ���ĵ�ַ
#pragma optimize("",off)
VOID WINAPI CreateProcess_Encrypt(PBYTE pbuff)
{
    DWORD dwTick;
	DWORD dwRandom;
	DWORD dwLastDword;
	DWORD dwPid=0;
	DWORD dwTid=0;
	BYTE bCheckSum;
	BYTE bEncrypt[21] = {0};
	BYTE bRandom[4] = {0};
	VMProtectBeginVirtualization
	
	if(Cross_Is64Bit==false)
	{
		GetCount(dwTick)
	}
	else
		dwTick=GetTickCount();

	dwPid=GetCurrentProcessId();
	Sleep(1);
	//dwTid=GetRandom();

	//dwRandom = GetRandom();
	__asm pushad 
	__asm pushfd 
	__asm lea edi,bRandom 
	__asm mov eax,dwRandom
	__asm stosd 
	__asm lea edi,bEncrypt
	__asm mov eax,dwTick
	__asm stosd
	__asm mov eax,dwPid
	__asm stosd
	__asm lea edi,[edi+5]
	__asm mov eax,dwMagic1
	__asm stosd
	__asm mov eax,dwTid
	__asm stosd
	__asm popfd
	__asm popad

	//loop and xor
	for(int i = 0;i<=4;i++)
	{ 
		Computation(bEncrypt[i],bRandom[3],bRandom[1]);
		Computation(bEncrypt[i+1],bRandom[1],bRandom[2]);
		Computation(bEncrypt[i+2],bRandom[2],bRandom[0]);
		Computation(bEncrypt[i+3],bRandom[0],bRandom[3]);
	} 
	for(int i = 20;i>=16;i--)
	{
		Computation(bEncrypt[i],bRandom[3],bRandom[0]);
		Computation(bEncrypt[i-1],bRandom[0],bRandom[2]);
		Computation(bEncrypt[i-2],bRandom[2],bRandom[1]);
		Computation(bEncrypt[i-3],bRandom[1],bRandom[3]);
	}

	//����У��ͣ����õ��м�λ��
    bCheckSum = CheckSum(bEncrypt,8);
	bCheckSum ^= CheckSum(bEncrypt+13,8);
	bEncrypt[8] = bCheckSum;
	dwLastDword = dwRandom ^ dwMagic1 ^ dwMagic2;
	memcpy(bEncrypt+9,&dwLastDword,4);
	memcpy(pbuff,bEncrypt,21);

	VMProtectEnd
	return;
}



/*
����PP���½���
szPPDir		ָ��PPĿ¼
lpTitleName	ָ�����³���ı���
lpShowText	ָ�����³������ʾ����
lpReOpen	�����Ƿ���Ҫ�ٴδ򿪸��³���
dwLaunchNum	���ǵڼ��δ򿪸��³���

����ֵ
0   �ɹ�
101		���������¼�ʧ��
102		�������¹����ڴ�ʧ��
103		ӳ����¹����ڴ�ʧ��
104		��������ڴ�ʧ��
105		�������½���ʧ��
106		�ȴ������¼���ʱ
107		�ȴ������¼�ʧ��


1001	�������ļ���section����ʧ��
1002	�������ļ���Versֵʧ��
1003	�������ļ���fileֵʧ��
1011	downloadĿ¼�������Ҵ���ʧ��
1012	����version.txtʧ��
1013	����md5ʧ��(�Ѿ��ų��ļ������ڵĴ���)
1014	�����ļ�ʧ��
1015	���������ص��ļ�md5ʧ��
1016	���ص��ļ�md5ֵ��version.txt�еĲ�ͬ
1017	�������������ļ����Ƶ�ppĿ¼ʧ�ܣ���ԭ�򲢲����ļ����ڱ�ռ��

1021	����ppupdate_temp.exeʧ��
1022	������������ʧ��
1029	�û�����ȡ��


*/

DWORD UpdatePP(TCHAR* szPPDir,TCHAR* lpTitleName,TCHAR* lpShowText,DWORD *lpReOpen,DWORD dwLaunchNum)
{
	STARTUPINFO stSI={0};
	PROCESS_INFORMATION stPI={0};
	DWORD dwWait=0;
	TCHAR *lpCmdLine=0;
	TCHAR szDefaultTitle[]=_T("PPUpdate");
	TCHAR szDefaultText[]=_T("ppupdating...");

	TCHAR *szTitle=0;
	TCHAR *szShowText=0;
	DWORD dwError=0;

	DWORD dwBufSize=0;
	HANDLE hBuf=0;
	HANDLE hEvent=0;
	HANDLE hMapFile=0;
	DWORD *lpBuf=0;

	//init event
	hEvent = CreateEvent(NULL,TRUE,FALSE,_T("PP_UPDATE_EVENT"));
	if(hEvent == NULL)
	{
		dwError=101;
		goto _end;
	}

	//init share mem
	hMapFile = CreateFileMapping(INVALID_HANDLE_VALUE,0,PAGE_READWRITE,0,1024,_T("PP_UPDATE_SHAREMEM"));
	if(hMapFile == NULL)
	{
		dwError=102;
		goto _end;
	}


	lpBuf =(DWORD *)MapViewOfFile(hMapFile,FILE_MAP_READ | FILE_MAP_WRITE,0,0,1024);
	if(lpBuf == NULL)
	{
		dwError=103;
		goto _end;
	}

	//create process
	dwBufSize=(lstrlen(szPPDir)+lstrlen(lpTitleName)+lstrlen(lpShowText))*10;
	hBuf=GlobalAlloc(GPTR,dwBufSize);
	lpCmdLine=(TCHAR *)GlobalLock(hBuf);
	if(lpCmdLine==0)
	{
		dwError=104;
		goto _end;
	}

	if(lpTitleName==0)
		szTitle=szDefaultText;
	else
		szTitle=lpTitleName;

	if(lpShowText==0)
		szShowText=szDefaultText;
	else
		szShowText=lpShowText;

	wsprintf(lpCmdLine,_T("%s\\PPUpdate.run %s %s 0 %d"),szPPDir,szTitle,szShowText,dwLaunchNum);
	stSI.cb=sizeof(stSI);
	if(!CreateProcess(0,lpCmdLine,0,0,0,0,0,szPPDir,&stSI,&stPI))
	{
		dwError=105;
		goto _end;
	}

	//wait for 30 mins
	dwWait=WaitForSingleObject(hEvent,30*60*1000);
	if(dwWait==WAIT_TIMEOUT)
	{
		dwError=106;
		goto _end;
	}
	else if(dwWait==WAIT_FAILED)
	{
		dwError=107;
		goto _end;
	}
	
	dwError=lpBuf[0];
	*lpReOpen=lpBuf[1];

_end:
	if(hEvent)
		CloseHandle(hEvent);
	if(lpBuf)
		UnmapViewOfFile(lpBuf);
	if(hMapFile)
		CloseHandle(hMapFile);
	if(lpCmdLine)
	{
		DeleteFile(lpCmdLine);
		GlobalFree(lpCmdLine);
	}
	
	return dwError;
}


typedef   void   (CALLBACK*   _GetNativeSystemInfo)(LPSYSTEM_INFO);
bool Is64BitSystem()
{
	SYSTEM_INFO stSI={0};
	HMODULE hDll=0;
	TCHAR szBuf[512]={0};
	
	VMProtectBeginVirtualization
	_GetNativeSystemInfo lpGetNativeSystemInfo=0;
	hDll=GetModuleHandle(_T("kernel32.dll"));
	if(hDll==0)
	{
		//MessageBox(0,_T("hDll is 0"),_T("test"),0);
		return false;
	}

	lpGetNativeSystemInfo=(_GetNativeSystemInfo)GetProcAddress(hDll,(LPCSTR)"GetNativeSystemInfo");

	if(lpGetNativeSystemInfo==0)
	{
		//wsprintf(szBuf,_T("lpGetNativeSystemInfo is 0,error %d,hdll %x"),GetLastError(),hDll);

		//MessageBox(0,szBuf,_T("test"),0);
		return false;
	}

	lpGetNativeSystemInfo(&stSI);

	if(stSI.wProcessorArchitecture==PROCESSOR_ARCHITECTURE_AMD64 || stSI.wProcessorArchitecture==PROCESSOR_ARCHITECTURE_IA64)
		return true;

	return false;
	VMProtectEnd
}
/*
����ֵ˵����
0		�ɹ���
1		����ͬ���¼�ʧ�ܣ�
2		���������ڴ�ʧ�ܣ�
3		ӳ�乲���ڴ�ʧ�� 
4		�޷�����kernel32.dll��
5		��ȡCreateProcessInternalʧ��
6		������Ϸ����ʧ��
7		��Ϸ���̴����ɹ����ȴ�ͬ���¼�ʧ��
8		��Ϸ���̴����ɹ����ȴ�ͬ���¼���ʱ
		������ԭ����Ϸ������ִ�е���Ӧ����ǰ���쳣����
		��Ϸ�����еĴ��뻹û��ִ�е���Ӧ������봦

101		���������¼�ʧ��
102		�������¹����ڴ�ʧ��
103		ӳ����¹����ڴ�ʧ��
104		��������ڴ�ʧ��
105		�������½���ʧ��
106		�ȴ������¼���ʱ
107		�ȴ������¼�ʧ��


1001	�������ļ���section����ʧ��
1002	�������ļ���Versֵʧ��
1003	�������ļ���fileֵʧ��
1011	downloadĿ¼�������Ҵ���ʧ��
1012	����version.txtʧ��
1013	����md5ʧ��(�Ѿ��ų��ļ������ڵĴ���)
1014	�����ļ�ʧ��
1015	���������ص��ļ�md5ʧ��
1016	���ص��ļ�md5ֵ��version.txt�еĲ�ͬ
1017	�������������ļ����Ƶ�ppĿ¼ʧ�ܣ���ԭ�򲢲����ļ����ڱ�ռ��

1021	����ppupdate_temp.exeʧ��
1022	������������ʧ��
1029	�û�����ȡ��
*/


DWORD WINAPI MyCreateProcess(LPTSTR lpCommandLine,LPCTSTR lpCurrentDirectory,LPCTSTR lpPPDir,LPCTSTR lpTitleName,LPCTSTR lpShowText)
{
	HMODULE hModule;
	DWORD dwReOpen=0;
	DWORD dwLaunchNum=0;
#ifdef _UNICODE
	CreateProcessInternalW pCreateProcessInternalW;
#else
	CreateProcessInternalA pCreateProcessInternalA;
#endif
	PROCESS_INFORMATION pi;
	STARTUPINFO si;
	BOOL bFlag=false;
	DWORD dwError=0;
	//DWORD dwError1=0;
	VMProtectBeginVirtualization

	//check system version
	Cross_Is64Bit=Is64BitSystem();

#ifdef USE_SELF_UPDATE
_re_open:
	//update pp
	dwError=UpdatePP((TCHAR *)lpPPDir,(TCHAR *)lpTitleName,(TCHAR *)lpShowText,&dwReOpen,dwLaunchNum++);
	if(dwError)
		return dwError;
	if(dwReOpen)
	{
		dwReOpen=0;
		goto _re_open;
	}
#endif

//Initial event
	HANDLE hEvent;
	
	hEvent = CreateEvent(NULL,TRUE,FALSE,szEventName);
	if(hEvent == NULL)
	{
		//*lpdwError1 = GetLastError();
		return 1;
	}

//Initial the mem
	HANDLE hMapFile;
	PBYTE pBuff;
	hMapFile = CreateFileMapping(INVALID_HANDLE_VALUE,
		                                            NULL,
		                                            PAGE_READWRITE,
		                                            0,
		                                            256,
		                                            szMapFileName);
	if(hMapFile == NULL)
	{
		//*lpdwError1 = GetLastError();
		CloseHandle(hEvent);
		return 2;
	}
	pBuff = (PBYTE)MapViewOfFile(hMapFile,
		                                             FILE_MAP_ALL_ACCESS,
		                                             0,
		                                             0,
		                                             256);
	if(pBuff == NULL)
	{
		//*lpdwError1 = GetLastError();
		CloseHandle(hMapFile);
		CloseHandle(hEvent);
		return 3;
	}

//Get CreateProcessInternal's Address
	hModule = GetModuleHandle(_T("kernel32.dll"));
	if(!hModule)
	{
		hModule = LoadLibrary(_T("kernel32.dll"));
		if(!hModule)
		{
			//*lpdwError1 = GetLastError();
			CloseHandle(hEvent);
			UnmapViewOfFile(pBuff);
			CloseHandle(hMapFile);
			return 4;
		}
	}
#ifdef _UNICODE
	pCreateProcessInternalW = (CreateProcessInternalW)GetProcAddress(hModule,"CreateProcessInternalW");
	if(!pCreateProcessInternalW)
	{
		//*lpdwError1 = GetLastError();
		CloseHandle(hEvent);
		UnmapViewOfFile(pBuff);
		CloseHandle(hMapFile);
		return 5;
	}
#else
	pCreateProcessInternalA = (CreateProcessInternalA)GetProcAddress(hModule,"CreateProcessInternalA");
	if(!pCreateProcessInternalA)
	{
       // *lpdwError1 = GetLastError();
		CloseHandle(hEvent);
		UnmapViewOfFile(pBuff);
		CloseHandle(hMapFile);
		return 5;
	}
#endif

	//��ʵ�������ʹ��pi�ﷵ�ص�pid and tid���м��ܣ��Ժ��������
	//Get the encrypt context
	CreateProcess_Encrypt(pBuff);
	

	//Call the func with default args
	ZeroMemory(&si,sizeof(si));
	si.cb = sizeof(si);
	si.wShowWindow=SW_SHOW;
	ZeroMemory(&pi,sizeof(pi));
#ifdef  _UNICODE
	bFlag = pCreateProcessInternalW(0,
														   NULL,
														   lpCommandLine,
														   NULL,
														   NULL,
														   FALSE,
														   0,
														   NULL,
														   lpCurrentDirectory,
														   &si,
														   &pi,
														   0);
#else
	bFlag = pCreateProcessInternalA(0,
		NULL,
		lpCommandLine,
		NULL,
		NULL,
		FALSE,
		0,
		NULL,
		lpCurrentDirectory,
		&si,
		&pi,
		0);
#endif



	if(bFlag == FALSE)
	{
		//*lpdwError1 = GetLastError();
		CloseHandle(hEvent);
		UnmapViewOfFile(pBuff);
		CloseHandle(hMapFile);
		return 6;
	}


	//Sync with game.exe by event
    DWORD dwWait;
	dwWait = WaitForSingleObject(hEvent,20000);
	if(dwWait == WAIT_FAILED)
	{
	    //*lpdwError1 = GetLastError();
		UnmapViewOfFile(pBuff);
		CloseHandle(hMapFile);
		CloseHandle(hEvent);
		return 7;
	}
	else if(dwWait==WAIT_TIMEOUT)
	{
	    //*lpdwError1 = GetLastError();
		UnmapViewOfFile(pBuff);
		CloseHandle(hMapFile);
		CloseHandle(hEvent);
		return 8;	
	}

	UnmapViewOfFile(pBuff);
	CloseHandle(hMapFile);
	CloseHandle(hEvent);
	VMProtectEnd
	return 0;
}
#pragma optimize("",on)


// ����̼��(�������Ŀ)
#define MAX_INSTANCE_COUNT CLIENT_NUM_MAX

BOOL WINAPI MultiInstanceCheck(LPCTSTR lpszProcessModuleFileName )
{
	VMProtectBeginVirtualization

	HANDLE hSnapshot = CreateToolhelp32Snapshot( TH32CS_SNAPPROCESS, 0 );
	if ( hSnapshot == INVALID_HANDLE_VALUE  )
	{
		return TRUE;
	}

	PROCESSENTRY32 ProcessEntry = {0};
	ProcessEntry.dwSize = sizeof(PROCESSENTRY32);
	if( !Process32First( hSnapshot, &ProcessEntry ) )
	{
		CloseHandle( hSnapshot );
		return TRUE;
	}

	unsigned int c = 0;

	do
	{
		PathStripPath( ProcessEntry.szExeFile ); // ֻ���������ļ���
		if( _tcsicmp( ProcessEntry.szExeFile, lpszProcessModuleFileName ) == 0 )
		{
			if( ++c >= MAX_INSTANCE_COUNT )
			{
				CloseHandle( hSnapshot );
				return FALSE;
			}
		}

	}while ( Process32Next( hSnapshot, &ProcessEntry ) );

	CloseHandle( hSnapshot );

	VMProtectEnd

	return TRUE;
}
