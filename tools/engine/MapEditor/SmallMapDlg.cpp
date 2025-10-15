

#include "stdafx.h"
#include "MapEditor.h"
#include "MapEditorDoc.h"
#include "MapEditorView.h"
#include "SmallMapDlg.h"

#include "..\engine\map\pimg.h"
#include "..\engine\common\log.h"
#include "..\engine\common\pfsutil.h"
#include "..\engine\world\lkotreenodelist.h"



#ifdef _DEBUG
#define new DEBUG_NEW
#endif

IMPLEMENT_DYNAMIC(CSmallMapDlg, CDialog)

void CSmallMapDlg::OnExportSmallMap()
{
	CString path, tmpName;
	Nuclear::PMap& map = m_pMapEditorView->GetDocument()->GetToolsMap();
	if (m_nViewportWidth < map.GetWidth() || m_nViewportHeight < map.GetHeight())
	{
		if (AfxMessageBox(_T("当前窗口并没有完全显示地图，\n这将导致小地图显示不完整，是否继续？"), MB_YESNO) != IDYES)
		{
			return;
		}
	}

	if (!CMapEditorApp::SelectExportFolder(path, tmpName, GetSafeHwnd()))
		return ;
	path = path + _T("\\mini_map.jpg");
	Nuclear::CRECT rect(0, 0, static_cast<LONG>(m_nViewportWidth * m_fRatio), static_cast<LONG>(m_nViewportHeight * m_fRatio));

	void* buf;
	buf = NULL;
	int size;
	m_bIsPrintScreen = true;
	drawMap();
	if (m_pRenderer->PrintScreen(&buf, &size, Nuclear::XPIFF_JPG, &rect))
	{

		FILE *f = NULL;
		_tfopen_s(&f, path, _T("wb"));
		if (f)
		{
			Nuclear::FWrite(f, buf, size);
			fclose(f);
		}
		Nuclear::XFree(buf);
	}
	m_bIsPrintScreen = false;
}
void CSmallMapDlg::OnKeyDown(UINT nChar, UINT nRepCnt, UINT nFlags)
{

	switch(nChar)
	{
	case VK_ADD:
		OnPopZoomIn();
		InvalidateRect(&m_DestRect);
		break;
	case VK_SUBTRACT:
		OnPopZoomOut();
		InvalidateRect(&m_DestRect);
		break;
	}
	__super::OnKeyDown(nChar, nRepCnt, nFlags);
}

void CSmallMapDlg::OnPopZoomOut()
{

	SetRatio(m_fRatio / static_cast<float>(1.5));
	ResetScrollBar();
}

void CSmallMapDlg::OnPopZoomIn()
{


	SetRatio(m_fRatio * static_cast<float>(1.5));
	ResetScrollBar();
}

void CSmallMapDlg::OnPopNonzoom()
{

	SetRatio(1);
	ResetScrollBar();
}

void CSmallMapDlg::OnPopFullScreen()
{

	float tmpRatio;
	Nuclear::PMap & map = m_pMapEditorView->GetDocument()->GetToolsMap();
	SetRatio(static_cast<float>(m_nDestWidth) / map.GetWidth());
	tmpRatio = static_cast<float>(m_nDestHeight) / map.GetHeight();
	if (tmpRatio < m_fRatio)
		SetRatio(tmpRatio);
	ResetScrollBar();
}

void CSmallMapDlg::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{

	TransScrollPosition(SB_HORZ, nSBCode, nPos);
	m_DrawScrollPosition.x = GetScrollPos(SB_HORZ);
	TryInvalidate();
	__super::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CSmallMapDlg::OnContextMenu(CWnd* /*pWnd*/, CPoint point)
{

	CMenu* popup = m_PopMenu.GetSubMenu(0);
	XPASSERT(popup != NULL);
	popup->TrackPopupMenu(TPM_LEFTALIGN|TPM_RIGHTBUTTON, point.x, point.y, this);

}

void CSmallMapDlg::OnVScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{

	TransScrollPosition(SB_VERT, nSBCode, nPos);
	m_DrawScrollPosition.y = GetScrollPos(SB_VERT);
	TryInvalidate();
	__super::OnVScroll(nSBCode, nPos, pScrollBar);
}

void CSmallMapDlg::TransScrollPosition(int nBar, UINT nSBCode, UINT nPos)
{
	SCROLLINFO info;
	GetScrollInfo(nBar, &info, SIF_ALL);  
	switch(nSBCode)  
	{  
	case   SB_PAGEUP:  

		info.nPos -= info.nPage;
		break;  
	case   SB_PAGEDOWN:  

		info.nPos += info.nPage;
		break;  
	case   SB_BOTTOM:  

		info.nPos = info.nMax;
		break;  
	case   SB_LINEDOWN:  

		info.nPos += info.nPage / 10;
		break;  
	case   SB_LINEUP:  

		info.nPos -= info.nPage / 10;
		break;  
	case   SB_TOP: 
		info.nPos = info.nMin;

		break;  
	case   SB_THUMBTRACK:  
	case   SB_THUMBPOSITION:  

		info.nPos = nPos;  
		break;  
	}  
	info.fMask = SIF_POS;
	SetScrollInfo(nBar, &info);
}


void CSmallMapDlg::OnOK()
{

}

void CSmallMapDlg::SetClientPos(CPoint point)
{
	point.x = static_cast<LONG>(point.x / m_fRatio);
	point.y = static_cast<LONG>(point.y / m_fRatio);
	Nuclear::PMap& map = m_pMapEditorView->GetDocument()->GetToolsMap();

	Nuclear::CRECT mainViewport = m_pMapEditorView->GetViewport();
	int clientWidth, clientHeight;
	clientWidth = mainViewport.Width();
	clientHeight = mainViewport.Height();

	point.x += m_DrawScrollPosition.x - clientWidth / 2;
	point.y += m_DrawScrollPosition.y - clientHeight / 2;

	if (point.x + clientWidth > map.GetWidth())
	{
		point.x = map.GetWidth() - clientWidth;
	}
	if (point.y + clientHeight > map.GetHeight())
	{
		point.y = map.GetHeight() - clientHeight;
	}
	if (point.x < 0)
		point.x = 0;
	if (point.y < 0)
		point.y = 0;

	m_pMapEditorView->SetScrollPos(SB_HORZ, static_cast<int>(point.x * m_pMapEditorView->GetRatio()));
	m_pMapEditorView->SetScrollPos(SB_VERT, static_cast<int>(point.y * m_pMapEditorView->GetRatio()));
	TryInvalidate();
}

void CSmallMapDlg::OnMouseMove(UINT nFlags, CPoint point)
{

	if (m_bLButtonDown)
	{
		SetClientPos(point);
	}
	CDialog::OnMouseMove(nFlags, point);
}

void CSmallMapDlg::OnLButtonUp(UINT nFlags, CPoint point)
{

	m_bLButtonDown = false;
	ReleaseCapture();
	SetClientPos(point);
	CDialog::OnLButtonUp(nFlags, point);
}

void CSmallMapDlg::OnLButtonDown(UINT nFlags, CPoint point)
{

	m_bLButtonDown = true;
	SetCapture();
	SetClientPos(point);
	CDialog::OnLButtonDown(nFlags, point);
}

BOOL CSmallMapDlg::OnEraseBkgnd(CDC* pDC)
{
	return TRUE;
}

void CSmallMapDlg::TryInvalidate(bool changeSize)
{
	if (m_hWnd != NULL && IsWindowVisible())
	{
		if (changeSize)
		{
			ResetDestAndSourceRects();
			m_ScreenElements.ClearLayers();
		}
		m_ScreenElements.SetMustRenew();
		InvalidateRect(&m_DestRect);
	}
}
bool CSmallMapDlg::drawSelectedRects()
{
	if (m_bIsPrintScreen)
		return true;
	Nuclear::CRECT rect = m_pMapEditorView->GetViewport();
	float x1,y1,x2,y2;
	x1 = (rect.left - m_DrawScrollPosition.x) * m_fRatio;
	y1 = (rect.top - m_DrawScrollPosition.y) * m_fRatio;
	x2 = (rect.right - m_DrawScrollPosition.x) * m_fRatio;
	y2 = (rect.bottom - m_DrawScrollPosition.y) * m_fRatio;
	m_pRenderer->DrawBox(x1,y1,x2-1,y2-1,0xFFFF0000,Nuclear::XPFM_WIREFRAME);
	m_pRenderer->DrawBox(x1+1,y1+1,x2-2,y2-2,0xFFFF0000,Nuclear::XPFM_WIREFRAME);
	return true;
}

void CSmallMapDlg::ResetDestAndSourceRects()
{
	checkRenderer();
	GetDestRect();
	m_pRenderer->SetDestRect(&m_DestRect);
	m_pRenderer->SetSourceRect(&m_DestRect);
	ResetScrollBar();
	Invalidate();
}

void CSmallMapDlg::OnSize(UINT nType, int cx, int cy)
{
	CDialog::OnSize(nType, cx, cy);

	ResetDestAndSourceRects();
}


void CSmallMapDlg::OnPaint()
{
	CPaintDC dc(this); 

	drawMap();
}

void CSmallMapDlg::GetDestRect() 
{
	Nuclear::PMap & map = m_pMapEditorView->GetDocument()->GetToolsMap();
	GetClientRect(&m_DestRect);
	m_nDestWidth = m_DestRect.right - m_DestRect.left;
	m_nDestHeight = m_DestRect.bottom - m_DestRect.top;
	m_nViewportWidth = static_cast<int>(m_nDestWidth / m_fRatio);
	m_nViewportHeight = static_cast<int>(m_nDestHeight / m_fRatio);

	if (m_nViewportWidth > map.GetWidth())
	{
		m_nViewportWidth = map.GetWidth();
	}

	if (m_nViewportHeight > map.GetHeight())
	{
		m_nViewportHeight = map.GetHeight();
	}
}

void CSmallMapDlg::ResetScrollBar()
{
	GetDestRect();
	SCROLLINFO info;
	info.cbSize = sizeof(SCROLLINFO);
	info.fMask = SIF_ALL;
	info.nMax = m_pMapEditorView->GetDocument()->GetToolsMap().GetHeight() - m_nViewportHeight;
	info.nMin = 0;
	info.nPage = info.nMax / 20;
	if (m_DrawScrollPosition.y > info.nMax)
		m_DrawScrollPosition.y = info.nMax;
	info.nPos = m_DrawScrollPosition.y;
	SetScrollInfo(SB_VERT, &info, FALSE);


	info.nMax = m_pMapEditorView->GetDocument()->GetToolsMap().GetWidth() - m_nViewportWidth;
	info.nPage = info.nMax / 20;
	if (m_DrawScrollPosition.x > info.nMax)
		m_DrawScrollPosition.x = info.nMax;
	info.nPos = m_DrawScrollPosition.x;
	SetScrollInfo(SB_HORZ, &info);
}
CMapEditorDoc* CSmallMapDlg::GetDocument() const
{
	return m_pMapEditorView->GetDocument();
}

const Nuclear::XPColorBalanceParam* CSmallMapDlg::GetTimeEffectParam() const 
{
	return m_pMapEditorView->GetTimeEffectParam(); 
}

BOOL CSmallMapDlg::Create(CMapEditorView* pMapEditorView/* = NULL*/)
{

	m_pMapEditorView = pMapEditorView;
	BOOL result = CDialog::Create(IDD, pMapEditorView);
	if (result) 
	{
		m_ScreenElements.Init(pMapEditorView->GetDocument(), this);
	}
	VERIFY(m_PopMenu.LoadMenu(IDR_MENU_POP));
	SetRatio(1.0f);
	m_DrawScrollPosition.x = 0;
	m_DrawScrollPosition.y = 0;
	ResetScrollBar();

	return result;
}

BEGIN_MESSAGE_MAP(CSmallMapDlg, CDialog)
	ON_WM_PAINT()
	ON_WM_SIZE()
	ON_WM_ERASEBKGND()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_WM_VSCROLL()
	ON_WM_CONTEXTMENU()
	ON_WM_HSCROLL()
	ON_COMMAND(ID_POP_FULL_SCREEN, &CSmallMapDlg::OnPopFullScreen)
	ON_COMMAND(ID_POP_NONZOOM, &CSmallMapDlg::OnPopNonzoom)
	ON_COMMAND(ID_POP_ZOOM_IN, &CSmallMapDlg::OnPopZoomIn)
	ON_COMMAND(ID_POP_ZOOM_OUT, &CSmallMapDlg::OnPopZoomOut)
	ON_WM_KEYDOWN()
	ON_COMMAND(ID_EXPORT_SMALL_MAP, &CSmallMapDlg::OnExportSmallMap)
END_MESSAGE_MAP()

void CSmallMapDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
}

CSmallMapDlg::~CSmallMapDlg()
{
}

CSmallMapDlg::CSmallMapDlg(CWnd* pParent /*=NULL*/)
: CDialog(CSmallMapDlg::IDD, pParent) , m_bLButtonDown(false), m_bIsPrintScreen(false)
{
	m_bShowBase = false;
}

