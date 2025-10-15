#pragma once

namespace GXWINDOW { 

namespace INLINECLASS { class CTabControlItemVector; class CTabChangedNotifySet; }

class GXWINDOW_EXPORTS_DLL CTabComponentAllocator : public CUncopyableObject
{
public:
	CTabComponentAllocator();
	virtual ~CTabComponentAllocator();
public:
	virtual CAbstractRadioButton* NewRadioButton();
	virtual void DeleteRadioButton( CAbstractRadioButton* pbtn);
};

class GXWINDOW_EXPORTS_DLL CTabChangedNotify : public CComponentNotify
{
	friend class CTabControl;
public:
	CTabChangedNotify();
	virtual ~CTabChangedNotify();
protected:
	virtual void OnTabChanged( CTabControl& src, int from, int to) = 0;
};

class GXWINDOW_EXPORTS_DLL CTabControl : public CBKComponent, public CButtonClickNotify
{
	CTabComponentAllocator*				m_pAlloctor;
	INLINECLASS::CTabControlItemVector*	m_piv;
	INLINECLASS::CTabChangedNotifySet*	m_tcns;
	CComponent*							m_pLastMouseIn;

	CPanel								m_TitlePanel;

	int									m_nClickAudio;
	bool								m_bUseDefaultAudio;

	bool								m_bVertical;
	bool								m_bRightBottom;
	int									m_nBtnWidth;
	int									m_nBtnHeight;
	int									m_nTitleSize;
	
	int									m_nSelected;
	int									m_nTitleOffset;	// 用于控制标签按钮偏移

	COLOR								m_clrBorder;
public:
	CTabControl();
	CTabControl( CTabComponentAllocator& allocator);
	virtual ~CTabControl();
public:
	CTabComponentAllocator& GetAllocator();
	virtual void SetTitleSize( int n);
	virtual int GetTitleSize() const;
	virtual void SetButtonWidth( int n);
	virtual int GetButtonWidth() const;
	virtual void SetButtonHeight( int n);
	virtual int GetButtonHeight() const;
	virtual void SetTitleVertical( bool bv);
	virtual bool IsTitleVertical() const;
	virtual void SetTitleRightBottom( bool br);
	virtual bool IsTitleRightBottom() const;

	virtual int InsetComponent( CComponent& com);
	virtual int InsetComponent( CComponent& com, const ImageParams& normal, const ImageParams& selected, const ImageParams& mouseonnormal = CImageParams(), const ImageParams& mouseonselect = CImageParams(), const ImageParams& disable = CImageParams());
	virtual void RemoveComponent( CComponent& com);
	virtual int GetComponentCount() const;
	virtual CComponent& GetComponent( int nIndex);
	virtual CAbstractRadioButton* GetRadioButton( int nIndex);

	virtual int GetSelect() const;
	virtual void SetSelect( int nIndex);

	virtual void SetSize( int w, int h);

	void TabChangedNotifyAdd( CTabChangedNotify& tcn);
	void TabChangedNotifyRemove( CTabChangedNotify& tcn);
private:
	int GetIndexByComponent( CComponent& com);
	int GetIndexByRadioButton( CComponent& btn);
	void CheckTitlePanelPos();
	void CheckTitleButtonSize();
	void CheckClientComponentSize();
	void Init( CTabComponentAllocator& allocator);
protected:
	virtual void OnMouseLeave();
	virtual void OnInitializeButton( int nIndex, CAbstractRadioButton* arb);
	virtual void OnButtonClick( CAbstractButton& c);
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	virtual void OnComponentShow();
	virtual void OnComponentHide();

	virtual void SetBorderColor(COLOR clr);
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

	virtual void SetTitleOffset( int n);
	virtual int  GetTitleOffset() const;

	virtual void FreeAllTextDraw();

	virtual void SetTabVisible( int index, bool bVisible);
	virtual bool IsTabVisible( int index) const;

	virtual void SetTabClickAudio( int n);
	virtual int GetTabClickAudio() const;
	virtual int GetCurrentTabClickAudio() const;

	static void SetDefaultTabClickAudio( int n);
	static int GetDefaultTabClickAudio();
};


} // namespace GXWINDOW { 
