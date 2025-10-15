#include "StdAfx.h"
#include <intrin.h>
#include <string>
#include "DbgHelp.h"

#include "CrashDump.h"

extern "C"
{
#ifdef _M_IX86
#define _CRT_DEBUGGER_HOOK _crt_debugger_hook
#elif defined (_M_IA64)
#define _CRT_DEBUGGER_HOOK __crt_debugger_hook_ex
	_CRTIMP void __cdecl __crt_debugger_hook(int);
#else  
#define _CRT_DEBUGGER_HOOK __crt_debugger_hook
#endif  

	_CRTIMP void __cdecl _CRT_DEBUGGER_HOOK(int);


#ifndef _CRT_DEBUGGER_INVALIDPARAMETER
#define _CRT_DEBUGGER_INVALIDPARAMETER  2
#endif


#ifndef STATUS_INVALID_PARAMETER
#define STATUS_INVALID_PARAMETER         ((DWORD)0xC000000DL)
#endif

	void __cdecl gfx_invalid_parameter_handler(
		const wchar_t *pszExpression,
		const wchar_t *pszFunction,
		const wchar_t *pszFile,
		unsigned int nLine,
		uintptr_t pReserved);

} 

#define _OutLog( ll, ...)  LOG::OutLog( __FILEW__, __LINE__, ll, L"CrashDump", __VA_ARGS__)
#define _OutLogInfo( ...)  LOG::OutLog( __FILEW__, __LINE__, LOG::LL_INFO, L"CrushDump", __VA_ARGS__)


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
		static CCrashDump *GetInstance() 
		{
			if (!s_pDump)
				s_pDump = new CCrashDump();
			return s_pDump;
		}

		~CCrashDump()
		{
			Clean();
		}

	public:
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

		bool Init( const std::wstring& szDumFilename, const std::wstring& szReportExeName, eDumpLevel dlevel)
		{
			m_DumpFilename = szDumFilename;
			m_ReportExeName = szReportExeName;
			m_DumpLevel = dlevel;

			m_hDbgHelpDll = LoadLibraryW(L"dbghelp.dll");
			if( m_hDbgHelpDll == NULL )
			{

				return false;
			}

			m_pfnMiniDumpWriteDump = (MiniDumpWriteDumpFunction)GetProcAddress( m_hDbgHelpDll, "MiniDumpWriteDump");
			if( m_pfnMiniDumpWriteDump == NULL )
			{

				return false;
			}

			m_OldHandler = SetUnhandledExceptionFilter( CrashDump_Handler);


			g_old = _set_invalid_parameter_handler( &gfx_invalid_parameter_handler );


			return true;
		}



	private:
		friend void __cdecl ::gfx_invalid_parameter_handler(
			const wchar_t *pszExpression,
			const wchar_t *pszFunction,
			const wchar_t *pszFile,
			unsigned int nLine,
			uintptr_t pReserved);

		static long _stdcall CrashDump_Handler( PEXCEPTION_POINTERS ExceptionInfo)
		{
			long lRet = CCrashDump::GetInstance()->OnCrashDumpHandler( ExceptionInfo);
			return lRet;
		}

		long OnCrashDumpHandler( PEXCEPTION_POINTERS ExceptionInfo)
		{

			HANDLE hDumpFile = CreateFile( m_DumpFilename.c_str(), 
				GENERIC_READ | GENERIC_WRITE, 0, 
				0, CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, 0);
			if( hDumpFile == INVALID_HANDLE_VALUE)
			{


				return EXCEPTION_CONTINUE_SEARCH;
			}



			MINIDUMP_EXCEPTION_INFORMATION stMEI = {0};
			stMEI.ThreadId = GetCurrentThreadId();
			stMEI.ExceptionPointers = ExceptionInfo;
			stMEI.ClientPointers = true;



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

			}

			CloseHandle( hDumpFile );

			if( bRet )
			{


				STARTUPINFO stSI = {0};
				PROCESS_INFORMATION	stPI = {0};
				stSI.cb = sizeof(stSI);


				std::wstring strDumpFilenameArg = L" " +  m_DumpFilename;
				if( CreateProcess( m_ReportExeName.c_str(),
					(wchar_t*)strDumpFilenameArg.c_str(), NULL, NULL, FALSE, 0, NULL, NULL, &stSI, &stPI))
				{

					CloseHandle(stPI.hProcess);
					CloseHandle(stPI.hThread);
				}
				else
				{

				}
			}


			{
				MEMORYSTATUSEX memstatus = { sizeof(memstatus) };
				::GlobalMemoryStatusEx(&memstatus);
				wchar_t buf[256];
				wsprintf(buf,L"Crash Memory(use/ava/tot): %d/%d/%dM %d%%"
					, (int)(memstatus.ullTotalVirtual/1024)/1024 - (int)(memstatus.ullAvailVirtual/1024)/1024
					, (int)(memstatus.ullAvailPhys/1024)/1024, (int)(memstatus.ullTotalPhys/1024)/1024, (int)memstatus.dwMemoryLoad);

			}




			return EXCEPTION_CONTINUE_SEARCH;
		}

		BOOL TryDump( HANDLE hDumpFile, eDumpLevel dlevel, MINIDUMP_TYPE type, MINIDUMP_EXCEPTION_INFORMATION& stMEI)
		{

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


				return TRUE;
			}

			return FALSE;
		}



	};

	CCrashDump *CCrashDump::s_pDump = NULL;

	void CrashDump_Clean(void)
	{
		CCrashDump *pDump = CCrashDump::GetInstance();
		pDump->Clean();
		delete pDump;
	}

	bool CrashDump_Init( const std::wstring& szDumFilename, const std::wstring& szReportExeName, eDumpLevel dlevel)
	{
		return CCrashDump::GetInstance()->Init( szDumFilename, szReportExeName, dlevel);
	}
} 
#include <memory>
void __cdecl gfx_invalid_parameter_handler(
	const wchar_t *pszExpression,
	const wchar_t *pszFunction,
	const wchar_t *pszFile,
	unsigned int nLine,
	uintptr_t pReserved)
{
	CrashDump::CCrashDump *pDump = CrashDump::CCrashDump::GetInstance();

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


	RtlCaptureContext(&ContextRecord);

#else  

	ZeroMemory(&ContextRecord, sizeof(ContextRecord));

#endif  

	ZeroMemory(&ExceptionRecord, sizeof(ExceptionRecord));

	ExceptionRecord.ExceptionCode = STATUS_INVALID_PARAMETER;
	ExceptionRecord.ExceptionAddress = _ReturnAddress();

	ExceptionPointers.ExceptionRecord = &ExceptionRecord;
	ExceptionPointers.ContextRecord = &ContextRecord;


	DWORD ret = CrashDump::CCrashDump::CrashDump_Handler(&ExceptionPointers);
	if ( ret == EXCEPTION_EXECUTE_HANDLER || ret == EXCEPTION_CONTINUE_EXECUTION  )
	{

		return;
	}


	LPTOP_LEVEL_EXCEPTION_FILTER pOldHandler = SetUnhandledExceptionFilter(NULL);


	ret = UnhandledExceptionFilter(&ExceptionPointers);



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

		TerminateProcess(GetCurrentProcess(), STATUS_INVALID_PARAMETER);
		return;
	}
	else
	{

		DebugBreak();


		SetUnhandledExceptionFilter(pOldHandler);
	}


}