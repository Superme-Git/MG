// DlgPicCutParam.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "DlgPicCutParam.h"


// CDlgPicCutParam �Ի���

IMPLEMENT_DYNAMIC(CDlgPicCutParam, CDialog)

CDlgPicCutParam::CDlgPicCutParam(CWnd* pParent /*=NULL*/)
	: CDialog(CDlgPicCutParam::IDD, pParent)
	, m_nRow(1)
	, m_nCol(1)
	, m_strFilePathDir(_T(""))
{
}

CDlgPicCutParam::~CDlgPicCutParam()
{
}

void CDlgPicCutParam::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_ROW, m_nRow);
	DDV_MinMaxInt(pDX, m_nRow, 1, 20);

	DDX_Text(pDX, IDC_EDIT_COL, m_nCol);
	DDV_MinMaxInt(pDX, m_nCol, 1, 20);

	DDX_Text(pDX, IDC_EDIT_FILEPATH, m_strFilePathDir);
}


BEGIN_MESSAGE_MAP(CDlgPicCutParam, CDialog)
	ON_BN_CLICKED(IDOK, &CDlgPicCutParam::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CDlgPicCutParam::OnBnClickedCancel)
	ON_BN_CLICKED(IDC_BUTTON_SETFILEPATH, &CDlgPicCutParam::OnBnClickedButtonSetfilepath)

	ON_CONTROL_RANGE(EN_CHANGE, IDC_EDIT_ROW, IDC_EDIT_COL, &CDlgPicCutParam::OnEnChangeEdit)

END_MESSAGE_MAP()


// CDlgPicCutParam ��Ϣ�������

void CDlgPicCutParam::OnBnClickedOk()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnOK();
}

void CDlgPicCutParam::OnBnClickedCancel()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnCancel();
}

void CDlgPicCutParam::OnBnClickedButtonSetfilepath()
{
	DWORD dwFlags = OFN_HIDEREADONLY|OFN_PATHMUSTEXIST;
	CFileDialog dlg(true, NULL, NULL, dwFlags, NULL, NULL);

	dlg.DoModal();

	CString filePath = dlg.GetFileName();
	if( filePath.IsEmpty() )
		return;
//	filePath = dlg.GetPathName();
//	int nPos = filePath.ReverseFind('\\');	
//	m_strFilePathDir = filePath.Left(nPos); //ָ���ļ�����Ŀ¼...
	
	m_strFilePathDir = dlg.GetPathName();
	UpdateData(false);
}

BOOL CDlgPicCutParam::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  �ڴ���Ӷ���ĳ�ʼ��

	return TRUE;  // return TRUE unless you set the focus to a control
	// �쳣: OCX ����ҳӦ���� FALSE
}

void CDlgPicCutParam::OnEnChangeEdit(UINT nID)
{
	// TODO:  ����ÿؼ��� RICHEDIT �ؼ�������������
	// ���͸�֪ͨ��������д CDialog::OnInitDialog()
	// ���������� CRichEditCtrl().SetEventMask()��
	// ͬʱ�� ENM_CHANGE ��־�������㵽�����С�

	// TODO:  �ڴ���ӿؼ�֪ͨ����������

	UpdateData(true);
	m_nRow = m_nRow>20 ? 20:m_nRow;
	m_nRow = m_nRow<1 ? 1:m_nRow;

	m_nCol = m_nCol>20 ? 20:m_nCol;
	m_nCol = m_nCol<1 ? 1:m_nCol;

	UpdateData(false);
}
