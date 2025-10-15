#include "gxwindowdef.h"

namespace GXWINDOW { 
namespace INLINECLASS
{
struct CTextState 
{
	int m_nText;
	COLOR	m_clrState;
	COLOR	m_clrStateGradual;
	COLOR	m_clrStateEffect;
	unsigned char m_uStateEffectType;
	CTextState() : m_nText( -1), m_clrState( 0), m_clrStateGradual( 0), m_clrStateEffect( 0), m_uStateEffectType( FET_NONE) {}
};

class CTextStateMap : public std::map<int, CTextState>
{
public:
	CTextStateMap() {}
	virtual ~CTextStateMap() {}
};

class CTextResMap : public std::map<int, std::wstring>
{
public:
	CTextResMap() {}
	virtual ~CTextResMap() {}
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

class CScrollTextParam 
{
public:
	CTextComponent*		tc;			// 用于显示文字
	int					posx;		// tc在滚动文字控件中的开始显示位置 0 <= tc <= CScrollTextComponent.GetWidth()
	int					offset;		// tc自身显示的开始位置 0 <= tc <= tc.GetWidth()
	int					width;		// tc显示的宽度 0 <= width <= CScrollTextComponent.GetWidth()
	CScrollTextParam() : tc( NULL), posx( 0), offset( 0), width( 0) {}
	CScrollTextParam( const CScrollTextParam& src) : tc( src.tc), posx( src.posx), offset( src.offset), width( src.width) {}
	virtual ~CScrollTextParam() {} 
	bool operator==( const wchar_t* text) const { return tc ? 0 == _wcsicmp( tc->GetComponentText(), text) : false; }
	CScrollTextParam& operator=( const CScrollTextParam& src)
	{
		if ( this == &src)
			return *this;
		this->tc = src.tc;
		this->posx = src.posx;
		this->offset = src.offset;
		this->width = src.width;
		return *this;
	}
};

class CScrollTextParamVector : public std::vector<CScrollTextParam>
{
public:
	CScrollTextParamVector() {}
	virtual ~CScrollTextParamVector() {}
};

}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextResourceManager::CTextResourceManager() : m_ptrm( new INLINECLASS::CTextResMap())
{

}

CTextResourceManager::~CTextResourceManager()
{
	delete m_ptrm;
}

void CTextResourceManager::SetText(int n, const wchar_t* szText)
{
	if ( NULL == szText || 0 == wcslen( szText))
		return;

	m_ptrm->insert( std::make_pair( n, szText));
}

const wchar_t* CTextResourceManager::GetText(int n) const
{
	INLINECLASS::CTextResMap::iterator it = m_ptrm->find( n);
	if ( it != m_ptrm->end())
		return it->second.c_str();

	return L"";
}

CTextResourceManager& CTextResourceManager::GetInstance()
{
	static CTextResourceManager s_TextResMgr;
	return s_TextResMgr;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	CTextDrawStatManager::CTextDrawStatManager() : m_nNormal( -1), m_nMouseIn( -1), m_nDisable( -1), m_nFont( 0), m_clrNomal( MakeColor( 255, 255, 255)), m_clrNomalGradual( MakeColor( 255, 255, 255)),
		m_clrMouseIn( MakeColor( 0, 0, 255)), m_clrMouseInGradual( MakeColor( 0, 0, 255)), m_clrDisable( MakeColor( 100, 100, 100)), m_clrDisableGradual( MakeColor( 0, 0, 255)), m_clrNormalEffect( 0), m_clrMouseInEffect( 0), m_clrDisableEffect( 0),
		m_uNormalEffect( FET_NONE), m_uMouseInEffect( FET_NONE), m_uDisableEffect( FET_NONE)
{

}

CTextDrawStatManager::~CTextDrawStatManager()
{

}

void CTextDrawStatManager::SetFont(int n, CTextDrawManager* pdm)
{
	m_nFont = n;
	FreeAllTextDraw( pdm);
}

int CTextDrawStatManager::GetFont() const
{
	return m_nFont;
}

void CTextDrawStatManager::SetNormalColor(COLOR clr, CTextDrawManager* pdm, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if ( m_clrNomal == clr && m_clrNormalEffect == clrEffect && m_uNormalEffect == type && ( bGradualColor ? clrGradual == m_clrNomalGradual : true))
		return;
	m_clrNomal = clr;
	m_clrNomalGradual = bGradualColor ? clrGradual : clr;
	m_uNormalEffect = type;
	m_clrNormalEffect = clrEffect;
	FreeNormalTextDraw( pdm);
}

COLOR CTextDrawStatManager::GetNormalColor() const
{
	return m_clrNomal;
}

COLOR CTextDrawStatManager::GetNormalGradualColor() const
{
	return m_clrNomalGradual;
}

void CTextDrawStatManager::SetMouseInColor(COLOR clr, CTextDrawManager* pdm, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if ( m_clrMouseIn == clr && m_clrMouseInEffect == clrEffect && m_uMouseInEffect == type && ( bGradualColor ? clrGradual == m_clrNomalGradual : true))
		return;
	m_clrMouseIn = clr;
	m_clrMouseInGradual = bGradualColor ? clrGradual : clr;
	m_uMouseInEffect = type;
	m_clrMouseInEffect = clrEffect;
	FreeMouseInTextDraw( pdm);
}

COLOR CTextDrawStatManager::GetMouseInColor() const
{
	return m_clrMouseIn;
}

COLOR CTextDrawStatManager::GetMouseInGradualColor() const
{
	return m_clrMouseInGradual;
}

void CTextDrawStatManager::SetDisableColor(COLOR clr, CTextDrawManager* pdm, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if ( m_clrDisable == clr && m_clrDisableEffect == clrEffect && m_uDisableEffect == type && ( bGradualColor ? clrGradual == m_clrNomalGradual : true))
		return;
	m_clrDisable = clr;
	m_clrDisableGradual = bGradualColor ? clrGradual : clr;
	m_uDisableEffect = type;
	m_clrDisableEffect = clrEffect;
	FreeDisableTextDraw( pdm);
}

COLOR CTextDrawStatManager::GetDisableColor() const
{
	return m_clrDisable;
}

COLOR CTextDrawStatManager::GetDisableGradualColor() const
{
	return m_clrDisableGradual;
}

void CTextDrawStatManager::DrawNormal(CDraw& draw, CTextDrawManager* pdm, const wchar_t* text, int l, int t, int cx, int cy, int cw, int ch)
{
	if( -1 == m_nNormal)
		m_nNormal = pdm->NewTextDraw( text, m_nFont, m_clrNomal, m_clrNomalGradual, 0, m_uNormalEffect, m_clrNormalEffect);
	pdm->Draw( m_nNormal, l, t, cx, cy, cw, ch);
}

void CTextDrawStatManager::DrawMouseIn(CDraw& draw, CTextDrawManager* pdm, const wchar_t* text, int l, int t, int cx, int cy, int cw, int ch)
{
	if( -1 == m_nMouseIn)
		m_nMouseIn = pdm->NewTextDraw( text, m_nFont, m_clrMouseIn, m_clrMouseInGradual, 0, m_uMouseInEffect, m_clrMouseInEffect);
	pdm->Draw( m_nMouseIn, l, t, cx, cy, cw, ch);
}

void CTextDrawStatManager::DrawDisable(CDraw& draw, CTextDrawManager* pdm, const wchar_t* text, int l, int t, int cx, int cy, int cw, int ch)
{
	if( -1 == m_nDisable)
		m_nDisable = pdm->NewTextDraw( text, m_nFont, m_clrDisable, m_clrDisableGradual, 0, m_uDisableEffect, m_clrDisableEffect);
	pdm->Draw( m_nDisable, l, t, cx, cy, cw, ch);
}

void CTextDrawStatManager::FreeNormalTextDraw(CTextDrawManager* pdm)
{
	if( -1 != m_nNormal)
		pdm->FreeTextDraw( m_nNormal);
	m_nNormal = -1;
}

void CTextDrawStatManager::FreeMouseInTextDraw(CTextDrawManager* pdm)
{
	if( -1 != m_nMouseIn)
		pdm->FreeTextDraw( m_nMouseIn);
	m_nMouseIn = -1;
}

void CTextDrawStatManager::FreeDisableTextDraw(CTextDrawManager* pdm)
{
	if( -1 != m_nDisable)
		pdm->FreeTextDraw( m_nDisable);
	m_nDisable = -1;
}

void CTextDrawStatManager::FreeAllTextDraw(CTextDrawManager* pdm)
{
	FreeNormalTextDraw( pdm);
	FreeMouseInTextDraw( pdm);
	FreeDisableTextDraw( pdm);
}

FONTEFEECTFLAG CTextDrawStatManager::GetNormalEffect() const
{
	return m_uNormalEffect;
}

FONTEFEECTFLAG CTextDrawStatManager::GetDisableEffect() const
{
	return m_uDisableEffect;
}

FONTEFEECTFLAG CTextDrawStatManager::GetMouseInEffect() const
{
	return m_uMouseInEffect;
}

COLOR CTextDrawStatManager::GetNormalEffectColor() const
{
	return m_clrNormalEffect;
}

COLOR CTextDrawStatManager::GetDisableEffectColor() const
{
	return m_clrDisableEffect;
}

COLOR CTextDrawStatManager::GetMouseInEffectColor() const
{
	return m_clrMouseInEffect;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCustomeTextDrawStatManager::CCustomeTextDrawStatManager() : m_ptsm( new INLINECLASS::CTextStateMap())
{
}

CCustomeTextDrawStatManager::~CCustomeTextDrawStatManager()
{
	delete m_ptsm;
}

void CCustomeTextDrawStatManager::SetFont( int n, CTextDrawManager* pdm)
{
	m_nFont = n;
	FreeAllTextDraw( pdm);
}

int  CCustomeTextDrawStatManager::GetFont() const
{
	return m_nFont;
}

void CCustomeTextDrawStatManager::SetTextStateColor( int nIndex, COLOR clr, CTextDrawManager* pdm, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	INLINECLASS::CTextState& ts = (*m_ptsm)[nIndex];
	if ( ts.m_clrState == clr && ts.m_clrStateEffect == clrEffect && ts.m_uStateEffectType == type)
		return;
	ts.m_clrState = clr;
	ts.m_clrStateGradual = bGradualColor ? clrGradual : clr;
	ts.m_clrStateEffect = clrEffect;
	ts.m_uStateEffectType = type;
	FreeTextDraw( nIndex, pdm);
}

COLOR CCustomeTextDrawStatManager::GetTextStateColor( int nIndex) const
{
	INLINECLASS::CTextStateMap::iterator it = m_ptsm->find( nIndex);
	assert( it != m_ptsm->end());
	return it->second.m_clrState;
}

bool CCustomeTextDrawStatManager::DrawTextState( int nIndex, CDraw& draw, CTextDrawManager* pdm, const wchar_t* text, int l, int t, int cx, int cy, int cw, int ch)
{
	INLINECLASS::CTextStateMap::iterator it = m_ptsm->find( nIndex);
	if ( it == m_ptsm->end())
		return false;

	if( -1 == it->second.m_nText)
	{
		INLINECLASS::CTextState& ts = it->second;
		ts.m_nText = pdm->NewTextDraw( text, m_nFont, ts.m_clrState, ts.m_clrStateGradual, 0, ts.m_uStateEffectType, ts.m_clrStateEffect);
	}
	pdm->Draw( it->second.m_nText, l, t, cx, cy, cw, ch);

	return true;
}

void CCustomeTextDrawStatManager::FreeTextDraw( int nIndex, CTextDrawManager* pdm)
{
	INLINECLASS::CTextStateMap::iterator it = m_ptsm->find( nIndex);
	assert( it != m_ptsm->end());
	if( -1 != it->second.m_nText)
		pdm->FreeTextDraw( it->second.m_nText);
	it->second.m_nText = -1;
}

void CCustomeTextDrawStatManager::FreeAllTextDraw( CTextDrawManager* pdm)
{
	for ( INLINECLASS::CTextStateMap::iterator it = m_ptsm->begin(); it != m_ptsm->end(); ++ it)
	{
		FreeTextDraw( it->first, pdm); // 绘制文本的纹理资源等在此处释放
	}
	//m_ptsm->clear(); // 此处不能clear, m_ptsm中只是存放了字体、颜色等属性, 如clear, 将导致文本属性丢失
}

void CCustomeTextDrawStatManager::Clear( CTextDrawManager* pdm)
{
	for ( INLINECLASS::CTextStateMap::iterator it = m_ptsm->begin(); it != m_ptsm->end(); ++ it)
	{
		FreeTextDraw( it->first, pdm);
	}
	m_ptsm->clear();
}

bool CCustomeTextDrawStatManager::IsTextState( int nIndex) const
{
	INLINECLASS::CTextStateMap::iterator it = m_ptsm->find( nIndex);
	return it != m_ptsm->end();
}

COLOR CCustomeTextDrawStatManager::GetTextStateEffectColor( int nIndex) const
{
	INLINECLASS::CTextStateMap::iterator it = m_ptsm->find( nIndex);
	assert( it != m_ptsm->end());
	return it->second.m_clrStateEffect;
}

COLOR CCustomeTextDrawStatManager::GetTextStateGradualColor( int nIndex) const
{
	INLINECLASS::CTextStateMap::iterator it = m_ptsm->find( nIndex);
	assert( it != m_ptsm->end());
	return it->second.m_clrStateGradual;
}

bool CCustomeTextDrawStatManager::IsTextStateGradualColor( int nIndex) const
{
	INLINECLASS::CTextStateMap::iterator it = m_ptsm->find( nIndex);
	assert( it != m_ptsm->end());
	return it->second.m_clrState != it->second.m_clrStateGradual;
}

FONTEFEECTFLAG CCustomeTextDrawStatManager::GetTextStateEffect( int nIndex) const
{
	INLINECLASS::CTextStateMap::iterator it = m_ptsm->find( nIndex);
	assert( it != m_ptsm->end());
	return it->second.m_uStateEffectType;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextable::CTextable()
	: m_ptdsm( new CTextDrawStatManager), m_text( new INLINECLASS::CSimpleString()), m_bDrawMouseIn( false), m_bDrawDisable( false),
	m_nOffsetX( 0), m_nOffsetY( 0)
{
	ReleaseTextDrawManager();
}

CTextable::~CTextable()
{
	FreeAllTextDraw();
	delete m_ptdsm;
	delete m_text;
}

void CTextable::FreeAllTextDraw()
{
	m_ptdsm->FreeAllTextDraw( m_pdm);
}

CTextDrawManager& CTextable::GetTextDrawManager()
{
	return *m_pdm;
}

void CTextable::ReleaseTextDrawManager()
{
	CTextDrawManager& tdm = CTextDrawManager::GetInstance();
	FreeAllTextDraw();
	m_pdm = &tdm;
}

const wchar_t* CTextable::GetComponentText() const
{
	return m_text->c_str();
}
void CTextable::SetComponentText( const wchar_t* text)
{
	if( NULL == text || 0 == text[0])
		m_text->erase();
	else
		m_text->assign( text);
	FreeAllTextDraw();
}

void CTextable::SetComponentText( int n)
{
	SetComponentText( CTextResourceManager::GetInstance().GetText( n));
}

void CTextable::SetComponentTextSimpleColor( COLOR clr)
{
	m_ptdsm->SetNormalColor( clr, m_pdm, GetComponentTextEffect(), GetComponentTextEffectColor(), false, clr);
}

void CTextable::SetComponentTextColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	m_ptdsm->SetNormalColor( clr, m_pdm, type, clrEffect, bGradualColor, clrGradual);
}

COLOR CTextable::GetComponentTextColor() const
{
	return m_ptdsm->GetNormalColor();
}

FONTEFEECTFLAG CTextable::GetComponentTextEffect() const
{
	return m_ptdsm->GetNormalEffect();
}

COLOR CTextable::GetComponentTextEffectColor() const
{
	return m_ptdsm->GetNormalEffectColor();
}

bool CTextable::IsTextGradualColor() const
{
	return m_ptdsm->GetNormalGradualColor() != m_ptdsm->GetNormalColor();
}

COLOR CTextable::GetComponentTextGradualColor() const
{
	return m_ptdsm->GetNormalGradualColor();
}

void CTextable::SetComponentTextMouseInColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	m_bDrawMouseIn = true;
	m_ptdsm->SetMouseInColor( clr, m_pdm, type, clrEffect, bGradualColor, clrGradual);
}

void CTextable::ReleaseComponentTextMouseInColor()
{
	m_bDrawMouseIn = false;
	m_ptdsm->FreeMouseInTextDraw( m_pdm);
}

COLOR CTextable::GetComponentTextMouseInColor() const
{
	return m_ptdsm->GetMouseInColor();
}


FONTEFEECTFLAG CTextable::GetComponentTextMouseInEffect() const
{
	return m_ptdsm->GetMouseInEffect();
}

COLOR CTextable::GetComponentTextMouseInEffectColor() const
{
	return m_ptdsm->GetMouseInEffectColor();
}

bool CTextable::IsTextMouseInGradualColor() const
{
	return m_ptdsm->GetMouseInGradualColor() != m_ptdsm->GetMouseInColor();
}

COLOR CTextable::GetComponentTextMouseInGradualColor() const
{
	return m_ptdsm->GetMouseInGradualColor();
}

void CTextable::SetComponentFont( int nFont)
{
	m_ptdsm->SetFont( nFont, m_pdm);
}

int CTextable::GetComponentFont() const
{
	return m_ptdsm->GetFont();
}

void CTextable::SetComponentTextDisableColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	m_bDrawDisable = true;
	m_ptdsm->SetDisableColor( clr, m_pdm, type, clrEffect, bGradualColor, clrGradual);
}

void CTextable::ReleaseComponentTextDisableColor()
{
	m_bDrawDisable = false;
	m_ptdsm->FreeDisableTextDraw( m_pdm);
}

COLOR CTextable::GetComponentTextDisableColor() const
{
	return m_ptdsm->GetDisableColor();
}

bool CTextable::IsTextDisableGradualColor() const
{
	return m_ptdsm->GetDisableGradualColor() != m_ptdsm->GetDisableColor();
}

COLOR CTextable::GetComponentTextDisableGradualColor() const
{
	return m_ptdsm->GetDisableGradualColor();
}

FONTEFEECTFLAG CTextable::GetComponentTextDisableEffect() const
{
	return m_ptdsm->GetDisableEffect();
}

COLOR CTextable::GetComponentTextDisableEffectColor() const
{
	return m_ptdsm->GetDisableEffectColor();
}

void CTextable::TextDraw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bEnable)
{
	if( bEnable)
	{
		bMouseIn = bMouseIn && m_bDrawMouseIn; 
		if( bMouseIn)
		{
			m_ptdsm->DrawMouseIn( draw, m_pdm, m_text->c_str(), l + m_nOffsetX, t + m_nOffsetY, max( 0, cx - m_nOffsetX), max( 0, cy - m_nOffsetY), max( 0, cw - m_nOffsetX + cx), max( 0, ch - m_nOffsetY + cy));
			return;
		}
	}
	else
	{
		bEnable = !bEnable && m_bDrawDisable;
		if( bEnable)
		{
			m_ptdsm->DrawDisable( draw, m_pdm, m_text->c_str(), l + m_nOffsetX, t + m_nOffsetY, max( 0, cx - m_nOffsetX), max( 0, cy - m_nOffsetY), max( 0, cw - m_nOffsetX + cx), max( 0, ch - m_nOffsetY + cy));
			return;
		}
	}

    m_ptdsm->DrawNormal( draw, m_pdm, m_text->c_str(), l + m_nOffsetX, t + m_nOffsetY, max( 0, cx - m_nOffsetX), max( 0, cy - m_nOffsetY), max( 0, cw - m_nOffsetX + cx), max( 0, ch - m_nOffsetY + cy));
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextComponent::CTextComponent()
	: m_bMouseIn( false), m_bAutoWidth( false), m_taFlag( 0)
{
}

CTextComponent::~CTextComponent()
{
}

void CTextComponent::TextDraw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn)
{
	CTextable::TextDraw( draw, l, t, cx, cy, cw, ch, bMouseIn, IsEnable());
}

void CTextComponent::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CComponent::Draw( draw, l, t, cx, cy, cw, ch);
	TextDraw( draw, l, t, cx, cy, cw, ch, m_bMouseIn);
}

void CTextComponent::OnMouseEnter()
{
	if ( IsViewOnly())
		return;

	m_bMouseIn = true;
	CComponent::OnMouseEnter();
}

void CTextComponent::OnMouseLeave()
{
	if ( IsViewOnly())
		return;

	m_bMouseIn = false;
	CComponent::OnMouseLeave();
}

void CTextComponent::SetTextAlign( TEXTALIGNFLAG flag)
{
	m_taFlag = flag;
	AdjustAlign();
}

TEXTALIGNFLAG CTextComponent::GetTextAlign() const
{
	return m_taFlag;
}

void CTextComponent::SetAutoWidth( bool bAuto)
{
	m_bAutoWidth = bAuto;
}

bool CTextComponent::IsAutoWidth() const
{
	return m_bAutoWidth;
}

void CTextComponent::SetComponentText(const wchar_t* text)
{
	CTextable::SetComponentText( text);
	AdjustAlign();
}

void CTextComponent::SetComponentText(int n)
{
	CTextable::SetComponentText( n);
	AdjustAlign();
}

void CTextComponent::AdjustAlign()
{
	if ( m_text->empty())
		return;

	m_nOffsetX = 0;
	m_nOffsetY = 0;

	if ( m_bAutoWidth)
		CComponent::SetSize( GetTextDrawManager().GetStringPixelWidth( m_text->c_str(), GetComponentFont()), GetHeight());
	else
	{
		if ( m_taFlag & TXA_CENTER)
			m_nOffsetX = max( 0, ( GetWidth() - GetTextDrawManager().GetStringPixelWidth( m_text->c_str(), GetComponentFont())) >> 1);
		else if ( m_taFlag & TXA_RIGHT)
			m_nOffsetX = max( 0, GetWidth() - GetTextDrawManager().GetStringPixelWidth( m_text->c_str(), GetComponentFont()));
	}

	if ( m_taFlag & TXA_MIDDLE)
		m_nOffsetY = max( 0, ( GetHeight() - GetTextDrawManager().GetFontHeightWithoutUnderline( GetComponentFont())) >> 1);
	else if ( m_taFlag & TXA_BOTTOM)
		m_nOffsetY = max( 0, GetHeight() - GetTextDrawManager().GetFontHeight( GetComponentFont()));

	INVALIDATE();
}

void CTextComponent::SetSize(int w, int h)
{
	CComponent::SetSize( w, h);
	AdjustAlign();
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CSimpleTextable::CSimpleTextable()
	: m_clrNormal( MakeColor( 255, 255, 255)), m_clrNormalGradual( MakeColor( 255, 255, 255)), m_clrSelected( MakeColor( 0, 0, 255)), m_clrSelectedGradual( MakeColor( 0, 0, 255)), m_clrMouseOn(  MakeColor( 100, 100, 200)), m_clrMouseOnGradual( MakeColor( 100, 100, 200)), m_nFont( 0), m_clrNormalEffect( 0), m_clrSelectedEffect( 0),
	  m_clrMouseOnEffect( 0), m_uNormalEffect( 0), m_uSelectedEffect( 0), m_uMouseOnEffect( 0)
{
}

CSimpleTextable::~CSimpleTextable()
{
}

void CSimpleTextable::SetTextColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	m_clrNormal = clr;
	m_clrNormalEffect = clrEffect;
	m_uNormalEffect = type;
	m_clrNormalGradual = bGradualColor ? clrGradual : clr;
}

COLOR CSimpleTextable::GetTextColor() const
{
	return m_clrNormal;
}

COLOR CSimpleTextable::GetTextGradualColor() const
{
	return m_clrNormalGradual;
}

void CSimpleTextable::SetTextSelectedColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	m_clrSelected = clr;
	m_clrSelectedEffect = clrEffect;
	m_uSelectedEffect = type;
	m_clrSelectedGradual = bGradualColor ? clrGradual : clr;
}

COLOR CSimpleTextable::GetTextSelectedColor() const
{
	return m_clrSelected;
}

COLOR CSimpleTextable::GetTextSelectedGradualColor() const
{
	return m_clrSelectedGradual;
}

void CSimpleTextable::SetTextMouseOnColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	m_clrMouseOn = clr;
	m_clrMouseOnEffect = clrEffect;
	m_uMouseOnEffect = type;
	m_clrMouseOnGradual = bGradualColor ? clrGradual : clr;
}

COLOR CSimpleTextable::GetTextMouseOnColor() const
{
	return m_clrMouseOn;
}

COLOR CSimpleTextable::GetTextMouseOnGradualColor() const
{
	return m_clrMouseOnGradual;
}

void CSimpleTextable::SetTextFont( int font)
{
	m_nFont = font;
}

int CSimpleTextable::GetTextFont() const
{
	return m_nFont;
}

FONTEFEECTFLAG CSimpleTextable::GetNormalEffect() const
{
	return m_uNormalEffect;
}

FONTEFEECTFLAG CSimpleTextable::GetSelectedEffect() const
{
	return m_uSelectedEffect;
}

FONTEFEECTFLAG CSimpleTextable::GetMouseOnEffect() const
{
	return m_uMouseOnEffect;
}

COLOR CSimpleTextable::GetNormalEffectColor() const
{
	return m_clrNormalEffect;
}

COLOR CSimpleTextable::GetSelectedEffectColor() const
{
	return m_clrSelectedEffect;
}

COLOR CSimpleTextable::GetMouseOnEffectColor() const
{
	return m_clrMouseOnEffect;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CScrollTextComponent::CScrollTextComponent()
: m_ptexts( new INLINECLASS::CTextVector()), m_pstp( new INLINECLASS::CScrollTextParamVector()), m_limite( 50), m_space( 10), m_interval( 10), m_step( 1), m_loop( true)
{
}

CScrollTextComponent::~CScrollTextComponent()
{
	Stop();
	delete m_ptexts;
	delete m_pstp;
}

void CScrollTextComponent::SetTextNumberLimite( int n)
{
	m_limite = n;
	if ( (int)m_ptexts->size() > n)
	{
		m_ptexts->erase( m_ptexts->begin(), m_ptexts->begin() + ( (int)m_ptexts->size() - n));
	}
}

int  CScrollTextComponent::GetTextNumberLimite() const
{
	return m_limite;
}

void CScrollTextComponent::SetSpaceBetweenScrollTexts( int n)
{
	m_space = n;
}

int  CScrollTextComponent::GetSpaceBetweenScrollTexts() const
{
	return m_space;
}

void CScrollTextComponent::SetScrollInterval( int n)
{
	m_interval = n;
}

int  CScrollTextComponent::GetScrollInterval() const
{
	return m_interval;
}

void CScrollTextComponent::SetScrollStep( int n)
{
	m_step = n;
}

int  CScrollTextComponent::GetScrollStep() const
{
	return m_step;
}

void CScrollTextComponent::SetLoopScroll( bool bLoop /*= true*/)
{
	m_loop = bLoop;
}

bool CScrollTextComponent::IsLoopScroll() const
{
	return m_loop;
}

void CScrollTextComponent::InsertText( wchar_t* text)
{
	if ( (int)m_ptexts->size() < m_limite)
	{
		m_ptexts->push_back( text);
	}
}

void CScrollTextComponent::EraseText( int n)
{
	if ( n >= 0 && n < ( int)m_ptexts->size())
	{
		INLINECLASS::CScrollTextParamVector::iterator it = std::find( m_pstp->begin(), m_pstp->end(), m_ptexts->at( n).c_str());
		if ( it == m_pstp->end())
			m_ptexts->erase( m_ptexts->begin() + n);
	}
}

void CScrollTextComponent::ClearText()
{
	Stop();
	m_ptexts->clear();
}

int  CScrollTextComponent::GetTextCount() const
{
	return (int)m_ptexts->size();
}

const wchar_t* CScrollTextComponent::GetText( int n) const
{
	return m_ptexts->at( n).c_str();
}

void CScrollTextComponent::Start( int n)
{
	if ( !m_ptexts->empty())
	{
		m_pstp->reserve( m_limite);
		m_pstp->push_back( INLINECLASS::CScrollTextParam());
		SetScrollTextParam( n, m_pstp->back());
		CTimer::NewTimer( *this, m_interval, m_timerid);
	}
}

void CScrollTextComponent::Stop()
{
	FreeAllTextDraw();
}

void CScrollTextComponent::OnTimer( const TIMERID& nid)
{
	if ( m_pstp->empty())
		Stop();
	else
	{
		// 定时调整各文本控件的位置以实现滚动动作
		int lastdel = -1;
		for ( INLINECLASS::CScrollTextParamVector::iterator it = m_pstp->begin(); it != m_pstp->end(); )
		{
			INLINECLASS::CScrollTextParam& stp = *it;
			stp.posx -= m_step;
			if ( stp.posx < 0)
			{
				stp.posx = 0;
				stp.offset += m_step;
			}
			stp.width = min( stp.tc->GetWidth() - stp.offset, GetWidth() - stp.posx);

			if ( stp.width <= 0)
			{
				lastdel = m_ptexts->GetIndex( (*it).tc->GetComponentText());
				delete ( *it).tc;
				it = m_pstp->erase( it);
			}
			else 
				++ it;
		}

		int posx, n;
		bool bNextText = false;

		if ( !m_pstp->empty()) // 如存在滚动中的文本, 则在最后一个文本后检查能否继续插入新文本
		{
			INLINECLASS::CScrollTextParam& last = m_pstp->back();
			posx = last.posx + last.width + m_space;;
			if ( posx < GetWidth())
			{
				n = m_ptexts->GetIndex( last.tc->GetComponentText());
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
			m_pstp->push_back( INLINECLASS::CScrollTextParam());
			INLINECLASS::CScrollTextParam& stp = m_pstp->back();

			SetScrollTextParam( n, stp);
			stp.posx = posx;
			stp.width = GetWidth() - posx;
		}

		CTimer::NewTimer( *this, m_interval, m_timerid);
	}
}

void CScrollTextComponent::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CBKComponent::Draw( draw, l, t, cx, cy, cw, ch);
	for ( INLINECLASS::CScrollTextParamVector::iterator it = m_pstp->begin(); it != m_pstp->end(); ++ it)
	{
		INLINECLASS::CScrollTextParam& stp = *it;
		stp.tc->Draw( draw, l + stp.posx - stp.offset, t + max( 0, (ch - CTextDrawManager::GetInstance().GetFontHeightWithoutUnderline( stp.tc->GetComponentFont())) >> 1 ), stp.offset, cy, stp.width, ch);
	}
}

void CScrollTextComponent::SetScrollTextParam( int n, INLINECLASS::CScrollTextParam& stp)
{
	if ( n >= 0 && n < (int)m_ptexts->size())
	{
		stp.tc = new CTextComponent();
		stp.tc->SetComponentFont( GetComponentFont());
		stp.tc->SetComponentTextColor( GetComponentTextColor(), GetComponentTextEffect(), GetComponentTextEffectColor(), IsTextGradualColor(), GetComponentTextGradualColor());
		stp.tc->SetComponentTextMouseInColor( GetComponentTextMouseInColor(), GetComponentTextMouseInEffect(), GetComponentTextMouseInEffectColor(), IsTextMouseInGradualColor(), GetComponentTextMouseInGradualColor());
		stp.tc->SetComponentTextDisableColor( GetComponentTextDisableColor(), GetComponentTextDisableEffect(), GetComponentTextDisableEffectColor(), IsTextDisableGradualColor(), GetComponentTextDisableGradualColor());
		stp.tc->SetComponentText( m_ptexts->at( n).c_str());
		stp.tc->SetSize( CTextDrawManager::GetInstance().GetStringPixelWidth( m_ptexts->at( n).c_str(), GetComponentFont()), GetHeight());
		stp.offset = 0;
		stp.posx = GetWidth();
		stp.width = 0;
	}
}

void CScrollTextComponent::FreeAllTextDraw()
{
	CTextable::FreeAllTextDraw();
	for ( INLINECLASS::CScrollTextParamVector::iterator it = m_pstp->begin(); it != m_pstp->end(); ++ it)
	{
		CTextComponent* pc = ( *it).tc;
		if ( pc)
		{
			pc->FreeAllTextDraw();
			delete pc;
			( *it).tc = NULL;
		}
	}
	m_pstp->clear();
}

} // namespace GXWINDOW { 