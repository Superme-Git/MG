#pragma once
#include "..\libxwnd\xwframewnd.h"
#include "XWProcessTreeControl.h"
#include "XWProcessListControl.h"

class CTaskExplorerFrameWnd :
	public CXWFrameWnd
{
public:
	CTaskExplorerFrameWnd(void);
	~CTaskExplorerFrameWnd(void);

	virtual int OnCreate( LPCREATESTRUCT lpCreateStruct );
	virtual BOOL OnCommand( WPARAM wParam, LPARAM lParam );
	virtual void OnDestroy( );
	virtual void OnSize(UINT nType, int cx, int cy);
private:

	CXWProcessTreeControl m_treeCtrl;

	CXWProcessListControl m_listCtrl;
};
