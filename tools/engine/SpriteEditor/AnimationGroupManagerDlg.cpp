

#include "stdafx.h"
#include "SpriteEditor.h"
#include "AnimationGroupManagerDlg.h"




IMPLEMENT_DYNAMIC(CAnimationGroupManagerDlg, CDialog)

void CAnimationGroupManagerDlg::OnBnClickedBtnDeleteAnimationMember()
{
	if( m_CurGroupName == L"" ) return;
	int cursel = m_ListBoxAnimationMembers.GetCurSel();
	if( cursel < 0 || cursel >= (int)m_ListBoxAnimationMembers.GetCount() ) return;
	wchar_t buf[64];
	m_ListBoxAnimationMembers.GetText(cursel, buf);
	std::wstring mem = buf;

	if( !m_pGroupMgr->DelElement(m_CurGroupName, mem) ) return;

	m_ListBoxAnimationMembers.ResetContent();
	std::vector<std::wstring> v;
	m_pGroupMgr->DumpGroup(m_CurGroupName, v);
	for(int i=0; i<(int)v.size();++i)
	{
		m_ListBoxAnimationMembers.AddString(v[i].c_str());
	}
}

void CAnimationGroupManagerDlg::OnBnClickedBtnAddAnimationMember()
{
	if( m_CurGroupName == L"") return;
	int cursel = m_ListBoxAnimationMembersCan.GetCurSel();
	if( cursel < 0 || cursel >= (int)m_AnimationNames.size() ) return;
	std::wstring &mem = m_AnimationNames[cursel];

	if( !m_pGroupMgr->AddElement(m_CurGroupName, mem)) return;

	m_ListBoxAnimationMembers.ResetContent();
	std::vector<std::wstring> v;
	m_pGroupMgr->DumpGroup(m_CurGroupName, v);
	for(int i=0; i<(int)v.size();++i)
	{
		m_ListBoxAnimationMembers.AddString(v[i].c_str());
		if( mem == v[i] )
			m_ListBoxAnimationMembers.SetCurSel(i);
	}
}

void CAnimationGroupManagerDlg::OnLbnDblclkListBoxAnimationGroups()
{
	int cursel = m_ListBoxAnimationGroups.GetCurSel();
	if( cursel < 0 || cursel >=(int)m_GroupNames.size()) return;

	UpdateMemberList(m_GroupNames[cursel]);
}

void CAnimationGroupManagerDlg::OnBnClickedBtnEditAnimationGroup()
{
	int cursel = m_ListBoxAnimationGroups.GetCurSel();
	if( cursel < 0 || cursel >=(int)m_GroupNames.size()) return;

	UpdateMemberList(m_GroupNames[cursel]);
}

void CAnimationGroupManagerDlg::OnOK()
{
	m_pGroupMgr->Save();

	CDialog::OnOK();
}

BOOL CAnimationGroupManagerDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	for(int i=0; i<(int)m_AnimationNames.size();++i)
		m_ListBoxAnimationMembersCan.AddString(m_AnimationNames[i].c_str());
	UpdateGroupList();

	return TRUE;  

}

void CAnimationGroupManagerDlg::OnBnClickedBtnAddAnimationGroup()
{
	wchar_t buf[64];
	GetDlgItemText(IDC_EDT_ACTGROUPNAME,buf,64);
	std::wstring newg = buf;
	if( newg == L"" || newg == L"all" ) return;
	if( m_pGroupMgr->AddGroup(newg))
	{
		UpdateGroupList();
		UpdateMemberList(newg);
	}
}

void CAnimationGroupManagerDlg::UpdateGroupList()
{
	m_GroupNames.clear();
	m_pGroupMgr->DumpGroups(m_GroupNames);
	m_ListBoxAnimationGroups.ResetContent();
	for(int i=0; i<(int)m_GroupNames.size();++i)
		m_ListBoxAnimationGroups.AddString(m_GroupNames[i].c_str());
}

void CAnimationGroupManagerDlg::UpdateMemberList(const std::wstring &g)
{
	if( m_CurGroupName == g ) return;
	m_CurGroupName = g;

	SetDlgItemText(IDC_EDT_EDTACTGROUP, g.c_str());

	m_ListBoxAnimationMembers.ResetContent();
	std::vector<std::wstring> v;
	m_pGroupMgr->DumpGroup(g, v);
	for(int i=0; i<(int)v.size();++i)
		m_ListBoxAnimationMembers.AddString(v[i].c_str());
}

void CAnimationGroupManagerDlg::OnBnClickedBtnDeleteAnimationGroup()
{
	int cursel = m_ListBoxAnimationGroups.GetCurSel();
	if( cursel < 0 || cursel >=(int)m_GroupNames.size()) return;
	wchar_t buf[128];
	wsprintf(buf,L"确实要删除动作组 %s 吗？", m_GroupNames[cursel].c_str());
	if( IDYES != MessageBox(buf, L"警告",MB_YESNO) ) return;
	if( m_pGroupMgr->DelGroup(m_GroupNames[cursel]))
	{
		bool f = m_GroupNames[cursel] == m_CurGroupName;
		UpdateGroupList();
		if( f )
			UpdateMemberList(L"");
	}
}

BEGIN_MESSAGE_MAP(CAnimationGroupManagerDlg, CDialog)
	ON_BN_CLICKED(IDC_BTN_DELACTGROUP, &CAnimationGroupManagerDlg::OnBnClickedBtnDeleteAnimationGroup)
	ON_BN_CLICKED(IDC_BTN_ADDACTGROUP, &CAnimationGroupManagerDlg::OnBnClickedBtnAddAnimationGroup)
	ON_BN_CLICKED(IDC_BTN_EDTACTGROUP, &CAnimationGroupManagerDlg::OnBnClickedBtnEditAnimationGroup)
	ON_LBN_DBLCLK(IDC_LST_ACTGROUPS, &CAnimationGroupManagerDlg::OnLbnDblclkListBoxAnimationGroups)
	ON_BN_CLICKED(IDC_BTN_ADDACTMEMBER, &CAnimationGroupManagerDlg::OnBnClickedBtnAddAnimationMember)
	ON_BN_CLICKED(IDC_BTN_DELACTMEMBER, &CAnimationGroupManagerDlg::OnBnClickedBtnDeleteAnimationMember)
END_MESSAGE_MAP()

void CAnimationGroupManagerDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LST_ACTGROUPS, m_ListBoxAnimationGroups);
	DDX_Text(pDX, IDC_EDT_ACTGROUPNAME, m_edtActGroupName);
	DDX_Control(pDX, IDC_LST_ACTMEMBERS, m_ListBoxAnimationMembers);
	DDX_Control(pDX, IDC_LST_ACTMEMBERSCAN, m_ListBoxAnimationMembersCan);
}

CAnimationGroupManagerDlg::~CAnimationGroupManagerDlg()
{
}

CAnimationGroupManagerDlg::CAnimationGroupManagerDlg(Nuclear::GroupManager *pGroupMan, CWnd* pParent )
: CDialog(CAnimationGroupManagerDlg::IDD, pParent), m_pGroupMgr(pGroupMan)
, m_edtActGroupName(_T(""))
{

}

