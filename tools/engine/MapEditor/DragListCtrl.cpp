#include "StdAfx.h"
#include "DragListCtrl.h"
#include<list>

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


void CDragListCtrl::OnLvnEndlabeledit(NMHDR *pNMHDR, LRESULT *pResult)
{
	NMLVDISPINFO *pDispInfo = reinterpret_cast<NMLVDISPINFO*>(pNMHDR);

	if (pDispInfo->item.pszText)
	{
		SetItemText(pDispInfo->item.iItem, 0, pDispInfo->item.pszText);
	}
	*pResult = 0;
}
void CDragListCtrl::DropItemOntoList(CListCtrl* pSourceList, CListCtrl* pDestList)
{
	pDestList->SetItemState (m_nDropIndex, 0, LVIS_DROPHILITED);
	CExlvItem* pExlvItem;
	std::list<CExlvItem*> itemlist;
	POSITION pos = pSourceList->GetFirstSelectedItemPosition();
	while(pos)
	{
		pExlvItem = new CExlvItem();
		m_nDragIndex = pSourceList->GetNextSelectedItem(pos);
		pExlvItem->m_pItem->iItem = m_nDragIndex;


		pExlvItem->m_pItem->mask = LVIF_TEXT;
		pExlvItem->m_pItem->pszText = new TCHAR[256];
		pExlvItem->m_pItem->cchTextMax = 255;


		pSourceList->GetItem (pExlvItem->m_pItem);


		pSourceList->GetItemText(pExlvItem->m_pItem->iItem, 1);


		ListToItemData(pExlvItem, pSourceList);
		itemlist.push_back(pExlvItem);
	}

	if(pSourceList == pDestList)
	{
		pos = pSourceList->GetFirstSelectedItemPosition();
		while(pos)
		{
			m_nDragIndex = pSourceList->GetNextSelectedItem(pos);
			pSourceList->DeleteItem(m_nDragIndex);
			if(m_nDragIndex < m_nDropIndex) m_nDropIndex--;	
			pos = pSourceList->GetFirstSelectedItemPosition();
		}
	}

	while(itemlist.begin()!=itemlist.end())
	{
		pExlvItem = itemlist.front();
		m_nDropIndex = (m_nDropIndex == -1) ? pDestList->GetItemCount() : m_nDropIndex;

		pExlvItem->m_pItem->iItem = m_nDropIndex;

		pDestList->InsertItem (pExlvItem->m_pItem);
		ItemDataToList(pExlvItem, pDestList);


		pDestList->SetItemState (pExlvItem->m_pItem->iItem, LVIS_SELECTED, LVIS_SELECTED);


		delete(pExlvItem->m_pItem->pszText);

		delete(pExlvItem);
		m_nDropIndex++; 
		itemlist.pop_front();
	}	
}

void CDragListCtrl::OnMouseMove(UINT nFlags, CPoint point)
{

	if (m_bDragging)
	{

		CPoint pt(point);
		ClientToScreen(&pt);
		m_pDragImage->DragMove(pt);
		m_pDragImage->DragShowNolock(false);


		CWnd* pDropWnd = WindowFromPoint (pt);


		if (pDropWnd != m_pDestList)
		{
			if (m_nDropIndex != -1) 
			{

				CListCtrl* pList = m_pDestList;
				VERIFY (pList->SetItemState (m_nDropIndex, 0, LVIS_DROPHILITED));

				VERIFY (pList->RedrawItems (m_nDropIndex, m_nDropIndex));
				pList->UpdateWindow ();
				m_nDropIndex = -1;
			}
			else 
			{

				CListCtrl* pList = m_pDestList;
				int i = 0;
				int nCount = pList->GetItemCount();
				for(i = 0; i < nCount; i++)
				{
					pList->SetItemState(i, 0, LVIS_DROPHILITED);
				}
				pList->RedrawItems(0, nCount);
				pList->UpdateWindow();
			}
		}

		pDropWnd->ScreenToClient(&pt);

		if(pDropWnd->IsKindOf(RUNTIME_CLASS (CListCtrl)))
		{			
			m_pDestList = reinterpret_cast<CListCtrl*>(pDropWnd);

			SetCursor(LoadCursor(NULL, IDC_ARROW));
			UINT uFlags;


			m_pDestList->SetItemState (m_nDropIndex, 0, LVIS_DROPHILITED);

			m_pDestList->RedrawItems (m_nDropIndex, m_nDropIndex);


			m_nDropIndex = ((CListCtrl*)pDropWnd)->HitTest(pt, &uFlags);

			m_pDestList->SetItemState(m_nDropIndex, LVIS_DROPHILITED, LVIS_DROPHILITED);

			m_pDestList->RedrawItems(m_nDropIndex, m_nDropIndex);
			m_pDestList->UpdateWindow();
		} else {

			SetCursor(LoadCursor(NULL, IDC_NO));
		}

		m_pDragImage->DragShowNolock(true);
	}
	CListCtrl::OnMouseMove(nFlags, point);
}

void CDragListCtrl::OnLButtonUp(UINT nFlags, CPoint point)
{

	if (m_bDragging)
	{
		m_bDragging = false;
		ReleaseCapture();
		m_pDragImage->DragLeave (GetDesktopWindow());
		m_pDragImage->EndDrag();
		delete m_pDragImage;
		CPoint pt (point);
		ClientToScreen (&pt);
		CWnd* pDropWnd = WindowFromPoint (pt);
		ASSERT (pDropWnd); 
		if (pDropWnd->IsKindOf (RUNTIME_CLASS (CListCtrl)))
		{
			DropItemOntoList(this, reinterpret_cast<CListCtrl*>(pDropWnd));
			GetParentOwner()->PostMessage(DRAGLISTCTRL_LVN_ENDDRAGGING, GetDlgCtrlID(), 0);
		}
	}

	CListCtrl::OnLButtonUp(nFlags, point);
}

void CDragListCtrl::OnLvnBegindrag(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);


	m_nDragIndex = pNMLV->iItem;


	POINT point;
	int offset = -10; 
	if(GetSelectedCount() > 1) 
		point.x = offset;
	point.y = offset;

	m_pDragImage = CreateDragImage(m_nDragIndex, &point);



	m_pDragImage->BeginDrag(0, CPoint(offset, offset - 4));
	m_pDragImage->DragEnter(GetDesktopWindow(), pNMLV->ptAction);

	m_bDragging = true;
	m_nDropIndex = -1;
	m_pDestList = this;

	SetCapture ();

	*pResult = 0;
}

BEGIN_MESSAGE_MAP(CDragListCtrl, CListCtrl)
	ON_NOTIFY_REFLECT(LVN_BEGINDRAG, &CDragListCtrl::OnLvnBegindrag)
	ON_WM_LBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_NOTIFY_REFLECT(LVN_ENDLABELEDIT, &CDragListCtrl::OnLvnEndlabeledit)
END_MESSAGE_MAP()

CDragListCtrl::~CDragListCtrl(void)
{
}
CDragListCtrl::CDragListCtrl(void) : m_bDragging(false)
{
}

UINT const CDragListCtrl::DRAGLISTCTRL_LVN_ENDDRAGGING = RegisterWindowMessage(_T("Lvn End Dragging"));

