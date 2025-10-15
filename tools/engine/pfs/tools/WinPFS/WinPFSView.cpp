// WinPFSView.cpp : CWinPFSView ���ʵ��
//

#include "stdafx.h"
#include "WinPFS.h"
#include "WinPFSDoc.h"
#include "WinPFSView.h"

#include "SystemFileIconList.h"
#include "MainFrm.h"
#include "FileViewerFrame.h"
#include "OperatorThread.h"
#include "../packzip/BrowsePath.h"
#include "ProgressDlg.h"
#include "LeftView.h"
#include "WriteConfirmDlg.h"
#include "FileCentralCache.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

#define ID_VIEW_HEXSHOW 100

// CWinPFSView

IMPLEMENT_DYNCREATE(CWinPFSView, CListView)

BEGIN_MESSAGE_MAP(CWinPFSView, CListView)
	ON_WM_STYLECHANGED()
	ON_NOTIFY_REFLECT(LVN_GETDISPINFO, &OnLvnGetdispinfo)
	ON_COMMAND(ID_VIEW_HEXSHOW, &OnViewHexshow)
	ON_UPDATE_COMMAND_UI(ID_VIEW_HEXSHOW, &OnUpdateViewHexshow)
	ON_WM_CREATE()
	ON_NOTIFY_REFLECT(LVN_COLUMNCLICK, &CWinPFSView::OnLvnColumnclick)
	ON_NOTIFY_REFLECT(NM_DBLCLK, &CWinPFSView::OnNMDblclk)
	ON_NOTIFY_REFLECT(NM_RETURN, &CWinPFSView::OnNMReturn)
	ON_NOTIFY_REFLECT(NM_RCLICK, &CWinPFSView::OnNMRclick)
	ON_COMMAND(ID_MENU_INFLATE, &CWinPFSView::OnMenuInflate)
	ON_COMMAND(ID_MENU_VIEWFILE, &CWinPFSView::OnMenuViewFile)
	ON_NOTIFY_REFLECT(LVN_BEGINDRAG, &CWinPFSView::OnLvnBegindrag)
END_MESSAGE_MAP()

LPCTSTR g_column[] = {
	_T( "����"),
	_T( "��С"),
	_T( "ѹ�����С"),
	_T( "����"),
	_T( "ƫ����"),
	_T( "�޸�ʱ��"),
	_T( "CRC32"),
	_T( "ѹ����"),
	//_T( "hash1"),
	//_T( "hash2"),
	//_T( "version"),
	//_T( "unzipVersion"),
	_T( "METHOD"), // 8
	_T( "FLAG"),
};

// CWinPFSView ����/����

CWinPFSView::CWinPFSView()
: m_bHex( false)
, m_sortType( 0)
, m_bSortAsc( true)
, m_pLeftView( NULL)
{
	// TODO: �ڴ˴���ӹ������
}

CWinPFSView::~CWinPFSView()
{
}

BOOL CWinPFSView::PreCreateWindow(CREATESTRUCT& cs)
{
	cs.style |= LVS_REPORT|LVS_OWNERDATA ;
	return CListView::PreCreateWindow(cs);
}

int CWinPFSView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CListView::OnCreate(lpCreateStruct) == -1)
		return -1;

	// ��չ���ֻ��ͨ������ķ�ʽ����/*LVS_EX_DOUBLEBUFFER|*/
	GetListCtrl().ModifyStyle( 0, LVS_SHOWSELALWAYS);
	GetListCtrl().SetExtendedStyle( LVS_EX_FULLROWSELECT );
	
	SFIL::CSystemFileIconList& sfil = SFIL::CSystemFileIconList::GetInstance();
	ListView_SetImageList( GetListCtrl().GetSafeHwnd(), sfil.GetLargeIconList(), LVSIL_NORMAL);
	ListView_SetImageList( GetListCtrl().GetSafeHwnd(), sfil.GetSmallIconList(), LVSIL_SMALL);
	InsertColumn();

	m_dlgProgress.Create( IDD_PROGRESS, this);
	
	return 0;
}

void CWinPFSView::OnInitialUpdate()
{
	CListView::OnInitialUpdate();
}

// CWinPFSView ��Ϣ�������
void CWinPFSView::OnStyleChanged(int nStyleType, LPSTYLESTRUCT lpStyleStruct)
{
	CListView::OnStyleChanged(nStyleType,lpStyleStruct);	
}

// CWinPFSView message handlers
void CWinPFSView::InsertColumn()
{
	int count = sizeof( g_column) / sizeof( g_column[0]);
	GetListCtrl().InsertColumn( 0, g_column[0], LVCFMT_LEFT, 220);
	for( int i=1; i<count; i++)
	{
		GetListCtrl().InsertColumn( i, g_column[i], LVCFMT_RIGHT, 100);
	}
}

void CWinPFSView::SetItemCount()
{
	GetListCtrl().SetItemCount( m_cache.size());

	// ͨ������ͨ�����ItemCount
	NMLISTITEMCOUNT nmhdr;
	nmhdr.hdr.code = LVN_ITEMCOUNTUPDATED;
	nmhdr.hdr.hwndFrom = GetSafeHwnd();
	nmhdr.hdr.idFrom = GetDlgCtrlID(); // ���ID������ȷ,����MAINWND�޷��ӵ��ɷ�ͨ��
	nmhdr.count = GetListCtrl().GetItemCount();
	AfxGetMainWnd()->SendMessage( WM_NOTIFY, (WPARAM)nmhdr.hdr.idFrom, (LPARAM)(LONG_PTR)&nmhdr );

	Invalidate();
}

struct dostime
{
	union
	{
		unsigned short time;
		struct 
		{
			unsigned short sec : 5; // ��Ҫ*2
			unsigned short min : 6; //
			unsigned short hour : 5;//
		};
	};
	union
	{
		unsigned short date;
		struct 
		{
			unsigned short day : 5; // 
			unsigned short mon : 4; // 
			unsigned short year : 7;// ��Ҫ + 1980
		};
	};

	dostime( unsigned short _time, unsigned short _date )
	{
		time = _time;
		date = _date;
	}

	int Format( LPTSTR lpszBuffer, UINT cchBufferSize )
	{
		// ��ȷ����
		return _stprintf_s( lpszBuffer, cchBufferSize, 
			L"%hu-%02hu-%02hu %02hu:%02hu", year+1980, mon, day, hour, min );
	}
};

void CWinPFSView::OnLvnGetdispinfo(NMHDR *pNMHDR, LRESULT *pResult)
{
	NMLVDISPINFO *pDispInfo = reinterpret_cast<NMLVDISPINFO*>(pNMHDR);

	LV_ITEM* pItem= &(pDispInfo)->item;
	size_t index = pItem->iItem;

	if( pItem->mask & LVIF_IMAGE )
	{	
		if( index < m_cache.size())
		{
			ZIPFILE::CFileCentral* fc = ( ZIPFILE::CFileCentral*)m_cache[index];
			switch( pItem->iSubItem)
			{
			case 0:	
				if( !fc->IsDirectory() ) 
				{
					SFIL::CSystemFileIconList& sfil = SFIL::CSystemFileIconList::GetInstance();
					pItem->iImage = sfil.GetIndex( fc->m_name.c_str() );
				}
				else
				{
					SFIL::CSystemFileIconList& sfil = SFIL::CSystemFileIconList::GetInstance();
					pItem->iImage = sfil.GetFolderIndex( fc->m_name.c_str() );
				}
				break;
			default:
				break;
			}
		}
	}

	if( pItem->mask & LVIF_TEXT)	// �ַ�����������Ч
	{
		if( index < m_cache.size())
		{
			ZIPFILE::CFileCentral* fc = ( ZIPFILE::CFileCentral*)m_cache[index];

			if( fc->IsDirectory() ) 
			{
				switch( pItem->iSubItem)
				{
				case 0:		// name
					wcscpy_s( pItem->pszText, pItem->cchTextMax, 
						CWinPFSDoc::GetLastFileName( fc->m_name.c_str()) );
					break;
				case 3:		// type
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"���ϼ�" );
					break;
				case 4:		// offset
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%u", fc->m_uiOffset);
					break;
				case 5:
					{
						dostime dt(fc->m_usModifyTime, fc->m_usModifyDate); 
						dt.Format( pItem->pszText, pItem->cchTextMax );
					}
					break;
				default:
					break;
				}
			}
			else
			{
				switch( pItem->iSubItem)
				{
				case 0:		// name
					wcscpy_s( pItem->pszText, pItem->cchTextMax, 
						CWinPFSDoc::GetLastFileName( fc->m_name.c_str() ));
					break;
				case 1:		// sizeOrg
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%u", fc->m_uiSizeOrg);
					break;
				case 2:		// sizeNow
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%u", fc->m_uiSizeNow);
					break;
				case 3:		// type
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"�ļ�" );
					break;
				case 4:		// offset
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%u", fc->m_uiOffset);
					break;
				case 5:
					{
						dostime dt(fc->m_usModifyTime, fc->m_usModifyDate); 
						dt.Format( pItem->pszText, pItem->cchTextMax );
					}
					break;
				case 6:		// CRC32
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%08X", fc->m_uiCRC32 );
					break;
				case 7:		// zip radio
					if( fc->m_uiSizeOrg == 0 )
					{
						wcscpy_s( pItem->pszText, pItem->cchTextMax, L"N/A");
					}
					else
					{
						if( fc->m_uiSizeOrg < fc->m_uiSizeNow )
						{
							// exception case
							_stprintf_s( pItem->pszText, pItem->cchTextMax, L"-%.3f%%",
								100.0f * (fc->m_uiSizeNow-fc->m_uiSizeOrg) / fc->m_uiSizeOrg);
						}
						else
						{
							_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%.3f%%",
								100.0f * (fc->m_uiSizeOrg-fc->m_uiSizeNow) / fc->m_uiSizeOrg);
						}
					}
					break;
				case 8: // method
					{
						_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%hu", fc->GetMethod() );
					}
					break;
				case 9: // flag
					{
						_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%04hX", fc->m_usFlag );
					}
					break;
				default:
					break;
				}
			}
		}
	}
	*pResult = 0;
}

void CWinPFSView::OnViewHexshow()
{
	m_bHex = !m_bHex;
	Invalidate();
}

void CWinPFSView::OnUpdateViewHexshow(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_bHex);
}

void CWinPFSView::FillCache( LPCTSTR parent)
{
	m_cache.clear();
	GetDocument()->m_parentPath = parent;

	CWinPFSDoc* doc = GetDocument();
	if( doc)
	{
		doc->GetFolderList( parent, false, m_cache);
		m_cache.Sort( m_sortType, m_bSortAsc);
	}
	SetItemCount();
}

void CWinPFSView::OnLvnColumnclick(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	if( TYPE != pNMLV->iSubItem)
	{
		m_sortType = pNMLV->iSubItem;
		m_bSortAsc = !m_bSortAsc;
		m_cache.Sort( m_sortType, m_bSortAsc);
		Invalidate();
	}
	
	*pResult = 0;
}

// ��ָ���������Ӧ�Ĳ���(�鿴�ļ�����)
BOOL CWinPFSView::ExecuteItem( int iItem )
{
	ASSERT( iItem != -1 && iItem < (int)m_cache.size() );

	ZIPFILE::CFileCentral* fc = ( ZIPFILE::CFileCentral*)m_cache[iItem];
	ASSERT( fc );

	if( fc->IsDirectory() ) 
	{
		// ������Ϣ,��treectrl�ı���ͼ
		if( m_pLeftView)
		{
			CString str = GetListCtrl().GetItemText( iItem, 0);
			m_pLeftView->SelectChildItem( str);
		}
		return FALSE;
	}

	// ���ļ��鿴��,�鿴�ļ�����
	CFileViewerFrame* _pFileViewer = (CFileViewerFrame*)CFileViewerFrame::CreateObject();
	if( _pFileViewer  != NULL )
	{
		CRect rcWindow(0,0, 800, 600);
		CString str = _T("�ļ��鿴 - ");
		str += fc->m_name.c_str();
		if( !_pFileViewer->Create( NULL, str, 0, rcWindow, AfxGetMainWnd(), 0, 0, 0 ) )
		{
			return FALSE;
		}
		
		ZIPFILE::CFileCentralRead fcr(  GetDocument()->m_filereader, *fc );
		PFS::CImageData data = fcr.GetImage();

		_pFileViewer->ShowData( data.GetData(), (size_t)data.GetSize() );

		_pFileViewer->CenterWindow();

		_pFileViewer->ActivateFrame(SW_SHOW);

		return TRUE;
	}

	return FALSE;
}

void CWinPFSView::OnNMDblclk(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 0;

	LPNMITEMACTIVATE lpnmitem = (LPNMITEMACTIVATE) pNMHDR;

	ASSERT( lpnmitem != NULL  );
	if( lpnmitem->iItem != - 1 )
	{
		*pResult = !!ExecuteItem( lpnmitem->iItem );
	}
}

void CWinPFSView::OnNMReturn(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 0;

	// ���ֻѡ��һ��,ִ��ѡ����
	OnMenuViewFile();
}

void CWinPFSView::OnNMRclick(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 0;

	if( GetListCtrl().GetSelectedCount() == 0 )
	{
		return;
	}

	CMenu popupMenu;
	if( !popupMenu.LoadMenu( IDR_MENU1 ) )
	{
		return;
	}

	CMenu *pSubMenu = popupMenu.GetSubMenu( 0);
	if ( pSubMenu != NULL )
	{
		CPoint point;
		::GetCursorPos( &point);

		pSubMenu->TrackPopupMenu( 0, point.x, point.y, this );
		
		*pResult = 1;
	}
}

void CWinPFSView::OnMenuInflate()
{
	if( GetListCtrl().GetSelectedCount() == 0 )
	{
		return;
	}

	CBrowsePath browser( GetSafeHwnd());
	browser.SetIniPath( GetDocument()->m_lastSelPath);
	if( !browser.DoModal())
	{
		return;
	}
	
	COperatorList::GetInstance().Clear();
	GetDocument()->m_lastSelPath = browser.GetSelectPath();

	if( GetDocument()->m_thread != NULL )
		GetDocument()->m_thread->SetWnd( &m_dlgProgress);

	int state = 0;
	POSITION pos = GetListCtrl().GetFirstSelectedItemPosition();
	while( pos)
	{
		int index = GetListCtrl().GetNextSelectedItem( pos);
		CString str = GetListCtrl().GetItemText( index, 0);
		
		COperatorList::Item item;
		item.m_des = GetDocument()->m_lastSelPath;
		item.m_des += L"\\";
		item.m_des += str.GetString();

		item.m_isPath = ( L"���ϼ�" == GetListCtrl().GetItemText( index, 3));
		
		item.m_type = ( pos ? COperatorList::INFLATE : COperatorList::INFLATEEND);
		item.m_src = GetDocument()->m_parentPath;
		item.m_src += str.GetString();

		CString prompt;
		if( item.m_isPath)
		{
			item.m_src += L"/";
			item.m_des += L"/";
			if( PFS::CFileIO::IsDirectoryExisting( item.m_des))
			{
				prompt.Format( L"%s Ŀ¼�Ѵ��ڣ�Ҫ����ô��", item.m_des.c_str());		
			}
		}
		else
		{
			if( PFS::CFileIO::IsFileExisting( item.m_des))
			{
				prompt.Format( L"%s �ļ��Ѵ��ڣ�Ҫ����ô��", item.m_des.c_str());		
			}
		}

		if( prompt.IsEmpty())
		{
			COperatorList::GetInstance().Push( item);
		}
		else
		{
			switch( state)
			{
			case 0:
			case IDOK:
			case IDCANCEL:
				state = WriteConfirmBox( prompt);
				break;

			default:
				break;
			}

			if( IDCANCEL == state || IDCANCEL2 == state)
			{
				if( COperatorList::INFLATEEND == item.m_type)
				{
					// ������һ�ȡ���ˣ�ҲҪpushһ����Ч��item�ý��ȶԻ����˳�
					// item.m_src.clear();
					// item.m_src - ���Ϊ�գ���ʾ��Ŀ¼��
					item.m_des.clear();
					COperatorList::GetInstance().Push( item);
				}
			}
			else
			{
				COperatorList::GetInstance().Push( item);
			}
		}
		
	}

	m_dlgProgress.ShowWindow( SW_SHOW);
}

void CWinPFSView::OnLvnBegindrag(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	
	// �ϷŽ�ѹ��ʱʵ��Ϊ����������ļ��ϴ�ʱ������
	// ��δ�о���WinRar�Ϸ�ʱ�첽��ѹ����ôʵ�ֵ�

	// ͨ������ListView��ѡ�е�������Ŀ�Ѽ��û�ѡ�����ק��Ŀ��
	// ����Ŀ¼��Ŀ���ݹ鴦��
	CFileCentralCache fcc;
	POSITION pos = GetListCtrl().GetFirstSelectedItemPosition();
	std::wstring str;
	while( pos)
	{
		int index = GetListCtrl().GetNextSelectedItem( pos );

		str = GetDocument()->m_parentPath;
		str += GetListCtrl().GetItemText( index, 0).GetString();

		bool bPath = ( GetListCtrl().GetItemText( index, 3) == L"���ϼ�");
		if( bPath)
		{
			str += L"/";
		}

		const ZIPFILE::CFileCentral* fc = GetDocument()->m_filereader.GetFileCentral( str);
		if( fc )
		{
			fcc.push_back( (DWORD_PTR)fc);
		}

		if( bPath)
		{
			GetDocument()->AddDropFilesInPath( str, fcc);
		}
	}

	GetDocument()->DoDragDrop( fcc );

	*pResult = 1;
}

void CWinPFSView::OnMenuViewFile()
{
	int iItem = -1;
	UINT nCount = GetListCtrl().GetSelectedCount();
	if ( nCount == 1 )
	{
		iItem = GetListCtrl().GetNextItem(-1, LVNI_SELECTED );
	}
	else
	{
		if ( nCount > 1 )
		{
			iItem = GetListCtrl().GetNextItem(-1, LVNI_FOCUSED );
			if( iItem == -1 )
			{
				iItem = GetListCtrl().GetNextItem(-1, LVNI_DROPHILITED );
			}
		}
	}

	if ( iItem != -1 )
	{
		ExecuteItem( iItem );
	}
}