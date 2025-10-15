#pragma once

namespace GXWINDOW {

namespace INLINECLASS {

class CMessageDispatchNotifySet : public CNotifySet<CMessageDispatchNotify*>
{
public:
	CMessageDispatchNotifySet() {}
	virtual ~CMessageDispatchNotifySet() {}
};

class CPostFrameRenderNotifySet : public CNotifySet<CPostFrameRenderNotify*>
{
public:
	CPostFrameRenderNotifySet() {}
	virtual ~CPostFrameRenderNotifySet() {}
};


} // namespace INLINECLASS

class CDesktop : public CPanel
{
	friend class CPanel;
	friend class CFrame;
	friend class CComboBox;
	friend class CKeyInput;
	friend CFrame* GXWGetTopAutoCloseFrame();

	CFrame*								m_PopupFrame;
	CFrame*								m_HighPriOptFrame;

	CComponent*							m_pMouseCapture;

	int									m_nMousePosX;
	int									m_nMousePosY;

	CDraw*								m_GlobalGXWRenderer;

	INLINECLASS::CMessageDispatchNotifySet*			m_pMessageDispatchBefore;
	INLINECLASS::CMessageDispatchNotifySet*			m_pMessageDispatchAfter;
	INLINECLASS::CPostFrameRenderNotifySet*			m_pPostFrameRender;

	CFrame*								m_pTipInfo;
	CTipShowPosLayout*					m_pTipShowPos;
	CAbstractTipInfoFrameManager*		m_pTipFrameMgr;

	bool					m_bRestrictFrame;

	class CFrameShowHideNotifySet : public INLINECLASS::CNotifySet<CFrameShowHideNotify*>
	{
	public:
		CFrameShowHideNotifySet() {}
		virtual ~CFrameShowHideNotifySet() {}
	}	m_NotifySet;

public:
	CDesktop();
	virtual ~CDesktop();
private:
	template<typename F>
	bool CheckPopupFrame( F f, int x, int y, int key, bool bClose)
	{
		if( NULL == m_PopupFrame)
			return false;
		assert( m_PopupFrame->IsVisible());
		if( m_PopupFrame->IsAbsPointIn( x, y))
			return (m_PopupFrame->*f)( x, y, key);
		if( bClose)
		{
			m_PopupFrame->Hide();
			m_PopupFrame = NULL;
		}
		return true;
	}
protected:
	void Draw( CDraw& draw);
	bool OnLeftButtonDown( int x, int y, int key);
	bool OnLeftButtonUp( int x, int y, int key);
//	bool OnLeftButtonDoubleClick( int x, int y, int key);
	bool OnRightButtonDown( int x, int y, int key);
	bool OnRightButtonUp( int x, int y, int key);
//	bool OnRightButtonDoubleClick( int x, int y, int key);
	bool OnMouseMove( int x, int y, int key);
	bool OnMouseWheel( int x, int y, int key);
	CComponent* ComponentFromPoint( int x, int y);
protected:
	void SetPopupFrame( CFrame* p);
	CFrame* GetPopupFrame() const;

	void SetHighPriOptFrame( CFrame* p);
	void ClearHighPriOptFrame( CFrame* p);
	CFrame* GetHighPriOptFrame() const;

	virtual void DoCheckMouseEnterLeaveComponent( int x, int y);
public:
	const CPanel& GetDesktopPanel() const;

	void CloseAllFrame();

	virtual bool IsPointInMsgTransparent( int x, int y) const;

	void SetDesktopLayoutManager( const CLayoutManager& lm);
	void SetSize( int w, int h);

	CAbstractTipInfoFrameManager* SetTipInfoFrameManager( CAbstractTipInfoFrameManager* tipInfoMgr);
	CAbstractTipInfoFrameManager& GetTipInfoFrameManager();
	CFrame* SetTipFrame( CFrame* pFrame = NULL);
	CFrame* GetTipFrame();
	CTipShowPosLayout* SetTipShowPosLayout( CTipShowPosLayout* pLayout = NULL);
	CTipShowPosLayout* GetTipShowPosLayout();

	void Update( CDraw& draw);
	void Update();
	bool Message( HWND hWnd, UINT uMsg, WPARAM wParam, LPARAM lParam);

	void MessageDispatchHookBeforeAdd( CMessageDispatchNotify& mdn);
	void MessageDispatchHookBeforeRemove( CMessageDispatchNotify& mdn);
	void MessageDispatchHookAfterAdd( CMessageDispatchNotify& mdn);
	void MessageDispatchHookAfterRemove( CMessageDispatchNotify& mdn);
	void PostFrameRenderAdd( CPostFrameRenderNotify& pfrn);
	void PostFrameRenderRemove( CPostFrameRenderNotify& pfrn);
	void FirePostFrameRender(CFrame* frame);

	int GetMousePointX();
	int GetMousePointY();

	int GetFrameCount();
	CFrame* GetFrame( int n);

	CDraw* SetDefaultRenderer( CDraw* renderer);
	CDraw* GetDefaultRenderer();

	HWND SetMainHwnd( HWND hWnd);
	HWND GetMainHwnd();

	void ShowTipInfo( CComponent* p, CComponent& base);
	void HideTipInfo( CComponent* p);

	void SetCapture( CComponent* c);
	void ReleaseCapture( CComponent* c);

	void SetBorderColor(COLOR clr) { CPanel::SetBorderColor( clr); }
	void SetFramePositionRestrict( bool bRestrict);
	bool IsFramePositionRestrict() const;

	void FrameClosedNotifyAdd( CFrameShowHideNotify& fcn);
	void FrameClosedNotifyRemove( CFrameShowHideNotify& fcn);
	void NotifyFrameShowClose( CFrame& f, bool bShow);

	void EnableMousePosChange( bool bEnable);

#ifdef _GXW_UPDATE_CHECK
	private:
		bool	g_bWindowsUpdating;
		void InitializeWindowsUpdating() { g_bWindowsUpdating = false; }
	public:
		void UPDATE_CHECK_BEGIN() { g_bWindowsUpdating = true; }
		void UPDATE_CHECK_END() { g_bWindowsUpdating = false; }
		void UPDATE_CHECK_ASSERT() 
		{ 
			assert( !g_bWindowsUpdating); 
		}
#else
	private:
		bool	g_bWindowsUpdating;
		void InitializeWindowsUpdating() { g_bWindowsUpdating = false; }
	public:
		void UPDATE_CHECK_BEGIN() {}
		void UPDATE_CHECK_END() {}
		void UPDATE_CHECK_ASSERT() {}
#endif
public:
	static CDesktop& GetDefault(); 
	static CDesktop* GetDefaultPointer();
	static void CloseDefault();
};


} // namespace GXWINDOW {
