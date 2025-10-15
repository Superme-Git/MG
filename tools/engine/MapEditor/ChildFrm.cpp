
#include "stdafx.h"
#include "MapEditor.h"

#include "ChildFrm.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif




IMPLEMENT_DYNCREATE(CChildFrame, CMDIChildWnd)

#ifdef _DEBUG
void CChildFrame::AssertValid() const
{
	CMDIChildWnd::AssertValid();
}

void CChildFrame::Dump(CDumpContext& dc) const
{
	CMDIChildWnd::Dump(dc);
}

#endif 

BOOL CChildFrame::PreCreateWindow(CREATESTRUCT& cs)
{

	if( !CMDIChildWnd::PreCreateWindow(cs) )
		return FALSE;



	return TRUE;
}

CChildFrame::~CChildFrame()
{
}

CChildFrame::CChildFrame()
{
}

BEGIN_MESSAGE_MAP(CChildFrame, CMDIChildWnd)
END_MESSAGE_MAP()

