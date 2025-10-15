#ifndef DUIWindow_H
#define DUIWindow_H
#include "../stdafx.h"
#include "../../resource.h"
#include "../Utils/SystemUtil.h"
#include "DUIBase.h"

using namespace std;
using namespace Gdiplus;
class DUIWindow : public DUIBase
{
public:
	HWND m_hWnd;
	HDC m_hdcWindow;
	HDC m_hdcBG;
	int m_iNeedRedraw;
	bool m_bOver;
	int m_iMainWindow;

	HDC hdcTemp;
	HDC hdcMemory;
	HBITMAP hBitMap;
	HBITMAP hBitOld;

	//vector<HWND> m_WebBrowserParentStaticArr;

	DUIWindow(DUIBase* pParent, wstring wsName) :DUIBase(pParent, wsName)
	{
		m_Type = DUIType_Window;
		m_hWnd = 0;
		m_hdcWindow = 0;
		m_hdcBG = 0;
		m_iNeedRedraw = 0;
		m_bOver = false;
		m_iMainWindow = 0;
	}
	~DUIWindow()
	{
		SelectObject(hdcMemory, hBitOld);
		DeleteObject(hBitMap);
		DeleteDC(hdcMemory);
	}
	int Init(HINSTANCE hInstance, wstring wsClassName, wstring wsTitle, unsigned int uiStyle, unsigned int uiWidth, unsigned int uiHeight, wstring wsRgnFileName, int iWindowPosType = 0, int iX = 0, int iY = 0, int iMainWindow = 0)
	{
		m_bOver = false;
		m_uiWidth = uiWidth;
		m_uiHeight = uiHeight;
		m_iMainWindow = iMainWindow;
		unsigned int uiWidthScreen = GetSystemMetrics(SM_CXFULLSCREEN);
		unsigned int uiHeightScreen = GetSystemMetrics(SM_CYFULLSCREEN);
		int iXNew = iX;
		int iYNew = iY;
		if (iWindowPosType == 1)
		{
			if (uiWidthScreen >= m_uiWidth)
			{
				iXNew = (uiWidthScreen - m_uiWidth) / 2;
			}
			else
			{
				iXNew = (m_uiWidth - uiWidthScreen) / 2;
			}
			if (uiHeightScreen > m_uiHeight)
			{
				iYNew = (uiHeightScreen - m_uiHeight) / 2;
			}
			else
			{
				iYNew = (m_uiHeight - uiHeightScreen) / 2;
			}
		}
		m_hWnd = CreateWindow(wsClassName.c_str(), wsTitle.c_str(), uiStyle, iXNew, iYNew, m_uiWidth, m_uiHeight, NULL, NULL, hInstance, NULL);
		if (m_hWnd)
		{
			m_hdcWindow = GetDC(m_hWnd);
			//ShowWindow(m_hWnd, iCmdShow);
			UpdateWindow(m_hWnd);

			hdcTemp = GetDC(0);
			hdcMemory = CreateCompatibleDC(hdcTemp);
			hBitMap = CreateCompatibleBitmap(hdcTemp, uiWidth, uiHeight);
			hBitOld = (HBITMAP)SelectObject(hdcMemory, hBitMap);
			m_hdcBG = hdcMemory;

			if (wsRgnFileName != L"")
			{
				HRGN hRgnSrc;
				LoadRgnFormFile(wsRgnFileName, hRgnSrc);
				SetWindowRgn(m_hWnd, hRgnSrc, true);
				DeleteObject(hRgnSrc);
			}
			return 0;
		}
		return -1;
	}
	virtual HWND GetBGHandle()
	{
		return m_hWnd;
	}
	virtual HDC GetBGDC()
	{
		return m_hdcBG;
	}
	virtual int UpdateMeAfter()
	{
		if (GetNeedRedraw() > 0)
		{
			if (!m_bOver)
			{
				SendMessage(m_hWnd, WM_PAINT, 0, 0);
				//Redraw();
			}
		}
		return 0;
	}
	virtual int DrawMeAfter(HDC hdcBG)
	{
		BitBlt(m_hdcWindow, 0, 0, m_uiWidth, m_uiHeight, m_hdcBG, 0, 0, SRCCOPY);
		return 0;
	}
	virtual int AddNeedRedraw()
	{
		return ++m_iNeedRedraw;
	}
	virtual void SetNeedRedraw(int iNeedRedraw)
	{
		m_iNeedRedraw = iNeedRedraw;
	}
	virtual int GetNeedRedraw()
	{
		return m_iNeedRedraw;
	}
	virtual int OnLButtonDownMe(int iX, int iY)
	{
		ReleaseCapture();
		SendMessage(m_hWnd, WM_NCLBUTTONDOWN, 2, 0);
		//for (int i = 0; i < m_WebBrowserParentStaticArr.size(); i++)
		//{
		//	ShowWindow(m_WebBrowserParentStaticArr[i], SW_HIDE);
		//	ShowWindow(m_WebBrowserParentStaticArr[i], SW_SHOW);
		//}
		return 0;
	}
	//void AddWebBrowserParentStatic(HWND hWndStatic)
	//{
	//	m_WebBrowserParentStaticArr.push_back(hWndStatic);
	//}
	virtual void CloseMe()
	{
		DestroyWindow(m_hWnd);
	}

	virtual LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
	{
		switch (message)
		{
		case WM_PAINT:
		{
			HDC hdc;
			PAINTSTRUCT ps;
			hdc = BeginPaint(hWnd, &ps);
			Redraw();
			EndPaint(hWnd, &ps);
			DefWindowProc(hWnd, message, wParam, lParam);
		}
		break;
		case WM_LBUTTONDOWN:
		{
			int iResult = 0;
			int iPosX = LOWORD(lParam);
			int iPosY = HIWORD(lParam);
			OnLButtonDown(iPosX, iPosY);
			DefWindowProc(hWnd, message, wParam, lParam);
		}
		break;
		case WM_DESTROY:
		{
			m_bOver = true;
			DefWindowProc(hWnd, message, wParam, lParam);
		}
		break;
		default:
			return DefWindowProc(hWnd, message, wParam, lParam);
		}
		return 0;
	}
};
#endif