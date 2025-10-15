// MiniMapPanel.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "MiniMapPanel.h"
#include "PropView.h"
#include "SoundEditorDoc.h"
// CMiniMapPanel dialog

IMPLEMENT_DYNAMIC(CMiniMapPanel, CDialog)

CMiniMapPanel::CMiniMapPanel(CWnd* pParent /*=NULL*/)
	: CDialog(CMiniMapPanel::IDD, pParent), m_bIsInited(false)
{

}

CMiniMapPanel::~CMiniMapPanel()
{
}

void CMiniMapPanel::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_SLIDER_SCALE, m_sliderScale);
}


BEGIN_MESSAGE_MAP(CMiniMapPanel, CDialog)
	ON_WM_SIZE()
	ON_WM_SIZING()
END_MESSAGE_MAP()


void CMiniMapPanel::ResetSize()
{
	if (m_bIsInited)
	{
		CRect rect;
		GetClientRect(&rect);
		rect.bottom -= 2;
		rect.top = rect.bottom - 20;
		m_sliderScale.MoveWindow(&rect);
	}
}

void CMiniMapPanel::ResetCont()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	if (pDoc->m_nUpdateViewSign & UVS_ENV_SOUND_AREA_SHAPE)
	{

	}

	if (pDoc->m_nUpdateViewSign & UVS_VIEWPORT)
	{

	}
}

// CMiniMapPanel message handlers

void CMiniMapPanel::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

void CMiniMapPanel::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}

BOOL CMiniMapPanel::OnInitDialog()
{
	CDialog::OnInitDialog();
	m_bIsInited = true;
	m_pView = static_cast<CPropView*>(GetParent());
	m_sliderScale.SetRange(0, 100);
	ResetSize();
	ResetCont();
	return TRUE;
}
