

#include "stdafx.h"
#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"

#include "ObjectListDlg.h"

#include <vector>



#ifdef _DEBUG
#define new DEBUG_NEW
#endif




IMPLEMENT_DYNAMIC(CObjectListDlg, CDialog)

void CObjectListDlg::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	if (m_bSizing)
	{
		RenewSize();
		m_bSizing = false;
	}
}

void CObjectListDlg::RenewSize()
{
	CRect rectCtrl;
	GetClientRect(&rectCtrl);
	rectCtrl.left += 5;
	rectCtrl.right -= 5;
	rectCtrl.bottom -= 5;
	rectCtrl.top += 5;
	m_listObjects.MoveWindow(&rectCtrl);

}

void CObjectListDlg::OnSizing(UINT fwSide, LPRECT pRect)
{
	if (pRect->right - pRect->left < m_nMinWidth)
	{
		pRect->right = pRect->left + m_nMinWidth;
	}

	if (pRect->bottom - pRect->top < m_nMinHeight)
	{
		pRect->bottom = pRect->top + m_nMinHeight;
	}
	CDialog::OnSizing(fwSide, pRect);
	m_bSizing = true;
	RenewSize();
}

BOOL CObjectListDlg::OnInitDialog()
{
	CDialog::OnInitDialog();


	CRect rect;
	GetWindowRect(&rect);
	m_nMinWidth = rect.Width();
	m_nMinHeight = rect.Height();

	RenewSize();
	return FALSE;  

}

void CObjectListDlg::UnselectAllItems()
{
	if (m_hWnd != NULL)
	{
		int selCount = m_listObjects.GetSelCount();
		int *sels = new int[selCount];
		int i;
		m_listObjects.GetSelItems(selCount, sels);
		for (i=0;i<selCount;i++)
		{
			m_listObjects.SetSel(sels[i], FALSE);
		}
		delete (sels);
	}
}
void CObjectListDlg::TryChangeSelectedObject(Nuclear::SortBaseId sortBaseID, BOOL add)
{
	if (m_hWnd != NULL)
	{
		if (m_SortIdToListIdMap.find(sortBaseID) != m_SortIdToListIdMap.end())
		{
			m_listObjects.SetSel(m_SortIdToListIdMap[sortBaseID], add);
		}
	}
}

void CObjectListDlg::OnOK()
{
}

void CObjectListDlg::OnLbnSelchangeObjsListBox()
{

	int selCount = m_listObjects.GetSelCount();
	int *sels = new int[selCount];
	int i;
	m_listObjects.GetSelItems(selCount, sels);
	m_pMapEditorView->ClearAllSelection();
	for (i=0;i<selCount;i++)
	{
		m_pMapEditorView->TrySelectObject(m_nLayerEditId - CMapEditorView::FIRST_ELEMENT_LAYER, 
			static_cast<Nuclear::SortBaseId>(m_listObjects.GetItemData(sels[i])));
	}
	delete (sels);
}

void CObjectListDlg::TryRemoveObject(Nuclear::SortBaseId sortBaseID)
{
	if (m_hWnd != NULL)
	{
		if (m_SortIdToListIdMap.find(sortBaseID) != m_SortIdToListIdMap.end())
		{
			int id = m_SortIdToListIdMap[sortBaseID];
			m_listObjects.DeleteString(id);
			m_SortIdToListIdMap.erase(sortBaseID);
			std::map<Nuclear::SortBaseId, int>::iterator iter = m_SortIdToListIdMap.begin();
			for(;iter!=m_SortIdToListIdMap.end();++iter)
			{
				if (iter->second > id)
					iter->second--;
			}

		}
	}
}

void CObjectListDlg::TryAddObject(Nuclear::SortBaseId sortBaseID)
{
	if (m_hWnd != NULL)
	{
		int id;
		PMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
		PMap::MAP_OBJ_TYPE objType;
		if (CLayerView::GetObjectType(sortBaseID, objType))
		{
			Nuclear::SubMap::ObjectId objID;
			objID = CLayerView::GetObjectId(sortBaseID);
			const Nuclear::SubMap::ObjInfoVector &objs = 
				pmap.GetConstObjInfo(objType, m_nLayerEditId - CMapEditorView::FIRST_ELEMENT_LAYER);
			if (objs.size() <= objID)
				return;
			id = m_listObjects.AddString(pmap.GetObjFileName(objType, m_nLayerEditId - CMapEditorView::FIRST_ELEMENT_LAYER, objID).data());
			m_listObjects.SetItemData(id, sortBaseID);
			m_SortIdToListIdMap[sortBaseID] = id;	
		}

	}
}

void CObjectListDlg::ResetLists()
{
	m_nLayerEditId = m_pMapEditorView->GetLayerEditId();
	switch (m_nLayerEditId)
	{
	case CMapEditorView::LEI_GROUND:
	case CMapEditorView::LEI_GAMING:	
		break;
	case CMapEditorView::LEI_WATER_1:
	case CMapEditorView::LEI_WATER_2:
	case CMapEditorView::LEI_LOW_1:
	case CMapEditorView::LEI_LOW_2:
	case CMapEditorView::LEI_LOW_3:
	case CMapEditorView::LEI_LOW_4:
	case CMapEditorView::LEI_LOW_5:
	case CMapEditorView::LEI_LOW_6:
	case CMapEditorView::LEI_MID:
	case CMapEditorView::LEI_HIGH_1:
	case CMapEditorView::LEI_HIGH_2:
		const Nuclear::SortBaseIDSet& selectedObjs = m_pMapEditorView->GetSelectedObjects();
		Nuclear::SortBaseIDSet::const_iterator selEnd = selectedObjs.end();
		int id;
		m_listObjects.ResetContent();
		m_SortIdToListIdMap.clear();
		PMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
		PMap::MAP_OBJ_TYPE objType;
		Nuclear::SubMap::ObjInfoVector::const_iterator iter;
		CLayerView::LAYER_OBJ_TYPE layerObjType;
		Nuclear::SortBaseId sortBaseID;
		for (int i=0;i<PMap::MAP_OBJ_COUNT;++i)
		{
			objType = static_cast<PMap::MAP_OBJ_TYPE>(i);
			layerObjType = CLayerView::ConvertObjectType(objType);
			const Nuclear::SubMap::ObjInfoVector &objs = 
				pmap.GetConstObjInfo(objType, m_nLayerEditId - CMapEditorView::FIRST_ELEMENT_LAYER);
			iter = objs.begin();
			for (;iter!=objs.end();++iter)
			{
				if (!iter->m_bDeleted)
				{
					id = m_listObjects.AddString(pmap.GetObjFileName(objType, iter->m_MapKey).data());
					sortBaseID = CLayerView::GetSortBaseId(iter->m_ObjectId, layerObjType);
					m_listObjects.SetItemData(id, sortBaseID);
					m_SortIdToListIdMap[sortBaseID] = id;
					if (selectedObjs.find(sortBaseID) != selEnd)
					{
						m_listObjects.SetSel(id);
					}
				}
			}

		}
		break;
	}
}

BOOL CObjectListDlg::Create(CMapEditorView* pMapEditorView/* = NULL*/)
{

	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(IDD, pMapEditorView);
}

BEGIN_MESSAGE_MAP(CObjectListDlg, CDialog)
	ON_LBN_SELCHANGE(IDC_OBJS_LIST_BOX, &CObjectListDlg::OnLbnSelchangeObjsListBox)
	ON_WM_SIZING()
	ON_WM_SIZE()
END_MESSAGE_MAP()

void CObjectListDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_OBJS_LIST_BOX, m_listObjects);
}

CObjectListDlg::~CObjectListDlg()
{
}

CObjectListDlg::CObjectListDlg(CWnd* pParent /*=NULL*/)
: CDialog(CObjectListDlg::IDD, pParent) , m_nLayerEditId(0), m_bSizing(false)
{

}

