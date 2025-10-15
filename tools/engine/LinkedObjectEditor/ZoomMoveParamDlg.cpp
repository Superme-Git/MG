// ZoomMoveParamDlg.cpp : ʵ���ļ�
//

#include "stdafx.h"
#include "LinkedObjectEditor.h"
#include "ZoomMoveParamDlg.h"


// CZoomMoveParamDlg �Ի���

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


// CZoomMoveParamDlg ��Ϣ�������

void CZoomMoveParamDlg::OnBnClickedOk()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnOK();
}

void CZoomMoveParamDlg::OnBnClickedCancel()
{
	// TODO: �ڴ���ӿؼ�֪ͨ����������
	OnCancel();
}

void CZoomMoveParamDlg::OnEnKillFocusEdit(UINT nID)
{
	// TODO:  ����ÿؼ��� RICHEDIT �ؼ�������������
	// ���͸�֪ͨ��������д CDialog::OnInitDialog()
	// ���������� CRichEditCtrl().SetEventMask()��
	// ͬʱ�� ENM_CHANGE ��־�������㵽�����С�
	// TODO:  �ڴ���ӿؼ�֪ͨ����������
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

	// TODO:  �ڴ���Ӷ���ĳ�ʼ��

	GetDlgItem(IDC_EDIT_ZOOM)->EnableWindow(m_bEnableZoomParam);
	GetDlgItem(IDC_EDIT_TIME)->EnableWindow(m_bEnableZoomParam);

	return TRUE;  // return TRUE unless you set the focus to a control
	// �쳣: OCX ����ҳӦ���� FALSE
}
