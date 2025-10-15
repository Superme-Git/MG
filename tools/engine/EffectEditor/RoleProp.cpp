

#include "stdafx.h"
#include "EffectEditor.h"
#include "PropertyView.h"
#include "EffectEditorDoc.h"
#include "RoleProp.h"
#include "DlgPropDoubleCurve.h"
#include "DlgPropSingleCurve.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CRoleProp, CDialog)

void CRoleProp::OnBnClickedButtonColor()
{
	CColorDialog colorDlg;
	if (colorDlg.DoModal() == IDOK)
	{
		CEffectEditorDoc *pDoc = m_pView->GetDocument();
		COLORREF colorRef = colorDlg.GetColor();
		Nuclear::XPCOLOR color(255, GetRValue(colorRef), GetGValue(colorRef), GetBValue(colorRef));
		pDoc->SetComponent(m_layer.GetCurSel(), pDoc->GetComponent(m_layer.GetCurSel()), color);

	}
}
void CRoleProp::OnCbnSelendokComboDivLayer()
{
	if (m_model.GetCurSel() != 0)
	{
		m_pView->GetDocument()->SetFrameDivideLayer(m_comboDivLayer.GetCurSel());
	}
}
void CRoleProp::OnBnClickedRotationButton()
{
	if (m_model.GetCurSel() != 0)
	{
		CDlgPropSingleCurve::ConstructorParam param;
		param.fYdiff = 400.0f * 2;
		param.kt = Nuclear::AbstractEffectClip::KT_ROTATION;
		param.pDoc = m_pView->GetDocument();
		param.title = L"旋转曲线";
		param.fMaxMaxY = 400.0f;
		param.fMinMinY = -400.0f;
		CDlgPropSingleCurve dlg(param);
		dlg.DoModal();
	}
}

void CRoleProp::OnBnClickedPosButton()
{
	if (m_model.GetCurSel() != 0)
	{
		CDlgPropDoubleCurve::ConstructorParam param;
		param.pDoc = m_pView->GetDocument();
		param.fYdiff = 200.0f;
		param.kt = Nuclear::AbstractEffectClip::KT_POS;
		param.title = L"位置曲线";
		CDlgPropDoubleCurve dlg(param);
		dlg.DoModal();
	}
}

void CRoleProp::SetAction(const std::wstring &actionName)
{
	CString strName;
	m_pView->GetDocument()->SetActionByName(actionName);
	strName.Format(L"%d", m_pView->GetDocument()->GetFrameCount());
	m_frameCount.SetWindowText(strName);
	strName.Format(L"%d", m_pView->GetDocument()->GetSpriteTotalTime());
	m_totalTime.SetWindowText(strName);
}

void CRoleProp::OnCbnSelendokComboAction()
{
	CString strName;
	m_action.GetWindowText(strName);
	std::wstring actionname = strName.GetBuffer();
	strName.ReleaseBuffer();
	SetAction(actionname);
}

void CRoleProp::OnCbnSelendokComboComponent()
{
	CString strName;
	m_component.GetWindowText(strName);
	std::wstring eqipname = strName.GetBuffer();
	strName.ReleaseBuffer();
	m_pView->GetDocument()->SetComponent(m_layer.GetCurSel(), eqipname);
}

void CRoleProp::OnCbnSelendokComboLayer()
{
	ResetComponent();
}

void CRoleProp::OnBnClickedButtonLayer()
{
	if (m_model.GetCurSel() != 0)
	{
		CDlgPropSingleCurve::ConstructorParam param;
		param.fYdiff = 11.0f;
		param.kt = Nuclear::AbstractEffectClip::KT_DIVIDE;
		param.pDoc = m_pView->GetDocument();
		param.title = L"层次曲线";
		param.fMaxMaxY = static_cast<float>(Nuclear::GEffect::MAX_CLIP_LAYER);
		param.fMinMinY = 0.0f;
		CDlgPropSingleCurve dlg(param);
		dlg.DoModal();
	}
}

void CRoleProp::OnRender()
{
	CEffectEditorDoc *pDoc = m_pView->GetDocument();
	XPASSERT(pDoc->GetEffectFileType() == EFT_SEBIND);
	m_comboDivLayer.EnableWindow(pDoc->GetPlayState() == Nuclear::XPES_PAUSE);
	Nuclear::GEffect* pEffect = dynamic_cast<Nuclear::GEffect*>(pDoc->GetBindEffect());
	if (pEffect == NULL)
	{
		m_comboDivLayer.EnableWindow(false);
		m_comboDivLayer.SetCurSel(0);
	} else {
		int layer = pEffect->GetDivideLayer();
		if (!m_comboDivLayer.GetDroppedState())
		{
			m_comboDivLayer.SetCurSel(layer);
		}
	}	
}

void CRoleProp::OnCbnSelendokComboModel()
{
	if (m_model.GetCurSel() != 0)
	{
		CString strName;
		m_model.GetWindowText(strName);
		std::wstring modelName = strName.GetBuffer();
		strName.ReleaseBuffer();
		m_pView->GetDocument()->SetModelByName(modelName);
	} else {
		m_pView->GetDocument()->SetModelByName(L"");
	}
	ResetEquip();
	ResetActionCombo();
}

void CRoleProp::OnBnClickedCheckShowRole()
{
	m_pView->GetDocument()->SetDrawRole(m_buttonShowRole.GetCheck() != 0);
}

void CRoleProp::OnBnClickedRadioSd(UINT nID)
{
	m_pView->GetDocument()->SetSpriteDirection(static_cast<Nuclear::XPDIRECTION>(nID - IDC_RADIO_SD_0));
}

void CRoleProp::ResetActionCombo()
{
	m_action.ResetContent();
	if (m_model.GetCurSel() != 0)
	{
		Nuclear::SpriteManager* pSprMan = m_pView->GetDocument()->GetSpriteManager();
		XPASSERT(pSprMan != NULL);
		std::wstring act_name = m_pView->GetDocument()->GetActionName();
		CString strName;
		m_model.GetWindowText(strName);
		std::wstring modelName = strName.GetBuffer();
		strName.ReleaseBuffer();
		std::vector<std::wstring> actlist;
		pSprMan->DumpAction(modelName, actlist);
		int n = (int)actlist.size();
		int curSel = 0;
		for(int i=0; i<n; ++i)
		{
			m_action.AddString(actlist[i].c_str());
			if (act_name == actlist[i])
			{
				curSel = i;
			}
		}
		m_action.SetCurSel(curSel);
		if (actlist.size()>0)
			SetAction(actlist[curSel]);
		else
			SetAction(L"");
	}
}

void CRoleProp::ResetComponent()
{
	m_component.ResetContent();
	if (m_layer.GetCount() == 0)
		return;
	m_component.AddString(L"");
	std::wstring nowComp = m_pView->GetDocument()->GetComponent(m_layer.GetCurSel());
	Nuclear::SpriteManager* pSprMan = m_pView->GetDocument()->GetSpriteManager();
	XPASSERT(pSprMan != NULL);
	CString strName;
	m_model.GetWindowText(strName);
	std::wstring modelName = strName.GetBuffer();
	strName.ReleaseBuffer();
	std::vector<std::wstring> compos;
	pSprMan->DumpComponent(modelName, m_layer.GetCurSel(), compos);
	int n = (int)compos.size();
	m_component.SetCurSel(0);
	for(int i=0; i<n; ++i)
	{
		m_component.AddString(compos[i].c_str());
		if (compos[i] == nowComp)
			m_component.SetCurSel(i+1);
	}

}

void CRoleProp::ResetEquip()
{
	m_layer.ResetContent();
	if (m_model.GetCurSel() != 0)
	{
		Nuclear::SpriteManager* pSprMan = m_pView->GetDocument()->GetSpriteManager();
		XPASSERT(pSprMan != NULL);
		CString strName;
		m_model.GetWindowText(strName);
		std::wstring modelName = strName.GetBuffer();
		strName.ReleaseBuffer();
		int layerCount = pSprMan->GetLayerCount(modelName);
		for (int i=0;i<layerCount;++i)
		{
			m_layer.AddString(pSprMan->GetLayerDes(modelName, i).c_str());
		}
		m_layer.SetCurSel(0);
	}
	ResetComponent();

}

BOOL CRoleProp::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_pView = dynamic_cast<CPropertyView*>(GetParent()->GetParent());


	m_model.ResetContent();
	m_modelMap.clear();
	m_model.AddString(L"无");
	m_model.SetCurSel(0);
	m_modelMap[L""] = 0;
	std::vector<std::wstring> v;
	m_pView->GetDocument()->GetSpriteManager()->DumpModelNames(v);
	int i;
	for(i=0; i<(int)v.size();++i)
	{
		m_model.AddString(v[i].c_str());
		m_modelMap[v[i]] = i + 1;
	}
	ResetEquip();
	ResetActionCombo();

	CheckRadioButton(IDC_RADIO_SD_0, IDC_RADIO_SD_7, IDC_RADIO_SD_0);
	m_comboDivLayer.ResetContent();
	CString tmpStr;
	for (i=0; i<=Nuclear::GEffect::MAX_CLIP_LAYER;++i)
	{
		tmpStr.Format(L"%d", i);
		m_comboDivLayer.AddString(tmpStr);
	}

	m_bIsInited = true;
	return TRUE;
}

void CRoleProp::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}

void CRoleProp::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

BEGIN_MESSAGE_MAP(CRoleProp, CDialog)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_CONTROL_RANGE(BN_CLICKED, IDC_RADIO_SD_0, IDC_RADIO_SD_7, &CRoleProp::OnBnClickedRadioSd)
	ON_BN_CLICKED(IDC_CHECK_SHOW_ROLE, &CRoleProp::OnBnClickedCheckShowRole)
	ON_BN_CLICKED(IDC_BUTTON_LAYER, &CRoleProp::OnBnClickedButtonLayer)
	ON_BN_CLICKED(IDC_POS_BUTTON, &CRoleProp::OnBnClickedPosButton)
	ON_BN_CLICKED(IDC_ROTATION_BUTTON, &CRoleProp::OnBnClickedRotationButton)
	ON_CBN_SELENDOK(IDC_COMBO_DIV_LAYER, &CRoleProp::OnCbnSelendokComboDivLayer)
	ON_CBN_SELENDOK(IDC_COMBO_ACTION, &CRoleProp::OnCbnSelendokComboAction)
	ON_CBN_SELENDOK(IDC_COMBO_COMPONENT, &CRoleProp::OnCbnSelendokComboComponent)
	ON_CBN_SELENDOK(IDC_COMBO_LAYER, &CRoleProp::OnCbnSelendokComboLayer)
	ON_CBN_SELENDOK(IDC_COMBO_MODEL, &CRoleProp::OnCbnSelendokComboModel)
	ON_BN_CLICKED(IDC_BUTTON_COLOR, &CRoleProp::OnBnClickedButtonColor)
END_MESSAGE_MAP()



void CRoleProp::ResetSize()
{

}

void CRoleProp::ResetData()
{
	CEffectEditorDoc *pDoc = m_pView->GetDocument();
	switch (pDoc->GetEffectFileType())
	{
	case EFT_EFFECT:
		m_buttonLayer.ShowWindow(SW_HIDE);
		m_buttonPos.ShowWindow(SW_HIDE);
		m_buttonRotation.ShowWindow(SW_HIDE);
		m_comboDivLayer.ShowWindow(SW_HIDE);
		GetDlgItem(IDC_STATIC_LAYER)->ShowWindow(SW_HIDE);
		break;
	case EFT_SEBIND:
		if (pDoc->AddNotify(this))
		{
			m_comboDivLayer.EnableWindow(false);
		}
		m_buttonLayer.ShowWindow(SW_SHOW);
		m_buttonPos.ShowWindow(SW_SHOW);
		m_buttonRotation.ShowWindow(SW_SHOW);
		m_comboDivLayer.ShowWindow(SW_SHOW);
		GetDlgItem(IDC_STATIC_LAYER)->ShowWindow(SW_SHOW);
		break;
	default:
		break;
	}
	m_buttonShowRole.SetCheck(pDoc->IsDrawRole());
	CheckRadioButton(IDC_RADIO_SD_0, IDC_RADIO_SD_7, IDC_RADIO_SD_0 + pDoc->GetDirection());
	std::wstring model_name = pDoc->GetModelName();
	std::map<std::wstring, int>::iterator iter = m_modelMap.find(model_name);
	XPASSERT(iter != m_modelMap.end());
	if (m_model.GetCurSel() != iter->second)
	{
		m_model.SetCurSel(iter->second);
		ResetEquip();
		ResetActionCombo();
	}
}

void CRoleProp::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_RADIO_SD_0, m_RadioSD[0]);
	DDX_Control(pDX, IDC_RADIO_SD_1, m_RadioSD[1]);
	DDX_Control(pDX, IDC_RADIO_SD_2, m_RadioSD[2]);
	DDX_Control(pDX, IDC_RADIO_SD_3, m_RadioSD[3]);
	DDX_Control(pDX, IDC_RADIO_SD_4, m_RadioSD[4]);
	DDX_Control(pDX, IDC_RADIO_SD_5, m_RadioSD[5]);
	DDX_Control(pDX, IDC_RADIO_SD_6, m_RadioSD[6]);
	DDX_Control(pDX, IDC_RADIO_SD_7, m_RadioSD[7]);
	DDX_Control(pDX, IDC_CHECK_SHOW_ROLE, m_buttonShowRole);
	DDX_Control(pDX, IDC_COMBO_MODEL, m_model);
	DDX_Control(pDX, IDC_COMBO_LAYER, m_layer);
	DDX_Control(pDX, IDC_COMBO_COMPONENT, m_component);
	DDX_Control(pDX, IDC_COMBO_ACTION, m_action);
	DDX_Control(pDX, IDC_FRAME_COUNT, m_frameCount);
	DDX_Control(pDX, IDC_STATIC_TOTAL_TIME, m_totalTime);
	DDX_Control(pDX, IDC_BUTTON_LAYER, m_buttonLayer);
	DDX_Control(pDX, IDC_POS_BUTTON, m_buttonPos);
	DDX_Control(pDX, IDC_ROTATION_BUTTON, m_buttonRotation);
	DDX_Control(pDX, IDC_COMBO_DIV_LAYER, m_comboDivLayer);
}

CRoleProp::~CRoleProp()
{
}

CRoleProp::CRoleProp(CWnd* pParent /*=NULL*/)
: CDialog(CRoleProp::IDD, pParent)
{
	m_bIsInited = false;
}

