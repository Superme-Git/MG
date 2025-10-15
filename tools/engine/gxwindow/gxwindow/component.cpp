#include "gxwindowdef.h"

namespace GXWINDOW { 

namespace INLINECLASS {

class CComponentSizeChangedNotifySet : public CNotifySet<CComponentSizeChangedNotify*>
{
public:
	CComponentSizeChangedNotifySet() {}
	virtual ~CComponentSizeChangedNotifySet() {}
};

class CComponentMouseEventNotifySet : public CNotifySet<CComponentMouseEventNotify*>
{
public:
	CComponentMouseEventNotifySet() {}
	virtual ~CComponentMouseEventNotifySet() {}
};

class CComponentShowHideNotifySet : public CNotifySet<CComponentShowHideNotify*>
{
public:
	CComponentShowHideNotifySet() {}
	virtual ~CComponentShowHideNotifySet() {}
};

} // namespace INLINECLASS {


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CComponentSizeChangedNotify::CComponentSizeChangedNotify()
{
}

CComponentSizeChangedNotify::~CComponentSizeChangedNotify()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CComponentMouseEventNotify::CComponentMouseEventNotify()
{
}

CComponentMouseEventNotify::~CComponentMouseEventNotify()
{
}

void CComponentMouseEventNotify::OnLeftButtonDown( CComponent& c, int x, int y, int key)
{
}

void CComponentMouseEventNotify::OnLeftButtonUp( CComponent& c, int x, int y, int key)
{
}

//void CComponentMouseEventNotify::OnLeftButtonDoubleClick( CComponent& c, int x, int y, int key)
//{
//}

void CComponentMouseEventNotify::OnRightButtonDown( CComponent& c, int x, int y, int key)
{
}

void CComponentMouseEventNotify::OnRightButtonUp( CComponent& c, int x, int y, int key)
{
}

//void CComponentMouseEventNotify::OnRightButtonDoubleClick( CComponent& c, int x, int y, int key)
//{
//}

void CComponentMouseEventNotify::OnMouseMove( CComponent& c, int x, int y, int key)
{
}

void CComponentMouseEventNotify::OnMouseWheel( CComponent& c, int x, int y, int key)
{
}

void CComponentMouseEventNotify::OnMouseEnter( CComponent&c)
{

}

void CComponentMouseEventNotify::OnMouseLeave( CComponent&c)
{

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTipFrameQuery::CTipFrameQuery()
{
}

CTipFrameQuery::~CTipFrameQuery()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CComponentAfterDrawNofity::CComponentAfterDrawNofity()
{
}

CComponentAfterDrawNofity::~CComponentAfterDrawNofity()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CComponentShowHideNotify::CComponentShowHideNotify() {}
CComponentShowHideNotify::~CComponentShowHideNotify() {}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
CComponent::CComponent()
: m_parent( NULL), m_tipinfo( NULL), m_pTipShowPos( NULL), 
m_sizens( new INLINECLASS::CComponentSizeChangedNotifySet()), m_bMouseEnter( false), m_nWidth( 0), m_nHeight( 0),
m_bEnable( true), m_bNotTransparent(true), m_bViewOnly( false), m_mens( new INLINECLASS::CComponentMouseEventNotifySet()),
m_shns( new INLINECLASS::CComponentShowHideNotifySet()),m_pTipQuery( NULL), m_pname( new INLINECLASS::CSimpleString( L"")),
m_ptiptext( new INLINECLASS::CSimpleString( L"")), m_x( -1), m_y( -1), m_showtip( NULL),m_ComponentAfterDrawNofity( NULL)
{
}

CComponent::~CComponent()
{
	assert( NULL == m_showtip && "析构对象前，必须隐藏已经显示的TIP框");
	ReleaseCapture();
	delete m_sizens;
	delete m_mens;
	delete m_shns;
	delete m_pname;
	delete m_ptiptext;
}

void CComponent::ComponentAfterDrawNofityAdd( CComponentAfterDrawNofity& n)
{
	if( m_ComponentAfterDrawNofity)
	{
		assert( !"每个控件只允许设置一个 CComponentAfterDrawNofity 对象");
		return;
	}
	m_ComponentAfterDrawNofity = &n;
}

void CComponent::ComponentAfterDrawNofityRemove( CComponentAfterDrawNofity& n)
{
	if( m_ComponentAfterDrawNofity != &n)
	{
		assert( !"移除一个没有设置成功的 CComponentAfterDrawNofity 对象");
		return;
	}
	m_ComponentAfterDrawNofity = NULL;
}

void CComponent::ComponentAfterDrawNofityClear()
{
	m_ComponentAfterDrawNofity = NULL;
}


void CComponent::SetTipInfo( CComponent* pinfo, CTipShowPosLayout* poslayout)
{
	HideTipInfo();
	m_tipinfo = pinfo;
	m_pTipShowPos = poslayout;
}

CComponent* CComponent::GetTipInfo()
{
	return m_tipinfo;
}

const CComponent* CComponent::GetTipInfo() const
{
	return m_tipinfo;
}

static inline bool CheckCompomentIsVisible( CComponent& com)
{
	CFrame* f = com.GetAbsParentFrame();
	if( NULL == f)
		assert( !"当前控件必须属于一个窗体，才可以显示TIP信息");
	if( !f->IsVisible())
		assert( !"当前控件必须属于一个显示的窗体，才可以显示TIP信息");
	return true;
}

void CComponent::ShowTipInfo()
{
	CDesktop& desktop = CDesktop::GetDefault();

	m_showtip = NULL;
	if( m_tipinfo)
		m_showtip = m_tipinfo;
	else if ( !m_ptiptext->empty())
		m_showtip = desktop.GetTipInfoFrameManager().GetTipInfoFrame( m_ptiptext->c_str());
	else if ( m_pTipQuery)
		m_showtip = m_pTipQuery->QueryTipFrame( *this);
	if( NULL == m_showtip)
		return;	
	assert( CheckCompomentIsVisible( *this));
	
	CTipShowPosLayout* oldlayout = desktop.GetTipShowPosLayout();
	if ( m_pTipShowPos)
		desktop.SetTipShowPosLayout( m_pTipShowPos);
	desktop.ShowTipInfo( m_showtip, *this);
	desktop.SetTipShowPosLayout( oldlayout);
}

void CComponent::HideTipInfo()
{
	if( m_showtip)
	{
		CDesktop::GetDefault().HideTipInfo( m_showtip);
		m_showtip = NULL;
	}
}

void CComponent::SetComponentName( const wchar_t* name)
{
	m_pname->assign( NULL != name ? name : L"");
}

const wchar_t* CComponent::GetComponentName() const
{
	return m_pname->c_str();
}

void CComponent::SetTipText( const wchar_t* text)
{
	m_ptiptext->assign( NULL != text ? text : L"");
}

void CComponent::SetTipText( int n)
{
	SetTipText( CTextResourceManager::GetInstance().GetText( n));
}

const wchar_t* CComponent::GetTipText() const
{
	return m_ptiptext->c_str();
}

CComponent* CComponent::GetParent()
{
	return m_parent;
}

const CComponent* CComponent::GetParent() const
{
	return m_parent;
}

void CComponent::SetEnable( bool bEnable)
{
	m_bEnable = bEnable;
	INVALIDATE();
}

bool CComponent::IsEnable() const
{
	return m_bEnable;
}

void CComponent::SetTransparent(bool bTrans /* = true */)
{
	m_bNotTransparent = !bTrans;
}

bool CComponent::IsTransparent() const
{
	return !m_bNotTransparent;
}

void CComponent::SetViewOnly( bool bViewOnly /*= true*/)
{
	m_bViewOnly = bViewOnly;
}

bool CComponent::IsViewOnly() const
{
	return m_bViewOnly;
}

void CComponent::SetParent( CComponent* parent)
{
	m_parent = parent;
}

void CComponent::ComponentSizeChangedNotifyAdd( CComponentSizeChangedNotify& cmsn)
{
	m_sizens->Insert( &cmsn);
}

void CComponent::ComponentSizeChangedNotifyRemove( CComponentSizeChangedNotify& cmsn)
{
	m_sizens->Remove( &cmsn);
}

void CComponent::ComponentMouseEventNotifyAdd( CComponentMouseEventNotify& men)
{
	m_mens->Insert( &men);
}

void CComponent::ComponentMouseEventNotifyRemove( CComponentMouseEventNotify& men)
{
	m_mens->Remove( &men);
}

void CComponent::ComponentShowHideNotifyAdd( CComponentShowHideNotify& shn)
{
	m_shns->Insert( &shn);
}

void CComponent::ComponentShowHideNotifyRemove( CComponentShowHideNotify& shn)
{
	m_shns->Remove( &shn);
}

void CComponent::TipFrameQueryAdd( CTipFrameQuery& tfq)
{
	assert( !m_pTipQuery && "不要随意设置2个查询对象，只能有一个有效");
	m_pTipQuery = &tfq;
}

void CComponent::TipFrameQueryRemove()
{
	m_pTipQuery = NULL;
}

bool CComponent::IsViewCliped( int cw, int ch) const
{
	return m_nWidth != cw || m_nHeight != ch;
}

CFrame* CComponent::GetParentFrame()
{
	CComponent* p = this;
	while( p )
	{
		CFrame* f = dynamic_cast<CFrame*>(p);
		if( f)
			return f;
		p = p->m_parent;
	}
	return NULL;
}

CFrame* CComponent::GetAbsParentFrame()
{
	CComponent* p = this;
	while ( p )
	{
		CFrame* f = dynamic_cast<CFrame*>(p);
		p = p->m_parent;
		if ( f)
		{
			if ( p && !CDesktop::GetDefault().IsSameComponent( p))
				return p->GetAbsParentFrame();
			else
				return f;
		}
	}
	return NULL;
}

bool CComponent::IsSameComponent( const CComponent& c) const
{
	return this == &c;
}

bool CComponent::IsSameComponent( const CComponent* p) const
{
	return this == p;
}

bool CComponent::operator==( const CComponent& c) const
{
	return this == &c;
}

void CComponent::SetStyle( int x, int y, int w, int h)
{
	SetPos( x, y);
	SetSize( w, h);
}

void CComponent::OnMouseEnter()
{
	if ( m_bViewOnly)
		return;

	if ( !m_bMouseEnter)
		INVALIDATE();

//	if ( m_bNotTransparent)
	{
		ShowTipInfo();
		if ( m_bEnable && !m_bMouseEnter)
			m_mens->NotifyAll( *this, &CComponentMouseEventNotify::OnMouseEnter);
		m_bMouseEnter = true;
	}
}

void CComponent::OnMouseLeave()
{
	if ( m_bViewOnly)
		return;

	if ( m_bMouseEnter)
		INVALIDATE();

	HideTipInfo();
	if ( m_bNotTransparent && m_bEnable && m_bMouseEnter)
		m_mens->NotifyAll( *this, &CComponentMouseEventNotify::OnMouseLeave);
	m_bMouseEnter = false;
}

void CComponent::OnComponentShow()
{
	static bool bShow = true;
	m_shns->NotifyAll( *this, &CComponentShowHideNotify::OnComponentShowHide, bShow);
	INVALIDATE();
}

void CComponent::OnComponentHide()
{
	static bool bShow = false;
	m_shns->NotifyAll( *this, &CComponentShowHideNotify::OnComponentShowHide, bShow);
	HideTipInfo();
	INVALIDATE();
}

void CComponent::SetCapture()
{
	if ( m_bViewOnly)
		return;

	if ( m_bEnable)
		CDesktop::GetDefault().SetCapture( this);
}

void CComponent::ReleaseCapture()
{
	if ( CDesktop* p = CDesktop::GetDefaultPointer())
		p->ReleaseCapture( this);
}

void CComponent::SetPos( int x, int y)
{
	m_x = x;
	m_y = y;

	m_nOrigX = x;
	m_nOrigY = y;

	INVALIDATE();
}

void CComponent::SetSize( int w, int h)
{
	int ow = m_nWidth;
	int oh = m_nHeight;

	m_nWidth = w;
	m_nHeight = h;

	if( ow != w || oh != h)
		m_sizens->NotifyAll( *this, &CComponentSizeChangedNotify::OnComponentSizeChanged, w, h, ow, oh);

	INVALIDATE();
}

int CComponent::GetPosX() const
{
	return m_x;
}

int CComponent::GetPosY() const
{
	return m_y;
}

int CComponent::GetPosR() const
{
	return m_x + m_nWidth;
}

int CComponent::GetPosB() const
{
	return m_y + m_nHeight;
}

int CComponent::GetWidth() const
{
	return m_nWidth;
}

int CComponent::GetHeight() const
{
	return m_nHeight;
}

int CComponent::GetAbsWidth() const
{
	return m_nWidth;
}

int CComponent::GetAbsHeight() const
{
	return m_nHeight;
}

int CComponent::GetAbsPosX() const
{
	if( NULL == m_parent)
		return m_x;
	else
		return m_parent->GetAbsPosX() + m_x;
}

int CComponent::GetAbsPosY() const
{
	if( NULL == m_parent)
		return m_y;
	else
		return m_parent->GetAbsPosY() + m_y;
}

int CComponent::GetAbsPosR() const
{
	if( NULL == m_parent)
		return GetPosR();
	else
		return GetAbsPosX() + m_nWidth;
}

int CComponent::GetAbsPosB() const
{
	if( NULL == m_parent)
		return GetPosB();
	else
		return GetAbsPosY() + m_nHeight;
}

void CComponent::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
}

void CComponent::Draw( CDraw& draw)
{
	int l = GetAbsPosX();
	int	t = GetAbsPosY();
	Draw( draw, l, t, 0, 0, GetWidth(), GetHeight());
}

void CComponent::DoComponentAfterDrawNofity( CDraw& draw)
{
	if( m_ComponentAfterDrawNofity)
	{
		int l = GetAbsPosX();
		int	t = GetAbsPosY();
		m_ComponentAfterDrawNofity->OnComponentAfterDraw( *this, draw, l, t, 0, 0, GetWidth(), GetHeight());
	}
}

void CComponent::DoComponentAfterDrawNofity( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	if( m_ComponentAfterDrawNofity)
		m_ComponentAfterDrawNofity->OnComponentAfterDraw( *this, draw, l, t, cx, cy, cw, ch);
}

bool CComponent::IsPointIn( int x, int y) const
{
	return ( x >= m_x && y >= m_y && x <= GetPosR() && y <= GetPosB() );
}

bool CComponent::IsAbsPointIn( int x, int y) const
{
	const CComponent* p = GetParent();
	if( NULL == p) // this is desktop
		return true;
	else
		return IsPointIn( x - p->GetAbsPosX(), y - p->GetAbsPosY());
}

bool CComponent::OnLeftButtonDown( int x, int y, int key)
{
	if ( m_bViewOnly)
		return false;

	if ( m_bEnable && m_bNotTransparent)
		m_mens->NotifyAll( *this, &CComponentMouseEventNotify::OnLeftButtonDown, x, y, key);
	return m_bNotTransparent;
}

bool CComponent::OnLeftButtonUp( int x, int y, int key)
{
	if ( m_bViewOnly)
		return false;

	if ( m_bEnable && m_bNotTransparent)
		m_mens->NotifyAll( *this, &CComponentMouseEventNotify::OnLeftButtonUp, x, y, key);
	return m_bNotTransparent;
}
//
//bool CComponent::OnLeftButtonDoubleClick( int x, int y, int key)
//{
//	if ( m_bViewOnly)
//		return false;
//
//	if ( m_bEnable && m_bNotTransparent)
//		m_mens->NotifyAll( *this, &CComponentMouseEventNotify::OnLeftButtonDoubleClick, x, y, key);
//	return m_bNotTransparent;
//}

bool CComponent::OnRightButtonDown( int x, int y, int key)
{
	if ( m_bViewOnly)
		return false;

	if ( m_bEnable && m_bNotTransparent)
		m_mens->NotifyAll( *this, &CComponentMouseEventNotify::OnRightButtonDown, x, y, key);
	return m_bNotTransparent;
}

bool CComponent::OnRightButtonUp( int x, int y, int key)
{
	if ( m_bViewOnly)
		return false;

	if ( m_bEnable && m_bNotTransparent)
		m_mens->NotifyAll( *this, &CComponentMouseEventNotify::OnRightButtonUp, x, y, key);
	return m_bNotTransparent;
}

//bool CComponent::OnRightButtonDoubleClick( int x, int y, int key)
//{
//	if ( m_bViewOnly)
//		return false;
//
//	if ( m_bEnable && m_bNotTransparent)
//		m_mens->NotifyAll( *this, &CComponentMouseEventNotify::OnRightButtonDoubleClick, x, y, key);
//	return  m_bNotTransparent;
//}

bool CComponent::OnMouseMove( int x, int y, int key)
{
	if ( m_bViewOnly)
		return false;

	if ( m_bEnable && m_bNotTransparent)
		m_mens->NotifyAll( *this, &CComponentMouseEventNotify::OnMouseMove, x, y, key);
	return  m_bNotTransparent;
}

bool CComponent::OnMouseWheel( int x, int y, int key)
{
	if ( m_bViewOnly)
		return false;

	if ( m_bEnable && m_bNotTransparent)
		m_mens->NotifyAll( *this, &CComponentMouseEventNotify::OnMouseWheel, x, y, key);
	return  m_bNotTransparent;
}

bool CComponent::IsPointInMsgTransparent( int x, int y) const
{
	assert( IsPointIn( x, y) && "该方法要求输入的点，必须在控件内");
	return !m_bNotTransparent;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CBKComponent::CBKComponent()
	: m_BackGroundStyle( BGS_NONE), m_BackGroundColor( 0)
{
}

CBKComponent::~CBKComponent()
{
}

void CBKComponent::SetStyle( int x, int y, int w, int h, BackGroundStyle bgs)
{
	CComponent::SetStyle( x, y, w, h);
	SetBackGroundStyle( bgs);
}

void CBKComponent::SetBackGroundStyle( BackGroundStyle bgs)
{
	m_BackGroundStyle = bgs;
}

void CBKComponent::SetBackGroundStyleImage( const ImageParams& ip)
{
	SetBackGroundStyle( BGS_IMAGE);
	m_BackGroundTileImages.SetGridSize( 0, 0, IFP_TOPLEFT);
	m_BackGroundTileImages.SetGridSize( 0, 0, IFP_BOTTOMRIGHT);
	m_BackGroundTileImages.SetGridSize( GetWidth(), GetHeight(), IFP_CENTER);
	m_BackGroundTileImages.SetTileSize( GetWidth(), GetHeight());
	m_BackGroundTileImages.SetTileImage( ip, IFP_CENTER);
}

void CBKComponent::SetBackGroundStyleColor( COLOR clr)
{
	SetBackGroundStyle( BGS_COLOR);
	m_BackGroundColor = clr;
}

BackGroundStyle CBKComponent::GetBackGroundStyle() const
{
	return m_BackGroundStyle;
}

void CBKComponent::DrawBackGround( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{

}

void CBKComponent::DoDrawBackGround( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	switch( m_BackGroundStyle) 
	{
	case BGS_CUSTOM:
		DrawBackGround( draw, l, t, cx, cy, cw, ch);
		break;
	case BGS_COLOR:
		draw.FillRect( l + cx, t + cy, l + cx + cw, t + cy + ch, m_BackGroundColor);
		break;
	case BGS_IMAGE:
		//draw.DrawPicture( m_BackGroundBitmap, l + cx, t + cy, cw, ch, cx, cy);
		m_BackGroundTileImages.Draw( draw, l, t, cx, cy, cw, ch);
		break;
	default:
		break;
	}
}

void CBKComponent::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	DoDrawBackGround( draw, l, t, cx, cy, cw, ch);
	CComponent::Draw( draw, l, t, cx, cy, cw, ch);
}

void CBKComponent::OnComponentShow()
{
	m_BackGroundTileImages.RegisterImages();
	CComponent::OnComponentShow();
}

void CBKComponent::OnComponentHide()
{
	m_BackGroundTileImages.FreeImages();
	CComponent::OnComponentHide();
}

CImagesFill& CBKComponent::GetTilesBackGround()
{
	return m_BackGroundTileImages;
}

const CImagesFill& CBKComponent::GetTilesBackGround() const
{
	return m_BackGroundTileImages;
}

void CBKComponent::SetSize( int w, int h)
{
	CComponent::SetSize( w, h);
	if ( BGS_IMAGE == m_BackGroundStyle)
	{
		m_BackGroundTileImages.SetGridSize( w - m_BackGroundTileImages.GetGridWidth( IFP_TOPLEFT) - m_BackGroundTileImages.GetGridWidth( IFP_BOTTOMRIGHT)
			, h - m_BackGroundTileImages.GetGridHeight( IFP_TOPLEFT) - m_BackGroundTileImages.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
	}
}

} // namespace GXWINDOW {
