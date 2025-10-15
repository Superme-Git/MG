#include "gxwindowdef.h"

namespace GXWINDOW { 

namespace INLINECLASS { 

CTabComponentAllocator	g_DefaultTabControlAlloctor;

struct CTabControlItem
{
	CAbstractRadioButton*		btn;
	CComponent*					panel;
	bool						visible;
	CTabControlItem() : visible( true) {}
};

class CTabControlItemVector : public std::vector<CTabControlItem>
{
public:
	CTabControlItemVector() {}
	virtual ~CTabControlItemVector() {}
};

template<typename F>
	bool DispatchTabControlMouseMessage( F f, CPanel& panel, CComponent& com, int x, int y, int key)
{
	if( panel.IsAbsPointIn( x, y))
		return (panel.*f)( x, y, key);
	else
		return (com.*f)( x, y, key);
}

class CTabChangedNotifySet : public CNotifySet<CTabChangedNotify*>
{
public:
	CTabChangedNotifySet() {}
	~CTabChangedNotifySet() {}
};

static int	g_DefaultTabClickAudio = -1;

} // namespace INLINECLASS {
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTabComponentAllocator::CTabComponentAllocator()
{
}

CTabComponentAllocator::~CTabComponentAllocator()
{
}

CAbstractRadioButton* CTabComponentAllocator::NewRadioButton()
{
	//return new CImageRadioButton();
	return new CTextImageRadioButton();
}

void CTabComponentAllocator::DeleteRadioButton( CAbstractRadioButton* pbtn)
{
	delete pbtn;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTabChangedNotify::CTabChangedNotify() {}
CTabChangedNotify::~CTabChangedNotify() {}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void CTabControl::SetDefaultTabClickAudio( int n)
{
	INLINECLASS::g_DefaultTabClickAudio = n;
}

int CTabControl::GetDefaultTabClickAudio()
{
	return INLINECLASS::g_DefaultTabClickAudio;
}

CTabControl::CTabControl( CTabComponentAllocator& allocator)
{
	Init( allocator);
}

CTabControl::CTabControl()
{
	Init( INLINECLASS::g_DefaultTabControlAlloctor);
}

CTabControl::~CTabControl()
{
	for( INLINECLASS::CTabControlItemVector::iterator it = m_piv->begin(); it != m_piv->end(); ++ it)
	{
		INLINECLASS::CTabControlItem& tci = *it;
		m_pAlloctor->DeleteRadioButton( tci.btn);
	} // for( INLINECLASS::CTabControlItemVector::iterator it = m_piv->begin(); it != m_piv->end(); ++ it)
	delete m_piv;
	delete m_tcns;
}

void CTabControl::Init( CTabComponentAllocator& allocator)
{
	m_pLastMouseIn = NULL;
	m_pAlloctor = &allocator;
	m_piv = new INLINECLASS::CTabControlItemVector();
	m_tcns = new INLINECLASS::CTabChangedNotifySet();
	m_bVertical = false;
	m_nBtnWidth =  60;
	m_nBtnHeight = 20;
	m_nSelected = -1;
	m_TitlePanel.SetParent( this);
	m_TitlePanel.SetPos( 0, 0);
	SetTitleSize( 30);

	m_bRightBottom = false;
	m_nTitleOffset = 0;

	m_clrBorder = 0;
	m_bUseDefaultAudio = true;
	m_nClickAudio = -1;
}

void CTabControl::SetTabClickAudio( int n)
{
	m_bUseDefaultAudio = false;
	m_nClickAudio = n;
	
	n = GetCurrentTabClickAudio();
	for( INLINECLASS::CTabControlItemVector::iterator it = m_piv->begin(), ite = m_piv->end(); it != ite; ++ it)
	{
		INLINECLASS::CTabControlItem& tci = *it;
		tci.btn->SetClickAudio( n);
	}
}

int CTabControl::GetCurrentTabClickAudio() const
{
	if( m_bUseDefaultAudio)
		return INLINECLASS::g_DefaultTabClickAudio;
	else
		return m_nClickAudio;
}

int CTabControl::GetTabClickAudio() const
{
	return m_nClickAudio;
}

void CTabControl::TabChangedNotifyAdd( CTabChangedNotify& tcn)
{
	m_tcns->Insert( &tcn);
}

void CTabControl::TabChangedNotifyRemove( CTabChangedNotify& tcn)
{
	m_tcns->Remove( &tcn);
}

CTabComponentAllocator& CTabControl::GetAllocator()
{
	return *m_pAlloctor;
}

void CTabControl::SetTitleSize( int n)
{
	m_nTitleSize = n;
	CheckTitlePanelPos();
	CheckTitleButtonSize();
	CheckClientComponentSize();
}

int CTabControl::GetTitleSize() const
{
	return m_nTitleSize;
}

void CTabControl::SetButtonWidth( int n)
{
	if( n != m_nBtnWidth)
	{
		m_nBtnWidth = n;
		CheckTitleButtonSize();
	}
}

int CTabControl::GetButtonWidth() const
{
	return m_nBtnWidth;
}

void CTabControl::SetButtonHeight( int n)
{
	if( n != m_nBtnHeight)
	{
		m_nBtnHeight = n;
		CheckTitleButtonSize();
	}
}

int CTabControl::GetButtonHeight() const
{
	return m_nBtnHeight;
}

void CTabControl::SetTitleVertical( bool bv)
{
	if( bv == m_bVertical)
		return;
	m_bVertical = bv;

	CheckTitlePanelPos();
	CheckTitleButtonSize();
	CheckClientComponentSize();
}

bool CTabControl::IsTitleVertical() const
{
	return m_bVertical;
}

void CTabControl::SetTitleRightBottom( bool br)
{
	if ( br == m_bRightBottom)
		return;
	m_bRightBottom = br;

	CheckTitlePanelPos();
	CheckTitleButtonSize();
	CheckClientComponentSize();
}

bool CTabControl::IsTitleRightBottom() const
{
	return m_bRightBottom;
}

void CTabControl::CheckTitlePanelPos()
{
	if ( m_bVertical)
	{
		int w = m_piv->empty() ? m_nTitleSize : max( m_nTitleSize, m_piv->at( 0).btn->GetWidth());
		m_TitlePanel.SetSize( w, GetHeight());
		if ( m_bRightBottom)
			m_TitlePanel.SetPos( GetWidth() - max( GetButtonWidth(), w), m_nTitleOffset);
		else
			m_TitlePanel.SetPos( 0, m_nTitleOffset);
	}
	else
	{
		int h = m_piv->empty() ? m_nTitleSize : max( m_nTitleSize, m_piv->at( 0).btn->GetHeight());
		m_TitlePanel.SetSize( GetWidth(), h);
		if ( m_bRightBottom)
			m_TitlePanel.SetPos( m_nTitleOffset, GetHeight() - max( GetButtonHeight(), h));
		else
			m_TitlePanel.SetPos( m_nTitleOffset, 0);
	}

	INVALIDATE();
}

void CTabControl::CheckTitleButtonSize()
{
	int nCount = (int)m_piv->size();

	int num = 0;
	int titlesize = m_bVertical ? m_TitlePanel.GetWidth() : m_TitlePanel.GetHeight();
	for( int i = 0; i < nCount; i ++)
	{
		INLINECLASS::CTabControlItem& tci = m_piv->at( i);
		if ( !tci.visible)
			continue;
		tci.btn->SetSize( m_nBtnWidth, m_nBtnHeight);
		if( m_bVertical)
		{
			int n = (m_nTitleSize - m_nBtnWidth) / 2;
			int p = m_bRightBottom && n < 0 ? m_nTitleSize - m_nBtnWidth : max( 0, n);
			n = max( 0, n);
			tci.btn->SetPos( p, (m_nBtnHeight + n ) * num + n);
			titlesize = max( tci.btn->GetWidth(), titlesize);
		}
		else
		{
			int n = (m_nTitleSize - m_nBtnHeight) / 2;
			int p = m_bRightBottom && n < 0 ? m_nTitleSize - m_nBtnHeight : max( 0, n);
			n = max( 0, n);
			tci.btn->SetPos( (m_nBtnWidth + n ) * num + n, p);
			titlesize = max( tci.btn->GetHeight(), titlesize);
		}
		num++;
	}
	if ( m_bVertical)
		m_TitlePanel.SetSize( titlesize, m_TitlePanel.GetHeight());
	else 
		m_TitlePanel.SetSize( m_TitlePanel.GetWidth(), titlesize);

	INVALIDATE();
}

void CTabControl::CheckClientComponentSize()
{
	for( INLINECLASS::CTabControlItemVector::iterator it = m_piv->begin(); it != m_piv->end(); ++ it)
	{
		INLINECLASS::CTabControlItem& tci = *it;
		if( m_bVertical)
		{
			//int tw = m_TitlePanel.GetWidth();
			if ( m_bRightBottom)
				tci.panel->SetPos( 0, 0);
			else
				tci.panel->SetPos( m_nTitleSize, 0);
			tci.panel->SetSize( GetWidth() - m_nTitleSize, GetHeight());
		}
		else
		{
			//int th = m_TitlePanel.GetHeight();
			if ( m_bRightBottom)
				tci.panel->SetPos( 0, 0);
			else
				tci.panel->SetPos( 0, m_nTitleSize);
			tci.panel->SetSize( GetWidth(), GetHeight() - m_nTitleSize);
		}
	} // for( INLINECLASS::CTabControlItemVector::iterator it = m_piv->begin(); it != m_piv->end(); ++ it)

	INVALIDATE();
}

void CTabControl::SetSize( int w, int h)
{
	CBKComponent::SetSize( w, h);
	SetTitleSize( m_bVertical ? m_TitlePanel.GetWidth() : m_TitlePanel.GetHeight());
	CheckClientComponentSize();
}

int CTabControl::InsetComponent( CComponent& com)
{
	ImageParams	ip;
	ip.nBitmap = -1;
	return InsetComponent( com, ip, ip, ip, ip);
}

int CTabControl::InsetComponent( CComponent& com, const ImageParams& normal, const ImageParams& selected, const ImageParams& mouseonnormal, const ImageParams& mouseonselect, const ImageParams& disable)
{
	INLINECLASS::CTabControlItem tci;

	int nIndex = (int)m_piv->size();
	tci.btn = m_pAlloctor->NewRadioButton();
	tci.panel = &com;
	assert( tci.btn && tci.panel);

	OnInitializeButton( nIndex, tci.btn);
	tci.btn->SetButtonBoxStyleImage( normal, selected, mouseonnormal, mouseonselect, disable);
	m_TitlePanel.InsertComponent( tci.btn);

	com.SetParent( this);
	com.SetBorderColor( m_clrBorder);
	if( m_bVertical)
	{
		int tw = m_TitlePanel.GetWidth();
		com.SetPos( tw, 0);
		com.SetSize( GetWidth() - tw, GetHeight());
	}
	else
	{
		int th = m_TitlePanel.GetHeight();
		com.SetPos( 0, th);
		com.SetSize( GetWidth(), GetHeight() - th);
	}
	m_piv->push_back( tci);
	if( 0 == nIndex)
	{
		assert( -1 == m_nSelected);
		tci.btn->SetChecked();
	}

	INVALIDATE();

	return nIndex;
}

void CTabControl::OnInitializeButton( int nIndex, CAbstractRadioButton* arb)
{
	arb->ButtonClickNotifyAdd( *this);
	arb->SetClickAudio( GetCurrentTabClickAudio());
	arb->SetGroupID( 1);
	arb->SetSize( m_nBtnWidth, m_nBtnHeight);
	arb->SetBoxWidth( m_nBtnWidth);
	if( m_bVertical)
	{
		int n = (m_TitlePanel.GetWidth() - m_nBtnWidth) / 2;
		n = max( 0, n);
		arb->SetPos( n, (m_nBtnHeight + n ) * nIndex + n);
	}
	else
	{
		int n = (m_TitlePanel.GetHeight() - m_nBtnHeight) / 2;
		n = max( 0, n);
		arb->SetPos( (m_nBtnWidth + n ) * nIndex + n, n);
	}
}

int CTabControl::GetIndexByComponent( CComponent& com)
{
	int nCount = (int)m_piv->size();
	for( int i = 0; i < nCount; i ++)
	{
		INLINECLASS::CTabControlItem& tci = m_piv->at( i);
		if( tci.panel->IsSameComponent( com))
			return i;
	} 
	return -1;
}

void CTabControl::RemoveComponent( CComponent& com)
{
	int nIndex = GetIndexByComponent( com);
	if( -1 == nIndex)
		return;
	INLINECLASS::CTabControlItem& tci = m_piv->at( nIndex);
	m_TitlePanel.RemoveComponent( tci.btn);
	tci.btn->ButtonClickNotifyRemove( *this);
	m_pAlloctor->DeleteRadioButton( tci.btn);
	m_piv->erase( m_piv->begin() + nIndex);

	INVALIDATE();
}

int CTabControl::GetComponentCount() const
{
	return (int)m_piv->size();
}

CComponent& CTabControl::GetComponent( int nIndex)
{
	assert( nIndex >= 0 && nIndex < GetComponentCount());
	INLINECLASS::CTabControlItem& tci = m_piv->at( nIndex);
	return *tci.panel;
}

CAbstractRadioButton* CTabControl::GetRadioButton( int nIndex)
{
	assert( nIndex >= 0 && nIndex < GetComponentCount());
	INLINECLASS::CTabControlItem& tci = m_piv->at( nIndex);
	return tci.btn;
}

int CTabControl::GetIndexByRadioButton( CComponent& btn)
{
	int nCount = (int)m_piv->size();
	for( int i = 0; i < nCount; i ++)
	{
		INLINECLASS::CTabControlItem& tci = m_piv->at( i);
		if( tci.btn->IsSameComponent( btn))
			return i;
	} 
	return -1;
}

void CTabControl::OnButtonClick( CAbstractButton& c)
{
	if ( IsViewOnly())
		return;

	int nIndex = GetIndexByRadioButton( c);
	assert( -1 != nIndex);
	if( -1 != m_nSelected)
	{
		m_tcns->NotifyAll( *this, &CTabChangedNotify::OnTabChanged, m_nSelected, nIndex);

		INLINECLASS::CTabControlItem& tci = m_piv->at( m_nSelected);
		tci.panel->OnComponentHide();
	}
	m_nSelected = nIndex;

	if( -1 != m_nSelected)
	{
		INLINECLASS::CTabControlItem& tci = m_piv->at( m_nSelected);
		tci.panel->OnComponentShow();
	}
}	

int CTabControl::GetSelect() const
{
	return m_nSelected;
}

void CTabControl::SetSelect( int nIndex)
{
	assert( nIndex >= 0 && nIndex < GetComponentCount());
	INLINECLASS::CTabControlItem& tci = m_piv->at( nIndex);
	if ( tci.visible)
		tci.btn->SetChecked();

	INVALIDATE();
}

void CTabControl::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	if( -1 == m_nSelected)
		return;
	INLINECLASS::CTabControlItem& tci = m_piv->at( m_nSelected);
	if( m_bVertical)
	{
		int ps = m_nTitleSize; //m_TitlePanel.GetWidth();

		if ( tci.panel->GetWidth() > GetWidth() - ps)
			tci.panel->SetSize( GetWidth() - ps, tci.panel->GetHeight());

		int nl = l + ( m_bRightBottom ? 0 : ps);
		int ncx = cx < ps ? 0 : cx - ps;
		int ncw = min( cw, tci.panel->GetWidth());
		int nch = min( ch, tci.panel->GetHeight());


		tci.panel->Draw( draw, nl, t, ncx, cy, ncw, nch);
		tci.panel->DoComponentAfterDrawNofity( draw, nl, t, ncx, cy, ncw, nch);


		ps = m_TitlePanel.GetWidth();
		if( cx < ps)
		{
			int nw = ps - cx;
			nw = min( nw, cw);
			m_TitlePanel.Draw( draw, l, t, cx, cy, nw, ch);
			m_TitlePanel.DoComponentAfterDrawNofity( draw, l, t, cx, cy, nw, ch);
			cw -= nw;
			cx = 0;
		}
		//else
		//{
		//	cx -= ps;
		//}
		//tci.panel->Draw( draw, l + ( m_bRightBottom ? 0 : ps), t, cx, cy, cw, ch);
	}
	else
	{
		int ps = m_nTitleSize; //m_TitlePanel.GetHeight();

		if ( tci.panel->GetHeight() > GetHeight() - ps)
			tci.panel->SetSize( GetWidth(), GetHeight() - ps);
		
		int nt = t + ( m_bRightBottom ? 0 : ps);
		int ncy = cy < ps ? 0 : cy - ps;
		int ncw = min( cw, tci.panel->GetWidth());
		int nch = min( ch, tci.panel->GetHeight());
		tci.panel->Draw( draw, l, nt, cx, ncy, ncw, nch);
		tci.panel->DoComponentAfterDrawNofity( draw, l, nt, cx, ncy, ncw, nch);

		ps = m_TitlePanel.GetHeight();
		if( cy < ps)
		{
			int nh = ps - cy;
			nh = min( nh, ch);
			m_TitlePanel.Draw( draw, l, t, cx, cy, cw, nh);
			m_TitlePanel.DoComponentAfterDrawNofity( draw, l, t, cx, cy, cw, nh);
			ch -= nh;
			cy = 0;
		}
		//else
		//{
		//	cy -= ps;
		//}
		//tci.panel->Draw( draw, l, t + ( m_bRightBottom ? 0 : ps), cx, cy, cw ,ch);
	}
}

void CTabControl::OnComponentShow()
{
	for( INLINECLASS::CTabControlItemVector::iterator it = m_piv->begin(); it != m_piv->end(); ++ it)
	{
		INLINECLASS::CTabControlItem& tci = *it;
		tci.btn->OnComponentShow();
	}
	if( -1 != m_nSelected)
	{
		INLINECLASS::CTabControlItem& tci = m_piv->at( m_nSelected);
		tci.panel->OnComponentShow();
	}
	CBKComponent::OnComponentShow();
}

void CTabControl::OnComponentHide()
{
	for( INLINECLASS::CTabControlItemVector::iterator it = m_piv->begin(); it != m_piv->end(); ++ it)
	{
		INLINECLASS::CTabControlItem& tci = *it;
		tci.btn->OnComponentHide();
	}
	if( -1 != m_nSelected)
	{
		INLINECLASS::CTabControlItem& tci = m_piv->at( m_nSelected);
		tci.panel->OnComponentHide();
	}
	CBKComponent::OnComponentHide();
}

void CTabControl::SetBorderColor(COLOR clr)
{
	m_clrBorder = clr;
	m_TitlePanel.SetBorderColor( clr);
	for( INLINECLASS::CTabControlItemVector::iterator it = m_piv->begin(); it != m_piv->end(); ++ it)
	{
		( *it).panel->SetBorderColor( clr);
	}
}

bool CTabControl::OnLeftButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	return INLINECLASS::DispatchTabControlMouseMessage( &CComponent::OnLeftButtonDown, m_TitlePanel, GetComponent( m_nSelected), x, y, key);
}

bool CTabControl::OnLeftButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	return INLINECLASS::DispatchTabControlMouseMessage( &CComponent::OnLeftButtonUp, m_TitlePanel, GetComponent( m_nSelected), x, y, key);
}

//bool CTabControl::OnLeftButtonDoubleClick( int x, int y, int key)
//{
//	if ( IsViewOnly())
//		return false;
//
//	return INLINECLASS::DispatchTabControlMouseMessage( &CComponent::OnLeftButtonDoubleClick, m_TitlePanel, GetComponent( m_nSelected), x, y, key);
//}

bool CTabControl::OnRightButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	return INLINECLASS::DispatchTabControlMouseMessage( &CComponent::OnRightButtonDown, m_TitlePanel, GetComponent( m_nSelected), x, y, key);
}

bool CTabControl::OnRightButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	return INLINECLASS::DispatchTabControlMouseMessage( &CComponent::OnRightButtonUp, m_TitlePanel, GetComponent( m_nSelected), x, y, key);
}

//bool CTabControl::OnRightButtonDoubleClick( int x, int y, int key)
//{
//	if ( IsViewOnly())
//		return false;
//
//	return INLINECLASS::DispatchTabControlMouseMessage( &CComponent::OnRightButtonDoubleClick, m_TitlePanel, GetComponent( m_nSelected), x, y, key);
//}

bool CTabControl::OnMouseMove( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if( m_TitlePanel.IsAbsPointIn( x, y))
	{
		if( !m_TitlePanel.IsSameComponent( m_pLastMouseIn))
		{
			if( m_pLastMouseIn)
				m_pLastMouseIn->OnMouseLeave();
			m_TitlePanel.OnMouseEnter();
			m_pLastMouseIn = &m_TitlePanel;
		}
	}
	else
	{
		CComponent& com = GetComponent( m_nSelected);
		if( !com.IsSameComponent( m_pLastMouseIn))
		{
			if( m_pLastMouseIn)
				m_pLastMouseIn->OnMouseLeave();
			com.OnMouseEnter();
			m_pLastMouseIn = &com;
		}
	}

	return INLINECLASS::DispatchTabControlMouseMessage( &CComponent::OnMouseMove, m_TitlePanel, GetComponent( m_nSelected), x, y, key);
}

bool CTabControl::OnMouseWheel( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	return INLINECLASS::DispatchTabControlMouseMessage( &CComponent::OnMouseWheel, m_TitlePanel, GetComponent( m_nSelected), x, y, key);
}

void CTabControl::OnMouseLeave()
{
	if ( IsViewOnly())
		return;

	if( m_pLastMouseIn)
	{
		m_pLastMouseIn->OnMouseLeave();
		m_pLastMouseIn = NULL;
	}
	CBKComponent::OnMouseLeave();
}

void CTabControl::SetTitleOffset( int n)
{
	if ( m_bVertical)
		m_TitlePanel.SetPos( m_TitlePanel.GetPosX(), m_TitlePanel.GetPosY() + n - m_nTitleOffset);
	else
		m_TitlePanel.SetPos( m_TitlePanel.GetPosX() + n - m_nTitleOffset, m_TitlePanel.GetPosY());
	m_nTitleOffset = n;

	INVALIDATE();
}

int CTabControl::GetTitleOffset() const
{
	return m_nTitleOffset;
}

void CTabControl::FreeAllTextDraw()
{
	for( INLINECLASS::CTabControlItemVector::iterator it = m_piv->begin(); it != m_piv->end(); ++ it)
	{
		INLINECLASS::CTabControlItem& tci = *it;
		if ( CTextImageRadioButton* pbtn = dynamic_cast<CTextImageRadioButton*> ( tci.btn))
			pbtn->FreeAllTextDraw();
	} // for( INLINECLASS::CTabControlItemVector::iterator it = m_piv->begin(); it != m_piv->end(); ++ it)
}

void CTabControl::SetTabVisible( int index, bool bVisible)
{
	m_piv->at( index).visible = bVisible;
	if ( !bVisible)
		m_piv->at( index).btn->SetChecked( false);
	m_TitlePanel.RemoveAllComponent();
	for ( INLINECLASS::CTabControlItemVector::iterator it = m_piv->begin(), ite = m_piv->end(); it != ite; ++ it)
	{
		INLINECLASS::CTabControlItem& item = *it;
		if ( item.visible)
			m_TitlePanel.InsertComponent( item.btn);
	}
	CheckTitleButtonSize();
	CheckTitlePanelPos();
	if ( !bVisible && m_nSelected == index)
	{
		for ( int i = 0; i < (int)m_piv->size(); ++ i)
		{
			if ( m_piv->at( i).visible)
			{
				SetSelect( i);
				break;
			}
		}
	}
	INVALIDATE();
}

bool CTabControl::IsTabVisible( int index) const
{
	return m_piv->at( index).visible;
}


} // namespace GXWINDOW { 

