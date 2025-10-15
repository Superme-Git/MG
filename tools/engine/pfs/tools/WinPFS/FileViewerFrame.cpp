// FileViewerFrame.cpp : implementation file
//

#include "stdafx.h"
#include "WinPFS.h"
#include "FileViewerFrame.h"
#include <string>
#include "TextFileViewer.h"
#include "XmlFileViewer.h"
#include "BinaryFileViewer.h"
#include "ImageFileViewer.h"
#include "FilesMetaFileViewer.h"

#include "../../src/zip/ziphelper.h" // TODO

static UINT indicators[] =
{
	ID_SEPARATOR,           // 状态行指示器
	ID_INDICATOR_ITEMCOUNT,
};


CFileViewerMap::CFileViewerMap()
{

}

CFileViewerMap::~CFileViewerMap()
{
	for( iterator i=begin(), e=end(); i!=e; ++i)
	{
		delete i->second;
	}
}

CFileViewer* CFileViewerMap::CreateFileViewer( DWORD format, CWnd* parent)
{
	iterator i = find( format);
	if( i != end())
	{
		return i->second;
	}
	CFileViewer* viewer = NULL;
	switch( format)
	{
	case FVDF_TEXT:
	case FVDF_UNICODETEXT:
		viewer = new CTextFileViewer;
		break;
	case FVDF_XML:
	case FVDF_OCTETS_XML:
		viewer = new CXmlFileViewer;
		break;
	case FVDF_FILES_META:
		viewer = new CFilesMetaFileViewer;
		break;
	case FVDF_IMAGE_BMP:
	case FVDF_IMAGE_TGA:
	case FVDF_IMAGE_JPG:
	case FVDF_IMAGE_DDS:
		viewer = new CImageFileViewer;
		break;
	default:
		viewer = new CBinaryFileViewer;
		break;
	}
	if( NULL == viewer)
	{
		return NULL;
	}
	if( !viewer->Create( parent))
	{
		delete viewer;
		return NULL;
	}
	insert( std::make_pair( format, viewer));
	return viewer;
}

// CFileViewerFrame

IMPLEMENT_DYNCREATE(CFileViewerFrame, CFrameWnd)

CFileViewerFrame::CFileViewerFrame()
: m_curViewer( NULL)
, m_format( FVDF_RAW)
{
	m_bHexMode = FALSE;
	m_bTryUnzip = FALSE;
}

CFileViewerFrame::~CFileViewerFrame()
{
}

BEGIN_MESSAGE_MAP(CFileViewerFrame, CFrameWnd)
	ON_WM_CREATE()
	ON_COMMAND(ID_VIEW_AS_BINARY, &OnViewHexMode)
	ON_UPDATE_COMMAND_UI(ID_VIEW_AS_BINARY, &OnUpdateViewHexMode)
	ON_COMMAND(ID_VIEW_AS_TEXT, &OnViewTextMode)
	ON_UPDATE_COMMAND_UI(ID_VIEW_AS_TEXT, &OnUpdateViewTextMode)
	ON_COMMAND(ID_VIEW_AS_XML, &OnViewXmlMode)
	ON_UPDATE_COMMAND_UI(ID_VIEW_AS_XML, &OnUpdateViewXmlMode)
	ON_COMMAND(ID_VIEW_AS_FILES_META, &OnViewFilesMeta)
	ON_UPDATE_COMMAND_UI(ID_VIEW_AS_FILES_META, &OnUpdateViewFilesMeta)
	ON_COMMAND(ID_NEED_UNZIPPED, &OnTryUnzip)
	ON_UPDATE_COMMAND_UI(ID_NEED_UNZIPPED, &OnUpdateTryUnzip)
END_MESSAGE_MAP()

void CFileViewerFrame::OnTryUnzip()
{
	m_bTryUnzip = !m_bTryUnzip;
	ShowFormatData( m_format );
}

void CFileViewerFrame::OnUpdateTryUnzip(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_bTryUnzip );
}

void CFileViewerFrame::OnViewHexMode()
{
	m_format = FVDF_RAW;
	ShowFormatData( m_format);
	//Invalidate();
}

void CFileViewerFrame::OnUpdateViewHexMode(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_format == FVDF_RAW );
}

void CFileViewerFrame::OnViewTextMode()
{
	m_format = FVDF_TEXT;
	ShowFormatData( m_format);
}

void CFileViewerFrame::OnUpdateViewTextMode(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_format == FVDF_TEXT );
}

void CFileViewerFrame::OnViewXmlMode()
{
	m_format = FVDF_XML;
	ShowFormatData( m_format);
}

void CFileViewerFrame::OnUpdateViewXmlMode(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_format == FVDF_XML );
}

void CFileViewerFrame::OnViewFilesMeta()
{
	m_format = FVDF_FILES_META;
	ShowFormatData( m_format);
}

void CFileViewerFrame::OnUpdateViewFilesMeta(CCmdUI *pCmdUI)
{
	pCmdUI->SetCheck( m_format == FVDF_FILES_META );
}

BOOL CFileViewerFrame::PreCreateWindow(CREATESTRUCT& cs)
{
	if( !CFrameWnd::PreCreateWindow(cs) )
		return FALSE;

	cs.style = WS_CAPTION | WS_MAXIMIZEBOX | WS_SYSMENU;
	cs.dwExStyle &= ~WS_EX_CLIENTEDGE;

	return TRUE;
}

// CFileViewerFrame message handlers

int CFileViewerFrame::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CFrameWnd::OnCreate(lpCreateStruct) == -1)
		return -1;

	if (!m_wndToolBar.CreateEx(this, 
		TBSTYLE_FLAT|TBSTYLE_AUTOSIZE, WS_CHILD | WS_VISIBLE | CBRS_TOP
		| CBRS_GRIPPER | CBRS_TOOLTIPS | CBRS_FLYBY | CBRS_SIZE_DYNAMIC
		) ||
		!m_wndToolBar.LoadToolBar(IDR_FILEVIEWER))
	{
		TRACE0("未能创建工具栏\n");
		return -1;      // 未能创建
	}

	if (!m_wndStatusBar.Create(this) ||
		!m_wndStatusBar.SetIndicators(indicators,
		sizeof(indicators)/sizeof(UINT)))
	{
		TRACE0("未能创建状态栏\n");
		return -1;      // 未能创建
	}

	// 支持快捷键(Ctrl+C 等)
	LoadAccelTable( MAKEINTRESOURCE(IDR_MAINFRAME) );

	return 0;
}

BOOL CFileViewerFrame::OnCreateClient(LPCREATESTRUCT lpcs, CCreateContext* pContext)
{
	// 创建Edit控件作为Client
	return TRUE;
}

BOOL CFileViewerFrame::ShowText( LPCTSTR lpszText )
{
	return TRUE;
}

BOOL CFileViewerFrame::ShowData( const void* pData, size_t len )
{
	m_buffer.Clear();
	m_buffer.Assign( pData, len);
	return ShowFormatData( m_format );
}

BOOL CFileViewerFrame::ShowFormatData( DWORD dwFormatFlags )
{
	// Unicode 检测
	CFileViewer* viewer = m_viewers.CreateFileViewer( dwFormatFlags, this);
	if( viewer)
	{
		if( m_bTryUnzip )
		{
			// 显示数据内容之前首先尝试对数据进行解压缩，将压缩之后的数据送交FileViewer去显示
			try
			{
				ZIPFILE::helper::CZipStreamInflate unzipper;
				if( unzipper.Inflate( m_buffer, m_bufferUnzipped ) > 0 )
				{
					viewer->SetData( m_bufferUnzipped.GetData(), m_bufferUnzipped.GetSize());
				}
				else
				{
					viewer->SetData( m_buffer.GetData(), m_buffer.GetSize());
				}
			}
			catch( ZIPFILE::helper::CZipStreamException& )
			{
				// 无效的压缩数据，显示原始数据
				viewer->SetData( m_buffer.GetData(), m_buffer.GetSize());
			}
		}
		else
		{
			viewer->SetData( m_buffer.GetData(), m_buffer.GetSize());
		}
		if( m_curViewer)
		{
			m_curViewer->Show( false);
		}
		m_curViewer = viewer;
		m_curViewer->Show( true);
		RecalcLayout();
	}
	else
	{
		if( m_curViewer)
		{
			m_curViewer->SetData( m_buffer.GetData(), m_buffer.GetSize());
		}
	}
	return FALSE;
}

BOOL CFileViewerFrame::PreTranslateMessage(MSG* pMsg)
{
	if( pMsg->message == WM_KEYDOWN && pMsg->wParam == VK_ESCAPE)
	{
		// 按 Esc 关闭窗口
		PostMessage( WM_CLOSE );
		return TRUE;
	}
	return CFrameWnd::PreTranslateMessage(pMsg);
}

BOOL CFileViewerFrame::OnCmdMsg(UINT nID, int nCode, void* pExtra, AFX_CMDHANDLERINFO* pHandlerInfo)
{
	if( m_curViewer)
	{
		// 消息路由到视图
		if( m_curViewer->OnCmdMsg(nID, nCode, pExtra, pHandlerInfo) )
		{
			return TRUE;
		}
	}
	return CFrameWnd::OnCmdMsg(nID, nCode, pExtra, pHandlerInfo);
}
