#pragma once
#include <Psapi.h>
#pragma comment( lib, "Psapi.lib" )
class CProcessInfo
{
public:
	CProcessInfo()
	{
		m_hProcess = NULL;
	}
	~CProcessInfo()
	{
		Close();
	}

	BOOL Attach( HANDLE hProcess )
	{
		m_hProcess = hProcess;
	}

	HANDLE Detach( )
	{
		HANDLE hProcess = m_hProcess;
		m_hProcess = NULL;
		return hProcess;
	}

	BOOL Open( DWORD dwProcessId )
	{
		// Flags is default
		m_hProcess = ::OpenProcess( PROCESS_QUERY_INFORMATION |
			PROCESS_VM_READ, FALSE,  dwProcessId );
		if( m_hProcess != NULL )
		{
			return TRUE;
		}
		else
		{
			return FALSE;
		}
	}

	void Close()
	{
		if( m_hProcess != NULL )
		{
			CloseHandle( m_hProcess );
			m_hProcess = NULL;
		}
	}

	BOOL GetCreationTime( LPFILETIME lpCreationTime )
	{
		_ASSERT( m_hProcess != NULL );
		FILETIME f2, f3, f4;
		return GetProcessTimes( m_hProcess, lpCreationTime, &f2, &f3, &f4);
	}

	BOOL GetCreationLocalSystemTime( LPSYSTEMTIME lpSystemCreationTime )
	{
		FILETIME creationTime = {0};
		if( GetCreationTime( &creationTime ) )
		{
			// To local time
			FileTimeToLocalFileTime( &creationTime, &creationTime );
			FileTimeToSystemTime(&creationTime, lpSystemCreationTime);
			return TRUE;
		}
		return FALSE;
	}

	DWORD GetExePathName( LPWSTR lpFilename, DWORD nSize )
	{
		// MSDN for GetModuleFileNameEx:
		// If hModule is NULL, GetModuleFileNameEx returns 
		// the path of the executable file of the process specified in hProcess. 
		_ASSERT( m_hProcess != NULL );
		return ::GetModuleFileNameExW( m_hProcess, NULL, lpFilename, nSize );
	}

	DWORD GetImageFileName( LPWSTR lpImageFileName, DWORD nSize )
	{
		_ASSERT( m_hProcess != NULL );
		return ::GetProcessImageFileNameW( m_hProcess, lpImageFileName, nSize );
	}

protected:
	HANDLE m_hProcess;
};
