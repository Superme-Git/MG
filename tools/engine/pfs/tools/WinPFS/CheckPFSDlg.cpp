// CheckPFSDlg.cpp : implementation file
//

#include "stdafx.h"
#include "WinPFS.h"
#include "CheckPFSDlg.h"
#include "ThreadStateList.h"

// CCheckPFSDlg dialog

IMPLEMENT_DYNAMIC(CCheckPFSDlg, CDialog)

CCheckPFSDlg::CCheckPFSDlg(CWnd* pParent /*=NULL*/)
: CDialog(CCheckPFSDlg::IDD, pParent)
, m_doc( NULL)
, m_thread( NULL)
{

}

CCheckPFSDlg::~CCheckPFSDlg()
{
}

void CCheckPFSDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST_RESULT, m_list);
}


BEGIN_MESSAGE_MAP(CCheckPFSDlg, CDialog)
	ON_MESSAGE( WM_THREADSTATE, OnCheckResult)
END_MESSAGE_MAP()


// CCheckPFSDlg message handlers

void CCheckPFSDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class

	//CDialog::OnOK();
	m_list.DeleteAllItems();

	m_thread = (CCheckPFSThread*)AfxBeginThread( RUNTIME_CLASS(CCheckPFSThread),
		THREAD_PRIORITY_NORMAL, 0, CREATE_SUSPENDED, NULL);
	if( m_thread)
	{
		m_thread->SetDocument( m_doc);
		m_thread->SetWnd( this);
		m_thread->ResumeThread();

		GetDlgItem(IDOK)->EnableWindow( FALSE);
	}
	else
	{
		AfxMessageBox( L"启动验证线程失败！");
	}
}

BOOL CCheckPFSDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  Add extra initialization here
	m_list.InsertColumn( 0, L"文件名", LVCFMT_LEFT, 220);
	m_list.InsertColumn( 1, L"crc校验", LVCFMT_LEFT, 80);
	m_list.InsertColumn( 2, L"md5校验", LVCFMT_LEFT, 80);

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

LPCTSTR g_result[] = {
L"ok",
L"不正确",
L"未找到",
L"未知错误",
};

LRESULT CCheckPFSDlg::OnCheckResult( WPARAM wParam, LPARAM lParam)
{
	if( END ==lParam)
	{
		m_thread = NULL;
		GetDlgItem(IDOK)->EnableWindow( TRUE);
		AfxMessageBox( L"验证完成！");
	}
	else
	{
		CThreadStateList::Item cr;
		CThreadStateList::GetInstance().Pop( cr);
		if( !cr.m_name.empty())
		{
			int crcResult = LOWORD( cr.m_state);
			int md5Result = HIWORD( cr.m_state);
			int index = m_list.InsertItem( m_list.GetItemCount(), cr.m_name.c_str());
			if( crcResult >= 0 && crcResult < 3)
			{
				m_list.SetItemText( index, 1, g_result[crcResult]);
			}
			else
			{
				m_list.SetItemText( index, 1, g_result[3]);
			}
			if( md5Result >= 0 && md5Result < 3)
			{
				m_list.SetItemText( index, 2, g_result[md5Result]);
			}
			else
			{
				m_list.SetItemText( index, 2, g_result[3]);
			}
			m_list.EnsureVisible( index, FALSE);
		}
	}
	return 0;
}

void CCheckPFSDlg::OnCancel()
{
	if( m_thread && m_thread->IsRunning())
	{
		if( IDNO == AfxMessageBox( L"验证过程正在进行中，确认要取消么？", MB_YESNO|MB_DEFBUTTON2))
		{
			return;
		}
		m_thread->Stop();
		CThreadStateList::GetInstance().Clear();
	}

	CDialog::OnCancel();
}
