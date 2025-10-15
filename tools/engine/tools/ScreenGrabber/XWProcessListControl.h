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
	// ������Ϣ·��/���غ���(������,���ظ�����Ϣ)
	//
	virtual BOOL OnWndMsg( UINT message, WPARAM wParam, LPARAM lParam, LRESULT* pResult );


	BOOL UpdateView();
};
