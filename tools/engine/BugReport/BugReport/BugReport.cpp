#include "BugReport.h"
#include <commctrl.h>
#include <malloc.h>
#include <time.h>
#include "Configuration.h"

#pragma comment(lib,"comctl32.lib") // for InitCommonControls

//#define debug 1
HINSTANCE g_hInstance = NULL;
TCHAR	g_szDumpFile[MAX_PATH];
HWND	g_hDlg = NULL;
HWND	g_hProgressBar = NULL;
HWND	g_hFileList = NULL;
HWND	g_hStatusInfo = NULL;
HWND	g_hDescript = NULL;
HWND	g_hQQMsn = NULL;
HWND	g_hPhone = NULL;
HWND	g_hEMail = NULL;
HANDLE	g_hPostThread = 0;
BOOL	g_bGetDumpInfo = 0;
HICON	g_hIconLarge = NULL;
HICON	g_hIconSmall = NULL;
CHAR	g_ccDumpFile[MAX_PATH];

INT_PTR CALLBACK DiagnosisDlgProc(HWND hwndDlg,UINT uMsg,WPARAM wParam,LPARAM lParam);

// GameClient.exe 程序位于install directory 的bin/binD目录下
// 因此，InstallDir为ModuleDir的父目录
LPCTSTR GetInstallDir()
{
	static TCHAR szPathBuffer[MAX_PATH];
	if ( szPathBuffer[0] == 0 )
	{
		DWORD dwRet = GetModuleFileName( NULL, szPathBuffer, MAX_PATH);
		LPTSTR lpSep = &szPathBuffer[dwRet-1];
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

		lpSep[0] = _T('\0');
	}

	return szPathBuffer;
}

int APIENTRY _tWinMain(HINSTANCE hInstance,
                     HINSTANCE hPrevInstance,
                     LPTSTR    lpCmdLine,
                     int       nCmdShow)
{
	UNREFERENCED_PARAMETER(hPrevInstance);
	UNREFERENCED_PARAMETER(lpCmdLine);
	g_hInstance = hInstance;
	g_hIconLarge = ::LoadIcon(hInstance, MAKEINTRESOURCE(IDI_BUGREPORT));
	g_hIconSmall = ::LoadIcon(hInstance, MAKEINTRESOURCE(IDI_SMALL));

	//Initcommon control
	InitCommonControls();
 	
	//初始化
	TCHAR path[MAX_PATH] = {0};
	_tcscpy_s( path, GetInstallDir());
	_tcscat_s( path, _T("\\log"));
	::CreateDirectory( path, NULL);
	_tcscat_s( path, _T("\\CrashDump.log"));
	InitLog( path);

	//初始化dump文件名
#ifndef debug
	lstrcpy( g_szDumpFile,lpCmdLine);
#else
	lstrcpy( g_szDumpFile,_TEXT("1.dmp"));
#endif
	Log(_TEXT("Get Dumpfile Name is %s\r\n"), g_szDumpFile);

	DialogBoxParam(hInstance,MAKEINTRESOURCE(IDD_DIAGNOSIS_DIALOG),0,DiagnosisDlgProc,0);
	
	CloseLog();

	return 0;
}

// 显示状态信息
void NotifyStatus( LPCTSTR lpszMessage )
{
	SendMessage( g_hStatusInfo, WM_SETTEXT, 0, (LPARAM)lpszMessage );
}

// 将窗口居中显示
void CenterWindow( HWND hWnd)
{
	RECT rc;
	GetClientRect( hWnd, &rc);
	RECT rcWorkArea;
	SystemParametersInfo( SPI_GETWORKAREA, 0, &rcWorkArea, 0 );
	rc.left = ( rcWorkArea.right - rc.right) / 2;
	rc.top = ( rcWorkArea.bottom - rc.bottom) / 2;
	SetWindowPos( hWnd, NULL, rc.left, rc.top, 0, 0, SWP_NOSIZE);
}

WNDPROC lpfn_OriListProc = NULL;

UINT InsertFileList( HWND hwndList, LPCTSTR pszFile )
{
	LVFINDINFO lvFindInfo = {0};
	lvFindInfo.flags = LVFI_STRING;
	lvFindInfo.psz = pszFile;

	if ( ListView_FindItem( hwndList, -1, &lvFindInfo) != -1 )
	{
		// 发现重复项
		return -1;
	}

	// 得到全路径文件名的最后一级文件名
	LPCTSTR filename = _tcsrchr( pszFile, _T('\\'));
	if( filename)
	{
		filename++;
	}
	else
	{
		filename = pszFile;
	}
	if( _tcsicmp( filename, _T("crashdmp.dmp")) == 0 ||
		_tcsicmp( filename, _T("crushdmp.dmp")) == 0)
	{
		if( g_szDumpFile[0] == 0)
		{
			_tcscpy_s( g_szDumpFile, pszFile);
		}
	}

	LV_ITEM lvItem;
	lvItem.mask = LVIF_TEXT;
	lvItem.iItem = ListView_GetItemCount( hwndList );
	lvItem.iSubItem = 0;
	lvItem.pszText = (LPTSTR)pszFile;
	lvItem.cchTextMax = MAX_PATH;
	int index = ListView_InsertItem( hwndList, &lvItem);
	// 自动调整列宽
	ListView_SetColumnWidth(hwndList,0,LVSCW_AUTOSIZE);
	ListView_EnsureVisible( hwndList, index, TRUE);
	return index;
}

// 插入文件时确保文件已存在,并且不是目录
UINT InsertFileListEnsureExistent( HWND hwndList, LPCTSTR pszFile )
{
	DWORD dwAttributes = GetFileAttributes( pszFile);

	if( INVALID_FILE_ATTRIBUTES == dwAttributes )
	{
		return -1;
	}

	if ( dwAttributes & FILE_ATTRIBUTE_DIRECTORY )
	{
		return -2;
	}

	return InsertFileList( hwndList, pszFile );
}

//
// 插入HDROP对象中所含有的文件名列表
// HDROP中可能还有文件夹，需要过滤这些文件夹
// 连续执行InsertFileList可能还会导致重复项，必须消除重复项
//
UINT InsertFileList( HWND hwndList, HDROP hDrop )
{
	UINT nFileCount = ::DragQueryFile( hDrop, 0xFFFFFFFF, NULL, 0 );
	UINT j = 0;
	if( nFileCount > 0 ) 
	{
		int nHave = ListView_GetItemCount( hwndList );

		TCHAR fileName[MAX_PATH];
		
		for ( UINT i = 0; i < nFileCount; i ++ )
		{
			::DragQueryFile(hDrop, i, fileName, MAX_PATH );
			
			if( GetFileAttributes( fileName) & FILE_ATTRIBUTE_DIRECTORY)
			{
				continue;	// 是目录就忽略
			}
			InsertFileList( hwndList, fileName);
		}
	}

	return j;
}

// 删除所有选中项
int DeleteSelectedItems( HWND hwndList )
{
	int nCount  = ListView_GetSelectedCount(hwndList);

	if( nCount  > 0 )
	{
		int nAll = ListView_GetItemCount(hwndList);
		if ( nCount == nAll )
		{
			// 删除所有项目, 特殊处理一下
			ListView_DeleteAllItems(hwndList);
			g_szDumpFile[0] = 0;
			return nCount;
		}

		// 定义索引数组
		int* IndexArray = (int*)_alloca( nCount * sizeof(int) ); // 在堆栈上分配内存，无需释放
		int iIndex = 0;
		int iItem = -1;

		// 搜集选择状态的List项
		do 
		{
			iItem = ListView_GetNextItem( hwndList, iItem, LVNI_SELECTED);
			if( iItem != -1 )
			{
				IndexArray[iIndex++] = iItem;
			}
		} while( iItem != -1 );

		// 倒序删除
		for( int i = nCount-1; i >= 0; i -- )
		{
			iItem = IndexArray[i];
			wchar_t buffer[MAX_PATH] = {0};
			ListView_GetItemText( hwndList, iItem, 0, buffer, MAX_PATH);
			if( _tcsicmp( buffer, g_szDumpFile) == 0)
			{
				g_szDumpFile[0] = 0;
			}
			ListView_DeleteItem( hwndList, IndexArray[i] );
		}
	}

	return nCount;
}

LRESULT CALLBACK FileListProc(HWND hwndList,UINT uMsg, WPARAM wParam,LPARAM lParam)
{
	switch(uMsg)
	{
	case WM_KEYDOWN:
		{
			if ( wParam == 'V' )
			{
				SHORT nState = ::GetKeyState(VK_CONTROL);
				if( (nState & 0x8000) != 0 )
				{
					SendMessageW(hwndList, WM_PASTE, 0, 0);
					return 0;
				}
			}
			else if ( wParam == 'A' )
			{
				SHORT nState = ::GetKeyState(VK_CONTROL);
				if( (nState & 0x8000) != 0 )
				{
					// 选中所有项
					ListView_SetItemState( hwndList, -1, LVIS_SELECTED, LVIS_SELECTED);
					return 0;
				}
			}
			else if ( wParam == VK_DELETE )
			{
				DeleteSelectedItems(hwndList);
				return 0;
			}
		}
		break;
	case WM_PASTE:
		{
			if ( !IsClipboardFormatAvailable( CF_HDROP) )
			{
				MessageBeep(MB_ICONEXCLAMATION);
				return 0;
			}

			BOOL bRet = OpenClipboard( ::GetDesktopWindow() );
			if( !bRet )
			{
				return 0;
			}

			HDROP hDrop = (HDROP)GetClipboardData(CF_HDROP);
			if( hDrop == NULL )
			{
				CloseClipboard();
				return 0;
			}

			InsertFileList( hwndList, hDrop );

			CloseClipboard();
		}
		return 0;
	case WM_DROPFILES:
		{
			HDROP hDrop = (HDROP)wParam;
			InsertFileList( hwndList, hDrop );
			::DragFinish( hDrop );
		}
		return 0;
	}
	return CallWindowProc( lpfn_OriListProc, hwndList, uMsg, wParam, lParam );
}

void AddDefaultFiles()
{
	if( g_szDumpFile[0] == 0)
	{
		swprintf_s( g_szDumpFile, MAX_PATH, L"%s\\log\\crushdmp.dmp", GetInstallDir());
	}

	if( (int)InsertFileListEnsureExistent( g_hFileList, g_szDumpFile) < 0)
	{
		// 再尝试另一个名字。原来的名字将废弃，但是为了兼容旧数据，保留原来的逻辑。
		swprintf_s( g_szDumpFile, MAX_PATH, L"%s\\log\\crashdmp.dmp", GetInstallDir());
		if( (int)InsertFileListEnsureExistent( g_hFileList, g_szDumpFile) < 0)
		{
			g_szDumpFile[0] = 0;
		}
	}

	// 查找当日的log
	SYSTEMTIME localTime;
	GetLocalTime( &localTime );
	wchar_t file[MAX_PATH] = {0};
	swprintf_s( file, MAX_PATH, L"%s\\log\\%04d_%02d_%02d.log", GetInstallDir(), 
		localTime.wYear, localTime.wMonth, localTime.wDay);
	InsertFileListEnsureExistent( g_hFileList, file);

	// 查找当日的带进程ID的log
	swprintf_s( file, MAX_PATH, L"%s\\log\\%04d_%02d_%02d_?????.log", GetInstallDir(), 
		localTime.wYear, localTime.wMonth, localTime.wDay);
	WIN32_FIND_DATAW wfd;
	HANDLE	hFind = FindFirstFileW( file, &wfd);
	if( INVALID_HANDLE_VALUE != hFind)
	{
		do
		{
			swprintf_s( file, MAX_PATH, L"%s\\log\\%s", GetInstallDir(), wfd.cFileName);
			InsertFileList( g_hFileList, file);
		}while( FindNextFile( hFind, &wfd));
		FindClose( hFind);
	}

	swprintf_s( file, MAX_PATH, L"%s\\log\\launcher.log", GetInstallDir());
	InsertFileListEnsureExistent( g_hFileList, file);

	swprintf_s( file, MAX_PATH, L"%s\\log\\patcher.log", GetInstallDir());
	InsertFileListEnsureExistent( g_hFileList, file);

	swprintf_s( file, MAX_PATH, L"%s\\log\\repair.log", GetInstallDir());
	InsertFileListEnsureExistent( g_hFileList, file);

	// pp 相关的日志和配置文件
	swprintf_s( file, MAX_PATH, L"%s\\bin\\pp_005.dat", GetInstallDir());
	InsertFileListEnsureExistent( g_hFileList, file);

	swprintf_s( file, MAX_PATH, L"%s\\bin\\pp\\pp_005.dat", GetInstallDir());
	InsertFileListEnsureExistent( g_hFileList, file);

	swprintf_s( file, MAX_PATH, L"%s\\bin\\pp\\pp_005_x64.dat", GetInstallDir());
	InsertFileListEnsureExistent( g_hFileList, file);

	swprintf_s( file, MAX_PATH, L"%s\\bin\\pp\\config.ini", GetInstallDir());
	InsertFileListEnsureExistent( g_hFileList, file);

	swprintf_s( file, MAX_PATH, L"%s\\bin\\pp\\config-x64.ini", GetInstallDir());
	InsertFileListEnsureExistent( g_hFileList, file);

	swprintf_s( file, MAX_PATH, L"%s\\bin\\pp\\mini*.dmp", GetInstallDir());
	hFind = FindFirstFileW( file, &wfd);
	if( INVALID_HANDLE_VALUE != hFind)
	{
		do
		{
			swprintf_s( file, MAX_PATH, L"%s\\bin\\pp\\%s", GetInstallDir(), wfd.cFileName);
			InsertFileList( g_hFileList, file);
		}while( FindNextFile( hFind, &wfd));
		FindClose( hFind);
	}

	// 添加.version.meta文件
	swprintf_s( file, MAX_PATH, L"%s\\.version.meta", GetInstallDir());
	InsertFileListEnsureExistent( g_hFileList, file);
}

// 判断字符串是否仅仅含有空格字符
BOOL IsReadableString( LPCTSTR lpszString )
{
	for( ; lpszString[0]; )
	{
		if( (int)(lpszString[0]) >= 256 || isalnum( lpszString[0] ) )
		{
			return FALSE;
		}

		lpszString++;
	}

	return TRUE;
}

//
// 为了避免用户上传大量无意义的崩溃日志，这里做了简单检测
//
// 检测崩溃日志文件，描述信息，联系方式等信息是否足够完整
// 如果有崩溃日志，用户可以不用填描述信息和联系方式
// 否则，用户必须填写描述信息，并填写至少一种联系方式
BOOL IsValidPostInfo()
{
	if( g_szDumpFile[0] != L'\0' )
	{
		// 崩溃日志文件不为空
		return TRUE;
	}

	const int MAXSIZE = 1024;
	wchar_t buffer[MAXSIZE] = { 0 };

	LRESULT res = SendMessageW( g_hDescript, WM_GETTEXTLENGTH, 0, 0);
	if( res > 0 )
	{
		if ( res < 4 )
		{
			// 描述信息太短
			return FALSE;
		}

		SendMessageW( g_hDescript, WM_GETTEXT, MAXSIZE, (LPARAM)buffer);
		if( IsReadableString( buffer ) )
		{
			// 描述信息不能为空
			return FALSE;
		}
	}
	else
	{
		// 描述信息为空
		return FALSE;
	}

	res = SendMessageW( g_hQQMsn, WM_GETTEXTLENGTH, 0, 0);
	if( res > 0 )
	{
		if ( res < 4 )
		{
			// QQ信息太短
			return FALSE;
		}

		SendMessageW( g_hQQMsn, WM_GETTEXT, MAXSIZE, (LPARAM)buffer);
		if( !IsReadableString( buffer ) )
		{
			// QQ不为空
			return TRUE;
		}
	}

	res = SendMessageW( g_hEMail, WM_GETTEXTLENGTH, 0, 0);
	if( res > 0 )
	{
		if ( res < 4 )
		{
			// QQ信息太短
			return FALSE;
		}

		SendMessageW( g_hEMail, WM_GETTEXT, MAXSIZE, (LPARAM)buffer);
		if( !IsReadableString( buffer ) )
		{
			// 邮箱不为空
			return TRUE;
		}
	}

	res = SendMessageW( g_hPhone, WM_GETTEXTLENGTH, 0, 0);
	if( res > 0 )
	{
		if ( res < 7 )
		{
			// 电话信息太短
			return FALSE;
		}

		SendMessageW( g_hPhone, WM_GETTEXT, MAXSIZE, (LPARAM)buffer);
		if( !IsReadableString( buffer ) )
		{
			// 电话不为空
			return TRUE;
		}
	}

	return FALSE;
}

// 创建link控件打开论坛
void CreateLinkControl()
{
	HWND hLink = ::GetDlgItem( g_hDlg, IDC_STATIC_LINK);
	if( ::IsWindow( hLink))
	{
		RECT rcLink;
		::GetWindowRect( hLink, &rcLink);
		::DestroyWindow( hLink);
		hLink = NULL;

		RECT rcDlg;
		::GetWindowRect( g_hDlg, &rcDlg);
		::OffsetRect( &rcLink, -rcDlg.left, -rcDlg.top-20);	// 减去20是标题栏高度
		WCHAR _LinkTitle[64] = L"<A>访问论坛</A>";
		::LoadStringW( g_hInstance, IDC_STATIC_LINK, _LinkTitle, 64 );

		// 从bugreport.ini中读取配置的BBS网页HTML。
		GetOptionValueUnicodeString(L"bugreport", L"bbs-home-link-html", _LinkTitle, 64);

		::CreateWindow( L"SysLink",_LinkTitle, WS_CHILD|WS_VISIBLE, rcLink.left, rcLink.top, 
			rcLink.right - rcLink.left, rcLink.bottom - rcLink.top, g_hDlg, (HMENU)(ULONG_PTR)IDC_STATIC_LINK, 0, 0);
	}
}

INT_PTR CALLBACK DiagnosisDlgProc(HWND hwndDlg,UINT uMsg,WPARAM wParam,LPARAM lParam)
{
	switch(uMsg)
	{
	case WM_INITDIALOG:
		// 设置此对话框的图标
		if ( g_hIconLarge != NULL && g_hIconSmall != NULL )
		{
			::SendMessage(hwndDlg, WM_SETICON, TRUE, (LPARAM)g_hIconLarge);// 设置大图标
			::SendMessage(hwndDlg, WM_SETICON, FALSE, (LPARAM)g_hIconSmall);// 设置小图标
		}

		//初始化对话框
		g_hDlg = hwndDlg;
		CenterWindow( g_hDlg);
		g_hProgressBar = GetDlgItem( hwndDlg,IDC_SENDING_PROGRESS);
		g_hDescript = GetDlgItem( hwndDlg, IDC_DESC_INFO);
		g_hQQMsn = GetDlgItem( hwndDlg, IDC_QQ_MSN);
		g_hPhone = GetDlgItem( hwndDlg, IDC_PNONE_NUMBER);
		g_hEMail = GetDlgItem( hwndDlg, IDC_EMAIL_ADDRESS);
		g_hStatusInfo = GetDlgItem( hwndDlg,IDC_STATUS_INFO);

		SendMessage( g_hDescript, EM_SETLIMITTEXT, 1024, 0 );

		SendMessage( g_hQQMsn, EM_SETLIMITTEXT, 64, 0 );
		SendMessage( g_hPhone, EM_SETLIMITTEXT, 64, 0 );
		SendMessage( g_hEMail, EM_SETLIMITTEXT, 64, 0 );

		SetFocus( g_hDescript);
		g_hFileList = GetDlgItem( hwndDlg, IDC_FILE_LIST);
		
		LV_COLUMN lvColumn;
		lvColumn.mask = LVCF_TEXT|LVCF_WIDTH|LVCF_FMT;
		lvColumn.fmt = LVCFMT_LEFT;
		lvColumn.pszText = _TEXT("File");
		lvColumn.cchTextMax = 50;
		lvColumn.cx = LVSCW_AUTOSIZE;
		ListView_InsertColumn( g_hFileList,0, &lvColumn );

		AddDefaultFiles();

		CreateLinkControl();

		// 我们已经在对话框资源属性中对FILE_LIST控件设置了AcceptFiles属性
		// 但是,该控件必须为对话框的第一个控件(TABINDEX为1),否则AcceptFiles属性无效
		//::DragAcceptFiles(g_hFileList, TRUE);

		lpfn_OriListProc = (WNDPROC)(LONG_PTR)::SetWindowLongPtr( g_hFileList, GWLP_WNDPROC, (LONG)(LONG_PTR)(&FileListProc) );
		break;

	case WM_DESTROY:
		::SetWindowLongPtr( g_hFileList, GWLP_WNDPROC, (LONG)(LONG_PTR)(lpfn_OriListProc) );
		lpfn_OriListProc = NULL;

		if ( g_hIconLarge != NULL )
		{
			DestroyIcon( g_hIconLarge );
			g_hIconLarge = NULL;
		}

		if ( g_hIconSmall != NULL )
		{
			DestroyIcon( g_hIconSmall );
			g_hIconSmall = NULL;
		}
		break;

	case WM_COMMAND:
		if(LOWORD(wParam) == IDOK) //发送
		{
			if( !IsValidPostInfo() )
			{
				WCHAR _MsgboxTitleBuffer[64] = {0};
				WCHAR _MessageTextBuffer[1024] = {0};
				::LoadStringW( g_hInstance, IDS_MSGBOX_TITLE_INFO, _MsgboxTitleBuffer, 64 );
				::LoadStringW( g_hInstance, IDS_DESCRIPTION_BRIEF, _MessageTextBuffer, 1024 );
				::MessageBoxW( g_hDlg,
					//_TEXT("您填写的信息不够完整或描述不够清晰！\n")
					//_TEXT("请填写必要的问题描述和至少一个您的有效联系方式，以方便我们定位问题并及时回访您。\n")
					//_TEXT("\n")
					//_TEXT("谢谢支持！")
					_MessageTextBuffer,
					//_TEXT("提示")
					_MsgboxTitleBuffer,
					MB_OK | MB_ICONWARNING );
				break;
			}
			if(g_hPostThread == NULL) //防止多次点击发送重复创建线程，如果前一次发送失败，会将hPostThread清0
			{
				DWORD dwPostThreadId = 0;
				g_hPostThread = CreateThread(0,0,(LPTHREAD_START_ROUTINE)PostUp_PostInfo,0,0,&dwPostThreadId);
				if(g_hPostThread)
					CloseHandle(g_hPostThread);
			}
			EnableWindow( GetDlgItem( g_hDlg, IDOK), FALSE);
		}
		else if(LOWORD(wParam) == IDCANCEL)//关闭
		{
			EndDialog(hwndDlg,0);
		}
		break;

	case WM_SENDRESULT:
		if( !wParam ) // 暂时，只有发送失败才会允许重复发送。
		{
			EnableWindow( GetDlgItem( g_hDlg, IDOK), TRUE);
		}
		g_hPostThread = NULL;
		break;

	case WM_NOTIFY:
		{
			// 处理link控件的点击事件
			LPNMHDR lpnmh = (LPNMHDR)lParam;
			if( IDC_STATIC_LINK == lpnmh->idFrom)
			{
				if( NM_RETURN == lpnmh->code || NM_CLICK == lpnmh->code)
				{
					// 产品或者论坛主页地址
					WCHAR _szBBSHomePageUrl[1024] = L"http://bbs.mhzx.locojoy.com";
					::LoadStringW( g_hInstance, IDS_BBS_HOME_PAGE_URL, _szBBSHomePageUrl, 1024 );

					// 从bugreport.ini中读取配置的BBS网页URL。
					GetOptionValueUnicodeString(L"bugreport", L"bbs-home-url", _szBBSHomePageUrl, 1024);

					ShellExecute( NULL, L"open", _szBBSHomePageUrl, NULL, NULL, SW_SHOWNORMAL);
				}
			}
		}
	}

	return (INT_PTR)FALSE;
}