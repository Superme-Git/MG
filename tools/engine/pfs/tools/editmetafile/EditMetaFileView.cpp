// EditMetaFileView.cpp : implementation file
//

#include "stdafx.h"
#include "EditMetaFile.h"
#include "EditMetaFileDoc.h"
#include "EditMetaFileView.h"
#include "MainFrame.h"
#include "../../stringhelper.h"

// CEditMetaFileView

IMPLEMENT_DYNCREATE(CEditMetaFileView, CFormView)

CEditMetaFileView::CEditMetaFileView()
: CFormView(CEditMetaFileView::IDD)
, m_first( true)
{

}

CEditMetaFileView::~CEditMetaFileView()
{
}

void CEditMetaFileView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST1, m_list);
}


//#define ID_EDIT_COPY                    0xE122
//#define ID_EDIT_CUT                     0xE123
//#define ID_EDIT_SELECT_ALL              0xE12A
#ifndef ID_EDIT_DELETE
#define ID_EDIT_DELETE                    0xE12D
#endif

BEGIN_MESSAGE_MAP(CEditMetaFileView, CFormView)
	ON_WM_SIZE()
	ON_NOTIFY( LVN_GETDISPINFO, IDC_LIST1, &CEditMetaFileView::OnLvnGetdispinfoList1)
	ON_MESSAGE( WM_VALIDATE, OnEndLabelEditVariableCriteria)
	ON_COMMAND( ID_EDIT_COPY, Copy )
	ON_COMMAND( ID_EDIT_DELETE, Delete )
	ON_COMMAND( ID_EDIT_SELECT_ALL, SelectAll )
	ON_UPDATE_COMMAND_UI( ID_EDIT_COPY, OnUpdateCopy )
	ON_UPDATE_COMMAND_UI( ID_EDIT_DELETE, OnUpdateDelete )
	ON_UPDATE_COMMAND_UI( ID_EDIT_SELECT_ALL, OnUpdateSelectAll )
END_MESSAGE_MAP()

void CEditMetaFileView::OnUpdateSelectAll(CCmdUI *pCmdUI)
{
	pCmdUI->Enable( m_list.GetItemCount() > 0 );
}

void CEditMetaFileView::OnUpdateDelete(CCmdUI *pCmdUI)
{
	int iIndex = m_list.GetNextItem( -1, LVIS_SELECTED);
	pCmdUI->Enable( iIndex >= 0 );
}

void CEditMetaFileView::OnUpdateCopy(CCmdUI *pCmdUI)
{
	int iIndex = m_list.GetNextItem( -1, LVIS_SELECTED);
	pCmdUI->Enable( iIndex >= 0 );
}

// CEditMetaFileView message handlers

void CEditMetaFileView::OnSize(UINT nType, int cx, int cy)
{
	CFormView::OnSize(nType, cx, cy);
	if( ::IsWindow( m_list.GetSafeHwnd()))
		m_list.MoveWindow( 0, 0, cx, cy);
}

void CEditMetaFileView::OnInitialUpdate()
{
	CFormView::OnInitialUpdate();

	if( m_first)
	{
		m_list.InsertColumn( 0, L"name", LVCFMT_LEFT, 250);
		m_list.InsertColumn( 1, L"size", LVCFMT_LEFT, 100);
		m_list.InsertColumn( 2, L"method", LVCFMT_LEFT, 60);
		m_list.InsertColumn( 3, L"hash", LVCFMT_LEFT, 250);
		m_list.InsertColumn( 4, L"TSID", LVCFMT_LEFT, 100);
		m_list.InsertColumn( 5, L"CRC32", LVCFMT_LEFT, 100);
		m_list.InsertColumn( 6, L"size2", LVCFMT_LEFT, 100);
		m_list.InsertColumn( 7, L"hash2", LVCFMT_LEFT, 250);
		m_list.SetExtendedStyle( LVS_EX_FULLROWSELECT);
		m_list.SetReadOnlyColumns( 0);

		m_first = false;
	}

	m_list.DeleteAllItems();
	m_cache.clear();

	CEditMetaFileDoc* doc = (CEditMetaFileDoc*)GetDocument();
	if( doc)
	{
		size_t size = doc->m_map.size();
		m_list.SetItemCount( size);

		m_cache.reserve( size);
		CMetaInfoPtr	temp;
		for( PFS::CMetaInfoMap::iterator itor = doc->m_map.begin(); itor != doc->m_map.end(); ++itor)
		{
			temp.m_name = itor->first.c_str();
			temp.m_meta = &itor->second;
			m_cache.push_back( temp);
		}

		((CMainFrame*)AfxGetMainWnd())->UpdateStatusBar( size);
	}

}

wchar_t Byte2WChar( BYTE byte)
{
	if( byte >= 0 && byte <= 9)
		return L'0' + byte;
	if( byte >= 10 && byte <= 15)
		return L'a' + byte - 10;
	return L'0';
}

void Data2String( const void* data, size_t len, wchar_t* des)
{
	//if( !data)
	//	return;
	BYTE* c = (BYTE*)data;
	size_t i = 0, j = 0;
	while( i < len)
	{
		des[j++] = Byte2WChar( (*c >> 4) & 0x0f);
		des[j++] = Byte2WChar( *c & 0x0f );
		c++;
		i++;
	}
}

void CEditMetaFileView::OnLvnGetdispinfoList1(NMHDR *pNMHDR, LRESULT *pResult)
{
	NMLVDISPINFO *pDispInfo = reinterpret_cast<NMLVDISPINFO*>(pNMHDR);
	
	LV_ITEM* pItem= &(pDispInfo->item);
	size_t index = pItem->iItem;
	//PFS::CMetaInfoMap::iterator itor = GetDocument()->m_map.begin();
	//while( iItemIndex-- >= 0)
	//	itor++;
	if( index < m_cache.size())
	{
		if( pItem->mask & LVIF_TEXT)	// 字符串缓冲区有效
		{
			const size_t size = 64;
			wchar_t	str[size] = {0};
			switch( pItem->iSubItem)
			{
			case 0:		// name
				wcscpy_s( pItem->pszText, pItem->cchTextMax, m_cache[index].m_name);
				break;
			case 1:		// size
				_i64tow_s( m_cache[index].m_meta->GetFileSize(), str, size, 10);
				wcscpy_s( pItem->pszText, pItem->cchTextMax, str);
				break;
			case 2:		// method
				_itow_s( m_cache[index].m_meta->GetMethod(), str, size, 10);
				wcscpy_s( pItem->pszText, pItem->cchTextMax, str);
				break;
			case 3:
				{
					// half MD5
					const PFS::FILE_DATA_HASH& dataHash = m_cache[index].m_meta->GetFileHash();
					Data2String( &dataHash, sizeof( dataHash), str);
					wcscpy_s( pItem->pszText, pItem->cchTextMax, str);
				}
				break;
			case 4:
				{
					// TSID
					PFSX::TSID tsid = m_cache[index].m_meta->GetTSID();
					str[4] = 0;
					str[0] = tsid&0xFF;
					str[1] = (tsid>>8)&0xFF;
					str[2] = (tsid>>16)&0xFF;
					str[3] = (tsid>>24)&0xFF;
					wcscpy_s( pItem->pszText, pItem->cchTextMax, str);
				}
				break;
			case 5:
				{
					// CRC32
					PFSX::CRC32 crc32 = m_cache[index].m_meta->GetCRC32();
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%08X", crc32 );
				}
				break;
			case 6:		// size2
				_i64tow_s( m_cache[index].m_meta->GetFileSize2(), str, size, 10);
				wcscpy_s( pItem->pszText, pItem->cchTextMax, str);
				break;
			case 7:
				{
					// half hash2
					const PFS::FILE_DATA_HASH& dataHash = m_cache[index].m_meta->GetFileHash2();
					Data2String( &dataHash, sizeof( dataHash), str);
					wcscpy_s( pItem->pszText, pItem->cchTextMax, str);
				}
				break;
			}
		}
	}
	*pResult = 0;
}

LRESULT CEditMetaFileView::OnEndLabelEditVariableCriteria(WPARAM wParam, LPARAM lParam)
{
	LV_DISPINFO* pDispInfo = (LV_DISPINFO*)lParam;
	LV_ITEM* pItem= &(pDispInfo)->item;
	if( pItem->pszText)
	{
		size_t index = pItem->iItem;
		if( index < m_cache.size())
		{
			switch( pItem->iSubItem)
			{
			case 1:		// size
				m_cache[index].m_meta->SetFileSize( _wtoi( pItem->pszText));
				break;
			case 2:		// method
				m_cache[index].m_meta->SetMethod( _wtoi( pItem->pszText));
				break;
			case 3:		// half MD5
				{
					PFS::CDataBlock data = PFSX::CStringHelper::String2Data( pItem->pszText);
					PFS::FILE_DATA_HASH hash = 0;
					memcpy( &hash, data.GetData(), sizeof( PFS::FILE_DATA_HASH));
					m_cache[index].m_meta->SetFileHash( hash);
				}
				break;
			case 4:		// TSID
				{
					char a = (char)pItem->pszText[0];
					char b = ' ', c = ' ', d = ' ';
					if ( a != 0 )
					{
						b = (char)pItem->pszText[1];
						if ( b != 0 )
						{
							c = (char)pItem->pszText[2];
							if ( c != 0 )
							{
								d = (char)pItem->pszText[3];
								if( d == 0 )
								{
									d = ' ';
								}
							}
							else
							{
								c = ' ';
							}
						}
						else
						{
							b = ' ';
						}
					}
					else
					{
						a = ' ';
					}
					
					PFSX::TSID tsid = PFSX::MakeTypeIDByName(a, b, c, d);
					m_cache[index].m_meta->SetTSID( tsid );
				}
				break;
			case 5:		// CRC32
				m_cache[index].m_meta->SetFileDownloadURI( pItem->pszText);
				break;
			case 6:		// size2
				m_cache[index].m_meta->SetFileSize2( _wtoi( pItem->pszText));
				break;
			case 7:		// half hash2
				{
					PFS::CDataBlock data = PFSX::CStringHelper::String2Data( pItem->pszText);
					PFS::FILE_DATA_HASH hash = 0;
					memcpy( &hash, data.GetData(), sizeof( PFS::FILE_DATA_HASH));
					m_cache[index].m_meta->SetFileHash2( hash);
				}
				break;
			}
			GetDocument()->SetModifiedFlag( TRUE);
		}
	}
	return 0;
}

BOOL CEditMetaFileView::PreTranslateMessage(MSG* pMsg)
{	
	if( pMsg->message == WM_KEYDOWN &&
		( pMsg->hwnd == m_list.GetSafeHwnd() ||
		::IsChild(m_list.GetSafeHwnd(), pMsg->hwnd ) ) )
	{
		if( m_list.PreTranslateMessage(pMsg ) )
		{
			return TRUE;
		}

		if( pMsg->wParam == VK_DELETE )
		{	
			Delete();
			return TRUE;
		}
		else if ( (GetKeyState( VK_CONTROL ) & 0x8000) != 0 )
		{
			if( pMsg->wParam == 'C' || pMsg->wParam == VK_INSERT )
			{
				// Ctrl + C or Ctrl + Insert
				Copy();
				return TRUE;
			}
			else if ( pMsg->wParam == 'A' )
			{
				SelectAll();
				return TRUE;
			}
		}
	}
	return CFormView::PreTranslateMessage( pMsg);
}

void CEditMetaFileView::SelectAll()
{
	// 选中所有项目
	m_list.SetItemState( -1, LVIS_SELECTED, LVIS_SELECTED );
}

void CEditMetaFileView::Delete()
{
	int end = -1;
	bool bChanged = false;
	for (int i = m_list.GetItemCount()-1; i>=-1; i--)
	{
		if( m_list.GetItemState(i, LVIS_SELECTED) == LVIS_SELECTED)
		{
			CString str = m_list.GetItemText( i, 0);
			CEditMetaFileDoc* doc = (CEditMetaFileDoc*)GetDocument();
			doc->m_map.erase( str.GetString());
			bChanged = true;
			//m_list.DeleteItem(i);
			m_list.SetItemState( i, 0, LVIS_SELECTED);

			if( end == -1)
				end = i;
		}
		else
		{
			if( end != -1)
			{
				int delstart = i + 1;
				int delend = end + 1;
				m_cache.erase( m_cache.begin() + delstart, m_cache.begin() + delend);
				bChanged = true;
				end = -1;
			}
		}
	}
	
	if( bChanged)
	{
		size_t count = m_cache.size();
		m_list.SetItemCount( count);
		((CMainFrame*)AfxGetMainWnd())->UpdateStatusBar( count);
		GetDocument()->SetModifiedFlag( TRUE);
	}
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

void CEditMetaFileView::Copy()
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

	AfxGetMainWnd()->SendMessageToDescendants( WM_SETTEXT, 0, (LPARAM)(LONG_PTR)L"选中的内容已经被Copy到剪贴板!" );
}
