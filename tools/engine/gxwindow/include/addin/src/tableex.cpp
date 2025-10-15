#include "gxwaddindef.h"

namespace GXWADDIN
{

struct CellComponent 
{
	GXWINDOW::CComponent*	m_pComponent;
	int	m_offsetx;
	int m_offsety;

	explicit CellComponent( GXWINDOW::CComponent* c, int offsetx = 0, int offsety = 0)
		: m_pComponent( c), m_offsetx( offsetx), m_offsety( offsety)
	{

	}
};
class CellComponentMap : public std::map<int, CellComponent>
{
public:
	CellComponentMap()  {}
	~CellComponentMap() {}

	void InsertCompoent( int key, GXWINDOW::CComponent* c, int offsetx, int offsety)
	{
		for ( iterator it = this->begin(), ite = this->end(); it != ite; ++ it)
		{
			if ( c->IsSameComponent( it->second.m_pComponent))
			{
				assert( 0 && "不能将同一个控件多次插入表格中!");
				return;
			}
		}
		this->insert( std::make_pair( key, CellComponent( c, offsetx, offsety)) );
	}
	void RemoveComponent( int key)
	{
		iterator it = this->find( key);
		if ( it != this->end())
		{
			it->second.m_pComponent->SetParent( NULL);
			this->erase( it);
		}
	}
	void RemoveAllComponent()
	{
		for ( iterator it = this->begin(), ite = this->end(); it != ite; ++it)
		{
			it->second.m_pComponent->SetParent( NULL);
		}
		this->clear();
	}
	GXWINDOW::CComponent* GetComponent( int key)
	{
		iterator it = this->find( key);
		if ( it == this->end())
			return NULL;
		return it->second.m_pComponent;
	}
	CellComponent* GetCellComponent( int key)
	{
		iterator it = this->find( key);
		if ( it == this->end())
			return NULL;
		return &(it->second);
	}
};

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CComponentTableDataModule::CComponentTableDataModule()
: m_pComponentMap( new CellComponentMap()), m_pLastMoveComponent(NULL)
{

}

CComponentTableDataModule::~CComponentTableDataModule()
{
	delete m_pComponentMap;
}

void CComponentTableDataModule::SetCellComponent( int col, int row, GXWINDOW::CComponent& c, int offsetx, int offsety)
{
	m_pComponentMap->InsertCompoent( GetCellGUID( col, row), &c, offsetx, offsety);
}

void CComponentTableDataModule::RemoveCellComponent( int col, int row)
{
	int key = GetCellGUID( col, row);
	if ( m_pLastMoveComponent)
	{
		if ( m_pLastMoveComponent == m_pComponentMap->GetComponent( key))
			m_pLastMoveComponent = NULL;
	}
	m_pComponentMap->RemoveComponent( key);
}

const GXWINDOW::CComponent* CComponentTableDataModule::GetCellComponent( int col, int row) const
{
	CellComponentMap::const_iterator it = m_pComponentMap->find( GetCellGUID( col, row));
	if ( it != m_pComponentMap->end())
		return it->second.m_pComponent;
	return NULL;
}

void CComponentTableDataModule::RemoveRow( int row)
{
	if ( GXWINDOW::CTable* parent = GetParentTable())
	{
		if ( parent->GetSelectRow() == row)
			parent->SetSelectRow( -1);
	}

	int cols = GetColumnCount();
	for ( int col = 0; col < cols; ++ col)
	{
		int key = GetCellGUID( col, row);
		if ( m_pLastMoveComponent)
		{
			if ( m_pLastMoveComponent == m_pComponentMap->GetComponent( key))
				m_pLastMoveComponent = NULL;
		}
		m_pComponentMap->RemoveComponent( key);
	}
	return CTextTableDataModule::RemoveRow( row);
}

void CComponentTableDataModule::ClearRows()
{
	m_pLastMoveComponent = NULL;
	m_pComponentMap->RemoveAllComponent();
	CTextTableDataModule::ClearRows();
}

void CComponentTableDataModule::DrawClientComponent(int col, int row, CCTYPE cct, GXWINDOW::CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight)
{
	CTextTableDataModule::DrawClientComponent( col, row, cct, draw, l, t, cx, cy, cw, ch, nCellWidth, nCellHeight);
	if ( CellComponent* pc = m_pComponentMap->GetCellComponent( GetCellGUID( col, row)) )
	{
		GXWINDOW::CTable* p = GetParentTable();
		pc->m_pComponent->SetParent( p);
		pc->m_pComponent->SetPos( l - p->GetAbsPosX() + pc->m_offsetx, t - p->GetAbsPosY() + pc->m_offsety);
		DrawCellComponent( *(pc->m_pComponent), draw, l, t, cx + pc->m_offsetx, cy + pc->m_offsety, cw - pc->m_offsetx, ch - pc->m_offsety);
	}
}

void CComponentTableDataModule::OnComponentShow()
{
	CTextTableDataModule::OnComponentShow();
	for ( CellComponentMap::iterator it = m_pComponentMap->begin(), ite = m_pComponentMap->end(); it != ite; ++ it)
	{
		ComponentShow( *(it->second.m_pComponent));
	}
}

void CComponentTableDataModule::OnComponentHide()
{
	CTextTableDataModule::OnComponentHide();
	for ( CellComponentMap::iterator it = m_pComponentMap->begin(), ite = m_pComponentMap->end(); it != ite; ++ it)
	{
		ComponentHide( *(it->second.m_pComponent));
	}
}

void CComponentTableDataModule::OnMouseLeave()
{
	CTextTableDataModule::OnMouseLeave();
	if (m_pLastMoveComponent)
	{
		ComponentMouseLeave( *m_pLastMoveComponent);
		m_pLastMoveComponent = NULL;
	}
}

bool CComponentTableDataModule::OnMouseMove(int x, int y, int key, int col, int row)
{
	if ( GXWINDOW::CComponent* c = m_pComponentMap->GetComponent( GetCellGUID( col, row)))
	{
		if (m_pLastMoveComponent != c)
		{
			if (m_pLastMoveComponent)
				ComponentMouseLeave( *m_pLastMoveComponent);
			m_pLastMoveComponent = c;
			ComponentMouseEnter( *m_pLastMoveComponent);
		}
		return c->OnMouseMove( x, y, key);
	}
	return false;
}

bool CComponentTableDataModule::OnLeftButtonUp(int x, int y, int key, int col, int row)
{
	if ( GXWINDOW::CComponent* c = m_pComponentMap->GetComponent( GetCellGUID( col, row)))
		return c->OnLeftButtonUp( x, y, key);
	return false;
}

bool CComponentTableDataModule::OnLeftButtonDown(int x, int y, int key, int col, int row)
{
	if ( GXWINDOW::CComponent* c = m_pComponentMap->GetComponent( GetCellGUID( col, row)))
		return c->OnLeftButtonDown( x, y, key);
	return false;
}


} // namespace GXWADDIN
