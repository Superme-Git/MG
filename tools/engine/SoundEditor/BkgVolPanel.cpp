// BkgVolPanel.cpp : implementation file
//

#include "stdafx.h"
#include "SoundEditor.h"
#include "BkgVolPanel.h"
#include "PropView.h"
#include "SoundEditorDoc.h"


// CBkgVolPanel dialog

IMPLEMENT_DYNAMIC(CBkgVolPanel, CDialog)

CBkgVolPanel::CBkgVolPanel(CWnd* pParent /*=NULL*/)
	: CDialog(CBkgVolPanel::IDD, pParent), m_pView(NULL)
	, m_nTransition(0)
	, m_fVolume(0.0f)
{

}

CBkgVolPanel::~CBkgVolPanel()
{
}

void CBkgVolPanel::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_EDIT_L, m_editL);
	DDX_Control(pDX, IDC_EDIT1, m_editVol);
	DDX_Text(pDX, IDC_EDIT_L, m_nTransition);
	DDX_Text(pDX, IDC_EDIT1, m_fVolume);
}


BEGIN_MESSAGE_MAP(CBkgVolPanel, CDialog)
	ON_EN_KILLFOCUS(IDC_EDIT_L, &CBkgVolPanel::OnEnKillfocusEditL)
	ON_EN_KILLFOCUS(IDC_EDIT1, &CBkgVolPanel::OnEnKillfocusEdit1)
	ON_BN_CLICKED(IDC_BUTTON_DELETE_CUTS, &CBkgVolPanel::OnBnClickedButtonDeleteCuts)
	ON_CONTROL_RANGE(BN_CLICKED, IDC_RADIO_POLYGON_NONE, IDC_RADIO_POLYGON_CUT, &CBkgVolPanel::OnBnClickedRadioPolygonRadio)
	ON_WM_LBUTTONDOWN()
END_MESSAGE_MAP()


// CBkgVolPanel message handlers

void CBkgVolPanel::OnEnKillfocusEditL()
{
	UpdateData();
	m_pView->GetDocument()->SetSelectedAreaTransition(m_nTransition);
}

void CBkgVolPanel::OnEnKillfocusEdit1()
{
	UpdateData();
	m_pView->GetDocument()->SetSelectedBkgVolAreaVol(m_fVolume);
}

void CBkgVolPanel::OnBnClickedButtonDeleteCuts()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	pDoc->ClearPolygonCuts(pDoc->m_pSelectedBkgVolArea);
}

void CBkgVolPanel::OnBnClickedRadioPolygonRadio(UINT nID)
{
	m_pView->GetDocument()->m_nPolygonEditState = nID - IDC_RADIO_POLYGON_NONE;
	m_pView->GetDocument()->m_PolygonMouseState = PMS_NORMAL;
}
BOOL CBkgVolPanel::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_pView = static_cast<CPropView*>(GetParent());
	ResetCont();

	return TRUE; 
}

void CBkgVolPanel::OnLButtonDown(UINT nFlags, CPoint point)
{
	GetDlgItem(IDC_STATIC_FADE_IN)->SetFocus();

	CDialog::OnLButtonDown(nFlags, point);
}

void CBkgVolPanel::ResetCont()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	if (!pDoc)
		return;
	static int sign = UVS_BKV_SOUND_AREA_SELECTED | UVS_TAB | UVS_LOADMAP | UVS_BKV_SOUND_AREA_ADD_DELETED;
	if (pDoc->m_nUpdateViewSign & sign)
	{
		if (pDoc->m_pSelectedBkgVolArea)
		{
			m_editL.EnableWindow(TRUE);
			m_editVol.EnableWindow(TRUE);
			m_nTransition = pDoc->m_pSelectedBkgVolArea->GetTransition();
			m_fVolume = pDoc->m_pSelectedBkgVolArea->GetVol();
			ShowPolygonProp(pDoc->m_pSelectedBkgVolArea->GetType() == Nuclear::XPAREA_TYPE_POLYGON);
		} else {
			m_editL.EnableWindow(FALSE);
			m_editVol.EnableWindow(FALSE);
			m_nTransition = 0;
			m_fVolume = 0.0f;
			ShowPolygonProp(FALSE);
		}
		UpdateData(FALSE);
	}
}

void CBkgVolPanel::ShowPolygonProp(BOOL b)
{
	GetDlgItem(IDC_RADIO_POLYGON_NONE)->ShowWindow(b?SW_SHOW:SW_HIDE);
	GetDlgItem(IDC_RADIO_POLYGON_BASE)->ShowWindow(b?SW_SHOW:SW_HIDE);
	GetDlgItem(IDC_RADIO_POLYGON_CUT)->ShowWindow(b?SW_SHOW:SW_HIDE);
	GetDlgItem(IDC_BUTTON_DELETE_CUTS)->ShowWindow(b?SW_SHOW:SW_HIDE);
	if (b)
	{
		CheckRadioButton(IDC_RADIO_POLYGON_NONE, IDC_RADIO_POLYGON_CUT, IDC_RADIO_POLYGON_NONE);
		m_pView->GetDocument()->m_nPolygonEditState = 0;
		m_pView->GetDocument()->m_PolygonMouseState = PMS_NORMAL;
	}
}