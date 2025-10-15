// RecentProjectDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "RecentProjectDlg.h"
#include "MainFrm.h"
#include "RecentProject.h"

// CRecentProjectDlg dialog

IMPLEMENT_DYNAMIC(CRecentProjectDlg, CDialog)

CRecentProjectDlg::CRecentProjectDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CRecentProjectDlg::IDD, pParent)
{

}

CRecentProjectDlg::~CRecentProjectDlg()
{
}

void CRecentProjectDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST1, m_recents);
}


BEGIN_MESSAGE_MAP(CRecentProjectDlg, CDialog)
	ON_WM_CLOSE()
	ON_BN_CLICKED(IDC_BUTTON_OPEN, &CRecentProjectDlg::OnBnClickedButtonOpen)
	ON_BN_CLICKED(IDC_BUTTON_NEW, &CRecentProjectDlg::OnBnClickedButtonNew)
	ON_LBN_DBLCLK(IDC_LIST1, &CRecentProjectDlg::OnLbnDblclkList1)
END_MESSAGE_MAP()

// CRecentProjectDlg message handlers

BOOL CRecentProjectDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	OpenProject();

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void ResetListHorizontalExtent( CListBox& rListBox )
{
	// Find the longest string in the list box.
	CString str;
	CSize sz;
	int dx = 0;
	TEXTMETRIC tm;
	CDC* pDC = rListBox.GetDC();
	CFont* pFont = rListBox.GetFont();

	// Select the listbox font, save the old font
	CFont* pOldFont = pDC->SelectObject(pFont);
	// Get the text metrics for avg char width
	pDC->GetTextMetrics(&tm); 

	for (int i = 0; i < rListBox.GetCount(); i++)
	{
		rListBox.GetText(i, str);
		sz = pDC->GetTextExtent(str);

		// Add the avg width to prevent clipping
		sz.cx += tm.tmAveCharWidth;

		if (sz.cx > dx)
			dx = sz.cx;
	}
	// Select the old font back into the DC
	pDC->SelectObject(pOldFont);
	rListBox.ReleaseDC(pDC);

	// Set the horizontal extent so every character of all strings 
	// can be scrolled to.
	rListBox.SetHorizontalExtent(dx);
}

void CRecentProjectDlg::OpenProject()
{
	m_recents.ResetContent();
	std::vector<std::wstring>& projects = CRecentProject::GetInstance().GetProjects();
	for( std::vector<std::wstring>::iterator itor = projects.begin(); itor != projects.end(); ++itor)
		m_recents.InsertString( 0, itor->c_str());
	m_recents.SetCurSel( 0);
	
	// ListBox 不能自动调整水平滚动条，我们需要自己计算
	ResetListHorizontalExtent( m_recents ); 
}

void CRecentProjectDlg::OnBnClickedButtonOpen()
{
	int sel = m_recents.GetCurSel();
	if( sel < 0 )
	{
		// 如果没有选中（recent project list 为空）时从目录查找
		AfxGetMainWnd()->PostMessage( WM_COMMAND, ID_FILE_OPEN, 0 );
		return;
	}

	CString str;
	m_recents.GetText( sel, str);
	CMainFrame* pFrame = dynamic_cast<CMainFrame*>( AfxGetMainWnd());
	if( pFrame)
	{
		std::wstring select = str.GetString();
		if( !pFrame->OpenProjectFile( select))
		{
			str.Format( L"%s 无法打开，是否要从\"最近工程列表\"中删除？", select.c_str());
			if( IDYES == AfxMessageBox( str, MB_YESNO))
			{
				CRecentProject::GetInstance().DelProject( select);
				int index = m_recents.GetCurSel();
				m_recents.DeleteString( index);
				m_recents.SetCurSel( --index < 0 ? 0 : index);
			}
		}
	}
}

void CRecentProjectDlg::OnBnClickedButtonNew()
{
	AfxGetMainWnd()->PostMessage( WM_COMMAND, ID_FILE_NEW, 0 );
}

void CRecentProjectDlg::OnLbnDblclkList1()
{
	OnBnClickedButtonOpen();
}
