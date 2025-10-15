

#include "stdafx.h"
#include "MapEditor.h"
#include "..\engine\sprite\spritemanager.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "TriggerEditorDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

using Nuclear::SubMap::TriggerInfoVet;


IMPLEMENT_DYNAMIC(CTriggerEditorDlg, CDialog)

void CTriggerEditorDlg::OnBnClickedCtrlSingle()
{
	UpdateData(TRUE);
	if (m_bNonrandomAndSingle && m_bSpriteTrigger)
	{
		AfxMessageBox(L"固定唯一触发 和 精灵触发 不能同时启用！");
		m_bNonrandomAndSingle = FALSE;
		UpdateData(FALSE);
	} else {
		m_bModifyFlag = true;
	}
}

void CTriggerEditorDlg::OnEnKillfocusEditY()
{
	int oldY = m_SpriteExcPoint.y;
	UpdateData(TRUE);
	if (oldY != m_SpriteExcPoint.y)
		m_bModifyFlag = true;
}

void CTriggerEditorDlg::OnEnKillfocusEditX()
{
	int oldX = m_SpriteExcPoint.x;
	UpdateData(TRUE);
	if (oldX != m_SpriteExcPoint.x)
		m_bModifyFlag = true;
}

void CTriggerEditorDlg::OnEnKillfocusEdit1()
{
	UpdateData(TRUE);
}

void CTriggerEditorDlg::OnEnKillfocusMaxDelta()
{
	int oldMaxDelta = m_nMaxDelta;
	UpdateData(TRUE);
	if (oldMaxDelta != m_nMaxDelta)
		m_bModifyFlag = true;
}

void CTriggerEditorDlg::OnEnKillfocusFrequency()
{
	float oldFrequency = m_fFrequency;
	UpdateData(TRUE);
	if (fabs(oldFrequency - m_fFrequency) > 0.00001)
		m_bModifyFlag = true;
}

void CTriggerEditorDlg::OnNMKillfocusList2(NMHDR *pNMHDR, LRESULT *pResult)
{
	m_bModifyFlag = true;
	*pResult = 0;
}

void CTriggerEditorDlg::OnEnKillfocusEndTime()
{
	int oldEndTime = m_nEndTime;
	UpdateData(TRUE);
	if (oldEndTime != m_nEndTime)
		m_bModifyFlag = true;
}

void CTriggerEditorDlg::OnEnKillfocusDelta()
{
	int oldDelta = m_nDelta;
	UpdateData(TRUE);
	if (oldDelta != m_nDelta)
		m_bModifyFlag = true;
}

void CTriggerEditorDlg::OnCbnSelchangeComboLayer()
{
	m_bModifyFlag = true;
}

void CTriggerEditorDlg::OnEnKillfocusBeginTime()
{
	int oldBeginTime = m_nBeginTime;
	UpdateData(TRUE);
	if (oldBeginTime != m_nBeginTime)
		m_bModifyFlag = true;
}

void CTriggerEditorDlg::OnBnClickedButton3()	
{
	FillToCtrl();
}

void CTriggerEditorDlg::OnBnClickedButton2()	
{
	CString errStr;
	if (CheckData(errStr))
	{
		FillFromCtrl(m_nCurSelectedEffect);
	} else {
		AfxMessageBox(L"发现如下错误： \n" + errStr);
	}
}

void CTriggerEditorDlg::OnBnClickedCtrlSprite()
{
	UpdateData(TRUE);
	if (m_bNonrandomAndSingle && m_bSpriteTrigger)
	{
		AfxMessageBox(L"固定唯一触发 和 精灵触发 不能同时启用！");
		m_bSpriteTrigger = FALSE;
		UpdateData(FALSE);
	} else {
		ResetEnable();
		m_bModifyFlag = true;
	}
}

void CTriggerEditorDlg::OnCbnSelchangeCombo1()
{
	int selID = m_comboEffects.GetCurSel();
	if (m_bModifyFlag)
	{
		switch (AfxMessageBox(L"当前特效的修改没有得到保存，是否保存？", MB_YESNOCANCEL))
		{
		case IDYES:
			if (selID != m_nCurSelectedEffect)
			{
				CString errStr;
				if (CheckData(errStr))
				{
					FillFromCtrl(m_nCurSelectedEffect);
				} else {
					AfxMessageBox(L"发现如下错误： \n" + errStr);
					m_comboEffects.SetCurSel(m_nCurSelectedEffect);
					return;
				}
			}
			break;
		case IDNO:
			break;
		case IDCANCEL:
			m_comboEffects.SetCurSel(m_nCurSelectedEffect);
			return;
			break;
		}
	}
	FillToCtrl();
	m_nCurSelectedEffect = selID;
}

void CTriggerEditorDlg::OnBnClickedButton1()
{
	int nID = m_comboEffects.GetCurSel();
	int id = static_cast<int>(m_comboEffects.GetItemData(nID));
	if (id != -1)	
	{
		m_TriggerInfoArr[id].triggerTypeFlag |= Nuclear::SubMap::TRIGGER_DELETE;
		m_comboEffects.DeleteString(m_comboEffects.GetCurSel());
		if (nID > 0)
			--nID;
		m_nCurSelectedEffect = nID;
		m_comboEffects.SetCurSel(nID);
		FillToCtrl();
	}
}

void CTriggerEditorDlg::OnBnClickedOk()
{
	UpdateData(TRUE);
	if (m_Name == L"")
	{
		AfxMessageBox(L"名字不能为空！");
		return;
	}
	CString errStr;
	if (!CheckData(errStr))
	{
		AfxMessageBox(L"发现如下错误：\n" + errStr);
	} else {
		FillFromCtrl(m_nCurSelectedEffect);
		OnOK();
	}
}

bool CTriggerEditorDlg::CheckData(CString &errStr)
{
	UpdateData(TRUE);
	errStr = L"";
	bool result = true;
	if (m_EffectName == L"")
	{
		errStr = L"  特效的名字为空\n";
		result = false;
	}
	if (m_fFrequency < 0.00001f)
	{
		errStr += L"  特效触发概率不能为0\n";
		result = false;
	}

	if (m_fFrequency > 1.0f)
	{
		errStr += L"  特效触发概率不能大于1\n";
		result = false;
	}

	if (m_nDelta <= 0)
	{
		errStr += L"  特效触发间隔要大于0\n";
		result = false;
	}
	return result;
}

BOOL CTriggerEditorDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_listActions.SetExtendedStyle(LVS_EX_CHECKBOXES);
	const std::map<unsigned char, std::wstring> &desMap = m_pView->GetSpriteManager()->actionType.GetDesMap();
	std::map<unsigned char, std::wstring>::const_iterator it, ie;
	it = desMap.begin();
	ie = desMap.end();
	int i=0;
	m_listActions.DeleteAllItems();
	for (;it!=ie;++it, ++i)
	{
		m_listActions.InsertItem(i, it->second.c_str());
		m_listActions.SetItemData(i, it->first);
	}
	m_comboLayer.AddString(L"物低与物中之间");
	m_comboLayer.AddString(L"物件中层");
	m_comboLayer.AddString(L"物中与物高之间");
	m_comboLayer.AddString(L"最上层");

	ResetList();
	m_nCurSelectedEffect = 0;
	m_comboEffects.SetCurSel(0);
	FillToCtrl();

	return TRUE;  

}

void CTriggerEditorDlg::FillFromCtrl(int selID)
{
	int id = static_cast<int>(m_comboEffects.GetItemData(selID));
	bool neweffect = false;
	if (id == -1)	
	{
		id = static_cast<int>(m_TriggerInfoArr.size());
		Nuclear::SubMap::TriggerInfo tmpInfo;
		m_TriggerInfoArr.push_back(tmpInfo);
		m_comboEffects.InsertString(0, m_EffectName);
		m_comboEffects.SetItemData(0, id);
		m_nCurSelectedEffect = 0;
		m_comboEffects.SetCurSel(0);
		neweffect = true;
	}
	UpdateData(TRUE);
	Nuclear::SubMap::TriggerInfo& info = m_TriggerInfoArr[id];
	Nuclear::SubMap::ObjMapKey newKey = m_pView->GetDocument()->GetObjectMapKey(Nuclear::PMap::MAP_OBJ_EFFECT, m_EffectName.GetBuffer(), NULL);
	if ((newKey != info.effectMapKey) && (!neweffect))
	{
		int oldSel = m_comboEffects.GetCurSel();
		int oldNowEffectSel = m_nCurSelectedEffect;
		m_comboEffects.DeleteString(selID);
		m_comboEffects.InsertString(selID, m_EffectName);
		m_comboEffects.SetItemData(selID, id);
		m_nCurSelectedEffect = oldSel;
		m_comboEffects.SetCurSel(oldSel);
		m_nCurSelectedEffect = oldNowEffectSel;
	}
	info.effectMapKey = newKey;
	m_EffectName.ReleaseBuffer();
	info.triggerTypeFlag = 0;
	info.triggerTypeFlag |= m_bSpriteTrigger?Nuclear::SubMap::TRIGGER_SPRITE:0;
	info.triggerTypeFlag |= m_bTimeTrigger?Nuclear::SubMap::TRIGGER_TIME:0;
	info.triggerTypeFlag |= m_bNonrandomAndSingle?Nuclear::SubMap::TRIGGER_NONRANDOM_AND_SINGLE:0;
	info.beginTime = m_nBeginTime;
	info.endTime = m_nEndTime;
	info.frequency = m_fFrequency;
	info.triDelta = m_nDelta;
	info.maxDelta = m_nMaxDelta;
	info.excursion = m_SpriteExcPoint;
	info.layer = m_Layer;
	info.actionsFlag = m_Flag;
	m_bModifyFlag = false;
}

void CTriggerEditorDlg::FillToCtrl()
{
	int id = static_cast<int>(m_comboEffects.GetItemData(m_comboEffects.GetCurSel()));
	if (id == -1)	
	{
		m_EffectName = L"";
		m_bSpriteTrigger = FALSE;
		m_bTimeTrigger = FALSE;
		m_bNonrandomAndSingle = FALSE;
		m_nBeginTime = 0;
		m_nEndTime = 0;
		m_fFrequency = 0.0f;
		m_nDelta = 0;
		m_nMaxDelta = 0;
		m_SpriteExcPoint.x = 0;
		m_SpriteExcPoint.y = 0;
		m_Layer = Nuclear::XPEL_UNDER_SPRITE;
		m_Flag = 0;
	} else {
		Nuclear::SubMap::TriggerInfo& info = m_TriggerInfoArr[id];
		m_EffectName = m_pView->GetDocument()->GetToolsMap().GetObjFileName(Nuclear::PMap::MAP_OBJ_EFFECT, info.effectMapKey).c_str();
		m_bSpriteTrigger = (info.triggerTypeFlag & Nuclear::SubMap::TRIGGER_SPRITE) > 0;
		m_bTimeTrigger = (info.triggerTypeFlag & Nuclear::SubMap::TRIGGER_TIME) > 0;
		m_bNonrandomAndSingle = (info.triggerTypeFlag & Nuclear::SubMap::TRIGGER_NONRANDOM_AND_SINGLE) > 0;
		m_nBeginTime = info.beginTime;
		m_nEndTime = info.endTime;
		m_fFrequency = info.frequency;
		m_nDelta = info.triDelta;
		m_nMaxDelta = info.maxDelta;
		m_SpriteExcPoint.x = info.excursion.x;
		m_SpriteExcPoint.y = info.excursion.y;
		m_Layer = info.layer;
		m_Flag = info.actionsFlag;
	}
	m_bModifyFlag = false;

	UpdateData(FALSE);
	ResetEnable();
}

void CTriggerEditorDlg::FillFromActionTypeCtrl()
{
	int count = m_listActions.GetItemCount();
	m_Flag = 0;
	for (int i=0;i<count;++i)
	{
		if (m_listActions.GetCheck(i))
		{
			m_Flag |= 1 << (m_listActions.GetItemData(i) - 1);
		}
	}
}

void CTriggerEditorDlg::FillToActionTypeCtrl()
{
	int count = m_listActions.GetItemCount();
	for (int i=0;i<count;++i)
	{
		m_listActions.SetCheck(i, m_Flag &  1 << ((m_listActions.GetItemData(i) - 1)));
	}
}

void CTriggerEditorDlg::ResetEnable()
{
	m_editBeginTime.EnableWindow(m_bTimeTrigger);
	m_editEndTime.EnableWindow(m_bTimeTrigger);
	m_listActions.EnableWindow(m_bSpriteTrigger);
	m_editSpriteExcX.EnableWindow(m_bSpriteTrigger);
	m_editSpriteExcY.EnableWindow(m_bSpriteTrigger);
}

void CTriggerEditorDlg::ResetList()
{
	TriggerInfoVet::iterator it, ie;
	it = m_TriggerInfoArr.begin();
	ie = m_TriggerInfoArr.end();
	m_comboEffects.Clear();
	CToolsMap &pmap = m_pView->GetDocument()->GetToolsMap();
	int i = 0, j = 0;
	for (;it!=ie;++it,++i)
	{
		if (it->triggerTypeFlag & Nuclear::SubMap::TRIGGER_DELETE)
			continue;		
		m_comboEffects.InsertString(j, pmap.GetObjFileName(Nuclear::PMap::MAP_OBJ_EFFECT, it->effectMapKey).c_str());
		m_comboEffects.SetItemData(j, i);
		++j;
	}
	m_comboEffects.InsertString(j, L"增加");
	m_comboEffects.SetItemData(j, -1);
}

void CTriggerEditorDlg::OnBnClickedCtrlTime()
{
	UpdateData(TRUE);
	ResetEnable();
	m_bModifyFlag = true;
}

void CTriggerEditorDlg::OnBnClickedButtonBrowse()
{
	WCHAR szExtFilter[256];
	swprintf(szExtFilter, L"特效文件(*%s;*.%s;*%s)|*%s;*.%s;*%s||", SFI_ExtName, FNExt_ParticleWS, Effect_ExtName, SFI_ExtName, FNExt_ParticleWS, Effect_ExtName);
	CFileDialog dlg(TRUE, NULL, NULL, OFN_FILEMUSTEXIST, szExtFilter);
	CString dir = theApp.GetResPath() + L"\\effect";
	dlg.m_ofn.lpstrInitialDir = dir.GetBuffer();
	if (dlg.DoModal() == IDOK)
	{
		std::wstring name;
		if (PFS::CEnv::NativeFileNameToPFSFileName(dlg.GetPathName().GetBuffer(), name))
		{
			m_EffectName = name.c_str();
			m_EffectName = m_EffectName.Mid(7).MakeLower();
			if (m_EffectName.Right(_tcslen(_T(".inf"))) == _T(".inf"))
			{
				m_EffectName = m_EffectName.Left(m_EffectName.GetLength() - 9);
			} else {
				m_EffectName = m_EffectName.Left(m_EffectName.GetLength() - 4);
			}
			m_bModifyFlag = true;
			UpdateData(FALSE);
		} else {
			AfxMessageBox(_T("请选择本编辑器所在文件夹下的文件"));
		}
	}
}

BEGIN_MESSAGE_MAP(CTriggerEditorDlg, CDialog)
	ON_BN_CLICKED(IDC_BUTTON_BROWSE, &CTriggerEditorDlg::OnBnClickedButtonBrowse)
	ON_BN_CLICKED(IDC_CTRL_TIME, &CTriggerEditorDlg::OnBnClickedCtrlTime)
	ON_BN_CLICKED(IDOK, &CTriggerEditorDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDC_BUTTON1, &CTriggerEditorDlg::OnBnClickedButton1)
	ON_CBN_SELCHANGE(IDC_COMBO1, &CTriggerEditorDlg::OnCbnSelchangeCombo1)
	ON_BN_CLICKED(IDC_CTRL_SPRITE, &CTriggerEditorDlg::OnBnClickedCtrlSprite)
	ON_BN_CLICKED(IDC_BUTTON2, &CTriggerEditorDlg::OnBnClickedButton2)
	ON_BN_CLICKED(IDC_BUTTON3, &CTriggerEditorDlg::OnBnClickedButton3)
	ON_EN_KILLFOCUS(IDC_BEGIN_TIME, &CTriggerEditorDlg::OnEnKillfocusBeginTime)
	ON_CBN_SELCHANGE(IDC_COMBO_LAYER, &CTriggerEditorDlg::OnCbnSelchangeComboLayer)
	ON_EN_KILLFOCUS(IDC_DELTA, &CTriggerEditorDlg::OnEnKillfocusDelta)
	ON_EN_KILLFOCUS(IDC_END_TIME, &CTriggerEditorDlg::OnEnKillfocusEndTime)
	ON_NOTIFY(NM_KILLFOCUS, IDC_LIST2, &CTriggerEditorDlg::OnNMKillfocusList2)
	ON_EN_KILLFOCUS(IDC_FREQUENCY, &CTriggerEditorDlg::OnEnKillfocusFrequency)
	ON_EN_KILLFOCUS(IDC_MAX_DELTA, &CTriggerEditorDlg::OnEnKillfocusMaxDelta)
	ON_EN_KILLFOCUS(IDC_EDIT1, &CTriggerEditorDlg::OnEnKillfocusEdit1)
	ON_EN_KILLFOCUS(IDC_EDIT_X, &CTriggerEditorDlg::OnEnKillfocusEditX)
	ON_EN_KILLFOCUS(IDC_EDIT_Y, &CTriggerEditorDlg::OnEnKillfocusEditY)
	ON_BN_CLICKED(IDC_CTRL_SINGLE, &CTriggerEditorDlg::OnBnClickedCtrlSingle)
END_MESSAGE_MAP()




void CTriggerEditorDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT1, m_Name);
	DDX_Text(pDX, IDC_EDIT_EFFECT_NAME, m_EffectName);
	DDX_Check(pDX, IDC_CTRL_SPRITE, m_bSpriteTrigger);
	DDX_Check(pDX, IDC_CTRL_TIME, m_bTimeTrigger);
	DDX_Check(pDX, IDC_CTRL_SINGLE, m_bNonrandomAndSingle);
	DDX_Control(pDX, IDC_BEGIN_TIME, m_editBeginTime);
	DDX_Control(pDX, IDC_END_TIME, m_editEndTime);
	DDX_Control(pDX, IDC_COMBO_LAYER, m_comboLayer);
	DDX_Text(pDX, IDC_BEGIN_TIME, m_nBeginTime);
	DDX_Text(pDX, IDC_END_TIME, m_nEndTime);
	DDX_Text(pDX, IDC_FREQUENCY, m_fFrequency);
	DDX_Text(pDX, IDC_DELTA, m_nDelta);
	DDX_Text(pDX, IDC_MAX_DELTA, m_nMaxDelta);
	DDX_Text(pDX, IDC_EDIT_X, m_SpriteExcPoint.x);
	DDX_Text(pDX, IDC_EDIT_Y, m_SpriteExcPoint.y);
	DDX_Control(pDX, IDC_EDIT_X, m_editSpriteExcX);
	DDX_Control(pDX, IDC_EDIT_Y, m_editSpriteExcY);
	DDX_Control(pDX, IDC_LIST2, m_listActions);
	DDX_Control(pDX, IDC_COMBO1, m_comboEffects);
	if (pDX->m_bSaveAndValidate)
	{
		FillFromActionTypeCtrl();
		m_Layer = static_cast<Nuclear::XPEFFECT_LAYER>(m_comboLayer.GetCurSel());
	} else {
		FillToActionTypeCtrl();
		m_comboLayer.SetCurSel(m_Layer);
	}

}


CTriggerEditorDlg::~CTriggerEditorDlg()
{
}

CTriggerEditorDlg::CTriggerEditorDlg(CMapEditorView* pView, CWnd* pParent /*=NULL*/)
: CDialog(CTriggerEditorDlg::IDD, pParent)
, m_bModifyFlag(false)
, m_EffectName(_T(""))
, m_bSpriteTrigger(FALSE)
, m_bTimeTrigger(FALSE)
, m_bNonrandomAndSingle(FALSE)
, m_nBeginTime(0)
, m_nEndTime(0)
, m_fFrequency(0.0f)
, m_nDelta(0)
, m_nMaxDelta(0)
, m_Layer(Nuclear::XPEL_UNDER_SPRITE)
, m_Flag(0)
, m_pView(pView)
, m_Name(L"")
{
	m_SpriteExcPoint.x = 0;
	m_SpriteExcPoint.y = 0;
}

