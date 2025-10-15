#pragma once

namespace GXWINDOW { 

namespace INLINECLASS {  class CButtonClickNotifySet; }

class GXWINDOW_EXPORTS_DLL CButtonClickNotify : public CComponentNotify
{
	friend class CAbstractButton;
public:
	CButtonClickNotify();
	virtual ~CButtonClickNotify();
protected:
	virtual void OnButtonClick( CAbstractButton& c) = 0;
};

class GXWINDOW_EXPORTS_DLL CAbstractButton : public CComponent
{
	bool		m_bDrawDown;
	bool		m_bMouseIn;
	bool		m_bMouseMove;

	int			m_nClickAudio;

	INLINECLASS::CButtonClickNotifySet*	m_cls;
public:
	CAbstractButton();
	virtual ~CAbstractButton();
protected:
	void SetButtonDown( bool bDown);
	void SetButtonMouseIn( bool bMouseIn);
	bool IsButtonDown() const;
	bool IsButtonMouseIn() const;

	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown) = 0;

	virtual void OnMouseEnter();
	virtual void OnMouseLeave();

	virtual void OnComponentHide();
public:
	virtual bool OnLeftButtonUp( int x, int y, int key);
	virtual bool OnLeftButtonDown( int x, int y, int key);
	virtual bool OnMouseMove( int x, int y, int key);

	virtual void OnButtonClick();

	void ButtonClickNotifyAdd( CButtonClickNotify& bcn);
	void ButtonClickNotifyRemove( CButtonClickNotify& bcn);
public:
	virtual void SetClickAudio( int n);
	virtual int GetClickAudio() const;

	static void SetDefaultClickAudio( int n);
	static int GetDefaultClickAudio();
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GXWINDOW_EXPORTS_DLL CImageButton : public CAbstractButton
{
	friend class CScrollBar;
	friend class CComboBox;

	CImageable	m_ips;
public:
	CImageButton();
	virtual ~CImageButton();
protected:
	virtual void DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown);

	virtual void OnComponentShow();
	virtual void OnComponentHide();

	//virtual void SetButtonDown( bool bDown);
public:
	virtual const ImageParams& GetImageButtonDown() const;
	virtual void SetImageButtonDown( const ImageParams& ip);

	virtual void SetComponentImageNormal( const ImageParams& ip);
	virtual const ImageParams& GetComponentImageNormal() const;

	virtual void SetComponentImageMouseIn( const ImageParams& ip);
	virtual const ImageParams& GetComponentImageMouseIn() const;

	virtual const ImageParams& GetComponentImageDisable() const;
	virtual void SetComponentImageDisable( const ImageParams& ip);
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GXWINDOW_EXPORTS_DLL CTextButton : public CAbstractButton, public CTextable
{
	enum { CIB_MOUSEDOWN = 0, };
	CCustomeTextDrawStatManager*	m_pctdsm;
	bool	m_bDrawMouseDown;
public:
	CTextButton();
	virtual ~CTextButton();
protected:
	virtual void DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown);
public:
	virtual void SetComponentTextMouseDownColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void ReleaseComponentTextMouseDownColor();
	virtual COLOR GetComponentTextMouseDownColor() const;

	virtual void SetComponentFont(int nFont);

	virtual void FreeAllTextDraw();
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GXWINDOW_EXPORTS_DLL CTileImageButton : public CAbstractButton
{
	CTileImageAble		m_tileimages;
public:
	CTileImageButton();
	virtual ~CTileImageButton();
protected:
	virtual void DrawButton(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown);

	virtual void OnComponentShow();
	virtual void OnComponentHide();
public:
	virtual CImagesFill& GetTileImageNormal();
	virtual CImagesFill& GetTileImageMouseIn();
	virtual CImagesFill& GetTileImageButtonDown();
	virtual CImagesFill& GetTileImageDisable();

	virtual void FreeAllImage();

	virtual void SetSize(int w, int h);
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GXWINDOW_EXPORTS_DLL CTextImageButton : public CTileImageButton, public CTextable
{
	enum { CIB_MOUSEDOWN = 0, };
	CCustomeTextDrawStatManager*	m_pctdsm;
public:
	CTextImageButton();
	virtual ~CTextImageButton();
protected:
	virtual void DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown);
public:
	virtual void SetComponentTextMouseDownColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void ReleaseComponentTextMouseDownColor();
	virtual COLOR GetComponentTextMouseDownColor() const;
	virtual FONTEFEECTFLAG GetComponentTextMouseDownEffect() const;
	virtual COLOR GetComponentTextMouseDownEffectColor() const;

	virtual void FreeAllTextDraw();
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

enum ButtonBoxStyle
{
	BBS_CUSTOM		= -1,
	BBS_NONE		= 0,
	BBS_TYPAL,
	BBS_IMAGE,
	BBS_TILE,
};

class GXWINDOW_EXPORTS_DLL CAbstractCheckButton : public CAbstractButton
{
	//CImageable		m_ips;
	CTileImageAble	m_tileimages;

	int					m_nBoxWidth;
	ButtonBoxStyle		m_bbs;
	bool				m_bChecked;
	bool				m_bBoxMutex;	// 如为true，选中时只渲染box部分，为false则选中时同时渲染box和其余
public:
	CAbstractCheckButton();
	virtual ~CAbstractCheckButton();
protected:
	virtual void SetChecked( bool bCheck, bool bNotify, bool bOnCheck);
	virtual void OnButtonClick();
	virtual void OnButtonCheck( bool bCheck);
	
	virtual void DrawCheckBox( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bCheck);
	virtual void DrawCheckBoxTypal( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bCheck);
	//virtual void DrawCheckBoxImage( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bCheck);
	virtual void DoDrawCheckBox( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bCheck);
	virtual void DoDrawCheckBoxTile( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bCheck);

	virtual void DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown);

	virtual void OnComponentShow();
	virtual void OnComponentHide();
public:
	virtual void SetChecked( bool bCheck = true, bool bNotify = true);
	virtual bool IsChecked() const;

	virtual void SetSize( int w, int h);

	virtual void SetBoxWidth( int w);
	virtual int GetBoxWidth() const;
	virtual int GetAbsBoxWidth() const;
	virtual void SetButtonBoxStyleImage( const ImageParams& normal, const ImageParams& selected, const ImageParams& mouseonnormal = CImageParams(), const ImageParams& mouseonselect = CImageParams(), const ImageParams& disable = CImageParams());
	virtual void SetButtonBoxStyle( ButtonBoxStyle bbs);
	virtual ButtonBoxStyle GetButtonBoxStyle() const;

	virtual void SetBoxMutex( bool bMutex);
	virtual bool IsBoxMutex() const;

	virtual CImagesFill& GetTileImageNormal();
	virtual CImagesFill& GetTileImageMouseIn();
	virtual CImagesFill& GetTileImageSelect();
	virtual CImagesFill& GetTileImageMouseInSelect();
	virtual CImagesFill& GetTileImageDisable();

	void SetStyle( int x, int y, int w = 60, int h = 25, int nBoxWidth = 25, BackGroundStyle bgs = BGS_CUSTOM, ButtonBoxStyle bbs = BBS_CUSTOM);
};

class GXWINDOW_EXPORTS_DLL CAbstractTextCheckButton : public CAbstractCheckButton
{
public:
	CAbstractTextCheckButton();
	virtual ~CAbstractTextCheckButton();
protected:
	virtual void DrawButtonTip( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn) = 0;
	virtual void DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown);
};

class GXWINDOW_EXPORTS_DLL CAbstractRadioButton : public CAbstractCheckButton
{
	friend class CTabControl;
	int		m_nGroupID;
public:
	CAbstractRadioButton();
	virtual ~CAbstractRadioButton();
//private:
	virtual void SetChecked( bool bCheck, bool bNotify);
protected:
	virtual void SetChecked( bool bCheck, bool bNotify, bool bOnCheck);
	virtual void OnButtonClick();
	virtual void OnButtonCheck( bool bCheck);
public:
	virtual void SetChecked( bool bCheck = true);

	virtual void SetGroupID( int id);
	virtual int GetGroupID() const;
	
	void SetStyle( int x, int y, int w = 60, int h = 25, int nGroupID = 0, int nBoxWidth = 25, BackGroundStyle bgs = BGS_CUSTOM, ButtonBoxStyle bbs = BBS_CUSTOM);
};

class GXWINDOW_EXPORTS_DLL CAbstractTextRadioButton : public CAbstractRadioButton
{
public:
	CAbstractTextRadioButton();
	virtual ~CAbstractTextRadioButton();
protected:
	virtual void DrawButtonTip( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn) = 0;
	virtual void DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown);
};


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GXWINDOW_EXPORTS_DLL CImageCheckButton : public CAbstractCheckButton
{
public:
	CImageCheckButton();
	virtual ~CImageCheckButton();
};

class GXWINDOW_EXPORTS_DLL CTextCheckButton : public CAbstractTextCheckButton, public CTextable
{
public:
	CTextCheckButton();
	virtual ~CTextCheckButton();
protected:
	virtual void DrawButtonTip( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn);
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GXWINDOW_EXPORTS_DLL CImageRadioButton : public CAbstractRadioButton
{
public:
	CImageRadioButton();
	virtual ~CImageRadioButton();
};

class GXWINDOW_EXPORTS_DLL CTextRadioButton : public CAbstractTextRadioButton, public CTextable
{
public:
	CTextRadioButton();
	virtual ~CTextRadioButton();
protected:
	virtual void DrawButtonTip( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn);
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GXWINDOW_EXPORTS_DLL CTextImageRadioButton : public CAbstractRadioButton, public CTextable
{
	enum { CTIRB_SELECTED = 0, CTIRB_SELECTEDMOUSEIN, };
	CCustomeTextDrawStatManager*	m_pctdsm;
	bool							m_bDrawSelected;
	bool							m_bDrawSelectedMouseIn;
public:
	CTextImageRadioButton();
	virtual ~CTextImageRadioButton();
protected:
	virtual void DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown);
public:
	virtual void SetSize(int w, int h);
	virtual void SetComponentTextSelectedColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void ReleaseComponentTextSelectedColor();
	virtual COLOR GetComponentTextSelectedColor() const;
	virtual FONTEFEECTFLAG GetComponentTextSelectedEffect() const;
	virtual COLOR GetComponentTextMouseSelectedEffectColor() const;
	virtual bool IsTextMouseSelectGradualColor() const;
	virtual COLOR GetComponentTextMouseSelectedGradualColor() const;

	virtual void SetComponentTextSelectedMouseInColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void ReleaseComponentTextSelectedMouseInColor();
	virtual COLOR GetComponentTextSelectedMouseInColor() const;
	virtual FONTEFEECTFLAG GetComponentTextSelectedMouseInEffect() const;
	virtual COLOR GetComponentTextSelectedMouseInEffectColor() const;
	virtual bool IsTextTextSelectedMouseInGradualColor() const;
	virtual COLOR GetComponentTextSelectedMouseSelectedGradualColor() const;

	virtual void FreeAllTextDraw();
//private:
	virtual void SetBoxWidth( int w);
//	virtual int GetBoxWidth() const;
//	virtual int GetAbsBoxWidth() const;
//	virtual void SetButtonBoxStyle( ButtonBoxStyle bbs);
//	virtual ButtonBoxStyle GetButtonBoxStyle() const;
};


class GXWINDOW_EXPORTS_DLL CTextImageCheckButton : public CAbstractCheckButton, public CTextable
{
	enum { CTIRB_SELECTED = 0, CTIRB_SELECTEDMOUSEIN, };
	CCustomeTextDrawStatManager*	m_pctdsm;
	bool							m_bDrawSelected;
	bool							m_bDrawSelectedMouseIn;
public:
	CTextImageCheckButton();
	virtual ~CTextImageCheckButton();
protected:
	virtual void DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown);
public:
	virtual void SetSize(int w, int h);
	virtual void SetComponentTextSelectedColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void ReleaseComponentTextSelectedColor();
	virtual COLOR GetComponentTextSelectedColor() const;
	virtual FONTEFEECTFLAG GetComponentTextSelectedEffect() const;
	virtual COLOR GetComponentTextMouseSelectedEffectColor() const;
	virtual bool IsTextMouseSelectGradualColor() const;
	virtual COLOR GetComponentTextMouseSelectedGradualColor() const;

	virtual void SetComponentTextSelectedMouseInColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void ReleaseComponentTextSelectedMouseInColor();
	virtual COLOR GetComponentTextSelectedMouseInColor() const;
	virtual FONTEFEECTFLAG GetComponentTextSelectedMouseInEffect() const;
	virtual COLOR GetComponentTextSelectedMouseInEffectColor() const;
	virtual bool IsTextTextSelectedMouseInGradualColor() const;
	virtual COLOR GetComponentTextSelectedMouseSelectedGradualColor() const;

	virtual void FreeAllTextDraw();
	virtual void SetBoxWidth( int w);
};
} //namespace GXWINDOW { 
