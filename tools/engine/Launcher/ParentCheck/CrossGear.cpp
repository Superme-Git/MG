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

//产生随机数
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


//产生加密内容，存储进pbuff指定的地址
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

	//运算校验和，放置到中间位置
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
创建PP更新进程
szPPDir		指定PP目录
lpTitleName	指定更新程序的标题
lpShowText	指定更新程序的显示名称
lpReOpen	返回是否需要再次打开更新程序
dwLaunchNum	这是第几次打开更新程序

返回值
0   成功
101		创建更新事件失败
102		创建更新共享内存失败
103		映射更新共享内存失败
104		分配更新内存失败
105		创建更新进程失败
106		等待更新事件超时
107		等待更新事件失败


1001	读配置文件的section名称失败
1002	读配置文件的Vers值失败
1003	读配置文件的file值失败
1011	download目录不存在且创建失败
1012	下载version.txt失败
1013	计算md5失败(已经排除文件不存在的错误)
1014	下载文件失败
1015	计算新下载的文件md5失败
1016	下载的文件md5值和version.txt中的不同
1017	把下载下来的文件复制到pp目录失败，但原因并不是文件正在被占用

1021	复制ppupdate_temp.exe失败
1022	创建副本进程失败
1029	用户点了取消


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
返回值说明：
0		成功；
1		创建同步事件失败；
2		创建共享内存失败；
3		映射共享内存失败 
4		无法加载kernel32.dll；
5		获取CreateProcessInternal失败
6		创建游戏进程失败
7		游戏进程创建成功，等待同步事件失败
8		游戏进程创建成功，等待同步事件超时
		有两个原因：游戏进程在执行到响应请求前就异常结束
		游戏进程中的代码还没有执行到响应请求代码处

101		创建更新事件失败
102		创建更新共享内存失败
103		映射更新共享内存失败
104		分配更新内存失败
105		创建更新进程失败
106		等待更新事件超时
107		等待更新事件失败


1001	读配置文件的section名称失败
1002	读配置文件的Vers值失败
1003	读配置文件的file值失败
1011	download目录不存在且创建失败
1012	下载version.txt失败
1013	计算md5失败(已经排除文件不存在的错误)
1014	下载文件失败
1015	计算新下载的文件md5失败
1016	下载的文件md5值和version.txt中的不同
1017	把下载下来的文件复制到pp目录失败，但原因并不是文件正在被占用

1021	复制ppupdate_temp.exe失败
1022	创建副本进程失败
1029	用户点了取消
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

	//其实这里可以使用pi里返回的pid and tid进行加密，以后添加试试
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


// 多进程检测(多进程数目)
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
		PathStripPath( ProcessEntry.szExeFile ); // 只保留进程文件名
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
