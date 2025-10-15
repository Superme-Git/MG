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
	: CDialog(CMiniMapPanel::IDD, pParent), m_bIsInited(false), m_bImgValid(false),
	m_mouseState(MOUSE_STATE_NORMAL), m_fRatio(1.0f)
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
	ON_WM_PAINT()
	ON_WM_HSCROLL()
	ON_WM_LBUTTONDOWN()
	ON_WM_LBUTTONUP()
	ON_WM_RBUTTONDOWN()
	ON_WM_RBUTTONUP()
	ON_WM_MOUSEMOVE()
	ON_WM_TIMER()
END_MESSAGE_MAP()


void CMiniMapPanel::ResetSize()
{
	if (m_bIsInited)
	{
		CRect rect;
		GetClientRect(&rect);
		m_miniMapRect = rect;
		rect.bottom -= 2;
		rect.top = rect.bottom - 20;
		m_sliderScale.MoveWindow(&rect);
		m_miniMapRect.DeflateRect(2, 2);
		m_miniMapRect.bottom = rect.top - 2;
		ResetMinRatio();
	}
}

void CMiniMapPanel::ResetMinRatio()
{
	if (m_bImgValid)
	{
		m_fMinRatio = static_cast<float>(m_miniMapRect.Width()) / m_nMiniMapWidth;
		float tmpRatio = static_cast<float>(m_miniMapRect.Height()) / m_nMiniMapHeight;
		if (tmpRatio < m_fMinRatio)
			m_fMinRatio = tmpRatio;
	}
}

void CMiniMapPanel::ResetEngineViewport()
{
	if (m_bImgValid)
	{
		CSoundEditorDoc* pDoc = m_pView->GetDocument();
		const static CSize vpsize(1024, 768);
		m_engineViewport = CRect(pDoc->GetViewport(), vpsize);

		if(pDoc->GetMapWidth()!=0)
		{
			m_engineViewport.left = m_engineViewport.left * m_nMiniMapWidth / pDoc->GetMapWidth();
		}else
		{
			m_engineViewport.left = m_engineViewport.left * m_nMiniMapWidth;
		}

		if(pDoc->GetMapWidth()!=0)
		{
			m_engineViewport.right = m_engineViewport.right * m_nMiniMapWidth / pDoc->GetMapWidth();
		}else
		{
			m_engineViewport.right = m_engineViewport.right * m_nMiniMapWidth;
		}

		if(pDoc->GetMapHeight()!=0)
		{
			m_engineViewport.top = m_engineViewport.top * m_nMiniMapHeight / pDoc->GetMapHeight();
		}else
		{
			m_engineViewport.top = m_engineViewport.top * m_nMiniMapHeight;
		}

		if(pDoc->GetMapHeight()!=0)
		{
			m_engineViewport.bottom = m_engineViewport.bottom * m_nMiniMapHeight / pDoc->GetMapHeight();
		}else
		{
			m_engineViewport.bottom = m_engineViewport.bottom * m_nMiniMapHeight;
		}
	}
}

void CMiniMapPanel::ResetCont()
{
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	bool needInvalid = false;
	if (pDoc->m_nUpdateViewSign & UVS_LOADMAP)
	{
		m_bImgValid = false;
		m_miniMap.Destroy();
		if (pDoc->m_strMiniMapURL != L"")
		{
			HRESULT hr = m_miniMap.Load(pDoc->m_strMiniMapURL.GetBuffer());
			pDoc->m_strMiniMapURL.ReleaseBuffer();
			if (FAILED(hr))
			{
				AfxMessageBox(L"加载小地图失败！");
			} else {

				m_bImgValid			= true;
				needInvalid			= true;
				m_nMiniMapWidth		= m_miniMap.GetWidth();
				m_nMiniMapHeight	= m_miniMap.GetHeight();
				int mapWidth		= pDoc->GetMapWidth();
				int mapHeight		= pDoc->GetMapHeight();
				ResetMinRatio();
				m_fRatio			= m_fMinRatio + 1;
				m_DrawScrollPos.x	= 0;
				m_DrawScrollPos.y	= 0;
				m_nViewportWidth	= 0;
				m_nViewportHeight	= 0;
				SetRatio(m_fMinRatio);
				if (fabs(static_cast<float>(mapWidth) / mapHeight - static_cast<float>(m_nMiniMapWidth) / m_nMiniMapHeight) > 0.1)
				{
					AfxMessageBox(L"小地图数据可能已过时，请重新生成");
				}

			}
		}

	}

	if (pDoc->m_nUpdateViewSign & UVS_ENV_SOUND_AREA_SHAPE)
	{
		needInvalid = true;
	}

	if (pDoc->m_nUpdateViewSign & UVS_BKG_SOUND_AREA_SHAPE)
	{
		needInvalid = true;
	}

	if (pDoc->m_nUpdateViewSign & UVS_BKV_SOUND_AREA_SHAPE)
	{
		needInvalid = true;
	}

	if (pDoc->m_nUpdateViewSign & UVS_VIEWPORT)
	{
		ResetEngineViewport();
		needInvalid = true;
	}
	if (needInvalid)
	{
		Invalidate(FALSE);
	}
}

void CMiniMapPanel::SetRatio(float ratio)
{
	if (!m_bImgValid)
		return;
	if (fabs(m_fRatio - ratio) < 0.001)
		return;
	m_fRatio = ratio;
	//当前屏幕中心
	m_DrawScrollPos.x += m_nViewportWidth / 2;
	m_DrawScrollPos.y += m_nViewportHeight / 2;
	m_nViewportWidth = static_cast<int>((m_miniMapRect.right - m_miniMapRect.left) / ratio);
	m_nViewportHeight = static_cast<int>((m_miniMapRect.bottom - m_miniMapRect.top) / ratio);
	if (m_nViewportWidth > m_nMiniMapWidth)
	{
		m_nViewportWidth = m_nMiniMapWidth;
	}

	if (m_nViewportHeight > m_nMiniMapHeight)
	{
		m_nViewportHeight = m_nMiniMapHeight;
	}
	//新的m_DrawScrollPos
	m_DrawScrollPos.x -= m_nViewportWidth / 2;
	m_DrawScrollPos.y -= m_nViewportHeight / 2;
	if (m_DrawScrollPos.x < 0)
		m_DrawScrollPos.x = 0;
	if (m_DrawScrollPos.y < 0)
		m_DrawScrollPos.y = 0;
	//m_nAreaMapState |= (AMS_ENV_SIZE_DIRTY | AMS_BKG_SIZE_DIRTY);
}

void CMiniMapPanel::FixViewport(CPoint &lt, const CPoint &v)
{
	lt -= v;
	if (m_nViewportWidth < m_nMiniMapWidth)
	{
		if (lt.x + m_nViewportWidth > m_nMiniMapWidth)
		{
			lt.x = m_nMiniMapWidth - m_nViewportWidth;
		}
		if (lt.x < 0)
			lt.x = 0;
	} else {
		lt.x = 0;
	}

	if (m_nViewportHeight < m_nMiniMapHeight)
	{
		if (lt.y + m_nViewportHeight > m_nMiniMapHeight)
		{
			lt.y = m_nMiniMapHeight - m_nViewportHeight;
		}
		if (lt.y < 0)
			lt.y = 0;
	} else {
		lt.y = 0;
	}

}

void CMiniMapPanel::DrawEnvArea(CDC *pDC, CPoint lt)
{
	CSoundEditorDoc *pDoc = m_pView->GetDocument();
	int mapWidth = pDoc->GetMapWidth();
	float ratio = m_nMiniMapWidth * m_fRatio / mapWidth;
	const EnvAreaCont& areas = pDoc->GetEnvArea();
	EnvAreaCont::const_iterator it = areas.begin(), ie = areas.end();

	CPen pen(PS_SOLID, 1, RGB(255, 255, 255));
	CGdiObject* pOldObj = pDC->SelectStockObject(NULL_BRUSH);
	CPen* pOldPen = pDC->SelectObject(&pen);

	lt.x = static_cast<int>(lt.x * m_fRatio);
	lt.y = static_cast<int>(lt.y * m_fRatio);

	for (;it!=ie;++it)
	{
		(*it)->DrawArea(pDC, lt, ratio);
	}

	pDC->SelectObject(pOldPen);
	pDC->SelectObject(pOldObj);
}

void CMiniMapPanel::DrawBkgArea(CDC *pDC, CPoint lt)
{
	CSoundEditorDoc *pDoc = m_pView->GetDocument();
	int mapWidth = pDoc->GetMapWidth();
	float ratio = m_nMiniMapWidth * m_fRatio / mapWidth;
	const BkgAreaCont& areas = pDoc->GetBkgArea();
	BkgAreaCont::const_iterator it = areas.begin(), ie = areas.end();

	CPen pen(PS_SOLID, 1, RGB(255, 255, 255));
	CGdiObject* pOldObj = pDC->SelectStockObject(NULL_BRUSH);
	CPen* pOldPen = pDC->SelectObject(&pen);

	lt.x = static_cast<int>(lt.x * m_fRatio);
	lt.y = static_cast<int>(lt.y * m_fRatio);

	for (;it!=ie;++it)
	{
		(*it)->DrawArea(pDC, lt, ratio);
	}

	pDC->SelectObject(pOldPen);
	pDC->SelectObject(pOldObj);
}

void CMiniMapPanel::DrawBkgVolArea(CDC *pDC, CPoint lt)
{
	CSoundEditorDoc *pDoc = m_pView->GetDocument();
	int mapWidth = pDoc->GetMapWidth();
	float ratio = m_nMiniMapWidth * m_fRatio / mapWidth;
	const BkgVolAreaCont& areas = pDoc->GetBkgVolArea();
	BkgVolAreaCont::const_iterator it = areas.begin(), ie = areas.end();

	CPen pen(PS_SOLID, 1, RGB(255, 255, 255));
	CGdiObject* pOldObj = pDC->SelectStockObject(NULL_BRUSH);
	CPen* pOldPen = pDC->SelectObject(&pen);

	lt.x = static_cast<int>(lt.x * m_fRatio);
	lt.y = static_cast<int>(lt.y * m_fRatio);

	for (;it!=ie;++it)
	{
		(*it)->DrawArea(pDC, lt, ratio);
	}

	pDC->SelectObject(pOldPen);
	pDC->SelectObject(pOldObj);
}

void CMiniMapPanel::DrawCtrl(CDC *pDC)
{
	SetStretchBltMode(pDC->GetSafeHdc(), HALFTONE);
	CPoint lt = m_DrawScrollPos;
	if (m_mouseState & RIGHT_BUTTON_DOWN)
	{
		CPoint v = m_MousePos - m_RMouseDownPos;
		v.x = static_cast<int>(v.x / m_fRatio);
		v.y = static_cast<int>(v.y / m_fRatio);
		FixViewport(lt, v);
		
	}
	//小地图
	m_miniMap.Draw(pDC->GetSafeHdc(), 0, 0, static_cast<int>(m_nViewportWidth * m_fRatio), 
		static_cast<int>(m_nViewportHeight * m_fRatio), lt.x, lt.y, m_nViewportWidth, m_nViewportHeight);
	//区域
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	bool drawEnv = pDoc->GetEditState() == EDIT_STATE_ENV_SOUND;
	if (pDoc->GetEditState() == EDIT_STATE_TESTING)
		drawEnv = (pDoc->m_bTestingDrawEnvArea == TRUE);
	if (drawEnv)
	{
		DrawEnvArea(pDC, lt);
	}

	bool drawBkg = pDoc->GetEditState() == EDIT_STATE_BKG_MUSIC;
	if (pDoc->GetEditState() == EDIT_STATE_TESTING)
		drawBkg = (pDoc->m_bTestingDrawBkgArea == TRUE);
	if (drawBkg)
	{
		DrawBkgArea(pDC, lt);
	}

	bool drawBkgVol = pDoc->GetEditState() == EDIT_STATE_BKG_VOL;
	if (pDoc->GetEditState() == EDIT_STATE_TESTING)
		drawBkgVol = (pDoc->m_bTestingDrawBkgVolArea == TRUE);
	if (drawBkgVol)
	{
		DrawBkgVolArea(pDC, lt);
	}

	//Viewport
	CPen pen(PS_SOLID, 2, RGB(255, 0, 0));
	CGdiObject* pOldObj = pDC->SelectStockObject(NULL_BRUSH);
	CPen* pOldPen = pDC->SelectObject(&pen);
	pDC->Rectangle(static_cast<int>((m_engineViewport.left - lt.x) * m_fRatio), 
		static_cast<int>((m_engineViewport.top - lt.y) * m_fRatio),
		static_cast<int>((m_engineViewport.right - lt.x) * m_fRatio), 
		static_cast<int>((m_engineViewport.bottom - lt.y) * m_fRatio));
	pDC->SelectObject(pOldPen);
	pDC->SelectObject(pOldObj);
	
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
	SetTimer(1050, 50, NULL);
	return TRUE;
}

void CMiniMapPanel::OnPaint()
{
	CPaintDC dc(this); // device context for painting
	CBitmap	ShowBitmap;
	ShowBitmap.CreateCompatibleBitmap(&dc, m_miniMapRect.Width(), m_miniMapRect.Height());
	if(!ShowBitmap.m_hObject)
		return;
	CDC memDC;
	memDC.CreateCompatibleDC(&dc);
	if(!memDC.m_hDC)
		return;
	memDC.SelectObject(&ShowBitmap);
	memDC.SetBkMode(TRANSPARENT);
	if (m_bImgValid)
	{
		DrawCtrl(&memDC);
	}
	dc.BitBlt(m_miniMapRect.left, m_miniMapRect.top, m_miniMapRect.Width(), m_miniMapRect.Height(), &memDC, 0, 0, SRCCOPY);
	//dc.StretchBlt()
	
	memDC.DeleteDC( );
	ShowBitmap.DeleteObject();
}

void CMiniMapPanel::OnHScroll(UINT nSBCode, UINT nPos, CScrollBar* pScrollBar)
{
	if (pScrollBar->GetDlgCtrlID() == IDC_SLIDER_SCALE)
	{
		if (m_bImgValid)
		{
			SetRatio((m_sliderScale.GetPos() / 100.0f) * (1.0f - m_fMinRatio) + m_fMinRatio);
			Invalidate(FALSE);
		}
	}
	CDialog::OnHScroll(nSBCode, nPos, pScrollBar);
}

void CMiniMapPanel::OnLButtonDown(UINT nFlags, CPoint point)
{
	SetCapture();
	m_LMouseDownPos = point;
	m_mouseState |= LEFT_BUTTON_DOWN;

	CPoint realScal = point;
	realScal.x = static_cast<int>(static_cast<float>(realScal.x) / m_fRatio) + m_DrawScrollPos.x;
	realScal.y = static_cast<int>(static_cast<float>(realScal.y) / m_fRatio) + m_DrawScrollPos.y;
	CSoundEditorDoc* pDoc = m_pView->GetDocument();
	realScal.x = realScal.x * pDoc->GetMapWidth() / m_nMiniMapWidth - 512;//1024/2
	realScal.y = realScal.y * pDoc->GetMapHeight() / m_nMiniMapHeight - 384;//768/2
	pDoc->UpdateViewport(realScal, US_PANEAL);
	//ResetEngineViewport();
	CDialog::OnLButtonDown(nFlags, point);
}

void CMiniMapPanel::OnLButtonUp(UINT nFlags, CPoint point)
{
	m_mouseState &= ~LEFT_BUTTON_DOWN;
	if (m_mouseState == MOUSE_STATE_NORMAL)
	{
		ReleaseCapture();
	}
	

	CDialog::OnLButtonUp(nFlags, point);
}

void CMiniMapPanel::OnRButtonDown(UINT nFlags, CPoint point)
{
	SetCapture();
	m_RMouseDownPos = point;
	m_mouseState |= RIGHT_BUTTON_DOWN;
	CDialog::OnRButtonDown(nFlags, point);
}

void CMiniMapPanel::OnRButtonUp(UINT nFlags, CPoint point)
{
	m_mouseState &= ~RIGHT_BUTTON_DOWN;
	if (m_mouseState == MOUSE_STATE_NORMAL)
	{
		ReleaseCapture();
	}

	CPoint v = point - m_RMouseDownPos;
	v.x = static_cast<int>(v.x / m_fRatio);
	v.y = static_cast<int>(v.y / m_fRatio);
	FixViewport(m_DrawScrollPos, v);

	CDialog::OnRButtonUp(nFlags, point);
}

void CMiniMapPanel::OnMouseMove(UINT nFlags, CPoint point)
{
	m_MousePos = point;
	CDialog::OnMouseMove(nFlags, point);
}

void CMiniMapPanel::OnTimer(UINT_PTR nIDEvent)
{
	if (nIDEvent == 1050)
	{
		Invalidate(FALSE);
	}
	CDialog::OnTimer(nIDEvent);
}
