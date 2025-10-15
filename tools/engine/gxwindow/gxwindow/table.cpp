#include "gxwindowdef.h"

namespace GXWINDOW { 

namespace INLINECLASS { 

class CTableDataModuleDataChangeNotifySet : public CNotifySet<CTableDataModuleDataChangeNotify*>
{
public:
	CTableDataModuleDataChangeNotifySet() {}
	virtual ~CTableDataModuleDataChangeNotifySet() {}
};

class CTableMouseOnChangeNotifySet : public CNotifySet<CTableMouseOnChangeNotify*>
{
public:
	CTableMouseOnChangeNotifySet() {}
	virtual ~CTableMouseOnChangeNotifySet() {}
};

class CTableSelectChangeNotifySet : public CNotifySet<CTableSelectChangeNotify*>
{
public:
	CTableSelectChangeNotifySet() {}
	virtual ~CTableSelectChangeNotifySet() {}
};

class CTableHeaderVector : public std::vector<CTableHeaderColumn>
{
public:
	CTableHeaderVector() {}
	virtual ~CTableHeaderVector() {}
};

struct IconTableCell
{
	int				iStart;
	int				iEnd;
	unsigned long	iInterval;

	IconTableCell() :
	iStart( -1), iEnd( -1), iInterval( -1)
	{
	}
};

typedef std::vector<int>	IntVector;
struct TextTableCell
{
	int				guid;		// 保证所有cell的guid唯一, 该值是为了方便往格子里挂图片之类的, 可以当key使用
	int				n;
	int				s;
	int				m;
	IntVector		multiline_n;
	IntVector		multiline_s;
	IntVector		multiline_m;

	std::wstring		text;

	TextTableCell()
		: guid( GenGUID()), n( -1), s( -1), m( -1)
	{
	}

	void FreeTextNormal( CTextDrawManager& tdm)
	{
		for ( IntVector::iterator it = multiline_n.begin(); it != multiline_n.end(); ++ it)
			tdm.FreeTextDraw( *it);
		multiline_n.clear();

		if( -1 == n)
			return;
		tdm.FreeTextDraw( n);
		n = -1;
	}
	void FreeTextSelect( CTextDrawManager& tdm)
	{
		for ( IntVector::iterator it = multiline_s.begin(); it != multiline_s.end(); ++ it)
			tdm.FreeTextDraw( *it);
		multiline_s.clear();

		if( -1 == s)
			return;
		tdm.FreeTextDraw( s);
		s = -1;
	}
	void FreeTextMouseOn( CTextDrawManager& tdm)
	{
		for ( IntVector::iterator it = multiline_m.begin(); it != multiline_m.end(); ++ it)
			tdm.FreeTextDraw( *it);
		multiline_m.clear();

		if( -1 == m)
			return;
		tdm.FreeTextDraw( m);
		m = -1;
	}
	void FreeTextAll( CTextDrawManager& tdm)
	{
		FreeTextNormal( tdm);
		FreeTextSelect( tdm);
		FreeTextMouseOn( tdm);
	}
private:
	static int GenGUID()
	{
		static int i = 0;
		return ++i;
	}
};


void SplitNormal2Multilines(  CTextDrawManager& tdm, IntVector& lines, const wchar_t* text, int linewidth, int font, COLOR color, FONTEFEECTFLAG effect = FET_NONE, COLOR effectcolor = 0, bool bGradualColor = false, COLOR clrGradual = 0)
{
	std::wstring lstr, tmp;
	while ( *text)
	{
		tmp = lstr + *text;
		if ( tdm.GetStringPixelWidth( tmp.c_str(), font) > linewidth)
		{
			lines.push_back( tdm.NewTextDraw( lstr.c_str(), font, color, bGradualColor ? clrGradual : color, 0, effect, effectcolor));
			lstr = *text;
		}
		else
			lstr = tmp;
		
		++ text;
	}

	if ( lstr.compare( tmp) == 0)
	{
		lines.push_back( tdm.NewTextDraw( lstr.c_str(), font, color, bGradualColor ? clrGradual : color, 0, effect, effectcolor));
	}
}

typedef std::vector<TextTableCell>	 CTextTableCellVector;

struct TextTableColumn
{
	std::wstring			strColName;
	int						d;
	CTextTableCellVector	rows;

	TextTableColumn()
		: d( -1)
	{
	}

	int GetColumnText( CTextDrawManager& tdm, int font, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0)
	{
		if( -1 == d)
			d = tdm.NewTextDraw( strColName.c_str(), font, clr, bGradualColor ? clrGradual : clr, 0, type, clrEffect);
		return d;
	}
	TextTableCell& GetCell( int row)
	{
		return rows.at( row);
	}

	void FreeTextHerader( CTextDrawManager& tdm)
	{
		if( -1 == d)
			return;
		tdm.FreeTextDraw( d);
		d = -1;
	}

	void FreeTextNormal( CTextDrawManager& tdm)
	{
		for( CTextTableCellVector::iterator it = rows.begin(); it != rows.end(); ++ it)
		{
			TextTableCell& ttc = *it;
			ttc.FreeTextNormal( tdm);
		}
	}
	void FreeTextSelect( CTextDrawManager& tdm)
	{
		for( CTextTableCellVector::iterator it = rows.begin(); it != rows.end(); ++ it)
		{
			TextTableCell& ttc = *it;
			ttc.FreeTextSelect( tdm);
		}
	}
	void FreeTextMouseOn( CTextDrawManager& tdm)
	{
		for( CTextTableCellVector::iterator it = rows.begin(); it != rows.end(); ++ it)
		{
			TextTableCell& ttc = *it;
			ttc.FreeTextMouseOn( tdm);
		}
	}
	void FreeTextAll( CTextDrawManager& tdm)
	{
		FreeTextHerader( tdm);
		for( CTextTableCellVector::iterator it = rows.begin(); it != rows.end(); ++ it)
		{
			TextTableCell& ttc = *it;
			ttc.FreeTextAll( tdm);
		}
	}
};

class CTextTableDataGrid : public std::vector<TextTableColumn>
{
public:
	CTextTableDataGrid()
	{
	}
	virtual ~CTextTableDataGrid()
	{
	}
public:
	void FreeTextNormal( CTextDrawManager& tdm)
	{
		for( iterator it = begin(); it != end(); ++ it)
		{
			TextTableColumn& ttc = *it;
			ttc.FreeTextNormal( tdm);
		}
	}
	void FreeTextSelect( CTextDrawManager& tdm)
	{
		for( iterator it = begin(); it != end(); ++ it)
		{
			TextTableColumn& ttc = *it;
			ttc.FreeTextSelect( tdm);
		}
	}
	void FreeTextMouseOn( CTextDrawManager& tdm)
	{
		for( iterator it = begin(); it != end(); ++ it)
		{
			TextTableColumn& ttc = *it;
			ttc.FreeTextMouseOn( tdm);
		}
	}
	void FreeTextAll( CTextDrawManager& tdm)
	{
		for( iterator it = begin(); it != end(); ++ it)
		{
			TextTableColumn& ttc = *it;
			ttc.FreeTextAll( tdm);
		}
	}
	void FreeTextHerader( CTextDrawManager& tdm)
	{
		for( iterator it = begin(); it != end(); ++ it)
		{
			TextTableColumn& ttc = *it;
			ttc.FreeTextHerader( tdm);
		}
	}
public:
	int GetColumnText( int c, CTextDrawManager& tdm, int font, COLOR clr, FONTEFEECTFLAG type = FET_NONE, COLOR clrEffect = 0, bool bGradualColor = false, COLOR clrGradual = 0)
	{
		TextTableColumn& ttc = at( c);
		return ttc.GetColumnText( tdm, font, clr, type, clrEffect, bGradualColor, clrGradual);
	}
	TextTableCell& GetCell( int col, int row)
	{
		TextTableColumn& ttc = at( col);
		return ttc.GetCell( row);
	}
	int GetColumnTextWidth( int c, CTextDrawManager& tdm, int font)
	{
		TextTableColumn& ttc = at( c);
		return tdm.GetStringPixelWidth( ttc.d, font);
	}
};

class CTableDataModuleRowParamSet : public std::vector<unsigned int>
{
public:
	CTableDataModuleRowParamSet() {};
	~CTableDataModuleRowParamSet() {};
};

class CTableDataModuleRowLongParamSet : public std::vector<unsigned __int64>
{
public:
	CTableDataModuleRowLongParamSet() {};
	~CTableDataModuleRowLongParamSet() {};
};

class CTableHeaderImages
{
	std::map<int, CImagesFill*> m_imagesmap;
public:
	CTableHeaderImages() {};
	~CTableHeaderImages() 
	{
		Clear();
	};

	CImagesFill* Insert( int index)
	{
		std::map<int, CImagesFill*>::iterator it = m_imagesmap.find( index);
		if ( it == m_imagesmap.end())
			m_imagesmap.insert( std::make_pair( index, new CImagesFill()));
		return m_imagesmap[index];
	}

	CImagesFill* GetAt( int index)
	{
		std::map<int, CImagesFill*>::iterator it = m_imagesmap.find( index);
		if ( it == m_imagesmap.end())
			return NULL;
		return it->second;
	}

	void Remove( int index)
	{
		std::map<int, CImagesFill*>::iterator it = m_imagesmap.find( index);
		if ( it != m_imagesmap.end())
		{
			delete it->second;
			m_imagesmap.erase( it);
		}
	}

	void Clear()
	{
		for ( std::map<int, CImagesFill*>::iterator it = m_imagesmap.begin(); it != m_imagesmap.end(); ++ it)
		{
			if ( it->second)
				delete it->second;
		}
		m_imagesmap.clear();
	}

	int GetCount() const
	{
		return ( int)m_imagesmap.size();
	}

	void RegisterImages()
	{
		for ( std::map<int, CImagesFill*>::iterator it = m_imagesmap.begin(); it != m_imagesmap.end(); ++ it)
		{
			it->second->RegisterImages();
		}
	}

	void FreeImages()
	{
		for ( std::map<int, CImagesFill*>::iterator it = m_imagesmap.begin(); it != m_imagesmap.end(); ++ it)
		{
			it->second->FreeImages();
		}
	}
};

//////////////////////////////////////////////////////////////////////////

class CTableRowsImages
{
	typedef std::map<__int64, CImagesFill*> IMAGE_MAP;
	IMAGE_MAP m_imagesmap;
public:
	CTableRowsImages() {};
	~CTableRowsImages() 
	{
		Clear();
	};

	CImagesFill* Insert( int index)
	{
		IMAGE_MAP::iterator it = m_imagesmap.find( index);
		if ( it == m_imagesmap.end())
			return m_imagesmap.insert( std::make_pair( index, new CImagesFill())).first->second;
		return it->second;
	}

	CImagesFill* GetAt( int index)
	{
		IMAGE_MAP::iterator it = m_imagesmap.find( index);
		if ( it != m_imagesmap.end())
			return it->second;
		return NULL;
	}

	void Remove( int index)
	{
		IMAGE_MAP::iterator it = m_imagesmap.find( index);
		if ( it != m_imagesmap.end())
		{
			CImagesFill* cif = it->second;
			delete cif;
			m_imagesmap.erase( it);
		}
	}

	void Clear()
	{
		for ( IMAGE_MAP::iterator it = m_imagesmap.begin(), ite = m_imagesmap.end(); it != ite; ++ it)
		{
			if ( it->second)
				delete it->second;
		}
		m_imagesmap.clear();
	}

	int GetCount() const
	{
		return ( int)m_imagesmap.size();
	}

	void RegisterImages()
	{
		for ( IMAGE_MAP::iterator it = m_imagesmap.begin(); it != m_imagesmap.end(); ++ it)
		{
			if(it->second)
				it->second->RegisterImages();
		}
	}

	void FreeImages()
	{
		for ( IMAGE_MAP::iterator it = m_imagesmap.begin(); it != m_imagesmap.end(); ++ it)
		{
			if(it->second)
				it->second->FreeImages();
		}
	}
};

} // namespace INLINECLASS { 

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTableDataModuleDataChangeNotify::CTableDataModuleDataChangeNotify()
{
}

CTableDataModuleDataChangeNotify::~CTableDataModuleDataChangeNotify()
{
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTableDataModule::CTableDataModule()
	: m_pParent( NULL), m_bShow( false), m_dcn( new INLINECLASS::CTableDataModuleDataChangeNotifySet()),/* m_bFixHeader( true),*/ m_clrGrid( MakeColor( 0, 0, 0)), m_gridStyle( GRID_LINE), m_nCellTextOffsetX( 0)
{
}

CTableDataModule::~CTableDataModule()
{
	if ( m_bShow)
		OnComponentHide();
	if ( m_pParent)
		m_pParent->ResetTableDataModule( this);
	delete m_dcn;
}

void CTableDataModule::OnComponentShow()
{
	m_bShow = true;
	m_imgHeaderGrid.RegisterImages();
	m_imgCellGrid.RegisterImages();
}

void CTableDataModule::OnComponentHide()
{
	m_bShow = false;
	m_imgHeaderGrid.FreeImages();
	m_imgCellGrid.FreeImages();
}

void CTableDataModule::TableDataModuleDataChangeNotifyAdd( CTableDataModuleDataChangeNotify& dcn)
{
	m_dcn->Insert( &dcn);
}

void CTableDataModule::TableDataModuleDataChangeNotifyRemove( CTableDataModuleDataChangeNotify& dcn)
{
	m_dcn->Remove( &dcn);
}

void CTableDataModule::FireDataChanged()
{
	m_dcn->NotifyAll( *this, &CTableDataModuleDataChangeNotify::OnTableDataModuleDataChange);
}

void CTableDataModule::SetGridColor( COLOR clr)
{
	m_clrGrid = clr;
}

COLOR CTableDataModule::GetGridColor() const
{
	return m_clrGrid;
}

CImagesFill& CTableDataModule::GetCellGridImagesFill() 
{ 
	return m_imgCellGrid; 
}

CImagesFill& CTableDataModule::GetHeaderGridImagesFill() 
{ 
	return m_imgHeaderGrid; 
}

void CTableDataModule::SetGridStyle( GRIDSTYLE style)
{
	m_gridStyle = style;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTextTableDataModule::CTextTableDataModule()
: m_tdm( &CTextDrawManager::GetInstance()), m_grid( new INLINECLASS::CTextTableDataGrid()), m_params( new INLINECLASS::CTableDataModuleRowParamSet()), m_longparams( new INLINECLASS::CTableDataModuleRowLongParamSet()),
	m_nFontHeader( 0), m_clrHeader( MakeColor( 255, 255, 255)) , m_clrHeaderGradual( MakeColor( 255, 255, 255)) ,m_clrRowSelected( MakeColor( 0, 0, 0, 0)), m_clrRowMouseIn( MakeColor( 0, 0, 0, 0)),
	m_clrHeaderEffect( 0), m_fefHeader( FET_NONE), m_taFlag( TXA_MIDDLE | TXA_CENTER), m_bMultilines( false)
{
}

CTextTableDataModule::~CTextTableDataModule()
{
	delete m_grid;
	delete m_params;
	delete m_longparams;
}

void CTextTableDataModule::DrawHeaderComponent( int nIndex, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight)
{
	assert( nIndex >= 0 && nIndex < GetColumnCount());

	if ( GRID_LINE == m_gridStyle)
		draw.Rect( l + cx, t + cy, l + cx + cw, t + cy + ch, m_clrGrid);
	else
	{
		// 注释掉的部分是之前为了处理梦诛中由图片拼全网格的代码，为了处理网格线空心效果，约定的网格图素形式如下
		// -------------
		// |           |
		// ------------+
		// 即一个普通格子，但右下角替换成一个交叉十字
		// 
		/*
		if ( nIndex < GetColumnCount() - 1)
			m_imgHeaderGrid.SetGridSize( 0, m_imgHeaderGrid.GetGridHeight( GXWINDOW::IFP_BOTTOMRIGHT), GXWINDOW::IFP_BOTTOMRIGHT);
		else
			m_imgHeaderGrid.SetGridSize( m_imgHeaderGrid.GetGridWidth( GXWINDOW::IFP_TOPLEFT), m_imgHeaderGrid.GetGridHeight( GXWINDOW::IFP_BOTTOMRIGHT), GXWINDOW::IFP_BOTTOMRIGHT);
		*/
		m_imgHeaderGrid.SetGridSize( cw - m_imgHeaderGrid.GetGridWidth( IFP_TOPLEFT) - m_imgHeaderGrid.GetGridWidth( IFP_BOTTOMRIGHT), ch - m_imgHeaderGrid.GetGridHeight( IFP_TOPLEFT) - m_imgHeaderGrid.GetGridHeight( IFP_BOTTOMRIGHT), IFP_CENTER);
		m_imgHeaderGrid.Draw( draw, l + cx, t + cy, 0, 0, cw, ch);
/*
		if ( nIndex == 0)
		{
			int w = m_imgHeaderGrid.GetGridWidth( IFP_TOPLEFT);
			int h = m_imgHeaderGrid.GetGridHeight( IFP_BOTTOMRIGHT);

			draw.DrawPicture( m_imgHeaderGrid.GetTileImage( IFP_TOPRIGHT), l + cx, t  + cy , w, h, w, 0, -w, h);
			draw.DrawPicture( m_imgHeaderGrid.GetTileImage( IFP_BOTTOMRIGHT), l + cx, t  + cy + m_imgHeaderGrid.GetGridHeight( IFP_TOPLEFT) + m_imgHeaderGrid.GetGridHeight( IFP_CENTER), w, h, w, 0, -w, h);
		}
*/
	}

	int n = m_grid->GetColumnText( nIndex, *m_tdm, m_nFontHeader, m_clrHeader, m_fefHeader, m_clrHeaderEffect, m_clrHeader != m_clrHeaderGradual, m_clrHeaderGradual);
	if( -1 != n)
	{
		int offset_x = m_nCellTextOffsetX;
		int offset_y = 0;

		if ( m_taFlag & TXA_CENTER)
			offset_x = max( m_nCellTextOffsetX, ( nCellWidth - m_grid->GetColumnTextWidth( nIndex, *m_tdm, m_nFontHeader)) >> 1);
		else if ( m_taFlag & TXA_RIGHT)
			offset_x = max( m_nCellTextOffsetX, nCellWidth - m_grid->GetColumnTextWidth( nIndex, *m_tdm, m_nFontHeader));

		if ( m_taFlag & TXA_MIDDLE)
			offset_y = max( 0, ( nCellHeight - m_tdm->GetFontHeightWithoutUnderline( m_nFontHeader)) >> 1);
		else if ( m_taFlag & TXA_BOTTOM)
			offset_y = max( 0, nCellHeight - m_tdm->GetFontHeight( m_nFontHeader));
		
		m_tdm->Draw( n, l + offset_x, t + offset_y, max( 0, cx - offset_x), max( 0, cy - offset_y), cw + cx - offset_x , ch + cy - offset_y);
	}
}

void CTextTableDataModule::DrawClientComponent( int col, int row, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight)
{
	assert( col >= 0 && col < GetColumnCount());
	assert( row >= 0 && row < GetRowCount());

	if  ( m_bMultilines)
	{
		DrawMultilineClientComponent( col, row, cct, draw, l, t, cx, cy, cw, ch, nCellWidth, nCellHeight);
		return;
	}

	INLINECLASS::TextTableCell&  ttc = m_grid->GetCell( col, row);
	int n = -1;
	int bkclr = MakeColor( 0, 0, 0, 0);
	switch( cct)
	{
	case CCT_NORMAL:
		if( -1 == ttc.n)
			ttc.n = m_tdm->NewTextDraw( ttc.text.c_str(), m_nFont, m_clrNormal, m_clrNormalGradual, 0, m_uNormalEffect, m_clrNormalEffect);
		n = ttc.n;
		break;
	case CCT_MOUSEON:
		if( -1 == ttc.m)
			ttc.m = m_tdm->NewTextDraw( ttc.text.c_str(), m_nFont, m_clrMouseOn, m_clrMouseOnGradual, 0, m_uMouseOnEffect, m_clrMouseOnEffect);
		n = ttc.m;
		bkclr = m_clrRowMouseIn;
		break;
	case CCT_SELECT:
		if( -1 == ttc.s)
			ttc.s = m_tdm->NewTextDraw( ttc.text.c_str(), m_nFont, m_clrSelected, m_clrSelectedGradual, 0, m_uSelectedEffect, m_clrSelectedEffect);
		n = ttc.s;
		bkclr = m_clrRowSelected;
		break;
	default:
		assert( false);
		break;
	}

	if ( GRID_LINE == m_gridStyle)
		draw.Rect( l + cx, t + cy, l + cx + cw, t + cy + ch, m_clrGrid);
	else if ( m_imgCellGrid.IsValid())
	{
		// 注释掉部分含义同DrawHeaderComponent注掉部分
		/*
		if ( col < GetColumnCount() - 1)
			m_imgCellGrid.SetGridSize( 0, m_imgCellGrid.GetGridHeight( GXWINDOW::IFP_BOTTOMRIGHT), GXWINDOW::IFP_BOTTOMRIGHT);
		else
			m_imgCellGrid.SetGridSize( m_imgCellGrid.GetGridWidth( GXWINDOW::IFP_TOPLEFT), m_imgCellGrid.GetGridHeight( GXWINDOW::IFP_BOTTOMRIGHT), GXWINDOW::IFP_BOTTOMRIGHT);

		//if ( row < GetRowCount() - 1)
			m_imgCellGrid.SetGridSize( m_imgCellGrid.GetGridWidth( GXWINDOW::IFP_TOPLEFT), 0, GXWINDOW::IFP_TOPLEFT);
		//else
			//m_imgCellGrid.SetGridSize( m_imgCellGrid.GetGridWidth( GXWINDOW::IFP_TOPLEFT), m_imgCellGrid.GetGridHeight( GXWINDOW::IFP_BOTTOMRIGHT), GXWINDOW::IFP_TOPLEFT);
		*/

		m_imgCellGrid.SetGridSize( cw - m_imgCellGrid.GetGridWidth( IFP_TOPLEFT) - m_imgCellGrid.GetGridWidth( IFP_BOTTOMRIGHT), max( 0, ch - m_imgCellGrid.GetGridHeight( IFP_TOPLEFT) - m_imgCellGrid.GetGridHeight( IFP_BOTTOMRIGHT)), IFP_CENTER);
		m_imgCellGrid.Draw( draw, l + cx, t + cy, 0, 0, cw, ch);

		/*
		if ( ch >= m_imgCellGrid.GetGridHeight( IFP_BOTTOMRIGHT))
		{
			if ( col == 0)
			{
				int w = min( cw, m_imgCellGrid.GetGridWidth( IFP_TOPLEFT));
				int h = min( ch, m_imgCellGrid.GetGridHeight( IFP_BOTTOMRIGHT));

				if ( row == GetRowCount() - 1)
					draw.DrawPicture( m_imgCellGrid.GetTileImage( IFP_TOPRIGHT), l + cx, t  + cy + m_imgCellGrid.GetGridHeight( IFP_TOPLEFT) + m_imgCellGrid.GetGridHeight( IFP_CENTER), w, h, w, h, -w, -h);
				else
					draw.DrawPicture( m_imgCellGrid.GetTileImage( IFP_BOTTOMRIGHT), l + cx, t  + cy + m_imgCellGrid.GetGridHeight( IFP_TOPLEFT) + m_imgCellGrid.GetGridHeight( IFP_CENTER), w, h, w, 0, -w, h);
			}
			else if ( row == GetRowCount() - 1)
			{
				int w = m_imgCellGrid.GetGridWidth( IFP_TOPLEFT);
				int h = m_imgCellGrid.GetGridHeight( IFP_BOTTOMRIGHT);
				draw.DrawPicture( m_imgCellGrid.GetTileImage( IFP_TOPLEFT), l + cx, t  + cy + m_imgCellGrid.GetGridHeight( IFP_TOPLEFT) + m_imgCellGrid.GetGridHeight( IFP_CENTER), w, h, 0, h, w, -h);

				if ( col == GetColumnCount() - 1)
					draw.DrawPicture( m_imgCellGrid.GetTileImage( IFP_TOPRIGHT), l + cx + m_imgCellGrid.GetGridWidth( IFP_TOPLEFT) + m_imgCellGrid.GetGridWidth( IFP_CENTER), t  + cy + m_imgCellGrid.GetGridHeight( IFP_TOPLEFT) + m_imgCellGrid.GetGridHeight( IFP_CENTER), w, h, 0, h, w, -h);
			}
		}
		*/

	}

	if ( 0 != GetColorAlpha( bkclr))
		draw.FillRect( l + cx, t + cy, l + cx + cw, t + cy + ch, bkclr);

	if( -1 != n)
	{
		int offset_x = m_nCellTextOffsetX;
		int offset_y = 0;

		if ( m_taFlag & TXA_CENTER)
			offset_x = max( m_nCellTextOffsetX, ( nCellWidth - m_tdm->GetStringPixelWidth( n, m_nFont)) >> 1);
		else if ( m_taFlag & TXA_RIGHT)
			offset_x = max( m_nCellTextOffsetX, nCellWidth - m_tdm->GetStringPixelWidth( n, m_nFont));

		if ( m_taFlag & TXA_MIDDLE)
			offset_y = max( 0, ( nCellHeight - m_tdm->GetFontHeightWithoutUnderline( m_nFont)) >> 1);
		else if ( m_taFlag & TXA_BOTTOM)
			offset_y = max( 0, nCellHeight - m_tdm->GetFontHeight( m_nFont));

		m_tdm->Draw( n, l + offset_x, t + offset_y, max( 0, cx - offset_x), max( 0, cy - offset_y), max( 0, cw - offset_x), max( 0, ch - offset_y));
	}
}

void CTextTableDataModule::DrawMultilineClientComponent( int col, int row, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight)
{
	INLINECLASS::TextTableCell&  ttc = m_grid->GetCell( col, row);
	INLINECLASS::IntVector n;
	int bkclr = MakeColor( 0, 0, 0, 0);
	switch( cct)
	{
	case CCT_NORMAL:
		if( ttc.multiline_n.empty())
		{
			ttc.FreeTextNormal( *m_tdm);
			INLINECLASS::SplitNormal2Multilines( *m_tdm, ttc.multiline_n, ttc.text.c_str(), nCellWidth, m_nFont, m_clrNormal, m_uNormalEffect, m_clrNormalEffect, m_clrNormal != m_clrNormalGradual, m_clrNormalGradual);
		}
		n = ttc.multiline_n;
		break;
	case CCT_MOUSEON:
		if( ttc.multiline_m.empty())
		{
			ttc.FreeTextMouseOn( *m_tdm);
			INLINECLASS::SplitNormal2Multilines( *m_tdm, ttc.multiline_m, ttc.text.c_str(), nCellWidth, m_nFont, m_clrMouseOn, m_uMouseOnEffect, m_clrMouseOnEffect, m_clrMouseOn != m_clrMouseOnGradual, m_clrMouseOnGradual);
		}
		n = ttc.multiline_m;
		bkclr = m_clrRowMouseIn;
		break;
	case CCT_SELECT:
		if( ttc.multiline_s.empty())
		{
			ttc.FreeTextSelect( *m_tdm);
			INLINECLASS::SplitNormal2Multilines( *m_tdm, ttc.multiline_s, ttc.text.c_str(), nCellWidth, m_nFont, m_clrSelected, m_uSelectedEffect, m_clrSelectedEffect, m_clrSelected != m_clrSelectedGradual, m_clrSelectedGradual);
		}
		n = ttc.multiline_s;
		bkclr = m_clrRowSelected;
		break;
	default:
		assert( false);
		break;
	}

	if ( GRID_LINE == m_gridStyle)
		draw.Rect( l + cx, t + cy, l + cx + cw, t + cy + ch, m_clrGrid);
	else
	{
		if ( col < GetColumnCount() - 1)
			m_imgCellGrid.SetGridSize( 0, m_imgCellGrid.GetGridHeight( GXWINDOW::IFP_BOTTOMRIGHT), GXWINDOW::IFP_BOTTOMRIGHT);
		else
			m_imgCellGrid.SetGridSize( m_imgCellGrid.GetGridWidth( GXWINDOW::IFP_TOPLEFT), m_imgCellGrid.GetGridHeight( GXWINDOW::IFP_BOTTOMRIGHT), GXWINDOW::IFP_BOTTOMRIGHT);

		//if ( row < GetRowCount() - 1)
		m_imgCellGrid.SetGridSize( m_imgCellGrid.GetGridWidth( GXWINDOW::IFP_TOPLEFT), 0, GXWINDOW::IFP_TOPLEFT);
		//else
		//m_imgCellGrid.SetGridSize( m_imgCellGrid.GetGridWidth( GXWINDOW::IFP_TOPLEFT), m_imgCellGrid.GetGridHeight( GXWINDOW::IFP_BOTTOMRIGHT), GXWINDOW::IFP_TOPLEFT);

		m_imgCellGrid.SetGridSize( cw - m_imgCellGrid.GetGridWidth( IFP_TOPLEFT) - m_imgCellGrid.GetGridWidth( IFP_BOTTOMRIGHT), max( 0, ch - m_imgCellGrid.GetGridHeight( IFP_TOPLEFT) - m_imgCellGrid.GetGridHeight( IFP_BOTTOMRIGHT)), IFP_CENTER);
		m_imgCellGrid.Draw( draw, l + cx, t + cy, 0, 0, cw, ch);

		if ( ch >= m_imgCellGrid.GetGridHeight( IFP_BOTTOMRIGHT))
		{
			if ( col == 0)
			{
				int w = min( cw, m_imgCellGrid.GetGridWidth( IFP_TOPLEFT));
				int h = min( ch, m_imgCellGrid.GetGridHeight( IFP_BOTTOMRIGHT));

				if ( row == GetRowCount() - 1)
					draw.DrawPicture( m_imgCellGrid.GetTileImage( IFP_TOPRIGHT), l + cx, t  + cy + m_imgCellGrid.GetGridHeight( IFP_TOPLEFT) + m_imgCellGrid.GetGridHeight( IFP_CENTER), w, h, w, h, -w, -h);
				else
					draw.DrawPicture( m_imgCellGrid.GetTileImage( IFP_BOTTOMRIGHT), l + cx, t  + cy + m_imgCellGrid.GetGridHeight( IFP_TOPLEFT) + m_imgCellGrid.GetGridHeight( IFP_CENTER), w, h, w, 0, -w, h);
			}
			else if ( row == GetRowCount() - 1)
			{
				int w = m_imgCellGrid.GetGridWidth( IFP_TOPLEFT);
				int h = m_imgCellGrid.GetGridHeight( IFP_BOTTOMRIGHT);
				draw.DrawPicture( m_imgCellGrid.GetTileImage( IFP_TOPLEFT), l + cx, t  + cy + m_imgCellGrid.GetGridHeight( IFP_TOPLEFT) + m_imgCellGrid.GetGridHeight( IFP_CENTER), w, h, 0, h, w, -h);

				if ( col == GetColumnCount() - 1)
					draw.DrawPicture( m_imgCellGrid.GetTileImage( IFP_TOPRIGHT), l + cx + m_imgCellGrid.GetGridWidth( IFP_TOPLEFT) + m_imgCellGrid.GetGridWidth( IFP_CENTER), t  + cy + m_imgCellGrid.GetGridHeight( IFP_TOPLEFT) + m_imgCellGrid.GetGridHeight( IFP_CENTER), w, h, 0, h, w, -h);
			}
		}

	}

	if ( 0 != GetColorAlpha( bkclr))
		draw.FillRect( l + cx, t + cy, l + cx + cw, t + cy + ch, bkclr);

	if( !n.empty())
	{
		int fh = m_tdm->GetFontHeight( m_nFont);
		int offset_x = 0;
		int offset_y = 0;

		if ( m_taFlag & TXA_CENTER)
			offset_x = max( 0, ( nCellWidth - m_tdm->GetStringPixelWidth( n.at( 0), m_nFont)) >> 1);
		else if ( m_taFlag & TXA_RIGHT)
			offset_x = max( 0, nCellWidth - m_tdm->GetStringPixelWidth( n.at( 0), m_nFont));

		if ( m_taFlag & TXA_MIDDLE)
			offset_y = max( 0, ( nCellHeight - m_tdm->GetFontHeightWithoutUnderline( m_nFont) * (int)n.size()) >> 1);
		else if ( m_taFlag & TXA_BOTTOM)
			offset_y = max( 0, nCellHeight - fh * (int)n.size());

		int index = 0;
		for ( INLINECLASS::IntVector::iterator it = n.begin(); it != n.end(); ++ it, ++ index)
		{
			if ( offset_y >= ch)
				break;
			m_tdm->Draw( n.at( index), l + offset_x, t + offset_y, max( 0, cx - offset_x), max( 0, cy - offset_y), max( 0, cw - offset_x), max( 0, ch - offset_y));
			offset_y += fh;
		}
	}
}

int CTextTableDataModule::GetColumnCount()
{
	return (int)m_grid->size();
}

int CTextTableDataModule::GetRowCount()
{
	if( m_grid->empty())
		return 0;
	else
		return (int)m_grid->front().rows.size();
}

void CTextTableDataModule::SetTextColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrNormal != clr || m_clrNormalEffect != clrEffect || m_uNormalEffect != type || ( bGradualColor && ( clrGradual != m_clrNormalGradual)))
	{
		m_grid->FreeTextNormal( *m_tdm);
		CSimpleTextable::SetTextColor( clr, type, clrEffect, bGradualColor, clrGradual);
	}
}

void CTextTableDataModule::SetTextSelectedColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrSelected != clr || m_clrSelectedEffect != clrEffect || m_uSelectedEffect != type || ( bGradualColor && ( clrGradual != m_clrSelectedGradual)))
	{
		m_grid->FreeTextSelect( *m_tdm);
		CSimpleTextable::SetTextSelectedColor( clr, type, clrEffect, bGradualColor, clrGradual);
	}
}

void CTextTableDataModule::SetTextMouseOnColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrMouseOn != clr || m_clrMouseOnEffect != clrEffect || m_uMouseOnEffect != type || ( bGradualColor && ( clrGradual != m_clrMouseOnGradual)))
	{
		m_grid->FreeTextMouseOn( *m_tdm);
		CSimpleTextable::SetTextMouseOnColor( clr, type, clrEffect, bGradualColor, clrGradual);
	}
}

void CTextTableDataModule::SetTextFont( int font)
{
	if( m_nFont == font)
		return;
	m_grid->FreeTextAll( *m_tdm);
	m_nFont = font;
}

void CTextTableDataModule::SetCellTextColor( int col, int row, COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	INLINECLASS::TextTableCell&  ttc = m_grid->GetCell( col, row);
	if ( ttc.n != -1)
		ttc.FreeTextNormal( *m_tdm);
	ttc.n = m_tdm->NewTextDraw( ttc.text.c_str(), m_nFont, clr, bGradualColor ? clrGradual : clr, 0, type, clrEffect);
}

void CTextTableDataModule::SetCellTextSelectedColor( int col, int row, COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	INLINECLASS::TextTableCell& ttc = m_grid->GetCell( col, row);
	if ( ttc.s != -1)
		ttc.FreeTextSelect( *m_tdm);
	ttc.s = m_tdm->NewTextDraw( ttc.text.c_str(), m_nFont, clr, bGradualColor ? clrGradual : clr, 0, type, clrEffect);
}

void CTextTableDataModule::SetCellTextMouseOnColor( int col, int row, COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	INLINECLASS::TextTableCell& ttc = m_grid->GetCell( col, row);
	if ( ttc.m != -1)
		ttc.FreeTextMouseOn( *m_tdm);
	ttc.m = m_tdm->NewTextDraw( ttc.text.c_str(), m_nFont, clr, bGradualColor ? clrGradual : clr, 0, type, clrEffect);
}

void CTextTableDataModule::SetRowTextColor( int row, COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	for ( int i = 0; i < GetColumnCount(); ++ i)
		SetCellTextColor( i, row, clr, type, clrEffect, bGradualColor, clrGradual);
}

void CTextTableDataModule::SetRowTextSelectedColor( int row, COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	for ( int i = 0; i < GetColumnCount(); ++ i)
		SetCellTextSelectedColor( i, row, clr, type, clrEffect, bGradualColor, clrGradual);
}

void CTextTableDataModule::SetRowTextMouseOnColor( int row, COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	for ( int i = 0; i < GetColumnCount(); ++ i)
		SetCellTextMouseOnColor( i, row, clr, type, clrEffect, bGradualColor, clrGradual);
}

int CTextTableDataModule::GetTableHeaderFont() const
{
	return m_nFontHeader;
}

void CTextTableDataModule::SetTableHeaderFont( int font)
{
	if( m_nFontHeader == font)
		return;
	m_grid->FreeTextHerader( *m_tdm);
	m_nFontHeader = font;
}

COLOR CTextTableDataModule::GetTableHeaderColor() const
{
	return m_clrHeader;
}

COLOR CTextTableDataModule::GetTableHeaderGradualColor() const
{
	return m_clrHeaderGradual;
}

FONTEFEECTFLAG CTextTableDataModule::GetTableHeaderEffect() const
{
	return m_fefHeader;
}

COLOR CTextTableDataModule::GetTableHeaderEffectColor() const
{
	return m_clrHeaderEffect;
}

void CTextTableDataModule::SetTableHeaderColor( COLOR clr, FONTEFEECTFLAG type, COLOR clrEffect, bool bGradualColor, COLOR clrGradual)
{
	if( m_clrHeader != clr || type != m_fefHeader || clrEffect != m_clrHeaderEffect || ( bGradualColor && ( clrGradual != m_clrHeaderGradual)))
	{
		m_grid->FreeTextHerader( *m_tdm);
		m_clrHeader = clr;
		m_clrHeaderGradual = bGradualColor ? clrGradual : clr;
		m_clrHeaderEffect = clrEffect;
		m_fefHeader = type;
	}
}

void CTextTableDataModule::InsertColumn( const wchar_t* szTitle)
{
	if( NULL == szTitle)
		szTitle = L"";

	INLINECLASS::TextTableColumn		ttc;
	ttc.strColName = szTitle;
	ttc.rows.resize( GetRowCount());
	
	m_grid->push_back( ttc);
}

void CTextTableDataModule::InsertColumn( int nAt, const wchar_t* szTitle)
{
	if( NULL == szTitle)
		szTitle = L"";

	INLINECLASS::TextTableColumn		ttc;
	ttc.strColName = szTitle;
	ttc.rows.resize( GetRowCount());

	m_grid->insert( m_grid->begin() + nAt, ttc);
}

const wchar_t* CTextTableDataModule::GetColumnText( int n) const
{
	if( n < 0 || n >= (int)m_grid->size())
		return NULL;
	return m_grid->at( n).strColName.c_str();
}

void CTextTableDataModule::RemoveColumn( int n)
{
	if( n < 0 || n >= (int)m_grid->size())
		return;
	INLINECLASS::TextTableColumn& ttc = m_grid->at( n);
	ttc.FreeTextAll( *m_tdm);
	m_grid->erase( m_grid->begin() + n);
}

void CTextTableDataModule::ClearColumns()
{
	m_grid->FreeTextAll( *m_tdm);
	m_grid->clear();
}

void CTextTableDataModule::FreeAllTextDraw()
{
	m_grid->FreeTextAll( *m_tdm);
}

void CTextTableDataModule::SetColumnText( int n, const wchar_t* szTitle)
{
	if( n < 0 || n >= (int)m_grid->size())
		return;
	if( NULL == szTitle)
		szTitle = L"";
	INLINECLASS::TextTableColumn& ttc = m_grid->at( n);
	ttc.strColName = szTitle;
	ttc.FreeTextHerader( *m_tdm);
}

void CTextTableDataModule::SetCellText( int col, int row, const wchar_t* szText)
{
	if( col < 0 || col >= (int)m_grid->size() )
		return;
	INLINECLASS::TextTableColumn& ttc = m_grid->at( col);
	if( row < 0 || row >= (int)ttc.rows.size() )
		return;
	INLINECLASS::TextTableCell& cell = ttc.rows.at( row);
	if( NULL == szText)
		cell.text.erase();
	else
		cell.text = szText;

	cell.FreeTextAll( *m_tdm);
}

const wchar_t* CTextTableDataModule::GetCellText( int col, int row)
{
	if( col < 0 || col >= (int)m_grid->size() )
		return NULL;
	INLINECLASS::TextTableColumn& ttc = m_grid->at( col);
	if( row < 0 || row >= (int)ttc.rows.size() )
		return NULL;
	INLINECLASS::TextTableCell& cell = ttc.rows.at( row);
	return cell.text.c_str();
}

int CTextTableDataModule::InsertRow( int n, unsigned int uiParam /* = 0 */)
{
	for( INLINECLASS::CTextTableDataGrid::iterator it = m_grid->begin(); it != m_grid->end(); ++ it )
	{
		INLINECLASS::TextTableColumn& ttc = *it;
		ttc.rows.insert( ttc.rows.begin() + n, INLINECLASS::TextTableCell());
	}
	m_params->insert( m_params->begin() + n, uiParam);
	m_longparams->insert( m_longparams->begin() + n, 0);
	return n;
}

int CTextTableDataModule::InsertRow()
{
	int n = GetRowCount();
	SetRowCount( n + 1);
	return n;
}

void CTextTableDataModule::SetRowCount( int n)
{
	for( INLINECLASS::CTextTableDataGrid::iterator it = m_grid->begin(); it != m_grid->end(); ++ it )
	{
		INLINECLASS::TextTableColumn& ttc = *it;
		int nCount = (int)ttc.rows.size();
		for( int i = n; i < nCount; i ++)
			ttc.GetCell( i).FreeTextAll( *m_tdm);
		ttc.rows.resize( n);
	}
	m_params->resize( n);
	m_longparams->resize( n);
}

void CTextTableDataModule::RemoveRow( int row)
{
	if ( row < 0 || row >= ( int)m_params->size())
		return;

	if ( CTable* p = GetParentTable())
	{
		if ( p->GetSelectRow() == row )
			p->SetSelectRow( -1);
	}
	
	for( INLINECLASS::CTextTableDataGrid::iterator it = m_grid->begin(); it != m_grid->end(); ++ it )
	{
		INLINECLASS::TextTableColumn& ttc = *it;
		ttc.GetCell( row).FreeTextAll( *m_tdm);
		ttc.rows.erase( ttc.rows.begin() + row);
	}
	m_params->erase( m_params->begin() + row);
	m_longparams->erase( m_longparams->begin() + row);
}

void CTextTableDataModule::ClearRows()
{
	for( INLINECLASS::CTextTableDataGrid::iterator it = m_grid->begin(); it != m_grid->end(); ++ it )
	{
		INLINECLASS::TextTableColumn& ttc = *it;
		ttc.FreeTextAll( *m_tdm);
		ttc.rows.clear();
	}
	m_params->clear();
	m_longparams->clear();
}

CTextDrawManager& CTextTableDataModule::GetTextDrawManager()
{
	return *m_tdm;
}

void CTextTableDataModule::SetRowParam( int row, unsigned int uiParam)
{
	if ( row < 0 || row >= ( int)m_params->size())
		return;

	(*m_params)[row] = uiParam;
}

unsigned int CTextTableDataModule::GetRowParam( int row) const
{
	if ( row < 0 || row >= ( int)m_params->size())
		return -1;

	return m_params->at( row);
}

void CTextTableDataModule::SetRowLongParam( int row, unsigned __int64 ulParam)
{
	if ( row < 0 || row >= (int)m_longparams->size())
		return;

	(*m_longparams)[row] = ulParam;
}

unsigned __int64 CTextTableDataModule::GetRowLongParam( int row) const
{
	if ( row < 0 || row >= ( int)m_longparams->size())
		return -1;

	return m_longparams->at( row);
}

void CTextTableDataModule::SetBackMouseOnColor(COLOR clr)
{
	m_clrRowMouseIn = clr;
}

void CTextTableDataModule::SetBackSelectedColor(COLOR clr)
{
	m_clrRowSelected = clr;
}

void CTextTableDataModule::SetTextAlign( TEXTALIGNFLAG flag)
{
	m_taFlag = flag;
}

TEXTALIGNFLAG CTextTableDataModule::GetTextAlign() const
{
	return m_taFlag;
}

void CTextTableDataModule::SetMultilines( bool bMultiline)
{
	m_bMultilines = bMultiline;
}

bool CTextTableDataModule::IsMultilines() const
{
	return m_bMultilines;
}

int CTextTableDataModule::GetCellGUID( int col, int row) const
{
	if( col < 0 || col >= (int)m_grid->size() )
		return 0;
	INLINECLASS::TextTableColumn& ttc = m_grid->at( col);
	if( row < 0 || row >= (int)ttc.rows.size() )
		return 0;
	INLINECLASS::TextTableCell& cell = ttc.rows.at( row);
	return cell.guid;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CImageTextTableDataModule::CImageTextTableDataModule():m_bShow(false) , m_nCellImageOffsetX(0) , m_rowImages(new INLINECLASS::CTableRowsImages())
{
}

CImageTextTableDataModule::~CImageTextTableDataModule()
{
	delete m_rowImages;   
}


void CImageTextTableDataModule::DrawHeaderComponent( int nIndex, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight)
{
	CTextTableDataModule::DrawHeaderComponent( nIndex, draw, l, t, cx, cy, cw, ch, nCellWidth, nCellHeight);
}

void CImageTextTableDataModule::DrawClientComponent( int col, int row, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight)
{
	CTextTableDataModule::DrawClientComponent( col, row, cct, draw, l, t, cx, cy, cw, ch, nCellWidth, nCellHeight);

	/*draw cell image*/

	CImagesFill* cif = GetCellImage(col,row);

	if(cif)
	{
		int imagew = cif->GetTileWidth();
		int imageh = cif->GetTileHeight();
		int offsetx = m_nCellImageOffsetX;
		int offsety = (nCellHeight - imageh) / 2;
		//imagew = min(imagew,cx + cw);
		//imageh = min(imageh,cy + ch);
		int sx = cx <= offsetx ? 0 : cx - offsetx;
		int sy = cy <= offsety ? 0 : cy - offsety;
		int sw = (imagew + offsetx) <= (cx + cw) ? imagew - sx : imagew - sx - ((imagew + offsetx) - (cx + cw));
		int sh = (imageh + offsety) <= (cy + ch) ? imageh - sy : imageh - sy - ((imageh + offsety) - (cy + ch));
		cif->Draw( draw, l + offsetx, t + offsety, sx, sy, sw, sh);

	}

}

void CImageTextTableDataModule::DrawMultilineClientComponent( int col, int row, CCTYPE cct, CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, int nCellWidth, int nCellHeight)
{
	CTextTableDataModule::DrawMultilineClientComponent( col, row, cct, draw, l, t, cx, cy, cw, ch, nCellWidth, nCellHeight);
}

void CImageTextTableDataModule::OnComponentShow()
{
	m_rowImages->RegisterImages();
	CTextTableDataModule::OnComponentShow();
	m_bShow = true;
}

void CImageTextTableDataModule::OnComponentHide()
{
	m_rowImages->FreeImages();
	CTextTableDataModule::OnComponentHide();
	m_bShow = false;
}

void CImageTextTableDataModule::RemoveRow( int row)
{
	if ( row < 0 || row >= GetRowCount())
		return;

	if ( GXWINDOW::CTable* parent = GetParentTable())
	{
		if ( parent->GetSelectRow() == row)
			parent->SetSelectRow( -1);
	}

	int cols = GetColumnCount();
	for ( int col = 0; col < cols; ++ col)
	{
		m_rowImages->Remove( GetCellGUID( col ,row));
	}
	return CTextTableDataModule::RemoveRow( row);
}

void CImageTextTableDataModule::ClearRows()
{
	CTextTableDataModule::ClearRows();
	if(m_bShow)
		m_rowImages->FreeImages();
	m_bShow = false;
	m_rowImages->Clear();

}

CImagesFill* CImageTextTableDataModule::SetCellImage( int col, int row)
{
	return m_rowImages->Insert( GetCellGUID( col, row));
}

CImagesFill* CImageTextTableDataModule::GetCellImage( int col, int row)
{
	return m_rowImages->GetAt( GetCellGUID( col ,row));
}

void CImageTextTableDataModule::RemoveCellImage( int col, int row)
{
	m_rowImages->Remove( GetCellGUID( col ,row));
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTableHeader::CTableHeader()
: m_thv( new INLINECLASS::CTableHeaderVector()), m_ptdm( NULL), m_nCurSizeCol( -1), m_pheaderimgs( new INLINECLASS::CTableHeaderImages()), m_bImageTitleAlignCenter( true)
{
	CComponent::SetSize( 100, 20);
}

CTableHeader::~CTableHeader()
{
	delete m_thv;
	delete m_pheaderimgs;
}

void CTableHeader::SetSize( int w, int h)
{
	CBKComponent::SetSize( w, h);
}

void CTableHeader::SetHeaderHeight( int n)
{
	if( 0 == n)
		n = 20;
	CBKComponent::SetSize( GetWidth(), n);
}

void CTableHeader::SetColumnCount( int n)
{
	CTableHeaderColumn	defthc;
	defthc.bSizable = true;
	defthc.nWidth = 100;
	defthc.nMax = 200;
	defthc.nMin = 50;

	m_thv->resize( n, defthc);
	
	FireTableHeaderColumnSizeChanged();
}

int CTableHeader::GetColumnCount() const
{
	return (int)m_thv->size();
}

CTableHeaderColumn& CTableHeader::GetHeaderColumn( int n)
{
	return m_thv->at( n);
}

int CTableHeader::ColumnIndexFromPoint( int x, int y)
{
	int cx = x - GetAbsPosX();
	int cy = y = GetAbsPosY();
	if( cx < 0 && cx >= GetWidth())
		return -1;
	if( cy < 0 && cy >= GetHeight())
		return -1;
	return DoGetColumnIndexFromCX( cx);
}

int CTableHeader::DoGetColumnIndexFromCX( int cx)
{
	int c = 0, nCount = (int)m_thv->size();
	for( int i = 0; i < nCount; i ++)
	{
		CTableHeaderColumn&	thc = m_thv->at( i);
		c += thc.nWidth;
		if( cx < c)
			return i;
	}
	return -1;
}

const CTableHeaderColumn& CTableHeader::GetHeaderColumn( int n) const
{
	return m_thv->at( n);
}

void CTableHeader::SetTableDataModule( CTableDataModule& tdm)
{
	m_ptdm = &tdm;
	if( m_ptdm)
		SetColumnCount( m_ptdm->GetColumnCount());
}

bool CTableHeader::OnLeftButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if( -1 != m_nCurSizeCol)
	{
		DoSizeHeaderColumn( x);
		m_nCurSizeCol = -1;
		ReleaseCapture();
	}
	return CBKComponent::OnLeftButtonUp( x, y, key);
}

bool CTableHeader::OnLeftButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if( -1 == m_nCurSizeCol)
	{
		int cx = x - GetAbsPosX();
		int c = 0, nCount = (int)m_thv->size();
		for( int i = 0; i < nCount; i ++)
		{
			CTableHeaderColumn&	thc = m_thv->at( i);
			c += thc.nWidth;
			if( cx < c)
			{
				if( thc.bSizable && ( c - cx < 4 ) )
				{
					m_nCurSizeCol = i;
					m_nLastSizeColX = x;
					SetCapture();
				}
				break;
			}
		}
	} // if( -1 == m_nCurSizeCol)
	return CBKComponent::OnLeftButtonDown( x, y, key);
}

bool CTableHeader::OnMouseMove( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if( -1 != m_nCurSizeCol)
		DoSizeHeaderColumn( x);
	return CBKComponent::OnMouseMove( x, y, key);
}

void CTableHeader::DoSizeHeaderColumn( int x)
{
	CTableHeaderColumn&	thc = m_thv->at( m_nCurSizeCol);

	int nold = thc.nWidth;
	thc.nWidth += ( x - m_nLastSizeColX);
	if( thc.nWidth < thc.nMin)
		thc.nWidth = thc.nMin;
	if( thc.nWidth > thc.nMax)
		thc.nWidth = thc.nMax;
	m_nLastSizeColX = x;

	if( nold != thc.nWidth)
		FireTableHeaderColumnSizeChanged();
}

void CTableHeader::FireTableHeaderColumnSizeChanged()
{
	int nCount = (int)m_thv->size();
	int nWidth = 0;
	for( int i = 0; i < nCount; i ++)
	{
		CTableHeaderColumn&	thc = m_thv->at( i);
		nWidth += thc.nWidth;
		AdjustColumnImagesPos( i);
	}
	SetSize( nWidth, GetHeight());
}

void CTableHeader::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CBKComponent::Draw( draw, l, t, cx, cy, cw, ch);

	int nx = 0;
	int nHeight = GetHeight();
	int nCount = (int)m_thv->size();
	int or = cx + cw;

	for( int i = 0; i < nCount; i ++)
	{
		CTableHeaderColumn&	thc = m_thv->at( i);
		int nr = nx + thc.nWidth;
		if( nx >= or)
			break;
		if( cx < nr)
		{
			cx -= nx;

			int nw = min( thc.nWidth - cx, cw);
			m_ptdm->DrawHeaderComponent( i, draw, l + nx, t, cx, cy, nw, ch, thc.nWidth, GetHeight());

			if ( CImagesFill* img = m_pheaderimgs->GetAt( i))
				img->Draw( draw, l + nx, t, cx, cy, nw, ch);

			cx = nr;
			cw -= nw;
		}
		nx = nr;
	} // for( int i = 0; i < nCount; i ++)
}

void CTableHeader::OnComponentShow()
{
	m_pheaderimgs->RegisterImages();
	CComponent::OnComponentShow();
}

void CTableHeader::OnComponentHide()
{
	m_pheaderimgs->FreeImages();
	CComponent::OnComponentHide();
}

void CTableHeader::SetHeaderCellImage( int index, const ImageParams& ip, int width, int height)
{
	if ( index < 0 || index >= GetColumnCount() || width <= 0 || height <= 0)
		return;

	int cy = ( GetHeight() - height) >> 1;
	cy = max( 0, cy);
	width = min( width, GetHeaderColumn( index).nWidth);
	height = min( height, GetHeight());

	CImagesFill& img = *m_pheaderimgs->Insert( index);
	img.SetTileImage( ip, IFP_CENTER);
	img.SetGridSize( 0, cy, IFP_TOPLEFT);
	img.SetGridSize( width, height, IFP_CENTER);
	img.SetGridSize( 0, 0, IFP_BOTTOMRIGHT);
	img.SetTileSize( width, height);

	AdjustColumnImagesPos( index);	
}
void CTableHeader::RemoveHeaderCellImage( int index)
{
	m_pheaderimgs->Remove( index);
}

void CTableHeader::AdjustColumnImagesPos( int index)
{
	if ( index < 0 || index >= GetColumnCount() || !m_bImageTitleAlignCenter)
		return;

	CImagesFill* pimg = m_pheaderimgs->GetAt( index);
	if ( !pimg)
		return;

	int cellwidth = GetHeaderColumn( index).nWidth;
	int cellheight = GetHeight();
	int imgwidth = pimg->GetTileWidth();
	int imgheight = pimg->GetTileHeight();

	int space_h = max( 0, ( cellheight - imgheight) >> 1);
	int space_w = max( 0, ( cellwidth - imgwidth) >> 1);

	pimg->SetGridSize( space_w, space_h, IFP_TOPLEFT);
}

void CTableHeader::SetHeaderCellImageAlignCenter( bool bCenter)
{
	m_bImageTitleAlignCenter = bCenter;
}

bool CTableHeader::IsHeaderCellImageAlignCenter() const
{
	return m_bImageTitleAlignCenter;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTableMouseOnChangeNotify::CTableMouseOnChangeNotify() {}
CTableMouseOnChangeNotify::~CTableMouseOnChangeNotify() {}

CTableSelectChangeNotify::CTableSelectChangeNotify() {}
CTableSelectChangeNotify::~CTableSelectChangeNotify() {}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTable::CTable()
	: m_ptdm( NULL), m_nSelectedRow( -1), m_nMouseOnRow( -1), m_nRowHeight( 20), m_pLastMouseIn( NULL),
	m_tmcns( new INLINECLASS::CTableMouseOnChangeNotifySet), m_tscns( new INLINECLASS::CTableSelectChangeNotifySet)
{
	m_th.SetParent( this);
	m_th.SetPos( 0, 0);
	m_th.ComponentSizeChangedNotifyAdd( *this);
}

CTable::~CTable()
{
	m_th.ComponentSizeChangedNotifyRemove( *this);
	delete m_tmcns;
	delete m_tscns;
}

CTableHeader& CTable::GetTableHeader()
{
	return m_th;
}

void CTable::SetTableDataModule( CTableDataModule& tdm)
{
	if( m_ptdm)
		m_ptdm->TableDataModuleDataChangeNotifyRemove( *this);
	m_ptdm = &tdm;
	m_th.SetTableDataModule( tdm);
	if( m_ptdm)
	{
		m_ptdm->TableDataModuleDataChangeNotifyAdd( *this);
		CheckTableComponentSize();
	}
	tdm.SetParentTable( this);
}

void CTable::SetTableHeaderHeight( int n)
{
	m_th.SetHeaderHeight( n);
}

void CTable::OnTableDataModuleDataChange( CTableDataModule& ldm)
{
	assert( m_ptdm);
	m_th.SetColumnCount( m_ptdm->GetColumnCount());
	m_nMouseOnRow = -1;
	SetSelectRow( -1);
	CheckTableComponentSize();
}

void CTable::OnComponentSizeChanged( CComponent& c, int nw, int nh, int ow, int oh)
{
	if( c.IsSameComponent( m_th))
		CheckTableComponentSize();
}

void CTable::SetSize( int w, int h)
{
	CBKComponent::SetSize( w, h);
}

void CTable::SetRowHeight( int n)
{
	m_nRowHeight = n;
	if( m_nRowHeight <= 0)
		m_nRowHeight = 20;
	CheckTableComponentSize();
}

int CTable::GetRowHeight()
{
	return m_nRowHeight;
}

void CTable::CheckTableComponentSize()
{
	assert( m_ptdm);

	int nWidth = m_th.GetWidth();
	int nHeight = m_ptdm->GetRowCount() * m_nRowHeight + m_th.GetHeight();
	CBKComponent::SetSize( nWidth, nHeight);
}

void CTable::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	if( NULL == m_ptdm)
		return;

	CBKComponent::Draw( draw, l + cx, t + cy, 0, 0, cw, ch);

	int nHeaderHeight = m_th.GetHeight();
	{
		int nh = min( nHeaderHeight, ch);
		m_th.SetPos( 0, cy);
		// draw tableheader
		m_th.Draw( draw, l, t + cy, cx, 0, cw, nh);	
		m_th.DoComponentAfterDrawNofity( draw, l, t + cy, cx, 0, cw, nh);
		ch -= nHeaderHeight;
	}

	if( ch <= 0 || m_nRowHeight <= 0)
		return;
	
	int nRowCount = m_ptdm->GetRowCount();
	if( 0 == nRowCount)
		return;
	int nColCount = m_ptdm->GetColumnCount();

	int nStart = cy / m_nRowHeight;
	if( nStart > nRowCount)
		return;
	int cb = cy + ch;
	int nStop = min( cb / m_nRowHeight, nRowCount - 1);
	cy += nHeaderHeight;
	cb += nHeaderHeight;

	struct InlineFunc
	{
		static inline CTableDataModule::CCTYPE GetCCType( bool nSel, bool bMouseOn)
		{
			if( nSel)
				return CTableDataModule::CCT_SELECT;
			else if( bMouseOn)
				return CTableDataModule::CCT_MOUSEON;
			else
				return CTableDataModule::CCT_NORMAL;
		}
	};

	COLOR	clrBorder[] = { m_ptdm->GetGridColor(), };
	int i, j, nx = 0, or = cx + cw;
	bool hadDrawFirstCol = false; // 标记是否已经真正的画了一列
	for( i = 0; i < nColCount; i ++)
	{
		CTableHeaderColumn&	thc = m_th.GetHeaderColumn( i);
		int nr = nx + thc.nWidth;
		if( nx >= or)
			break;
		if( cx < nr)
		{
			cx -= nx;
			int ny = nStart * m_nRowHeight + nHeaderHeight;
			int nb = ny + m_nRowHeight;
			int nw = min( cw, thc.nWidth - cx);

			{
				int nh = min( ch, nb - cy);
				if ( !hadDrawFirstCol)
				{
					if ( nStart == m_nSelectedRow)
						m_ifSelected.Draw( draw, l + nx, t + ny, cx, cy - ny, cw, nh);
					else if ( nStart == m_nMouseOnRow)
						m_ifMouseOn.Draw( draw, l + nx, t + ny, cx, cy - ny, cw, nh);
				}
				m_ptdm->DrawClientComponent( i, nStart, InlineFunc::GetCCType( nStart == m_nSelectedRow && !m_ifSelected.IsValid(), nStart == m_nMouseOnRow && !m_ifMouseOn.IsValid()), draw, l + nx, t + ny, cx, cy - ny, nw, nh, thc.nWidth, m_nRowHeight);
			}

			for( j = nStart + 1; j < nStop; j ++)
			{
				ny = nb;
				nb += m_nRowHeight;

				if ( !hadDrawFirstCol)
				{
					if ( j == m_nSelectedRow)
						m_ifSelected.Draw( draw, l + nx, t + ny, cx, 0, cw, m_nRowHeight);
					else if ( j == m_nMouseOnRow)
						m_ifMouseOn.Draw( draw, l + nx, t + ny, cx, 0, cw, m_nRowHeight);
				}

				m_ptdm->DrawClientComponent( i, j, InlineFunc::GetCCType( j == m_nSelectedRow && !m_ifSelected.IsValid(), j == m_nMouseOnRow && !m_ifMouseOn.IsValid()), draw, l + nx, t + ny, cx, 0, nw, m_nRowHeight, thc.nWidth, m_nRowHeight);
			}

			{
				ny = nb;
				nb += m_nRowHeight;
				int nh = min( cb - ny, m_nRowHeight);
				if( nh > 0)
				{
					if ( !hadDrawFirstCol)
					{
						if ( nStop == m_nSelectedRow)
							m_ifSelected.Draw( draw, l + nx, t + ny, cx, 0, cw, nh);
						else if ( nStop == m_nMouseOnRow)
							m_ifMouseOn.Draw( draw, l + nx, t + ny, cx, 0, cw, nh);
					}
					m_ptdm->DrawClientComponent( i, nStop, InlineFunc::GetCCType( nStop == m_nSelectedRow && !m_ifSelected.IsValid(), nStop == m_nMouseOnRow && !m_ifMouseOn.IsValid()), draw, l + nx, t + ny, cx, 0, nw, nh, thc.nWidth, m_nRowHeight);
				}
			}

			cx = nr;
			cw -= nw;

			hadDrawFirstCol = true;
		} // if( cx < nr)
		nx = nr;
	} // for( i = 0; i < nColCount; i ++)
}

bool CTable::OnLeftButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if( m_th.IsAbsPointIn( x, y))
		return m_th.OnLeftButtonUp( x, y, key);
	else if ( m_ptdm->OnLeftButtonUp( x, y, key, ColumnIndexFromPoint( x, y), RowIndexFromPoint( x, y))) // 为true表明datamodule中处理了
		return true;
	return CBKComponent::OnLeftButtonUp( x, y, key);
}

bool CTable::OnLeftButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if( m_th.IsAbsPointIn( x, y))
		return m_th.OnLeftButtonDown( x, y, key);
	else if ( m_ptdm->OnLeftButtonDown( x, y, key, ColumnIndexFromPoint( x, y), RowIndexFromPoint( x, y))) // 为true表明datamodule中处理了
		return true;
	SetSelectRow( RowIndexFromPoint( x, y));
	return CBKComponent::OnLeftButtonDown( x, y, key);
}

bool CTable::OnMouseMove( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	int		nNew = -1;
	if( m_th.IsAbsPointIn( x, y))
	{
		if( NULL == m_pLastMouseIn)
		{
			m_pLastMouseIn = &m_th;
			m_pLastMouseIn->OnMouseEnter();
		}
		m_th.OnMouseMove( x, y, key);

		INVALIDATE();
	}
	else 
	{
		if( m_pLastMouseIn)
		{
			m_pLastMouseIn->OnMouseLeave();
			m_pLastMouseIn = NULL;
		}
		CBKComponent::OnMouseMove( x, y, key);
		nNew = RowIndexFromPoint( x, y);

		m_ptdm->OnMouseMove( x, y, key, ColumnIndexFromPoint( x, y), nNew);
	}
	if( nNew != m_nMouseOnRow)
	{
		int nOld = m_nMouseOnRow;
		m_nMouseOnRow = nNew;
		m_tmcns->NotifyAll( *this, &CTableMouseOnChangeNotify::OnTableMouseOnChangeNotify, m_nMouseOnRow, nOld);

		INVALIDATE();
	}
	return true;
}

void CTable::OnMouseLeave()
{
	if ( IsViewOnly())
		return;

	if( m_pLastMouseIn)
	{
		m_pLastMouseIn->OnMouseLeave();
		m_pLastMouseIn = NULL;
	}
	if( -1 != m_nMouseOnRow)
	{
		int nOld = m_nMouseOnRow;
		m_nMouseOnRow = -1;
		m_tmcns->NotifyAll( *this, &CTableMouseOnChangeNotify::OnTableMouseOnChangeNotify, m_nMouseOnRow, nOld);
	}
	if (m_ptdm)
		m_ptdm->OnMouseLeave();

	CBKComponent::OnMouseLeave();
}

void CTable::OnComponentShow()
{
	m_th.OnComponentShow();
	m_ifMouseOn.RegisterImages();
	m_ifSelected.RegisterImages();
	if ( m_ptdm)
		m_ptdm->OnComponentShow();
	CBKComponent::OnComponentShow();
}

void CTable::OnComponentHide()
{
	m_th.OnComponentHide();
	m_ifMouseOn.FreeImages();
	m_ifSelected.FreeImages();
	if ( m_ptdm)
		m_ptdm->OnComponentHide();
	CBKComponent::OnComponentHide();
}

void CTable::ResetTableDataModule( CTableDataModule* tdm)
{
	if ( tdm && m_ptdm == tdm)
		m_ptdm = NULL;
}

int CTable::ColumnIndexFromPoint( int x, int y)
{
	int cx = x - GetAbsPosX();
	int cy = y - GetAbsPosY();
	if( cy < m_th.GetHeight() && cy >= GetHeight())
		return -1;
	return m_th.DoGetColumnIndexFromCX( cx);
}

int CTable::RowIndexFromPoint( int x, int y)
{
	if( m_nRowHeight <= 0 || NULL == m_ptdm)
		return -1;

	int cx = x - GetAbsPosX();
	int cy = y - GetAbsPosY();
	if( cx < 0 && cx >= GetWidth())
		return -1;
	if( cy < m_th.GetHeight() && cy >= GetHeight())
		return -1;
	cy -= m_th.GetHeight();
	int n = cy / m_nRowHeight;
	if( n < 0 || n >= m_ptdm->GetRowCount())
		return -1;
	else
		return n;
}

int CTable::GetSelectRow() const
{
	return m_nSelectedRow;
}

void CTable::SetSelectRow( int n)
{
	if( n == m_nSelectedRow)
		return;
	int nOld = m_nSelectedRow;
	m_nSelectedRow = n;
	m_tscns->NotifyAll( *this, &CTableSelectChangeNotify::OnTableSelectChangeNotify, m_nSelectedRow, nOld);
	
	INVALIDATE();
}

void CTable::TableMouseOnChangeNotifyAdd( CTableMouseOnChangeNotify& mcn)
{
	m_tmcns->Insert( &mcn);
}

void CTable::TableMouseOnChangeNotifyRemove( CTableMouseOnChangeNotify& mcn)
{
	m_tmcns->Remove( &mcn);
}

void CTable::TableSelectChangeNotifyAdd( CTableSelectChangeNotify& scn)
{
	m_tscns->Insert( &scn);
}

void CTable::TableSelectChangeNotifyRemove( CTableSelectChangeNotify& scn)
{
	m_tscns->Remove( &scn);
}

CImagesFill& CTable::GetRowMouseOnImage()
{
	return m_ifMouseOn;
}

CImagesFill& CTable::GetRowSelectedImage()
{
	return m_ifSelected;
}



} // namespace GXWINDOW { 