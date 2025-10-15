// DlgPicCutParam.cpp : 实现文件
//

#include "stdafx.h"
#include "ParticleSystemEditor.h"
#include "DlgPicCutParam.h"


// CDlgPicCutParam 对话框

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


// CDlgPicCutParam 消息处理程序

void CDlgPicCutParam::OnBnClickedOk()
{
	// TODO: 在此添加控件通知处理程序代码
	OnOK();
}

void CDlgPicCutParam::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
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
//	m_strFilePathDir = filePath.Left(nPos); //指定文件所在目录...
	
	m_strFilePathDir = dlg.GetPathName();
	UpdateData(false);
}

BOOL CDlgPicCutParam::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  在此添加额外的初始化

	return TRUE;  // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}

void CDlgPicCutParam::OnEnChangeEdit(UINT nID)
{
	// TODO:  如果该控件是 RICHEDIT 控件，则它将不会
	// 发送该通知，除非重写 CDialog::OnInitDialog()
	// 函数并调用 CRichEditCtrl().SetEventMask()，
	// 同时将 ENM_CHANGE 标志“或”运算到掩码中。

	// TODO:  在此添加控件通知处理程序代码

	UpdateData(true);
	m_nRow = m_nRow>20 ? 20:m_nRow;
	m_nRow = m_nRow<1 ? 1:m_nRow;

	m_nCol = m_nCol>20 ? 20:m_nCol;
	m_nCol = m_nCol<1 ? 1:m_nCol;

	UpdateData(false);
}
