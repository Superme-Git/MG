// PatchMetaFileView.cpp : implementation of the CPatchMetaFileView class
//

#include "stdafx.h"
#include "PatchMetaFile.h"

#include "PatchMetaFileDoc.h"
#include "PatchMetaFileView.h"
#include "../../versionmetafile.h"
#include "../../stringhelper.h"
#include "AddVersionDlg.h"
#include <shlwapi.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

const std::wstring PATCH = L"/patch";

inline bool Version2DWORD( const std::wstring& src, DWORD& dwVersion)
{
	return PFSX::String2Version(src, dwVersion );
}

// CPatchMetaFileView

IMPLEMENT_DYNCREATE(CPatchMetaFileView, CFormView)

BEGIN_MESSAGE_MAP(CPatchMetaFileView, CFormView)
	ON_BN_CLICKED(IDC_ADD_PATCH, &CPatchMetaFileView::OnBnClickedAddPatch)
	ON_BN_CLICKED(IDC_DEL_PATCH, &CPatchMetaFileView::OnBnClickedDelPatch)
	ON_BN_CLICKED(IDC_ADD_UPDATE, &CPatchMetaFileView::OnBnClickedAddUpdate)
	ON_BN_CLICKED(IDC_DEL_UPDATE, &CPatchMetaFileView::OnBnClickedDelUpdate)
	ON_NOTIFY(NM_DBLCLK, IDC_LIST_UPDATE, &CPatchMetaFileView::OnNMDblclkListUpdate)
	ON_NOTIFY(NM_DBLCLK, IDC_LIST_PATCH, &CPatchMetaFileView::OnNMDblclkListPatch)
	ON_WM_CREATE()
END_MESSAGE_MAP()

// CPatchMetaFileView construction/destruction

CPatchMetaFileView::CPatchMetaFileView()
	: CFormView(CPatchMetaFileView::IDD)
	, m_first(true)
	, m_versionLatest( 0)
{
}

CPatchMetaFileView::~CPatchMetaFileView()
{
}

void CPatchMetaFileView::DoDataExchange(CDataExchange* pDX)
{
	CFormView::DoDataExchange(pDX);
	DDX_Control( pDX, IDC_LIST_PATCH, m_lcPatch);
	DDX_Control( pDX, IDC_LIST_UPDATE, m_lcUpdate);
}

void CPatchMetaFileView::OnInitialUpdate()
{
	CFormView::OnInitialUpdate();
	GetParentFrame()->RecalcLayout();
	ResizeParentToFit();

	m_versionLatest = 0;
	GetDocument()->m_version.Close();

	if( m_first)
	{
		m_lcPatch.InsertColumn( 0, L"id", LVCFMT_LEFT, 40);
		m_lcPatch.InsertColumn( 1, L"文件名", LVCFMT_LEFT, 150);
		m_lcPatch.InsertColumn( 2, L"源版本号", LVCFMT_LEFT, 80);
		m_lcPatch.InsertColumn( 3, L"目标版本号", LVCFMT_LEFT, 80);
		m_lcPatch.InsertColumn( 5, L"md5", LVCFMT_LEFT, 210);
		m_lcPatch.SetExtendedStyle( LVS_EX_FULLROWSELECT);

		m_lcUpdate.InsertColumn( 0, L"源版本号", LVCFMT_LEFT, 80);
		m_lcUpdate.InsertColumn( 1, L"目标版本号", LVCFMT_LEFT, 80);
		m_lcUpdate.InsertColumn( 2, L"补丁包列表", LVCFMT_LEFT, 350);
		m_lcUpdate.SetExtendedStyle( LVS_EX_FULLROWSELECT);

		m_first = false;
	}

	FillPatchInfo();
	FillUpdateInfo();
}

bool GetVersionInfoFromPatch(const std::wstring& strPatchPath, PFSX::CVersionInfo& vi);

// CPatchMetaFileView message handlers
void CPatchMetaFileView::FillPatchInfo()
{
	m_lcPatch.DeleteAllItems();

	PFSX::CPatchInfoMap& pim = GetDocument()->m_pmf.GetPatchInfoMap();
	PFSX::CPatchInfoMap::const_iterator iLatest = pim.begin();
	for( PFSX::CPatchInfoMap::const_iterator i=pim.begin(), e=pim.end(); i!=e; ++i)
	{
		AddPatchInfoToList( i);

		const PFSX::PatchInfo& pi = i->second;
		if( pi.versionTo > m_versionLatest)
		{
			m_versionLatest = pi.versionTo;
			iLatest = i;
		}
	}

	if( iLatest == pim.end() )
		return;

	std::wstring filename = iLatest->second.filename;
	CString strFilePath = GetDocument()->GetPathName();
	if( !strFilePath.IsEmpty() )
	{ 
		PathRemoveFileSpec(strFilePath.GetBuffer());
		strFilePath.ReleaseBuffer();

		std::wstring natviefilepath; // TODO:
		natviefilepath.resize(MAX_PATH);

		PathCombineW( (LPWSTR)natviefilepath.data(),  strFilePath.GetString(), filename.c_str() );
		natviefilepath.resize( wcslen(natviefilepath.data()) );

		PFSX::CVersionInfo vi;
		if( !GetVersionInfoFromPatch( natviefilepath, vi ) )
		{
			// 获取补丁包版本信息失败。

			// 寻找.version.meta文件
			if( !PFSX::GetLocalVersion(strFilePath.GetString(), vi ) )
				return;
		}

		if( vi.m_Version >= m_versionLatest )
		{
			m_versionLatest = vi.m_Version;
			GetDocument()->m_version = vi;
		}
	}
}

void CPatchMetaFileView::UpdatePatchInfoInList( int index, const PFSX::PatchInfo& pi )
{
	// 列对照表：
	//m_lcPatch.InsertColumn( 0, L"id", LVCFMT_LEFT, 40);
	//m_lcPatch.InsertColumn( 1, L"文件名", LVCFMT_LEFT, 150);
	//m_lcPatch.InsertColumn( 2, L"源版本号", LVCFMT_LEFT, 80);
	//m_lcPatch.InsertColumn( 3, L"目标版本号", LVCFMT_LEFT, 80);
	//m_lcPatch.InsertColumn( 4, L"md5", LVCFMT_LEFT, 210);
	
	m_lcPatch.SetItemText( index, 1, pi.filename.c_str());

	std::wstring version;
	PFSX::Version2String( pi.versionFrom, version);
	m_lcPatch.SetItemText( index, 2, version.c_str());
	PFSX::Version2String( pi.versionTo, version);
	m_lcPatch.SetItemText( index, 3, version.c_str());

	// 显示MD5，对于以前没有配置的，提醒他配置。
	m_lcPatch.SetItemText( index, 4, pi.md5.c_str());
}

void CPatchMetaFileView::AddPatchInfoToList( PFSX::CPatchInfoMap::const_iterator& i)
{
	std::wostringstream woss;
	woss << i->first;
	int index = m_lcPatch.InsertItem( m_lcPatch.GetItemCount(), woss.str().c_str());

	const PFSX::PatchInfo& pi = i->second;

	UpdatePatchInfoInList( index, pi );
}

void CPatchMetaFileView::FillUpdateInfo()
{
	PFSX::CUpdateInfoMap& uim = GetDocument()->m_pmf.GetUpdateInfoMap();

	m_lcUpdate.DeleteAllItems();
	if( !uim.empty())
	{
		const PFSX::CUpdateInfoVector& uiv = uim.begin()->second;
		for( PFSX::CUpdateInfoVector::const_iterator i=uiv.begin(), e=uiv.end(); i!=e; ++i)
		{
			const PFSX::UpdateInfo& ui = *i;
			AddUpdateInfoToList( ui);
		}
	}
}

void CPatchMetaFileView::AddUpdateInfoToList( const PFSX::UpdateInfo& ui)
{
	std::wstring temp;

	PFSX::Version2String( ui.versionFrom, temp);
	int index = m_lcUpdate.InsertItem( m_lcUpdate.GetItemCount(), temp.c_str());
	PFSX::Version2String( ui.versionTo, temp);
	m_lcUpdate.SetItemText( index, 1, temp.c_str());
	PFSX::GetStrFromIntVector( ui.use, temp);
	m_lcUpdate.SetItemText( index, 2, temp.c_str());
}

// 从补丁包中读取版本信息。
bool GetVersionInfoFromPatch(const std::wstring& strPatchPath, PFSX::CVersionInfo& vi)
{
	if( !PFS::CEnv::Mount( PATCH, strPatchPath, PFS::FST_RUNZIP))
	{
		AfxMessageBox( (strPatchPath + L" 不是有效的补丁包文件！").c_str() );
		return false;
	}

	if( !PFSX::GetLocalVersion( PATCH, vi))
	{
		PFS::CEnv::Unmount( PATCH);
		AfxMessageBox( (strPatchPath + L" - 补丁包没找到 .version.meta 文件！").c_str() );
		return false;
	}

	PFS::CEnv::Unmount( PATCH);

	return true;
}

bool CPatchMetaFileView::GetPatchInfoFromPatchFile(std::wstring natviefilepath,
												   std::wstring filename,
												   PFSX::PatchInfo& pi,
												   PFSX::CVersionInfo& vi )
{
	if( !GetVersionInfoFromPatch( natviefilepath, vi ) )
	{
		// 获取补丁包版本信息失败。
		return false;
	}

	pi.filename = L"/";
	pi.filename += filename;
	pi.versionFrom = vi.m_lastVersion;
	pi.versionTo = vi.m_Version;

	PFSX::CNativeFile nf;
	if( !nf.Open( natviefilepath, PFS::FM_EXCL, PFS::FA_RDONLY))
	{
		CString str;
		str.Format( L"打开补丁包文件失败(%d)！", PFS::CEnv::GetLastError());
		AfxMessageBox( str);
		return false;
	}

	pi.size = nf.GetSize();
	PFS::helper::CMD5FileHashMethod md5Hash;
	PFS::CDataBlock md5data;
	if( !md5Hash.MakeFileHash( nf, md5data ) )
	{
		CString str;
		str.Format( L"生成补丁包md5值失败(%d)！", PFS::CEnv::GetLastError());
		AfxMessageBox( str);
		return false;
	}

	pi.md5 = PFSX::CStringHelper::Data2String( md5data );
	// 优化：用前面生成的128bit MD5值计算FILE_DATA_HASH值。
	pi.hash = *(PFS::FILE_DATA_HASH*)md5data.GetData() ^ *((PFS::FILE_DATA_HASH*)md5data.GetData() + 1);

	return true;
}

void CPatchMetaFileView::OnBnClickedAddPatch()
{
	CFileDialog dlg( TRUE, NULL, 0, 
		OFN_READONLY|OFN_NOCHANGEDIR|OFN_PATHMUSTEXIST|OFN_FILEMUSTEXIST|OFN_EXPLORER|OFN_ENABLESIZING,
		L"更新包(*.cpch)|*.cpch|所有文件(*.*)|*.*||", this, 0);
	if( dlg.DoModal() != IDOK )
	{
		return;
	}

	std::wstring str = dlg.GetPathName().GetString();
	
	PFSX::CVersionInfo vi;
	PFSX::PatchInfo pi;
	if( !GetPatchInfoFromPatchFile(str, dlg.GetFileName().GetString(), pi, vi) )
	{
		return;
	}

	PFSX::CPatchInfoMap& pim = GetDocument()->m_pmf.GetPatchInfoMap();
	int id = 0;
	if( !pim.empty())
	{
		PFSX::CPatchInfoMap::const_reverse_iterator i = pim.rbegin();
		id = i->first + 1;
	}

	PFSX::CPatchInfoMap::_Pairib pb = pim.insert( std::make_pair( id, pi));
	if( !pb.second)
	{
		AfxMessageBox( L"添加失败！");
		return ;
	}

	AddPatchInfoToList( pb.first);
	GetDocument()->SetModifiedFlag( TRUE);

	if( pi.versionTo >= m_versionLatest )
	{
		m_versionLatest = pi.versionTo;
		GetDocument()->m_version = vi;
	}
}

void CPatchMetaFileView::OnBnClickedDelPatch()
{
	POSITION pos = m_lcPatch.GetFirstSelectedItemPosition();
	if (pos == NULL)
	{
		AfxMessageBox( L"请先选择要删除的行！");
	}
	else
	{
		PFSX::CPatchInfoMap& pim = GetDocument()->m_pmf.GetPatchInfoMap();
		for (int i=m_lcPatch.GetItemCount()-1; i>=0; i--)
		{
			if( m_lcPatch.GetItemState( i, LVIS_SELECTED) == LVIS_SELECTED)
			{
				CString str = m_lcPatch.GetItemText( i, 0);
				if( pim.erase( _wtoi( str.GetString())) > 0)
				{
					m_lcPatch.DeleteItem( i);
				}
			}
		}

		DWORD maxVersionTo = 0;
		for( PFSX::CPatchInfoMap::const_iterator i=pim.begin(), e=pim.end(); i!=e; ++i)
		{
			const PFSX::PatchInfo& pi = i->second;
			if( pi.versionTo > maxVersionTo)
			{
				maxVersionTo = pi.versionTo;
			}
		}

		if( maxVersionTo != m_versionLatest)
		{
			m_versionLatest = maxVersionTo;

			// todo
			// 现在从.patch.meta中只能得到versionTo信息
			GetDocument()->m_version.m_Version = m_versionLatest;
		}

		GetDocument()->SetModifiedFlag( TRUE);
	}
}

void CPatchMetaFileView::OnBnClickedAddUpdate()
{
	CAddVersionDlg dlg;
	dlg.SetDocument( GetDocument(), m_versionLatest );
	if( IDCANCEL == dlg.DoModal())
	{
		return;
	}

	PFSX::UpdateInfo ui;
	Version2DWORD( dlg.m_src.GetString(), ui.versionFrom);
	Version2DWORD( dlg.m_des.GetString(), ui.versionTo);
	PFSX::GetIntVectorFromStr( ui.use, dlg.m_patchs.GetString());
	
	PFSX::CUpdateInfoMap& uim = GetDocument()->m_pmf.GetUpdateInfoMap();
	if( uim.empty())
	{
		uim.insert( std::make_pair( 0, PFSX::CUpdateInfoVector()));
	}
	PFSX::CUpdateInfoVector& uiv = uim.begin()->second;
	for( PFSX::CUpdateInfoVector::iterator i=uiv.begin(), e=uiv.end(); i!=e; ++i)
	{
		if( i->versionFrom == ui.versionFrom && i->versionTo == ui.versionTo)
		{
			AfxMessageBox( L"当前版本的补丁包列表已存在");
			return;
		}
	}
	uiv.push_back( ui);

	AddUpdateInfoToList( uiv.back());
	GetDocument()->SetModifiedFlag( TRUE);
}

void CPatchMetaFileView::OnBnClickedDelUpdate()
{
	POSITION pos = m_lcUpdate.GetFirstSelectedItemPosition();
	if (pos == NULL)
	{
		AfxMessageBox( L"请先选择要删除的行！");
		return;
	}

	PFSX::CUpdateInfoMap& uim = GetDocument()->m_pmf.GetUpdateInfoMap();
	if( uim.empty())
	{
		return;
	}
	PFSX::CUpdateInfoVector& uiv = uim.begin()->second;

	for (int j=m_lcUpdate.GetItemCount()-1; j>=0; j--)
	{
		if( m_lcUpdate.GetItemState( j, LVIS_SELECTED) == LVIS_SELECTED)
		{
			DWORD srcVersion = 0, desVersion = 0;
			Version2DWORD( m_lcUpdate.GetItemText( j, 0).GetString(), srcVersion);
			Version2DWORD( m_lcUpdate.GetItemText( j, 1).GetString(), desVersion);
			for( PFSX::CUpdateInfoVector::iterator i=uiv.begin(), e=uiv.end(); i!=e; ++i)
			{
				if( i->versionFrom == srcVersion && i->versionTo == desVersion)
				{
					uiv.erase( i);
					break;
				}
			}
			m_lcUpdate.DeleteItem( j);
		}
	}

	GetDocument()->SetModifiedFlag( TRUE);
}

void CPatchMetaFileView::OnNMDblclkListPatch(NMHDR *pNMHDR, LRESULT *pResult)
{
	for (int i=m_lcPatch.GetItemCount()-1; i>=0; i--)
	{
		if( m_lcPatch.GetItemState( i, LVIS_SELECTED) != LVIS_SELECTED)
		{
			continue;
		}

		CString str = m_lcPatch.GetItemText( i, 0 );
		CString strFilePath = m_lcPatch.GetItemText( i, 1 );
		std::wstring wstrFileName = strFilePath.Mid(1);
					
		CFileDialog dlg( TRUE, NULL, wstrFileName.c_str(), 
			OFN_READONLY|
			OFN_NOCHANGEDIR|
			OFN_PATHMUSTEXIST|
			OFN_FILEMUSTEXIST|
			OFN_EXPLORER|
			OFN_ENABLESIZING,
			L"更新包(*.cpch)|*.cpch|所有文件(*.*)|*.*||", this, 0);
		if( dlg.DoModal() != IDOK )
		{
			continue;
		}

		// file natvie path
		std::wstring wstrFilePath = dlg.GetPathName().GetString();
		// file name
		wstrFileName = dlg.GetFileName().GetString();
		int fileId =  _wtoi( str.GetString() );
		
		PFSX::CVersionInfo vi;
		PFSX::CPatchInfoMap& pim = GetDocument()->m_pmf.GetPatchInfoMap();
		PFSX::PatchInfo& pi = pim.find(fileId)->second;
		if( !GetPatchInfoFromPatchFile(wstrFilePath, wstrFileName, pi, vi) )
		{
			continue;
		}

		UpdatePatchInfoInList( i, pi );

		GetDocument()->SetModifiedFlag( TRUE);

		if( vi.m_Version >= m_versionLatest )
		{
			m_versionLatest = vi.m_Version;
			GetDocument()->m_version = vi;
		}
	}

	*pResult = 1;
}

void CPatchMetaFileView::OnNMDblclkListUpdate(NMHDR *pNMHDR, LRESULT *pResult)
{
	LPNMITEMACTIVATE pDispInfo = (LPNMITEMACTIVATE)pNMHDR;
	if( !pDispInfo )
	{
		*pResult = 0;
		return;
	}

	CAddVersionDlg dlg;
	dlg.m_srcInit = m_lcUpdate.GetItemText( pDispInfo->iItem, 0);
	dlg.m_desInit = m_lcUpdate.GetItemText( pDispInfo->iItem, 1);
	dlg.SetDocument( GetDocument(), m_versionLatest );
	if( IDOK == dlg.DoModal())
	{
		if( m_lcUpdate.GetItemText( pDispInfo->iItem, 2) != dlg.m_patchs)
		{
			PFSX::CUpdateInfoMap& uim = GetDocument()->m_pmf.GetUpdateInfoMap();
			if( !uim.empty())
			{
				DWORD srcVersion = 0, desVersion = 0;
				Version2DWORD( dlg.m_src.GetString(), srcVersion);
				Version2DWORD( dlg.m_des.GetString(), desVersion);

				PFSX::CUpdateInfoVector& uiv = uim.begin()->second;
				for( PFSX::CUpdateInfoVector::iterator i=uiv.begin(), e=uiv.end(); i!=e; ++i)
				{
					if( i->versionFrom == srcVersion && i->versionTo == desVersion)
					{
						i->use.clear();
						PFSX::GetIntVectorFromStr( i->use, dlg.m_patchs.GetString());
						GetDocument()->SetModifiedFlag( TRUE);
						break;
					}
				}
				m_lcUpdate.SetItemText( pDispInfo->iItem, 2, dlg.m_patchs);
			}
		}
	}

	*pResult = 1;
}
