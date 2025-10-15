// TestPanel.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "TestPanel.h"
#include "PropView.h"
#include "SoundEditorDoc.h"
#include "..\engine\common\fileiomanager.h"
#include "..\engine\engine\enginebase.h"
#include "..\engine\sprite\spritemanager.h"
#include "..\engine\soundarea\stepsoundtype.h"
// CTestPanel dialog



IMPLEMENT_DYNAMIC(CTestPanel, CDialog)

CTestPanel::CTestPanel(CWnd* pParent /*=NULL*/)
	: CDialog(CTestPanel::IDD, pParent), m_bIsInited(false)
	, m_nTime(0)
	, m_bEnableEnv(TRUE)
	, m_bEnableBkg(TRUE)
	, m_nRideLayer(-1)
	, m_bActorMapDirty(false)
	, m_nOldModelSel(0)
{
}

CTestPanel::~CTestPanel()
{
}

void CTestPanel::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Check(pDX, IDC_CHECK_DRAW_ENV_AREA, m_drawEnvArea);
	DDX_Check(pDX, IDC_CHECK_DRAW_BKG_AREA, m_drawBkgArea);
	DDX_Check(pDX, IDC_CHECK_DRAW_BKG_VOL, m_drawBkgVolArea);
	DDX_Check(pDX, IDC_CHECK_ENV, m_bEnableEnv);
	DDX_Check(pDX, IDC_CHECK_BKG, m_bEnableBkg);
	DDX_Check(pDX, IDC_CHECK_DRAW_STEP, m_drawStep);
	DDX_Check(pDX, IDC_CHECK_DARK, m_testingEnableDark);
	DDX_Text(pDX, IDC_LAB_TIME, m_nTime);
	DDX_Control(pDX, IDC_SLIDER_CYCLE, m_silderCycle);
	DDX_Control(pDX, IDC_SLIDER_TIME, m_silderTime);
	DDX_Control(pDX, IDC_SLIDER_ENV, m_silderEnv);
	DDX_Control(pDX, IDC_SLIDER_BKG, m_silderBkg);
	DDX_Control(pDX, IDC_GROUP, m_group);
	DDX_Control(pDX, IDC_COMBO_MODEL, m_comboModel);
	DDX_Control(pDX, IDC_COMBO_ACT_TYPE, m_comboActorType);
	DDX_Control(pDX, IDC_BUTTON_SAVE, m_buttonSave);
	DDX_Control(pDX, IDC_COMBO_LAYER, m_comboLayer);
	DDX_Control(pDX, IDC_COMBO_COMPONENT, m_comboComponent);
	DDX_Control(pDX, IDC_COMBO_ACTION_TYPE, m_comboActionType);
	DDX_Control(pDX, IDC_COMBO_ACTION_NAME, m_comboActionName);
	DDX_Control(pDX, IDC_BUTTON_SAVE_AS_DEFAULT, m_buttonSetAsDefault);
	DDX_Control(pDX, IDC_COMBO_RIDE_TYPE, m_comboRide);
	DDX_Control(pDX, IDC_LIST_NOTIFY, m_listNotify);
	DDX_Control(pDX, IDC_COMBO_NOTIFY_ACTION_NAME, m_comboNActName);
	DDX_Control(pDX, IDC_COMBO_NOTIFY_ACTION_FRAME, m_comboNActFrame);
}


BEGIN_MESSAGE_MAP(CTestPanel, CDialog)
	ON_BN_CLICKED(IDC_CHECK_DRAW_ENV_AREA, &CTestPanel::OnBnClickedCheckDrawEnvArea)
	ON_BN_CLICKED(IDC_CHECK_DRAW_BKG_AREA, &CTestPanel::OnBnClickedCheckDrawBkgArea)
	ON_BN_CLICKED(IDC_CHECK_DARK, &CTestPanel::OnBnClickedCheckDark)
	ON_WM_HSCROLL()
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_BN_CLICKED(IDC_CHECK_BKG, &CTestPanel::OnBnClickedCheckBkg)
	ON_BN_CLICKED(IDC_CHECK_ENV, &CTestPanel::OnBnClickedCheckEnv)
	ON_BN_CLICKED(IDC_BUTTON_SAVE_AS_DEFAULT, &CTestPanel::OnBnClickedButtonSaveAsDefault)
	ON_CBN_SELCHANGE(IDC_COMBO_MODEL, &CTestPanel::OnCbnSelchangeComboModel)
	ON_BN_CLICKED(IDC_BUTTON_SAVE, &CTestPanel::OnBnClickedButtonSave)
	ON_CBN_SELCHANGE(IDC_COMBO_LAYER, &CTestPanel::OnCbnSelchangeComboLayer)
	ON_CBN_SELCHANGE(IDC_COMBO_COMPONENT, &CTestPanel::OnCbnSelchangeComboComponent)
	ON_CBN_SELCHANGE(IDC_COMBO_ACTION_TYPE, &CTestPanel::OnCbnSelchangeComboActionType)
	ON_CBN_SELCHANGE(IDC_COMBO_ACTION_NAME, &CTestPanel::OnCbnSelchangeComboActionName)
	ON_CBN_SELCHANGE(IDC_COMBO_ACT_TYPE, &CTestPanel::OnCbnSelchangeComboActType)
	ON_CBN_SELCHANGE(IDC_COMBO_RIDE_TYPE, &CTestPanel::OnCbnSelchangeComboRideType)
	ON_NOTIFY(LVN_ITEMCHANGED, IDC_LIST_NOTIFY, &CTestPanel::OnLvnItemchangedListNotify)
	ON_NOTIFY(LVN_ENDSCROLL, IDC_LIST_NOTIFY, &CTestPanel::OnLvnEndScrollListNotify)
	ON_CBN_SELCHANGE(IDC_COMBO_NOTIFY_ACTION_NAME, &CTestPanel::OnCbnSelchangeComboNotifyActionName)
	ON_CBN_SELCHANGE(IDC_COMBO_NOTIFY_ACTION_FRAME, &CTestPanel::OnCbnSelchangeComboNotifyActionFrame)
	ON_BN_CLICKED(IDC_CHECK_DRAW_STEP, &CTestPanel::OnBnClickedCheckDrawStep)
	ON_BN_CLICKED(IDC_CHECK_DRAW_BKG_VOL, &CTestPanel::OnBnClickedCheckDrawBkgVol)
END_MESSAGE_MAP()

void CTestPanel::ResetCont()
{
	if (m_bIsInited)
	{
		CSoundEditorDoc* pDoc = m_pView->GetDocument();
		if ((pDoc->GetUpdateSource() != US_PANEAL) && (pDoc->m_nUpdateViewSign & UVS_TEST_SET_GAMETIME))
		{
			m_nTime = static_cast<int>(m_pView->GetDocument()->GetGameTime() * SE_EDITOR_TIME_A_DAY);
			m_silderTime.SetPos(m_nTime);
			UpdateData(FALSE);
		}
		if (pDoc->m_nUpdateViewSign & UVS_TAB)
		{
			ResetActorType();
		}
	}
}

void CTestPanel::ResetSize()
{
	if (m_bIsInited)
	{
		CRect rect, rct;
		GetClientRect(&rect);
		m_silderCycle.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.right = rect.right;
		m_silderCycle.MoveWindow(&rct);
		m_silderTime.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.right = rect.right;
		m_silderTime.MoveWindow(&rct);
		m_silderEnv.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.right = rect.right;
		m_silderEnv.MoveWindow(&rct);
		m_silderBkg.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.right = rect.right;
		m_silderBkg.MoveWindow(&rct);
		m_group.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.left = rect.left + 5;
		rct.right = rect.right - 5;
		m_group.MoveWindow(&rct);
		m_comboModel.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.right = rect.right - 10;
		m_comboModel.MoveWindow(&rct);
		m_listNotify.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.right = rect.right - 10;
		m_listNotify.MoveWindow(&rct);
		m_listNotify.SetColumnWidth(0, rct.Width() - 80);
		m_listNotify.SetColumnWidth(1, 50);
		m_comboRide.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.right = rect.right - 10;
		m_comboRide.MoveWindow(&rct);
		m_comboActorType.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.right = rect.right - 55;
		m_comboActorType.MoveWindow(&rct);
		m_buttonSave.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.left = rect.right - 50;
		rct.right = rect.right - 10;
		m_buttonSave.MoveWindow(&rct);
		m_comboLayer.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.right = rect.right - 115;
		m_comboLayer.MoveWindow(&rct);
		rct.left = rct.right + 5;
		rct.right = rect.right - 10;
		m_comboComponent.MoveWindow(&rct);
		m_comboActionType.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.right = rect.right - 115;
		m_comboActionType.MoveWindow(&rct);
		rct.left = rct.right + 5;
		rct.right = rect.right - 10;
		m_comboActionName.MoveWindow(&rct);
		m_buttonSetAsDefault.GetWindowRect(&rct);
		ScreenToClient(&rct);
		rct.right = rect.right - 10;
		rct.left = rct.right - 100;
		m_buttonSetAsDefault.MoveWindow(&rct);
		ResetNotifyComboPosition();
	}
}

// CTestPanel message handlers
BOOL CTestPanel::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_pView = dynamic_cast<CPropView*>(GetParent());
	CSoundEditorDoc *pDoc = m_pView->GetDocument();
	m_drawEnvArea = pDoc->m_bTestingDrawEnvArea;
	m_drawBkgArea = pDoc->m_bTestingDrawBkgArea;
	m_drawBkgVolArea = pDoc->m_bTestingDrawBkgVolArea;
	m_drawStep = pDoc->m_bTestingDrawStep;	
	m_testingEnableDark = pDoc->m_bTestingEnableDark;
	m_silderCycle.SetRange(1, 240);
	m_silderCycle.SetTicFreq(20);
	m_silderCycle.SetPos(12);
	m_silderTime.SetRange(0, SE_EDITOR_TIME_A_DAY);
	m_silderTime.SetTic(GameTime2EditorTime(Nuclear::XP_DAY_START_TIME));
	m_silderTime.SetTic(GameTime2EditorTime(Nuclear::XP_NIGHT_START_TIME));
	m_silderTime.SetPos(0);
	m_silderEnv.SetRange(0, 100);
	m_silderEnv.SetTicFreq(10);
	m_silderEnv.SetPos(100);
	m_silderBkg.SetRange(0, 100);
	m_silderBkg.SetPos(100);
	m_silderBkg.SetTicFreq(10);

	//模型Combo
	m_comboModel.ResetContent();
	m_modelMap.clear();
	m_comboModel.AddString(L"无");
	m_nOldModelSel = 0;
	const std::wstring &curmodel = pDoc->GetModelName();
	m_modelMap[L""] = 0;
	std::vector<std::wstring> v;
	if (!pDoc->GetEngineBase())
		return FALSE;
	pDoc->GetEngineBase()->GetSpriteManager()->DumpModelNames(v);
	int i;
	for(i=0; i<(int)v.size();++i)
	{
		m_comboModel.AddString(v[i].c_str());
		m_modelMap[v[i]] = i + 1;
		if (curmodel == v[i])
			m_nOldModelSel = i + 1;
	}
	m_comboModel.SetCurSel(m_nOldModelSel);
	m_comboActionType.ResetContent();
	m_comboActionType.AddString(L"站立");
	m_comboActionType.AddString(L"左脚跑");
	m_comboActionType.AddString(L"右脚跑");
	m_comboActionType.SetCurSel(0);

	m_listNotify.InsertColumn(0, L"动作");
	m_listNotify.InsertColumn(1, L"帧");
	m_listNotify.SetExtendedStyle(LVS_EX_FULLROWSELECT);
	LONG style = GetWindowLong(m_listNotify.GetSafeHwnd(), GWL_STYLE);
	style |= WS_CLIPSIBLINGS;
	SetWindowLong(m_listNotify.GetSafeHwnd(), GWL_STYLE, style);
	m_listNotify.ShowWindow(SW_SHOW);
	m_listNotify.BringWindowToTop();

	
	ResetRide();
	ResetEquip();
	ResetActionCombo();
	ResetActorType();
	ResetNotify();
	ResetNotifyCombo();


	UpdateData(FALSE);
	m_bIsInited = true;
	ResetCont();
	ResetSize();
	return FALSE;
}

void CTestPanel::ResetEquip()
{
	m_comboLayer.ResetContent();
	if (m_comboModel.GetCurSel() != 0)
	{
		Nuclear::SpriteManager* pSprMan = m_pView->GetDocument()->GetEngineBase()->GetSpriteManager();
		XPASSERT(pSprMan != NULL);
		CString strName;
		m_comboModel.GetWindowText(strName);
		std::wstring modelName = strName.GetBuffer();
		strName.ReleaseBuffer();
		int layerCount = pSprMan->GetLayerCount(modelName);
		for (int i=0;i<layerCount;++i)
		{
			m_comboLayer.AddString(pSprMan->GetLayerDes(modelName, i).c_str());
		}
		m_comboLayer.SetCurSel(0);
	}
	ResetComponent();

}

void CTestPanel::ResetComponent()
{
	m_comboComponent.ResetContent();
	if (m_comboLayer.GetCount() == 0)
		return;//没有任何模型
	m_comboComponent.AddString(L"");
	std::wstring nowComp = m_pView->GetDocument()->GetComponent(m_comboLayer.GetCurSel());
	Nuclear::SpriteManager* pSprMan = m_pView->GetDocument()->GetEngineBase()->GetSpriteManager();
	XPASSERT(pSprMan != NULL);
	CString strName;
	m_comboModel.GetWindowText(strName);
	std::wstring modelName = strName.GetBuffer();
	strName.ReleaseBuffer();
	std::vector<std::wstring> compos;
	pSprMan->DumpComponent(modelName, m_comboLayer.GetCurSel(), compos);
	int n = (int)compos.size();
	m_comboComponent.SetCurSel(0);
	for(int i=0; i<n; ++i)
	{
		m_comboComponent.AddString(compos[i].c_str());
		if (compos[i] == nowComp)
			m_comboComponent.SetCurSel(i+1);
	}
}

void CTestPanel::SetAction()
{
	int curSel = 0;
	std::map<std::wstring, int>::iterator iter;
	switch (m_comboActionType.GetCurSel())
	{
	case 0:	//站立
		iter = m_actionMap.find(m_pView->GetDocument()->GetStandAct());
		break;
	case 1:	//左脚
		iter = m_actionMap.find(m_pView->GetDocument()->GetRunLeftAct());
		break;
	case 2:	//右脚
		iter = m_actionMap.find(m_pView->GetDocument()->GetRunRightAct());
		break;
	default:
		iter = m_actionMap.end();
		break;
	}
	if (iter != m_actionMap.end())
	{
		curSel = iter->second;
	}
	m_comboActionName.SetCurSel(curSel);
}

void CTestPanel::ResetActionCombo()
{
	m_comboActionName.ResetContent();
	m_actionMap.clear();
	if (m_comboModel.GetCurSel() != 0)
	{
		Nuclear::SpriteManager* pSprMan = m_pView->GetDocument()->GetEngineBase()->GetSpriteManager();
		XPASSERT(pSprMan != NULL);
		CString strName;
		m_comboModel.GetWindowText(strName);
		std::wstring modelName = strName.GetBuffer();
		strName.ReleaseBuffer();
		std::vector<std::wstring> actlist;
		pSprMan->DumpAction(modelName, actlist);
		int n = (int)actlist.size();
		for(int i=0; i<n; ++i)
		{
			m_comboActionName.AddString(actlist[i].c_str());
			m_actionMap[actlist[i]] = i;
		}
		
		SetAction();
	}
}


void CTestPanel::OnBnClickedCheckDrawEnvArea()
{
	UpdateData();
	m_pView->GetDocument()->m_bTestingDrawEnvArea = m_drawEnvArea;
}

void CTestPanel::OnBnClickedCheckDrawBkgArea()
{
	UpdateData();
	m_pView->GetDocument()->m_bTestingDrawBkgArea = m_drawBkgArea;
}

void CTestPanel::OnBnClickedCheckDark()
{
	UpdateData();
	CSoundEditorDoc *pDoc = m_pView->GetDocument();
	pDoc->m_bTestingEnableDark = m_testingEnableDark?true:false;
	pDoc->m_nUpdateViewSign |= UVS_TEST_SET_DARK;
	pDoc->UpdateAllViews(m_pView);
	pDoc->m_nUpdateViewSign &= ~UVS_TEST_SET_DARK;
}

void CTestPanel::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	switch (pScrollBar->GetDlgCtrlID())
	{
	case IDC_SLIDER_CYCLE:
		m_pView->GetDocument()->SetGameTimeCycle(m_silderCycle.GetPos() * 60000);
		break;
	case IDC_SLIDER_TIME:
		m_nTime = m_silderTime.GetPos();
		m_pView->GetDocument()->SetGameTime(static_cast<float>(m_nTime) / SE_EDITOR_TIME_A_DAY, US_PANEAL);
		UpdateData(FALSE);
		break;
	case IDC_SLIDER_ENV:
		m_pView->GetDocument()->SetEnvVolume(m_silderEnv.GetPos(), US_PANEAL);
		break;
	case IDC_SLIDER_BKG:
		m_pView->GetDocument()->SetBkgVolume(m_silderBkg.GetPos(), US_PANEAL);
		break;
	default:
		break;
	}
	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CTestPanel::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

void CTestPanel::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}

void CTestPanel::OnBnClickedCheckBkg()
{
	UpdateData();
	m_pView->GetDocument()->EnableBkg(m_bEnableBkg?true:false, US_PANEAL);
}

void CTestPanel::OnBnClickedCheckEnv()
{
	UpdateData();
	m_pView->GetDocument()->EnableEnv(m_bEnableEnv?true:false, US_PANEAL);
}

void CTestPanel::OnBnClickedButtonSaveAsDefault()
{
	m_pView->GetDocument()->SaveSpriteCfg();
}

void CTestPanel::OnCbnSelchangeComboModel()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	if (m_bActorMapDirty)
	{
		int result = MessageBox(L"此模型的脚步类型已被更改，是否保存？", NULL, MB_YESNOCANCEL);
		switch (result)
		{
		case IDYES:
			{
				std::wstring filename = theApp.GetExePath();
				filename += L"\\res\\sound\\";
				pDoc->GetEngineBase()->GetStepSoundType()->SaveModelMap(filename);
			}
			break;
		case IDNO:
			pDoc->GetEngineBase()->GetStepSoundType()->LoadModelMap(pDoc->GetEngineBase()->GetFileIOManager());
			break;
		case IDCANCEL:
			m_comboModel.SetCurSel(m_nOldModelSel);
			return;
			break;
		}
		m_bActorMapDirty = false;
	}
	if (m_comboModel.GetCurSel() != 0)
	{
		CString strName;
		m_comboModel.GetWindowText(strName);
		std::wstring modelName = strName.GetBuffer();
		strName.ReleaseBuffer();
		pDoc->SetModel(modelName);
	} else {
		pDoc->SetModel(L"");
	}
	ResetRide();
	ResetEquip();
	ResetActionCombo();
	ResetNotify();
	m_nOldModelSel = m_comboModel.GetCurSel();
}

void CTestPanel::ResetRide()
{
	m_comboRide.ResetContent();
	m_comboRide.AddString(L"");
	m_comboRide.SetCurSel(0);
	m_nRideLayer = -1;
	if (m_comboModel.GetCurSel() != 0)
	{
		Nuclear::SpriteManager* pSprMan = m_pView->GetDocument()->GetEngineBase()->GetSpriteManager();
		XPASSERT(pSprMan != NULL);
		CString strName;
		m_comboModel.GetWindowText(strName);
		std::wstring modelName = strName.GetBuffer();
		strName.ReleaseBuffer();
		int layerCount = pSprMan->GetLayerCount(modelName);
		for (int i=0;i<layerCount;++i)
		{
			if (pSprMan->GetLayerRideEnable(modelName, i))
			{
				m_nRideLayer = i;
				break;
			}
		}

		if (m_nRideLayer >= 0)
		{
			std::vector<std::wstring> compos;
			pSprMan->DumpComponent(modelName, m_nRideLayer, compos);
			const std::wstring &ridename = m_pView->GetDocument()->GetRideName();
			int n = (int)compos.size();
			for(int i=0; i<n; ++i)
			{
				m_comboRide.AddString(compos[i].c_str());
				if (compos[i] == ridename)
					m_comboRide.SetCurSel(i+1);
			}
		}
	}
	SetActorType();
}

void CTestPanel::OnBnClickedButtonSave()
{
	std::wstring filename = theApp.GetExePath();
	filename += L"\\res\\sound\\";
	m_pView->GetDocument()->GetEngineBase()->GetStepSoundType()->SaveModelMap(filename);
	m_bActorMapDirty = false;
}

void CTestPanel::OnCbnSelchangeComboLayer()
{
	ResetComponent();
}

void CTestPanel::OnCbnSelchangeComboComponent()
{
	if (m_comboLayer.GetCount() == 0)
		return;//没有任何模型
	CString strName;
	m_comboComponent.GetWindowText(strName);
	std::wstring compo = strName.GetBuffer();
	strName.ReleaseBuffer();
	m_pView->GetDocument()->SetComponent(m_comboLayer.GetCurSel(), compo);
	
}

void CTestPanel::OnCbnSelchangeComboActionType()
{
	SetAction();
}

void CTestPanel::OnCbnSelchangeComboActionName()
{
	CString strName;
	m_comboActionName.GetWindowText(strName);
	std::wstring actName = strName.GetBuffer();
	strName.ReleaseBuffer();
	switch (m_comboActionType.GetCurSel())
	{
	case 0:	//站立
		m_pView->GetDocument()->SetStandAct(actName);
		break;
	case 1:	//左脚
		m_pView->GetDocument()->SetRunLeftAct(actName);
		break;
	case 2:	//右脚
		m_pView->GetDocument()->SetRunRightAct(actName);
		break;
	default:
		break;
	}
}

void CTestPanel::OnCbnSelchangeComboActType()
{
	if (m_comboModel.GetCurSel() == 0)
		return;
	CString strName;
	m_comboModel.GetWindowText(strName);
	std::wstring modelname = strName.GetBuffer();
	strName.ReleaseBuffer();
	m_comboRide.GetWindowText(strName);
	std::wstring ride = strName.GetBuffer();
	strName.ReleaseBuffer();
	int type = m_comboActorType.GetCurSel();
	if (ride == L"")
	{
		if (type == 0)
		{
			if (MessageBox(L"模型如果没有默认类型，\n则意味着这个模型没有脚步声音，是否继续？", NULL, MB_YESNO) == IDYES)
			{
				m_pView->GetDocument()->GetEngineBase()->GetStepSoundType()->SetModelDefaultType(modelname, L"");
				m_bActorMapDirty = true;
			} else {
				SetActorType();
			}
		} else {
			m_pView->GetDocument()->GetEngineBase()->GetStepSoundType()->SetModelDefaultType(modelname, m_actorTypeMap[type]);
			m_bActorMapDirty = true;
		}
		
	} else {
		m_pView->GetDocument()->GetEngineBase()->GetStepSoundType()->SetModelRideType(modelname, ride, m_actorTypeMap[type]);
		m_bActorMapDirty = true;
	}
}

void CTestPanel::OnCbnSelchangeComboRideType()
{
	if (m_comboLayer.GetCount() == 0)
		return;//没有任何模型
	CString strName;
	m_comboRide.GetWindowText(strName);
	std::wstring ride = strName.GetBuffer();
	strName.ReleaseBuffer();
	m_pView->GetDocument()->SetRidename(ride);
	SetActorType();
}

void CTestPanel::SetActorType()
{
	if (m_comboModel.GetCurSel() == 0)
		return;
	CString strName;
	m_comboModel.GetWindowText(strName);
	std::wstring modelname = strName.GetBuffer();
	strName.ReleaseBuffer();
	const Nuclear::XPSSModelMap &modelmap = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType()->GetModelMap();
	Nuclear::XPSSModelMap::const_iterator mit = modelmap.find(modelname);
	if (mit == modelmap.end())
	{
		m_comboActorType.SetCurSel(0);
		return;
	}
	m_comboRide.GetWindowText(strName);
	std::wstring ride = strName.GetBuffer();
	strName.ReleaseBuffer();
	if (ride == L"")
	{
		m_comboActorType.SetCurSel(m_actorMap[mit->second.defaultType]);
	} else {
		Nuclear::XPRideType::const_iterator rIt = mit->second.ridetype.find(ride);
		if (rIt == mit->second.ridetype.end())
		{
			m_comboActorType.SetCurSel(0);
		} else {
			m_comboActorType.SetCurSel(m_actorMap[rIt->second]);
		}
	}
}

void CTestPanel::ResetActorType()
{
	m_actorMap.clear();
	m_actorTypeMap.clear();
	m_comboActorType.ResetContent();
	const Nuclear::XPSSATypeMap &actmap = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType()->GetActorMap();
	Nuclear::XPSSATypeMap::const_iterator it = actmap.begin(), ie = actmap.end();
	m_comboActorType.AddString(L"无");
	m_actorTypeMap[0] = L"";
	int i = 1;
	for (;it!=ie;++it,++i)
	{
		m_comboActorType.AddString(it->second.c_str());
		m_actorMap[it->first] = i;
		m_actorTypeMap[i] = it->first;
	}
	SetActorType();
}

void CTestPanel::OnLvnItemchangedListNotify(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMLISTVIEW pNMLV = reinterpret_cast<LPNMLISTVIEW>(pNMHDR);
	if (pNMLV->uChanged == LVIF_STATE)
	{
		if ((pNMLV->uNewState ^ pNMLV->uOldState) & LVIS_SELECTED)//取消选择或者选择
		{
			ResetNotifyCombo();
		}
	}
	*pResult = 0;
}

void CTestPanel::OnLvnEndScrollListNotify(NMHDR *pNMHDR, LRESULT *pResult)
{
	ResetNotifyComboPosition();
	*pResult = 0;
}

void CTestPanel::ResetNotify()
{
	m_listNotify.DeleteAllItems();
	if (m_comboModel.GetCurSel() == 0)
		return;
	CString strName;
	m_comboModel.GetWindowText(strName);
	std::wstring modelname = strName.GetBuffer();
	strName.ReleaseBuffer();
	const Nuclear::XPSSModelMap &modelmap = m_pView->GetDocument()->GetEngineBase()->GetStepSoundType()->GetModelMap();
	Nuclear::XPSSModelMap::const_iterator mit = modelmap.find(modelname);
	int i = 0;
	if (mit != modelmap.end())
	{
		Nuclear::XPStepNotifies::const_iterator nIt = mit->second.notifies.begin(), nIe = mit->second.notifies.end();
		for (;nIt!=nIe;++nIt, ++i)
		{
			m_listNotify.InsertItem(i, nIt->actname.c_str());
			CString frame;
			frame.Format(L"%d", nIt->frame);
			m_listNotify.SetItemText(i, 1, frame.GetBuffer());
			frame.ReleaseBuffer();
			m_listNotify.SetItemData(i, 1 + m_actionMap[nIt->actname]);
		}
	}
	m_listNotify.InsertItem(i, L"无");
	m_listNotify.SetItemData(i, 0);
	m_listNotify.SetItemText(i, 1, L"");
	m_comboNActName.ResetContent();
	Nuclear::SpriteManager* pSprMan = m_pView->GetDocument()->GetEngineBase()->GetSpriteManager();
	XPASSERT(pSprMan != NULL);
	std::vector<std::wstring> actlist;
	pSprMan->DumpAction(modelname, actlist);
	int n = (int)actlist.size();
	m_comboNActName.AddString(L"无");
	for(int i=0; i<n; ++i)
	{
		m_comboNActName.AddString(actlist[i].c_str());
	}

}

void CTestPanel::ResetNotifyCombo()
{
	if (m_listNotify.GetSelectedCount() == 0)
	{
		m_nNotifyCurSel = -1;
		ResetNotifyComboPosition();
	} else {
		m_nNotifyCurSel = -1;
		POSITION pos = m_listNotify.GetFirstSelectedItemPosition();
		if (pos != NULL)
		{
			m_nNotifyCurSel = m_listNotify.GetNextSelectedItem(pos);
		}
		m_comboNActName.SetCurSel((int)m_listNotify.GetItemData(m_nNotifyCurSel));
		ResetNotifyFrameCombo();
		ResetNotifyComboPosition();
	}
}

void CTestPanel::ResetNotifyFrameCombo()
{
	int oldSel = m_comboNActFrame.GetCurSel();
	m_comboNActFrame.ResetContent();
	if (m_comboModel.GetCurSel() == 0)
		return;
	if (m_comboNActName.GetCurSel() == 0)
		return;
	if (oldSel < 0)
		oldSel = 0;
	CString strName;
	m_comboModel.GetWindowText(strName);
	std::wstring modelname = strName.GetBuffer();
	strName.ReleaseBuffer();
	m_comboNActName.GetWindowText(strName);
	std::wstring action_name = strName.GetBuffer();
	strName.ReleaseBuffer();
	Nuclear::SpriteManager* pSprMan = m_pView->GetDocument()->GetEngineBase()->GetSpriteManager();
	XPASSERT(pSprMan != NULL);
	std::wstring actionref_name = pSprMan->GetActionRef(modelname, action_name);
	if( actionref_name == L"" ) return;
	const Nuclear::Action *act = pSprMan->GetAction(modelname, actionref_name);
	int count = act->GetFrameCount();
	for (int i=0;i<count;++i)
	{
		strName.Format(L"%d", i);
		m_comboNActFrame.InsertString(i, strName.GetBuffer());
		strName.ReleaseBuffer();
	}
	oldSel = oldSel<count?oldSel:0;
	m_comboNActFrame.SetCurSel(oldSel);
}

void CTestPanel::ResetNotifyComboPosition()
{
	
	if (m_listNotify.GetSelectedCount() == 0)
	{
		m_comboNActFrame.ShowWindow(SW_HIDE);
		m_comboNActName.ShowWindow(SW_HIDE);
	} else {
		CRect rect, listRect, rct;
		m_listNotify.GetWindowRect(&listRect);
		ScreenToClient(&listRect);
		m_listNotify.GetItemRect(m_nNotifyCurSel, &rect, LVIR_BOUNDS);
		int w0 = m_listNotify.GetColumnWidth(0);
		int w1 = m_listNotify.GetColumnWidth(1);
		rct.left = listRect.left + 2;
		rct.top = listRect.top + rect.top;
		rct.bottom = listRect.top + rect.bottom;
		if ((rct.top < listRect.top + 10) || (rct.bottom > listRect.bottom))
		{
			m_comboNActFrame.ShowWindow(SW_HIDE);
			m_comboNActName.ShowWindow(SW_HIDE);
			return;
		}
		rct.right = rct.left + w0 - 2;
		m_comboNActName.MoveWindow(&rct);
		rct.left = rct.right + 4;
		rct.right = rct.left + w1 - 2;
		m_comboNActFrame.MoveWindow(&rct);
		m_comboNActFrame.ShowWindow(SW_SHOW);
		m_comboNActName.ShowWindow(SW_SHOW);
		m_comboNActFrame.BringWindowToTop();
		m_comboNActName.BringWindowToTop();
		Invalidate();
	}
}

void CTestPanel::OnCbnSelchangeComboNotifyActionName()
{
	CString strName;
	m_comboModel.GetWindowText(strName);
	std::wstring modelname = strName.GetBuffer();
	strName.ReleaseBuffer();
	int curSel = m_comboNActName.GetCurSel();
	if (curSel == 0)
	{
		//删除
		m_pView->GetDocument()->GetEngineBase()->GetStepSoundType()->RemoveModelStepNotify(modelname, m_nNotifyCurSel);
		m_listNotify.DeleteItem(m_nNotifyCurSel);
		ResetNotifyCombo();
	} else {
		//增加或者修改
		//原来的值
		ResetNotifyFrameCombo();
		int oldAction = (int)m_listNotify.GetItemData(m_nNotifyCurSel);
		Nuclear::StepNotifieV v;
		m_comboNActName.GetWindowText(strName);
		v.actname = strName.GetBuffer();
		strName.ReleaseBuffer();
		v.frame = m_comboNActFrame.GetCurSel();
		if (oldAction == 0)
		{
			//增加
			m_pView->GetDocument()->GetEngineBase()->GetStepSoundType()->InsertModelStepNotify(modelname, v);
			int c = m_listNotify.GetItemCount();
			m_listNotify.InsertItem(c, L"无");
			m_listNotify.SetItemData(c, 0);
			m_listNotify.SetItemText(c, 1, L"");
		} else {
			//修改
			m_pView->GetDocument()->GetEngineBase()->GetStepSoundType()->ModifyModelStepNotify(modelname, m_nNotifyCurSel, v);
		}
		m_listNotify.SetItemText(m_nNotifyCurSel, 0, v.actname.c_str());
		m_listNotify.SetItemData(m_nNotifyCurSel, m_actionMap[v.actname] + 1);
		strName.Format(L"%d", v.frame);
		m_listNotify.SetItemText(m_nNotifyCurSel, 1, strName.GetBuffer());
		strName.ReleaseBuffer();
	}
}

void CTestPanel::OnCbnSelchangeComboNotifyActionFrame()
{
	CString strName;
	m_comboModel.GetWindowText(strName);
	std::wstring modelname = strName.GetBuffer();
	strName.ReleaseBuffer();
	Nuclear::StepNotifieV v;
	m_comboNActName.GetWindowText(strName);
	v.actname = strName.GetBuffer();
	strName.ReleaseBuffer();
	v.frame = m_comboNActFrame.GetCurSel();
	m_pView->GetDocument()->GetEngineBase()->GetStepSoundType()->ModifyModelStepNotify(modelname, m_nNotifyCurSel, v);
	strName.Format(L"%d", v.frame);
	m_listNotify.SetItemText(m_nNotifyCurSel, 1, strName.GetBuffer());
	strName.ReleaseBuffer();
}

void CTestPanel::OnBnClickedCheckDrawStep()
{
	UpdateData();
	m_pView->GetDocument()->m_bTestingDrawStep = m_drawStep;
}

void CTestPanel::OnBnClickedCheckDrawBkgVol()
{
	UpdateData();
	m_pView->GetDocument()->m_bTestingDrawBkgVolArea = m_drawBkgVolArea;
}
