#pragma	once

namespace GXWINDOW { 

namespace INLINECLASS { 
class CTextAreaLineList; 
struct TextAreaDispatchMessage; 
class CEditElementContainer; 
class CComplexEditElement; 
class CTextAreaLine; 
class CLineObject; 
class CSimpleString; 
class CTextAreaButtonClickNotifySet;
class CTextAreaTextClickNotifySet;
class CTextAreaElementMouseEventNotifySet;
class CTextAreaFocusNotifySet;
class CTextVector;
class CScrollTextAreaParam;
class CScrollTextAreaParamVector;
class CCustomTextPiece;

} // namespace INLINECLASS { 

class GXWINDOW_EXPORTS_DLL CTextAreaBackGroundEffect
{
	friend class CTextArea;
	friend class INLINECLASS::CTextAreaLine;
public:
	CTextAreaBackGroundEffect() {};
	virtual ~CTextAreaBackGroundEffect() {};
protected:
	// 同一个特效会被绘制在多处, 所以由nBackgroundEffect和nInstance来确定一个特效
	virtual void DrawBackGroundEffect( CDraw& draw, int nBackgroundEffect, int nInstance, int l, int t, int cx, int cy, int cw, int ch) = 0;
	virtual void ReleaseBackGroundEffect( int nBackgroundEffect, int nInstance) = 0;
};

class GXWINDOW_EXPORTS_DLL CTextAreaProperties
{
	int								m_nFont;
	COLOR							m_clrText;
	COLOR							m_clrTextGradual;
	COLOR							m_clrSelect;
	COLOR							m_clrSelectGradual;
	COLOR							m_clrBKSelect;
	
	COLOR							m_clrTextEffect;
	COLOR							m_clrSelectEffect;
	unsigned char					m_uTextEffect;
	unsigned char					m_uSelectEffect;

	int											m_nLineSpace;
	bool										m_bSingleLine;

	ImageParams			m_ipCursor;
	int					m_nCursorCycle;
	int					m_nCursorWidth;
	int					m_nCursorHeight;
	int					m_nMinHeight;

	bool				m_bAllowSelected;
	bool				m_bPassword;

	static ImageParams	s_DefaultCursorImage;
	static int			s_nDefaultCursorWidth;
	static int			s_nDefaultCursorHeight;
	//是否启动单词自动换行功能 add by yzp 2011-04-09
	static bool s_bEnableWordBreak;
public:
	CTextAreaProperties();
	virtual ~CTextAreaProperties();
public:
	static void SetDefaultCursor( const ImageParams& ip, int w, int h);
	static int GetStringWidth( int nFont, const wchar_t* szText);

	static const ImageParams& GetDefaultCursor();
	static int GetDefaultCursorWidth();
	static int GetDefaultCursorHeight();
	static void SetEnableWordBreak(bool b) {s_bEnableWordBreak = b;}
	static bool GetEnableWordBreak() {return s_bEnableWordBreak;}
public:
	virtual void SetDefaultFont( int font);
	virtual int GetDefaultFont() const;
	virtual void SetDefaultTextColor( COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bUseGradualColor = false, COLOR clrGradual = 0);
	virtual COLOR GetDefaultTextColor() const;
	virtual COLOR GetDefaultTextGradualColor() const;
	virtual void SetDefaultSelectColor( COLOR clrText, COLOR clrBackground, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bUseGradualColor = false, COLOR clrGradual = 0);
	virtual COLOR GetDefaultSelectTextColor() const;
	virtual COLOR GetDefaultSelectTextGradualColor() const;
	virtual COLOR GetDefaultSelectBackgroundColor() const;
	virtual COLOR GetDefaultEffectColor() const;
	virtual COLOR GetDefaultSelectEffectColor() const;
	virtual FONTEFEECTFLAG GetDefaultEffectType() const;
	virtual FONTEFEECTFLAG GetDefaultSelectEffectType() const;

	virtual void SetSingleLine( bool b = true);
	virtual bool IsSingleLine() const;
	virtual void SetLineSpace( int n);
	virtual int GetLineSpace() const;

	virtual bool IsCursorShow() const;
	virtual void HideCursor();
	virtual void ShowCursor( const ImageParams& ip);
	virtual const ImageParams& GetCursor() const;
	virtual void SetCursorCycle( int n);
	virtual int GetCursorCycle() const;
	virtual void SetCursorSize( int w, int h);
	virtual int GetCursorWidth() const;
	virtual int GetCursorHeight() const;

	virtual bool IsSelectAllow() const;
	virtual void SetSelectAllow( bool b = true);

	virtual void SetMinWidthHeight( int h);
	virtual int GetMinWidthHeight() const;

	virtual void SetPassword( bool b = true);
	virtual bool IsPassword() const;
protected:
	virtual void RegisterCursorImage();
	virtual void FreeCursorImage();
};

class GXWINDOW_EXPORTS_DLL CTextAreaComponentAllocator : public CUncopyableObject
{
public:
	CTextAreaComponentAllocator();
	virtual ~CTextAreaComponentAllocator();
public:
	virtual CEditAnimateIconElement* NewAnimateImageComponent();
	virtual CEditIconElement* NewImageComponent();
	virtual CBlendEditElement* NewEditElement( CTextDrawManager& tdm);
	virtual CBlendEditElement* NewComplexElement( CTextDrawManager& tdm);
	virtual CEditButtonElement* NewButtonElement( CTextDrawManager& tdm);
	virtual CEditEmotionElement* NewEmotionComponent();
	virtual CCustomTextPiece*	NewCustomTextPiece();
	virtual void DeleteComponent( CComponent* pc);
public:
	static CTextAreaComponentAllocator& GetInstance();
};

class GXWINDOW_EXPORTS_DLL CTextAreaButtonClickNotify : public CComponentNotify
{
	friend class CTextArea;
public:
	CTextAreaButtonClickNotify();
	virtual ~CTextAreaButtonClickNotify();
protected:
	virtual void OnTextAreaButtonClick( CTextArea& ta, const wchar_t* szNotify) = 0;
};

class GXWINDOW_EXPORTS_DLL CTextAreaTextClickNotify : public CComponentNotify
{
	friend class CTextArea;
public:
	CTextAreaTextClickNotify();
	virtual ~CTextAreaTextClickNotify();
protected:
	virtual void OnTextAreaTextClick( CTextArea& a, const wchar_t* szInfo) = 0;
	virtual void OnTextAreaTextRightClick( CTextArea& a, const wchar_t* szInfo) {}
};

class GXWINDOW_EXPORTS_DLL CTextAreaElementMouseEventNotify : public CComponentNotify
{
	friend class CTextArea;
public:
	CTextAreaElementMouseEventNotify();
	virtual ~CTextAreaElementMouseEventNotify();
protected:
	virtual void OnTextAreaElementLeftButtonDown( CTextArea& ta, const wchar_t* szInfo);
	virtual void OnTextAreaElementRightButtonDown( CTextArea& ta, const wchar_t* szInfo);
};

class GXWINDOW_EXPORTS_DLL CTextAreaFocusNotify : public CComponentNotify
{
	friend class CTextArea;
public:
	CTextAreaFocusNotify();
	virtual ~CTextAreaFocusNotify();
protected:
	virtual void OnSetKeyFocus( CTextArea& ta);
	virtual void OnLostKeyFocus( CTextArea& ta);
};

class GXWINDOW_EXPORTS_DLL CTextArea : public CComponent , public CTextAreaProperties, public CKeyInputNotify, public CButtonClickNotify, public CTimerNotify, public CComponentMouseEventNotify
{
	friend struct INLINECLASS::TextAreaDispatchMessage;
	friend class INLINECLASS::CComplexEditElement;
	friend class CBlendEditElement;
	friend class CEditButtonElement;
	friend class CScrollSingleLineTextArea;

	CTextDrawManager*						m_tdm;
	CTextAreaComponentAllocator*			m_allocator;
	INLINECLASS::CTextAreaLineList*			m_tall;
	CTextAreaBackGroundEffect*				m_pBackgroundEffectDraw;

	bool				m_bCursorShowing;
	TIMERID				m_cursortimer;

	bool				m_bFucosed;
	bool				m_bSelectNow;
	bool				m_bSureSelected;
	//bool				m_bNotTransparent;
	int					m_nSelectLastX;
	int					m_nSelectLastY;

	int					m_nTotalWidth;

	CComponent*					m_pLastMouseIn;
	INLINECLASS::CTextAreaButtonClickNotifySet*				m_ButtonNotifySet;
	INLINECLASS::CTextAreaTextClickNotifySet*				m_TextNotifySet;
	INLINECLASS::CTextAreaElementMouseEventNotifySet*		m_MouseNotifySet;
	INLINECLASS::CTextAreaFocusNotifySet*					m_FocusNotifySet;

	bool				m_bAutoWidthForSingle;			//yzp
protected:
	int							m_nCursorX;
	int							m_nCursorY;

	CAbstarctTextDecoder*					m_decoder;
	INLINECLASS::CEditElementContainer*		m_eec;
private:
	// 给CScrollSingleLineTextArea使用
	CTextArea( bool bHideCursor, CAbstarctTextDecoder& decoder = CFormatTextDecoder(), CTextAreaComponentAllocator& allocator = CTextAreaComponentAllocator::GetInstance());
public:
	CTextArea( CAbstarctTextDecoder& decoder = CFormatTextDecoder(), CTextAreaComponentAllocator& allocator = CTextAreaComponentAllocator::GetInstance());
	virtual ~CTextArea();
protected:
	virtual void RelayoutView( bool bFullRelayout = true);
	virtual void CheckCursorPosition();

	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void DrawCursor( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	virtual void OnMouseLeave();
	virtual void OnTimer( const TIMERID& nid);
	virtual bool OnKeyInput( UINT uMsg, WPARAM wParam, LPARAM lParam);
	virtual void OnButtonClick( CAbstractButton& c);
	virtual void OnLeftButtonDown( CComponent& c, int x, int y, int key);
	virtual void OnRightButtonDown( CComponent& c, int x, int y, int key);

	virtual bool OnSetKeyFocus();
	virtual void OnLostKeyFocus();

	virtual void OnComponentShow();
	virtual void OnComponentHide();

	void EnsureViewCursor();
	void CancelAllSelect();

private:
	void SetSize( int w, int h);

	void NewLineElements( int& x, int y, int lh, INLINECLASS::CTextAreaLine& tal, const INLINECLASS::CLineObject& lo, bool bPassword = false);
	void NewTextElement( int& nIndex, int& x, int y, int lh, INLINECLASS::CSimpleString& str, CDecoderTextPiece& dtp, INLINECLASS::CTextAreaLine& tal);
	void DrawOneLine( INLINECLASS::CTextAreaLine& tal, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	void DrawOneLineCompoment( CComponent& com, INLINECLASS::CTextAreaLine& tal, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
	void ResetAllSelect( int x, int y, bool bSetBuffer = false);
	void OnMouseSetCursorPosition( int x, int y);
	void EnsureViewPos( int x, int y);
public:
	CAbstarctTextDecoder& GetTextDecoder();

	virtual void SetWidth( int w);
	virtual void AppendText( const wchar_t* szText);
	virtual void AppendText( int n);
	virtual void AppendText( CDecoderTextPieceList& tpl);
	virtual void SetCursorPos( int x, int y);
	virtual void SetCursorCharPos( int row, int col);
	virtual void GetCursorCharPos( int& row, int& col) const;
	virtual void SetMinWidthHeight( int h);
	virtual void SetPassword( bool b = true);
	virtual int GetLineCount() const;
	virtual int EraseLines( int nStart, int nCount); // 返回值为删除行的高度

	virtual bool Encode( CAbstarctTextEncoder& fte);

	virtual bool EraseAll();

	virtual void ShowCursor();
	virtual void ShowCursor( const ImageParams& ip);
	virtual void HideCursor();

	virtual void SetEnable( bool bEnable = true);
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
	bool CopyToClipboard();
	bool IsHasSelect();

	//virtual void SetTransparent( bool b);
	//virtual bool IsTransparent() const;
	virtual bool IsPointInMsgTransparent( int x, int y) const;

	void TextAreaButtonClickNotifyAdd( CTextAreaButtonClickNotify& tancn);
	void TextAreaButtonClickNotifyRemove( CTextAreaButtonClickNotify& tancn);
	void TextAreaTextClickNotifyAdd( CTextAreaTextClickNotify& tatcn);
	void TextAreaTextClickNotifyRemove( CTextAreaTextClickNotify& tatcn);
	void TextAreaElementMouseEventNotifyAdd( CTextAreaElementMouseEventNotify& taimen);
	void TextAreaElementMouseEventNotifyRemove( CTextAreaElementMouseEventNotify& taimen);
	void TextAreaFocusNotifyAdd( CTextAreaFocusNotify& tfn);
	void TextAreaFocusNotifyRemove( CTextAreaFocusNotify& tfn);

	void SetAutoWidth(bool b = true) { m_bAutoWidthForSingle = b; }//yzp

	int GetTotalWidth() const;
	int GetAllLinesHeight() const;

	void SetAllSelected();

	// 设光标到最前
	virtual void SetCursorToBegin();

	// 设光标到最后
	virtual void SetCursorToEnd();

	void SetExistTextAlpha( unsigned char alpha); // 给已经存在的文字设置透明度

	// 判读是否有输入焦点
	bool IsFocused() const;

public:
	virtual void AppendText( const wchar_t* szText, int backgroundEffectId);
	void SetTextAreaBackGroundEffect( CTextAreaBackGroundEffect& tabge);
	void RemoveTextAreaBackGroundEffect( CTextAreaBackGroundEffect& tabge);
};

class GXWINDOW_EXPORTS_DLL CCombinationTextAreaAllocator : public CUncopyableObject
{
	CAbstarctTextDecoder*	m_Decoder;
public:
	CCombinationTextAreaAllocator( CAbstarctTextDecoder& decoder = CFormatTextDecoder());
	virtual ~CCombinationTextAreaAllocator();
public:
	virtual CScrollPanel* NewScrollPanel();
	virtual CTextArea* NewTextArea();
	virtual void DeleteComponent( CComponent* pc);
protected:
	CAbstarctTextDecoder& GetTextDecoder();
public:
	static CCombinationTextAreaAllocator& GetInstance();
};

class GXWINDOW_EXPORTS_DLL CCombinationTextArea : public CUncopyableObject
{
	CCombinationTextAreaAllocator*	m_alloc;
	
	CScrollPanel*	m_sp;
	CTextArea*		m_ta;
public:
	CCombinationTextArea( CCombinationTextAreaAllocator& alloc = CCombinationTextAreaAllocator::GetInstance());
	virtual ~CCombinationTextArea();
public:
	CScrollPanel& GetScrollPanel();
	CTextArea& GetTextArea();
public:
	virtual void SetSize( int w, int h);
	virtual void SetCombinationSize( int w, int h);	// 避开ta的限制设置整个CombinationTextArea控件的大小(包括sp和ta)
	virtual void SetPos( int x, int y);
	virtual void ShowCursor( const ImageParams& ip);
	virtual void AppendText( const wchar_t* szText);
	virtual void AppendText( const wchar_t* szText, int nBackgroundEffect);
	virtual void SetSelectAllow( bool b = true);
	virtual void SetSingleLine( bool b = true);
	virtual void SetViewOnly( bool bViewOnly = true);
	virtual bool IsViewOnly() const;
public:
	operator CComponent*();
	operator CComponent&();
	CComponent* operator&();

	void SetComponentName( const wchar_t* name);
	const wchar_t* GetComponentName() const;
};

class GXWINDOW_EXPORTS_DLL CScrollSingleLineTextArea : public CBKComponent, public CTextAreaProperties, public CTimerNotify
{
	CTextAreaElementMouseEventNotify*			m_taimen;
	CTextAreaButtonClickNotify*					m_tabcn;
	CTextAreaTextClickNotify*					m_tatcn;

	CAbstarctTextDecoder*						m_decoder;
	INLINECLASS::CTextVector*					m_ptexts;
	INLINECLASS::CScrollTextAreaParamVector*	m_pstap;
	int											m_limite;
	int											m_space;
	int											m_interval;
	int											m_step;
	TIMERID										m_timerid;
	bool										m_loop;
	bool										m_bOneTextMode; // 特殊模式, 设为true时, 当只存在一条滚动文本, 将判断文本长度, 如长度小于控件则不滚动直接显示
public:
	CScrollSingleLineTextArea( CAbstarctTextDecoder& decoder = CFormatTextDecoder());
	virtual ~CScrollSingleLineTextArea();

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

	void InsertText( const wchar_t* text);
	void SetText( int n, const wchar_t* text);
	void EraseText( int n);				// 正在滚动中的文本不能删除
	void ClearText();					// 停止滚动并删除文本
	int  GetTextCount() const;
	const wchar_t* GetText( int n) const;

	void Start( int n = 0);
	void Stop();
	void Reset( int n = 0);

	void SetOneTextMode( bool bOneTextMode);
	bool IsOneTextMode() const;

	virtual void FreeAllTextDraw();
	virtual void SetTransparent( bool bTrans = true);

	void TextAreaButtonClickNotifyAdd( CTextAreaButtonClickNotify& tabcn);
	void TextAreaButtonClickNotifyRemove( CTextAreaButtonClickNotify& tabcn);
	void TextAreaTextClickNotifyAdd( CTextAreaTextClickNotify& tatcn);
	void TextAreaTextClickNotifyRemove( CTextAreaTextClickNotify& tatcn);
	void TextAreaElementMouseEventNotifyAdd( CTextAreaElementMouseEventNotify& taimen);
	void TextAreaElementMouseEventNotifyRemove( CTextAreaElementMouseEventNotify& taimen);

	bool IsShowEmpty();
	bool IsCanRoll(int n = 0);
protected:
	virtual void OnTimer( const TIMERID& nid);
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);

	virtual bool OnRightButtonDown(int x, int y, int key);
	virtual bool OnLeftButtonDown(int x, int y, int key);
private:
	void SetScrollTextAreaParam( int n, INLINECLASS::CScrollTextAreaParam& stap);
};

} // namespace GXWINDOW { 