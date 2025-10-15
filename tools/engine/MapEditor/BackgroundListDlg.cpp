

#include "stdafx.h"
#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "BackgroundListDlg.h"
#include "BackgroundInfoDlg.h"




IMPLEMENT_DYNAMIC(CBackgroundListDlg, CDialog)

LRESULT CBackgroundListDlg::OnLvnEnddraggingList(WPARAM wParam, LPARAM lParam)
{
	int id1 = m_BackgroundInfoList.GetDragIndex();
	int id2 = m_BackgroundInfoList.GetDropIndex() - 1;
	if (id1 != id2)
	{
		CAction* pNowOper = new CAction(CAction::AT_BACKGROUND_ORDER, m_pMapEditorView->GetLayerEditId());
		CAction::CBackgroundOrderActionAtom* oper = pNowOper->GetBackgroundOrderActionData();
		oper->m_InfoID[0] = id1;
		oper->m_InfoID[1] = id2;
		CToolsMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
		pmap.SwapBackgroundInfo(id1, id2);
		m_pMapEditorView->m_pOperatorList->DoOneAction(pNowOper);
		m_pMapEditorView->GetDocument()->SetModifiedFlag();
		RenewList();
		m_BackgroundInfoList.SetItemState(id2, LVIS_SELECTED|LVIS_FOCUSED, LVIS_SELECTED|LVIS_FOCUSED|LVIS_DROPHILITED);
	}	
	return 1;
}

void CBackgroundListDlg::OnLvnItemchangedList1(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	if (pNMLV->uChanged & LVIF_STATE)
	{
		if (((pNMLV->uOldState ^ pNMLV->uNewState) & LVIS_SELECTED) && (!m_BackgroundInfoList.isDragging())) 
		{
			POSITION pos = m_BackgroundInfoList.GetFirstSelectedItemPosition();
			if (pos)
			{
				int nID = m_BackgroundInfoList.GetNextSelectedItem(pos);
				int id = static_cast<int>(m_BackgroundInfoList.GetItemData(nID));
				m_pMapEditorView->SetSelectMoveBackground(id);
			}
		}
	}

	*pResult = 0;
}

void CBackgroundListDlg::RenewList()
{
	const CToolsMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
	const Nuclear::SubMap::PBackGoundInfoVector &vet = pmap.GetBackGroundInfoVet();
	Nuclear::SubMap::PBackGoundInfoVector::const_iterator iter = vet.begin(), iterEnd = vet.end();
	int i = 0;
	m_BackgroundInfoList.DeleteAllItems();
	for (;iter!=iterEnd;++iter, ++i)
	{
		m_BackgroundInfoList.InsertItem(i, iter->name.c_str());
		m_BackgroundInfoList.SetItemData(i, i);
	}
}

BOOL CBackgroundListDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	RenewList();

	return TRUE;  

}

void CBackgroundListDlg::OnBnClickedButtonDelete()
{
	CToolsMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();

	POSITION pos = m_BackgroundInfoList.GetFirstSelectedItemPosition();
	while (pos)
	{
		int nID = m_BackgroundInfoList.GetNextSelectedItem(pos);
		int id = static_cast<int>(m_BackgroundInfoList.GetItemData(nID));
		CAction* pNowOper = new CAction(CAction::AT_BACKGROUND_INFO, m_pMapEditorView->GetLayerEditId());
		CAction::CBackgroundInfoActionAtom* oper = pNowOper->GetBackgroundInfoActionData();
		oper->m_InfoID[CAction::CActionAtom::BEFORE_OPERATOR] = id;
		oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pmap.GetBackGroundInfoVet()[id];

		if (pmap.DelBackgroundInfo(id))
		{
			oper->m_InfoID[CAction::CActionAtom::AFTER_OPERATOR] = id;
			oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = pmap.GetBackGroundInfoVet()[id];
			m_pMapEditorView->m_pOperatorList->DoOneAction(pNowOper);

			m_BackgroundInfoList.DeleteItem(nID);
			m_pMapEditorView->GetDocument()->SetModifiedFlag();
		} else {
			delete pNowOper;
		}
	}
}

void CBackgroundListDlg::OnBnClickedButtonEdit()
{
	CToolsMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
	if (m_BackgroundInfoList.GetSelectedCount() != 1)
	{
		AfxMessageBox(L"只能选择一个！");
		return;
	}
	POSITION pos = m_BackgroundInfoList.GetFirstSelectedItemPosition();
	int nItem = m_BackgroundInfoList.GetNextSelectedItem(pos);
	int id = static_cast<int>(m_BackgroundInfoList.GetItemData(nItem));
	const Nuclear::SubMap::PBackGoundInfoVector &vet = pmap.GetBackGroundInfoVet();
	if (static_cast<int>(vet.size()) <= id)
	{
		AfxMessageBox(L"出错了…………");
		return;
	}
	CBackgroundInfoDlg dlg;
	dlg.info = vet.at(id);
	if (dlg.DoModal() == IDOK)
	{
		CAction* pNowOper = new CAction(CAction::AT_BACKGROUND_INFO, m_pMapEditorView->GetLayerEditId());
		CAction::CBackgroundInfoActionAtom* oper = pNowOper->GetBackgroundInfoActionData();
		oper->m_InfoID[CAction::CActionAtom::BEFORE_OPERATOR] = id;
		oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = vet[id];

		if (vet[id].name != dlg.info.name)	
		{
			m_BackgroundInfoList.SetItemText(nItem, 0, dlg.info.name.c_str());
		}
		pmap.ModifyBackgroundInfo(id, dlg.info);

		oper->m_InfoID[CAction::CActionAtom::AFTER_OPERATOR] = id;
		oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = vet[id];
		m_pMapEditorView->m_pOperatorList->DoOneAction(pNowOper);

		m_pMapEditorView->GetDocument()->SetModifiedFlag();
	}
}

void CBackgroundListDlg::OnBnClickedButtonAdd()
{
	CBackgroundInfoDlg dlg;
	if (dlg.DoModal() == IDOK)
	{
		CAction* pNowOper = new CAction(CAction::AT_BACKGROUND_INFO, m_pMapEditorView->GetLayerEditId());
		CAction::CBackgroundInfoActionAtom* oper = pNowOper->GetBackgroundInfoActionData();
		oper->m_InfoID[CAction::CActionAtom::BEFORE_OPERATOR] = -1;
		CToolsMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
		int id = pmap.AddBackgroundInfo(dlg.info, NULL);
		int last = m_BackgroundInfoList.GetItemCount();
		m_BackgroundInfoList.InsertItem(last, dlg.info.name.c_str());
		m_BackgroundInfoList.SetItemData(last, id);
		oper->m_InfoID[CAction::CActionAtom::AFTER_OPERATOR] = id;
		oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = pmap.GetBackGroundInfoVet()[id];
		m_pMapEditorView->m_pOperatorList->DoOneAction(pNowOper);
		m_pMapEditorView->GetDocument()->SetModifiedFlag();
	}
}

BEGIN_MESSAGE_MAP(CBackgroundListDlg, CDialog)
	ON_BN_CLICKED(IDC_BUTTON_ADD, &CBackgroundListDlg::OnBnClickedButtonAdd)
	ON_BN_CLICKED(IDC_BUTTON_EDIT, &CBackgroundListDlg::OnBnClickedButtonEdit)
	ON_BN_CLICKED(IDC_BUTTON_DEL, &CBackgroundListDlg::OnBnClickedButtonDelete)
	ON_NOTIFY(LVN_ITEMCHANGED, IDC_LIST1, &CBackgroundListDlg::OnLvnItemchangedList1)
	ON_REGISTERED_MESSAGE(CLayerCtrl::DRAGLISTCTRL_LVN_ENDDRAGGING, &CBackgroundListDlg::OnLvnEnddraggingList)
END_MESSAGE_MAP()

BOOL CBackgroundListDlg::Create(CMapEditorView* pMapEditorView /*=NULL*/)
{

	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(IDD, pMapEditorView);
}

void CBackgroundListDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST1, m_BackgroundInfoList);
}

CBackgroundListDlg::~CBackgroundListDlg()
{
}

CBackgroundListDlg::CBackgroundListDlg(CWnd* pParent /*=NULL*/)
: CDialog(CBackgroundListDlg::IDD, pParent)
{

}

