#pragma once

namespace GXWINDOW { 

namespace INLINECLASS {	class CTextPieceVector; struct TextAreaDispatchMessage;  }

class CAbstarctTextDecoder;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 

class GXWINDOW_EXPORTS_DLL CEditElementable 
{
	CTextDrawManager*	m_pdm;
protected:
	CTextDrawManager& GetTextDrawManager();
public:
	CEditElementable( CTextDrawManager& tdm);
	virtual ~CEditElementable();
};

class GXWINDOW_EXPORTS_DLL CEditElement : public CComponent, public CEditElementable
{
	friend class CBlendEditElement;

	int					m_nText;
public:
	CEditElement( CTextDrawManager& tdm);
	virtual ~CEditElement();
protected:
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
public:
	virtual void SetTextIndex( int nText);
	virtual int GetTextIndex() const;
};

class GXWINDOW_EXPORTS_DLL CEditElementAllocator : public CUncopyableObject
{
public:
	CEditElementAllocator();
	virtual ~CEditElementAllocator();
public:
	virtual CEditElement* NewEditElement( CTextDrawManager& tdm);
	virtual void DeleteEditElement( CEditElement* ee);
};

enum
{
	EE_SELECT_START		= 0,
	EE_SELECT_END		= 65536,
};

class GXWINDOW_EXPORTS_DLL CBlendEditElement : public CEditElement
{
	friend struct INLINECLASS::TextAreaDispatchMessage;

	CEditElementAllocator*			m_Alloc;
	INLINECLASS::CSimpleString*		m_ss;
	INLINECLASS::CSimpleString*		m_ssselect;
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

	int			m_nSelectStart;
	int			m_nSelectEnd;
	int			m_nSelectCharStart;

	CEditElement*	m_select;
	CComponent*		m_pLastMouseOn;

	INLINECLASS::CSimpleString*		m_ssinfo;
public:
	CBlendEditElement( CTextDrawManager& tdm);
	CBlendEditElement( CTextDrawManager& tdm, CEditElementAllocator& alloc);
	virtual ~CBlendEditElement();
private:
	void Initialize( CEditElementAllocator& alloc);
	void CheckSizeByText();
	void ResetNormalElement();
	void ResetSelectElement();
	void CheckSelectString( int& s, int& w);
private:
	void SetSize( int w, int h);
	void SetTextIndex( int nText);
	int GetTextIndex() const;
public:
	virtual void SetText( const wchar_t* text);
	virtual const wchar_t* GetText() const;
	virtual int GetCharCount() const;
	virtual void SetFont( int font);
	virtual int GetFont() const;

	virtual void SetTextColor( COLOR clr, COLOR clrGradual, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0);
	virtual COLOR GetTextColor() const;
	virtual COLOR GetTextGradualColor() const;
	virtual void SetSelectColor( COLOR clr, COLOR clrGradual, COLOR clrbk, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0);
	virtual COLOR GetSelectColor() const;
	virtual COLOR GetSelectGradualColor() const;
	virtual COLOR GetSelectBKColor() const;

	virtual COLOR GetTextEffectColor() const;
	virtual COLOR GetSelectEffectColor() const;
	virtual FONTEFEECTFLAG GetTextEffectType() const;
	virtual FONTEFEECTFLAG GetSelectEffectType() const;

	virtual void SetSelect( int s = EE_SELECT_START, int e = EE_SELECT_END);
	virtual void SetSelectNone();
	virtual const wchar_t* GetSelectString() const;
	virtual int GetSelectStringLength() const;

	virtual int GetSelectCharStart() const;

	virtual const wchar_t* GetInfo() const;
	virtual int GetInfoCount() const;
	virtual void SetInfo( const wchar_t* szInfo);

protected:
	virtual void OnMouseLeave();
	virtual void Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch);
public:
	virtual bool OnLeftButtonDown( int x, int y, int key);
	virtual bool OnLeftButtonUp( int x, int y, int key);
//	virtual bool OnLeftButtonDoubleClick( int x, int y, int key);
	
	virtual bool OnRightButtonDown( int x, int y, int key);
	virtual bool OnRightButtonUp( int x, int y, int key);
//	virtual bool OnRightButtonDoubleClick( int x, int y, int key);

	virtual bool OnMouseMove( int x, int y, int key);
	virtual bool OnMouseWheel( int x, int y, int key);
};

enum { EBE_DEFAULT_BUTTON_WIDTH = 16, EBE_DEFAULT_BUTTON_HEIGHT = 16};
class GXWINDOW_EXPORTS_DLL CEditButtonElement : public CAbstractButton, public CEditElementable
{
	int					m_nImage;
	int					m_nImageMouseOn;
	int					m_nImageDown;
	int					m_nImageWidth;
	int					m_nImageHeight;
	int					m_nTextWidth;
	int					m_nTextHeight;

	int					m_nTextNormal;
	int					m_nTextMouseOn;
	INLINECLASS::CSimpleString*		m_textNotify;
	INLINECLASS::CSimpleString*		m_mouseNotify;
public:
	CEditButtonElement( CTextDrawManager& tdm);
	virtual ~CEditButtonElement();
private:
	void SetSize( int w, int h);
	void CheckSize();
protected:
	virtual void DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown);
public:
	virtual void SetText( const wchar_t* szText, int nFont, COLOR clrNormal, COLOR clrNormalGradual, COLOR clrMouseOn, COLOR clrMouseOnGradual, FONTEFEECTFLAG fefNormal = FET_NONE, COLOR clrNormalEffect = 0, FONTEFEECTFLAG fefMouseOn = FET_NONE, COLOR clrMouseOnEffect = 0);
	virtual void SetImage( int ip, int nMouseOn, int nDown);
	virtual int GetImage() const;
	virtual int GetMouseOnImage() const;
	virtual int GetMouseDownImage() const;
	virtual void SetButtonSize( int w = EBE_DEFAULT_BUTTON_WIDTH, int h = EBE_DEFAULT_BUTTON_HEIGHT);
	virtual int GetButtonWidth() const;
	virtual int GetButtonHeight() const;
	virtual void SetNotifyString( const wchar_t* szText);
	virtual const wchar_t* GetNotifyString() const;
	virtual void SetMouseNotifyString( const wchar_t* szText);
	virtual const wchar_t* GetMouseNotifyString() const;
};

class GXWINDOW_EXPORTS_DLL CEditIconElement : public CIconComponent
{
	INLINECLASS::CSimpleString*		m_textNotify;
public:
	CEditIconElement();
	virtual ~CEditIconElement();
public:
	virtual void SetNotifyString( const wchar_t* szText);
	virtual const wchar_t* GetNotifyString() const;

	virtual bool OnLeftButtonDown( int x, int y, int key);

};

class GXWINDOW_EXPORTS_DLL CEditAnimateIconElement : public CAnimateIconComponent
{
	INLINECLASS::CSimpleString*		m_textNotify;
public:
	CEditAnimateIconElement();
	virtual ~CEditAnimateIconElement();
public:
	virtual void SetNotifyString( const wchar_t* szText);
	virtual const wchar_t* GetNotifyString() const;

	virtual bool OnLeftButtonDown( int x, int y, int key);
};

class GXWINDOW_EXPORTS_DLL CEditEmotionElement : public CEmotionComponent
{
	INLINECLASS::CSimpleString*		m_textNotify;
public:
	CEditEmotionElement();
	virtual ~CEditEmotionElement();
public:
	virtual void SetNotifyString( const wchar_t* szText);
	virtual const wchar_t* GetNotifyString() const;

	virtual bool OnLeftButtonDown( int x, int y, int key);

};

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 

enum DTP_TYPE
{
	DTPT_DEFAULT_TEXT = 0,
	DTPT_FORMAT_TEXT,
	DTPT_STATIC_IMAGE,
	DTPT_ANIMATE_IMAGE,
	DTPT_COMPLEX,
	DTPT_BUTTON,
	DTPT_RETURN,
	DTPT_EMOTION_IMAGE,
	DTPT_CUSTOM_PIECE,
};


class GXWINDOW_EXPORTS_DLL CDecoderTextPiece
{
	DTP_TYPE		m_type;
	INLINECLASS::CSimpleString*		m_textTipInfo;

	union
	{
		INLINECLASS::CSimpleString*		m_textDeault;
		struct 
		{
			INLINECLASS::CSimpleString*		m_textFormat;
			int								m_nFormatFont;
			int								m_nTextFormatID;
			COLOR							m_clrFormatText;
			COLOR							m_clrFormatTextGradual;	// 渐变色, 按m_clrFormatText->m_clrFormatTextGradual的顺序由上至下渐变, 如两者一样则无渐变效果
			COLOR							m_clrFormatSelect;
			COLOR							m_clrFormatSelectGradual;
			COLOR							m_clrFormatBKSelect;
			COLOR							m_clrFormatTextEffect;
			COLOR							m_clrFormatSelectEffect;
			FONTEFEECTFLAG					m_fefFormatText;
			FONTEFEECTFLAG					m_fefFormatSelect;
			INLINECLASS::CSimpleString*		m_textFormatNotify;
		};
		struct 
		{
			int		m_nStaticImage;
			int		m_nStaticWidth;
			int		m_nStaticHeight;
			INLINECLASS::CSimpleString*		m_imageMouseNotify;
		};
		struct 
		{
			int					m_nAnimateStart;
			int					m_nAnimateEnd;
			int					m_nAnimateWidth;
			int					m_nAnimateHeight;
			int					m_nAnimateInterval;
			INLINECLASS::CSimpleString*		m_animateMouseNotify;
		};
		struct 
		{
			INLINECLASS::CSimpleString*		m_textButton;
			int								m_nButtonFont;
			int								m_nButtonFormatID;
			COLOR							m_clrButtonText;
			COLOR							m_clrButtonTextGradual;
			COLOR							m_clrButtonMouseOn;
			COLOR							m_clrButtonMouseOnGradual;
			COLOR							m_clrButtonTextEffect;
			COLOR							m_clrButtonMouseOnEffect;
			FONTEFEECTFLAG					m_fefButtonText;
			FONTEFEECTFLAG					m_fefButtonMouseOn;
			int								m_nButtonImage;
			int								m_nButtonMouseOnImage;
			int								m_nButtonDownImage;
			int								m_nButtonImageWidth;
			int								m_nButtonImageHeight;
			INLINECLASS::CSimpleString*		m_textNotify;
			INLINECLASS::CSimpleString*		m_buttonMouseNotify;
		};
		struct  
		{
			int		m_nPack;
			int		m_nEmotion;
			int		m_nShowWidth;
			int		m_nShowHeight;
			int		m_nIconWidth;
			int		m_nIconHeight;
			INLINECLASS::CSimpleString*		m_emotionMouseNotify;
		};
		struct //CustomPiece
		{
			int		m_nWidth;
			int		m_nHeight;
			CCustomTextPieceAbstractImpl*	m_customimpl;
			INLINECLASS::CSimpleString*			m_customMouseNotify;
		};
	};
public:
	CDecoderTextPiece( const wchar_t* szText); // DTPT_DEFAULT_TEXT
	CDecoderTextPiece( const wchar_t* szText, int font, int textformatid, COLOR clrText, COLOR clrTextGradual, COLOR clrSelect, COLOR clrSelectGradual, COLOR clrBKSelect, const wchar_t* szInfo = NULL, bool bComplex = false, const wchar_t* szTip = NULL, FONTEFEECTFLAG texteffect = FET_NONE, COLOR clrTextEffect = 0, FONTEFEECTFLAG selecteffect = FET_NONE, COLOR clrSelectEffect = 0); // bComplex ? DRPT_COMPLEX : DTPT_FORMAT_TEXT
	CDecoderTextPiece( int nImage, int nWidth, int nHeight, const wchar_t* szTip = NULL, const wchar_t* szMouseNotify = NULL); // DTPT_STATIC_IMAGE
	CDecoderTextPiece( int nStart, int nEnd, int nWidth, int nHeight, int nInterval, const wchar_t* szTip = NULL, const wchar_t* szMouseNotify = NULL); // DTPT_ANIMATE_IMAGE
	CDecoderTextPiece( const wchar_t* szText, int font, int textformatid, COLOR clrText, COLOR clrTextGradual, COLOR clrMouseOn, COLOR clrMouseOnGradual, int nImage, int nImageMouseOn, int nImageDown, const wchar_t* szNotify, int nImageWidth, int nImageHeight, const wchar_t* szTip = NULL, const wchar_t* szMouseNotify = NULL, FONTEFEECTFLAG texteffect = FET_NONE, COLOR clrTextEffect = 0, FONTEFEECTFLAG mouseoneffect = FET_NONE, COLOR clrMouseOnEffect = 0); // DRPT_BUTTON
	CDecoderTextPiece(); // DRPT_RETURN
	CDecoderTextPiece( const wchar_t* szTip, int nPack, int nEmotion, int nShowWidth, int nShowHeight, int nIconWidth, int nIconHeight, const wchar_t* szMouseNotify = NULL); // DTPT_EMOTION_IMAGE
	CDecoderTextPiece( int nWidth, int nHeight, CCustomTextPieceAbstractImpl*	impl, const wchar_t* szTip = NULL, const wchar_t* szMouseNotify = NULL); // DTPT_CUSTOM_PIECE

	CDecoderTextPiece( const CDecoderTextPiece& src);
	virtual ~CDecoderTextPiece();
public:
	CDecoderTextPiece& operator=( const CDecoderTextPiece& src);
private:	
	void Free();
	void CopyFrom( const CDecoderTextPiece& src);
public:
	DTP_TYPE GetType() const;

	const wchar_t* GetText() const;
	int GetTextLength() const;
	
	int GetTextFont() const;
	int GetTextFormatID() const;
	int GetTextColor() const;
	int GetTextGradualColor() const;
	COLOR GetTextSelectColor() const;
	COLOR GetTextSelectGradualColor() const;
	COLOR GetTextSelectBKColor() const;
	FONTEFEECTFLAG GetTextEffect() const;
	FONTEFEECTFLAG GetSelectEffect() const;
	COLOR GetTextEffectColor() const;
	COLOR GetSelectEffectColor() const;
	

	const wchar_t* GetTipFormatText() const;
	int GetTipFormatTextLength() const;

	int GetImage() const;

	int GetImageWidth() const;
	int GetImageHeight() const;

	int GetAnimateInterval() const;
	int GetAnimateImageStart() const;
	int GetAnimateImageEnd() const;

	int GetEmotionPack() const;
	int GetEmotion() const;
	int GetEmotionWidth() const;
	int GetEmotionHeight() const;

	COLOR GetButtonMouseOnColor() const;
	COLOR GetButtonMouseOnGradualColor() const;
	FONTEFEECTFLAG GetButtonMouseOnEffect() const;
	COLOR GetButtonMouseOnEffectColor() const;
	int GetButtonMouseOnImage() const;
	int GetButtonDownImage() const;
	int GetButtonImageWidth() const;
	int GetButtonImageHeight() const;
	const wchar_t* GetButtonNotify() const;
	const wchar_t* GetTextInfo() const;
	const wchar_t* GetMouseNotify() const;
};

class GXWINDOW_EXPORTS_DLL CDecoderTextPieceList : public CUncopyableObject
{
	INLINECLASS::CTextPieceVector*		m_tpv;
public:
	CDecoderTextPieceList();
	virtual ~CDecoderTextPieceList();
public:
	virtual void Insert( const CDecoderTextPiece& tp);
	virtual int GetCount() const;
	virtual const CDecoderTextPiece& GetAt( int nIndex) const;
};

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 

class GXWINDOW_EXPORTS_DLL CAbstarctTextDecoder : public CUncopyableObject
{
public:
	CAbstarctTextDecoder();
	virtual ~CAbstarctTextDecoder();
public:
	virtual void Decode( const wchar_t* fulltext, CDecoderTextPieceList& tpl) = 0;
	virtual void Delete();
	virtual CAbstarctTextDecoder* Clone() const = 0;
};

class GXWINDOW_EXPORTS_DLL CAbstarctTextEncoder : public CUncopyableObject
{
public:
	CAbstarctTextEncoder();
	virtual ~CAbstarctTextEncoder();
public:
	virtual void AddTextPiece( const CDecoderTextPiece& tp) = 0;
	virtual void AddTextPieces( const CDecoderTextPieceList& tpl);
	virtual const wchar_t* GetResult() = 0;
};

} // namespace GXWINDOW { 

