// StepPanel.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "StepPanel.h"
#include "PropView.h"
#include "SoundEditorDoc.h"
#include "operators.h"
#include "Input2Dlg.h"
#include "..\engine\soundarea\stepsoundtype.h"
// CStepPanel dialog

IMPLEMENT_DYNAMIC(CStepPanel, CDialog)

CStepPanel::CStepPanel(CWnd* pParent /*=NULL*/)
	: CDialog(CStepPanel::IDD, pParent), m_pView(NULL), m_bIsInited(false)
	, m_nBrushSize(1)
{

}

CStepPanel::~CStepPanel()
{
}

void CStepPanel::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_BUTTON_ADD_TYPE, m_butAddGroundType);
	DDX_Control(pDX, IDC_BUTTON_DEL_TYPE, m_butDelGroundType);
	DDX_Control(pDX, IDC_GROUND_TYPE_LIST, m_lstGroundType);
	DDX_Control(pDX, IDC_STATIC_BRUSH_SIZE, m_stcBrushSize);
	DDX_Control(pDX, IDC_COMBO_BRUSH_SIZE, m_comBrushSize);
	DDX_Control(pDX, IDC_GROUP_GROUND_TYPE, m_stcGround);
	DDX_Control(pDX, IDC_GROUP_ACTOR_TYPE, m_stcActor);
	DDX_Control(pDX, IDC_LIST_ACTOR_TYPE, m_lstActor);
	DDX_Control(pDX, IDC_BUTTON_ADD_ACTOR, m_butAddActorType);
	DDX_Control(pDX, IDC_BUTTON_DEL_ACTOR, m_butDelActorType);
	DDX_Control(pDX, IDC_BUTTON_RESET_SEL_ACT, m_butResetSelAct);
	DDX_Control(pDX, IDC_BUTTON_RESET_ALL_ACT_TYPE, m_butResetAllAct);
	DDX_Text(pDX, IDC_COMBO_BRUSH_SIZE, m_nBrushSize);
}


BEGIN_MESSAGE_MAP(CStepPanel, CDialog)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_BN_CLICKED(IDC_BUTTON_RESET_SEL_ACT, &CStepPanel::OnBnClickedButtonResetSelAct)
	ON_BN_CLICKED(IDC_BUTTON_ADD_TYPE, &CStepPanel::OnBnClickedButtonAddType)
	ON_BN_CLICKED(IDC_BUTTON_DEL_TYPE, &CStepPanel::OnBnClickedButtonDelType)
	ON_BN_CLICKED(IDC_BUTTON_ADD_ACTOR, &CStepPanel::OnBnClickedButtonAddActor)
	ON_BN_CLICKED(IDC_BUTTON_DEL_ACTOR, &CStepPanel::OnBnClickedButtonDelActor)
	ON_BN_CLICKED(IDC_BUTTON_RESET_ALL_ACT_TYPE, &CStepPanel::OnBnClickedButtonResetAllActType)
	ON_NOTIFY(LVN_ENDLABELEDIT, IDC_LIST_ACTOR_TYPE, &CStepPanel::OnLvnEndlabeleditListActorType)
	ON_NOTIFY(LVN_ENDLABELEDIT, IDC_GROUND_TYPE_LIST, &CStepPanel::OnLvnEndlabeleditGroundTypeList)
	ON_NOTIFY(NM_DBLCLK, IDC_GROUND_TYPE_LIST, &CStepPanel::OnNMDblclkGroundTypeList)
	ON_NOTIFY(LVN_ITEMCHANGED, IDC_GROUND_TYPE_LIST, &CStepPanel::OnLvnItemchangedGroundTypeList)
	ON_CBN_EDITCHANGE(IDC_COMBO_BRUSH_SIZE, &CStepPanel::OnCbnEditchangeComboBrushSize)
	ON_CBN_SELCHANGE(IDC_COMBO_BRUSH_SIZE, &CStepPanel::OnCbnSelchangeComboBrushSize)
END_MESSAGE_MAP()

void CStepPanel::ResetSize()
{
	if (!m_bIsInited)
		return;
	CRect rect;
	GetClientRect(&rect);
	rect.DeflateRect(5,5,5,5);
	rect.bottom -= 310;
	m_stcGround.MoveWindow(&rect);
	CRect tmpRect(rect.left + 10, rect.top + 20, rect.Width() / 2, rect.top + 45);
	m_butAddGroundType.MoveWindow(&tmpRect);
	tmpRect.left = tmpRect.right + 10;
	tmpRect.right = rect.right - 10;
	m_butDelGroundType.MoveWindow(&tmpRect);
	tmpRect.left = rect.left + 10;
	tmpRect.right = rect.right - 10;
	tmpRect.top = tmpRect.bottom + 10;
	tmpRect.bottom = rect.bottom - 40;
	m_lstGroundType.MoveWindow(&tmpRect);
	m_lstGroundType.SetColumnWidth(0, tmpRect.Width() / 2);
	m_lstGroundType.SetColumnWidth(1, tmpRect.Width() / 2 - 10);
	tmpRect.top = tmpRect.bottom + 10;
	tmpRect.right = tmpRect.left + 80;
	tmpRect.bottom = rect.bottom - 10;
	m_stcBrushSize.MoveWindow(&tmpRect);
	tmpRect.left = tmpRect.right + 10;
	tmpRect.right = tmpRect.left + 100;
	m_comBrushSize.MoveWindow(&tmpRect);
	rect.top = rect.bottom + 10;
	rect.bottom = rect.top + 300;
	m_stcActor.MoveWindow(&rect);
	tmpRect.left = rect.left + 10;
	tmpRect.top = rect.top + 20;
	tmpRect.right = rect.right - 10;
	tmpRect.bottom = rect.bottom - 80;
	m_lstActor.MoveWindow(&tmpRect);
	m_lstActor.SetColumnWidth(0, tmpRect.Width() / 2);
	m_lstActor.SetColumnWidth(1, tmpRect.Width() / 2 - 10);
	tmpRect.right = rect.Width() / 2;
	tmpRect.top = tmpRect.bottom + 10;
	tmpRect.bottom = tmpRect.top + 25;
	m_butAddActorType.MoveWindow(&tmpRect);
	tmpRect.left = tmpRect.right + 10;
	tmpRect.right = rect.right - 10;
	m_butDelActorType.MoveWindow(&tmpRect);
	tmpRect.top = tmpRect.bottom + 10;
	tmpRect.bottom = tmpRect.top + 25;
	m_butResetAllAct.MoveWindow(&tmpRect);
	tmpRect.left = rect.left + 10;
	tmpRect.right = rect.Width() / 2;
	m_butResetSelAct.MoveWindow(&tmpRect);
}

void CStepPanel::ResetCont()
{
	if (!m_bIsInited)
		return;
}


void CStepPanel::InsertActorItem(int pos, const std::wstring &name, const std::wstring &des)
{
	m_lstActor.InsertItem(pos, des.c_str());
	m_lstActor.SetItemText(pos, 1, name.c_str());
	m_ActorNames.insert(m_ActorNames.begin() + pos, name);
}
void CStepPanel::InsertGroundItem(int pos, int id, const std::wstring &des, COLORREF color, bool visable)
{
	m_lstGroundType.InsertItem(pos, des.c_str());
	CString tmpStr;
	tmpStr.Format(L"%d", id);
	m_lstGroundType.SetItemText(pos, 1, tmpStr);
	m_lstGroundType.SetItemData(pos, id);
	m_lstGroundType.SetItemColor(id, color);
	m_lstGroundType.SetCheck(pos, visable);
}

// CStepPanel message handlers

BOOL CStepPanel::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_lstActor.SetExtendedStyle(LVS_EX_FULLROWSELECT);
	m_lstGroundType.SetExtendedStyle(LVS_EX_CHECKBOXES);
	m_pView = static_cast<CPropView*>(GetParent());
	if (!m_pView->GetDocument()->GetEngineBase())
		return FALSE;
	Nuclear::StepSoundType* pSST = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType();
	const Nuclear::XPSSATypeMap &atm = pSST->GetActorMap();
	Nuclear::XPSSATypeMap::const_iterator aIt = atm.begin(), aIe = atm.end();
	m_ActorNames.clear();
	int i = 0;
	m_lstActor.InsertColumn(0, L"角色类型说明");
	m_lstActor.InsertColumn(1, L"角色类型名字");
	for (;aIt!=aIe;++aIt,++i)
	{
		InsertActorItem(i, aIt->first, aIt->second);
	}
	const Nuclear::XPSSGTypeMap &gtm = pSST->GetGroundMap();
	Nuclear::XPSSGTypeMap::const_iterator gIt = gtm.begin(), gIe = gtm.end();
	Nuclear::XPSSGColorMap &gtc = pSST->GetGroundColor();
	m_lstGroundType.InsertColumn(0, L"地表类型说明");
	m_lstGroundType.InsertColumn(1, L"地表类型ID");
	for (i=0;gIt!=gIe;++gIt,++i)
	{
		InsertGroundItem(i, gIt->first, gIt->second, XPC2REF(gtc[gIt->first]), true);
	}

	m_comBrushSize.ResetContent();
	m_comBrushSize.InsertString(0, L"1");
	m_comBrushSize.SetItemData(0, 1);
	m_comBrushSize.InsertString(1, L"2");
	m_comBrushSize.SetItemData(1, 2);
	m_comBrushSize.InsertString(2, L"3");
	m_comBrushSize.SetItemData(2, 3);
	m_comBrushSize.InsertString(3, L"4");
	m_comBrushSize.SetItemData(3, 4);
	m_comBrushSize.InsertString(4, L"5");
	m_comBrushSize.SetItemData(4, 5);
	m_comBrushSize.InsertString(5, L"10");
	m_comBrushSize.SetItemData(5, 10);
	m_comBrushSize.SetCurSel(0);

	m_bIsInited = true;
	ResetCont();
	ResetSize();
	return TRUE;
}

void CStepPanel::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

void CStepPanel::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}

void CStepPanel::OnBnClickedButtonAddType()
{
	CInput2Dlg dlg;
	dlg.strCaption = L"添加地表类型";
	dlg.strInfo = L"    请输入要添加的地表类型的ID（不能和已有的ID重复）和说明:";
	dlg.strInfo1 = L"ID:";
	dlg.strInfo2 = L"说明:";
	Nuclear::StepSoundType* pSST = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType();
	const Nuclear::XPSSGTypeMap &gtm = pSST->GetGroundMap();
	int id = 0;
	bool ok = true;
	do {
		ok = true;
		if (dlg.DoModal() == IDOK)
		{
			id = _wtoi(dlg.strEdit1.GetBuffer());
			dlg.strEdit1.ReleaseBuffer();
			if (gtm.find(id) != gtm.end())
			{
				MessageBox(L"地表类型ID重复了");
				ok = false;
			}
		} else {
			return;
		}
	} while (!ok);
	std::wstring des = dlg.strEdit2.GetBuffer();
	dlg.strEdit2.ReleaseBuffer();
	pSST->SetGroundType(id, des);
	Nuclear::XPSSGColorMap &gtc = pSST->GetGroundColor();
	int i1 = (rand() % 5) * 64;
	if (i1 == 256)
		i1 -= 1;
	int i2 = (rand() % 5) * 64;
	if (i2 == 256)
		i2 -= 1;
	int i3 = (rand() % 5) * 64;
	if (i3 == 256)
		i3 -= 1;
	Nuclear::XPCOLOR color(0xFF, i1, i2, i3);
	gtc[id] = color;
	InsertGroundItem(m_lstGroundType.GetItemCount(), id, des, XPC2REF(color), true);
	std::wstring filename = theApp.GetExePath();
	filename += L"\\res\\sound\\";
	pSST->SaveSoundType(filename);
}

void CStepPanel::OnBnClickedButtonDelType()
{
	if (m_lstGroundType.GetSelectedCount() != 1)
	{
		MessageBox(L"请选择一种地表类型");
		return;
	}
	POSITION pos = m_lstGroundType.GetFirstSelectedItemPosition();
	int curSel = m_lstGroundType.GetNextSelectedItem(pos);
	int id = static_cast<int>(m_lstGroundType.GetItemData(curSel));
	Nuclear::StepSoundType* pSST = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType();
	const Nuclear::XPSSGTypeMap &gtm = pSST->GetGroundMap();
	Nuclear::XPSSGTypeMap::const_iterator it = gtm.find(id);
	if (it == gtm.end())
		return;
	CString prop;
	prop.Format(L"请确认删除：\n    ID：%d\n  描述：%s", id, it->second.c_str());
	if (MessageBox(prop, NULL, MB_YESNO) == IDYES)
	{
		pSST->RemoveGroundType(id);
		std::wstring filename = theApp.GetExePath();
		filename += L"\\res\\sound\\";
		pSST->SaveSoundType(filename);
		m_lstGroundType.DeleteItem(curSel);
	}
}

void CStepPanel::OnBnClickedButtonAddActor()
{
	CInput2Dlg dlg;
	dlg.strCaption = L"添加角色类型";
	dlg.strInfo = L"    请输入要添加的角色类型的名字（不能和已有的名字重复，只能为英文字母）和说明:";
	dlg.strInfo1 = L"名字:";
	dlg.strInfo2 = L"说明:";
	Nuclear::StepSoundType* pSST = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType();
	const Nuclear::XPSSATypeMap &atm = pSST->GetActorMap();
	std::wstring name;
	bool ok = true;
	do {
		ok = true;
		if (dlg.DoModal() == IDOK)
		{
			name = dlg.strEdit1.GetBuffer();
			dlg.strEdit1.ReleaseBuffer();
			if (atm.find(name) != atm.end())
			{
				MessageBox(L"角色类型名字重复了");
				ok = false;
			}
		} else {
			return;
		}
	} while (!ok);
	std::wstring des = dlg.strEdit2.GetBuffer();
	dlg.strEdit2.ReleaseBuffer();
	pSST->SetActorType(name, des);
	InsertActorItem(m_lstActor.GetItemCount(), name, des);
	std::wstring filename = theApp.GetExePath();
	filename += L"\\res\\sound\\";
	pSST->SaveSoundType(filename);
}

void CStepPanel::OnBnClickedButtonDelActor()
{
	if (m_lstActor.GetSelectedCount() != 1)
	{
		MessageBox(L"请选择一种角色类型");
		return;
	}
	POSITION pos = m_lstActor.GetFirstSelectedItemPosition();
	int curSel = m_lstActor.GetNextSelectedItem(pos);
	Nuclear::StepSoundType* pSST = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType();
	const Nuclear::XPSSATypeMap &atm = pSST->GetActorMap();
	Nuclear::XPSSATypeMap::const_iterator it = atm.find(m_ActorNames[curSel]);
	if (it == atm.end())
		return;
	CString prop;
	prop.Format(L"请确认删除：\n  名字：%s\n  描述：%s", it->first.c_str(), it->second.c_str());
	if (MessageBox(prop, NULL, MB_YESNO) == IDYES)
	{
		prop.Format(L"删除成功，请自行删除 sound\\step 文件夹下面的 %s 文件夹", it->first.c_str());
		pSST->RemoveActorType(it->first);
		std::wstring filename = theApp.GetExePath();
		filename += L"\\res\\sound\\";
		pSST->SaveSoundType(filename);
		m_lstActor.DeleteItem(curSel);
		m_ActorNames.erase(m_ActorNames.begin()+curSel);
		MessageBox(prop);
	}
}

void CStepPanel::ResetActType(const std::wstring &name)
{
	Nuclear::StepSoundType* pSST = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType();
	pSST->ClearSoundCount(name);
	const Nuclear::XPSSGTypeMap &gtm = pSST->GetGroundMap();
	Nuclear::XPSSGTypeMap::const_iterator it = gtm.begin(), ie = gtm.end();
	CString path = theApp.GetExePath() + L"\\res\\sound\\step\\";
	path = path + name.c_str();
	path = path + L"\\%d-%d.*";
	CFileFind myFinder;
	for (;it!=ie;++it)
	{
		int i = 0;
		while(1) 
		{
			CString filepad;
			filepad.Format(path, it->first, i);
			BOOL working = myFinder.FindFile(filepad.GetBuffer());
			filepad.ReleaseBuffer();
			if (working)
			{
				++i;
			} else {
				break;
			}
		}
		if (i > 0)
		{
			pSST->SetSoundCount(name, it->first, i);
		}
	}
}

void CStepPanel::OnBnClickedButtonResetSelAct()
{
	if (m_lstActor.GetSelectedCount() != 1)
	{
		MessageBox(L"请选择一种角色类型");
		return;
	}
	POSITION pos = m_lstActor.GetFirstSelectedItemPosition();
	int curSel = m_lstActor.GetNextSelectedItem(pos);
	Nuclear::StepSoundType* pSST = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType();
	const Nuclear::XPSSATypeMap &atm = pSST->GetActorMap();
	Nuclear::XPSSATypeMap::const_iterator it = atm.find(m_ActorNames[curSel]);
	if (it == atm.end())
	{
		MessageBox(L"重设失败");
		return;
	}
	ResetActType(m_ActorNames[curSel]);
	std::wstring filename = theApp.GetExePath();
	filename += L"\\res\\sound\\";
	pSST->SaveSoundType(filename);
}


void CStepPanel::OnBnClickedButtonResetAllActType()
{
	Nuclear::StepSoundType* pSST = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType();
	const Nuclear::XPSSATypeMap &atm = pSST->GetActorMap();
	Nuclear::XPSSATypeMap::const_iterator it = atm.begin(), ie = atm.end();
	for (;it!=ie;++it)
	{
		ResetActType(it->first);
	}
	std::wstring filename = theApp.GetExePath();
	filename += L"\\res\\sound\\";
	pSST->SaveSoundType(filename);
}

void CStepPanel::OnLvnEndlabeleditListActorType(NMHDR *pNMHDR, LRESULT *pResult)
{
	NMLVDISPINFO *pDispInfo = reinterpret_cast<NMLVDISPINFO*>(pNMHDR);
	if (!pDispInfo->item.pszText)
	{
		*pResult = 0;
		return;
	}
	Nuclear::StepSoundType* pSST = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType();
	const Nuclear::XPSSATypeMap &atm = pSST->GetActorMap();
	Nuclear::XPSSATypeMap::const_iterator it = atm.find(m_ActorNames[pDispInfo->item.iItem]);
	if (it == atm.end())
		return;
	pSST->SetActorType(it->first, pDispInfo->item.pszText);
	std::wstring filename = theApp.GetExePath();
	filename += L"\\res\\sound\\";
	pSST->SaveSoundType(filename);
	*pResult = TRUE;
}

void CStepPanel::OnLvnEndlabeleditGroundTypeList(NMHDR *pNMHDR, LRESULT *pResult)
{
	NMLVDISPINFO *pDispInfo = reinterpret_cast<NMLVDISPINFO*>(pNMHDR);
	if (!pDispInfo->item.pszText)
	{
		*pResult = 0;
		return;
	}
	Nuclear::StepSoundType* pSST = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType();
	const Nuclear::XPSSGTypeMap &gtm = pSST->GetGroundMap();
	int id = static_cast<int>(pDispInfo->item.lParam);
	Nuclear::XPSSGTypeMap::const_iterator it = gtm.find(id);
	if (it == gtm.end())
		return;
	pSST->SetGroundType(id, pDispInfo->item.pszText);
	std::wstring filename = theApp.GetExePath();
	filename += L"\\res\\sound\\";
	pSST->SaveSoundType(filename);
	*pResult = TRUE;
}

void CStepPanel::OnNMDblclkGroundTypeList(NMHDR *pNMHDR, LRESULT *pResult)
{
	*pResult = 0;
	if (m_lstGroundType.GetSelectedCount() != 1)
	{
		MessageBox(L"请选择一种地表类型");
		return;
	}
	POSITION pos = m_lstGroundType.GetFirstSelectedItemPosition();
	int curSel = m_lstGroundType.GetNextSelectedItem(pos);
	int id = static_cast<int>(m_lstGroundType.GetItemData(curSel));
	Nuclear::StepSoundType* pSST = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType();
	Nuclear::XPSSGColorMap &gtc = pSST->GetGroundColor();
	Nuclear::XPSSGColorMap::iterator it = gtc.find(id);
	if (it == gtc.end())
		return;
	COLORREF color = XPC2REF(it->second);
	CColorDialog dlg(color);
	if (dlg.DoModal() == IDOK)
	{
		color = dlg.GetColor();
		it->second = REF2XPC(color);
		m_lstGroundType.SetItemColor(id, color);
		m_lstGroundType.RedrawItems(curSel, curSel);
		std::wstring filename = theApp.GetExePath();
		filename += L"\\res\\sound\\";
		pSST->SaveSoundType(filename);
	}
}

void CStepPanel::OnLvnItemchangedGroundTypeList(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	if (pNMLV->uChanged == LVIF_STATE)
	{
		if ((pNMLV->uNewState ^ pNMLV->uOldState) & LVIS_SELECTED)//取消选择或者选择
		{
			if (pNMLV->uOldState & LVIS_SELECTED)	//取消选择
			{
				m_pView->GetDocument()->m_nStepBrush = -1;
			} else {
				m_pView->GetDocument()->m_nStepBrush = static_cast<int>(pNMLV->lParam);
			}
		}

		if (((pNMLV->uNewState ^ pNMLV->uOldState) & INDEXTOSTATEIMAGEMASK(1)) && pNMLV->uOldState)//复选框
		{
			m_pView->GetDocument()->SetStepVisable(static_cast<int>(pNMLV->lParam), (pNMLV->uNewState & INDEXTOSTATEIMAGEMASK(1)) == 0);
		}
	}
	*pResult = 0;
}

void CStepPanel::OnCbnEditchangeComboBrushSize()
{
	UpdateData();
	m_pView->GetDocument()->m_nBrushSize = m_nBrushSize;
}

void CStepPanel::OnCbnSelchangeComboBrushSize()
{
	m_nBrushSize = static_cast<int>(m_comBrushSize.GetItemData(m_comBrushSize.GetCurSel()));
	m_pView->GetDocument()->m_nBrushSize = m_nBrushSize;
}
