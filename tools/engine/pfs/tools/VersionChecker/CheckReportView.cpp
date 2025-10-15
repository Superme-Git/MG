// CheckReportView.cpp : implementation file
//

#include "stdafx.h"
#include "VersionChecker.h"
#include "CheckReportView.h"
#include "GlobalNotification.h"

#include <sstream>

// CCheckReportView

IMPLEMENT_DYNCREATE(CCheckReportView, CEditView)

CCheckReportView::CCheckReportView()
{

}

CCheckReportView::~CCheckReportView()
{
}

BEGIN_MESSAGE_MAP(CCheckReportView, CEditView)
	ON_MESSAGE( WM_GLOBALSTATUS, OnGlobalStatus)
	ON_WM_CREATE()
END_MESSAGE_MAP()


// CCheckReportView message handlers

LRESULT CCheckReportView::OnGlobalStatus( WPARAM wParam, LPARAM lParam )
{
	MessageId mid = (MessageId )lParam;
	Message rMessage;
	if ( GlobalGetMessage( mid, &rMessage) )
	{
		int nLength = GetWindowTextLength();
		GetEditCtrl().SetSel( nLength, nLength, FALSE );
		
		if ( rMessage.dwStatusCode != 0 )
		{
			std::wostringstream oss;
			oss << rMessage.strDetail << L"(" << (int) rMessage.dwStatusCode << L")\r\n";
			GetEditCtrl().ReplaceSel( oss.str().c_str() );
		}
		else
		{
			rMessage.strDetail.append( L"\r\n" );
			GetEditCtrl().ReplaceSel( rMessage.strDetail.c_str() );
		}
	}

	return 1;
}

int CCheckReportView::OnCreate(LPCREATESTRUCT lpCreateStruct)
{
	if (CEditView::OnCreate(lpCreateStruct) == -1)
		return -1;

	GetEditCtrl().SetLimitText( (UINT)-1 );
	CFont font;
	HGDIOBJ hFont = GetStockObject ( DEFAULT_GUI_FONT );
	font.Attach( hFont );
	GetEditCtrl().SetFont( &font );
	GetEditCtrl().SetReadOnly(TRUE);

	return 0;
}
