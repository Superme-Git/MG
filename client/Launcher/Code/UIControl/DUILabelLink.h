#ifndef DUILabelLink_H
#define DUILabelLink_H
#include "../stdafx.h"
#include "DUILabel.h"
using namespace std;
class DUILabelLink : public DUILabel
{
public:
	wstring m_wsURL;
	wstring m_wsRandomStr;
	DUILabelLink(DUIBase* pParent, wstring wsName) :DUILabel(pParent, wsName)
	{
		m_Type = DUIType_LabelLink;
	}
	~DUILabelLink()
	{
		
	}
	int Init(unsigned int uiBackMode, unsigned int uiBackColor, unsigned int uiTextColor, wstring wsText, wstring wsURL, int iX, int iY, unsigned int uiWidth, unsigned int uiHeight, wstring wsFontName = L"", int iFontHeight = 0, wstring wsRandomStr = L"")
	{
		DUILabel::Init(uiBackMode, uiBackColor, uiTextColor, wsText, iX, iY, uiWidth, uiHeight, wsFontName, iFontHeight);
		m_wsRandomStr = wsRandomStr;
		SetURL(wsURL);
		return 0;
	}
	void SetURL(wstring wsURL)
	{
		m_wsURL = wsURL;
		if (m_wsURL != L"")
		{
			m_LBDownCB_Func = DUILabelLink::OpenURL;
		}
		else
		{
			m_LBDownCB_Func = NULL;
		}
	}
	static void OpenURL(DUIBase* pUI, int iX, int iY)
	{
		if (pUI->m_Type != DUIType_LabelLink)
		{
			return;
		}
		wstring wsURL = ((DUILabelLink*)pUI)->m_wsURL;
		if (wsURL != L"")
		{
			wstring wsRandomStr = ((DUILabelLink*)pUI)->m_wsRandomStr;
			if (wsRandomStr != L"")
			{
				wsRandomStr += GetRandomNumStr();
				wsURL += wsRandomStr;
			}
			ShellExecute(NULL, L"open", wsURL.c_str(), NULL, NULL, SW_SHOW);
		}
	}

};
#endif