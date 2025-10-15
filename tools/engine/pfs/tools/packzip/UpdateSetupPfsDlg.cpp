// UpdateSetupPfsDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "UpdateSetupPfsDlg.h"
#include "SaveProgressDlg.h"
#include "EditVersionDlg.h"

// CUpdateSetupPfsDlg dialog
const std::wstring SRCSETUP = L"/srcsetup";
const std::wstring UPDATE = L"/setup";

void CUpdateSetupPfsOperator::Init()
{
	if( m_updatePfsFile.empty())
	{
		return;
	}

	std::wstring str;
	if( m_listener )
	{	
		str = L"开始分析合并补丁包！- " + m_updatePfsFile;
		m_listener->OnNotify( (WPARAM)str.c_str(), COperator::PT_FILENAME);
	}
	if( !PFS::CEnv::Mount( UPDATE, m_updatePfsFile, PFS::FST_ZIP, PFS::MT_READONLY))
	{
		if( m_listener )
		{	
			str = L"原安装包mount失败！- " + m_pfsFile;
			m_listener->OnNotify( (WPARAM)str.c_str(), COperator::PT_FILEERROR);
		}
		throw 0;
	}
}

void CUpdateSetupPfsOperator::Update()
{
	if( m_updatePfsFile.empty())
	{
		return;
	}

	std::wstring str;

	PFS::CMetaInfoMap mim;
	PFS::CEnv::GetMetaInfos( UPDATE, mim);

	if( m_listener)
	{
		m_listener->OnNotify( mim.size(), COperator::PT_TOTALCOUNT);
	}
	for( PFS::CMetaInfoMap::iterator i=mim.begin(), e=mim.end(); i!=e; ++i)
	{
		const std::wstring& file = i->first;
		if( (0 != wcscmp( file.c_str(), L"/.version.meta")) 
			&& (0 != wcscmp( file.c_str(), L"/.setup.meta")))
		{
			if( !PFS::CEnv::FileCopy( UPDATE+file, SRCSETUP+file, false))
			{
				if( m_listener )
				{	
					str = L"更新文件失败！- " + file;
					m_listener->OnNotify( (WPARAM)str.c_str(), COperator::PT_FILEERROR);
					throw 0;
				}
			}
		}

		if( m_listener)
		{
			m_listener->OnNotify( 1, COperator::PT_TOTALPROGRESS);
		}
	}
}

void CUpdateSetupPfsOperator::UpdateVersionInfo()
{
	if( !m_bChangeVersion)
	{
		return;
	}

	if( m_listener)
	{
		m_listener->OnNotify( (WPARAM)L"更新.version.meta文件", COperator::PT_FILENAME);
	}

	PFS::CFile file;
	if( !file.Open( SRCSETUP + L"/.version.meta", PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
	{
		if( m_listener)
		{
			m_listener->OnNotify( (WPARAM)L"打开安装包 .version.meta文件出错！", COperator::PT_FILEERROR);	
		}
		throw 0;
	}
	PFSX::CVersionInfo& versionInfo = m_project.GetVersionMeta();
	if( !versionInfo.SaveTo( file))
	{
		if( m_listener)
		{
			m_listener->OnNotify( (WPARAM)L"保存安装包 .version.meta文件出错！", COperator::PT_FILEERROR);	
		}
		throw 0;
	}
}

bool CUpdateSetupPfsOperator::MakeMd5()
{
	std::wstring str;
	if( m_listener)
	{
		str = L"生成安装包md5值！- "  + m_pfsFile;
		m_listener->OnNotify( (WPARAM)str.c_str(), COperator::PT_FILENAME);
	}
	PFSX::CNativeFile nf;
	if( !nf.Open( m_pfsFile, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		if( m_listener)
		{
			str = L"只读方式打开安装包失败！- " + m_pfsFile;
			m_listener->OnNotify( (WPARAM)str.c_str(), COperator::PT_FILEERROR);
		}
		return false;
	}

	PFS::CDataBlock buffer;
	PFS::helper::CMD5FileHashMethod md5Method;
	if( !md5Method.MakeFileHash( nf, buffer))
	{
		if( m_listener)
		{
			m_listener->OnNotify( (WPARAM)L"生成安装包md5值失败！", COperator::PT_FILEERROR);
		}
		return false;
	}

	std::wstring md5file = m_pfsFile;
	md5file += L".md5";
	PFSX::CNativeFile nfMd5;
	if( !nfMd5.Open( md5file, PFS::FM_CREAT|PFS::FM_TRUNC, PFS::FA_WRONLY))
	{
		if( m_listener)
		{
			m_listener->OnNotify( (WPARAM)L"保存安装包md5值文件失败！", COperator::PT_FILEERROR);
		}
		return false;
	}
	std::string md5Str;
	Data2AnsiString( buffer, md5Str);
	nfMd5.Write( md5Str.c_str(), md5Str.size());
	//nfMd5.Write( buffer.GetData(), buffer.GetSize());

	return true;
}

CZipTreeItem* CUpdateSetupPfsOperator::Do()
{
	bool res = true;
	try
	{
		Init();
		Update();
		UpdateVersionInfo();
	}
	catch( int )
	{
		res = false;
	}
	PFS::CEnv::Unmount( SRCSETUP);
	PFS::CEnv::Unmount( UPDATE);

	if( res && m_bMakeMd5)
	{
		MakeMd5();
	}
	
	return 0;
}

IMPLEMENT_DYNAMIC(CUpdateSetupPfsDlg, CDialog)

CUpdateSetupPfsDlg::CUpdateSetupPfsDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CUpdateSetupPfsDlg::IDD, pParent)
{

}

CUpdateSetupPfsDlg::~CUpdateSetupPfsDlg()
{
}

void CUpdateSetupPfsDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_EDIT_PFSFILE, m_editPfsFile);
	DDX_Control(pDX, IDC_BUTTON_OPENPFS, m_btnOpenPfs);
	DDX_Control(pDX, IDC_EDIT_UPDATEPFSFILE, m_editUpdatePfsFile);
	DDX_Control(pDX, IDC_BUTTON_OPENUPDATEPFS, m_btnOpenUpdatePfs);
	DDX_Control(pDX, IDC_CHECK_MAKEMD5, m_checkMakeMd5);
}


BEGIN_MESSAGE_MAP(CUpdateSetupPfsDlg, CDialog)
	ON_BN_CLICKED(IDC_BUTTON_OPENPFS, &CUpdateSetupPfsDlg::OnBnClickedButtonOpenpfs)
	ON_BN_CLICKED(IDC_BUTTON_OPENUPDATEPFS, &CUpdateSetupPfsDlg::OnBnClickedButtonOpenupdatepfs)
END_MESSAGE_MAP()


// CUpdateSetupPfsDlg message handlers

void CUpdateSetupPfsDlg::OnOK()
{
	// TODO: Add your specialized code here and/or call the base class
	CString str;
	m_editPfsFile.GetWindowText( str);
	if( str.IsEmpty())
	{
		AfxMessageBox( L"清先选择安装包路径！");
		return;
	}
	CString strTemp;
	strTemp.Format( L"接下来的操作会修改安装包文件 - %s，要继续么？", str);
	if( IDNO == AfxMessageBox( strTemp, MB_YESNO))
	{
		return;
	}

	AfxGetApp()->BeginWaitCursor();

	std::wstring fileName = str.GetString();
	if( !PFS::CEnv::Mount( SRCSETUP, fileName, PFS::FST_ZIP, PFS::MT_WRITABLE))
	{
		std::wostringstream woss;
		woss << fileName << L" 文件mount失败！";
		AfxMessageBox( woss.str().c_str());
		AfxGetApp()->EndWaitCursor();
		return;
	}

	AfxGetApp()->EndWaitCursor();

	PFSX::CVersionInfo lastVersionInfo;
	if( !PFSX::GetLocalVersion( SRCSETUP, lastVersionInfo))
	{
		std::wostringstream woss;
		woss << fileName << L" 文件中获得版本信息出错！";
		return;
	}
	// 提示编辑版本
	CProject project;
	project.SetVersionMeta( lastVersionInfo);
	CEditVersionDlg dlgVersion;
	dlgVersion.OpenProject( &project);
	dlgVersion.DoModal();
	
	CString strUpdate;
	m_editUpdatePfsFile.GetWindowText( strUpdate);
	bool bCheck = ( BST_CHECKED == m_checkMakeMd5.GetCheck());

	CZipTreeItem temp;
	COperator::COperatorVector ov;
	CUpdateSetupPfsOperator* uspo = new CUpdateSetupPfsOperator( temp, project, str.GetString(), 
		strUpdate.GetString(), bCheck, dlgVersion.m_changed);
	ov.push_back( uspo );

	CSaveProgressDlg dlgProgress( false);
	dlgProgress.SetOperators( ov);
	dlgProgress.DoModal( );
	
	delete uspo;
	//CDialog::OnOK();
}

void CUpdateSetupPfsDlg::OnBnClickedButtonOpenpfs()
{
	// TODO: Add your control notification handler code here
	wchar_t szFilter[] = L"pfs file|*.pfs|all files|*.*|";
	CFileDialog dlgFile( TRUE, L"pfs", m_lastPfsFile.c_str(), OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, szFilter);
	if( IDCANCEL == dlgFile.DoModal())
		return;
	
	CString str = dlgFile.GetPathName();
	m_lastPfsFile = str.GetString();
	m_editPfsFile.SetWindowText( str);
}

void CUpdateSetupPfsDlg::OnBnClickedButtonOpenupdatepfs()
{
	// TODO: Add your control notification handler code here
	wchar_t szFilter[] = L"pfs file|*.pfs|all files|*.*|";
	CFileDialog dlgFile( TRUE, L"pfs", m_lastUpdatePfsFile.c_str(), OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT, szFilter);
	if( IDCANCEL == dlgFile.DoModal())
		return;

	CString str = dlgFile.GetPathName();
	m_lastUpdatePfsFile = str.GetString();
	m_editUpdatePfsFile.SetWindowText( str);
}
