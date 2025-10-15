#include "stdafx.h"
//#define _WIN32_WINNT 0x0510
//#include <windows.h>
//#include <Winbase.h>
//#include <string>
//#include <stdlib.h> // for _invalid_parameter_handler function pointer type
#include <intrin.h> // for _ReturnAddress(), _AddressOfReturnAddress()
#include <time.h>
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

	void __cdecl MHSD_gfx_invalid_parameter_handler(
		const wchar_t *pszExpression,
		const wchar_t *pszFunction,
		const wchar_t *pszFile,
		unsigned int nLine,
		uintptr_t pReserved);

	BOOL CALLBACK MyMiniDumpCallback(
		PVOID                            pParam, 
		const PMINIDUMP_CALLBACK_INPUT   pInput, 
		PMINIDUMP_CALLBACK_OUTPUT        pOutput 
		); 

} // extern "C"

#define _OutLog( ll, ...)  
#define _OutLogInfo( ...)  

namespace LOGLOG {
	enum
	{
		LL_INFO,
	};
	void LogFlush(){}
}
//#define _OutLog( ll, ...)  LOG::OutLog( __FILEW__, __LINE__, ll, L"CrushDump", __VA_ARGS__)
//#define _OutLogInfo( ...)  LOG::OutLog( __FILEW__, __LINE__, LOG::LL_INFO, L"CrushDump", __VA_ARGS__)

// Store the default invalid_arg handler for the process
static _invalid_parameter_handler g_old = NULL;

typedef BOOL( WINAPI *PFN_MiniDumpWriteDump)(
				  IN HANDLE hProcess,
				  IN DWORD ProcessId,
				  IN HANDLE hFile,
				  IN MINIDUMP_TYPE DumpType,
				  IN CONST PMINIDUMP_EXCEPTION_INFORMATION ExceptionParam, OPTIONAL
				  IN CONST PMINIDUMP_USER_STREAM_INFORMATION UserStreamParam, OPTIONAL
				  IN CONST PMINIDUMP_CALLBACK_INFORMATION CallbackParam OPTIONAL
				  );

namespace MHSD_CrashDump
{
	static class CCrashDump
	{
		std::wstring					m_DumpFilename;
		std::wstring					m_ReportExeName;
		LPTOP_LEVEL_EXCEPTION_FILTER	m_OldHandler;
		HMODULE							m_hDbgHelpDll; 
		PFN_MiniDumpWriteDump			m_pfnMiniDumpWriteDump;

	public:
		CCrashDump()
			: m_OldHandler( NULL)
			, m_hDbgHelpDll( NULL )
			, m_pfnMiniDumpWriteDump( NULL )
		{
		}
		~CCrashDump()
		{
			Clean();
		}
	public:
		bool Init( const std::wstring& szDumFilename, const std::wstring& szReportExeName)
		{
			m_DumpFilename = szDumFilename;
			m_ReportExeName = szReportExeName;

			m_hDbgHelpDll = LoadLibraryW(L"dbghelp.dll");
			if( m_hDbgHelpDll == NULL )
			{
				_OutLog( LOG::LL_INFO, L"load library(dbghelp.dll) failed(%d)", GetLastError());
				//LOG_OutLog_Info(L"load library(dbghelp.dll) failed");
				return false;
			}

			m_pfnMiniDumpWriteDump = (PFN_MiniDumpWriteDump)GetProcAddress( m_hDbgHelpDll, "MiniDumpWriteDump");
			if( m_pfnMiniDumpWriteDump == NULL )
			{
				_OutLog( LOG::LL_INFO, L"GetProcAddress( MiniDumpWriteDump ) from (dbghelp.dll) failed(%d)", GetLastError());
				return false;
			}

			//LOG_OutLog_Info(L"process kinghtclient id:%d write dump \"%s\" ", GetCurrentProcessId(), m_DumpFilename.c_str());

			m_OldHandler = SetUnhandledExceptionFilter( CrashDump_Handler);
			_OutLogInfo( L"set unhandled exception okey,old handler is %x", m_OldHandler );

			g_old = _set_invalid_parameter_handler( &MHSD_gfx_invalid_parameter_handler );
			_OutLogInfo( L"set_invalid_parameter_handler okey, old handler is %x", g_old );

			//LOG::LogFlush();

			return true;
		}

		void Clean()
		{
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

		void SendAssert()
		{
			STARTUPINFO stSI = {0};
			PROCESS_INFORMATION	stPI = {0};
			stSI.cb = sizeof(stSI);

			//60内没有收到assert，再弹出。所以收到assert，只更新时间，不弹框。
			static clock_t last = clock();
			clock_t now = clock();
			if (now - last < 60*CLOCKS_PER_SEC)
			{
				last = now;
				return;
			}
			last = now;

			std::wstring strArg = L" ";
			if( CreateProcess( m_ReportExeName.c_str(),
				(wchar_t*)strArg.c_str(), NULL, NULL, FALSE, 0, NULL, NULL, &stSI, &stPI))
			{
				CloseHandle(stPI.hProcess);
				CloseHandle(stPI.hThread);
			}
		}

	private:
		long OnCrashDumpHandler( PEXCEPTION_POINTERS ExceptionInfo)
		{
			//生成dump文件
			HANDLE hDumpFile = CreateFile( m_DumpFilename.c_str(), 
				GENERIC_READ | GENERIC_WRITE, 0, 
				0, CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, 0);
			if( hDumpFile == INVALID_HANDLE_VALUE)
			{
				_OutLog( LOG::LL_IFFISH, L"open/create dump file fail %s(%#08x)",
					m_DumpFilename.c_str(), GetLastError());
				//LOG::LogFlush();
				// 继续查找异常处理器
				return EXCEPTION_CONTINUE_SEARCH;
			}
				
			_OutLogInfo( L"Create DumpFile %s okey", g_Dump.m_DumpFilename.c_str());

			MINIDUMP_EXCEPTION_INFORMATION stMEI = {0};
			stMEI.ThreadId = GetCurrentThreadId();
			stMEI.ExceptionPointers = ExceptionInfo;
			stMEI.ClientPointers = true;

			MINIDUMP_CALLBACK_INFORMATION mci; 
			mci.CallbackRoutine     = (MINIDUMP_CALLBACK_ROUTINE)MyMiniDumpCallback; 
			mci.CallbackParam       = 0; 


			//////////////////////////////////////////////////////////////////////////
			//
			// 下面逐一尝试MiniDump文件的生成方式
			// 如果最大规模内存模式写失败,则减少内存规模.
			//
			//////////////////////////////////////////////////////////////////////////
			BOOL bRet = m_pfnMiniDumpWriteDump(
						GetCurrentProcess(), 
						GetCurrentProcessId(),
						hDumpFile,
						//(MINIDUMP_TYPE) (MiniDumpWithCodeSegs|MiniDumpWithDataSegs|MiniDumpWithIndirectlyReferencedMemory|MiniDumpScanMemory),
                        (MINIDUMP_TYPE)(MiniDumpWithIndirectlyReferencedMemory | MiniDumpWithFullMemoryInfo | MiniDumpWithCodeSegs),
						&stMEI, 0, 0);

			if( bRet )
			{
				//LOG_OutLog_Info(L"Write MiniDumpFileWithDataSegs okey");
				_OutLogInfo( L"Write MiniDumpFileWithDataSegs okey");
			}
			else
			{
				// 清空不完整的崩溃日志
				::SetFilePointer( hDumpFile, 0, 0, SEEK_SET );
				::SetEndOfFile( hDumpFile );

				bRet = m_pfnMiniDumpWriteDump(
						GetCurrentProcess(), 
						GetCurrentProcessId(),
						hDumpFile,
                        (MINIDUMP_TYPE) (MiniDumpNormal), 
						&stMEI, 0, 0);
				if ( bRet )
				{
					_OutLogInfo( L"Write MiniDumpFileNormal okey");
				}
				else
				{
					DWORD dwLastError = GetLastError();
					_OutLog( LOG::LL_IFFISH, L"create dump file fail(%#08x)!", dwLastError);
				}
			}
			CloseHandle( hDumpFile );

			if( bRet )
			{
				_OutLogInfo( L"Write MiniDumpFile okey");

				STARTUPINFO stSI = {0};
				PROCESS_INFORMATION	stPI = {0};
				stSI.cb = sizeof(stSI);

				// 这里暂时只发送crushdmp.dmp文件
				std::wstring strDumpFilenameArg = L" " +  m_DumpFilename;
				if( CreateProcess( m_ReportExeName.c_str(),
					(wchar_t*)strDumpFilenameArg.c_str(), NULL, NULL, FALSE, 0, NULL, NULL, &stSI, &stPI))
				{
					_OutLogInfo( L"Create dump upload process \"%s\" okey", m_ReportExeName.c_str());
					CloseHandle(stPI.hProcess);
					CloseHandle(stPI.hThread);
				}
				else
				{
					_OutLog( LOG::LL_IFFISH, 
						L"创建崩溃日志搜集进程\"%s\"失败(%#08x).\n您可以尝试手动启动 %s 程序向客服提供该崩溃日志.",
						m_ReportExeName.c_str(), GetLastError(), m_ReportExeName.c_str() );
				}
			}
			
			//LOG::LogFlush();

			// 继续查找异常处理器
			return EXCEPTION_CONTINUE_SEARCH;
		}

		static long _stdcall CrashDump_Handler( PEXCEPTION_POINTERS ExceptionInfo)
		{
			long lRet = g_Dump.OnCrashDumpHandler( ExceptionInfo);
			return lRet;
		}

		friend void __cdecl ::MHSD_gfx_invalid_parameter_handler(
			const wchar_t *pszExpression,
			const wchar_t *pszFunction,
			const wchar_t *pszFile,
			unsigned int nLine,
			uintptr_t pReserved);

	} g_Dump;

	bool CrashDump_Init( const std::wstring& szDumFilename, const std::wstring& szReportExeName)
	{
		return g_Dump.Init( szDumFilename, szReportExeName);
	}
	
	void CrashDump_SendAssert()
	{
		return g_Dump.SendAssert();
	}

	void CrashDump_Clean(void)
	{
		g_Dump.Clean();
	}
} // namespace CrashDump

bool IsCodeSectionNeed(const wchar_t* pModuleName)
{
	if (pModuleName == 0)
	{
		return false;
	}
	wchar_t szFileName[100] = L"";
	_wsplitpath(pModuleName, NULL, NULL, szFileName, NULL);

	if (_wcsicmp(szFileName, L"FireClient") == 0)
	{
		return true;
	}

	return false;
}

BOOL CALLBACK MyMiniDumpCallback(
								 PVOID                            pParam, 
								 const PMINIDUMP_CALLBACK_INPUT   pInput, 
								 PMINIDUMP_CALLBACK_OUTPUT        pOutput 
								 ) 
{
	BOOL bRet = FALSE; 

	// Check parameters 
	if( pInput == 0 ) 
		return FALSE; 

	if( pOutput == 0 ) 
		return FALSE; 

	// Process the callbacks 
	switch( pInput->CallbackType ) 
	{
	case IncludeModuleCallback: 
		{
			return TRUE;
		}
		break; 
	case IncludeThreadCallback: 
		{
			// Include the thread into the dump 
			bRet = TRUE; 
		}
		break; 
	case ModuleCallback: 
		{
			// Does the module have ModuleReferencedByMemory flag set ? 

			if( !(pOutput->ModuleWriteFlags & ModuleReferencedByMemory) ) 
			{
				// No, it does not - exclude it 
				wprintf( L"Excluding module: %s \n", pInput->Module.FullPath ); 
				pOutput->ModuleWriteFlags &= (~ModuleWriteModule); 
			}
			else
			{
				if ( !IsCodeSectionNeed(pInput->Module.FullPath))
				{
					wprintf( L"Excluding module: %s \n", pInput->Module.FullPath );
					pOutput->ModuleWriteFlags &= (~ModuleWriteCodeSegs); 
					pOutput->ModuleWriteFlags &= (~ModuleWriteDataSeg);
				}
			}
			bRet = TRUE; 
		}
		break;
	case ThreadCallback: 
		{
			// Include all thread information into the minidump 
			bRet = TRUE;  
		}
		break;
	case ThreadExCallback: 
		{
			// Include this information 
			bRet = TRUE;  
		}
		break; 
	case MemoryCallback: 
		{
			// We do not include any information here -> return FALSE 
			bRet = FALSE; 
		}
		break;
	case CancelCallback: 
		break; 
	}
	return bRet; 
}

void __cdecl MHSD_gfx_invalid_parameter_handler(
	const wchar_t *pszExpression,
	const wchar_t *pszFunction,
	const wchar_t *pszFile,
	unsigned int nLine,
	uintptr_t pReserved)
{
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
	DWORD ret = MHSD_CrashDump::CCrashDump::CrashDump_Handler(&ExceptionPointers);
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
