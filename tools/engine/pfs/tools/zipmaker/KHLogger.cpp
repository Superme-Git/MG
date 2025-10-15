///=============================================================================
///	文 件 名：KHLogger.cpp
///	作    者：刘东学
///=============================================================================
///	模块说明：日志记录器
///=============================================================================
/// 版本历史：
///=============================================================================
///		2007-06-21 14:22:44 - V1.0 创建
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
///	外挂消息钩子
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
// summary: 尝试打开日志文件
//
// returns:
//	如果已打开，返回TRUE，
//	否则尝试打开文件，如果打开失败，返回FALSE;
//	否则初始化文件并返回TRUE。
//
// remarks：没有对其加锁
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
		//替换 .exe 为 .log
		StringCchCopy( pDirEnd, _MAX_PATH, g_lpszSuffix );

		CFileException fileException;
		
		// 如果用户未曾设置日志文件路径
		if( g_lpszLogFileName[0] == 0)
		{
			// 得到不依赖于CurrentDirectory的绝对路径,该绝对路径是当前
			// Module的实际存放路径相对的。
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
				// 根据进程ID重新确定文件名
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
		// 关闭当前的日志文件
		g_logFile.Close();
		
		// 备份文件并重新建立日志文件

		// 备份文件名使用日期做后缀
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
			// 重命名
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

		// 尝试重新建立日志文件
		if( bReopen == FALSE)
		{
			// 避免重复reopen
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
// 为日志附加前缀
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
	///	交由消息钩子处理
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
	///	写到日志文件中
	///=============================================================================
	g_logFile.Write(buffer, (UINT)wcslen(buffer)*2);
}

//
// 对lpszMessage的换行符进行规范化
//
// 对单独的\r和\n都转换成\r\n对(PC标准)
//
// 如果lplpszMessage2为NULL,那么只对lpszMessage进行检测
// 否则，如果需要规范化，*lplpszMessage2为lpszMessage规范化的结果
// 
// 返回值为是否需要规范化，或是否对输入的lpszMessage进行了规范化处理
// 对于无需规范化的字符串，*lplpszMessage2将为NULL。
// 对于需规范化的字符串，如果lplpszMessage2不为NULL，*lplpszMessage2 为规范化结果
// 使用LocalFree释放之.
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
// 将消息写到日志文件中[附加消息前缀]
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

	// 附加日志前缀信息
	_WriteLogPrefix();

	if( len < 64 )
	{
		static WCHAR buffer[128] = {0};
		int nWlen = MultiByteToWideChar( CP_ACP, 0, lpszMessage, len, buffer, 128 );
		buffer[nWlen] = 0; // 截断

		///=============================================================================
		///	交由消息钩子处理
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
		///	写到日志文件中
		///=============================================================================
		g_logFile.Write( buffer, nWlen*2 );
	}
	else
	{
		// 动态分配缓冲区
		WCHAR *buffer = new WCHAR[len];
		ZeroMemory(buffer,len*2);
		int nWlen = MultiByteToWideChar( CP_ACP, 0, lpszMessage, len, buffer, len*2 );

		///=============================================================================
		///	交由消息钩子处理
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
		///	写到日志文件中
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
// 将消息写到日志文件中[附加消息前缀]
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

	// 附加日志前缀信息
	_WriteLogPrefix();

	///=============================================================================
	///	交由消息钩子处理
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
			buffer[nlen] = 0; // 截断
			(*g_KHMessageHookA)(g_HookA_UserParam, buffer );
		}
		else
		{
			CHAR *buffer = new CHAR[len*2];
			ZeroMemory(buffer,len*2);
			int nlen = WideCharToMultiByte( CP_ACP, 0, lpszMessage, len, buffer, len*2, NULL, NULL );
			buffer[nlen] = 0; // 截断
			(*g_KHMessageHookA)(g_HookA_UserParam, buffer );
			delete[] buffer;
		}
	}

	///=============================================================================
	///	写到日志文件中
	///=============================================================================
	// 以UNICODE方式写入数据块
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


