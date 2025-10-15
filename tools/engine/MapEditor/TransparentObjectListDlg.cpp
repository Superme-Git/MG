

#include "stdafx.h"
#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "TransparentObjectListDlg.h"
#include "TransparentObjectEditorDlg.h"

#include "Action.h"



IMPLEMENT_DYNAMIC(CTransparentObjectListDlg, CDialog)

void CTransparentObjectListDlg::OnBnClickedButton2()
{
	
	m_pMapEditorView->ToPlaySelectedTransparentObjects();
}
void CTransparentObjectListDlg::OnBnClickedButton1()
{
	
	if (m_listTrans.GetSelectedCount() != 1)
	{
		AfxMessageBox(_T("请只选择1个传送对象"));
		return;
	}
	POSITION pos = m_listTrans.GetFirstSelectedItemPosition();
	int nItem = m_listTrans.GetNextSelectedItem(pos);
	EditTransObj(nItem);
}

void CTransparentObjectListDlg::UnselectAllItems()
{
	int i;
	for (i=0;i<m_listTrans.GetItemCount();i++)
	{
		m_listTrans.SetItemState(i, ~LVIS_SELECTED, LVIS_SELECTED);
	}
}

void CTransparentObjectListDlg::TryChangeSelectedTransparentObject(unsigned int transID, BOOL add)
{
	/*if (m_mapElementIdToListId.find(elementID) != m_mapElementIdToListId.end())
	{
			m_listElements.SetSel(m_mapElementIdToListId[elementID], add);
	}*/
	int i;
	for (i=0;i<m_listTrans.GetItemCount();i++)
	{
		if (static_cast<unsigned int>(m_listTrans.GetItemData(i)) == transID)
		{
			m_listTrans.SetItemState(i, add?LVIS_SELECTED:~LVIS_SELECTED, LVIS_SELECTED);
		}
	}
	
}

void CTransparentObjectListDlg::OnLvnItemchangedList1(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	
	if ((pNMLV->uOldState ^ pNMLV->uNewState) & LVIS_SELECTED) 
	{
		if (pNMLV->uOldState & LVIS_SELECTED)
		{
			
			m_pMapEditorView->TryUnselectTransparentObject(static_cast<unsigned int>(m_listTrans.GetItemData(pNMLV->iItem)));
		} else {
			
			m_pMapEditorView->TrySelectTransparentObject(static_cast<unsigned int>(m_listTrans.GetItemData(pNMLV->iItem)));
		}
	}
	*pResult = 0;
}


void CTransparentObjectListDlg::OnNMDblclkList1(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 0;
	
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	if (pNMLV->iItem < 0)
		return;
	EditTransObj(pNMLV->iItem);
	
}

void CTransparentObjectListDlg::EditTransObj(int item)
{
	unsigned int transID = static_cast<unsigned int>(m_listTrans.GetItemData(item));
	const Nuclear::SubMap::TransObjInfoMap &transObjs = 
		m_pMapEditorView->GetDocument()->GetToolsMap().GetTransInfos();
	Nuclear::SubMap::TransObjInfoMap::const_iterator iter = transObjs.find(transID);
	if (iter == transObjs.end())
		return;
	CTransparentObjectEditorDlg dlg;
	dlg.m_nTime = iter->second.time;
	dlg.m_nX = iter->second.scalPoint.x;
	dlg.m_nY = iter->second.scalPoint.y;
	dlg.m_nWidth = iter->second.width;
	dlg.m_nHeight = iter->second.height;
	
	if( IDOK != dlg.DoModal() )
		return;
	Nuclear::SubMap::TransObjInfo tmpInfo = iter->second;
	tmpInfo.time = dlg.m_nTime;
	tmpInfo.scalPoint.x = dlg.m_nX;
	tmpInfo.scalPoint.y = dlg.m_nY;
	tmpInfo.width = dlg.m_nWidth;
	tmpInfo.height = dlg.m_nHeight;

	CAction *pNowOper = new CAction(CAction::AT_TRANSPARENT_OBJECT, m_pMapEditorView->GetLayerEditId());
	CAction::CTransparentObjectActionAtom *oper = pNowOper->GetTransparentObjectsActionData();
	Nuclear::PMap &map = m_pMapEditorView->GetDocument()->GetToolsMap();
	oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = map.GetTransInfos();
	map.ModifyAnTransObj(iter->first, tmpInfo);
	oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = map.GetTransInfos();
	m_pMapEditorView->DoAction(pNowOper);
	m_pMapEditorView->GetDocument()->SetModifiedFlag();
	

}

void CTransparentObjectListDlg::RenewSize()
{
	CRect rectCtrl;
	GetClientRect(&rectCtrl);
	rectCtrl.left += 5;
	rectCtrl.right -= 5;
	rectCtrl.bottom -= 28;
	rectCtrl.top += 5;
	m_listTrans.MoveWindow(&rectCtrl);
	rectCtrl.right = rectCtrl.left + 30;
	rectCtrl.top = rectCtrl.bottom + 5;
	rectCtrl.bottom = rectCtrl.top + 20;
	GetDlgItem(IDC_BUTTON_NEW_LAYER)->MoveWindow(&rectCtrl);
	rectCtrl.left = rectCtrl.right + 5;
	rectCtrl.right = rectCtrl.left + 30;
	GetDlgItem(IDC_BUTTON_DEL_LAYER)->MoveWindow(&rectCtrl);
	rectCtrl.left = rectCtrl.right + 5;
	rectCtrl.right = rectCtrl.left + 30;
	GetDlgItem(IDC_BUTTON1)->MoveWindow(&rectCtrl);
	rectCtrl.left = rectCtrl.right + 5;
	rectCtrl.right = rectCtrl.left + 30;
	GetDlgItem(IDC_BUTTON2)->MoveWindow(&rectCtrl);
}

void CTransparentObjectListDlg::OnSizing(UINT fwSide, LPRECT pRect)
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

	
	RenewSize();
}

void CTransparentObjectListDlg::OnOK()
{
	
}

void CTransparentObjectListDlg::OnBnClickedDeleteObject()
{
	
	POSITION pos = m_listTrans.GetFirstSelectedItemPosition();
	unsigned int transID;
	int nItem;
	CAction *pNowOper = new CAction(CAction::AT_TRANSPARENT_OBJECT, m_pMapEditorView->GetLayerEditId());
	CAction::CTransparentObjectActionAtom *oper = pNowOper->GetTransparentObjectsActionData();
	Nuclear::PMap &map = m_pMapEditorView->GetDocument()->GetToolsMap();
	oper->oper[CAction::CActionAtom::BEFORE_OPERATOR] = map.GetTransInfos();
	while(pos) 
	{
		nItem = m_listTrans.GetNextSelectedItem(pos);
		transID = static_cast<unsigned int>(m_listTrans.GetItemData(nItem));
		map.RemoveAnTransObj(transID);
		m_listTrans.DeleteItem(nItem);
		pos = m_listTrans.GetFirstSelectedItemPosition();
	}
	oper->oper[CAction::CActionAtom::AFTER_OPERATOR] = map.GetTransInfos();
	m_pMapEditorView->DoAction(pNowOper);
	m_pMapEditorView->GetDocument()->SetModifiedFlag();
}

void CTransparentObjectListDlg::OnBnClickedNewObject()
{
	CTransparentObjectEditorDlg dlg;
	if (IDOK != dlg.DoModal())
	{
		return;
	}
	if (m_pMapEditorView->AddTransparentObject(dlg.m_nTime, dlg.m_nX, dlg.m_nY, dlg.m_nWidth, dlg.m_nHeight))
	{
		ReNewTree();
	} else {
		AfxMessageBox(_T("添加失败"));
	}	
}

void CTransparentObjectListDlg::ReNewTree()
{
	const Nuclear::SubMap::TransObjInfoMap &transObjs = 
		m_pMapEditorView->GetDocument()->GetToolsMap().GetTransInfos();
	Nuclear::SubMap::TransObjInfoMap::const_iterator iter = transObjs.begin();
	m_listTrans.DeleteAllItems();
	for (;iter!=transObjs.end();iter++)
	{
		CString transID;
		transID.Format(_T("%d"), iter->first);
		m_listTrans.InsertItem(0, transID);
		m_listTrans.SetItemText(0, 1, _T("aaaaa"));
		
		m_listTrans.SetItemData(0, iter->first);
	}
}

BOOL CTransparentObjectListDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	
	CRect rect;
	GetWindowRect(&rect);
	m_nMinWidth = rect.Width();
	m_nMinHeight = rect.Height();

	RenewSize();
	m_listTrans.InsertColumn(0, _T("ID"), LVCFMT_LEFT, 40);
	m_listTrans.InsertColumn(1, _T("图片路径"), LVCFMT_LEFT, 120);
	m_listTrans.SetExtendedStyle(LVS_EX_FULLROWSELECT);
	ReNewTree();


	return FALSE;  
}

BOOL CTransparentObjectListDlg::Create(CMapEditorView* pMapEditorView /*=NULL*/)
{
	
	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(IDD, pMapEditorView);
}

BEGIN_MESSAGE_MAP(CTransparentObjectListDlg, CDialog)
	ON_BN_CLICKED(IDC_BUTTON_NEW_LAYER, &CTransparentObjectListDlg::OnBnClickedNewObject)
	ON_BN_CLICKED(IDC_BUTTON_DEL_LAYER, &CTransparentObjectListDlg::OnBnClickedDeleteObject)
	ON_WM_SIZING()
	ON_NOTIFY(NM_DBLCLK, IDC_LIST1, &CTransparentObjectListDlg::OnNMDblclkList1)
	ON_NOTIFY(LVN_ITEMCHANGED, IDC_LIST1, &CTransparentObjectListDlg::OnLvnItemchangedList1)
	ON_BN_CLICKED(IDC_BUTTON1, &CTransparentObjectListDlg::OnBnClickedButton1)
	ON_BN_CLICKED(IDC_BUTTON2, &CTransparentObjectListDlg::OnBnClickedButton2)
END_MESSAGE_MAP()



void CTransparentObjectListDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST1, m_listTrans);
}


CTransparentObjectListDlg::~CTransparentObjectListDlg()
{
}

CTransparentObjectListDlg::CTransparentObjectListDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CTransparentObjectListDlg::IDD, pParent)
{

}

