#include "gxwindowdef.h"

namespace GXWINDOW { 

namespace INLINECLASS {

class CFrameClosedNotifySet : public CNotifySet<CFrameClosedNotify*>
{
public:
	CFrameClosedNotifySet() {}
	virtual ~CFrameClosedNotifySet() {}
};

class CFramePosChangedNotifySet : public CNotifySet<CFramePosChangedNotify*>
{
public:
	CFramePosChangedNotifySet() {}
	virtual ~CFramePosChangedNotifySet() {}
};

class CFrameShowHideNotifySet : public CNotifySet<CFrameShowHideNotify*>
{
public:
	CFrameShowHideNotifySet() {}
	virtual ~CFrameShowHideNotifySet() {}
};

class CFrameDestructureNotifySet : public CNotifySet<CFrameDestructureNotify*>
{
public:
	CFrameDestructureNotifySet() {}
	virtual ~CFrameDestructureNotifySet() {}
};


} // namespace INLINECLASS {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CLayoutManager::CLayoutManager() {}
CLayoutManager::~CLayoutManager() {}

CPositionLayoutManager::CPositionLayoutManager() {}
CPositionLayoutManager::~CPositionLayoutManager() {}

CLayoutManager* CPositionLayoutManager::Clone() const
{
	return new CPositionLayoutManager;
}

void CPositionLayoutManager::Delete()
{
	delete this;
}

void CPositionLayoutManager::Layout( CPanel& panel) {}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CPanel::CPanel( void*)
	: m_children( new INLINECLASS::CDesktopContainer()), m_pLastMoveIn( NULL), m_layout( CPositionLayoutManager().Clone()), m_clrBorder( 0)
{

}


CPanel::CPanel()
	: m_children( new INLINECLASS::CContainer()), m_pLastMoveIn( NULL), m_layout( CPositionLayoutManager().Clone()), m_clrBorder( 0)
{
}

CPanel::~CPanel()
{
	delete m_children;
	m_layout->Delete();
}

void CPanel::SetLayoutManager( const CLayoutManager& lm)
{
	m_layout->Delete();
	m_layout = lm.Clone();
}

const CLayoutManager& CPanel::GetLayoutManager() const
{
	return *m_layout;
}

void CPanel::DoLayout()
{
	m_layout->Layout( *this);
	int ncount = GetChildCount();
	for( int i = 0; i < ncount; i ++)
	{
		if( CPanel* sub = dynamic_cast<CPanel*>( GetChild( i)))
			sub->DoLayout();
	}
	INVALIDATE();
}

void CPanel::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CComponent::Draw( draw, l, t, cx, cy, cw, ch);
	if( IsViewCliped( cw, ch))
		m_children->Draw( draw, cx, cy, cw, ch, m_clrBorder);
	else
		m_children->Draw( draw, m_clrBorder);
}

void CPanel::OnComponentShow()
{
	DoLayout();
	for ( INLINECLASS::CContainer::iterator it = m_children->begin(); it != m_children->end(); ++ it)
	{
		( *it)->OnComponentShow();
	}
	CComponent::OnComponentShow();
}

void CPanel::OnComponentHide()
{
	for ( INLINECLASS::CContainer::iterator it = m_children->begin(); it != m_children->end(); ++ it)
	{
		( *it)->OnComponentHide();
	}
	CComponent::OnComponentHide();
}

void CPanel::SetBorderColor( COLOR clr)
{
	m_clrBorder = clr;
	for(GXWINDOW::INLINECLASS::CContainer::iterator it = m_children->begin(); it != m_children->end(); ++it)
	{
		( *it)->SetBorderColor( clr);
	}
}

bool CPanel::OnLeftButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	CComponent::OnLeftButtonDown( x, y, key);
	return m_children->DispatchMouseMessage( &CComponent::OnLeftButtonDown, this, x, y, key);
}

bool CPanel::OnLeftButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	CComponent::OnLeftButtonUp( x, y, key);
	return m_children->DispatchMouseMessage( &CComponent::OnLeftButtonUp, this, x, y, key);
}
//bool CPanel::OnLeftButtonDoubleClick( int x, int y, int key)
//{
//	if ( IsViewOnly())
//		return false;
//
//	CComponent::OnLeftButtonDoubleClick( x, y, key);
//	return m_children->DispatchMouseMessage( &CComponent::OnLeftButtonDoubleClick, this, x, y, key);
//}

bool CPanel::OnRightButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	CComponent::OnRightButtonDown( x, y, key);
	return m_children->DispatchMouseMessage( &CComponent::OnRightButtonDown, this, x, y, key);
}
bool CPanel::OnRightButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	CComponent::OnRightButtonUp( x, y, key);
	return m_children->DispatchMouseMessage( &CComponent::OnRightButtonUp, this, x, y, key);
}
//bool CPanel::OnRightButtonDoubleClick( int x, int y, int key)
//{
//	if ( IsViewOnly())
//		return false;
//
//	CComponent::OnRightButtonDoubleClick( x, y, key);
//	return m_children->DispatchMouseMessage( &CComponent::OnRightButtonDoubleClick, this, x, y, key);
//}

bool CPanel::OnMouseMove( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	DoCheckMouseEnterLeaveComponent( x, y);
	m_children->DispatchMouseMessage( &CComponent::OnMouseMove, this, x, y, key);
	return true;
}

void CPanel::OnMouseEnter()
{
	if ( IsViewOnly())
		return;

	if( m_pLastMoveIn)
	{
		m_pLastMoveIn->OnMouseLeave();
		m_pLastMoveIn = NULL;
	}
	CComponent::OnMouseEnter();
}

void CPanel::OnMouseLeave()
{
	if ( IsViewOnly())
		return;

	if( m_pLastMoveIn)
		m_pLastMoveIn = NULL;
	m_children->OnMouseMoveLeave();
	CComponent::OnMouseLeave();
}

bool CPanel::OnMouseWheel( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	return m_children->DispatchMouseMessage( &CComponent::OnMouseWheel, this, x, y, key);
}

void CPanel::InsertComponent( CComponent* c)
{
	 _GXW_UPDATE_CHECK_ASSERT;

	 c->SetParent( this);
	 if( CFrame* parent = GetParentFrame())
	 {
		 if( parent->IsVisible())
			c->OnComponentShow();
	 } // if( CFrame* parent = GetParentFrame())
	 m_children->Insert( c);
}

void CPanel::RemoveComponent( CComponent* c)
{
	_GXW_UPDATE_CHECK_ASSERT;

	if( c->IsSameComponent( m_pLastMoveIn))
		m_pLastMoveIn = NULL;
	if( m_children->Remove( c))
	{
		if( CFrame* parent = GetParentFrame())
		{
			if( parent->IsVisible())
				c->OnComponentHide();
		} // if( CFrame* parent = GetParentFrame())
		c->SetParent( NULL);
	} // if( m_children->Remove( c))
}

void CPanel::RemoveAllComponent()
{
	bool bNeedHideNotify = false;
	if( CFrame* parent = GetParentFrame())
		bNeedHideNotify = parent->IsVisible();

	for( INLINECLASS::CContainer::iterator it = m_children->begin(); it != m_children->end(); ++ it)
	{
		CComponent& pc = **it;
		if( bNeedHideNotify)
			pc.OnComponentHide();
		pc.SetParent( NULL);
	}
	m_children->clear();
}

void CPanel::SetComponentForeground( CComponent* c)
{
	m_children->SetForeground( c);
}

void CPanel::SetComponentBackground( CComponent* c)
{
	m_children->SetBackground( c);
}

bool CPanel::IsContainer( const CComponent& c) const
{
	for( INLINECLASS::CContainer::const_iterator it = m_children->begin(); it != m_children->end(); ++ it)
	{
		const CComponent& pc = **it;
		if( c.IsSameComponent( pc))
			return true;
	}
	return false;
}

bool CPanel::IsContainer( const CComponent* p) const
{
	if( p)
		return IsContainer( *p);
	else
		return false;
}

CComponent* CPanel::ComponentFromPoint( int x, int y)
{
	int nx = x - GetAbsPosX();
	int ny = y - GetAbsPosY();
	CComponent* pn = m_children->ComponentFromPoint( nx, ny);
	if( NULL == pn)
		return this;
	else
		return pn;
}

const CComponent* CPanel::ComponentFromPoint( int x, int y) const
{
	return ((CPanel*)this)->ComponentFromPoint( x, y);
}

void CPanel::DoCheckMouseEnterLeaveComponent( int x, int y)
{
	CComponent* pn = ComponentFromPoint( x, y);
	if( IsSameComponent( pn))
		pn = NULL;
	if( NULL == m_pLastMoveIn && NULL == pn)
		return;
	if( m_pLastMoveIn && m_pLastMoveIn->IsSameComponent( pn))
		return;

	m_children->OnMouseMoveLeave();
	m_pLastMoveIn = pn;
	if( m_pLastMoveIn)
		m_pLastMoveIn->OnMouseEnter();
}

int CPanel::GetChildCount() const
{
	return (int)m_children->size();
}

CComponent* CPanel::GetChild( int n)
{
	return m_children->at( n);
}

bool CPanel::IsPointInMsgTransparent( int x, int y) const
{
	assert( IsPointIn( x, y) && "该方法要求输入的点，必须在控件内") ;

	INLINECLASS::CComponentList coms;
	x -= GetPosX();
	y -= GetPosY();

	m_children->ComponentFromPoint( x, y, coms);

	for( INLINECLASS::CComponentList::iterator it = coms.begin(); it != coms.end(); ++ it)
	{
		CComponent* pc = *it;
		if( !pc->IsPointInMsgTransparent( x, y))
			return false;
	}
	return IsTransparent();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CBKPanel::CBKPanel()
	: m_BackGroundStyle( BGS_NONE), m_BackGroundColor( 0)
{
}

CBKPanel::~CBKPanel()
{
}

void CBKPanel::SetStyle( int x, int y, int w, int h, BackGroundStyle bgs)
{
	CPanel::SetStyle( x, y, w, h);
	SetBackGroundStyle( bgs);
}

void CBKPanel::SetBackGroundStyle( BackGroundStyle bgs)
{
	m_BackGroundStyle = bgs;
}

void CBKPanel::SetBackGroundStyleImage( const ImageParams& ip)
{
	SetBackGroundStyle( BGS_IMAGE);
	m_BackGroundTileImages.SetGridSize( 0, 0, IFP_TOPLEFT);
	m_BackGroundTileImages.SetGridSize( 0, 0, IFP_BOTTOMRIGHT);
	m_BackGroundTileImages.SetGridSize( GetWidth(), GetHeight(), IFP_CENTER);
	m_BackGroundTileImages.SetTileSize( GetWidth(), GetHeight());
	m_BackGroundTileImages.SetTileImage( ip, IFP_CENTER);
}

void CBKPanel::SetBackGroundStyleColor( COLOR clr)
{
	SetBackGroundStyle( BGS_COLOR);
	m_BackGroundColor = clr;
}

COLOR CBKPanel::GetBackGroundStyleColor() const
{
	return m_BackGroundColor;
}

BackGroundStyle CBKPanel::GetBackGroundStyle() const
{
	return m_BackGroundStyle;
}

void CBKPanel::RemoveBackGroundStyleImage()
{
	SetBackGroundStyle( BGS_NONE);
}

void CBKPanel::DrawBackGround( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{

}

void CBKPanel::DoDrawBackGround( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
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
	case BGS_TILES:
		m_BackGroundTileImages.Draw( draw, l, t, cx, cy, cw, ch);
		break;
	default:
		break;
	}
}

void CBKPanel::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	DoDrawBackGround( draw, l, t, cx, cy, cw, ch);
	CPanel::Draw( draw, l, t, cx, cy, cw, ch);
}

void CBKPanel::OnComponentShow()
{
	m_BackGroundTileImages.RegisterImages();
	CPanel::OnComponentShow();
}

void CBKPanel::OnComponentHide()
{
	m_BackGroundTileImages.FreeImages();
	CPanel::OnComponentHide();
}

CImagesFill& CBKPanel::GetTilesBackGround()
{
	return m_BackGroundTileImages;
}

const CImagesFill& CBKPanel::GetTilesBackGround() const
{
	return m_BackGroundTileImages;
}

void CBKPanel::SetSize( int w, int h)
{
	CPanel::SetSize( w, h);
	if ( BGS_IMAGE == m_BackGroundStyle)
	{
		m_BackGroundTileImages.SetGridSize( w - m_BackGroundTileImages.GetGridWidth( IFP_TOPLEFT) - m_BackGroundTileImages.GetGridWidth( IFP_BOTTOMRIGHT)
			, h - m_BackGroundTileImages.GetGridHeight( IFP_TOPLEFT) - m_BackGroundTileImages.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
	}
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CFrameClosedNotify::CFrameClosedNotify() { }
CFrameClosedNotify::~CFrameClosedNotify() { }

CFramePosChangedNotify::CFramePosChangedNotify() {}

CFramePosChangedNotify::~CFramePosChangedNotify() {}

CFrameShowHideNotify::CFrameShowHideNotify() {}
CFrameShowHideNotify::~CFrameShowHideNotify() {}


CFrameShowQuery::CFrameShowQuery() {}
CFrameShowQuery::~CFrameShowQuery() {}

CFrameDestructureNotify::CFrameDestructureNotify() {}
CFrameDestructureNotify::~CFrameDestructureNotify() {}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CFrameShowQuery*						CFrame::s_fsq = NULL;

void CFrame::SetMinSize( bool b)
{
	m_bMinSized = b;
}

bool CFrame::IsMinSize() const
{
	return m_bMinSized;
}

void CFrame::SetHighPriorityOperator( bool b)
{
	m_bHighPriOpt = b;
}

bool CFrame::IsHighPriorityOperator() const
{
	return m_bHighPriOpt;
}

bool CFrame::FrameShowQueryAdd( CFrameShowQuery& q)
{
	if( s_fsq)
	{
		assert( !"同时只能设置一个查询器");
		return false;
	}
	s_fsq = &q;
	return true;
}

bool CFrame::FrameShowQueryRemove( CFrameShowQuery& q)
{
	if( s_fsq != &q)
	{
		assert( !"当前的查询器与需要移除的不匹配");
		return false;
	}
	s_fsq = NULL;
	return true;
}

void CFrame::FrameShowQueryClear()
{
	s_fsq;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CFrame::CFrame()
	: m_nViewLevel( 0), m_bVisible( false), m_nTitleHeight( 0), m_nTitleWidth( 0), m_fms( FMS_NONE), 
	m_bMoving( false), m_fcns( new INLINECLASS::CFrameClosedNotifySet()), m_bEscClose( false), m_bRBClose( false),
	m_bViewAsPopup( false), m_bEnable( true), m_pModalParent( NULL), m_pModalChild( NULL),
	m_fpcns( new INLINECLASS::CFramePosChangedNotifySet()), m_fpshns( new INLINECLASS::CFrameShowHideNotifySet()),
	m_bMinSized( false), m_bHighPriOpt( false), m_bUnCheckMustView( false), m_fdestns( new INLINECLASS::CFrameDestructureNotifySet())
{
}

CFrame::~CFrame()
{
	m_fdestns->NotifyAll( *this, &CFrameDestructureNotify::OnFrameDestructure);

	if ( m_pModalChild)
	{
		ModalChildRemove( m_pModalChild);
		m_pModalChild = NULL;
	}

	delete m_fcns;
	delete m_fpcns;
	delete m_fpshns;
	delete m_fdestns;
}

void CFrame::SetStyle( int x, int y, int w, int h, int nTitleHeight, BackGroundStyle bgs, FrameMoveStyle fms)
{
	SetTitleHeight( nTitleHeight);
	SetMoveStyle( fms);
}

void CFrame::SetAutoClose( bool b)
{
	m_bEscClose = b;
	m_bRBClose = b;
}

void CFrame::SetEscClose( bool b)
{
	m_bEscClose = b;
}

void CFrame::SetRightButtonClose( bool b)
{
	m_bRBClose = b;
}

bool CFrame::IsEscClose() const
{
	return m_bEscClose;
}

bool CFrame::IsRightButtonClose() const
{
	return m_bRBClose;
}

bool CFrame::IsCanEscClose() const
{
	return m_bEnable && m_bEscClose;
}

bool CFrame::IsCanRightButtonClose() const
{
	return m_bEnable && m_bRBClose;
}

void CFrame::SetViewAsPopup( bool b)
{
	if( m_bVisible)
	{
		assert( "窗口正在显示中，不能更改ViewAsPopup属性");
		return;
	}
	m_bViewAsPopup = b;
}

bool CFrame::IsViewAsPopup() const
{
	return m_bViewAsPopup;
}

void CFrame::SetViewLevel( int level)
{
	if( m_bVisible)
	{
		assert( "窗口正在显示中，不能更改ViewLevel属性");
		return;
	}
	m_nViewLevel = level;
}

int CFrame::GetViewLevel() const
{
	return m_nViewLevel;
}

bool CFrame::IsVisible() const
{
	return m_bVisible;
}

void CFrame::SetMoveStyle( FrameMoveStyle fms)
{
	m_fms = fms;
}

FrameMoveStyle CFrame::GetMoveStyle() const
{
	return m_fms;
}

void CFrame::SetTitleHeight( int nHeight)
{
	m_nTitleHeight = nHeight;
}

int CFrame::GetTitleHeight() const
{
	return m_nTitleHeight;
}

void CFrame::SetTitleWidth( int nWidth)
{
	m_nTitleWidth = nWidth;
}

int CFrame::GetTitleWidth() const
{
	return m_nTitleWidth;
}

bool CFrame::IsPointInTitle( int x, int y)
{
	if ( m_nTitleWidth > 0)
	{
		if ( x > m_nTitleWidth)
			return false;
	}
	return y <= m_nTitleHeight;
}

bool CFrame::IsPointInBody( int x, int y)
{
	return y > m_nTitleHeight;
}

void CFrame::SetEnable( bool bEnable /*= true*/)
{
	m_bEnable = bEnable;
}

bool CFrame::IsEnable() const
{
	return m_bEnable;
}

void CFrame::ModalChildAdd( CFrame* child)
{
	if ( !IsViewOnly() && !m_pModalChild && child)
	{
		m_pModalChild = child;
		m_pModalChild->m_pModalParent = this;
		ReleaseCapture();
		SetEnable( false);
	}
}

void CFrame::ModalChildRemove( CFrame* child)
{
	if ( !IsViewOnly() && m_pModalChild && m_pModalChild == child)
	{
		SetEnable();
		m_pModalChild->m_pModalParent = NULL;
		m_pModalChild = NULL;
	}
}

void CFrame::SetUnCheckPosBeforeShow( bool b)
{
	m_bUnCheckMustView = b;
}

bool CFrame::IsUnCheckPosBeforeShow() const
{
	return m_bUnCheckMustView;
}

void CFrame::Show()
{
	if( s_fsq && !s_fsq->QueryIsFrameCanShow( *this))
		return;
	
	_GXW_UPDATE_CHECK_ASSERT;

	CDesktop& desktop = CDesktop::GetDefault();

	if( m_bVisible)
		return;
	m_bVisible = true;

	assert( NULL == m_pModalChild && "目前窗口不应该有模态子窗口存在");

	if ( !m_bUnCheckMustView) // 检查窗口是否被移出桌面外并且移出部分较大, 如有需要可加开关
	{
		static const int MIN_LEAVING = 120;

		int x = GetPosX();
		int y = GetPosY();

		if ( GetPosR() < MIN_LEAVING && x < 0)
			x += MIN_LEAVING - GetPosR();
		else if ( desktop.GetWidth() - x < MIN_LEAVING && GetPosR() >= desktop.GetWidth())
			x -= MIN_LEAVING - ( desktop.GetWidth() - x);

		if ( GetPosB() < MIN_LEAVING && y < 0)
			y += MIN_LEAVING - GetPosB();
		else if ( desktop.GetHeight() - y < MIN_LEAVING && GetPosB() >= desktop.GetHeight())
			y -= MIN_LEAVING - ( desktop.GetHeight() - y);

		SetPos( x, y);
	}
	
	desktop.InsertComponent( this);

	if( m_bViewAsPopup)
		desktop.SetPopupFrame( this);
	if( m_bHighPriOpt)
		desktop.SetHighPriOptFrame( this);

	SetBorderColor( desktop.GetBorderColor());

	OnComponentShow();

	{
		bool bShow = true;
		m_fpshns->NotifyAll(*this, &CFrameShowHideNotify::OnFrameShowHide, bShow);
		desktop.NotifyFrameShowClose( *this, bShow);
	}
}

void CFrame::ShowModal( CFrame& parent)
{
	assert( &parent != this && "不能将窗口设为自身的模态对话框");
	if ( IsViewOnly())
		return;
	assert( !parent.IsSameComponent( this)); // add by xuhui

	m_pModalParent = &parent;
	m_pModalParent->ModalChildAdd( this);
	Show();
}

void CFrame::Hide()
{
	_GXW_UPDATE_CHECK_ASSERT;

	if ( m_pModalChild)
	{
		assert( !"目前窗口有模态子窗口存在，会自动关闭子窗口，有可能关闭失败。该断言只是提示使用，不一定是发生了错误！");
		m_pModalChild->Hide();
	}

	if( m_bVisible)
	{
		m_bVisible = false;
		CDesktop& desktop = CDesktop::GetDefault();

		if( m_bViewAsPopup)
			desktop.SetPopupFrame( NULL);
		desktop.RemoveComponent( this);

		SetBorderColor( 0);

		OnComponentHide();
		if( m_bHighPriOpt)
			desktop.ClearHighPriOptFrame( this);
		m_fcns->NotifyAll( *this, &CFrameClosedNotify::OnFrameClosed);

		{
			bool bShow = false;
			m_fpshns->NotifyAll(*this, &CFrameShowHideNotify::OnFrameShowHide, bShow);
			CDesktop::GetDefault().NotifyFrameShowClose( *this, false);
		}

	}

	//{
	//	bool bShow = false;
	//	m_fpshns->NotifyAll(*this, &CFrameShowHideNotify::OnFrameShowHide, bShow);
	//	CDesktop::GetDefault().NotifyFrameShowClose( *this, false);
	//}

	if ( m_pModalParent)
	{
		m_pModalParent->ModalChildRemove( this);
		m_pModalParent = NULL;
	}
}


bool CFrame::OnLeftButtonDown( int x, int y, int key)
{
	if( m_bMinSized)
		return false;

	if ( IsViewOnly())
		return false;

	if ( !m_bEnable)
		return true;

	CDesktop& desktop = CDesktop::GetDefault();
	desktop.SetComponentForeground( this);

	if( CPanel::OnLeftButtonDown( x, y, key))
		return true;

	if( FMS_NONE == m_fms)
		return !IsTransparent();
	bool bNeedMove = false;
	switch(  m_fms)
	{
	case FMS_TITLE:
		{
			int nx = x - GetPosX();
			int ny = y - GetPosY();

			bNeedMove = IsPointInTitle( nx, ny);
			if ( !bNeedMove)
				return !IsTransparent();
		}
		break;
	case FMS_BODY:
		{
			int nx = x - GetPosX();
			int ny = y - GetPosY();

			bNeedMove = IsPointInBody( nx, ny);
		}
		break;
	case FMS_ALL:
		bNeedMove = true;
		break;
	default:
		break;
	}
	
	if( bNeedMove)
	{
		m_bMoving = true;
		m_nLastX = x;
		m_nLastY = y;

		SetCapture();
	}

	return true;
}

bool CFrame::OnLeftButtonUp( int x, int y, int key)
{
	if( m_bMinSized)
		return false;

	if ( IsViewOnly())
		return false;

	if ( !m_bEnable)
		return true;

	if( m_bMoving)
	{
		SetPos( GetPosX() + x - m_nLastX, GetPosY() + y - m_nLastY);

		m_bMoving = false;
		ReleaseCapture();
	}

	if ( CPanel::OnLeftButtonUp( x, y, key))
		return true;

	if ( FMS_NONE == m_fms)
		return !IsTransparent();

	return true;
}

bool CFrame::OnMouseMove( int x, int y, int key)
{
	if( m_bMinSized)
		return false;

	if ( IsViewOnly())
		return false;

	if ( !m_bEnable)
		return true;

	if ( m_bMoving && ( key & MK_LBUTTON))
	{
		CDesktop& desktop = CDesktop::GetDefault();

		int stepX = x - m_nLastX;
		int stepY = y - m_nLastY;

		CFrame* pp = this;
		// 移动模态对话框时如需移动父窗口，需打开以下代码，或有需要可以做一个开关
		//while ( pp->m_pModalParent && FMS_NONE != pp->m_pModalParent->m_fms) pp = pp->m_pModalParent;

		//if ( desktop.IsFramePositionRestrict())
		//{
		//	CFrame* p = pp;
		//	while ( p)
		//	{
		//		if ( p->GetPosX() + stepX < 0)
		//			stepX = max( stepX, 0 - p->GetPosX());
		//		else if ( p->GetPosR() + stepX > desktop.GetWidth())
		//			stepX = min( stepX, desktop.GetWidth() - p->GetPosR());

		//		if ( p->GetPosY() + stepY < 0)
		//			stepY = max( stepY, 0 - p->GetPosY());
		//		else if ( p->GetPosB() + stepY > desktop.GetHeight())
		//			stepY = min( stepY, desktop.GetHeight() - p->GetPosB());

		//		p = p->m_pModalChild;
		//	}
		//}
		
		while ( pp)
		{
			pp->SetPos( pp->GetPosX() + stepX, pp->GetPosY() + stepY);
			pp = pp->m_pModalChild;
		}

		m_nLastX = x;
		m_nLastY = y;
		return true;
	}

	CPanel::OnMouseMove( x, y, key);
	return true;
}

bool CFrame::OnRightButtonDown( int x, int y, int key)
{
	if( m_bMinSized)
		return false;

	if ( IsViewOnly())
		return false;

	if ( !m_bEnable)
		return true;

	CDesktop& desktop = CDesktop::GetDefault();
	desktop.SetComponentForeground( this);
	if ( CPanel::OnRightButtonDown( x, y, key))
		return true;

	return !IsTransparent();
}

bool CFrame::OnRightButtonUp( int x, int y, int key)
{
	if( m_bMinSized)
		return false;

	if ( IsViewOnly())
		return false;

	if ( !m_bEnable)
		return true;

	if( IsCanRightButtonClose())
	{
		if( !CPanel::OnRightButtonUp( x, y, key))
		{
			Hide();
			return true;
		}
	}
	else
	{
		if ( CPanel::OnRightButtonUp( x, y, key))
			return true;
	}

	return !IsTransparent();
}

void CFrame::FrameClosedNotifyAdd( CFrameClosedNotify& fcn)
{
	m_fcns->Insert( &fcn);
}

void CFrame::FrameClosedNotifyRemove( CFrameClosedNotify& fcn)
{
	m_fcns->Remove( &fcn);
}

void CFrame::CenterToDesktop()
{
	CDesktop& desktop = CDesktop::GetDefault();
	SetPos( ( desktop.GetWidth() - GetWidth() ) / 2, ( desktop.GetHeight() - GetHeight() ) / 2);
}

void CFrame::FramePosChangedNotifyAdd( CFramePosChangedNotify& fpcn)
{
	m_fpcns->Insert( &fpcn);
}

void CFrame::FramePosChangedNotifyRemove( CFramePosChangedNotify& fpcn)
{
	m_fpcns->Remove( &fpcn);
}

void CFrame::FrameShowHideNotifyAdd( CFrameShowHideNotify& fshn)
{
	m_fpshns->Insert(&fshn);
}
void CFrame::FrameShowHideNotifyRemove( CFrameShowHideNotify& fshn)
{
	m_fpshns->Remove(&fshn);
}

void CFrame::FrameDestructureNotifyAdd( CFrameDestructureNotify& notify)
{
	m_fdestns->Insert( &notify);
}
void CFrame::FrameDestructureNotifyRemove( CFrameDestructureNotify& notify)
{
	m_fdestns->Remove( &notify);
}

void CFrame::SetPos( int x, int y)
{
	int nOldX = GetPosX();
	int nOldY = GetPosY();
	CDesktop& desktop = CDesktop::GetDefault();
	if( !IsViewOnly() && desktop.IsFramePositionRestrict() && -1 != nOldX && -1 != nOldY)
	{
		const CPanel& dp = desktop.GetDesktopPanel();
		int dw = dp.GetWidth() - GetWidth();
		int dh = dp.GetHeight() - GetHeight();
		x = min( x, dw);
		x = max( 0, x);
		y = min( y, dh);
		y = max( 0, y);
	}
	CPanel::SetPos( x, y);
	m_fpcns->NotifyAll( *this, &CFramePosChangedNotify::OnFramePosChange, x, y, nOldX, nOldY);
}

void CFrame::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	if( m_bMinSized)
		return;
	CBKPanel::Draw( draw, l, t, cx, cy, cw, ch);
	CDesktop::GetDefault().FirePostFrameRender(this);
}

bool CFrame::OnMouseWheel( int x, int y, int key)
{
	if( m_bMinSized)
		return false;
	return CBKPanel::OnMouseWheel( x, y, key);
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CMessageDispatchNotify::CMessageDispatchNotify()
{
}

CMessageDispatchNotify::~CMessageDispatchNotify()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CPostFrameRenderNotify::CPostFrameRenderNotify() 
{
}

CPostFrameRenderNotify::~CPostFrameRenderNotify()
{
}

} // namespace GXWINDOW { 
