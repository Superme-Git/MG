///=============================================================================
/// Product   : MHZX
/// Project   : DXTest
/// Module    : CommonUtils.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	Common Utils
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-5-16 Created.
///=============================================================================
#include "StdAfx.h"
#include "CommonUtils.h"
#include <Shlwapi.h> // for PathFileExists()
#pragma comment(lib, "Shlwapi.lib")

namespace UTIL
{
	LPCTSTR GetModuleDirectory()
	{
		static TCHAR szModuleDir[MAX_PATH];
		if ( szModuleDir[0] == 0 )
		{
			DWORD dwRet = GetModuleFileName( NULL, szModuleDir, MAX_PATH);
			LPTSTR lpSep = &szModuleDir[dwRet];
			while( *lpSep != '/' && *lpSep != '\\' && *lpSep != ':' )
			{
				lpSep --;
			}
			lpSep[0] = _T('\0');
		}

		return szModuleDir;
	}

	// Work Directory Ϊ Module Directory �ĸ�Ŀ¼
	LPCTSTR GetWorkDirectory()
	{
		static TCHAR szWorkDir[MAX_PATH];
		if ( szWorkDir[0] == 0 )
		{
			DWORD dwRet = GetModuleFileName( NULL, szWorkDir, MAX_PATH);
			LPTSTR lpSep = &szWorkDir[dwRet-1];
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

		return szWorkDir;
	}

	void AbsolutePath( CString& rRelPath )
	{
		CString strApplicationName2 = rRelPath;
		PathCombine( strApplicationName2.GetBufferSetLength(MAX_PATH), GetModuleDirectory(), rRelPath );
		strApplicationName2.ReleaseBuffer();

		PathCanonicalize( rRelPath.GetBufferSetLength(MAX_PATH), strApplicationName2.GetString() );
		rRelPath.ReleaseBuffer();
	}

} // namespace UTIL