// Bootstrap.cpp
// 
// ��Ϸ�Ծٳ���
//
// author : yanghaibo
// date : 2008-12-01 
//
// ��ģ��ʵ�������ַ����������������ֱ�μ�����2��������ע��˵���ĵ���
//		ChooseVersion()
//		SnapshotVersion()
// ��������ʹ��SnapshotVersion()����������Լ򻯸����߼���
//
// author : yanghaibo
// date : 2008-12-17
//
// ��ģ��ʵ�������ַ����������������ֱ�μ�����3��������ע��˵���ĵ���
//		ChooseVersion()
//		SnapshotVersion()
//		SnapshotPatcherFiles()
// ��������ʹ��SnapshotPatcherFiles()����������Լ򻯶�汾�����߼���
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
// ��ȡ���г��������ļ���
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
// ��ȡ���г���Launcher����Ŀ¼
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

// Launcher ����λ��install directory ��bin/binDĿ¼��
// ��ˣ�InstallDirΪModuleDir�ĸ�Ŀ¼
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

// ��ȡlauncher�Լ����ڵ��ļ�������
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
// ��ʽ��ϵͳAPI�����Ĵ�����Ϣ
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
// ���������Ϣ
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
// ɾ��ָ����Ŀ¼�Լ�Ŀ¼�µ������ļ�
//
bool DeleteFolder( std::wstring strFolder )
{
	// ���Ǳ��뱣֤�ַ��������2��0��β
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
				KHMsgLog_Error( _T("������������з�������. �����ļ�ʧ��(%d): %s -> %s %s\n!"),
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
// Copyָ����Ŀ¼�µ������ļ���Ŀ��Ŀ¼
//
bool CopyFilesUnderFolder( std::wstring strFolderSrc, std::wstring strFolderDst )
{
	// ���Ǳ��뱣֤�ַ��������2��0��β
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
// Moveָ����Ŀ¼�µ������ļ���Ŀ��Ŀ¼
//
bool MoveFilesUnderFolder( std::wstring strFolderSrc, std::wstring strFolderDst )
{
	// ���Ǳ��뱣֤�ַ��������2��0��β
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
//  ���汾Ŀ¼�Ƿ�������Ч
//
BOOL SanityCheck( LPCTSTR lpszVersionFolder )
{
	//
	// TODO: ����°汾Ŀ¼�Ƿ�������Ч
	//
	return TRUE;
}

//
// ѡ��launcher�汾
// 
// ��������:
// Patcher Ϊ�ɰ汾��ǰΨһ�汾.
// A. �������Patcher.1����ôPatcher��Ϊ�ɰ汾��Patcher.1Ϊ�°汾
// B. ����Patcher��ΪΨһ�汾.
// �����A�£�������Ҫɾ��PatcherĿ¼������Patcher.1������ΪPatcher
// Ȼ������Ϊ���µ�Ψһ�汾��
// 
// Patcher.1ΪPatcher����ӷ����������صı��ݳ���
// ����Ҫ�Ը��³���ĸ����߼������⴦��
// ������Patcher���³��򶼽�������Patcher.1Ŀ¼�£�����PatcherĿ¼��
//
// �ɹ�ʱ����Patcher.exe��ȫ·��
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
		// ����°汾Ŀ¼�Ƿ�������Ч��ֻ�����������ǲ�ִ���滻�����򣬷����ð汾
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
				// ��ô��?
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
// ����launcher�汾������Ŀ¼��
// 
// ��������:
// Patcher Ϊ�ɰ汾��ǰΨһ�汾.
// A. �������Patcher.1����ôPatcher��Ϊ�ɰ汾��Patcher.1Ϊ�°汾
// B. ����Patcher��ΪΨһ�汾.
// �����A�£�������Ҫ���PatcherĿ¼��Patcher.1Ŀ¼��
//		���PatcherĿ¼��Ч����ô����PatcherĿ¼��Patcher.1�С�
//			����������£�ҪôPatcherĿ¼��Patcher.1һ����ҪôPatcher�ѱ����£�
//		���PatcherĿ¼��Ч��
//			���Patcher.1Ŀ¼��Ч������ʧ�ܣ�
//			���Patcher.1Ŀ¼��Ч������Patcher.1Ŀ¼��PatcherĿ¼�С�
//				(�����ڻָ�����Patcherʧ�ܵ���������⴦��)
// �����B�£����PatcherĿ¼��Ч����ô����PatcherĿ¼��Patcher.1�С�
// 
// Patcher.1ΪPatcher���������Ŀ¼��
// PatcherĿ¼��Ϸ�ͻ���ԭʼ��װĿ¼��ͬʱҲ�Ǹ��³���ӷ����������ص���Patcher��Ŀ¼��
// ���ַ�������Ҫ�Ը��³���ĸ����߼������⴦��
// ������Patcher���³��򶼽�������PatcherĿ¼�£�����Patcher.1Ŀ¼��
//
// �ɹ�ʱ����Patcher.exe��ȫ·��
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
		// ���Ŀ��Ŀ¼�����ڣ����Ǳ����ֶ�����һ����������һ�������ڵ�Ŀ¼��CopyFile��ʧ��
		if( !CreateDirectory( szLauncherFolderAbsNew, NULL ) )
		{
			DWORD dwError = GetLastError();
			std::tstring strMessage;
			FormatErrorMessage(dwError, &strMessage, szLauncherFolderAbsNew );
			ReportMessage( GlobalFormatText( ERR_START_NOT_CREATE_FILE, dwError, strMessage.c_str()).c_str() );
			return NULL;
		}
	}
	// �� patcher.1 Ŀ¼��Ϊ����
	::SetFileAttributes( szLauncherFolderAbsNew, FILE_ATTRIBUTE_HIDDEN);
	
	if( !CopyFilesUnderFolder( szLauncherFolderAbsOld, szLauncherFolderAbsNew) )
	{
		DWORD dwError = GetLastError();
		if( dwError != 0 )
		{
			// �����Ѿ�����
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
// ����launcher�е�patcher���򵽻���Ŀ¼��
// 
// ��������:
// launcherĿ¼�еĲ����ļ�(inetutil.dll,patcher.exe,repair.exe, ͳ��ΪPatcher����)Ϊ�ɰ汾��ǰΨһ�汾.
// A. �������Patcher.1����ôPatcher����Ϊ�ɰ汾��Patcher.1Ϊ�°汾
// B. ����Patcher����ΪΨһ�汾.
// �����A�£�������Ҫ���Patcher�����Patcher.1Ŀ¼��
//		���Patcher������Ч����ô����Patcher����Patcher.1�С�
//			����������£�ҪôPatcher��Patcher.1һ����ҪôPatcher�ѱ����£�
//		���Patcher������Ч��
//			���Patcher.1Ŀ¼��Ч������ʧ�ܣ�
//			���Patcher.1Ŀ¼��Ч������Patcher.1Ŀ¼��Launcher�С�
//				(�����ڻָ�����Patcher����ʧ�ܵ���������⴦��)
// �����B�£����Patcher������Ч����ô����Patcher����Patcher.1�С�
// 
// Patcher.1ΪPatcher���������Ŀ¼��
// LauncherĿ¼��Ϸ�ͻ���ԭʼ��װĿ¼��ͬʱҲ�Ǹ��³���ӷ����������ص���Launcher(������Patcher����)��Ŀ¼��
// ���ַ�������Ҫ�Ը��³���ĸ����߼������⴦��
// ������Patcher���³��򶼽�������LauncherĿ¼�£�����Patcher.1Ŀ¼��
//
// �ɹ�ʱ����Patcher.exe��ȫ·��
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
		// ���Ŀ��Ŀ¼�����ڣ����Ǳ����ֶ�����һ����������һ�������ڵ�Ŀ¼��CopyFile��ʧ��
		if( !CreateDirectory( szLauncherFolderAbsNew, NULL ) )
		{
			DWORD dwError = GetLastError();
			std::tstring strMessage;
			FormatErrorMessage(dwError, &strMessage, szLauncherFolderAbsNew );
			ReportMessage( GlobalFormatText( ERR_START_NOT_CREATE_FILE, dwError, strMessage.c_str()).c_str() );
			return NULL;
		}
	}
	// �� patcher.1 Ŀ¼��Ϊ����
	::SetFileAttributes( szLauncherFolderAbsNew, FILE_ATTRIBUTE_HIDDEN);

	if( !CopyFilesUnderFolder( szLauncherFolderAbsOld, szLauncherFolderAbsNew) )
	{
		DWORD dwError = GetLastError();
		if( dwError != 0 )
		{
			// �����Ѿ�����
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
// ���п�ִ�г���
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
// ������Ϸ��Patcher����
//
BOOL RunLauncher( LPCTSTR cmdline )
{
	KHMsgLog_Debug( _T("Set commandLine to patcher: %s\n"), cmdline );

	LPCTSTR szApplicationName = SnapshotPatcherFiles();
	if( szApplicationName == NULL )
	{
		// �Ѿ��������
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
// �жϽ����Ƿ���LauncherΪͬһ�ݰ�װ��
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

	// TODO: �Ƿ��б�Ҫʹ�� PathCommonPrefix() ȷ��·����
	return FALSE;
}

//
// �жϴ����Ƿ���LauncherΪͬһ�ݰ�װ��
//
BOOL IsSameInstallationWindow( HWND hWnd )
{
	// ��ȡ���ڵĽ���ID
	DWORD dwProcessId = 0;
	::GetWindowThreadProcessId( hWnd, &dwProcessId );

	// �򿪽���
	HANDLE hProcess = OpenProcess( PROCESS_QUERY_INFORMATION|PROCESS_VM_READ, FALSE, dwProcessId );
	if ( hProcess == NULL )
	{
		return FALSE;
	}

	// �жϽ����Ƿ���launcher.exe��ͬһ����װĿ¼��
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
	// ʹ��ATL�Ķ������REGISTRY��Դ�ļ���ע��
	ATL::CRegObject ro;
	if( SUCCEEDED( ro.FinalConstruct() ) )
	{
		ro.AddReplacement( L"MODULE", pszModulePath);
		ro.AddReplacement( L"EXTENSION", pszExtension);
	}

	for( ;; )
	{
		// ISSUE:
		// ��Ҳ����Թ���Ա��ݵ�¼ʱ�������ע���д������ʧ�ܡ�
		// �����жϴ����룬����������Ի�����ʾ�û���
		// �򵥲��Է�������д g_reg ��HKCRΪ�Ƿ�ֵ���ɿ�������Ի����Ч����
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
	// ����ķ������ڵõ�����·��
	TCHAR szPath[ MAX_PATH] = {0};
	::GetModuleFileName( NULL, szPath, MAX_PATH);

	// ʹ��ATL�Ķ������REGISTRY��Դ�ļ���ע��
	LPCTSTR pszExtension = GetExtension();
	// ɾ��ע����Ϣ - ж��ʱʹ�á�
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

	// ע��
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
// ʹ��WinMain��������main��ԭ���ǣ�����console���ڳ���
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

	// ��ȡ�û�ȱʡUI����
	LANGID langId = GetUserDefaultUILanguage();
	WORD prilangId = PRIMARYLANGID(langId); // ������
	// ������������ID
	LANGID s_chineseLangId = MAKELANGID(LANG_CHINESE, SUBLANG_CHINESE_SIMPLIFIED);

	int iChoice;
	do 
	{
		iChoice = IDOK;
		// װ���ı���Դ
		// ��Ϊpatcher��repair���úܶ��ı���Դ�����Խ�patcher��repair�ı���Դ����ͬһ���ļ�
		if( !CTextManager::GetInstance().Init( CTextManager::FILE_XML, strLauncherResource ))
		{
			std::wstring strErrorMessage;
			strErrorMessage = GetApplicationFileName();
			if( langId == s_chineseLangId ) // �������İ��û�UI
			{
				strErrorMessage += L"�����쳣��\n�ı���Դ�ļ���ʧ���ܱ�������\n�����ָ�����ļ�·��ȷ�ϸ��ļ������ڵ����⣺\n\n    ";
			}
			else if ( prilangId == LANG_CHINESE ) // �������İ��û�UI
			{
				strErrorMessage += L"���Ӯ�����\n�ı��YԴ�ļ��Gʧ���ܱ�������\nՈ����ָ�����ļ�·���_�Jԓ�ļ������ڵĆ��}��\n\n    ";
			}
			else
			{
				strErrorMessage += L"Exception occurred while startuping!\nThe text resource file is missing or could not be resolved!\nPlease check it manually upon the following path:\n\n    ";
			}
			strErrorMessage += strLauncherResource;

			KHMsgLog_ErrorW( (strErrorMessage + L"\n").c_str() );
			iChoice = MessageBox( NULL, strErrorMessage.c_str(), GetApplicationFileName(), MB_ABORTRETRYIGNORE | MB_ICONWARNING );
			// ���ڱ�������˵��ȱ���ı���Դ���������û�����ִ��
			if( iChoice == IDABORT )
			{
				return -1;
			}
			// else continue to run;
		}

		// �����û�����
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
				// �ȴ�launcher.exe�˳�
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

			// ����ʣ��������в���
			LPCTSTR pStrCmdLine = _tcsstr( pStr+6, _T("-"));
			if ( pStrCmdLine == NULL )
			{
				pStrCmdLine = _tcsstr( pStr+6, _T(" "));
			}

			const _TCHAR* pStrStart = _tcsstr( pStrCmdLine, L"--launcher-folder \"" );
			if ( pStrStart == NULL )
			{
				// ���� --launcher-folder ��������֤�汾����ʱ����˳��
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
	// ����ͨ��������ȷ����ʵ������Patcher����(������Debug/Release�汾)
	//
	// �������Ĵ�����Ҫ�޸ģ���ע�Ᵽ����Patcher.cpp����ͬ����
	// 
	// IDC_CHECK_UPDATE ҲҪ��Patcher���򱣳�һ��
	//////////////////////////////////////////////////////////////////////////
	HWND hWndPrev = NULL, hWndChild = NULL;

	// ע��IDS_LAUNCHERNAME ��ָʾ���ַ���������Patcher�е�IDR_MAINFRAME��ͬ
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
			// ���launcher�Ѵ�����ʼ����
			BOOL res = ::PostMessage( hWndPrev, WM_COMMAND, IDC_CHECK_UPDATE, 0);
			if( res)
				break;
			if( retrytimes++ >= 3)	// ����3�Σ���֤ PostMessage �ɹ�
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

	// ����Ĵ���������commandline�в��� 
	// --launcher-folder "launcher" ���� ��
	// --launcher-folder "launcher_tiyan" ����
	LPCTSTR szLauncherFolder = GetLauncherFolder();
	TCHAR CommandLineBuffer[MAX_PATH] = {0};
	_tcscat_s( CommandLineBuffer, _T("--launcher-folder \""));
	_tcscat_s( CommandLineBuffer, szLauncherFolder);
	_tcscat_s( CommandLineBuffer, _T("\" "));
	
	// ����ķ������ڵõ�����·��
	TCHAR szPath[ MAX_PATH] = {0};
	::GetModuleFileName( NULL, szPath, MAX_PATH);

	// ��������в����д���.mzpch�ļ�
	LPCTSTR pszExtension = GetExtension();	
	swprintf_s( szPath, MAX_PATH, L".%s", pszExtension);
	LPTSTR pPatchExtStr = _tcsstr( lpCmdLine, szPath );
	if ( pPatchExtStr !=  NULL )
	{
		// ����-patch 
		_tcscat_s( CommandLineBuffer, _T("-patch "));
		_tcscat_s( CommandLineBuffer, lpCmdLine);

		RunLauncher( CommandLineBuffer );
	}
	else
	{
		// û��.mzpch������ֱ��ִ��ȱʡ������launcher
		_tcscat_s( CommandLineBuffer, lpCmdLine);
		RunLauncher( CommandLineBuffer );
	}

	return 0;
}
