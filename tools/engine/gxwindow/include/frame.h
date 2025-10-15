#pragma once

namespace GXWINDOW { 

namespace INLINECLASS { class CContainer; class CFrameClosedNotifySet; class CFramePosChangedNotifySet; class CFrameShowHideNotifySet; class CAbstactDesktop; class CFrameDestructureNotifySet; }


class GXWINDOW_EXPORTS_DLL CLayoutManager : public CUncopyableObject
{
	friend class CPanel;
public:
	CLayoutManager();
	virtual ~CLayoutManager();
public:
	virtual CLayoutManager* Clone() const = 0;
	virtual void Delete() = 0;
protected:
	virtual void Layout( CPanel& panel) = 0;
};

class GXWINDOW_EXPORTS_DLL CPositionLayoutManager : public CLayoutManager
{
public:
	CPositionLayoutManager();
	virtual ~CPositionLayoutManager();
public:
	virtual CLayoutManager* Clone() const;
	virtual void Delete();
public:
	virtual void Layout( CPanel& panel);
};


class GXWINDOW_EXPORTS_DLL CPanel : public CComponent
{
	friend class CTabControl;
	friend class CDesktop;

	COLOR				m_clrBorder;	// ���alphaֵ��Ϊ0, ����ñ߿����, ���򲻵���

	CPanel( void*);
protected:
	CComponent*			m_pLastMoveIn;
	INLINECLASS::CContainer*		m_children;
	CLayoutManager*		m_layout;
public:
	CPanel();
	virtual ~CPanel();
public:
	virtual bool OnLeftButtonDown( int x, int y, int key);
	virtual bool OnLeftButtonUp( int x, int y, int key);
//	virtual bool OnLeftButtonDoubleClick( int x, int y, int key);

	virtual bool OnRightButtonDown( int x, int y, int key);
	virtual bool OnRightButtonUp( int x, int y, int key);
//	virtual bool OnRightButtonDoubleClick( int x, int y, int key);

	virtual bool OnMouseMove( int x, int y, int key);
	virtual bool OnMouseWheel( int x, int y, int key);
public:
	virtual void InsertComponent( CComponent* c);
	virtual void RemoveComponent( CComponent* c);
	virtual void RemoveAllComponent();
	virtual void SetComponentForeground( CComponent* c);
	virtual void SetComponentBackground( CComponent* c);

	virtual CComponent* ComponentFromPoint( int x, int y);
	virtual const CComponent* ComponentFromPoint( int x, int y) const;

	virtual bool IsContainer( const CComponent& c) const;
	virtual bool IsContainer( const CComponent* p) const;
	virtual int GetChildCount() const;
	virtual CComponent* GetChild( int n);
public:
	virtual void DoLayout();
	virtual void SetLayoutManager( const CLayoutManager& lm);
	virtual const CLayoutManager& GetLayoutManager() const;
protected:
	virtual void OnMouseEnter();
	virtual void OnMouseLeave();

	virtual void DoCheckMouseEnterLeaveComponent( int x, int y);

	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	virtual void OnComponentShow();
	virtual void OnComponentHide();

	virtual void SetBorderColor( COLOR clr);
	virtual COLOR GetBorderColor() const { return m_clrBorder; }
public:
	virtual bool IsPointInMsgTransparent( int x, int y) const;
};

class GXWINDOW_EXPORTS_DLL CBKPanel : public CPanel
{
	BackGroundStyle		m_BackGroundStyle;
	COLOR				m_BackGroundColor;
	CImagesFill			m_BackGroundTileImages;
public:
	CBKPanel();
	virtual ~CBKPanel();
public:
	virtual void SetBackGroundStyle( BackGroundStyle bgs);
	virtual void SetBackGroundStyleImage( const ImageParams& ip);
	virtual void SetBackGroundStyleColor( COLOR clr);
	virtual COLOR GetBackGroundStyleColor() const;
	virtual BackGroundStyle GetBackGroundStyle() const;
	virtual void RemoveBackGroundStyleImage();

	void SetStyle( int x, int y, int w = 60, int h = 25, BackGroundStyle bgs = BGS_CUSTOM);

	virtual CImagesFill& GetTilesBackGround();
	virtual const CImagesFill& GetTilesBackGround() const;

	virtual void SetSize( int w, int h);
protected:
	void DoDrawBackGround( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	virtual void DrawBackGround( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	virtual void OnComponentShow();
	virtual void OnComponentHide();
};

enum FrameMoveStyle
{
	FMS_NONE	= 0,
	FMS_TITLE,
	FMS_BODY,
	FMS_ALL,
};


// ��ͨ������Ŀǰ���󣬸�ͨ�����ͬʱ֪ͨ��ʾ�͹ر�
class GXWINDOW_EXPORTS_DLL CFrameClosedNotify : public CComponentNotify
{
	friend class CFrame;
public:
	CFrameClosedNotify();
	virtual ~CFrameClosedNotify();
protected:
	virtual void OnFrameClosed( CFrame& c) = 0;
};

class GXWINDOW_EXPORTS_DLL CFramePosChangedNotify : public CComponentNotify
{
	friend class CFrame;
public:
	CFramePosChangedNotify();
	virtual ~CFramePosChangedNotify();
protected:
	virtual void OnFramePosChange( CFrame& c, int nx, int ny, int ox, int oy) = 0;
};

class GXWINDOW_EXPORTS_DLL CFrameShowHideNotify : virtual public CComponentNotify
{
	friend class CFrame;
	friend class CDesktop;
public:
	CFrameShowHideNotify();
	virtual ~CFrameShowHideNotify();
protected:
	virtual void OnFrameShowHide( CFrame& c, bool show) = 0;
};

class GXWINDOW_EXPORTS_DLL CFrameShowQuery : virtual public CComponentNotify
{
	friend class CFrame;
public:
	CFrameShowQuery();
	virtual ~CFrameShowQuery();
protected:
	virtual bool QueryIsFrameCanShow( GXWINDOW::CFrame& c) = 0;
};

// Frame����ʱ����ȥ, ��ĳЩ�ͻ��˽�������п�������
class GXWINDOW_EXPORTS_DLL CFrameDestructureNotify : virtual public CComponentNotify
{
	friend class CFrame;
public:
	CFrameDestructureNotify();
	virtual ~CFrameDestructureNotify();
protected:
	virtual void OnFrameDestructure( GXWINDOW::CFrame& f) = 0;
};

class GXWINDOW_EXPORTS_DLL CFrame : public CBKPanel
{
	friend class CComboBox;

	bool		m_bVisible;
	bool		m_bMoving;

	bool		m_bEscClose;		// ESC �Զ��ر�
	bool		m_bRBClose;			// �Ҽ��ر�
	bool		m_bViewAsPopup;
	bool		m_bEnable;		// Ϊtrueʱ������Frame, Ϊfalseʱ�������κ��ⲿ��Ϣ������, �����齫��ֵ����Ȩ��¶���ⲿ

	bool		m_bMinSized;	// �ô�������С�����������κ���Ϣ������ʾ
	bool		m_bHighPriOpt;
	bool		m_bUnCheckMustView;

	int			m_nTitleHeight;
	int			m_nTitleWidth;  // Ŀǰ�趨titleλ��Ϊ(0,0)

	int			m_nViewLevel;
	int			m_nLastX;
	int			m_nLastY;

	CFrame*		m_pModalParent;
	CFrame*		m_pModalChild;

	FrameMoveStyle		m_fms;

	INLINECLASS::CFrameClosedNotifySet*			m_fcns;
	INLINECLASS::CFramePosChangedNotifySet*		m_fpcns;

	INLINECLASS::CFrameShowHideNotifySet*		m_fpshns;
	INLINECLASS::CFrameDestructureNotifySet*	m_fdestns;
	static CFrameShowQuery*						s_fsq;
public:
	CFrame();
	virtual ~CFrame();
public:
	void SetViewLevel( int level = 0);
	int GetViewLevel() const;

	void SetMinSize( bool b = true);
	bool IsMinSize() const;

	void SetHighPriorityOperator( bool b = true);
	bool IsHighPriorityOperator() const;

	bool IsVisible() const;

	void SetMoveStyle( FrameMoveStyle fms);
	FrameMoveStyle GetMoveStyle() const;

	void SetTitleHeight( int nHeight);
	int GetTitleHeight() const;
	void SetTitleWidth( int nWidth);
	int GetTitleWidth() const;

	void SetAutoClose( bool b);
	void SetEscClose( bool b);
	void SetRightButtonClose( bool b);
	bool IsEscClose() const;
	bool IsRightButtonClose() const;

	bool IsCanEscClose() const;
	bool IsCanRightButtonClose() const;

	void SetViewAsPopup( bool b);
	bool IsViewAsPopup() const;

	void SetStyle( int x, int y, int w = 60, int h = 25, int nTitleHeight = 10, BackGroundStyle bgs = BGS_CUSTOM, FrameMoveStyle fms = FMS_ALL);
protected:
	virtual bool IsPointInTitle( int x, int y);		// x ,y base this frame
	virtual bool IsPointInBody( int x, int y);		// x ,y base this frame
private:
	virtual void SetEnable( bool bEnable = true);
	virtual bool IsEnable() const;
	virtual void ModalChildAdd( CFrame* child);
	virtual void ModalChildRemove( CFrame* child);
public:
	virtual void SetPos( int x, int y);
	virtual void CenterToDesktop();
	virtual void Show();
	virtual void ShowModal( CFrame& parent);
	virtual void Hide();

	virtual bool OnLeftButtonDown( int x, int y, int key);
	virtual bool OnLeftButtonUp( int x, int y, int key);
	virtual bool OnMouseMove( int x, int y, int key);
	virtual bool OnRightButtonUp( int x, int y, int key);
	virtual bool OnRightButtonDown( int x, int y, int key);

	void FrameClosedNotifyAdd( CFrameClosedNotify& fcn);
	void FrameClosedNotifyRemove( CFrameClosedNotify& fcn);

	void FramePosChangedNotifyAdd( CFramePosChangedNotify& fpcn);
	void FramePosChangedNotifyRemove( CFramePosChangedNotify& fpcn);

	void FrameShowHideNotifyAdd( CFrameShowHideNotify& fshn);
	void FrameShowHideNotifyRemove( CFrameShowHideNotify& fshn);

	void FrameDestructureNotifyAdd( CFrameDestructureNotify& notify);
	void FrameDestructureNotifyRemove( CFrameDestructureNotify& notify);

	void SetUnCheckPosBeforeShow( bool b);
	bool IsUnCheckPosBeforeShow() const;
public:
	static bool FrameShowQueryAdd( CFrameShowQuery& q);
	static bool FrameShowQueryRemove( CFrameShowQuery& q);
	static void FrameShowQueryClear();
protected:
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual bool OnMouseWheel( int x, int y, int key);
};

class GXWINDOW_EXPORTS_DLL CMessageDispatchNotify : public CComponentNotify
{
	friend class CDesktop;
public:
	CMessageDispatchNotify();
	virtual ~CMessageDispatchNotify();
protected:
	virtual bool OnMessageDispatch( UINT uMsg, WPARAM wParam, LPARAM lParam, bool bBefore) = 0;
};

class GXWINDOW_EXPORTS_DLL CPostFrameRenderNotify : public CComponentNotify
{
	friend class CDesktop;
public:
	CPostFrameRenderNotify();
	virtual ~CPostFrameRenderNotify();
protected:
	virtual void OnPostFrameRender( CFrame* frame) = 0;
};

} // namespace GXWINDOW { 

