#include "StdAfx.h"
#include "TextFileViewer.h"

struct StreamContext
{
	LPBYTE	pbBuffer;
	LONG	cbRemaining;
	LONG	cbOffset;
};

static DWORD CALLBACK BinaryStreamCallback( DWORD dwCookie, LPBYTE pbBuff, LONG cb, LONG *pcb)
{
	StreamContext* data = (StreamContext*) dwCookie;
	if( data->cbRemaining >= cb)
	{
		memcpy( pbBuff, data->pbBuffer+data->cbOffset, cb);
		data->cbOffset += cb;
		data->cbRemaining -= cb;
		*pcb = cb;
	}
	else
	{
		*pcb = data->cbRemaining;
		memcpy( pbBuff, data->pbBuffer+data->cbOffset, *pcb);
		data->cbRemaining = 0;
		data->cbOffset += *pcb;
	}
	return 0;
}


CTextFileViewer::CTextFileViewer(void)
{
}

CTextFileViewer::~CTextFileViewer(void)
{
}

BEGIN_MESSAGE_MAP( CTextFileViewer, CFileViewer )
	ON_COMMAND(ID_EDIT_COPY, &CTextFileViewer::OnEditCopy)
	ON_UPDATE_COMMAND_UI(ID_EDIT_COPY, &CTextFileViewer::OnUpdateEditCopy)
END_MESSAGE_MAP()

void CTextFileViewer::OnEditCopy()
{
	m_wndEditPanel.Copy();	
}

void CTextFileViewer::OnUpdateEditCopy(CCmdUI *pCmdUI)
{
	pCmdUI->Enable();
}

bool CTextFileViewer::Create( CWnd* parent)
{
	CRect rc;
	parent->GetClientRect( &rc);

	if( !m_wndEditPanel.CreateEx( WS_EX_CLIENTEDGE, 
		WS_CHILD | WS_VISIBLE | ES_MULTILINE | ES_READONLY|WS_VSCROLL, rc, parent, AFX_IDW_PANE_FIRST ) )
	{
		return false;
	}

	// 设置Edit控件的字体
	HGDIOBJ hFont = ::GetStockObject( DEFAULT_GUI_FONT );
	m_wndEditPanel.SendMessage( WM_SETFONT, (WPARAM)(LONG_PTR)hFont, 0 );
	return true;
}

bool CTextFileViewer::SetData( const void* pData, size_t len )
{
	// Unicode 检测
	INT nFormat = IS_TEXT_UNICODE_CONTROLS | IS_TEXT_UNICODE_STATISTICS | IS_TEXT_UNICODE_SIGNATURE;
	BOOL bUnicode = IsTextUnicode( pData, len, &nFormat );
	if ( bUnicode || (nFormat & IS_TEXT_UNICODE_SIGNATURE) != 0 ) 
	{
		bUnicode = TRUE;
	}

	StreamContext sc;
	sc.cbOffset = 0;
	if( nFormat & IS_TEXT_UNICODE_SIGNATURE )
	{
		sc.pbBuffer = ((LPBYTE)pData)+2;
		sc.cbRemaining = (LONG)len-2;
	}
	else
	{
		sc.pbBuffer = (LPBYTE)pData;
		sc.cbRemaining = (LONG)len;
	}

	EDITSTREAM es;
	es.dwCookie = (DWORD)( &sc);
	es.pfnCallback = BinaryStreamCallback;
	if( bUnicode )
	{
		m_wndEditPanel.StreamIn( SF_TEXT|SF_UNICODE, es);
	}
	else
	{
		m_wndEditPanel.StreamIn( SF_TEXT, es);
	}
	return true;
}