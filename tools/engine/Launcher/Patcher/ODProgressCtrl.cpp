// ODProgressCtrl.cpp : implementation file
//

#include "stdafx.h"
#include "Launcher.h"
#include "ODProgressCtrl.h"
#include "BitmapUtils.h"

COLORREF TEXT_COLOR = RGB(255, 86, 13);

// CODProgressCtrl

IMPLEMENT_DYNAMIC(CODProgressCtrl, CProgressCtrl)

CODProgressCtrl::CODProgressCtrl()
{
	m_LastPixPosX = -1;
	m_dwAppearanceFlag = AF_DEFAULT;
	m_rcLastClient.SetRect(0,0,0,0);
	m_clrTextColor = TEXT_COLOR; // default color
}

CODProgressCtrl::~CODProgressCtrl()
{
}

BEGIN_MESSAGE_MAP(CODProgressCtrl, CProgressCtrl)
	ON_WM_PAINT()
	ON_WM_ERASEBKGND()
END_MESSAGE_MAP()

void CODProgressCtrl::PreSubclassWindow()
{
	CProgressCtrl::PreSubclassWindow();
	// ��Windows��ʾЧ������Ϊ"�������"������,
	// ���ǻῴ��WS_EX_STATICEDGE����µ�һ�����صķ�CLIENT���ױ�,�����Ƴ�֮.
	ModifyStyleEx(WS_EX_STATICEDGE, 0, SWP_FRAMECHANGED);
}

// CODProgressCtrl message handlers

float CODProgressCtrl::GetPercent()
{
	int nLower = 0;
	int nUpper = 0;
	GetRange(nLower, nUpper);

	int nRange = nUpper - nLower;
	if ( nRange == 0 )
	{
		nRange = 1;
	}
	
	int nPos = GetPos();

	float fPercent = (nPos - nLower) * 1.0f / nRange;

	return fPercent;
}

BOOL CODProgressCtrl::SetAppearance(const CBitmap& rbmFR, const CRect& rBoxFR, 
									const CBitmap& rbmBK, const CRect& rBoxBK)
{
	PaintDIB(m_dcMemFR, rbmFR, rBoxFR);
	PaintDIB(m_dcMemBK, rbmBK, rBoxBK);

	return TRUE;
}

BOOL CODProgressCtrl::SetAppearance(const CBitmap& rbmAll, 
									const CRect& rBoxFR, 
									const CRect& rBoxBK)
{
	PaintDIB(m_dcMemFR, rbmAll, rBoxFR);
	PaintDIB(m_dcMemBK, rbmAll, rBoxBK);

	return TRUE;
}

static struct CBLENDFUNCTION : public BLENDFUNCTION
{
	//
	// ע�⣬��ҪΪ�˽ṹ���κ����⺯����������������
	// ����ᵼ�¸ýṹ���ڴ沼����BLENDFUNCTION��һ��
	//
	CBLENDFUNCTION()
	{
		BlendOp = AC_SRC_OVER;
		BlendFlags = 0;
		SourceConstantAlpha = 255;
		AlphaFormat = AC_SRC_ALPHA;
	}
} blend;

BOOL CODProgressCtrl::_CheckUpdate(CDC* pDC)
{
	if ( m_dcMemFR.GetSafeHdc() == NULL )
	{
		return FALSE;
	}

	CRect rcClient;
	GetClientRect(&rcClient);
	float fPercent = GetPercent();
	int nPosPx = (int)(fPercent * (rcClient.Width()) );
	int nPos = GetPos();
	if( !(nPos == 0 && m_LastPixPosX == 0) && m_LastPixPosX == nPosPx && m_rcLastClient == rcClient )
	{
		// No need redraw
		return FALSE;
	}

	if (m_dcMemWnd.GetSafeHdc() == NULL )
	{
		// When we create DIB memory DC, we should pass in the NULL DC.
		m_dcMemWnd.CreateCompatibleDC( NULL );
	}

	if ( m_rcLastClient != rcClient ) 
	{
		m_bmWnd.DeleteObject();
		// ����Ҫ��m_dcMemBK/m_dcMemFR��Ϊ����DC
		m_bmWnd.CreateCompatibleBitmap( &m_dcMemBK, rcClient.Width(), rcClient.Height() );
		m_dcMemWnd.SelectObject(&m_bmWnd);
	}

	//====================================================================================
	// ���Ʊ���
	CBitmap* pBmp = m_dcMemBK.GetCurrentBitmap();
	CSize szBMP = pBmp->GetBitmapDimension();
	if( szBMP.cx >= rcClient.Width() )
	{
		m_dcMemWnd.AlphaBlend(0, 0, rcClient.Width(), rcClient.Height(), 
			&m_dcMemBK, 0, 0, rcClient.Width(), szBMP.cy, blend );
	}
	else
	{
		m_dcMemWnd.AlphaBlend(0, 0, rcClient.Width(), rcClient.Height(), 
			&m_dcMemBK, 0, 0, szBMP.cx, szBMP.cy, blend );
	}

	//====================================================================================
	// ����ǰ��
	if ( nPosPx > 0 ) // ������Ϣ�Ƿ�Ϊ0
	{
		pBmp = m_dcMemFR.GetCurrentBitmap();
		CSize szBMP = pBmp->GetBitmapDimension();

		if( szBMP.cx >= nPosPx )
		{
			m_dcMemWnd.AlphaBlend( 0, 0, nPosPx, rcClient.Height(), 
				&m_dcMemFR, 0, 0, nPosPx, szBMP.cy, blend );
		}
		else
		{
			m_dcMemWnd.AlphaBlend( 0, 0, nPosPx, rcClient.Height(),
				&m_dcMemFR, 0, 0, szBMP.cx, szBMP.cy, blend );
		}

		// ���Ȱٷֱ�������ʾ
		if ( GetShowPercent() )
		{
			int oldBkMode = m_dcMemWnd.SetBkMode( TRANSPARENT );
			m_dcMemWnd.SelectStockObject( ANSI_VAR_FONT );
			m_dcMemWnd.SetTextColor(m_clrTextColor);

			LOGFONT logFont = {0};
			CFont* font = m_dcMemWnd.GetCurrentFont();
			if (font != NULL) font->GetLogFont( &logFont );

			CString strPercent; 
			strPercent.Format(_T("%.2f%%"), fPercent*100.0f);

			CSize szExtent = m_dcMemWnd.GetTextExtent(strPercent);
			m_dcMemWnd.TextOut( (rcClient.Width()-szExtent.cx)/2, (rcClient.Height()-szExtent.cy)/2, strPercent );
			m_dcMemWnd.SetBkMode( oldBkMode );
		}
	}
	else if( nPos == 0 && m_LastPixPosX == -1 )
	{
		// ��ǰ��100%��Ч����ʾ����Ϊ0��״̬
		pBmp = m_dcMemFR.GetCurrentBitmap();
		CSize szBMP = pBmp->GetBitmapDimension();
		m_dcMemWnd.AlphaBlend( 0, 0, rcClient.Width(), rcClient.Height(),
			&m_dcMemFR, 0, 0, szBMP.cx, szBMP.cy, blend );
	}

	m_LastPixPosX = nPosPx;
	m_rcLastClient = rcClient;

	return TRUE;
}

void CODProgressCtrl::OnPaint()
{
	if ( GetWindowLong( GetSafeHwnd(), GWL_STYLE ) & PBS_VERTICAL )
	{
		Default();
		return;
	}

	BOOL bRedraw = TRUE;
	BOOL bDefault = TRUE;

	do
	{
		CPaintDC dc(this); 
		bRedraw = _CheckUpdate( &dc );
		if( m_dcMemWnd.GetSafeHdc() != NULL )
		{
			CRect rcClient;
			GetClientRect(&rcClient);
			dc.BitBlt( 0,0, rcClient.Width(), rcClient.Height(), &m_dcMemWnd, 0, 0, SRCCOPY );
			//dc.AlphaBlend(0,0, rcClient.Width(), rcClient.Height(), &m_dcMemWnd, 0, 0, rcClient.Width(), rcClient.Height(), blend );

			bDefault = FALSE;
		}
	}
	while(0);

	if (bDefault)
	{
		Default();
	}
}

BOOL CODProgressCtrl::OnEraseBkgnd(CDC* pDC)
{
	return TRUE;
}
