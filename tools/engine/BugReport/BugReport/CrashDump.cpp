#define _WIN32_WINNT 0x0510
#include <windows.h>
#include <Winbase.h>
#include <string>
#include <stdlib.h> // for _invalid_parameter_handler function pointer type
#include <intrin.h> // for _ReturnAddress(), _AddressOfReturnAddress()
#include <DbgHelp.h>
// #pragma comment(lib,"DbgHelp.lib") 
// ��ΪWindows2K���dbghelp.dll��֧��������Ҫ�ĺ������������ǲ��ܾ�̬���Ӹÿ�

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
			//����dump�ļ�
			HANDLE hDumpFile = CreateFile( m_DumpFilename.c_str(), 
				GENERIC_READ | GENERIC_WRITE, 0, 
				0, CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, 0);
			if( hDumpFile == INVALID_HANDLE_VALUE)
			{
				// TODO: IF Finish?
				_OutLog( L"open/create dump file fail %s(%#08x)\n",
					m_DumpFilename.c_str(), GetLastError());

				// ���������쳣������
				return EXCEPTION_CONTINUE_SEARCH;
			}
				
			_OutLog( L"Create DumpFile %s okey\n", g_Dump.m_DumpFilename.c_str());

			MINIDUMP_EXCEPTION_INFORMATION stMEI = {0};
			stMEI.ThreadId = GetCurrentThreadId();
			stMEI.ExceptionPointers = ExceptionInfo;
			stMEI.ClientPointers = true;

			//////////////////////////////////////////////////////////////////////////
			//
			// ������һ����MiniDump�ļ������ɷ�ʽ
			// �������ģ�ڴ�ģʽдʧ��,������ڴ��ģ.
			//
			//////////////////////////////////////////////////////////////////////////
			BOOL bRet = m_pfnMiniDumpWriteDump(
				GetCurrentProcess(), 
				GetCurrentProcessId(),
				hDumpFile,
				(MINIDUMP_TYPE) (MiniDumpWithFullMemory), // ���ɵ�dmp�ļ�̫���ˣ�����200M
				&stMEI, 0, 0);
			if( bRet )
			{
				_OutLog( L"Write MiniDumpFileWithFullMemory okey\n");
			}
			else
			{
				// ��ղ������ı�����־
				::SetFilePointer( hDumpFile, 0, 0, SEEK_SET );
				::SetEndOfFile( hDumpFile );

				bRet = m_pfnMiniDumpWriteDump(
					GetCurrentProcess(), 
					GetCurrentProcessId(),
					hDumpFile,
					(MINIDUMP_TYPE) (
					MiniDumpWithDataSegs | // ��ѡ�����Ӵ�Լ6M���ҵ����ݣ����ܲ�С������Ϊ�˷�����ԣ�Ҳ��ֵ�õ�
					MiniDumpWithIndirectlyReferencedMemory |
					MiniDumpWithPrivateReadWriteMemory | // ���ϴ���ᵼ��dmp�ļ�����100M
					MiniDumpScanMemory
					), 
					&stMEI, 0, 0);
				if( bRet )
				{
					_OutLog( L"Write MiniDumpFileWithDataSegs and PrivateReadWriteMemory okey\n");
				}
				else
				{
					// ��ղ������ı�����־
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
						// ��ղ������ı�����־
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

				// ������ʱֻ����crushdmp.dmp�ļ�
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
						L"����������־�Ѽ�����\"%s\"ʧ��(%#08x).\n�����Գ����ֶ����� %s ������ͷ��ṩ�ñ�����־.\n",
						m_ReportExeName.c_str(), GetLastError(), m_ReportExeName.c_str() );
				}
			}
			
			// ���������쳣������
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

	// ����ͨ��InitLog�����Ա�֤logfilename�Լ�lpszDumpFilename�ļ����ڵ�Ŀ¼�������ɹ���
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
		// �쳣�Ѿ�������
		return;
	}

	// ���ԭ�����쳣����(��Ӧ����BUGREPORT::CCrashDump::ExceptionFilter)
	LPTOP_LEVEL_EXCEPTION_FILTER pOldHandler = SetUnhandledExceptionFilter(NULL);

	// ʹ��ȱʡ���쳣������������������ҽӲ����Գ���
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
		// �����ʱ����û�б��������ҽӣ���ֹ����
		TerminateProcess(GetCurrentProcess(), STATUS_INVALID_PARAMETER);
		return;
	}
	else
	{
		// ���Գ���
		DebugBreak();
		
		// ���쳣�����ָ�ȱʡ���쳣������
		SetUnhandledExceptionFilter(pOldHandler);
	}

	// �쳣�Ѿ�������
}