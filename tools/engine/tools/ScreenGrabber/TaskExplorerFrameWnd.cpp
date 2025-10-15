#include "StdAfx.h"
#include "TaskExplorerFrameWnd.h"
#include "resource.h"

CTaskExplorerFrameWnd::CTaskExplorerFrameWnd(void)
{
}

CTaskExplorerFrameWnd::~CTaskExplorerFrameWnd(void)
{
}

int CTaskExplorerFrameWnd::OnCreate( LPCREATESTRUCT lpCreateStruct )
{
	if( __super::OnCreate(lpCreateStruct) != 0 )
	{
		return -1;
	}

	BOOL bRet = m_treeCtrl.Create( 
		L"Process Tree", 0,0,
		 min( 100, lpCreateStruct->cx), lpCreateStruct->cy,
		GetSafeHwnd(), 263 );
	if( !bRet )
	{
		return -1;
	}

	return 0;
}

BOOL CTaskExplorerFrameWnd::OnCommand( WPARAM wParam, LPARAM lParam )
{
	if ( wParam == ID_TASKEXPLORER )
	{
		//m_listCtrl.UpdateView();
		return TRUE;
	}

	return FALSE;
}

void CTaskExplorerFrameWnd::OnDestroy( )
{

}


void CTaskExplorerFrameWnd::OnSize(UINT nType, int cx, int cy)
{

	if( nType != SIZE_MINIMIZED &&
		nType != SIZE_MAXSHOW && 
		nType != SIZE_MAXHIDE )
	{
		m_treeCtrl.MoveWindow( 0, 0, min( 200, cx ), cy);
	}

	//if( nType != SIZE_MINIMIZED &&
	//	nType != SIZE_MAXSHOW && 
	//	nType != SIZE_MAXHIDE )
	//{
	//	m_listCtrl.MoveWindow( 0, 0, cx, cy);
	//}

	return __super::OnSize( nType, cx , cy );
}