#include "gxwindowdef.h"

namespace GXWINDOW {  

	namespace INLINECLASS {

		class CTipFrame : public CFrame
		{
		public:
			CTipFrame() { }
			virtual ~CTipFrame() {}
		public:
			virtual bool OnLeftButtonDown( int x, int y, int key) { return false; }
			virtual bool OnLeftButtonUp( int x, int y, int key) { return false; }
			virtual bool OnLeftButtonDoubleClick( int x, int y, int key) { return false; }

			virtual bool OnRightButtonDown( int x, int y, int key) { return false; }
			virtual bool OnRightButtonUp( int x, int y, int key) { return false; }
			virtual bool OnRightButtonDoubleClick( int x, int y, int key) { return false; }

			virtual bool OnMouseMove( int x, int y, int key) { return false; }
			virtual bool OnMouseWheel( int x, int y, int key) { return false; }
		} m_TipInfoDefault;

		class CDefaultTipInfoFrameManger : public CAbstractTipInfoFrameManager
		{
		public:
			CDefaultTipInfoFrameManger() {}
			virtual ~CDefaultTipInfoFrameManger() {}
		public:
			virtual CFrame* GetTipInfoFrame( const wchar_t* tiptext) { return NULL;	}
		} g_DefaultTipInfoFrameManager;

		CDefaultTipShowLayout	m_DefaultTipShowPos;
		CDesktop*	g_pDefaultDesktop = NULL;

		static class CDefaultDesktopDeleteScoped
		{
		public:
			CDefaultDesktopDeleteScoped() {}
			~CDefaultDesktopDeleteScoped() 
			{
				if( g_pDefaultDesktop)
				{
					delete g_pDefaultDesktop;
					g_pDefaultDesktop = NULL;
				}
			}

		} g_DefaultDesktopDeleteScopedDummy;

		static class CMousePosChange
		{
			bool		m_bEnable; // 做一个开关, 默认启用转换, 当引擎已经统一处理过坐标转换了, 则不需在gxwindow内部进行
			double		m_w;
			double		m_h;

			HWND		m_hMainWnd;
		public:
			CMousePosChange() 
				: m_bEnable( true), m_w( 0.0), m_h( 0.0), m_hMainWnd( NULL)
			{}
			~CMousePosChange() 
			{}
		public:
			void SetSize( int x, int y)
			{
				m_w = x;
				m_h = y;
			}
			inline void Translate( int& x, int& y)
			{
				if ( m_bEnable)
				{
					RECT	rt;

					GetClientRect( m_hMainWnd, &rt);

					if( rt.right)
						x = (int)( (double)x * m_w / (double)rt.right);
					if( rt.bottom)
						y = (int)( (double)y * m_h / (double)rt.bottom);
				}
			}
			void SetMainHwnd( HWND hwnd)
			{
				m_hMainWnd = hwnd;
			}
			inline HWND GetMainHwnd() const
			{
				return m_hMainWnd;
			}
			void EnableMousePosChange( bool bEnable)
			{
				m_bEnable = bEnable;
			}
		}	g_MousePosChange;

	} // namespace INLINECLASS {

	CDesktop::CDesktop()
		: CPanel( NULL), m_pMouseCapture( NULL), m_PopupFrame( NULL), m_pTipInfo( &INLINECLASS::m_TipInfoDefault), m_pTipShowPos( &INLINECLASS::m_DefaultTipShowPos), m_GlobalGXWRenderer( NULL)
		, m_pMessageDispatchBefore( new INLINECLASS::CMessageDispatchNotifySet), m_pMessageDispatchAfter( new INLINECLASS::CMessageDispatchNotifySet)
		, m_bRestrictFrame( false), m_HighPriOptFrame( NULL), m_pTipFrameMgr( NULL) , m_pPostFrameRender( new INLINECLASS::CPostFrameRenderNotifySet)
	{
		SetPos( 0, 0);
		InitializeWindowsUpdating();
	}

	CDesktop::~CDesktop()
	{
		delete m_pMessageDispatchBefore;
		delete m_pMessageDispatchAfter;
		delete m_pPostFrameRender;
	}

	void CDesktop::Draw( CDraw& draw)
	{
#ifdef GXWINDOW_ENABLE_BATCH_RENDER
		if ( GXWGetBatchRender())
		{
			if ( GXWIsNeedRedraw())
			{
				CComponent::Draw( draw);
				if ( GetColorAlpha( GetBorderColor()))
					draw.Rect( 0, 0, GetWidth(), GetHeight(), GetBorderColor());
				GXWCancelNeedRedraw();
			}
			draw.BatchDraw();
			return;
		}
#endif
		CComponent::Draw( draw);
		if ( GetColorAlpha( GetBorderColor()))
			draw.Rect( 0, 0, GetWidth(), GetHeight(), GetBorderColor());
	}

	const CPanel& CDesktop::GetDesktopPanel() const
	{
		return *this;
	}

	bool CDesktop::IsPointInMsgTransparent( int x, int y) const
	{
		INLINECLASS::CComponentList coms; 
		x -= GetPosX();
		y -= GetPosY();
		m_children->ComponentFromPoint( x, y, coms);
		if( coms.empty())
			return true;

		for( INLINECLASS::CComponentList::iterator it = coms.begin(), ite = coms.end(); it != ite; ++ it)
		{
			CComponent* com = *it;
			if( CFrame* f = dynamic_cast<CFrame*>( com))
			{
				if( f->IsMinSize())
					continue;
			}

			if( !com->IsPointInMsgTransparent( x, y))
				return false;
		}
		return true;
	}

	void CDesktop::SetDesktopLayoutManager( const CLayoutManager& lm)
	{
		SetLayoutManager( lm);
	}

	void CDesktop::SetSize( int w, int h)
	{
		INLINECLASS::g_MousePosChange.SetSize( w, h);
		CPanel::SetSize( w, h);
		DoLayout();
	}

	void CDesktop::EnableMousePosChange( bool bEnable)
	{
		INLINECLASS::g_MousePosChange.EnableMousePosChange( bEnable);
	}

	CAbstractTipInfoFrameManager* CDesktop::SetTipInfoFrameManager( CAbstractTipInfoFrameManager* tipInfoMgr)
	{
		CAbstractTipInfoFrameManager* old = &GetTipInfoFrameManager();
		m_pTipFrameMgr = tipInfoMgr;
		return old;
	}

	CAbstractTipInfoFrameManager& CDesktop::GetTipInfoFrameManager()
	{
		if ( m_pTipFrameMgr)
			return *m_pTipFrameMgr;
		else
			return INLINECLASS::g_DefaultTipInfoFrameManager;
	}

	CFrame* CDesktop::SetTipFrame( CFrame* pFrame)
	{
		CFrame* old = GetTipFrame();
		m_pTipInfo = pFrame;
		if( NULL == m_pTipInfo)
			m_pTipInfo = &INLINECLASS::m_TipInfoDefault;
		return old;
	}

	CFrame* CDesktop::GetTipFrame()
	{
		return m_pTipInfo;
	}

	CTipShowPosLayout* CDesktop::SetTipShowPosLayout( CTipShowPosLayout* pLayout)
	{
		CTipShowPosLayout* old = GetTipShowPosLayout();
		m_pTipShowPos = pLayout;
		if( NULL == m_pTipShowPos)
			m_pTipShowPos = &INLINECLASS::m_DefaultTipShowPos;
		return old;
	}

	CTipShowPosLayout* CDesktop::GetTipShowPosLayout()
	{
		return m_pTipShowPos;
	}

	void CDesktop::Update( CDraw& draw)
	{
		UPDATE_CHECK_BEGIN();
		Draw( draw);
		UPDATE_CHECK_END();
	}

	void CDesktop::Update()
	{
		UPDATE_CHECK_BEGIN();
		Draw( *GetDefaultRenderer());
		UPDATE_CHECK_END();
	}

	bool CDesktop::Message( HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
	{
		assert( NULL == GetMainHwnd() || GetMainHwnd() == hWnd);

		if ( m_pMessageDispatchBefore->NotifyHasReturn( &CMessageDispatchNotify::OnMessageDispatch, uMsg, wParam, lParam, true))
			return true;

		switch( uMsg) 
		{
		case WM_LBUTTONDOWN:
			{
				int x = LOWORD( lParam);
				int y = HIWORD( lParam);

				INLINECLASS::g_MousePosChange.Translate( x, y);
				if( OnLeftButtonDown( x, y, (int)wParam))
					return true;
			}
			break;
		case WM_LBUTTONUP:
			{
				int x = LOWORD( lParam);
				int y = HIWORD( lParam);
				INLINECLASS::g_MousePosChange.Translate( x, y);
				if( OnLeftButtonUp( x, y, (int)wParam))
					return true;
			}
			break;
		case WM_MOUSEMOVE:
			{
				int x = LOWORD( lParam);
				int y = HIWORD( lParam);
				INLINECLASS::g_MousePosChange.Translate( x, y);
				if( OnMouseMove( x, y, (int)wParam))
					return true;
			}
			break;
		case WM_RBUTTONDOWN:
			{
				int x = LOWORD( lParam);
				int y = HIWORD( lParam);
				INLINECLASS::g_MousePosChange.Translate( x, y);
				if( OnRightButtonDown( x, y, (int)wParam))
					return true;
			}
			break;
		case WM_RBUTTONUP:
			{
				int x = LOWORD( lParam);
				int y = HIWORD( lParam);
				INLINECLASS::g_MousePosChange.Translate( x, y);
				if( OnRightButtonUp( x, y, (int)wParam))
					return true;
			}
			break;
		case WM_MOUSEWHEEL:
			{
				POINT		pt;
				pt.x = LOWORD( lParam);
				pt.y = HIWORD( lParam);
				ScreenToClient( hWnd, &pt);

				int x = pt.x;
				int y = pt.y;
				INLINECLASS::g_MousePosChange.Translate( x, y);
				if( OnMouseWheel( x, y, (int)wParam))
					return true;
				break;
			}
		case WM_CHAR:
			if( CKeyInput::GetInstance().OnDispatchCharMessage( WM_CHAR, wParam, lParam, *this))
				return true;
			break;
		case WM_KEYDOWN:
			if( CKeyInput::GetInstance().OnDispatchCharMessage( WM_KEYDOWN, wParam, lParam, *this))
				return true;
			break;
		case WM_KEYUP:
			if( CKeyInput::GetInstance().OnDispatchCharMessage( WM_KEYUP, wParam, lParam, *this))
				return true;
			break;
		default:
			break;
		}
		return m_pMessageDispatchAfter->NotifyHasReturn( &CMessageDispatchNotify::OnMessageDispatch, uMsg, wParam, lParam, false);
	}

	void CDesktop::MessageDispatchHookBeforeAdd( CMessageDispatchNotify& mdn)
	{
		m_pMessageDispatchBefore->Insert( &mdn);
	}

	void CDesktop::MessageDispatchHookBeforeRemove( CMessageDispatchNotify& mdn)
	{
		m_pMessageDispatchBefore->Remove( &mdn);
	}

	void CDesktop::MessageDispatchHookAfterAdd( CMessageDispatchNotify& mdn)
	{
		m_pMessageDispatchAfter->Insert( &mdn);
	}

	void CDesktop::MessageDispatchHookAfterRemove( CMessageDispatchNotify& mdn)
	{
		m_pMessageDispatchAfter->Remove( &mdn);
	}
	
	void CDesktop::PostFrameRenderAdd( CPostFrameRenderNotify& pfrn)
	{
		m_pPostFrameRender->Insert( &pfrn);
	}

	void CDesktop::PostFrameRenderRemove( CPostFrameRenderNotify& pfrn)
	{
		m_pPostFrameRender->Remove( &pfrn);
	}

	void CDesktop::FirePostFrameRender(CFrame* frame)
	{
		m_pPostFrameRender->NotifyAll( frame, &CPostFrameRenderNotify::OnPostFrameRender);
	}

	bool CDesktop::OnLeftButtonDown( int x, int y, int key)
	{
		m_nMousePosX = x;
		m_nMousePosY = y;

		if( m_pMouseCapture)
			return m_pMouseCapture->OnLeftButtonDown( x, y, key);
		else if( CheckPopupFrame( &CComponent::OnLeftButtonDown, x, y, key, true))
			return true;
		else if( m_HighPriOptFrame)
		{
			bool bVisible = m_HighPriOptFrame->IsVisible() && !m_HighPriOptFrame->IsMinSize();
			assert( bVisible);
			if( bVisible)		
			{
				const CComponent* ptop = ComponentFromPoint( x, y);
				if( ptop->IsSameComponent( this))
					return false;
				if( !ptop->IsSameComponent( m_HighPriOptFrame))
					return true; // 如果当前在窗口上, 吃掉这个消息
			} // if( bVisible)
		} // else if( m_HighPriOptFrame)
		return CPanel::OnLeftButtonDown( x, y, key);
	}

	bool CDesktop::OnLeftButtonUp( int x, int y, int key)
	{
		m_nMousePosX = x;
		m_nMousePosY = y;

		if( m_pMouseCapture)
			return m_pMouseCapture->OnLeftButtonUp( x, y, key);
		else if( CheckPopupFrame( &CComponent::OnLeftButtonUp, x, y, key, false))
			return true;
		else if( m_HighPriOptFrame)
		{
			bool bVisible = m_HighPriOptFrame->IsVisible() && !m_HighPriOptFrame->IsMinSize();
			assert( bVisible);
			if( bVisible)		
			{
				const CComponent* ptop = ComponentFromPoint( x, y);
				if( ptop->IsSameComponent( this))
					return false;
				if( !ptop->IsSameComponent( m_HighPriOptFrame))
				{
					SetComponentForeground( m_HighPriOptFrame);
					return true;
				}
			} // if( bVisible)		
		} // else if( m_HighPriOptFrame)
		return CPanel::OnLeftButtonUp( x, y, key);
	}

	//bool CDesktop::OnLeftButtonDoubleClick( int x, int y, int key)
	//{
	//	m_nMousePosX = x;
	//	m_nMousePosY = y;
	//
	//	if( m_pMouseCapture)
	//		return m_pMouseCapture->OnLeftButtonDoubleClick( x, y, key);
	//	else if( CheckPopupFrame( &CComponent::OnLeftButtonDoubleClick, x, y, key, true))
	//		return true;
	//	else
	//		return CPanel::OnLeftButtonDoubleClick( x, y, key);
	//}

	bool CDesktop::OnRightButtonDown( int x, int y, int key)
	{
		m_nMousePosX = x;
		m_nMousePosY = y;

		if( m_pMouseCapture)
			return m_pMouseCapture->OnRightButtonDown( x, y, key);
		else if( CheckPopupFrame( &CComponent::OnRightButtonDown, x, y, key, true))
			return true;
		else if( m_HighPriOptFrame)
		{
			bool bVisible = m_HighPriOptFrame->IsVisible() && !m_HighPriOptFrame->IsMinSize();
			assert( bVisible);
			if( bVisible)		
			{
				const CComponent* ptop = ComponentFromPoint( x, y);
				if( ptop->IsSameComponent( this))
					return false;
				if( !ptop->IsSameComponent( m_HighPriOptFrame))
					return true; // 如果当前在窗口上, 吃掉这个消息
			} // if( bVisible)
		} // else if( m_HighPriOptFrame)
		return CPanel::OnRightButtonDown( x, y, key);
	}

	bool CDesktop::OnRightButtonUp( int x, int y, int key)
	{
		m_nMousePosX = x;
		m_nMousePosY = y;

		if( m_pMouseCapture)
			return m_pMouseCapture->OnRightButtonUp( x, y, key);
		else if( CheckPopupFrame( &CComponent::OnRightButtonUp, x, y, key, false))
			return true;
		else if( m_HighPriOptFrame)
		{
			bool bVisible = m_HighPriOptFrame->IsVisible() && !m_HighPriOptFrame->IsMinSize();
			assert( bVisible);
			if( bVisible)		
			{
				const CComponent* ptop = ComponentFromPoint( x, y);
				if( ptop->IsSameComponent( this))
					return false;
				if( !ptop->IsSameComponent( m_HighPriOptFrame))
					return true; // 如果当前在窗口上, 吃掉这个消息
			} // if( bVisible)
		} // else if( m_HighPriOptFrame)
		return CPanel::OnRightButtonUp( x, y, key);
	}

	//bool CDesktop::OnRightButtonDoubleClick( int x, int y, int key)
	//{
	//	m_nMousePosX = x;
	//	m_nMousePosY = y;
	//
	//	if( m_pMouseCapture)
	//		return m_pMouseCapture->OnRightButtonDoubleClick( x, y, key);
	//	else if( CheckPopupFrame( &CComponent::OnRightButtonDoubleClick, x, y, key, false))
	//		return true;
	//	else
	//		return CPanel::OnRightButtonDoubleClick( x, y, key);
	//}

	bool CDesktop::OnMouseMove( int x, int y, int key)
	{
		m_nMousePosX = x;
		m_nMousePosY = y;

		if( m_pMouseCapture)
			return m_pMouseCapture->OnMouseMove( x, y, key);

		DoCheckMouseEnterLeaveComponent( x, y);

		return m_children->DispatchMouseMessage( &CComponent::OnMouseMove, this, x, y, key);
	}

	bool CDesktop::OnMouseWheel( int x, int y, int key)
	{
		m_nMousePosX = x;
		m_nMousePosY = y;

		if( m_pMouseCapture)
			return m_pMouseCapture->OnMouseWheel( x, y, key);
		else
			return CPanel::OnMouseWheel( x, y, key);
	}

	CComponent* CDesktop::ComponentFromPoint( int x, int y)
	{
		int nx = x - GetPosX();
		int ny = y - GetPosY();
		CComponent* pn = m_children->ComponentFromPoint( nx, ny);
		if( NULL == pn)
			return this;
		else
			return pn;
	}

	void CDesktop::SetHighPriOptFrame( CFrame* p)
	{
#ifdef _DEBUG
		assert( p);
		if( m_HighPriOptFrame && p != m_HighPriOptFrame)
			assert( !"不应该同时出现2个都需要高优先输入的窗口");
#endif
		m_HighPriOptFrame = p;
	}

	void CDesktop::ClearHighPriOptFrame( CFrame* p)
	{
#ifdef _DEBUG
		assert( p);
		if( m_HighPriOptFrame && p != m_HighPriOptFrame)
			assert( !"不应该出现 清除一个不是自己的高优先输入的窗口");
#endif
		m_HighPriOptFrame = NULL;
	}

	CFrame* CDesktop::GetHighPriOptFrame() const
	{
		return m_HighPriOptFrame;
	}

	void CDesktop::SetPopupFrame( CFrame* p)
	{
		if( m_PopupFrame && m_PopupFrame->IsVisible())
			m_PopupFrame->Hide();
		m_PopupFrame = p;
		if( m_PopupFrame)
			m_PopupFrame->Show();
	}

	CFrame* CDesktop::GetPopupFrame() const
	{
		return m_PopupFrame;
	}

	int CDesktop::GetMousePointX()
	{
		return m_nMousePosX;
	}

	int CDesktop::GetMousePointY()
	{
		return m_nMousePosY;
	}

	int CDesktop::GetFrameCount()
	{
		return GetChildCount();
	}

	CFrame* CDesktop::GetFrame( int n)
	{
		return dynamic_cast<CFrame*>( GetChild( n));
	}

	CDraw* CDesktop::SetDefaultRenderer( CDraw* renderer)
	{
		CDraw* old = m_GlobalGXWRenderer; 
		m_GlobalGXWRenderer = renderer;
		return old;
	}

	CDraw* CDesktop::GetDefaultRenderer()
	{
		return m_GlobalGXWRenderer;
	}

	HWND CDesktop::SetMainHwnd( HWND hWnd) 
	{
		HWND hOld = INLINECLASS::g_MousePosChange.GetMainHwnd();
		INLINECLASS::g_MousePosChange.SetMainHwnd( hWnd);
		return hOld;
	}

	HWND CDesktop::GetMainHwnd()
	{
		return INLINECLASS::g_MousePosChange.GetMainHwnd();
	}

	void CDesktop::ShowTipInfo( CComponent* p, CComponent& base)
	{
		assert( m_pTipInfo);
		CFrame& TipInfo = *m_pTipInfo;
		if( TipInfo.IsVisible())
		{
			TipInfo.Hide();
			TipInfo.RemoveAllComponent();
		}
		int fw = p->GetWidth();
		int fh = p->GetHeight();
		TipInfo.InsertComponent( p);
		TipInfo.SetSize( fw, fh);
		p->SetPos( 0, 0);

		int x = 0;
		int y = 0;
		assert( m_pTipShowPos);
		m_pTipShowPos->LayOutTipPos( base, TipInfo, GetWidth(), GetHeight(), m_nMousePosX, m_nMousePosY, x, y);
		TipInfo.SetPos( x, y);
		TipInfo.SetViewLevel( 0x7FFFFFFF);
		TipInfo.Show();
		SetComponentForeground( &TipInfo);
	}
	void CDesktop::HideTipInfo( CComponent* p)
	{
		assert( m_pTipInfo);
		CFrame& TipInfo = *m_pTipInfo;
		if(TipInfo.IsVisible() && TipInfo.IsContainer( p))
		{
			TipInfo.Hide();
			TipInfo.RemoveAllComponent();
		}
	}

	void CDesktop::SetCapture( CComponent* c)
	{
		assert( c);
		if( NULL == m_pMouseCapture)
			m_pMouseCapture = c;
	}
	void CDesktop::ReleaseCapture( CComponent* c)
	{
		if( c == m_pMouseCapture)
			m_pMouseCapture = NULL;
	}

	void CDesktop::SetFramePositionRestrict( bool bRestrict)
	{
		m_bRestrictFrame = bRestrict;
	}

	bool CDesktop::IsFramePositionRestrict() const
	{
		return m_bRestrictFrame;
	}

	void CDesktop::DoCheckMouseEnterLeaveComponent( int x, int y)
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
			INLINECLASS::CContainer::OnComponentMouseMoveEnter( *m_pLastMoveIn);
	}

	void CDesktop::CloseAllFrame()
	{
		INLINECLASS::CContainer	container = *m_children;
		for( INLINECLASS::CContainer::iterator it = container.begin(), ite = container.end(); it != ite; ++ it)
		{
			CFrame* f = dynamic_cast<CFrame*>( *it);
			assert( f);
			if( f)
				f->Hide();
		}
		assert( m_children->empty());
	}

	void CDesktop::FrameClosedNotifyAdd( CFrameShowHideNotify& fcn)
	{
		m_NotifySet.Insert( &fcn);
	}

	void CDesktop::FrameClosedNotifyRemove( CFrameShowHideNotify& fcn)
	{
		m_NotifySet.Remove( &fcn);
	}

	void CDesktop::NotifyFrameShowClose( CFrame& f, bool bShow)
	{
		m_NotifySet.NotifyAll( f, &CFrameShowHideNotify::OnFrameShowHide, bShow);
	}

	CDesktop& CDesktop::GetDefault()
	{
		if ( NULL == INLINECLASS::g_pDefaultDesktop)
			INLINECLASS::g_pDefaultDesktop = new CDesktop();
		return *INLINECLASS::g_pDefaultDesktop;
	}

	CDesktop* CDesktop::GetDefaultPointer()
	{
		return INLINECLASS::g_pDefaultDesktop;
	}

	void CDesktop::CloseDefault()
	{
		// 生命周期有点乱, 打补丁
		// 析构时, CComponent::ReleaseCapture()中会调用CDesktop::GetDefaultPointer(), 只能先清了g_pDefaultDesktop
		CDesktop* temp = INLINECLASS::g_pDefaultDesktop;
		INLINECLASS::g_pDefaultDesktop = NULL;
		delete temp;
	}

} // namespace GXWINDOW { 