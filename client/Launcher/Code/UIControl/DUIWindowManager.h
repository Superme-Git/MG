#ifndef DUIWindowManager_H
#define DUIWindowManager_H

#include "DUIWindow.h"
class DUIWindowManager;
DUIWindowManager* GetDUIWindowManager();
class DUIWindowManager
{
public:
	vector<wstring> m_ClassNames;
	map<HWND, DUIWindow*> m_Items;
	DUIWindowManager()
	{
	}
	~DUIWindowManager()
	{
	}
	DUIWindow* FindItemByHWND(HWND hWnd)
	{
		map<HWND, DUIWindow*>::iterator it = m_Items.find(hWnd);
		if (it != m_Items.end())
		{
			return it->second;
		}
		return NULL;
	}
	DUIWindow* AddItem(DUIBase* pParent, wstring wsName, HINSTANCE hInstance, wstring wsClassName, wstring wsTitle, unsigned int uiStyle, unsigned int uiWidth, unsigned int uiHeight, wstring wsRgnFileName = L"", int iWindowPosType = 0, int iX = 0, int iY = 0, int iMainWindow = 0)
	{
		DUIWindow* pResult = new DUIWindow(NULL, wsName);
		pResult->Init(hInstance, wsClassName, wsTitle, uiStyle, uiWidth, uiHeight, wsRgnFileName, iWindowPosType, iX, iY, iMainWindow);
		if (!pResult->m_hWnd)
		{
			delete pResult;
			pResult = NULL;
		}
		else
		{
			m_Items[pResult->m_hWnd] = pResult;
		}
		return pResult;
	}
	DUIWindow* FindItem(wstring wsName)
	{
		map<HWND, DUIWindow*>::iterator it = m_Items.begin();
		while (it != m_Items.end())
		{
			if (it->second)
			{
				if (it->second->GetName() == wsName)
				{
					return it->second;
				}
			}
			it++;
		}
		return NULL;
	}
	DUIWindow* FindMainWindow()
	{
		map<HWND, DUIWindow*>::iterator it = m_Items.begin();
		while (it != m_Items.end())
		{
			if (it->second)
			{
				if (it->second->m_iMainWindow != 0)
				{
					return it->second;
				}
			}
			it++;
		}
		return NULL;
	}
	void Run()
	{
		int iMainWindow = 0;
		map<HWND, DUIWindow*>::iterator it = m_Items.begin();
		while (it != m_Items.end())
		{
			if (it->second)
			{
				if (it->second->m_bOver == true)
				{
					if (it->second->m_iMainWindow != 0)
					{
						iMainWindow = 1;
					}
					delete it->second;
					it->second = NULL;
				}
				else
				{
					it->second->Update();
				}
			}
			it++;
		}
		if (iMainWindow != 0)
		{
			while (it != m_Items.end())
			{
				if (it->second)
				{
					delete it->second;
					it->second = NULL;
				}
				it++;
			}
			PostQuitMessage(0);
		}
	}
	static ATOM RegisterClass(HINSTANCE hInstance, wstring wsClassName, int iIcon, int iIconSm, WNDPROC lpfnWndProc)
	{
		WNDCLASSEX wcex;
		wcex.cbSize = sizeof(WNDCLASSEX);
		wcex.style = CS_HREDRAW | CS_VREDRAW;
		wcex.lpfnWndProc = lpfnWndProc;
		wcex.cbClsExtra = 0;
		wcex.cbWndExtra = 0;
		wcex.hInstance = hInstance;
		wcex.hIcon = LoadIcon(hInstance, MAKEINTRESOURCE(iIcon));//IDI_LAUNCHER
		wcex.hIconSm = LoadIcon(hInstance, MAKEINTRESOURCE(iIconSm));//IDI_SMALL
		wcex.hCursor = LoadCursor(NULL, IDC_ARROW);
		wcex.hbrBackground = (HBRUSH)(COLOR_WINDOW + 1);
		wcex.lpszMenuName = NULL;
		wcex.lpszClassName = wsClassName.c_str();
		return RegisterClassEx(&wcex);
	}
	static LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
	{
		DUIWindow* pItem = GetDUIWindowManager()->FindItemByHWND(hWnd);
		if (pItem)
		{
			return pItem->WndProc(hWnd, message, wParam, lParam);
		}
		return DefWindowProc(hWnd, message, wParam, lParam);
	}
};
static DUIWindowManager* gs_pDUIWindowManager = NULL;
inline DUIWindowManager* GetDUIWindowManager()
{
	if (!gs_pDUIWindowManager)
	{
		gs_pDUIWindowManager = new DUIWindowManager();
	}
	return gs_pDUIWindowManager;
}
inline void ReleaseDUIWindowManager()
{
	if (gs_pDUIWindowManager)
	{
		delete gs_pDUIWindowManager;
	}
}
#endif