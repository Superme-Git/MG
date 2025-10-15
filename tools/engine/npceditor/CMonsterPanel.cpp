// CMonsterPanel.cpp : 实现文件
//

#include "stdafx.h"
#include "CMonsterPanel.h"
#include "MainDlg.h"
#include "MainFrm.h"
#include "SoundEditorView.h"

// CMonsterPanel 对话框

IMPLEMENT_DYNAMIC(CMonsterPanel, CDialog)

CMonsterPanel::CMonsterPanel(CWnd* pParent /*=NULL*/)
	: CDialog(CMonsterPanel::IDD, pParent)
{
	mMonsterIndex = -1;
}

CMonsterPanel::~CMonsterPanel()
{
}

BOOL CMonsterPanel::OnInitDialog()
{
	CDialog::OnInitDialog();	
	CreateMonCombo();
	
	return true;
}

void CMonsterPanel::CreateMonCombo()
{
	CComboBox* pMonsterCombo=(CComboBox*)GetDlgItem(IDC_MON_COMBO);
	CMainFrame* pMainFrame=(CMainFrame*)AfxGetApp()->m_pMainWnd;
	if (pMainFrame!=NULL)
	{
		CSoundEditorView* pMaiView=(CSoundEditorView*)pMainFrame->m_HSplitterWindow.GetPane(0,0);
		if (pMaiView)
		{
			m_pMainDlg=(CMainDlg*)(&(pMaiView->m_mainDlg));
			m_pMainDlg->m_soundApp.CreateMonCombo(pMonsterCombo);
		}
	}
}

void CMonsterPanel::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

void CMonsterPanel::GetMonsterKind(int &MonKind)
{
	MonKind = mMonsterKind;
}

void CMonsterPanel::SetMonsterKind(int MonKind)
{
	m_pMainDlg->m_soundApp.SetMonKind(MonKind);
}

void CMonsterPanel::GetMonsterIndex(int &MonIndex)
{
	MonIndex = mMonsterIndex;
}

int CMonsterPanel::GetSelectMonster()
{
	CComboBox* pMonsterCombo=(CComboBox*)GetDlgItem(IDC_MON_COMBO);
	int strMonId = pMonsterCombo->GetCurSel();
	
	return strMonId;
}

BEGIN_MESSAGE_MAP(CMonsterPanel, CDialog)
	ON_BN_CLICKED(ID_MONSTER_OK, &CMonsterPanel::OnBnClickedMonOk)
END_MESSAGE_MAP()


// CMonsterPanel 消息处理程序
void CMonsterPanel::OnBnClickedMonOk()
{
	// TODO: 在此添加控件通知处理程序代码
	mMonsterIndex = GetSelectMonster();
	mMonsterKind       = m_pMainDlg->m_soundApp.GetMonKind(mMonsterIndex);
	m_pMainDlg->m_soundApp.SetMonKind(mMonsterKind);
}
