#pragma once
#include "..\libxwnd\xwtreecontrol.h"

class CXWProcessTreeControl :
	public CXWTreeControl
{
public:
	CXWProcessTreeControl(void);
	~CXWProcessTreeControl(void);

	virtual int OnCreate( LPCREATESTRUCT lpCreateStruct );
};
