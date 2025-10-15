#define _WIN32_WINNT 0x0510
#include <windows.h>
#include <Winbase.h>
#include <string>
#include <stdlib.h> // for _invalid_parameter_handler function pointer type
#include <intrin.h> // for _ReturnAddress(), _AddressOfReturnAddress()
#include <DbgHelp.h>
// #pragma comment(lib,"DbgHelp.lib") 
// 因为Windows2K版的dbghelp.dll不支持我们需要的函数，所以我们不能静态连接该库

#include "CrashDump.h"

extern void Log(...);
extern bool InitLog(const TCHAR *lpFileName);
extern void CloseLog();

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

#define _OutLog( ...)  Log(__VA_ARGS__)

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

namespace BUGREPORT
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
		bool Init( const wchar_t* lpszDumpFilename, const wchar_t* lpszReportExeName )
		{
			m_DumpFilename.assign(lpszDumpFilename);
			m_ReportExeName.assign(lpszReportExeName);

			m_hDbgHelpDll = LoadLibraryW(L"dbghelp.dll");
			if( m_hDbgHelpDll == NULL )
			{
				_OutLog( L"load library(dbghelp.dll) failed(%d)\n", GetLastError());
				return false;
			}

			m_pfnMiniDumpWriteDump = (PFN_MiniDumpWriteDump)GetProcAddress( m_hDbgHelpDll, "MiniDumpWriteDump");
			if( m_pfnMiniDumpWriteDump == NULL )
			{
				_OutLog( L"GetProcAddress( MiniDumpWriteDump ) from (dbghelp.dll) failed(%d)\n", GetLastError());
				return false;
			}

			m_OldHandler = SetUnhandledExceptionFilter( ExceptionFilter);
			_OutLog( L"set unhandled exception okey,old handler is %x\n", m_OldHandler );

			g_old = _set_invalid_parameter_handler( &gfx_invalid_parameter_handler );
			_OutLog( L"set_invalid_parameter_handler okey, old handler is %x\n", g_old );

			// LOG::LogFlush();

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

	private:
		long _OnCrashDump( PEXCEPTION_POINTERS ExceptionInfo)
		{
			//生成dump文件
			HANDLE hDumpFile = CreateFile( m_DumpFilename.c_str(), 
				GENERIC_READ | GENERIC_WRITE, 0, 
				0, CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, 0);
			if( hDumpFile == INVALID_HANDLE_VALUE)
			{
				// TODO: IF Finish?
				_OutLog( L"open/create dump file fail %s(%#08x)\n",
					m_DumpFilename.c_str(), GetLastError());

				// 继续查找异常处理器
				return EXCEPTION_CONTINUE_SEARCH;
			}
				
			_OutLog( L"Create DumpFile %s okey\n", g_Dump.m_DumpFilename.c_str());

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
			BOOL bRet = m_pfnMiniDumpWriteDump(
				GetCurrentProcess(), 
				GetCurrentProcessId(),
				hDumpFile,
				(MINIDUMP_TYPE) (MiniDumpWithFullMemory), // 生成的dmp文件太大了，大于200M
				&stMEI, 0, 0);
			if( bRet )
			{
				_OutLog( L"Write MiniDumpFileWithFullMemory okey\n");
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
					(MINIDUMP_TYPE) (
					MiniDumpWithDataSegs | // 该选项增加大约6M左右的数据，尽管不小，但是为了方便调试，也是值得的
					MiniDumpWithIndirectlyReferencedMemory |
					MiniDumpWithPrivateReadWriteMemory | // 加上此项会导致dmp文件大于100M
					MiniDumpScanMemory
					), 
					&stMEI, 0, 0);
				if( bRet )
				{
					_OutLog( L"Write MiniDumpFileWithDataSegs and PrivateReadWriteMemory okey\n");
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
						(MINIDUMP_TYPE) (
						MiniDumpWithDataSegs |
						MiniDumpWithIndirectlyReferencedMemory |
						MiniDumpScanMemory ), 
						&stMEI, 0, 0);
					if( bRet )
					{
						_OutLog( L"Write MiniDumpFileWithDataSegs okey\n");
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
							_OutLog( L"Write MiniDumpFileNormal okey\n");
						}
						else
						{
							DWORD dwLastError = GetLastError();

							// TODO LL_IFFISH 
							_OutLog( L"create dump file fail(%#08x)!\n", dwLastError);
						}
					}
				}
			}

			CloseHandle( hDumpFile );

			if( bRet )
			{
				_OutLog( L"Write MiniDumpFile okey\n");

				STARTUPINFO stSI = {0};
				PROCESS_INFORMATION	stPI = {0};
				stSI.cb = sizeof(stSI);

				// 这里暂时只发送crushdmp.dmp文件
				std::wstring strDumpFilenameArg = L" " +  m_DumpFilename;
				if( CreateProcess( m_ReportExeName.c_str(),
					(wchar_t*)strDumpFilenameArg.c_str(), NULL, NULL, FALSE, 0, NULL, NULL, &stSI, &stPI))
				{
					_OutLog( L"Create dump upload process \"%s\" okey\n", m_ReportExeName.c_str());
					CloseHandle(stPI.hProcess);
					CloseHandle(stPI.hThread);
				}
				else
				{
					// TODO 
					_OutLog(
						L"创建崩溃日志搜集进程\"%s\"失败(%#08x).\n您可以尝试手动启动 %s 程序向客服提供该崩溃日志.\n",
						m_ReportExeName.c_str(), GetLastError(), m_ReportExeName.c_str() );
				}
			}
			
			// 继续查找异常处理器
			return EXCEPTION_CONTINUE_SEARCH;
		}

		static long _stdcall ExceptionFilter( PEXCEPTION_POINTERS ExceptionInfo)
		{
			long lRet = g_Dump._OnCrashDump( ExceptionInfo);

			// LOG::LogFlush();

			return lRet;
		}

		friend void __cdecl ::gfx_invalid_parameter_handler(
			const wchar_t *pszExpression,
			const wchar_t *pszFunction,
			const wchar_t *pszFile,
			unsigned int nLine,
			uintptr_t pReserved);

	} g_Dump;

} // namespace BUGREPORT

bool CrashDump_Init( const wchar_t* lpszDumpFilename, const wchar_t* lpszReportExeName )
{
	std::wstring logfilename(lpszDumpFilename);
	std::wstring::size_type dotpos = logfilename.rfind(L'.');
	if( dotpos != std::wstring::npos )
	{
		std::wstring::size_type slashpos = logfilename.rfind(L'/');
		if( slashpos == std::wstring::npos || slashpos < dotpos )
		{
			std::wstring::size_type backslashpos = logfilename.rfind(L'\\');
			if( backslashpos == std::wstring::npos || backslashpos < dotpos  )
			{
				// trim the extension with dot. 
				// like 
				// abc.txt --> abc
				// ./a/bc.txt --> ./a/bc
				// ./a\bc.txt --> ./a\bc
				// ./a.b/c --> ./a.b/c
				logfilename.resize( dotpos );
			}
		}
	}
	
	logfilename.append(L".log");

	// 这里通过InitLog，可以保证logfilename以及lpszDumpFilename文件所在的目录被创建成功。
	InitLog( logfilename.c_str() );

	return BUGREPORT::g_Dump.Init( lpszDumpFilename, lpszReportExeName );
}

void CrashDump_Clean(void)
{
	CloseLog();
	BUGREPORT::g_Dump.Clean();
}

static void __cdecl gfx_invalid_parameter_handler(
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
	DWORD ret = BUGREPORT::CCrashDump::ExceptionFilter(&ExceptionPointers);
	if ( ret == EXCEPTION_EXECUTE_HANDLER || ret == EXCEPTION_CONTINUE_EXECUTION  )
	{
		// 异常已经被处理
		return;
	}

	// 清除原来的异常处理(它应该是BUGREPORT::CCrashDump::ExceptionFilter)
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