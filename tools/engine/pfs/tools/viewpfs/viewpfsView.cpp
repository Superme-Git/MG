// viewpfsView.cpp : implementation of the CviewpfsView class
//

#include "stdafx.h"
#include "viewpfs.h"

#include "viewpfsDoc.h"
#include "viewpfsView.h"
#include "MainFrm.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

LPCTSTR g_column[] = {
_T( "name"),
_T( "offset"),
_T( "sizeNow"),
_T( "pagecount"),
_T( "sizeOrg"),
_T( "zip radio"),
_T( "hash1"),
_T( "hash2"),
//_T( "modifyTime"),
//_T( "modifyDate"),
//_T( "version"),
//_T( "unzipVersion"),
//_T( "flag"),
//_T( "method"),
};

// CviewpfsView

IMPLEMENT_DYNCREATE(CviewpfsView, CListView)

BEGIN_MESSAGE_MAP(CviewpfsView, CListView)
	ON_NOTIFY_REFLECT(LVN_GETDISPINFO, &CviewpfsView::OnLvnGetdispinfo)
	ON_COMMAND(ID_VIEW_HEXSHOW, &CviewpfsView::OnViewHexshow)
	ON_UPDATE_COMMAND_UI(ID_VIEW_HEXSHOW, &CviewpfsView::OnUpdateViewHexshow)
	ON_COMMAND(ID_VIEW_RUNZIPSHOW, &CviewpfsView::OnViewRunzipshow)
	ON_UPDATE_COMMAND_UI(ID_VIEW_RUNZIPSHOW, &CviewpfsView::OnUpdateViewRunzipshow)
END_MESSAGE_MAP()

// CviewpfsView construction/destruction

CviewpfsView::CviewpfsView()
	: m_first( true)
	, m_bHex( false)
	, m_bRunZip( false)
{
}

CviewpfsView::~CviewpfsView()
{
}

BOOL CviewpfsView::PreCreateWindow(CREATESTRUCT& cs)
{
	// TODO: Modify the Window class or styles here by modifying
	//  the CREATESTRUCT cs
	cs.style |= LVS_REPORT|LVS_OWNERDATA;
	return CListView::PreCreateWindow(cs);
}

// CviewpfsView drawing

void CviewpfsView::OnDraw(CDC* /*pDC*/)
{
	CviewpfsDoc* pDoc = GetDocument();
	ASSERT_VALID(pDoc);
	if (!pDoc)
		return;

	// TODO: add draw code for native data here
}

// CviewpfsView message handlers
void CviewpfsView::InsertColumn()
{
	int count = sizeof( g_column) / sizeof( g_column[0]);
	GetListCtrl().InsertColumn( 0, g_column[0], LVCFMT_LEFT, 300);
	for( int i=1; i<count; i++)
	{
		GetListCtrl().InsertColumn( i, g_column[i], LVCFMT_RIGHT, 80);
	}
}

void CviewpfsView::SetItemCount()
{
	CviewpfsDoc* doc = GetDocument();
	if( doc)
	{
		int memorysize = 0;
		if( m_bRunZip)
		{
			const ZIPFILE::CPFSInfoFileReader::CRunFileCentralVector& map = doc->GetFileCentralVector();
			GetListCtrl().SetItemCount( map.size());
			memorysize = map.capacity() * sizeof( ZIPFILE::CRunFileCentral);
		}
		else
		{
			GetListCtrl().SetItemCount( m_cache.size());
			memorysize = m_cache.size() * sizeof( PFS::CMetaInfo );
		}

		((CMainFrame*)AfxGetMainWnd())->UpdateStatusBar( GetListCtrl().GetItemCount(), memorysize);
	}

	Invalidate();
}

void CviewpfsView::FillCache()
{
	m_cache.clear();
	GetListCtrl().SetItemCount( 0 );
	CviewpfsDoc* doc = GetDocument();
	if( doc)
	{
		if( !m_bRunZip)
		{
			ZIPFILE::CPFSInfoFileReader::CFileCentralMap& fcm = doc->m_filereader.GetFileCentralMap();
			ZIPFILE::CPFSInfoFileReader::CFileCentralMap::const_iterator iEnd = fcm.end();

			for( ZIPFILE::CPFSInfoFileReader::CFileCentralMap::const_iterator itor = fcm.begin(); itor != iEnd; ++itor)
			{
				m_cache.push_back( (DWORD)(&(itor->second)));
			}
		}
	}
}

void CviewpfsView::OnInitialUpdate()
{
	CListView::OnInitialUpdate();

	if( m_first)
	{
		InsertColumn();
		//GetListCtrl().SetImageList( &CFileIcon::GetInstance().GetImageList(), LVSIL_SMALL);
		m_first = false;
	}
	else
	{
		FillCache();
		SetItemCount();
	}
}

void CviewpfsView::OnLvnGetdispinfo(NMHDR *pNMHDR, LRESULT *pResult)
{
	NMLVDISPINFO *pDispInfo = reinterpret_cast<NMLVDISPINFO*>(pNMHDR);
	
	LV_ITEM* pItem= &(pDispInfo)->item;
	
	*pResult = 0;
	if( (pItem->mask & LVIF_TEXT) != LVIF_TEXT )	// ×Ö·û´®»º³åÇøÓÐÐ§
	{
		return ;
	}

	// int radix = m_bHex ? 16 : 10;
	size_t index = pItem->iItem;
	const int PAGESIZE_N = 4096;
	const float PAGESIZE_F = 1024.0f;
	if( m_bRunZip)
	{
		CviewpfsDoc* doc = GetDocument();
		if( doc)
		{
			const ZIPFILE::CPFSInfoFileReader::CRunFileCentralVector& rfcv = doc->GetFileCentralVector();
			if( index < rfcv.size())
			{
				const ZIPFILE::CPFSInfoFileCentral& rfc = rfcv[index];
			
				switch( pItem->iSubItem)
				{
				case 0:		// name
					break;
				case 1:		// offset
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%.3f", rfc.GetDataOffset() / PAGESIZE_F);
					break;

				case 2:		// sizeNow
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%.3f", rfc.GetCompressedSize() / PAGESIZE_F);
					break;

				case 3:		// pagecount
					{
						int pagecount = 0;
						if( rfc.GetCompressedSize())
						{
							int nStart = rfc.GetDataOffset();
							int nStartCount = nStart / PAGESIZE_N;

							int nEnd = rfc.GetDataOffset() + rfc.GetCompressedSize();
							int nEndCount = nEnd / PAGESIZE_N;

							pagecount = nEndCount - nStartCount + 1;
							if( ( 0 == nStart % PAGESIZE_N) && ( 0 == nEnd %PAGESIZE_N))
								pagecount--;
						}
						// _ultow_s( pagecount, pItem->pszText, pItem->cchTextMax, radix);
						_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%u", pagecount);
					}
					break;

				case 4:		// sizeOrg
					//_itow_s( fc->m_uiSizeOrg, pItem->pszText, pItem->cchTextMax, radix);
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%.3f", rfc.GetOrginalSize() / PAGESIZE_F);
					break;

				case 5:		// zip radio
					if( rfc.GetOrginalSize() == 0 )
					{
						wcscpy_s( pItem->pszText, pItem->cchTextMax, L"N/A");
					}
					else
					{
						_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%.3f%%", 100.0f * rfc.GetCompressedSize() / rfc.GetOrginalSize());
					}
					break;
				case 6: // hash1
					{
						// _ultow_s( rfc.GetHash1(), pItem->pszText, pItem->cchTextMax, radix);
						if( m_bHex )
							_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%08X", rfc.GetHash1());
						else
							_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%08u", rfc.GetHash1());

					}
					break;
				case 7: // hash2
					{
						// _ultow_s( rfc.GetHash2(), pItem->pszText, pItem->cchTextMax, radix);
						if( m_bHex )
							_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%08X", rfc.GetHash2());
						else
							_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%08u", rfc.GetHash2());
					}
					break;
				default:
					return;
				}
			}
		}
	}
	else
	{
		if( index < m_cache.size())
		{
			ZIPFILE::CPFSInfoFileCentral* fc = ( ZIPFILE::CPFSInfoFileCentral*)m_cache[index];
				
			switch( pItem->iSubItem)
			{
			case 0:		// name
				wcscpy_s( pItem->pszText, pItem->cchTextMax, fc->m_name.c_str());
				break;
			case 1:		// offset
				//_itow_s( fc->m_uiOffset, pItem->pszText, pItem->cchTextMax, radix);
				_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%.3f", fc->m_uiOffset / PAGESIZE_F);
				break;

			case 2:		// sizeNow
				//_itow_s( fc->m_uiSizeNow, pItem->pszText, pItem->cchTextMax, radix);
				_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%.3f", fc->m_uiSizeNow / PAGESIZE_F);
				break;

			case 3:		// pagecount
				{
					int pagecount = 0;
					if( fc->m_uiSizeNow)
					{
						int nStart = fc->m_uiOffset;
						int nStartCount = nStart / PAGESIZE_N;

						int nEnd = fc->m_uiOffset + fc->m_uiSizeNow;
						int nEndCount = nEnd / PAGESIZE_N;
					
						pagecount = nEndCount - nStartCount + 1;
						if( ( 0 == nStart % PAGESIZE_N) && ( 0 == nEnd %PAGESIZE_N))
							pagecount--;
					}
					// _ultow_s( pagecount, pItem->pszText, pItem->cchTextMax, radix);
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%u", pagecount);
				}
				break;
			
			case 4:		// sizeOrg
				//_itow_s( fc->m_uiSizeOrg, pItem->pszText, pItem->cchTextMax, radix);
				_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%.3f", fc->m_uiSizeOrg / PAGESIZE_F);
				break;
		
			case 5:		// zip radio
				if( fc->m_uiSizeOrg == 0 )
				{
					wcscpy_s( pItem->pszText, pItem->cchTextMax, L"N/A");
				}
				else
				{
					_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%.3f%%", 100.0f * fc->m_uiSizeNow / fc->m_uiSizeOrg);
				}
				break;
			case 6: // hash1
				{
					// _ultow_s( rfc.GetHash1(), pItem->pszText, pItem->cchTextMax, radix);
					if( m_bHex )
						_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%08X", fc->GetHash1());
					else
						_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%08u", fc->GetHash1());

				}
				break;
			case 7: // hash2
				{
					// _ultow_s( rfc.GetHash2(), pItem->pszText, pItem->cchTextMax, radix);
					if( m_bHex )
						_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%08X", fc->GetHash2());
					else
						_stprintf_s( pItem->pszText, pItem->cchTextMax, L"%08u", fc->GetHash2());
				}
				break;
			default:
				return;
			}
		}
	}

	*pResult = 1;
}

void CviewpfsView::OnViewHexshow()
{
	m_bHex = !m_bHex;
	Invalidate();
}

void CviewpfsView::OnUpdateViewHexshow(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_bHex);
}

void CviewpfsView::OnViewRunzipshow()
{
	m_bRunZip = !m_bRunZip;
	SetItemCount();
	Invalidate();
}

void CviewpfsView::OnUpdateViewRunzipshow(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_bRunZip);
}
