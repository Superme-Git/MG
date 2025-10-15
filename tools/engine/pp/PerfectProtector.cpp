#include "stdafx.h"
#include <windows.h>
#include <tchar.h>
#include <winioctl.h>	//用于i/o IRP
#include <Aclapi.h>		//用于修改服务属性和参数

#include "native.h"
#include "PerfectProtector.h"
#include "VMProtectSDK.h"

#pragma comment(lib,"shlwapi.lib")
#pragma comment(lib,"ntdll.lib")
#pragma comment(lib,"kernel32.lib")

namespace Protector
{
//#define	protector_debug	1
#define protector_logdat 1

#define	IOCTL_INSERT_PID		CTL_CODE(FILE_DEVICE_UNKNOWN,0x800, METHOD_BUFFERED, FILE_READ_ACCESS + FILE_WRITE_ACCESS)
#define	IOCTL_DEL_PID			CTL_CODE(FILE_DEVICE_UNKNOWN,0x801, METHOD_BUFFERED, FILE_READ_ACCESS + FILE_WRITE_ACCESS)
#define	IOCTL_INIT_SYSTEM_DIR	CTL_CODE(FILE_DEVICE_UNKNOWN,0x802, METHOD_BUFFERED, FILE_READ_ACCESS + FILE_WRITE_ACCESS)

	HANDLE	hDevice=0;
	int		hLeaveEvent=0;
	char	*lpLoadLibraryExW;
	char	dwOldLoadLibraryExW[5];
	char	dwPatchLoadLibraryExW[5];
	char	*lpClientLoadLibraryBegin;
	char	*lpClientLoadLibraryEnd;
	int		dwTableIndex=0;
	int		dwParamLibrary=0;

	int		dwIdNtOpenProcess=0;
	int		dwIdNtReadVirtualMemory=0;
	int		dwIdNtWriteVirtualMemory=0;
	int		dwIdNtGetContextThread=0;

	DWORD  Common_Last_CheckDate=0;

	bool	bPrivilegeNotHold=false;

	TCHAR	szWhiteDll1[64];
	TCHAR	szWhiteDll2[64];

	PWCHAR szWhiteDll3=L"uxtheme.dll";

	TCHAR	*szServiceName=_T("SecRomDrv");
	TCHAR	*szDisplayName=_T("Secure CDROM Property");
	TCHAR	*szImageFileName=_T("system32\\DRIVERS\\cdrom.sys");

#ifdef	protector_logdat
	HANDLE	hLogFile;
#pragma optimize("y", off)
	void _Log(...)
	{
		char szBuf[512]={0};
		SYSTEMTIME	stTime;
		int	dwSize;
		unsigned long	dwRtn;

		GetLocalTime(&stTime);
		__asm 
		{
			pushad
			pushfd
			lea		esi,[ebp+8h]	;参数列表
			lea		edi,szBuf
			cld 
			lodsd					;取ID
			mov		ecx,eax
			lodsd					;取参数个数
			mov		edx,eax

			inc	eax					;计算要写入文件的内存大小
			inc	eax
			shl	eax,2
			mov	dwSize,eax
			stosb

			movzx	eax,stTime.wHour	;写入时间信息
			stosb
			movzx	eax,stTime.wMinute
			stosb
			movzx	eax,stTime.wSecond
			stosb
			
			mov		eax,ecx
			stosd						;写入ID
			mov		byte ptr [edi-1],1	;写入ring3标记

			mov		ecx,edx				;写入剩余参数
			rep		movsd
			popfd
			popad
		}
		SetFilePointer(hLogFile,0,0,FILE_END);
		WriteFile(hLogFile,szBuf,dwSize,&dwRtn,0);
	}
	void _LogErrorCode(...)
	{
		char szBuf[512]={0};
		SYSTEMTIME	stTime;
		int	dwSize;
		unsigned long	dwRtn;

		GetLocalTime(&stTime);
		__asm 
		{
			pushad
			pushfd
			lea		esi,[ebp+8h]	;参数列表
			lea		edi,szBuf
			cld 
			lodsd					;取ID
			mov		ecx,eax
			lodsd					;取参数个数
			mov		edx,eax

			inc	eax					;计算要写入文件的内存大小
			inc	eax
			shl	eax,2
			mov	dwSize,eax
			stosb

			movzx	eax,stTime.wHour	;写入时间信息
			stosb
			movzx	eax,stTime.wMinute
			stosb
			movzx	eax,stTime.wSecond
			stosb

			mov		eax,ecx
			stosd						;写入ID
			mov		byte ptr [edi-1],3	;写入ring3,ErrorCode标记

			mov		ecx,edx				;写入剩余参数
			rep		movsd
			popfd
			popad
		}
		SetFilePointer(hLogFile,0,0,FILE_END);
		WriteFile(hLogFile,szBuf,dwSize,&dwRtn,0);
	}

	HANDLE	_InitLog(TCHAR *lpFileName)
	{
		HANDLE hFile;
		SYSTEMTIME	stTime;
		TCHAR szBuf[512]={0};
		//	DWORD dwSize;
		//	unsigned long	dwRtn;
		hFile=CreateFile(lpFileName,GENERIC_WRITE,FILE_SHARE_READ | FILE_SHARE_WRITE,\
			0,CREATE_NEW,FILE_ATTRIBUTE_NORMAL,0);
		if(hFile==INVALID_HANDLE_VALUE || hFile==0)
		{
			hFile=CreateFile(lpFileName,GENERIC_WRITE,FILE_SHARE_READ | FILE_SHARE_WRITE,\
				0,OPEN_EXISTING,FILE_ATTRIBUTE_NORMAL,0);
			if(hFile==INVALID_HANDLE_VALUE || hFile==0)
				hFile=0;
		}

		hLogFile=hFile;

		if(hFile)
		{	
			//记录当前日期
			GetLocalTime(&stTime);
			_Log(1,4,stTime.wYear,stTime.wMonth,stTime.wDay,GetCurrentProcessId());
#ifdef _UNICODE
			_Log(2,0);
#else
			_Log(3,0);
#endif
		}
		return hFile;
	}
#pragma optimize("",  on)
	int	_ErrorRet(DWORD dwID)
	{
		int	dwErrCode;
		//TCHAR	*lpMsg=0;
		dwErrCode=GetLastError();
		//FormatMessage(FORMAT_MESSAGE_FROM_SYSTEM|FORMAT_MESSAGE_ALLOCATE_BUFFER,0,dwErrCode,0,(TCHAR *)&lpMsg,0,0);
//#ifdef	protector_debug
		//_Log(_T("%s : %d %s"),_lpStr,dwErrCode,lpMsg);
		_LogErrorCode(dwID,1,dwErrCode);
//#endif
		//LocalFree(lpMsg);
		return dwErrCode;
	}

	int	_SetDebugEnv()
	{
		HKEY hKey=0;
		TCHAR szBuf[MAX_PATH]={0};
		int	 dwValue,dwPos;
		if(RegCreateKey(HKEY_LOCAL_MACHINE,_T("SYSTEM\\CurrentControlSet\\Control\\CrashControl"),&hKey)==ERROR_SUCCESS)
		{
			dwValue=1;
			if(RegSetValueEx(hKey,_T("AutoReboot"),0,REG_DWORD,(const unsigned char *)&dwValue,4))
			{
#ifdef	protector_logdat
				_ErrorRet(97);
#endif			
				RegCloseKey(hKey);
				return 0;
			}
			if(RegSetValueEx(hKey,_T("Overwrite"),0,REG_DWORD,(const unsigned char *)&dwValue,4))
			{
#ifdef	protector_logdat
				_ErrorRet(98);
#endif			
				RegCloseKey(hKey);
				return 0;
			}

			dwValue=3;
			if(RegSetValueEx(hKey,_T("CrashDumpEnabled"),0,REG_DWORD,(const unsigned char *)&dwValue,4))
			{
#ifdef	protector_logdat
				_ErrorRet(99);
#endif			
				RegCloseKey(hKey);
				return 0;
			}

			dwPos=GetCurrentDirectory(MAX_PATH,szBuf);
			//这里要判断是不是根目录，如果是，返回的是c:\，如果不是，返回的是c:\file
			if(szBuf[dwPos-1]!=_T('\\'))
				szBuf[dwPos]=_T('\\');
			lstrcat(szBuf,_T("log_memory.dmp"));

			//RegSetValueEx(hKey,_T("DumpFile"),0,REG_EXPAND_SZ,(const unsigned char *)szBuf,lstrlen(szBuf)*sizeof(TCHAR));
			if(RegSetValueEx(hKey,_T("MinidumpDir"),0,REG_EXPAND_SZ,(const unsigned char *)szBuf,lstrlen(szBuf)*sizeof(TCHAR)))
			{
#ifdef	protector_logdat
				_ErrorRet(100);
#endif			
				RegCloseKey(hKey);
				return 0;
			}
			RegCloseKey(hKey);
			return 1;
		}
		else
		{
#ifdef	protector_logdat
			_ErrorRet(4);
#endif
			return 0;
		}
	}
#endif

#ifdef	protector_debug
	HANDLE	hLogFile;
	TCHAR	*szMsgDllFormat=_T("_NewLoadLibraryExW: %ws");	
		TCHAR	*szMsgDllFormat_MaybeHook=_T("_NewLoadLibraryExW: %ws call from ClientLoadLibrary");	
			TCHAR	*szMsgDllFormat_MustBeHook=_T("_NewLoadLibraryExW %ws it is a global hook need load dll");	
	TCHAR	*szMsgDllFormat_Deny=_T("_NewLoadLibraryExW deny: %ws");	
	TCHAR	*szMsgDllFormat_OK=_T("_NewLoadLibraryExW okey: %ws");

	PWCHAR  szKingSoft1=L"c:\\Program Files\\Kingsoft\\KnightV\\KSKnight.dll";
	PWCHAR  szKingSoft2=L"c:\\Program Files\\Kingsoft\\KnightV\\KSDriver.dll";

	DWORD	dwKingSoft1=0;
	DWORD	dwKingSoft2=0;

#pragma optimize("y", off)
	void _Log(TCHAR *lpStrBuf,...)
	{
		TCHAR	szBuf[512]={0};
		char szNewBuf[512]={0};
		SYSTEMTIME	stTime;
		int	dwSize;
		unsigned long	dwRtn;
		char	*dwArgList;

		GetLocalTime(&stTime);
		dwSize=wsprintf(szBuf,_T("%02d:%02d:%02d ring3 "),stTime.wHour,stTime.wMinute,stTime.wSecond);
		__asm 
		{
			lea	eax,[ebp+0ch]
			mov dwArgList,eax
		}

		dwSize+=wvsprintf(&szBuf[dwSize],lpStrBuf,dwArgList);
		szBuf[dwSize++]=_T('\r');
		szBuf[dwSize++]=_T('\n');
		/*
		__asm
		{
		lea	eax,szBuf
		add eax,dwSize
		mov word ptr [eax],0x0a0d
		inc dwSize
		inc	dwSize
		}
		*/
		SetFilePointer(hLogFile,0,0,FILE_END);

#ifdef _UNICODE
		dwSize=WideCharToMultiByte(CP_ACP,0,(LPCWSTR)szBuf,dwSize,szNewBuf,512,0,0);
		WriteFile(hLogFile,szNewBuf,dwSize,&dwRtn,0);
#else
		WriteFile(hLogFile,szBuf,dwSize,&dwRtn,0);
#endif
	}
#pragma optimize("",  on)

	HANDLE	_InitLog(TCHAR *lpFileName)
	{
		HANDLE hFile;
		SYSTEMTIME	stTime;
		TCHAR szBuf[512]={0};
		//	DWORD dwSize;
		//	unsigned long	dwRtn;
		hFile=CreateFile(lpFileName,GENERIC_WRITE,FILE_SHARE_READ | FILE_SHARE_WRITE,\
			0,CREATE_NEW,FILE_ATTRIBUTE_NORMAL,0);
		if(hFile==INVALID_HANDLE_VALUE || hFile==0)
		{
			hFile=CreateFile(lpFileName,GENERIC_WRITE,FILE_SHARE_READ | FILE_SHARE_WRITE,\
				0,OPEN_EXISTING,FILE_ATTRIBUTE_NORMAL,0);
			if(hFile==INVALID_HANDLE_VALUE || hFile==0)
				hFile=0;
		}

		hLogFile=hFile;

		if(hFile)
		{	
			//记录当前日期
			GetLocalTime(&stTime);
			_Log(_T("*****************%d-%02d-%02d Process %d*****************"),stTime.wYear,\
				stTime.wMonth,stTime.wDay,GetCurrentProcessId());
#ifdef _UNICODE
			_Log(_T("_InitLog:unicode"));
#else
			_Log(_T("_InitLog:ansi"));
#endif
			/*
			dwSize=wsprintf(szBuf,\
			_T("\r\n*****************%d-%02d-%02d Process %d*****************\r\n"),\
			stTime.wYear,stTime.wMonth,stTime.wDay,GetCurrentProcessId());

			SetFilePointer(hFile,0,0,FILE_END);
			WriteFile(hFile,szBuf,dwSize*sizeof(TCHAR),&dwRtn,0);
			*/
		}

		return hFile;
	}

	int	_ErrorRet(TCHAR *_lpStr)
	{
		int	dwErrCode;
		TCHAR	*lpMsg=0;
		dwErrCode=GetLastError();
		FormatMessage(FORMAT_MESSAGE_FROM_SYSTEM|FORMAT_MESSAGE_ALLOCATE_BUFFER,0,dwErrCode,0,(TCHAR *)&lpMsg,0,0);
#ifdef	protector_debug
		_Log(_T("%s : %d %s"),_lpStr,dwErrCode,lpMsg);
#endif
		LocalFree(lpMsg);
		return dwErrCode;
	}

	int	_SetDebugEnv()
	{
		HKEY hKey=0;
		TCHAR szBuf[MAX_PATH]={0};
		int	 dwValue,dwPos;
		if(RegCreateKey(HKEY_LOCAL_MACHINE,_T("SYSTEM\\CurrentControlSet\\Control\\CrashControl"),&hKey)==ERROR_SUCCESS)
		{
			dwValue=1;
			RegSetValueEx(hKey,_T("AutoReboot"),0,REG_DWORD,(const unsigned char *)&dwValue,4);
			RegSetValueEx(hKey,_T("Overwrite"),0,REG_DWORD,(const unsigned char *)&dwValue,4);

			dwValue=2;
			RegSetValueEx(hKey,_T("CrashDumpEnabled"),0,REG_DWORD,(const unsigned char *)&dwValue,4);

			dwPos=GetCurrentDirectory(MAX_PATH,szBuf);
			//这里要判断是不是根目录，如果是，返回的是c:\，如果不是，返回的是c:\file
			if(szBuf[dwPos-1]!=_T('\\'))
				szBuf[dwPos]=_T('\\');
			lstrcat(szBuf,_T("log_memory.dmp"));

			//RegSetValueEx(hKey,_T("DumpFile"),0,REG_EXPAND_SZ,(const unsigned char *)szBuf,lstrlen(szBuf)*sizeof(TCHAR));
			RegSetValueEx(hKey,_T("MinidumpDir"),0,REG_EXPAND_SZ,(const unsigned char *)szBuf,lstrlen(szBuf)*sizeof(TCHAR));
			RegCloseKey(hKey);
			return 1;
		}
		else
		{
#ifdef	protector_debug
			_ErrorRet(_T("_SetDebugEnv:RegCreateKey"));
#endif
			return 0;
		}
	}
#endif

#pragma optimize("",  off)
	char *_GetClientLoadLibraryAddress(void)
	{
		char *dwRtn=0;
		VMProtectBeginVirtualization
			__asm
		{

			mov	eax,fs:[30h]
			mov	eax,dword ptr [eax+2ch]	;peb->KernelCallbackTable
				test	eax,eax
				jz	_end
				mov	ecx,dwTableIndex
				lea	eax,[eax+ecx*4]
			mov	eax,dword ptr [eax]
_end:
			mov	dwRtn,eax
		}
		VMProtectEnd
			return dwRtn;
	}
#pragma optimize("",  on)

	int	_SearchRet(char *lpBuf)
	{
		VMProtectBeginVirtualization
			char *TempPos=0;
		char *BeginPos=lpBuf;
		for(int i=0;i<512;)
		{
			if((unsigned char)lpBuf[i]==0xe9)
			{
				TempPos=&lpBuf[i];
				lpBuf=(unsigned int)(*(int *)(TempPos+1))+TempPos+5;
				i=0;
				continue;
			}
			if((unsigned char)lpBuf[i++]==0xc9 && (unsigned char)lpBuf[i]==0xc2)
				return (int)(lpBuf-BeginPos)+i+3;
		}
		VMProtectEnd
			return 0;
	}

	int	_EditHookApi(char *lpOldApi,char *lpPatch,int size)
	{
		MEMORY_BASIC_INFORMATION stMBI;
		VirtualQuery(lpOldApi,&stMBI,sizeof stMBI);
		if(!VirtualProtect(stMBI.BaseAddress,stMBI.RegionSize,PAGE_EXECUTE_READWRITE,&stMBI.Protect))
		{
#ifdef	protector_debug
			_ErrorRet(_T("_EditHookApi:VirtualProtect"));
#endif
#ifdef	protector_logdat
			_ErrorRet(6);
#endif
			return 0;
		}
		RtlMoveMemory(lpOldApi,lpPatch,size);
		return 1;
	}

	#pragma optimize("",  off)
	__declspec(naked) void _NewLoadLibraryExW()
	{
		__asm
		{
			push	ebp
			mov		ebp,esp
				/*
#ifdef	protector_debug

			pushad
			push	dword ptr [ebp+8]
			push	szMsgDllFormat
			call	_Log
			add	esp,8

			push	szKingSoft1
			push	dword ptr [ebp+8]
			call	dword ptr[_wcsicmp]
			add		esp,8
			test	eax,eax
			jnz		noking1
			mov		dwKingSoft1,1
			jmp		noking2
noking1:		mov		dwKingSoft1,0
noking2:

			push	szKingSoft2
			push	dword ptr [ebp+8]
			call	dword ptr[_wcsicmp]
			add		esp,8
			test	eax,eax
			jnz		noking3
			mov		dwKingSoft2,1
			jmp		noking4
noking3:	mov		dwKingSoft2,0
noking4:
			popad

#endif
				*/
			mov		eax,dword ptr [ebp+4]	;return address
			cmp		eax,lpClientLoadLibraryBegin
			jle		_Pass
			cmp		eax,lpClientLoadLibraryEnd
			jge		_Pass

			//-----说明有可能是钩子调用，根据调用参数再进一步判断----
/*
#ifdef	protector_debug
				
			pushad
			push	dword ptr [ebp+8]
			push	szMsgDllFormat_MaybeHook
			call	_Log
			add	esp,8
			popad
				
#endif
*/
			mov		ecx,dword ptr [ebp]		//old ebp
			mov		ecx,dword ptr [ecx+8]	//__ClientLoadLibrary parameter1
			mov		eax,dwParamLibrary
			cmp		dword ptr [ecx+20h],0
			jne		_Pass
			cmp		dword ptr [ecx+10h],eax	//ecx+10,win2k03下面是28h,win2k和winxp是24h
			jne		_Pass

			//说明是全局钩子调用，需要加载DLL
			//此处进行白名单检测ptp
			//mov		eax,szWhiteDll1
			//int 3
			/*
#ifdef	protector_debug
			//int 3
			pushad
			push	dword ptr [ebp+8]
		push	szMsgDllFormat_MustBeHook
			call	_Log
			add	esp,8
			popad
#endif
*/
			lea		eax,szWhiteDll1
			push	eax
			push	dword ptr [ebp+8]
#if _MSC_VER<=1200
			call	_wcsicmp
#else
			call	dword ptr[_wcsicmp]
#endif
			add		esp,8
			cmp		eax,0
			jz		_Pass

			lea		eax,szWhiteDll2
			push	eax
			push	dword ptr [ebp+8]
#if _MSC_VER<=1200
			call	_wcsicmp
#else
			call	dword ptr[_wcsicmp]
#endif
			add		esp,8
			cmp		eax,0
			jz		_Pass

			mov		eax,szWhiteDll3
			push	eax
			push	dword ptr [ebp+8]
#if _MSC_VER<=1200
			call	_wcsicmp
#else
			call	dword ptr[_wcsicmp]
#endif
			add		esp,8
			cmp		eax,0
			jz		_Pass

			//没有在白名单里面，返回0，退出
/*
#ifdef	protector_debug
			//int 3
			pushad
			push	dword ptr [ebp+8]
			push	szMsgDllFormat_Deny
			call	_Log
			add	esp,8
			popad
#endif
*/
			mov	esp,ebp
			pop	ebp
			mov	eax,0
			ret	0ch

_Pass:
			/*
#ifdef	protector_debug
			//int 3
			pushad
			push	dword ptr [ebp+8]
			push	szMsgDllFormat_OK
			call	_Log
			add	esp,8
			popad

			cmp		dwKingSoft1,0
			jz		label1
			int 3
label1:
			cmp		dwKingSoft2,0
			jz		label2
			int 3
label2:
#endif

*/
			//--restore back-------
			push	edi
			push	esi
			mov		edi,lpLoadLibraryExW
			lea		esi,dwOldLoadLibraryExW
			mov		ecx,5
			cld
			rep		movsb
			pop		esi
			pop		edi

			//--call orignal LoadLibraryExW---------
			push	[ebp+10h]
			push	[ebp+0ch]
			push	[ebp+8]
			call	dword ptr [lpLoadLibraryExW]

			//--restore hook-------
			push	edi
			push	esi
			mov		edi,lpLoadLibraryExW
			lea		esi,dwPatchLoadLibraryExW
			mov		ecx,5
			cld
			rep		movsb
			pop		esi
			pop		edi

			mov		esp,ebp
			pop		ebp
			retn	0ch
		}
	}
#pragma optimize("",  on)

	int	_SetHook()
	{
		if(!(lpLoadLibraryExW=(char *)GetProcAddress(GetModuleHandle(_T("kernel32.dll")),"LoadLibraryExW")))
		{
#ifdef	protector_debug
			_ErrorRet(_T("_SetHook:GetProcAddress"));
#endif
#ifdef	protector_logdat
			_ErrorRet(7);
#endif
			return 0;
		}
		RtlMoveMemory(dwOldLoadLibraryExW,lpLoadLibraryExW,5);
		dwPatchLoadLibraryExW[0]=(char)0xe9;
		*(unsigned int *)(dwPatchLoadLibraryExW+1)=(unsigned int)(__int64)_NewLoadLibraryExW - (unsigned int)(__int64)lpLoadLibraryExW - 5;
		return _EditHookApi(lpLoadLibraryExW,dwPatchLoadLibraryExW  ,sizeof dwPatchLoadLibraryExW);
	}

	int	_SecureMsgHook(void)
	{

		TCHAR	szSysBuf[MAX_PATH]={0};
		TCHAR	szBuf[512]={0};
		int		dwPos=0;
		size_t	dwConvSize=0;

		GetSystemDirectory(szSysBuf,MAX_PATH);
		VMProtectBeginVirtualization
#ifndef _UNICODE 
		//非unicode版本，需要把字符串转成unicode的
		MultiByteToWideChar(CP_ACP,0,(LPCSTR)szBuf,wsprintf(szBuf,_T("%s\\%s"),szSysBuf,_T("MSCTF.DLL")),(LPWSTR)szWhiteDll1,sizeof(szWhiteDll1));
		MultiByteToWideChar(CP_ACP,0,(LPCSTR)szBuf,wsprintf(szBuf,_T("%s\\%s"),szSysBuf,_T("uxtheme.dll")),(LPWSTR)szWhiteDll2,sizeof(szWhiteDll2));

		//mbstowcs((wchar_t *)szWhiteDll1,szBuf,wsprintf(szBuf,_T("%s\\%s"),szSysBuf,_T("MSCTF.DLL")));
		//mbstowcs((wchar_t *)szWhiteDll2,szBuf,wsprintf(szBuf,_T("%s\\%s"),szSysBuf,_T("uxtheme.dll")));
		//mbstowcs_s(&dwConvSize,(wchar_t *)szWhiteDll1,sizeof(szWhiteDll1),szBuf,wsprintf(szBuf,_T("%s\\%s"),szSysBuf,_T("MSCTF.DLL")));
		//mbstowcs_s(&dwConvSize,(wchar_t *)szWhiteDll2,sizeof(szWhiteDll2),szBuf,wsprintf(szBuf,_T("%s\\%s"),szSysBuf,_T("uxtheme.dll")));
#else
		wsprintf(szWhiteDll1,_T("%s\\%s"),szSysBuf,_T("MSCTF.DLL"));
		wsprintf(szWhiteDll2,_T("%s\\%s"),szSysBuf,_T("uxtheme.dll"));
#endif

#ifdef	protector_debug
#ifdef	_UNICODE
		_Log(_T("SecureMsgHook:White List: %s %s"),szWhiteDll1,szWhiteDll2);
#else
		_Log(_T("SecureMsgHook:White List: %ws %ws"),szWhiteDll1,szWhiteDll2);
#endif
#endif

		if(!(lpClientLoadLibraryBegin=_GetClientLoadLibraryAddress()))
		{
#ifdef	protector_debug
			_Log(_T("_SecureMsgHook:_GetClientLoadLibraryAddress failed"));
#endif
#ifdef	protector_logdat
			_Log(8,0);
#endif
			return 0;
		}
		if(!(dwPos=_SearchRet(lpClientLoadLibraryBegin)))
		{
#ifdef	protector_debug
			_Log(_T("_SecureMsgHook:_SearchRet failed,lpClientLoadLibraryBegin %08x"),lpClientLoadLibraryBegin);
#endif
#ifdef	protector_logdat
			_Log(9,1,lpClientLoadLibraryBegin);
#endif
			return 0;
		}
		lpClientLoadLibraryEnd=lpClientLoadLibraryBegin+dwPos;

#ifdef	protector_debug
		_Log(_T("_SecureMsgHook:__ClientLoadLibrary begins at %08x,length is %08x"),lpClientLoadLibraryBegin,dwPos);
#endif
#ifdef	protector_logdat
		_Log(10,2,lpClientLoadLibraryBegin,dwPos);
#endif
		VMProtectEnd
			//inline hook
		return _SetHook();
	}

	int	_RVAToOffset(char *lpFileHead,unsigned int dwRVA)
	{
		IMAGE_NT_HEADERS	*lpNtHeader;
		IMAGE_SECTION_HEADER	*lpSecHead;
		int	NumberOfSections;

		lpNtHeader=(IMAGE_NT_HEADERS *)(lpFileHead+((IMAGE_DOS_HEADER *)lpFileHead)->e_lfanew);

		NumberOfSections=lpNtHeader->FileHeader.NumberOfSections;
		lpSecHead=(IMAGE_SECTION_HEADER *)((char *)lpNtHeader+sizeof(IMAGE_NT_HEADERS));
		do
		{
			if( dwRVA>=lpSecHead->VirtualAddress && dwRVA<lpSecHead->SizeOfRawData )
			{
				return dwRVA-lpSecHead->VirtualAddress+lpSecHead->PointerToRawData;
			}
			lpSecHead+=sizeof(IMAGE_SECTION_HEADER);
		}	
		while (NumberOfSections--);
		return -1;
	}

	//bEnable=1,从当前目录加载
	//bEnable=2,从system32目录加载
	//bEnable=0,设置为空
	int _ChangeServiceConfig(SC_HANDLE hSvc,int bEnable)
	{
		TCHAR	szBuf[MAX_PATH]={0};
		TCHAR   szSystemDir[MAX_PATH]={0};
		if(bEnable==1)
		{	
			//恢复
			if(!GetFullPathName(_T("PerfectProtector.sys"),MAX_PATH,szBuf,0))
			{
#ifdef	protector_debug
				_ErrorRet(_T("_ChangeServiceConfig(%d):GetFullPathName"));
#endif
#ifdef	protector_logdat
				_ErrorRet(11);
#endif
				return 0;
			}
		}
		else if(bEnable==2)
		{
			//把驱动文件复制到 windows\system32\drivers目录下加载
			//先获取系统目录
			if(GetSystemDirectory(szSystemDir,MAX_PATH)==0)
			{
#ifdef	protector_logdat
				_ErrorRet(113);
#endif
				return 0;
			}
			
			lstrcat(szSystemDir,_T("\\drivers\\PerfectProtector.sys"));
			//再把文件复制过去
			if(!CopyFile(_T("PerfectProtector.sys"),szSystemDir,0))
			{
#ifdef	protector_logdat
				_ErrorRet(114);
#endif
				return 0;
			}
			
			//准备新的目录
			RtlZeroMemory(szBuf,sizeof(szBuf));
			lstrcpy(szBuf,szSystemDir);
		}
		else
		{
			lstrcpy(szBuf,szImageFileName);
		}

		if(!ChangeServiceConfig(hSvc,SERVICE_NO_CHANGE,SERVICE_NO_CHANGE,SERVICE_NO_CHANGE,szBuf,0,0,0,0,0,0))
		{
#ifdef	protector_debug
			_ErrorRet(_T("_ChangeServiceConfig:ChangeServiceConfig"));
#endif
#ifdef	protector_logdat
			_ErrorRet(13);
#endif
			return 0;
		}
		return 1;
	}

	int _ChangeServiceACL(SC_HANDLE hSvc)
	{
		SECURITY_DESCRIPTOR	stSD;
		SECURITY_DESCRIPTOR	stNewSD;
		EXPLICIT_ACCESS		stEA;
		unsigned long	dwSize=0;
		char	*lpBuf=0;
		int		DaclPresent=0,DaclDefaulted=0,dwRtn=0;
		ACL *lpACL=0;
		ACL *lpNewACL=0;

		if(!QueryServiceObjectSecurity(hSvc,DACL_SECURITY_INFORMATION,&stSD,0,&dwSize))
		{
			if(GetLastError()==ERROR_INSUFFICIENT_BUFFER)
			{
				if((lpBuf=(char *)GlobalAlloc(GPTR,dwSize))==0)
				{
#ifdef	protector_debug
					_ErrorRet(_T("_ChangeServiceACL:GlobalAlloc"));
#endif
#ifdef	protector_logdat
					_ErrorRet(14);
#endif
					goto	_end;
				}
				if(!QueryServiceObjectSecurity(hSvc,DACL_SECURITY_INFORMATION,lpBuf,dwSize,&dwSize))
				{
#ifdef	protector_debug
					_ErrorRet(_T("_ChangeServiceACL:QueryServiceObjectSecurity"));
#endif
#ifdef	protector_logdat
					_ErrorRet(15);
#endif
					goto	_end;
				}
			}
			else
			{
#ifdef	protector_debug
				_ErrorRet(_T("_ChangeServiceACL:QueryServiceObjectSecurity"));
#endif
#ifdef	protector_logdat
				_ErrorRet(16);
#endif
				goto	_end;
			}
		}

		if(!GetSecurityDescriptorDacl(lpBuf ? lpBuf : (char *)&stSD,&DaclPresent,&lpACL,&DaclDefaulted))
		{
#ifdef	protector_debug
			_ErrorRet(_T("_ChangeServiceACL:GetSecurityDescriptorDacl"));
#endif
#ifdef	protector_logdat
			_ErrorRet(17);
#endif
			goto	_end;
		}

		BuildExplicitAccessWithName(&stEA,(TCHAR *)_T("EVERYONE"),SERVICE_START | SERVICE_STOP | SERVICE_CHANGE_CONFIG,SET_ACCESS,NO_INHERITANCE);

		if(SetEntriesInAcl(1,&stEA,lpACL,&lpNewACL))
		{
#ifdef	protector_debug
			_ErrorRet(_T("_ChangeServiceACL:SetEntriesInAcl"));
#endif
#ifdef	protector_logdat
			_ErrorRet(18);
#endif
			goto	_end;
		}

		if(!InitializeSecurityDescriptor(&stNewSD,SECURITY_DESCRIPTOR_REVISION))
		{
#ifdef	protector_debug
			_ErrorRet(_T("_ChangeServiceACL:InitializeSecurityDescriptor"));
#endif
#ifdef	protector_logdat
			_ErrorRet(19);
#endif
			goto	_end;
		}

		if(!SetSecurityDescriptorDacl(&stNewSD,1,lpNewACL,0))
		{
#ifdef	protector_debug
			_ErrorRet(_T("_ChangeServiceACL:SetSecurityDescriptorDacl"));
#endif
#ifdef	protector_logdat
			_ErrorRet(20);
#endif
			goto	_end;
		}

		if(!SetServiceObjectSecurity(hSvc,DACL_SECURITY_INFORMATION,&stNewSD))
		{
#ifdef	protector_debug
			_ErrorRet(_T("_ChangeServiceACL:SetServiceObjectSecurity"));
#endif
#ifdef	protector_logdat
			_ErrorRet(21);
#endif
		}
		else
		{
#ifdef	protector_debug
			_Log(_T("_ChangeServiceACL:set new acl successful!"));
#endif
#ifdef	protector_logdat
			_Log(22,0);
#endif
			dwRtn=1;
		}

_end:
		if(lpBuf)
			GlobalFree(lpBuf);
		if(lpNewACL)
			LocalFree(lpNewACL);
		return dwRtn;

	}

	//返回值
	//-1	失败，必须退出
	//0		失败，因为服务不存在，需要创建服务
	//1		成功
	signed int	_StartService(void)
	{
		SC_HANDLE	hSCM=0,hSvc=0;
		int			dwRtn=-1,dwErrorCode=0,dwReStartTime=0;
		BOOL		bTriedLoadFromSys=false;

		if(!(hSCM=OpenSCManager(0,0,SC_MANAGER_CONNECT)))
		{
			if(GetLastError()==ERROR_ACCESS_DENIED)
				bPrivilegeNotHold=true;
#ifdef	protector_debug
			_ErrorRet(_T("_StartService:OpenSCManager"));
#endif
#ifdef	protector_logdat
			_ErrorRet(23);
#endif
			return	-1;
		}

		if(!(hSvc=OpenService(hSCM,szServiceName,SERVICE_START | SERVICE_STOP | SERVICE_CHANGE_CONFIG)))
		{
			dwErrorCode=GetLastError();
#ifdef	protector_debug
			_ErrorRet(_T("_StartService:OpenService"));
#endif
#ifdef	protector_logdat
			_ErrorRet(24);
#endif

			if(dwErrorCode==ERROR_ACCESS_DENIED)
				bPrivilegeNotHold=true;
			else if(dwErrorCode==ERROR_SERVICE_DOES_NOT_EXIST)
			{
				//服务不存在，返回0
				dwRtn=0;
			}
			goto	_end;
		}

		//从当前目录启动
		if(_ChangeServiceConfig(hSvc,1))
		{
#ifdef	protector_debug
			_Log(_T("_StartService:Change Service Config okey"));
#endif
#ifdef	protector_logdat
			_Log(25,0);
#endif
		}
		else
		{
#ifdef	protector_logdat
			_Log(116,0);	//_StartService;open service okey,but can't change ImagePatch to current dir,try copy and set ImagePath to system32\drivers	
#endif
			//返回0，尝试从系统目录启动
			if(_ChangeServiceConfig(hSvc,2)==0)
			{
#ifdef	protector_logdat
				_Log(117,0);	//_StartService:set ImagePath to system32\drivers failed!
#endif
				dwRtn=0;//返回0，尝试重新创建服务
				goto	_end;
			}
#ifdef	protector_logdat
			_Log(118,0);	//_StartService:set ImagePath to system32\drivers okey!
#endif
		}

_ReStart:
		if(StartService(hSvc,0,0))
		{
			hDevice=CreateFile(_T("\\\\.\\SlPerfectProtector"),GENERIC_READ|GENERIC_WRITE,0,0,OPEN_EXISTING,0,0);
			if(hDevice!=INVALID_HANDLE_VALUE)
				dwRtn=1;
			else
			{
				hDevice=0;
#ifdef	protector_debug
				_ErrorRet(_T("_StartService:CreateFile"));
#endif
#ifdef	protector_logdat
				_ErrorRet(26);
#endif
			}
		}
		else
		{
			dwErrorCode=GetLastError();
			if(dwErrorCode==ERROR_CHILD_NOT_COMPLETE)
			{
#ifdef	protector_debug
				_Log(_T("_StartService:start service failed for config not completed,sleep 0.5s and restart"));
#endif
#ifdef	protector_logdat
				_Log(27,0);
#endif
				Sleep(1000);
				goto	_ReStart;
			}
			if(dwErrorCode==ERROR_SERVICE_ALREADY_RUNNING)
			{
				hDevice=CreateFile(_T("\\\\.\\SlPerfectProtector"),GENERIC_READ|GENERIC_WRITE,0,0,OPEN_EXISTING,0,0);
				if(hDevice!=INVALID_HANDLE_VALUE)
				{
					dwRtn=1;
#ifdef	protector_debug
					_Log(_T("_StartService:start service failed for competing reason,so open symbolic link directly"));
#endif
#ifdef	protector_logdat
					_Log(28,0);
#endif
				}
				else
				{
					hDevice=0;
#ifdef	protector_debug
					_Log(_T("_StartService:#%2d start service failed for driver unloading not completed,sleep 3s and restart"),dwReStartTime);
#endif
#ifdef	protector_logdat
					_Log(29,1,dwReStartTime);
#endif
					if(dwReStartTime++<3)
					{
						Sleep(3000);
						goto	_ReStart;
					}
				}
			}
			else if(dwErrorCode==ERROR_MOD_NOT_FOUND)
			{//PP找不到内核模块基址，一般是自动更新引起，需要重启电脑
#ifdef	protector_logdat
				_Log(136,0);	//_StartService:StartService return 126,Common_GetImageNameByZwQuery failed, need to restart!
#endif			
				dwRtn=126;

			}
			else if(bTriedLoadFromSys==false)// && dwErrorCode==ERROR_PATH_NOT_FOUND)
			{
#ifdef protector_logdat
				_Log(115,1,GetLastError());//_StartService:StartService fail %d,try copy to system32\drivers,load from there
#endif
				//找不到路径，在虚拟磁盘或unc路径中会出现，复制到windows\system32\drivers目录下加载
				if(!_ChangeServiceConfig(hSvc,2))
				{
					//复制不过去？
#ifdef	protector_logdat
					_Log(111,0);	//_StartService:_ChangeServiceConfig to system32 failed!
#endif
				}
				else
				{
#ifdef	protector_logdat
					_Log(112,0);	//StartService:_ChangeServiceConfig to system32 oky,try load from system32!
#endif
					//复制成功，重新启动服务
					bTriedLoadFromSys=true;//要记下已经这样尝试过了，下一次就不需要再尝试了
					goto	_ReStart;
				}
			}
			else
			{
				if(dwErrorCode==5)
					bPrivilegeNotHold=true;
#ifdef	protector_debug
				_ErrorRet(_T("_StartService:StartService"));
#endif
#ifdef	protector_logdat
				_ErrorRet(30);
#endif
			}
		}

		_ChangeServiceConfig(hSvc,0);

_end:
		if(hSCM)
			CloseServiceHandle(hSCM);
		if(hSvc)
			CloseServiceHandle(hSvc);
		return dwRtn;
	}

	DWORD	_CreateService(void)
	{
		SERVICE_STATUS	stSvcStatus;
		SC_HANDLE		hSCM=0,hSvc=0;
		int		dwTemp=0,dwErrCode=0,ReCreateCount=0;
		int		dwRtn=0;

		if(!(hSCM=OpenSCManager(0,0,SC_MANAGER_ALL_ACCESS)))
		{
			if(GetLastError()==ERROR_ACCESS_DENIED)
				bPrivilegeNotHold=true;
#ifdef	protector_debug
			_ErrorRet(_T("_CreateService:OpenSCManager"));
#endif

#ifdef	protector_logdat
			_ErrorRet(31);
#endif
			return	0;
		}

_Create:
		if(ReCreateCount>=4)
		{
#ifdef	protector_debug
			_Log(_T("_CreateService:re create 4 times,return fail!"));
#endif
#ifdef	protector_logdat
			_Log(32,0);
#endif
			goto	_end;
		}

		hSvc=CreateService(hSCM,szServiceName,szDisplayName,\
			SERVICE_ALL_ACCESS,SERVICE_KERNEL_DRIVER,SERVICE_DEMAND_START,\
			SERVICE_ERROR_IGNORE,szImageFileName,0,0,0,0,0);
		if(hSvc)
		{
#ifdef	protector_debug
			_Log(_T("_CreateService:create service ok,begin to change acl"));
#endif
#ifdef	protector_logdat
			_Log(33,0);
#endif
			dwTemp=_ChangeServiceACL(hSvc);

#ifdef	protector_debug
			if(dwTemp)
				_Log(_T("_CreateService:change acl ok"));
#endif
#ifdef	protector_logdat
			if(dwTemp)
				_Log(34,0);
#endif
			dwRtn=1;

			/*
			if(_ChangeServiceConfig(hSvc,1))
			{
#ifdef	protector_debug
				_Log(_T("_CreateService:Change Service Config okey"));
#endif
#ifdef	protector_logdat
				_Log(35,0);
#endif
			}
			else
				goto	_end;
			if(StartService(hSvc,0,0))
			{
				if((hDevice=CreateFile(_T("\\\\.\\SlPerfectProtector"),GENERIC_READ|GENERIC_WRITE,\
					0,0,OPEN_EXISTING,0,0))!=INVALID_HANDLE_VALUE)
					dwRtn=1;
				else
				{
					hDevice=0;
#ifdef	protector_debug
					_ErrorRet(_T("_CreateService:CreateFile"));
#endif
#ifdef	protector_logdat
					_ErrorRet(36);
#endif
				}
			}
			else
			{
#ifdef	protector_debug
				_ErrorRet(_T("_CreateService:StartService"));
#endif
#ifdef	protector_logdat
				_ErrorRet(37);
#endif
			}
			_ChangeServiceConfig(hSvc,0);
			*/
			

		}
		else
		{
			dwErrCode=GetLastError();
			if(dwErrCode==ERROR_SERVICE_EXISTS || dwErrCode==ERROR_SERVICE_MARKED_FOR_DELETE)
				//服务已经存在，删除之
			{
				if(hSvc=OpenService(hSCM,szServiceName,SERVICE_STOP|DELETE))
				{
					//停止服务
					if(ControlService(hSvc,SERVICE_CONTROL_STOP,&stSvcStatus))
					{
_Delete:
						if(DeleteService(hSvc))
						{
							CloseServiceHandle(hSvc);
							hSvc=0;
							goto	_Create;
						}
						else if(GetLastError()==ERROR_SERVICE_MARKED_FOR_DELETE)
						{
#ifdef	protector_debug
							_Log(_T("_CreateService:DeleteService failed because ERROR_SERVICE_MARKED_FOR_DELETE,now sleep 1 sec and re-create"));
#endif
#ifdef	protector_logdat
							_Log(38,0);
#endif
							Sleep(1000);
							ReCreateCount++;
							CloseServiceHandle(hSvc);
							hSvc=0;

							LPENUM_SERVICE_STATUSA lpSeriverBuf;
							DWORD	dwNeedSize;
							DWORD	dwServiceRtn;

							HANDLE hServiceBuf=GlobalAlloc(GPTR,64*1024);
							if(hServiceBuf==0)
							{
#ifdef	protector_logdat
								_ErrorRet(123);	//_CreateService:GlobalAlloc
#endif
							}
							else
							{
								lpSeriverBuf=(LPENUM_SERVICE_STATUSA)GlobalLock(hServiceBuf);
								if(lpSeriverBuf==0)
								{
#ifdef	protector_logdat
									_ErrorRet(124);	//_CreateService:GlobalLock
#endif
								}
								else
								{
									if(EnumServicesStatus(hSCM,SERVICE_DRIVER,SERVICE_STATE_ALL,lpSeriverBuf,64*1024,&dwNeedSize,&dwServiceRtn,0))
									{
#ifdef	protector_logdat
										_Log(125,0);	//_CreateService:EnumServicesStatus okey!Flush all service status;
#endif
									}
									else
									{
#ifdef	protector_logdat
										_ErrorRet(126);//_CreateService:EnumServicesStatus
#endif
									}
									GlobalUnlock(hServiceBuf);
								}
								GlobalFree(hServiceBuf);
							}
							goto	_Create;
						}
						else
						{
#ifdef	protector_debug
							_ErrorRet(_T("_CreateService:DeleteService"));
#endif
#ifdef	protector_logdat
							_ErrorRet(39);
#endif
						}
					}
					else
					{
						if(GetLastError()==ERROR_SERVICE_NOT_ACTIVE || GetLastError()==ERROR_INVALID_SERVICE_CONTROL)
						{
#ifdef	protector_logdat
							_Log(119,0);//_CreateService:创建服务失败是因为服务已经存在，或服务标记为删除，尝试停止服务发现服务没有启动或请求对服务无效，尝试删除服务再重新创建
#endif
							goto	_Delete;
						}
#ifdef	protector_debug
						_ErrorRet(_T("_CreateService:ControlService"));
#endif
#ifdef	protector_logdat
						_ErrorRet(40);
#endif
					}
				}
				else
				{
#ifdef	protector_debug
					_ErrorRet(_T("_CreateService:OpenService"));
#endif
#ifdef	protector_logdat
					_ErrorRet(41);
#endif
				}
			}
			else
			{
#ifdef	protector_debug
				_ErrorRet(_T("_CreateService:CreateService"));
#endif
#ifdef	protector_logdat
				_ErrorRet(42);
#endif
			}
		}

_end:
		if(hSCM)
			CloseServiceHandle(hSCM);
		if(hSvc)
			CloseServiceHandle(hSvc);
		return dwRtn;
	}

	void _DelService(void)
	{
		SERVICE_STATUS	stSvcStatus;
		SC_HANDLE		hSCM,hSvc;
		if(!CloseHandle(hDevice))
		{
#ifdef	protector_debug
			_ErrorRet(_T("_DelService:CloseHandle"));
#endif
#ifdef	protector_logdat
			_ErrorRet(43);
#endif
		}
		if(hSCM=OpenSCManager(0,0,SC_MANAGER_ALL_ACCESS))
		{
			if(hSvc=OpenService(hSCM,szServiceName,SERVICE_STOP|DELETE))
			{
				if(!ControlService(hSvc,SERVICE_CONTROL_STOP,&stSvcStatus))
				{
#ifdef	protector_debug
					_ErrorRet(_T("_DelService:ControlService"));
#endif
#ifdef	protector_logdat
					_ErrorRet(44);
#endif
				}
				//即使停止服务出错，仍然进行删除
				if(!DeleteService(hSvc))
				{
#ifdef	protector_debug
					_ErrorRet(_T("_DelService:DeleteService"));
#endif
#ifdef	protector_logdat
					_ErrorRet(45);
#endif
				}
				if(!CloseServiceHandle(hSvc))
				{
#ifdef	protector_debug
					_ErrorRet(_T("_DelService:CloseHandle hSvc"));
#endif
#ifdef	protector_logdat
					_ErrorRet(46);
#endif
				}
			}
			else
			{
#ifdef	protector_debug
				_ErrorRet(_T("_DelService:OpenService"));
#endif
#ifdef	protector_logdat
				_ErrorRet(47);
#endif
			}
			if(!CloseServiceHandle(hSCM))
			{
#ifdef	protector_debug
				_ErrorRet(_T("_DelService:CloseHandle @hSCM"));
#endif
#ifdef	protector_logdat
				_ErrorRet(48);
#endif
			}
		}
		else
		{
#ifdef	protector_debug
			_ErrorRet(_T("_DelService:OpenSCManager"));
#endif
#ifdef	protector_logdat
			_ErrorRet(49);
#endif
		}
		return;
	}

	int _StopService(void)
	{
		SERVICE_STATUS	stSvcStatus;
		SC_HANDLE		hSCM,hSvc;
		int				dwRtn=0;

		if(!(hSCM=OpenSCManager(0,0,SC_MANAGER_CONNECT)))
		{
#ifdef	protector_debug
			_ErrorRet(_T("_StopService:OpenSCManager"));
#endif
#ifdef	protector_logdat
			_ErrorRet(50);
#endif
			return 0;
		}

		if(!(hSvc=OpenService(hSCM,szServiceName,SERVICE_STOP|SERVICE_CHANGE_CONFIG)))
		{
#ifdef	protector_debug
			_ErrorRet(_T("_StopService:OpenService"));
#endif
#ifdef	protector_logdat
			_ErrorRet(51);
#endif
			goto	_end;
		}

		if(!(dwRtn=ControlService(hSvc,SERVICE_CONTROL_STOP,&stSvcStatus)))
		{
#ifdef	protector_debug
			_ErrorRet(_T("_StopService:ControlService"));
#endif
#ifdef	protector_logdat
			_ErrorRet(52);
#endif
		}

_end:
		if(hSCM)
			CloseServiceHandle(hSCM);
		if(hSvc)
			CloseServiceHandle(hSvc);
		return dwRtn;
	}

	int _CheckIfDriverLoaded(void)
	{
		VMProtectBeginVirtualization
		if((hDevice=CreateFile(_T("\\\\.\\SlPerfectProtector"),GENERIC_READ|GENERIC_WRITE,0,0,OPEN_EXISTING,\
			0,0))!=INVALID_HANDLE_VALUE)
		{
			return 1;
		}
#ifdef	protector_debug
		//_ErrorRet(_T("_CheckIfDriverLoaded:CreateFile:\\\\.\\SlPerfectProtector"));
#endif
#ifdef	protector_logdat
		_Log(53,1,GetLastError());
#endif
		hDevice=0;
		VMProtectEnd
		return 0;

	}

	int	Fake_InsertCurrentPid()
	{
		int	dwOutBuf=0,ret=0;
		unsigned long	dwTemp=0;
		char dwInBuf[24]={0};
		char szBuf[24]={0};

		VMProtectBeginVirtualization
		*(DWORD *)(&szBuf[0])=GetCurrentProcessId();
		szBuf[4]='a';
		*(DWORD *)(&szBuf[5])=GetCurrentThreadId();
		*(DWORD *)(&szBuf[9])='kiss';

		_asm
		{
			pushad
			pushfd
			lea edi,dwInBuf
			lea esi,szBuf
			mov ecx,13
			mov ebx,0x98dfce13
		}

		Common_Encrypt(a)

		_asm
		{
			lea edi,dwInBuf
			mov dword ptr [edi+19],edx
			xor edx,edx
			xor ebx,ebx
			lea edi,szBuf
			xor eax,eax
			mov ecx,24
			rep stosb
			popfd
			popad
		}		

		ret=DeviceIoControl(hDevice,IOCTL_INSERT_PID,(DWORD *)dwInBuf,sizeof dwInBuf,\
			&dwOutBuf,sizeof dwOutBuf,&dwTemp,0);
		VMProtectEnd
		if(ret==0 || dwOutBuf)
		{
#ifdef	protector_debug
			_Log(_T("Fake_InsertCurrentPid:IOCTL_INSERT_PID failed %d"),dwOutBuf);
#endif
#ifdef	protector_logdat
			_Log(54,1,dwOutBuf);
#endif
			return 0;
		}
		return 1;
	}

bool	_InitSystemDir(void)
{
	DWORD dwTemp=0;
	TCHAR szSystemDir[MAX_PATH]={0};

#ifdef	_UNICODE
	if(!GetSystemDirectory(szSystemDir,MAX_PATH))
	{
#ifdef	protector_logdat
		_ErrorRet(120);	//_InitSystemDir:GetSystemDirectory faild
#endif
		return false;
	}
#else
	char szSystemDirA[MAX_PATH]={0};
	int dwBufSize=0;
	dwBufSize=GetSystemDirectory(szSystemDirA,MAX_PATH);
	if(dwBufSize==0)
	{
#ifdef	protector_logdat
		_ErrorRet(120);	//_InitSystemDir:GetSystemDirectory faild
#endif
		return false;
	}
	MultiByteToWideChar(CP_ACP,0,(LPCSTR)szSystemDirA,dwBufSize,(LPWSTR)szSystemDir,MAX_PATH);
#endif

	if(!DeviceIoControl(hDevice,IOCTL_INIT_SYSTEM_DIR,(DWORD *)szSystemDir,MAX_PATH,0,0,&dwTemp,0))
	{
#ifdef	protector_logdat
		_ErrorRet(121);	//_InitSystemDir:DeviceIoControl faild
#endif
		return false;
	}
	return true;

}

	int	Fake_DelCurrentPid()
	{
		int	dwOutBuf=0,ret=0;
		unsigned long	dwTemp=0;
		char dwInBuf[24]={0};
		char szBuf[24]={0};
		DWORD dwDelPid=0;

		VMProtectBeginVirtualization
			dwDelPid=GetCurrentProcessId();
		*(DWORD *)(&szBuf[0])=dwDelPid;
		szBuf[4]='a';
		*(DWORD *)(&szBuf[5])=GetCurrentThreadId();
		*(DWORD *)(&szBuf[9])='kiss';

		_asm
		{
			pushad
			pushfd
			lea edi,dwInBuf
			lea esi,szBuf
			mov ecx,13
			mov ebx,0x98dfce13
		}

		Common_Encrypt(a)

			_asm
		{
			lea edi,dwInBuf
			mov dword ptr [edi+19],edx
			xor edx,edx
			xor ebx,ebx
			lea edi,szBuf
			xor eax,eax
			mov ecx,24
			rep stosb
			popfd
			popad
		}		

		ret=DeviceIoControl(hDevice,IOCTL_DEL_PID,(DWORD *)dwInBuf,sizeof dwInBuf,\
			&dwOutBuf,sizeof dwOutBuf,&dwTemp,0);
		VMProtectEnd
#ifdef	protector_debug
			_Log(_T("Fake_DelCurrentPid:IOCTL_DEL_PID %d returned %d"),dwDelPid,dwOutBuf);
#endif
#ifdef	protector_logdat
		_Log(55,2,dwDelPid,dwOutBuf);
#endif
		return dwOutBuf;
	}

#pragma optimize("",  off)

	__declspec(naked) int _InsertCurrentPid()
	{
		__asm
		{
			push ebp
			mov  ebp,esp
		}

		VMProtectBeginVirtualization
		DoIntCall(0,ac,_Error)
_Error:
		VMProtectEnd
		__asm
		{
			mov esp,ebp
			pop ebp
			ret
		}
	}

	__declspec(naked) int _DelCurrentPid()
	{
		__asm
		{
			push ebp
			mov  ebp,esp
		}
		VMProtectBeginVirtualization
		DoIntCall(1,ac,_Error)
_Error:
		VMProtectEnd
		__asm
		{
			mov esp,ebp
			pop ebp
			ret
		}
	}
#pragma optimize("",on)

	void StopProtector(void)
	{
		int dwRtn=0,dwStatus=0;
		VMProtectBeginVirtualization
		if(hDevice==0)
		{
#ifdef	protector_debug
			_Log(_T("StopProtector:hDevice is 0,return directly!"));
			CloseHandle(hLogFile);
#endif
#ifdef	protector_logdat
			_Log(56,0);
			CloseHandle(hLogFile);
#endif
			return;
		}

		dwRtn=Fake_DelCurrentPid();
		dwStatus=_DelCurrentPid();

		//返回值是当前PID列表中剩下PID的个数，如果不为0，直接退出就行，不需要卸载驱动
		if(dwRtn==0 || dwStatus==0 || dwStatus==-1)
		{
			//pid列表空掉了，说明是最后一个游戏进程，退出前恢复HOOK并卸载驱动
#ifdef	protector_logdat
			_Log(87,0);
#endif
			_StopService();	
		}
		CloseHandle(hDevice);
		hDevice=0;
#ifdef	protector_logdat
		_Log(88,0);
#endif
#ifdef	protector_debug
		CloseHandle(hLogFile);
#endif
#ifdef	protector_logdat
		CloseHandle(hLogFile);
#endif
		VMProtectEnd
		return;
	}

	PVOID _CreateSharedMem(HANDLE* _lpSection)
	{
		ANSI_STRING stKeyA={0};
		UNICODE_STRING stKeyW={0};
		OBJECT_ATTRIBUTES stOA={0};
		LARGE_INTEGER SectionSize={0};
		LARGE_INTEGER stSectionOffset={0};
		HANDLE hSection=0;
		NTSTATUS dwRtn=0;
		PVOID ViewBase=0;
		SIZE_T ViewSize=0;
		VMProtectBeginVirtualization
		
		RtlInitAnsiString(&stKeyA,"\\BaseNamedObjects\\SecDrvMem1");
		RtlAnsiStringToUnicodeString(&stKeyW,&stKeyA,1);

		stOA.ObjectName=&stKeyW;
		stOA.Length=sizeof OBJECT_ATTRIBUTES;
		stOA.Attributes=OBJ_CASE_INSENSITIVE;

		SectionSize.LowPart=10240;

		dwRtn=ZwCreateSection(&hSection,STANDARD_RIGHTS_REQUIRED | SECTION_MAP_READ | SECTION_MAP_WRITE | SECTION_QUERY,&stOA,&SectionSize,PAGE_READWRITE,SEC_COMMIT,0);
		if(dwRtn)
		{
#ifdef protector_debug
			_Log(_T("_CreateSharedMem:ZwCreateSection failed %x"),dwRtn);
#endif
#ifdef	protector_logdat
			_Log(57,1,dwRtn);
#endif
			if(dwRtn==0xc0000022)
				bPrivilegeNotHold=true;
		}
		else
		{
			ViewSize=10240;
			dwRtn=ZwMapViewOfSection(hSection,GetCurrentProcess(),&ViewBase,0,0,&stSectionOffset,&ViewSize,ViewShare,0,PAGE_READWRITE);
			if(dwRtn)
			{
#ifdef protector_debug
				_Log(_T("_CreateSharedMem:ZwMapViewOfSection failed %x"),dwRtn);
#endif
#ifdef	protector_logdat
				_Log(58,1,dwRtn);
#endif
			}
		}

		*_lpSection=hSection;
		RtlFreeUnicodeString(&stKeyW);
		VMProtectEnd
		return ViewBase;
	}
/*
	//用于父进程检测时，在vista下，进程间共享内存需要使用Create/OpenFileMapping，直接使用Zw有问题，打开时返回找不到
	PVOID _OpenSharedMem(TCHAR *MemName,HANDLE *_lpSection,DWORD MemSize)
	{
		ANSI_STRING stKeyA={0};
		UNICODE_STRING stKeyW={0};
		OBJECT_ATTRIBUTES stOA={0};
		LARGE_INTEGER stSectionOffset={0};
		HANDLE hSection=0;
		NTSTATUS dwRtn=0;
		PVOID ViewBase=0;
		SIZE_T ViewSize=0;
		VMProtectBeginVirtualization
#ifdef	_UNICODE	
		RtlInitUnicodeString(&stKeyW,MemName);
#else
		RtlInitAnsiString(&stKeyA,MemName);
		RtlAnsiStringToUnicodeString(&stKeyW,&stKeyA,1);
#endif
		stOA.ObjectName=&stKeyW;
		stOA.Length=sizeof OBJECT_ATTRIBUTES;
		stOA.Attributes=OBJ_CASE_INSENSITIVE;
	
		dwRtn=ZwOpenSection(&hSection,SECTION_MAP_READ | SECTION_MAP_WRITE,&stOA);
		if(dwRtn)
		{
#ifdef	protector_logdat
			_Log(101,1,dwRtn);
#endif
			if(dwRtn==0xc0000022)
				bPrivilegeNotHold=true;
		}
		else
		{
			ViewSize=MemSize;
			dwRtn=ZwMapViewOfSection(hSection,GetCurrentProcess(),&ViewBase,0,0,&stSectionOffset,&ViewSize,ViewShare,0,PAGE_READWRITE);
			if(dwRtn)
			{
#ifdef	protector_logdat
				_Log(102,1,dwRtn);
#endif
			}
		}

		*_lpSection=hSection;
#ifndef		_UNICODE
		RtlFreeUnicodeString(&stKeyW);
#endif
		VMProtectEnd
		return ViewBase;
	}
*/

	PVOID _OpenSharedMem(TCHAR *MemName,HANDLE *_lpSection,DWORD MemSize)
	{

		HANDLE hSection=0;
		NTSTATUS dwRtn=0;
		PVOID ViewBase=0;
		VMProtectBeginVirtualization

		hSection=OpenFileMapping(PAGE_READWRITE,0,MemName);
		if(hSection==0)
		{
#ifdef	protector_logdat
			_ErrorRet(101);
#endif
		
		}
		else
		{
			ViewBase=MapViewOfFile(hSection,FILE_MAP_READ,0,0,MemSize);

			if(ViewBase==0)
			{
#ifdef	protector_logdat
				_ErrorRet(102);
#endif
			}
		}

		*_lpSection=hSection;

		VMProtectEnd
		return ViewBase;
	}

	PVOID	_SetDrvIdentificationData(HANDLE *lpSection)
	{
		char szBuf[64]={0};
		PVOID lpBuf;
		VMProtectBeginVirtualization
		if((lpBuf=_CreateSharedMem(lpSection))==0)
			return 0;
		*(DWORD *)(&szBuf[0])=GetCurrentProcessId();
		szBuf[4]='f';
		*(DWORD *)(&szBuf[5])=GetCurrentThreadId();
		*(DWORD *)(&szBuf[9])='ppin';

		_asm{
			pushad
			pushfd
			mov edi,lpBuf
			lea esi,szBuf
			mov ecx,13
			mov ebx,0x89fdec37
			push edi
		}

		Common_Encrypt(a)

		_asm{
			xor ebx,ebx

			lea edi,szBuf
			xor eax,eax
			mov ecx,64
			rep stosb

			pop eax
			mov dword ptr [eax+19],edx
			xor edx,edx
			popfd
			popad
		}		
		VMProtectEnd
		return lpBuf;
	}

bool _CheckLoader();

	/********************
	返回值
	0	成功
	1	系统版本号不对
	2	创建互斥体失败
	3	等待互斥体超时
	4	启动服务失败
	5	创建服务失败
	6	pid插入fake列表失败
	7	pid插入common列表失败
	8	初始化钩子保护模块失败
	9	初始化驱动加载验证数据失败
	10	父进程检测失败
	11	非管理员权限
	12	初始化系统目录失败


	13	13号较验失败
	14	14号较验失败
	15	15号较验失败
	16	16号较验失败

	1		当前使用的操作系统版本不支持
	4和11	请确认游戏没有被杀毒软件或防火墙禁止，并确认您是在管理员帐号下启动游戏
	126		由于操作系统有未完成的操作，如自动更新，请完成这些操作并重启计算机再尝试打开游戏
	其他值	游戏初始化失败，这时可弹出一个对话框，让玩家给我们发送pp_005.dat。
	*********************/
	int StartProtector(void)
	{
		int	bCanLoad=0;
		int dwRtn=0;
		unsigned long dwTemp=0;
		int dwStatus=0;
		OSVERSIONINFO	stOsInfo;
		HANDLE hLoadMutex=0,hSection=0;
		PVOID lpDrvIdentificationMem=0;
		HANDLE hLauncherEvent=0;

		VMProtectBeginVirtualization
		
		//先判断操作系统版本是否符号要求,并根据版本初化一些变量
		RtlZeroMemory(&stOsInfo,sizeof stOsInfo);
		stOsInfo.dwOSVersionInfoSize=sizeof stOsInfo;
		GetVersionEx(&stOsInfo);
		if(stOsInfo.dwMajorVersion == 5)
		{
			if(stOsInfo.dwMinorVersion==0)
			{
				//win2k
				dwTableIndex=0x40;
				dwParamLibrary=0x24;
				bCanLoad=1;
			}
			else if(stOsInfo.dwMinorVersion==1)
			{
				//winxp
				dwTableIndex=0x42;
				dwParamLibrary=0x24;
				bCanLoad=1;
			}
			else if(stOsInfo.dwMinorVersion==2)
			{
				//win2k03
				dwTableIndex=0x42;
				dwParamLibrary=0x28;
				bCanLoad=1;
			}
		}
		else if(stOsInfo.dwMajorVersion==6)
		{
			if(stOsInfo.dwMinorVersion==0)
			{
				//vista or windows server 2008
				dwTableIndex=0x42;
				dwParamLibrary=0x28;
				bCanLoad=1;
			}
		}

#ifdef	protector_debug
		_InitLog(_T("log_protector.txt"));
		_Log(_T("StartProtector:v005 OS Version %x %x"),stOsInfo.dwMajorVersion,stOsInfo.dwMinorVersion);
#endif

#ifdef	protector_logdat
		_InitLog(_T("pp_005.dat"));
		_Log(5,2,stOsInfo.dwMajorVersion,stOsInfo.dwMinorVersion);
#endif

		if(!bCanLoad)
		{
			//系统版本不对，不能启动
#ifdef	protector_logdat
			_Log(93,0);
#endif
			return 1;
		}

#ifdef protector_ParentCheck
		if(!_CheckLoader())
		{
#ifdef	protector_debug
			_Log(_T("StartProtector:_CheckLoader failed"));
#endif
#ifdef	protector_logdat
			_Log(59,0);
#endif
			return 10;
		}
#ifdef	protector_debug
		_Log(_T("StartProtector:Check loader okey"));
#endif
#ifdef	protector_logdat
		_Log(60,0);
#endif

#endif

		//设置内存转储文件相关的注册表变量
#ifdef	protector_debug
		_SetDebugEnv();
#endif
#ifdef	protector_logdat
		_SetDebugEnv();
#endif

		//---------------------
		//加载保护驱动
		//---------------------
		//需要先锁上
		//因为如果2个客户端同时在启动，_CheckIfDriverLoaded都返回false,则两个客户端都会进行加载驱动的操作，必须要避免
		if((hLoadMutex=CreateMutex(0,1,_T("PP_LOAD_MUTEXT")))==0)
		{
#ifdef	protector_debug
			_ErrorRet(_T("StartProtector:CreateMutex PP_LOAD_MUTEXT failed"));
#endif
#ifdef	protector_logdat
			_ErrorRet(61);
#endif
			dwRtn=2;
			goto _Error;
		}
		if(WaitForSingleObject(hLoadMutex,10000))
		{
#ifdef	protector_debug
			_ErrorRet(_T("StartProtector:WaitForSingleObject PP_LOAD_MUTEXT failed"));
#endif
#ifdef	protector_logdat
			_ErrorRet(62);
#endif
			dwRtn=3;
			goto _Error;
		}

		//判断是否已加载驱动
		if(_CheckIfDriverLoaded())
		{
			//驱动已经加载
#ifdef	protector_debug
			_Log(_T("StartProtector:Driver has been loaded,begin to insert current pid"));
#endif
#ifdef	protector_logdat
			_Log(63,0);
#endif
		}
		else
		{
			//驱动没有加载，先看看服务是不是已经存在
#ifdef	protector_debug
			_Log(_T("StartProtector:Driver does not load,start service"));
#endif
#ifdef	protector_logdat
			_Log(64,0);
#endif
			if((lpDrvIdentificationMem=_SetDrvIdentificationData(&hSection))==0)
			{
#ifdef protector_debug
				_Log(_T("StartProtector_SetDrvIdentificationData failed"));
#endif
#ifdef	protector_logdat
				_Log(65,0);
#endif
				dwRtn=9;
				goto _Error;
			}
			dwRtn=_StartService();
			if(dwRtn==-1)
			{
				//不能容忍的错误
				dwRtn=4;
				goto _Error;
			}
			else if(dwRtn==126)
			{
				//需要重启计算机
				goto _Error;
			}
			else if(dwRtn==0)
			{
				//服务不存在或其它原因失败,重新创建服务
#ifdef	protector_debug
				_Log(_T("StartProtector:Service does not exist,create service"));
#endif
#ifdef	protector_logdat
				_Log(66,0);
#endif
				//不管三七二十一，先删除，再创建
				_DelService();

				if(!_CreateService())
				{
					dwRtn=5;
					goto _Error;
				}
				dwRtn=_StartService();
				//创建服务成功，启动服务
				if(dwRtn==126)
				{
					//需要重启计算机
					goto _Error;
				}
				else if(dwRtn!=1)
				{
					dwRtn=4;
					goto _Error;
				}

			}
#ifdef	protector_debug
			_Log(_T("StartProtector:Load driver ok,now insert current pid"));
#endif
#ifdef	protector_logdat
			_Log(67,0);
#endif
		}

		//先把当前PID加入到列表中，才能进行驱动初始化，因为很多初始化需要保护进程列表
		if(Fake_InsertCurrentPid()==0)
		{
			dwRtn=6;
			goto _Error;
		}

		if((dwStatus=_InsertCurrentPid())!=0)
		{
#ifdef	protector_debug
			_Log(_T("StartProtector:_InsertCurrentPid failed %d"),dwStatus);
#endif
#ifdef	protector_logdat
			_Log(68,1,dwStatus);
#endif
			dwRtn=7;
			goto _Error;
		}

		//初始化当前的系统目录
		if(!_InitSystemDir())
		{
#ifdef	protector_logdat
			_Log(122,0);	//StartProtector:_InitSystemDir failed
#endif
			dwRtn=12;
			goto _Error;
		}

#ifdef	protector_logdat
		_Log(84,0);
#endif
		//初始化消息钩子保护模块
		if(!_SecureMsgHook())
		{
#ifdef	protector_debug
			_Log(_T("StartProtector:_SecureMsgHook failed"));
#endif
#ifdef	protector_logdat
			_Log(69,0);
#endif
			dwRtn=8;
			goto _Error;
		}
#ifdef	protector_logdat
		_Log(85,0);
#endif

		//调用一些检测接口
		//11号检测需要间隔12秒
		//12号检测需要间隔6秒
		//其他没有时间限制
		dwRtn=13;
		DoIntCall(13,a,_Error);

		dwRtn=14;
		DoIntCall(14,b,_Error);

		dwRtn=15;
		DoIntCall(15,c,_Error);

		//dwRtn=16;
		//DoIntCall(16,d,_Error);

		dwRtn=0;

_Error:		
#ifdef	protector_logdat
		_Log(86,0);
#endif
		if(hLoadMutex)
		{
			ReleaseMutex(hLoadMutex);
			CloseHandle(hLoadMutex);
		}
		if(hSection)
			ZwClose(hSection);
		if(lpDrvIdentificationMem)
			ZwUnmapViewOfSection(GetCurrentProcess(),lpDrvIdentificationMem);
		if(bPrivilegeNotHold)
		{
#ifdef	protector_logdat
			_Log(89,1,dwRtn);
#endif
			dwRtn=11;
		}
#ifdef	protector_logdat
		_Log(83,1,dwRtn);
#endif

#ifdef protector_ParentCheck
		if(dwRtn==0)
		{
			//告诉老爸，我成功了！！
			hLauncherEvent=OpenEvent(EVENT_MODIFY_STATE,0,_T("PPStartEvent"));
			if(hLauncherEvent)
			{
				if(SetEvent(hLauncherEvent)==0)
				{
#ifdef	protector_logdat
					_ErrorRet(110);
#endif
				}
			}
			else
			{
#ifdef	protector_logdat
				_ErrorRet(109);
#endif
			}
		}
#endif

		VMProtectEnd
		return dwRtn;
	}

#ifdef	protector_ParentCheck
	typedef struct _CHECK_LOADER
	{
		DWORD dwSec;
		DWORD dwPid;
	}CHECK_LOADER;

	DWORD _GetParentProcessId()
	{
		PROCESS_BASIC_INFORMATION stPI={0};
		DWORD dwTemp=0,dwStatus=0;
		VMProtectBeginMutation
		if((dwStatus=ZwQueryInformationProcess(GetCurrentProcess(),ProcessBasicInformation,&stPI,sizeof(stPI),&dwTemp))!=0)
		{
#ifdef protector_debug
			_Log(_T("_GetParentProcessId:ZwQueryInformationProcess failed %x"),dwStatus);
#endif
#ifdef	protector_logdat
			_Log(72,1,dwStatus);
#endif
			return 0;
		}
		else if(stPI.InheritedFromUniqueProcessId==0)
		{
#ifdef protector_debug
			_Log(_T("_GetParentProcessId:ZwQueryInformationProcess okey but InheritedFromUniqueProcessId is 0"));
#endif
#ifdef	protector_logdat
			_Log(90,0);
#endif
			return 0;
		}
		VMProtectEnd
		return stPI.InheritedFromUniqueProcessId;
	}

#pragma optimize("",  off)
	BYTE WINAPI _Loader_CheckSum(PBYTE bArray,DWORD dwArrSize)
	{
		BYTE bCheck = bArray[0];
		VMProtectBeginMutation
		while(dwArrSize>0)
		{
			dwArrSize--;
			bCheck ^= bArray[dwArrSize];
		}
		VMProtectEnd
		return bCheck;
	}
#pragma optimize("",  on)

	BOOL WINAPI _DecryptionMem(PVOID pBuff,CHECK_LOADER *pCheck)
	{
		DWORD dwMagic1,dwMagic2,dwMagic1Con;
		DWORD dwRandom;
		DWORD dwLastDword;
		BYTE bCheckSum;
		BYTE pContext[17];
		BYTE pRandom[4];
		VMProtectBeginMutation

		dwMagic1 = 0x4d5a4d57;
		dwMagic2 = 0x5a4d584c;

		memcpy(pContext,pBuff,17);
		memcpy(&dwLastDword,pContext+13,4);
		dwRandom = dwLastDword ^ dwMagic2;
		memcpy(pRandom,&dwRandom,4);
		bCheckSum = pContext[12];
		if(bCheckSum != _Loader_CheckSum(pContext,12))//checksum error
			return FALSE;
		//check sum ok, now loop and xor again, from tail to head
		for(int i = 8;i>=0;i--)
		{
			pContext[i+3] ^= pRandom[0];
			pContext[i+2] ^= pRandom[1];
			pContext[i+1] ^= pRandom[2];
			pContext[i] ^= pRandom[3];
		}
		memcpy(&dwMagic1Con,pContext+8,4);
		if(dwMagic1Con != dwMagic1)//magic1不对称
			return FALSE;
		memcpy(pCheck,pContext,8);
		VMProtectEnd
		return TRUE;
	}

	bool _CheckLoader()
	{
		PVOID lpMem=0;
		HANDLE hSection=0,hEvent=0;
		bool   bRtn=false;
		DWORD  dwSecGap=0,dwPPid=0;
		CHECK_LOADER stLoader={0};

		VMProtectBeginMutation
		
		//读取共享内存
		//lpMem=_OpenSharedMem(_T("\\BaseNamedObjects\\PP_Lanucher_Mem1"),&hSection,256);
		lpMem=_OpenSharedMem(_T("PP_Lanucher_Mem1"),&hSection,256);
		if(!lpMem)
		{
#ifdef	protector_logdat
			_Log(103,0);
#endif
			goto _end;
		}

		//解密
		if(!_DecryptionMem(lpMem,&stLoader))
		{
#ifdef	protector_logdat
			_Log(104,0);
#endif
			goto _end;
		}
		
		//检测时间间隔
		dwSecGap=GetTickCount()-stLoader.dwSec;
		if(dwSecGap>=10000)
		{
#ifdef	protector_logdat
			_Log(105,1,dwSecGap);
#endif
			goto _end;
		}

		//检测父进程PID
		dwPPid=_GetParentProcessId();
		if(dwPPid!=stLoader.dwPid)
		{
#ifdef	protector_logdat
			_Log(106,2,dwPPid,stLoader.dwPid);
#endif
		}
		else
			bRtn=true;

_end:
		//if(lpMem)
			//ZwUnmapViewOfSection(GetCurrentProcess(),lpMem);
		
		if(lpMem)
			UnmapViewOfFile(lpMem);
		if(hSection)
			ZwClose(hSection);
		
		//通知父进程退出
		hEvent=OpenEvent(EVENT_MODIFY_STATE,0,_T("PP_Lanucher_Event1"));
		if(hEvent!=0)
		{
#ifdef	protector_logdat
			_Log(134,1,hEvent);	//_CheckLoader:OpenEvent PP_Lanucher_Event1 okey,hEvent is %x
#endif
			if(!SetEvent(hEvent))
			{
#ifdef	protector_logdat
				_ErrorRet(108);
#endif
			}
#ifdef	protector_logdat
			_Log(135,1,hEvent);	//_CheckLoader:set PP_Lanucher_Event1 okey
#endif
			CloseHandle(hEvent);
		}
		else
		{
#ifdef	protector_logdat
			_ErrorRet(107);
#endif	
		}
		VMProtectEnd
		return bRtn;
	}
#endif

}