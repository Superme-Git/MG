// ValidateFileDlg.cpp : implementation file
//

#include "stdafx.h"
#include "DXTest.h"
#include "ValidateFileDlg.h"


// CValidateFileDlg dialog

IMPLEMENT_DYNAMIC(CValidateFileDlg, CDialog)

CValidateFileDlg::CValidateFileDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CValidateFileDlg::IDD, pParent)
	, m_loader( NULL)
{

}

CValidateFileDlg::~CValidateFileDlg()
{
}

void CValidateFileDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST1, m_lcFile);
}


BEGIN_MESSAGE_MAP(CValidateFileDlg, CDialog)
END_MESSAGE_MAP()


// CValidateFileDlg message handlers

static bool IsFileExisting( const std::wstring& filename)
{
	DWORD	dwAttribute = GetFileAttributesW( filename.c_str());
	if( INVALID_FILE_ATTRIBUTES == dwAttribute)
		return false;
	return FILE_ATTRIBUTE_DIRECTORY != ( dwAttribute & FILE_ATTRIBUTE_DIRECTORY);
}

// 刷新文件状态
void CValidateFileDlg::RefreshFileState()
{
	m_lcFile.DeleteAllItems();
	if( NULL == m_loader)
	{
		return;
	}
	std::wstring svgPath;
	CShowPartLoader::GetSvgFullPath( svgPath);
	std::wstring tgaPath;
	CShowPartLoader::GetTgaFullPath( tgaPath);

	TShowPartContainer& tspc = m_loader->GetAllParts();
	for( TShowPartContainer::iterator i=tspc.begin(), e=tspc.end(); i!=e; ++i)
	{
		ShowPartItem& spi = *i;
		if( spi.ulID < CHANGED_PART_MIN_ID)
		{
			continue;
		}
		bool bSvg = false, bTga = false;
		bSvg = IsFileExisting( svgPath + spi.strSvg);
		bTga = IsFileExisting( tgaPath + spi.strTga);
		if( bSvg && bTga)
		{
			// 文件都正确
			continue;
		}

		std::wostringstream woss;
		woss << spi.ulID;
		int index = m_lcFile.InsertItem( m_lcFile.GetItemCount(), woss.str().c_str());
		m_lcFile.SetItemText( index, 1, spi.name.c_str());
		m_lcFile.SetItemText( index, 2, spi.strSvg.c_str());
		if( bSvg)
		{
			m_lcFile.SetItemText( index, 3, L"ok");
		}
		else
		{
			m_lcFile.SetItemText( index, 3, L"不存在");
		}
		m_lcFile.SetItemText( index, 4, spi.strTga.c_str());
		if( bTga)
		{
			m_lcFile.SetItemText( index, 5, L"ok");
		}
		else
		{
			m_lcFile.SetItemText( index, 5, L"不存在");
		}
	}
}

BOOL CValidateFileDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// TODO:  Add extra initialization here
	m_lcFile.SetExtendedStyle( LVS_EX_FULLROWSELECT);
	m_lcFile.InsertColumn( 0,		L"id",				LVCFMT_LEFT,	80);
	m_lcFile.InsertColumn( 1,		L"名字",			LVCFMT_LEFT,	80);
	m_lcFile.InsertColumn( 2,		L"svg文件名",		LVCFMT_LEFT,	140);
	m_lcFile.InsertColumn( 3,		L"svg文件状态",		LVCFMT_LEFT,	80);
	m_lcFile.InsertColumn( 4,		L"tga文件名",		LVCFMT_LEFT,	140);
	m_lcFile.InsertColumn( 5,		L"tga文件状态",		LVCFMT_LEFT,	80);

	RefreshFileState();

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CValidateFileDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class

	//CDialog::OnOK();
	AfxGetApp()->BeginWaitCursor();

	RefreshFileState();

	AfxGetApp()->EndWaitCursor();
}
