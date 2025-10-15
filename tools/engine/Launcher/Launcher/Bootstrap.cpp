// Bootstrap.cpp
// 
// 游戏自举程序
//
// author : yanghaibo
// date : 2008-12-01 
//
// 本模块实现了两种方案的启动方案，分别参见下面2个函数的注释说明文档。
//		ChooseVersion()
//		SnapshotVersion()
// 现在我们使用SnapshotVersion()方案，这可以简化更新逻辑。
//
// author : yanghaibo
// date : 2008-12-17
//
// 本模块实现了三种方案的启动方案，分别参见下面3个函数的注释说明文档。
//		ChooseVersion()
//		SnapshotVersion()
//		SnapshotPatcherFiles()
// 现在我们使用SnapshotPatcherFiles()方案，这可以简化多版本更新逻辑。
//
// author : yanghaibo
// date : 2009-12-15
//
#include "stdafx.h"
#include "resource.h"
#include <atlbase.h>
#include <statreg.h>
#include <psapi.h>
#include <list>
#pragma comment( lib, "psapi.lib" )
#include "../Common/MZLogger.h"
#include "../Patcher/TextManager.h"
#include "TextID.h"

//
// 获取运行程序自身文件名
//
LPCTSTR GetApplicationFileName()
{
	static TCHAR FileName[MAX_PATH];
	if ( FileName[0] == NULL )
	{
		GetModuleFileName( NULL, FileName, MAX_PATH);
		PathStripPath( FileName );//Leaves the file name only
	}
	return FileName;
}

//
// 获取运行程序Launcher所在目录
//
LPCTSTR GetModuleDir()
{
	static TCHAR szModuleDir[MAX_PATH];
	if ( szModuleDir[0] == 0 )
	{
		DWORD dwRet = GetModuleFileName( NULL, szModuleDir, MAX_PATH);
		LPTSTR lpSep = &szModuleDir[dwRet];
		while( *lpSep != _T('/') && *lpSep != _T('\\') && *lpSep != _T(':') )
		{
			lpSep --;
		}
		lpSep[0] = _T('\0');
	}

	return szModuleDir;
}

// Launcher 程序位于install directory 的bin/binD目录下
// 因此，InstallDir为ModuleDir的父目录
LPCTSTR GetInstallDir()
{
	static TCHAR szLauncherDir[MAX_PATH];
	if ( szLauncherDir[0] == 0 )
	{
		DWORD dwRet = GetModuleFileName( NULL, szLauncherDir, MAX_PATH);
		LPTSTR lpSep = &szLauncherDir[dwRet-1];
		while( *lpSep != _T('/') && *lpSep != _T('\\') && *lpSep != _T(':') && *lpSep != _T('\0') )
		{
			lpSep --;
		}
		
		if( *lpSep != _T('\0') )
			lpSep --;
		
		while( *lpSep != _T('/') && *lpSep != _T('\\') && *lpSep != _T(':') && *lpSep != _T('\0') )
		{
			lpSep --;
		}

		lpSep[1] = _T('\0');
	}

	return szLauncherDir;
}

// 获取launcher自己所在的文件夹名字
LPCTSTR GetLauncherFolder()
{
	static TCHAR szLauncherFolder[MAX_PATH];
	if( szLauncherFolder[0] == 0 )
	{
		TCHAR szModuleDir[MAX_PATH] = {0};
		DWORD dwRet = GetModuleFileName( NULL, szModuleDir, MAX_PATH);
		LPTSTR lpSep = &szModuleDir[dwRet];
		while( *lpSep != _T('/') && *lpSep != _T('\\') && *lpSep != _T(':') )
		{
			lpSep --;
		}

		LPTSTR lpSepEnd = lpSep;
		
		do
		{
			lpSep --;
		}while( *lpSep != _T('/') && *lpSep != _T('\\') && *lpSep != _T(':') && *lpSep != _T('\0') );

		LPTSTR lpSepStart = ++ lpSep;

		_tcsncpy_s( szLauncherFolder, MAX_PATH, lpSepStart, lpSepEnd-lpSepStart );
	}

	return szLauncherFolder;
}


//
// 格式化系统API产生的错误消息
//
void FormatErrorMessage( DWORD dwErrorCode, std::tstring* pMessage, ... )
{
	va_list vl;
	va_start(vl, pMessage);

	LPTSTR lpBuffer = NULL;
	::FormatMessage(
		FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_ALLOCATE_BUFFER, NULL, 
		dwErrorCode, 0, (LPTSTR)&lpBuffer, 0, &vl);
	if( lpBuffer != NULL )
	{
		*pMessage = lpBuffer;
		LocalFree(lpBuffer);
	}

	va_end(vl);
}

//
// 报告错误消息
//
void ReportMessage( LPCTSTR lpszMessageFormat, ... )
{
	va_list vl;
	va_start(vl, lpszMessageFormat);
	TCHAR buffer[ 1024 ];
	_vstprintf_s( buffer, _countof(buffer), lpszMessageFormat, vl);
	::MessageBox( NULL, buffer, GlobalGetText( TIP_ERROR).c_str(), MB_OK|MB_ICONERROR );
	va_end(vl);
}

//
// 删除指定的目录以及目录下的所有文件
//
bool DeleteFolder( std::wstring strFolder )
{
	// 我们必须保证字符串最后有2个0结尾
	strFolder.resize( strFolder.size() + 1, 0 );
	SHFILEOPSTRUCT FileOp = {0};
	FileOp.pFrom = strFolder.c_str();
	FileOp.wFunc = FO_DELETE;
	FileOp.fFlags = FOF_NOCONFIRMATION | FOF_NOERRORUI | FOF_SILENT;
	int iRet = SHFileOperation(&FileOp);
	if ( iRet != ERROR_SUCCESS )
	{
		SetLastError( iRet );
		return false;
	}

	return true;
}

class CFileHelperBase
{
public:
	void TryFindFile( const std::wstring& path, const std::wstring& name, bool br )
	{
		if( br)
		{
			std::wstring	find = path + L"\\*.*";
			WIN32_FIND_DATAW		wfd;
			HANDLE hfind = FindFirstFileW( find.c_str(), &wfd);
			if( INVALID_HANDLE_VALUE == hfind)
				return;
			OnFoundPath( path, name, wfd, br );
			while( FindNextFileW( hfind, &wfd))
				OnFoundPath( path, name, wfd, br);
			FindClose( hfind);
		}

		{
			std::wstring	find = path + L"\\" + name;
			WIN32_FIND_DATAW		wfd;
			HANDLE hfind = FindFirstFileW( find.c_str(), &wfd);
			if( INVALID_HANDLE_VALUE == hfind)
				return;
			OnFoundFile( path, name, wfd );
			while( FindNextFileW( hfind, &wfd ) )
				OnFoundFile( path, name, wfd );
			FindClose( hfind);
		}
	}

	void OnFoundPath( const std::wstring& path, const std::wstring& name, WIN32_FIND_DATAW& wfd, bool br )
	{
		if( (wfd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) != FILE_ATTRIBUTE_DIRECTORY)
			return;
		if( L'.' == wfd.cFileName[0])
			return;
		_ASSERTE(br);

		std::wstring newpath = path + L"\\" + wfd.cFileName;
		TryFindFile( newpath, name, br );
	}

	void OnFoundFile( const std::wstring& path, const std::wstring& name, WIN32_FIND_DATAW& wfd )
	{
		if( (wfd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) == FILE_ATTRIBUTE_DIRECTORY)
			return;

		std::wstring filename = path + L"\\" + wfd.cFileName;

		OnFindFile( filename );
	}

protected:
	virtual void OnFindFile( const std::wstring& filename ){}
};

class CCopyFile : public CFileHelperBase
{
	std::wstring m_strFolderSrc;
	std::wstring m_strFolderDst;
	std::list< std::wstring > m_pathlist_src;
	std::list< std::wstring > m_pathlist_dst;

public:
	CCopyFile( std::wstring strFolderSrc, std::wstring strFolderDst )
	{
		m_strFolderSrc = strFolderSrc;
		m_strFolderDst = strFolderDst;
	}

	bool Open( const std::list<std::wstring>& strPatternList )
	{
		std::list< std::wstring >::const_iterator itr = strPatternList.begin();
		std::list< std::wstring >::const_iterator itrEnd = strPatternList.end();
		for ( ; itr != itrEnd; ++ itr )
		{
			TryFindFile( m_strFolderSrc, (*itr), false );
		}
		return true;
	}

	bool Do()
	{
		std::list< std::wstring >::const_iterator citrSrc = m_pathlist_src.begin();
		std::list< std::wstring >::iterator citrDst = m_pathlist_dst.begin();
		std::list< std::wstring >::iterator citrEnd = m_pathlist_dst.end();
		size_t szSourceFolder = m_strFolderSrc.size();
		size_t szDestCount = m_strFolderDst.size();
		const wchar_t* pszDestFolder = m_strFolderDst.c_str();
		for ( ; citrDst != citrEnd; ++ citrDst, ++ citrSrc )
		{
			(*citrDst).replace(0, szSourceFolder, pszDestFolder, szDestCount );
			BOOL bOK = CopyFile( (*citrSrc).c_str(), (*citrDst).c_str(), FALSE );
			if ( !bOK )
			{
				DWORD dwError = GetLastError();
				std::tstring strMessage;
				FormatErrorMessage(dwError, &strMessage, (*citrSrc).c_str() );
				ReportMessage( GlobalFormatText( ERR_START,	dwError, (*citrSrc).c_str(), 
					(*citrDst).c_str(), strMessage.c_str()).c_str() );
				KHMsgLog_Error( _T("启动程序过程中发生错误. 拷贝文件失败(%d): %s -> %s %s\n!"),
					dwError, (*citrSrc).c_str(), (*citrDst).c_str(), strMessage.c_str() );
				return false;
			}
		}

		return true;
	}

	void Close()
	{
	}

protected:
	virtual void OnFindFile( const std::wstring& filename )
	{
		m_pathlist_src.push_back(filename);
		m_pathlist_dst.push_back(filename);
	}
};

//
// Copy指定的目录下的所有文件到目标目录
//
bool CopyFilesUnderFolder( std::wstring strFolderSrc, std::wstring strFolderDst )
{
	// 我们必须保证字符串最后有2个0结尾
	//strFolderSrc += L"\\*";
	//strFolderSrc.resize( strFolderSrc.size() + 1, 0 );
	//SHFILEOPSTRUCT FileOp = {0};
	//FileOp.pFrom = strFolderSrc.c_str();
	//FileOp.pTo = strFolderDst.c_str();
	//FileOp.wFunc = FO_COPY;
	//FileOp.fFlags = FOF_NOCONFIRMATION | FOF_SILENT | FOF_NOCONFIRMMKDIR;
	//int iRet = SHFileOperation(&FileOp);
	//if ( iRet != ERROR_SUCCESS )
	//{
	//	SetLastError( iRet );
	//	return false;
	//}

	CCopyFile copyFiles( strFolderSrc, strFolderDst );
	std::list<std::wstring> patternList;
#ifdef _DEBUG
	patternList.push_back( std::wstring(L"patcherd.exe") );
	patternList.push_back( std::wstring(L"inetutilsd.dll") );
	patternList.push_back( std::wstring(L"repaird.exe") );
	patternList.push_back( std::wstring(L"p2sp_4th_libd.dll"));
#else
	patternList.push_back( std::wstring(L"patcher.exe") );
	patternList.push_back( std::wstring(L"inetutils.dll") );
	patternList.push_back( std::wstring(L"repair.exe") );
	patternList.push_back( std::wstring(L"p2sp_4th_lib.dll"));
#endif
	if( !copyFiles.Open(patternList) )
	{
		return false;
	}

	if ( !copyFiles.Do() )
	{
		copyFiles.Close();
		return false;
	}

	copyFiles.Close();
	return true;
}

//
// Move指定的目录下的所有文件到目标目录
//
bool MoveFilesUnderFolder( std::wstring strFolderSrc, std::wstring strFolderDst )
{
	// 我们必须保证字符串最后有2个0结尾
	strFolderSrc += L"\\*";
	strFolderSrc.resize( strFolderSrc.size() + 1, 0 );
	SHFILEOPSTRUCT FileOp = {0};
	FileOp.pFrom = strFolderSrc.c_str();
	FileOp.pTo = strFolderDst.c_str();
	FileOp.wFunc = FO_MOVE;
	FileOp.fFlags = FOF_CONFIRMMOUSE | FOF_NOCONFIRMATION | FOF_SILENT;
	int iRet = SHFileOperation(&FileOp);
	if ( iRet != ERROR_SUCCESS )
	{
		SetLastError( iRet );
		return false;
	}

	return true;
}
//
//  检查版本目录是否完整有效
//
BOOL SanityCheck( LPCTSTR lpszVersionFolder )
{
	//
	// TODO: 检查新版本目录是否完整有效
	//
	return TRUE;
}

//
// 选择launcher版本
// 
// 策略如下:
// Patcher 为旧版本或当前唯一版本.
// A. 如果存在Patcher.1，那么Patcher即为旧版本，Patcher.1为新版本
// B. 否则Patcher即为唯一版本.
// 在情况A下，我们需要删除Patcher目录，并将Patcher.1重命名为Patcher
// 然后将其作为最新的唯一版本。
// 
// Patcher.1为Patcher程序从服务器上下载的备份程序
// 这需要对更新程序的更新逻辑做特殊处理，
// 即所有Patcher的新程序都将放置在Patcher.1目录下，而非Patcher目录下
//
// 成功时返回Patcher.exe的全路径
//
LPCTSTR ChooseVersion()
{
	TCHAR szLauncherFolderAbs[MAX_PATH];
	TCHAR szLauncherFolderAbsNew[MAX_PATH];
	TCHAR szLauncherFolder[] = TEXT("patcher");
	TCHAR szLauncherFolderNew[] = TEXT("patcher.1");

#ifdef _DEBUG
	TCHAR szLauncherName[] = TEXT("Patcherd.exe");
#else
	TCHAR szLauncherName[] = TEXT("Patcher.exe");
#endif

	_tcscpy_s( szLauncherFolderAbs, MAX_PATH, GetInstallDir());
	_tcscat_s( szLauncherFolderAbs, MAX_PATH, szLauncherFolder );

	_tcscpy_s( szLauncherFolderAbsNew, MAX_PATH, GetInstallDir());
	_tcscat_s( szLauncherFolderAbsNew, MAX_PATH, szLauncherFolderNew );


	BOOL bOld = PathFileExists( szLauncherFolderAbs );
	BOOL bNew = PathFileExists( szLauncherFolderAbsNew );
	if ( !bOld && !bNew )
	{
		ReportMessage( GlobalFormatText( ERR_START_MISSING_FILE, szLauncherFolderAbs).c_str() );
		return NULL;
	}

	if ( bNew )
	{
		// 检查新版本目录是否完整有效，只有完整，我们才执行替换，否则，放弃该版本
		if( !SanityCheck( szLauncherFolderAbsNew ) )
		{
			DeleteFolder( szLauncherFolderAbsNew );
			bNew = FALSE;
		}
	}

	if ( bNew )
	{
		if( bOld )
		{
			if( !CopyFilesUnderFolder( szLauncherFolderAbsNew, szLauncherFolderAbs ) )
			{
				return NULL;
			}

			if( !DeleteFolder( szLauncherFolderAbsNew ) )
			{
				// 怎么办?
				DWORD dwError = GetLastError();
				std::tstring strMessage;
				FormatErrorMessage(dwError, &strMessage, szLauncherFolderAbs);
				ReportMessage( GlobalFormatText( ERR_START_NOT_DELETE_FILE, dwError, strMessage.c_str()).c_str() );
				return NULL;
			}
		}
		else
		{
			if( !MoveFile(szLauncherFolderAbsNew, szLauncherFolderAbs) )
			{
				DWORD dwError = GetLastError();
				std::tstring strMessage;
				FormatErrorMessage(dwError, &strMessage, szLauncherFolderAbsNew, szLauncherFolderAbs);
				ReportMessage( GlobalFormatText( ERR_START_NOT_MOVE_FILE, dwError, strMessage.c_str()).c_str() );
				return NULL;
			}
		}
	}
	
	static TCHAR szApplicationName[MAX_PATH] = {0};
	_tcscpy_s( szApplicationName, MAX_PATH, szLauncherFolderAbs );
	_tcscat_s( szApplicationName, MAX_PATH, szLauncherName );

	if ( !PathFileExists( szApplicationName ) )
	{
		ReportMessage( GlobalFormatText( ERR_START_MISSING_FILE, szApplicationName).c_str() );
		return NULL;
	}
	else
	{
		return szApplicationName;
	}
}


//
// 备份launcher版本到幻像目录中
// 
// 策略如下:
// Patcher 为旧版本或当前唯一版本.
// A. 如果存在Patcher.1，那么Patcher即为旧版本，Patcher.1为新版本
// B. 否则Patcher即为唯一版本.
// 在情况A下，我们需要检查Patcher目录和Patcher.1目录，
//		如果Patcher目录有效，那么复制Patcher目录到Patcher.1中。
//			（这种情况下，要么Patcher目录和Patcher.1一样，要么Patcher已被更新）
//		如果Patcher目录无效，
//			如果Patcher.1目录无效，启动失败！
//			如果Patcher.1目录有效，复制Patcher.1目录到Patcher目录中。
//				(这用于恢复更新Patcher失败的情况的特殊处理。)
// 在情况B下，如果Patcher目录有效，那么复制Patcher目录到Patcher.1中。
// 
// Patcher.1为Patcher程序的启动目录。
// Patcher目录游戏客户端原始安装目录，同时也是更新程序从服务器上下载的新Patcher的目录。
// 这种方案不许要对更新程序的更新逻辑做特殊处理，
// 即所有Patcher的新程序都将放置在Patcher目录下，而非Patcher.1目录下
//
// 成功时返回Patcher.exe的全路径
//
LPCTSTR SnapshotVersion()
{
	TCHAR szLauncherFolderAbsOld[MAX_PATH];
	TCHAR szLauncherFolderAbsNew[MAX_PATH];
	TCHAR szLauncherFolderOld[] = TEXT("patcher");
	TCHAR szLauncherFolderNew[] = TEXT("patcher.1");

#ifdef _DEBUG
	TCHAR szLauncherName[] = TEXT("\\Patcherd.exe");
#else
	TCHAR szLauncherName[] = TEXT("\\Patcher.exe");
#endif

	_tcscpy_s( szLauncherFolderAbsOld, MAX_PATH, GetInstallDir());
	_tcscat_s( szLauncherFolderAbsOld, MAX_PATH, szLauncherFolderOld );

	_tcscpy_s( szLauncherFolderAbsNew, MAX_PATH, GetInstallDir());
	_tcscat_s( szLauncherFolderAbsNew, MAX_PATH, szLauncherFolderNew );

	BOOL bOld = PathFileExists( szLauncherFolderAbsOld );
	BOOL bNew = PathFileExists( szLauncherFolderAbsNew );
	if ( !bOld && !bNew )
	{
		ReportMessage( GlobalFormatText( ERR_START_MISSING_FILE, szLauncherFolderAbsOld).c_str() );
		return NULL;
	}

	if( !SanityCheck( szLauncherFolderAbsOld ) )
	{
		if( !bNew || 
			!SanityCheck( szLauncherFolderAbsNew ) || 
			!CopyFilesUnderFolder( szLauncherFolderAbsNew, szLauncherFolderAbsOld ) )
		{
			ReportMessage( GlobalFormatText( ERR_START_FILE_DAMAGE, szLauncherFolderAbsOld).c_str() );
			return NULL;
		}
	}
	
	if( !bNew )
	{
		// 如果目标目录不存在，我们必须手动创建一个，否则往一个不存在的目录中CopyFile会失败
		if( !CreateDirectory( szLauncherFolderAbsNew, NULL ) )
		{
			DWORD dwError = GetLastError();
			std::tstring strMessage;
			FormatErrorMessage(dwError, &strMessage, szLauncherFolderAbsNew );
			ReportMessage( GlobalFormatText( ERR_START_NOT_CREATE_FILE, dwError, strMessage.c_str()).c_str() );
			return NULL;
		}
	}
	// 将 patcher.1 目录设为隐藏
	::SetFileAttributes( szLauncherFolderAbsNew, FILE_ATTRIBUTE_HIDDEN);
	
	if( !CopyFilesUnderFolder( szLauncherFolderAbsOld, szLauncherFolderAbsNew) )
	{
		DWORD dwError = GetLastError();
		if( dwError != 0 )
		{
			// 错误已经报告
			std::tstring strMessage;
			FormatErrorMessage(dwError, &strMessage, szLauncherFolderAbsOld, szLauncherFolderAbsNew );
			ReportMessage( GlobalFormatText( ERR_START_NOT_BACKUP_FILE, strMessage.c_str(), dwError).c_str() );
		}

		return NULL;
	}

	static TCHAR szApplicationName[MAX_PATH] = {0};
	_tcscpy_s( szApplicationName, MAX_PATH, szLauncherFolderAbsNew );
	_tcscat_s( szApplicationName, MAX_PATH, szLauncherName );

	if ( !PathFileExists( szApplicationName ) )
	{
		ReportMessage( GlobalFormatText( ERR_START_MISSING_FILE, szApplicationName).c_str() );
		return NULL;
	}
	else
	{
		return szApplicationName;
	}
}

//
// 备份launcher中的patcher程序到幻像目录中
// 
// 策略如下:
// launcher目录中的部分文件(inetutil.dll,patcher.exe,repair.exe, 统称为Patcher程序)为旧版本或当前唯一版本.
// A. 如果存在Patcher.1，那么Patcher程序即为旧版本，Patcher.1为新版本
// B. 否则Patcher程序即为唯一版本.
// 在情况A下，我们需要检查Patcher程序和Patcher.1目录，
//		如果Patcher程序有效，那么复制Patcher程序到Patcher.1中。
//			（这种情况下，要么Patcher和Patcher.1一样，要么Patcher已被更新）
//		如果Patcher程序无效，
//			如果Patcher.1目录无效，启动失败！
//			如果Patcher.1目录有效，复制Patcher.1目录到Launcher中。
//				(这用于恢复更新Patcher程序失败的情况的特殊处理。)
// 在情况B下，如果Patcher程序有效，那么复制Patcher程序到Patcher.1中。
// 
// Patcher.1为Patcher程序的启动目录。
// Launcher目录游戏客户端原始安装目录，同时也是更新程序从服务器上下载的新Launcher(包含新Patcher程序)的目录。
// 这种方案不许要对更新程序的更新逻辑做特殊处理，
// 即所有Patcher的新程序都将放置在Launcher目录下，而非Patcher.1目录下
//
// 成功时返回Patcher.exe的全路径
//
LPCTSTR SnapshotPatcherFiles()
{
	TCHAR szLauncherFolderAbsOld[MAX_PATH];
	TCHAR szLauncherFolderAbsNew[MAX_PATH];
	TCHAR szLauncherFolderOld[MAX_PATH];
	TCHAR szLauncherFolderNew[] = TEXT("patcher.1");

	_tcscpy_s( szLauncherFolderOld, MAX_PATH, GetLauncherFolder() );

#ifdef _DEBUG
	TCHAR szLauncherName[] = TEXT("\\Patcherd.exe");
#else
	TCHAR szLauncherName[] = TEXT("\\Patcher.exe");
#endif

	_tcscpy_s( szLauncherFolderAbsOld, MAX_PATH, GetInstallDir());
	_tcscat_s( szLauncherFolderAbsOld, MAX_PATH, szLauncherFolderOld );

	_tcscpy_s( szLauncherFolderAbsNew, MAX_PATH, GetInstallDir());
	_tcscat_s( szLauncherFolderAbsNew, MAX_PATH, szLauncherFolderNew );

	BOOL bOld = PathFileExists( szLauncherFolderAbsOld );
	BOOL bNew = PathFileExists( szLauncherFolderAbsNew );
	if ( !bOld && !bNew )
	{
		ReportMessage( GlobalFormatText( ERR_START_MISSING_FILE, szLauncherFolderAbsOld).c_str() );
		return NULL;
	}

	if( !SanityCheck( szLauncherFolderAbsOld ) )
	{
		if( !bNew || 
			!SanityCheck( szLauncherFolderAbsNew ) || 
			!CopyFilesUnderFolder( szLauncherFolderAbsNew, szLauncherFolderAbsOld ) )
		{
			ReportMessage( GlobalFormatText( ERR_START_FILE_DAMAGE, szLauncherFolderAbsOld).c_str() );
			return NULL;
		}
	}

	if( !bNew )
	{
		// 如果目标目录不存在，我们必须手动创建一个，否则往一个不存在的目录中CopyFile会失败
		if( !CreateDirectory( szLauncherFolderAbsNew, NULL ) )
		{
			DWORD dwError = GetLastError();
			std::tstring strMessage;
			FormatErrorMessage(dwError, &strMessage, szLauncherFolderAbsNew );
			ReportMessage( GlobalFormatText( ERR_START_NOT_CREATE_FILE, dwError, strMessage.c_str()).c_str() );
			return NULL;
		}
	}
	// 将 patcher.1 目录设为隐藏
	::SetFileAttributes( szLauncherFolderAbsNew, FILE_ATTRIBUTE_HIDDEN);

	if( !CopyFilesUnderFolder( szLauncherFolderAbsOld, szLauncherFolderAbsNew) )
	{
		DWORD dwError = GetLastError();
		if( dwError != 0 )
		{
			// 错误已经报告
			std::tstring strMessage;
			FormatErrorMessage(dwError, &strMessage, szLauncherFolderAbsOld, szLauncherFolderAbsNew );
			ReportMessage( GlobalFormatText( ERR_START_NOT_BACKUP_FILE, strMessage.c_str(), dwError).c_str() );
		}

		return NULL;
	}

	static TCHAR szApplicationName[MAX_PATH] = {0};
	_tcscpy_s( szApplicationName, MAX_PATH, szLauncherFolderAbsNew );
	_tcscat_s( szApplicationName, MAX_PATH, szLauncherName );

	if ( !PathFileExists( szApplicationName ) )
	{
		ReportMessage( GlobalFormatText( ERR_START_MISSING_FILE, szApplicationName).c_str() );
		return NULL;
	}
	else
	{
		return szApplicationName;
	}
}

//
// 运行可执行程序
//
BOOL RunExecutable( LPCTSTR szApplicationName, LPCTSTR cmdline)
{
	STARTUPINFO startupInfo = {0};
	startupInfo.cb = sizeof(STARTUPINFO);
	startupInfo.dwFlags = STARTF_USESHOWWINDOW;
	// Activates the window and displays it in its current size and position.
	startupInfo.wShowWindow = SW_SHOW;

	PROCESS_INFORMATION processInfo = {0}; 

	TCHAR szPath[MAX_PATH + 1];
	TCHAR szPath2[MAX_PATH + 1];
	int i = 0;
	do
	{
		if( szApplicationName[i] == L'/' )
			szPath2[i] = L'\\';
		else
			szPath2[i] = szApplicationName[i];
	}
	while( szApplicationName[i ++ ] );

	PathCanonicalize( szPath, szPath2 );
	PathQuoteSpaces( szPath );
	
	if( cmdline)
	{
		_tcscat_s( szPath, L" ");
		_tcscat_s( szPath, cmdline);
	}

	BOOL bRet = CreateProcess( NULL, szPath,
		NULL, NULL, FALSE, 0, 0, 0, &startupInfo, &processInfo );
	if ( bRet )
	{
		CloseHandle( processInfo.hThread );
		CloseHandle( processInfo.hProcess );
	}

	return bRet;
}

//
// 启动游戏的Patcher程序
//
BOOL RunLauncher( LPCTSTR cmdline )
{
	KHMsgLog_Debug( _T("Set commandLine to patcher: %s\n"), cmdline );

	LPCTSTR szApplicationName = SnapshotPatcherFiles();
	if( szApplicationName == NULL )
	{
		// 已经报告错误
		return FALSE;
	}

	if( !RunExecutable( szApplicationName, cmdline) )
	{
		DWORD dwError = GetLastError();
		std::tstring strMessage;
		FormatErrorMessage(dwError, &strMessage, szApplicationName);
		ReportMessage( GlobalFormatText( ERR_LOAD_PROGRAME, szApplicationName, dwError, strMessage.c_str()).c_str() );
		return FALSE;
	}

	return TRUE;
}

//
// 判断进程是否与Launcher为同一份安装版
//
BOOL IsSameInstallationProcess( HANDLE hProcess )
{
	TCHAR PathBuffer[MAX_PATH] = {0};
	GetModuleFileNameEx(hProcess, NULL, PathBuffer, MAX_PATH );

	LPCTSTR lpszCurrentInstallDir = GetInstallDir();
	static int nDirLen = lstrlen( lpszCurrentInstallDir );
	
	PathBuffer[nDirLen] = TEXT('\0');
	if( 0 == lstrcmpi( lpszCurrentInstallDir, PathBuffer ) )
	{
		return TRUE;
	}

	// TODO: 是否有必要使用 PathCommonPrefix() 确定路径？
	return FALSE;
}

//
// 判断窗口是否与Launcher为同一份安装版
//
BOOL IsSameInstallationWindow( HWND hWnd )
{
	// 获取窗口的进程ID
	DWORD dwProcessId = 0;
	::GetWindowThreadProcessId( hWnd, &dwProcessId );

	// 打开进程
	HANDLE hProcess = OpenProcess( PROCESS_QUERY_INFORMATION|PROCESS_VM_READ, FALSE, dwProcessId );
	if ( hProcess == NULL )
	{
		return FALSE;
	}

	// 判断进程是否与launcher.exe在同一个安装目录下
	BOOL bIsSameInstallation = IsSameInstallationProcess( hProcess );
	CloseHandle( hProcess );
	return bIsSameInstallation;
}

LPCTSTR g_reg = L"HKCR\n\
{\n\
	NoRemove .%EXTENSION% = s 'MZBootstrap.%EXTENSION%'\n\
	{\n\
	}\n\
	ForceRemove MZBootstrap.%EXTENSION%\n\
	{\n\
		DefaultIcon = s '%MODULE%,0'\n\
		shell\n\
		{\n\
			open\n\
			{\n\
				command = s '%MODULE% -patch \"%%1\"'\n\
				val MuiVerb = s '@%MODULE%,-102'\n\
			}\n\
		}\n\
	}\n\
}";

LPCTSTR GetExtension()
{
	static wchar_t buffer[MAX_PATH] = {0};

	wchar_t file[MAX_PATH] = {0};
	swprintf_s( file, MAX_PATH, L"%s\\launcher.ini", GetModuleDir());
	GetPrivateProfileString( L"setting", L"extension", L"mzpch", buffer, MAX_PATH, file);
	return buffer;
}

LPCTSTR GetExtension2()
{
	static wchar_t buffer[MAX_PATH] = {0};
	wchar_t file[MAX_PATH] = {0};
	swprintf_s( file, MAX_PATH, L"%s\\launcher.ini", GetModuleDir());
	GetPrivateProfileString( L"setting", L"extension2", L"", buffer, MAX_PATH, file);
	return buffer;
}

// returns one of the following code:
//# IDOK        1 - Register/Unregister OK
//# IDCANCEL	2 - Register/Unregister FAILED and cancelled by user.
//# IDIGNORE	5 - Register OK or Register FAILED and not be forced with (-r) switch argument.
int HandleRegisterImpl(LPCTSTR pszRegTemplate, 
						LPCTSTR pszModulePath, 
						LPCTSTR pszExtension, 
						bool bRegister,
						bool force,
						bool bWriteLog)
{
	// 使用ATL的对象完成REGISTRY资源文件的注册
	ATL::CRegObject ro;
	if( SUCCEEDED( ro.FinalConstruct() ) )
	{
		ro.AddReplacement( L"MODULE", pszModulePath);
		ro.AddReplacement( L"EXTENSION", pszExtension);
	}

	for( ;; )
	{
		// ISSUE:
		// 玩家不是以管理员身份登录时，下面的注册表写操作会失败。
		// 这里判断错误码，并弹出错误对话框提示用户。
		// 简单测试方案：改写 g_reg 的HKCR为非法值即可看到错误对话框的效果。
		HRESULT hr = S_OK;
		if( bRegister )
			ro.StringRegister( pszRegTemplate);
		else
			ro.StringUnregister( pszRegTemplate);

		if( !SUCCEEDED( hr ) )
		{
			std::tstring strDetail;
			FormatErrorMessage(HRESULT_CODE(hr), &strDetail);

			if( bWriteLog )
			{
				KHMsgLog_Error( _T("MODULE: %s\n"), pszModulePath );
				KHMsgLog_Error( _T("EXTENSION: %s\n"), pszExtension );
				KHMsgLog_Error( (strDetail + _T("\n")).c_str() );
			}

			if( force )
			{
				int iChoice = ::MessageBox( NULL, 
					GlobalFormatText( ERR_START_OPERATE_REGISTER, strDetail.c_str(), HRESULT_CODE(hr)).c_str(), 
					GlobalGetText( TIP_ERROR).c_str(), 
					MB_ABORTRETRYIGNORE|MB_ICONEXCLAMATION );

				if ( iChoice == IDABORT )
				{
					return IDABORT;
				}
				else if ( iChoice == IDRETRY )
				{
					continue;
				}
			}
			else
			{
				// Program should ignore the function result.
				return IDIGNORE;
			}
		}

		if( bWriteLog )
		{
			if( bRegister )
				KHMsgLog_Debug( _T("The registry was registered successfully!\n") );
			else
				KHMsgLog_Debug( _T("The registry was unregistered successfully!\n") );

			KHMsgLog_Debug( _T("MODULE: %s\n"), pszModulePath );
			KHMsgLog_Debug( _T("EXTENSION: %s\n"), pszExtension );
		}

		return IDOK;

	} // for(;;)
}

bool CheckContainsSimpleSwitch( LPTSTR lpCmdLine, LPTSTR szFlag )
{
	std::tstring flagSlash(_T("/"));
	flagSlash.append(szFlag);

	std::tstring flagDash(_T("-"));
	flagDash.append(szFlag);

	std::tstring strCommandLine;
	strCommandLine.assign(lpCmdLine);
	std::tstring::size_type nPos = 0;
	std::tstring::size_type nNext = 0;
	do
	{
		nPos = strCommandLine.find(flagSlash, nNext);
		if( nPos == std::tstring::npos )
		{
			nPos = strCommandLine.find(flagDash);
			if( nPos == std::tstring::npos )
				return false;
		}

		nNext = nPos + flagDash.size();

		if( nPos > 0 && !_istspace( strCommandLine.at( nPos - 1 ) ) )
		{
			continue;
		}

		break;

	}while(1);

	if( nNext == strCommandLine.size() ||
		_istspace( strCommandLine.at( nPos + flagDash.size() ) ) )
	{
		return true;
	}

	return false;
}

bool CheckContainsSwitch( LPTSTR lpCmdLine, LPTSTR szFlag )
{
	size_t nLen =  _tcslen( szFlag );
	if( nLen == 0 )
	{
		return false;
	}

	if( nLen  == 1 )
	{
		return CheckContainsSimpleSwitch(lpCmdLine, szFlag );
	}
	
	// Check long switch argument.

	std::tstring flagDoubleDash(_T("--"));
	flagDoubleDash.append(szFlag);

	std::tstring strCommandLine;
	strCommandLine.assign(lpCmdLine);

	std::tstring::size_type nPos = 0;
	std::tstring::size_type nNext = 0;

	do
	{
		nPos = strCommandLine.find(flagDoubleDash, nNext);
		if( nPos == std::tstring::npos )
		{
			return false;
		}

		nNext = nPos + flagDoubleDash.size();

		if( nPos > 0 && !_istspace( strCommandLine.at( nPos - 1 ) ) )
		{
			continue;
		}

		break;

	}while(1);

	if( nNext == strCommandLine.size() ||
		_istspace( strCommandLine.at( nPos + flagDoubleDash.size() ) ) )
	{
		return true;
	}

	return false;
}

// returns one of the following code:
//# IDOK        1 - Register/Unregister OK
//# IDCANCEL	2 - Register/Unregister FAILED and cancelled by user.
//# IDIGNORE	5 - Register OK or Register FAILED and not be forced with (-r) switch argument.
int HandleRegister( LPTSTR lpCmdLine )
{
	// 下面的方法用于得到程序路径
	TCHAR szPath[ MAX_PATH] = {0};
	::GetModuleFileName( NULL, szPath, MAX_PATH);

	// 使用ATL的对象完成REGISTRY资源文件的注册
	LPCTSTR pszExtension = GetExtension();
	// 删除注册信息 - 卸载时使用。
	if( CheckContainsSimpleSwitch( lpCmdLine, _T("u") ) )
	{
		int iRet = HandleRegisterImpl( g_reg, szPath, pszExtension, false, true, true );
		if( iRet == IDOK || iRet == IDIGNORE )
		{	
			LPCTSTR pszExtension2 = GetExtension2();
			if( lstrlenW(pszExtension2) != 0 )
				iRet = HandleRegisterImpl( g_reg, szPath, pszExtension2, false, false, true );
		}

		return iRet;
	}

	// 注册
	bool bForce = CheckContainsSimpleSwitch( lpCmdLine, _T("r") );
	int iRet = HandleRegisterImpl( g_reg, szPath, pszExtension, true, bForce, bForce );
	if( iRet == IDOK || iRet == IDIGNORE )
	{
		LPCTSTR pszExtension2 = GetExtension2();
		if( lstrlenW(pszExtension2) != 0 )
			iRet = HandleRegisterImpl( g_reg, szPath, pszExtension2, true, false, bForce );
	}

	// Program should be ignoring the function processing result.
	if( bForce )
		return iRet;
	else
		return IDIGNORE;
}

//
// 使用WinMain而不是用main的原因是，避免console窗口出现
//
int WINAPI _tWinMain(
					 __in HINSTANCE hInstance, 
					 __in_opt HINSTANCE hPrevInstance, 
					 __in_opt LPTSTR lpCmdLine, 
					 __in int nShowCmd )
{
	std::wstring strModuleDir = GetInstallDir();
	strModuleDir += L"log\\";
	KHMsgLog_SetFilePathW( strModuleDir.c_str() );

	std::wstring strLauncherResource = GetModuleDir();
	strLauncherResource += L"\\launcher\\text\\launcher.xml";

	// 获取用户缺省UI语言
	LANGID langId = GetUserDefaultUILanguage();
	WORD prilangId = PRIMARYLANGID(langId); // 主语言
	// 简体中文语言ID
	LANGID s_chineseLangId = MAKELANGID(LANG_CHINESE, SUBLANG_CHINESE_SIMPLIFIED);

	int iChoice;
	do 
	{
		iChoice = IDOK;
		// 装载文本资源
		// 因为patcher和repair公用很多文本资源，所以将patcher和repair文本资源放在同一个文件
		if( !CTextManager::GetInstance().Init( CTextManager::FILE_XML, strLauncherResource ))
		{
			std::wstring strErrorMessage;
			strErrorMessage = GetApplicationFileName();
			if( langId == s_chineseLangId ) // 简体中文版用户UI
			{
				strErrorMessage += L"启动异常！\n文本资源文件丢失或不能被解析！\n请根据指定的文件路径确认该文件所存在的问题：\n\n    ";
			}
			else if ( prilangId == LANG_CHINESE ) // 繁体中文版用户UI
			{
				strErrorMessage += L"啟動異常！\n文本資源文件丟失或不能被解析！\n請根據指定的文件路徑確認該文件所存在的問題：\n\n    ";
			}
			else
			{
				strErrorMessage += L"Exception occurred while startuping!\nThe text resource file is missing or could not be resolved!\nPlease check it manually upon the following path:\n\n    ";
			}
			strErrorMessage += strLauncherResource;

			KHMsgLog_ErrorW( (strErrorMessage + L"\n").c_str() );
			iChoice = MessageBox( NULL, strErrorMessage.c_str(), GetApplicationFileName(), MB_ABORTRETRYIGNORE | MB_ICONWARNING );
			// 对于本程序来说，缺少文本资源可以允许用户继续执行
			if( iChoice == IDABORT )
			{
				return -1;
			}
			// else continue to run;
		}

		// 允许用户重试
	} while( iChoice == IDRETRY );
	

	KHMsgLog_Debug( _T("CommandLine: %s\n"), lpCmdLine );

	LPTSTR pStr = _tcsstr( lpCmdLine, _T("--reboot-launcher ") );
	if( pStr == NULL )
		pStr = _tcsstr( lpCmdLine, _T("-reboot-launcher ") );
	if ( pStr !=  NULL )
	{
		KHMsgLog_Debug( _T("-reboot-launcher\n"));

		pStr = _tcsstr( pStr, _T("-pid "));
		if ( pStr != NULL )
		{
			DWORD dwProcessId = _ttoi( pStr + 5 );
			
			HANDLE hProcess = OpenProcess( SYNCHRONIZE|PROCESS_TERMINATE, FALSE, dwProcessId );
			KHMsgLog_Debug( _T("pid: %u hProcess=0x%08x\n"), dwProcessId, hProcess);
			if ( hProcess != NULL )
			{
				// 等待launcher.exe退出
				while( true)
				{
					DWORD res = WaitForSingleObject( hProcess, 10000 );
					if( WAIT_OBJECT_0 == res || WAIT_ABANDONED == res || res == WAIT_FAILED )
					{
						if( WAIT_OBJECT_0 == res )
							KHMsgLog_Info( _T("WaitForSingleObject(%x): %d WAIT_OBJECT_0\n"), hProcess, res );
						else if(WAIT_ABANDONED == res )
							KHMsgLog_Error( _T("WaitForSingleObject(%x): %d WAIT_ABANDONED\n"), hProcess, res );
						else if ( res == WAIT_FAILED )
						{
							DWORD dwLastError = GetLastError();
							KHMsgLog_Error( _T("WaitForSingleObject(%x): %d WAIT_FAILED LastError %d \n"), hProcess, res, dwLastError );
						}
						break;
					}

					KHMsgLog_Debug( _T("WaitForSingleObject(%x): %d\n"), hProcess, res );

					TerminateProcess( hProcess, 2);

					int sel = ::MessageBox( NULL, GlobalGetText( QST_OLD_PATCHER_NOT_EXIT).c_str(), 
						GlobalGetText( TIP_TIP).c_str(), MB_RETRYCANCEL|MB_ICONINFORMATION );
					if( IDCANCEL == sel)
					{
						return 0;
					}
				}
				CloseHandle( hProcess );
			}
			else
			{
				DWORD dwError = GetLastError();
				KHMsgLog_Error( _T("Error: %d(0x%08x)\n"), dwError,  dwError );
				if ( dwError == ERROR_ACCESS_DENIED )
				{
					::MessageBox( NULL, lpCmdLine, GlobalGetText( ERR_OPEN_PROCESS).c_str(), MB_OK|MB_ICONINFORMATION );
					return 0;
				}
			}

			// 传递剩余的命令行参数
			LPCTSTR pStrCmdLine = _tcsstr( pStr+6, _T("-"));
			if ( pStrCmdLine == NULL )
			{
				pStrCmdLine = _tcsstr( pStr+6, _T(" "));
			}

			const _TCHAR* pStrStart = _tcsstr( pStrCmdLine, L"--launcher-folder \"" );
			if ( pStrStart == NULL )
			{
				// 附加 --launcher-folder 参数，保证版本过渡时更新顺利
				LPCTSTR szLauncherFolder = GetLauncherFolder();
				TCHAR CommandLineBuffer[MAX_PATH] = {0};
				_tcscat_s( CommandLineBuffer, _T("--launcher-folder \""));
				_tcscat_s( CommandLineBuffer, szLauncherFolder);
				_tcscat_s( CommandLineBuffer, _T("\" "));
				_tcscat_s( CommandLineBuffer, pStrCmdLine );

				RunLauncher( CommandLineBuffer );
			}
			else
			{
				RunLauncher( pStrCmdLine );
			}

			return 0;
		}
	}
	
	int iRet = HandleRegister( lpCmdLine );
	if( iRet == IDOK)
	{
		return 0;
	}
	else if( iRet == IDCANCEL )
	{
		return 1;
	}
	// hence iRet == IDIGNORE

	//////////////////////////////////////////////////////////////////////////
	//
	// 这里通过窗口名确定单实例启动Patcher程序(不区分Debug/Release版本)
	//
	// 如果下面的代码需要修改，请注意保持与Patcher.cpp保持同步。
	// 
	// IDC_CHECK_UPDATE 也要和Patcher程序保持一致
	//////////////////////////////////////////////////////////////////////////
	HWND hWndPrev = NULL, hWndChild = NULL;

	// 注：IDS_LAUNCHERNAME 所指示的字符串必须与Patcher中的IDR_MAINFRAME相同
	TCHAR strAppName[256] = {0};

	const std::wstring& strPatcherName = GlobalGetText( TOK_PRODUCT_PATCHER );
	if(!strPatcherName.empty())
		strPatcherName._Copy_s( strAppName, 256, 256 );
	else
		::LoadString( hInstance, IDS_LAUNCHERNAME, strAppName, _countof(strAppName));

	// Determine if a window with the class name exists...
	while( hWndPrev = ::FindWindowEx(NULL, hWndChild, _T("#32770"), strAppName) )
	{
		// Determine if the window same as my installation
		if( !IsSameInstallationWindow( hWndPrev ) )
		{
			hWndChild = hWndPrev;
			continue;
		}

		// If so, does it have any popups?
		hWndChild = ::GetLastActivePopup(hWndPrev);

		// If iconic, restore the main window
		if (::IsIconic(hWndPrev))
			::ShowWindow(hWndPrev, SW_RESTORE);

		// If hidden, show the main window
		if ( !::IsWindowVisible(hWndPrev) )
			::ShowWindow(hWndPrev, SW_SHOW );

		// Bring the main window or its popup to the foreground
		::SetForegroundWindow(hWndChild);

		int retrytimes = 0;
		while( true)
		{
			// 如果launcher已存在则开始更新
			BOOL res = ::PostMessage( hWndPrev, WM_COMMAND, IDC_CHECK_UPDATE, 0);
			if( res)
				break;
			if( retrytimes++ >= 3)	// 重试3次，保证 PostMessage 成功
				break;
			Sleep( 100);
		} 
		// and you are done activating the other application
		return 0;
	}

	pStr = _tcsstr( lpCmdLine, _T("-patch "));
	if( pStr != NULL )
	{
		RunLauncher( lpCmdLine );
		return 0;
	}

	// 下面的代码用于在commandline中插入 
	// --launcher-folder "launcher" 参数 或
	// --launcher-folder "launcher_tiyan" 参数
	LPCTSTR szLauncherFolder = GetLauncherFolder();
	TCHAR CommandLineBuffer[MAX_PATH] = {0};
	_tcscat_s( CommandLineBuffer, _T("--launcher-folder \""));
	_tcscat_s( CommandLineBuffer, szLauncherFolder);
	_tcscat_s( CommandLineBuffer, _T("\" "));
	
	// 下面的方法用于得到程序路径
	TCHAR szPath[ MAX_PATH] = {0};
	::GetModuleFileName( NULL, szPath, MAX_PATH);

	// 检查命令行参数中带有.mzpch文件
	LPCTSTR pszExtension = GetExtension();	
	swprintf_s( szPath, MAX_PATH, L".%s", pszExtension);
	LPTSTR pPatchExtStr = _tcsstr( lpCmdLine, szPath );
	if ( pPatchExtStr !=  NULL )
	{
		// 插入-patch 
		_tcscat_s( CommandLineBuffer, _T("-patch "));
		_tcscat_s( CommandLineBuffer, lpCmdLine);

		RunLauncher( CommandLineBuffer );
	}
	else
	{
		// 没有.mzpch参数，直接执行缺省参数的launcher
		_tcscat_s( CommandLineBuffer, lpCmdLine);
		RunLauncher( CommandLineBuffer );
	}

	return 0;
}
