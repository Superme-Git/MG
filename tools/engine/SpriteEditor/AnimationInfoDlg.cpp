

#include "stdafx.h"
#include "SpriteEditor.h"
#include "AnimationInfoDlg.h"
#include "EdtIndActDlg.h"




IMPLEMENT_DYNAMIC(CAnimationInfoDlg, CDialog)

void CAnimationInfoDlg::OnCbnSelchangeComboAnimationType()
{
	GetDlgItem(IDC_BTN_EDTINDACT)->ShowWindow(m_ComboBoxAnimationType.GetCurSel()==1?SW_SHOW:SW_HIDE);
}

void CAnimationInfoDlg::OnBnClickedBtnEdtindact()
{
	CEdtIndActDlg dlg;
	wchar_t buf[64];
	GetDlgItemText(IDC_EDT_FRAME, buf, 64);
	dlg.m_nFrame = _wtoi(buf);
	if( dlg.m_nFrame < 1 ) return;
	dlg.m_pInfoDlg = this;
	dlg.DoModal();
}

BOOL CAnimationInfoDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_ComboBoxAnimationType.AddString(L"基本动作");
	m_ComboBoxAnimationType.AddString(L"间接动作");
	m_ComboBoxAnimationType.SetCurSel(m_fIndAction?1:0);

	GetDlgItem(IDC_BTN_EDTINDACT)->ShowWindow(m_fIndAction?SW_SHOW:SW_HIDE);

	int n = m_pSpriteMgr->GetLayerCount(m_ModelName);

#define M1(i) GetDlgItem(IDC_CHK_L##i)->ShowWindow(n>=i ? SW_SHOW : SW_HIDE); \
	if( n>= i ) GetDlgItem(IDC_CHK_L##i)->SetWindowText(m_pSpriteMgr->GetLayerName(m_ModelName, i-1).c_str())

	M1(1);
	M1(2);
	M1(3);
	M1(4);
	M1(5);
	M1(6);
	M1(7);
	M1(8);
	M1(9);
	M1(10);
	M1(11);
	M1(12);

	for(int i=0; i<n; ++i)
	{
		if( m_pSpriteMgr->GetLayerRideEnable(m_ModelName,i) )
			m_EffectLayers.push_back(std::make_pair(i,m_pSpriteMgr->GetLayerName(m_ModelName,i)));
	}

	n = (int)m_EffectLayers.size();
	for(int i=0; i<n; ++i)
		m_ComboBoxEditEffectLayer.AddString(m_EffectLayers[i].second.c_str());
	if( n != 0 ) m_ComboBoxEditEffectLayer.SetCurSel(0);

	return TRUE;  

}

void CAnimationInfoDlg::OnOK()
{
	UpdateData();
	if( m_Name.IsEmpty() )
	{
		AfxMessageBox(L"动作名称不能为空");
		return;
	}
	if( !m_fDir0 && !m_fDir1 && !m_fDir2 && !m_fDir3 && !m_fDir4 && !m_fDir5 && !m_fDir6 && !m_fDir7)
	{
		AfxMessageBox(L"至少要有一个方向吧");
		return;
	}
	if( m_DamagePoint <-1 || m_DamagePoint >= m_nFrame )
	{
		AfxMessageBox(L"伤害点设置不正确");
		return;
	}
	if( m_EffectPoint <-1 || m_EffectPoint >= m_nFrame )
	{
		AfxMessageBox(L"效果发射点设置不正确");
		return;
	}
	m_fIndAction = (m_ComboBoxAnimationType.GetCurSel() == 1);
	CDialog::OnOK();
}

BEGIN_MESSAGE_MAP(CAnimationInfoDlg, CDialog)
	ON_BN_CLICKED(IDC_BTN_EDTINDACT, &CAnimationInfoDlg::OnBnClickedBtnEdtindact)
	ON_CBN_SELCHANGE(IDC_CMB_ACTTYPE, &CAnimationInfoDlg::OnCbnSelchangeComboAnimationType)
END_MESSAGE_MAP()

void CAnimationInfoDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDT_NAME, m_Name);
	DDV_MaxChars(pDX, m_Name, 32);
	DDX_Text(pDX, IDC_EDT_FRAME, m_nFrame);
	DDV_MinMaxInt(pDX, m_nFrame, 1, 128);
	DDX_Text(pDX, IDC_EDT_TIME, m_Time);
	DDV_MinMaxInt(pDX, m_Time, 16, 65536);

	DDX_Check(pDX, IDC_CHK_L1, m_fLayer[0]);
	DDX_Check(pDX, IDC_CHK_L2, m_fLayer[1]);
	DDX_Check(pDX, IDC_CHK_L3, m_fLayer[2]);
	DDX_Check(pDX, IDC_CHK_L4, m_fLayer[3]);
	DDX_Check(pDX, IDC_CHK_L5, m_fLayer[4]);
	DDX_Check(pDX, IDC_CHK_L6, m_fLayer[5]);
	DDX_Check(pDX, IDC_CHK_L7, m_fLayer[6]);
	DDX_Check(pDX, IDC_CHK_L8, m_fLayer[7]);
	DDX_Check(pDX, IDC_CHK_L9, m_fLayer[8]);
	DDX_Check(pDX, IDC_CHK_L10, m_fLayer[9]);
	DDX_Check(pDX, IDC_CHK_L11, m_fLayer[10]);
	DDX_Check(pDX, IDC_CHK_L12, m_fLayer[11]);

	DDX_Check(pDX, IDC_CHK_DIR0, m_fDir0);
	DDX_Check(pDX, IDC_CHK_DIR1, m_fDir1);
	DDX_Check(pDX, IDC_CHK_DIR2, m_fDir2);
	DDX_Check(pDX, IDC_CHK_DIR3, m_fDir3);
	DDX_Check(pDX, IDC_CHK_DIR4, m_fDir4);
	DDX_Check(pDX, IDC_CHK_DIR5, m_fDir5);
	DDX_Check(pDX, IDC_CHK_DIR6, m_fDir6);
	DDX_Check(pDX, IDC_CHK_DIR7, m_fDir7);
	DDX_Text(pDX, IDC_EDT_STRIDE, m_Stride);
	DDV_MinMaxInt(pDX, m_Stride, 0, 512);
	DDX_Text(pDX, IDC_EDT_DAMAGEPOINT, m_DamagePoint);
	DDX_Text(pDX, IDC_EDT_EFFECTPOINT, m_EffectPoint);

	DDX_Control(pDX, IDC_CMB_ACTTYPE, m_ComboBoxAnimationType);
	DDX_Control(pDX, IDC_CMB_EDTEFFECTLAYER, m_ComboBoxEditEffectLayer);
}

void CAnimationInfoDlg::InitNew()
{
	for(int i=0; i< Nuclear::XPSC_MAXCOUNT; ++i)
	{
		m_fLayer[i] = i > 4 ? FALSE : TRUE;
	}

	m_fDir0 = true;
	m_fDir1 = true;
	m_fDir2 = true;
	m_fDir3 = true;
	m_fDir4 = true;
	m_fDir5 = true;
	m_fDir6 = true;
	m_fDir7 = true;

	m_fIndAction = false;
	m_nFrame = 8;
	m_Time = 1000;
}

CAnimationInfoDlg::~CAnimationInfoDlg()
{
}

CAnimationInfoDlg::CAnimationInfoDlg(Nuclear::SpriteManager *pSprMan, const std::wstring &modelname, CWnd* pParent /*=NULL*/)
: CDialog(CAnimationInfoDlg::IDD, pParent)
, m_pSpriteMgr(pSprMan)
, m_ModelName(modelname)
, m_fIndAction(false)
, m_Name(_T(""))
, m_nFrame(0)
, m_Time(0)
, m_fDir0(FALSE)
, m_fDir1(FALSE)
, m_fDir2(FALSE)
, m_fDir3(FALSE)
, m_fDir4(FALSE)
, m_fDir5(FALSE)
, m_fDir6(FALSE)
, m_fDir7(FALSE)
, m_Stride(0)
, m_DamagePoint(-1)
, m_EffectPoint(-1)
{
	for(int i=0; i< Nuclear::XPSC_MAXCOUNT; ++i)
		m_fLayer[i] = FALSE;
}

