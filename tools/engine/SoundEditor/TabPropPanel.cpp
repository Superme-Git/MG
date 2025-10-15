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
END_MESSAGE_MAP()

void CTabPropPanel::ResetCont()
{
	m_miniMapPanel.BringWindowToTop();
	m_miniMapPanel.ResetCont();
	static int sign = UVS_ENV_SOUND_AREA_SELECTED | UVS_BKG_SOUND_AREA_SELECTED | UVS_TAB | UVS_TEST_SET_GAMETIME;
	if (m_pView->GetDocument()->m_nUpdateViewSign & UVS_TAB)
	{
		CSoundEditorDoc *pDoc = m_pView->GetDocument();
		if (pDoc->GetEditState() != m_Tab.GetCurSel())
			m_Tab.SetCurSel(pDoc->GetEditState());
		switch (m_Tab.GetCurSel())
		{
		case EDIT_STATE_ENV_SOUND:
			m_testPanel.ShowWindow(SW_HIDE);
			m_envPanel.ShowWindow(SW_SHOW);
			m_bkgPanel.ShowWindow(SW_HIDE);
			m_bkgVolPanel.ShowWindow(SW_HIDE);
			m_stepPanel.ShowWindow(SW_HIDE);
			m_envPanel.BringWindowToTop();
			theApp.EnableSecToolBar(true);
			break;
		case EDIT_STATE_BKG_MUSIC:
			m_testPanel.ShowWindow(SW_HIDE);
			m_envPanel.ShowWindow(SW_HIDE);
			m_bkgVolPanel.ShowWindow(SW_HIDE);
			m_stepPanel.ShowWindow(SW_HIDE);
			m_bkgPanel.ShowWindow(SW_SHOW);
			m_bkgPanel.BringWindowToTop();
			theApp.EnableSecToolBar(true);
			break;
		case EDIT_STATE_BKG_VOL:
			m_testPanel.ShowWindow(SW_HIDE);
			m_envPanel.ShowWindow(SW_HIDE);
			m_stepPanel.ShowWindow(SW_HIDE);
			m_bkgVolPanel.ShowWindow(SW_SHOW);
			m_bkgPanel.ShowWindow(SW_HIDE);
			m_bkgVolPanel.BringWindowToTop();
			theApp.EnableSecToolBar(true);
			break;
		case EDIT_STATE_STEP:
			m_testPanel.ShowWindow(SW_HIDE);
			m_envPanel.ShowWindow(SW_HIDE);
			m_bkgPanel.ShowWindow(SW_HIDE);
			m_bkgVolPanel.ShowWindow(SW_HIDE);
			m_stepPanel.ShowWindow(SW_SHOW);
			m_stepPanel.BringWindowToTop();
			theApp.EnableSecToolBar(false);
			break;
		case EDIT_STATE_TESTING:
			m_testPanel.ShowWindow(SW_SHOW);
			m_envPanel.ShowWindow(SW_HIDE);
			m_bkgPanel.ShowWindow(SW_HIDE);
			m_bkgVolPanel.ShowWindow(SW_HIDE);
			m_stepPanel.ShowWindow(SW_HIDE);
			m_testPanel.BringWindowToTop();
			theApp.EnableSecToolBar(false);
			break;
		}
	}
	m_envPanel.ResetCont();
	m_bkgPanel.ResetCont();
	m_testPanel.ResetCont();
	m_stepPanel.ResetCont();
	m_bkgVolPanel.ResetCont();
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
		m_testPanel.MoveWindow(&rect);
		m_envPanel.MoveWindow(&rect);
		m_bkgPanel.MoveWindow(&rect);
		m_bkgVolPanel.MoveWindow(&rect);
		m_stepPanel.MoveWindow(&rect);
	}
}


// CTabPropPanel message handlers

BOOL CTabPropPanel::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_pView = dynamic_cast<CPropView*>(GetParent());
	m_testPanel.Create(m_testPanel.IDD, m_pView);
	m_envPanel.Create(m_envPanel.IDD, m_pView);
	m_bkgPanel.Create(m_bkgPanel.IDD, m_pView);
	m_bkgVolPanel.Create(m_bkgVolPanel.IDD, m_pView);
	m_stepPanel.Create(m_stepPanel.IDD, m_pView);
	m_miniMapPanel.Create(m_miniMapPanel.IDD, m_pView);
	m_Tab.InsertItem(0, L"»·¾³Òô");
	m_Tab.InsertItem(1, L"±³¾°Òô");
	m_Tab.InsertItem(2, L"±³¾°ÒôÁ¿");
	m_Tab.InsertItem(3, L"½Å²½");
	m_Tab.InsertItem(4, L"²âÊÔ");
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
	pDoc->m_nUpdateViewSign = UVS_TAB;
	pDoc->m_pSelectedShape = NULL;
	pDoc->m_pSelectedEnvArea = NULL;
	pDoc->m_pSelectedBkgArea = NULL;
	pDoc->UpdateAllViews(NULL);
	pDoc->m_nUpdateViewSign = 0;
	ResetCont();
	*pResult = 0;
}
