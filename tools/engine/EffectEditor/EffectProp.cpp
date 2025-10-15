

#include "stdafx.h"
#include "EffectEditor.h"
#include "PropertyView.h"
#include "EffectProp.h"
#include "EffectEditorDoc.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CEffectProp, CDialog)

void CEffectProp::OnBnClickedCheckBindType()
{
	UpdateData();
	m_pView->GetDocument()->SetBindType(m_bBindType?Nuclear::XPEBT_EFFECT_POS:Nuclear::XPEBT_ORIGIN);
}

void CEffectProp::OnLButtonDown(UINT nFlags, CPoint point)
{
	if (m_pView->GetDocument()->GetEffectFileType() == EFT_EFFECT)
		GetDlgItem(IDC_TOTAL_FRAME)->SetFocus();
	CDialog::OnLButtonDown(nFlags, point);
}

void CEffectProp::OnCancel()
{
	ResetData();

}

void CEffectProp::OnOK()
{

}

void CEffectProp::ResetData()
{
	CEffectEditorDoc* pDoc = m_pView->GetDocument();
	m_fFPS = pDoc->GetFPS();
	switch (pDoc->GetEffectFileType())
	{
	case EFT_EFFECT:
		{
			GetDlgItem(IDC_EDIT_FPS)->ShowWindow(SW_SHOW);
			GetDlgItem(IDC_TOTAL_FRAME)->ShowWindow(SW_SHOW);
			GetDlgItem(IDC_TOTAL_TIME)->ShowWindow(SW_SHOW);
			GetDlgItem(IDC_CHECK_BIND_TYPE)->ShowWindow(SW_SHOW);
			GetDlgItem(IDC_EDIT_FPS)->EnableWindow(TRUE);
			CString str;
			str.Format(L"%d", pDoc->GetTotalFrames());
			GetDlgItem(IDC_TOTAL_FRAME)->SetWindowText(str);
			str.Format(L"%f", pDoc->GetTotalFrames() / pDoc->GetFPS());
			GetDlgItem(IDC_TOTAL_TIME)->SetWindowText(str);

			m_bBindType = (pDoc->GetBindType() == Nuclear::XPEBT_EFFECT_POS);
			UpdateData(FALSE);
		}
		break;
	case EFT_SEBIND:
		GetDlgItem(IDC_EDIT_FPS)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_TOTAL_FRAME)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_TOTAL_TIME)->ShowWindow(SW_HIDE);
		GetDlgItem(IDC_CHECK_BIND_TYPE)->ShowWindow(SW_HIDE);
		break;
	}

	Invalidate();
}

BOOL CEffectProp::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_pView = dynamic_cast<CPropertyView*>(GetParent()->GetParent());

	return FALSE;  

}

void CEffectProp::OnEnKillfocusEditFps()
{
	UpdateData(TRUE);
	CEffectEditorDoc* pDoc = m_pView->GetDocument();
	float fFps = pDoc->GetFPS();
	if (fabs(fFps - m_fFPS) > EPSILON)
	{
		pDoc->SetFPS(m_fFPS);
		pDoc->m_bNeedReplay = true;
		pDoc->UpdateAllViews(m_pView);
		pDoc->SetModifiedFlag();
		ResetData();
	}
}

BEGIN_MESSAGE_MAP(CEffectProp, CDialog)
	ON_EN_KILLFOCUS(IDC_EDIT_FPS, &CEffectProp::OnEnKillfocusEditFps)
	ON_WM_LBUTTONDOWN()
	ON_BN_CLICKED(IDC_CHECK_BIND_TYPE, &CEffectProp::OnBnClickedCheckBindType)
END_MESSAGE_MAP()




void CEffectProp::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT_FPS, m_fFPS);
	DDX_Check(pDX, IDC_CHECK_BIND_TYPE, m_bBindType);
}


CEffectProp::~CEffectProp()
{
}

CEffectProp::CEffectProp(CWnd* pParent /*=NULL*/)
: CDialog(CEffectProp::IDD, pParent)
, m_fFPS(0)
, m_bBindType(FALSE)
{

}

