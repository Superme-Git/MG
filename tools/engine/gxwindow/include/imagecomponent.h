#pragma once 

namespace GXWINDOW { 

namespace INLINECLASS { class CAnimateImageVector; }

class GXWINDOW_EXPORTS_DLL CImageComponent : public CComponent
{
	bool		m_bMouseIn;
	CImageable	m_stateip;
public:
	CImageComponent();
	virtual ~CImageComponent();
protected:
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void OnMouseEnter();
	virtual void OnMouseLeave();

	virtual void OnComponentShow();
	virtual void OnComponentHide();
public:
	virtual void SetViewAsMouseIn( bool bin = true);
public:
	virtual void SetComponentImageNormal( const ImageParams& ip);
	virtual const ImageParams& GetComponentImageNormal() const;
	virtual void RemoveComponentImageNormal();

	virtual void SetComponentImageMouseIn( const ImageParams& ip);
	virtual const ImageParams& GetComponentImageMouseIn() const;
	virtual void RemoveComponentImageMouseIn();

	virtual const ImageParams& GetComponentImageDisable() const;
	virtual void SetComponentImageDisable( const ImageParams& ip);
	virtual void RemoveComponentImageDisable();
};

class GXWINDOW_EXPORTS_DLL CAbstractAnimateImageComponent : public CComponent, public CTimerNotify
{
	int			m_nInterval;
	TIMERID		m_nTimeID;
	int			m_nCurrentIndex;
	bool		m_bStopAtFrame;
public:
	CAbstractAnimateImageComponent();
	virtual ~CAbstractAnimateImageComponent();
public:
	virtual void SetInterval( int ms);
	virtual int GetInterval() const;

	virtual int GetCount() const = 0;
	virtual int GetCurrentIndex() const;

	virtual void StopAt( int frame);	// 使动画停止在指定帧,若指定帧不在动画集合范围内,则恢复为播放动画
protected:
	virtual void OnTimer( const TIMERID& nid);
};

class GXWINDOW_EXPORTS_DLL CAnimateImageComponent : public CAbstractAnimateImageComponent
{
	INLINECLASS::CAnimateImageVector*	m_aiv;
public:
	CAnimateImageComponent();
	virtual ~CAnimateImageComponent();
public:
	virtual int GetCount() const;
	virtual const ImageParams& GetAt( int nIndex) const;
	virtual ImageParams& GetAt( int nIndex);
	virtual void RemoveAt( int nIndex);
	virtual void RemoveAll();
	virtual void Insert( const ImageParams& ip);
protected:
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	virtual void OnComponentShow();
	virtual void OnComponentHide();
};

class GXWINDOW_EXPORTS_DLL CTileImageComponent : public CComponent
{
	bool			m_bDrawDown;
	bool			m_bMouseIn;
	CImagesFill		m_ifNormal;
	CImagesFill		m_ifMouseIn;
	CImagesFill		m_ifMouseDown;
public:
	CTileImageComponent();
	virtual ~CTileImageComponent();
protected:
	bool IsMouseDown() const;
	bool IsMouseIn() const;

	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void OnMouseEnter();
	virtual void OnMouseLeave();

	virtual void OnComponentShow();
	virtual void OnComponentHide();
public:
	virtual bool OnLeftButtonUp( int x, int y, int key);
	virtual bool OnLeftButtonDown( int x, int y, int key);

	virtual void SetViewAsMouseIn( bool bIn = true);
	virtual void SetViewAsMouseDonw( bool bDown = true);

	virtual CImagesFill& GetTileImageNormal();
	virtual CImagesFill& GetTileImageMouseIn();
	virtual CImagesFill& GetTileImageMouseDown();
};

} //namespace GXWINDOW { 