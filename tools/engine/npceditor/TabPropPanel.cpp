// TabPropPanel.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "TabPropPanel.h"
#include "PropView.h"
#include "SoundEditorDoc.h"



// CTabPropPanel dialog

IMPLEMENT_DYNAMIC(CTabPropPanel, CDialog)

CTabPropPanel::CTabPropPanel(CWnd* pParent /*=NULL*/)
	: CDialog(CTabPropPanel::IDD, pParent), m_bIsInited(false)
{
}

CTabPropPanel::~CTabPropPanel()
{
}

void CTabPropPanel::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_PROP_TAB, m_Tab);
}


BEGIN_MESSAGE_MAP(CTabPropPanel, CDialog)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_NOTIFY(TCN_SELCHANGE, IDC_PROP_TAB, &CTabPropPanel::OnTcnSelchangePropTab)
	ON_BN_CLICKED(IDC_BUTTON_JUMPISLAND, &CTabPropPanel::OnBnClickedButtonJumpisland)
END_MESSAGE_MAP()

void CTabPropPanel::ResetCont()
{
	m_miniMapPanel.BringWindowToTop();
	m_miniMapPanel.ResetCont();
	static int sign = UVS_ENV_SOUND_AREA_SELECTED | UVS_TAB;
	if (m_pView->GetDocument()->m_nUpdateViewSign & sign)
	{
		switch (m_Tab.GetCurSel())
		{
		case EDIT_STATE_MAZE:
			theApp.EnableSecToolBar(true);
			break;
		case EDIT_STATE_NPC:
			theApp.EnableSecToolBar(true);
			break;
		case EDIT_STATE_TESTING:
			theApp.EnableSecToolBar(false);
			break;
		}
	}
}

void CTabPropPanel::ResetSize()
{
	if (m_bIsInited)
	{
		CRect rect;
		GetClientRect(&rect);
		int oldBottom = rect.bottom;
		rect.bottom = rect.top + 150;
		m_miniMapPanel.MoveWindow(&rect);
		rect.top = rect.bottom;
		rect.bottom = oldBottom;
		m_Tab.MoveWindow(&rect);
		rect.top += 23;
		rect.left += 5;
		rect.right -= 5;
		rect.bottom -= 5;
	}
}


// CTabPropPanel message handlers

BOOL CTabPropPanel::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_pView = dynamic_cast<CPropView*>(GetParent());
	m_miniMapPanel.Create(m_miniMapPanel.IDD, m_pView);
	
	
	m_Tab.InsertItem(0, L"»·¾³Òô");
	m_Tab.InsertItem(1, L"±à¼­NPC");
	m_Tab.InsertItem(2, L"±à¼­Ìø×ªµã");
	m_Tab.InsertItem(3, L"²âÊÔ");
	m_Tab.InsertItem(4, L"¹ÖÊÞ");
	
	TCITEM* pTabCtrlItem=NULL;
	m_Tab.GetItem(1,pTabCtrlItem);
	m_pdlgNpcPropertyDlg=new CNpcPropertyDlg;
	m_pJPInfDlg=new CJumpPointInfDlg;
	//2015 Yao
	m_pMonPanel = new CMonsterPanel;


	m_pdlgNpcPropertyDlg->Create(IDD_NPCPANEL,&m_Tab);
	m_pJPInfDlg->Create(IDD_DIALOG_JPINF,&m_Tab);
	
	//2015 Yao
	m_pMonPanel->Create(IDD_MONSTER_PANEL,&m_Tab);
	m_pMonPanel->SetWindowPos(NULL,8,30,0,0,SWP_NOSIZE);
	m_pMonPanel->ShowWindow(SW_HIDE);
	
	m_pdlgNpcPropertyDlg->SetWindowPos(NULL,8,30,0,0,SWP_NOSIZE);
	m_pdlgNpcPropertyDlg->ShowWindow(SW_HIDE);


	m_pJPInfDlg->SetWindowPos(NULL,1040,250,0,0,SWP_NOSIZE);
	m_pJPInfDlg->ShowWindow(SW_HIDE);


	m_Tab.SetCurSel(0);
	m_bIsInited = true;
	ResetSize();
	ResetCont();
	return FALSE;
}

void CTabPropPanel::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

void CTabPropPanel::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}

void CTabPropPanel::OnTcnSelchangePropTab(NMHDR *pNMHDR, LRESULT *pResult)
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	pDoc->SetEditState(static_cast<EDIT_STATE>(m_Tab.GetCurSel()));

	int nCurSel=m_Tab.GetCurSel();
	if (nCurSel==1)
	{
		m_pdlgNpcPropertyDlg->ShowWindow(SW_SHOW);
	}else
	{
		m_pdlgNpcPropertyDlg->ShowWindow(SW_HIDE);

	}

	if (nCurSel==2)
	{
		m_pJPInfDlg->ShowWindow(SW_SHOW);
	}else
	{
		m_pJPInfDlg->ShowWindow(SW_HIDE);

	}

	//2015 Yao
	if (nCurSel==4)
	{
		m_pMonPanel->ShowWindow(SW_SHOW);
	}else
	{
		m_pMonPanel->ShowWindow(SW_HIDE);

	}

	pDoc->m_nUpdateViewSign = UVS_TAB;
	pDoc->UpdateAllViews(NULL);
	pDoc->m_nUpdateViewSign = 0;
	ResetCont();
	*pResult = 0;
}

void CTabPropPanel::OnBnClickedButtonJumpisland()
{
	// TODO: Add your control notification handler code here
	CEdit* pEdit=(CEdit*)GetDlgItem(IDC_EDIT_ISLANDIDX);
	if (pEdit)
	{
		CString cstrIdx;
		pEdit->GetWindowText(cstrIdx);
		int idx=_ttoi(cstrIdx.GetBuffer());

		if (m_pJPInfDlg)
		{
			m_pJPInfDlg->JumpIsland(idx);
		}
	}
}


