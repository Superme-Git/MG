#pragma once
#include "..\libxwnd\XWOwnerDrawedWnd.h"

class CScreenshotWnd :
	public CXWOwnerDrawedWnd
{
public:
	CScreenshotWnd(void);
	~CScreenshotWnd(void);

	BOOL CaptureScreen();

protected:
	void OnDraw( HDC hDC );
	int OnCreate(LPCREATESTRUCT lpCreateStruct );

	HBITMAP m_hBitmap;

};
