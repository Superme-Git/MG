#include "gxwindowdef.h"

namespace GXWINDOW {

namespace INLINECLASS {  

class CPackageCellClickNotifySet : public CNotifySet<CPackageCellClickNotify*>
{
public:
	CPackageCellClickNotifySet() {}
	virtual ~CPackageCellClickNotifySet() {}
};

class CPackageCellVector : public std::vector<CAbstactPackageCell*>
{
public:
	CPackageCellVector() {}
	virtual ~CPackageCellVector() {}
};

} // namespace INLINECLASS {  

CPackageCellClickNotify::CPackageCellClickNotify() 
{
}

CPackageCellClickNotify::~CPackageCellClickNotify() 
{
}

CAbstactPackageCell::CAbstactPackageCell()
	: m_bMouseIn( false), m_bSelected( false), m_NotifySet( new INLINECLASS::CPackageCellClickNotifySet)
{
	InitializeWindowsUpdating();
}

CAbstactPackageCell::~CAbstactPackageCell()
{
	UPDATE_CHECK_ASSERT();
	delete m_NotifySet;
}

void CAbstactPackageCell::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
//	CComponent::Draw( draw, l, t, cx, cy, cw, ch);
	OnDrawIcon( draw, l, t, cx, cy, cw, ch);
	if( m_bSelected)
		OnDrawSelect( draw, l, t, cx, cy, cw,ch);
	if( m_bMouseIn)
		OnDrawMouseIn( draw, l, t, cx, cy, cw, ch);
}

void CAbstactPackageCell::OnDrawSelect( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
}

void CAbstactPackageCell::OnDrawMouseIn( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
}

void CAbstactPackageCell::OnMouseEnter()
{ 
	if ( IsViewOnly())
		return;

	m_bMouseIn = true; 
	CComponent::OnMouseEnter();
}

void CAbstactPackageCell::OnMouseLeave() 
{ 
	m_bMouseIn = false; 
	if ( IsViewOnly())
		return;

	CComponent::OnMouseLeave();
}

bool CAbstactPackageCell::OnLeftButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	UPDATE_CHECK_BEGIN();

	m_NotifySet->NotifyAll( *this, &CPackageCellClickNotify::OnPackageCellClick, key);
	bool r = CComponent::OnLeftButtonUp( x, y, key);

	UPDATE_CHECK_END();
	return r;
}

bool CAbstactPackageCell::OnRightButtonUp(int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	UPDATE_CHECK_BEGIN();

	m_NotifySet->NotifyAll( *this, &CPackageCellClickNotify::OnPackageCellRClick);
	bool r = CComponent::OnRightButtonUp( x, y, key);

	UPDATE_CHECK_END();
	return r;
}

bool CAbstactPackageCell::OnMouseMove(int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	UPDATE_CHECK_BEGIN();

	m_NotifySet->NotifyAll( *this, &CPackageCellClickNotify::OnPackageCellMouseMove);
	bool r = CComponent::OnMouseMove( x, y, key);

	UPDATE_CHECK_END();
	return r;
}

bool CAbstactPackageCell::IsSelected() const 
{ 
	return m_bSelected; 
}

void CAbstactPackageCell::SetSelect( bool bSelect) 
{ 
	m_bSelected = bSelect; 
}

bool CAbstactPackageCell::IsMouseIn() const
{
	CFrame* f = ((CComponent*)this)->GetParentFrame();
	if( NULL == f)
		return false;
	if( !f->IsVisible())
		return false;
	return m_bMouseIn;
}

void CAbstactPackageCell::CellClickNotifyAdd( CPackageCellClickNotify& pccn)
{
	m_NotifySet->Insert( &pccn);
}

void CAbstactPackageCell::CellClickNotifyRemove( CPackageCellClickNotify& pccn)
{
	m_NotifySet->Remove( &pccn);
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CPackagePanelLayout::CPackagePanelLayout() {}
CPackagePanelLayout::~CPackagePanelLayout() {}

CGridPackagePanelLayout::CGridPackagePanelLayout() : m_nRows( 1), m_nCols( 1), m_nVerticalSpace( 6), m_nHorizontalSpace( 6), m_nXMargin( 0), m_nYMargin( 0) {}
CGridPackagePanelLayout::~CGridPackagePanelLayout() {}

CPackagePanelLayout* CGridPackagePanelLayout::Clone() const 
{ 
	CGridPackagePanelLayout* layout = new CGridPackagePanelLayout(); 
	layout->SetRowCol( GetRowCount(), GetColCount());
	layout->SetSpace( GetVerticalSpace(), GetHorizontalSpace());
	layout->SetMargin( GetXMargin(), GetYMargin());
	return layout;
}

void CGridPackagePanelLayout::Delete() { delete this; }

void CGridPackagePanelLayout::Layout( CAbstactPackagePanel& panel)
{
	int nCellWidth = panel.GetCellWidth();
	int nCellHeight = panel.GetCellHeight();

	for( int j = 0; j < m_nRows; j ++)
		for( int i = 0; i < m_nCols; i ++)
		{
			if ( j * m_nCols + i >= panel.GetCount())
				break;

			CAbstactPackageCell& pc = panel.GetCell( j * m_nCols + i);
			pc.SetPos( m_nXMargin + i * ( nCellWidth + m_nHorizontalSpace), m_nYMargin + j * ( nCellHeight + m_nVerticalSpace));
			pc.SetSize( nCellWidth, nCellHeight);
		}

	panel.SetSize( m_nXMargin + m_nCols * ( nCellWidth + m_nHorizontalSpace) - m_nHorizontalSpace, m_nYMargin + m_nRows * ( nCellHeight + m_nVerticalSpace) - m_nVerticalSpace);

	INVALIDATE();
}

void CGridPackagePanelLayout::SetRowCol( int nRows, int nCols)
{
	assert( nRows > 0 && nCols > 0);
	m_nRows = nRows;
	m_nCols = nCols;
}

int CGridPackagePanelLayout::GetColCount() const
{
	return m_nCols;
}

int CGridPackagePanelLayout::GetRowCount() const
{
	return m_nRows;
}

void CGridPackagePanelLayout::SetSpace( int nVerticalSpace, int nHorizontalSpace)
{
	m_nVerticalSpace = nVerticalSpace;
	m_nHorizontalSpace = nHorizontalSpace;
}

int CGridPackagePanelLayout::GetVerticalSpace() const
{
	return m_nVerticalSpace;
}

int CGridPackagePanelLayout::GetHorizontalSpace() const
{
	return m_nHorizontalSpace;
}

void CGridPackagePanelLayout::SetMargin( int xmargin, int ymargin)
{
	m_nXMargin = xmargin;
	m_nYMargin = ymargin;
}

int CGridPackagePanelLayout::GetXMargin() const
{
	return m_nXMargin;
}

int CGridPackagePanelLayout::GetYMargin() const
{
	return m_nYMargin;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CAbstactPackagePanel::CAbstactPackagePanel()
	: m_nCount( 0), m_nCellWidth( 32), m_nCellHeight( 32),
	m_cells( new INLINECLASS::CPackageCellVector), m_layout( CGridPackagePanelLayout().Clone()),
	m_NotifySet( new INLINECLASS::CPackageCellClickNotifySet())
{
}

CAbstactPackagePanel::~CAbstactPackagePanel()
{
	delete m_NotifySet;
	delete m_cells;
	m_layout->Delete();
}

void CAbstactPackagePanel::SetLayout( const CPackagePanelLayout& ppl)
{
	m_layout->Delete();
	m_layout = ppl.Clone();
	Relayout();
}

const CPackagePanelLayout& CAbstactPackagePanel::GetLayout() const
{
	return *m_layout;
}

CPackagePanelLayout& CAbstactPackagePanel::GetLayout()
{
	return *m_layout;
}

void CAbstactPackagePanel::DoInitialize()
{
	CreateCell();
	Relayout();
}

void CAbstactPackagePanel::DoUninitialize()
{
	ClearCell();
}

void CAbstactPackagePanel::SetCount( int count)
{
	ClearCell();
	m_nCount = count;
	CreateCell();
	Relayout();
}

int CAbstactPackagePanel::GetCount() const
{
	return m_nCount;
}

void CAbstactPackagePanel::Clear()
{
	ClearCell();
	m_nCount = 0;
}

void CAbstactPackagePanel::SetCellSize( int w, int h)
{
	m_nCellWidth = w;
	m_nCellHeight = h;
	Relayout();
}

int CAbstactPackagePanel::GetCellWidth() const
{
	return m_nCellWidth;
}

int CAbstactPackagePanel::GetCellHeight() const
{
	return m_nCellHeight;
}

int CAbstactPackagePanel::GetAbsCellWidth() const
{
	return m_nCellWidth;
}

int CAbstactPackagePanel::GetAbsCellHeight() const
{
	return m_nCellHeight;
}

void CAbstactPackagePanel::Relayout()
{
	m_layout->Layout( *this);
	INVALIDATE();
}

void CAbstactPackagePanel::ClearCell()
{
	for( INLINECLASS::CPackageCellVector::iterator it = m_cells->begin(); it != m_cells->end(); ++ it)
	{
		CAbstactPackageCell*& pc = *it;
		RemoveComponent( pc);
		pc->CellClickNotifyRemove( *this);
		DeleteCell( pc);
	}
	m_cells->clear();
	m_nCount = 0;
	INVALIDATE();
}

void CAbstactPackagePanel::CreateCell()
{
	assert( m_cells->empty());
	for( int i = 0; i < m_nCount; i ++)
	{
		CAbstactPackageCell* pc = NewCell();
		*pc->m_NotifySet = *m_NotifySet;
		pc->InitializeWindowsUpdating();
		InsertComponent( pc);
		pc->CellClickNotifyAdd( *this);
		m_cells->push_back( pc);
	}
}

void CAbstactPackagePanel::CreateOneCell()
{
	CAbstactPackageCell* pc = NewCell();
	*pc->m_NotifySet = *m_NotifySet;
	pc->InitializeWindowsUpdating();
	InsertComponent( pc);
	pc->CellClickNotifyAdd( *this);
	m_cells->push_back( pc);
	m_nCount ++;
}

void CAbstactPackagePanel::RemoveOneCell( CAbstactPackageCell* pc)
{
	INLINECLASS::CPackageCellVector::iterator it = std::find( m_cells->begin(), m_cells->end(), pc);
	if ( it != m_cells->end())
	{
		RemoveComponent( pc);
		m_cells->erase( it);
		DeleteCell( pc);
		-- m_nCount;
	}
}

const CAbstactPackageCell& CAbstactPackagePanel::GetCell( int nIndex) const
{
	assert( nIndex >= 0 && nIndex < (int)m_cells->size());
	return *(m_cells->at( nIndex));
}

CAbstactPackageCell& CAbstactPackagePanel::GetCell( int nIndex)
{
	assert( nIndex >= 0 && nIndex < (int)m_cells->size());
	return *(m_cells->at( nIndex));
}

void CAbstactPackagePanel::CellClickNotifyAdd( CPackageCellClickNotify& pccn)
{
	m_NotifySet->Insert( &pccn);
	for( INLINECLASS::CPackageCellVector::iterator it = m_cells->begin(), ite = m_cells->end(); it != ite; ++ it)
	{
		CAbstactPackageCell*& cell = *it;
		cell->CellClickNotifyAdd( pccn);
	} // for( INLINECLASS::CPackageCellVector::iterator it = m_cells->begin(), ite = m_cells->end(); it != ite; ++ it)
}

void CAbstactPackagePanel::CellClickNotifyRemove( CPackageCellClickNotify& pccn)
{
	m_NotifySet->Remove( &pccn);
	for( INLINECLASS::CPackageCellVector::iterator it = m_cells->begin(), ite = m_cells->end(); it != ite; ++ it)
	{
		CAbstactPackageCell*& cell = *it;
		cell->CellClickNotifyRemove( pccn);
	} // for( INLINECLASS::CPackageCellVector::iterator it = m_cells->begin(), ite = m_cells->end(); it != ite; ++ it)
}


} // namespace GXWINDOW {
