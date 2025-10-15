#include "gxwindowdef.h"
#include "editinline.h" 

namespace GXWINDOW { 

namespace INLINECLASS {

static CTextAreaComponentAllocator		g_DefaultTextAreaComponentAllocator;
static CCombinationTextAreaAllocator		g_DefaultCombinationTextAreaAllocator;

class CTextAreaLine : public std::list<CComponent*>
{
	CTextAreaBackGroundEffect*	m_pTextAreaBackGroundEffect;
public:
	CTextAreaLine( CTextAreaBackGroundEffect* pTextAreaBackGroundEffect = NULL) : m_pTextAreaBackGroundEffect( pTextAreaBackGroundEffect), m_nHeight( 0), m_nBackgroundEffectInstanceId( 0), m_nBackgroundEffect( -1), m_nBgEffectStartPos( -1), m_nBgEffectEndPos( -1) {}
	virtual ~CTextAreaLine() {}
public:
	void DeleteAll( CTextAreaComponentAllocator& alloc)
	{
		for( iterator it = begin(), ite = end(); it != ite; ++ it)
		{
			CComponent*& pc = *it;
			alloc.DeleteComponent( pc);
		}
		clear();
		if ( m_pTextAreaBackGroundEffect && m_nBackgroundEffect > 0)
			m_pTextAreaBackGroundEffect->ReleaseBackGroundEffect( m_nBackgroundEffect, m_nBackgroundEffectInstanceId);
	}
	static int GenEffectGUID()
	{
		static int s_guid = 0;
		return ++ s_guid;
	}
public:
	int			m_nHeight;
	int			m_nBackgroundEffectInstanceId;
	int			m_nBackgroundEffect;
	int			m_nBgEffectStartPos;
	int			m_nBgEffectEndPos;
};

class CTextAreaLineList : public std::list<CTextAreaLine>
{
public:
	CTextAreaLineList() {}
	virtual ~CTextAreaLineList() {}
public:
	void DeleteAll( CTextAreaComponentAllocator& alloc)
	{
		for( iterator it = begin(), ite = end(); it != ite; ++ it)
		{
			CTextAreaLine& tal = *it;
			tal.DeleteAll( alloc);
		}
		clear();
	}
	CComponent* GetComponentFromPoint( int x, int y, int ls)
	{
		int ny = 0;
		for( iterator it = begin(); it != end(); ++ it)
		{
			CTextAreaLine&  tal = (*it);
			int nb = ny + tal.m_nHeight + ls;
			if( y >= ny && y < nb)
				return GetComponentOneLineFromPoint( tal, x, y);
			ny = nb;
		} // for( CTextAreaLineList::const_iterator it = ta.m_tall->begin(); it != ta.m_tall->end(); ++ it)
		return NULL;
	}

	void SetSelect( int sx, int sy, int x, int y, int ls, int nw)
	{
		iterator itStart = end();
		iterator itEnd = end();
		int ny = 0;

		for( iterator it = begin(); it != end(); ++ it)
		{
			CTextAreaLine& tal = *it;
			int nh = ny + tal.m_nHeight + ls;
			if( sy >= ny && sy < nh)
				itStart = it;
			if( y >= ny && y < nh)
			{
				itEnd = it;
				break;
			}
			ny = nh;
		} // for( INLINECLASS::CTextAreaLineList::iterator it = m_tall->begin(); it != m_tall->end(); ++ it)

		if( itStart == end())
			return;

		if( itStart == itEnd)
		{
			INLINECLASS::CTextAreaLine& tal = *itStart;
			if( x < sx)
				ResetOneLineSelect( tal, x, sx);
			else
				ResetOneLineSelect( tal, sx, x);
			return;
		}

		{
			INLINECLASS::CTextAreaLine& tal = *itStart;
			ResetOneLineSelect( tal, sx, nw);
		}

		for( ++ itStart; itStart != itEnd; ++ itStart)
		{
			INLINECLASS::CTextAreaLine& tal = *itStart;
			ResetOneLineSelectAll( tal);
		}

		if( itEnd != end())
		{
			INLINECLASS::CTextAreaLine& tal = *itStart;
			ResetOneLineSelect( tal, 0, x);
		}
	}
private:
	CComponent* GetComponentOneLineFromPoint( CTextAreaLine& tal, int x, int y)
	{
		int nx = 0;
		for( CTextAreaLine::iterator it = tal.begin(); it != tal.end(); ++ it)
		{
			CComponent*& pc = *it;
			int nr = nx + pc->GetWidth();
			if( x >= nx && x < nr)
				return pc;
			nx = nr;
		} // for( CTextAreaLine::const_iterator it = tal.begin(); it != tal.end(); ++ it)
		return NULL;
	}
	void ResetOneLineSelect( CTextAreaLine& tal, int xs, int xe)
	{
		CTextAreaLine::iterator itStart = tal.end();
		CTextAreaLine::iterator itEnd = tal.end();

		int nx = 0;
		for( CTextAreaLine::iterator it = tal.begin(); it != tal.end(); ++ it)
		{
			CComponent*& pc = *it;
			int nw = nx + pc->GetWidth();
			if( xs >= nx && xs < nw)
				itStart = it;
			if( xe >= nx && xe < nw)
			{
				itEnd = it;
				break;
			}
			nx = nw;
		} // for( INLINECLASS::CTextAreaLineList::iterator it = m_tall->begin(); it != m_tall->end(); ++ it)

		if( itStart == tal.end())
			return;
		if( itStart == itEnd)
		{
			CComponent*& pc = *itStart;
			nx = pc->GetPosX();
			ResetOneComponentSelect( pc, xs - nx, xe - nx);
			return;
		}

		{
			CComponent*& pc = *itStart;
			nx = pc->GetPosX();
			ResetOneComponentSelect( pc, xs - nx, pc->GetWidth());
		}

		for( ++ itStart; itStart != itEnd; ++ itStart)
		{
			CComponent*& pc = *itStart;
			ResetOneComponentSelectAll( pc);
		}
		if( itEnd != tal.end())
		{
			CComponent*& pc = *itStart;
			nx = pc->GetPosX();
			ResetOneComponentSelect( pc, 0, xe - nx);
		}
	}

	void ResetOneLineSelectAll( CTextAreaLine& tal)
	{
		for( CTextAreaLine::iterator it = tal.begin(); it != tal.end(); ++ it)
		{
			CComponent*& pc = *it;
			ResetOneComponentSelectAll( pc);
		}
	}

	void ResetOneComponentSelectAll( CComponent* pc)
	{
		CBlendEditElement* bee = dynamic_cast<CBlendEditElement*>( pc);
		if( bee)
			bee->SetSelect();
	}
	void ResetOneComponentSelect( CComponent* pc, int xs, int xe)
	{
		CBlendEditElement* bee = dynamic_cast<CBlendEditElement*>( pc);
		if( bee)
			bee->SetSelect( xs, xe);
	}
};


struct TextAreaDispatchMessage
{
	template< typename F>
		static bool DispatchMouseMessage( CTextArea& ta, F f, int x, int y, int key)
	{
		CComponent* pc = ta.m_tall->GetComponentFromPoint( x - ta.GetAbsPosX(), y - ta.GetAbsPosY(), ta.GetLineSpace());
		if( pc)
			return (pc->*f)( x, y, key);
		else
			return false;
	}
};

class CTextAreaButtonClickNotifySet : public CNotifySet<CTextAreaButtonClickNotify*>
{
public:
	CTextAreaButtonClickNotifySet() {}
	virtual ~CTextAreaButtonClickNotifySet() {}
};

class CTextAreaTextClickNotifySet : public CNotifySet<CTextAreaTextClickNotify*>
{
public:
	CTextAreaTextClickNotifySet() {}
	virtual ~CTextAreaTextClickNotifySet() {}
};

class CTextAreaElementMouseEventNotifySet : public CNotifySet<CTextAreaElementMouseEventNotify*>
{
public:
	CTextAreaElementMouseEventNotifySet() {}
	virtual ~CTextAreaElementMouseEventNotifySet() {}
};

class CTextAreaFocusNotifySet : public CNotifySet<CTextAreaFocusNotify*>
{
public:
	CTextAreaFocusNotifySet() {}
	virtual ~CTextAreaFocusNotifySet() {}
};



class CTextVector : public std::vector<std::wstring>
{
public:
	CTextVector() {}
	virtual ~CTextVector() {}

	int GetIndex( const std::wstring& text)
	{
		CTextVector::iterator it = std::find( this->begin(), this->end(), text);
		return it == this->end() ? -1 : it - this->begin();
	}
};

class CScrollTextAreaParam 
{
public:
	CTextArea*			ta;			// 用于显示文字
	int					posx;		// ta在滚动文字控件中的开始显示位置 0 <= tc <= CScrollSingleLineTextArea.GetWidth()
	int					offset;		// ta自身显示的开始位置 0 <= tc <= tc.GetWidth()
	int					width;		// ta显示的宽度 0 <= width <= CScrollSingleLineTextArea.GetWidth()
	std::wstring		text;		// 文本内容
	CScrollTextAreaParam() : ta( NULL), posx( 0), offset( 0), width( 0), text( L"") {}
	CScrollTextAreaParam( const CScrollTextAreaParam& src) : ta( src.ta), posx( src.posx), offset( src.offset), width( src.width), text( src.text) {}
	virtual ~CScrollTextAreaParam() {} 
	bool operator==( const wchar_t* _text) const { return ta ? 0 == _wcsicmp( text.c_str(), _text) : false; }
	CScrollTextAreaParam& operator=( const CScrollTextAreaParam& src)
	{
		if ( this == &src)
			return *this;
		this->ta = src.ta;
		this->posx = src.posx;
		this->offset = src.offset;
		this->width = src.width;
		this->text = src.text;
		return *this;
	}
};

class CScrollTextAreaParamVector : public std::vector<CScrollTextAreaParam>
{
public:
	CScrollTextAreaParamVector() {}
	virtual ~CScrollTextAreaParamVector() {}
};

} // namespcae INLINECLASS

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

ImageParams	CTextAreaProperties::s_DefaultCursorImage = CImageParams();
int			CTextAreaProperties::s_nDefaultCursorWidth = 5;
int			CTextAreaProperties::s_nDefaultCursorHeight = 20;
bool		CTextAreaProperties::s_bEnableWordBreak = false;

CTextAreaProperties::CTextAreaProperties()
	: m_bSingleLine( false), m_nLineSpace( 2), m_nCursorCycle( 500), m_nCursorWidth( s_nDefaultCursorWidth), m_nCursorHeight( s_nDefaultCursorHeight), m_bAllowSelected( false), m_nMinHeight( 20),
	m_nFont( 0), m_clrText( MakeColor( 255, 255, 255)), m_clrTextGradual( MakeColor( 255, 255, 255)), m_clrSelect( MakeColor( 255, 255, 255)), m_clrSelectGradual( MakeColor( 255, 255, 255)), m_clrBKSelect( MakeColor( 0, 0, 128)), m_bPassword( false),
	m_clrTextEffect( 0), m_clrSelectEffect( 0), m_uTextEffect( FET_NONE), m_uSelectEffect( FET_NONE)
{
	m_ipCursor.nBitmap = -1;
}

CTextAreaProperties::~CTextAreaProperties()
{
}

void CTextAreaProperties::SetDefaultCursor( const ImageParams& ip, int w, int h)
{
	s_DefaultCursorImage = ip;
	s_nDefaultCursorWidth = w;
	s_nDefaultCursorHeight = h;
}

const ImageParams& CTextAreaProperties::GetDefaultCursor()
{
	return s_DefaultCursorImage;
}

int CTextAreaProperties::GetDefaultCursorWidth()
{
	return s_nDefaultCursorWidth;
}

int CTextAreaProperties::GetDefaultCursorHeight()
{
	return s_nDefaultCursorHeight;
}

int CTextAreaProperties::GetStringWidth( int nFont, const wchar_t* szText)
{
	if( NULL == szText || 0 == szText[0])
		return 0;
	return CTextDrawManager::GetInstance().GetStringPixelWidth( szText, nFont);
}

void CTextAreaProperties::SetPassword( bool b)
{
	m_bPassword = b;
}

bool CTextAreaProperties::IsPassword() const
{
	return m_bPassword;
}

void CTextAreaProperties::SetDefaultFont( int font)
{
	m_nFont = font;
}

int CTextAreaProperties::GetDefaultFont() const
{
	return m_nFont;
}

void CTextAreaProperties::SetDefaultTextColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bUseGradualColor, COLOR clrGradual)
{
	m_clrText = clr;
	m_clrTextEffect = clrEffect;
	m_uTextEffect = type;
	m_clrTextGradual = bUseGradualColor ? clrGradual : clr;
}

COLOR CTextAreaProperties::GetDefaultTextColor() const
{
	return m_clrText;
}

COLOR CTextAreaProperties::GetDefaultTextGradualColor() const
{
	return m_clrTextGradual;
}

COLOR CTextAreaProperties::GetDefaultSelectTextGradualColor() const
{
	return m_clrSelectGradual;
}

void CTextAreaProperties::SetDefaultSelectColor( COLOR clrText, COLOR clrBackground, FONTEFEECTFLAG type, COLOR clrEffect, bool bUseGradualColor, COLOR clrGradualt)
{
	m_clrSelect = clrText;
	m_clrBKSelect = clrBackground;
	m_clrSelectEffect = clrEffect;
	m_uSelectEffect = type;
	m_clrSelectGradual = bUseGradualColor ? clrGradualt : clrText;
}

COLOR CTextAreaProperties::GetDefaultSelectTextColor() const
{
	return m_clrSelect;
}

COLOR CTextAreaProperties::GetDefaultSelectBackgroundColor() const
{
	return m_clrBKSelect;
}

COLOR CTextAreaProperties::GetDefaultEffectColor() const
{
	return m_clrTextEffect;
}

COLOR CTextAreaProperties::GetDefaultSelectEffectColor() const
{
	return m_clrSelectEffect;
}

FONTEFEECTFLAG CTextAreaProperties::GetDefaultEffectType() const
{
	return m_uTextEffect;
}

FONTEFEECTFLAG CTextAreaProperties::GetDefaultSelectEffectType() const
{
	return m_uSelectEffect;
}

void CTextAreaProperties::SetSingleLine( bool b)
{
	m_bSingleLine = b;
}
bool CTextAreaProperties::IsSingleLine() const
{
	return m_bSingleLine;
}

void CTextAreaProperties::SetLineSpace( int n)
{
	m_nLineSpace = n;
}

int CTextAreaProperties::GetLineSpace() const
{
	return m_nLineSpace;
}

bool CTextAreaProperties::IsCursorShow() const
{
	return -1 != m_ipCursor.nBitmap;
}

void CTextAreaProperties::HideCursor()
{
	m_ipCursor.nBitmap = -1;
}

void CTextAreaProperties::ShowCursor( const ImageParams& ip)
{
	m_ipCursor = ip;
	if( -1 == m_ipCursor.nBitmap)
	{
		if ( -1 == s_DefaultCursorImage.nBitmap)
			HideCursor();
		m_ipCursor = s_DefaultCursorImage;
	}	
}

const ImageParams& CTextAreaProperties::GetCursor() const
{
	return m_ipCursor;
}

bool CTextAreaProperties::IsSelectAllow() const
{
	return m_bAllowSelected;
}

void CTextAreaProperties::SetSelectAllow( bool b)
{
	m_bAllowSelected = b;
}

void CTextAreaProperties::SetMinWidthHeight( int h)
{
	m_nMinHeight = h;
}

int CTextAreaProperties::GetMinWidthHeight() const
{
	return m_nMinHeight;
}

int CTextAreaProperties::GetCursorHeight() const
{
	return m_nCursorHeight;
}

int CTextAreaProperties::GetCursorWidth() const
{
	return m_nCursorWidth;
}

void CTextAreaProperties::SetCursorCycle( int n)
{
	m_nCursorCycle = n;
}

int CTextAreaProperties::GetCursorCycle() const
{
	return m_nCursorCycle;
}

void CTextAreaProperties::SetCursorSize( int w, int h)
{
	m_nCursorWidth = w;
	m_nCursorHeight = h;
}

void CTextAreaProperties::RegisterCursorImage()
{
	CImageTextureManager::GetInstance().InsertImage( m_ipCursor.nBitmap);
}

void CTextAreaProperties::FreeCursorImage()
{
	CImageTextureManager::GetInstance().RemoveImage( m_ipCursor.nBitmap);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextAreaComponentAllocator::CTextAreaComponentAllocator()
{
}

CTextAreaComponentAllocator::~CTextAreaComponentAllocator()
{
}

CEditAnimateIconElement* CTextAreaComponentAllocator::NewAnimateImageComponent()
{
	return new CEditAnimateIconElement();
}

CEditIconElement* CTextAreaComponentAllocator::NewImageComponent()
{
	return new CEditIconElement();
}

CBlendEditElement* CTextAreaComponentAllocator::NewEditElement( CTextDrawManager& tdm)
{
	return new CBlendEditElement( tdm);
}

CBlendEditElement* CTextAreaComponentAllocator::NewComplexElement( CTextDrawManager& tdm)
{
	return new CBlendEditElement( tdm);
}

CEditButtonElement* CTextAreaComponentAllocator::NewButtonElement( CTextDrawManager& tdm)
{
	return new CEditButtonElement( tdm);
}

CEditEmotionElement* CTextAreaComponentAllocator::NewEmotionComponent()
{
	return new CEditEmotionElement();
}

CCustomTextPiece* CTextAreaComponentAllocator::NewCustomTextPiece()
{
	return new CCustomTextPiece();
}

void CTextAreaComponentAllocator::DeleteComponent( CComponent* pc)
{
	delete pc;
}

CTextAreaComponentAllocator& CTextAreaComponentAllocator::GetInstance()
{
	return INLINECLASS::g_DefaultTextAreaComponentAllocator;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextAreaButtonClickNotify::CTextAreaButtonClickNotify() {}
CTextAreaButtonClickNotify::~CTextAreaButtonClickNotify() {}

CTextAreaTextClickNotify::CTextAreaTextClickNotify() {}
CTextAreaTextClickNotify::~CTextAreaTextClickNotify() {}

CTextAreaElementMouseEventNotify::CTextAreaElementMouseEventNotify() {}
CTextAreaElementMouseEventNotify::~CTextAreaElementMouseEventNotify() {}
void CTextAreaElementMouseEventNotify::OnTextAreaElementLeftButtonDown( CTextArea& ta, const wchar_t* szInfo) {}
void CTextAreaElementMouseEventNotify::OnTextAreaElementRightButtonDown( CTextArea& ta, const wchar_t* szInfo) {}

CTextAreaFocusNotify::CTextAreaFocusNotify() {}
CTextAreaFocusNotify::~CTextAreaFocusNotify() {}
void CTextAreaFocusNotify::OnSetKeyFocus( CTextArea& ta) {}
void CTextAreaFocusNotify::OnLostKeyFocus( CTextArea& ta) {}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextArea::CTextArea( bool bHideCursor, CAbstarctTextDecoder& decoder, CTextAreaComponentAllocator& allocator)
: m_decoder( decoder.Clone()), m_tdm( &CTextDrawManager::GetInstance()), m_allocator( &allocator), m_eec( new INLINECLASS::CEditElementContainer()), m_tall( new INLINECLASS::CTextAreaLineList()), 
m_pLastMouseIn( NULL), m_bCursorShowing( false), m_nCursorX( 0), m_nCursorY( 0),
m_ButtonNotifySet( new INLINECLASS::CTextAreaButtonClickNotifySet()), m_TextNotifySet( new INLINECLASS::CTextAreaTextClickNotifySet()), m_MouseNotifySet( new INLINECLASS::CTextAreaElementMouseEventNotifySet()),
m_FocusNotifySet( new INLINECLASS::CTextAreaFocusNotifySet()), m_bSelectNow( false), m_bFucosed( false), m_bAutoWidthForSingle(false), m_nTotalWidth( 0), m_pBackgroundEffectDraw( NULL)
{
	SetSize( GetCursorWidth(), GetMinWidthHeight());
	CKeyInput::GetInstance().KeyInputNotifyAdd( *this);

	if ( bHideCursor)
		ShowCursor( GetCursor());
}

CTextArea::CTextArea( CAbstarctTextDecoder& decoder, CTextAreaComponentAllocator& allocator)
: m_decoder( decoder.Clone()), m_tdm( &CTextDrawManager::GetInstance()), m_allocator( &allocator), m_eec( new INLINECLASS::CEditElementContainer()), m_tall( new INLINECLASS::CTextAreaLineList()), 
	m_pLastMouseIn( NULL), m_bCursorShowing( false), m_nCursorX( 0), m_nCursorY( 0),
	m_ButtonNotifySet( new INLINECLASS::CTextAreaButtonClickNotifySet()), m_TextNotifySet( new INLINECLASS::CTextAreaTextClickNotifySet()), m_MouseNotifySet( new INLINECLASS::CTextAreaElementMouseEventNotifySet()),
	m_FocusNotifySet( new INLINECLASS::CTextAreaFocusNotifySet()), m_bSelectNow( false), m_bFucosed( false), m_bAutoWidthForSingle(false), m_nTotalWidth( 0), m_pBackgroundEffectDraw( NULL)
{
	SetSize( GetCursorWidth(), GetMinWidthHeight());
	CKeyInput::GetInstance().KeyInputNotifyAdd( *this);

	//ShowCursor( GetCursor()); // 文本框默认不需要有光标, 如确实需要光标, 自己调用ShowCursor()
}

CTextArea::~CTextArea()
{
	CTimer::CancelTimer( m_cursortimer);
	CKeyInput::GetInstance().KeyInputNotifyRemove( *this);

	m_decoder->Delete();
	m_tall->DeleteAll( *m_allocator);
	delete m_eec;
	delete m_tall;
	delete m_ButtonNotifySet;
	delete m_TextNotifySet;
	delete m_MouseNotifySet;
	delete m_FocusNotifySet;
}

void CTextArea::TextAreaButtonClickNotifyAdd( CTextAreaButtonClickNotify& tancn)
{
	m_ButtonNotifySet->Insert( &tancn);
}

void CTextArea::TextAreaButtonClickNotifyRemove( CTextAreaButtonClickNotify& tancn)
{
	m_ButtonNotifySet->Remove( &tancn);
}

void CTextArea::TextAreaTextClickNotifyAdd( CTextAreaTextClickNotify& tatcn)
{
	m_TextNotifySet->Insert( &tatcn);
}

void CTextArea::TextAreaTextClickNotifyRemove( CTextAreaTextClickNotify& tatcn)
{
	m_TextNotifySet->Remove( &tatcn);
}

void CTextArea::TextAreaElementMouseEventNotifyAdd( CTextAreaElementMouseEventNotify& taimen)
{
	m_MouseNotifySet->Insert( &taimen);
}

void CTextArea::TextAreaElementMouseEventNotifyRemove( CTextAreaElementMouseEventNotify& taimen)
{
	m_MouseNotifySet->Remove( &taimen);
}

void CTextArea::TextAreaFocusNotifyAdd( CTextAreaFocusNotify& tfn)
{
	m_FocusNotifySet->Insert( &tfn);
}

void CTextArea::TextAreaFocusNotifyRemove( CTextAreaFocusNotify& tfn)
{
	m_FocusNotifySet->Remove( &tfn);
}

CAbstarctTextDecoder& CTextArea::GetTextDecoder()
{
	return *m_decoder; 
}

void CTextArea::AppendText( const wchar_t* szText)
{
	if ( szText)
	{
		CDecoderTextPieceList		tpl;
		m_decoder->Decode( szText, tpl);
		m_eec->AppendToEnd( tpl);
		RelayoutView(false);
		SetCursorToEnd();
	}
}

void CTextArea::AppendText( int n)
{
	AppendText( CTextResourceManager::GetInstance().GetText( n));
}

void CTextArea::AppendText( CDecoderTextPieceList& tpl)
{
	m_eec->AppendToEnd( tpl);
	RelayoutView(false);
}

void CTextArea::SetSize( int w, int h)
{
	CComponent::SetSize( w, h);
	RelayoutView();
}

void CTextArea::SetWidth( int w)
{
	if( GetWidth() == w)
		return;
	CComponent::SetSize( w, GetHeight());
	RelayoutView();
}

void CTextArea::SetMinWidthHeight( int h)
{
	CTextAreaProperties::SetMinWidthHeight( h);
	if( IsSingleLine())
	{
		int cw = GetWidth();
		CComponent::SetSize( max( cw, h), GetHeight());
	}
	else
	{
		int ch = GetHeight();
		CComponent::SetSize( GetWidth(), max( ch, h));
	}
}

void CTextArea::NewTextElement( int& nIndex, int& x, int y, int lh, INLINECLASS::CSimpleString& str, CDecoderTextPiece& dtp, INLINECLASS::CTextAreaLine& tal)
{
	if( -1 == nIndex)
		return;

	int				nFont = 0;

	CBlendEditElement* bee = NULL;
	switch( dtp.GetType())
	{
	case DTPT_COMPLEX:
		bee = m_allocator->NewComplexElement( *m_tdm);
		nFont = dtp.GetTextFont();
		bee->SetTextColor( dtp.GetTextColor(), dtp.GetTextGradualColor(), dtp.GetTextEffect(), dtp.GetTextEffectColor());
		bee->SetSelectColor( dtp.GetTextSelectColor(), dtp.GetTextGradualColor(), dtp.GetTextSelectBKColor(), dtp.GetSelectEffect(), dtp.GetSelectEffectColor());
		if( const wchar_t* szInfo = dtp.GetTextInfo())
		{
			if( szInfo[0])
			{
				bee->SetInfo( szInfo);
				bee->ComponentMouseEventNotifyAdd( *this);
			}
			else
			{
				bee->SetTransparent( IsTransparent());
			}
		}
		break;
	case DTPT_FORMAT_TEXT:
		bee = m_allocator->NewEditElement( *m_tdm);
		nFont = dtp.GetTextFont();
		bee->SetTextColor( dtp.GetTextColor(), dtp.GetTextGradualColor(), dtp.GetTextEffect(), dtp.GetTextEffectColor());
		bee->SetSelectColor( dtp.GetTextSelectColor(), dtp.GetTextSelectGradualColor(), dtp.GetTextSelectBKColor(), dtp.GetSelectEffect(), dtp.GetSelectEffectColor());
		if( const wchar_t* szInfo = dtp.GetTextInfo())
		{
			if( szInfo[0])
			{
				bee->SetInfo( szInfo);
				bee->ComponentMouseEventNotifyAdd( *this);
			}
			else
			{
				bee->SetTransparent( IsTransparent());
			}
		}
		break;
	case DTPT_DEFAULT_TEXT:
		bee = m_allocator->NewEditElement( *m_tdm);
		nFont = GetDefaultFont();
		bee->SetTextColor( GetDefaultTextColor(), GetDefaultTextGradualColor(), GetDefaultEffectType(), GetDefaultEffectColor());
		bee->SetSelectColor( GetDefaultSelectTextColor(), GetDefaultSelectTextGradualColor(), GetDefaultSelectBackgroundColor(), GetDefaultSelectEffectType(), GetDefaultSelectEffectColor());
		break;
	default:
		assert( false);
		break;
	}

	bee->SetParent( this);
	bee->SetFont( nFont);
	bee->SetText( str.c_str());

	y += ( lh - bee->GetHeight());
	bee->SetPos( x, y);
	tal.push_back( bee);

	str.erase();
	nIndex = -1;
	x += bee->GetWidth();
}

void CTextArea::NewLineElements( int& x, int y, int lh, INLINECLASS::CTextAreaLine& tal, const INLINECLASS::CLineObject& lo, bool bPassword)
{
	int nIndex = -1;
	CDecoderTextPiece		dtpLast;
	INLINECLASS::CSimpleString			strTemp;
	bool bGetBgEffectStartPos = false;

	for( INLINECLASS::CLineObject::const_iterator itl = lo.begin(); itl != lo.end(); ++ itl)
	{
		const INLINECLASS::CEditElementObject& ee = *itl;
		const CDecoderTextPiece& dtp = m_eec->GetDecoderTextPieceByEditElement( ee);

		if ( ee.m_nBackgroundEffect > 0 && tal.m_nBackgroundEffect <= 0)
		{
			tal.m_nBackgroundEffectInstanceId = INLINECLASS::CTextAreaLine::GenEffectGUID();
			tal.m_nBackgroundEffect = ee.m_nBackgroundEffect;
			tal.m_nBgEffectStartPos = x;
			bGetBgEffectStartPos = true;
		}

		switch( dtp.GetType())
		{
		case DTPT_RETURN:
			bGetBgEffectStartPos = false;
			continue;
		case DTPT_STATIC_IMAGE:
			{
				NewTextElement( nIndex, x, 0, lh, strTemp, dtpLast, tal);
				if ( bGetBgEffectStartPos)
				{
					bGetBgEffectStartPos = false;
					//tal.m_nBgEffectStartPos = x;
				}

				CEditIconElement* ic = m_allocator->NewImageComponent();
				ic->SetParent( this);
				ic->SetIcon( dtp.GetImage());
				int w = dtp.GetImageWidth();
				int h = dtp.GetImageHeight();
				ic->SetPos( x, y + lh - h);
				ic->SetSize( w, h);
				ic->SetNotifyString( dtp.GetMouseNotify());
				tal.push_back( ic);

				x += w;
				lh = max( lh, h);

				if ( NULL != ic->GetNotifyString() && 0 != ic->GetNotifyString()[0])
					ic->ComponentMouseEventNotifyAdd( *this);
				else
					ic->SetTransparent( this->IsTransparent());
			}
			break;
		case DTPT_ANIMATE_IMAGE:
			{
				NewTextElement( nIndex, x, 0, lh, strTemp, dtpLast, tal);
				if ( bGetBgEffectStartPos)
				{
					bGetBgEffectStartPos = false;
					//tal.m_nBgEffectStartPos = x;
				}

				CEditAnimateIconElement* aic = m_allocator->NewAnimateImageComponent();
				int nend = dtp.GetAnimateImageEnd();
				for( int i = dtp.GetAnimateImageStart(); i <= nend; i ++)
					aic->Insert( i);
				aic->SetInterval( dtp.GetAnimateInterval());
				int w = dtp.GetImageWidth();
				int h = dtp.GetImageHeight();
				aic->SetParent( this);
				aic->SetPos( x, y + lh - h);
				aic->SetSize( w, h);
				aic->SetNotifyString( dtp.GetMouseNotify());					
				tal.push_back( aic);

				x += w;
				lh = max( lh, h);

				if ( NULL != aic->GetNotifyString() && 0 != aic->GetNotifyString()[0])
					aic->ComponentMouseEventNotifyAdd( *this);
				else
					aic->SetTransparent( IsTransparent());
			}
			break;
		case DTPT_EMOTION_IMAGE:
			{
				NewTextElement( nIndex, x, 0, lh, strTemp, dtpLast, tal);
				if ( bGetBgEffectStartPos)
				{
					bGetBgEffectStartPos = false;
					//tal.m_nBgEffectStartPos = x;
				}

				CEditEmotionElement* ec = m_allocator->NewEmotionComponent();
				ec->SetParent( this);
				ec->SetEmotion( dtp.GetEmotionPack(), dtp.GetEmotion());
				ec->SetIconSize( dtp.GetEmotionWidth(), dtp.GetEmotionHeight());
				int w = dtp.GetImageWidth();
				int h = dtp.GetImageHeight();
				ec->SetShowSize( w, h);
				ec->SetPos( x, y + lh - h);
				ec->SetSize( w, h);
				ec->SetNotifyString( dtp.GetMouseNotify());
				tal.push_back( ec);

				x += w;
				lh = max( lh, h);

				if ( NULL != ec->GetNotifyString() && 0 != ec->GetNotifyString()[0])
					ec->ComponentMouseEventNotifyAdd( *this);
				else
					ec->SetTransparent( IsTransparent());
			}
			break;
		case DTPT_COMPLEX:
		case DTPT_FORMAT_TEXT:
		case DTPT_DEFAULT_TEXT:
			{
				if( -1 == nIndex)
				{
					dtpLast = dtp;
					strTemp.erase();
					INLINECLASS::DividableString::StringAppendChar( strTemp, ee.m_char, bPassword);
					nIndex = ee.m_nIndex;
				}
				else if( ee.m_nIndex == nIndex)
				{
					INLINECLASS::DividableString::StringAppendChar( strTemp, ee.m_char, bPassword);
				}
				else
				{
					NewTextElement( nIndex, x, y, lh, strTemp, dtpLast, tal);
					if ( bGetBgEffectStartPos)
					{
						bGetBgEffectStartPos = false;
						//tal.m_nBgEffectStartPos = x;
					}

					nIndex = ee.m_nIndex;
					dtpLast = dtp;
					strTemp.erase();
					INLINECLASS::DividableString::StringAppendChar( strTemp, ee.m_char, bPassword);
				}
			}
			break;
		case DTPT_BUTTON:
			{
				assert( -1 == nIndex);
				CEditButtonElement* ebe = m_allocator->NewButtonElement( *m_tdm);
				ebe->SetParent( this);

				ebe->SetText( dtp.GetText(), dtp.GetTextFont(), dtp.GetTextColor(), dtp.GetTextGradualColor(), dtp.GetButtonMouseOnColor(), dtp.GetButtonMouseOnGradualColor(), dtp.GetTextEffect(), dtp.GetTextEffectColor(), dtp.GetButtonMouseOnEffect(), dtp.GetButtonMouseOnEffectColor());
				ebe->SetImage( dtp.GetImage(), dtp.GetButtonMouseOnImage(), dtp.GetButtonDownImage());
				ebe->SetButtonSize( dtp.GetButtonImageWidth(), dtp.GetButtonImageHeight());
				ebe->SetNotifyString( dtp.GetButtonNotify());
				ebe->SetMouseNotifyString( dtp.GetMouseNotify());
				ebe->SetTipText( dtp.GetTipFormatText());

				y += ( lh - ebe->GetHeight());
				ebe->SetPos( x, y);
				tal.push_back( ebe);
				x += ebe->GetWidth();

				if ( NULL != ebe->GetNotifyString() && 0 != ebe->GetNotifyString()[0])
				{
					ebe->ButtonClickNotifyAdd( *this);
					ebe->ComponentMouseEventNotifyAdd( *this);
				}
				else
				{
					ebe->SetTransparent( this->IsTransparent());
				}

				if ( NULL != ebe->GetMouseNotifyString() && 0 != ebe->GetMouseNotifyString()[0])
					ebe->ComponentMouseEventNotifyAdd( *this);
				else
					ebe->SetTransparent( this->IsTransparent());
			}
			break;
		case DTPT_CUSTOM_PIECE:
			{
				NewTextElement( nIndex, x, 0, lh, strTemp, dtpLast, tal);
				if ( bGetBgEffectStartPos)
				{
					bGetBgEffectStartPos = false;
					//tal.m_nBgEffectStartPos = x;
				}

				CCustomTextPiece* cc = m_allocator->NewCustomTextPiece();
				cc->SetParent( this);
				cc->SetTransparent( IsTransparent());

				int w = dtp.GetImageWidth();
				int h = dtp.GetImageHeight();
				cc->SetPos( x, y + lh - h);
				cc->SetSize( w, h);
				cc->SetNotifyString( dtp.GetMouseNotify());
				cc->SetAbstractImpl(dtp.m_customimpl);
				tal.push_back( cc);

				x += w;
				lh = max( lh, h);

				if ( NULL != cc->GetNotifyString() && 0 != cc->GetNotifyString()[0])
					cc->ComponentMouseEventNotifyAdd( *this);

			}
			break;
		default:
			assert( false);
			break;
		}

	} // for( INLINECLASS::CLineObject::const_iterator itl = lo.begin(); itl != lo.end(); ++ itl)
	NewTextElement( nIndex, x, y, lh, strTemp, dtpLast, tal);
	if ( tal.m_nBackgroundEffect > 0)
		tal.m_nBgEffectEndPos = x;
}

void CTextArea::RelayoutView( bool bFullRelayout)
{
	bool bSingle = IsSingleLine();
	bFullRelayout = bFullRelayout || bSingle;
	int  lastees = 0;
	INLINECLASS::CTextAreaLineList* ptemp = NULL;
	if ( bFullRelayout)
	{
		ptemp = m_tall;
		m_tall = new INLINECLASS::CTextAreaLineList();
	}
	else
	{
		lastees = m_eec->m_ees.size();
		if ( !m_tall->empty())
		{
			m_tall->back().DeleteAll( *m_allocator);
			m_tall->pop_back();
		}
	}

	int y = 0;
	int ls = GetLineSpace();

	
	m_eec->Recalculate( GetWidth(), GetDefaultFont(), bSingle, IsPassword(), bFullRelayout);
	if ( (int)m_eec->m_ees.size() < lastees)
		lastees = m_eec->m_ees.size();

	if( bSingle)
	{
		int						lh = CTextDrawManager::GetInstance().GetFontHeight( GetDefaultFont());
		
		for( INLINECLASS::CContainerObject::const_iterator it = m_eec->m_ees.begin(); it != m_eec->m_ees.end(); ++ it)
		{
			const INLINECLASS::CLineObject& lo = *it;
			lh = max( lh, lo.m_nHeight);
		}

		m_tall->push_back( INLINECLASS::CTextAreaLine(m_pBackgroundEffectDraw));
		INLINECLASS::CTextAreaLine& tal = m_tall->back();
		tal.m_nHeight = lh;
		int x = 0;
		for( INLINECLASS::CContainerObject::const_iterator it = m_eec->m_ees.begin(); it != m_eec->m_ees.end(); ++ it)
		{
			const INLINECLASS::CLineObject& lo = *it;
			NewLineElements( x, y, lh, tal, lo, IsPassword());
		}
		
		x += GetCursorWidth();
		int nmw = GetMinWidthHeight();
		CComponent::SetSize( max( x, nmw), lh);
		m_nTotalWidth = x;
	}
	else
	{
		INLINECLASS::CContainerObject::const_iterator it = m_eec->m_ees.begin(), ite = m_eec->m_ees.end();
		while ( lastees > 1)
		{
			y += (*it).m_nHeight + ls;
			++ it;
			-- lastees;
		}
		int maxx = 0;
		for( ; it != ite; ++ it)
		{
			const INLINECLASS::CLineObject& lo = *it;

			m_tall->push_back( INLINECLASS::CTextAreaLine(m_pBackgroundEffectDraw));
			INLINECLASS::CTextAreaLine& tal = m_tall->back();

			int x = 0;
			tal.m_nHeight = lo.m_nHeight;
			NewLineElements( x, y, lo.m_nHeight, tal, lo);
			if(maxx < x)
				maxx = x;
			y += (lo.m_nHeight + ls);
		} // for( INLINECLASS::CTextAreaItemVector::iterator it = m_taiv->begin(); it != m_taiv->end(); ++ it)
		y -= ls;
		maxx += GetCursorWidth();

		int nmh = GetMinWidthHeight();
		//yzp modify 08-7-22
		if(m_bAutoWidthForSingle == false)
			CComponent::SetSize( GetWidth(), max( y, nmh));
		else
		{
			int nmw = GetMinWidthHeight();
			CComponent::SetSize( max( maxx, nmw), max( y, nmh));
		}
		m_nTotalWidth = maxx;
	}

	if( m_pLastMouseIn)
	{
		m_pLastMouseIn->OnMouseLeave();
		m_pLastMouseIn = NULL;	
	}

	if ( ptemp)
	{
		ptemp->DeleteAll( *m_allocator);
		delete ptemp;
	}

	m_eec->GetCursorPosition(m_nCursorX, m_nCursorY, GetLineSpace(), GetDefaultFont(), IsPassword());

	// by llq 08.07.25
	if (!m_eec->m_ees.empty())
	{		
		INLINECLASS::CContainerObject::const_iterator it = m_eec->m_ees.end();--it;
		if ((*it).m_nHeight > GetCursorHeight())
			m_nCursorY += (*it).m_nHeight - GetCursorHeight();
	}

	INVALIDATE();
}

bool CTextArea::OnSetKeyFocus()
{
	m_bFucosed = true;

	if( IsCursorShow())
	{
		m_bCursorShowing = true;
		m_cursortimer.NewTimer( *this, GetCursorCycle());
		INVALIDATE();
	}
	m_FocusNotifySet->NotifyAll( *this, &CTextAreaFocusNotify::OnSetKeyFocus);
	return !IsPassword();
}

void CTextArea::OnLostKeyFocus()
{
	if( IsCursorShow())
	{
		m_bCursorShowing = false;
		m_cursortimer.CancelTimer();
		INVALIDATE();
	}

	m_bFucosed = false;
	m_FocusNotifySet->NotifyAll( *this, &CTextAreaFocusNotify::OnLostKeyFocus);
}

void CTextArea::OnComponentShow()
{
	RegisterCursorImage();
	CComponent::OnComponentShow();
}

void CTextArea::OnComponentHide()
{
	CKeyInput::GetInstance().ReleaseFocus( *this);
	FreeCursorImage();
	CComponent::OnComponentHide();
}

void CTextArea::SetPassword( bool b)
{
	assert( !( b && !IsSingleLine()) && "文本框密码模式只能在单行设置!");

	if( IsPassword() == b)
		return;
	CTextAreaProperties::SetPassword( b);
}

int CTextArea::GetLineCount() const
{
	return (int)m_tall->size();
}

int CTextArea::EraseLines( int nStart, int nCount)
{
	if ( nStart < 0 || nStart >= (int)m_tall->size() || nCount <= 0 || nCount + nStart > (int)m_tall->size())
		return 0;

	int ls = GetLineSpace();
	int h = 0;
	int i = 0;
	for ( INLINECLASS::CTextAreaLineList::iterator it = m_tall->begin(); it != m_tall->end() && i < nStart + nCount; i++)
	{
		if ( i >= nStart)
		{
			INLINECLASS::CTextAreaLine& tal = *it;
			h += tal.m_nHeight + ls;
			tal.DeleteAll( *m_allocator);
			it = m_tall->erase( it);
		}
		else
			++ it;
	}

	i = 0;
	for( INLINECLASS::CContainerObject::iterator it = m_eec->m_ees.begin(); it != m_eec->m_ees.end() && i < nStart + nCount; i++)
	{
		if ( i >= nStart)
			it = m_eec->m_ees.erase( it);
		else
			++ it;
	}

	RelayoutView();

	return h;
}

// int l, int t, int cx, int cy, int cw, int ch 的意义:
// (l, t) 被绘制对象左上角坐标
// (cx, cy) 被绘制对象的绘制区域左上角坐标
// (cw, ch) 被绘制对象的绘制区域的宽高尺寸
void CTextArea::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CComponent::Draw( draw, l, t, cx, cy, cw, ch);

	{
		int _T = t + cy;
		int _B = _T + ch;
		int _t = t;
		INLINECLASS::CTextAreaLineList::iterator it = m_tall->begin();
		for (; it != m_tall->end(); ++it)
		{
			INLINECLASS::CTextAreaLine& tal = *it;
			int _b = _t + tal.m_nHeight;
			if (!(_t > _B || _b < _T))
			{
				int _ch;
				int _cy;
 				if (_t <= _T)
 				{
 					_cy = _T - _t;
 					_ch = tal.m_nHeight - _cy;
					if ( _ch > ch)
						_ch = ch;
 				}
 				else if (_b >= _B)
 				{
 					_cy = 0;
 					_ch = tal.m_nHeight - (_b - _B);
 				}
 				else
				{
					_ch = tal.m_nHeight;
					_cy = 0;
				}
				DrawOneLine(tal, draw, l, _t, cx, _cy, cw, _ch);
			}
			_t += tal.m_nHeight + GetLineSpace();
		}
	}


	int y = 0;
	int cb = cy + ch;
	int nLineSpace = GetLineSpace();

//	INLINECLASS::CTextAreaLineList::iterator it;

	// 绘制第一行
// 	for( it = m_tall->begin(); it != m_tall->end(); ++ it)
// 	{
// 		INLINECLASS::CTextAreaLine& tal = *it;
// 		int ny = y + tal.m_nHeight + nLineSpace;
// 		if( cy >= y && cy < ny)
// 		{
// 			int nh = min( ny, cb) - cy;
// 			DrawOneLine( tal, draw, l, t + y, cx , cy - y, cw, nh);
// 			y = ny;
// 			ch -= nh;
// 			break;
// 		}
// 		y = ny;
// 	} // for( INLINECLASS::CTextAreaLineList::iterator it = m_tall->begin(); it != m_tall->end(); ++ it)

	// 绘制其余行，m_tall里元素数为行总数+1
//  	if( it != m_tall->end())
//  	{
//  		for( ++ it; ch > 0 && ( it != m_tall->end() ); ++ it)
//  		{
//  			INLINECLASS::CTextAreaLine& tal = *it;
//  			int ny = y + tal.m_nHeight + nLineSpace;
//  			int nh = min( ny, cb) - y;
//  			DrawOneLine( tal, draw, l, t + y, cx , 0, cw, nh);
//  			y = ny;
//  			ch -= nh;
//  		}
//  	}

	if( m_bFucosed && m_bCursorShowing)
	{
		int nCursorHeight = GetCursorHeight();
		int sb = m_nCursorY + nCursorHeight;
		if( m_nCursorY < cy)
		{
			if( sb < cy)
				return;
			if( sb < cb)
				ch = sb - cy;
			cy = cy - m_nCursorY;
		}
		else if(  m_nCursorY < cb)
		{
			if( sb < cb)
				ch = nCursorHeight;
			else
				ch = cb - m_nCursorY;
			cy = 0;
		}
		else // m_nCursorY >= cb
		{
			return;
		}

		int nCursorWidth = GetCursorWidth();
		sb = m_nCursorX + nCursorWidth;
		int cr = cx + cw;

		if( m_nCursorX < cx)
		{
			if( sb < cx)
				return;
			if( sb < cr)
				cw = sb - cx;
			cx = cx - m_nCursorX;
		}
		else if( m_nCursorX < cr)
		{
			if( sb < cr)
				cw = nCursorWidth;
			else
				cw = cr - m_nCursorX;
			cx = 0;
		}
		else // nx >= cr
		{
			return; 
		}
		DrawCursor( draw, l + m_nCursorX, t + m_nCursorY, cx, cy, cw, ch);
	}
}

void CTextArea::DrawOneLine( INLINECLASS::CTextAreaLine& tal, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	if ( m_pBackgroundEffectDraw && tal.m_nBackgroundEffect > 0)
	{
		m_pBackgroundEffectDraw->DrawBackGroundEffect( draw, tal.m_nBackgroundEffect, tal.m_nBackgroundEffectInstanceId, l + tal.m_nBgEffectStartPos, t + cy, cx, cy, min( cw, tal.m_nBgEffectEndPos - tal.m_nBgEffectStartPos), min( ch, tal.m_nHeight - cy));
	}

	int x = 0;
	int cr = cx + cw;
	INLINECLASS::CTextAreaLine::iterator it;

	for( it = tal.begin(); it != tal.end(); ++ it)
	{
		CComponent*& bee = *it;
		int bw = bee->GetWidth();
		int nx = x + bw;
		if( cx >= x && cx < nx)
		{
			int nw = min( cr, nx) - cx;

			DrawOneLineCompoment( *bee, tal, draw, l + x, t, cx - x, cy, nw, ch);

			x = nx;
			cw -= nw;
			break;
		}
		else
		{
			x = nx;
		}
	} 
	if( it == tal.end())
		return;

	for( ++ it; cw > 0 && ( it != tal.end()); ++ it)
	{
		CComponent*& bee = *it;
		int bw = bee->GetWidth();
		int nx = x + bw;
		int nw = min( cr, nx) - x;

		DrawOneLineCompoment( *bee, tal, draw, l + x, t, 0, cy, nw, ch);
		x = nx;
		cw -= nw;
	}
}

void CTextArea::DrawOneLineCompoment( CComponent& com, INLINECLASS::CTextAreaLine& tal, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	int bh = com.GetHeight();
	int dh = tal.m_nHeight - bh;
	if( dh > 0)
	{
		t += dh;
		if( dh > cy)
		{
			ch -= (dh - cy);
			cy = 0;
		}
		else
		{
			ch = tal.m_nHeight - cy;
			cy -= dh;
		}
	}
	if( ch >= 0)
		com.Draw( draw, l, t, cx, cy, cw, ch);
	// com.DoComponentAfterDrawNofity( draw, l, t, cx, cy, cw, ch);  目前还不需要对文本内的控件进行类似操作
}

bool CTextArea::OnLeftButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	bool bProcessed = INLINECLASS::TextAreaDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnLeftButtonDown, x, y, key);
	if( !bProcessed)
		CComponent::OnLeftButtonDown( x, y, key);

	if( IsSelectAllow())
	{
		m_nSelectLastX = x - GetAbsPosX();
		m_nSelectLastY = y - GetAbsPosY();
		m_bSelectNow = true;
		m_bSureSelected = false;
		SetCapture();
		return true;
	}
	if( bProcessed)
		return true;
	else
		return m_bNotTransparent;
}
//
//void CTextArea::SetTransparent( bool b)
//{
//	m_bNotTransparent = !b;
//}
//
//bool CTextArea::IsTransparent() const
//{
//	return !m_bNotTransparent;
//}

bool CTextArea::IsPointInMsgTransparent( int x, int y) const
{
	CComponent* pc = m_tall->GetComponentFromPoint( x, y, GetLineSpace());
	if( NULL != pc)
	{
		if ( !pc->IsTransparent())
			return false;
	}
	return IsTransparent();
}

bool CTextArea::OnLeftButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if( !INLINECLASS::TextAreaDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnLeftButtonUp, x, y, key))
		CComponent::OnLeftButtonUp( x, y, key);

	if( m_bSelectNow)
	{
		m_bSelectNow = false;
		ReleaseCapture();
		if( m_bSureSelected)
		{
			int nx = max( 0, x - GetAbsPosX());
			int ny = max( 0, y - GetAbsPosY());
			ResetAllSelect( nx, ny, true);
		}
		else
		{
			CancelAllSelect();
		}
	}
	if( IsCursorShow() && IsEnable()) 
		CKeyInput::GetInstance().SetFocus( *this);
	OnMouseSetCursorPosition( min( GetWidth() + GetAbsPosX(), max( GetAbsPosX(), x)), min( GetHeight() + GetAbsPosY(), max( GetAbsPosY(), y)));
	return m_bNotTransparent;
}

void CTextArea::SetEnable( bool bEnable)
{
	CComponent::SetEnable( bEnable);
	if( !bEnable)
		CKeyInput::GetInstance().ReleaseFocus( *this);
}


//bool CTextArea::OnLeftButtonDoubleClick( int x, int y, int key)
//{
//	if ( IsViewOnly())
//		return false;
//
//	if( !INLINECLASS::TextAreaDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnLeftButtonDoubleClick, x, y, key))
//		CComponent::OnLeftButtonDoubleClick( x, y, key);
//	return true;
//}

bool CTextArea::OnRightButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	bool bProcessed = INLINECLASS::TextAreaDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnRightButtonDown, x, y, key);
	if ( !bProcessed)
		CComponent::OnRightButtonDown( x, y, key);

	if( bProcessed)
		return true;
	else
		return m_bNotTransparent;
}

bool CTextArea::OnRightButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	bool bProcessed = INLINECLASS::TextAreaDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnRightButtonUp, x, y, key);
	if ( !bProcessed)
		CComponent::OnRightButtonUp( x, y, key);

	if( bProcessed)
		return true;
	else
		return m_bNotTransparent;
}

//bool CTextArea::OnRightButtonDoubleClick( int x, int y, int key)
//{
//	if ( IsViewOnly())
//		return false;
//
//	if( !INLINECLASS::TextAreaDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnRightButtonDoubleClick, x, y, key))
//		CComponent::OnRightButtonDoubleClick( x, y, key);
//	return true;
//}

bool CTextArea::OnMouseMove( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if( m_bSelectNow)
	{
		int nx = max( 0, x - GetAbsPosX());
		int ny = max( 0, y - GetAbsPosY());

		if( !m_bSureSelected)
		{
			int nCursorWidth = GetCursorWidth();
			m_bSureSelected = !( abs( m_nSelectLastX - nx) <= nCursorWidth && abs( m_nSelectLastY - ny) <= nCursorWidth);
		}
		if( m_bSureSelected)
		{
			ResetAllSelect( nx, ny);
			EnsureViewPos( nx, ny);
		}
		OnMouseSetCursorPosition( min( GetWidth() + GetAbsPosX(), max( GetAbsPosX(), x)), min( GetHeight() + GetAbsPosY(), max( GetAbsPosY(), y)));
	}

	CComponent* pc = m_tall->GetComponentFromPoint( x - GetAbsPosX(), y - GetAbsPosY(), GetLineSpace());
	if( NULL == pc)
	{
		if( m_pLastMouseIn)
		{
			m_pLastMouseIn->OnMouseLeave();
			m_pLastMouseIn = pc;
		}
	}
	else if( !pc->IsSameComponent( m_pLastMouseIn))
	{
		if( m_pLastMouseIn)
			m_pLastMouseIn->OnMouseLeave();
		m_pLastMouseIn = pc;
		m_pLastMouseIn->OnMouseEnter();
	}
	if( NULL == pc || !pc->OnMouseMove( x, y, key))
		CComponent::OnMouseMove( x, y, key);
	return true;
}

bool CTextArea::OnMouseWheel( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if( !INLINECLASS::TextAreaDispatchMessage::DispatchMouseMessage( *this, &CComponent::OnMouseWheel, x, y, key))
		CComponent::OnMouseWheel( x, y, key);
	return true;
}

void CTextArea::OnMouseLeave()
{
	if ( IsViewOnly())
		return;

	if( m_pLastMouseIn)
		m_pLastMouseIn = NULL;

	for( INLINECLASS::CTextAreaLineList::iterator it = m_tall->begin(), ite = m_tall->end(); it != ite; ++ it)
	{
		INLINECLASS::CTextAreaLine& tal = *it;
		for( INLINECLASS::CTextAreaLine::iterator itl = tal.begin(), itle = tal.end(); itl != itle; ++ itl)
		{
			CComponent& pc = **itl;
			pc.OnMouseLeave();
		}
	}
	CComponent::OnMouseLeave();
}

void CTextArea::OnTimer( const TIMERID& nid)
{
	if( nid == m_cursortimer)
	{
		m_bCursorShowing = !m_bCursorShowing;
		CTimer::ResetTimer( *this, m_cursortimer, GetCursorCycle());
		INVALIDATE();
	}
}

void CTextArea::SetCursorPos( int x, int y)
{
	m_nCursorX = x;
	m_nCursorY = y;
	CheckCursorPosition();
}

void CTextArea::SetCursorCharPos( int row, int col)
{
	if( IsCursorShow())
		m_eec->CheckCursorCharPosition( row, col, m_nCursorX, m_nCursorY, GetLineSpace(), GetDefaultFont(), IsPassword());
}

void CTextArea::GetCursorCharPos( int& row, int& col) const
{
	assert( IsCursorShow() && "只有在光标显示的时候才能够获取光标位置!");
	m_eec->GetCursorCharPosition( row, col);
}

void CTextArea::DrawCursor( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	draw.DrawPicture( GetCursor(), l + cx, t + cy, cw, ch, cx, cy, cw, ch);
}

void CTextArea::CheckCursorPosition()
{
	if( IsCursorShow())
		m_eec->CheckCursorPosition( m_nCursorX, m_nCursorY, GetLineSpace(), GetDefaultFont(), IsPassword());
}

void CTextArea::ShowCursor()
{
	ShowCursor( GetCursor());
}

void CTextArea::ShowCursor( const ImageParams& ip)
{
	CTextAreaProperties::ShowCursor( ip);
	if( -1 == ip.nBitmap)
		HideCursor();
//	else
//		CTimer::NewTimer( *this, GetCursorCycle(), m_cursortimer);		
}

void CTextArea::HideCursor()
{
	CTextAreaProperties::HideCursor();
	if( m_bCursorShowing)
	{
		assert( !m_cursortimer.IsNull());
		m_bCursorShowing = false;
		CTimer::CancelTimer( m_cursortimer);
		INVALIDATE();
	}
}

void CTextArea::OnMouseSetCursorPosition( int x, int y)
{
	if( m_bFucosed && IsCursorShow())
	{
		m_nCursorX = x - GetAbsPosX();
		m_nCursorY = y - GetAbsPosY();
		CheckCursorPosition();
		if( m_bCursorShowing)
			return;
		assert( !m_cursortimer.IsNull());
		OnTimer( m_cursortimer);
	}
}

void CTextArea::CancelAllSelect()
{
	m_eec->CancelAllSelect();
	for( INLINECLASS::CTextAreaLineList::iterator it = m_tall->begin(); it != m_tall->end(); ++ it)
	{
		INLINECLASS::CTextAreaLine& tal = *it;
		for( INLINECLASS::CTextAreaLine::iterator itl = tal.begin(); itl != tal.end(); ++ itl)
		{
			CComponent*& pc = *itl;
			CBlendEditElement* bee = dynamic_cast<CBlendEditElement*>( pc);
			if( NULL == bee) 
				continue;
			bee->SetSelectNone();
		} // for( INLINECLASS::CTextAreaLine::iterator itl = tal.begin(); itl != tal.end(); ++ itl)
	} // for( INLINECLASS::CTextAreaLineList::iterator it = m_tall->begin(); it != m_tall->end(); ++ it)
	INVALIDATE();
}

void CTextArea::ResetAllSelect( int x, int y, bool bSetBuffer)
{
	CancelAllSelect();

	bool	bReversed = false;
	int		sy = m_nSelectLastY;
	int		sx = m_nSelectLastX;

	if( y < sy)
	{
		std::swap( sy, y);
		std::swap( sx, x);
	}
	if( bSetBuffer)
		m_eec->SetSelect( sx, sy, x, y, GetLineSpace(), GetDefaultFont(), IsPassword());
	m_tall->SetSelect( sx, sy, x, y, GetLineSpace(), GetWidth());
}


bool CTextArea::CopyToClipboard()
{
	std::wstring		strTemp;
	m_eec->GetSelectString( strTemp);
	return CClipBoard::GetInstace().SetString( strTemp);
}

bool CTextArea::OnKeyInput( UINT uMsg, WPARAM wParam, LPARAM lParam)
{
	if( WM_CHAR == uMsg)
	{
		switch( wParam)
		{
		case 3: // ctrl + c
			CopyToClipboard();
			return true;
		default:
			return false;
		}
	}
	return false;
}

void CTextArea::EnsureViewCursor()
{
	EnsureViewPos( m_nCursorX, m_nCursorY + GetCursorHeight());
}

void CTextArea::EnsureViewPos( int x, int y)
{
	CScrollPanel* sp = dynamic_cast<CScrollPanel*>( GetParent());
	if( NULL == sp)
		return;
	{
		CScrollBar* sb = sp->GetHorizontalScrollBar();
		int pos = sb->GetValuePos();
		if( x < pos )
			sb->SetValuePos( x);
		else
		{
			int max = sb->GetValueMax();
			int w = GetWidth() - max;
			if( x > ( w + pos))
				sb->SetValuePos( x - w + 1);
		}
	}
	{
		CScrollBar* sb = sp->GetVerticalScrollBar();
		int pos = sb->GetValuePos();
		if( y < pos )
			sb->SetValuePos( y);
		else
		{
			int max = sb->GetValueMax();
			int h = GetHeight() - max;
			if( y > ( h + pos))
				sb->SetValuePos( y - h + 1);
		}

	}
}

bool CTextArea::IsHasSelect()
{
	if( IsSelectAllow())
		return m_eec->IsHasSelect();
	else
		return false;
}

bool CTextArea::EraseAll()
{
	bool b = m_eec->EraseAll();
	if( b)
	{
		RelayoutView();
	}
	return b;
}

void CTextArea::OnButtonClick( CAbstractButton& c)
{
	if ( IsViewOnly())
		return;

	CEditButtonElement* ebe = dynamic_cast<CEditButtonElement*>( &c);
	if( NULL == ebe)
		return;
	m_ButtonNotifySet->NotifyAll( *this, &CTextAreaButtonClickNotify::OnTextAreaButtonClick, ebe->GetNotifyString());
}

void CTextArea::OnLeftButtonDown( CComponent& c, int x, int y, int key)
{
	if ( IsViewOnly())
		return;

	CBlendEditElement* bee = dynamic_cast<CBlendEditElement*>( &c);
	if( bee)
	{
		const wchar_t* szInfo = bee->GetInfo();
		if( szInfo && szInfo[0])
			m_TextNotifySet->NotifyAll( *this, &CTextAreaTextClickNotify::OnTextAreaTextClick, szInfo);
		return;
	}

	CEditIconElement* eie = dynamic_cast<CEditIconElement*>( &c);
	if ( eie)
	{
		m_MouseNotifySet->NotifyAll( *this, &CTextAreaElementMouseEventNotify::OnTextAreaElementLeftButtonDown, eie->GetNotifyString());
		return;
	}

	CEditAnimateIconElement* eaie = dynamic_cast<CEditAnimateIconElement*>( &c);
	if ( eaie)
	{
		m_MouseNotifySet->NotifyAll( *this, &CTextAreaElementMouseEventNotify::OnTextAreaElementLeftButtonDown, eaie->GetNotifyString());
		return;
	}

	CEditEmotionElement* eee = dynamic_cast<CEditEmotionElement*>( &c);
	if ( eee)
	{
		m_MouseNotifySet->NotifyAll( *this, &CTextAreaElementMouseEventNotify::OnTextAreaElementLeftButtonDown, eee->GetNotifyString());
		return;
	}
}

void CTextArea::OnRightButtonDown( CComponent& c, int x, int y, int key)
{
	if ( IsViewOnly())
		return;

	CBlendEditElement* bee = dynamic_cast<CBlendEditElement*>( &c);
	if( bee)
	{
		const wchar_t* szInfo = bee->GetInfo();
		if( szInfo && szInfo[0])
			m_TextNotifySet->NotifyAll( *this, &CTextAreaTextClickNotify::OnTextAreaTextRightClick, szInfo);
		return;
	}

	CEditButtonElement* ebe = dynamic_cast<CEditButtonElement*>( &c);
	if ( ebe)
	{
		m_MouseNotifySet->NotifyAll( *this, &CTextAreaElementMouseEventNotify::OnTextAreaElementRightButtonDown, ebe->GetMouseNotifyString());
		return;
	}

	CEditIconElement* eie = dynamic_cast<CEditIconElement*>( &c);
	if ( eie)
	{
		m_MouseNotifySet->NotifyAll( *this, &CTextAreaElementMouseEventNotify::OnTextAreaElementRightButtonDown, eie->GetNotifyString());
		return;
	}

	CEditAnimateIconElement* eaie = dynamic_cast<CEditAnimateIconElement*>( &c);
	if ( eaie)
	{
		m_MouseNotifySet->NotifyAll( *this, &CTextAreaElementMouseEventNotify::OnTextAreaElementRightButtonDown, eaie->GetNotifyString());
		return;
	}

	CEditEmotionElement* eee = dynamic_cast<CEditEmotionElement*>( &c);
	if ( eee)
	{
		m_MouseNotifySet->NotifyAll( *this, &CTextAreaElementMouseEventNotify::OnTextAreaElementRightButtonDown, eee->GetNotifyString());
		return;
	}
}

int CTextArea::GetTotalWidth() const
{
	return m_nTotalWidth;
}

int CTextArea::GetAllLinesHeight() const
{
	int h = 0;
	for( INLINECLASS::CTextAreaLineList::const_iterator it = m_tall->begin(); it != m_tall->end(); ++ it)
	{
		const INLINECLASS::CTextAreaLine& tal = *it;
		h += tal.m_nHeight + GetLineSpace();
	}
	return h;
}

void CTextArea::SetAllSelected()
{
	m_nSelectLastX = 0;
	m_nSelectLastY = 0;
	ResetAllSelect( GetAbsPosR(), GetAbsPosB(), true);
	OnMouseSetCursorPosition( GetAbsPosR(), GetAbsPosB());
}

bool CTextArea::Encode( CAbstarctTextEncoder& fte)
{
	CDecoderTextPieceList tpl;
	m_eec->EncodeTextPieceList( tpl);
	fte.AddTextPieces( tpl);
	return true;
}

void CTextArea::SetExistTextAlpha( unsigned char alpha)
{
	for ( INLINECLASS::CTextAreaLineList::iterator it = m_tall->begin(), ite = m_tall->end(); it != ite; ++ it)
	{
		INLINECLASS::CTextAreaLine& tal = *it;
		for ( INLINECLASS::CTextAreaLine::iterator ital = tal.begin(), itale = tal.end(); ital != itale; ++ ital)
		{
			if ( CBlendEditElement* pc = dynamic_cast<CBlendEditElement*>( *ital))
			{
				COLOR a = alpha << 24;
				COLOR clr = pc->GetTextColor() & 0x00ffffff | a;
				COLOR clr2 = pc->GetTextGradualColor() & 0x00ffffff | a;
				COLOR clrs = pc->GetSelectColor() & 0x00ffffff | a;
				COLOR clrs2 = pc->GetSelectGradualColor() & 0x00ffffff | a;
				COLOR clre = pc->GetTextEffectColor() & 0x00ffffff | a;
				COLOR clrse = pc->GetSelectEffectColor() & 0x00ffffff | a;
				COLOR clrsb = pc->GetSelectBKColor() & 0x00ffffff | a;
				pc->SetTextColor( clr, clr2, pc->GetTextEffectType(), clre);
				pc->SetSelectColor( clrs, clrs2, clrsb, pc->GetSelectEffectType(), clrse);
			}
		}
	}
	INVALIDATE();
}

// 判读是否有输入焦点
bool CTextArea::IsFocused() const
{
	return m_bFucosed;
}

// 设光标到最前
void CTextArea::SetCursorToBegin()
{
	SetCursorPos( 0, 0);
}

// 设光标到最后
void CTextArea::SetCursorToEnd()
{
	SetCursorPos( GetWidth(), GetHeight());
}


void CTextArea::AppendText( const wchar_t* szText, int backgroundEffectId)
{
	if ( szText)
	{
		CDecoderTextPieceList		tpl;
		m_decoder->Decode( szText, tpl);
		m_eec->AppendToEnd( tpl, backgroundEffectId);
		RelayoutView(false);
		SetCursorToEnd();
	}
}

void CTextArea::SetTextAreaBackGroundEffect( CTextAreaBackGroundEffect& tabge)
{
	m_pBackgroundEffectDraw = &tabge;
}

void CTextArea::RemoveTextAreaBackGroundEffect( CTextAreaBackGroundEffect& tabge)
{
	if ( m_pBackgroundEffectDraw == &tabge)
		m_pBackgroundEffectDraw = NULL;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCombinationTextAreaAllocator::CCombinationTextAreaAllocator( CAbstarctTextDecoder& decoder)
	: m_Decoder( decoder.Clone())
{
}

CCombinationTextAreaAllocator::~CCombinationTextAreaAllocator()
{
	m_Decoder->Delete();
}

CAbstarctTextDecoder& CCombinationTextAreaAllocator::GetTextDecoder()
{
	return *m_Decoder;
}

CScrollPanel* CCombinationTextAreaAllocator::NewScrollPanel()
{
	return new CScrollPanel();
}

CTextArea* CCombinationTextAreaAllocator::NewTextArea()
{
	return new CTextArea( GetTextDecoder());
}

void CCombinationTextAreaAllocator::DeleteComponent( CComponent* pc)
{
	delete pc;
}

CCombinationTextAreaAllocator& CCombinationTextAreaAllocator::GetInstance()
{
	return INLINECLASS::g_DefaultCombinationTextAreaAllocator;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCombinationTextArea::CCombinationTextArea( CCombinationTextAreaAllocator& alloc)
	: m_alloc( &alloc)
{
	m_sp = m_alloc->NewScrollPanel();
	m_ta = m_alloc->NewTextArea();

	m_sp->SetAutoClientWidth( true);
	m_sp->SetComponent( *m_ta);
}

CCombinationTextArea::~CCombinationTextArea()
{
	m_alloc->DeleteComponent( m_sp);
	m_alloc->DeleteComponent( m_ta);
}

CScrollPanel& CCombinationTextArea::GetScrollPanel()
{
	return *m_sp;
}

CTextArea& CCombinationTextArea::GetTextArea()
{
	return *m_ta;
}

void CCombinationTextArea::SetSize( int w, int h)
{
	m_sp->SetSize( w, h);
	m_ta->SetWidth( w - m_sp->GetVerticalScrollBar()->GetWidth());
	m_ta->SetMinWidthHeight( h);
}

void CCombinationTextArea::SetCombinationSize( int w, int h)
{
	SetSize( w, h);
	if ( CComponent* pc = dynamic_cast<CComponent*>(m_ta))
		pc->SetSize( pc->GetWidth(), h);
}

void CCombinationTextArea::SetPos( int x, int y)
{
	m_sp->SetPos( x, y);
}

void CCombinationTextArea::ShowCursor( const ImageParams& ip)
{
	m_ta->ShowCursor( ip);
}

void CCombinationTextArea::AppendText( const wchar_t* szText)
{
	m_ta->AppendText( szText);
}

void CCombinationTextArea::AppendText( const wchar_t* szText, int nBackgroundEffect)
{
	m_ta->AppendText( szText, nBackgroundEffect);
}

void CCombinationTextArea::SetSelectAllow( bool b)
{
	m_ta->SetSelectAllow( b);
}

void CCombinationTextArea::SetSingleLine( bool b)
{
	m_ta->SetSingleLine( b);
}

void CCombinationTextArea::SetViewOnly( bool bViewOnly)
{
	m_sp->SetViewOnly( bViewOnly);
}

bool CCombinationTextArea::IsViewOnly() const
{
	return m_sp->IsViewOnly();
}

CCombinationTextArea::operator CComponent*()
{
	return m_sp;
}
CCombinationTextArea::operator CComponent&()
{
	return *m_sp;
}

CComponent* CCombinationTextArea::operator&()
{
	return m_sp;
}

void CCombinationTextArea::SetComponentName( const wchar_t* name)
{
	m_sp->SetComponentName( name);
	m_ta->SetComponentName( name);
}

const wchar_t* CCombinationTextArea::GetComponentName() const
{
	return m_sp->GetComponentName();
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CScrollSingleLineTextArea::CScrollSingleLineTextArea( CAbstarctTextDecoder& decoder /*= CFormatTextDecoder()*/)
: m_taimen( NULL), m_tabcn( NULL), m_tatcn( NULL), m_decoder( decoder.Clone()), m_ptexts( new INLINECLASS::CTextVector()), m_pstap( new INLINECLASS::CScrollTextAreaParamVector())
, m_limite( 50), m_space( 10), m_interval( 10), m_step( 1), m_loop( true), m_bOneTextMode( true)
{
}

CScrollSingleLineTextArea::~CScrollSingleLineTextArea()
{
	Stop();
	delete m_decoder;
	delete m_ptexts;
	delete m_pstap;
}

void CScrollSingleLineTextArea::SetTextNumberLimite( int n)
{
	m_limite = n;
	if ( (int)m_ptexts->size() > n)
	{
		m_ptexts->erase( m_ptexts->begin(), m_ptexts->begin() + ( (int)m_ptexts->size() - n));
	}
}

int  CScrollSingleLineTextArea::GetTextNumberLimite() const
{
	return m_limite;
}

void CScrollSingleLineTextArea::SetSpaceBetweenScrollTexts( int n)
{
	m_space = n;
}

int  CScrollSingleLineTextArea::GetSpaceBetweenScrollTexts() const
{
	return m_space;
}

void CScrollSingleLineTextArea::SetScrollInterval( int n)
{
	m_interval = n;
}

int  CScrollSingleLineTextArea::GetScrollInterval() const
{
	return m_interval;
}

void CScrollSingleLineTextArea::SetScrollStep( int n)
{
	m_step = n;
}

int  CScrollSingleLineTextArea::GetScrollStep() const
{
	return m_step;
}

void CScrollSingleLineTextArea::SetLoopScroll( bool bLoop /*= true*/)
{
	m_loop = bLoop;
}

bool CScrollSingleLineTextArea::IsLoopScroll() const
{
	return m_loop;
}

void CScrollSingleLineTextArea::InsertText( const wchar_t* text)
{
	if ( (int)m_ptexts->size() < m_limite)
	{
		m_ptexts->push_back( text);
	}
}

void CScrollSingleLineTextArea::SetText( int n, const wchar_t* text)
{
	m_ptexts->at( n) = text;
}

void CScrollSingleLineTextArea::EraseText( int n)
{
	if ( n >= 0 && n < ( int)m_ptexts->size())
	{
		INLINECLASS::CScrollTextAreaParamVector::iterator it = std::find( m_pstap->begin(), m_pstap->end(), m_ptexts->at( n).c_str());
		if ( it == m_pstap->end())
			m_ptexts->erase( m_ptexts->begin() + n);
	}
}

void CScrollSingleLineTextArea::ClearText()
{
	Stop();
	m_ptexts->clear();
}

int  CScrollSingleLineTextArea::GetTextCount() const
{
	return (int)m_ptexts->size();
}

const wchar_t* CScrollSingleLineTextArea::GetText( int n) const
{
	return m_ptexts->at( n).c_str();
}

void CScrollSingleLineTextArea::SetOneTextMode( bool bOneTextMode)
{
	m_bOneTextMode = bOneTextMode;
}

bool CScrollSingleLineTextArea::IsOneTextMode() const
{
	return m_bOneTextMode;
}

void CScrollSingleLineTextArea::Start( int n)
{
	Stop();
	if ( !m_ptexts->empty())
	{
		m_pstap->reserve( m_limite);
		m_pstap->push_back( INLINECLASS::CScrollTextAreaParam());
		SetScrollTextAreaParam( n, m_pstap->back());
		OnTimer( m_timerid);
		CTimer::NewTimer( *this, m_interval, m_timerid);
	}
}

void CScrollSingleLineTextArea::Stop()
{
	CTimer::CancelTimer( m_timerid);
	FreeAllTextDraw();
}

void CScrollSingleLineTextArea::OnTimer( const TIMERID& nid)
{
	if ( nid == m_timerid)
	{
		if ( !m_pstap->empty())
		{
			if ( m_bOneTextMode && m_pstap->size() == 1) // 只有一条滚动文本, 检查其长度, 如长度小于控件宽度则是一种特例
			{
				INLINECLASS::CScrollTextAreaParam& stap = m_pstap->back();
				int nw = GetWidth() - stap.ta->GetWidth();
				if( nw >= 0) // 文本长度小于控件宽度, 不必滚动直接显示
				{
//					int tw = CTextAreaProperties::GetStringWidth( stap.ta->getdef)
					stap.posx = nw / 2;
					stap.width = GetWidth() - nw;
					return;
				}
			}

			// 定时调整各文本控件的位置以实现滚动动作
			int lastdel = -1;
			for ( INLINECLASS::CScrollTextAreaParamVector::iterator it = m_pstap->begin(); it != m_pstap->end(); )
			{
				INLINECLASS::CScrollTextAreaParam& stap = *it;
				stap.posx -= m_step;
				if ( stap.posx < 0)
				{
					stap.posx = 0;
					stap.offset += m_step;
				}
				stap.width = min( stap.ta->GetWidth() - stap.offset, GetWidth() - stap.posx);

				if ( stap.width <= 0)
				{
					lastdel = m_ptexts->GetIndex( (*it).text);
					delete ( *it).ta;
					it = m_pstap->erase( it);
				}
				else 
					++ it;
			}

			int posx, n;
			bool bNextText = false;

			if ( !m_pstap->empty()) // 如存在滚动中的文本, 则在最后一个文本后检查能否继续插入新文本
			{
				INLINECLASS::CScrollTextAreaParam& last = m_pstap->back();
				posx = last.posx + last.width + m_space;;
				if ( posx < GetWidth())
				{
					n = m_ptexts->GetIndex( last.text);
					n = ( m_loop && n + 1 >= (int)m_ptexts->size()) ? 0 : n + 1;
					bNextText = n < ( int)m_ptexts->size();
				}
			}
			else // 如不存在滚动文本, 取下一条进行滚动, 如已经到达末尾, 则检查循环规则
			{
				posx = GetWidth();
				n = ( m_loop && lastdel + 1 >= (int)m_ptexts->size()) ? 0 : lastdel + 1;
				bNextText = n < ( int)m_ptexts->size();
			}

			if ( bNextText)
			{
				m_pstap->push_back( INLINECLASS::CScrollTextAreaParam());
				INLINECLASS::CScrollTextAreaParam& stap = m_pstap->back();

				SetScrollTextAreaParam( n, stap);

				stap.posx = posx;
				stap.width = GetWidth() - posx;
			}

			if ( !m_timerid.IsNull())
				CTimer::ResetTimer( *this, m_timerid, m_interval);

			INVALIDATE();
		}
	}

}

void CScrollSingleLineTextArea::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CBKComponent::Draw( draw, l, t, cx, cy, cw, ch);
	for ( INLINECLASS::CScrollTextAreaParamVector::iterator it = m_pstap->begin(); it != m_pstap->end(); ++ it)
	{
		INLINECLASS::CScrollTextAreaParam& stap = *it;
		stap.ta->SetPos( l + stap.posx - stap.offset, t + max( 0, (ch - CTextDrawManager::GetInstance().GetFontHeightWithoutUnderline( stap.ta->GetDefaultFont())) >> 1 ));
		stap.ta->Draw( draw, l + stap.posx - stap.offset, t + max( 0, (ch - CTextDrawManager::GetInstance().GetFontHeightWithoutUnderline( stap.ta->GetDefaultFont())) >> 1 ), stap.offset, cy, stap.width, ch);
	}
}

void CScrollSingleLineTextArea::SetScrollTextAreaParam( int n, INLINECLASS::CScrollTextAreaParam& stap)
{
	if ( n >= 0 && n < (int)m_ptexts->size())
	{
		stap.ta = new CTextArea( false, *m_decoder);
		stap.ta->SetTransparent( IsTransparent());

		stap.ta->SetDefaultFont( GetDefaultFont());
		stap.ta->SetDefaultTextColor( GetDefaultTextColor(), GetDefaultEffectType(), GetDefaultEffectColor());
		stap.ta->SetDefaultSelectColor( GetDefaultSelectTextColor(), GetDefaultSelectEffectType(), GetDefaultSelectEffectColor());
		//stap.ta->HideCursor();
		stap.ta->SetSingleLine();
		stap.ta->SetSelectAllow( false);
		stap.text = m_ptexts->at( n);
		stap.ta->AppendText( stap.text.c_str());
		stap.offset = 0;
		stap.posx = GetWidth();
		stap.width = 0;

		if ( m_taimen) stap.ta->TextAreaElementMouseEventNotifyAdd( *m_taimen);
		if ( m_tabcn) stap.ta->TextAreaButtonClickNotifyAdd( *m_tabcn);
		if ( m_tatcn) stap.ta->TextAreaTextClickNotifyAdd( *m_tatcn);
	}
}

void CScrollSingleLineTextArea::FreeAllTextDraw()
{
	for ( INLINECLASS::CScrollTextAreaParamVector::iterator it = m_pstap->begin(); it != m_pstap->end(); ++ it)
	{
		CTextArea* pc = ( *it).ta;
		if ( pc)
		{
			pc->EraseAll();
			delete pc;
			( *it).ta = NULL;
		}
	}
	m_pstap->clear();
}

void CScrollSingleLineTextArea::TextAreaButtonClickNotifyAdd( CTextAreaButtonClickNotify& tabcn)
{
	m_tabcn = &tabcn;
}

void CScrollSingleLineTextArea::TextAreaButtonClickNotifyRemove( CTextAreaButtonClickNotify& tabcn)
{
	if ( m_tabcn->IsSameNotify( tabcn))
	{
		for ( INLINECLASS::CScrollTextAreaParamVector::iterator it = m_pstap->begin(); it != m_pstap->end(); ++ it)
		{
			CTextArea* pc = ( *it).ta;
			pc->TextAreaButtonClickNotifyRemove( *m_tabcn);
		}
		m_tabcn = NULL;
	}
}

void CScrollSingleLineTextArea::TextAreaTextClickNotifyAdd( CTextAreaTextClickNotify& tatcn)
{
	m_tatcn = &tatcn;
}

void CScrollSingleLineTextArea::TextAreaTextClickNotifyRemove( CTextAreaTextClickNotify& tatcn)
{
	if ( m_tatcn->IsSameNotify( tatcn))
	{
		for ( INLINECLASS::CScrollTextAreaParamVector::iterator it = m_pstap->begin(); it != m_pstap->end(); ++ it)
		{
			CTextArea* pc = ( *it).ta;
			pc->TextAreaTextClickNotifyRemove( *m_tatcn);
		}
		m_tatcn = NULL;
	}
}

void CScrollSingleLineTextArea::TextAreaElementMouseEventNotifyAdd( CTextAreaElementMouseEventNotify& taimen)
{
	m_taimen = &taimen;
}

void CScrollSingleLineTextArea::TextAreaElementMouseEventNotifyRemove( CTextAreaElementMouseEventNotify& taimen)
{
	if ( m_taimen->IsSameNotify( &taimen))
	{
		for ( INLINECLASS::CScrollTextAreaParamVector::iterator it = m_pstap->begin(); it != m_pstap->end(); ++ it)
		{
			CTextArea* pc = ( *it).ta;
			pc->TextAreaElementMouseEventNotifyRemove( *m_taimen);
		}
		m_taimen = NULL;
	}
}

bool CScrollSingleLineTextArea::OnRightButtonDown(int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	for ( INLINECLASS::CScrollTextAreaParamVector::iterator it = m_pstap->begin(); it != m_pstap->end(); ++ it)
	{
		CTextArea* pc = ( *it).ta;
		pc->OnRightButtonDown( x, y, key);
	}
	return CBKComponent::OnRightButtonDown( x, y, key);
}

bool CScrollSingleLineTextArea::OnLeftButtonDown(int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	bool	b = false;
	for ( INLINECLASS::CScrollTextAreaParamVector::iterator it = m_pstap->begin(); it != m_pstap->end(); ++ it)
	{
		CTextArea* pc = ( *it).ta;
		b |= pc->OnLeftButtonDown( x, y, key);
	}
	return b || CBKComponent::OnLeftButtonDown( x, y, key);
}

void CScrollSingleLineTextArea::SetTransparent( bool bTrans)
{
	for ( INLINECLASS::CScrollTextAreaParamVector::iterator it = m_pstap->begin(); it != m_pstap->end(); ++ it)
	{
		CTextArea* pc = ( *it).ta;
		pc->SetTransparent( bTrans);
	}
	CBKComponent::SetTransparent( bTrans);
}

bool CScrollSingleLineTextArea::IsShowEmpty() // 当前是否有显示 by llq 09.08.03
{ 
	if (!m_pstap)
		return true;
	if (m_pstap->empty())
		return true;
	return false;
}

bool CScrollSingleLineTextArea::IsCanRoll(int n) // 第n个是否可以滚动 by llq 09.08.03
{
	if (IsShowEmpty())
		return false;

	assert(!(n < 0 || n >= (int)m_pstap->size()));
	if (n < 0 || n >= (int)m_pstap->size())
		return false;

	INLINECLASS::CScrollTextAreaParam& stap = (*m_pstap)[n];
	int nw = GetWidth() - stap.ta->GetWidth();
	if( nw >= 0) // 文本长度小于控件宽度, 不必滚动直接显示
	{
		//					int tw = CTextAreaProperties::GetStringWidth( stap.ta->getdef)
		stap.posx = nw / 2;
		stap.width = GetWidth() - nw;
		return false;
	}
	return true;
}


} // namespace GXWINDOW { 
