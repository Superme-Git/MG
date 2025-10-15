

#include "stdafx.h"
#include "SpriteEditor.h"
#include "AnimationRefManagerDlg.h"

#include <algorithm>




IMPLEMENT_DYNAMIC(CAnimationRefManagerDlg, CDialog)

void CAnimationRefManagerDlg::OnBnClickedBtnActrefDel()
{
	UpdateData(TRUE);

	int cursel = m_ActRefName2.GetCurSel();
	if( cursel < 0 ) return;
	if( m_ActRefName1 == L"" || m_ActRefName1 == L"无" )
	{
		AfxMessageBox(L"非法的动作名称");
		return;
	}

	std::wstring name1 = m_ActRefName1;

	std::map<std::wstring,std::wstring>::const_iterator it = m_ActRefMap.find(name1);
	if( it == m_ActRefMap.end() )
	{
		AfxMessageBox(L"此动作名称不存在");
		return;
	}

	m_ActRefMap.erase(name1);
	Refresh();
}

void CAnimationRefManagerDlg::OnBnClickedBtnActrefEdt()
{
	UpdateData(TRUE);

	int cursel = m_ActRefName2.GetCurSel();
	if( cursel < 0 ) return;
	if( m_ActRefName1 == L"" || m_ActRefName1 == L"无" )
	{
		AfxMessageBox(L"非法的动作名称");
		return;
	}

	std::wstring name1 = m_ActRefName1;

	std::map<std::wstring,std::wstring>::const_iterator it = m_ActRefMap.find(name1);
	if( it == m_ActRefMap.end() )
	{
		AfxMessageBox(L"此动作名称不存在");
		return;
	}

	if( std::find(m_ActList.begin(), m_ActList.end(), name1) != m_ActList.end() )
	{
		AfxMessageBox(L"此动作名称已经存在");
		return;
	}
	CString str;
	m_ActRefName2.GetLBText(cursel, str);
	m_ActRefMap[name1] = std::wstring(str);
	Refresh();
}

void CAnimationRefManagerDlg::OnNMClickLstActref(NMHDR *pNMHDR, LRESULT *pResult)
{
	int cursel = -1;
	POSITION pos = m_RefList.GetFirstSelectedItemPosition();
	if (pos != NULL)
	{
		while (pos)
		{
			cursel = m_RefList.GetNextSelectedItem(pos);
			break;
		}		
	}
	if( cursel >= 0 )
	{
		CString str = m_RefList.GetItemText(cursel,0);
		SetDlgItemText(IDC_EDT_ACTREF_NAME1, str);
		str = m_RefList.GetItemText(cursel,1);

		int n = m_ActRefName2.GetCount();
		for(int i=0; i<n; ++i)
		{
			CString str2;
			m_ActRefName2.GetLBText(i,str2);
			if( str2 == str )
			{
				m_ActRefName2.SetCurSel(i);
				break;
			}
		}
	}
	*pResult = 0;
}

void CAnimationRefManagerDlg::Refresh()
{
	m_RefList.DeleteAllItems();
	int i =0;
	for(std::map<std::wstring,std::wstring>::const_iterator it = m_ActRefMap.begin(),ie=m_ActRefMap.end(); it != ie; ++it)
	{
		m_RefList.InsertItem(i, it->first.c_str());
		m_RefList.SetItemText(i, 1, it->second.c_str());
		++i;
	}
	UpdateData(FALSE);
}

void CAnimationRefManagerDlg::OnBnClickedBtnActrefNew()
{
	UpdateData(TRUE);

	int cursel = m_ActRefName2.GetCurSel();
	if( cursel < 0 ) return;
	if( m_ActRefName1 == L"" || m_ActRefName1 == L"无" )
	{
		AfxMessageBox(L"非法的动作名称");
		return;
	}

	std::wstring name1 = m_ActRefName1;

	std::map<std::wstring,std::wstring>::const_iterator it = m_ActRefMap.find(name1);
	if( it != m_ActRefMap.end() )
	{
		AfxMessageBox(L"此动作名称已经存在");
		return;
	}

	if( std::find(m_ActList.begin(), m_ActList.end(), name1) != m_ActList.end() )
	{
		AfxMessageBox(L"此动作名称已经存在");
		return;
	}
	CString str;
	m_ActRefName2.GetLBText(cursel, str);
	m_ActRefMap[name1] = std::wstring(str);
	Refresh();
}

BOOL CAnimationRefManagerDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_CurEdt = -1;

	m_RefList.SetExtendedStyle(LVS_EX_FULLROWSELECT);

	m_RefList.InsertColumn(0, L"动作名称", 0, 300);
	m_RefList.InsertColumn(1, L"引用动作名称", 0, 300);

	int i =0;
	for(std::map<std::wstring,std::wstring>::const_iterator it = m_ActRefMap.begin(),ie=m_ActRefMap.end(); it != ie; ++it)
	{
		m_RefList.InsertItem(i, it->first.c_str());
		m_RefList.SetItemText(i, 1, it->second.c_str());
		++i;
	}

	int nAct = (int)m_ActList.size();

	for(int i=0; i<nAct; ++i)
	{
		m_ActRefName2.AddString(m_ActList[i].c_str());
	}
	if( nAct > 0 )
		m_ActRefName2.SetCurSel(0);


	return TRUE;  

}

void CAnimationRefManagerDlg::OnOK()
{
	CDialog::OnOK();
}

BEGIN_MESSAGE_MAP(CAnimationRefManagerDlg, CDialog)
	ON_BN_CLICKED(IDC_BTN_ACTREF_NEW, &CAnimationRefManagerDlg::OnBnClickedBtnActrefNew)
	ON_NOTIFY(NM_CLICK, IDC_LST_ACTREF, &CAnimationRefManagerDlg::OnNMClickLstActref)
	ON_BN_CLICKED(IDC_BTN_ACTREF_EDT, &CAnimationRefManagerDlg::OnBnClickedBtnActrefEdt)
	ON_BN_CLICKED(IDC_BTN_ACTREF_DEL, &CAnimationRefManagerDlg::OnBnClickedBtnActrefDel)
END_MESSAGE_MAP()

void CAnimationRefManagerDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_LST_ACTREF, m_RefList);
	DDX_Text(pDX, IDC_EDT_ACTREF_NAME1, m_ActRefName1);
	DDX_Control(pDX, IDC_CMB_ACTREF_NAME2, m_ActRefName2);
}

CAnimationRefManagerDlg::~CAnimationRefManagerDlg()
{
}

CAnimationRefManagerDlg::CAnimationRefManagerDlg(CWnd* pParent /*=NULL*/)
: CDialog(CAnimationRefManagerDlg::IDD, pParent)
, m_ActRefName1(_T(""))
{

}

