#pragma once

namespace GXWINDOW { 

namespace INLINECLASS { class CAnimateIconVector; }

class GXWINDOW_EXPORTS_DLL CIconImageManager : public CUncopyableObject 
{
	CIconImageManager();
	~CIconImageManager();
public:
	class GXWINDOW_EXPORTS_DLL CAbstractImplement
	{
	public:
		CAbstractImplement();
		virtual ~CAbstractImplement();
	public:
		virtual void DrawIcon32( CDraw& draw, int nIcon, int l, int t, int cx, int cy, int cw, int ch) = 0;
		virtual void DrawIcon16( CDraw& draw, int nIcon, int l, int t, int cx, int cy, int cw, int ch) = 0;
		virtual void DrawEmotion( CDraw& draw, int nPack, int nEmotion, int l, int t, int cw, int ch, int sx, int sy, int sw, int sh) = 0;
		virtual void DrawTreeIcon( CDraw& draw, int nIcon, int l, int t, int cx, int cy, int cw, int ch) {};
	};
private:
	CAbstractImplement*		m_impl;
public:
	void SetImplement( CAbstractImplement& impl);
	void DrawIcon32( CDraw& draw, int nIcon, int l, int t, int cx, int cy, int cw, int ch);
	void DrawIcon16( CDraw& draw, int nIcon, int l, int t, int cx, int cy, int cw, int ch);
	void DrawEmotion( CDraw& draw, int nPack, int nEmotion, int l, int t, int cw, int ch, int sx, int sy, int sw, int sh);
	void DrawTreeIcon( CDraw& draw, int nIcon, int l, int t, int cx, int cy, int cw, int ch);
public:
	static CIconImageManager& GetInstance();
};

class GXWINDOW_EXPORTS_DLL CAnimateIconComponent : public CAbstractAnimateImageComponent
{
	INLINECLASS::CAnimateIconVector*	m_aiv;
public:
	CAnimateIconComponent();
	virtual ~CAnimateIconComponent();
public:
	virtual int GetCount() const;
	virtual int GetAt( int nIndex) const;
	virtual void SetAt( int nIndex, int nIcon);
	virtual void RemoveAt( int nIndex);
	virtual void RemoveAll();
	virtual void Insert( int nIcon);
protected:
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
};

class GXWINDOW_EXPORTS_DLL CIconComponent : public CComponent
{
	int				m_nImage;
public:
	CIconComponent();
	virtual ~CIconComponent();
protected:
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
public:
	virtual void SetIcon( int nIcon);
	virtual int GetIcon() const;
};

class GXWINDOW_EXPORTS_DLL CEmotionComponent : public CComponent
{
	int				m_nPack;
	int				m_nEmotion;
	int				m_nShowWidth;
	int				m_nShowHeight;
	int				m_nIconWidth;
	int				m_nIconHeight;
	float			m_fXScale;
	float			m_fYScale;
public:
	CEmotionComponent();
	virtual ~CEmotionComponent();
protected:
	virtual void Draw(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
public:
	virtual void SetEmotion( int nPack, int nEmotion);
	virtual int GetEmotionPack() const;
	virtual int GetEmotion() const;

	virtual void SetIconSize( int w, int h);
	virtual int GetIconWidth() const;
	virtual int GetIconHeight() const;

	virtual void SetShowSize( int w, int h);
	virtual int GetShowWidth() const;
	virtual int GetShowHeight() const;
};

} // namespace GXWINDOW { 