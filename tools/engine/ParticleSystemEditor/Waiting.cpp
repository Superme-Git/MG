// Waiting.cpp : implementation file
//

#include "stdafx.h"
#include "Waiting.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CWaiting
CString _strWaiting	= L"First Initialize ";
CWaiting::CWaiting()
{
	m_nMax = 1;
	m_nPos = 0;
}

CWaiting::~CWaiting()
{
}


BEGIN_MESSAGE_MAP(CWaiting, CWnd)
	//{{AFX_MSG_MAP(CWaiting)
	ON_WM_PAINT()
	ON_WM_CREATE()
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()


/////////////////////////////////////////////////////////////////////////////
// CWaiting message handlers
BOOL CWaiting::Create( CWnd* pParentWnd)
{
	CRect rt;
	int cx	=	GetSystemMetrics(SM_CXFULLSCREEN);
	int cy	=	GetSystemMetrics(SM_CYFULLSCREEN);
	rt.left		= cx/2-100;
	rt.right	= cx/2+100;
	rt.top		= cy/2-10;
	rt.bottom	= cy/2+10;
	if(!pParentWnd)
	{
		pParentWnd = AfxGetMainWnd();
	}


	LPCTSTR	lpszClassName = AfxRegisterWndClass( CS_HREDRAW|CS_VREDRAW|CS_GLOBALCLASS|CS_DBLCLKS, AfxGetApp()->LoadStandardCursor(IDC_ARROW) );
	return CreateEx(NULL, lpszClassName, L"Waiting",
		WS_POPUPWINDOW|DS_CENTER,
		rt.left, rt.top,
		rt.right - rt.left, rt.bottom - rt.top,
		pParentWnd==NULL ? NULL:pParentWnd->GetSafeHwnd(), NULL, NULL );
}

int CWaiting::OnCreate(LPCREATESTRUCT lpCreateStruct) 
{
	if (CWnd::OnCreate(lpCreateStruct) == -1)
		return -1;
	
	// TODO: Add your specialized creation code here
	SetWindowPos( &wndTopMost, 0, 0, 0, 0, SWP_NOSIZE|SWP_NOMOVE);
	return 0;
}

void CWaiting::SetRange(int nMax)
{
	m_nMax = nMax;
	if(m_nMax == 0)
		m_nMax = 100;
}

void CWaiting::SetPos(int nPos)
{
	m_nPos = nPos;	
	if(IsWindow(GetSafeHwnd()))
	{
		Invalidate();
		UpdateWindow();
	}
}


void CWaiting::OnPaint() 
{
	CPaintDC dc(this); // device context for painting
	
	// TODO: Add your message handler code here
	CRect rt;
	GetClientRect(&rt);

	dc.FillSolidRect(&rt, RGB(0, 0, 255));
	float fRatio = m_nPos/(float)m_nMax*100;
	CString str;
	str.Format(L"%s%2.0f%%", _strWaiting, fRatio);
	dc.SetBkMode(TRANSPARENT);
	dc.SetTextColor(RGB(255, 255, 255));
	dc.DrawText(str, &rt, DT_TOP|DT_CENTER);
	// Do not call CWnd::OnPaint() for painting messages
}

