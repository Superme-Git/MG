#include "gxwindowdef.h"

namespace GXWINDOW { 

namespace INLINECLASS { 

static CCombinationListBoxAllocator	g_DefaultCombinationListBoxAllocator;

struct ListDataStringTextParam
{
	std::wstring		str;
	unsigned int		param;
	unsigned __int64	lparam;
	int					n;
	int					s;
	int					m;

	void ReleaeTextDraw( CTextDrawManager& tdm)
	{
		if( -1 != n)
		{
			tdm.FreeTextDraw( n);
			n = -1;
		}
		if( -1 != s)
		{
			tdm.FreeTextDraw( s);
			s = -1;
		}
		if( -1 != m)
		{
			tdm.FreeTextDraw( m);
			m = -1;
		}
	}
};

struct ListDataImageParam
{
	ImageParams					ip;
	unsigned int				uiParam;
};

class CListDataStringList : public std::vector<ListDataStringTextParam>
{
public:
	CListDataStringList() { }
	virtual ~CListDataStringList() { }
};

class CListDataImageList : public std::vector<ListDataImageParam>
{
public:
	CListDataImageList() {}
	virtual ~CListDataImageList() {}
};

class CListDataModuleDataChangeSet : public CNotifySet<CListDataModuleDataChangeNotify*>
{
public:
	CListDataModuleDataChangeSet() { }
	virtual ~CListDataModuleDataChangeSet() { }
};

class CListSelectIndexSet : public std::set<int>
{
public:
	CListSelectIndexSet() { };
	virtual ~CListSelectIndexSet() { };
};

class CListBoxSelectChangeNotifySet : public CNotifySet<CListBoxSelectChangeNotify*>
{
public:
	CListBoxSelectChangeNotifySet() {}
	virtual ~CListBoxSelectChangeNotifySet() {}
};

class CComboBoxSelectChangeNotifySet : public CNotifySet<CComboBoxSelectChangeNotify*>
{
public:
	CComboBoxSelectChangeNotifySet() {}
	virtual ~CComboBoxSelectChangeNotifySet() {}
};

class CListBoxMouseOnChangeNotifySet : public CNotifySet<CListBoxMouseOnChangeNotify*>
{
public:
	CListBoxMouseOnChangeNotifySet() {}
	virtual ~CListBoxMouseOnChangeNotifySet() {}
};

CComboBoxComponentAllocator	g_DefaultComboBoxAllocator;

}  // namespace INLINECLASS { 

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


CListDataModuleDataChangeNotify::CListDataModuleDataChangeNotify()
{
}

CListDataModuleDataChangeNotify::~CListDataModuleDataChangeNotify()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CListDataModule::CListDataModule()
: m_pParent( NULL), m_bShow( false), m_nset( new INLINECLASS::CListDataModuleDataChangeSet()), m_fZoomScale( 1.0f)
{
}

CListDataModule::~CListDataModule()
{
	if ( m_bShow)
		OnComponentHide();
	if ( m_pParent)
		m_pParent->ResetListDataModule( this);

	delete m_nset;
}

void CListDataModule::OnComponentShow()
{
	m_bShow = true;
	m_bgRowImg.RegisterImages();
}

void CListDataModule::OnComponentHide()
{
	m_bShow = false;
	m_bgRowImg.FreeImages();
}

void CListDataModule::FireDataChanged()
{
	m_nset->NotifyAll( *this, &CListDataModuleDataChangeNotify::OnListDataModuleDataChange);
	INVALIDATE();
}

void CListDataModule::ListDataModuleDataChangeNotifyAdd( CListDataModuleDataChangeNotify& dcn)
{
	m_nset->Insert( &dcn);
}

void CListDataModule::ListDataModuleDataChangeNotifyRemove( CListDataModuleDataChangeNotify& dcn)
{
	m_nset->Remove( &dcn);
}

CImagesFill& CListDataModule::GetRowBackGround()
{
	return m_bgRowImg;
}

void CListDataModule::DrawRowBackGround( int nIndex, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, bool bDrawFullBG)
{
	if ( bDrawFullBG)
		m_bgRowImg.SetGridSize( m_bgRowImg.GetGridWidth( IFP_TOPLEFT), m_bgRowImg.GetGridHeight( IFP_BOTTOMRIGHT), IFP_TOPLEFT);
	else
		m_bgRowImg.SetGridSize( m_bgRowImg.GetGridWidth( IFP_TOPLEFT), 0, IFP_TOPLEFT);
	m_bgRowImg.SetGridSize( cw - m_bgRowImg.GetGridWidth( IFP_TOPLEFT) - m_bgRowImg.GetGridWidth( IFP_BOTTOMRIGHT), ch - m_bgRowImg.GetGridHeight( IFP_TOPLEFT) - m_bgRowImg.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
	m_bgRowImg.Draw( draw, l + cx, t + cy, 0, 0, cw, ch);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
CImageListDataModlue::CImageListDataModlue():
m_pList( new INLINECLASS::CListDataImageList )
{
	
}

CImageListDataModlue::~CImageListDataModlue()
{
	m_pList->clear();
	delete m_pList;
}

void CImageListDataModlue::DrawClientComponent( int nIndex, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int clientW, int clientH, bool bDrawFullBG)
{
	assert( nIndex >= 0 && nIndex < GetClientComponentCount());

	CListDataModule::DrawRowBackGround( nIndex, draw, l, t, cx, cy, cw, ch, bDrawFullBG);

	INLINECLASS::ListDataImageParam& ldip = m_pList->at(nIndex); 
	switch(cct)
	{
		case CCT_NORMAL:
			ldip.ip.color = GXWINDOW::MakeColor( 255, 255, 255);
			break;
		case CCT_MOUSEON:
			ldip.ip.color = m_MouseOnColor;
			break;
		case CCT_SELECT:
			ldip.ip.color = m_SelectColor;
			break;
	}
	draw.DrawPicture(ldip.ip, l + cx, t + cy, cw, ch, cx, cy, cw, ch);
}

void CImageListDataModlue::OnComponentShow()
{
	for ( INLINECLASS::CListDataImageList::iterator it = m_pList->begin(); it != m_pList->end(); ++ it)
	{
		CImageTextureManager::GetInstance().InsertImage( (*it).ip.nBitmap);
	}
	CListDataModule::OnComponentShow();
}

void CImageListDataModlue::OnComponentHide()
{
	for ( INLINECLASS::CListDataImageList::iterator it = m_pList->begin(); it != m_pList->end(); ++ it)
	{
		CImageTextureManager::GetInstance().RemoveImage( (*it).ip.nBitmap);
	}
	CListDataModule::OnComponentHide();
}

int CImageListDataModlue::GetClientComponentCount() const
{
	return (int)m_pList->size();
}

void CImageListDataModlue::InsertItem( ImageParams& ip,  unsigned int uiParam )
{
	INLINECLASS::ListDataImageParam ldip;
	ldip.ip.nBitmap = ip.nBitmap;
	ldip.ip.x		= ip.x;
	ldip.ip.y		= ip.y;
	ldip.ip.color	= ip.color;
	ldip.uiParam	= uiParam;

	m_pList->push_back(ldip);
}

void CImageListDataModlue::RemoveItem( int nIndex )
{
	assert(nIndex < 0 || nIndex > (int)m_pList->size());
	m_pList->erase(m_pList->begin() + nIndex);
}

void CImageListDataModlue::RemoveAll()
{
	m_pList->clear();
}

int CImageListDataModlue::GetParam( int nIndex ) const
{
	assert(nIndex < 0 || nIndex > (int)m_pList->size());
	INLINECLASS::ListDataImageParam& ldip = m_pList->at(nIndex); 
	return ldip.uiParam;
}

void CImageListDataModlue::SetImageSelectedColor( COLOR clr)
{
	m_SelectColor = clr;
}

void CImageListDataModlue::SetImageMouseOnColor( COLOR clr)
{
	m_MouseOnColor = clr;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


CTextListDataModule::CTextListDataModule()
: m_tdm( &CTextDrawManager::GetInstance()), m_plist( new INLINECLASS::CListDataStringList()), m_clrRowBGMouseOn( 0), m_clrRowBGSelected( 0), m_taFlag( TXA_MIDDLE)
{
}

CTextListDataModule::~CTextListDataModule()
{
	for( INLINECLASS::CListDataStringList::iterator it = m_plist->begin(); it != m_plist->end(); ++ it)
	{
		INLINECLASS::ListDataStringTextParam& tp = *it;
		tp.ReleaeTextDraw( *m_tdm);
	}
	delete m_plist;
}

void CTextListDataModule::DrawClientComponent( int nIndex, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int clientW, int clientH, bool bDrawFullBG)
{
	assert( nIndex >= 0 && nIndex < GetClientComponentCount());

	CListDataModule::DrawRowBackGround( nIndex, draw, l, t, cx, cy, cw, ch, bDrawFullBG);

	INLINECLASS::ListDataStringTextParam& tp = m_plist->at( nIndex);
	int n = -1;
	CListBox* parent = GetParent();
	COLOR bgclr = 0; 
	CImagesFill* pHighlightImg = NULL;

	switch( cct)
	{
	case CCT_NORMAL:
		if( -1 == tp.n)
			tp.n = m_tdm->NewTextDraw( tp.str.c_str(), m_nFont, m_clrNormal, m_clrNormalGradual, 0, m_uNormalEffect, m_clrNormalEffect);
		n = tp.n;
		break;
	case CCT_MOUSEON:
		if( -1 == tp.m)
			tp.m = m_tdm->NewTextDraw( tp.str.c_str(), m_nFont, m_clrMouseOn, m_clrMouseOnGradual, 0, m_uMouseOnEffect, m_clrMouseOnEffect);
		n = tp.m;
		pHighlightImg = & parent->GetMouseOnHighLightImage();
		if ( !pHighlightImg->IsValid())
		{
			bgclr = m_clrRowBGMouseOn;
			pHighlightImg = NULL;
		}
		break;
	case CCT_SELECT:
		if( -1 == tp.s)
			tp.s = m_tdm->NewTextDraw( tp.str.c_str(), m_nFont, m_clrSelected, m_clrSelectedGradual, 0, m_uSelectedEffect, m_clrSelectedEffect);
		n = tp.s;
		pHighlightImg = & parent->GetSelectedHightLightImage();
		if ( !pHighlightImg->IsValid())
		{
			bgclr = m_clrRowBGSelected;
			pHighlightImg = NULL;
		}
		break;
	default:
		assert( false);
		break;
	}
	if ( pHighlightImg)
		pHighlightImg->Draw( draw, l, t, cx, cy, cw, ch);
	else if ( 0 != GetColorAlpha( bgclr))
		draw.FillRect( l + cx, t + cy, l + cx + cw, t + cy + ch, bgclr);
	if( -1 != n)
	{
		int offset_x = 0;
		int offset_y = 0;

		if ( m_taFlag & TXA_CENTER)
			offset_x = max( 0, ( clientW - m_tdm->GetStringPixelWidth( n, m_nFont)) >> 1);
		else if ( m_taFlag & TXA_RIGHT)
			offset_x = max( 0, clientW - m_tdm->GetStringPixelWidth( n, m_nFont));

		if ( m_taFlag & TXA_MIDDLE)
			offset_y = max( 0, ( clientH - m_tdm->GetFontHeightWithoutUnderline( m_nFont)) >> 1);
		else if ( m_taFlag & TXA_BOTTOM)
			offset_y = max( 0, clientH - m_tdm->GetFontHeight( m_nFont));

		m_tdm->Draw( n, l + offset_x, t + offset_y, max( 0, cx - offset_x), max( 0, cy - offset_y), min( cw, cw + cx - offset_x), min( ch, ch + cy - offset_y));
		//m_tdm->Draw( n, l + 2, t, cx, cy, cw, ch);
	}
}

int CTextListDataModule::GetClientComponentCount() const
{
	return GetTextCount();
}

void CTextListDataModule::InsertText( const wchar_t* szText, unsigned int uiParam, unsigned __int64 ulParam)
{
	INLINECLASS::ListDataStringTextParam	tp;

	tp.param = uiParam;
	tp.lparam = ulParam;
	tp.n = -1;
	tp.s = -1;
	tp.m = -1;
	if( szText)
		tp.str = szText;
	m_plist->push_back( tp);
}

void CTextListDataModule::SetParam( int nIndex, unsigned int uiParam)
{
	if( nIndex < 0 || nIndex >= (int)m_plist->size())
		return;
	INLINECLASS::ListDataStringTextParam& tp = m_plist->at( nIndex);
	tp.param = uiParam;
}

unsigned int CTextListDataModule::GetParam( int nIndex) const
{
	if( nIndex < 0 || nIndex >= (int)m_plist->size())
		return -1;
	const INLINECLASS::ListDataStringTextParam& tp = m_plist->at( nIndex);
	return tp.param;
}

void CTextListDataModule::SetLongParam( int nIndex, unsigned __int64 ulParam)
{
	if( nIndex < 0 || nIndex >= (int)m_plist->size())
		return;
	INLINECLASS::ListDataStringTextParam& tp = m_plist->at( nIndex);
	tp.lparam = ulParam;
}

unsigned __int64 CTextListDataModule::GetLongParam( int nIndex) const
{
	if( nIndex < 0 || nIndex >= (int)m_plist->size())
		return -1;
	const INLINECLASS::ListDataStringTextParam& tp = m_plist->at( nIndex);
	return tp.lparam;
}

const wchar_t* CTextListDataModule::GetText( int nIndex) const
{
	if( nIndex < 0 || nIndex >= (int)m_plist->size())
		return NULL;
	const INLINECLASS::ListDataStringTextParam& tp = m_plist->at( nIndex);
	return tp.str.c_str();
}

void CTextListDataModule::SetText( int nIndex, const wchar_t* szText)
{
	if( nIndex < 0 || nIndex >= (int)m_plist->size())
		return;
	INLINECLASS::ListDataStringTextParam& tp = m_plist->at( nIndex);
	if( NULL == szText)
		tp.str.erase();
	else
		tp.str = szText;

	tp.ReleaeTextDraw( *m_tdm);
}

void CTextListDataModule::RemoveText( int nIndex)
{
	if( nIndex < 0 || nIndex >= (int)m_plist->size())
		return;

	INLINECLASS::ListDataStringTextParam& tp = m_plist->at( nIndex);
	tp.ReleaeTextDraw( *m_tdm);
	m_plist->erase( m_plist->begin() + nIndex);
}

void CTextListDataModule::RemoveAll()
{
	for( INLINECLASS::CListDataStringList::iterator it = m_plist->begin(); it != m_plist->end(); ++ it)
	{
		INLINECLASS::ListDataStringTextParam& tp = *it;
		tp.ReleaeTextDraw( *m_tdm);
	}
	m_plist->clear();
}

void CTextListDataModule::SetTextColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrNormal != clr || m_clrNormalEffect != clrEffect || m_uMouseOnEffect != type || ( bGradualColor && ( clrGradual != m_clrNormalGradual)))
	{
		for( INLINECLASS::CListDataStringList::iterator it = m_plist->begin(); it != m_plist->end(); ++ it)
		{
			INLINECLASS::ListDataStringTextParam& tp = *it;
			if( -1 != tp.n)
			{
				m_tdm->FreeTextDraw( tp.n);
				tp.n = -1;
			}
		} // for( INLINECLASS::CListDataStringList::iterator it = m_plist->begin(); it != m_plist->end(); ++ it)
	} // if( m_clrNormal != clr)
	CSimpleTextable::SetTextColor( clr, type, clrEffect, bGradualColor, clrGradual);

}

void CTextListDataModule::SetTextSelectedColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrSelected != clr || m_clrSelectedEffect != clr || m_uSelectedEffect != type || ( bGradualColor && ( clrGradual != m_clrSelectedGradual)))
	{
		for( INLINECLASS::CListDataStringList::iterator it = m_plist->begin(); it != m_plist->end(); ++ it)
		{
			INLINECLASS::ListDataStringTextParam& tp = *it;
			if( -1 != tp.s)
			{
				m_tdm->FreeTextDraw( tp.s);
				tp.s = -1;
			}
		} // for( INLINECLASS::CListDataStringList::iterator it = m_plist->begin(); it != m_plist->end(); ++ it)
	} // if( m_clrSelected != clr)
	CSimpleTextable::SetTextSelectedColor( clr, type, clrEffect, bGradualColor, clrGradual);
}

void CTextListDataModule::SetTextMouseOnColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrMouseOn != clr || m_clrMouseOnEffect != clrEffect || m_uMouseOnEffect == type || ( bGradualColor && ( clrGradual != m_clrMouseOnGradual)))
	{
		for( INLINECLASS::CListDataStringList::iterator it = m_plist->begin(); it != m_plist->end(); ++ it)
		{
			INLINECLASS::ListDataStringTextParam& tp = *it;
			if( -1 != tp.m)
			{
				m_tdm->FreeTextDraw( tp.m);
				tp.m = -1;
			}
		} // for( INLINECLASS::CListDataStringList::iterator it = m_plist->begin(); it != m_plist->end(); ++ it)
	}
	CSimpleTextable::SetTextMouseOnColor( clr, type, clrEffect, bGradualColor, clrGradual);
}

void CTextListDataModule::SetTextFont( int font)
{
	if( m_nFont != font)
	{
		for( INLINECLASS::CListDataStringList::iterator it = m_plist->begin(); it != m_plist->end(); ++ it)
		{
			INLINECLASS::ListDataStringTextParam& tp = *it;
			tp.ReleaeTextDraw( *m_tdm);
		}
	}
	m_nFont = font;
}

COLOR CTextListDataModule::GetTextColor() const
{
	return m_clrNormal;
}

COLOR CTextListDataModule::GetTextSelectedColor() const
{
	return m_clrSelected;
}

COLOR CTextListDataModule::GetTextMouseOnColor() const
{
	return m_clrMouseOn;
}

int CTextListDataModule::GetTextFont() const
{
	return m_nFont;
}

void CTextListDataModule::SetTextColor( int nIndex, COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	INLINECLASS::ListDataStringTextParam& tp = m_plist->at( nIndex);
	if ( tp.n != -1)
		m_tdm->FreeTextDraw( tp.n);
	tp.n = m_tdm->NewTextDraw( tp.str.c_str(), m_nFont, clr, bGradualColor ? clrGradual : clr, 0, type, clrEffect);
}

void CTextListDataModule::SetTextSelectedColor( int nIndex, COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	INLINECLASS::ListDataStringTextParam& tp = m_plist->at( nIndex);
	if ( tp.s != -1)
		m_tdm->FreeTextDraw( tp.s);
	tp.s = m_tdm->NewTextDraw( tp.str.c_str(), m_nFont, clr, bGradualColor ? clrGradual : clr, 0, type, clrEffect);
}

void CTextListDataModule::SetTextMouseOnColor( int nIndex, COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	INLINECLASS::ListDataStringTextParam& tp = m_plist->at( nIndex);
	if ( tp.m != -1)
		m_tdm->FreeTextDraw( tp.m);
	tp.m = m_tdm->NewTextDraw( tp.str.c_str(), m_nFont, clr, bGradualColor ? clrGradual : clr, 0, type, clrEffect);
}

int CTextListDataModule::GetTextCount() const
{
	return (int)m_plist->size();
}

void CTextListDataModule::FreeAllTextDraw()
{
	for( INLINECLASS::CListDataStringList::iterator it = m_plist->begin(); it != m_plist->end(); ++ it)
	{
		INLINECLASS::ListDataStringTextParam& tp = *it;
		tp.ReleaeTextDraw( *m_tdm);
	}
}

void CTextListDataModule::SetBackGroundMouseOnColor( COLOR clr)
{
	m_clrRowBGMouseOn = clr;
}

void CTextListDataModule::SetBackGroundSelectedColor( COLOR clr)
{
	m_clrRowBGSelected = clr;
}

void CTextListDataModule::SetTextAlign( TEXTALIGNFLAG flag)
{
	m_taFlag = flag;
}

TEXTALIGNFLAG CTextListDataModule::GetTextAlign() const
{
	return m_taFlag;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CListBoxSelectChangeNotify::CListBoxSelectChangeNotify()
{
}

CListBoxSelectChangeNotify::~CListBoxSelectChangeNotify()
{
}

CListBoxMouseOnChangeNotify::CListBoxMouseOnChangeNotify()
{
}

CListBoxMouseOnChangeNotify::~CListBoxMouseOnChangeNotify()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CListBox::CListBox()
	: m_pDataModule( NULL), m_nClientHeight( 20), m_nCurrentSelected( -1), m_sis( NULL), m_scns( new INLINECLASS::CListBoxSelectChangeNotifySet()),
	m_nMouseOnIndex( -1), m_mons( new INLINECLASS::CListBoxMouseOnChangeNotifySet()), m_bMouseRightDownSendNotify( true)
{
}

CListBox::~CListBox()
{
	if ( m_pDataModule)
		ResetListDataModule( m_pDataModule);

	if( m_sis)
		delete m_sis;
	delete m_scns;
	delete m_mons;
}

void CListBox::SetListDataModule( CListDataModule& ldm)
{
	if( m_pDataModule)
	{
		m_pDataModule->ListDataModuleDataChangeNotifyRemove( *this);
		m_pDataModule->m_pParent = NULL;
	}
	m_pDataModule = &ldm;
	if( m_pDataModule)
	{
		m_pDataModule->ListDataModuleDataChangeNotifyAdd( *this);
		m_pDataModule->m_pParent = this;
	}
}

CListDataModule& CListBox::GetListDataModule()
{
	return *m_pDataModule;
}

void CListBox::ListBoxSelectChangeNotifyAdd( CListBoxSelectChangeNotify& scn)
{
	m_scns->Insert( &scn);
}

void CListBox::ListBoxSelectChangeNotifyRemove( CListBoxSelectChangeNotify& scn)
{
	m_scns->Remove( &scn);
}

void CListBox::ListBoxMouseOnChangeNotifyAdd( CListBoxMouseOnChangeNotify& mcn)
{
	m_mons->Insert( &mcn);
}

void CListBox::ListBoxMouseOnChangeNotifyRemove( CListBoxMouseOnChangeNotify& mcn)
{
	m_mons->Remove( &mcn);
}

void CListBox::SetClientComponentHeight( int n)
{
	m_nClientHeight = n;
	INVALIDATE();
}

int CListBox::GetClientComponentHeight() const
{
	return m_nClientHeight;
}

bool CListBox::OnMouseMove( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	int old = m_nMouseOnIndex;
	m_nMouseOnIndex = GetIndexByPoint( x, y);
	if( old != m_nMouseOnIndex)
		m_mons->NotifyAll( *this, &CListBoxMouseOnChangeNotify::OnListBoxMouseOnChange, m_nMouseOnIndex, old);
	INVALIDATE();
	if ( m_pDataModule && m_pDataModule->OnMouseMove( m_nMouseOnIndex, x, y, key))
		return true;
	return CBKComponent::OnMouseMove( x, y, key);
}

bool CListBox::OnMouseWheel( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	m_nMouseOnIndex = GetIndexByPoint( x, y);
	if ( m_pDataModule && m_pDataModule->OnMouseWheel( m_nMouseOnIndex, x, y, key))
		return true;
	return CBKComponent::OnMouseWheel( x, y, key);
}

void CListBox::OnMouseLeave()
{
	if ( IsViewOnly())
		return;
	int old = m_nMouseOnIndex;
	m_nMouseOnIndex = -1;
	if( old != m_nMouseOnIndex)
		m_mons->NotifyAll( *this, &CListBoxMouseOnChangeNotify::OnListBoxMouseOnChange, m_nMouseOnIndex, old);
	CBKComponent::OnMouseLeave();
}

void CListBox::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CBKComponent::Draw( draw, l, t, cx, cy, cw, ch);

	if( NULL == m_pDataModule || 0 == m_nClientHeight)
		return;

	int nCount = m_pDataModule->GetClientComponentCount();
	int nComWidth = GetAbsWidth();

	int nStart = cy / m_nClientHeight;
	if( nStart >= nCount)
		return;
	int nStop = min( (cy + ch) / m_nClientHeight, nCount - 1);
	//SetSize( nComWidth, nCount * m_nClientHeight);

	struct InlineFunc
	{
		static inline CListDataModule::CCTYPE GetCCType( bool bSelect, bool bMouseOn)
		{
			if( bSelect)
				return CListDataModule::CCT_SELECT;
			else if( bMouseOn)
				return CListDataModule::CCT_MOUSEON;
			else
				return CListDataModule::CCT_NORMAL;
		}
	};

	int nt = nStart * m_nClientHeight;
	m_pDataModule->DrawClientComponent( nStart, InlineFunc::GetCCType( IsSelected( nStart), nStart == m_nMouseOnIndex), draw, 
										l, t + nt, cx, cy - nt, cw, m_nClientHeight + nt - cy, nComWidth, m_nClientHeight, true);

	for( int i = nStart + 1; i < nStop; i ++)
	{
		nt += m_nClientHeight;
		m_pDataModule->DrawClientComponent( i, InlineFunc::GetCCType( IsSelected( i), i == m_nMouseOnIndex), draw, l, t + nt, cx, 0, cw, m_nClientHeight, nComWidth, m_nClientHeight);
	}

	{
		nt += m_nClientHeight;
		int nch = cy + ch - nt;
		if( nch > 0)
			m_pDataModule->DrawClientComponent( nStop, InlineFunc::GetCCType( IsSelected( nStop), nStop == m_nMouseOnIndex), draw, l, t + nt, cx, 0, cw, nch, nComWidth, m_nClientHeight);
	}
}

void CListBox::OnListDataModuleDataChange( CListDataModule& ldm)
{
	m_nCurrentSelected = -1;
	if( m_sis)
		m_sis->clear();
	SetSize( GetAbsWidth(), m_pDataModule->GetClientComponentCount() * m_nClientHeight);
}

void CListBox::OnComponentShow()
{
	if ( m_pDataModule)
		m_pDataModule->OnComponentShow();
	CBKComponent::OnComponentShow();
}

void CListBox::ResetListDataModule( CListDataModule* ldm)
{
	if ( ldm && m_pDataModule == ldm)
	{
		m_pDataModule->m_pParent = NULL;
		m_pDataModule->ListDataModuleDataChangeNotifyRemove( *this);
		m_pDataModule = NULL;
	}
}

void CListBox::OnComponentHide()
{
	if ( m_pDataModule)
		m_pDataModule->OnComponentHide();
	CBKComponent::OnComponentHide();
}

int CListBox::GetSelectIndex() const
{
	return m_nCurrentSelected;
}

void CListBox::SetSelectIndex( int nIndex)
{
	int nCount = m_pDataModule->GetClientComponentCount();
	if( nIndex < 0 || nIndex >= nCount)
		m_nCurrentSelected = -1;
	else
		m_nCurrentSelected = nIndex;

	if( -1 != m_nCurrentSelected)
		m_scns->NotifyAll( *this, &CListBoxSelectChangeNotify::OnListBoxSelectChange, m_nCurrentSelected);
	INVALIDATE();
}

void CListBox::SetSelect( int nIndex, bool bSelect)
{
	if( NULL == m_sis)
		return;
	int nCount = m_pDataModule->GetClientComponentCount();
	if( nIndex < 0 || nIndex >= nCount)
		return;

	if( bSelect)
		m_sis->insert( nIndex);
	else
		m_sis->erase( nIndex);
	INVALIDATE();
}

bool CListBox::IsSelected( int nIndex) const
{
	if( NULL == m_sis)
		return m_nCurrentSelected == nIndex;
	else
		return m_sis->find( nIndex) != m_sis->end();
}

int CListBox::GetIndexByPoint( int x, int y)
{
	int n = ( y - GetAbsPosY() - 1) / m_nClientHeight;
	int nCount = m_pDataModule->GetClientComponentCount();
	return ( n >= 0 && n < nCount) ? n : -1;
}

bool CListBox::OnLeftButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	int n = GetIndexByPoint( x, y);
	INVALIDATE();
	if ( m_pDataModule && m_pDataModule->OnLeftButtonDown( n, x, y, key))
		return true;
	return CBKComponent::OnLeftButtonDown( x, y, key);
}

bool CListBox::OnLeftButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	int n = GetIndexByPoint( x, y);
	if( -1 != n)
	{
		if( NULL == m_sis)
		{
			SetSelectIndex( n);
		}
		else
		{
			INLINECLASS::CListSelectIndexSet::iterator it = m_sis->find( n);
			if(  it == m_sis->end())
				m_sis->insert( n);
			else
				m_sis->erase( it);
		}
		INVALIDATE();
	} // if( -1 != n)

	if ( m_pDataModule && m_pDataModule->OnLeftButtonUp( GetIndexByPoint( x, y), x, y, key))
		return true;
	return CBKComponent::OnLeftButtonUp( x, y, key);
}

//bool CListBox::OnLeftButtonDoubleClick( int x, int y, int key)
//{
//	if ( IsViewOnly())
//		return false;
//
//	if ( m_pDataModule && m_pDataModule->OnLeftButtonDoubleClick( GetIndexByPoint( x, y), x, y, key))
//		return true;
//	return CBKComponent::OnLeftButtonDoubleClick( x, y, key);
//}

bool CListBox::OnRightButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	int n = GetIndexByPoint( x, y);
	if( -1 != n)
	{
		if( NULL == m_sis)
		{
			if ( m_bMouseRightDownSendNotify)
				SetSelectIndex( n);
			else
			{
				// 更改选中项但不触发通告
				int nCount = m_pDataModule->GetClientComponentCount();
				if( n < 0 || n >= nCount)
					m_nCurrentSelected = -1;
				else
					m_nCurrentSelected = n;
			}
		}
		else
		{
			INLINECLASS::CListSelectIndexSet::iterator it = m_sis->find( n);
			if(  it == m_sis->end())
				m_sis->insert( n);
			else
				m_sis->erase( it);
		}
	} // if( -1 != n)
	if ( m_pDataModule && m_pDataModule->OnRightButtonDown( n, x, y, key))
		return true;
	return CBKComponent::OnRightButtonDown( x, y, key);
}

bool CListBox::OnRightButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if ( m_pDataModule && m_pDataModule->OnRightButtonUp( GetIndexByPoint( x, y), x, y, key))
		return true;
	return CBKComponent::OnRightButtonUp( x, y, key);	
}

//bool CListBox::OnRightButtonDoubleClick( int x, int y, int key)
//{
//	if ( IsViewOnly())
//		return false;
//
//	if ( m_pDataModule && m_pDataModule->OnRightButtonDoubleClick( GetIndexByPoint( x, y), x, y, key))
//		return true;
//	return CBKComponent::OnRightButtonDoubleClick( x, y, key);	
//}

bool CListBox::IsMultiSelect() const
{
	return NULL != m_sis;
}

void CListBox::SetMultiSelect( bool b)
{
	if( b)
	{
		if( NULL == m_sis)
			m_sis = new INLINECLASS::CListSelectIndexSet();
	}
	else
	{
		if( m_sis)
		{
			delete m_sis;
			m_sis = NULL;
 		}
	}
}

int CListBox::GetHoverIndex() const
{
	return m_nMouseOnIndex;
}

void CListBox::SetRightButtonDownSendNotify( bool b)
{
	m_bMouseRightDownSendNotify = b;
}

bool CListBox::IsRightButtonDownSendNotify() const
{
	return m_bMouseRightDownSendNotify;
}

CImagesFill& CListBox::GetSelectedHightLightImage()
{
	return m_SelectedHighlightIamge;
}

CImagesFill& CListBox::GetMouseOnHighLightImage()
{
	return m_MouseOnHighlightImage;
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCombinationListBoxAllocator::CCombinationListBoxAllocator()
{
}

CCombinationListBoxAllocator::~CCombinationListBoxAllocator()
{
}

CScrollPanel* CCombinationListBoxAllocator::NewScrollPanel()
{
	return new CScrollPanel();
}

CListBox* CCombinationListBoxAllocator::NewListBox()
{
	return new CListBox();
}

void CCombinationListBoxAllocator::DeleteComponent( CComponent* pc)
{
	delete pc;
}

CCombinationListBoxAllocator& CCombinationListBoxAllocator::GetInstance()
{
	return INLINECLASS::g_DefaultCombinationListBoxAllocator;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CCombinationListBox::CCombinationListBox( CCombinationListBoxAllocator& alloc)
	: m_alloc( &alloc)
{
	m_sp = m_alloc->NewScrollPanel();
	m_lb = m_alloc->NewListBox();

	m_sp->SetComponent( *m_lb);
}

CCombinationListBox::~CCombinationListBox()
{
	m_alloc->DeleteComponent( m_sp);
	m_alloc->DeleteComponent( m_lb	);
}

CScrollPanel& CCombinationListBox::GetScrollPanel()
{
	return *m_sp;
}

CListBox& CCombinationListBox::GetListBox()
{
	return *m_lb;
}

void CCombinationListBox::SetSize( int w, int h, int lw)
{
	m_sp->SetSize( w, h);
	m_lb->SetSize( lw, h);
}

void CCombinationListBox::SetPos( int x, int y)
{
	m_sp->SetPos( x, y);
}

void CCombinationListBox::SetListDataModule( CListDataModule& ldm)
{
	m_lb->SetListDataModule( ldm);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CComboBoxSelectChangeNotify::CComboBoxSelectChangeNotify()
{
}

CComboBoxSelectChangeNotify::~CComboBoxSelectChangeNotify()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CComboBoxComponentAllocator::CComboBoxComponentAllocator()
{
}

CComboBoxComponentAllocator::~CComboBoxComponentAllocator()
{
}

CScrollPanel* CComboBoxComponentAllocator::NewListScrollPanel()
{
	return new CScrollPanel();
}

void CComboBoxComponentAllocator::DeleteListScrollPanel( CScrollPanel* sp)
{
	delete sp;
}

CImageButton* CComboBoxComponentAllocator::NewBoxImageButton()
{
	return new CImageButton();
}

void CComboBoxComponentAllocator::DeleteImageButton( CImageButton* ib)
{
	delete ib;
}

CListBox* CComboBoxComponentAllocator::NewListBox()
{
	return new CListBox();
}

void CComboBoxComponentAllocator::DeleteListBox( CListBox* lb)
{
	delete lb;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CComboBox::CComboBox() : m_nBorderWidth( 0), m_bDownDraw( true)
{
	Initialize( INLINECLASS::g_DefaultComboBoxAllocator);
}

CComboBox::CComboBox( CComboBoxComponentAllocator& alloc)
{
	Initialize( alloc);
}

CComboBox::~CComboBox()
{
	m_frame.FrameShowHideNotifyRemove( *this);
	HideListBox();
	delete m_scns;
	m_Alloc->DeleteImageButton( m_btn);
	m_Alloc->DeleteListScrollPanel( m_panel);
	m_Alloc->DeleteListBox( m_list);
}

void CComboBox::Initialize( CComboBoxComponentAllocator& alloc)
{
	m_Alloc = &alloc;

	m_list = m_Alloc->NewListBox();
	m_panel = m_Alloc->NewListScrollPanel();
	m_btn = m_Alloc->NewBoxImageButton();

	m_scns = new INLINECLASS::CComboBoxSelectChangeNotifySet();

	SetButtonWidth( 20);
	m_btn->SetParent( this);
	m_btn->SetPos( 0, 0);

	m_panel->SetComponent( *m_list);
	m_panel->SetPos( m_nBorderWidth, m_nBorderWidth);
	m_frame.InsertComponent( m_panel);

	m_list->ListBoxSelectChangeNotifyAdd( *this);

	m_frame.FrameShowHideNotifyAdd( *this);
}

void CComboBox::SelectChangeNotifyAdd( CComboBoxSelectChangeNotify& scn)
{
	m_scns->Insert( &scn);
}

void CComboBox::SelectChangeNotifyRemove( CComboBoxSelectChangeNotify& scn)
{
	m_scns->Remove( &scn);
}

void CComboBox::SetListDataModule( CListDataModule& ldm)
{
	m_list->SetListDataModule( ldm);
}

CListDataModule& CComboBox::GetListDataModule()
{
	return m_list->GetListDataModule();
}

void CComboBox::SetSelectIndex( int nIndex)
{
	m_list->SetSelectIndex( nIndex);
}

int CComboBox::GetSelectIndex() const
{
	return m_list->GetSelectIndex();
}

void CComboBox::SetButtonWidth( int w)
{
	m_btn->SetSize( w, GetHeight());
}

int CComboBox::GetButtonWidth()
{
	return m_btn->GetWidth();
}

void CComboBox::SetSize( int w, int h)
{
	CBKComponent::SetSize( w, h);
	int nw = m_btn->GetWidth();
	m_btn->SetPos( w - nw, 0);
	m_btn->SetSize( nw, h);
}

void CComboBox::SetBorderWidth(int n)
{
	m_nBorderWidth = n;
	//m_panel->SetBorderWidth( n);
	//m_panel->SetPos( m_nBorderWidth, m_nBorderWidth);
}

void CComboBox::SetListBoxSize( int w, int h)
{
	m_nListWidth = w;
	m_nListHeight = h;
}

int CComboBox::GetListBoxHeight() const
{
	return m_nListHeight;
}

int CComboBox::GetListBoxWidth() const
{
	return m_nListWidth;
}

void CComboBox::SetClientComponentHeight( int n)
{
	m_list->SetClientComponentHeight( n);
}

int CComboBox::GetClientComponentHeight() const
{
	return m_list->GetClientComponentHeight();
}


void CComboBox::SetDownDraw( bool bDown)
{
	m_bDownDraw = bDown;
}

bool CComboBox::IsDownDraw() const
{
	return m_bDownDraw;
}

void CComboBox::OnMouseEnter()
{
	if ( !IsViewOnly() && !m_frame.IsVisible())
	{
		if ( m_btn->IsAbsPointIn( GXWGetMousePointX(), GXWGetMousePointY()))
			m_btn->OnMouseEnter();
		CBKComponent::OnMouseEnter();
	}
}

void CComboBox::OnMouseLeave()
{
	if ( !IsViewOnly() && !m_frame.IsVisible())
	{
		if ( m_btn->IsButtonMouseIn())
			m_btn->OnMouseLeave();
		CBKComponent::OnMouseLeave();
	}
}

bool CComboBox::OnLeftButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if (IsEnable())
	{
		if( m_btn->IsAbsPointIn( x, y))
		{
			if ( !m_btn->IsEnable())
				return CBKComponent::OnLeftButtonDown( x, y, key);
			m_btn->OnLeftButtonDown( x, y, key);
		}
		ShowListBox();
	}
	return CBKComponent::OnLeftButtonDown( x, y, key);
}

bool CComboBox::OnLeftButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if( m_btn->IsAbsPointIn( x, y))
		m_btn->OnLeftButtonUp( x, y, key);
	return CBKComponent::OnLeftButtonUp( x, y, key);
}

bool CComboBox::OnMouseMove( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if( m_btn->IsAbsPointIn( x, y))
	{
		if ( !m_frame.IsVisible() && !m_btn->IsButtonMouseIn())
			m_btn->OnMouseEnter();
		m_btn->OnMouseMove( x, y, key);
	}
	else 
	{
		if ( !m_frame.IsVisible() && m_btn->IsButtonMouseIn())
			m_btn->OnMouseLeave();
	}
	return CBKComponent::OnMouseMove( x, y, key);
}

void CComboBox::ShowListBox()
{
	int		l = GetAbsPosX();
	int		t = GetAbsPosY() + GetHeight();
	int		w = GetWidth();
	int		ah = m_list->m_nClientHeight * m_list->m_pDataModule->GetClientComponentCount();
	int		h = min( ah + ( m_nBorderWidth << 1), m_nListHeight);

	if ( m_bDownDraw)
		m_frame.SetPos( l, t);
	else
		m_frame.SetPos( l, t - h - GetHeight());
	m_frame.SetSize( w, h);
	m_panel->SetPos( m_nBorderWidth, m_nBorderWidth);
	m_panel->SetSize( w - ( m_nBorderWidth << 1), h - ( m_nBorderWidth << 1));
	// m_list->SetSize( max( m_nListWidth, w), ah);
	m_list->SetSize( m_nListWidth, ah);
	m_panel->AdjustScrollInfo();

	int n = m_list->GetSelectIndex();
	if( -1 != n)
		m_panel->GetVerticalScrollBar()->SetValuePos( m_list->m_nClientHeight * n);

	CDesktop& desktop = CDesktop::GetDefault();
	desktop.SetPopupFrame( &m_frame);
}

void CComboBox::HideListBox()
{
	CDesktop& desktop = CDesktop::GetDefault();
	desktop.SetPopupFrame( NULL);
	m_frame.Hide();
}

void CComboBox::OnFrameShowHide( CFrame& c, bool show)
{
	if ( m_frame.IsSameComponent( c))
	{
		if ( !show)
		{
			CDesktop& desktop = CDesktop::GetDefault();
			desktop.SetPopupFrame( NULL);
		}
	}
}

void CComboBox::OnListBoxSelectChange( CListBox& lb, int n)
{
	if( lb.IsSameComponent( m_list))
	{
		HideListBox();
		m_scns->NotifyAll( *this, &CComboBoxSelectChangeNotify::OnComboBoxSelectChange, n);
	}
}

void CComboBox::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CBKComponent::Draw( draw, l, t, cx, cy, cw, ch);

	int w = GetWidth() - m_btn->GetWidth() - m_nBorderWidth;
	int nw = cx + cw;
	if( nw > w)
	{
		if ( m_frame.IsVisible())
			m_btn->DrawButton( draw, l + w + m_nBorderWidth, t + m_nBorderWidth, 0, cy, nw - w - ( m_nBorderWidth << 1), ch - ( m_nBorderWidth << 1), true, true);
		else
			m_btn->Draw( draw, l + w + m_nBorderWidth, t + m_nBorderWidth, 0, cy, nw - w - ( m_nBorderWidth << 1), ch - ( m_nBorderWidth << 1));
		cw = w - cx;
	}

	int nIndex = m_list->GetSelectIndex();
	if( -1 == nIndex)
		return;
	CListDataModule& ldm = m_list->GetListDataModule();
	ldm.DrawClientComponent( nIndex, CListDataModule::CCT_NORMAL, draw, l + m_nBorderWidth, t + m_nBorderWidth, cx, cy, cw - m_nBorderWidth, ch - ( m_nBorderWidth << 1), GetWidth(), GetClientComponentHeight());
}

void CComboBox::OnComponentShow()
{
	m_list->OnComponentShow();
	m_frame.OnComponentShow();
	m_panel->OnComponentShow();
	m_btn->OnComponentShow();
	CBKComponent::OnComponentShow();
}

void CComboBox::OnComponentHide()
{
	HideListBox();
	m_list->OnComponentHide();
	m_frame.OnComponentHide();
	m_panel->OnComponentHide();
	m_btn->OnComponentHide();
	CBKComponent::OnComponentHide();
}

//bool CComboBox::OnLeftButtonDoubleClick( int x, int y, int key)
//{
//	if( m_btn->IsAbsPointIn( x, y))
//		m_btn->OnLeftButtonDoubleClick( x, y, key);
//	return CBKComponent::OnLeftButtonDoubleClick( x, y, key);
//}

bool CComboBox::OnRightButtonDown( int x, int y, int key)
{
	if( m_btn->IsAbsPointIn( x, y))
		m_btn->OnRightButtonDown( x, y, key);
	return CBKComponent::OnRightButtonDown( x, y, key);
}

bool CComboBox::OnRightButtonUp( int x, int y, int key)
{
	if( m_btn->IsAbsPointIn( x, y))
		m_btn->OnRightButtonUp( x, y, key);
	return CBKComponent::OnRightButtonUp( x, y, key);
}

//bool CComboBox::OnRightButtonDoubleClick( int x, int y, int key)
//{
//	if( m_btn->IsAbsPointIn( x, y))
//		m_btn->OnRightButtonDoubleClick( x, y, key);
//	return CBKComponent::OnRightButtonDoubleClick( x, y, key);
//}

bool CComboBox::OnMouseWheel( int x, int y, int key)
{
	if( m_btn->IsAbsPointIn( x, y))
		m_btn->OnMouseWheel( x, y, key);
	return CBKComponent::OnMouseWheel( x, y, key);
}
CImageButton* CComboBox::GetButton() const
{
	return m_btn;
}

CScrollPanel* CComboBox::GetScrollPanel() const
{
	return m_panel;
}
void CComboBox::SetEnable( bool bEnable)
{
	m_btn->SetEnable( bEnable);
	CBKComponent::SetEnable( bEnable);
}

CImagesFill& CComboBox::GetDrawListBackGround()
{
	m_frame.SetBackGroundStyle( BGS_IMAGE);
	return m_frame.GetTilesBackGround();
}

void CComboBox::SetDrawListBackGroundColor( GXWINDOW::COLOR clr)
{
	m_frame.SetBackGroundStyle( BGS_COLOR);
	m_frame.SetBackGroundStyleColor( clr);
}

void CComboBox::SetBackGroundStyle( BackGroundStyle bgs)
{
	CBKComponent::SetBackGroundStyle( bgs);
	m_frame.SetBackGroundStyle( bgs);
}

CImagesFill& CComboBox::GetSelectedHightLightImage()
{
	return m_list->GetSelectedHightLightImage();
}

CImagesFill& CComboBox::GetMouseOnHighLightImage()
{
	return m_list->GetMouseOnHighLightImage();
}

} // namespace GXWINDOW { 