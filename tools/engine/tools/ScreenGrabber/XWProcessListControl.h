#pragma once
#include "..\libxwnd\xwlistcontrol.h"

class CXWProcessListControl :
	public CXWListControl
{
public:
	CXWProcessListControl(void);
	~CXWProcessListControl(void);

	virtual int OnCreate( LPCREATESTRUCT lpCreateStruct );

	BOOL InitData();

	//
	// 窗口消息路由/拦截函数(可重载,拦截更多消息)
	//
	virtual BOOL OnWndMsg( UINT message, WPARAM wParam, LPARAM lParam, LRESULT* pResult );


	BOOL UpdateView();
};
