#pragma once

namespace GXWINDOW {

namespace INLINECLASS {  class CScrollPanelDispatchMessage;	}

class GXWINDOW_EXPORTS_DLL CScrollPanelComponentAllocator : public CUncopyableObject
{
public:
	CScrollPanelComponentAllocator();
	virtual ~CScrollPanelComponentAllocator();
public:
	virtual CScrollBar* NewScrollBar();
	virtual void DeleteScrollBar( CScrollBar* sb);
};

class GXWINDOW_EXPORTS_DLL CScrollPanel : public CComponent, private CComponentSizeChangedNotify, private CBarComponentPositionNotify
{
	friend class INLINECLASS::CScrollPanelDispatchMessage;
	friend class CComboBox;

	CScrollPanelComponentAllocator*	m_Alloc;

	CScrollBar*		m_vsb;
	CScrollBar*		m_hsb;
	CScrollBar*		m_bindingvsb;	// 绑定外部的垂直滚动条
	CScrollBar*		m_bindinghsb;	// 绑定外部的水平滚动条
	bool			m_bvsb;
	bool			m_bhsb;
	bool			m_bvsbforceshow;
	bool			m_bhsbforceshow;
	bool			m_bAutoHide;
	bool			m_bForceHide;
	bool			m_bMouseIn;
	bool			m_bAutoClientWidth;
	int				m_nOffsetX;
	int				m_nOffsetY;

	CComponent*		m_pLastMouseIn;
	CComponent*		m_client;

	COLOR			m_clrBorder;
public:
	CScrollPanel();
	CScrollPanel( CScrollPanelComponentAllocator& alloc);
	virtual ~CScrollPanel();
private:
	void Initialize( CScrollPanelComponentAllocator& alloc);
	void OnComponentSizeChanged( CComponent& c, int nw, int nh, int ow, int oh);
	void OnBarComponentPosition( CBarComponent& c, int pos, int max);
	void CheckMouseEnterLeave( CComponent& com);
protected:
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void OnMouseEnter();
	virtual void OnMouseLeave();

	virtual void OnComponentShow();
	virtual void OnComponentHide();

	virtual void SetBorderColor( COLOR clr);
public:
	CScrollBar* GetVerticalScrollBar();
	CScrollBar* GetHorizontalScrollBar();

	virtual void SetSize( int w, int h);

	virtual void SetComponent( CComponent& client);
	virtual CComponent& GetClient();
	virtual void RemoveComponent();

	virtual bool OnLeftButtonDown( int x, int y, int key);
	virtual bool OnLeftButtonUp( int x, int y, int key);
//	virtual bool OnLeftButtonDoubleClick( int x, int y, int key);

	virtual bool OnRightButtonDown( int x, int y, int key);
	virtual bool OnRightButtonUp( int x, int y, int key);
//	virtual bool OnRightButtonDoubleClick( int x, int y, int key);

	virtual bool OnMouseMove( int x, int y, int key);
	virtual bool OnMouseWheel( int x, int y, int key);

	virtual void SetVerticalBarForceShow( bool bShow = true);
	virtual void SetHorizontalBarForceShow( bool bShow = true);
	virtual bool IsVerticalBarForceShow() const;
	virtual bool IsHorizontalBarForceShow() const;

	virtual void SetAutoHideScrollBar( bool bShow = true);
	virtual bool IsAutoHideScroBar() const;

	virtual void SetForceHideScrollBar( bool bHide = true);
	virtual bool IsForceHideScrollBar() const;

	// 设为true则client控件会根据垂直滚动条的出现与否调整自身宽度, 该模式下一般没有水平滚动条, 主要应用于多行文本框、多行编辑框
	virtual void SetAutoClientWidth( bool bAuto);	
	virtual bool IsAutoClientWidth() const;

	// 绑定外部滚动条
	virtual void BindingVerticalBar( CScrollBar* vsb);
	virtual void BindingHorizontalBar( CScrollBar* hsb);

	virtual bool IsPointInMsgTransparent( int x, int y) const;

	void AdjustScrollInfo();	
private:
	virtual void AdjustScrollInfo( int cw, int ch);	
	virtual void AdjustBindingScrollInfo();
};

} // namespace GXWINDOW {