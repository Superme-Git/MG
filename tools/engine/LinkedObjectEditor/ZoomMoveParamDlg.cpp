// ZoomMoveParamDlg.cpp : 实现文件
//

#include "stdafx.h"
#include "LinkedObjectEditor.h"
#include "ZoomMoveParamDlg.h"


// CZoomMoveParamDlg 对话框

IMPLEMENT_DYNAMIC(CZoomMoveParamDlg, CDialog)

CZoomMoveParamDlg::CZoomMoveParamDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CZoomMoveParamDlg::IDD, pParent)
	, m_fZoom(2.0f)
	, m_fTime(5.0f)
	, m_bEnableZoomParam(FALSE)
{
}

CZoomMoveParamDlg::~CZoomMoveParamDlg()
{
}

void CZoomMoveParamDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_ZOOM, m_fZoom);
	DDX_Text(pDX, IDC_EDIT_TIME, m_fTime);
	DDX_Check(pDX, IDC_CHECK_ZOOMENABLE, m_bEnableZoomParam);
}


BEGIN_MESSAGE_MAP(CZoomMoveParamDlg, CDialog)
	ON_BN_CLICKED(IDOK, &CZoomMoveParamDlg::OnBnClickedOk)
	ON_BN_CLICKED(IDCANCEL, &CZoomMoveParamDlg::OnBnClickedCancel)

	ON_CONTROL_RANGE(EN_KILLFOCUS, IDC_EDIT_ZOOM, IDC_EDIT_TIME, &CZoomMoveParamDlg::OnEnKillFocusEdit)

	ON_BN_CLICKED(IDC_CHECK_ZOOMENABLE, &CZoomMoveParamDlg::OnBnClickedCheckZoomenable)
END_MESSAGE_MAP()


// CZoomMoveParamDlg 消息处理程序

void CZoomMoveParamDlg::OnBnClickedOk()
{
	// TODO: 在此添加控件通知处理程序代码
	OnOK();
}

void CZoomMoveParamDlg::OnBnClickedCancel()
{
	// TODO: 在此添加控件通知处理程序代码
	OnCancel();
}

void CZoomMoveParamDlg::OnEnKillFocusEdit(UINT nID)
{
	// TODO:  如果该控件是 RICHEDIT 控件，则它将不会
	// 发送该通知，除非重写 CDialog::OnInitDialog()
	// 函数并调用 CRichEditCtrl().SetEventMask()，
	// 同时将 ENM_CHANGE 标志“或”运算到掩码中。
	// TODO:  在此添加控件通知处理程序代码
	UpdateData(true);
}
void CZoomMoveParamDlg::OnBnClickedCheckZoomenable()
{
	UpdateData(true);
	GetDlgItem(IDC_EDIT_ZOOM)->EnableWindow(m_bEnableZoomParam);
	GetDlgItem(IDC_EDIT_TIME)->EnableWindow(m_bEnableZoomParam);
}

BOOL CZoomMoveParamDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  在此添加额外的初始化

	GetDlgItem(IDC_EDIT_ZOOM)->EnableWindow(m_bEnableZoomParam);
	GetDlgItem(IDC_EDIT_TIME)->EnableWindow(m_bEnableZoomParam);

	return TRUE;  // return TRUE unless you set the focus to a control
	// 异常: OCX 属性页应返回 FALSE
}
