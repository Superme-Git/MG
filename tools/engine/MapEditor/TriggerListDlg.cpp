

#include "stdafx.h"
#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "TriggerListDlg.h"

#include "TriggerEditorDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif



IMPLEMENT_DYNAMIC(CTriggerListDlg, CDialog)

void CTriggerListDlg::OnNMClickTree1(NMHDR *pNMHDR, LRESULT *pResult)
{
	CPoint pt;
	UINT uFlag;
	GetCursorPos(&pt);
	m_treeTriggers.ScreenToClient(&pt);
	HTREEITEM hItem = m_treeTriggers.HitTest(pt, &uFlag);
	if (hItem && (TVHT_ONITEMSTATEICON & uFlag))
	{
		HTREEITEM parent = m_treeTriggers.GetParentItem(hItem);
		if (!parent)
		{

			Nuclear::SubMap::TriggerInfoID id = 
				static_cast<Nuclear::SubMap::TriggerInfoID>(m_treeTriggers.GetItemData(hItem));
			m_pMapEditorView->ShowTriggerInfoArea(id, !m_treeTriggers.GetCheck(hItem));
		} else {

			*pResult = 1;
			return;
		}
	}
	*pResult = 0;
}

void CTriggerListDlg::OnBnClickedButton2()
{
	if (!m_treeTriggers.GetSelectedItem())
	{

		AfxMessageBox(L"请先选择一处触发特效属性\n");
	} else {
		m_pMapEditorView->SetEditMode(EMT_TRIGGER_ADD_RECT);
	}
}

void CTriggerListDlg::OnBnClickedButtonDeleteLayer()
{

	HTREEITEM hItem = m_treeTriggers.GetSelectedItem();
	if (!hItem)
	{

		AfxMessageBox(L"请先选择要删除的项\n");
		return;
	}
	HTREEITEM parent = m_treeTriggers.GetParentItem(hItem);
	Nuclear::PMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
	CAction* pOper = new CAction(CAction::AT_TRIGGER_OBJECT, m_pMapEditorView->GetLayerEditId());
	CAction::CTriggerObjectActionAtom* pTriggerData = pOper->GetTriggerObjectActionData();
	pTriggerData->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pmap.GetTriggerInfos();
	if (parent)
	{

		Nuclear::SubMap::TriggerID tID;
		tID.infoID = static_cast<Nuclear::SubMap::TriggerInfoID>(m_treeTriggers.GetItemData(parent));

		tID.areaID = static_cast<Nuclear::SubMap::WaterAreaSmallID>(m_treeTriggers.GetItemData(hItem));
		pmap.RemoveAnTriggerArea(tID);
	} else {

		Nuclear::SubMap::TriggerInfoID id = 
			static_cast<Nuclear::SubMap::TriggerInfoID>(m_treeTriggers.GetItemData(hItem));
		pmap.RemoveAnTriggerInfoVet(id);
	}
	pTriggerData->oper[CAction::CActionAtom::AFTER_OPERATOR] = pmap.GetTriggerInfos();
	m_pMapEditorView->DoAction(pOper);
	ReNewTree();
}

void CTriggerListDlg::OnBnClickedButton1()
{
	HTREEITEM hItem = m_treeTriggers.GetSelectedItem();
	if (!hItem)
	{

		AfxMessageBox(L"请先选择一处触发特效属性\n");
		return;
	}
	HTREEITEM parent = m_treeTriggers.GetParentItem(hItem);
	if (!parent)
	{

		Nuclear::SubMap::TriggerInfoID id = 
			static_cast<Nuclear::SubMap::TriggerInfoID>(m_treeTriggers.GetItemData(hItem));
		Nuclear::PMap& pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
		const Nuclear::SubMap::TriggerInfoMap &trigger = pmap.GetTriggerInfos();
		Nuclear::SubMap::TriggerInfoMap::const_iterator iter = trigger.find(id);
		if (iter != trigger.end())
		{
			CTriggerEditorDlg dlg(m_pMapEditorView);
			dlg.m_Name = iter->second.name.c_str();
			dlg.m_TriggerInfoArr = iter->second.triggerInfos;
			if (dlg.DoModal() == IDOK)
			{
				CAction* pOper = new CAction(CAction::AT_TRIGGER_OBJECT, m_pMapEditorView->GetLayerEditId());
				CAction::CTriggerObjectActionAtom* pTriggerData = pOper->GetTriggerObjectActionData();
				pTriggerData->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pmap.GetTriggerInfos();
				pmap.ModifyAnTriggerInfoVet(id, dlg.m_Name.GetBuffer(), dlg.m_TriggerInfoArr);
				pTriggerData->oper[CAction::CActionAtom::AFTER_OPERATOR] = pmap.GetTriggerInfos();
				m_pMapEditorView->DoAction(pOper);
				ReNewTree();
			}
		}
	} else {

		AfxMessageBox(L"请不要选择具体区域\n");
	}
}

void CTriggerListDlg::OnBnClickedButtonNewLayer()
{
	CTriggerEditorDlg dlg(m_pMapEditorView);
	if (dlg.DoModal() == IDOK)
	{
		Nuclear::PMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
		CAction* pOper = new CAction(CAction::AT_TRIGGER_OBJECT, m_pMapEditorView->GetLayerEditId());
		CAction::CTriggerObjectActionAtom* pTriggerData = pOper->GetTriggerObjectActionData();
		pTriggerData->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pmap.GetTriggerInfos();
		pmap.AddAnTriggerInfoVet(dlg.m_Name.GetBuffer(), dlg.m_TriggerInfoArr);
		pTriggerData->oper[CAction::CActionAtom::AFTER_OPERATOR] = pmap.GetTriggerInfos();
		m_pMapEditorView->DoAction(pOper);
		ReNewTree();
	}
}

Nuclear::SubMap::TriggerInfoID CTriggerListDlg::GetCurInfoID() const
{
	HTREEITEM hItem = m_treeTriggers.GetSelectedItem();
	HTREEITEM parent = m_treeTriggers.GetParentItem(hItem);
	if (parent)
	{

		return static_cast<Nuclear::SubMap::TriggerInfoID>(m_treeTriggers.GetItemData(parent));
	} else {

		return static_cast<Nuclear::SubMap::TriggerInfoID>(m_treeTriggers.GetItemData(hItem));
	}
}

void CTriggerListDlg::UnselectAllItems()
{
	m_treeTriggers.SelectItem(NULL);
}

void CTriggerListDlg::ListRemoveTrigger(Nuclear::SubMap::TriggerID id)
{
	std::map<unsigned int, HTREEITEM>::iterator iter = m_ItemMap.find(id.id);
	if (iter != m_ItemMap.end())
	{
		m_treeTriggers.DeleteItem(iter->second);
	}
}

void CTriggerListDlg::TryChangeSelectedTrigger(Nuclear::SubMap::TriggerID id)
{
	std::map<unsigned int, HTREEITEM>::iterator iter = m_ItemMap.find(id.id);
	if (iter != m_ItemMap.end())
	{
		m_treeTriggers.SelectItem(iter->second);
	}
}

void CTriggerListDlg::ReNewTree()
{
	CMapEditorDoc *pDoc = m_pMapEditorView->GetDocument();
	CToolsMap& pmap = pDoc->GetToolsMap();
	const Nuclear::SubMap::TriggerInfoMap &triggers = pmap.GetTriggerInfos();
	Nuclear::SubMap::TriggerInfoMap::const_iterator iter = triggers.begin();
	Nuclear::SubMap::TriggerAreaMap::const_iterator rects, rectsEnd;
	Nuclear::SubMap::TriggerID id;
	m_treeTriggers.ModifyStyle(TVS_CHECKBOXES, 0);
	m_treeTriggers.ModifyStyle(0, TVS_CHECKBOXES);
	m_treeTriggers.DeleteAllItems();
	m_ItemMap.clear();
	HTREEITEM hItem, tmpItem;
	CString tmp;
	for (;iter!=triggers.end();++iter)
	{
		hItem = m_treeTriggers.InsertItem(iter->second.name.c_str(), 0);
		m_treeTriggers.SetCheck(hItem, m_pMapEditorView->CheckUpdateTrigger(iter->first));
		id.infoID = iter->first;
		m_treeTriggers.SetItemData(hItem, id.infoID);
		rects = iter->second.areas.begin();
		rectsEnd = iter->second.areas.end();
		for (;rects!=rectsEnd;++rects)
		{
			tmp.Format(L"上:%d, 下:%d, 左:%d, 右:%d", rects->second.top, 
				rects->second.bottom, rects->second.left, rects->second.right);
			tmpItem = m_treeTriggers.InsertItem(tmp, hItem);
			m_treeTriggers.SetItemData(tmpItem, rects->first);
			id.areaID = rects->first;
			m_ItemMap[id.id] = tmpItem;
		}
	}

}

BOOL CTriggerListDlg::Create(CMapEditorView* pMapEditorView)
{

	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(IDD, pMapEditorView);
}

BOOL CTriggerListDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	ReNewTree();

	return TRUE;  

}

BEGIN_MESSAGE_MAP(CTriggerListDlg, CDialog)
	ON_BN_CLICKED(IDC_BUTTON_NEW_LAYER, &CTriggerListDlg::OnBnClickedButtonNewLayer)
	ON_BN_CLICKED(IDC_BUTTON1, &CTriggerListDlg::OnBnClickedButton1)
	ON_BN_CLICKED(IDC_BUTTON_DEL_LAYER, &CTriggerListDlg::OnBnClickedButtonDeleteLayer)
	ON_BN_CLICKED(IDC_BUTTON2, &CTriggerListDlg::OnBnClickedButton2)
	ON_NOTIFY(NM_CLICK, IDC_TREE1, &CTriggerListDlg::OnNMClickTree1)
END_MESSAGE_MAP()

void CTriggerListDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_TREE1, m_treeTriggers);
}

CTriggerListDlg::~CTriggerListDlg()
{
}

CTriggerListDlg::CTriggerListDlg(CWnd* pParent /*=NULL*/)
: CDialog(CTriggerListDlg::IDD, pParent)
{

}