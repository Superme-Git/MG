#include "StdAfx.h"
#include "XWProcessTreeControl.h"

CXWProcessTreeControl::CXWProcessTreeControl(void)
{
}

CXWProcessTreeControl::~CXWProcessTreeControl(void)
{
}

int CXWProcessTreeControl::OnCreate( LPCREATESTRUCT lpCreateStruct )
{
	if( __super::OnCreate(lpCreateStruct) != 0 )
	{
		return -1;
	}
	// CreateWindow时设置LVS_REPORT不起作用，只好这里设置
	LONG lStyle = GetWindowLong( GetSafeHwnd(), GWL_STYLE );
	SetWindowLong(GetSafeHwnd(), GWL_STYLE, lStyle | TVS_LINESATROOT | TVS_HASLINES | TVS_HASBUTTONS );
	
	HWND hTree = GetSafeHwnd();
	TreeView_SetUnicodeFormat( hTree, TRUE );

	TVINSERTSTRUCT tvis = {0};
	tvis.hParent = NULL;
	tvis.hInsertAfter = TVI_ROOT;
	tvis.itemex.hItem = NULL;
	tvis.itemex.mask = TVIF_TEXT | TVIF_CHILDREN /*| TVIF_STATE*/;
	tvis.itemex.pszText = L"Root";
	tvis.itemex.cchTextMax = 64;
	tvis.itemex.cChildren = 1;
	tvis.itemex.state = TVIS_BOLD;
	tvis.itemex.stateMask = TVIS_BOLD;

	HTREEITEM hRoot = TreeView_InsertItem( hTree,  &tvis );

	int nError = GetLastError();

	if( hRoot != NULL )
	{
		TVINSERTSTRUCT tvis2 = {0};
		tvis2.hParent = hRoot;
		tvis2.hInsertAfter = TVI_FIRST;
		tvis2.itemex.hItem = NULL;
		tvis2.itemex.mask = TVIF_TEXT | TVIF_CHILDREN;
		tvis2.itemex.pszText = L"Child1";
		tvis2.itemex.cchTextMax = 64;
		tvis2.itemex.cChildren = 0;
		tvis2.itemex.state = 0;
		tvis2.itemex.stateMask = 0;
		TreeView_InsertItem( hTree,  &tvis2 );
	}

	return 0;
}