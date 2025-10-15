//#include "StdAfx.h"
//#include <windows.h>
//#include <Winbase.h>
//#include <tchar.h>
//
//#include "DbgHelp.h"
//#pragma comment(lib,"kernel32.lib")
//#pragma comment(lib,"DbgHelp.lib")
//
//#include "CrashDump.h"
//
//namespace CrashDump
//{
//	HANDLE							hLogFile=0;
//	LPTOP_LEVEL_EXCEPTION_FILTER	OldHandler=0;
//	bool							bVEH=false;
//	TCHAR							FullDumpFileName[MAX_PATH]={0};
//	TCHAR							FullDumpProcessName[MAX_PATH]={0};
//	TCHAR							DumpProcessName[]=_T("DumpUploader.exe");
//
//	#pragma optimize("y", off)
//	void	Log(...)
//	{
//		TCHAR szBuf[512]={0};
//		TCHAR *szUserFormat;
//		va_list Parameter=0;
//		DWORD dwSize=0,dwRtn=0;
//		SYSTEMTIME	stTime;
//		
//		//获取当前时间
//		GetLocalTime(&stTime);
//
//		//构造格式化字符串
//		dwSize=wsprintf(szBuf,_T("%d-%02d-%02d %02d:%02d:%02d "),stTime.wYear,stTime.wMonth,stTime.wDay,stTime.wHour,stTime.wMinute,stTime.wSecond);
//		__asm
//		{
//			mov	eax,dword ptr [ebp+8]
//			mov	szUserFormat,eax
//			lea	eax,dword ptr [ebp+0xc]
//			mov	Parameter,eax
//		}
//
//		//lstrcat(szBuf,szUserFormat);
//		
//		//格式化字符串
//		dwSize+=wvsprintf(&szBuf[dwSize],szUserFormat,Parameter);
//		
//		dwSize=dwSize*(sizeof TCHAR);
//		SetFilePointer(hLogFile,0,0,FILE_END);
//		WriteFile(hLogFile,szBuf,dwSize,&dwRtn,0);	
//	}
//	#pragma optimize("",  on)
//
//	HANDLE	InitLog(TCHAR *lpFileName)
//	{
//		HANDLE hFile;
//
//		hFile=CreateFile(lpFileName,GENERIC_WRITE,FILE_SHARE_READ | FILE_SHARE_WRITE,\
//			0,CREATE_ALWAYS,FILE_ATTRIBUTE_NORMAL,0);
//		if(hFile==INVALID_HANDLE_VALUE || hFile==0)
//		{
//			return 0;
//		}
//		return hFile;
//	}
//
//	long CrashDump_Handler(PEXCEPTION_POINTERS ExceptionInfo)
//	{
//		MINIDUMP_EXCEPTION_INFORMATION	stMEI={0};
//		HANDLE	hDump=0;
//		STARTUPINFO stSI={0};
//		PROCESS_INFORMATION	stPI={0};
//		
//		stMEI.ThreadId=GetCurrentThreadId();
//		stMEI.ExceptionPointers=ExceptionInfo;
//		stMEI.ClientPointers=true;
//
//		stSI.cb=sizeof(stSI);
//
//		//生成dump文件
//		hDump=CreateFile(FullDumpFileName,GENERIC_READ | GENERIC_WRITE,FILE_SHARE_READ | FILE_SHARE_WRITE,0,CREATE_ALWAYS,FILE_ATTRIBUTE_NORMAL,0);
//		if(hDump!=INVALID_HANDLE_VALUE)
//		{
//			Log(_T("Create DumpFile %s okey\r\n"),FullDumpFileName);
//
//			if(MiniDumpWriteDump(GetCurrentProcess(),GetCurrentProcessId(),hDump,MiniDumpNormal,&stMEI,0,0))
//			{
//				Log(_T("Write MiniDumpFile okey\r\n"));
//
//				//创建dump处理进程
//				if(CreateProcess(0,FullDumpProcessName,0,0,0,0,0,0,&stSI,&stPI))
//				{
//					Log(_T("Create dump upload process okey\r\n"));
//					CloseHandle(stPI.hProcess);
//					CloseHandle(stPI.hThread);
//				}
//				else
//					Log(_T("Create dump upload process failed %d\r\n"),GetLastError());
//			}
//			else
//				Log(_T("write mini dump file failed,error is %d\r\n"),GetLastError());
//			CloseHandle(hDump);
//		}
//		else
//			Log(_T("CreateFile %s failed,error is %d\r\n"),FullDumpFileName,GetLastError());
//		
//		Log(_T("ExitProcess with error code  %x\r\n"),ExceptionInfo->ExceptionRecord->ExceptionCode);
//
//		ExitProcess(ExceptionInfo->ExceptionRecord->ExceptionCode);
//
//		LOG_FLUSH();
//
//		return 0;
//	}
//
//	
//	ULONG  (WINAPI* lpAddVectoredExceptionHandler)(ULONG,PVECTORED_EXCEPTION_HANDLER);
//	ULONG  (WINAPI* lpRemoveVectoredExceptionHandler)(PVOID);
//
//	bool	CrashDump_Init(TCHAR* DumpFileName)
//	{
//		
//		OSVERSIONINFO	stOsInfo={0};
//		DWORD			Pos=0;
//		SYSTEMTIME		stTime={0};
//		
//		//获取当前时间
//		GetLocalTime(&stTime);
//
//		//初始化log
//		hLogFile=InitLog(_T("CrashDump.dat"));
//
//		if(hLogFile==0)
//			return false;
//		
//		//构造dump文件名
//		Pos=GetCurrentDirectory(sizeof(FullDumpFileName),FullDumpFileName);
//		if(Pos==0)
//		{
//			Log("CrashDump_Init:GetCurrentDirectory failed %d,return false\r\n",GetLastError());
//			return false;
//		}
//		//wsprintf(&FullDumpFileName[Pos],_T("\\%02d%02d%02d_%d_%s"),stTime.wYear,stTime.wMonth,stTime.wDay,GetCurrentProcessId(),DumpFileName);
//		wsprintf(&FullDumpFileName[Pos],_T("\\%s"),DumpFileName);
//	
//		
//		//构造dump处理进程名
//		Pos=GetCurrentDirectory(sizeof(FullDumpProcessName),FullDumpProcessName);
//		if(!Pos)
//		{
//			Log("CrashDump_Init:GetCurrentDirectory failed %d,return false\r\n",GetLastError());
//			return false;
//		}
//		//wsprintf(&FullDumpProcessName[Pos],_T("\\%s %02d%02d%02d_%d_%s"),DumpProcessName,stTime.wYear,stTime.wMonth,stTime.wDay,GetCurrentProcessId(),DumpFileName);
//		wsprintf(&FullDumpProcessName[Pos],_T("\\%s %s"),DumpProcessName,DumpFileName);
//		
//		/*
//		//先判断操作系统版本是否符号要求,并根据版本初化一些变量
//		stOsInfo.dwOSVersionInfoSize=sizeof stOsInfo;
//		GetVersionEx(&stOsInfo);
//		if( (stOsInfo.dwMajorVersion >= 5 && stOsInfo.dwMinorVersion>=1) || stOsInfo.dwMajorVersion >= 6)
//		{
//			bVEH=true;
//		}
//		
//		Log(_T("CrashDump,SystemVersion %d %d,try to use veh : %d\r\n"),stOsInfo.dwMajorVersion,stOsInfo.dwMinorVersion,bVEH);
//		
//		bVEH=false;
//
//		if(bVEH)
//		{
//			//使用向量化异常处理
//			lpAddVectoredExceptionHandler=(ULONG (WINAPI*)(ULONG,PVECTORED_EXCEPTION_HANDLER))GetProcAddress(GetModuleHandle(_T("kernel32.dll")),"AddVectoredExceptionHandler");
//			lpRemoveVectoredExceptionHandler=(ULONG (WINAPI*)(PVOID))GetProcAddress(GetModuleHandle(_T("kernel32.dll")),"RemoveVectoredExceptionHandler");
//
//			if(lpAddVectoredExceptionHandler==NULL)
//			{
//				Log(_T("Get AddVectoredExceptionHandler address failed\r\n"));
//				return false;
//			}
//			if(lpRemoveVectoredExceptionHandler==NULL)
//			{
//				Log(_T("Get RemoveVectoredExceptionHandler address failed\r\n"));
//				return false;
//			}
//			if(lpAddVectoredExceptionHandler(1,(PVECTORED_EXCEPTION_HANDLER)CrashDump_Handler))
//				Log(_T("add vectored exception handler okey\r\n"));
//			else
//			{
//				Log(_T("add vectored exception handler failed,return false\r\n"));
//				return false;
//			}
//		}
//		else
//		{
//			//使用筛选器异常处理
//			OldHandler=SetUnhandledExceptionFilter((LPTOP_LEVEL_EXCEPTION_FILTER)CrashDump_Handler);
//			Log(_T("set unhandled exception okey,old handler is %x\r\n"),OldHandler);
//		}
//		*/
//		OldHandler=SetUnhandledExceptionFilter((LPTOP_LEVEL_EXCEPTION_FILTER)CrashDump_Handler);
//		Log(_T("set unhandled exception okey,old handler is %x\r\n"),OldHandler);
//		return true;
//	}
//	
//	void	CrashDump_Clean(void)
//	{
//		/*
//		if(bVEH)
//			lpRemoveVectoredExceptionHandler(CrashDump_Handler);
//		else
//			SetUnhandledExceptionFilter(OldHandler);
//		*/
//		SetUnhandledExceptionFilter(OldHandler);
//	}
//
//}
#include "StdAfx.h"
#include <intrin.h> // for _ReturnAddress(), _AddressOfReturnAddress()
#include <string>
#include "DbgHelp.h"
// #pragma comment(lib,"DbgHelp.lib") 
// 因为Windows2K版的dbghelp.dll不支持我们需要的函数，所以我们不能静态连接该库

#include "CrashDump.h"

extern "C"
{
#ifdef _M_IX86
#define _CRT_DEBUGGER_HOOK _crt_debugger_hook
#elif defined (_M_IA64)
#define _CRT_DEBUGGER_HOOK __crt_debugger_hook_ex
	_CRTIMP void __cdecl __crt_debugger_hook(int);
#else  /* defined (_M_IA64) */
#define _CRT_DEBUGGER_HOOK __crt_debugger_hook
#endif  /* defined (_M_IA64) */

	_CRTIMP void __cdecl _CRT_DEBUGGER_HOOK(int);

	// INVALID_PARAMETER code for _CRT_DEBUGGER_HOOK
#ifndef _CRT_DEBUGGER_INVALIDPARAMETER
#define _CRT_DEBUGGER_INVALIDPARAMETER  2
#endif

	// Exception code used in _invalid_parameter
#ifndef STATUS_INVALID_PARAMETER
#define STATUS_INVALID_PARAMETER         ((DWORD)0xC000000DL)
#endif

	void __cdecl gfx_invalid_parameter_handler(
		const wchar_t *pszExpression,
		const wchar_t *pszFunction,
		const wchar_t *pszFile,
		unsigned int nLine,
		uintptr_t pReserved);

} // extern "C"

#define _OutLog( ll, ...)  LOG::OutLog( __FILEW__, __LINE__, ll, L"CrushDump", __VA_ARGS__)
#define _OutLogInfo( ...)  LOG::OutLog( __FILEW__, __LINE__, LOG::LL_INFO, L"CrushDump", __VA_ARGS__)

// Store the default invalid_arg handler for the process
static _invalid_parameter_handler g_old = NULL;

typedef BOOL( WINAPI *MiniDumpWriteDumpFunction)(
	IN HANDLE hProcess,
	IN DWORD ProcessId,
	IN HANDLE hFile,
	IN MINIDUMP_TYPE DumpType,
	IN CONST PMINIDUMP_EXCEPTION_INFORMATION ExceptionParam, OPTIONAL
	IN CONST PMINIDUMP_USER_STREAM_INFORMATION UserStreamParam, OPTIONAL
	IN CONST PMINIDUMP_CALLBACK_INFORMATION CallbackParam OPTIONAL
	);

namespace CrashDump
{
	class CCrashDump
	{
		static CCrashDump *				s_pDump;
		eDumpLevel						m_DumpLevel;
		std::wstring					m_DumpFilename;
		std::wstring					m_ReportExeName;
		LPTOP_LEVEL_EXCEPTION_FILTER	m_OldHandler;
		HMODULE							m_hDbgHelpDll; 
		MiniDumpWriteDumpFunction			m_pfnMiniDumpWriteDump;

	private:
		CCrashDump()
			: m_OldHandler( NULL)
			, m_hDbgHelpDll( NULL )
			, m_pfnMiniDumpWriteDump( NULL )
			, m_DumpLevel( DL_4)
		{
		}
	public:
		~CCrashDump()
		{
			Clean();
		}
		static CCrashDump *GetInstance() 
		{
			if (!s_pDump)
				s_pDump = new CCrashDump();
			return s_pDump;
		}
	public:
		bool Init( const std::wstring& szDumFilename, const std::wstring& szReportExeName, eDumpLevel dlevel)
		{
			m_DumpFilename = szDumFilename;
			m_ReportExeName = szReportExeName;
			m_DumpLevel = dlevel;

			m_hDbgHelpDll = LoadLibraryW(L"dbghelp.dll");
			if( m_hDbgHelpDll == NULL )
			{
				//GZGLOG( LL_NOTE, L"load library(dbghelp.dll) failed(" << GetLastError() << L")");
				return false;
			}

			m_pfnMiniDumpWriteDump = (MiniDumpWriteDumpFunction)GetProcAddress( m_hDbgHelpDll, "MiniDumpWriteDump");
			if( m_pfnMiniDumpWriteDump == NULL )
			{
				//GZGLOG( LL_NOTE, L"GetProcAddress( MiniDumpWriteDump ) from (dbghelp.dll) failed(" << GetLastError() << L")");
				return false;
			}

			m_OldHandler = SetUnhandledExceptionFilter( CrashDump_Handler);
			//GZGLOG( LL_NOTE, L"set unhandled exception okey,old handler is " << std::hex << m_OldHandler );

			g_old = _set_invalid_parameter_handler( &gfx_invalid_parameter_handler );
			//GZGLOG( LL_NOTE, L"set_invalid_parameter_handler okey, old handler is " << std::hex << g_old );

			return true;
		}

		void Clean()
		{
			s_pDump = NULL;
			if( m_OldHandler)
			{
				SetUnhandledExceptionFilter( m_OldHandler);
				m_OldHandler = NULL;
			}

			if( g_old )
			{
				_set_invalid_parameter_handler( g_old );
				g_old = NULL;
			}

			if( m_hDbgHelpDll != NULL )
			{
				FreeLibrary( m_hDbgHelpDll );
				m_hDbgHelpDll = NULL;
			}

			m_pfnMiniDumpWriteDump = NULL;
		}

	private:
		BOOL TryDump( HANDLE hDumpFile, eDumpLevel dlevel, MINIDUMP_TYPE type, MINIDUMP_EXCEPTION_INFORMATION& stMEI)
		{
			// 清空不完整的崩溃日志
			::SetFilePointer( hDumpFile, 0, 0, SEEK_SET );
			::SetEndOfFile( hDumpFile );

			BOOL bRet = m_pfnMiniDumpWriteDump(
				GetCurrentProcess(), 
				GetCurrentProcessId(),
				hDumpFile,
				type, 
				&stMEI, 0, 0);

			if( bRet )
			{
				//GZGLOG( LL_NOTE,  L"Write MiniDumpFileWithFullMemory(level" << dlevel << L") okey");
				
				return TRUE;
			}

			return FALSE;
		}

		long OnCrashDumpHandler( PEXCEPTION_POINTERS ExceptionInfo)
		{
			//生成dump文件
			HANDLE hDumpFile = CreateFile( m_DumpFilename.c_str(), 
				GENERIC_READ | GENERIC_WRITE, 0, 
				0, CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, 0);
			if( hDumpFile == INVALID_HANDLE_VALUE)
			{
				//GZGLOG( LL_NOTE, L"open/create crash dump file failed: " << m_DumpFilename << L"(" << GetLastError() << L")");
				// 继续查找异常处理器
				return EXCEPTION_CONTINUE_SEARCH;
			}

			//GZGLOG( LL_NOTE, L"Create DumpFile " << g_Dump.m_DumpFilename << L"okey");

			MINIDUMP_EXCEPTION_INFORMATION stMEI = {0};
			stMEI.ThreadId = GetCurrentThreadId();
			stMEI.ExceptionPointers = ExceptionInfo;
			stMEI.ClientPointers = true;

			//////////////////////////////////////////////////////////////////////////
			//
			// 下面逐一尝试MiniDump文件的生成方式
			// 如果最大规模内存模式写失败,则减少内存规模.
			//
			//////////////////////////////////////////////////////////////////////////

			BOOL bRet = TRUE;
			MINIDUMP_TYPE minidump_type = MiniDumpNormal;
			switch( m_DumpLevel)
			{
			case DL_1:
				minidump_type = MiniDumpWithFullMemory;
				if ( TryDump( hDumpFile, DL_1, minidump_type, stMEI))
					break;
			case DL_2:
				minidump_type = static_cast<MINIDUMP_TYPE>(MiniDumpWithDataSegs | MiniDumpWithIndirectlyReferencedMemory | MiniDumpWithPrivateReadWriteMemory | MiniDumpScanMemory);
				if ( TryDump( hDumpFile, DL_2, minidump_type, stMEI))
					break;
			case DL_3:
				minidump_type = static_cast<MINIDUMP_TYPE>(MiniDumpWithDataSegs | MiniDumpWithIndirectlyReferencedMemory | MiniDumpScanMemory);
				if ( TryDump( hDumpFile, DL_3, minidump_type, stMEI))
					break;
			case DL_4:
			default:
				minidump_type = MiniDumpNormal;
				if ( TryDump( hDumpFile, DL_4, minidump_type, stMEI))
					break;
				bRet = FALSE;
				break;
			}

			if ( !bRet)
			{
				//GZGLOG( LL_NOTE, L"Create crash dump file failed(" << GetLastError() << L")");
			}

			CloseHandle( hDumpFile );

			if( bRet )
			{
				//GZGLOG( LL_NOTE, L"Write MiniDumpFile okey");

				STARTUPINFO stSI = {0};
				PROCESS_INFORMATION	stPI = {0};
				stSI.cb = sizeof(stSI);

				// 这里暂时只发送crushdmp.dmp文件
				std::wstring strDumpFilenameArg = L" " +  m_DumpFilename;
				if( CreateProcess( m_ReportExeName.c_str(),
					(wchar_t*)strDumpFilenameArg.c_str(), NULL, NULL, FALSE, 0, NULL, NULL, &stSI, &stPI))
				{
					//GZGLOG( LL_NOTE, L"Create dump upload process \"" << m_ReportExeName << L"\" okey");
					CloseHandle(stPI.hProcess);
					CloseHandle(stPI.hThread);
				}
				else
				{
					//GZGLOG( LL_NOTE, L"Create dump upload process failed(" << GetLastError() << L")");
				}
			}

			// log memory status
			{
				MEMORYSTATUSEX memstatus = { sizeof(memstatus) };
				::GlobalMemoryStatusEx(&memstatus);
				wchar_t buf[256];
				wsprintf(buf,L"Crash Memory(use/ava/tot): %d/%d/%dM %d%%"
					, (int)(memstatus.ullTotalVirtual/1024)/1024 - (int)(memstatus.ullAvailVirtual/1024)/1024
					, (int)(memstatus.ullAvailPhys/1024)/1024, (int)(memstatus.ullTotalPhys/1024)/1024, (int)memstatus.dwMemoryLoad);
				//GZGLOG( LL_NOTE, buf);
			}

			//LOG_FLUSH();

			// 继续查找异常处理器
			return EXCEPTION_CONTINUE_SEARCH;
		}

		static long _stdcall CrashDump_Handler( PEXCEPTION_POINTERS ExceptionInfo)
		{
			long lRet = CCrashDump::GetInstance()->OnCrashDumpHandler( ExceptionInfo);
			return lRet;
		}

		friend void __cdecl ::gfx_invalid_parameter_handler(
			const wchar_t *pszExpression,
			const wchar_t *pszFunction,
			const wchar_t *pszFile,
			unsigned int nLine,
			uintptr_t pReserved);

	};

	CCrashDump *CCrashDump::s_pDump = NULL;

	bool CrashDump_Init( const std::wstring& szDumFilename, const std::wstring& szReportExeName, eDumpLevel dlevel)
	{
		return CCrashDump::GetInstance()->Init( szDumFilename, szReportExeName, dlevel);
	}

	void CrashDump_Clean(void)
	{
		CCrashDump *pDump = CCrashDump::GetInstance();
		pDump->Clean();
		delete pDump;
	}
} // namespace CrashDump
#include <memory>
void __cdecl gfx_invalid_parameter_handler(
	const wchar_t *pszExpression,
	const wchar_t *pszFunction,
	const wchar_t *pszFile,
	unsigned int nLine,
	uintptr_t pReserved)
{
	CrashDump::CCrashDump *pDump = CrashDump::CCrashDump::GetInstance();
	//std::auto_ptr<CrashDump::CCrashDump*> pAutoDump(pDump);
	/* Fake an exception to call report fault. */
	EXCEPTION_RECORD ExceptionRecord;
	CONTEXT ContextRecord;
	EXCEPTION_POINTERS ExceptionPointers;

	(pszExpression);
	(pszFunction);
	(pszFile);
	(nLine);
	(pReserved);

#ifdef _X86_

	__asm {
		mov dword ptr [ContextRecord.Eax], eax
			mov dword ptr [ContextRecord.Ecx], ecx
			mov dword ptr [ContextRecord.Edx], edx
			mov dword ptr [ContextRecord.Ebx], ebx
			mov dword ptr [ContextRecord.Esi], esi
			mov dword ptr [ContextRecord.Edi], edi
			mov word ptr [ContextRecord.SegSs], ss
			mov word ptr [ContextRecord.SegCs], cs
			mov word ptr [ContextRecord.SegDs], ds
			mov word ptr [ContextRecord.SegEs], es
			mov word ptr [ContextRecord.SegFs], fs
			mov word ptr [ContextRecord.SegGs], gs
			pushfd
			pop [ContextRecord.EFlags]
	}

	ContextRecord.ContextFlags = CONTEXT_CONTROL;
#pragma warning(push)
#pragma warning(disable:4311)
	ContextRecord.Eip = (ULONG)_ReturnAddress();
	ContextRecord.Esp = (ULONG)_AddressOfReturnAddress();
#pragma warning(pop)
	ContextRecord.Ebp = *((ULONG *)_AddressOfReturnAddress()-1);

#elif defined (_IA64_) || defined (_AMD64_)

	/* Need to fill up the Context in IA64 and AMD64. */
	RtlCaptureContext(&ContextRecord);

#else  /* defined (_IA64_) || defined (_AMD64_) */

	ZeroMemory(&ContextRecord, sizeof(ContextRecord));

#endif  /* defined (_IA64_) || defined (_AMD64_) */

	ZeroMemory(&ExceptionRecord, sizeof(ExceptionRecord));

	ExceptionRecord.ExceptionCode = STATUS_INVALID_PARAMETER;
	ExceptionRecord.ExceptionAddress = _ReturnAddress();

	ExceptionPointers.ExceptionRecord = &ExceptionRecord;
	ExceptionPointers.ContextRecord = &ContextRecord;

	// Dump this exception info to file, and send it to server
	DWORD ret = CrashDump::CCrashDump::CrashDump_Handler(&ExceptionPointers);
	if ( ret == EXCEPTION_EXECUTE_HANDLER || ret == EXCEPTION_CONTINUE_EXECUTION  )
	{
		// 异常已经被处理
		return;
	}

	// 清除原来的异常处理(它应该是CrashDump::CCrashDump::CrashDump_Handler)
	LPTOP_LEVEL_EXCEPTION_FILTER pOldHandler = SetUnhandledExceptionFilter(NULL);

	// 使用缺省的异常处理器，允许调试器挂接并调试程序
	ret = UnhandledExceptionFilter(&ExceptionPointers);

	// if no handler found and no debugger previously attached
	// the execution must stop into the debugger hook.
	if( ret == EXCEPTION_CONTINUE_SEARCH )
	{
		BOOL wasDebuggerPresent = IsDebuggerPresent();
		if( !wasDebuggerPresent )
		{
			_CRT_DEBUGGER_HOOK(_CRT_DEBUGGER_INVALIDPARAMETER);
		}
	}

	if( !IsDebuggerPresent() )
	{
		// 如果此时程序没有被调试器挂接，终止进程
		TerminateProcess(GetCurrentProcess(), STATUS_INVALID_PARAMETER);
		return;
	}
	else
	{
		// 调试程序
		DebugBreak();

		// 本异常处理后恢复缺省的异常过滤器
		SetUnhandledExceptionFilter(pOldHandler);
	}

	// 异常已经被处理
}