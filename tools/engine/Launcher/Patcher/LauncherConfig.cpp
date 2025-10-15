//////////////////////////////////////////////////////////////////////////
//
// LauncherConfig.cpp
//
// ������Ϣ������
//
// author: yanghaibo
// date : 2008-11-11
//
#include "StdAfx.h"
#include "LauncherConfig.h"
#include "../../pfs/pfs2.h"
#include "XmlReader.h" // for CXmlReader

CLauncherConfig* LauncherConfig()
{
	static CLauncherConfig s_Instance;
	return &s_Instance;
}

CLauncherConfig::CLauncherConfig(void)
{
	CoInitialize(NULL);
}

CLauncherConfig::~CLauncherConfig(void)
{
	CoUninitialize();
}

extern LPCTSTR GetModuleDir();
extern LPCTSTR GetInstallDir();

BOOL CLauncherConfig::Init(CComPtr<IXMLDOMNode>& rXmlRootNode)
{
	MSXMLDOM::CReader reader;

	PFSX::CNativeFile nativeFile;

	std::wstring path = GetInstallDir();
	path += L"/";
	path += GetLauncherFolder();
	path += L"/launcher/config.xml";

	if( !nativeFile.Open( path, PFS::FM_EXCL, PFS::FA_RDONLY ) )
	{
		return FALSE;
	}

	if( reader.OpenFile( nativeFile ) == 0 )
	{
		MSXMLDOM::CReadNode tempNode;

		reader.GetRootNode( tempNode);
		rXmlRootNode = tempNode.GetInterface();
	}

	return rXmlRootNode != NULL;
}

BSTR CLauncherConfig::GetValue( BSTR queryString )
{
	CComPtr<IXMLDOMNode> spXmlRootNode;
	if( !Init(spXmlRootNode) )
	{
		return L"";
	}

	CComPtr<IXMLDOMNode> spAttriNode;
	HRESULT hr = spXmlRootNode->selectSingleNode( queryString, &spAttriNode);
	if ( hr == 0 )
	{
		BSTR bstrValue;
		hr = spAttriNode->get_text( &bstrValue );
		return bstrValue;
	}

	return L"";
}

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

LPCTSTR GetModuleDir()
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

// Patcher ����λ��install directory ��bin/binDĿ¼��
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

static LPTSTR _GetLauncherFolderBuffer()
{
	static TCHAR szBuffer[MAX_PATH] = _T("launcher");
	return szBuffer;
}

LPCTSTR GetLauncherFolder()
{
	LPCTSTR lpszName = _GetLauncherFolderBuffer();
	return lpszName;
}

void SetLauncherFolder( LPCTSTR szFolder )
{
	LPTSTR lpszName = _GetLauncherFolderBuffer();
	_tcscpy_s( lpszName, MAX_PATH, szFolder );
}

void AbsolutePath( CString& rRelPath )
{
	CString strApplicationName2 = rRelPath;
	PathCombine( strApplicationName2.GetBufferSetLength(MAX_PATH), GetModuleDir(), rRelPath );
	strApplicationName2.ReleaseBuffer();

	strApplicationName2.Replace(L'/', L'\\');
	PathCanonicalize( rRelPath.GetBufferSetLength(MAX_PATH), strApplicationName2.GetString() );
	rRelPath.ReleaseBuffer();
}

CString GetGameClientDir()
{
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/game-client/@dir" );
	CString strApplicationName = bstrValue;
	if ( PathIsRelative( strApplicationName ) )
	{
		AbsolutePath( strApplicationName );
	}

	return strApplicationName;
}

//
// ��ȡRepair.exe����İ�װ·��
//
CString GetRepairPath()
{
#ifdef _DEBUG
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/repair/@pathd" );
#else
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/repair/@path" );
#endif

	CString strApplicationName = bstrValue;
	if ( PathIsRelative( strApplicationName ) )
	{
		AbsolutePath( strApplicationName );
	}

	return strApplicationName;
}

CString GetGameClientPath()
{
#ifdef _DEBUG
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/game-client/@pathd" );
#else
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/game-client/@path" );
#endif

	CString strApplicationName = bstrValue;
	if ( PathIsRelative( strApplicationName ) )
	{
		AbsolutePath( strApplicationName );
	}

	return strApplicationName;
}

CString GetPerfectProtectorDir()
{
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/perfect-protector/@dir" );
	CString strDirectoryName = bstrValue;
	if ( PathIsRelative( strDirectoryName ) )
	{
		AbsolutePath( strDirectoryName );
	}

	return strDirectoryName;
}

CString GetBootstrapPath()
{
#ifdef _DEBUG
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/bootstrap/@pathd" );
#else
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/bootstrap/@path" );
#endif

	// �滻 %launcher-folder% ����
	CString strApplicationName = bstrValue;
	strApplicationName.Replace(_T("%launcher-folder%"), GetLauncherFolder() );
	if ( PathIsRelative( strApplicationName ) )
	{
		AbsolutePath( strApplicationName );
	}

	return strApplicationName;
}

CString GetLauncherUIResourceDir()
{
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/ui-layout-info/@rel-dir" );
	CString strResourcePath = bstrValue;
	strResourcePath.Replace(_T("%launcher-folder%"), GetLauncherFolder() );
	AbsolutePath(strResourcePath);
	return strResourcePath;
}

CString GetLauncherUILayoutFile()
{
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/ui-layout-info/@config" );
	if( bstrValue.Length() != 0 )
	{
		CString strLayoutFile = bstrValue;
		return strLayoutFile;
	}
	else
	{
		// Default value
		return _T("ui/launcher.xml");
	}
}

//
// ��ȡ "ѡ�������" UI����Ĳ��������ļ�
//
CString GetSelectServerDlgUILayoutFile()
{
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/ui-layout-info/@selectserverdlg" );
	if( bstrValue.Length() != 0 )
	{
		CString strLayoutFile = bstrValue;
		return strLayoutFile;
	}
	else
	{
		// Default value
		return _T("ui/selectserverdlg.xml");
	}
}

//
// ��ȡ "��������" UI����Ĳ��������ļ�
//
CString GetSetDownloadDlgUILayoutFile()
{
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/download-set/@config" );
	if( bstrValue.Length() != 0 )
	{
		CString strLayoutFile = bstrValue;
		return strLayoutFile;
	}
	else
	{
		// Default value
		return _T("ui/setdownloaddlg.xml");
	}
}

//
// ��ȡ "ѡ��������б�" UI����Ĳ��������ļ�
//
CString GetSelectServerListUILayoutFile()
{
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/ui-layout-info/@selectserverlist" );
	if( bstrValue.Length() != 0 )
	{
		CString strLayoutFile = bstrValue;
		return strLayoutFile;
	}
	else
	{
		// Default value
		return _T("ui/selectserverlist.xml");
	}
}

CString GetLauncherOnlineResourceUrl()
{
	static CString strResourceUrl;

	if ( strResourceUrl.IsEmpty() )
	{
		CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/web-online-page/@zipurl" );
		if ( bstrValue.Length() == 0 )
		{
			// Default value
			return _T("http://mhzx.locojoy.com/launcher/launcher.zip");
		}
		else
		{
			strResourceUrl = bstrValue;
		}
	}

	return strResourceUrl;
}

CString GetLauncherLocalPagePath()
{
	static CString strPath;
	if( strPath.IsEmpty() )
	{
		CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/web-online-page/@rel-path" );
		if ( bstrValue.Length() != 0 )
		{
			strPath = bstrValue;
		}
		else
		{
			// Default value
			strPath = _T("../%launcher-folder%/launcher/webpage/");
		}

		strPath.Replace(_T("%launcher-folder%"), GetLauncherFolder() );

		AbsolutePath(strPath);
	}
	
	return strPath;
}

// 
// ��ȡlauncher�ϵ�Webҳ���URL·��
// �����������ҳ�治���ڣ���������ҳ���ַ.
//
CString GetLauncherWebPageUrl()
{
	CString path = GetLauncherOfflineUrl();
	
	if( !PathFileExists(path) )
	{
		// ������ʱ����վURL
		return GetLauncherOnlineUrl();
	}
	else
	{
		return path;
	}
}

//
// ��ȡlauncher��������ҳ���·��
//
CString GetLauncherOfflineUrl()
{
	CString path = GetLauncherLocalPagePath();
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/web-online-page/@index" );
	if ( bstrValue.Length() != 0 )
	{
		path += bstrValue;
	}
	else
	{
		// Default value
		path += _T("index.htm");
	}

	return path;
}

//
// ��ȡlauncher����ҳ���·��
//
CString GetLauncherOnlineUrl()
{
	static CString strResourceUrl;

	if ( strResourceUrl.IsEmpty() )
	{
		CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/web-online-page/@url" );
		if ( bstrValue.Length() == 0 )
		{
			// Default value
			return _T("http://mhzx.locojoy.com/launcher/index.htm");
		}
		else
		{
			strResourceUrl = bstrValue;
		}
	}

	return strResourceUrl;
}

//
// ��ȡ "�ֶ����ظ��°�" ��url
//
CString GetOnlineDownloadPageUrl()
{
	static CString strUrl;

	if ( strUrl.IsEmpty() )
	{
		// <download-page index = "http://mhzx.locojoy.com/download/index.htm"/>
		CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/download-page/@index" );
		if ( bstrValue.Length() == 0 )
		{
			// Default value
			return _T("http://mhzx.locojoy.com/download/index.htm");
		}
		else
		{
			strUrl = bstrValue;
		}
	}

	return strUrl;
}

//
// ��ȡ�ı���Դ�������ļ� - launcher
//
CString GetLauncherTextResourceFile()
{
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//launcher/textresource/@path" );
	CString strResourcePath = bstrValue;
	strResourcePath.Replace(_T("%launcher-folder%"), GetLauncherFolder() );
	AbsolutePath(strResourcePath);
	return strResourcePath;
}

//
// ��ȡ�ı���Դ�������ļ� - patcher
//
CString GetPatcherTextResourceFile()
{
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//patcher/textresource/@path" );
	CString strResourcePath = bstrValue;
	strResourcePath.Replace(_T("%launcher-folder%"), GetLauncherFolder() );
	AbsolutePath(strResourcePath);
	return strResourcePath;
}

//
// ��ȡ�ı���Դ�������ļ� - repair
//
CString GetRepairTextResourceFile()
{
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//repair/textresource/@path" );
	CString strResourcePath = bstrValue;
	strResourcePath.Replace(_T("%launcher-folder%"), GetLauncherFolder() );
	AbsolutePath(strResourcePath);
	return strResourcePath;
}

//
// ��ȡ��̨���µ�Ƶ��(�Է���Ϊ��λ��ȱʡֵ1Сʱ)
//
DWORD GetBackgroundUpdateFrequency()
{
	//<patcher>
	//    <background-update freq="60"/>
	//</patcher>
	CComBSTR bstrValue = LauncherConfig()->GetValue( L"//patcher/background-update/@freq" );
	
	DWORD dwFreq = 60;
	int nValue = _wtoi( (BSTR)bstrValue );
	if( nValue != 0 )
	{
#ifdef _DEBUG
		if( nValue < 1 )
			dwFreq = 1;
#else
		if( nValue < 10 )
			dwFreq = 10;
#endif
		else if( nValue > 1440 )
			dwFreq = 1440;
		else
			dwFreq = (DWORD)nValue;
	}

	return dwFreq;
}

BOOL CLauncherConfig::GetValueArray( BSTR queryString, CStringArray& rFilePathArray)
{
	CComPtr<IXMLDOMNode> spXmlRootNode;
	if( !Init(spXmlRootNode) )
	{
		return FALSE;
	}

	CComPtr<IXMLDOMNodeList> spNodeList;
	HRESULT hr = spXmlRootNode->selectNodes( queryString, &spNodeList);
	if( FAILED(hr) )
	{
		return FALSE;
	}
	
	long lCount = 0;
	hr = spNodeList->get_length(&lCount);
	if( FAILED(hr) )
	{
		return FALSE;
	}

	rFilePathArray.SetSize(0,(INT_PTR)lCount);

	for( long lIndex = 0; lIndex < lCount; lIndex ++ )
	{
		CComPtr<IXMLDOMNode> spXmlFileNode;
		hr = spNodeList->get_item(lIndex, &spXmlFileNode );
		if( FAILED(hr) )
		{
			continue;
		}

		CComBSTR bstrValue;
		hr = spXmlFileNode->get_text( &bstrValue );
		if( FAILED(hr) )
		{
			continue;
		}

		rFilePathArray.Add(bstrValue);
	}

	// rFilePathArray.FreeExtra();

	return TRUE;
}

//
// Ϊ�˷�����ԣ��˽ӿڷ��غ��Ը��µ��ļ�·���б�
//
BOOL GetUpdateIgnoringFilePathArray(CStringArray& rFilePathArray)
{
 	return LauncherConfig()->GetValueArray(L"//patcher/update/ignoring-list/file", rFilePathArray);
}