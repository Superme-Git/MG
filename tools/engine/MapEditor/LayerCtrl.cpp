#include "StdAfx.h"
#include "LayerCtrl.h"

void CLayerCtrl::ItemDataToList(const CExlvItem* pExlvItem, CListCtrl* pList)
{
	LayerCtrlItemData* pData = reinterpret_cast<LayerCtrlItemData*>(pExlvItem->m_pData);
	pList->SetCheck(pExlvItem->m_pItem->iItem, pData->m_Checked);
	pList->SetItemData(pExlvItem->m_pItem->iItem, pData->m_LayerId);
}

void CLayerCtrl::ListToItemData(CExlvItem* pExlvItem, CListCtrl* pList)
{
	LayerCtrlItemData* pData = new LayerCtrlItemData();
	pData->m_Checked = pList->GetCheck(pExlvItem->m_pItem->iItem);
	pData->m_LayerId = static_cast<int>(pList->GetItemData(pExlvItem->m_pItem->iItem));
	pExlvItem->m_pData = pData;

}
CLayerCtrl::~CLayerCtrl(void)
{
}

CLayerCtrl::CLayerCtrl(void)
{
}

