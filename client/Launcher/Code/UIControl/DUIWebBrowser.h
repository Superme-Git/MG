#ifndef DUIWebBrowser_H
#define DUIWebBrowser_H
#include "../stdafx.h"
#include "DUIBase.h"
#include "../WebBrowser/WebBrowser.h"
using namespace std;
class DUIWebBrowser : public DUIBase
{
public:

	HWND m_hWndText;
	HDC m_hDCText;
	WebBrowser* m_WB;

	DUIWebBrowser(DUIBase* pParent, wstring wsName) :DUIBase(pParent, wsName)
	{
		m_Type = DUIType_WebBrowser;
		m_WB = NULL;
	}
	~DUIWebBrowser()
	{
		if (m_WB)
		{
			delete m_WB;
		}
	}
	int Init(int iX, int iY, unsigned int uiWidth, unsigned int uiHeight, wstring wsURL, HINSTANCE hInstance, wstring wsRandomStr = L"")
	{
		m_iX = iX;
		m_iY = iY;
		m_uiWidth = uiWidth;
		m_uiHeight = uiHeight;
	
		m_hWndText = CreateWindow(L"STATIC", L"", WS_CHILD | WS_VISIBLE, m_iX, m_iY, m_uiWidth, m_uiHeight, GetParentWindow()->GetBGHandle(), (HMENU)this, hInstance, NULL);
		m_hDCText = GetDC(m_hWndText);
		m_WB = new WebBrowser(m_hWndText);
		if (!m_WB->CreateWebBrowser())
		{
			return FALSE;
		}
		if (wsURL != L"")
		{
			if (wsRandomStr != L"")
			{
				wsRandomStr += GetRandomNumStr();
				wsURL += wsRandomStr;
			}
		}
		m_WB->Navigate(wsURL.c_str());
		return 0;
	}
	virtual int DrawMeBefor(HDC hdcBG)
	{
		//ShowWindow(m_hWndText, SW_HIDE);
		//ShowWindow(m_hWndText, SW_SHOW);
		StretchBlt(hdcBG, m_iX, m_iY, m_uiWidth, m_uiHeight, m_hDCText, 0, 0, m_uiWidth, m_uiHeight, SRCCOPY);
		return 0;
	}
	//virtual int OnResize()
	//{
	//}
};
#endif