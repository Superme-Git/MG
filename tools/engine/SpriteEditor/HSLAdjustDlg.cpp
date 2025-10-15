


#include "stdafx.h"
#include "SpriteEditor.h"
#include "HSLAdjustDlg.h"




IMPLEMENT_DYNAMIC(CHSLAdjustDlg, CDialog)

void CHSLAdjustDlg::OnDelDyeProj()
{
	m_pModel->DelDyeProj(m_CurSelPart, m_CurSelProj);
	if(m_CurSelProj >= m_pModel->getDyeProjCount(m_CurSelPart))	
		m_CurSelProj = m_pModel->getDyeProjCount(m_CurSelPart) - 1;	
	m_comboBoxDyeProj.SetCurSel(m_CurSelProj);
	UpdateProjectView();
	UpdateDyeView();
	ModifyParams();
}

void CHSLAdjustDlg::OnAddDyeProj()
{
	int ct = m_pModel->getDyeProjCount(m_CurSelPart);
	if(ct >= 9)
		return;
	m_pModel->AddDyeProj(m_CurSelPart);
	m_CurSelProj = ct;
	UpdateProjectView();
	UpdateDyeView();
	ModifyParams();
}
void CHSLAdjustDlg::OnDelDyePart()
{	
	m_pModel->DelDyePart(m_CurSelPart);
	if(m_CurSelPart >= m_pModel->getDyePartCount())	
		m_CurSelPart = m_pModel->getDyePartCount() - 1;	
	m_comboBoxDyePart.SetCurSel(m_CurSelPart);
	UpdatePartView();
	UpdateProjectView();
	UpdateDyeView();
	ModifyParams();
}
void CHSLAdjustDlg::OnAddDyePart()
{
	int ct = m_pModel->getDyePartCount();
	if(ct >= 2)
		return;
	m_pModel->AddDyePart();
	m_CurSelPart = ct;
	UpdatePartView();
	UpdateProjectView();
	UpdateDyeView();
	ModifyParams();
}

int textDt[8] = {2,3,4,5,6,7,0,1};

void CHSLAdjustDlg::UpdateDyeView()
{
	if(m_pModel == NULL)	return;
	Nuclear::SpriteManager::XModel::DyeInfo* pInfo = m_pModel->GetDyeInfo(m_CurSelPart,m_CurSelProj);
	if(pInfo != NULL)
	{
		((CButton*)GetDlgItem(IDC_RADIO_HSL))->SetCheck(0);
		((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC))->SetCheck(0);
		((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC2))->SetCheck(0);
		((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC3))->SetCheck(0);
		switch(pInfo->iType)
		{
		case 1:((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC))->SetCheck(1);break;
		case 2:((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC3))->SetCheck(1);break;
		case 3:((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC2))->SetCheck(1);break;
		case 0:((CButton*)GetDlgItem(IDC_RADIO_HSL))->SetCheck(1);break;
		}
		((CSliderCtrl*)GetDlgItem(IDC_SLIDER_LIGHT_NESS))->SetPos(pInfo->iBright);

		for( int i = 0; i < 8; i++ )
		{
			if(pInfo->iR == textDt[i])
				m_comboBoxR.SetCurSel(i);
			if(pInfo->iG == textDt[i])
				m_comboBoxG.SetCurSel(i);
			if(pInfo->iB == textDt[i])
				m_comboBoxB.SetCurSel(i);
		}

		((CSliderCtrl*)GetDlgItem(IDC_SLIDER_H))->SetPos(pInfo->iH);
		((CSliderCtrl*)GetDlgItem(IDC_SLIDER_S))->SetPos(pInfo->iS);


		wchar_t   buf[32] = {0};
		wsprintf(buf, L"%d", pInfo->iH);
		SetDlgItemText(IDC_EDT_H, buf);
		wsprintf(buf, L"%d", pInfo->iS);
		SetDlgItemText(IDC_EDT_S, buf);


		wsprintf(buf, L"%d", pInfo->iBright);
		SetDlgItemText(IDC_EDIT_LIGHT_NESS, buf);
	}
}
void CHSLAdjustDlg::UpdateProjectView()
{
	if(m_pModel == NULL)	return;
	int selPart = m_comboBoxDyePart.GetCurSel();
	m_comboBoxDyeProj.ResetContent();
	int projCount = m_pModel->getDyeProjCount(selPart);
	for(int i = 0; i < projCount; i++)
	{
		wchar_t txt[32];
		wsprintf(txt,L"染色方案%d",i+1);
		m_comboBoxDyeProj.InsertString(i, txt);
	}
	if(m_CurSelProj >= projCount)
		m_CurSelProj = projCount - 1;
	m_comboBoxDyeProj.SetCurSel(m_CurSelProj);
}
void CHSLAdjustDlg::UpdatePartView()
{
	if(m_pModel == NULL)	return;

	m_comboBoxDyePart.ResetContent();
	int partCount = m_pModel->getDyePartCount();
	for(int i = 0; i < partCount; i++)
	{
		wchar_t txt[32];
		wsprintf(txt,L"部位%d",i+1);
		m_comboBoxDyePart.InsertString(i, txt);
	}
	m_comboBoxDyePart.SetCurSel(m_CurSelPart);
}

void CHSLAdjustDlg::OnUnDo()
{
	UpdateDyeView();
	ModifyParams();
}
void CHSLAdjustDlg::OnBnClickedCancle()
{
	OnCancel();
}
void CHSLAdjustDlg::OnBnClickedSave()
{
	if(m_pModel == NULL)	return;
	Nuclear::SpriteManager::XModel::DyeInfo* pInfo = m_pModel->GetDyeInfo(m_CurSelPart,m_CurSelProj);
	if(pInfo != NULL)
	{
		if(((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC))->GetCheck() != 0 )
			pInfo->iType = 1;
		else if(((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC3))->GetCheck() != 0)
			pInfo->iType = 2;
		else if(((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC2))->GetCheck() != 0)
			pInfo->iType = 3;
		else
			pInfo->iType = 0;

		pInfo->iBright = ((CSliderCtrl*)GetDlgItem(IDC_SLIDER_LIGHT_NESS))->GetPos();

		pInfo->iR = textDt[m_comboBoxR.GetCurSel()];
		pInfo->iG = textDt[m_comboBoxG.GetCurSel()];
		pInfo->iB = textDt[m_comboBoxB.GetCurSel()];

		pInfo->iH = ((CSliderCtrl*)GetDlgItem(IDC_SLIDER_H))->GetPos();
		pInfo->iS = ((CSliderCtrl*)GetDlgItem(IDC_SLIDER_S))->GetPos();
		pInfo->iL = ((CSliderCtrl*)GetDlgItem(IDC_SLIDER_L))->GetPos();
	}
}

void CHSLAdjustDlg::OnBnClickedOk()
{
	OnBnClickedSave();
	OnOK();
}

void CHSLAdjustDlg::ModifyParams()
{
	int iType = 0;
	if(((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC))->GetCheck() != 0 )
		iType = 1;
	else if(((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC3))->GetCheck() != 0)
		iType = 2;
	else if(((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC2))->GetCheck() != 0)
		iType = 3;

	int iBright = ((CSliderCtrl*)GetDlgItem(IDC_SLIDER_LIGHT_NESS))->GetPos();

	int iR = textDt[m_comboBoxR.GetCurSel()];
	int iG = textDt[m_comboBoxG.GetCurSel()];
	int iB = textDt[m_comboBoxB.GetCurSel()];

	int iH = ((CSliderCtrl*)GetDlgItem(IDC_SLIDER_H))->GetPos();
	int iS = ((CSliderCtrl*)GetDlgItem(IDC_SLIDER_S))->GetPos();
	int iL = ((CSliderCtrl*)GetDlgItem(IDC_SLIDER_L))->GetPos();

	Nuclear::SpriteManager::XModel::DyeInfo* pInfo = m_pModel->GetDyeInfo(m_CurSelPart,m_CurSelProj);
	if(pInfo == NULL)	return;
	if(m_CurSelPart == 0)
	{
		m_pParam->fParam0[0] = (float)iH/180;
		m_pParam->fParam0[1] = (float)iS/100;

		m_pParam->fParam1[3] = iType+0.5;
		if(iType > 0.5 )
		{
			m_pParam->fParam1[0] = (float)iBright/255;
		}
		else
		{
			m_pParam->fParam1[0] = iR;
			m_pParam->fParam1[1] = iG;
			m_pParam->fParam1[2] = iB;
		}
	}
	else
	{
		m_pParam->fParam0[2] = (float)iH/180;
		m_pParam->fParam0[3] = (float)iS/100;

		m_pParam->fParam2[3] = iType+0.5;
		if(iType > 0.5 )
		{
			m_pParam->fParam2[0] = (float)iBright/255;
		}
		else
		{
			m_pParam->fParam2[0] = iR;
			m_pParam->fParam2[1] = iG;
			m_pParam->fParam2[2] = iB;
		}
	}
}

void CHSLAdjustDlg::OnCbnSelchangeColor()
{
	ModifyParams();
}

void CHSLAdjustDlg::OnCbnSelchangeDyeProj()
{
	m_CurSelProj = m_comboBoxDyeProj.GetCurSel();
	m_CurPartProj[m_CurSelPart] = m_CurSelProj;
	UpdateDyeView();
	ModifyParams();
}
void CHSLAdjustDlg::OnCbnSelchangeDyePart()
{
	m_CurSelPart = m_comboBoxDyePart.GetCurSel();
	m_CurSelProj = m_CurPartProj[m_CurSelPart];
	UpdateProjectView();
	UpdateDyeView();
	ModifyParams();
}
void CHSLAdjustDlg::OnSliderReleasedcapture(UINT nID, NMHDR *pNMHDR, LRESULT *pResult)
{
	ModifyParams();
	*pResult = 0;
}

void CHSLAdjustDlg::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	wchar_t   buf[32] = {0};
	switch (pScrollBar->GetDlgCtrlID())
	{
	case IDC_SLIDER_H:
		m_nH = m_sliderH.GetPos();
		wsprintf(buf, L"%d", m_nH);
		SetDlgItemText(IDC_EDT_H, buf);
		break;
	case IDC_SLIDER_S:
		m_nS = m_sliderS.GetPos();
		wsprintf(buf, L"%d", m_nS);
		SetDlgItemText(IDC_EDT_S, buf);
		break;
	case IDC_SLIDER_L:
		m_nL = m_sliderL.GetPos();
		wsprintf(buf, L"%d", m_nL);
		SetDlgItemText(IDC_EDT_L, buf);
		break;
	case IDC_SLIDER_LIGHT_NESS:
		m_nBright = m_sliderBright.GetPos();
		wsprintf(buf, L"%d", m_nBright);
		SetDlgItemText(IDC_EDIT_LIGHT_NESS, buf);
		break;
	}
	UpdateData(FALSE);
	ModifyParams();
	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CHSLAdjustDlg::OnBnClickedRadioProj()
{

	m_Type = 0;
	if(((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC))->GetCheck() != 0 )
		m_Type = 1;
	else if(((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC3))->GetCheck() != 0)
		m_Type = 2;
	else if(((CButton*)GetDlgItem(IDC_RADIO_GRAYPIC2))->GetCheck() != 0)
		m_Type = 3;
	ModifyParams();
}

BOOL CHSLAdjustDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	std::wstring text[8] = {L"R",L"G",L"B",L"1-R",L"1-G",L"1-B",L"0",L"1"};

	for( int i = 0; i < 8; i++ )
	{
		m_comboBoxR.InsertString(i,text[i].c_str());
		m_comboBoxG.InsertString(i,text[i].c_str());
		m_comboBoxB.InsertString(i,text[i].c_str());

		m_comboBoxR.SetItemData(i,textDt[i]);
		m_comboBoxG.SetItemData(i,textDt[i]);
		m_comboBoxB.SetItemData(i,textDt[i]);

		m_CurPartProj[i] = 0;
	}

	m_sliderH.SetRange(-180, 180, TRUE);
	m_sliderS.SetRange(-100, 100, TRUE);
	m_sliderL.SetRange(-100, 100, TRUE);
	m_sliderBright.SetRange(-127, 127, TRUE);

	UpdatePartView();
	UpdateProjectView();
	UpdateDyeView();
	ModifyParams();
	return TRUE;  

}

BEGIN_MESSAGE_MAP(CHSLAdjustDlg, CDialog)
	ON_BN_CLICKED(IDC_RADIO_HSL, &CHSLAdjustDlg::OnBnClickedRadioProj)
	ON_BN_CLICKED(IDC_RADIO_GRAYPIC, &CHSLAdjustDlg::OnBnClickedRadioProj)
	ON_BN_CLICKED(IDC_RADIO_GRAYPIC2, &CHSLAdjustDlg::OnBnClickedRadioProj)
	ON_BN_CLICKED(IDC_RADIO_GRAYPIC3, &CHSLAdjustDlg::OnBnClickedRadioProj)
	ON_WM_HSCROLL()
	ON_BN_CLICKED(IDOK, &CHSLAdjustDlg::OnBnClickedOk)
	ON_NOTIFY_RANGE(NM_RELEASEDCAPTURE, IDC_SLIDER_H, IDC_SLIDER_L, &CHSLAdjustDlg::OnSliderReleasedcapture)
	ON_BN_CLICKED(IDC_CANCLE, &CHSLAdjustDlg::OnBnClickedCancle)
	ON_BN_CLICKED(IDC_OK, &CHSLAdjustDlg::OnBnClickedOk)
	ON_CBN_SELCHANGE(IDC_DYE_PROJ, &CHSLAdjustDlg::OnCbnSelchangeDyeProj)
	ON_CBN_SELCHANGE(IDC_DYE_PART, &CHSLAdjustDlg::OnCbnSelchangeDyePart)
	ON_BN_CLICKED(IDC_BTN_ADDPART, &CHSLAdjustDlg::OnAddDyePart)
	ON_BN_CLICKED(IDC_BTN_DELPART, &CHSLAdjustDlg::OnDelDyePart)
	ON_BN_CLICKED(IDC_BTN_ADDPROJ, &CHSLAdjustDlg::OnAddDyeProj)
	ON_BN_CLICKED(IDC_BTN_DELPROJ, &CHSLAdjustDlg::OnDelDyeProj)
	ON_CBN_SELCHANGE(IDC_COMB_CR, &CHSLAdjustDlg::OnCbnSelchangeColor)
	ON_CBN_SELCHANGE(IDC_COMB_CG, &CHSLAdjustDlg::OnCbnSelchangeColor)
	ON_CBN_SELCHANGE(IDC_COMB_CB, &CHSLAdjustDlg::OnCbnSelchangeColor)
	ON_BN_CLICKED(IDC_UNDO, &CHSLAdjustDlg::OnUnDo)
	ON_BN_CLICKED(IDC_SAVE, &CHSLAdjustDlg::OnBnClickedSave)
END_MESSAGE_MAP()

void CHSLAdjustDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_RADIO_HSL, m_radioProjs[0]);
	DDX_Control(pDX, IDC_RADIO_GRAYPIC, m_radioProjs[1]);
	DDX_Control(pDX, IDC_DYE_PROJ, m_comboBoxDyeProj);
	DDX_Control(pDX, IDC_DYE_PART, m_comboBoxDyePart);
	DDX_Control(pDX, IDC_SLIDER_H, m_sliderH);
	DDX_Control(pDX, IDC_SLIDER_S, m_sliderS);
	DDX_Control(pDX, IDC_SLIDER_L, m_sliderL);
	DDX_Control(pDX, IDC_SLIDER_LIGHT_NESS, m_sliderBright);
	DDX_Text(pDX, IDC_EDT_H, m_nH);
	DDX_Text(pDX, IDC_EDT_S, m_nS);
	DDX_Text(pDX, IDC_EDT_L, m_nL);
	DDX_Text(pDX, IDC_EDIT_LIGHT_NESS, m_nBright);	
	DDX_Control(pDX, IDC_COMB_CR, m_comboBoxR);
	DDX_Control(pDX, IDC_COMB_CG, m_comboBoxG);
	DDX_Control(pDX, IDC_COMB_CB, m_comboBoxB);
}

CHSLAdjustDlg::~CHSLAdjustDlg()
{
}

CHSLAdjustDlg::CHSLAdjustDlg( Nuclear::SpriteManager::XModel* pModel, Nuclear::XPDyeShaderParam* pParam, CWnd* pParent /*=NULL*/)
: CDialog(CHSLAdjustDlg::IDD, pParent)
, m_pModel(pModel)
, m_pParam(pParam)
{
	m_nH = 0;
	m_nS = 0;
	m_nL = 0;
	m_nBright = 0;

	m_CurSelProj = 0;
	m_CurSelPart = 0;
}

