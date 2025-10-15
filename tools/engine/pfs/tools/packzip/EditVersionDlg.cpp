// EditVersionDlg.cpp : implementation file
//

#include "stdafx.h"
#include "PackZip.h"
#include "EditVersionDlg.h"
#include "MainFrm.h"
#include <sstream>
#include "../../versionmetafile.h"

// CEditVersionDlg dialog
typedef struct  
{
	LPCTSTR		network;
	LPCTSTR		name;
	int			type;
}SERVERTYPE;

SERVERTYPE lpVersionType[] = 
{
	{_T("网通"), _T("网通缺省更新"), 0},
	{_T("网通"), _T("网通其它更新"), 1},
	{_T("网通"), _T("网通后台更新"), 2},
	{_T("网通"), _T("网通差异包更新"), 100},
	{_T("网通"), _T("网通版本文件更新"), 200},

	{_T("电信"), _T("电信缺省更新"),3},
	{_T("电信"), _T("电信其它更新"),4},
	{_T("电信"), _T("电信后台更新"),5},
	{_T("电信"), _T("电信差异包更新"), 103},
	{_T("电信"), _T("电信版本文件更新"), 203},
};

#define STRCOUNT( p) ( sizeof(p) / sizeof(p[0]))

IMPLEMENT_DYNAMIC(CEditVersionDlg, CDialog)

CEditVersionDlg::CEditVersionDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CEditVersionDlg::IDD, pParent)
	, m_project( NULL)
	, m_changed( false)
{
}

CEditVersionDlg::~CEditVersionDlg()
{
}

void CEditVersionDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LIST1, m_list);
	DDX_Control(pDX, IDC_EDIT1, m_version);
	DDX_Control(pDX, IDC_DATETIMEPICKER1, m_time);
	DDX_Control(pDX, IDC_EDIT3, m_luv);
}

BEGIN_MESSAGE_MAP(CEditVersionDlg, CDialog)
	ON_MESSAGE(WM_SET_ITEMS, PopulateComboList)
	ON_MESSAGE(WM_VALIDATE, OnEndLabelEditVersionList)
	ON_BN_CLICKED(IDC_BUTTON1, &CEditVersionDlg::OnBnClickedButton1)
	ON_BN_CLICKED(IDC_BUTTON2, &CEditVersionDlg::OnBnClickedButton2)
	ON_EN_CHANGE(IDC_EDIT1, &CEditVersionDlg::OnEnChangeEdit1)
END_MESSAGE_MAP()

// CEditVersionDlg message handlers

BOOL CEditVersionDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_list.InsertColumn( 0, L"类型", LVCFMT_LEFT, 100);
	m_list.InsertColumn( 1, L"下载url", LVCFMT_LEFT, 240);
	m_list.InsertColumn( 2, L"网络提供商", LVCFMT_LEFT, 80); // 可编辑，并且可以被保存下来。
	m_list.SetExtendedStyle( LVS_EX_FULLROWSELECT);
	m_list.SetComboColumns( 0, TRUE);
	
	if( m_project)
	{
		PFSX::CVersionInfo& versioninfo = m_project->GetVersionMeta();
		std::wstring versionstr;
		PFSX::Version2String( versioninfo.m_Version, versionstr);
		m_version.SetWindowText( versionstr.c_str());
		PFSX::Version2String( versioninfo.m_luv, versionstr);
		m_luv.SetWindowText( versionstr.c_str());
	
		PFSX::CVersionInfo::VersionUpdateInfoMap& map = versioninfo.GetUpdateInfoMap();
		for( PFSX::CVersionInfo::VersionUpdateInfoMap::iterator itor = map.begin(); itor != map.end(); ++itor)
		{
			AddVersionMeta( itor->first, itor->second.Url.c_str(), itor->second.Network.c_str() );
		}

		CTime time = CTime::GetCurrentTime();
		m_time.SetTime( &time);
	}

	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}

void CEditVersionDlg::OpenProject( CProject* project)
{
	m_project = project;
}

int GetTypeFromStr( LPCTSTR pszType)
{
	int typecount = STRCOUNT( lpVersionType);
	for( int i=0; i<typecount; i++)
	{
		if( wcscmp( lpVersionType[i].name, pszType) == 0)
			return lpVersionType[i].type;
	}
	return 0;
}

LPCTSTR GetStrFromType( int type)
{
	int typecount = STRCOUNT( lpVersionType);
	for( int i=0; i<typecount; i++)
	{
		if( lpVersionType[i].type == type)
		{
			return lpVersionType[i].name;
		}
	}
	return L"";
}

LPCTSTR GetNetworkFromType( int type)
{
	int typecount = STRCOUNT( lpVersionType);
	for( int i=0; i<typecount; i++)
	{
		if( lpVersionType[i].type == type)
		{
			return lpVersionType[i].network;
		}
	}
	return L"";
}

bool CEditVersionDlg::AddVersionMeta( int type, LPCWSTR lpszUrl, LPCWSTR lpszNetwork)
{
	int index = m_list.InsertItem( m_list.GetItemCount(), GetStrFromType(type));
	if( lpszUrl != NULL )
		m_list.SetItemText( index, 1, lpszUrl);

	if( lpszNetwork == NULL )
	{
		// Use Default Network Name 
		m_list.SetItemText( index, 2, GetNetworkFromType(type));
	}
	else
	{
		m_list.SetItemText( index, 2, lpszNetwork );
	}

	return true;
}

void CEditVersionDlg::OnBnClickedButton1()
{
	int type = 0;

	// 查找尚未使用的更新类型，不能重复配置相同的更新类型。否则保存之后会丢失。
	// 由于现在支持的更新类型很有限，下面的双重循环不值得担心效率问题。
	int typecount = STRCOUNT( lpVersionType);
	for( int t=0; t<typecount; t++)
	{
		type = lpVersionType[t].type;
		for (int i=0; i < m_list.GetItemCount(); i ++ )
		{
			int _type = GetTypeFromStr( m_list.GetItemText( i, 0).GetString());

			if( type == _type )
			{
				type = -1;
				break;
			}
		}

		if( type != -1 )
		{
			break;
		}
	}

	if( type == -1 )
	{
		AfxMessageBox(L"不支持更多的更新类型了！");
	}
	else
	{
		// Use Default Network Name 
		AddVersionMeta( type, NULL, NULL );
		m_changed = true;
	}
}

void CEditVersionDlg::OnBnClickedButton2()
{
	POSITION pos = m_list.GetFirstSelectedItemPosition();
	if (pos == NULL)
	   AfxMessageBox( L"Please select item to delete");
	else
	{
		for (int i=m_list.GetItemCount(); i>=0; i--)
		{
			if( m_list.GetItemState(i, LVIS_SELECTED) == LVIS_SELECTED)
			{
				m_list.DeleteItem(i);
			}
		}
		m_changed = true;
	}
}

void CEditVersionDlg::OnOK()
{
	if( !m_project)
		return;

	CString version;
	m_version.GetWindowText( version);
	DWORD dwVersion = 0;
	if( !PFSX::String2Version( version.GetString(), dwVersion))
	{
		AfxMessageBox( L"版本号格式不正确，请输入 \"1.0.0\" 类似的格式");
		m_version.SetFocus();
		return;
	}

	DWORD luv = 0;
	m_luv.GetWindowText( version);
	if( !PFSX::String2Version( version.GetString(), luv))
	{
		AfxMessageBox( L"最低可更新版本号格式不正确，请输入 \"1.0.0\" 类似的格式");
		m_luv.SetFocus();
		return;
	}

	// 检查由于手工调整更新类型导致的重复项目，并提示用户。
	int typecount = m_list.GetItemCount();
	int duplicated_indexA = -1;
	int duplicated_indexB = -1;
	for( int t=0; duplicated_indexA == -1 && t < typecount-1; t++)
	{
		int type = GetTypeFromStr( m_list.GetItemText( t, 0).GetString());
		for (int i=t+1; i < typecount; i ++ )
		{
			int _type = GetTypeFromStr( m_list.GetItemText( i, 0).GetString());
			if( type == _type )
			{
				duplicated_indexA = t;
				duplicated_indexB = i;
				break;
			}
		}
	}

	if( duplicated_indexA != -1 )
	{
		m_list.SetItemState( -1, LVIS_SELECTED, 0 ); // 清除所有选中项目。

		m_list.SetFocus();

		// 标记重复项目
		m_list.SetItemState( duplicated_indexA, LVIS_SELECTED, LVIS_SELECTED);
		m_list.SetItemState( duplicated_indexB, LVIS_SELECTED|LVIS_FOCUSED, LVIS_SELECTED|LVIS_FOCUSED);

		CString strType = m_list.GetItemText( duplicated_indexA, 0);
		CString strMessage;
		strMessage.Format( L"更新类型出现重复，请重新调整: %s", strType.GetString() );
		AfxMessageBox( strMessage );
		 
		m_list.SetFocus();
		return;
	}

	PFSX::CVersionInfo& metafile = m_project->GetVersionMeta();
	metafile.GetUpdateInfoMap().clear();
	metafile.m_Version = dwVersion;
	metafile.m_luv = luv;
	CTime time;
	m_time.GetTime( time);
	metafile.m_ReleaseDate = (long)time.GetTime();

	for( int i=0; i<m_list.GetItemCount(); i++)
	{
		PFSX::VersionUpdateInfo info;
		info.Type = GetTypeFromStr( m_list.GetItemText( i, 0).GetString());
		info.Url = m_list.GetItemText( i, 1).GetString();
		info.Network = m_list.GetItemText( i, 2).GetString();
		metafile.AddUpdateInfo( info);	
	}

	metafile.m_bValid = m_list.GetItemCount() > 0;

	CDialog::OnOK();
}

LRESULT CEditVersionDlg::OnEndLabelEditVersionList(WPARAM wParam, LPARAM lParam) 
{
	m_changed = true;
	return 0;
}

LRESULT CEditVersionDlg::PopulateComboList(WPARAM wParam, LPARAM lParam)
{
	CStringList* pComboList = reinterpret_cast<CStringList*>( lParam);
	if ( pComboList)
	{
		int typecount = STRCOUNT( lpVersionType);
		for( int i=0; i<typecount; i++)
		{
			pComboList->AddTail( lpVersionType[i].name);
		}
	}
	return 0;
}

void CEditVersionDlg::OnEnChangeEdit1()
{
	m_changed = true;
}

void CEditVersionDlg::OnCancel()
{
	m_changed = false;
	CDialog::OnCancel();
}
