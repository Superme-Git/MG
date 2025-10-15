#include "gxwindowdef.h"

namespace GXWINDOW
{

CBarable::CBarable()
	: m_bVertical( false),m_nPos( 0), m_nMax( 0), m_nStepSize( 5)
{
}

CBarable::~CBarable()
{
}

void CBarable::SetValuePos( int n)
{
	m_nPos = n;
}

int CBarable::GetValuePos() const
{
	return m_nPos;
}

void CBarable::SetVertical( bool b)
{
	m_bVertical = b;
}

bool CBarable::GetVertical() const
{
	return m_bVertical;
}

void CBarable::SetValueMax( int n)
{
	m_nMax = n;
	if( m_nPos > m_nMax)
		SetValuePos( m_nMax);
}

int CBarable::GetValueMax() const
{
	return m_nMax;
}

void CBarable::SetStepSize( int n)
{
	m_nStepSize = n;
}

int CBarable::GetStepSize() const
{
	return m_nStepSize;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CBarComponentPositionNotify::CBarComponentPositionNotify()
{
}

CBarComponentPositionNotify::~CBarComponentPositionNotify()
{
}

namespace INLINECLASS {

class CBarComponentPositionNotifySet : public CNotifySet<CBarComponentPositionNotify*>
{
public:
	CBarComponentPositionNotifySet() {}
	virtual ~CBarComponentPositionNotifySet() {}
};

} // namespace INLINECLASS {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CBarComponent::CBarComponent()
: m_nset( new INLINECLASS::CBarComponentPositionNotifySet())
{
}

CBarComponent::~CBarComponent()
{
	delete m_nset;
}

void CBarComponent::BarComponentPositionNotifyAdd( CBarComponentPositionNotify& bcpn)
{
	m_nset->Insert( &bcpn);
}

void CBarComponent::BarComponentPositionNotifyRemove( CBarComponentPositionNotify& bcpn)
{
	m_nset->Remove( &bcpn);
}

void CBarComponent::SetValuePos( int n)
{
	if( n > m_nMax)
		n = m_nMax;
	else if( n < 0)
		n = 0;

	if( n != m_nPos)
	{
		m_nPos = n;
		if( m_nMax)
			m_nset->NotifyAll( *this, &CBarComponentPositionNotify::OnBarComponentPosition, m_nPos, m_nMax);
	}
	INVALIDATE();
}

int CBarComponent::GetBarWidth() const
{
	return GetWidth();
}

int CBarComponent::GetBarHeight() const
{
	return GetHeight();
}

int CBarComponent::GetBarAbsX() const
{
	return CComponent::GetAbsPosX();
}

int CBarComponent::GetBarAbsY() const
{
	return CComponent::GetAbsPosY();
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CSliderRub::CSliderRub()
: m_bRubMoving( false), m_bBarMouseOn( false)
{
}

CSliderRub::~CSliderRub()
{
}

void CSliderRub::SetSliderRubStyle( SliderRubStyle srs)
{
	m_srs = srs;
}

SliderRubStyle CSliderRub::GetSliderRubStyle() const
{
	return m_srs;
}

bool CSliderRub::OnLeftButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	m_bRubMoving = false;
	ReleaseCapture();
	if ( !IsPointIn( x, y))
		m_bBarMouseOn = false;
	return CComponent::OnLeftButtonUp( x, y, key);
}

bool CSliderRub::OnLeftButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	m_bRubMoving = true;
	SetCapture();
	return CComponent::OnLeftButtonDown( x, y, key);
}

bool CSliderRub::OnMouseMove( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	m_bBarMouseOn = true;
	return CComponent::OnMouseMove( x, y, key);
}

void CSliderRub::OnMouseLeave()
{
	if ( IsViewOnly())
		return;

	m_bBarMouseOn = false;
	CComponent::OnMouseLeave();
}

void CSliderRub::Draw(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CComponent::Draw( draw, l, t, cx, cy, cw, ch);

	switch( m_srs)
	{
	case SRS_CUSTOM:
		DrawRub( draw, l, t, cx, cy, cw, ch, m_bBarMouseOn, m_bRubMoving);
		break;
	case SRS_TYPAL:
		draw.FillRect( l + cx, t + cy, l + cx + cw, t + cy + ch, MakeColor( 0, 0, 0));
		break;
	case SRS_IMAGE:
		DrawImageRub( draw, l, t, cx, cy, cw, ch, m_bBarMouseOn, m_bRubMoving);
		break;
	default:
		break;
	}
}

void CSliderRub::DrawRub( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseOn, bool bMoving)
{

}

void CSliderRub::DrawImageRub(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown)
{
	//if ( bButtonDown && m_ips.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::MOUSEDOWN, GetZoomScale()))
	//	return;

	//if ( bMouseIn && m_ips.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::MOUSEON, GetZoomScale()))
	//	return;

	//m_ips.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::NORMAL, GetZoomScale());
	if ( bButtonDown && m_ifmoving.IsValid())
	{
		m_ifmoving.Draw( draw, l, t, cx, cy, cw, ch);
		return;
	}

	if ( bMouseIn && m_ifmouseon.IsValid())
	{
		m_ifmouseon.Draw( draw, l, t, cx, cy, cw, ch);
		return;
	}

	m_ifnormal.Draw( draw, l, t, cx, cy, cw, ch);
}

void CSliderRub::OnComponentShow()
{
	//m_ips.RegisterImages();
	m_ifnormal.RegisterImages();
	m_ifmouseon.RegisterImages();
	m_ifmoving.RegisterImages();
	CComponent::OnComponentShow();
}

void CSliderRub::OnComponentHide()
{
	//m_ips.FreeImages();
	m_ifnormal.FreeImages();
	m_ifmouseon.FreeImages();
	m_ifmoving.FreeImages();
	CComponent::OnComponentHide();
	ReleaseCapture();
}

void CSliderRub::SetRubImageNormal(const ImageParams& ip)
{
	//m_ips.SetImage( ip, CImageable::NORMAL);
	m_ifnormal.SetGridSize( 0, 0, IFP_TOPLEFT);
	m_ifnormal.SetGridSize( 0, 0, IFP_BOTTOMRIGHT);
	m_ifnormal.SetGridSize( GetWidth(), GetHeight(), IFP_CENTER);
	m_ifnormal.SetTileSize( GetWidth(), GetHeight());
	m_ifnormal.SetTileImage( ip, IFP_CENTER);
}

const ImageParams& CSliderRub::GetRubImageNormal() const
{
	//return *m_ips.GetImage( CImageable::NORMAL);
	return m_ifnormal.GetTileImage( IFP_CENTER);
}

void CSliderRub::SetRubImageMouseOn(const ImageParams& ip)
{
	//m_ips.SetImage( ip, CImageable::MOUSEON);
	m_ifmouseon.SetGridSize( 0, 0, IFP_TOPLEFT);
	m_ifmouseon.SetGridSize( 0, 0, IFP_BOTTOMRIGHT);
	m_ifmouseon.SetGridSize( GetWidth(), GetHeight(), IFP_CENTER);
	m_ifmouseon.SetTileSize( GetWidth(), GetHeight());
	m_ifmouseon.SetTileImage( ip, IFP_CENTER);
}

const ImageParams& CSliderRub::GetRubImageMouseOn() const
{
	//return *m_ips.GetImage( CImageable::MOUSEDOWN);
	return m_ifmouseon.GetTileImage( IFP_CENTER);
}

void CSliderRub::SetRubImageMoving(const ImageParams& ip)
{
	//m_ips.SetImage( ip, CImageable::MOUSEDOWN);
	m_ifmoving.SetGridSize( 0, 0, IFP_TOPLEFT);
	m_ifmoving.SetGridSize( 0, 0, IFP_BOTTOMRIGHT);
	m_ifmoving.SetGridSize( GetWidth(), GetHeight(), IFP_CENTER);
	m_ifmoving.SetTileSize( GetWidth(), GetHeight());
	m_ifmoving.SetTileImage( ip, IFP_CENTER);
}

const ImageParams& CSliderRub::GetRubImageMoving() const
{
	//return *m_ips.GetImage( CImageable::MOUSEDOWN);
	return m_ifmoving.GetTileImage( IFP_CENTER);
}

CImagesFill& CSliderRub::GetRubTilesNormal()
{
	return m_ifnormal;
}

CImagesFill& CSliderRub::GetRubTilesMouseOn()
{
	return m_ifmouseon;
}

CImagesFill& CSliderRub::GetRubTilesMoving()
{
	return m_ifmoving;
}

bool CSliderRub::IsMoving() const
{
	return m_bRubMoving;
}

void CSliderRub::SetSize(int w, int h)
{
	int minW = m_ifnormal.GetGridWidth( IFP_TOPLEFT) + m_ifnormal.GetGridWidth( IFP_BOTTOMRIGHT);
	int minH = m_ifnormal.GetGridHeight( IFP_TOPLEFT) + m_ifnormal.GetGridHeight( IFP_BOTTOMRIGHT);
	w = max( minW, w);
	h = max( minH, h);

	m_ifnormal.SetGridSize( w - minW, h - minH, IFP_CENTER);
	m_ifmouseon.SetGridSize( w - minW, h - minH, IFP_CENTER);
	m_ifmoving.SetGridSize( w - minW, h - minH, IFP_CENTER);

	CComponent::SetSize( w, h);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CSliderBar::CSliderBar()
	: m_bSmooth( true), m_nStartOff( 0)
{
	m_rub.SetPos( 0, 0);
	m_rub.SetSize( 5, 5);
	InsertComponent( &m_rub);
	m_rub.ComponentMouseEventNotifyAdd( *this);
}

CSliderBar::~CSliderBar()
{
	m_rub.ComponentMouseEventNotifyRemove( *this);
}

void CSliderBar::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CBarComponent::Draw( draw, l, t, cx, cy, cw, ch);
}


bool CSliderBar::OnLeftButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	PosSmoothFromPoint( x - GetBarAbsX(), y - GetBarAbsY());

	return CBarComponent::OnLeftButtonDown( x, y, key);
}

void CSliderBar::OnMouseMove( CComponent& c, int x, int y, int key)
{
	if ( IsViewOnly())
		return;

	if ( m_rub.IsSameComponent( c) && m_rub.IsMoving() && ( key & MK_LBUTTON))
	{
		if ( m_bSmooth)
			PosSmoothMovingFromPoint( x - GetBarAbsX(), y - GetBarAbsY());
		else
		{
			int pos = min( GetValueMax(), GetPosSmoothMovingFromPoint( x - GetBarAbsX(), y - GetBarAbsY()));
			int barposvalue = ( pos / GetStepSize()) * GetStepSize();
			if ( abs( barposvalue - GetValuePos()) >= GetStepSize() )
				SetValuePos( barposvalue);
			//if ( pos % GetStepSize() <= 1)
			//	SetValuePos( ( pos / GetStepSize()) * GetStepSize());
			else
				AdjustRubPosition( pos);
		}
	}
}

void CSliderBar::PosNoSmoothFromPoint( int x, int y)
{
	if( 0 == m_nMax)
	{
		SetValuePos( 0);
		return;
	}

	int p, a;

	if( m_bVertical)
	{
		p = y;
		a = GetBarHeight();
	}
	else
	{
		p = x;
		a = GetBarWidth();
	}
	if( 0 == a)
	{
		SetValuePos( 0);
		return;
	}
	
	double f = (double)a / (double)m_nMax;
	double fp = f / 2.0;
	if( p < (int)fp)
	{
		SetValuePos( 0);
		return;
	}
	
	fp += (double)p;
	SetValuePos( (int)(fp / f));
}

bool CSliderBar::PosSmoothFromPoint( int x, int y)
{
	if( 0 == m_nMax)
	{
		SetValuePos( 0);
		return false;
	}

	int p, a;
	if( m_bVertical)
	{
		p = y;
		a = GetBarHeight();
	}
	else
	{
		p = x;
		a = GetBarWidth();
	}
	if( a <= GetRubSize())
	{
		SetValuePos( 0);
		return false;
	}

	int bars = (int)((double)(m_nPos) * (double)( a - GetRubSize()) / (double)m_nMax);
	if( p < bars)
	{
		SetValuePos( m_nPos - m_nStepSize);
	}
	else if( p >= ( bars + GetRubSize()))
	{
		SetValuePos( m_nPos + m_nStepSize);
	}
	else
	{
		m_nStartOff = p - bars;
	}
	return true;
}

int  CSliderBar::GetPosSmoothMovingFromPoint( int x, int y)
{
	int p, a;

	if( m_bVertical)
	{
		p = y;
		a = GetBarHeight();
	}
	else
	{
		p = x;
		a = GetBarWidth();
	}
	if( a <= GetRubSize())
	{
		return 0;
	}

	return (int)((double)( p - m_nStartOff) * (double)m_nMax / (double)(a - GetRubSize()) );
}

void CSliderBar::PosSmoothMovingFromPoint( int x, int y)
{
	SetValuePos( GetPosSmoothMovingFromPoint( x, y));
}

void CSliderBar::SetSize(int w, int h)
{
	CBarComponent::SetSize( w, h);
	if ( m_bVertical)
		m_rub.SetSize( w, GetRubSize());
	else
		m_rub.SetSize( GetRubSize(), h);
	AdjustRubPosition();
}

void CSliderBar::SetVertical(bool b /* = true */)
{
	if ( b)
		m_rub.SetSize( GetBarWidth(), GetRubSize());
	else
		m_rub.SetSize( GetRubSize(), GetBarHeight());
	CBarComponent::SetVertical( b);
}

void CSliderBar::SetRubSize( int n)
{
	if ( m_bVertical)
		m_rub.SetSize( GetBarWidth(), n);
	else
		m_rub.SetSize( n, GetBarHeight());
}

int CSliderBar::GetRubSize() const
{
	return m_bVertical ? m_rub.GetHeight() : m_rub.GetWidth();
}

void CSliderBar::SetValuePos(int n)
{
	n = n < 0 ? 0 : n;
	n = n > m_nMax ? m_nMax : n;

	CBarComponent::SetValuePos( n);
	AdjustRubPosition();
}

void CSliderBar::SetValueMax( int n)
{
	if ( n <= 0)
		RemoveComponent( &m_rub);
	else if ( GetValueMax() <= 0)
		InsertComponent( &m_rub);
	CBarComponent::SetValueMax( n);
	AdjustRubPosition();
}

void CSliderBar::AdjustRubPosition( int pos)
{
	if ( pos < 0)
		pos = m_nPos;

	int rubpos = (int)( (double)pos * (double)( ( m_bVertical ? GetBarHeight() : GetBarWidth()) - GetRubSize()) / (double)m_nMax);
	if ( m_bVertical)
		m_rub.SetPos( 0, rubpos);
	else
		m_rub.SetPos( rubpos, 0);
}

void CSliderBar::OnComponentShow()
{
	m_rub.OnComponentShow();
	CBarComponent::OnComponentShow();
}

void CSliderBar::OnComponentHide()
{
	m_rub.OnComponentHide();
	CBarComponent::OnComponentHide();
}

void CSliderBar::SetSmooth( bool b)
{
	m_bSmooth = b;
}

bool CSliderBar::GetSmooth() const
{
	return m_bSmooth;
}

void CSliderBar::SetSliderRubStyle( SliderRubStyle srs)
{
	m_rub.SetSliderRubStyle( srs);
}

SliderRubStyle CSliderBar::GetSliderRubStyle() const
{
	return m_rub.GetSliderRubStyle();
}

void CSliderBar::SetSliderScaleStyleImage( const ImageParams& ip)
{
	SetBackGroundStyleImage( ip);
}

void CSliderBar::SetSliderRubStyleImage( const ImageParams& ipNormal, const ImageParams& ipMouseOn, const ImageParams& ipMoving)
{
	m_rub.SetSliderRubStyle( SRS_IMAGE);
	m_rub.SetRubImageNormal( ipNormal);
	m_rub.SetRubImageMouseOn( ipMouseOn);
	m_rub.SetRubImageMoving( ipMoving);
}

const ImageParams& CSliderBar::GetRubImageNormal() const
{
	return m_rub.GetRubImageNormal();
}

const ImageParams& CSliderBar::GetRubImageMouseOn() const
{
	return m_rub.GetRubImageMouseOn();
}

const ImageParams& CSliderBar::GetRubImageMoving() const
{
	return m_rub.GetRubImageMoving();
}

CImagesFill& CSliderBar::GetRubTilesNormal()
{
	return m_rub.GetRubTilesNormal();
}

CImagesFill& CSliderBar::GetRubTilesMouseOn()
{
	return m_rub.GetRubTilesMouseOn();
}

CImagesFill& CSliderBar::GetRubTilesMoving()
{
	return m_rub.GetRubTilesMoving();
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CProgressBar::CProgressBar()
	: m_pbs( PBS_NONE)
{
}

CProgressBar::~CProgressBar()
{
}

void CProgressBar::StepIt()
{
	SetValuePos( m_nPos + m_nStepSize);
}

void CProgressBar::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CBarComponent::Draw( draw, l, t, cx, cy, cw, ch);
	if( cw <= 1 || ch <= 1)
		return;
	
	DoDrawProgress( draw, l, t, cx, cy, cw, ch);
}

void CProgressBar::OnComponentShow()
{
	if ( PBS_IMAGE == m_pbs)
		CImageTextureManager::GetInstance().InsertImage( m_ipProgress.nBitmap);
	else if ( PBS_TILE == m_pbs)
		m_ifProgress.RegisterImages();
	CBarComponent::OnComponentShow();
}

void CProgressBar::OnComponentHide()
{
	if ( PBS_IMAGE == m_pbs)
		CImageTextureManager::GetInstance().RemoveImage( m_ipProgress.nBitmap);
	else if ( PBS_TILE == m_pbs)
		m_ifProgress.FreeImages();
	CBarComponent::OnComponentHide();
}

ProgressBarStyle CProgressBar::GetProgressBarStyle() const
{
	return m_pbs;
}

void CProgressBar::SetProgressBarStyle( ProgressBarStyle pbs)
{
	m_pbs = pbs;
}

void CProgressBar::SetProgressBarStyleColor( COLOR clr)
{
	m_pbs = PBS_COLOR;
	m_clrProgress = clr;
}

void CProgressBar::SetProgressBarStyleImage( const ImageParams& ip)
{
	m_pbs = PBS_IMAGE;
	m_ipProgress = ip;
}

void CProgressBar::SetProgressBarStyleSimpleTile( const ImageParams& ip, int nTileWidth, int nTileHeight)
{
	m_pbs = PBS_TILE;
	m_ifProgress.SetTileSize( nTileWidth, nTileHeight);
	m_ifProgress.SetTileImage( ip, GXWINDOW::IFP_CENTER);
}

CImagesFill& CProgressBar::GetProgressBarImageFill()
{
	return m_ifProgress;
}

COLOR CProgressBar::GetProgressBarColor()
{
	return m_clrProgress;
}

ImageParams& CProgressBar::GetProgressBarImage()
{
	return m_ipProgress;
}

void CProgressBar::DrawProgress( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int pos, int max)
{
}

void CProgressBar::DoDrawProgress( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	if( PBS_NONE == m_pbs || 0 == m_nMax || 0 == m_nPos)
		return;

	if( m_bVertical)
	{
		int nb = (int)((double)GetBarHeight() * (double)m_nPos / (double)m_nMax);
		
		if( nb <= cy)
			return;
		else if( nb <= ( cy + ch))
			ch = nb - cy;
	}
	else
	{
		int nr = (int)( (double)GetBarWidth() * (double)m_nPos / (double)m_nMax );
		if( nr <= cx)
			return;
		else if( nr < ( cx + cw))
			cw = nr - cx;
	}

	switch( m_pbs) 
	{
	case PBS_CUSTOM:
		DrawProgress( draw, l, t, cx, cy, cw, ch, m_nPos, m_nMax);
		break;
	case PBS_COLOR:
		draw.FillRect( l + cx, t + cy, l + cx + cw, t + cy + ch, m_clrProgress);
		break;
	case PBS_IMAGE:
		draw.DrawPicture( m_ipProgress, l + cx, t + cy, cw, ch, cx, cy, cw, ch);
		break;
	case PBS_TILE:
		m_ifProgress.SetGridSize( max( 0, cw - m_ifProgress.GetGridWidth( IFP_TOPLEFT) - m_ifProgress.GetGridWidth( IFP_BOTTOMRIGHT)), ch - m_ifProgress.GetGridHeight( IFP_TOPLEFT) - m_ifProgress.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
		m_ifProgress.Draw( draw, l + cx, t + cy, cx, cy, cw, ch);
		break;
	default:
		break;
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CScrollBarComponentAllocator::CScrollBarComponentAllocator()
{
}

CScrollBarComponentAllocator::~CScrollBarComponentAllocator()
{
}

CImageButton* CScrollBarComponentAllocator::NewImageButton()
{
	return new CImageButton();
}

void CScrollBarComponentAllocator::DeleteImageButton( CImageButton* btn)
{
	delete btn;
}

namespace INLINECLASS {

CScrollBarComponentAllocator g_DefaultScrollBarAllocator;

} // namespace INLINECLASS {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CScrollBar::CScrollBar()
{
	Initialize( INLINECLASS::g_DefaultScrollBarAllocator);
}

CScrollBar::~CScrollBar()
{
	m_btnTL->ButtonClickNotifyRemove( *this);
	m_btnRB->ButtonClickNotifyRemove( *this);

	m_Alloc->DeleteImageButton( m_btnTL);
	m_Alloc->DeleteImageButton( m_btnRB);
}

CScrollBar::CScrollBar( CScrollBarComponentAllocator& alloc)
{
	Initialize( alloc);
}

void CScrollBar::Initialize( CScrollBarComponentAllocator& alloc)
{
	m_Alloc = &alloc;

	m_btnTL = m_Alloc->NewImageButton();
	m_btnRB = m_Alloc->NewImageButton();

	m_btnTL->SetPos( 0, 0);
	m_btnTL->SetSize( 20, 20);
	m_btnTL->ButtonClickNotifyAdd( *this);
	m_btnRB->ButtonClickNotifyAdd( *this);	

	m_bAdaptMode = true;

	InsertComponent( m_btnTL);
	InsertComponent( m_btnRB);
}

void CScrollBar::CheckButtonSize( int w, int h, int bs)
{
	if( GetVertical())
	{
		m_btnTL->SetSize( w, bs);
		m_btnRB->SetSize( w, bs);
		m_btnRB->SetPos( 0, h - bs);
		m_rub.SetPos( 0, bs);
	}
	else
	{
		m_btnTL->SetSize( bs, h);
		m_btnRB->SetSize( bs, h);
		m_btnRB->SetPos( w - bs, 0);
		m_rub.SetPos( bs, 0);
	}
	AdjustRubPosition();
}

void CScrollBar::SetSize( int w, int h)
{
	CSliderBar::SetSize( w, h);
	CheckButtonSize( w, h, GetButtonSize() );
}

void CScrollBar::SetButtonSize( int n)
{
	CheckButtonSize( GetWidth(), GetHeight(), n);
}

int CScrollBar::GetButtonSize() const
{
	return m_bVertical ? m_btnTL->GetHeight() : m_btnTL->GetWidth();
}

void CScrollBar::SetAdaptMode( bool bAdapt)
{
	m_bAdaptMode = bAdapt;
}

bool CScrollBar::IsAdaptMode() const
{
	return m_bAdaptMode;
}

void CScrollBar::SetVertical( bool b)
{
	int btnsize = GetButtonSize();
	CSliderBar::SetVertical( b);
	CheckButtonSize( GetWidth(), GetHeight(), btnsize);
}

void CScrollBar::SetValueMax( int n)
{
	if ( n <= 0)
	{
		m_btnRB->SetEnable( false);
		m_btnTL->SetEnable( false);
	}
	else if ( GetValueMax() <= 0)
	{
		m_btnTL->SetEnable();
		m_btnRB->SetEnable();
	}
	CSliderBar::SetValueMax( n);
}

void CScrollBar::OnButtonClick( CAbstractButton& c)
{
	if ( IsViewOnly())
		return;

	if( c.IsSameComponent( m_btnTL))
		SetValuePos( GetValuePos() - GetStepSize());
	else if( c.IsSameComponent( m_btnRB))
		SetValuePos( GetValuePos() + GetStepSize());
}

int CScrollBar::GetBarWidth() const
{
	int w = CSliderBar::GetBarWidth();
	if( m_bVertical)
		return w;
	else
		return max( 0, w - 2 * GetButtonSize());
}

int CScrollBar::GetBarHeight() const
{
	int h = CSliderBar::GetBarHeight();
	if( m_bVertical)
		return max( 0, h - 2 * GetButtonSize());
	else
		return h;
}

int CScrollBar::GetBarAbsX() const
{
	int x = CSliderBar::GetBarAbsX();
	if( m_bVertical)
		return x;
	else
		return x + GetButtonSize();
}

int CScrollBar::GetBarAbsY() const
{
	int y = CSliderBar::GetBarAbsY();
	if( m_bVertical)
		return y + GetButtonSize();
	else
		return y;
}

void CScrollBar::AdjustRubPosition( int pos)
{
	if ( pos < 0)
		pos = m_nPos;

	if ( m_bAdaptMode)
	{
		if ( m_bVertical)
			m_rub.SetSize( GetBarWidth(), m_nMax > 0 ? GetBarHeight() * GetBarHeight() / ( GetBarHeight() + m_nMax) : 0);
		else
			m_rub.SetSize( m_nMax > 0 ? GetBarWidth() * GetBarWidth() / ( GetBarWidth() + m_nMax) : 0, GetBarHeight());
	}

	int rubpos = (int)( (double)pos * (double)( ( m_bVertical ? GetBarHeight() : GetBarWidth()) - GetRubSize()) / (double)m_nMax) + GetButtonSize();
	if ( m_bVertical)
		m_rub.SetPos( 0, rubpos);
	else
		m_rub.SetPos( rubpos, 0);
}

void CScrollBar::OnComponentShow()
{
	m_btnTL->OnComponentShow();
	m_btnRB->OnComponentShow();
	CSliderBar::OnComponentShow();
}

void CScrollBar::OnComponentHide()
{
	m_btnTL->OnComponentHide();
	m_btnRB->OnComponentHide();
	CSliderBar::OnComponentHide();
}

void CScrollBar::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	// 以下代码是为了不在两个按钮后面绘制槽
	{
		if ( GetVertical())
			CBKPanel::DoDrawBackGround( draw, l, t + m_btnTL->GetHeight(), cx, cy, cw, ch - m_btnTL->GetHeight() * 2);
		else
			CBKPanel::DoDrawBackGround( draw, l + m_btnRB->GetWidth(), t, cx, cy, cw - m_btnRB->GetWidth() * 2, ch);
		CPanel::Draw( draw, l, t, cx, cy, cw, ch);
	}
}

void CScrollBar::SetSliderRubStyleImage( const ImageParams& ipNormal, const ImageParams& ipMouseOn, const ImageParams& ipMoving)
{
	SetAdaptMode( false);
	CSliderBar::SetSliderRubStyleImage( ipNormal, ipMouseOn, ipMoving);
}

void CScrollBar::SetTopLeftButtonImage( const ImageParams& ipNormal, const ImageParams& ipMouseOn, const ImageParams& ipMouseDown, const ImageParams& ipDisable)
{
	m_btnTL->SetComponentImageNormal( ipNormal);
	m_btnTL->SetComponentImageMouseIn( ipMouseOn);
	m_btnTL->SetImageButtonDown( ipMouseDown);
	m_btnTL->SetComponentImageDisable( ipDisable);
}

void CScrollBar::SetRightBottomButtonImage( const ImageParams& ipNormal, const ImageParams& ipMouseOn, const ImageParams& ipMouseDown, const ImageParams& ipDisable)
{
	m_btnRB->SetComponentImageNormal( ipNormal);
	m_btnRB->SetComponentImageMouseIn( ipMouseOn);
	m_btnRB->SetImageButtonDown( ipMouseDown);
	m_btnRB->SetComponentImageDisable( ipDisable);
}

bool CScrollBar::OnLeftButtonDown( int x, int y, int key)
{
	if ( m_btnTL->IsAbsPointIn( x, y) || m_btnRB->IsAbsPointIn( x, y))
		return CBarComponent::OnLeftButtonDown( x, y, key);
	else
		return CSliderBar::OnLeftButtonDown( x, y, key);
}


} // namespace GXWINDOW
