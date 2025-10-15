// DlgPslInfoParam.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "DlgPslInfoParam.h"


// CDlgPslInfoParam �Ի���

IMPLEMENT_DYNAMIC(CDlgPslInfoParam, CDialog)

CDlgPslInfoParam::CDlgPslInfoParam(CWnd* pParent /*=NULL*/)
	: CDialog(CDlgPslInfoParam::IDD, pParent)
	, m_nMaxParticlesThreshold(800)
	, m_bMaxParticles(FALSE)
	, m_bFullScreen(FALSE)
	, m_bSpecialPS(FALSE)
	, m_bMultTexture(FALSE)
	, m_bCustomPath(FALSE)
	, m_bAlphaADD(FALSE)
{

}

CDlgPslInfoParam::~CDlgPslInfoParam()
{
}

void CDlgPslInfoParam::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_MaxParticles, m_nMaxParticlesThreshold);
	DDV_MinMaxInt(pDX, m_nMaxParticlesThreshold, 0, 2000);
	DDX_Check(pDX, IDC_CHECK_MaxParticles, m_bMaxParticles);
	DDX_Check(pDX, IDC_CHECK_FullScreen, m_bFullScreen);
	DDX_Check(pDX, IDC_CHECK_SpecialPS, m_bSpecialPS);
	DDX_Check(pDX, IDC_CHECK_MultTextures, m_bMultTexture);
	DDX_Check(pDX, IDC_CHECK_CustomPath, m_bCustomPath);
	DDX_Check(pDX, IDC_CHECK_AlphaADD, m_bAlphaADD);
}


BEGIN_MESSAGE_MAP(CDlgPslInfoParam, CDialog)
	ON_BN_CLICKED(IDOK, &CDlgPslInfoParam::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CDlgPslInfoParam::OnBnClickedCancel)
	ON_EN_KILLFOCUS(IDC_EDIT_MaxParticles, &CDlgPslInfoParam::OnEnKillfocusEditMaxparticles)
	ON_BN_CLICKED(IDC_CHECK_MaxParticles, &CDlgPslInfoParam::OnBnClickedCheckMaxparticles)
	ON_BN_CLICKED(IDC_CHECK_FullScreen, &CDlgPslInfoParam::OnBnClickedCheckFullscreen)
	ON_BN_CLICKED(IDC_CHECK_SpecialPS, &CDlgPslInfoParam::OnBnClickedCheckSpecialps)
	ON_BN_CLICKED(IDC_CHECK_MultTextures, &CDlgPslInfoParam::OnBnClickedCheckMulttextures)
	ON_BN_CLICKED(IDC_CHECK_CustomPath, &CDlgPslInfoParam::OnBnClickedCheckCustompath)
	ON_BN_CLICKED(IDC_CHECK_AlphaADD, &CDlgPslInfoParam::OnBnClickedCheckAlphaadd)
END_MESSAGE_MAP()


// CDlgPslInfoParam ��Ϣ�������

void CDlgPslInfoParam::OnBnClickedOk()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnOK();
}

void CDlgPslInfoParam::OnBnClickedCancel()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnCancel();
}

void CDlgPslInfoParam::OnEnKillfocusEditMaxparticles()
{
	UpdateData(true);	
	m_nMaxParticlesThreshold = m_nMaxParticlesThreshold<0 ? 0:m_nMaxParticlesThreshold;
	m_nMaxParticlesThreshold = m_nMaxParticlesThreshold>2000 ? 2000:m_nMaxParticlesThreshold;
	UpdateData(false);
}

void CDlgPslInfoParam::OnBnClickedCheckMaxparticles()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	UpdateData(TRUE);
}

void CDlgPslInfoParam::OnBnClickedCheckFullscreen()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	UpdateData(TRUE);
}

void CDlgPslInfoParam::OnBnClickedCheckSpecialps()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	UpdateData(TRUE);
}

void CDlgPslInfoParam::OnBnClickedCheckMulttextures()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	UpdateData(TRUE);
}

void CDlgPslInfoParam::OnBnClickedCheckCustompath()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	UpdateData(TRUE);
}

void CDlgPslInfoParam::OnBnClickedCheckAlphaadd()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	UpdateData(TRUE);
}
