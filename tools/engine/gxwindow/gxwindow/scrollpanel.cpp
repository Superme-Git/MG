#include "gxwindowdef.h"

namespace GXWINDOW {

namespace INLINECLASS { 

class CScrollPanelDispatchMessage
{
public:
	template<typename F>
		static bool DispatchMouseMessage( CScrollPanel& sp, F f, int x, int y, int key)
	{
		int nx = x - sp.GetAbsPosX();
		int ny = y - sp.GetAbsPosY();
		bool bResult = true;
		if( !sp.m_bForceHide && sp.m_bvsb && sp.m_vsb->IsPointIn( nx, ny) )
			bResult = (sp.m_vsb->*f)( x, y, key);
		else if( !sp.m_bForceHide && sp.m_bhsb && sp.m_hsb->IsPointIn( nx, ny) )
			bResult = (sp.m_hsb->*f)( x, y, key);
		else if( sp.m_client && sp.m_client->IsPointIn( nx, ny))
			bResult = (sp.m_client->*f)( x, y, key);
		else
			return false;
		return bResult;
	}
};

static CScrollPanelComponentAllocator		g_DefaultScrollPanelAllocator;

} //namespace INLINECLASS { 

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CScrollPanelComponentAllocator::CScrollPanelComponentAllocator()
{
}

CScrollPanelComponentAllocator::~CScrollPanelComponentAllocator()
{
}

CScrollBar* CScrollPanelComponentAllocator::NewScrollBar()
{
	return new CScrollBar();
}

void CScrollPanelComponentAllocator::DeleteScrollBar( CScrollBar* sb)
{
	delete sb;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CScrollPanel::CScrollPanel()
{
	Initialize( INLINECLASS::g_DefaultScrollPanelAllocator);
}

CScrollPanel::CScrollPanel( CScrollPanelComponentAllocator& alloc)
{
	Initialize( alloc);
}

CScrollPanel::~CScrollPanel()
{
	m_Alloc->DeleteScrollBar( m_vsb);
	m_Alloc->DeleteScrollBar( m_hsb);
}

void CScrollPanel::Initialize( CScrollPanelComponentAllocator& alloc)
{
	m_Alloc = &alloc;
	
	m_vsb = m_Alloc->NewScrollBar();
	m_hsb = m_Alloc->NewScrollBar();

	m_client = NULL;
	m_bvsb = false;
	m_bhsb = false;
	m_nOffsetX = 0;
	m_nOffsetY = 0;
	//m_nBorderWidth = 0;
	m_pLastMouseIn =  NULL;

	m_vsb->SetParent( this);
	m_vsb->BarComponentPositionNotifyAdd( *this);
	m_vsb->SetVertical( true);
	m_vsb->SetSize( 20, 20);
	m_vsb->SetRubSize( 20);
	//m_vsb->SetSliderScaleStyle( SSS_TYPAL);
	m_vsb->SetSliderRubStyle( SRS_TYPAL);

	m_hsb->SetParent( this);
	m_hsb->BarComponentPositionNotifyAdd( *this);
	m_hsb->SetVertical( false);
	m_hsb->SetSize( 20, 20);
	m_hsb->SetRubSize( 20);
	//m_hsb->SetSliderScaleStyle( SSS_TYPAL);
	m_hsb->SetSliderRubStyle( SRS_TYPAL);

	m_bhsbforceshow = false;
	m_bvsbforceshow = false;
	m_bAutoHide = false;
	m_bForceHide = false;
	m_bMouseIn = false;
	m_bAutoClientWidth = false;

	m_bindingvsb = NULL;
	m_bindinghsb = NULL;

	SetSize( 0, 0);

	m_clrBorder  = 0;
}

CScrollBar* CScrollPanel::GetVerticalScrollBar()
{
	return m_vsb;
}

CScrollBar* CScrollPanel::GetHorizontalScrollBar()
{
	return m_hsb;
}

void CScrollPanel::SetComponent( CComponent& client)
{
	if( m_client)
	{
		m_client->SetParent( NULL);
		m_client->ComponentSizeChangedNotifyRemove( *this);
		m_client->SetBorderColor( 0);
	}
	m_client = &client;
	if( m_client)
	{
		m_client->SetParent( this);
		m_client->ComponentSizeChangedNotifyAdd( *this);
		AdjustScrollInfo();
		m_client->SetBorderColor( m_clrBorder);
	}
}

CComponent& CScrollPanel::GetClient()
{
	return *m_client;
}

void CScrollPanel::RemoveComponent()
{
	if( m_client)
	{
		m_client->ComponentSizeChangedNotifyRemove( *this);
		m_client->SetParent( NULL);
		m_client->SetBorderColor( 0);
	}
	m_client = NULL;
}

void CScrollPanel::SetSize( int w, int h)
{
	CComponent::SetSize( w, h);
	AdjustScrollInfo();
}

void CScrollPanel::AdjustScrollInfo()
{
	if( m_client)
		AdjustScrollInfo( m_client->GetWidth(), m_client->GetHeight());
}

void CScrollPanel::AdjustScrollInfo( int cw, int ch)
{
	int vbsize = m_vsb->GetWidth();
	int hbsize = m_hsb->GetHeight();
	int spw = GetWidth();
	int sph = GetHeight();
	
	if ( spw == 0 || sph == 0)
		return;

	int dw = cw - spw;
	int dh = ch - sph; 

	m_bhsb = (dw > 0);
	m_bvsb = (dh > 0);
	if( m_bhsb && !m_bAutoHide)
		dh += hbsize;
	if( m_bvsb && !m_bAutoHide && !m_bAutoClientWidth)
		dw += vbsize;

	while( true) 
	{
		if( !m_bhsb && (dw > 0) )
		{
			m_bhsb = true;
			dh += hbsize;
		}
		else if( !m_bvsb && (dh > 0) )
		{
			m_bvsb = true;
			dw += vbsize;
		}
		else
		{
			break;
		}
	}

	m_bhsb = m_bhsb && !( m_bAutoHide && !m_bMouseIn);
	m_bvsb = m_bvsb && !( m_bAutoHide && !m_bMouseIn);

	if( m_bhsb)
	{
		m_hsb->SetValueMax( dw);
		m_hsb->SetSize( m_bvsb || m_bvsbforceshow ? ( spw - vbsize) : spw, m_hsb->GetAbsHeight());
		m_hsb->SetPos( 0, sph - hbsize);
	}
	else
	{
		if ( !m_bAutoHide)
		{
			m_hsb->SetValueMax( 0);
			m_nOffsetX = 0;
		}

		if ( m_bhsbforceshow)
		{
			m_hsb->SetSize( m_bvsb || m_bvsbforceshow ? ( spw - vbsize) : spw, m_hsb->GetAbsHeight());
			m_hsb->SetPos( 0, sph - hbsize);
		}
	}
	if( m_bvsb)
	{
		m_vsb->SetValueMax( dh);
		m_vsb->SetSize( m_vsb->GetAbsWidth(), m_bhsb || m_bhsbforceshow ? (sph - hbsize) : sph);
		m_vsb->SetPos( spw - vbsize, 0);
	}
	else
	{
		if ( !m_bAutoHide)
		{
			m_vsb->SetValueMax( 0);
			m_nOffsetY = 0;
		}

		if ( m_bvsbforceshow)
		{
			m_vsb->SetSize( m_vsb->GetAbsWidth(), m_bhsb || m_bhsbforceshow ? (sph - hbsize) : sph);
			m_vsb->SetPos( spw - vbsize, 0);
		}
	}

	if ( m_client && m_bAutoClientWidth && !m_bForceHide)
	{
		CComponent* pc = m_client;
		m_client = NULL;
		int oldw = pc->GetWidth();
		int oldh = pc->GetHeight();
		pc->SetSize( m_hsb->GetValueMax() > 0 ? pc->GetWidth() : GetWidth() - ( !m_bindingvsb && ( m_bvsb || m_bvsbforceshow) ? m_vsb->GetWidth() : 0), 
			m_vsb->GetValueMax() > 0 ? pc->GetHeight() : GetHeight() - ( m_bhsb || m_bhsbforceshow ? m_hsb->GetHeight() : 0));
		if ( m_bhsb)
			m_hsb->SetValueMax( m_hsb->GetValueMax() + pc->GetWidth() - oldw);
		if ( m_vsb)
			m_vsb->SetValueMax( m_vsb->GetValueMax() + pc->GetHeight() - oldh);
		m_client = pc;
	}

	AdjustBindingScrollInfo();

	INVALIDATE();
}

void CScrollPanel::OnComponentSizeChanged( CComponent& c, int nw, int nh, int ow, int oh)
{
	if( c.IsSameComponent( m_client))
		AdjustScrollInfo( nw, nh);
}

void CScrollPanel::OnBarComponentPosition( CBarComponent& c, int pos, int max)
{
	if( c.IsSameComponent( m_hsb))
	{
		m_nOffsetX = pos;
		AdjustBindingScrollInfo();
	}
	else if( c.IsSameComponent( m_vsb))
	{
		m_nOffsetY = pos;
		AdjustBindingScrollInfo();
	}
	else if ( c.IsSameComponent( m_bindingvsb))
	{
		m_vsb->SetValuePos( m_bindingvsb->GetValuePos());
	}
	else if ( c.IsSameComponent( m_bindinghsb))
	{
		m_hsb->SetValuePos( m_bindinghsb->GetValuePos());
	}
}

void CScrollPanel::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CComponent::Draw( draw, l, t, cx, cy, cw, ch);
	if ( GetColorAlpha( m_clrBorder))
		draw.Rect( l, t, l + cw, t + ch, m_clrBorder);

	int	nr = cx + cw;
	int nb = cy + ch;
	int nw = cw;
	int nh = ch;

	int nVerBarSize = m_vsb->GetWidth();
	int nHorBarSize = m_hsb->GetHeight();
	int nClientWidth = m_bvsb || m_bvsbforceshow ? ( GetWidth() - nVerBarSize) : GetWidth();
	int nClientHeight = m_bhsb || m_bhsbforceshow ? ( GetHeight() - nHorBarSize) : GetHeight();

	bool bDrawScb = m_bForceHide ? false : ( m_bAutoHide ? m_bMouseIn : true);
	if( !m_bindingvsb && ( m_bvsb || m_bvsbforceshow) && bDrawScb)
	{
		if( cx > nClientWidth)
		{
			int templ = l + cw - nVerBarSize;
			int tempcx = cx - nClientWidth;
			int tempnh = min( nClientHeight, nh);
			m_vsb->Draw( draw, templ, t, tempcx, cy, nw, tempnh);
			m_vsb->DoComponentAfterDrawNofity( draw, templ, t, tempcx, cy, nw, tempnh);
			return;
		}
		else if( nr > nClientWidth)
		{
			int templ = l + nClientWidth;
			int tempnw = nr - nClientWidth;
			int tempnh = min( nClientHeight, nh);
			m_vsb->Draw( draw, templ, t, 0, cy, tempnw,  tempnh);
			m_vsb->DoComponentAfterDrawNofity( draw, templ, t, 0, cy, tempnw, tempnh);
			cw -= (nr - nClientWidth);
		}
	}
	else if( m_client)
	{
		cw = min( cw, m_client->GetWidth());
	}
	if( ( m_bhsb || m_bhsbforceshow) && bDrawScb)
	{
		if( cy > nClientHeight)
		{
			int tempt = t + ch - nHorBarSize;
			int tempcy = cy - nClientHeight;
			int tempnw = min( nClientWidth, nw);
			m_hsb->Draw( draw, l, tempt, cx, tempcy, tempnw, nh);
			m_hsb->DoComponentAfterDrawNofity( draw, l, tempt, cx, tempcy, tempnw, nh);
			return;
		}
		else if( nb > nClientHeight)
		{
			int tempt = t + nClientHeight;
			int tempnw = min( nClientWidth, nw);
			int tempnh = nb - nClientHeight;
			m_hsb->Draw( draw, l, tempt, cx, 0, tempnw, tempnh);
			m_hsb->DoComponentAfterDrawNofity( draw, l, tempt, cx, 0, tempnw, tempnh);
			ch -= (nb - nClientHeight);
		}
	}
	else if( m_client)
	{
		ch = min( ch, m_client->GetHeight());
	}

	if( m_client)		
	{
		m_client->SetPos( -m_nOffsetX, -m_nOffsetY);
		cw = min( cw, m_client->GetWidth());
		ch = min( ch, m_client->GetHeight());
		int templ = l - m_nOffsetX;
		int tempt = t- m_nOffsetY;
		int tempcx =  cx + m_nOffsetX;
		int tempcy = cy + m_nOffsetY;
		m_client->Draw( draw, templ, tempt, tempcx, tempcy, cw, ch);
		m_client->DoComponentAfterDrawNofity( draw, templ, tempt, tempcx, tempcy, cw, ch);

		if ( GetColorAlpha( m_clrBorder))
			draw.Rect( l + cx, t + cy, l + cx + cw, t + cy + ch, m_clrBorder);
	}
}

bool CScrollPanel::OnLeftButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	bool b = INLINECLASS::CScrollPanelDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnLeftButtonDown, x, y, key);
	CComponent::OnLeftButtonDown( x, y, key);
	return b;
}

bool CScrollPanel::OnLeftButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	bool b = INLINECLASS::CScrollPanelDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnLeftButtonUp, x, y, key);
	CComponent::OnLeftButtonUp( x, y, key);
	return b;
}

//bool CScrollPanel::OnLeftButtonDoubleClick( int x, int y, int key)
//{
//	if ( IsViewOnly())
//		return false;
//
//	INLINECLASS::CScrollPanelDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnLeftButtonDoubleClick, x, y, key);
//	return CComponent::OnLeftButtonDoubleClick( x, y, key);
//}

bool CScrollPanel::OnRightButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	bool b = INLINECLASS::CScrollPanelDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnRightButtonDown, x, y, key);
	CComponent::OnRightButtonDown( x, y, key);
	return b;
}

bool CScrollPanel::OnRightButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	bool b = INLINECLASS::CScrollPanelDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnRightButtonUp, x, y, key);
	CComponent::OnRightButtonUp( x, y, key);
	return b;
}

//bool CScrollPanel::OnRightButtonDoubleClick( int x, int y, int key)
//{
//	if ( IsViewOnly())
//		return false;
//
//	INLINECLASS::CScrollPanelDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnRightButtonDoubleClick, x, y, key);
//	return CComponent::OnRightButtonDoubleClick( x, y, key);
//}

bool CScrollPanel::OnMouseMove( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	int nx = x - GetAbsPosX();
	int ny = y - GetAbsPosY();

	if( ( m_bvsb || m_bvsbforceshow) && m_vsb->IsPointIn( nx, ny) )
	{
		CheckMouseEnterLeave( *m_vsb);
		m_vsb->OnMouseMove( x, y, key);
	}
	else if( ( m_bhsb || m_bhsbforceshow) && m_hsb->IsPointIn( nx, ny) )
	{
		CheckMouseEnterLeave( *m_hsb);
		m_hsb->OnMouseMove( x, y, key);
	}
	else if( m_client && m_client->IsPointIn( nx, ny))
	{
		CheckMouseEnterLeave( *m_client);
		m_client->OnMouseMove( x, y, key);
	}
	else
	{
		if( m_pLastMouseIn)
		{
			m_pLastMouseIn->OnMouseLeave();
			m_pLastMouseIn = NULL;
		}
	}

	return CComponent::OnMouseMove( x, y, key);
}

void CScrollPanel::CheckMouseEnterLeave( CComponent& com)
{
	if( com.IsSameComponent( m_pLastMouseIn))
		return;
	if( m_pLastMouseIn)
		m_pLastMouseIn->OnMouseLeave();
	m_pLastMouseIn = &com;
	m_pLastMouseIn->OnMouseEnter();
}

void CScrollPanel::OnMouseEnter()
{
	if ( IsViewOnly())
		return;

	m_bMouseIn = true;
	if ( m_bAutoHide)
		AdjustScrollInfo();
	CComponent::OnMouseEnter();
}

void CScrollPanel::OnMouseLeave()
{
	if ( IsViewOnly())
		return;

	if( m_pLastMouseIn)
	{
		m_pLastMouseIn->OnMouseLeave();
		m_pLastMouseIn = NULL;
	}
	m_bMouseIn = false;
	if ( m_bAutoHide)
		AdjustScrollInfo();
	CComponent::OnMouseLeave();
}

void CScrollPanel::OnComponentShow()
{
	m_vsb->OnComponentShow();
	m_hsb->OnComponentShow();
	if ( m_client)
		m_client->OnComponentShow();
	CComponent::OnComponentShow();
}

void CScrollPanel::OnComponentHide()
{
	m_vsb->OnComponentHide();
	m_hsb->OnComponentHide();
	if ( m_client)
		m_client->OnComponentHide();
	CComponent::OnComponentHide();
}

void CScrollPanel::SetBorderColor( COLOR clr)
{
	m_clrBorder = clr;
	m_vsb->SetBorderColor( clr);
	m_hsb->SetBorderColor( clr);
	if ( m_client)
		m_client->SetBorderColor( clr);
}

bool CScrollPanel::OnMouseWheel( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	enum
	{
		SB_WHEEL_DELTA = -120,
	};
	
	int nRotate = ( key >> 16 ) / SB_WHEEL_DELTA;
	if( m_bvsb)
		m_vsb->SetValuePos( m_vsb->GetValuePos() + nRotate * m_vsb->GetStepSize());
	else if( m_bhsb)
		m_hsb->SetValuePos( m_hsb->GetValuePos() + nRotate * m_hsb->GetStepSize());
	INLINECLASS::CScrollPanelDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnMouseWheel, x, y, key);

	return CComponent::OnMouseWheel( x, y, key);
}

void CScrollPanel::SetVerticalBarForceShow( bool bShow)
{
	m_bvsbforceshow = bShow;
	AdjustScrollInfo();
}

void CScrollPanel::SetHorizontalBarForceShow( bool bShow)
{
	 m_bhsbforceshow = bShow;
}

bool CScrollPanel::IsVerticalBarForceShow() const
{
	return m_bvsbforceshow;
}

bool CScrollPanel::IsHorizontalBarForceShow() const
{
	return m_bhsbforceshow;
}

void CScrollPanel::SetAutoHideScrollBar( bool bShow)
{
	m_bAutoHide = bShow;
}

bool CScrollPanel::IsAutoHideScroBar() const
{
	return m_bAutoHide;
}

void CScrollPanel::SetForceHideScrollBar(bool bHide /* = true */)
{
	m_bForceHide = bHide;
}

bool CScrollPanel::IsForceHideScrollBar() const
{
	return m_bForceHide;
}

void CScrollPanel::SetAutoClientWidth( bool bAuto)
{
	m_bAutoClientWidth = bAuto;
}

bool CScrollPanel::IsAutoClientWidth() const
{
	return m_bAutoClientWidth;
}

void CScrollPanel::BindingVerticalBar( CScrollBar* vsb)
{
	if ( vsb)
	{
		assert( vsb->GetVertical());
		vsb->BarComponentPositionNotifyAdd( *this);
	}
	else if ( m_bindingvsb)
		m_bindingvsb->BarComponentPositionNotifyRemove( *this);

	m_bindingvsb = vsb;

	AdjustBindingScrollInfo();
}

void CScrollPanel::BindingHorizontalBar( CScrollBar* hsb)
{
	if ( hsb)
	{
		hsb->BarComponentPositionNotifyAdd( *this);
		assert( !hsb->GetVertical());
	}
	else if ( m_bindinghsb)
		m_bindinghsb->BarComponentPositionNotifyRemove( *this);

	m_bindinghsb = hsb;

	AdjustBindingScrollInfo();
}

void CScrollPanel::AdjustBindingScrollInfo()
{
	if ( m_bindingvsb)
	{
		m_bindingvsb->SetValueMax( m_vsb->GetValueMax());
		m_bindingvsb->SetStepSize( m_vsb->GetStepSize());
		m_bindingvsb->SetValuePos( m_vsb->GetValuePos());
	}

	if ( m_bindinghsb)
	{
		m_bindinghsb->SetValueMax( m_hsb->GetValueMax());
		m_bindinghsb->SetStepSize( m_hsb->GetStepSize());
		m_bindinghsb->SetValuePos( m_hsb->GetValuePos());
	}
}

bool CScrollPanel::IsPointInMsgTransparent( int x, int y) const
{
	if( ( m_bvsb || m_bvsbforceshow) && m_vsb->IsPointIn( x, y) )
		return false;
	else if( ( m_bhsb || m_bhsbforceshow) && m_hsb->IsPointIn( x, y) )
		return false;
	else if( m_client && m_client->IsPointIn( x, y))
		return m_client->IsPointInMsgTransparent( x, y);
	else
		return CComponent::IsPointInMsgTransparent( x, y);
}

} // namespace GXWINDOW {