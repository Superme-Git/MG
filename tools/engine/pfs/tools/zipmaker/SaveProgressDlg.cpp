// SaveProgressDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "SaveProgressDlg.h"

// CSaveProgressDlg dialog

IMPLEMENT_DYNAMIC(CSaveProgressDlg, CDialog)

CSaveProgressDlg::CSaveProgressDlg( bool bOpen, CWnd* pParent /*=NULL*/)
: CDialog( CSaveProgressDlg::IDD, pParent)
, m_bOpen( bOpen)
, m_scale( 1.0f)
{

}

CSaveProgressDlg::~CSaveProgressDlg()
{
	
}

void CSaveProgressDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_PROGRESS, m_progress);
	DDX_Control(pDX, IDC_EDIT1, m_editProgress);
	DDX_Control(pDX, IDC_EDIT2, m_editFile);
}


BEGIN_MESSAGE_MAP(CSaveProgressDlg, CDialog)
	//ON_BN_CLICKED(IDCANCEL, &CSaveProgressDlg::OnBnClickedCancel)
	ON_MESSAGE( WM_SAVEPROGRESS, OnSaveProgress)
END_MESSAGE_MAP()


// CSaveProgressDlg message handlers

BOOL CSaveProgressDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  Add extra initialization here
	if( m_bOpen)
	{
		CRect rc;
		GetWindowRect( &rc);
		rc.bottom -= 60;
		MoveWindow( &rc);
	}

	m_listener.SetHWnd( m_hWnd);
	m_thread.SetListener( &m_listener);
	COperator::m_bStop = false;
	m_thread.Start();

	KHMsgLog_Info( L"Start!\r\n" );
	
	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

HRESULT CSaveProgressDlg::OnSaveProgress( WPARAM wParam, LPARAM lParam)
{
	switch( lParam)
	{
	case COperator::PT_TOTALCOUNT:
		m_writtencount = 0;
		m_allfilecount = wParam;
		if( m_allfilecount)
			m_scale = 100.0f / m_allfilecount;
		m_progress.SetRange( 0, 100);
		m_progress.SetPos( 0);
		break;
	case COperator::PT_TOTALPROGRESS:
		{
			m_writtencount += wParam;
			m_progress.SetPos( static_cast<int>( m_writtencount*m_scale));

			CString str;
			str.Format( L"%d / %d", m_writtencount, m_allfilecount);
			m_editProgress.SetWindowText( str);
		}
		break;
	case COperator::PT_TOTALEND:
		//PostMessage( WM_CLOSE);
		CDialog::OnCancel();
		KHMsgLog_Info( L"End!\r\n" );
		break;
	case COperator::PT_FILENAME:
		m_editFile.SetWindowText( reinterpret_cast<LPCTSTR>( wParam));
		break;
	case COperator::PT_ADDEND:
	case COperator::PT_DELETEEND:
	case COperator::PT_ADDEND_PROJECT:
	case COperator::PT_DELETEEND_PROJECT:
	case COperator::PT_SETCHECKED:
		AfxGetMainWnd()->SendMessage( WM_OPERATOREND, wParam, lParam);
		break;
	case COperator::PT_FILEERROR:
		{
			std::wstring err;
			CErrorStringArray::GetInstance().Pop( err);
			if( !err.empty())
				KHMsgLog_Error( L" \"%s\" \r\n", err.c_str());
		}
		break;
	}
	return 0;
}

void CSaveProgressDlg::OnCancel()
{
	if( IDNO == AfxMessageBox( L"正在处理文件，是否确认要取消？", MB_YESNO|MB_DEFBUTTON2))
		return;

	COperator::m_bStop = true;
	m_thread.Stop();
	CErrorStringArray::GetInstance().Clear();

	KHMsgLog_Info( L"User canceled!\r\n" );

	CDialog::OnCancel();
}

void CSaveProgressDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class
	//__super::OnOK();
}
