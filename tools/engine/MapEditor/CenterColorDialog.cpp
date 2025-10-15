#include "StdAfx.h"
#include "CenterColorDialog.h"

BOOL CCenterColorDialog::OnInitDialog()
{
	CColorDialog::OnInitDialog();

	
	CRect rect;
	GetWindowRect(&rect);
	int width = rect.Width();
	int height = rect.Height();
	::SetWindowPos(m_hWnd,HWND_TOP,(GetSystemMetrics(SM_CXSCREEN) - width) / 2,
	(GetSystemMetrics(SM_CYSCREEN) - height) / 2, width, height, 0);

	return TRUE;  
	
}
