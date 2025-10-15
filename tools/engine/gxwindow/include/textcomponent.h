#pragma once

namespace GXWINDOW { 

namespace INLINECLASS { class CTextDrawStatManager; class CTextStateMap; class CTextResMap; class CTextVector; class CScrollTextParam; class CScrollTextParamVector; }

typedef unsigned int TEXTALIGNFLAG;	// 文字对齐方式标记类型
enum TEXT_ALIGN
{
	TXA_LEFT = 0,
	TXA_CENTER = 1,
	TXA_RIGHT = 2,
	TXA_TOP = 4,
	TXA_MIDDLE = 8,
	TXA_BOTTOM = 16,
};

class GXWINDOW_EXPORTS_DLL CTextResourceManager : public CUncopyableObject
{
	INLINECLASS::CTextResMap*	m_ptrm;
public:
	CTextResourceManager();
	virtual ~CTextResourceManager();

	void SetText( int n, const wchar_t* szText);
	const wchar_t* GetText( int n) const;
public:
	static CTextResourceManager& GetInstance();
};

class GXWINDOW_EXPORTS_DLL CSimpleTextable
{
protected:
	COLOR		m_clrNormal;
	COLOR		m_clrNormalGradual;
	COLOR		m_clrSelected;
	COLOR		m_clrSelectedGradual;
	COLOR		m_clrMouseOn;
	COLOR		m_clrMouseOnGradual;
	COLOR		m_clrNormalEffect;
	COLOR		m_clrSelectedEffect;
	COLOR		m_clrMouseOnEffect;
	int			m_nFont;
	FONTEFEECTFLAG		m_uNormalEffect;
	FONTEFEECTFLAG		m_uSelectedEffect;
	FONTEFEECTFLAG		m_uMouseOnEffect;
public:
	CSimpleTextable();
	virtual ~CSimpleTextable();
public:
	virtual void SetTextColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual COLOR GetTextColor() const;
	virtual COLOR GetTextGradualColor() const;
	virtual void SetTextSelectedColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual COLOR GetTextSelectedColor() const;
	virtual COLOR GetTextSelectedGradualColor() const;
	virtual void SetTextMouseOnColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual COLOR GetTextMouseOnColor() const;
	virtual COLOR GetTextMouseOnGradualColor() const;
	virtual void SetTextFont( int font);
	virtual int GetTextFont() const;

	virtual FONTEFEECTFLAG GetNormalEffect() const;
	virtual FONTEFEECTFLAG GetSelectedEffect() const;
	virtual FONTEFEECTFLAG GetMouseOnEffect() const;
	virtual COLOR GetNormalEffectColor() const;
	virtual COLOR GetSelectedEffectColor() const;
	virtual COLOR GetMouseOnEffectColor() const;
};

class GXWINDOW_EXPORTS_DLL CTextDrawStatManager
{
	friend class CTextable;

	CTextDrawManager*	m_pdm;
	int					m_nFont;
	int					m_nNormal;
	int					m_nMouseIn;
	int					m_nDisable;
	COLOR				m_clrNomal;
	COLOR				m_clrNomalGradual;
	COLOR				m_clrMouseIn;
	COLOR				m_clrMouseInGradual;
	COLOR				m_clrDisable;
	COLOR				m_clrDisableGradual;
	COLOR				m_clrNormalEffect;
	COLOR				m_clrMouseInEffect;
	COLOR				m_clrDisableEffect;
	FONTEFEECTFLAG		m_uNormalEffect;
	FONTEFEECTFLAG		m_uMouseInEffect;
	FONTEFEECTFLAG		m_uDisableEffect;
public:
	CTextDrawStatManager();
	virtual ~CTextDrawStatManager();
public:

	void				SetFont( int n, CTextDrawManager* pdm);
	int					GetFont() const;

	void				SetNormalColor( COLOR clr, CTextDrawManager* pdm, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	COLOR				GetNormalColor() const;
	COLOR				GetNormalGradualColor() const;
	void				SetMouseInColor( COLOR clr, CTextDrawManager* pdm, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	COLOR				GetMouseInColor() const;
	COLOR				GetMouseInGradualColor() const;
	void				SetDisableColor( COLOR clr, CTextDrawManager* pdm, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	COLOR				GetDisableColor() const;
	COLOR				GetDisableGradualColor() const;

	FONTEFEECTFLAG GetNormalEffect() const;
	FONTEFEECTFLAG GetDisableEffect() const;
	FONTEFEECTFLAG GetMouseInEffect() const;
	COLOR GetNormalEffectColor() const;
	COLOR GetDisableEffectColor() const;
	COLOR GetMouseInEffectColor() const;
protected:
	inline virtual void	DrawNormal( CDraw& draw, CTextDrawManager* pdm, const wchar_t* text, int l, int t, int cx, int cy, int cw, int ch);
	inline virtual void	DrawMouseIn( CDraw& draw, CTextDrawManager* pdm, const wchar_t* text, int l, int t, int cx, int cy, int cw, int ch);
	inline virtual void	DrawDisable( CDraw& draw, CTextDrawManager* pdm, const wchar_t* text, int l, int t, int cx, int cy, int cw, int ch);
	inline virtual void	FreeNormalTextDraw( CTextDrawManager* pdm);
	inline virtual void	FreeMouseInTextDraw( CTextDrawManager* pdm);
	inline virtual void	FreeDisableTextDraw( CTextDrawManager* pdm);
	inline virtual void	FreeAllTextDraw( CTextDrawManager* pdm);
};

class GXWINDOW_EXPORTS_DLL CCustomeTextDrawStatManager
{
	INLINECLASS::CTextStateMap*		m_ptsm;
	int								m_nFont;
public:
	CCustomeTextDrawStatManager();
	virtual ~CCustomeTextDrawStatManager();
public:
	void SetFont( int n, CTextDrawManager* pdm);
	int  GetFont() const;

	void SetTextStateColor( int nIndex, COLOR clr, CTextDrawManager* pdm, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	COLOR GetTextStateColor( int nIndex) const;
	COLOR GetTextStateEffectColor( int nIndex) const;
	COLOR GetTextStateGradualColor( int nIndex) const;
	bool IsTextStateGradualColor( int nIndex) const;
	FONTEFEECTFLAG GetTextStateEffect( int nIndex) const;

	inline virtual bool DrawTextState( int nIndex, CDraw& draw, CTextDrawManager* pdm, const wchar_t* text, int l, int t, int cx, int cy, int cw, int ch);
	inline virtual void FreeTextDraw( int nIndex, CTextDrawManager* pdm);
	inline virtual void FreeAllTextDraw( CTextDrawManager* pdm);
	inline virtual bool IsTextState( int nIndex) const;
	virtual void Clear( CTextDrawManager* pdm);
};

class GXWINDOW_EXPORTS_DLL CTextable
{
	CTextDrawManager*	m_pdm;
	CTextDrawStatManager* m_ptdsm;
public:
	CTextable();
	virtual ~CTextable();
protected:
	INLINECLASS::CSimpleString*		m_text;
	bool							m_bDrawMouseIn;
	bool							m_bDrawDisable;
	int		m_nOffsetX;		// 水平偏移值, 用于控制居左、居中、居右
	int		m_nOffsetY;		// 垂直偏移值, 用于控制居左、居中、居右
public:
	virtual const wchar_t* GetComponentText() const;
	virtual void SetComponentText( const wchar_t* text);
	virtual void SetComponentText( int n);

	virtual void SetComponentFont( int nFont);
	virtual int GetComponentFont() const;

	virtual void SetComponentTextSimpleColor( COLOR clr);
	virtual void SetComponentTextColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual COLOR GetComponentTextColor() const;
	virtual FONTEFEECTFLAG GetComponentTextEffect() const;
	virtual COLOR GetComponentTextEffectColor() const;
	virtual bool IsTextGradualColor() const;
	virtual COLOR GetComponentTextGradualColor() const;

	virtual void SetComponentTextMouseInColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void ReleaseComponentTextMouseInColor();
	virtual COLOR GetComponentTextMouseInColor() const;
	virtual FONTEFEECTFLAG GetComponentTextMouseInEffect() const;
	virtual COLOR GetComponentTextMouseInEffectColor() const;
	virtual bool IsTextMouseInGradualColor() const;
	virtual COLOR GetComponentTextMouseInGradualColor() const;

	virtual void SetComponentTextDisableColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0);
	virtual void ReleaseComponentTextDisableColor();
	virtual COLOR GetComponentTextDisableColor() const;
	virtual FONTEFEECTFLAG GetComponentTextDisableEffect() const;
	virtual COLOR GetComponentTextDisableEffectColor() const;
	virtual bool IsTextDisableGradualColor() const;
	virtual COLOR GetComponentTextDisableGradualColor() const;

	//void SetTextDrawManager( CTextDrawManager& tdm);
	CTextDrawManager& GetTextDrawManager();
	void ReleaseTextDrawManager();
	virtual void FreeAllTextDraw();
public:
	virtual void TextDraw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn = false, bool bEnable = true);
};

class GXWINDOW_EXPORTS_DLL CTextComponent : public CComponent, public CTextable
{
	friend class CScrollTextComponent;

	bool			m_bMouseIn;
	bool			m_bAutoWidth;
	TEXTALIGNFLAG	m_taFlag;
public:
	CTextComponent();
	virtual ~CTextComponent();

	virtual void SetTextAlign( TEXTALIGNFLAG flag);
	virtual TEXTALIGNFLAG GetTextAlign() const;

	// 设置是否根据SetComponentText()设置的文本长度自动调节控件宽度
	virtual void SetAutoWidth( bool bAuto);
	virtual bool IsAutoWidth() const;

	virtual void SetComponentText(const wchar_t* text);
	virtual void SetComponentText(int n);
	virtual void SetSize(int w, int h);
protected:
	virtual void TextDraw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn);

	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void OnMouseEnter();
	virtual void OnMouseLeave();

private:
	void AdjustAlign();
};

class GXWINDOW_EXPORTS_DLL CScrollTextComponent : public CBKComponent, public CTextable, public CTimerNotify
{
	INLINECLASS::CTextVector*				m_ptexts;
	INLINECLASS::CScrollTextParamVector*	m_pstp;
	int										m_limite;
	int										m_space;
	int										m_interval;
	int										m_step;
	TIMERID									m_timerid;
	bool									m_loop;
public:
	CScrollTextComponent();
	virtual ~CScrollTextComponent();

public:
	void SetTextNumberLimite( int n);
	int  GetTextNumberLimite() const;

	void SetSpaceBetweenScrollTexts( int n);
	int  GetSpaceBetweenScrollTexts() const;

	void SetScrollInterval( int n);
	int  GetScrollInterval() const;

	void SetScrollStep( int n);
	int  GetScrollStep() const;

	void SetLoopScroll( bool bLoop = true);
	bool IsLoopScroll() const;

	void InsertText( wchar_t* text);
	void EraseText( int n);	// 正在滚动中的文本不能删除
	void ClearText();		// 停止滚动并删除文本
	int  GetTextCount() const;
	const wchar_t* GetText( int n) const;

	void Start( int n = 0);
	void Stop();

	virtual void FreeAllTextDraw();

protected:
	virtual void OnTimer( const TIMERID& nid);
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

private:
	void SetScrollTextParam( int n, INLINECLASS::CScrollTextParam& stp);
};


} // namespace GXWINDOW { 