

#include "stdafx.h"
#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "LayerListDlg.h"

#include <vector>


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CLayerListDlg, CDialog)

void CLayerListDlg::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	m_bSizing = true;
	RenewSize();
}

void CLayerListDlg::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	if (m_bSizing)
	{
		RenewSize();
		m_bSizing = false;
	}
}

void CLayerListDlg::RenewSize()
{
	CRect rectCtrl;
	GetClientRect(&rectCtrl);
	rectCtrl.DeflateRect(5, 5);
	m_Tab.MoveWindow(&rectCtrl);
	rectCtrl.DeflateRect(5, 5);
	rectCtrl.top += 20;
	m_listGroundLayers.MoveWindow(&rectCtrl);
	rectCtrl.bottom -= 23;
	m_listSmallLayers.MoveWindow(&rectCtrl);
	rectCtrl.top = rectCtrl.bottom + 5;
	rectCtrl.bottom = rectCtrl.top + 15;
	rectCtrl.right = rectCtrl.left + 15;
	m_btnAddLayer.MoveWindow(&rectCtrl);
	rectCtrl.left = rectCtrl.right + 5;
	rectCtrl.right = rectCtrl.left + 15;
	m_btnDeleteLayer.MoveWindow(&rectCtrl);

}

void CLayerListDlg::ResetTabMode()
{
	switch(m_Tab.GetCurSel())
	{
	case 0:	
		m_btnDeleteLayer.ShowWindow(SW_SHOW);
		m_btnAddLayer.ShowWindow(SW_SHOW);
		m_listSmallLayers.ShowWindow(SW_SHOW);
		m_listGroundLayers.ShowWindow(SW_HIDE);
		break;
	case 1:
		m_btnDeleteLayer.ShowWindow(SW_HIDE);
		m_btnAddLayer.ShowWindow(SW_HIDE);
		m_listSmallLayers.ShowWindow(SW_HIDE);
		m_listGroundLayers.ShowWindow(SW_SHOW);
		break;
	}
}

void CLayerListDlg::OnTcnSelchangeTab1(NMHDR *pNMHDR, LRESULT *pResult)
{
	ResetTabMode();
	*pResult = 0;
}

void CLayerListDlg::OnLvnItemChangedList2(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	if (pNMLV->uChanged & LVIF_STATE)
	{
		if (((pNMLV->uOldState ^ pNMLV->uNewState) & LVIS_SELECTED) && (!m_listGroundLayers.isDragging())) 
		{
			if (m_listGroundLayers.GetSelectedCount() > 1)
			{
				m_pMapEditorView->SetGroundLayerEditID(Nuclear::SubMap::GL_BIG);
			} else {
				POSITION pos = m_listGroundLayers.GetFirstSelectedItemPosition();
				if (pos)
				{
					m_pMapEditorView->SetGroundLayerEditID(
						static_cast<Nuclear::SubMap::GroundLayers>(
						m_listGroundLayers.GetItemData(m_listGroundLayers.GetNextSelectedItem(pos))
						)
						);
				}
			}
		}

	}

	*pResult = 0;
}

void CLayerListDlg::OnBnClickedButtonDeleteLayer()
{
	POSITION pos = m_listSmallLayers.GetFirstSelectedItemPosition();
	while(pos) 
	{
		m_pMapEditorView->DeleteGroundLayer(m_listSmallLayers.GetNextSelectedItem(pos));
		pos = m_listSmallLayers.GetFirstSelectedItemPosition();
	}
}

void CLayerListDlg::OnBnClickedButtonNewLayer()
{
	m_pMapEditorView->NewGroundLayer();
}

void CLayerListDlg::SelectLayer(Nuclear::SubMap::GroundLayers layerID)
{
	DWORD layer = layerID;
	int i;
	for (i = 0;i<Nuclear::TILES_ALL_LAYER_COUNT;++i)
	{
		if (m_listGroundLayers.GetItemData(i) == layer) 
		{
			m_listGroundLayers.SetItemState(i, LVIS_SELECTED|LVIS_FOCUSED, LVIS_SELECTED|LVIS_FOCUSED);
		} else {
			m_listGroundLayers.SetItemState(i, ~LVIS_SELECTED, LVIS_SELECTED);
		}
	}
}

void CLayerListDlg::SelectLayer(int layerID)
{
	int i;
	for (i = 0;i<m_listSmallLayers.GetItemCount();++i)
	{
		if (m_pMapEditorView->GetGroundLayerID(i) == layerID) 
		{
			m_listSmallLayers.SetItemState(i, LVIS_SELECTED|LVIS_FOCUSED, LVIS_SELECTED|LVIS_FOCUSED);
		} else {
			m_listSmallLayers.SetItemState(i, ~LVIS_SELECTED, LVIS_SELECTED);
		}
	}
}

void CLayerListDlg::SetLayerName(int layer, CString name)
{
	m_listSmallLayers.SetItemText(layer, 0, name);
}

void CLayerListDlg::SetLayerChecked(int layer, bool isChecked)
{
	m_listSmallLayers.SetCheck(layer, isChecked?TRUE:FALSE);
}

void CLayerListDlg::OnLvnItemChangedList1(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	if (pNMLV->uChanged & LVIF_STATE)
	{
		if (((pNMLV->uOldState ^ pNMLV->uNewState) & LVIS_SELECTED) && (!m_listSmallLayers.isDragging())) 
		{
			if (m_listSmallLayers.GetSelectedCount() > 1)
			{
				m_pMapEditorView->SetMutiSelectedLayer();
				m_pMapEditorView->SetSmallLayerEditID(-1);
			} else {
				m_pMapEditorView->SetMutiSelectedLayer(false);
				POSITION pos = m_listSmallLayers.GetFirstSelectedItemPosition();
				if (pos)
				{
					m_pMapEditorView->SetSmallLayerEditID(m_listSmallLayers.GetNextSelectedItem(pos));
				} else {
					m_pMapEditorView->SetSmallLayerEditID(-1);
				}

			}
		}
		if (((pNMLV->uOldState ^ pNMLV->uNewState) & INDEXTOSTATEIMAGEMASK(1))  && (pNMLV->uOldState))	

		{
			m_pMapEditorView->SetGroundLayerShow(pNMLV->iItem, m_listSmallLayers.GetCheck(pNMLV->iItem)?true:false);
		}
	}

	if (pNMLV->uChanged & LVIF_TEXT)
	{
		m_pMapEditorView->SetGroundLayerName(pNMLV->iItem, m_listSmallLayers.GetItemText(pNMLV->iItem, 0), false);
	}

	*pResult = 0;
}

void CLayerListDlg::ReNewTree()
{

	int layer = 0;
	const CToolsMap::TileLayerInfoList& layers = m_pMapEditorView->GetDocument()->GetGroundLayerInfos();
	CToolsMap::TileLayerInfoList::const_iterator iter = layers.begin();
	m_listSmallLayers.DeleteAllItems();
	for (;iter!=layers.end();iter++,layer++)
	{
		m_listSmallLayers.InsertItem(layer, iter->name);
		m_listSmallLayers.SetItemData(layer, iter->layerID);
		m_listSmallLayers.SetCheck(layer, iter->isShow?TRUE:FALSE);
	}
}

BOOL CLayerListDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_listSmallLayers.SetExtendedStyle(LVS_EX_CHECKBOXES);
	m_listGroundLayers.SetExtendedStyle(LVS_EX_CHECKBOXES);
	static wchar_t* names[4] = {L"大地表", L"中地表1", L"中地表2", L"小地表"};
	int layer = 0;
	m_listGroundLayers.DeleteAllItems();
	Nuclear::PMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
	Nuclear::SubMap::GroundLayers glayer;
	for (;layer<4;layer++)
	{
		pmap.GetGroundOrders(layer, glayer);
		m_listGroundLayers.InsertItem(layer, names[glayer]);
		m_listGroundLayers.SetItemData(layer, glayer);
		m_listGroundLayers.SetCheck(layer, TRUE);
	}
	m_Tab.InsertItem(0, L"小地表");
	m_Tab.InsertItem(1, L"所有地表");
	ReNewTree();
	m_Tab.SetCurSel(0);
	ResetTabMode();
	RenewSize();
	return FALSE;  

}

void CLayerListDlg::SetLayerState(int layer, UINT nState)
{
	m_listSmallLayers.SetItemState(layer, nState, ~0);
}

UINT CLayerListDlg::GetLayerState(int layer)
{
	return m_listSmallLayers.GetItemState(layer, ~0);
}

void CLayerListDlg::DeleteLayer(int layer)
{
	DWORD_PTR layerID = m_listSmallLayers.GetItemData(layer);
	DWORD_PTR newLayerID;
	m_listSmallLayers.DeleteItem(layer);
	int i;
	for (i=0;i<m_listSmallLayers.GetItemCount();i++)
	{
		newLayerID = m_listSmallLayers.GetItemData(i);
		if (newLayerID > layerID)
		{
			newLayerID--;
			m_listSmallLayers.SetItemData(i, newLayerID);
		}
	}
}

void CLayerListDlg::NewLayer(CString name)
{
	int layerCount = static_cast<int>(m_pMapEditorView->GetGroundLayerCount());

	m_listSmallLayers.InsertItem(0, name);
	m_listSmallLayers.SetItemData(0, layerCount - 1);
	m_listSmallLayers.SetCheck(0);
}

LRESULT CLayerListDlg::OnLvnEndDraggingList(WPARAM wParam, LPARAM lParam)
{
	CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
	int layer;
	switch (wParam)
	{
	case IDC_LIST1:
		{
			int layerCount = m_listSmallLayers.GetItemCount();
			m_pMapEditorView->BeginDragging();
			for (layer=0;layer<layerCount;layer++)
			{
				pDoc->SetGroundLayerID(layer, static_cast<int>(m_listSmallLayers.GetItemData(layer)));
				pDoc->SetGroundLayerShow(layer, m_listSmallLayers.GetCheck(layer)?true:false);
				pDoc->SetGroundLayerName(layer, m_listSmallLayers.GetItemText(layer, 0));
			}
			m_pMapEditorView->EndDragging();
		}
		break;
	case IDC_LIST2:
		{			

			Nuclear::SubMap::GroundLayers gl;
			Nuclear::PMap& pmap = pDoc->GetToolsMap();
			for (layer=0;layer<Nuclear::TILES_ALL_LAYER_COUNT;layer++)
			{
				gl = static_cast<Nuclear::SubMap::GroundLayers>(m_listGroundLayers.GetItemData(layer));
				pmap.SetGroundOrders(layer, gl);
			}
		}
		break;
	}


	return 1;
}

BOOL CLayerListDlg::Create(CMapEditorView* pMapEditorView)
{

	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(IDD, pMapEditorView);
}

BEGIN_MESSAGE_MAP(CLayerListDlg, CDialog)
	ON_NOTIFY(LVN_ITEMCHANGED, IDC_LIST1, &CLayerListDlg::OnLvnItemChangedList1)
	ON_NOTIFY(LVN_ITEMCHANGED, IDC_LIST2, &CLayerListDlg::OnLvnItemChangedList2)
	ON_BN_CLICKED(IDC_BUTTON_NEW_LAYER, &CLayerListDlg::OnBnClickedButtonNewLayer)
	ON_BN_CLICKED(IDC_BUTTON_DEL_LAYER, &CLayerListDlg::OnBnClickedButtonDeleteLayer)
	ON_REGISTERED_MESSAGE(CLayerCtrl::DRAGLISTCTRL_LVN_ENDDRAGGING, &CLayerListDlg::OnLvnEndDraggingList)

	ON_NOTIFY(TCN_SELCHANGE, IDC_TAB1, &CLayerListDlg::OnTcnSelchangeTab1)
	ON_WM_SIZE()
	ON_WM_SIZING()
END_MESSAGE_MAP()

void CLayerListDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_TAB1, m_Tab);
	DDX_Control(pDX, IDC_LIST1, m_listSmallLayers);
	DDX_Control(pDX, IDC_LIST2, m_listGroundLayers);
	DDX_Control(pDX, IDC_BUTTON_DEL_LAYER, m_btnDeleteLayer);
	DDX_Control(pDX, IDC_BUTTON_NEW_LAYER, m_btnAddLayer);
}

CLayerListDlg::~CLayerListDlg()
{
}

CLayerListDlg::CLayerListDlg(CWnd* pParent /*=NULL*/)
: CDialog(CLayerListDlg::IDD, pParent)
{
	m_bSizing = false;
}

