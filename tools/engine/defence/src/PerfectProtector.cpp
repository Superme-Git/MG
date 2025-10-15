//#include "stdafx.h"
#include <windows.h>
#include <tchar.h>
#include <winioctl.h>	//用于i/o IRP
#include <Aclapi.h>		//用于修改服务属性和参数

#include "native.h"
#include "Cross_xlzj.h"
#include "PerfectProtector.h"

#ifdef	X64_Surport
#include "PerfectProtector-x64.h"
#endif




#include "VMProtectSDK.h"


#pragma comment(lib,"shlwapi.lib")
#pragma comment(lib,"ntdll.lib")
#pragma comment(lib,"kernel32.lib")
	
namespace Protector
{
//#define	protector_debug	1


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

	//DWORD  Common_Last_CheckDate=0;
	WORD	GACD_CallGate_Selector[3]={0};

	bool	bPrivilegeNotHold=false;
	bool	bIsX64=false;

	TCHAR	szWhiteDll1[64];
	TCHAR	szWhiteDll2[64];

	PWCHAR szWhiteDll3=L"uxtheme.dll";

	TCHAR	*szServiceName=_T("SecRomDrv");
	TCHAR	*szDisplayName=_T("Secure CDROM Property");
	TCHAR	*szImageFileName=_T("system32\\DRIVERS\\cdrom.sys");
	TCHAR   szDriverName[64]={0};

	HANDLE	hLogFile;
	
	DWORD dwMajorVersion=0;
	DWORD dwMinorVersion=0;
	DWORD dwMajorSP=0;
	DWORD dwMinorSP=0;
	DWORD dwBuildNumber=0;

	DWORD dwKernelMajorVersion=0;
	DWORD dwKernelMinorVersion=0;

	HANDLE hSectionShareMem=0;
	PVOID lpDrvIdentificationMem=0;

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
			add		eax,30000
			stosd						;写入ID
			//mov		byte ptr [edi-1],1	;写入ring3标记

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
			add		eax,30000
			stosd						;写入ID
			//mov		byte ptr [edi-1],3	;写入ring3,ErrorCode标记

			mov		ecx,edx				;写入剩余参数
			rep		movsd
			popfd
			popad
		}
		SetFilePointer(hLogFile,0,0,FILE_END);
		WriteFile(hLogFile,szBuf,dwSize,&dwRtn,0);
	}

	HANDLE	_InitLog(TCHAR* DirName,TCHAR *lpFileName)
	{
		HANDLE hFile;
		SYSTEMTIME	stTime;
		TCHAR szBuf[512]={0};
		DWORD dwWrite=0;
		//	DWORD dwSize;
		//	unsigned long	dwRtn;
		lstrcat(szBuf,DirName);
		lstrcat(szBuf,lpFileName);
		
		
		hFile=CreateFile(szBuf,GENERIC_WRITE,FILE_SHARE_READ | FILE_SHARE_WRITE,\
			0,CREATE_NEW,FILE_ATTRIBUTE_NORMAL,0);
		if(hFile==INVALID_HANDLE_VALUE || hFile==0)
		{
			hFile=CreateFile(szBuf,GENERIC_WRITE,FILE_SHARE_READ | FILE_SHARE_WRITE,\
				0,OPEN_EXISTING,FILE_ATTRIBUTE_NORMAL,0);
			if(hFile==INVALID_HANDLE_VALUE || hFile==0)
				hFile=0;
		}

		hLogFile=hFile;

		if(hFile)
		{	
			if(GetFileSize(hFile,0)>1024*1024)
			{
				CloseHandle(hFile);
				DeleteFile(szBuf);
				hFile=CreateFile(szBuf,GENERIC_READ | GENERIC_WRITE,FILE_SHARE_READ | FILE_SHARE_WRITE,\
					0,CREATE_ALWAYS,FILE_ATTRIBUTE_NORMAL,0);
				hLogFile=hFile;
			}
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


	int	_ErrorRet(DWORD dwID)
	{
		DWORD	dwErrCode;
		//TCHAR	*lpMsg=0;
		dwErrCode=GetLastError();
		_LogErrorCode(dwID,1,dwErrCode);
		return dwErrCode;
	}

	void _LogByte(byte* Buf,DWORD dwSize)
	{
		DWORD dwNum=0;

		if(dwSize==0)
			return;

		dwNum=(dwSize-1)/16 + 1;
		for(unsigned int i=0;i<dwNum;i++)
		{
			_Log(159,16,Buf[i*16+0],Buf[i*16+1],Buf[i*16+2],Buf[i*16+3],Buf[i*16+4],Buf[i*16+5],Buf[i*16+6],Buf[i*16+7],Buf[i*16+8],Buf[i*16+9],Buf[i*16+10],Buf[i*16+11],Buf[i*16+12],Buf[i*16+13],Buf[i*16+14],Buf[i*16+15]);
			//%02x %02x %02x %02x %02x %02x %02x %02x-%02x %02x %02x %02x %02x %02x %02x %02x 
		}
		return;
	}

	int	_SetDebugEnv(TCHAR* LogDir,bool bSetMiniDump)
	{
		HKEY hKey=0;
		TCHAR szBuf[MAX_PATH]={0};
		int	 dwValue;
		
		if(RegCreateKey(HKEY_LOCAL_MACHINE,_T("SYSTEM\\CurrentControlSet\\Control\\CrashControl"),&hKey)==ERROR_SUCCESS)
		{
			dwValue=1;
			if(RegSetValueEx(hKey,_T("AutoReboot"),0,REG_DWORD,(const unsigned char *)&dwValue,4))
			{
				_ErrorRet(97);		
				RegCloseKey(hKey);
				return 0;
			}
			if(RegSetValueEx(hKey,_T("Overwrite"),0,REG_DWORD,(const unsigned char *)&dwValue,4))
			{
				_ErrorRet(98);			
				RegCloseKey(hKey);
				return 0;
			}
			
			if(bSetMiniDump)
			{
				dwValue=3;
				if(RegSetValueEx(hKey,_T("CrashDumpEnabled"),0,REG_DWORD,(const unsigned char *)&dwValue,4))
				{
					_ErrorRet(99);		
					RegCloseKey(hKey);
					return 0;
				}

				lstrcat(szBuf,LogDir);
				//lstrcat(szBuf,_T("log_memory.dmp"));

				//RegSetValueEx(hKey,_T("DumpFile"),0,REG_EXPAND_SZ,(const unsigned char *)szBuf,lstrlen(szBuf)*sizeof(TCHAR));
				if(RegSetValueEx(hKey,_T("MinidumpDir"),0,REG_EXPAND_SZ,(const unsigned char *)szBuf,lstrlen(szBuf)*sizeof(TCHAR)))
				{
					_ErrorRet(100);		
					RegCloseKey(hKey);
					return 0;
				}

			}

			RegCloseKey(hKey);
			return 1;
		}
		else
		{
			_ErrorRet(4);
			return 0;
		}
	}

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

	__forceinline char *_GetClientLoadLibraryAddress(void)
	{
		char *dwRtn=0;
		//VMProtectBeginVirtualization
		__asm
		{
			mov	eax,fs:[30h]
			
			test	eax,eax
			jz	_end

			mov	eax,dword ptr [eax+2ch]	;peb->KernelCallbackTable
			test	eax,eax
			jz	_end
			mov	ecx,dwTableIndex
			lea	eax,[eax+ecx*4]
			mov	eax,dword ptr [eax]
_end:
			mov	dwRtn,eax
		}
		//VMProtectEnd
		return dwRtn;
	}

	int	_SearchRet(char *lpBuf)
	{
		VMProtectBeginMutation
		char *TempPos=0;
		char *BeginPos=lpBuf;
		char *TempBuf=0;
		MEMORY_BASIC_INFORMATION stMBI={0};
		char *lpNtdllBegin=0;
		char *lpNtdllEnd=0;
		char *lpNtdll=0;
		DWORD dwFindJmp=0;

		lpNtdll=(char *)GetModuleHandle(_T("user32.dll"));
		lpNtdllBegin=lpNtdll;

		lpNtdll+=((IMAGE_DOS_HEADER *)lpNtdll)->e_lfanew;
		lpNtdllEnd=lpNtdllBegin+ ((IMAGE_NT_HEADERS32 *)lpNtdll)->OptionalHeader.SizeOfImage;
		
		for(int i=0;i<512;)
		{
			if((unsigned char)lpBuf[i]==0xe9)
			{
				if(dwFindJmp++>=10)
				{
					_Log(171,0);		//_SearchRet:find e9 more than 10 times,failed!
					return 0;
				}

				TempPos=&lpBuf[i];
				TempBuf=(int)(*(int *)(TempPos+1))+TempPos+5;
				
				if(TempBuf>=lpNtdllBegin && TempBuf<=lpNtdllEnd)
				{
					VirtualQuery(TempBuf,&stMBI,sizeof stMBI);
					if(stMBI.State!=MEM_FREE)
					{
						_Log(170,4,&lpBuf[i],lpNtdllBegin,lpNtdllEnd,TempBuf);		//_SearchRet:find e9 at %08x between %08x %08x,set search buf to %08x
						i=0;
						lpBuf=TempBuf;
					}
					else
						i++;
				}
				else
					i++;

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
			_ErrorRet(6);	//_EditHookApi:VirtualProtect
			return 0;
		}
		RtlMoveMemory(lpOldApi,lpPatch,size);
		return 1;
	}


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

			mov		eax,dwMajorVersion
			cmp		eax,6
			jne		_cmp 
			mov		eax,dwMinorVersion
			cmp		eax,1
			jne		_cmp
			jmp     _skipcmp
			//if windows7,do not compare mp->20 with 0

_cmp:
			cmp		dword ptr [ecx+20h],0
			jne		_Pass
_skipcmp:
			mov		eax,dwParamLibrary
			cmp		dword ptr [ecx+10h],eax	//ecx+10,win2k03,vista,windows7下面是28h,win2k和winxp是24h
			jne		_Pass

			//说明是全局钩子调用，需要加载DLL
			//此处进行白名单检测ptp
			//mov		eax,szWhiteDll1
	
			/*
#ifdef	protector_debug

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

			pushad
			push	dword ptr [ebp+8]
			push	szMsgDllFormat_OK
			call	_Log
			add	esp,8
			popad

			cmp		dwKingSoft1,0
			jz		label1
	
label1:
			cmp		dwKingSoft2,0
			jz		label2
	
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


	int	_SetHook()
	{
		if(!(lpLoadLibraryExW=(char *)GetProcAddress(GetModuleHandle(_T("kernel32.dll")),"LoadLibraryExW")))
		{
			_ErrorRet(7);	//_SetHook:GetProcAddress
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
		MultiByteToWideChar(CP_ACP,0,(LPCSTR)szBuf,wsprintf(szBuf,_T("%s\\%s"),szSysBuf,_T("MSCTF.DLL")),(LPWSTR)szWhiteDll1,sizeof(szWhiteDll1)/2);
		MultiByteToWideChar(CP_ACP,0,(LPCSTR)szBuf,wsprintf(szBuf,_T("%s\\%s"),szSysBuf,_T("uxtheme.dll")),(LPWSTR)szWhiteDll2,sizeof(szWhiteDll2)/2);

		//mbstowcs((wchar_t *)szWhiteDll1,szBuf,wsprintf(szBuf,_T("%s\\%s"),szSysBuf,_T("MSCTF.DLL")));
		//mbstowcs((wchar_t *)szWhiteDll2,szBuf,wsprintf(szBuf,_T("%s\\%s"),szSysBuf,_T("uxtheme.dll")));
		//mbstowcs_s(&dwConvSize,(wchar_t *)szWhiteDll1,sizeof(szWhiteDll1),szBuf,wsprintf(szBuf,_T("%s\\%s"),szSysBuf,_T("MSCTF.DLL")));
		//mbstowcs_s(&dwConvSize,(wchar_t *)szWhiteDll2,sizeof(szWhiteDll2),szBuf,wsprintf(szBuf,_T("%s\\%s"),szSysBuf,_T("uxtheme.dll")));
#else
		wsprintf(szWhiteDll1,_T("%s\\%s"),szSysBuf,_T("MSCTF.DLL"));
		wsprintf(szWhiteDll2,_T("%s\\%s"),szSysBuf,_T("uxtheme.dll"));
#endif
	
#ifdef debug

		MessageBox(0,_T("test1"),_T("test"),0);
#endif
		if(!(lpClientLoadLibraryBegin=_GetClientLoadLibraryAddress()))
		{
#ifdef debug
			MessageBox(0,_T("test2"),_T("test"),0);
#endif
			_Log(8,0);	//_SecureMsgHook:_GetClientLoadLibraryAddress failed
			return 0;
		}
		
		_Log(158,1,lpClientLoadLibraryBegin)	;	//_SecureMsgHook:_GetClientLoadLibraryAddress %08x

#ifdef debug
		MessageBox(0,_T("test3"),_T("test"),0);
		_LogByte((byte *)lpClientLoadLibraryBegin,512);
#endif

		if(!(dwPos=_SearchRet(lpClientLoadLibraryBegin)))
		{
			_Log(9,1,lpClientLoadLibraryBegin);	//_SecureMsgHook:_SearchRet failed,lpClientLoadLibraryBegin %08x
			return 0;
		}
		lpClientLoadLibraryEnd=lpClientLoadLibraryBegin+dwPos;

		_Log(10,2,lpClientLoadLibraryBegin,dwPos);	//_SecureMsgHook:__ClientLoadLibrary begins at %08x,length is %08x

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
			lpSecHead+=1;
		}
		while (NumberOfSections--);
		return -1;
	}

	BOOL CopyToSystemDir(TCHAR* BinPath,TCHAR *FileName)
	{
		TCHAR   szSystemDir[MAX_PATH]={0};
		TCHAR   szFileName[MAX_PATH]={0};
		if(GetSystemDirectory(szSystemDir,MAX_PATH)==0)
		{
			_ErrorRet(137);
			return false;
		}

		lstrcat(szSystemDir,_T("\\drivers\\"));
		lstrcat(szSystemDir,FileName);

		lstrcat(szFileName,BinPath);
		lstrcat(szFileName,FileName);
		//再把文件复制过去
		if(!CopyFile(szFileName,szSystemDir,0))
		{
			_ErrorRet(138);
			return false;
		}
		return true;
	}

	//bEnable=1,从当前目录加载
	//bEnable=2,从system32目录加载
	//bEnable=0,设置为空
	int _ChangeServiceConfig(SC_HANDLE hSvc,int bEnable,TCHAR* BinDir)
	{
		TCHAR	szBuf[MAX_PATH]={0};
		TCHAR   szSystemDir[MAX_PATH]={0};

		if(bEnable==1)
		{	
			//恢复
			lstrcat(szBuf,BinDir);
			lstrcat(szBuf,szDriverName);
			/*
			if(!GetFullPathName(_T("PerfectProtector.sys"),MAX_PATH,szBuf,0))
			{
				_ErrorRet(11);	//_ChangeServiceConfig(%d):GetFullPathName
				return 0;
			}
			*/
		}
		else if(bEnable==2)
		{
			//把驱动文件复制到 windows\system32\drivers目录下加载
			//先获取系统目录
			
			if(GetSystemDirectory(szSystemDir,MAX_PATH)==0)
			{
				_ErrorRet(113);
				return 0;
			}
			
			lstrcat(szSystemDir,_T("\\drivers\\"));
			lstrcat(szSystemDir,szDriverName);
			/*
			//再把文件复制过去
			if(!CopyFile(szBuf,szSystemDir,0))
			{
				_ErrorRet(114);
				return 0;
			}
			*/

			if(!CopyToSystemDir(BinDir,szDriverName))
			{
				return 0;
			}
			if(!CopyToSystemDir(BinDir,_T("pp_data0")))
			{
				return 0;
			}
			
			//准备新的目录
			RtlZeroMemory(szBuf,sizeof(szBuf));
			lstrcpy(szBuf,szSystemDir);
		}
		else
		{
			RtlZeroMemory(szBuf,sizeof(szBuf));
			lstrcpy(szBuf,szImageFileName);
		}

		if(!ChangeServiceConfig(hSvc,SERVICE_NO_CHANGE,SERVICE_NO_CHANGE,SERVICE_NO_CHANGE,szBuf,0,0,0,0,0,0))
		{
			_ErrorRet(13);	//_ChangeServiceConfig:ChangeServiceConfig
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
					_ErrorRet(14);	//_ChangeServiceACL:GlobalAlloc
					goto	_end;
				}
				if(!QueryServiceObjectSecurity(hSvc,DACL_SECURITY_INFORMATION,lpBuf,dwSize,&dwSize))
				{
					_ErrorRet(15);	//_ChangeServiceACL:QueryServiceObjectSecurity
					goto	_end;
				}
			}
			else
			{
				_ErrorRet(16);	//_ChangeServiceACL:QueryServiceObjectSecurity
				goto	_end;
			}
		}

		if(!GetSecurityDescriptorDacl(lpBuf ? lpBuf : (char *)&stSD,&DaclPresent,&lpACL,&DaclDefaulted))
		{
			_ErrorRet(17);	//_ChangeServiceACL:GetSecurityDescriptorDacl
			goto	_end;
		}

		BuildExplicitAccessWithName(&stEA,(TCHAR *)_T("EVERYONE"),SERVICE_START | SERVICE_STOP | SERVICE_CHANGE_CONFIG,SET_ACCESS,NO_INHERITANCE);

		if(SetEntriesInAcl(1,&stEA,lpACL,&lpNewACL))
		{
			_ErrorRet(18);	//_ChangeServiceACL:SetEntriesInAcl
			goto	_end;
		}

		if(!InitializeSecurityDescriptor(&stNewSD,SECURITY_DESCRIPTOR_REVISION))
		{
			_ErrorRet(19);	//_ChangeServiceACL:InitializeSecurityDescriptor
			goto	_end;
		}

		if(!SetSecurityDescriptorDacl(&stNewSD,1,lpNewACL,0))
		{
			_ErrorRet(20);	//_ChangeServiceACL:SetSecurityDescriptorDacl
			goto	_end;
		}

		if(!SetServiceObjectSecurity(hSvc,DACL_SECURITY_INFORMATION,&stNewSD))
			_ErrorRet(21);	//_ChangeServiceACL:SetServiceObjectSecurity
		else
		{
			_Log(22,0);	//_ChangeServiceACL:set new acl successful!
			dwRtn=1;
		}

_end:
		if(lpBuf)
			GlobalFree(lpBuf);
		if(lpNewACL)
			LocalFree(lpNewACL);
		return dwRtn;

	}

	int _StopService(void)
	{
		SERVICE_STATUS	stSvcStatus;
		SC_HANDLE		hSCM,hSvc;
		int				dwRtn=0;

		if(!(hSCM=OpenSCManager(0,0,SC_MANAGER_CONNECT)))
		{
			_ErrorRet(50);	//_StopService:OpenSCManager
			return 0;
		}

		if(!(hSvc=OpenService(hSCM,szServiceName,SERVICE_STOP|SERVICE_CHANGE_CONFIG)))
		{
			_ErrorRet(51);	//_StopService:OpenService
			goto	_end;
		}

		if(!(dwRtn=ControlService(hSvc,SERVICE_CONTROL_STOP,&stSvcStatus)))
		{
			_ErrorRet(52);	//_StopService:ControlService
		}

_end:
		if(hSCM)
			CloseServiceHandle(hSCM);
		if(hSvc)
			CloseServiceHandle(hSvc);
		return dwRtn;
	}

	//返回值
	//-1	失败，必须退出
	//0		失败，因为服务不存在，需要创建服务
	//1		成功
	signed int	_StartService(TCHAR* BinDir)
	{
		SC_HANDLE	hSCM=0,hSvc=0;
		int			dwRtn=-1,dwErrorCode=0,dwReStartTime=0;
		BOOL		bTriedLoadFromSys=false;

		if(!(hSCM=OpenSCManager(0,0,SC_MANAGER_CONNECT)))
		{
			if(GetLastError()==ERROR_ACCESS_DENIED)
				bPrivilegeNotHold=true;
			_ErrorRet(23);	//_StartService:OpenSCManager
			return	-1;
		}

		if(!(hSvc=OpenService(hSCM,szServiceName,SERVICE_START | SERVICE_STOP | SERVICE_CHANGE_CONFIG)))
		{
			dwErrorCode=GetLastError();
			_ErrorRet(24);	//_StartService:OpenService

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
		if(_ChangeServiceConfig(hSvc,1,BinDir))
			_Log(25,0);	//_StartService:Change Service Config okey
		else
		{
			_Log(116,0);	//_StartService;open service okey,but can't change ImagePatch to current dir,try copy and set ImagePath to system32\drivers	

			//返回0，尝试从系统目录启动
			if(_ChangeServiceConfig(hSvc,2,BinDir)==0)
			{
				_Log(117,0);	//_StartService:set ImagePath to system32\drivers failed!

				dwRtn=0;//返回0，尝试重新创建服务
				goto	_end;
			}
			_Log(118,0);	//_StartService:set ImagePath to system32\drivers okey!
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
				_ErrorRet(26);	//_StartService:CreateFile

				_StopService();
			}
		}
		else
		{
			dwErrorCode=GetLastError();
			if(dwErrorCode==ERROR_CHILD_NOT_COMPLETE)
			{
				_Log(27,0);	//_StartService:start service failed for config not completed,sleep 0.5s and restart
				Sleep(1000);
				goto	_ReStart;
			}
			if(dwErrorCode==ERROR_SERVICE_ALREADY_RUNNING)
			{
				hDevice=CreateFile(_T("\\\\.\\SlPerfectProtector"),GENERIC_READ|GENERIC_WRITE,0,0,OPEN_EXISTING,0,0);
				if(hDevice!=INVALID_HANDLE_VALUE)
				{
					dwRtn=1;
					_Log(28,0);	//_StartService:start service failed for competing reason,so open symbolic link directly
				}
				else
				{
					hDevice=0;
					_Log(29,1,dwReStartTime);	//_StartService:#%2d start service failed for driver unloading not completed,sleep 3s and restart
					if(dwReStartTime++<3)
					{
						Sleep(3000);
						goto	_ReStart;
					}
				}
			}
			else if(dwErrorCode==ERROR_MOD_NOT_FOUND)
			{//PP找不到内核模块基址，一般是自动更新引起，需要重启电脑
				_Log(136,0);	//_StartService:StartService return 126,Common_GetImageNameByZwQuery failed, need to restart!		
				dwRtn=126;
			}			
			else if(dwErrorCode==1450)
			{//系统内存不足，需要重启电脑
				_Log(157,0);	//_StartService:StartService return 1450,ExAllocatePool failed, need to restart!	
				dwRtn=1450;
			}
			else if(bTriedLoadFromSys==false)// && dwErrorCode==ERROR_PATH_NOT_FOUND)
			{
				_Log(115,1,GetLastError());//_StartService:StartService fail %d,try copy to system32\drivers,load from there

				//找不到路径，在虚拟磁盘或unc路径中会出现，复制到windows\system32\drivers目录下加载
				if(!_ChangeServiceConfig(hSvc,2,BinDir))
					//复制不过去？
					_Log(111,0);	//_StartService:_ChangeServiceConfig to system32 failed!
				else
				{
					_Log(112,0);	//StartService:_ChangeServiceConfig to system32 oky,try load from system32!
					//复制成功，重新启动服务
					bTriedLoadFromSys=true;//要记下已经这样尝试过了，下一次就不需要再尝试了
					goto	_ReStart;
				}
			}
			else
			{
				if(dwErrorCode==5)
					bPrivilegeNotHold=true;
				_ErrorRet(30);	//_StartService:StartService
			}
		}

		_ChangeServiceConfig(hSvc,0,BinDir);

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
			_ErrorRet(31);	//_CreateService:OpenSCManager
			return	0;
		}

_Create:
		if(ReCreateCount>=4)
		{
			_Log(32,0);	//_CreateService:re create 4 times,return fail!
			goto	_end;
		}

		hSvc=CreateService(hSCM,szServiceName,szDisplayName,\
			SERVICE_ALL_ACCESS,SERVICE_KERNEL_DRIVER,SERVICE_DEMAND_START,\
			SERVICE_ERROR_IGNORE,szImageFileName,0,0,0,0,0);
		if(hSvc)
		{
			_Log(33,0);	//_CreateService:create service ok,begin to change acl
			dwTemp=_ChangeServiceACL(hSvc);
			if(dwTemp)
				_Log(34,0);	//_CreateService:change acl ok
			dwRtn=1;
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
							_Log(38,0);	//_CreateService:DeleteService failed because ERROR_SERVICE_MARKED_FOR_DELETE,now sleep 1 sec and re-create
							Sleep(1000);
							ReCreateCount++;
							CloseServiceHandle(hSvc);
							hSvc=0;

							LPENUM_SERVICE_STATUS lpSeriverBuf;
							DWORD	dwNeedSize;
							DWORD	dwServiceRtn;

							HANDLE hServiceBuf=GlobalAlloc(GPTR,64*1024);
							if(hServiceBuf==0)
								_ErrorRet(123);	//_CreateService:GlobalAlloc
							else
							{
								lpSeriverBuf=(LPENUM_SERVICE_STATUS)GlobalLock(hServiceBuf);
								if(lpSeriverBuf==0)
									_ErrorRet(124);	//_CreateService:GlobalLock
								else
								{
									if(EnumServicesStatus(hSCM,SERVICE_DRIVER,SERVICE_STATE_ALL,lpSeriverBuf,64*1024,&dwNeedSize,&dwServiceRtn,0))
										_Log(125,0);	//_CreateService:EnumServicesStatus okey!Flush all service status;
									else
										_ErrorRet(126);//_CreateService:EnumServicesStatus
									GlobalUnlock(hServiceBuf);
								}
								GlobalFree(hServiceBuf);
							}
							goto	_Create;
						}
						else
							_ErrorRet(39);	//_CreateService:DeleteService
					}
					else
					{
						if(GetLastError()==ERROR_SERVICE_NOT_ACTIVE || GetLastError()==ERROR_INVALID_SERVICE_CONTROL)
						{
							_Log(119,0);//_CreateService:创建服务失败是因为服务已经存在，或服务标记为删除，尝试停止服务发现服务没有启动或请求对服务无效，尝试删除服务再重新创建
							goto	_Delete;
						}
						_ErrorRet(40);	//_CreateService:ControlService
					}
				}
				else
					_ErrorRet(41);	//_CreateService:OpenService
			}
			else
				_ErrorRet(42);	//_CreateService:CreateService
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
			_ErrorRet(43);	//_DelService:CloseHandle
		if(hSCM=OpenSCManager(0,0,SC_MANAGER_ALL_ACCESS))
		{
			if(hSvc=OpenService(hSCM,szServiceName,SERVICE_STOP|DELETE))
			{
				if(!ControlService(hSvc,SERVICE_CONTROL_STOP,&stSvcStatus))
					_ErrorRet(44);	//_DelService:ControlService
				//即使停止服务出错，仍然进行删除
				if(!DeleteService(hSvc))
					_ErrorRet(45);	//_DelService:DeleteService
				if(!CloseServiceHandle(hSvc))
					_ErrorRet(46);	//_DelService:CloseHandle hSvc
			}
			else
				_ErrorRet(47);	//_DelService:OpenService
			if(!CloseServiceHandle(hSCM))
				_ErrorRet(48);	//_DelService:CloseHandle @hSCM
		}
		else
			_ErrorRet(49);	//_DelService:OpenSCManager
		return;
	}


	int _CheckIfDriverLoaded(void)
	{
		VMProtectBeginVirtualization
		if((hDevice=CreateFile(_T("\\\\.\\SlPerfectProtector"),GENERIC_READ|GENERIC_WRITE,0,0,OPEN_EXISTING,\
			0,0))!=INVALID_HANDLE_VALUE)
		{
			return 1;
		}

		_Log(53,1,GetLastError());	//_CheckIfDriverLoaded:CreateFile:\\\\.\\SlPerfectProtector

		hDevice=0;
		VMProtectEnd
		return 0;

	}

	bool _IsSysValid(TCHAR* BinDir,DWORD* lpError)
	{
		TCHAR szBuf[MAX_PATH]={0};
		HANDLE hSection=0;
		HANDLE hFile=0;
		byte* ViewBase=0;
		DWORD* lpFile=0;
		DWORD dwHash=0;
		DWORD dwHashInFile=0;
		SIZE_T dwFileSize=0;
		SECURITY_ATTRIBUTES stSA={0};
		bool bRtn=false;

		//VMProtectBeginMutation

		lstrcat(szBuf,BinDir);
		lstrcat(szBuf,_T("pp_data0"));

		hFile=CreateFile(szBuf,GENERIC_READ,FILE_SHARE_READ,0,OPEN_EXISTING,FILE_ATTRIBUTE_NORMAL,0);
	
		if(hFile==INVALID_HANDLE_VALUE || hFile==0)
		{
			*lpError=GetLastError();
			hFile=0;
			_ErrorRet(165);	//_IsSysValid:CreateFile failed
			goto _return;
		}

		hSection=CreateFileMapping(hFile,0,PAGE_READONLY,0,0,0);
		if(hSection==0)
		{
			_ErrorRet(166);	//_IsSysValid:CreateFileMapping failed
			goto _return;
		}
	
		ViewBase=(byte *)MapViewOfFile(hSection,FILE_MAP_READ,0,0,0);
		if(ViewBase==0)
		{
			_ErrorRet(167);	//_IsSysValid:MapViewOfFile failed
			goto _return;
		}
		
		//get file size
		dwFileSize=*(DWORD *)(ViewBase+*(DWORD *)(ViewBase+0x3c)+0x50);

		//calculate file hash
		dwFileSize-=0x10;
		lpFile=(DWORD *)(ViewBase+0x10);
		dwHash=0x537a1201;
	
		for(int i=0;dwFileSize>0;dwFileSize-=0x10,i+=4)
		{
			dwHash=dwHash^(lpFile[i]+lpFile[i+1])^(lpFile[i+2]-lpFile[i+3]);
		}
		
		//get hash in file
		dwHashInFile=(ViewBase[0]<<24)^(ViewBase[5]<<16)^(ViewBase[0xa]<<8)^(ViewBase[0xf]);

		if(dwHash==dwHashInFile)
			bRtn=true;
		else
			_Log(168,2,dwHash,dwHashInFile);	//_IsSysValid:hash %08x != hash in file %08x

_return:
		
		if(hFile)
			CloseHandle(hFile);
		if(ViewBase)
			UnmapViewOfFile(ViewBase);
		if(hSection)
			CloseHandle(hSection);

		//VMProtectEnd
		return bRtn;
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
			_Log(54,1,dwOutBuf);	//Fake_InsertCurrentPid:IOCTL_INSERT_PID failed %d

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
		_ErrorRet(120);	//_InitSystemDir:GetSystemDirectory faild
		return false;
	}
#else
	char szSystemDirA[MAX_PATH]={0};
	int dwBufSize=0;
	dwBufSize=GetSystemDirectory(szSystemDirA,MAX_PATH);
	if(dwBufSize==0)
	{
		_ErrorRet(120);	//_InitSystemDir:GetSystemDirectory faild
		return false;
	}
	MultiByteToWideChar(CP_ACP,0,(LPCSTR)szSystemDirA,dwBufSize,(LPWSTR)szSystemDir,MAX_PATH);
#endif

	if(!DeviceIoControl(hDevice,IOCTL_INIT_SYSTEM_DIR,(DWORD *)szSystemDir,MAX_PATH,0,0,&dwTemp,0))
	{
		_ErrorRet(121);	//_InitSystemDir:DeviceIoControl faild
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
		_Log(55,2,dwDelPid,dwOutBuf);	//Fake_DelCurrentPid:IOCTL_DEL_PID %d returned %d
		return dwOutBuf;
	}



	__declspec(naked) int _InsertCurrentPid()
	{
		__asm
		{
			push ebp
			mov  ebp,esp
		}

		VMProtectBeginVirtualization
		//DoIntCall(0,ac,_Error)
		//DWORD dwValue3=(DWORD)(DWORD &)lpDrvIdentificationMem;
		
		DoIntCall3Value(0,ac,_Error,CLIENT_NUM_INDEX,CLIENT_NUM_MAGIC,lpDrvIdentificationMem)

_Error:
		VMProtectEnd
		__asm
		{
			mov esp,ebp
			pop ebp
			ret
		}
	}

/*
	DWORD _InsertCurrentPid()
	{
		if(!bIsX64)
			return _InsertCurrentPid_x32();
		else
			return _InsertCurrentPid_x64();
	}
	*/



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

	/*
	int _DelCurrentPid()
	{
		if(!bIsX64)
			return _DelCurrentPid_x32();
		else
			return _DelCurrentPid_x64();
	}
*/

	void StopProtector(void)
	{
		int dwRtn=0,dwStatus=0;
		DWORD dwClientNum=0;
		VMProtectBeginVirtualization
		if(hDevice==0)
		{
			_Log(56,0);	//StopProtector:hDevice is 0,return directly!
			CloseHandle(hLogFile);

			return;
		}

#ifndef	LoadFromLoader
		dwRtn=Fake_DelCurrentPid();
#endif
		//返回的当前保护游戏进程的总数
		dwStatus=_DelCurrentPid();

		//返回值是当前PID列表中剩下PID的个数，如果不为0，直接退出就行，不需要卸载驱动
#ifdef	LoadFromLoader
		//if(dwStatus==0 || dwStatus==-1 || dwStatus==-2 || dwStatus==-3 || dwStatus==-4)

		if(dwStatus<=0)
#else
		if(dwRtn==0 || dwStatus==0 || dwStatus==-1 || dwStatus==-2 || dwStatus==-3 || dwStatus==-4)
#endif
		{
			//pid列表空掉了，说明是最后一个游戏进程，退出前恢复HOOK并卸载驱动
			_Log(87,0);		//StopProtector:this is the last process,unload driver

			_StopService();	
		}

		CloseHandle(hDevice);
		hDevice=0;

		if(hSectionShareMem)
			ZwClose(hSectionShareMem);

		if(lpDrvIdentificationMem)
			ZwUnmapViewOfSection(GetCurrentProcess(),lpDrvIdentificationMem);

		_Log(88,0);	//StopProtector:StopProtector okey,leave!

		CloseHandle(hLogFile);
		VMProtectEnd
		return;
	}

	PVOID _ZwCreateSharedMem(HANDLE* _lpSection)
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
			_Log(57,2,dwRtn,hSection);	//_ZwCreateSharedMem:ZwCreateSection failed %x,%x
			if(dwRtn==0xc0000035)
			{
				//共享内存已经存在，直接打开
				dwRtn=ZwOpenSection(&hSection,SECTION_MAP_READ | SECTION_MAP_WRITE,&stOA);
				if(dwRtn==0)
				{
					_Log(156,0);	//_ZwCreateSharedMem:Create failed try to Open okey,now Map this section
					goto _Map;
				}
				else
					_Log(155,2,dwRtn,hSection);	//_ZwCreateSharedMem:Create failed and ZwOpenSection failed %x,%x

			}
			else if(dwRtn==0xc0000022)
				bPrivilegeNotHold=true;
		}
		else
		{
_Map:
			ViewSize=10240;
			dwRtn=ZwMapViewOfSection(hSection,GetCurrentProcess(),&ViewBase,0,0,&stSectionOffset,&ViewSize,ViewShare,0,PAGE_READWRITE);
			if(dwRtn)
			{
				_Log(58,1,dwRtn);	//_ZwCreateSharedMem:ZwMapViewOfSection failed %x
			}
			else
				RtlZeroMemory(ViewBase,10240);
		}
		*_lpSection=hSection;
		RtlFreeUnicodeString(&stKeyW);
		VMProtectEnd
		return ViewBase;
	}
/*
	PVOID _CreateSharedMem(HANDLE* _lpSection)
	{
		HANDLE hSection=0;

		PVOID ViewBase=0;
		SIZE_T ViewSize=0;

		SECURITY_ATTRIBUTES stSA={0};
		VMProtectBeginVirtualization


		//hSection=CreateFileMapping((HANDLE)-1,&stSA,PAGE_READWRITE,0,0,_T("SecDrvMem1"));
		hSection=CreateFileMapping((HANDLE)-1,&stSA,PAGE_READWRITE,0,10240,_T("\\BaseNamedObjects\\SecDrvMem1"));
		if(hSection==0)
		{
			_ErrorRet(57);

			if(GetLastError()==5)
				bPrivilegeNotHold=true;
			//_Log(57,1,GetLastError());	//_CreateSharedMem:ZwCreateSection failed %x
			
		}
		else
		{
			//Sleep(1000*60);
			ViewSize=10240;

			ViewBase=MapViewOfFile(hSection,FILE_MAP_READ|FILE_MAP_WRITE,0,0,ViewSize);
			if(ViewBase==0)
			{
				_Log(58,1,GetLastError());	//_CreateSharedMem:ZwMapViewOfSection failed %x
			}
			else
				RtlZeroMemory(ViewBase,ViewSize);
		}
		*_lpSection=hSection;
	
		VMProtectEnd
		return ViewBase;
	}
*/
	//用于父进程检测时，在vista下，进程间共享内存需要使用Create/OpenFileMapping，直接使用Zw有问题，打开时返回找不到
	PVOID _ZwOpenSharedMem(TCHAR *MemName,HANDLE *_lpSection,DWORD MemSize)
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
			if(dwRtn==0xc0000022)
				bPrivilegeNotHold=true;
			_Log(153,1,dwRtn);	//_ZwOpenSharedMem:ZwOpenSection %x
		}
		else
		{
			ViewSize=MemSize;
			dwRtn=ZwMapViewOfSection(hSection,GetCurrentProcess(),&ViewBase,0,0,&stSectionOffset,&ViewSize,ViewShare,0,PAGE_READWRITE);
			if(dwRtn)
				_Log(154,1,dwRtn);	//_ZwOpenSharedMem:ZwMapViewOfSection %x
		}

		*_lpSection=hSection;
#ifndef		_UNICODE
		RtlFreeUnicodeString(&stKeyW);
#endif
		VMProtectEnd
		return ViewBase;
	}


	PVOID _OpenSharedMem(TCHAR *MemName,HANDLE *_lpSection,DWORD MemSize)
	{

		HANDLE hSection=0;
		NTSTATUS dwRtn=0;
		PVOID ViewBase=0;
		VMProtectBeginVirtualization
		hSection=OpenFileMapping(PAGE_READWRITE,0,MemName);
		if(hSection==0)
			_ErrorRet(101);
		else
		{
			ViewBase=MapViewOfFile(hSection,FILE_MAP_READ,0,0,MemSize);
			if(ViewBase==0)
				_ErrorRet(102);
		}
		*_lpSection=hSection;
		VMProtectEnd
		return ViewBase;
	}


	PVOID	_SetDrvIdentificationData(HANDLE *lpSection)
	{
		char szBuf[64]={0};
		TCHAR szSystemDir[MAX_PATH]={0};
		PVOID lpBuf;
		VMProtectBeginVirtualization
		if((lpBuf=_ZwCreateSharedMem(lpSection))==0)
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
			mov ebx,0x89fdec37	//dwVar
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
#ifdef	LoadFromLoader
		//再写入系统路径
#ifdef	_UNICODE
		if(!GetSystemDirectory(szSystemDir,MAX_PATH))
		{
			_ErrorRet(120);	//_InitSystemDir:GetSystemDirectory faild
			return false;
		}
#else
		char szSystemDirA[MAX_PATH]={0};
		int dwBufSize=0;
		dwBufSize=GetSystemDirectory(szSystemDirA,MAX_PATH);
		if(dwBufSize==0)
		{
			_ErrorRet(120);	//_InitSystemDir:GetSystemDirectory faild
			return false;
		}
		
		MultiByteToWideChar(CP_ACP,0,(LPCSTR)szSystemDirA,dwBufSize,(LPWSTR)szSystemDir ,MAX_PATH);
#endif
		lstrcpyW((LPWSTR)((char *)lpBuf+23),(LPWSTR)szSystemDir);
#endif
		VMProtectEnd
		return lpBuf;
	}


	bool DisableBreakIn()
	{
		BYTE *lpDbgBreakPoint=0;
		BYTE *lpDbgUserBreakPoint=0;
		BYTE *lpDbgUiIssueRemoteBreakin=0;
		BYTE *lpDbgUiRemoteBreakin=0;
		BYTE * lpExitProcess=0;
		char szBreak1[]={"DbgBreakPoint"};
		char szBreak2[]={"DbgUserBreakPoint"};
		char szBreak3[]={"DbgUiIssueRemoteBreakin"};
		char szBreak4[]={"DbgUiRemoteBreakin"};
		char szExitProcess[]={"ExitProcess"};
		MEMORY_BASIC_INFORMATION stMBI={0};
		HMODULE hNtdll=0;
		VMProtectBeginVirtualization

		hNtdll=GetModuleHandle(_T("ntdll.dll"));
		if(hNtdll==0)
			_ErrorRet(140);	//DisableBreakIn:get ntdll failed

		lpDbgBreakPoint=(BYTE *)GetProcAddress(hNtdll,szBreak1);
		if(lpDbgBreakPoint==0)
		{
			_ErrorRet(141);	//DisableBreakIn:get DbgBreakPoint failed
			return false;
		}

		lpDbgUserBreakPoint=(BYTE *)GetProcAddress(hNtdll,szBreak2);
		if(lpDbgUserBreakPoint==0)
		{
			_ErrorRet(142);	//DisableBreakIn:get DbgUserBreakPoint failed
			return false;
		}

		if(dwMajorVersion!=5 || dwMinorVersion!=0)
		{
			lpDbgUiIssueRemoteBreakin=(BYTE *)GetProcAddress(hNtdll,szBreak3);
			if(lpDbgUiIssueRemoteBreakin==0)
			{
				_ErrorRet(148);	//DisableBreakIn:get DbgUiIssueRemoteBreakin failed
				return false;
			}			
			
			lpDbgUiRemoteBreakin=(BYTE *)GetProcAddress(hNtdll,szBreak4);
			if(lpDbgUiRemoteBreakin==0)
			{
				_ErrorRet(149);	//DisableBreakIn:get DbgUiRemoteBreakin failed
				return false;
			}
		}

		/*lpExitProcess=(BYTE *)GetProcAddress(GetModuleHandle(_T("kernel32")),szExitProcess);
		if(lpExitProcess==0)
		{
			_ErrorRet(143);	//DisableBreakIn:get ExitProcess failed
			return false;
		}
*/
		VirtualQuery(lpDbgBreakPoint,&stMBI,sizeof(stMBI));
		if(!VirtualProtect(stMBI.BaseAddress,stMBI.RegionSize,PAGE_EXECUTE_READWRITE,&stMBI.Protect))
		{
			_ErrorRet(144);	//DisableBreakIn:VirtualProtect lpDbgBreakPoint failed
			return false;
		}

		VirtualQuery(lpDbgUserBreakPoint,&stMBI,sizeof(stMBI));
		if(!VirtualProtect(stMBI.BaseAddress,stMBI.RegionSize,PAGE_EXECUTE_READWRITE,&stMBI.Protect))
		{
			_ErrorRet(145);	//DisableBreakIn:VirtualProtect lpDbgUiRemoteBreakin failed
			return false;
		}
		
		*lpDbgBreakPoint=0xc3;
		*lpDbgUserBreakPoint=0xc3;

		if(dwMajorVersion!=5 || dwMinorVersion!=0)
		{
			VirtualQuery(lpDbgUiIssueRemoteBreakin,&stMBI,sizeof(stMBI));
			if(!VirtualProtect(stMBI.BaseAddress,stMBI.RegionSize,PAGE_EXECUTE_READWRITE,&stMBI.Protect))
			{
				_ErrorRet(150);	//DisableBreakIn:VirtualProtect lpDbgUiIssueRemoteBreakin failed
				return false;
			}		
			VirtualQuery(lpDbgUiRemoteBreakin,&stMBI,sizeof(stMBI));
			if(!VirtualProtect(stMBI.BaseAddress,stMBI.RegionSize,PAGE_EXECUTE_READWRITE,&stMBI.Protect))
			{
				_ErrorRet(151);	//DisableBreakIn:VirtualProtect lpDbgUiRemoteBreakin failed
				return false;
			}	
			*lpDbgUiIssueRemoteBreakin=0xc3;
			*lpDbgUiRemoteBreakin=0xc3;
		}
		VMProtectEnd
		/*
		*lpDbgBreakPoint=0xe8;
		*(DWORD *)(lpDbgBreakPoint+1)=(DWORD)(DWORD &)lpExitProcess-(DWORD)(DWORD &)lpDbgBreakPoint-5;

		*lpDbgUiRemoteBreakin=0xe8;
		*(DWORD *)(lpDbgUiRemoteBreakin+1)=(DWORD)(DWORD &)lpExitProcess-(DWORD)(DWORD &)lpDbgUiRemoteBreakin-5;
		*/

		_Log(146,4,lpDbgBreakPoint,lpDbgUserBreakPoint,lpDbgUiIssueRemoteBreakin,lpDbgUiRemoteBreakin);	//DisableBreakIn:disable DbgBreakPoint %x,DbgUserBreakPoint %x,DbgUiIssueRemoteBreakin %x,DbgUiRemoteBreakin %x okey
		return true;
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
		VMProtectBeginVirtualization
		if((dwStatus=ZwQueryInformationProcess(GetCurrentProcess(),ProcessBasicInformation,&stPI,sizeof(stPI),&dwTemp))!=0)
		{
			_Log(72,1,dwStatus);	//_GetParentProcessId:ZwQueryInformationProcess failed %x
			return 0;
		}
		else if(stPI.InheritedFromUniqueProcessId==0)
		{
			_Log(90,0);	//_GetParentProcessId:ZwQueryInformationProcess okey but InheritedFromUniqueProcessId is 0
			return 0;
		}
		VMProtectEnd
		return stPI.InheritedFromUniqueProcessId;
	}

	__forceinline BYTE WINAPI CheckSum(PBYTE bArray,DWORD dwArrSize)
	{
		BYTE bCheck = bArray[0];
		DWORD _dwSize = dwArrSize;
		//VMProtectBeginVirtualization
		while(_dwSize>0)
		{
			_dwSize--;
			bCheck ^= bArray[_dwSize];
			bCheck += bArray[dwArrSize-_dwSize-1];
		}
	//	VMProtectEnd
		return bCheck;
	}


	//解密函数
	//bool _DecryptionMem(PVOID a,CHECK_LOADER *b)
	//第一个参数就是内存地址，第二个参数是一个结构体，你把解决完的数据写入这个结构体。

	BOOL WINAPI _DecryptionMem(PVOID pBuff,CHECK_LOADER *pCheck)
	{
		DWORD dwMagic1Con;
		DWORD dwRandom;
		DWORD dwLastDword;
		BYTE bCheckSum;
		BYTE pContext[21];
		BYTE bRandom[4];

		VMProtectBeginVirtualization
		memcpy(pContext,pBuff,21);
		memcpy(&dwLastDword,pContext+9,4);
		dwRandom = dwLastDword ^ dwMagic2 ^ dwMagic1;
		memcpy(bRandom,&dwRandom,4);
		bCheckSum = pContext[8];
		if(bCheckSum != (CheckSum(pContext,8) ^ CheckSum(pContext+13,8)))//checksum error
			return FALSE;
		//check sum ok, now loop and xor again, from tail to head
		for(int i = 4;i>=0;i--)
		{
			Decomputation(pContext[i+3],bRandom[0],bRandom[3]);
			Decomputation(pContext[i+2],bRandom[2],bRandom[0]);
			Decomputation(pContext[i+1],bRandom[1],bRandom[2]);
			Decomputation(pContext[i],bRandom[3],bRandom[1]);
		}

		for(int i = 16;i <= 20;i++)
		{
			Decomputation(pContext[i-3],bRandom[1],bRandom[3]);
			Decomputation(pContext[i-2],bRandom[2],bRandom[1]);
			Decomputation(pContext[i-1],bRandom[0],bRandom[2]);
			Decomputation(pContext[i],bRandom[3],bRandom[0]);
		}
		memcpy(&dwMagic1Con,pContext+13,4);
		if(dwMagic1Con != dwMagic1)//magic1不对称
			return FALSE;
		memcpy(pCheck,pContext,8);
		VMProtectEnd
		return TRUE;
	}
/*
	DWORD _CheckLoader()
	{
		PVOID lpMem=0;
		HANDLE hSection=0,hEvent=0;
		DWORD   bRtn=0;
		DWORD  dwSecGap=0,dwPPid=0;
		CHECK_LOADER stLoader={0};

		VMProtectBeginVirtualization

		//读取共享内存
		//lpMem=_OpenSharedMem(_T("\\BaseNamedObjects\\PP_Lanucher_Mem1"),&hSection,256);
		lpMem=_OpenSharedMem(szMapFileName,&hSection,256);
		if(!lpMem)
		{
			_Log(103,0);
			bRtn=1;
			goto _end;
		}

		//解密
		if(!_DecryptionMem(lpMem,&stLoader))
		{
			_Log(104,0);
			bRtn=2;
			goto _end;
		}
		
		//检测时间间隔
		dwSecGap=GetTickCount()-stLoader.dwSec;
		if(dwSecGap>=20000)
		{
			_Log(105,1,dwSecGap);
			bRtn=3;
			goto _end;
		}

		//检测父进程PID
		dwPPid=_GetParentProcessId();
		if(dwPPid!=stLoader.dwPid)
		{
			_Log(106,2,dwPPid,stLoader.dwPid);	//_CheckLoader:父进程pid不符合 (ZwQuery)%x (get from shared mem)%x
			bRtn=4;
		}
		else
			bRtn=0;

_end:
		if(lpMem)
			UnmapViewOfFile(lpMem);
		if(hSection)
			ZwClose(hSection);
		
		//通知父进程退出
		hEvent=OpenEvent(EVENT_MODIFY_STATE,0,szEventName);
		if(hEvent!=0)
		{
			_Log(134,1,hEvent);	//_CheckLoader:OpenEvent PP_Lanucher_Event1 okey,hEvent is %x
			if(!SetEvent(hEvent))
				_ErrorRet(108);
			_Log(135,0);	//_CheckLoader:set PP_Lanucher_Event1 okey
			CloseHandle(hEvent);
		}
		else
			_ErrorRet(107);

		VMProtectEnd
		return bRtn;
	}
*/
#endif

	//获取本地TickCount
DWORD	My_GetTickCount()
{
	DWORD dwRtn=0;

	VMProtectBeginVirtualization
	if(dwKernelMajorVersion==5 && (dwKernelMinorVersion==0 || dwKernelMinorVersion==1))
	{
		_GetTickCount_2kxp()
		__asm mov	dwRtn,eax
	}
	else
	{
		_GetTickCount_2k3VistaWindows7()
		__asm	mov	dwRtn,eax
	}
	VMProtectEnd
	return dwRtn;
}

	/********************
	返回值
	0	成功
	1	系统版本号不对
	2	创建互斥体失败
	3	等待互斥体超时(打开过快)
	4	启动服务失败
	5	创建服务失败
	6	pid插入fake列表失败
	7	pid插入common列表失败(驱动的释放还没有完成)
	8	初始化钩子保护模块失败
	9	初始化驱动加载验证数据失败
	10	父进程检测失败
	11	非管理员权限
	12	初始化系统目录失败
	13	13号较验失败
	14	14号较验失败
	15	15号较验失败
	16	16号较验失败
	20	修改ntdll DbgBreakIn失败
	21	获取调用门选择子无效
	22  打开共享内存失败
	23	打开时间过长，请关闭所有游戏客户端，重新尝试(因为laucher打开client时为client准备的加密数据块超过了一个指定时间间隔以后，client才开始准备解密。即父进程检测机制中的密文数据已过期)
	24	多开
	25	pp_data0文件非法
	26	pp-data0文件不存在

	126	系统更新完需要重启
	1450 系统内存不足，一般由于系统内存碎片过多造成，重启后一般可以解决

	错误码提示参考值：
	1		当前使用的操作系统版本不支持
	3		请等待游戏完全启动以后再尝试
	7		请等待游戏完全结束以后再尝试
	4和11	请确认游戏没有被杀毒软件或防火墙禁止，并确认您是在管理员帐号下启动游戏
	126		由于操作系统有未完成的操作，如自动更新，请完成这些操作并重启计算机再尝试打开游戏
	1450    系统内存不足，请注销或重启电脑以后再尝试!
	其他值	游戏初始化失败，这时可弹出一个对话框，让玩家给我们发送pp_005.dat

	*********************/
	int StartProtector(TCHAR* BinDir)
	{
		int	bCanLoad=0;
		int dwRtn=0;
		unsigned long dwTemp=0;
		int dwStatus=0;
		//DWORD dwParChkRtn=0;
		OSVERSIONINFOEX	stOsInfo;
		HANDLE hLoadMutex=0;
		HANDLE hLauncherEvent=0;
		DWORD bCheckLoaderOK=0;
		DWORD dwError=0;
		DWORD dwClientNum=0;

#ifdef protector_ParentCheck
		PVOID lpCheckLoaderMem=0;
		HANDLE hCheckLoaderSection=0,hCheckLoaderEvent=0;
		DWORD   dwParChkRtn=0;
		DWORD  dwSecGap=0,dwPPid=0;
		CHECK_LOADER stLoader={0};
#endif

		VMProtectBeginVirtualization

		//先判断操作系统版本是否符号要求,并根据版本初化一些变量
		RtlZeroMemory(&stOsInfo,sizeof stOsInfo);
		stOsInfo.dwOSVersionInfoSize=sizeof stOsInfo;

		GetVersionEx((LPOSVERSIONINFO)&stOsInfo);

		dwMajorVersion=stOsInfo.dwMajorVersion;
		dwMinorVersion=stOsInfo.dwMinorVersion;
		dwMajorSP=stOsInfo.wServicePackMajor;
		dwMinorSP=stOsInfo.wServicePackMinor;
		dwBuildNumber =stOsInfo.dwBuildNumber;

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
#ifdef Windows7_Surport
			else if(stOsInfo.dwMinorVersion==1)
			{
				//windows7
				dwTableIndex=0x41;
				dwParamLibrary=0x28;
				bCanLoad=1;
			}
#endif
		}
#ifdef X64_Surport
		bIsX64=Is64BitSystem();
#endif
		if(!bIsX64)
			_InitLog(BinDir,_T("pp_005.dat"));
		else
			_InitLog(BinDir,_T("pp_005_x64.dat"));

		_Log(5,6,dwMajorVersion,dwMinorVersion,dwMajorSP,dwMinorSP,dwBuildNumber,bIsX64);
		//StartProtector:v005 OS Version %d %d,sp %d %d,build %d,b4bit %d

		if(!bCanLoad)
		{
			//系统版本不对，不能启动
			_Log(93,0);		//StartProtector:os does not supported!
			return 1;
		}


		if(bIsX64==false)
			lstrcat(szDriverName,_T("PerfectProtector.sys"));
		else
			lstrcat(szDriverName,_T("PerfectProtector-x64.sys"));

#ifdef protector_ParentCheck
		//dwParChkRtn=_CheckLoader();

		//读取共享内存
		//lpCheckLoaderEventMem=_OpenSharedMem(_T("\\BaseNamedObjects\\PP_Lanucher_Mem1"),&hSection,256);
		lpCheckLoaderMem=_OpenSharedMem(szMapFileName,&hCheckLoaderSection,256);
		if(!lpCheckLoaderMem)
		{
			_Log(103,0);	//_CheckLoader:_OpenSharedMem failed
			dwParChkRtn=1;
			goto _end;
		}
		
		_Log(161,0);	//StartProtector:_CheckLoader _OpenSharedMem okey

		//goto _end;

		//解密
		if(!_DecryptionMem(lpCheckLoaderMem,&stLoader))
		{
			_Log(104,0);	//_CheckLoader:_DecryptionMem failed
			dwParChkRtn=2;
			goto _end;
		}

		//检测时间间隔
		dwSecGap=GetTickCount()-stLoader.dwSec;
		if(dwSecGap>=20000)
		{
			_Log(105,1,dwSecGap);	//_CheckLoader:时间间隔大于规定值 %d
			dwParChkRtn=3;
			goto _end;
		}
		
		_Log(162,0);	//StartProtector:_CheckLoader _DecryptionMem and check tickcount okey

		//检测父进程PID
		dwPPid=_GetParentProcessId();
		if(dwPPid!=stLoader.dwPid)
		{
			_Log(106,2,dwPPid,stLoader.dwPid);	//_CheckLoader:父进程pid不符合 (ZwQuery)%x (get from shared mem)%x
			dwParChkRtn=4;
		}
		else
		{
			_Log(163,0);	//StartProtector:_CheckLoader set dwParChkRtn to 0 and set bCheckLoaderOK to 1

			dwParChkRtn=0;
			bCheckLoaderOK=1;
		}

_end:
		if(lpCheckLoaderMem)
			UnmapViewOfFile(lpCheckLoaderMem);
		if(hCheckLoaderSection)
			ZwClose(hCheckLoaderSection);

		//通知父进程退出
		hCheckLoaderEvent=OpenEvent(EVENT_MODIFY_STATE,0,szEventName);
		if(hCheckLoaderEvent!=0)
		{
			_Log(134,1,hCheckLoaderEvent);	//_CheckLoader:OpenEvent PP_Lanucher_Event1 okey,hEvent is %x
			if(!SetEvent(hCheckLoaderEvent))
				_ErrorRet(108);
			_Log(135,0);	//_CheckLoader:set PP_Lanucher_Event1 okey
			CloseHandle(hCheckLoaderEvent);
		}
		else
			_ErrorRet(107);	//_CheckLoader:OpenEvent

		if(dwParChkRtn)
		{
			_Log(59,1,dwParChkRtn);	//StartProtector:_CheckLoader failed %x
			if(dwParChkRtn==3)
				return 23;
			else
				return 10;
		}
		_Log(60,0);//StartProtector:Check loader okey
#else
		bCheckLoaderOK=1;
#endif

#ifdef		DISABLE_BREAK_IN
			if(!DisableBreakIn())
			{
				_Log(139,0);	//StartProtector:DisableBreakIn failed,return 20
				dwRtn=20;
				goto _Error;
			}
#endif
		

		_Log(160,1,bCheckLoaderOK);	//StartProtector: set bCheckLoaderOK %d

		//---------------------
		//加载保护驱动
		//---------------------
		//需要先锁上
		//因为如果2个客户端同时在启动，_CheckIfDriverLoaded都返回false,则两个客户端都会进行加载驱动的操作，必须要避免
		if((hLoadMutex=CreateMutex(0,1,_T("PP_LOAD_MUTEXT")))==0)
		{

			_ErrorRet(61);	//StartProtector:CreateMutex PP_LOAD_MUTEXT failed
			dwRtn=2;
			goto _Error;
		}
		if(WaitForSingleObject(hLoadMutex,10000))
		{
			_ErrorRet(62);	//StartProtector:WaitForSingleObject PP_LOAD_MUTEXT failed
			dwRtn=3;
			goto _Error;
		}

		//判断是否已加载驱动
		if(_CheckIfDriverLoaded())
		{

			//设置内存转储文件相关的注册表变量
			//_SetDebugEnv(BinDir,0);

			//驱动已经加载
			_Log(63,0);	//StartProtector:Driver has been loaded,begin to insert current pid

			//打开共享内存

			lpDrvIdentificationMem=_ZwOpenSharedMem(_T("\\BaseNamedObjects\\SecDrvMem1"),&hSectionShareMem,10240);

			_Log(152,1,lpDrvIdentificationMem);	//StartProtector:get SecDrvMem at %x

			if(lpDrvIdentificationMem==0)
			{
				dwRtn=22;
				goto _Error;
			}
			
			//多开判断
			((PDWORD)lpDrvIdentificationMem)[0]=((PDWORD)lpDrvIdentificationMem)[0];

			if(((PDWORD)lpDrvIdentificationMem)[CLIENT_NUM_INDEX]==0)
				dwClientNum=0;
			else
				dwClientNum=((PDWORD)lpDrvIdentificationMem)[CLIENT_NUM_INDEX]^CLIENT_NUM_MAGIC;
			
			_Log(172,2,dwClientNum,CLIENT_NUM_MAX);	//StartProtector:get client num %d,current max %d

			if(dwClientNum>=CLIENT_NUM_MAX)
			{
				
				dwRtn=24;
				goto _Error;
			}
			//增加的操作由内核来完成
			//dwClientNum++;
			//((PDWORD)lpDrvIdentificationMem)[CLIENT_NUM_INDEX]=dwClientNum ^ CLIENT_NUM_MAGIC;

		}
		else
		{
			if(bIsX64==false)
			{
				//判断pp_data0文件是否合法，防止恶意pp_data0被木马利用加载
				if(!_IsSysValid(BinDir,&dwError))
				{
					_Log(169,0);	//StartProtector:_IsSysValid failed,pp_data0 not valid!
					if(dwError==2 || dwError==3)
						dwRtn=26;	//不存在
					else
						dwRtn=25;	//文件非法
					goto _Error;
				}
			}

			//设置内存转储文件相关的注册表变量
			_SetDebugEnv(BinDir,0);

			//驱动没有加载，先看看服务是不是已经存在
			_Log(64,0);	//StartProtector:Driver does not load,start service
			if((lpDrvIdentificationMem=_SetDrvIdentificationData(&hSectionShareMem))==0)
			{
				_Log(65,0);	//StartProtector:_SetDrvIdentificationData failed
				dwRtn=9;
				goto _Error;
			}

			((PDWORD)lpDrvIdentificationMem)[CLIENT_NUM_INDEX]=((PDWORD)lpDrvIdentificationMem)[CLIENT_NUM_INDEX];


			//多开设置
			//dwClientNum=1;
			//((PDWORD)lpDrvIdentificationMem)[CLIENT_NUM_INDEX]=dwClientNum ^ CLIENT_NUM_MAGIC;

			//加载驱动
#ifdef Enable_Driver
			dwRtn=_StartService(BinDir);
#else
			dwRtn=1;
#endif
			if(dwRtn==-1)
			{
				//不能容忍的错误
				dwRtn=4;
				goto _Error;
			}
			else if(dwRtn==126)
			{
				//有更新，需要重启计算机
				goto _Error;
			}			
			else if(dwRtn==1450)
			{
				//内存不足，需要注销或重启计算机
				goto _Error;
			}

			else if(dwRtn==0)
			{
				//服务不存在或其它原因失败,重新创建服务
				_Log(66,0);	//StartProtector:Service does not exist,create service

				//不管三七二十一，先删除，再创建
				_DelService();

				if(!_CreateService())
				{
					dwRtn=5;
					goto _Error;
				}

				dwRtn=_StartService(BinDir);
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

			_Log(67,0);	//StartProtector:Load driver ok,now insert current pid
		}

#ifndef	LoadFromLoader
		//先把当前PID加入到列表中，才能进行驱动初始化，因为很多初始化需要保护进程列表
		if(Fake_InsertCurrentPid()==0)
		{
			dwRtn=6;
			goto _Error;
		}
#endif

#ifdef PP_InsertPid
		if((dwStatus=_InsertCurrentPid())!=0)
		{
			//0 成功 1 pid已经在保护列表中 2保护列表已满 3驱动正在卸载 4分配内存失败 5pid eprocess无效 6增加pid计数失败
			_Log(68,1,dwStatus);	//StartProtector:_InsertCurrentPid failed %d
			dwRtn=7;
			if(dwStatus==2)
				dwRtn=24;	//多开
			goto _Error;
		}
#endif

#ifndef	LoadFromLoader
		//初始化当前的系统目录
		if(!_InitSystemDir())
		{
			_Log(122,0);	//StartProtector:_InitSystemDir failed
			dwRtn=12;
			goto _Error;
		}
#endif

#ifdef PP_CallGate
		GACD_CallGate_Selector[0]=0;
		GACD_CallGate_Selector[1]=0;
		GACD_CallGate_Selector[2]=(WORD)(*(DWORD *)lpDrvIdentificationMem) ^ GACD_Service_MagicWord;

		if(GACD_CallGate_Selector[2]==0 || GACD_CallGate_Selector[2]&3)
		{
			_Log(147,1,(DWORD)GACD_CallGate_Selector[2]);	//StartProtector:get GACD_CallGate_Selector %x,not valid!
			dwRtn=21;
			goto _Error;
		}

		_Log(84,1,(DWORD)GACD_CallGate_Selector[2]);	//StartProtector:insert pid okey and get GACD_CallGate_Selector %x,begin to hook LoadLibraryExW

		GACD_CallGate_Selector[2]|=3;
#endif

		//获取内核中判断的操作系统版本

		dwKernelMajorVersion=((DWORD *)lpDrvIdentificationMem)[1];
		dwKernelMinorVersion=((DWORD *)lpDrvIdentificationMem)[2];
		
		_Log(164,2,dwKernelMajorVersion,dwKernelMinorVersion);	//StartProtector:get version %d %d from shared memory

#ifdef	DISABLE_MSG_HOOK
		//初始化消息钩子保护模块
		if(!_SecureMsgHook())
		{
			_Log(69,0);	//StartProtector:_SecureMsgHook failed
			dwRtn=8;
			goto _Error;
		}

		_Log(85,0);	//StartProtector:Sec MsgHook okey,begin to verify driver
#endif


		//调用一些检测接口
		//11号检测需要间隔12秒
		//12号检测需要间隔6秒
		//其他没有时间限制
#ifdef PP_IntCallCheck
		
		dwRtn=13;
		DoIntCall(13,a,_Error);
	
	
		dwRtn=14;
		DoIntCall(14,b,_Error);


		dwRtn=15;
		DoIntCallValue(15,c,_Error,bCheckLoaderOK);

#endif

		dwRtn=0;

_Error:		

#ifdef dp
		DbgPrint("StartProtector:return to game %d\n",dwRtn);
#endif
		_Log(86,0);	//StartProtector:ready to leave,do some clean

		if(hLoadMutex)
		{
			ReleaseMutex(hLoadMutex);
			CloseHandle(hLoadMutex);
		}

		if(bPrivilegeNotHold)
		{
			_Log(89,1,dwRtn);	//StartProtector:Find bPrivilegeNotHold is true,change dwRtn from %d to 11
			dwRtn=11;
		}
		_Log(83,1,dwRtn);		//StartProtector:return to game : %d

#ifdef protector_ParentCheck
		if(dwRtn==0)
		{
			//告诉老爸，我成功了！！
			hLauncherEvent=OpenEvent(EVENT_MODIFY_STATE,0,_T("PPStartEvent"));
			if(hLauncherEvent)
			{
				if(SetEvent(hLauncherEvent)==0)
					_ErrorRet(110);
			}
			else
				_ErrorRet(109);
		}
#endif

		VMProtectEnd
		return dwRtn;
	}

#pragma optimize("",  on)


}