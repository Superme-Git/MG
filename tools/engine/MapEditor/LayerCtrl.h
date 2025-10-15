#pragma once
#include "draglistctrl.h"


class LayerCtrlItemData :
	public CExlvItemData
{
public:
	~LayerCtrlItemData() {}
	BOOL m_Checked;
	int m_LayerId;
};

class CLayerCtrl :
	public CDragListCtrl
{
protected:
	virtual void ItemDataToList(const CExlvItem* pExlvItem, CListCtrl* pList);
	virtual void ListToItemData(CExlvItem* pExlvItem, CListCtrl* pList);
public:
	virtual ~CLayerCtrl(void);
	CLayerCtrl(void);
};
