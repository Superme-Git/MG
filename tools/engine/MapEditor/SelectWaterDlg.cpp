

#include "stdafx.h"
#include "MapEditor.h"
#include "..\engine\sprite\spritemanager.h"
#include "..\engine\engine\animanager.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "SelectWaterDlg.h"
#include "WaterAreaInfoDlg.h"




void CSelectWaterDlg::OnBnClickedWaterTypeCheck(UINT nID)
{
	nID -= IDC_CHECK1;
	unsigned char mask = 1 << nID;
	if (m_btnWaterTypeCheck[nID].GetCheck())
	{
		m_nTileType.tileType |= mask;
	} else {
		m_nTileType.tileType &= ~mask;
	}
	m_pMapEditorView->SetBrushType(m_nTileType.data);
}
void CSelectWaterDlg::UnselectAllItems()
{
	m_treeWaterArea.SelectItem(NULL);
}
void CSelectWaterDlg::TryChangeSelectWaterArea(Nuclear::SubMap::WaterAreaID id)
{
	std::map<unsigned int, HTREEITEM>::iterator iter = m_TreeItemMap.find(id.id);
	if (iter != m_TreeItemMap.end())
	{
		m_treeWaterArea.SelectItem(iter->second);
	}
}

void CSelectWaterDlg::OnBnClickedButton1()
{
	HTREEITEM hItem = m_treeWaterArea.GetSelectedItem();
	if (!hItem)
	{

		AfxMessageBox(L"请先选择一处水面属性\n");
		return;
	}
	HTREEITEM parent = m_treeWaterArea.GetParentItem(hItem);
	if (!parent)
	{

		Nuclear::SubMap::WaterAreaInfoID id = 
			static_cast<Nuclear::SubMap::WaterAreaInfoID>(m_treeWaterArea.GetItemData(hItem));
		Nuclear::PMap& pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
		const Nuclear::SubMap::WaterAreaInfoMap &area = pmap.GetWaterAreaInfos();
		Nuclear::SubMap::WaterAreaInfoMap::const_iterator iter = area.find(id);
		if (iter != area.end())
		{
			CWaterAreaInfoDlg dlg;
			dlg.m_Depth = iter->second.depth;
			dlg.m_Border = iter->second.border;
			if (dlg.DoModal() == IDOK)
			{
				CAction* pOper = new CAction(CAction::AT_WATER_AREA, m_pMapEditorView->GetLayerEditId());
				CAction::CWaterAreaActionAtom* pWaterData = pOper->GetWaterAreaActionData();
				pWaterData->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pmap.GetWaterAreaInfos();
				pmap.ModifyAnWaterArea(id, dlg.m_Depth, dlg.m_Border);
				pWaterData->oper[CAction::CActionAtom::AFTER_OPERATOR] = pmap.GetWaterAreaInfos();
				m_pMapEditorView->DoAction(pOper);
				RenewList();
			}
		}
	} else {

		AfxMessageBox(L"请不要选择具体区域\n");
	}
}


Nuclear::SubMap::WaterAreaInfoID CSelectWaterDlg::GetCurInfoID() const
{
	HTREEITEM hItem = m_treeWaterArea.GetSelectedItem();
	HTREEITEM parent = m_treeWaterArea.GetParentItem(hItem);
	if (parent)
	{

		return static_cast<Nuclear::SubMap::WaterAreaInfoID>(m_treeWaterArea.GetItemData(parent));
	} else {

		return static_cast<Nuclear::SubMap::WaterAreaInfoID>(m_treeWaterArea.GetItemData(hItem));
	}
}

bool CSelectWaterDlg::HasSelectWaterArea() const
{
	return (m_treeWaterArea.GetSelectedItem() != NULL);
}

void CSelectWaterDlg::OnBnClickedButtonDeleteLayer()
{
	HTREEITEM hItem = m_treeWaterArea.GetSelectedItem();
	if (!hItem)
	{

		AfxMessageBox(L"请先选择要删除的项\n");
		return;
	}
	HTREEITEM parent = m_treeWaterArea.GetParentItem(hItem);
	Nuclear::PMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
	CAction* pOper = new CAction(CAction::AT_WATER_AREA, m_pMapEditorView->GetLayerEditId());
	CAction::CWaterAreaActionAtom* pWaterData = pOper->GetWaterAreaActionData();
	pWaterData->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pmap.GetWaterAreaInfos();
	if (parent)
	{

		Nuclear::SubMap::WaterAreaInfoID id = 
			static_cast<Nuclear::SubMap::WaterAreaInfoID>(m_treeWaterArea.GetItemData(parent));
		Nuclear::SubMap::WaterAreaID WAID;
		WAID.areaID = 
			static_cast<Nuclear::SubMap::WaterAreaSmallID>(m_treeWaterArea.GetItemData(hItem));
		WAID.infoID = id;
		pmap.RemoveAnWaterArea(id, WAID);
	} else {

		Nuclear::SubMap::WaterAreaInfoID id = 
			static_cast<Nuclear::SubMap::WaterAreaInfoID>(m_treeWaterArea.GetItemData(hItem));
		pmap.RemoveAnWaterArea(id);
	}
	pWaterData->oper[CAction::CActionAtom::AFTER_OPERATOR] = pmap.GetWaterAreaInfos();
	m_pMapEditorView->DoAction(pOper);
	RenewList();
}

void CSelectWaterDlg::OnBnClickedButtonNewLayer()
{
	CWaterAreaInfoDlg dlg;
	if (dlg.DoModal() == IDOK)
	{
		Nuclear::PMap &pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
		CAction* pOper = new CAction(CAction::AT_WATER_AREA, m_pMapEditorView->GetLayerEditId());
		CAction::CWaterAreaActionAtom* pWaterData = pOper->GetWaterAreaActionData();
		pWaterData->oper[CAction::CActionAtom::BEFORE_OPERATOR] = pmap.GetWaterAreaInfos();
		pmap.AddAnWaterArea(dlg.m_Depth, dlg.m_Border);
		pWaterData->oper[CAction::CActionAtom::AFTER_OPERATOR] = pmap.GetWaterAreaInfos();
		m_pMapEditorView->DoAction(pOper);
		RenewList();
	}
}

void CSelectWaterDlg::OnBnClickedButton2()
{
	if (!m_treeWaterArea.GetSelectedItem())
	{

		AfxMessageBox(L"请先选择一处水面属性\n");
	} else {
		m_pMapEditorView->SetEditMode(EMT_WATER_IMMERSION_ADD_RECT);
	}
}

void CSelectWaterDlg::OnTcnSelchangeTab1(NMHDR *pNMHDR, LRESULT *pResult)
{	
	*pResult = 0;
	RenewEditMode();
}

void CSelectWaterDlg::RenewEditMode()
{
	int sel = m_dxTab.GetCurSel();
	switch (sel)
	{
	case 0:
		m_treeWaterList.ShowWindow(SW_SHOW);
		m_treeWaterArea.ShowWindow(SW_HIDE);
		m_comboBrushSize.ShowWindow(SW_SHOW);
		m_btnBrushSizeText.ShowWindow(SW_SHOW);
		m_btnDelete.ShowWindow(SW_HIDE);
		m_btnNew.ShowWindow(SW_HIDE);
		m_btnNewArea.ShowWindow(SW_HIDE);
		m_btnEdit.ShowWindow(SW_HIDE);
		for (int i=0;i<4;++i)
		{
			m_btnWaterTypeCheck[i].ShowWindow(SW_SHOW);
		}
		GetDlgItem(IDC_TRANS_TEXT)->ShowWindow(SW_SHOW);
		m_pMapEditorView->SetEditMode(EMT_WATER_TILE);
		m_EditMode = EMT_WATER_TILE;
		m_dxTab.m_bDrawWater = true;
		m_dxTab.m_nBrushType = m_nTileType.tileRes;
		m_pMapEditorView->SetBrushType(m_nTileType.data);
		break;
	case 1:
		m_treeWaterArea.ShowWindow(SW_SHOW);
		m_treeWaterList.ShowWindow(SW_HIDE);
		m_comboBrushSize.ShowWindow(SW_HIDE);
		m_btnBrushSizeText.ShowWindow(SW_HIDE);
		m_btnDelete.ShowWindow(SW_SHOW);
		m_btnNew.ShowWindow(SW_SHOW);
		m_btnNewArea.ShowWindow(SW_SHOW);
		m_btnEdit.ShowWindow(SW_SHOW);
		for (int i=0;i<4;++i)
		{
			m_btnWaterTypeCheck[i].ShowWindow(SW_HIDE);
		}
		GetDlgItem(IDC_TRANS_TEXT)->ShowWindow(SW_HIDE);
		m_pMapEditorView->SetEditMode(EMT_WATER_IMMERSION_NORMAL);
		m_pMapEditorView->SetBrushType(0);
		m_EditMode = EMT_WATER_IMMERSION_NORMAL;
		m_btnNewArea.SetWindowText(L"添加水域");
		m_dxTab.m_bDrawWater = false;
		break;
	}
	m_dxTab.InvalidateDXRect();
}

void CSelectWaterDlg::OnTimer(UINT_PTR nIDEvent)
{

	switch (nIDEvent)
	{
	case 1000:
		if (m_EditMode == EMT_WATER_TILE)
		{
			m_dxTab.PaintEffect();
		}
		break;
	default:
		break;
	}

	__super::OnTimer(nIDEvent);
}

void CSelectWaterDlg::OnTvnSelchangedTree1(NMHDR *pNMHDR, LRESULT *pResult)
{
	if (m_EditMode == EMT_WATER_TILE)
	{
		LPNMTREEVIEW pNMTreeView = reinterpret_cast<LPNMTREEVIEW>(pNMHDR);
		m_nTileType.tileRes = static_cast<unsigned char>(m_treeWaterList.GetItemData(pNMTreeView->itemNew.hItem));
		m_dxTab.m_nBrushType = m_nTileType.tileRes;
		m_pMapEditorView->SetBrushType(m_nTileType.data);
	}
	*pResult = 0;
}

BOOL CSelectWaterDlg::OnInitDialog()
{
	CDialog::OnInitDialog();


	m_dxTab.InsertItem( 0, _T("水面特效") );
	m_dxTab.InsertItem( 1, _T("涉水区域") );
	m_dxTab.SetCurSel(0);
	m_treeWaterList.BringWindowToTop();
	m_treeWaterArea.BringWindowToTop();
	m_comboBrushSize.BringWindowToTop();
	m_btnBrushSizeText.BringWindowToTop();
	m_btnDelete.BringWindowToTop();
	m_btnNew.BringWindowToTop();
	m_btnEdit.BringWindowToTop();
	m_btnNewArea.BringWindowToTop();
	GetDlgItem(IDC_TRANS_TEXT)->BringWindowToTop();
	for (int i=0;i<4;++i)
	{
		m_btnWaterTypeCheck[i].BringWindowToTop();
		m_btnWaterTypeCheck[i].SetCheck(TRUE);
	}
	RenewList();
	RenewCombo();
	RenewEditMode();
	SetTimer(1000, 40, NULL);
	m_dxTab.PaintEffect();

	return TRUE;  

}

BEGIN_MESSAGE_MAP(CSelectWaterDlg, CDialog)
	ON_CBN_EDITCHANGE(IDC_BRUSH_SIZE, &CSelectWaterDlg::OnCbnEditchangeBrushSize)
	ON_CBN_SELCHANGE(IDC_BRUSH_SIZE, &CSelectWaterDlg::OnCbnSelchangeBrushSize)
	ON_NOTIFY(TVN_SELCHANGED, IDC_TREE1, &CSelectWaterDlg::OnTvnSelchangedTree1)
	ON_WM_TIMER()
	ON_NOTIFY(TCN_SELCHANGE, IDC_TAB1, &CSelectWaterDlg::OnTcnSelchangeTab1)
	ON_BN_CLICKED(IDC_BUTTON2, &CSelectWaterDlg::OnBnClickedButton2)
	ON_BN_CLICKED(IDC_BUTTON_NEW_LAYER, &CSelectWaterDlg::OnBnClickedButtonNewLayer)
	ON_BN_CLICKED(IDC_BUTTON_DEL_LAYER, &CSelectWaterDlg::OnBnClickedButtonDeleteLayer)
	ON_BN_CLICKED(IDC_BUTTON1, &CSelectWaterDlg::OnBnClickedButton1)
	ON_CONTROL_RANGE(BN_CLICKED, IDC_CHECK1, IDC_CHECK4, &CSelectWaterDlg::OnBnClickedWaterTypeCheck)
END_MESSAGE_MAP()

bool CSelectWaterDlg::SetBrushSize(CString &val)
{
	int size = 0;
	size = _wtoi(val);
	if (size == 0)
		return false;
	m_pMapEditorView->SetBrushSize(size);
	return true;
}

void CSelectWaterDlg::RenewList()
{
	Nuclear::PMap& pmap = m_pMapEditorView->GetDocument()->GetToolsMap();
	m_treeWaterList.DeleteAllItems();
	m_treeWaterList.InsertItem(L"删除", 0);
	HTREEITEM hItem, tmpItem;

	for (int i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
	{
		hItem = m_treeWaterList.InsertItem(pmap.GetWaterName(i).c_str(), 0);
		m_treeWaterList.SetItemData(hItem, i+1);
		m_dxTab.SetWater(i,pmap.GetWaterName(i));
	}



	m_treeWaterArea.DeleteAllItems();
	const Nuclear::SubMap::WaterAreaInfoMap &infos = pmap.GetWaterAreaInfos();
	Nuclear::SubMap::WaterAreaInfoMap::const_iterator iter, iterEnd;
	Nuclear::SubMap::WaterAreaMap::const_iterator rects, rectsEnd;
	CString tmp;
	Nuclear::SubMap::WaterAreaID id;
	iterEnd = infos.end();
	iter=infos.begin();
	m_TreeItemMap.clear();
	for (;iter!=iterEnd;++iter)
	{
		tmp.Format(L"深度:%d，过渡区宽度:%d", iter->second.depth, iter->second.border);
		hItem = m_treeWaterArea.InsertItem(tmp, 0);
		m_treeWaterArea.SetItemData(hItem, iter->first);
		rects = iter->second.areas.begin();
		rectsEnd = iter->second.areas.end();
		id.infoID = iter->first;
		for (;rects!=rectsEnd;++rects)
		{
			tmp.Format(L"上:%d, 下:%d, 左:%d, 右:%d", rects->second.top, 
				rects->second.bottom, rects->second.left, rects->second.right);
			tmpItem = m_treeWaterArea.InsertItem(tmp, hItem);
			m_treeWaterArea.SetItemData(tmpItem, rects->first);
			id.areaID = rects->first;
			m_TreeItemMap[id.id] = tmpItem;
		}
	}

}

void CSelectWaterDlg::RenewCombo()
{
	m_comboBrushSize.InsertString(0, _T("1"));
	m_comboBrushSize.InsertString(1, _T("2"));
	m_comboBrushSize.InsertString(2, _T("3"));
	m_comboBrushSize.InsertString(3, _T("4"));
	m_comboBrushSize.InsertString(4, _T("5"));
	m_comboBrushSize.InsertString(5, _T("8"));
	m_comboBrushSize.InsertString(6, _T("10"));
	m_comboBrushSize.InsertString(7, _T("15"));
	m_comboBrushSize.SetCurSel(0);
	m_pMapEditorView->SetBrushSize(1);
}

void CSelectWaterDlg::OnCbnSelchangeBrushSize()
{

	CString temp;
	m_comboBrushSize.GetLBText(m_comboBrushSize.GetCurSel(), temp);
	if (!SetBrushSize(temp))
	{
		AfxMessageBox(_T("请选择大于0的数字"));
	}
}

void CSelectWaterDlg::OnCbnEditchangeBrushSize()
{

	CString temp;
	m_comboBrushSize.GetWindowText(temp);
	if (!SetBrushSize(temp))
	{
		AfxMessageBox(_T("请输入大于0的数字"));
	}
}

BOOL CSelectWaterDlg::Create(CMapEditorView* pMapEditorView /*=NULL*/)
{

	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(IDD, pMapEditorView);
}

void CSelectWaterDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_TAB1, m_dxTab);
	DDX_Control(pDX, IDC_TREE1, m_treeWaterList);
	DDX_Control(pDX, IDC_BRUSH_SIZE, m_comboBrushSize);
	DDX_Control(pDX, IDC_BRUSH_SIZE_TEXT, m_btnBrushSizeText);
	DDX_Control(pDX, IDC_BUTTON_DEL_LAYER, m_btnDelete);
	DDX_Control(pDX, IDC_BUTTON_NEW_LAYER, m_btnNew);
	DDX_Control(pDX, IDC_BUTTON1, m_btnEdit);
	DDX_Control(pDX, IDC_BUTTON2, m_btnNewArea);
	DDX_Control(pDX, IDC_TREE2, m_treeWaterArea);
	DDX_Control(pDX, IDC_CHECK1, m_btnWaterTypeCheck[0]);
	DDX_Control(pDX, IDC_CHECK2, m_btnWaterTypeCheck[1]);
	DDX_Control(pDX, IDC_CHECK3, m_btnWaterTypeCheck[2]);
	DDX_Control(pDX, IDC_CHECK4, m_btnWaterTypeCheck[3]);
}
CSelectWaterDlg::~CSelectWaterDlg()
{

}

CSelectWaterDlg::CSelectWaterDlg(CWnd* pParent /*=NULL*/)
: CDialog(CSelectWaterDlg::IDD, pParent)
{
	m_nTileType.tileRes = 0;
	m_nTileType.tileType = 15;
}

void CDirectXTabCtrl::PaintEffect()
{
	if (m_bDrawWater)
	{
		m_pEffectMgr->OnTick();
		checkRenderer();
		m_pRenderer->Begin();
		if (m_nBrushType > 0 && m_WaterEffects[m_nBrushType-1])
		{
			m_WaterEffects[m_nBrushType-1]->Update(25);
			m_WaterEffects[m_nBrushType-1]->Render(NULL);
		}
		m_pRenderer->End();
	}
}


void CDirectXTabCtrl::SetWater(int i, const std::wstring &name)
{
	checkRenderer();
	Nuclear::Effect* pEffect = NULL;
	if (name != L"")
	{
		pEffect = m_pEffectMgr->CreateEffect(name, true);
		pEffect->AssureResource(false);
		pEffect->SetLocation(Nuclear::SubMap::PGround::WATER_WIDTH / 2, Nuclear::SubMap::PGround::WATER_HEIGHT / 2);
		pEffect->Play();
	}
	if (m_WaterEffects[i])
		delete m_WaterEffects[i];
	m_WaterEffects[i] = pEffect;
}


CDirectXTabCtrl::~CDirectXTabCtrl()
{
	std::vector<Nuclear::Effect*>::iterator it = m_WaterEffects.begin(), ie = m_WaterEffects.end();
	for (;it!=ie;++it)
	{
		delete (*it);
	}
	delete m_pConfigMgr;
	delete m_pEffectMgr;
	if (m_pAniMgr)
		delete m_pAniMgr;
	if (m_pRenderer)
		DestroyRenderer(m_pRenderer);
}

CDirectXTabCtrl::CDirectXTabCtrl() : m_pRenderer(NULL), m_nBrushType(0), m_bDrawWater(false), m_pEffectMgr(NULL)
{
	m_pAniMgr = new Nuclear::AniManager(this);
	if( !m_pAniMgr->Init() ) 
	{

		XPLOG_INFO(L"Init Faild.\n");
		return;
	}
	for (int i=0;i<Nuclear::SubMap::PGround::WATER_EFFECT_COUNT;++i)
	{
		m_WaterEffects.push_back(NULL);
	}
	m_pConfigMgr = NULL;
}

BEGIN_MESSAGE_MAP(CDirectXTabCtrl, CTabCtrl)
END_MESSAGE_MAP()

IMPLEMENT_DYNAMIC(CSelectWaterDlg, CDialog)
IMPLEMENT_DYNAMIC(CDirectXTabCtrl, CTabCtrl)

