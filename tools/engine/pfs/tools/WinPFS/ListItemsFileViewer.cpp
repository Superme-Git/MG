#include "StdAfx.h"
#include "ListItemsFileViewer.h"

#include <sstream> // for wostringstream

CListItemsFileViewer::CListItemsFileViewer(void)
{
}

CListItemsFileViewer::~CListItemsFileViewer(void)
{
}

BEGIN_MESSAGE_MAP( CListItemsFileViewer, CFileViewer )
	ON_COMMAND( ID_EDIT_COPY, Copy )
	ON_COMMAND( ID_EDIT_SELECT_ALL, SelectAll )
	ON_UPDATE_COMMAND_UI( ID_EDIT_COPY, OnUpdateCopy )
	ON_UPDATE_COMMAND_UI( ID_EDIT_SELECT_ALL, OnUpdateSelectAll )
END_MESSAGE_MAP()

bool CListItemsFileViewer::Create( CWnd* parent)
{
	CRect rc;
	parent->GetClientRect( &rc);

	if( !m_list.CreateEx( WS_EX_CLIENTEDGE, 
		WS_CHILD | WS_VISIBLE | WS_VSCROLL | LVS_REPORT | LVS_OWNERDATA, 
		rc, parent, AFX_IDW_PANE_FIRST ) )
	{
		return false;
	}

	// 设置ListCtrl的字体
	HGDIOBJ hFont = ::GetStockObject( DEFAULT_GUI_FONT );
	m_list.SendMessage( WM_SETFONT, (WPARAM)(LONG_PTR)hFont, 0 );

	if( ! InitColumns() )
	{
		return false;
	}

	return true;
}

bool CListItemsFileViewer::SetData( const void* pData, size_t len )
{
	return true;
}

void CListItemsFileViewer::Show( bool bShow)
{
	m_list.ShowWindow( bShow ? SW_SHOW : SW_HIDE);
}

void CListItemsFileViewer::OnUpdateSelectAll(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_list.GetItemCount() > 0 );
}

void CListItemsFileViewer::OnUpdateCopy(CCmdUI *pCmdUI)
{
	int iIndex = m_list.GetNextItem( -1, LVIS_SELECTED);
	pCmdUI->Enable( iIndex >= 0 );
}

void CListItemsFileViewer::SelectAll()
{
	// 选中所有项目
	m_list.SetItemState( -1, LVIS_SELECTED, LVIS_SELECTED );
}

class CClipboard
{
public:
	
	static BOOL Open( HWND hOwnerWnd )
	{
		BOOL bRet = OpenClipboard( hOwnerWnd );
		if ( !bRet )
		{
			ASSERT( !L"OpenClipboard() failed!" );
		}

		return bRet;
	}

	static BOOL SetStringData( LPCSTR lpszText )
	{
		int cch = lstrlenA( lpszText );
		
		HGLOBAL hglbCopy = GlobalAlloc(GMEM_MOVEABLE,  (cch + 1)); 
		if (hglbCopy == NULL) 
		{ 
			CloseClipboard(); 
			return FALSE; 
		} 

		// Lock the handle and copy the text to the buffer. 
		LPSTR lptstrCopy = (LPSTR)GlobalLock(hglbCopy); 
		memcpy(lptstrCopy, lpszText, cch); 
		lptstrCopy[cch] = (CHAR)0; // null character 
		GlobalUnlock(hglbCopy); 

		HANDLE hData = SetClipboardData(CF_TEXT, hglbCopy);
		if ( hData == NULL )
		{
			TRACE( L"SetClipboardData(CF_TEXT) failed! %08x", GetLastError() );
			ASSERT( !L"SetClipboardData() failed!" );
		}

		return hData != NULL;
	}

	static BOOL SetStringData( LPCWSTR lpszText )
	{
		int cch = lstrlenW( lpszText );

		HGLOBAL hglbCopy = GlobalAlloc(GMEM_MOVEABLE,  (cch + 1) * sizeof(WCHAR)); 
		if (hglbCopy == NULL) 
		{ 
			CloseClipboard(); 
			return FALSE; 
		} 

		// Lock the handle and copy the text to the buffer. 
		LPWSTR lptstrCopy = (LPWSTR)GlobalLock(hglbCopy); 
		memcpy(lptstrCopy, lpszText, cch * sizeof(WCHAR) ); 
		lptstrCopy[cch] = (WCHAR)0;    // null character 
		GlobalUnlock(hglbCopy); 
		HANDLE hData = SetClipboardData(CF_UNICODETEXT, hglbCopy);
		if ( hData == NULL )
		{
			TRACE( L"SetClipboardData(CF_UNICODETEXT) failed! %08x", GetLastError() );
			ASSERT( !L"SetClipboardData() failed!" );
		}

		return hData != NULL;
	}
	
	static void Close()
	{
		CloseClipboard();
	}
};

class CClipboardScoped : public CClipboard
{
public:
	CClipboardScoped(HWND hOwnerWnd)
	{
		Open(hOwnerWnd);
	}

	~CClipboardScoped()
	{
		Close();
	}
};

void CListItemsFileViewer::Copy()
{
	int nCount = m_list.GetItemCount();
	if( nCount == 0 )
	{
		return ;
	}

	int nColumnCount = 1;
	CHeaderCtrl* pHeader = m_list.GetHeaderCtrl();
	if( pHeader != NULL )
	{
		nColumnCount = pHeader->GetItemCount();
	}

	std::wostringstream woss;
	for( int i = 0; i < nCount; ++ i )
	{
		if( m_list.GetItemState(i, LVIS_SELECTED) == LVIS_SELECTED)
		{
			for( int c=0; c < nColumnCount; c ++ )
			{
				CString str = m_list.GetItemText( i, c);
				woss<<str.GetString();
				if ( c < nColumnCount - 1 )
					woss << L'\t';
			}

			woss << L"\r\n";
		}
	}

	CClipboardScoped clipboad(AfxGetMainWnd()->GetSafeHwnd());
	clipboad.SetStringData( woss.str().c_str() );

	// AfxGetMainWnd()->SendMessageToDescendants( WM_SETTEXT, 0, (LPARAM)(LONG_PTR)L"选中的内容已经被Copy到剪贴板!" );
	m_list.GetParent()->SendMessageToDescendants( WM_SETTEXT, 0, (LPARAM)(LONG_PTR)L"选中的内容已经被Copy到剪贴板!" );
}

