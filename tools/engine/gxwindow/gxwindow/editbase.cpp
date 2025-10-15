#include "gxwindowdef.h"
#include "editinline.h" 

namespace GXWINDOW { 
namespace INLINECLASS {

static CEditElementAllocator	g_DefaultEditElementAllocator;

class CTextPieceVector : public std::vector<CDecoderTextPiece>
{
public:
	CTextPieceVector() {}
	virtual ~CTextPieceVector() {}
};

} // namespace INLINECLASS {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CEditElementable::CEditElementable( CTextDrawManager& tdm)
{
	m_pdm = &tdm;
	if( NULL == m_pdm)
		m_pdm = &CTextDrawManager::GetInstance();
}

CEditElementable::~CEditElementable()
{
}

CTextDrawManager& CEditElementable::GetTextDrawManager()
{
	return *m_pdm;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CEditElement::CEditElement( CTextDrawManager& tdm)
	: CEditElementable( tdm), m_nText( -1)
{
}

CEditElement::~CEditElement()
{
}

void CEditElement::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CComponent::Draw( draw, l, t, cx, cy, cw, ch);
	if( -1 != m_nText)
		CEditElementable::GetTextDrawManager().Draw( m_nText, l, t, cx, cy, cw ,ch);
}

void CEditElement::SetTextIndex( int nText)
{
	m_nText = nText;
}

int CEditElement::GetTextIndex() const
{
	return m_nText;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CEditElementAllocator::CEditElementAllocator()
{
}

CEditElementAllocator::~CEditElementAllocator()
{
}

CEditElement* CEditElementAllocator::NewEditElement( CTextDrawManager& tdm)
{
	return new CEditElement( tdm);
}

void CEditElementAllocator::DeleteEditElement( CEditElement* ee)
{
	delete ee;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CBlendEditElement::CBlendEditElement( CTextDrawManager& tdm)
	: CEditElement( tdm), m_nSelectCharStart( 0)
{
	Initialize( INLINECLASS::g_DefaultEditElementAllocator);
}

CBlendEditElement::CBlendEditElement( CTextDrawManager& tdm, CEditElementAllocator& alloc)
	: CEditElement( tdm), m_nSelectCharStart( 0)
{
	Initialize( alloc);
}

CBlendEditElement::~CBlendEditElement()
{
	delete m_ssselect;	
	delete m_ss;

	CTextDrawManager& tdm = CEditElementable::GetTextDrawManager();
	if( m_select)
	{
		tdm.FreeTextDraw( m_select->GetTextIndex());			
		m_Alloc->DeleteEditElement( m_select);
	}
	tdm.FreeTextDraw( GetTextIndex());
	
	delete m_ssinfo;
}

void CBlendEditElement::Initialize( CEditElementAllocator& alloc)
{
	m_Alloc = &alloc;
	m_ss = new INLINECLASS::CSimpleString();
	m_ssselect = new INLINECLASS::CSimpleString();
	m_nSelectStart =  EE_SELECT_END;
	m_nSelectEnd = EE_SELECT_START;

	m_select = NULL;
	m_pLastMouseOn = NULL;

	m_nFont = 0;
	m_clrText = MakeColor( 255, 255, 255);
	m_clrTextGradual = m_clrText;
	m_clrSelect = MakeColor( 255, 255, 255);
	m_clrSelectGradual = m_clrSelect;
	m_clrBKSelect = MakeColor( 80, 80, 80);

	m_ssinfo = new INLINECLASS::CSimpleString();
}

const wchar_t* CBlendEditElement::GetInfo() const
{
	return m_ssinfo->c_str();
}
int CBlendEditElement::GetInfoCount() const
{
	return (int)m_ssinfo->size();
}

void CBlendEditElement::SetInfo( const wchar_t* szInfo)
{
	if( szInfo && szInfo[0])
		m_ssinfo->assign( szInfo);
	else
		m_ssinfo->erase();
}

void CBlendEditElement::SetSize( int w, int h)
{
	CEditElement::SetSize( w, h);
}

void CBlendEditElement::SetTextIndex( int nText)
{
	CEditElement::SetTextIndex( nText);
}

int CBlendEditElement::GetTextIndex() const
{
	return CEditElement::GetTextIndex();
}

void CBlendEditElement::SetText( const wchar_t* text)
{
	if( NULL == text)
		text = L"";
	if( 0 == wcscmp( m_ss->c_str(), text))
		return;
	m_ss->assign( text);
	CheckSizeByText();
	ResetNormalElement();
	SetSelectNone();
}

const wchar_t* CBlendEditElement::GetText() const
{
	return m_ss->c_str();
}

int CBlendEditElement::GetCharCount() const
{
	return (int)m_ss->size();
}

void CBlendEditElement::SetFont( int font)
{
	if( font != m_nFont)
	{
		m_nFont = font;
		CheckSizeByText();
		ResetSelectElement();
	}
}

int CBlendEditElement::GetFont() const
{
	return m_nFont;
}

void CBlendEditElement::SetTextColor( COLOR clr, COLOR clrGradual, FONTEFEECTFLAG type, COLOR clrEffect)
{
	if( clr != m_clrText || m_clrTextGradual != clrGradual || m_clrTextEffect != clrEffect ||type !=  m_uTextEffect)
	{
		m_clrText = clr;
		m_clrTextGradual = clrGradual;
		m_clrTextEffect = clrEffect;
		m_uTextEffect = type;
		ResetNormalElement();
	}
}

COLOR CBlendEditElement::GetTextColor() const
{
	return m_clrText;
}

COLOR CBlendEditElement::GetTextGradualColor() const
{
	return m_clrTextGradual;
}

void CBlendEditElement::SetSelectColor( COLOR clr, COLOR clrGradual, COLOR clrbk, FONTEFEECTFLAG type, COLOR clrEffect)
{
	if( clr != m_clrSelect || m_clrTextGradual != clrGradual ||  clrbk != m_clrBKSelect || clrEffect != m_clrSelectEffect || type != m_uSelectEffect)
	{
		m_clrSelect = clr;
		m_clrSelectGradual = clrGradual;
		m_clrBKSelect = clrbk;
		m_clrSelectEffect = clrEffect;
		m_uSelectEffect = type;
		ResetSelectElement();
	}
}

COLOR CBlendEditElement::GetSelectColor() const
{
	return m_clrSelect;
}

COLOR CBlendEditElement::GetSelectGradualColor() const
{
	return m_clrSelectGradual;
}

COLOR CBlendEditElement::GetSelectBKColor() const
{
	return m_clrBKSelect;
}

COLOR CBlendEditElement::GetTextEffectColor() const
{
	return m_clrTextEffect;
}

COLOR CBlendEditElement::GetSelectEffectColor() const
{
	return m_clrSelectEffect;
}

FONTEFEECTFLAG CBlendEditElement::GetTextEffectType() const
{
	return m_uTextEffect;
}

FONTEFEECTFLAG CBlendEditElement::GetSelectEffectType() const
{
	return m_uSelectEffect;
}

void CBlendEditElement::CheckSizeByText()
{
	CTextArea* ta = dynamic_cast<CTextArea*>( GetParent());
	assert( ta);
	INLINECLASS::CWCharWidth& ww = ta->m_eec->GetWCharWidth();
	int nWidth = 0;
	int nHeight = 0;
	INLINECLASS::DividableString::GetStringSize( m_nFont, *m_ss, ww, nWidth, nHeight);
	SetSize( nWidth, nHeight);
}

void CBlendEditElement::ResetNormalElement()
{
	CTextDrawManager& tdm = GetTextDrawManager();
	tdm.FreeTextDraw( GetTextIndex());
	int n = tdm.NewTextDraw( m_ss->c_str(), m_nFont, m_clrText, m_clrTextGradual, 0, m_uTextEffect, m_clrTextEffect );
	SetTextIndex( n);
}

void CBlendEditElement::ResetSelectElement()
{
	CTextDrawManager& tdm = GetTextDrawManager();
	if( m_select)
		tdm.FreeTextDraw( m_select->GetTextIndex());

	int s, w;

	CheckSelectString( s, w);

	if( 0 == w)
	{
		if( m_select)
		{
			m_Alloc->DeleteEditElement( m_select);
			m_pLastMouseOn = NULL;
			m_select = NULL;
		}
		return;
	}

	if( NULL == m_select)	
	{
		m_select = m_Alloc->NewEditElement( tdm);
		m_select->SetParent( this);
	}


	m_select->SetPos( s, 0);
	m_select->SetSize( w, GetHeight());
	int n = tdm.NewTextDraw( m_ssselect->c_str(), m_nFont, m_clrSelect, m_clrSelectGradual, m_clrBKSelect, m_uSelectEffect, m_clrTextEffect);
	if ( m_uSelectEffect & FET_SHADOW)
		tdm.SetShadow( n, m_clrSelectEffect);
	else if ( m_uSelectEffect & FET_STROKE)
		tdm.SetStroke( n, m_clrSelectEffect);
	m_select->SetTextIndex( n);
}

void CBlendEditElement::CheckSelectString( int& s, int& w)
{
	int			cw = GetWidth();
	
	m_nSelectCharStart = 0;

	if( m_nSelectStart >= m_nSelectEnd)
	{
		m_ssselect->erase();
		s = cw;
		w = 0;
		return;
	}

	{
		int nFontWidth = CTextDrawManager::GetInstance().GetCharPixelWidth( L'H', m_nFont);
		if( m_nSelectStart < (nFontWidth / 2) && m_nSelectEnd >= cw )
		{
			*m_ssselect = *m_ss;
			s = 0;
			w = cw;
			return;
		}
	}
	
	std::wstring::size_type		sstart = std::wstring::npos;
	std::wstring::size_type		send = std::wstring::npos;
	int							nw = 0;
	std::wstring::size_type		scount = m_ss->size();

	CTextArea* ta = dynamic_cast<CTextArea*>( GetParent());
	assert( ta);
	INLINECLASS::CWCharWidth& ww = ta->m_eec->GetWCharWidth();

	for( std::wstring::size_type i = 0; i < scount; i ++)
	{
		int onew = ww.GetWCharWidth( m_ss->at( i), m_nFont);
		int tempw = nw + onew;
		if( m_nSelectStart >= nw && m_nSelectStart < tempw)
		{
			if( ( m_nSelectStart - nw) < (onew / 2) )
			{
				sstart = i;
				s = nw;
			}
			else
			{
				sstart = i + 1;
				s = tempw;
			}
		}
		if( m_nSelectEnd >= nw && m_nSelectEnd < tempw)
		{
			if( ( m_nSelectEnd - nw) < (onew / 2) )
				send = i;
			else
				send = i + 1;
			break;
		}
		nw = tempw;
	} // for( std::wstring::size_type i = 0; i < scount; i ++)
	
	if( std::wstring::npos == sstart || sstart >= scount)
	{
		s = cw;
		w = 0;
		return;
	}
	if( send >= scount)
		send = std::wstring::npos;
	else
		send = send - sstart; 
	if( 0 == send)
	{
		m_ssselect->erase();
		w = 0;
	}
	else
	{
		m_nSelectCharStart = (int)sstart;
		m_ssselect->assign( m_ss->substr( sstart, send));
		w = INLINECLASS::DividableString::GetStringWidth( m_nFont, *m_ssselect, ww);
	}
}

void CBlendEditElement::SetSelect( int s, int e)
{
	if( s == m_nSelectStart && e == m_nSelectEnd)
		return;
	m_nSelectStart = s;
	m_nSelectEnd = e;
	ResetSelectElement();
}

void CBlendEditElement::SetSelectNone()
{
	SetSelect( EE_SELECT_END, EE_SELECT_START);
}

const wchar_t* CBlendEditElement::GetSelectString() const
{
	return m_ssselect->c_str();
}

int CBlendEditElement::GetSelectStringLength() const
{
	return (int)m_ssselect->size();
}

int CBlendEditElement::GetSelectCharStart() const
{
	return m_nSelectCharStart;
}

void CBlendEditElement::OnMouseLeave()
{
	if( m_pLastMouseOn)
	{
		m_pLastMouseOn->OnMouseLeave();
		m_pLastMouseOn = NULL;
	}
	CEditElement::OnMouseLeave();
}

bool CBlendEditElement::OnLeftButtonDown( int x, int y, int key)
{
	if( m_select && m_select->IsAbsPointIn( x, y))
		return m_select->OnLeftButtonDown( x, y, key);
//	else  xuhui disable for click notify
	CEditElement::OnLeftButtonDown( x, y, key);
	return !IsTransparent() || !m_ssinfo->empty(); // xuhui add 20081021
}

bool CBlendEditElement::OnLeftButtonUp( int x, int y, int key)
{
	if( m_select && m_select->IsAbsPointIn( x, y))
		return m_select->OnLeftButtonUp( x, y, key);
	else
		return CEditElement::OnLeftButtonUp( x, y, key);
}

//bool CBlendEditElement::OnLeftButtonDoubleClick( int x, int y, int key)
//{
//	if( m_select && m_select->IsAbsPointIn( x, y))
//		return m_select->OnLeftButtonDoubleClick( x, y, key);
//	else
//		return CEditElement::OnLeftButtonDoubleClick( x, y, key);
//}

bool CBlendEditElement::OnRightButtonDown( int x, int y, int key)
{
	if( m_select && m_select->IsAbsPointIn( x, y))
		return m_select->OnRightButtonDown( x, y, key);
	else
		return CEditElement::OnRightButtonDown( x, y, key);
}

bool CBlendEditElement::OnRightButtonUp( int x, int y, int key)
{
	if( m_select && m_select->IsAbsPointIn( x, y))
		return m_select->OnRightButtonUp( x, y, key);
	else
		return CEditElement::OnRightButtonUp( x, y, key);
}

//bool CBlendEditElement::OnRightButtonDoubleClick( int x, int y, int key)
//{
//	if( m_select && m_select->IsAbsPointIn( x, y))
//		return m_select->OnRightButtonDoubleClick( x, y, key);
//	else
//		return CEditElement::OnRightButtonDoubleClick( x, y, key);
//}

bool CBlendEditElement::OnMouseMove( int x, int y, int key)
{
	if( m_select && m_select->IsAbsPointIn( x, y))
	{
		if( !m_select->IsSameComponent( m_pLastMouseOn))
		{
			if( m_pLastMouseOn)
				m_pLastMouseOn->OnMouseLeave();
			m_pLastMouseOn = m_select;
			m_select->OnMouseEnter();
		}
		return m_select->OnMouseMove( x, y, key);
	}
	else
	{
		if( m_pLastMouseOn)
		{
			m_pLastMouseOn->OnMouseLeave();
			m_pLastMouseOn = NULL;
		}
		return CEditElement::OnMouseMove( x, y, key);
	}
}

bool CBlendEditElement::OnMouseWheel( int x, int y, int key)
{
	if( m_select && m_select->IsAbsPointIn( x, y))
		return m_select->OnMouseWheel( x, y, key);
	else
		return CEditElement::OnMouseWheel( x, y, key);
}

void CBlendEditElement::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CEditElement::Draw( draw, l, t, cx, cy, cw, ch);
	if( m_select)
	{
		int nw = cw;
		int nx = 0;
		int cr = cx + cw;
		int nl = m_select->GetPosX();
		int nr = m_select->GetPosR();
		if( nl < cx)
		{
			if( nr < cx)
				return;
			if( nr < cr)
				nw = nr - cx;
			nx = cx - nl;
		}
		else if( nl < cr)
		{
			if( nr < cr)
				nw = nr - nl;
			else
				nw = cr - nl;
		}
		else
		{
			return;
		}
		m_select->Draw( draw, l + nl, t, nx, cy, nw, ch);
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

namespace INLINECLASS { enum { EBE_OFFSET_DOWN = 2, }; }

CEditButtonElement::CEditButtonElement( CTextDrawManager& tdm)
	: CEditElementable( tdm), m_nImage( -1), m_nImageMouseOn( -1), m_nImageWidth( EBE_DEFAULT_BUTTON_WIDTH), m_nImageHeight( EBE_DEFAULT_BUTTON_HEIGHT), m_nTextNormal( -1), m_nTextMouseOn( -1), 
	m_textNotify( new INLINECLASS::CSimpleString), m_mouseNotify( new INLINECLASS::CSimpleString), m_nTextWidth( 0), m_nTextHeight( EBE_DEFAULT_BUTTON_WIDTH)
{
	SetClickAudio( -1);
}

CEditButtonElement::~CEditButtonElement()
{
	CTextDrawManager& tdm = GetTextDrawManager();
	if( -1 != m_nTextNormal)
		tdm.FreeTextDraw( m_nTextNormal);
	if( -1 != m_nTextMouseOn)
		tdm.FreeTextDraw( m_nTextMouseOn);
	delete m_textNotify;
	delete m_mouseNotify;
}

void CEditButtonElement::SetSize( int w, int h)
{
	CAbstractButton::SetSize( w, h);
}

void CEditButtonElement::SetText( const wchar_t* szText, int nFont, COLOR clrNormal, COLOR clrNormalGradual, COLOR clrMouseOn, COLOR clrMouseOnGradual, FONTEFEECTFLAG fefNormal, COLOR clrNormalEffect, FONTEFEECTFLAG fefMouseOn, COLOR clrMouseOnEffect)
{
	CTextDrawManager& tdm = GetTextDrawManager();
	if( -1 != m_nTextNormal)
		tdm.FreeTextDraw( m_nTextNormal);
	m_nTextNormal = tdm.NewTextDraw( szText, nFont, clrNormal, clrNormalGradual, 0, fefNormal, clrNormalEffect);
	if( -1 != m_nTextMouseOn)
		tdm.FreeTextDraw( m_nTextMouseOn);
	m_nTextMouseOn = tdm.NewTextDraw( szText, nFont, clrMouseOn, clrMouseOnGradual, 0, fefMouseOn, clrMouseOnEffect);

	CTextArea* ta = dynamic_cast<CTextArea*>( GetParent());
	assert( ta);
	INLINECLASS::CWCharWidth& ww = ta->m_eec->GetWCharWidth();
	INLINECLASS::DividableString::GetStringSize( nFont, szText, ww, m_nTextWidth, m_nTextHeight);
	CheckSize();
}

void CEditButtonElement::CheckSize()
{
	int nWidth = m_nImageWidth + m_nTextWidth + INLINECLASS::EBE_OFFSET_DOWN;
	int nHeight = max( m_nTextHeight, m_nImageHeight) + INLINECLASS::EBE_OFFSET_DOWN;
	SetSize( nWidth, nHeight);
}

void CEditButtonElement::SetImage( int nImage, int nMouseOn, int nDown)
{
	m_nImage = nImage;
	m_nImageMouseOn = nMouseOn;
	m_nImageDown = nDown;
}

int  CEditButtonElement::GetImage() const
{
	return m_nImage;
}

int CEditButtonElement::GetMouseOnImage() const
{
	return m_nImageMouseOn;
}

int CEditButtonElement::GetMouseDownImage() const
{
	return m_nImageDown;
}

void CEditButtonElement::SetButtonSize( int w, int h)
{
	m_nImageWidth = w;
	m_nImageHeight = h;
	CheckSize();
}

int CEditButtonElement::GetButtonWidth() const
{
	return m_nImageWidth;
}

int CEditButtonElement::GetButtonHeight() const
{
	return m_nImageHeight;
}

void CEditButtonElement::SetNotifyString( const wchar_t* szText)
{
	m_textNotify->assign( szText);
}

const wchar_t* CEditButtonElement::GetNotifyString() const
{
	return m_textNotify->c_str();
}

void CEditButtonElement::SetMouseNotifyString( const wchar_t* szText)
{
	m_mouseNotify->assign( szText);
}

const wchar_t* CEditButtonElement::GetMouseNotifyString() const
{
	return m_mouseNotify->c_str();
}

void CEditButtonElement::DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown)
{
	if( bButtonDown)
	{
		cw -= INLINECLASS::EBE_OFFSET_DOWN;
		ch -= INLINECLASS::EBE_OFFSET_DOWN;
		if( cw <= 0 || ch <= 0)
			return;
		l += INLINECLASS::EBE_OFFSET_DOWN;
		t += INLINECLASS::EBE_OFFSET_DOWN;
	}
	if( cx < m_nImageWidth)
	{
		// 目前只用了两态, 需要三态的话换注释掉的这句
		// int nImage = ( bMouseIn && -1 != m_nImageMouseOn) ? m_nImageMouseOn : ( bButtonDown && -1 != m_nImageDown ? m_nImageDown : m_nImage);
		int nImage = bMouseIn ? m_nImageMouseOn : m_nImage;
		int nw = cw;
		int nh = ch;
		if( cx + cw > m_nImageWidth)
			nw = m_nImageWidth - cx;
		if ( cy + ch > m_nImageHeight)
			nh = m_nImageHeight - cy;
		if( -1 != nImage)
			CIconImageManager::GetInstance().DrawIcon16( draw, nImage, l + cx, t + cy, cx, cy, nw, nh);
		cw -= nw;
		if( cw <= 0)
			return;
		cx = 0;
	}
	else
	{
		cx -= m_nImageWidth;
	}

	int nText = bMouseIn ? m_nTextMouseOn : m_nTextNormal;
	if(  -1 == nText)
		return;
	CEditElementable::GetTextDrawManager().Draw( nText, l + m_nImageWidth, t, cx, cy, cw ,ch);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CEditIconElement::CEditIconElement() : m_textNotify( new INLINECLASS::CSimpleString) {}
CEditIconElement::~CEditIconElement()  
{
	delete m_textNotify;
}

void CEditIconElement::SetNotifyString( const wchar_t* szText)
{
	m_textNotify->assign( szText);
}

const wchar_t* CEditIconElement::GetNotifyString() const
{
	return m_textNotify->c_str();
}

bool CEditIconElement::OnLeftButtonDown( int x, int y, int key)
{
	CIconComponent::OnLeftButtonDown( x, y, key);
	if( m_textNotify->empty())
		return !IsTransparent();
	else
		return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CEditAnimateIconElement::CEditAnimateIconElement() : m_textNotify( new INLINECLASS::CSimpleString) {}
CEditAnimateIconElement::~CEditAnimateIconElement()
{
	delete m_textNotify;
}

void CEditAnimateIconElement::SetNotifyString( const wchar_t* szText)
{
	m_textNotify->assign( szText);
}

const wchar_t* CEditAnimateIconElement::GetNotifyString() const
{
	return m_textNotify->c_str();
}

bool CEditAnimateIconElement::OnLeftButtonDown( int x, int y, int key)
{
	CAnimateIconComponent::OnLeftButtonDown( x, y, key);
	if( m_textNotify->empty())
		return !IsTransparent();
	else
		return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CEditEmotionElement::CEditEmotionElement() : m_textNotify( new INLINECLASS::CSimpleString) {}
CEditEmotionElement::~CEditEmotionElement()
{
	delete m_textNotify;
}

void CEditEmotionElement::SetNotifyString( const wchar_t* szText)
{
	m_textNotify->assign( szText);
}

const wchar_t* CEditEmotionElement::GetNotifyString() const
{
	return m_textNotify->c_str();
}

bool CEditEmotionElement::OnLeftButtonDown( int x, int y, int key)
{
	CEmotionComponent::OnLeftButtonDown( x, y, key);
	if( m_textNotify->empty())
		return !IsTransparent();
	else
		return true;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CDecoderTextPiece::CDecoderTextPiece( const wchar_t* szText)
	: m_type( DTPT_DEFAULT_TEXT), m_textDeault( new INLINECLASS::CSimpleString( szText)), m_textTipInfo( NULL)
{
}

CDecoderTextPiece::CDecoderTextPiece( const wchar_t* szText, int font, int textformatid, COLOR clrText, COLOR clrTextGradual, COLOR clrSelect, COLOR clrSelectGradual, COLOR clrBKSelect, 
									 const wchar_t* szInfo, bool bComplex, const wchar_t* szTip, FONTEFEECTFLAG texteffect, 
									 COLOR clrTextEffect, FONTEFEECTFLAG selecteffect, COLOR clrSelectEffect)
	: m_type( bComplex ? DTPT_COMPLEX : DTPT_FORMAT_TEXT), m_textFormat( new INLINECLASS::CSimpleString( szText)), m_nFormatFont( font), m_nTextFormatID( textformatid), m_clrFormatText( clrText), m_clrFormatTextGradual( clrTextGradual),
	m_clrFormatSelect( clrSelect), m_clrFormatSelectGradual( clrSelectGradual), m_clrFormatBKSelect( clrBKSelect), m_textTipInfo( NULL), m_textFormatNotify( new INLINECLASS::CSimpleString( NULL == szInfo ? L"" : szInfo)),
	m_fefFormatText( texteffect), m_clrFormatTextEffect( clrTextEffect), m_fefFormatSelect( selecteffect), m_clrFormatSelectEffect( clrSelectEffect)
{
	if( szTip && szTip[0])
		m_textTipInfo = new INLINECLASS::CSimpleString( szTip);
}

CDecoderTextPiece::CDecoderTextPiece( int nImage, int nWidth, int nHeight, const wchar_t* szTip, const wchar_t* szMouseNotify)
	: m_type( DTPT_STATIC_IMAGE), m_nStaticImage( nImage), m_nStaticWidth( nWidth), m_nStaticHeight( nHeight), m_textTipInfo( NULL),
	m_imageMouseNotify( new INLINECLASS::CSimpleString( NULL == szMouseNotify ? L"" : szMouseNotify))
{
	if( szTip && szTip[0])
		m_textTipInfo = new INLINECLASS::CSimpleString( szTip);
}

CDecoderTextPiece::CDecoderTextPiece( int nWidth, int nHeight, CCustomTextPieceAbstractImpl*	impl, const wchar_t* szTip, const wchar_t* szMouseNotify)
:m_type(DTPT_CUSTOM_PIECE), m_nWidth(nWidth), m_nHeight(nHeight), m_customimpl(impl), m_textTipInfo( NULL),
m_customMouseNotify( new INLINECLASS::CSimpleString( NULL == szMouseNotify ? L"" : szMouseNotify))
{
	if( szTip && szTip[0])
		m_textTipInfo = new INLINECLASS::CSimpleString( szTip);
}

CDecoderTextPiece::CDecoderTextPiece( int nStart, int nEnd, int nWidth, int nHeight, int nInterval, const wchar_t* szTip, const wchar_t* szMouseNotify)
	: m_type( DTPT_ANIMATE_IMAGE), m_nAnimateStart( nStart), m_nAnimateEnd( nEnd), m_nAnimateWidth( nWidth), m_nAnimateHeight( nHeight), 
	m_nAnimateInterval( nInterval), m_textTipInfo( NULL), m_animateMouseNotify( new INLINECLASS::CSimpleString( NULL == szMouseNotify ? L"" : szMouseNotify))
{
	if( szTip && szTip[0])
		m_textTipInfo = new INLINECLASS::CSimpleString( szTip);
}

CDecoderTextPiece::CDecoderTextPiece()
	: m_type( DTPT_RETURN), m_textTipInfo( NULL)
{
}

CDecoderTextPiece::CDecoderTextPiece( const wchar_t* szText, int font, int textformatid, COLOR clrText, COLOR clrTextGradual, COLOR clrMouseOn, COLOR clrMouseOnGradual, int nImage, int nImageMouseOn, int nImageDown, 
									 const wchar_t* szNotify, int nImageWidth, int nImageHeight, const wchar_t* szTip, const wchar_t* szMouseNotify, FONTEFEECTFLAG texteffect, 
									 COLOR clrTextEffect, FONTEFEECTFLAG mouseoneffect, COLOR clrMouseOnEffect)
	: m_type( DTPT_BUTTON), m_textButton( new INLINECLASS::CSimpleString( szText)), m_nButtonFont( font), m_nButtonFormatID( textformatid), m_clrButtonText( clrText), m_clrButtonTextGradual( clrTextGradual),
	m_clrButtonMouseOn( clrMouseOn), m_clrButtonMouseOnGradual( clrMouseOnGradual),m_nButtonImage( nImage), m_nButtonMouseOnImage( nImageMouseOn), m_nButtonDownImage( nImageDown), 
	m_textNotify( new INLINECLASS::CSimpleString( szNotify)), m_nButtonImageWidth( nImageWidth), m_nButtonImageHeight( nImageHeight), m_textTipInfo( NULL), m_buttonMouseNotify( new INLINECLASS::CSimpleString( NULL == szMouseNotify ? L"" : szMouseNotify)),
	m_fefButtonText( texteffect), m_clrButtonTextEffect( clrTextEffect), m_fefButtonMouseOn( mouseoneffect), m_clrButtonMouseOnEffect( clrMouseOnEffect)
{	
	if( szTip && szTip[0])
		m_textTipInfo = new INLINECLASS::CSimpleString( szTip);
}

CDecoderTextPiece::CDecoderTextPiece( const wchar_t* szTip, int nPack, int nEmotion, int nShowWidth, int nShowHeight, int nIconWidth, int nIconHeight, const wchar_t* szMouseNotify) // DTPT_EMOTION_IMAGE
: m_type( DTPT_EMOTION_IMAGE), m_nPack( nPack), m_nEmotion( nEmotion), m_nShowWidth( nShowWidth), m_nShowHeight( nShowHeight), 
m_nIconWidth( nIconWidth), m_nIconHeight( nIconHeight), m_textTipInfo( NULL), m_emotionMouseNotify( new INLINECLASS::CSimpleString( NULL == szMouseNotify ? L"" : szMouseNotify))
{
	if ( szTip && szTip[0])
		m_textTipInfo = new INLINECLASS::CSimpleString( szTip);
}

CDecoderTextPiece::~CDecoderTextPiece()
{
	Free();
}

void CDecoderTextPiece::Free()
{
	switch( m_type)
	{
	case DTPT_DEFAULT_TEXT:
		delete m_textDeault;
		break;
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		delete m_textFormat;
		delete m_textFormatNotify;
		break;
	case DTPT_BUTTON:
		delete m_textButton;
		delete m_textNotify;
		delete m_buttonMouseNotify;
		break;
	case DTPT_STATIC_IMAGE:
		delete m_imageMouseNotify;
		break;
	case DTPT_ANIMATE_IMAGE:
		delete m_animateMouseNotify;
		break;
	case DTPT_EMOTION_IMAGE:
		delete m_emotionMouseNotify;
		break;
	case DTPT_CUSTOM_PIECE:
		delete m_customMouseNotify;
		break;
	default:
		break;
	}
	if( m_textTipInfo)
	{
		delete m_textTipInfo;
		m_textTipInfo = NULL;
	}
	m_type = DTPT_RETURN;
}

void CDecoderTextPiece::CopyFrom( const CDecoderTextPiece& src)
{
	m_type = src.m_type;
	if( src.m_textTipInfo)
		m_textTipInfo = new INLINECLASS::CSimpleString( *src.m_textTipInfo);
	switch( m_type)
	{
	case DTPT_DEFAULT_TEXT:
		m_textDeault = new INLINECLASS::CSimpleString( *src.m_textDeault);
		break;
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		m_textFormat = new INLINECLASS::CSimpleString( *src.m_textFormat);
		m_textFormatNotify = new INLINECLASS::CSimpleString( *src.m_textFormatNotify);
		m_nFormatFont = src.m_nFormatFont;
		m_nTextFormatID = src.m_nTextFormatID;
		m_clrFormatText = src.m_clrFormatText;
		m_clrFormatTextGradual = src.m_clrFormatTextGradual;
		m_clrFormatSelect = src.m_clrFormatSelect;
		m_clrFormatSelectGradual = src.m_clrFormatSelectGradual;
		m_clrFormatBKSelect = src.m_clrFormatBKSelect;
		m_fefFormatText = src.m_fefFormatText;
		m_fefFormatSelect = src.m_fefFormatSelect;
		m_clrFormatTextEffect = src.m_clrFormatTextEffect;
		m_clrFormatSelectEffect = src.m_clrFormatSelectEffect;
		break;
	case DTPT_STATIC_IMAGE:
		m_nStaticImage = src.m_nStaticImage;
		m_nStaticWidth = src.m_nStaticWidth;
		m_nStaticHeight = src.m_nStaticHeight;
		m_imageMouseNotify = new INLINECLASS::CSimpleString( *src.m_imageMouseNotify);
		break;
	case DTPT_ANIMATE_IMAGE:
		m_nAnimateWidth = src.m_nAnimateWidth;
		m_nAnimateStart = src.m_nAnimateStart;
		m_nAnimateEnd = src.m_nAnimateEnd;
		m_nAnimateHeight = src.m_nAnimateHeight;
		m_nAnimateInterval = src.m_nAnimateInterval;
		m_animateMouseNotify = new INLINECLASS::CSimpleString( *src.m_animateMouseNotify);
		break;
	case DTPT_BUTTON:
		m_textButton = new INLINECLASS::CSimpleString( *src.m_textButton);
		m_nButtonFont = src.m_nButtonFont;
		m_nButtonFormatID = src.m_nButtonFormatID;
		m_clrButtonText = src.m_clrButtonText;
		m_clrButtonTextGradual = src.m_clrButtonTextGradual;
		m_clrButtonMouseOn = src.m_clrButtonMouseOn;
		m_clrButtonMouseOnGradual = src.m_clrButtonMouseOnGradual;
		m_textNotify = new INLINECLASS::CSimpleString( *src.m_textNotify);
		m_nButtonImage = src.m_nButtonImage;
		m_nButtonMouseOnImage = src.m_nButtonMouseOnImage;
		m_nButtonDownImage = src.m_nButtonDownImage;
		m_buttonMouseNotify = new INLINECLASS::CSimpleString( *src.m_buttonMouseNotify);
		m_fefButtonText = src.m_fefButtonText;
		m_fefButtonMouseOn = src.m_fefButtonMouseOn;
		m_clrButtonTextEffect = src.m_clrButtonTextEffect;
		m_clrButtonMouseOnEffect = src.m_clrButtonMouseOnEffect;
		m_nButtonImageWidth = src.m_nButtonImageWidth;
		m_nButtonImageHeight = src.m_nButtonImageHeight;
		break;
	case DTPT_EMOTION_IMAGE:
		m_nPack = src.m_nPack;
		m_nEmotion = src.m_nEmotion;
		m_nShowWidth = src.m_nShowWidth;
		m_nShowHeight = src.m_nShowHeight;
		m_nIconWidth = src.m_nIconWidth;
		m_nIconHeight = src.m_nIconHeight;
		m_emotionMouseNotify = new INLINECLASS::CSimpleString( *src.m_emotionMouseNotify);
		break;
	case DTPT_CUSTOM_PIECE:
		m_nWidth = src.m_nWidth;
		m_nHeight = src.m_nHeight;
		m_customimpl = src.m_customimpl;
		m_customMouseNotify = new INLINECLASS::CSimpleString( *src.m_customMouseNotify);
		break;
	default:
		break;
	}
}

CDecoderTextPiece::CDecoderTextPiece( const CDecoderTextPiece& src)
	: m_type( DTPT_RETURN), m_textTipInfo( NULL)
{
	CopyFrom( src);
}

CDecoderTextPiece& CDecoderTextPiece::operator=( const CDecoderTextPiece& src)
{
	if( this == &src)
		return *this;
	Free();
	CopyFrom( src);
	return *this;
}

DTP_TYPE CDecoderTextPiece::GetType() const
{
	return m_type;
}

const wchar_t* CDecoderTextPiece::GetText() const
{
	switch( m_type)
	{
	case DTPT_DEFAULT_TEXT:
		return m_textDeault->c_str();
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_textFormat->c_str();
	case DTPT_BUTTON:
		return m_textButton->c_str();
	default:
		return L"";
	}
}

int CDecoderTextPiece::GetTextLength() const
{
	switch( m_type)
	{
	case DTPT_DEFAULT_TEXT:
		return (int)m_textDeault->size();
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return (int)m_textFormat->size();
	case DTPT_BUTTON:
		return (int)m_textButton->size();
	default:
		return 0;
	}
}

int CDecoderTextPiece::GetTextFont() const
{
	switch( m_type)
	{
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_nFormatFont;
	case DTPT_BUTTON:
		return m_nButtonFont;
	default:
		return -1;
	}
}

int CDecoderTextPiece::GetTextFormatID() const
{
	switch( m_type)
	{
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_nTextFormatID;
	case DTPT_BUTTON:
		return m_nButtonFormatID;
	default:
		return -1;
	}
}

int CDecoderTextPiece::GetTextColor() const
{
	switch( m_type)
	{
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_clrFormatText;
	case DTPT_BUTTON:
		return m_clrButtonText;
	default:
		return -1;
	}
}

int CDecoderTextPiece::GetTextGradualColor() const
{
	switch( m_type)
	{
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_clrFormatTextGradual;
	case DTPT_BUTTON:
		return m_clrButtonTextGradual;
	default:
		return -1;
	}
}

const wchar_t* CDecoderTextPiece::GetTextInfo() const
{
	switch( m_type)
	{
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_textFormatNotify->c_str();
	default:
		return L"";
	}
}

COLOR CDecoderTextPiece::GetTextSelectColor() const
{
	switch( m_type)
	{
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_clrFormatSelect;
	default:
		return -1;
	}
}

COLOR CDecoderTextPiece::GetTextSelectGradualColor() const
{
	switch( m_type)
	{
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_clrFormatSelectGradual;
	default:
		return -1;
	}	
}

COLOR CDecoderTextPiece::GetTextSelectBKColor() const
{
	switch( m_type)
	{
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_clrFormatBKSelect;
	default:
		return -1;
	}
}

FONTEFEECTFLAG CDecoderTextPiece::GetTextEffect() const
{
	switch( m_type)
	{
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_fefFormatText;
	case DTPT_BUTTON:
		return m_fefButtonText;
	default:
		return FET_NONE;
	}
}

FONTEFEECTFLAG CDecoderTextPiece::GetSelectEffect() const
{
	switch( m_type)
	{
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_fefFormatSelect;
	default:
		return FET_NONE;
	}
}

COLOR CDecoderTextPiece::GetTextEffectColor() const
{
	switch( m_type)
	{
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_clrFormatTextEffect;
	case DTPT_BUTTON:
		return m_clrButtonTextEffect;
	default:
		return 0;
	}
}

COLOR CDecoderTextPiece::GetSelectEffectColor() const
{
	switch( m_type)
	{
	case DTPT_FORMAT_TEXT:
	case DTPT_COMPLEX:
		return m_clrFormatSelectEffect;
	default:
		return 0;
	}
}

int CDecoderTextPiece::GetImage() const
{
	switch( m_type)
	{
	case DTPT_STATIC_IMAGE:
		return m_nStaticImage;
	case DTPT_BUTTON:
		return m_nButtonImage;
	default:
		return -1;
	}
}

int CDecoderTextPiece::GetImageWidth() const
{
	switch( m_type)
	{
	case DTPT_STATIC_IMAGE:
		return m_nStaticWidth;
	case DTPT_ANIMATE_IMAGE:
		return m_nAnimateWidth;
	case DTPT_EMOTION_IMAGE:
		return m_nShowWidth;
	case DTPT_CUSTOM_PIECE:
		return m_nWidth;
	default:
		return -1;
	}
}

int CDecoderTextPiece::GetImageHeight() const
{
	switch( m_type)
	{
	case DTPT_STATIC_IMAGE:
		return m_nStaticHeight;
	case DTPT_ANIMATE_IMAGE:
		return m_nAnimateHeight;
	case DTPT_EMOTION_IMAGE:
		return m_nShowHeight;
	case DTPT_CUSTOM_PIECE:
		return m_nWidth;
	default:
		return -1;
	}
}

int CDecoderTextPiece::GetAnimateInterval() const
{
	switch( m_type)
	{
	case DTPT_ANIMATE_IMAGE:
		return m_nAnimateInterval;
	default:
		return 0;
	}
}

int CDecoderTextPiece::GetAnimateImageStart() const
{
	switch( m_type)
	{
	case DTPT_ANIMATE_IMAGE:
		return m_nAnimateStart;
	default:
		return 0;
	}
}

int CDecoderTextPiece::GetAnimateImageEnd() const
{
	switch( m_type)
	{
	case DTPT_ANIMATE_IMAGE:
		return m_nAnimateEnd;
	default:
		return 0;
	}
}

int CDecoderTextPiece::GetEmotionPack() const
{
	switch( m_type)
	{
	case DTPT_EMOTION_IMAGE:
		return m_nPack;
	default:
		return 0;
	}
}

int CDecoderTextPiece::GetEmotion() const
{
	switch( m_type)
	{
	case DTPT_EMOTION_IMAGE:
		return m_nEmotion;
	default:
		return 0;
	}
}

int CDecoderTextPiece::GetEmotionWidth() const
{
	switch( m_type)
	{
	case DTPT_EMOTION_IMAGE:
		return m_nIconWidth;
	default:
		return 0;
	}
}

int CDecoderTextPiece::GetEmotionHeight() const
{
	switch( m_type)
	{
	case DTPT_EMOTION_IMAGE:
		return m_nIconHeight;
	default:
		return 0;
	}
}

const wchar_t* CDecoderTextPiece::GetTipFormatText() const
{
	if( m_textTipInfo)
		return m_textTipInfo->c_str();
	else
		return NULL;
}

int CDecoderTextPiece::GetTipFormatTextLength() const
{
	if( m_textTipInfo)
		return (int)m_textTipInfo->size();
	else
		return 0;
}

COLOR CDecoderTextPiece::GetButtonMouseOnColor() const
{
	switch( m_type)
	{
	case DTPT_BUTTON:
		return m_clrButtonMouseOn;
	default:
		return 0;
	}
}

COLOR CDecoderTextPiece::GetButtonMouseOnGradualColor() const
{
	switch( m_type)
	{
	case DTPT_BUTTON:
		return m_clrButtonMouseOnGradual;
	default:
		return 0;
	}
}

FONTEFEECTFLAG CDecoderTextPiece::GetButtonMouseOnEffect() const
{
	switch( m_type)
	{
	case DTPT_BUTTON:
		return m_fefButtonMouseOn;
	default:
		return FET_NONE;
	}
}

COLOR CDecoderTextPiece::GetButtonMouseOnEffectColor() const
{
	switch( m_type)
	{
	case DTPT_BUTTON:
		return m_clrButtonMouseOnEffect;
	default:
		return 0;
	}
}

int CDecoderTextPiece::GetButtonMouseOnImage() const
{
	switch( m_type)
	{
	case DTPT_BUTTON:
		return m_nButtonMouseOnImage;
	default:
		return 0;
	}
}

int CDecoderTextPiece::GetButtonDownImage() const
{
	switch( m_type)
	{
	case DTPT_BUTTON:
		return m_nButtonDownImage;
	default:
		return 0;
	}
}

int CDecoderTextPiece::GetButtonImageWidth() const
{
	switch( m_type)
	{
	case DTPT_BUTTON:
		return m_nButtonImageWidth;
	default:
		return 0;
	}
}

int CDecoderTextPiece::GetButtonImageHeight() const
{
	switch( m_type)
	{
	case DTPT_BUTTON:
		return m_nButtonImageHeight;
	default:
		return 0;
	}
}

const wchar_t* CDecoderTextPiece::GetButtonNotify() const
{
	switch( m_type)
	{
	case DTPT_BUTTON:
		return m_textNotify->c_str();
	default:
		return L"";
	}
}

const wchar_t* CDecoderTextPiece::GetMouseNotify() const
{
	switch( m_type)
	{
	case DTPT_STATIC_IMAGE:
		return m_imageMouseNotify->c_str();
	case DTPT_BUTTON:
		if ( m_buttonMouseNotify->empty())
			return GetButtonNotify();
		else
			return m_buttonMouseNotify->c_str();
	case DTPT_ANIMATE_IMAGE:
		return m_animateMouseNotify->c_str();
	case DTPT_EMOTION_IMAGE:
	    return m_emotionMouseNotify->c_str();
	case DTPT_CUSTOM_PIECE:
		return m_customMouseNotify->c_str();
	default:
	    return L"";
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CDecoderTextPieceList::CDecoderTextPieceList()
	: m_tpv( new INLINECLASS::CTextPieceVector())
{
}

CDecoderTextPieceList::~CDecoderTextPieceList()
{
	delete m_tpv;
}

void CDecoderTextPieceList::Insert( const CDecoderTextPiece& tp)
{
	m_tpv->push_back( tp);
}

int CDecoderTextPieceList::GetCount() const
{
	return (int)m_tpv->size();
}

const CDecoderTextPiece& CDecoderTextPieceList::GetAt( int nIndex) const
{
	assert( nIndex >= 0 && nIndex < GetCount());
	return m_tpv->at( nIndex);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CAbstarctTextDecoder::CAbstarctTextDecoder()
{
}

CAbstarctTextDecoder::~CAbstarctTextDecoder()
{
}

void CAbstarctTextDecoder::Delete()
{
	delete this;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CAbstarctTextEncoder::CAbstarctTextEncoder()
{
}

CAbstarctTextEncoder::~CAbstarctTextEncoder()
{
}

void CAbstarctTextEncoder::AddTextPieces( const CDecoderTextPieceList& tpl)
{
	int nCount = tpl.GetCount();
	for( int i = 0; i < nCount; i ++)
		AddTextPiece( tpl.GetAt( i));
}

}	// namespace GXWINDOW { 

