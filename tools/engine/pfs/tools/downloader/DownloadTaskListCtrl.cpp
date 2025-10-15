// DownloadTaskListCtrl.cpp : implementation file
//

#include "stdafx.h"
#include "downloader.h"
#include "DownloadTaskListCtrl.h"

HACCEL g_haccel = NULL;

// CDownloadTaskListCtrl

IMPLEMENT_DYNAMIC(CDownloadTaskListCtrl, CListCtrl)

CDownloadTaskListCtrl::CDownloadTaskListCtrl()
{

}

CDownloadTaskListCtrl::~CDownloadTaskListCtrl()
{
	if ( g_haccel != NULL )
	{
		DestroyAcceleratorTable( g_haccel );
		g_haccel = NULL;
	}
}


BEGIN_MESSAGE_MAP(CDownloadTaskListCtrl, CListCtrl)
	ON_WM_CREATE()
	ON_COMMAND( ID_EDIT_PASTE, Paste )
	ON_COMMAND( ID_EDIT_DELETE, Delete )
	ON_COMMAND( ID_EDIT_SELECT_ALL, SelectAll )
END_MESSAGE_MAP()

BOOL CDownloadTaskListCtrl::_InnerInit()
{
	// UINT mask;
	// int fmt;
	// int cx;
	// LPWSTR pszText;
	// int cchTextMax;
	// int iSubItem;
	// int iImage;
	// int iOrder;

	LVCOLUMN lvColmns[ ] = 
	{
		{	LVCF_TEXT|LVCF_FMT, LVCFMT_LEFT, 100, L"�ļ���",256,0,0,0 },
		{	LVCF_TEXT|LVCF_FMT,	LVCFMT_LEFT, 100, L"��С",64,0,0,0 },
		{	LVCF_TEXT|LVCF_FMT,	LVCFMT_LEFT, 100, L"����",128,0,0,0 },
		{	LVCF_TEXT|LVCF_FMT,	LVCFMT_LEFT, 100, L"�ٶ�",64,0,0,0 },		
		{	LVCF_TEXT|LVCF_FMT,	LVCFMT_LEFT, 100, L"�ٷֱ�",64,0,0,0 },		
	};

	for( int iIndex = 0; iIndex < _countof(lvColmns); iIndex ++ )
	{
		InsertColumn( iIndex, &lvColmns[iIndex] );
	}

	//UINT mask;
	//int iItem;
	//int iSubItem;
	//UINT state;
	//UINT stateMask;
	//LPWSTR pszText;
	//int cchTextMax;
	//int iImage;
	//LPARAM lParam;
	//int iIndent;
	//int iGroupId;
	//UINT cColumns; // tile view columns
	//PUINT puColumns;

	//LVITEM lvItems[] = 
	//{
	//	{ LVIF_TEXT, 0, 0, 0, 0, L"sss", 0, 0, 0, 0, 0, 0 },
	//	{ LVIF_TEXT, 0, 0, 0, 0, L"sss", 0, 0, 0, 0, 0, 0 },
	//	{ LVIF_TEXT, 0, 0, 0, 0, L"sss", 0, 0, 0, 0, 0, 0 },
	//	{ LVIF_TEXT, 0, 0, 0, 0, L"sss", 0, 0, 0, 0, 0, 0 },
	//	{ LVIF_TEXT, 0, 0, 0, 0, L"sss", 0, 0, 0, 0, 0, 0 },
	//}

	LPCWSTR lpszFileNames[] = 
	{
		L"/.files.meta",
		L"/.setup.meta",
		L"/.version.meta",
		L"/bin/bugreport.exe",
		L"/bin/d3dx9_33.dll",
		L"/bin/gameclient.exe",
		L"/bin/gxwindow.dll",
		L"/bin/libdds.dll",
		L"/bin/libdxt.dll",
		L"/bin/log.dll",
		L"/bin/ogg.dll",
		L"/bin/openal32.dll",
		L"/bin/perfectprotector.sys",
		L"/bin/pp_data0",
		L"/bin/renderer.dll",
		L"/bin/showengine.dll",
		L"/bin/vorbis.dll",
		L"/bin/vorbisfile.dll",
		L"/bin/wrap_oal.dll",
		L"/cfg.pfs",
		L"/character.pfs",
		L"/effect.pfs",
		L"/fonts/fzhtk.ttf",
		L"/fonts/fzy4k.ttf",
		L"/launcher/launcher.exe",
		L"/launcher/launcher/config.xml",
		L"/launcher/launcher/ui/buttons.bmp",
		L"/launcher/launcher/ui/launcher.ico",
		L"/launcher/launcher/ui/launcher.xml",
		L"/launcher/launcher/ui/main.bmp",
		L"/launcher/launcher/ui/selectserverdlg.xml",
		L"/launcher/launcher/ui/selectserverlist.xml",
		L"/launcher/launcher/ui/svrdlg.bmp",
		L"/launcher/launcher/ui/svrdlgctrl.bmp",
		L"/launcher/launcher/webpage/images/btn01.jpg",
		L"/launcher/launcher/webpage/images/btn02.jpg",
		L"/launcher/launcher/webpage/images/btn03.jpg",
		L"/launcher/launcher/webpage/images/btn04.jpg",
		L"/launcher/launcher/webpage/images/index01.jpg",
		L"/launcher/launcher/webpage/images/index02.jpg",
		L"/launcher/launcher/webpage/images/index03.jpg",
		L"/launcher/launcher/webpage/images/pic01.jpg",
		L"/launcher/launcher/webpage/images/pic02.jpg",
		L"/launcher/launcher/webpage/images/pic03.jpg",
		L"/launcher/launcher/webpage/style/master.css",
		L"/map.pfs",
		L"/monster.pfs",
		L"/npc.pfs",
		L"/pak.pfs",
		L"/patcher/inetutils.dll",
		L"/patcher/patcher.exe",
		L"/patcher/repair.exe",
		L"/show.pfs",
		L"/sound.pfs",
		L"/ui.pfs",
		L"/uninstall.exe",
		L"/uninstall.xml",
		L"/wtf/dxt/fontdef.xml",
		L"/wtf/dxt/v4ui_contexttool.xml",
		L"/wtf/mount.xml",
		L"/wtf/servers.xml",
	};

	//for( int iIndex = 0; iIndex < _countof(lpszFileNames); iIndex ++ )
	//{
	//	InsertItem( iIndex, lpszFileNames[iIndex]);
	//}

	SetColumnWidth( 0, LVSCW_AUTOSIZE );

	for( int iIndex = 1; iIndex < _countof(lvColmns); iIndex ++ )
	{

		SetColumnWidth( iIndex, LVSCW_AUTOSIZE_USEHEADER );
	}

	g_haccel = LoadAcceleratorsW( AfxGetResourceHandle(), MAKEINTRESOURCE(IDR_MAINFRAME) );
	
	return TRUE;

}

// CDownloadTaskListCtrl message handlers

int CDownloadTaskListCtrl::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CListCtrl::OnCreate(lpCreateStruct) == -1)
		return -1;

	if( ! _InnerInit() )
	{
		return -1;
	}

	return 0;
}

void CDownloadTaskListCtrl::PreSubclassWindow()
{
	_InnerInit();

	CListCtrl::PreSubclassWindow();
}

// summary: Tokenize a string to the string stl container,
//          which could be a vector/list/deque, etc.
// returns: the input destination string container.
// note:    pszDelimiter should not be NULL.
template< class stringcontainer >
stringcontainer& Tokenize(const wchar_t*   pszString, 
						  stringcontainer& rContainer, const wchar_t* pszDelimiter)
{
	wchar_t * pContext = NULL;
	wstring stringCopied = pszString; // Copy the input first
	wchar_t * pToken = wcstok_s((wchar_t *)stringCopied.c_str(), pszDelimiter, &pContext);
	while ( pToken != NULL )
	{
		rContainer.push_back(wstring(pToken));
		pToken = wcstok_s(NULL, pszDelimiter, &pContext);
	}

	return rContainer;
}

UINT InsertFileList( HWND hwndList, LPCTSTR pszFile );

template< class stringcontainer >
UINT InsertFileList( HWND hwndList, const stringcontainer& rlist )
{
	stringcontainer::const_iterator itr = rlist.begin();
	stringcontainer::const_iterator itre = rlist.end();
	UINT nCount = 0;
	for ( ; itr != itre; ++ itr )
	{
		if( InsertFileList( hwndList, (*itr).c_str() ) != -1 )
		{
			nCount ++;
		}
	}

	return nCount;
}

UINT InsertFileList( HWND hwndList, LPCTSTR pszFile )
{
	LVFINDINFO lvFindInfo = {0};
	lvFindInfo.flags = LVFI_STRING;
	lvFindInfo.psz = pszFile;

	if ( ListView_FindItem( hwndList, -1, &lvFindInfo) != -1 )
	{
		// �����ظ���
		return -1;
	}

	LV_ITEM lvItem;
	lvItem.mask = LVIF_TEXT;
	lvItem.iItem = ListView_GetItemCount( hwndList );
	lvItem.iSubItem = 0;
	lvItem.pszText = (LPTSTR)pszFile;
	lvItem.cchTextMax = MAX_PATH;
	int index = ListView_InsertItem( hwndList, &lvItem);
	// �Զ������п�
	ListView_SetColumnWidth(hwndList,0,LVSCW_AUTOSIZE);
	ListView_EnsureVisible( hwndList, index, TRUE);
	return index;
}


void CDownloadTaskListCtrl::SelectAll()
{
	SetItemState( -1, LVIS_SELECTED, LVIS_SELECTED );
}

// ɾ������ѡ����
int DeleteSelectedItems( HWND hwndList )
{
	int nCount  = ListView_GetSelectedCount(hwndList);
	int nAll = ListView_GetItemCount(hwndList);
	if ( nAll == nCount )
	{
		ListView_DeleteAllItems(hwndList);
		return nCount;
	}

	if( nCount  > 0 )
	{
		// ������������
		int* IndexArray = (int*)_alloca( nCount * sizeof(int) ); // �ڶ�ջ�Ϸ����ڴ棬�����ͷ�
		int iIndex = 0;
		int iItem = -1;

		// �Ѽ�ѡ��״̬��List��
		do 
		{
			iItem = ListView_GetNextItem( hwndList, iItem, LVNI_SELECTED);
			if( iItem != -1 )
			{
				IndexArray[iIndex++] = iItem;
			}
		} while( iItem != -1 );

		// ����ɾ��
		for( int i = nCount-1; i >= 0; i -- )
		{
			ListView_DeleteItem( hwndList, IndexArray[i] );
		}
	}

	return nCount;
}

void CDownloadTaskListCtrl::Delete()
{
	// TODO: �ж�����״̬
	DeleteSelectedItems( GetSafeHwnd() );
}

int DoPaste( HWND hWndList )
{
	if ( !IsClipboardFormatAvailable( CF_UNICODETEXT ) )
	{
		MessageBeep(MB_ICONEXCLAMATION);
		return 0;
	}

	BOOL bRet = OpenClipboard( ::GetDesktopWindow() );
	if( !bRet )
	{
		return 0;
	}

	HGLOBAL hgblData = (HGLOBAL)GetClipboardData(CF_UNICODETEXT);
	if( hgblData == NULL )
	{
		CloseClipboard();
		return 0;
	}

	LPCWSTR lpszItemList = (LPCWSTR)GlobalLock( hgblData );
	wstringlist strlist;
	Tokenize( lpszItemList, strlist, L"\r\n");
	InsertFileList( hWndList, strlist );

	GlobalUnlock( hgblData );

	CloseClipboard();

	return 0;
}

void CDownloadTaskListCtrl::Paste()
{
	DoPaste( GetSafeHwnd() );
}

// ��������
void CDownloadTaskListCtrl::Start()
{

}

void CDownloadTaskListCtrl::Pause()
{

}

void CDownloadTaskListCtrl::Cancel()
{

}


BOOL CDownloadTaskListCtrl::PreTranslateMessage(MSG* pMsg)
{
	if( g_haccel != NULL )
	{
		if( TranslateAccelerator( GetSafeHwnd(), g_haccel, pMsg ) != 0 )
		{
			return TRUE;
		}
	}

	return CListCtrl::PreTranslateMessage(pMsg);
}

// ��ȡȫ���б�����
void CDownloadTaskListCtrl::GetFileNameList( wstringlist& rFileNameList )
{
	HWND hwndList = GetSafeHwnd();
	int nCount  = ListView_GetSelectedCount(hwndList);
	int nAll = ListView_GetItemCount(hwndList);
	if ( nAll == nCount || nCount == 0 )
	{
		// ��ȡ������Ŀ
		int iItem = -1;

		// �Ѽ�ѡ��״̬��List��
		do 
		{
			iItem = ListView_GetNextItem( hwndList, iItem, LVNI_ALL);
			if( iItem != -1 )
			{
				rFileNameList.push_back( wstring() );
				rFileNameList.back().resize(MAX_PATH);

				LV_ITEM _ms_lvi;
				_ms_lvi.iSubItem = 0;
				_ms_lvi.cchTextMax = MAX_PATH;
				_ms_lvi.pszText = (LPWSTR)rFileNameList.back().c_str();

				LRESULT lCount = ::SendMessage(hwndList, 
					LVM_GETITEMTEXT, (WPARAM)(iItem), (LPARAM)(LV_ITEM *)&_ms_lvi);

				rFileNameList.back().resize(lCount);
			}

		} while( iItem != -1 );

		return;
	}

	if( nCount  > 0 )
	{
		int iItem = -1;

		// �Ѽ�ѡ��״̬��List��
		do 
		{
			iItem = ListView_GetNextItem( hwndList, iItem, LVNI_SELECTED);
			if( iItem != -1 )
			{
				rFileNameList.push_back( wstring() );
				rFileNameList.back().resize(MAX_PATH);
				
				LV_ITEM _ms_lvi;
				_ms_lvi.iSubItem = 0;
				_ms_lvi.cchTextMax = MAX_PATH;
				_ms_lvi.pszText = (LPWSTR)rFileNameList.back().c_str();

				LRESULT lCount = ::SendMessage(hwndList, 
					LVM_GETITEMTEXT, (WPARAM)(iItem), (LPARAM)(LV_ITEM *)&_ms_lvi);

				rFileNameList.back().resize(lCount);
			}

		} while( iItem != -1 );
		
	}
}
