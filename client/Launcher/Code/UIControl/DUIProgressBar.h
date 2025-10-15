#ifndef DUIProgressBar_H
#define DUIProgressBar_H
#include "../stdafx.h"
#include "DUIImage.h"
using namespace std;
class DUIProgressBar : public DUIBase
{
public:
	unsigned int m_uiValueMax;
	unsigned int m_uiValueMin;
	unsigned int m_uiValueCur;
	DUIImage* m_pBorder;
	DUIImage* m_pCenter;
	DUIImage* m_pBackGround;

	DUIProgressBar(DUIBase* pParent, wstring wsName) :DUIBase(pParent, wsName)
	{
		m_Type = DUIType_ProgressBar;
		m_uiValueMax = 100;
		m_uiValueMin = 0;
		m_uiValueCur = 0;
		m_pBorder = NULL;
		m_pCenter = NULL;
		m_pBackGround = NULL;
	}
	~DUIProgressBar()
	{
	}
	int Init(int iX, int iY, unsigned int uiWidth, unsigned int uiHeight, unsigned int uiValueMax, unsigned int uiValueMin, unsigned int uiValueCur)
	{
		m_iX = iX;
		m_iY = iY;
		m_uiWidth = uiWidth;
		m_uiHeight = uiHeight;
		m_uiValueMax = uiValueMax;
		m_uiValueMin = uiValueMin;
		m_uiValueCur = uiValueCur;
		return 0;
	}
	void SetValueCur(unsigned int uiValueCur)
	{
		m_uiValueCur = uiValueCur;
		if (m_pCenter)
		{
			m_pCenter->SetWidth(GetWidth() * m_uiValueCur / (m_uiValueMax - m_uiValueMin));
		}
		AddNeedRedraw();
	}
	virtual int OnRemove()
	{
		if (m_pBorder)
		{
			m_pBorder->SetX(GetX());
			m_pBorder->SetY(GetY());
		}
		if (m_pCenter)
		{
			m_pCenter->SetX(GetX());
			m_pCenter->SetY(GetY());
		}
		if (m_pBackGround)
		{
			m_pBackGround->SetX(GetX());
			m_pBackGround->SetY(GetY());
		}
		AddNeedRedraw();
		return 0;
	}
	virtual int OnResize()
	{
		if (m_pBorder)
		{
			m_pBorder->SetWidth(GetWidth());
			m_pBorder->SetHeight(GetHeight());
		}
		if (m_pCenter)
		{
			m_pCenter->SetWidth(GetWidth() * m_uiValueCur / (m_uiValueMax - m_uiValueMin));
			m_pCenter->SetHeight(GetHeight());
		}
		if (m_pBackGround)
		{
			m_pBackGround->SetWidth(GetWidth());
			m_pBackGround->SetHeight(GetHeight());
		}
		AddNeedRedraw();
		return 0;
	}
	void ReLayer()
	{
		m_pBorder = (DUIImage*)FindItem(L"Border");
		m_pCenter = (DUIImage*)FindItem(L"Center");
		m_pBackGround = (DUIImage*)FindItem(L"BackGround");
		OnRemove();
		OnResize();
	}
};
#endif