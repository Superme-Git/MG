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
	{_T("��ͨ"), _T("��ͨȱʡ����"), 0},
	{_T("��ͨ"), _T("��ͨ��������"), 1},
	{_T("��ͨ"), _T("��ͨ��̨����"), 2},
	{_T("��ͨ"), _T("��ͨ���������"), 100},
	{_T("��ͨ"), _T("��ͨ�汾�ļ�����"), 200},

	{_T("����"), _T("����ȱʡ����"),3},
	{_T("����"), _T("������������"),4},
	{_T("����"), _T("���ź�̨����"),5},
	{_T("����"), _T("���Ų��������"), 103},
	{_T("����"), _T("���Ű汾�ļ�����"), 203},
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

	m_list.InsertColumn( 0, L"����", LVCFMT_LEFT, 100);
	m_list.InsertColumn( 1, L"����url", LVCFMT_LEFT, 240);
	m_list.InsertColumn( 2, L"�����ṩ��", LVCFMT_LEFT, 80); // �ɱ༭�����ҿ��Ա�����������
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

	// ������δʹ�õĸ������ͣ������ظ�������ͬ�ĸ������͡����򱣴�֮��ᶪʧ��
	// ��������֧�ֵĸ������ͺ����ޣ������˫��ѭ����ֵ�õ���Ч�����⡣
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
		AfxMessageBox(L"��֧�ָ���ĸ��������ˣ�");
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
		AfxMessageBox( L"�汾�Ÿ�ʽ����ȷ�������� \"1.0.0\" ���Ƶĸ�ʽ");
		m_version.SetFocus();
		return;
	}

	DWORD luv = 0;
	m_luv.GetWindowText( version);
	if( !PFSX::String2Version( version.GetString(), luv))
	{
		AfxMessageBox( L"��Ϳɸ��°汾�Ÿ�ʽ����ȷ�������� \"1.0.0\" ���Ƶĸ�ʽ");
		m_luv.SetFocus();
		return;
	}

	// ��������ֹ������������͵��µ��ظ���Ŀ������ʾ�û���
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
		m_list.SetItemState( -1, LVIS_SELECTED, 0 ); // �������ѡ����Ŀ��

		m_list.SetFocus();

		// ����ظ���Ŀ
		m_list.SetItemState( duplicated_indexA, LVIS_SELECTED, LVIS_SELECTED);
		m_list.SetItemState( duplicated_indexB, LVIS_SELECTED|LVIS_FOCUSED, LVIS_SELECTED|LVIS_FOCUSED);

		CString strType = m_list.GetItemText( duplicated_indexA, 0);
		CString strMessage;
		strMessage.Format( L"�������ͳ����ظ��������µ���: %s", strType.GetString() );
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
