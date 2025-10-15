

#include "stdafx.h"
#include "EffectEditor.h"
#include "DlgPropColor.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CDlgPropColor, CDialog)

void CDlgPropColor::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}
void CDlgPropColor::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

BOOL CDlgPropColor::OnInitDialog()
{
	CDialog::OnInitDialog();

	m_colorCtrl.Create(m_colorCtrl.IDD, this);
	int endFrame, startFrame;
	eAnimationPlayType playType;
	const Nuclear::AbstractEffectClip* pClip = *m_pDoc->GetSelectedClips().begin();
	m_pDoc->GetClipStartEndFrame(pClip, playType, startFrame, endFrame);
	if (playType == APT_SPEC_LOOP)
	{
		endFrame = m_pDoc->GetTotalFrames();
	}
	m_colorCtrl.SetMaxX(endFrame - startFrame + 1);
	colorbarctrl::CColorBarCtrl::Colors colors;
	int count = pClip->GetKeyCount(Nuclear::AbstractEffectClip::KT_COLOR);
	int i;
	int frame;
	Nuclear::XPCOLOR v;
	for (i=0;i<count;++i)
	{
		pClip->GetColorKeyValue(i, frame, v);
		colors[frame] = RGB(v.r, v.g, v.b);
	}
	m_colorCtrl.SetColors(colors);
	ResetSize();
	m_colorCtrl.ShowWindow(SW_SHOW);
	return TRUE;  

}

void CDlgPropColor::OnBnClickedOk()
{
	const colorbarctrl::CColorBarCtrl::Colors &colors = m_colorCtrl.GetColors();
	colorbarctrl::CColorBarCtrl::Colors::const_iterator iter = colors.begin();
	ColorKeyMap keyMap;
	Nuclear::XPCOLOR v;
	v.a = 255;
	for (;iter!=colors.end();++iter)
	{
		v.r = GetRValue(iter->second);
		v.g = GetGValue(iter->second);
		v.b = GetBValue(iter->second);
		keyMap[iter->first] = v;
	}
	m_pDoc->SetSelectedClipColors(keyMap);
	OnOK();
}

BEGIN_MESSAGE_MAP(CDlgPropColor, CDialog)
	ON_BN_CLICKED(IDOK, &CDlgPropColor::OnBnClickedOk)
	ON_WM_SIZE()
	ON_WM_SIZING()
END_MESSAGE_MAP()




void CDlgPropColor::ResetSize()
{
	if (!m_colorCtrl.m_hWnd)
		return;
	CRect rect;
	GetClientRect(&rect);
	int width = rect.Width();
	rect.top += 10;
	rect.left += 10;
	rect.right -= 10;
	rect.bottom -= 30;
	m_colorCtrl.MoveWindow(&rect);
	rect.top = rect.bottom + 5;
	rect.bottom = rect.top + 20;
	rect.left = width / 4 - 25;
	rect.right = rect.left + 50;
	m_buttonOK.MoveWindow(&rect);
	rect.left += width / 2;
	rect.right += width / 2;
	m_buttonCancel.MoveWindow(&rect);
}


void CDlgPropColor::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	DDX_Control(pDX, IDOK, m_buttonOK);
	DDX_Control(pDX, IDCANCEL, m_buttonCancel);
}

CDlgPropColor::~CDlgPropColor()
{
}

CDlgPropColor::CDlgPropColor(CEffectEditorDoc* pDoc)
: CDialog(CDlgPropColor::IDD, NULL), m_pDoc(pDoc)
{

}

