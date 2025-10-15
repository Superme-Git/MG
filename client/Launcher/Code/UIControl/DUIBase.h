#ifndef DUIBase_H
#define DUIBase_H
#include "../stdafx.h"

using namespace std;

enum DUIType
{
	DUIType_Base = 0,
	DUIType_WebBrowser = 1,
	DUIType_Window = 10,
	DUIType_Label = 20,
	DUIType_LabelLink = 21,
	DUIType_Image = 30,
	DUIType_Button = 40,
	DUIType_ProgressBar = 50,
};

class DUIBase;
typedef vector<DUIBase*> DUILayer;

typedef void(*LBDownCB_Func)(DUIBase* pUI, int iX, int iY);

class DUIBase
{
public:
	DUIType m_Type;
	DUILayer m_Items;
	DUIBase* m_pParent;
	wstring m_wsName;
	int m_iX;
	int m_iY;
	int m_iZ;
	unsigned int m_uiWidth;
	unsigned int m_uiHeight;

	int m_iEnable;
	int m_iVisible;

	LBDownCB_Func m_LBDownCB_Func;

	map<wstring, wstring> m_CustomData;

	DUIBase(DUIBase* pParent, wstring wsName)
	{
		m_Type = DUIType_Base;
		m_pParent = pParent;
		m_wsName = wsName;
		m_iX = 0;
		m_iY = 0;
		m_iZ = 0;
		m_uiWidth = 100;
		m_uiHeight = 100;
		m_iEnable = 1;
		m_iVisible = 1;
		m_LBDownCB_Func = NULL;
		if (pParent)
		{
			pParent->AddItem(this);
		}
	}
	~DUIBase()
	{
		for (int i = 0; i < GetItemCount(); i++)
		{
			delete m_Items[i];
			m_Items[i] = NULL;
		}
		m_Items.clear();
	}
//--------------------Being Base--------------------
	DUIBase* GetParent()
	{
		return m_pParent;
	}
	wstring GetName()
	{
		return m_wsName;
	}
	virtual HWND GetBGHandle()
	{
		HWND hWndResult;
		return hWndResult;
	}
	int AddCustomData(wstring wsKey, wstring wsValue)
	{
		m_CustomData[wsKey] = wsValue;
		return 0;
	}
	wstring FindCustomData(wstring wsKey)
	{
		if (m_CustomData.count(wsKey) > 0)
		{
			return m_CustomData[wsKey];
		}
		return L"";
	}
	void SetEnable(int iEnable)
	{
		m_iEnable = iEnable;
		OnChangeEnable();
	}
	int GetEnable()
	{
		return m_iEnable;
	}
	virtual int OnChangeEnable()
	{
		AddNeedRedraw();
		return 0;
	}
	void SetVisible(int iVisible)
	{
		m_iVisible = iVisible;
		OnChangeVisible();
	}
	int GetVisible()
	{
		return m_iVisible;
	}
	virtual int OnChangeVisible()
	{
		AddNeedRedraw();
		return 0;
	}
//--------------------End Base--------------------
//--------------------Being Pos--------------------
	void SetX(int iX)
	{
		m_iX = iX;
		OnRemove();
	}
	int GetX()
	{
		return m_iX;
	}
	void SetY(int iY)
	{
		m_iY = iY;
		OnRemove();
	}
	int GetY()
	{
		return m_iY;
	}
	void SetZ(int iZ)
	{
		m_iZ = iZ;
		OnRemove();
	}
	int GetZ()
	{
		return m_iZ;
	}
	virtual int OnRemove()
	{
		AddNeedRedraw();
		return 0;
	}
//--------------------End Pos--------------------
//--------------------Being Size--------------------
	void SetWidth(unsigned int uiWidth)
	{
		m_uiWidth = uiWidth;
		OnResize();
	}
	unsigned int GetWidth()
	{
		return m_uiWidth;
	}
	void SetHeight(unsigned int uiHeight)
	{
		m_uiHeight = uiHeight;
		OnResize();
	}
	unsigned int GetHeight()
	{
		return m_uiHeight;
	}
	virtual int OnResize()
	{
		AddNeedRedraw();
		return 0;
	}
//--------------------End Size--------------------
//--------------------Being Item--------------------
	int AddItem(DUIBase* pItem)
	{
		m_Items.push_back(pItem);
		for (int i = 0; i < GetItemCount(); i++)
		{
			GetItem(i)->m_iZ = i;
		}
		return m_Items.size() - 1;
	}
	int GetItemCount()
	{
		return m_Items.size();
	}
	DUIBase* GetItem(int iIndex)
	{
		return m_Items[iIndex];
	}
	DUIBase* FindItem(wstring wsName)
	{
		for (int i = 0; i < GetItemCount(); i++)
		{
			DUIBase* pCurItem = GetItem(i);
			if (pCurItem->GetName() == wsName)
			{
				return pCurItem;
			}
		}
		return NULL;
	}
//--------------------End Item--------------------
//--------------------Being Update--------------------
	virtual int UpdateMeBefor()
	{
		return 0;
	}
	int UpdateItems()
	{
		for (int i = 0; i < GetItemCount(); i++)
		{
			GetItem(i)->Update();
		}
		return 0;
	}
	virtual int UpdateMeAfter()
	{
		return 0;
	}
	int Update()
	{
		UpdateMeBefor();
		UpdateItems();
		UpdateMeAfter();
		return 0;
	}
//--------------------End Update--------------------
//--------------------Being Draw--------------------
	virtual int DrawMeBefor(HDC hdcBG)
	{
		return 0;
	}
	int DrawItems(HDC hdcBG)
	{
		for (int i = 0; i < GetItemCount(); i++)
		{
			GetItem(i)->Draw(hdcBG);
		}
		return 0;
	}
	virtual int DrawMeAfter(HDC hdcBG)
	{
		return 0;
	}
	int Draw(HDC hdcBG)
	{
		if (GetVisible() == 0)
		{
			return 0;
		}
		DrawMeBefor(hdcBG);
		DrawItems(hdcBG);
		DrawMeAfter(hdcBG);
		return 0;
	}
	virtual HDC GetBGDC()
	{
		return 0;
	}
	int Redraw()
	{
		if (m_pParent)
		{
			return m_pParent->Redraw();
		}
		SetNeedRedraw(0);
		return Draw(GetBGDC());
	}
	virtual int AddNeedRedraw()
	{
		if (m_pParent)
		{
			return m_pParent->AddNeedRedraw();
		}
		return 0;
	}
	virtual void SetNeedRedraw(int iNeedRedraw)
	{
	}
	virtual int GetNeedRedraw()
	{
		return 0;
	}
	int NotifyNeedRedraw()
	{
		if (m_pParent)
		{
			return m_pParent->NotifyNeedRedraw();
		}
		return AddNeedRedraw();
	}
//--------------------End Draw--------------------
//--------------------Being Event--------------------
	int CheckPickInArea(int iX, int iY)
	{
		//if (iX < m_iX || iX > m_iX + m_uiWidth || iY < m_iY || iY > m_iY + m_uiHeight)
		//{
		//	return 0;
		//}
		if (GetVisible() == 0)
		{
			return 0;
		}
		if (iX >= m_iX && iX <= m_iX + m_uiWidth && iY >= m_iY && iY <= m_iY + m_uiHeight)
		{
			return 1;
		}
		return 0;
	}
	int OnLButtonDown(int iX, int iY)
	{
		int iResult = CheckPickInArea(iX, iY);
		if (iResult == 0)
		{
			return iResult;
		}
		iResult = 0;
		if (GetItemCount() > 0)
		{
			for (int i = GetItemCount() - 1; i >= 0; i--)
			{
				iResult = GetItem(i)->OnLButtonDown(iX, iY);
				if (iResult != 0)
				{
					return iResult;
				}
			}
		}
		iResult = OnLButtonDownMe(iX, iY);
		return iResult;
	}
	virtual int OnLButtonDownMe(int iX, int iY)
	{
		int iResult = 0;
		if (GetEnable() != 0 && m_LBDownCB_Func)
		{
			m_LBDownCB_Func(this, iX, iY);
			return 1;
		}
		return iResult;
	}
//--------------------End Event--------------------
//--------------------Being UIControl--------------------
	virtual DUIBase* GetParentWindow()
	{
		if (m_pParent)
		{
			if (m_pParent->m_Type == DUIType_Window)
			{
				return m_pParent;
			}
			return m_pParent->GetParentWindow();
		}
		return NULL;
	}
	virtual void CloseMe()
	{

	}
//--------------------End UIControl--------------------
};
#endif