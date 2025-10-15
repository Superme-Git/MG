#pragma once

namespace GXWINDOW { 

enum BackGroundStyle
{
	BGS_CUSTOM		= -1,
	BGS_NONE		= 0,
	BGS_COLOR,
	BGS_IMAGE,
	BGS_TILES,
};

class CComponent;
class CPanel;
class CFrame;
class CSliderBar;
class CImageButton;
class CTextButton;
class CTabControl;
class CAbstactPackageCell;
class CAbstactPackagePanel;
class CDesktop;
class CTipShowPosLayout;

namespace INLINECLASS {  class CContainer; class CComponentSizeChangedNotifySet; class CSimpleString; class CComponentMouseEventNotifySet; class CComponentShowHideNotifySet; }

class GXWINDOW_EXPORTS_DLL CAbstractTipInfoFrameManager : public CUncopyableObject
{
public:
	virtual CComponent* GetTipInfoFrame( const wchar_t* tiptext) = 0;
};

class GXWINDOW_EXPORTS_DLL CComponentSizeChangedNotify : public CComponentNotify
{
	friend class CComponent;
public:
	CComponentSizeChangedNotify();
	virtual ~CComponentSizeChangedNotify();
protected:
	virtual void OnComponentSizeChanged( CComponent& c, int nw, int nh, int ow, int oh) = 0;
};

class GXWINDOW_EXPORTS_DLL CComponentMouseEventNotify : public CComponentNotify
{
	friend class CComponent;
public:
	CComponentMouseEventNotify();
	virtual ~CComponentMouseEventNotify();
protected:
	virtual void OnLeftButtonDown( CComponent& c, int x, int y, int key);
	virtual void OnLeftButtonUp( CComponent& c, int x, int y, int key);
//	virtual void OnLeftButtonDoubleClick( CComponent& c, int x, int y, int key);

	virtual void OnRightButtonDown( CComponent& c, int x, int y, int key);
	virtual void OnRightButtonUp( CComponent& c, int x, int y, int key);
//	virtual void OnRightButtonDoubleClick( CComponent& c, int x, int y, int key);

	virtual void OnMouseMove( CComponent& c, int x, int y, int key);
	virtual void OnMouseWheel( CComponent& c, int x, int y, int key);

	virtual void OnMouseEnter( CComponent&c);
	virtual void OnMouseLeave( CComponent&c);
};

class GXWINDOW_EXPORTS_DLL CTipFrameQuery : public CComponentMouseEventNotify
{
	friend class CComponent;
public:
	CTipFrameQuery();
	virtual ~CTipFrameQuery();
protected:
	virtual CComponent* QueryTipFrame( CComponent& c) = 0;
};

class GXWINDOW_EXPORTS_DLL CComponentAfterDrawNofity : public CComponentNotify
{
	friend class CComponent;
public:
	CComponentAfterDrawNofity();
	virtual ~CComponentAfterDrawNofity();
protected:
	virtual void OnComponentAfterDraw( CComponent& c, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch) = 0;
};

class GXWINDOW_EXPORTS_DLL CComponentShowHideNotify : public CComponentNotify
{
	friend class CComponent;
public:
	CComponentShowHideNotify();
	virtual ~CComponentShowHideNotify();
protected:
	virtual void OnComponentShowHide( GXWINDOW::CComponent& c, bool bShow) = 0;
};

class GXWINDOW_EXPORTS_DLL CComponent : public CUncopyableObject	// �ޱ���ͼƬ�Ŀؼ�
{
	friend class INLINECLASS::CContainer;
	friend class CDraw;
	friend class CPanel;
	friend class CListBox;
	friend class CComboBox;
	friend class CScrollPanel;
	friend class CTableHeader;
	friend class CTable;
	friend class CScrollBar;
	friend class CTabControl;
	friend class CBlendEditElement;
	friend class CTextArea;
	friend class CKeyInput;
	friend class CTableDataModule;

	CComponent*			m_parent;
	CTipShowPosLayout*	m_pTipShowPos;

	INLINECLASS::CComponentSizeChangedNotifySet*	m_sizens;
	INLINECLASS::CComponentMouseEventNotifySet*		m_mens;
	INLINECLASS::CComponentShowHideNotifySet*		m_shns;

	int				m_x;
	int				m_y;
	int				m_nWidth;
	int				m_nHeight;

	bool			m_bMouseEnter;			// MouseLeave�¼��Ƚ���, ��һ�����������ֻ֤���߼���ȷ����Ӧ֪ͨ��ȥ, ��ֵ��ֱ�ӱ�¶���ⲿʹ��
											// m_bMouseEnterΪtrue��ʾ����ѽ���, �����ͬ�ڴ�����һ��OnMouseEnter()

	bool			m_bEnable;
	bool			m_bNotTransparent;		// ���ڿ��ƶ������Ϣ��Ӧ��͸����
	bool			m_bViewOnly;			// Ϊtrueʱ�ؼ�ֻ������ʾ, ���������û���Ϣ������Ӧ���ػ���ɷ�, ��m_bNotTransparent����Ҫ���������Ƿ������ɷ���Ϣ
	int				m_nOrigX;				// ����ʱ���ܻ�ı����꣬�����������������ԭʼ����
	int				m_nOrigY;

	INLINECLASS::CSimpleString*	m_pname;	// �ؼ�����, �������

	INLINECLASS::CSimpleString* m_ptiptext;	// ������ʾ�ı�
	CTipFrameQuery*				m_pTipQuery;
	CComponent*					m_tipinfo;
	CComponent*					m_showtip;
	CComponentAfterDrawNofity*		m_ComponentAfterDrawNofity;
public:
	CComponent();
	virtual ~CComponent();
public:
	virtual void ShowTipInfo();
	virtual void HideTipInfo();

	void SetStyle( int x, int y, int w = 60, int h = 25);
protected:
	virtual bool IsViewCliped( int cw, int ch) const;

	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	virtual void OnMouseEnter();
	virtual void OnMouseLeave();

	virtual void OnComponentShow();			// �ڸ÷����ж������������Դ���м�¼
	virtual void OnComponentHide();			// �ڸ÷����ж��Ѿ�������������ע��, �緢��ĳ�������Ѿ���ȫû�б�ʹ��, ��ɶԸ���������ͷ�

	virtual void SetBorderColor( COLOR clr) {};
public:
	virtual void SetParent( CComponent* parent);
	virtual CComponent* GetParent();
	virtual const CComponent* GetParent() const;
	virtual CFrame* GetParentFrame();
	virtual CFrame* GetAbsParentFrame();
	virtual void SetTipInfo( CComponent* pinfo, CTipShowPosLayout* poslayout = NULL);
	virtual CComponent* GetTipInfo();
	virtual const CComponent* GetTipInfo() const;

	void SetComponentName( const wchar_t* name);
	const wchar_t* GetComponentName() const;

	void SetTipText( const wchar_t* text);
	void SetTipText( int n);
	const wchar_t* GetTipText() const;

	bool IsSameComponent( const CComponent& c) const;
	bool IsSameComponent( const CComponent* p) const;
	bool operator==( const CComponent& c) const;

	void SetCapture();
	void ReleaseCapture();

	virtual bool IsPointIn( int x, int y) const; // x y base parant component
	virtual bool IsAbsPointIn( int x, int y) const;

	virtual int GetPosX() const;
	virtual int GetPosY() const;
	virtual int GetPosR() const;
	virtual int GetPosB() const;

	virtual void SetPos( int x, int y); // x y base parant component
	virtual void SetSize( int w, int h);

	virtual int GetAbsPosX() const;
	virtual int GetAbsPosY() const;
	virtual int GetAbsPosR() const;
	virtual int GetAbsPosB() const;

	virtual int GetWidth() const;
	virtual int GetHeight() const;
	virtual int GetAbsWidth() const;
	virtual int GetAbsHeight() const;

	virtual void SetEnable( bool bEnable = true);
	virtual bool IsEnable() const;

	virtual void SetTransparent( bool bTrans = true);
	virtual bool IsTransparent() const;

	virtual void SetViewOnly( bool bViewOnly = true);
	virtual bool IsViewOnly() const;

	virtual void Draw( CDraw& draw);

	virtual bool OnLeftButtonDown( int x, int y, int key);
	virtual bool OnLeftButtonUp( int x, int y, int key);
//	virtual bool OnLeftButtonDoubleClick( int x, int y, int key);

	virtual bool OnRightButtonDown( int x, int y, int key);
	virtual bool OnRightButtonUp( int x, int y, int key);
//	virtual bool OnRightButtonDoubleClick( int x, int y, int key);

	virtual bool OnMouseMove( int x, int y, int key);
	virtual bool OnMouseWheel( int x, int y, int key);

	void ComponentSizeChangedNotifyAdd( CComponentSizeChangedNotify& cmsn);
	void ComponentSizeChangedNotifyRemove( CComponentSizeChangedNotify& cmsn);

	void ComponentMouseEventNotifyAdd( CComponentMouseEventNotify& men);
	void ComponentMouseEventNotifyRemove( CComponentMouseEventNotify& men);

	void ComponentShowHideNotifyAdd( CComponentShowHideNotify& shn);
	void ComponentShowHideNotifyRemove( CComponentShowHideNotify& shn);

	void TipFrameQueryAdd( CTipFrameQuery& tfq);
	void TipFrameQueryRemove();

	virtual bool IsPointInMsgTransparent( int x, int y) const;

	void DoComponentAfterDrawNofity( CDraw& draw);
	void DoComponentAfterDrawNofity( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	void ComponentAfterDrawNofityAdd( CComponentAfterDrawNofity& n);
	void ComponentAfterDrawNofityRemove( CComponentAfterDrawNofity& n);
	void ComponentAfterDrawNofityClear();
};

class GXWINDOW_EXPORTS_DLL CBKComponent : public CComponent
{
	BackGroundStyle		m_BackGroundStyle;
	COLOR				m_BackGroundColor;
	CImagesFill			m_BackGroundTileImages;
public:
	CBKComponent();
	virtual ~CBKComponent();
public:
	virtual void SetBackGroundStyle( BackGroundStyle bgs);
	virtual void SetBackGroundStyleImage( const ImageParams& ip);
	virtual void SetBackGroundStyleColor( COLOR clr);
	virtual BackGroundStyle GetBackGroundStyle() const;

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

} // namespace GXWINDOW {