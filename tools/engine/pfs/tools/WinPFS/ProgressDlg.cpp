// ProgressDlg.cpp : implementation file
//

#include "stdafx.h"
#include "WinPFS.h"
#include "ProgressDlg.h"
#include "ThreadStateList.h"

// CProgressDlg dialog

IMPLEMENT_DYNAMIC(CProgressDlg, CDialog)

CProgressDlg::CProgressDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CProgressDlg::IDD, pParent)
{

}

CProgressDlg::~CProgressDlg()
{
}

void CProgressDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_STATIC_PROMPT, m_prompt);
	DDX_Control(pDX, IDC_PROGRESS1, m_progress);
}


BEGIN_MESSAGE_MAP(CProgressDlg, CDialog)
	ON_MESSAGE( WM_THREADSTATE, OnThreadState)
	ON_MESSAGE( WM_THREADPROGRESS, OnThreadProgress)
END_MESSAGE_MAP()


// CProgressDlg message handlers

LRESULT CProgressDlg::OnThreadState( WPARAM wParam, LPARAM lParam)
{
	if( END == lParam)
	{
		ShowWindow( SW_HIDE);
	}
	else
	{
		CThreadStateList::Item cr;
		CThreadStateList::GetInstance().Pop( cr);
		if( !cr.m_name.empty())
		{
			m_prompt.SetWindowText( cr.m_name.c_str());
		}
	}
	return 0;
}

LRESULT CProgressDlg::OnThreadProgress( WPARAM wParam, LPARAM lParam)
{
	switch( lParam)
	{
	case INIT:
		m_progress.SetRange32( 0, wParam);
		m_curPos = 0;
		break;
	case PROGRESS:
		m_curPos += wParam;
		m_progress.SetPos( m_curPos);
		break;
	}
	return 0;
}

void CProgressDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class

	//CDialog::OnOK();
}

void CProgressDlg::OnCancel()
{
	// TODO: Add your specialized code here and/or call the base class
	if( IDNO == AfxMessageBox( L"数据正在处理中，确认要取消么？", MB_YESNO|MB_DEFBUTTON2))
	{
		return;
	}
	ShowWindow( SW_HIDE);
}
