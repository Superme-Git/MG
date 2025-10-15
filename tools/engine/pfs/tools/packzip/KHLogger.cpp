///=============================================================================
///	�� �� ����KHLogger.cpp
///	��    �ߣ�����ѧ
///=============================================================================
///	ģ��˵������־��¼��
///=============================================================================
/// �汾��ʷ��
///=============================================================================
///		2007-06-21 14:22:44 - V1.0 ����
///=============================================================================
#include "StdAfx.h"
#include ".\khlogger.h"
#include <afxmt.h>
#include <strsafe.h>

const int			MAX_LOG_FILE_SIZE = 32*1024*1024; // MAX is 32M
CFile				g_logFile;
TCHAR				g_lpszLogFileName[_MAX_PATH+1] = _T("");
const TCHAR			g_lpszSuffix[] =  _T(".log");
const TCHAR			g_lpszSuffixPID[] =  _T("_P%04X.log");
CCriticalSection	g_csLogFile;

#define AUTO_LOCK()	CSingleLock __autoLock(&g_csLogFile, TRUE);

///=============================================================================
///	�����Ϣ����
///=============================================================================
static LPF_KHMessageHookA	g_KHMessageHookA = NULL;
static LPF_KHMessageHookW	g_KHMessageHookW = NULL;
static LPVOID g_HookA_UserParam = NULL;
static LPVOID g_HookW_UserParam = NULL;

LPF_KHMessageHookA KHInstallMessageHookA( LPF_KHMessageHookA newHook, LPVOID pUserParam )
{
	AUTO_LOCK();
	LPF_KHMessageHookA old = g_KHMessageHookA;
	g_KHMessageHookA = newHook;
	g_HookA_UserParam = pUserParam;
	return old;
}

LPF_KHMessageHookW KHInstallMessageHookW( LPF_KHMessageHookW newHook, LPVOID pUserParam )
{
	AUTO_LOCK();
	LPF_KHMessageHookW old = g_KHMessageHookW;
	g_KHMessageHookW = newHook;
	g_HookW_UserParam = pUserParam;
	return old;
}

void KHMsgLog_SetFilePathA(LPCSTR lpszFilePath)
{
#ifdef UNICODE
	int nLen = MultiByteToWideChar(CP_ACP, 0, lpszFilePath, lstrlenA(lpszFilePath), g_lpszLogFileName, _MAX_PATH);
	if ( nLen > 0 && nLen <= _MAX_PATH )
	{
		g_lpszLogFileName[nLen] = 0;
	}	
#else
	StringCchCopy( g_lpszLogFileName, _MAX_PATH+1, lpszFilePath );
#endif
}

void KHMsgLog_SetFilePathW(LPCWSTR lpszFilePath)
{
#ifdef UNICODE
	StringCchCopy( g_lpszLogFileName, _MAX_PATH+1, lpszFilePath );
#else
	int nLen = WideCharToMultiByte(CP_ACP, 0, lpszFilePath, lstrlenW(lpszFilePath), g_lpszLogFileName, _MAX_PATH, NULL, NULL);
	if ( nLen > 0 && nLen <= _MAX_PATH )
	{
		g_lpszLogFileName[nLen] = 0;
	}
#endif
}

//==============================================================================
// summary: ���Դ���־�ļ�
//
// returns:
//	����Ѵ򿪣�����TRUE��
//	�����Դ��ļ��������ʧ�ܣ�����FALSE;
//	�����ʼ���ļ�������TRUE��
//
// remarks��û�ж������
// 2007-06-21 14:57:40 dongxue.liu
//==============================================================================
static BOOL _TryOpenLogFile()
{
	bool bReopen = FALSE;

__REOPEN__:
	if( g_logFile.m_hFile == INVALID_HANDLE_VALUE )
	{
		TCHAR szApp[_MAX_PATH] = {0};
		::GetModuleFileName( NULL, szApp, _MAX_PATH);
		TRACE(_T("%s\n"), szApp);
		LPTSTR pExeName = _tcsrchr( szApp, _T('\\'));
		pExeName[0] = 0;
		pExeName++;
		LPTSTR pDirEnd = _tcsrchr( pExeName, _T('.'));
		//�滻 .exe Ϊ .log
		StringCchCopy( pDirEnd, _MAX_PATH, g_lpszSuffix );

		CFileException fileException;
		
		// ����û�δ��������־�ļ�·��
		if( g_lpszLogFileName[0] == 0)
		{
			// �õ���������CurrentDirectory�ľ���·��,�þ���·���ǵ�ǰ
			// Module��ʵ�ʴ��·����Եġ�
			//  2006-05-18 09:41:24
			StringCchCopy( g_lpszLogFileName, _MAX_PATH+1, szApp );
			StringCchCat( g_lpszLogFileName, _MAX_PATH+1, _T("\\log\\"));
		}
		else
		{
			size_t len = _tcslen( g_lpszLogFileName);
			if( g_lpszLogFileName[len-1] != _T('\\') && g_lpszLogFileName[len-1] != _T('/'))
			{
				StringCchCat( g_lpszLogFileName, _MAX_PATH+1, _T("\\"));
			}
		}
		::CreateDirectory( g_lpszLogFileName, NULL);

		StringCchCat( g_lpszLogFileName, _MAX_PATH+1, pExeName);
		TRACE(_T("%s\n"), g_lpszLogFileName);

		bool bRetried = false; // Only Retry once for same failed reason.
__RETRY__:
		if( g_logFile.Open(
			g_lpszLogFileName, 
			CFile::modeCreate | 
			CFile::modeNoTruncate | 
			CFile::modeWrite | 
			CFile::shareDenyWrite, // Deny Write
			&fileException) )
		{		
			if( g_logFile.GetLength() == 0 )
			{
				static const WORD wUnicodeBOM = 0xFEFF;
				g_logFile.Write(&wUnicodeBOM,2);
			}
			else
			{
				g_logFile.SeekToEnd();
			}
		}
		else
		{
			if ( !bRetried && (
				fileException.m_cause == CFileException::accessDenied ||
				fileException.m_cause == CFileException::invalidFile ||
				fileException.m_cause == CFileException::sharingViolation ||
				fileException.m_cause == CFileException::lockViolation ) )
			{
				// ���ݽ���ID����ȷ���ļ���
				DWORD dwProcessId = GetCurrentProcessId();
				StringCchPrintf ( pDirEnd, _MAX_PATH, g_lpszSuffixPID, dwProcessId );

				StringCchCopy( g_lpszLogFileName, _MAX_PATH+1, szApp );
				StringCchCat( g_lpszLogFileName, _MAX_PATH+1, _T("\\log\\"));	
				StringCchCat( g_lpszLogFileName, _MAX_PATH+1, pExeName);
				TRACE(_T("%s\n"), g_lpszLogFileName);

				bRetried = true;
				goto __RETRY__;
			}
			else
			{
				TCHAR ErrorMessage[256] = {0};
				if( fileException.GetErrorMessage(ErrorMessage,256) && ErrorMessage[0] != 0 )
				{
					TRACE(ErrorMessage);TRACE(_T("\n"));
				}
				else
				{
					TRACE(_T("Open File [%s] Failed!(%hs:%d)\n"), g_lpszLogFileName, __FILE__, __LINE__ );
				}
			}

			return FALSE;
		}
	}

	if( g_logFile.GetLength() >= MAX_LOG_FILE_SIZE )
	{
		g_logFile.Flush();
		// �رյ�ǰ����־�ļ�
		g_logFile.Close();
		
		// �����ļ������½�����־�ļ�

		// �����ļ���ʹ����������׺
		SYSTEMTIME systemTime = {0};
		GetLocalTime(&systemTime);
		long lMilliSeconds = 1000*(systemTime.wHour * 3600 + systemTime.wMonth*60 + systemTime.wSecond) 
			+ systemTime.wMilliseconds;
		CString lpszLogFileName_backup = g_lpszLogFileName;
		lpszLogFileName_backup.Truncate(lpszLogFileName_backup.GetLength()-(int)_tcslen(g_lpszSuffix));
		lpszLogFileName_backup.AppendFormat(
			_T("_[%04hu-%02hu-%02hu][%-06d]%s"), 
			systemTime.wYear,systemTime.wMonth,systemTime.wDay, lMilliSeconds, g_lpszSuffix );

		TRY
		{
			// ������
			CFile::Rename(g_lpszLogFileName,  lpszLogFileName_backup);
		}
		CATCH_ALL(e)
		{
			TCHAR ErrorMessage[256] = {0};
			if( e->GetErrorMessage(ErrorMessage,256) && ErrorMessage[0] != 0 )
			{
				TRACE(ErrorMessage);TRACE(_T("\n"));
			}
			else
			{
				TRACE(_T("Open File [%s] Failed!(%hs:%d)\n"), g_lpszLogFileName, __FILE__, __LINE__ );
			}
			e->Delete();
		}
		END_CATCH_ALL

		// �������½�����־�ļ�
		if( bReopen == FALSE)
		{
			// �����ظ�reopen
			bReopen = TRUE;
			goto __REOPEN__;
		}
		else
		{
			return FALSE;
		}
	}

	return TRUE;
}

//==============================================================================
// Ϊ��־����ǰ׺
//==============================================================================
void _WriteLogPrefix()
{
	static WCHAR buffer[40] = {0};
	SYSTEMTIME systemTime = {0};
	GetLocalTime(&systemTime);
	StringCchPrintfW(
		buffer, _countof(buffer),
		L"[%04hu-%02hu-%02hu %02hu:%02hu:%02hu.%03hu][%04lu]",
		systemTime.wYear,systemTime.wMonth,systemTime.wDay,
		systemTime.wHour,systemTime.wMinute,systemTime.wSecond,
		systemTime.wMilliseconds, GetCurrentThreadId() );

	///=============================================================================
	///	������Ϣ���Ӵ���
	///=============================================================================
	if( g_KHMessageHookW != NULL )
	{
		(*g_KHMessageHookW)(g_HookW_UserParam, buffer );
	}
	else if( g_KHMessageHookA != NULL )
	{
		static CHAR bufferA[_countof(buffer)] = {0};
		StringCchPrintfA(
			bufferA, _countof(bufferA),
			"[%04hu-%02hu-%02hu %02hu:%02hu:%02hu.%03hu][%04lu]",
			systemTime.wYear,systemTime.wMonth,systemTime.wDay,
			systemTime.wHour,systemTime.wMinute,systemTime.wSecond,
			systemTime.wMilliseconds, GetCurrentThreadId() );
		(*g_KHMessageHookA)(g_HookA_UserParam, bufferA );
	}

	///=============================================================================
	///	д����־�ļ���
	///=============================================================================
	g_logFile.Write(buffer, (UINT)wcslen(buffer)*2);
}

//
// ��lpszMessage�Ļ��з����й淶��
//
// �Ե�����\r��\n��ת����\r\n��(PC��׼)
//
// ���lplpszMessage2ΪNULL,��ôֻ��lpszMessage���м��
// ���������Ҫ�淶����*lplpszMessage2ΪlpszMessage�淶���Ľ��
// 
// ����ֵΪ�Ƿ���Ҫ�淶�������Ƿ�������lpszMessage�����˹淶������
// ��������淶�����ַ�����*lplpszMessage2��ΪNULL��
// ������淶�����ַ��������lplpszMessage2��ΪNULL��*lplpszMessage2 Ϊ�淶�����
// ʹ��LocalFree�ͷ�֮.
//
BOOL CanonicalizeLineFeedsW( LPCWSTR lpszMessage, LPCWSTR *lplpszMessage2 )
{
	ASSERT( lpszMessage != NULL);
	size_t len = wcslen( lpszMessage );
	if( len <= 0 )
	{
		return FALSE;
	}

	BOOL bNeedCanonicalize = FALSE;
	size_t pos = 0;
	size_t len2 = len;
	for( LPCWSTR lpPtr = lpszMessage; lpPtr[0]; lpPtr += pos, len2 -= pos)
	{
		pos = wcscspn( lpPtr, L"\r\n");
		if( pos >= len2 )
		{
			break;
		}

		bNeedCanonicalize = lpPtr[pos] != L'\r' || lpPtr[pos+1] != L'\n';
		if ( bNeedCanonicalize )
		{
			break;
		}
		else
		{
			pos += 2;
		}
	}

	if( bNeedCanonicalize && lplpszMessage2 != NULL )
	{
		size_t maxneed = len*2+2-pos;
		LPWSTR lpszMessage2 = (LPWSTR)LocalAlloc( LPTR, maxneed * 2);
		wcsncpy_s( lpszMessage2, maxneed, lpszMessage, pos );

		wchar_t c_curr = 0, c_last = 0;
		size_t iDes = pos;
		size_t iSrc = pos;
		for ( ; iSrc <= len; iSrc ++ )
		{
			c_curr = lpszMessage[iSrc];

			if ( c_curr == L'\n' )
			{
				if ( c_last != L'\r' )
					lpszMessage2[iDes++] = L'\r';
			}
			else if( c_last == L'\r' )
			{
				if( c_curr != L'\n' )
					lpszMessage2[iDes++] = L'\n';
			}
			lpszMessage2[iDes++] = c_curr;
			c_last = c_curr;
		}

		*lplpszMessage2 = lpszMessage2;
	}

	return bNeedCanonicalize;
}

BOOL CanonicalizeLineFeedsA( LPCSTR lpszMessage, LPCSTR *lplpszMessage2 )
{
	ASSERT( lpszMessage != NULL);
	size_t len = strlen( lpszMessage );
	if( len <= 0 )
	{
		return FALSE;
	}

	BOOL bNeedCanonicalize = FALSE;
	size_t pos = 0;
	size_t len2 = len;
	for( LPCSTR lpPtr = lpszMessage; lpPtr[0]; lpPtr += pos, len2 -= pos)
	{
		pos = strcspn( lpPtr, "\r\n");
		if( pos >= len2 )
		{
			break;
		}

		bNeedCanonicalize = lpPtr[pos] != '\r' || lpPtr[pos+1] != '\n';
		if ( bNeedCanonicalize )
		{
			break;
		}
		else
		{
			pos += 2;
		}
	}

	if( bNeedCanonicalize && lplpszMessage2 != NULL )
	{
		size_t maxneed = len*2+2-pos;
		LPSTR lpszMessage2 = (LPSTR)LocalAlloc( LPTR, maxneed);
		strncpy_s( lpszMessage2, maxneed, lpszMessage, pos );

		char c_curr = 0, c_last = 0;
		size_t iDes = pos;
		size_t iSrc = pos;
		for ( ; iSrc <= len; iSrc ++ )
		{
			c_curr = lpszMessage[iSrc];

			if ( c_curr == '\n' )
			{
				if ( c_last != '\r' )
					lpszMessage2[iDes++] = '\r';
			}
			else if( c_last == '\r' )
			{
				if( c_curr != '\n' )
					lpszMessage2[iDes++] = '\n';
			}
			lpszMessage2[iDes++] = c_curr;
			c_last = c_curr;
		}

		*lplpszMessage2 = lpszMessage2;
	}

	return bNeedCanonicalize;
}

//==============================================================================
// ����Ϣд����־�ļ���[������Ϣǰ׺]
//==============================================================================
void KHMsgLogA(LPCSTR lpszMessage)
{
	if(lpszMessage == NULL) return;

	AUTO_LOCK();
	
	if( !_TryOpenLogFile() )
	{
		return;
	}
	
	int len = (int)strlen( lpszMessage );
	if( len <= 0 )
	{
		return;
	}


	LPCSTR lpszMessage2 = NULL;
	BOOL bCanonicallized = CanonicalizeLineFeedsA( lpszMessage, &lpszMessage2 );
	if ( bCanonicallized )
	{
		lpszMessage = lpszMessage2;
	}

	// ������־ǰ׺��Ϣ
	_WriteLogPrefix();

	if( len < 64 )
	{
		static WCHAR buffer[128] = {0};
		int nWlen = MultiByteToWideChar( CP_ACP, 0, lpszMessage, len, buffer, 128 );
		buffer[nWlen] = 0; // �ض�

		///=============================================================================
		///	������Ϣ���Ӵ���
		///=============================================================================
		if( g_KHMessageHookA != NULL )
		{
			(*g_KHMessageHookA)(g_HookA_UserParam, lpszMessage );
		}
		else if( g_KHMessageHookW != NULL )
		{
			(*g_KHMessageHookW)(g_HookW_UserParam, buffer );
		}

		///=============================================================================
		///	д����־�ļ���
		///=============================================================================
		g_logFile.Write( buffer, nWlen*2 );
	}
	else
	{
		// ��̬���仺����
		WCHAR *buffer = new WCHAR[len];
		ZeroMemory(buffer,len*2);
		int nWlen = MultiByteToWideChar( CP_ACP, 0, lpszMessage, len, buffer, len*2 );

		///=============================================================================
		///	������Ϣ���Ӵ���
		///=============================================================================
		if( g_KHMessageHookW != NULL )
		{
			(*g_KHMessageHookW)(g_HookW_UserParam, buffer );
		}
		else if( g_KHMessageHookA != NULL )
		{
			(*g_KHMessageHookA)(g_HookA_UserParam, lpszMessage );
		}
		///=============================================================================
		///	д����־�ļ���
		///=============================================================================
		g_logFile.Write( buffer, nWlen*2 );

		delete [] buffer;
	}

	if ( bCanonicallized )
	{
		LocalFree( (HLOCAL)lpszMessage2 );
	}

	g_logFile.Flush();
}

//==============================================================================
// ����Ϣд����־�ļ���[������Ϣǰ׺]
//==============================================================================
void KHMsgLogW(LPCWSTR lpszMessage)
{
	if(lpszMessage == NULL) return;

	AUTO_LOCK();
	
	if( !_TryOpenLogFile() )
	{
		return;
	}

	int len = (int)wcslen( lpszMessage );
	if( len <= 0 )
	{
		return;
	}

	LPCWSTR lpszMessage2 = NULL;
	BOOL bCanonicalized = CanonicalizeLineFeedsW( lpszMessage, &lpszMessage2 );
	if ( bCanonicalized )
	{
		lpszMessage = lpszMessage2;
	}

	// ������־ǰ׺��Ϣ
	_WriteLogPrefix();

	///=============================================================================
	///	������Ϣ���Ӵ���
	///=============================================================================
	if( g_KHMessageHookW != NULL )
	{
		(*g_KHMessageHookW)(g_HookW_UserParam, lpszMessage );
	}
	else if( g_KHMessageHookA != NULL )
	{
		if( len <= 64 )
		{
			static CHAR buffer[128] = {0};
			int nlen = WideCharToMultiByte( CP_ACP, 0, lpszMessage, len, buffer, 128, NULL, NULL );
			buffer[nlen] = 0; // �ض�
			(*g_KHMessageHookA)(g_HookA_UserParam, buffer );
		}
		else
		{
			CHAR *buffer = new CHAR[len*2];
			ZeroMemory(buffer,len*2);
			int nlen = WideCharToMultiByte( CP_ACP, 0, lpszMessage, len, buffer, len*2, NULL, NULL );
			buffer[nlen] = 0; // �ض�
			(*g_KHMessageHookA)(g_HookA_UserParam, buffer );
			delete[] buffer;
		}
	}

	///=============================================================================
	///	д����־�ļ���
	///=============================================================================
	// ��UNICODE��ʽд�����ݿ�
	g_logFile.Write( lpszMessage, (UINT)wcslen(lpszMessage)*2 );

	if ( bCanonicalized )
	{
		LocalFree( (HLOCAL)lpszMessage2 );
	}

	g_logFile.Flush();
}

#define KHMesageLog(Type)\
void __cdecl KHMsgLog_##Type##W(LPCWSTR lpszFormat, ...)\
	{\
		WCHAR szBuffer[16*1024] = {0};\
		va_list args;\
		va_start(args, lpszFormat);\
		HRESULT hr = StringCchVPrintfW(szBuffer, _countof(szBuffer), lpszFormat, args);\
		va_end(args);\
		if(SUCCEEDED(hr))\
		{\
			KHMsgLogW(szBuffer);\
		}\
		else\
		{\
			ASSERT(FALSE);\
		}\
	}\
	void __cdecl KHMsgLog_##Type##A(LPCSTR lpszFormat, ...)\
	{\
		CHAR szBuffer[16*1024] = {0};\
		va_list args;\
		va_start(args, lpszFormat);\
		HRESULT hr = StringCchVPrintfA(szBuffer, _countof(szBuffer), lpszFormat, args);\
		va_end(args);\
		if(SUCCEEDED(hr))\
		{\
			KHMsgLogA(szBuffer);\
		}\
		else\
		{\
			ASSERT(FALSE);\
		}\
	}\

KHMesageLog(Info)
KHMesageLog(Warning)
KHMesageLog(Debug)
KHMesageLog(Error)
KHMesageLog(Fatal)


