


#include "stdafx.h"
#include "EffectEditor.h"
#include "PropertyView.h"
#include "TabPropPanel.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CTabPropPanel, CDialog)

void CTabPropPanel::OnCancel()
{
}
void CTabPropPanel::OnOK()
{
}

void CTabPropPanel::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}

void CTabPropPanel::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

BOOL CTabPropPanel::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_pView = dynamic_cast<CPropertyView*>(GetParent());
	m_aniProp.Create(m_aniProp.IDD, this);
	m_audioProp.Create(m_audioProp.IDD, this);
	m_particleProp.Create(m_particleProp.IDD, this);
	m_aniEffectProp.Create(m_aniEffectProp.IDD, this);
	m_effectProp.Create(m_effectProp.IDD, this);
	m_roleProp.Create(m_roleProp.IDD, this);
	m_bIsInited = true;
	ResetSize();
	m_Tabs.InsertItem(0, L"特效属性");
	m_Tabs.InsertItem(1, L"人物属性");
	m_Tabs.ShowWindow(SW_SHOW);

	return TRUE;  

}

void CTabPropPanel::OnTcnSelchangeTab(NMHDR *pNMHDR, LRESULT *pResult)
{
	ResetCont();
	*pResult = 0;
}

BEGIN_MESSAGE_MAP(CTabPropPanel, CDialog)
	ON_NOTIFY(TCN_SELCHANGE, IDC_TAB, &CTabPropPanel::OnTcnSelchangeTab)
	ON_WM_SIZE()
	ON_WM_SIZING()
END_MESSAGE_MAP()




void CTabPropPanel::ResetCont()
{
	if (m_bIsInited)
	{
		CEffectEditorDoc *pDoc = m_pView->GetDocument();
		if (m_Tabs.GetCurSel() == 0)
		{
			const EffectClipSet& selectedClips = pDoc->GetSelectedClips();
			if (selectedClips.size() == 1 && pDoc->GetEffectFileType() == EFT_EFFECT)
			{
				switch ((*selectedClips.begin())->GetAnimation()->GetType())
				{
				case Nuclear::XPET_ANI:
					m_aniProp.ShowWindow(SW_SHOW);
					m_aniProp.ResetData();
					m_aniProp.Invalidate();
					m_audioProp.ShowWindow(SW_HIDE);
					m_particleProp.ShowWindow(SW_HIDE);
					m_aniEffectProp.ShowWindow(SW_HIDE);
					m_effectProp.ShowWindow(SW_HIDE);
					m_aniProp.BringWindowToTop();
					m_roleProp.ShowWindow(SW_HIDE);
					break;
				case Nuclear::XPET_PARTICLE:
					m_aniProp.ShowWindow(SW_HIDE);
					m_audioProp.ShowWindow(SW_HIDE);
					m_particleProp.ShowWindow(SW_SHOW);
					m_particleProp.ResetData();
					m_particleProp.Invalidate();
					m_aniEffectProp.ShowWindow(SW_HIDE);
					m_effectProp.ShowWindow(SW_HIDE);
					m_particleProp.BringWindowToTop();
					m_roleProp.ShowWindow(SW_HIDE);
					break;
				case Nuclear::XPET_AUDIO:
					m_aniProp.ShowWindow(SW_HIDE);
					m_audioProp.ShowWindow(SW_SHOW);
					m_audioProp.ResetData();
					m_audioProp.Invalidate();
					m_particleProp.ShowWindow(SW_HIDE);
					m_aniEffectProp.ShowWindow(SW_HIDE);
					m_effectProp.ShowWindow(SW_HIDE);
					m_roleProp.ShowWindow(SW_HIDE);
					m_audioProp.BringWindowToTop();
					break;
				case Nuclear::XPET_GEFFECT:
					m_aniProp.ShowWindow(SW_HIDE);
					m_audioProp.ShowWindow(SW_HIDE);
					m_particleProp.ShowWindow(SW_HIDE);
					m_aniEffectProp.ShowWindow(SW_SHOW);
					m_aniEffectProp.ResetData();
					m_aniEffectProp.Invalidate();
					m_effectProp.ShowWindow(SW_HIDE);
					m_aniEffectProp.BringWindowToTop();
					m_roleProp.ShowWindow(SW_HIDE);
					break;
				}
			} else {
				m_aniProp.ShowWindow(SW_HIDE);
				m_audioProp.ShowWindow(SW_HIDE);
				m_particleProp.ShowWindow(SW_HIDE);
				m_aniEffectProp.ShowWindow(SW_HIDE);
				m_effectProp.ShowWindow(SW_SHOW);
				m_effectProp.ResetData();
				m_effectProp.Invalidate();
				m_effectProp.BringWindowToTop();
				m_roleProp.ShowWindow(SW_HIDE);
			}
		} else {	
			m_aniProp.ShowWindow(SW_HIDE);
			m_audioProp.ShowWindow(SW_HIDE);
			m_aniEffectProp.ShowWindow(SW_HIDE);
			m_effectProp.ShowWindow(SW_HIDE);
			m_particleProp.ShowWindow(SW_HIDE);
			m_roleProp.ShowWindow(SW_SHOW);
			m_roleProp.ResetData();
			m_roleProp.Invalidate();
			m_roleProp.BringWindowToTop();
		}
	}
}


void CTabPropPanel::ResetSize()
{
	if (m_bIsInited)
	{
		CRect rect;
		GetClientRect(&rect);
		m_Tabs.MoveWindow(&rect);
		rect.top += 23;
		rect.left += 3;
		rect.right -= 5;
		rect.bottom -= 5;
		m_aniProp.MoveWindow(&rect, TRUE);
		m_audioProp.MoveWindow(&rect, TRUE);
		m_particleProp.MoveWindow(&rect, TRUE);
		m_aniEffectProp.MoveWindow(&rect, TRUE);
		m_effectProp.MoveWindow(&rect, TRUE);
		m_roleProp.MoveWindow(&rect, TRUE);
	}
}

void CTabPropPanel::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_TAB, m_Tabs);
}

CTabPropPanel::~CTabPropPanel()
{
}

CTabPropPanel::CTabPropPanel(CWnd* pParent /*=NULL*/)
: CDialog(CTabPropPanel::IDD, pParent)
{
	m_bIsInited = false;

}

