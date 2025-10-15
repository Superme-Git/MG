// MakeHashTableDlg.cpp : implementation file
//

#include "stdafx.h"
#include "WinPFS.h"
#include "MakeHashTableDlg.h"
#include "ThreadStateList.h"

// CMakeHashTableDlg dialog

IMPLEMENT_DYNAMIC(CMakeHashTableDlg, CDialog)

CMakeHashTableDlg::CMakeHashTableDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CMakeHashTableDlg::IDD, pParent)
	, m_thread( NULL)
{

}

CMakeHashTableDlg::~CMakeHashTableDlg()
{
}

void CMakeHashTableDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_EDIT_MOUNTNAME, m_editName);
	DDX_Control(pDX, IDC_CHECK1, m_btnOuputFileName);
	DDX_Control(pDX, IDC_LIST_RESULT, m_list);
}


BEGIN_MESSAGE_MAP(CMakeHashTableDlg, CDialog)
	ON_MESSAGE( WM_THREADSTATE, OnCheckResult)
END_MESSAGE_MAP()


// CMakeHashTableDlg message handlers

void CMakeHashTableDlg::OnOK()
{
	// TODO: Add your control notification handler code here
	//OnOK();

	CString str;
	m_editName.GetWindowText( str);
	if( str.IsEmpty())
	{
		AfxMessageBox( L"mount路径不能为空！");
		m_editName.SetFocus();
		return;
	}

	wchar_t szFilter[] = L"xml file|*.xml|all files|*.*|";
	CFileDialog dlgFile( FALSE, L"xml", m_pfsName.c_str(), OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, szFilter);
	if (IDCANCEL == dlgFile.DoModal())
	{
		return;
	}

	m_list.DeleteAllItems();

	m_thread = (CMakeHashTableThread*)AfxBeginThread( RUNTIME_CLASS(CMakeHashTableThread),
		THREAD_PRIORITY_NORMAL, 0, CREATE_SUSPENDED, NULL);
	if( m_thread)
	{
		m_thread->SetDocument( m_doc);
		m_thread->SetWnd( this);
		m_thread->SetOutputFileName( m_btnOuputFileName.GetCheck() == BST_CHECKED);
		m_thread->SetMountName( str.GetString());
		m_thread->SetSaveFileName( dlgFile.GetPathName().GetString());
		m_thread->ResumeThread();

		GetDlgItem(IDOK)->EnableWindow( FALSE);
	}
	else
	{
		AfxMessageBox( L"启动验证线程失败！");
	}	
}

BOOL CMakeHashTableDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  Add extra initialization here
	m_list.InsertColumn( 0, L"文件名", LVCFMT_LEFT, 320);
	m_list.InsertColumn( 1, L"状态", LVCFMT_LEFT, 80);

	m_btnOuputFileName.SetCheck( BST_CHECKED);
	if( m_doc)
	{
		std::wstring name = m_doc->GetPathName().GetString();
		std::wstring::size_type start = name.rfind( L'\\') + 1;
		std::wstring::size_type end = name.find( L'.', start);
		m_pfsName = name.substr( start, end-start);
	}
	CString temp( L"/");
	temp += m_pfsName.c_str();
	m_editName.SetWindowText( temp);

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

LRESULT CMakeHashTableDlg::OnCheckResult( WPARAM wParam, LPARAM lParam)
{
	if( END == lParam)
	{
		m_thread = NULL;
		GetDlgItem(IDOK)->EnableWindow( TRUE);
		if( wParam)
		{
			AfxMessageBox( L"生成完成！");
		}
		else
		{
			AfxMessageBox( L"生成失败！");
		}
	}
	else
	{
		CThreadStateList::Item cr;
		CThreadStateList::GetInstance().Pop( cr);
		if( !cr.m_name.empty())
		{
			int index = m_list.InsertItem( m_list.GetItemCount(), cr.m_name.c_str());
			m_list.SetItemText( index, 1, L"ok");
			
			m_list.EnsureVisible( index, FALSE);
		}
	}
	return 0;
}


void CMakeHashTableDlg::OnCancel()
{
	// TODO: Add your specialized code here and/or call the base class
	if( m_thread && m_thread->IsRunning())
	{
		if( IDNO == AfxMessageBox( L"正在生成文件hash表，确认要取消么？", MB_YESNO|MB_DEFBUTTON2))
		{
			return;
		}
		m_thread->Stop();
		CThreadStateList::GetInstance().Clear();
	}

	CDialog::OnCancel();
}
