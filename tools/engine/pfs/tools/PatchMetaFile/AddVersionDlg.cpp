// AddVersionDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PatchMetaFile.h"
#include "AddVersionDlg.h"
#include <string>
// CAddVersionDlg dialog

IMPLEMENT_DYNAMIC(CAddVersionDlg, CDialog)

CAddVersionDlg::CAddVersionDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CAddVersionDlg::IDD, pParent)
	, m_src(_T(""))
	, m_des(_T(""))
	, m_doc( NULL)
	, m_dwDestVersion(0)
{
}

CAddVersionDlg::~CAddVersionDlg()
{
}

void CAddVersionDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_EDIT_SRC, m_editSrc);
	DDX_Control(pDX, IDC_EDIT_DES, m_editDes);
	DDX_Control(pDX, IDC_COMBO1, m_cbPatch);
}


BEGIN_MESSAGE_MAP(CAddVersionDlg, CDialog)
	ON_EN_CHANGE(IDC_EDIT_DES, &CAddVersionDlg::OnEnChangeEditDes)
	ON_EN_CHANGE(IDC_EDIT_SRC, &CAddVersionDlg::OnEnChangeEditSrc)
END_MESSAGE_MAP()


// CAddVersionDlg message handlers
bool Version2DWORD( const std::wstring& src, DWORD& dwVersion);

void CAddVersionDlg::OnOK()
{
	m_cbPatch.GetWindowText( m_patchs);
	if( m_patchs.IsEmpty())
	{
		AfxMessageBox( L"补丁包列表为空，请输入正确的源版本号和目标版本号才能计算出列表！");
		return;
	}

	CDialog::OnOK();
}

BOOL CAddVersionDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	if( m_dwDestVersion != 0 )
	{
		std::wstring versionTo;
		PFSX::Version2String( m_dwDestVersion, versionTo);
		if( m_desInit.IsEmpty() )
			m_desInit = versionTo.c_str();
	}

	if( !m_srcInit.IsEmpty() )
	{
		m_editSrc.SetWindowText( m_srcInit);
	}

	if( !m_desInit.IsEmpty())
	{		
		m_editDes.SetWindowText( m_desInit);
	}

	if( !m_srcInit.IsEmpty() && !m_desInit.IsEmpty())
	{
		OnRefreshPatchList( m_srcInit, m_desInit);
		m_src = m_srcInit;
		m_des = m_desInit;
		m_cbPatch.SetFocus();
	}
	else
	{
		m_editSrc.SetFocus();
	}

	return FALSE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CAddVersionDlg::OnEnChangeEditDes()
{
	// TODO:  If this is a RICHEDIT control, the control will not
	// send this notification unless you override the CDialog::OnInitDialog()
	// function and call CRichEditCtrl().SetEventMask()
	// with the ENM_CHANGE flag ORed into the mask.
	m_editSrc.GetWindowText( m_src);
	m_editDes.GetWindowText( m_des);
	OnRefreshPatchList( m_src, m_des);
}

void CAddVersionDlg::OnEnChangeEditSrc()
{
	// TODO:  If this is a RICHEDIT control, the control will not
	// send this notification unless you override the CDialog::OnInitDialog()
	// function and call CRichEditCtrl().SetEventMask()
	// with the ENM_CHANGE flag ORed into the mask.
	m_editSrc.GetWindowText( m_src);
	m_editDes.GetWindowText( m_des);
	OnRefreshPatchList( m_src, m_des);
}

// 刷新补丁包列表
void CAddVersionDlg::OnRefreshPatchList( const CString& src, const CString& des)
{
	m_cbPatch.ResetContent();

	DWORD srcVersion = 0;
	if( !Version2DWORD( src.GetString(), srcVersion))
	{
		return;
	}

	DWORD desVersion = 0;
	if( !Version2DWORD( des.GetString(), desVersion))
	{
		return;
	}

	if( srcVersion >= desVersion)
	{
		return;
	}

	if( m_doc)
	{
		std::wstring temp;
		std::vector<std::vector<int> > uses;
		m_doc->m_pmf.ListAllPatchsFromVersion( uses, srcVersion, desVersion);
		for( std::vector<std::vector<int> >::const_iterator i=uses.begin(), e=uses.end(); i!=e; ++i)
		{
			const std::vector<int>& use = *i;
			PFSX::GetStrFromIntVector( use, temp);
			m_cbPatch.AddString( temp.c_str());
		}
		m_cbPatch.SetCurSel( 0);
	}
}