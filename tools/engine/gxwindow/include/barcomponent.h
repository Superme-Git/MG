#pragma once

namespace GXWINDOW
{

namespace INLINECLASS {  class CBarComponentPositionNotifySet; }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GXWINDOW_EXPORTS_DLL CBarable
{
protected:
	bool				m_bVertical;
	int					m_nMax;
	int					m_nPos;
	int					m_nStepSize;
public:
	CBarable();
	~CBarable();
public:
	virtual void SetVertical( bool b = true);
	virtual bool GetVertical() const;

	virtual void SetValueMax( int n);
	virtual int GetValueMax() const;
	virtual void SetValuePos( int n);
	virtual int GetValuePos() const;
	virtual void SetStepSize( int n);
	virtual int GetStepSize() const;
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GXWINDOW_EXPORTS_DLL CBarComponentPositionNotify : public CComponentNotify
{
	friend class CBarComponent;
public:
	CBarComponentPositionNotify();
	virtual ~CBarComponentPositionNotify();
protected:
	virtual void OnBarComponentPosition( CBarComponent& c, int pos, int max) = 0;
};

class GXWINDOW_EXPORTS_DLL CBarComponent : public CBKPanel, public CBarable
{
	INLINECLASS::CBarComponentPositionNotifySet*	m_nset;
public:
	CBarComponent();
	virtual ~CBarComponent();
public:
	void BarComponentPositionNotifyAdd( CBarComponentPositionNotify& bcpn);
	void BarComponentPositionNotifyRemove( CBarComponentPositionNotify& bcpn);

	virtual void SetValuePos( int n);
protected:
	virtual int GetBarWidth() const;
	virtual int GetBarHeight() const;

	virtual int GetBarAbsX() const;
	virtual int GetBarAbsY() const;
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


enum SliderRubStyle
{
	SRS_CUSTOM		= -1,
	SRS_NONE		= 0,
	SRS_TYPAL,
	SRS_IMAGE,
};

class GXWINDOW_EXPORTS_DLL CSliderRub : public CComponent
{
	friend class CSliderBar;

	bool				m_bRubMoving;
	bool				m_bBarMouseOn;
	SliderRubStyle		m_srs;
	//CImageable			m_ips;
	CImagesFill			m_ifnormal;
	CImagesFill			m_ifmouseon;
	CImagesFill			m_ifmoving;
public:
	CSliderRub();
	virtual ~CSliderRub();
public:
	virtual void SetSliderRubStyle( SliderRubStyle srs);
	virtual SliderRubStyle GetSliderRubStyle() const;
protected:
	virtual bool OnLeftButtonUp( int x, int y, int key);
	virtual bool OnLeftButtonDown( int x, int y, int key);
	virtual bool OnMouseMove( int x, int y, int key);
	virtual void OnMouseLeave();

	virtual void Draw(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void DrawRub( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseOn, bool bMoving);

	virtual void DrawImageRub(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown);

	virtual void OnComponentShow();
	virtual void OnComponentHide();
public:
	virtual void SetRubImageNormal( const ImageParams& ip);
	virtual const ImageParams& GetRubImageNormal() const;

	virtual void SetRubImageMouseOn( const ImageParams& ip);
	virtual const ImageParams& GetRubImageMouseOn() const;

	virtual void SetRubImageMoving( const ImageParams& ip);
	virtual const ImageParams& GetRubImageMoving() const;

	virtual CImagesFill& GetRubTilesNormal();
	virtual CImagesFill& GetRubTilesMouseOn();
	virtual CImagesFill& GetRubTilesMoving();

	virtual bool IsMoving() const;

	virtual void SetSize(int w, int h);
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

enum SliderScaleStyle
{
	SSS_CUSTOM		= -1,
	SSS_NONE		= 0,
	SSS_TYPAL,
	SSS_IMAGE,
};

class GXWINDOW_EXPORTS_DLL CSliderBar : public CBarComponent, private CComponentMouseEventNotify
{
	friend class CScrollBar;

	bool				m_bSmooth;
	int					m_nStartOff;
protected:
	CSliderRub			m_rub;
public:
	CSliderBar();
	virtual ~CSliderBar();
private:
	void PosNoSmoothFromPoint( int x, int y);
	bool PosSmoothFromPoint( int x, int y);
	void PosSmoothMovingFromPoint( int x, int y);
	int  GetPosSmoothMovingFromPoint( int x, int y);
protected:
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void AdjustRubPosition( int pos = -1);

	virtual void OnComponentShow();
	virtual void OnComponentHide();
public:
	virtual void SetSize(int w, int h);

	virtual void SetRubSize( int n);
	virtual int GetRubSize() const;
	
	virtual void SetSmooth( bool b = true);
	virtual bool GetSmooth() const;

	virtual void SetValuePos(int n);

	virtual void SetValueMax( int n);

	virtual void SetSliderScaleStyleImage( const ImageParams& ip);

	virtual void SetSliderRubStyle( SliderRubStyle srs);
	virtual void SetSliderRubStyleImage( const ImageParams& ipNormal, const ImageParams& ipMouseOn, const ImageParams& ipMoving);
	virtual const ImageParams& GetRubImageNormal() const;
	virtual const ImageParams& GetRubImageMouseOn() const;
	virtual const ImageParams& GetRubImageMoving() const;
	virtual SliderRubStyle GetSliderRubStyle() const;

	virtual CImagesFill& GetRubTilesNormal();
	virtual CImagesFill& GetRubTilesMouseOn();
	virtual CImagesFill& GetRubTilesMoving();

	virtual void SetVertical( bool b = true);

	using CBarComponent::OnLeftButtonUp;
//	using CBarComponent::OnLeftButtonDoubleClick;
	using CBarComponent::OnRightButtonDown;
	using CBarComponent::OnRightButtonUp;
//	using CBarComponent::OnRightButtonDoubleClick;
	using CBarComponent::OnMouseMove;
	using CBarComponent::OnMouseWheel;

	virtual bool OnLeftButtonDown( int x, int y, int key);

private:
	virtual void OnMouseMove( CComponent& c, int x, int y, int key);
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

enum ProgressBarStyle
{
	PBS_CUSTOM		= - 1,
	PBS_NONE		= 0,
	PBS_COLOR,
	PBS_IMAGE,
	PBS_TILE,
};

class GXWINDOW_EXPORTS_DLL CProgressBar : public CBarComponent
{
	ProgressBarStyle		m_pbs;
	union
	{
		COLOR				m_clrProgress;
		ImageParams			m_ipProgress;
	};
	CImagesFill			m_ifProgress;
public:
	CProgressBar();
	virtual ~CProgressBar();
public:
	virtual void StepIt();

	virtual ProgressBarStyle GetProgressBarStyle() const;
	virtual void SetProgressBarStyle( ProgressBarStyle pbs);
	virtual void SetProgressBarStyleColor( COLOR clr);
	virtual void SetProgressBarStyleImage( const ImageParams& ip);
	virtual void SetProgressBarStyleSimpleTile( const ImageParams& ip, int nTileWidth, int nTileHeight);

	virtual COLOR GetProgressBarColor();
	virtual ImageParams& GetProgressBarImage();
	virtual CImagesFill& GetProgressBarImageFill();
protected:
	void DoDrawProgress( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	virtual void DrawProgress( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int pos, int max);
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	virtual void OnComponentShow();
	virtual void OnComponentHide();
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class GXWINDOW_EXPORTS_DLL CScrollBarComponentAllocator : public CUncopyableObject
{
public:
	CScrollBarComponentAllocator();
	virtual ~CScrollBarComponentAllocator();
public:
	virtual CImageButton* NewImageButton();
	virtual void DeleteImageButton( CImageButton* btn);
};

class GXWINDOW_EXPORTS_DLL CScrollBar : public CSliderBar, private CButtonClickNotify 
{
	friend class CScrollPanel;
	CScrollBarComponentAllocator*	m_Alloc;
	CImageButton*		m_btnTL;
	CImageButton*		m_btnRB;
	bool				m_bAdaptMode;	// 为true时 滑块自适应调整长度
public:
	CScrollBar();
	CScrollBar( CScrollBarComponentAllocator& alloc);
	virtual ~CScrollBar();
private:
	void Initialize( CScrollBarComponentAllocator& alloc);
	void CheckButtonSize( int w, int h, int bs);

	virtual void OnButtonClick( CAbstractButton& c);
protected:
	virtual int GetBarWidth() const;
	virtual int GetBarHeight() const;
	virtual int GetBarAbsX() const;
	virtual int GetBarAbsY() const;

	virtual void AdjustRubPosition( int pos = -1);

	virtual void OnComponentShow();
	virtual void OnComponentHide();

	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
public:
	virtual void SetVertical( bool b = true);
	virtual void SetSize( int w, int h);
	virtual void SetButtonSize( int n);
	virtual int GetButtonSize() const;

	virtual void SetAdaptMode( bool bAdapt);
	virtual bool IsAdaptMode() const;

	virtual void SetValueMax( int n);

	virtual void SetSliderRubStyleImage( const ImageParams& ipNormal, const ImageParams& ipMouseOn, const ImageParams& ipMoving);

	virtual void SetTopLeftButtonImage( const ImageParams& ipNormal, const ImageParams& ipMouseOn, const ImageParams& ipMouseDown, const ImageParams& ipDisable = CImageParams());
	virtual void SetRightBottomButtonImage( const ImageParams& ipNormal, const ImageParams& ipMouseOn, const ImageParams& ipMouseDown, const ImageParams& ipDisable = CImageParams());

	virtual bool OnLeftButtonDown( int x, int y, int key);
};

} // namespace GXWINDOW
