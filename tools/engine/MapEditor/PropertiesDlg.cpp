

#include "stdafx.h"
#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "CenterColorDialog.h"

#include "PropertiesDlg.h"




IMPLEMENT_DYNAMIC(CPropertiesDlg, CDialog)

void CPropertiesDlg::OnPaint()
{
	CPaintDC dc(this); 

	CBrush brushGround(ReverseColor(m_pMapEditorView->GetSelectedItemColor()) & 0x00FFFFFF);
	dc.FillRect(&m_ColorRect, &brushGround);

	ReleaseDC(&dc);
}
void CPropertiesDlg::TryInvalidata()
{
	if (m_hWnd == NULL)
	{
		return;
	}
	m_pMapEditorView->GetSelectedItemRect(m_Rect);
	UpdateData(FALSE);
	InvalidateRect(&m_ColorRect, FALSE);
}
void CPropertiesDlg::OnBnClickedSelectBackgroundColor()
{

	CCenterColorDialog dlg;
	if (dlg.DoModal() == IDOK)
	{
		unsigned int cColor = dlg.GetColor();
		m_pMapEditorView->SetSelectedItemColor(ReverseColor(cColor) | 0xFF000000);
		InvalidateRect(&m_ColorRect, FALSE);
	}
}

BOOL CPropertiesDlg::Create(CMapEditorView* pMapEditorView /*=NULL*/)
{

	m_pMapEditorView = pMapEditorView;
	return CDialog::Create(IDD, pMapEditorView);
}


BEGIN_MESSAGE_MAP(CPropertiesDlg, CDialog)
	ON_BN_CLICKED(IDC_SELECT_BACKGROUND_COLOR, &CPropertiesDlg::OnBnClickedSelectBackgroundColor)
	ON_WM_PAINT()
END_MESSAGE_MAP()

void CPropertiesDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_UP, m_Rect.top);
	DDX_Text(pDX, IDC_DOWN, m_Rect.bottom);
	DDX_Text(pDX, IDC_LEFT, m_Rect.left);
	DDX_Text(pDX, IDC_RIGHT, m_Rect.right);
}

CPropertiesDlg::~CPropertiesDlg()
{
}

CPropertiesDlg::CPropertiesDlg(CWnd* pParent /*=NULL*/)
: CDialog(CPropertiesDlg::IDD, pParent)
{
	m_Rect.left = m_Rect.right = m_Rect.top = m_Rect.bottom = 0;
	m_ColorRect.left = 71;
	m_ColorRect.top = 64;
	m_ColorRect.right = m_ColorRect.left + 30;
	m_ColorRect.bottom = m_ColorRect.top + 12;
}

