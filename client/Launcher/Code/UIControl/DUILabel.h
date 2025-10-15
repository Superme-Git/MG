#ifndef DUILabel_H
#define DUILabel_H
#include "../stdafx.h"
#include "DUIBase.h"
using namespace std;
class DUILabel : public DUIBase
{
public:
	unsigned int m_uiBackMode;
	unsigned int m_uiBackColor;
	unsigned int m_uiTextColor;
	wstring m_wsFontName;
	int m_iFontHeight;
	HFONT m_hfFont;
	//Font
	wstring m_wsText;
	
	DUILabel(DUIBase* pParent, wstring wsName) :DUIBase(pParent, wsName)
	{
		m_Type = DUIType_Label;
		m_uiBackMode = 1;
		m_uiBackColor = 0;
		m_uiTextColor = 0xFFFFFF;
		m_wsText = wsName;
		m_iFontHeight = 0;
		m_hfFont = 0;
	}
	~DUILabel()
	{
		if (m_hfFont)
		{
			DeleteObject(m_hfFont);
		}
	}
	int Init(unsigned int uiBackMode, unsigned int uiBackColor, unsigned int uiTextColor, wstring wsText, int iX, int iY, unsigned int uiWidth, unsigned int uiHeight, wstring wsFontName = L"", int iFontHeight = 0)
	{
		m_uiBackMode = uiBackMode;
		m_uiBackColor = uiBackColor;
		m_uiTextColor = uiTextColor;
		m_wsText = wsText;
		m_iX = iX;
		m_iY = iY;
		m_uiWidth = uiWidth;
		m_uiHeight = uiHeight;
		m_wsFontName = wsFontName;
		m_iFontHeight = iFontHeight;
		if (m_wsFontName != L"")
		{
			m_hfFont = CreateFont(m_iFontHeight, 0, 0, 0, FW_NORMAL, FALSE, FALSE, 0, DEFAULT_CHARSET, OUT_DEFAULT_PRECIS, CLIP_DEFAULT_PRECIS, DEFAULT_QUALITY, DEFAULT_PITCH | FF_SWISS, m_wsFontName.c_str());
		}
		return 0;
	}
	void SetText(wstring wsText)
	{
		m_wsText = wsText;
		AddNeedRedraw();
	}
	virtual int DrawMeBefor(HDC hdcBG)
	{
		if (m_wsText != L"")
		{
			SetBkMode(hdcBG, m_uiBackMode);
			SetBkColor(hdcBG, m_uiBackColor);
			SetTextColor(hdcBG, m_uiTextColor);
			if (m_wsFontName != L"")
			{
				HFONT hfOld = (HFONT)SelectObject(hdcBG, m_hfFont);
				TextOut(hdcBG, m_iX, m_iY, m_wsText.c_str(), m_wsText.size());
				SelectObject(hdcBG, hfOld);
			}
			else
			{
				TextOut(hdcBG, m_iX, m_iY, m_wsText.c_str(), m_wsText.size());
			}
		}
		return 0;
	}
};
#endif