


#include "stdafx.h"
#include "EffectEditor.h"
#include "EffectEditorDoc.h"
#include "ToolsPanel.h"
#include "EffectEditorView.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


IMPLEMENT_DYNAMIC(CToolsPanel, CDialog)

void CToolsPanel::OnBnClickedAddClip()
{
	if (m_pNotify)
	{
		m_pNotify->OnAddClip();
	}
}

BOOL CToolsPanel::OnInitDialog()
{
	CDialog::OnInitDialog();
	if (!m_Tools.CreateEx(this, TBSTYLE_CHECK, WS_CHILD|WS_VISIBLE|CBRS_TOP|CBRS_TOOLTIPS|CBRS_SIZE_FIXED|CBRS_ORIENT_VERT) ||
		!m_Tools.LoadToolBar(IDR_TOOL_BAR))
	{
		return FALSE;
	}
	m_bIsInited = true;
	ResetSize();

	return FALSE;  

}

void CToolsPanel::OnSizing(UINT fwSide, LPRECT pRect)
{
	CDialog::OnSizing(fwSide, pRect);
	ResetSize();
}

void CToolsPanel::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);
	ResetSize();
}

void CToolsPanel::ResetSize()
{
	if (!m_bIsInited)
		return;
	CRect rect;
	GetClientRect(&rect);
	rect.left += 4;
	rect.right -= 4;
	rect.top += 4;
	rect.bottom = rect.top + 20;
	GetDlgItem(IDC_ADD_CLIP)->MoveWindow(&rect);
	if (rect.Width() > 0)
	{
		rect.top += 26;
		rect.left -= 4;
		rect.right += 4;

		int toolbarBtnSize = m_Tools.GetToolBarCtrl().GetButtonSize();
		int toolbarBtnWidth = LOWORD(toolbarBtnSize);
		int toolbarBtnHeight = HIWORD(toolbarBtnSize);

		int colCount = rect.Width() / toolbarBtnWidth;
		int rowCount = (int)ceilf((float)m_Tools.GetToolBarCtrl().GetButtonCount() / colCount);
		rect.bottom = rect.top + rowCount * toolbarBtnHeight;
		CRect r;
		m_Tools.GetToolBarCtrl().SetRows(rowCount, FALSE, &r);
		m_Tools.CalcDynamicLayout(r.Width(), LM_HORZ | LM_COMMIT);
		m_Tools.MoveWindow(&rect);
	}

}



BEGIN_MESSAGE_MAP(CToolsPanel, CDialog)
	ON_BN_CLICKED(IDC_ADD_CLIP, &CToolsPanel::OnBnClickedAddClip)
	ON_WM_SIZE()
	ON_WM_SIZING()
	ON_COMMAND_RANGE(CEffectEditorView::FIRST_TOOL_ID, CEffectEditorView::LAST_TOOL_ID, &CToolsPanel::OnToolsControl)
END_MESSAGE_MAP()

void CToolsPanel::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}


CToolsPanel::~CToolsPanel()
{
}

CToolsPanel::CToolsPanel(CWnd* pParent /*=NULL*/)
: CDialog(CToolsPanel::IDD, pParent), m_bIsInited(false), m_pNotify(NULL)
{

}

