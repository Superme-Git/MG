#include "gxwindowdef.h"

namespace GXWINDOW { 

namespace INLINECLASS {

class CButtonClickNotifySet : public CNotifySet<CButtonClickNotify*>
{
public:
	CButtonClickNotifySet() {}
	virtual ~CButtonClickNotifySet() {}
};

static int g_DefaultButtonClickAudio = -1;

} // namespace INLINECLASS {


CButtonClickNotify::CButtonClickNotify()
{
}

CButtonClickNotify::~CButtonClickNotify()
{
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void CAbstractButton::SetDefaultClickAudio( int n)
{
	INLINECLASS::g_DefaultButtonClickAudio = n;
}

int CAbstractButton::GetDefaultClickAudio()
{
	return INLINECLASS::g_DefaultButtonClickAudio;
}


CAbstractButton::CAbstractButton()
	: m_bDrawDown( false), m_bMouseMove( false), m_bMouseIn( false), m_cls( new INLINECLASS::CButtonClickNotifySet()), m_nClickAudio( INLINECLASS::g_DefaultButtonClickAudio)
{
}

CAbstractButton::~CAbstractButton()
{
	delete m_cls;
}

void CAbstractButton::SetClickAudio( int n)
{
	m_nClickAudio = n;
}

int CAbstractButton::GetClickAudio() const
{
	return m_nClickAudio;
}

bool CAbstractButton::OnLeftButtonDown( int x, int y, int key)
{
	if( !IsViewOnly() && IsEnable())
	{
		m_bMouseMove = true;
		m_bDrawDown = true;
		m_bMouseIn = true;
		SetCapture();
		INVALIDATE();
	}
	return CComponent::OnLeftButtonDown( x, y, key);
}

void CAbstractButton::OnComponentHide()
{
	ReleaseCapture();
	m_bDrawDown = false;
	m_bMouseMove = false;
	m_bMouseIn = false;
	CComponent::OnComponentHide();
}

bool CAbstractButton::OnLeftButtonUp( int x, int y, int key)
{
	if( !IsViewOnly() && m_bMouseMove)
	{
		m_bDrawDown = false;
		m_bMouseMove = false;
		m_bMouseIn = IsAbsPointIn( x, y);
		if( m_bMouseIn)
			OnButtonClick();
		ReleaseCapture();
		INVALIDATE();
	}
	return CComponent::OnLeftButtonUp( x, y, key);
}

bool CAbstractButton::OnMouseMove( int x, int y, int key)
{
	if( !IsViewOnly() && m_bMouseMove)
	{
		m_bMouseIn = IsAbsPointIn( x, y);
		m_bDrawDown = m_bMouseIn;
	}
	return CComponent::OnMouseMove( x, y, key);
}

void CAbstractButton::OnMouseEnter()
{
	if( !IsViewOnly() && IsEnable())
		m_bMouseIn = true;
	CComponent::OnMouseEnter();
}

void CAbstractButton::OnMouseLeave()
{
	if ( IsViewOnly())
		return;

	m_bMouseIn = false;
	CComponent::OnMouseLeave();
}

void CAbstractButton::OnButtonClick()
{
	if ( !IsViewOnly())
	{
		int nca = GetClickAudio();
		if( -1 != nca)
			CAudioInterface::GetInstance().Play( nca);
		m_cls->NotifyAll( *this, &CButtonClickNotify::OnButtonClick);
	}

	CFrame* parent = GetParentFrame();
	if( NULL == parent || !parent->IsVisible())
	{
		HideTipInfo();
		m_bMouseIn = false;
	}
}

void CAbstractButton::SetButtonDown( bool bDown)
{
	m_bDrawDown = bDown;
	INVALIDATE();
}

void CAbstractButton::SetButtonMouseIn( bool bMouseIn)
{
	m_bMouseIn = bMouseIn;
	INVALIDATE();
}

bool CAbstractButton::IsButtonDown() const
{
	return m_bDrawDown;
}

bool CAbstractButton::IsButtonMouseIn() const
{
	return m_bMouseIn;
}

void CAbstractButton::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CComponent::Draw( draw, l, t, cx, cy, cw, ch);
	DrawButton( draw, l, t, cx, cy, cw, ch, m_bMouseIn, m_bDrawDown);
}

void CAbstractButton::ButtonClickNotifyAdd( CButtonClickNotify& bcn)
{
	m_cls->Insert( &bcn);
}

void CAbstractButton::ButtonClickNotifyRemove( CButtonClickNotify& bcn)
{
	m_cls->Remove( &bcn);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CImageButton::CImageButton()
{
}

CImageButton::~CImageButton()
{
}

const ImageParams& CImageButton::GetImageButtonDown() const
{
	return *m_ips.GetImage( CImageable::MOUSEDOWN);
}

void CImageButton::SetImageButtonDown( const ImageParams& ip)
{
	m_ips.SetImage( ip, CImageable::MOUSEDOWN);
}

void CImageButton::SetComponentImageNormal( const ImageParams& ip)
{
	m_ips.SetImage( ip, CImageable::NORMAL);
}

const ImageParams& CImageButton::GetComponentImageNormal() const
{
	return *m_ips.GetImage( CImageable::NORMAL);
}

void CImageButton::SetComponentImageMouseIn( const ImageParams& ip)
{
	m_ips.SetImage( ip, CImageable::MOUSEON);
}

const ImageParams& CImageButton::GetComponentImageMouseIn() const
{
	return *m_ips.GetImage( CImageable::MOUSEON);
}

const ImageParams& CImageButton::GetComponentImageDisable() const
{
	return *m_ips.GetImage( CImageable::DISABLE);
}

void CImageButton::SetComponentImageDisable( const ImageParams& ip)
{
	m_ips.SetImage( ip, CImageable::DISABLE);
}

void CImageButton::DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown)
{
 	if( IsEnable() &&bButtonDown)
	{
		m_ips.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::MOUSEDOWN);
	}
	else
	{
		if ( IsEnable())
		{
			if ( bMouseIn && m_ips.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::MOUSEON))
				return;
		}
		else if ( m_ips.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::DISABLE))
			return;
		m_ips.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::NORMAL);
	}
}

void CImageButton::OnComponentShow()
{
	m_ips.RegisterImages();
	CAbstractButton::OnComponentShow();
}

void CImageButton::OnComponentHide()
{
	m_ips.FreeImages();
	CAbstractButton::OnComponentHide();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextButton::CTextButton() : m_pctdsm( new CCustomeTextDrawStatManager()), m_bDrawMouseDown( false)
{
}

CTextButton::~CTextButton()
{
	delete m_pctdsm;
}

void CTextButton::DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown)
{
	if( bButtonDown)
	{
		assert( bMouseIn);

		l += 2;
		t += 2;

		if( cx < 2)
		{
			cw -= cx;
			cx = 0;
		}
		else
		{
			cx -= 2;
		}

		if( cy < 2)
		{
			ch -= cy;
			cy = 0;
		}
		else
		{
			cy -= 2;
		}
	}

	if ( 0 == _wcsicmp( L"", GetComponentText()))
	
		return;
	if ( !( m_bDrawMouseDown && bButtonDown && m_pctdsm->DrawTextState( CIB_MOUSEDOWN, draw, &GetTextDrawManager(), GetComponentText(), l, t, cx, cy, cw, ch)))
		TextDraw( draw, l, t, cx, cy, cw, ch, bMouseIn, IsEnable());
}

void CTextButton::SetComponentTextMouseDownColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	m_bDrawMouseDown = true;
	m_pctdsm->SetTextStateColor( CIB_MOUSEDOWN, clr, &GetTextDrawManager(), type, clrEffect, bGradualColor, clrGradual);
}

void CTextButton::ReleaseComponentTextMouseDownColor()
{
	m_bDrawMouseDown = false;
	m_pctdsm->FreeTextDraw( CIB_MOUSEDOWN, &GetTextDrawManager());
}

COLOR CTextButton::GetComponentTextMouseDownColor() const
{
	return m_pctdsm->GetTextStateColor( CIB_MOUSEDOWN);
}

void CTextButton::FreeAllTextDraw()
{
	m_pctdsm->FreeAllTextDraw( &GetTextDrawManager());
	CTextable::FreeAllTextDraw();
}

void CTextButton::SetComponentFont(int nFont)
{
	FreeAllTextDraw();
	CTextable::SetComponentFont( nFont);
	m_pctdsm->SetFont( GetComponentFont(), &GetTextDrawManager());
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTileImageButton::CTileImageButton()
{

}
CTileImageButton::~CTileImageButton()
{

}

void CTileImageButton::DrawButton(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown)
{
	if( IsEnable() &&bButtonDown)
	{
		assert( bMouseIn);
		m_tileimages.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::MOUSEDOWN);
	}
	else
	{
		if ( IsEnable())
		{
			if ( bMouseIn && m_tileimages.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::MOUSEON))
				return;
		}
		else if ( m_tileimages.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::DISABLE))
			return;

		m_tileimages.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::NORMAL);
	}
}

void CTileImageButton::OnComponentShow()
{
	m_tileimages.RegisterImages();
	CAbstractButton::OnComponentShow();
}

void CTileImageButton::OnComponentHide()
{
	m_tileimages.FreeImages();
	CAbstractButton::OnComponentHide();
}

CImagesFill& CTileImageButton::GetTileImageNormal()
{
	return m_tileimages.GetTileImage( CTileImageAble::NORMAL);
}

CImagesFill& CTileImageButton::GetTileImageMouseIn()
{
	return m_tileimages.GetTileImage( CTileImageAble::MOUSEON);
}

CImagesFill& CTileImageButton::GetTileImageButtonDown()
{
	return m_tileimages.GetTileImage( CTileImageAble::MOUSEDOWN);
}

CImagesFill& CTileImageButton::GetTileImageDisable()
{
	return m_tileimages.GetTileImage( CTileImageAble::DISABLE);
}

void CTileImageButton::FreeAllImage()
{
	m_tileimages.Clear();
}

void CTileImageButton::SetSize(int w, int h)
{
	CAbstractButton::SetSize( w, h);

	if ( m_tileimages.IsContainImage( CTileImageAble::NORMAL))
	{
		CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::NORMAL);
		tile.SetGridSize( GetWidth() - tile.GetGridWidth( IFP_TOPLEFT) - tile.GetGridWidth( IFP_BOTTOMRIGHT), GetHeight() - tile.GetGridHeight( IFP_TOPLEFT) - tile.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
	}

	if ( m_tileimages.IsContainImage( CTileImageAble::MOUSEON))
	{
		CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::MOUSEON);
		//tile.SetTileSize( GetWidth(), GetHeight());
		tile.SetGridSize( GetWidth() - tile.GetGridWidth( IFP_TOPLEFT) - tile.GetGridWidth( IFP_BOTTOMRIGHT), GetHeight() - tile.GetGridHeight( IFP_TOPLEFT) - tile.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
	}

	if ( m_tileimages.IsContainImage( CTileImageAble::MOUSEDOWN))
	{
		CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::MOUSEDOWN);
		tile.SetGridSize( GetWidth() - tile.GetGridWidth( IFP_TOPLEFT) - tile.GetGridWidth( IFP_BOTTOMRIGHT), GetHeight() - tile.GetGridHeight( IFP_TOPLEFT) - tile.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
	}

	if ( m_tileimages.IsContainImage( CTileImageAble::DISABLE))
	{
		CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::DISABLE);
		tile.SetGridSize( GetWidth() - tile.GetGridWidth( IFP_TOPLEFT) - tile.GetGridWidth( IFP_BOTTOMRIGHT), GetHeight() - tile.GetGridHeight( IFP_TOPLEFT) - tile.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextImageButton::CTextImageButton() : m_pctdsm( new CCustomeTextDrawStatManager())
{
}

CTextImageButton::~CTextImageButton()
{
	delete m_pctdsm;
}

void CTextImageButton::DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown)
{
	CTileImageButton::DrawButton( draw, l, t, cx, cy, cw, ch, bMouseIn, bButtonDown);

	l += max( 0, ( int)( 0.5f + ( GetWidth() - GetTextDrawManager().GetStringPixelWidth( m_text->c_str(), GetComponentFont())) * 0.5f));
	t += max( 0, ( int)( 0.5f + ( GetHeight() - GetTextDrawManager().GetFontHeightWithoutUnderline( GetComponentFont())) * 0.5f));

	if ( bButtonDown)
		m_pctdsm->DrawTextState( CIB_MOUSEDOWN, draw, &GetTextDrawManager(), GetComponentText(), l + 1, t + 1, cx, cy, cw, ch);
	else
		TextDraw( draw, l, t, cx, cy, cw, ch, bMouseIn, IsEnable());
}

void CTextImageButton::SetComponentTextMouseDownColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	m_pctdsm->SetFont( GetComponentFont(), &GetTextDrawManager());
	m_pctdsm->SetTextStateColor( CIB_MOUSEDOWN, clr, &GetTextDrawManager(), type, clrEffect,bGradualColor, clrGradual);
}

void CTextImageButton::ReleaseComponentTextMouseDownColor()
{
	m_pctdsm->FreeTextDraw( CIB_MOUSEDOWN, &GetTextDrawManager());
}

FONTEFEECTFLAG CTextImageButton::GetComponentTextMouseDownEffect() const
{
	return m_pctdsm->GetTextStateEffect( CIB_MOUSEDOWN);
}

COLOR CTextImageButton::GetComponentTextMouseDownEffectColor() const
{
	return m_pctdsm->GetTextStateEffectColor( CIB_MOUSEDOWN);
}

COLOR CTextImageButton::GetComponentTextMouseDownColor() const
{
	return m_pctdsm->GetTextStateColor( CIB_MOUSEDOWN);
}

void CTextImageButton::FreeAllTextDraw()
{
	m_pctdsm->FreeAllTextDraw( &GetTextDrawManager());
	CTextable::FreeAllTextDraw();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/************************************************************************/
/* 
图像选择框，五个图像参数分别为
1. 常态未选（包括框部分和右边图像文字）
2. 常态已选（即框中所显示的钩，选中后同时画1和2即显示为选中）
3. 悬浮态未选（同1）
4. 悬浮态已选（同2）
5. disable（同1）

文字选择框图像：
1. 常态未选（只含框部分）
2. 常态已选（框中所显示的钩，选中后同时画1和2即显示为选中）
3. 悬浮态未选（同1）
4. 悬浮态已选（同2）
5. disable（同1）
而右边文字则有三种：常态，悬浮态，disable                               
																		*/
/************************************************************************/
CAbstractCheckButton::CAbstractCheckButton()
	: m_bChecked( false), m_nBoxWidth( 0), m_bbs( BBS_NONE), m_bBoxMutex( false)
{
	SetClickAudio( -1); // 默认不要有声音
}

CAbstractCheckButton::~CAbstractCheckButton()
{
}

void CAbstractCheckButton::SetStyle( int x, int y, int w, int h, int nBoxWidth, BackGroundStyle bgs, ButtonBoxStyle bbs)
{
	SetBoxWidth( nBoxWidth);
	SetButtonBoxStyle( bbs);
}

void CAbstractCheckButton::SetChecked( bool bCheck, bool bNotify)
{
	SetChecked( bCheck, bNotify, true);
}

bool CAbstractCheckButton::IsChecked() const
{
	return m_bChecked;
}

void CAbstractCheckButton::SetSize( int w, int h)
{
	CAbstractButton::SetSize( w, h);

	if ( BBS_TILE == m_bbs || BBS_IMAGE == m_bbs)
	{
		if ( m_tileimages.IsContainImage( CTileImageAble::NORMAL))
		{
			CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::NORMAL);
			tile.SetGridSize( GetWidth() - tile.GetGridWidth( IFP_TOPLEFT) - tile.GetGridWidth( IFP_BOTTOMRIGHT), GetHeight() - tile.GetGridHeight( IFP_TOPLEFT) - tile.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
		}

		if ( m_tileimages.IsContainImage( CTileImageAble::MOUSEON))
		{
			CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::MOUSEON);
			tile.SetGridSize( GetWidth() - tile.GetGridWidth( IFP_TOPLEFT) - tile.GetGridWidth( IFP_BOTTOMRIGHT), GetHeight() - tile.GetGridHeight( IFP_TOPLEFT) - tile.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
		}

		if ( m_tileimages.IsContainImage( CTileImageAble::SELECTED_NORMAL))
		{
			CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::SELECTED_NORMAL);
			tile.SetGridSize( m_nBoxWidth - tile.GetGridWidth( IFP_TOPLEFT) - tile.GetGridWidth( IFP_BOTTOMRIGHT), GetHeight() - tile.GetGridHeight( IFP_TOPLEFT) - tile.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
		}

		if ( m_tileimages.IsContainImage( CTileImageAble::SELECTED_MOUSEON))
		{
			CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::SELECTED_MOUSEON);
			tile.SetGridSize( m_nBoxWidth - tile.GetGridWidth( IFP_TOPLEFT) - tile.GetGridWidth( IFP_BOTTOMRIGHT), GetHeight() - tile.GetGridHeight( IFP_TOPLEFT) - tile.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
		}

		if ( m_tileimages.IsContainImage( CTileImageAble::DISABLE))
		{
			CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::DISABLE);
			tile.SetGridSize( GetWidth() - tile.GetGridWidth( IFP_TOPLEFT) - tile.GetGridWidth( IFP_BOTTOMRIGHT), GetHeight() - tile.GetGridHeight( IFP_TOPLEFT) - tile.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
		}
	}
}

void CAbstractCheckButton::SetBoxWidth( int w)
{
	m_nBoxWidth = w;

	if ( BBS_TILE == m_bbs || BBS_IMAGE == m_bbs)
	{
		if ( m_tileimages.IsContainImage( CTileImageAble::SELECTED_NORMAL))
		{
			CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::SELECTED_NORMAL);
			tile.SetGridSize( m_nBoxWidth - tile.GetGridWidth( IFP_TOPLEFT) - tile.GetGridWidth( IFP_BOTTOMRIGHT), GetHeight() - tile.GetGridHeight( IFP_TOPLEFT) - tile.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
		}

		if ( m_tileimages.IsContainImage( CTileImageAble::SELECTED_MOUSEON))
		{
			CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::SELECTED_MOUSEON);
			tile.SetGridSize( m_nBoxWidth - tile.GetGridWidth( IFP_TOPLEFT) - tile.GetGridWidth( IFP_BOTTOMRIGHT), GetHeight() - tile.GetGridHeight( IFP_TOPLEFT) - tile.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
		}
	}
	else
	{
		INVALIDATE();
	}
}

int CAbstractCheckButton::GetBoxWidth() const
{
	return m_nBoxWidth;
}

int CAbstractCheckButton::GetAbsBoxWidth() const
{
	return m_nBoxWidth;
}

void CAbstractCheckButton::SetButtonBoxStyle( ButtonBoxStyle bbs)
{
	m_bbs = bbs;

	if ( !( m_bbs == BBS_TILE || m_bbs == BBS_IMAGE))
		m_tileimages.Clear();
	INVALIDATE();
}

ButtonBoxStyle CAbstractCheckButton::GetButtonBoxStyle() const
{
	return m_bbs;
}

void CAbstractCheckButton::SetChecked( bool bCheck, bool bNotify, bool bOnCheck)
{
	m_bChecked = bCheck;
	if ( bCheck && bOnCheck)
		OnButtonCheck( bCheck);
	if ( bNotify)
		CAbstractButton::OnButtonClick();
	INVALIDATE();
}

void CAbstractCheckButton::OnButtonClick()
{
	SetChecked( !m_bChecked);
}

void CAbstractCheckButton::OnButtonCheck( bool bCheck)
{
}

void CAbstractCheckButton::SetButtonBoxStyleImage( const ImageParams& normal, const ImageParams& selected, const ImageParams& mouseonnormal, const ImageParams& mouseonselect, const ImageParams& disable)
{
	m_tileimages.Clear();

	m_bbs = BBS_IMAGE;

	if ( normal.nBitmap >= 0)
	{
		CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::NORMAL);
		tile.SetTileImage( normal, IFP_CENTER);
		tile.SetTileSize( GetWidth(), GetHeight());
		tile.SetGridSize( GetWidth(), GetHeight(), IFP_CENTER);
	}

	if ( mouseonnormal.nBitmap >= 0)
	{
		CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::MOUSEON);
		tile.SetTileImage( mouseonnormal, IFP_CENTER);
		tile.SetTileSize( GetWidth(), GetHeight());
		tile.SetGridSize( GetWidth(), GetHeight(), IFP_CENTER);
	}

	if ( selected.nBitmap >= 0)
	{
		CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::SELECTED_NORMAL);
		tile.SetTileImage( selected, IFP_CENTER);
		tile.SetTileSize( GetWidth(), GetHeight());
		tile.SetGridSize( GetWidth(), GetHeight(), IFP_CENTER);
	}

	if ( mouseonselect.nBitmap >= 0)
	{
		CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::SELECTED_MOUSEON);
		tile.SetTileImage( mouseonselect, IFP_CENTER);
		tile.SetTileSize( GetWidth(), GetHeight());
		tile.SetGridSize( GetWidth(), GetHeight(), IFP_CENTER);
	}

	if ( disable.nBitmap >= 0)
	{
		CImagesFill& tile = m_tileimages.GetTileImage( CTileImageAble::DISABLE);
		tile.SetTileImage( disable, IFP_CENTER);
		tile.SetTileSize( GetWidth(), GetHeight());
		tile.SetGridSize( GetWidth(), GetHeight(), IFP_CENTER);
	}

	INVALIDATE();
}

void CAbstractCheckButton::DrawCheckBox( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bCheck)
{
}

void CAbstractCheckButton::DoDrawCheckBox( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bCheck)
{
	switch( m_bbs)
	{
	case BBS_CUSTOM:
		DrawCheckBox( draw, l, t, cx, cy, cw, ch, bMouseIn, bCheck);
		break;
	case BBS_TYPAL:
		DrawCheckBoxTypal( draw, l, t, cx, cy, cw, ch, bMouseIn, bCheck);
		break;
	case BBS_IMAGE:
	case BBS_TILE:
		DoDrawCheckBoxTile( draw, l, t, cx, cy, cw, ch, bMouseIn, bCheck);
		break;
	default:
		break;
	}
}

void CAbstractCheckButton::DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown)
{
	if ( m_nBoxWidth > 0 && m_nBoxWidth <= GetWidth())
	{
		DoDrawCheckBox( draw, l, t, cx, cy, cw, ch, bMouseIn, m_bChecked);
	}
}

void CAbstractCheckButton::DrawCheckBoxTypal( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bCheck)
{
	COLOR	clr = MakeColor( 0, 0, 0);
	l += (cx + 5);
	t += (cy + 5);
	int r = l + cw - cx - 8;
	int b = t + ch - cy - 8;

	draw.Rect( l, t, r, b, clr);
	if( bCheck)
	{
		draw.Line( l, t, r, b, clr);
		draw.Line( r, t, l, b, clr);
	}
}

void CAbstractCheckButton::DoDrawCheckBoxTile( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bCheck)
{
	if ( !IsEnable())
	{
		if ( m_bChecked)
		{
			if ( !m_bBoxMutex)
				m_tileimages.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::NORMAL);
			m_tileimages.DrawImage( draw, l, t, cx, cy, min( cw, GetBoxWidth() - cx), ch, CImageable::SELECTED_NORMAL);
			return;
		}
		if ( !m_tileimages.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::DISABLE))
			m_tileimages.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::NORMAL);
		return;
	}

	bool bDrawOn = false;
	if ( bMouseIn && ( !m_bBoxMutex || !m_bChecked || m_nBoxWidth != GetWidth()))
		bDrawOn = m_tileimages.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::MOUSEON);
	if ( !bDrawOn && ( !m_bBoxMutex || !m_bChecked || m_nBoxWidth != GetWidth()))
		m_tileimages.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::NORMAL);

	if ( m_bChecked && cx < GetBoxWidth())
	{
		if ( !( bMouseIn && m_tileimages.DrawImage( draw, l, t, cx, cy, min( cw, GetBoxWidth() - cx), ch, CImageable::SELECTED_MOUSEON)) )
			m_tileimages.DrawImage( draw, l, t, cx, cy, min( cw, GetBoxWidth() - cx), ch, CImageable::SELECTED_NORMAL);
	}
}

void CAbstractCheckButton::OnComponentShow()
{
	m_tileimages.RegisterImages();
	CAbstractButton::OnComponentShow();
}

void CAbstractCheckButton::OnComponentHide()
{
	m_tileimages.FreeImages();
	CAbstractButton::OnComponentHide();
}

CImagesFill& CAbstractCheckButton::GetTileImageNormal()
{
	return m_tileimages.GetTileImage( CTileImageAble::NORMAL);
}

CImagesFill& CAbstractCheckButton::GetTileImageMouseIn()
{
	return m_tileimages.GetTileImage( CTileImageAble::MOUSEON);
}

CImagesFill& CAbstractCheckButton::GetTileImageSelect()
{
	return m_tileimages.GetTileImage( CTileImageAble::SELECTED_NORMAL);
}

CImagesFill& CAbstractCheckButton::GetTileImageMouseInSelect()
{
	return m_tileimages.GetTileImage( CTileImageAble::SELECTED_MOUSEON);
}

CImagesFill& CAbstractCheckButton::GetTileImageDisable()
{
	return m_tileimages.GetTileImage( CTileImageAble::DISABLE);
}

void CAbstractCheckButton::SetBoxMutex( bool bMutex)
{
	m_bBoxMutex = bMutex;
}

bool CAbstractCheckButton::IsBoxMutex() const
{
	return m_bBoxMutex;
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CAbstractTextCheckButton::CAbstractTextCheckButton()
{

}

CAbstractTextCheckButton::~CAbstractTextCheckButton()
{

}

void CAbstractTextCheckButton::DrawButton(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown)
{
	if( GetBoxWidth() > 0 && GetBoxWidth() <= GetWidth())
	{
		if( cx < GetBoxWidth())
		{
			DoDrawCheckBox( draw, l, t, cx, cy, GetBoxWidth() - cx, ch, IsButtonMouseIn(), IsChecked());
			if( cw > GetBoxWidth())
				DrawButtonTip( draw, l + GetBoxWidth(), t, 0, cy, cw - GetBoxWidth() + cx, ch, IsButtonMouseIn());
		}
		else
		{
			DrawButtonTip( draw, l + GetBoxWidth(), t, cx - GetBoxWidth(), cy, cw, ch, IsButtonMouseIn());
		}
	}
	else
	{
		DrawButtonTip( draw, l, t, cx, cy, cw, ch, IsButtonMouseIn());
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CAbstractTextRadioButton::CAbstractTextRadioButton()
{

}

CAbstractTextRadioButton::~CAbstractTextRadioButton()
{

}

void CAbstractTextRadioButton::DrawButton(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown)
{
	if( GetBoxWidth() > 0 && GetBoxWidth() <= GetWidth())
	{
		if( cx < GetBoxWidth())
		{
			DoDrawCheckBox( draw, l, t, cx, cy, GetBoxWidth() - cx, ch, IsButtonMouseIn(), IsChecked());
			if( cw > GetBoxWidth())
				DrawButtonTip( draw, l + GetBoxWidth(), t, 0, cy, cw - GetBoxWidth() + cx, ch, IsButtonMouseIn());
		}
		else
		{
			DrawButtonTip( draw, l + GetBoxWidth(), t, cx - GetBoxWidth(), cy, cw, ch, IsButtonMouseIn());
		}
	}
	else
	{
		DrawButtonTip( draw, l, t, cx, cy, cw, ch, IsButtonMouseIn());
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CAbstractRadioButton::CAbstractRadioButton()
	: m_nGroupID( 0)
{
}

CAbstractRadioButton::~CAbstractRadioButton()
{
}

void CAbstractRadioButton::SetChecked( bool bCheck, bool bNotify)
{
	CAbstractCheckButton::SetChecked( bCheck, bNotify);
}

void CAbstractRadioButton::OnButtonClick()
{
	SetChecked();
}

void CAbstractRadioButton::SetChecked( bool bCheck, bool bNotify, bool bOnCheck)
{
	CAbstractCheckButton::SetChecked( bCheck, bNotify, bOnCheck);
}

void CAbstractRadioButton::SetChecked( bool bCheck)
{
	if( IsChecked() && bCheck)
		return;
	else
		CAbstractCheckButton::SetChecked( bCheck, bCheck);
}

void CAbstractRadioButton::SetGroupID( int id)
{
	m_nGroupID = id;
}

int CAbstractRadioButton::GetGroupID() const
{
	return m_nGroupID;
}

void CAbstractRadioButton::SetStyle( int x, int y, int w, int h, int nGroupID, int nBoxWidth, BackGroundStyle bgs, ButtonBoxStyle bbs)
{
	SetGroupID( nGroupID);
}	

void CAbstractRadioButton::OnButtonCheck( bool bCheck)
{
	CPanel* panel = dynamic_cast<CPanel*>( GetParent());

	if( panel)
	{
		int nCount = panel->GetChildCount();
		for( int i = 0; i < nCount; i ++)
		{
			CComponent* p = panel->GetChild( i);
			CAbstractRadioButton* rb = dynamic_cast<CAbstractRadioButton*>(p);
			if( NULL == rb)
				continue;
            if( rb->m_nGroupID != m_nGroupID)
				continue;
			if( rb->IsSameComponent( this))
				continue;
			rb->SetChecked( false, false, false);
		}
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CImageCheckButton::CImageCheckButton()
{
}

CImageCheckButton::~CImageCheckButton()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextCheckButton::CTextCheckButton()
{
}

CTextCheckButton::~CTextCheckButton()
{
}

void CTextCheckButton::DrawButtonTip( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn)
{
	TextDraw( draw, l, t, cx, cy, cw, ch, bMouseIn, IsEnable());
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CImageRadioButton::CImageRadioButton()
{
}

CImageRadioButton::~CImageRadioButton()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextRadioButton::CTextRadioButton()
{
}

CTextRadioButton::~CTextRadioButton()
{
}

void CTextRadioButton::DrawButtonTip( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn)
{
	TextDraw( draw, l, t, cx, cy, cw, ch, bMouseIn, IsEnable());
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextImageRadioButton::CTextImageRadioButton() : m_pctdsm( new CCustomeTextDrawStatManager()), m_bDrawSelected( false), m_bDrawSelectedMouseIn( false)
{

}

CTextImageRadioButton::~CTextImageRadioButton()
{
	delete m_pctdsm;
}

void CTextImageRadioButton::DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown)
{
	CAbstractRadioButton::DrawButton( draw, l, t, cx, cy, cw, ch, bMouseIn, bButtonDown);

	int fontw = GetTextDrawManager().GetStringPixelWidth( m_text->c_str(), GetComponentFont());
	int fonth = GetTextDrawManager().GetFontHeightWithoutUnderline( GetComponentFont());
	int ox = ( int)( 0.5f + ((float)( GetWidth() - fontw) / 2.0f));
	m_nOffsetX = max( 0, ox - cx);

	int oy = ( int)( 0.5f + ((float)( GetHeight() - fonth) / 2.0f));
	m_nOffsetY = max( 0, oy - cy);

	int fontx = max( 0, cx - ox);
	int fonty = max( 0, cy - oy);

	if ( IsChecked())
	{
		bMouseIn = bMouseIn && m_bDrawSelectedMouseIn;
		if ( bMouseIn)
		{
			m_pctdsm->DrawTextState( CTIRB_SELECTEDMOUSEIN, draw, &GetTextDrawManager(), GetComponentText(), l + cx + m_nOffsetX - fontx, t + cy + m_nOffsetY - fonty, fontx, fonty, cx + cw - ox, cy + ch - oy);
			return;
		}
		if ( m_bDrawSelected)
		{
			m_pctdsm->DrawTextState( CTIRB_SELECTED, draw, &GetTextDrawManager(), GetComponentText(), l + cx + m_nOffsetX - fontx, t + cy + m_nOffsetY - fonty, fontx, fonty, cx + cw - ox, cy + ch - oy);
			return;
		}
	}
	TextDraw( draw, l + cx - fontx, t + cy - fonty, fontx, fonty, cw /*cx + cw - ox*/, ch /*cy + ch - oy*/, bMouseIn, IsEnable());
}

void CTextImageRadioButton::SetComponentTextSelectedColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	m_bDrawSelected = true;
	m_pctdsm->SetFont( GetComponentFont(), &GetTextDrawManager());
	m_pctdsm->SetTextStateColor( CTIRB_SELECTED, clr, &GetTextDrawManager(), type, clrEffect, bGradualColor, clrGradual);
}
void CTextImageRadioButton::ReleaseComponentTextSelectedColor()
{
	m_bDrawSelected = false;
	m_pctdsm->FreeTextDraw( CTIRB_SELECTED, &GetTextDrawManager());
}

COLOR CTextImageRadioButton::GetComponentTextSelectedColor() const
{
	return m_pctdsm->GetTextStateColor( CTIRB_SELECTED);
}

FONTEFEECTFLAG CTextImageRadioButton::GetComponentTextSelectedEffect() const
{
	return m_pctdsm->GetTextStateEffect( CTIRB_SELECTED);
}

COLOR CTextImageRadioButton::GetComponentTextMouseSelectedEffectColor() const
{
	return m_pctdsm->GetTextStateEffectColor( CTIRB_SELECTED);
}

bool CTextImageRadioButton::IsTextMouseSelectGradualColor() const
{
	return m_pctdsm->IsTextStateGradualColor( CTIRB_SELECTED);
}

COLOR CTextImageRadioButton::GetComponentTextMouseSelectedGradualColor() const
{
	return m_pctdsm->GetTextStateGradualColor( CTIRB_SELECTED);
}

void CTextImageRadioButton::SetComponentTextSelectedMouseInColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	m_bDrawSelectedMouseIn = true;
	m_pctdsm->SetFont( GetComponentFont(), &GetTextDrawManager());
	m_pctdsm->SetTextStateColor( CTIRB_SELECTEDMOUSEIN, clr, &GetTextDrawManager(), type, clrEffect, bGradualColor, clrGradual);
}

void CTextImageRadioButton::ReleaseComponentTextSelectedMouseInColor()
{
	m_bDrawSelectedMouseIn = false;
	m_pctdsm->FreeTextDraw( CTIRB_SELECTEDMOUSEIN, &GetTextDrawManager());
}

COLOR CTextImageRadioButton::GetComponentTextSelectedMouseInColor() const
{
	return m_pctdsm->GetTextStateColor( CTIRB_SELECTEDMOUSEIN);	
}

FONTEFEECTFLAG CTextImageRadioButton::GetComponentTextSelectedMouseInEffect() const
{
	return m_pctdsm->GetTextStateEffect( CTIRB_SELECTEDMOUSEIN);
}

COLOR CTextImageRadioButton::GetComponentTextSelectedMouseInEffectColor() const
{
	return m_pctdsm->GetTextStateEffectColor( CTIRB_SELECTEDMOUSEIN);
}

bool CTextImageRadioButton::IsTextTextSelectedMouseInGradualColor() const
{
	return m_pctdsm->IsTextStateGradualColor( CTIRB_SELECTEDMOUSEIN);
}

COLOR CTextImageRadioButton::GetComponentTextSelectedMouseSelectedGradualColor() const
{
	return m_pctdsm->GetTextStateGradualColor( CTIRB_SELECTEDMOUSEIN);
}

void CTextImageRadioButton::SetSize(int w, int h)
{
	CAbstractRadioButton::SetSize( w, h);
	CAbstractRadioButton::SetBoxWidth( w);
}

void CTextImageRadioButton::SetBoxWidth( int w) {}

void CTextImageRadioButton::FreeAllTextDraw()
{
	m_pctdsm->FreeAllTextDraw( &GetTextDrawManager());
	CTextable::FreeAllTextDraw();
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


CTextImageCheckButton::CTextImageCheckButton() : m_pctdsm( new CCustomeTextDrawStatManager()), m_bDrawSelected( false), m_bDrawSelectedMouseIn( false)
{

}

CTextImageCheckButton::~CTextImageCheckButton()
{
	delete m_pctdsm;
}

void CTextImageCheckButton::DrawButton( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bMouseIn, bool bButtonDown)
{
	CAbstractCheckButton::DrawButton( draw, l, t, cx, cy, cw, ch, bMouseIn, bButtonDown);

	int ox = ( int)( 0.5f + ((float)( GetWidth() - GetTextDrawManager().GetStringPixelWidth( m_text->c_str(), GetComponentFont())) / 2.0f));
	m_nOffsetX = max( 0, ox - cx);

	int oy = ( int)( 0.5f + ((float)( GetHeight() - GetTextDrawManager().GetFontHeightWithoutUnderline( GetComponentFont())) / 2.0f));
	m_nOffsetY = max( 0, oy - cy);

	int fontx = max( 0, cx - ox);
	int fonty = max( 0, cy - oy);

	if ( IsChecked())
	{
		bMouseIn = bMouseIn && m_bDrawSelectedMouseIn;
		if ( bMouseIn)
		{
			m_pctdsm->DrawTextState( CTIRB_SELECTEDMOUSEIN, draw, &GetTextDrawManager(), GetComponentText(), l + cx + m_nOffsetX - fontx, t + cy + m_nOffsetY - fonty, fontx, fonty, cx + cw - ox, cy + ch - oy);
			return;
		}
		if ( m_bDrawSelected)
		{
			m_pctdsm->DrawTextState( CTIRB_SELECTED, draw, &GetTextDrawManager(), GetComponentText(), l + cx + m_nOffsetX - fontx, t + cy + m_nOffsetY - fonty, fontx, fonty, cx + cw - ox, cy + ch - oy);
			return;
		}
	}
	TextDraw( draw, l + cx - fontx, t + cy - fonty, fontx, fonty, cw /*cx + cw - ox*/, ch /*cy + ch - oy*/, bMouseIn, IsEnable());
}

void CTextImageCheckButton::SetComponentTextSelectedColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	m_bDrawSelected = true;
	m_pctdsm->SetFont( GetComponentFont(), &GetTextDrawManager());
	m_pctdsm->SetTextStateColor( CTIRB_SELECTED, clr, &GetTextDrawManager(), type, clrEffect, bGradualColor, clrGradual);
}
void CTextImageCheckButton::ReleaseComponentTextSelectedColor()
{
	m_bDrawSelected = false;
	m_pctdsm->FreeTextDraw( CTIRB_SELECTED, &GetTextDrawManager());
}

COLOR CTextImageCheckButton::GetComponentTextSelectedColor() const
{
	return m_pctdsm->GetTextStateColor( CTIRB_SELECTED);
}

FONTEFEECTFLAG CTextImageCheckButton::GetComponentTextSelectedEffect() const
{
	return m_pctdsm->GetTextStateEffect( CTIRB_SELECTED);
}

COLOR CTextImageCheckButton::GetComponentTextMouseSelectedEffectColor() const
{
	return m_pctdsm->GetTextStateEffectColor( CTIRB_SELECTED);
}

bool CTextImageCheckButton::IsTextMouseSelectGradualColor() const
{
	return m_pctdsm->IsTextStateGradualColor( CTIRB_SELECTED);
}

COLOR CTextImageCheckButton::GetComponentTextMouseSelectedGradualColor() const
{
	return m_pctdsm->GetTextStateGradualColor( CTIRB_SELECTED);
}

void CTextImageCheckButton::SetComponentTextSelectedMouseInColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	m_bDrawSelectedMouseIn = true;
	m_pctdsm->SetFont( GetComponentFont(), &GetTextDrawManager());
	m_pctdsm->SetTextStateColor( CTIRB_SELECTEDMOUSEIN, clr, &GetTextDrawManager(), type, clrEffect, bGradualColor, clrGradual);
}

void CTextImageCheckButton::ReleaseComponentTextSelectedMouseInColor()
{
	m_bDrawSelectedMouseIn = false;
	m_pctdsm->FreeTextDraw( CTIRB_SELECTEDMOUSEIN, &GetTextDrawManager());
}

COLOR CTextImageCheckButton::GetComponentTextSelectedMouseInColor() const
{
	return m_pctdsm->GetTextStateColor( CTIRB_SELECTEDMOUSEIN);	
}

FONTEFEECTFLAG CTextImageCheckButton::GetComponentTextSelectedMouseInEffect() const
{
	return m_pctdsm->GetTextStateEffect( CTIRB_SELECTEDMOUSEIN);
}

COLOR CTextImageCheckButton::GetComponentTextSelectedMouseInEffectColor() const
{
	return m_pctdsm->GetTextStateEffectColor( CTIRB_SELECTEDMOUSEIN);
}

bool CTextImageCheckButton::IsTextTextSelectedMouseInGradualColor() const
{
	return m_pctdsm->IsTextStateGradualColor( CTIRB_SELECTEDMOUSEIN);
}

COLOR CTextImageCheckButton::GetComponentTextSelectedMouseSelectedGradualColor() const
{
	return m_pctdsm->GetTextStateGradualColor( CTIRB_SELECTEDMOUSEIN);
}

void CTextImageCheckButton::SetSize(int w, int h)
{
	CAbstractCheckButton::SetSize( w, h);
	CAbstractCheckButton::SetBoxWidth( w);
}

void CTextImageCheckButton::SetBoxWidth( int w) {}

void CTextImageCheckButton::FreeAllTextDraw()
{
	m_pctdsm->FreeAllTextDraw( &GetTextDrawManager());
	CTextable::FreeAllTextDraw();
}

} //namespace GXWINDOW { 
