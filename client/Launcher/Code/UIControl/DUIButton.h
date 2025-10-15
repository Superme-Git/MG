#ifndef DUIButton_H
#define DUIButton_H
#include "../stdafx.h"
#include "DUIImage.h"
using namespace std;
class DUIButton : public DUIBase
{
public:
	DUIImage* m_pImageEnable;
	DUIImage* m_pImageDisable;
	
	DUIButton(DUIBase* pParent, wstring wsName) :DUIBase(pParent, wsName)
	{
		m_Type = DUIType_Button;
		m_pImageEnable = NULL;
		m_pImageDisable = NULL;
	}
	~DUIButton()
	{
	}
	int Init(int iX, int iY, unsigned int uiWidth, unsigned int uiHeight, int iEnable)
	{
		m_iX = iX;
		m_iY = iY;
		m_uiWidth = uiWidth;
		m_uiHeight = uiHeight;
		m_iEnable = iEnable;
		return 0;
	}
	virtual int OnChangeEnable()
	{
		if (GetEnable() == 1)
		{
			if (m_pImageEnable)
			{
				m_pImageEnable->SetVisible(true);
			}
			if (m_pImageDisable)
			{
				m_pImageDisable->SetVisible(false);
			}
		}
		if (GetEnable() == 0)
		{
			if (m_pImageDisable)
			{
				m_pImageDisable->SetVisible(true);
			}
			if (m_pImageEnable)
			{
				m_pImageEnable->SetVisible(false);
			}
		}
		return 0;
	}
	virtual int OnRemove()
	{
		if (m_pImageEnable)
		{
			m_pImageEnable->SetX(GetX());
			m_pImageEnable->SetY(GetY());
		}
		if (m_pImageDisable)
		{
			m_pImageDisable->SetX(GetX());
			m_pImageDisable->SetY(GetY());
		}
		return 0;
	}
	virtual int OnResize()
	{
		if (m_pImageEnable)
		{
			m_pImageEnable->SetWidth(GetWidth());
			m_pImageEnable->SetHeight(GetHeight());
		}
		if (m_pImageDisable)
		{
			m_pImageDisable->SetWidth(GetWidth());
			m_pImageDisable->SetHeight(GetHeight());
		}
		return 0;
	}
	void ReLayer()
	{
		m_pImageEnable = (DUIImage*)FindItem(L"ImageEnable");
		m_pImageDisable = (DUIImage*)FindItem(L"ImageDisable");
		OnChangeEnable();
		OnRemove();
		OnResize();
	}
};
#endif