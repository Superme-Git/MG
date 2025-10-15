// TextInputDlg.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "TextInputDlg.h"


// CTextInputDlg �Ի���

IMPLEMENT_DYNAMIC(CTextInputDlg, CDialog)

CTextInputDlg::CTextInputDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CTextInputDlg::IDD, pParent)
	, m_nFontAngle(0)
{

	m_strInputText = L"2D����";
	m_nFontHeight = 180;
	m_nFontSpace = 1;
	m_strFontName = L"����";
}

CTextInputDlg::~CTextInputDlg()
{
}

void CTextInputDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_InputText, m_strInputText);
	DDX_Control(pDX, IDC_COMBO_FontName, m_comboFontName);
	DDX_Text(pDX, IDC_EDIT_FontSpace, m_nFontSpace);
	DDX_Text(pDX, IDC_EDIT_FontHeight, m_nFontHeight);
	DDX_Text(pDX, IDC_EDIT_FontAngle, m_nFontAngle);
	DDV_MinMaxInt(pDX, m_nFontAngle, -10000, 10000);
}


BEGIN_MESSAGE_MAP(CTextInputDlg, CDialog)
	ON_BN_CLICKED(IDOK, &CTextInputDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CTextInputDlg::OnBnClickedCancel)
	ON_CBN_SELCHANGE(IDC_COMBO_FontName, &CTextInputDlg::OnCbnSelchangeComboFontname)
END_MESSAGE_MAP()


// CTextInputDlg ��Ϣ�������

void CTextInputDlg::OnBnClickedOk()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnOK();
}

void CTextInputDlg::OnBnClickedCancel()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnCancel();
}

void CTextInputDlg::OnCbnSelchangeComboFontname()
{
    m_comboFontName.GetLBText(m_comboFontName.GetCurSel(), m_strFontName);	
}

BOOL CTextInputDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  �ڴ���Ӷ���ĳ�ʼ��
	fillFontFaceName();

	CString str;
	bool bFontFaceNameFind = false;
	for(int i=0; i<m_comboFontName.GetCount(); i++)
	{
		m_comboFontName.GetLBText(i, str);
		if(str==m_strFontName)
		{    
			m_comboFontName.SetCurSel(i);
			bFontFaceNameFind = true;
			break;
		}
	}
	if(!bFontFaceNameFind && (m_comboFontName.GetCount() >0) )
	{
		m_comboFontName.GetLBText(0, m_strFontName);
		m_comboFontName.SetCurSel(0);
	}

	UpdateData(false);

	return TRUE;  // return TRUE unless you set the focus to a control
	// �쳣: OCX ����ҳӦ���� FALSE
}

void CTextInputDlg::fillFontFaceName()
{
	m_comboFontName.ResetContent();
	LOGFONT logfont;
	logfont.lfCharSet = ANSI_CHARSET;
	wcscpy_s(logfont.lfFaceName, sizeof(TCHAR), L"");
//	wcscpy(logfont.lfFaceName, L"");

	CClientDC dc(this);
	::EnumFontFamiliesEx((HDC)dc, &logfont,
				(FONTENUMPROC)EnumFontFamProc, (LPARAM)this, 0);
}

int CALLBACK EnumFontFamProc(LPENUMLOGFONT lpelf, LPNEWTEXTMETRIC lpntm, DWORD nFontType, long lparam)
{
	CTextInputDlg* pWnd = (CTextInputDlg*) lparam;
	if (nFontType==TRUETYPE_FONTTYPE )
		pWnd->m_comboFontName.AddString(lpelf->elfLogFont.lfFaceName);
	return 1;
}