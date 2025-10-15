#include "gxwindowdef.h"

namespace GXWINDOW { 

namespace INLINECLASS { 

struct ImageParamsEx : public ImageParams
{
	bool	bxMirro;
	bool	byMirro;

	ImageParamsEx() : bxMirro( false), byMirro( false) 
	{
		nBitmap = -1;
		color = 0;
	}
	ImageParamsEx& operator=(const ImageParams& ip)
	{
		this->x = ip.x;
		this->y = ip.y;
		this->color = ip.color;
		this->nBitmap = ip.nBitmap;
		this->bxMirro = false;
		this->byMirro = false;
		return *this;
	}
};

class CStateImageMap : public std::map< unsigned int, ImageParamsEx>
{
public:
	CStateImageMap() {}
	virtual ~CStateImageMap() {}
};

class CStateTileImageMap : public std::map< unsigned int, CImagesFill*>
{
public:
	CStateTileImageMap() {}
	virtual ~CStateTileImageMap() {}
};

class CAnimateImageVector : public std::vector<ImageParams>
{
public:
	CAnimateImageVector() {}
	virtual ~CAnimateImageVector() {}
};

} // namespace INLINECLASS { 

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CImageable::CImageable() : m_pStateImages( new INLINECLASS::CStateImageMap())
{

}

CImageable::CImageable(unsigned int flag) : m_pStateImages( new INLINECLASS::CStateImageMap())
{
	if ( !flag)
		return;

	INLINECLASS::CStateImageMap& ips = *m_pStateImages;
	INLINECLASS::ImageParamsEx ip = INLINECLASS::ImageParamsEx();

	if ( NORMAL & flag)
		ips.insert( std::make_pair( NORMAL, ip));
	if ( MOUSEON & flag)
		ips.insert( std::make_pair( MOUSEON, ip));
	if ( MOUSEDOWN & flag)
		ips.insert( std::make_pair( MOUSEDOWN, ip));
	if ( DISABLE & flag)
		ips.insert( std::make_pair( DISABLE, ip));
	if ( SELECTED_NORMAL & flag)
		ips.insert( std::make_pair( SELECTED_NORMAL, ip));
	if ( SELECTED_MOUSEON & flag)
		ips.insert( std::make_pair( SELECTED_MOUSEON, ip));
	if ( USER_CUSTOM1 & flag)
		ips.insert( std::make_pair( USER_CUSTOM1, ip));
	if ( USER_CUSTOM2 & flag)
		ips.insert( std::make_pair( USER_CUSTOM2, ip));
}

CImageable::~CImageable()
{
	delete m_pStateImages;
}

const ImageParams* CImageable::GetImage(unsigned int flag) const
{
	INLINECLASS::CStateImageMap::const_iterator it = m_pStateImages->find( flag);
	if ( it == m_pStateImages->end())
	{
		std::pair<INLINECLASS::CStateImageMap::const_iterator, bool> insert_it = m_pStateImages->insert( std::make_pair( flag, INLINECLASS::ImageParamsEx()));
		it = insert_it.first;
	}
	return &( it->second);
}

bool CImageable::IsImageXMirro( unsigned int flag) const
{
	INLINECLASS::CStateImageMap::const_iterator it = m_pStateImages->find( flag);
	if ( it != m_pStateImages->end())
	{
		return it->second.bxMirro;
	}
	return false;
}

bool CImageable::IsImageYMirro( unsigned int flag) const
{
	INLINECLASS::CStateImageMap::const_iterator it = m_pStateImages->find( flag);
	if ( it != m_pStateImages->end())
	{
		return it->second.byMirro;
	}
	return false;
}

void CImageable::SetImage(const ImageParams& ip, unsigned int flag, bool bXMirro, bool bYMirro)
{
	INLINECLASS::ImageParamsEx& ipe  = (*m_pStateImages)[flag];
	ipe.nBitmap = ip.nBitmap;
	ipe.color = ip.color;
	ipe.x = ip.x;
	ipe.y = ip.y;
	ipe.bxMirro = bXMirro;
	ipe.byMirro = bYMirro;

	INVALIDATE();
}

void CImageable::RemoveImage(unsigned int flag)
{
	INLINECLASS::CStateImageMap::iterator it = m_pStateImages->find( flag);
	if ( it != m_pStateImages->end())
	{
		FreeImageByBitmapID( it->second.nBitmap);
		m_pStateImages->erase( it);
	}
	INVALIDATE();
}

bool CImageable::DrawImage(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, unsigned int flag) const
{
	INLINECLASS::CStateImageMap::iterator it = m_pStateImages->find( flag);
	if ( it != m_pStateImages->end() && it->second.nBitmap != -1)
	{
		draw.DrawPicture( it->second, l + cx, t + cy, cw, ch, cx, cy, cw, ch, it->second.bxMirro, it->second.byMirro);
		return true;
	}
	return false;
}

void CImageable::Clear()
{
	m_pStateImages->clear();
	INVALIDATE();
}

bool CImageable::IsValid() const
{
	if ( m_pStateImages->empty())
		return false;
	INLINECLASS::CStateImageMap::iterator it = m_pStateImages->find( IFP_CENTER);
	if ( it == m_pStateImages->end())
		return false;

	return it->second.nBitmap >= 0;
	//for ( INLINECLASS::CStateImageMap::iterator it = m_pStateImages->begin(), ite = m_pStateImages->end(); it != ite; ++ it)
	//{
	//	if ( it->second.nBitmap < 0)
	//		return false;
	//}
	//return true;
}

void CImageable::RegisterImageByBitmapID( int nBitmap)
{
	CImageTextureManager::GetInstance().InsertImage( nBitmap);
}

void CImageable::FreeImageByBitmapID( int nBitmap)
{
	CImageTextureManager::GetInstance().RemoveImage( nBitmap);
}

void CImageable::RegisterImage( unsigned int flag)
{
	INLINECLASS::CStateImageMap::iterator it = m_pStateImages->find( flag);
	if ( it != m_pStateImages->end())
	{
		RegisterImageByBitmapID( it->second.nBitmap);
	}
}

void CImageable::RegisterImages()
{
	for ( INLINECLASS::CStateImageMap::iterator it = m_pStateImages->begin(); it != m_pStateImages->end(); ++ it)
	{
		RegisterImageByBitmapID( it->second.nBitmap);
	}
}

void CImageable::FreeImage( unsigned int flag)
{
	INLINECLASS::CStateImageMap::iterator it = m_pStateImages->find( flag);
	if ( it != m_pStateImages->end())
	{
		FreeImageByBitmapID( it->second.nBitmap);
	}
}

void CImageable::FreeImages()
{
	for ( INLINECLASS::CStateImageMap::iterator it = m_pStateImages->begin(); it != m_pStateImages->end(); ++ it)
	{
		FreeImageByBitmapID( it->second.nBitmap);
	}
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CImagesFill::CImagesFill() :  m_bVTile( true), m_bHTile( true) 
{
	m_nGridW[0] = m_nGridW[1] = m_nGridW[2] = m_nGridH[0] = m_nGridH[1] = m_nGridH[2] = 0;
}

CImagesFill::~CImagesFill()
{
	m_tiles.Clear();
}


void CImagesFill::SetTileImage(const ImageParams& ip, TILEPOSITION position, bool bXMirro, bool bYMirro)
{
	m_tiles.SetImage( ip, position, bXMirro, bYMirro);
	INVALIDATE();
}

const ImageParams& CImagesFill::GetTileImage(TILEPOSITION position) const
{
	return *( m_tiles.GetImage( position));
}

void CImagesFill::SetTileSize(int w, int h)
{
	m_nTileW = w;
	m_nTileH = h;
	INVALIDATE();
}

void CImagesFill::SetGridSize(int w, int h, TILEPOSITION position)
{
	m_nGridW[ position % 3] = w;
	m_nGridH[ position / 3] = h;
	INVALIDATE();
}

int CImagesFill::GetGridWidth( TILEPOSITION position) const
{
	return m_nGridW[ position % 3];
}

int CImagesFill::GetGridHeight( TILEPOSITION position) const
{
	return m_nGridH[ position / 3];
}

int  CImagesFill::GetTileWidth() const
{
	return m_nTileW;
}

int  CImagesFill::GetTileHeight() const
{
	return m_nTileH;
}

bool CImagesFill::IsValid() const
{
	return m_tiles.IsValid();
}

void CImagesFill::SetFillMode(bool bVTile /* = true */, bool bHTile /* = true */)
{
	m_bVTile = bVTile;
	m_bHTile = bHTile;
	INVALIDATE();
}

bool CImagesFill::IsVerticalTile() const
{
	return m_bVTile;
}

bool CImagesFill::IsHorizontalTile() const
{
	return m_bHTile;
}

bool CImagesFill::GetVerticalFillMode() const
{
	return m_bVTile;
}

bool CImagesFill::GetHorizontalFillMode() const
{
	return m_bHTile;
}

void CImagesFill::CopyFrom( const CImagesFill& other)
{
	SetTileImage( other.GetTileImage( GXWINDOW::IFP_TOPLEFT), GXWINDOW::IFP_TOPLEFT);
	SetTileImage( other.GetTileImage( GXWINDOW::IFP_TOP), GXWINDOW::IFP_TOP);
	SetTileImage( other.GetTileImage( GXWINDOW::IFP_TOPRIGHT), GXWINDOW::IFP_TOPRIGHT);
	SetTileImage( other.GetTileImage( GXWINDOW::IFP_LEFT), GXWINDOW::IFP_LEFT);
	SetTileImage( other.GetTileImage( GXWINDOW::IFP_CENTER), GXWINDOW::IFP_CENTER);
	SetTileImage( other.GetTileImage( GXWINDOW::IFP_RIGHT), GXWINDOW::IFP_RIGHT);
	SetTileImage( other.GetTileImage( GXWINDOW::IFP_BOTTOMLEFT), GXWINDOW::IFP_BOTTOMLEFT);
	SetTileImage( other.GetTileImage( GXWINDOW::IFP_BOTTOM), GXWINDOW::IFP_BOTTOM);
	SetTileImage( other.GetTileImage( GXWINDOW::IFP_BOTTOMRIGHT), GXWINDOW::IFP_BOTTOMRIGHT);
	SetTileSize( other.GetTileWidth(), other.GetTileHeight());
	SetGridSize( other.GetGridWidth( GXWINDOW::IFP_TOPLEFT), other.GetGridHeight( GXWINDOW::IFP_TOPLEFT), GXWINDOW::IFP_TOPLEFT);
	SetGridSize( other.GetGridWidth( GXWINDOW::IFP_CENTER), other.GetGridHeight( GXWINDOW::IFP_CENTER), GXWINDOW::IFP_CENTER);
	SetGridSize( other.GetGridWidth( GXWINDOW::IFP_BOTTOMRIGHT), other.GetGridHeight( GXWINDOW::IFP_BOTTOMRIGHT), GXWINDOW::IFP_BOTTOMRIGHT);
	SetFillMode( other.IsVerticalTile(), other.IsHorizontalTile());
}
/*
int start_xtile = bHTile ? cx / TileW : 0;
int start_ytile = bVTile ? cy / TileH : 0;
int end_xtile = bHTile ? ( cx + cw) / TileW : 0;
int end_ytile = bVTile ? ( cy + ch) / TileH : 0;

float hscale = bHTile ? 1.0f : ( m_nGridW[1] == 0 ? 1 : (float)TileW * divscale / m_nGridW[1]);
float vscale = bVTile ? 1.0f : ( m_nGridW[1] == 0 ? 1 : (float)TileH * divscale / m_nGridH[1]);

int tmp_l = l;
for ( int i = start_xtile; i <= end_xtile && tmp_l < l + cw; i ++)
{
int tmp_t = t;
int tmp_w = bHTile ? ( i == start_xtile ? min( cw, TileW - cx % TileW) : ( i < end_xtile ? TileW : l + cx + cw - tmp_l)) : cw;
int tmp_x = bHTile ? ( i == start_xtile ? ( i < end_xtile ? TileW - tmp_w : 0) : 0) : 0;
int tmp_imgw = bHTile ? tmp_w : m_nGridW[1];
for ( int j = start_ytile; j <= end_ytile && tmp_t < t + ch; j ++)
{
int tmp_h = bVTile ? ( j == start_ytile ? min( ch, TileH- cy % TileH) : ( j < end_ytile ? TileH : t + cy + ch - tmp_t)) : ch;
int tmp_y = bVTile ? ( j == start_ytile ? ( j < end_ytile ? TileH - tmp_h : 0) : 0) : 0;
int tmp_imgh = bVTile ? tmp_h : m_nGridH[1];
draw.DrawPicture( ip, tmp_l, tmp_t, tmp_w, tmp_h, tmp_x, tmp_y, (int)( hscale * tmp_imgw * divscale + 0.5f), (int)( vscale * tmp_imgh * divscale + 0.5f), bXMirro, bYMirro);
tmp_t += tmp_h;
}
tmp_l += tmp_w;
}
*/

bool CImagesFill::FillTiles( CDraw& draw, const ImageParams& ip, int l, int t, int cw, int ch, int cx, int cy, int TileW, int TileH, bool bHTile, bool bVTile, bool bXMirro, bool bYMirro)
{
	if ( ip.nBitmap < 0 || cx < 0 || cy < 0 || TileW <= 0 || TileH <= 0)
		return false;

	int start_xtile = 0;
	int start_ytile = 0;
	int end_xtile = 0;
	int end_ytile = 0;

	float hscale = 1.0f;
	float vscale = 1.0f;

	int tmp_l = l;

	if ( bHTile)
	{
		start_xtile = cx / TileW;
		end_xtile = ( cx + cw) / TileW;

		if ( bVTile)
		{
			start_ytile = cy / TileH;
			end_ytile = ( cy + ch) / TileH;

			for ( int i = start_xtile; i <= end_xtile && tmp_l < l + cw; i ++)
			{
				int tmp_t = t;
				int tmp_w = ( i == start_xtile ? min( cw, TileW - cx % TileW) : ( i < end_xtile ? TileW : l + cw - tmp_l));
				int tmp_x = ( i == start_xtile ? ( i < end_xtile ? TileW - tmp_w : cx) : 0);
				int tmp_imgw = tmp_w;

				for ( int j = start_ytile; j <= end_ytile && tmp_t < t + ch; j ++)
				{
					int tmp_h = ( j == start_ytile ? min( ch, TileH- cy % TileH) : ( j < end_ytile ? TileH : t + ch - tmp_t));
					int tmp_y = ( j == start_ytile ? ( j < end_ytile ? TileH - tmp_h : cy) : 0);
					int tmp_imgh = tmp_h;
					draw.DrawPicture( ip, tmp_l, tmp_t, tmp_w, tmp_h, (int)( hscale * tmp_x + 0.5f), (int)( vscale * tmp_y + 0.5f), (int)( hscale * tmp_imgw + 0.5f), (int)( vscale * tmp_imgh + 0.5f), bXMirro, bYMirro);
					tmp_t += tmp_h;
				}
				tmp_l += tmp_w;
			}
		}
		else
		{
			vscale = m_nGridW[1] == 0 ? 1 : (float)TileH / m_nGridH[1];

			for ( int i = start_xtile; i <= end_xtile && tmp_l < l + cw; i ++)
			{
				int tmp_t = t;
				int tmp_w = ( i == start_xtile ? min( cw, TileW - cx % TileW) : ( i < end_xtile ? TileW : l + cw - tmp_l));
				int tmp_x = ( i == start_xtile ? ( i < end_xtile ? TileW - tmp_w : cx) : 0);
				int tmp_imgw = tmp_w;
				for ( int j = start_ytile; j <= end_ytile && tmp_t < t + ch; j ++)
				{
					int tmp_h = ch;
					int tmp_y = 0;
					int tmp_imgh = m_nGridH[1];
					draw.DrawPicture( ip, tmp_l, tmp_t, tmp_w, tmp_h, (int)( hscale * tmp_x + 0.5f), (int)( vscale * tmp_y + 0.5f), (int)( hscale * tmp_imgw + 0.5f), (int)( vscale * tmp_imgh + 0.5f), bXMirro, bYMirro);
					tmp_t += tmp_h;
				}
				tmp_l += tmp_w;
			}
		}
	}
	else
	{
		hscale = m_nGridW[1] == 0 ? 1 : (float)TileW / m_nGridW[1];

		if ( bVTile)
		{
			start_ytile = cy / TileH;
			end_ytile = ( cy + ch) / TileH;

			for ( int i = start_xtile; i <= end_xtile && tmp_l < l + cw; i ++)
			{
				int tmp_t = t;
				int tmp_w = cw;
				int tmp_x = 0;
				int tmp_imgw = m_nGridW[1];
				for ( int j = start_ytile; j <= end_ytile && tmp_t < t + ch; j ++)
				{
					int tmp_h = ( j == start_ytile ? min( ch, TileH- cy % TileH) : ( j < end_ytile ? TileH : t + ch - tmp_t));
					int tmp_y = ( j == start_ytile ? ( j < end_ytile ? TileH - tmp_h : cy) : 0);
					int tmp_imgh = tmp_h;
					draw.DrawPicture( ip, tmp_l, tmp_t, tmp_w, tmp_h, (int)( hscale * tmp_x + 0.5f), (int)( vscale * tmp_y + 0.5f), (int)( hscale * tmp_imgw + 0.5f), (int)( vscale * tmp_imgh + 0.5f), bXMirro, bYMirro);
					tmp_t += tmp_h;
				}
				tmp_l += tmp_w;
			}
		}
		else
		{
			vscale = m_nGridW[1] == 0 ? 1 : (float)TileH / m_nGridH[1];

			for ( int i = start_xtile; i <= end_xtile && tmp_l < l + cw; i ++)
			{
				int tmp_t = t;
				for ( int j = start_ytile; j <= end_ytile && tmp_t < t + ch; j ++)
				{
					draw.DrawPicture( ip, tmp_l, tmp_t, cw, ch, 0, 0, (int)( hscale * m_nGridW[1] + 0.5f), (int)( vscale * m_nGridH[1] + 0.5f), bXMirro, bYMirro);
					tmp_t += ch;
				}
				tmp_l += cw;
			}
		}
	}

	//if ( bHTile)
	//{
	//	start_xtile = cx / TileW;
	//	end_xtile = ( cx + cw) / TileW;
	//}
	//else
	//	hscale = m_nGridW[1] == 0 ? 1 : (float)TileW * divscale / m_nGridW[1];


	//if ( bVTile)
	//{
	//	start_ytile = cy / TileH;
	//	end_ytile = ( cy + ch) / TileH;
	//}
	//else
	//	vscale = m_nGridW[1] == 0 ? 1 : (float)TileH * divscale / m_nGridH[1];

	//int tmp_l = l;
	//for ( int i = start_xtile; i <= end_xtile && tmp_l < l + cw; i ++)
	//{
	//	int tmp_t = t;
	//	int tmp_w = bHTile ? ( i == start_xtile ? min( cw, TileW - cx % TileW) : ( i < end_xtile ? TileW : l + cx + cw - tmp_l)) : cw;
	//	int tmp_x = bHTile ? ( i == start_xtile ? ( i < end_xtile ? TileW - tmp_w : 0) : 0) : 0;
	//	int tmp_imgw = bHTile ? tmp_w : m_nGridW[1];
	//	for ( int j = start_ytile; j <= end_ytile && tmp_t < t + ch; j ++)
	//	{
	//		int tmp_h = bVTile ? ( j == start_ytile ? min( ch, TileH- cy % TileH) : ( j < end_ytile ? TileH : t + cy + ch - tmp_t)) : ch;
	//		int tmp_y = bVTile ? ( j == start_ytile ? ( j < end_ytile ? TileH - tmp_h : 0) : 0) : 0;
	//		int tmp_imgh = bVTile ? tmp_h : m_nGridH[1];
	//		draw.DrawPicture( ip, tmp_l, tmp_t, tmp_w, tmp_h, tmp_x, tmp_y, (int)( hscale * tmp_imgw * divscale + 0.5f), (int)( vscale * tmp_imgh * divscale + 0.5f), bXMirro, bYMirro);
	//		tmp_t += tmp_h;
	//	}
	//	tmp_l += tmp_w;
	//}

	return true;
}

void CImagesFill::Draw(CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	if ( cw <= 0 || ch <= 0)
		return;

	int cx_l = cx < m_nGridW[0] ? cx : m_nGridW[0];
	int cw_l = min( cw, m_nGridW[0] - cx_l);
	cw = max( 0, cw - cw_l);

	int cx_c = cw_l > 0 ? 0 : ( cx < m_nGridW[0] + m_nGridW[1] ? cx - m_nGridW[0] : m_nGridW[1]);
	int cw_c = min( cw, m_nGridW[1] - cx_c);
	cw = max( 0, cw - cw_c);

	int cx_r = cw_l + cw_c > 0 ? 0 : min( m_nGridW[2], cx - m_nGridW[0] - m_nGridW[1]);
	int cw_r = min( cw, m_nGridW[2] - cx_r);

	int cy_t = cy < m_nGridH[0] ? cy : m_nGridH[0];
	int ch_t = min( ch, m_nGridH[0] - cy_t);
	ch = max( 0, ch - ch_t);

	int cy_c = ch_t > 0 ? 0 : ( cy < m_nGridH[0] + m_nGridH[1] ? cy - m_nGridH[0] : m_nGridH[1]);
	int ch_c = min( ch, m_nGridH[1] - cy_c);
	ch = max( 0, ch - ch_c);

	int cy_b = ch_t + ch_c > 0 ? 0 : min( m_nGridH[2], cy - m_nGridH[0] - m_nGridH[1]);
	int ch_b = min( ch, m_nGridH[2] - cy_b);


	// 画第一行
	int current_x = l + cx;
	int current_y = t + cy;

	if ( const ImageParams* pip = m_tiles.GetImage( IFP_TOPLEFT))
	{
		if ( pip->nBitmap >= 0)
			draw.DrawPicture( *pip, current_x, current_y, cw_l, ch_t, cx_l, cy_t, cw_l, ch_t, m_tiles.IsImageXMirro( IFP_TOPLEFT), m_tiles.IsImageYMirro( IFP_TOPLEFT));
	}
	current_x += cw_l;

	if ( const ImageParams* pip = m_tiles.GetImage( IFP_TOP))
		FillTiles( draw, *pip, current_x, current_y, cw_c, ch_t, cx_c, cy_t, m_nTileW, m_nGridH[0], m_bHTile, true, m_tiles.IsImageXMirro( IFP_TOP), m_tiles.IsImageYMirro( IFP_TOP));
	current_x += cw_c;

	if ( const ImageParams* pip = m_tiles.GetImage( IFP_TOPRIGHT))
	{
		if ( pip->nBitmap >= 0)
			draw.DrawPicture( *pip, current_x, current_y, cw_r, ch_t, cx_r, cy_t, cw_r, ch_t, m_tiles.IsImageXMirro( IFP_TOPRIGHT), m_tiles.IsImageYMirro( IFP_TOPRIGHT));
	}

	// 画第二行
	current_x = l + cx;
	current_y += ch_t;

	if ( const ImageParams* pip = m_tiles.GetImage( IFP_LEFT))
		FillTiles( draw, *pip, current_x, current_y, cw_l, ch_c, cx_l, cy_c, m_nGridW[0], m_nTileH, true, m_bVTile, m_tiles.IsImageXMirro( IFP_LEFT), m_tiles.IsImageYMirro( IFP_LEFT));
	current_x += cw_l;

	if ( const ImageParams* pip = m_tiles.GetImage( IFP_CENTER))
		FillTiles( draw, *pip, current_x, current_y, cw_c, ch_c, cx_c, cy_c, m_nTileW, m_nTileH, m_bHTile, m_bVTile, m_tiles.IsImageXMirro( IFP_CENTER), m_tiles.IsImageYMirro( IFP_CENTER));
	current_x += cw_c;

	if ( const ImageParams* pip = m_tiles.GetImage( IFP_RIGHT))
		FillTiles( draw, *pip, current_x, current_y, cw_r, ch_c, cx_r, cy_c, m_nGridW[2], m_nTileH, true, m_bVTile, m_tiles.IsImageXMirro( IFP_RIGHT), m_tiles.IsImageYMirro( IFP_RIGHT));

	// 画第三行
	current_x = l + cx;
	current_y += ch_c;

	if ( const ImageParams* pip = m_tiles.GetImage( IFP_BOTTOMLEFT))
	{
		if ( pip->nBitmap >= 0)
			draw.DrawPicture( *pip, current_x, current_y, cw_l, ch_b, cx_l, cy_b, cw_l, ch_b, m_tiles.IsImageXMirro( IFP_BOTTOMLEFT), m_tiles.IsImageYMirro( IFP_BOTTOMLEFT));
	}
	current_x += cw_l;

	if ( const ImageParams* pip = m_tiles.GetImage( IFP_BOTTOM))
		FillTiles( draw, *pip, current_x, current_y, cw_c, ch_b, cx_c, cy_b, m_nTileW, m_nGridH[2], m_bHTile, true, m_tiles.IsImageXMirro( IFP_BOTTOM), m_tiles.IsImageYMirro( IFP_BOTTOM));
	current_x += cw_c;

	if ( const ImageParams* pip = m_tiles.GetImage( IFP_BOTTOMRIGHT))
	{
		if ( pip->nBitmap >= 0)
			draw.DrawPicture( *pip, current_x, current_y, cw_r, ch_b, cx_r, cy_b, cw_r, ch_b, m_tiles.IsImageXMirro( IFP_BOTTOMRIGHT), m_tiles.IsImageYMirro( IFP_BOTTOMRIGHT));
	}
}

void CImagesFill::RegisterImage( TILEPOSITION position)
{
	m_tiles.RegisterImage( position);
}

void CImagesFill::RegisterImages()
{
	m_tiles.RegisterImages();
}

void CImagesFill::FreeImage( TILEPOSITION position)
{
	m_tiles.FreeImage( position);
}

void CImagesFill::FreeImages()
{
	m_tiles.FreeImages();
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTileImageAble::CTileImageAble() : m_pStateTileImages( new INLINECLASS::CStateTileImageMap())
{
}

CTileImageAble::~CTileImageAble()
{
	Clear();
	delete m_pStateTileImages;
}

bool CTileImageAble::IsContainImage( unsigned int flag) const
{
	return m_pStateTileImages->find( flag) != m_pStateTileImages->end();
}

CImagesFill& CTileImageAble::GetTileImage( unsigned int flag)
{
	INLINECLASS::CStateTileImageMap::iterator it = m_pStateTileImages->find( flag);
	if ( it == m_pStateTileImages->end())
	{
		std::pair<INLINECLASS::CStateTileImageMap::iterator, bool> insert_image = m_pStateTileImages->insert( std::make_pair( flag, new CImagesFill()));
		it = insert_image.first;
	}
	return *( it->second);
}

void CTileImageAble::RemoveImage( unsigned int flag)
{
	INLINECLASS::CStateTileImageMap::iterator it = m_pStateTileImages->find( flag);
	if ( it != m_pStateTileImages->end())
	{
		delete it->second;
		m_pStateTileImages->erase( it);
	}
}

void CTileImageAble::Clear()
{
	for ( INLINECLASS::CStateTileImageMap::iterator it = m_pStateTileImages->begin(); it != m_pStateTileImages->end(); ++ it)
	{
		it->second->FreeImages();
		delete it->second;
	}
	m_pStateTileImages->clear();
}

bool CTileImageAble::DrawImage( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch, unsigned int flag) const
{
	INLINECLASS::CStateTileImageMap::iterator it = m_pStateTileImages->find( flag);
	if ( it != m_pStateTileImages->end())
	{
		it->second->Draw( draw, l, t, cx, cy, cw, ch);
		return true;
	}
	return false;
}

void CTileImageAble::RegisterImage( unsigned int flag)
{
	INLINECLASS::CStateTileImageMap::iterator it = m_pStateTileImages->find( flag);
	if ( it != m_pStateTileImages->end())
	{
		it->second->RegisterImages();
	}	
}

void CTileImageAble::RegisterImages()
{
	for ( INLINECLASS::CStateTileImageMap::iterator it = m_pStateTileImages->begin(); it != m_pStateTileImages->end(); ++ it)
	{
		it->second->RegisterImages();
	}
}

void CTileImageAble::FreeImage( unsigned int flag)
{
	INLINECLASS::CStateTileImageMap::iterator it = m_pStateTileImages->find( flag);
	if ( it != m_pStateTileImages->end())
	{
		it->second->FreeImages();
	}	
}

void CTileImageAble::FreeImages()
{
	for ( INLINECLASS::CStateTileImageMap::iterator it = m_pStateTileImages->begin(); it != m_pStateTileImages->end(); ++ it)
	{
		it->second->FreeImages();
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CImageComponent::CImageComponent()
: m_bMouseIn( false)
{
}

CImageComponent::~CImageComponent()
{
}

void CImageComponent::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CComponent::Draw( draw, l, t, cx, cy, cw, ch);
	if ( !IsEnable() && 
		( m_stateip.DrawImage( draw, l, t, cx, cy, cw ,ch, CImageable::DISABLE) || 
			m_stateip.DrawImage( draw, l, t, cx, cy, cw ,ch, CImageable::NORMAL) ) )
		return;
	
	if ( m_bMouseIn && m_stateip.DrawImage( draw, l, t, cx, cy, cw ,ch, CImageable::MOUSEON))
		return;
		
	m_stateip.DrawImage( draw, l, t, cx, cy, cw, ch, CImageable::NORMAL);
}

void CImageComponent::OnMouseEnter()
{
	if ( IsViewOnly())
		return;

	m_bMouseIn = true;
	CComponent::OnMouseEnter();
}

void CImageComponent::OnMouseLeave()
{
	if ( IsViewOnly())
		return;

	m_bMouseIn = false;
	CComponent::OnMouseLeave();
}

void CImageComponent::OnComponentShow()
{
	m_stateip.RegisterImages();
	CComponent::OnComponentShow();
}

void CImageComponent::OnComponentHide()
{
	m_stateip.FreeImages();
	CComponent::OnComponentHide();
}

void CImageComponent::SetViewAsMouseIn( bool bin)
{
	m_bMouseIn = bin;
}

void CImageComponent::SetComponentImageNormal(const ImageParams& ip)
{
	m_stateip.SetImage( ip, CImageable::NORMAL);
}

const ImageParams& CImageComponent::GetComponentImageNormal() const
{
	return *m_stateip.GetImage( CImageable::NORMAL);
}

void CImageComponent::RemoveComponentImageNormal()
{
	m_stateip.RemoveImage( CImageable::NORMAL);
}

void CImageComponent::SetComponentImageMouseIn(const ImageParams& ip)
{
	m_stateip.SetImage( ip, CImageable::MOUSEON);
}

const ImageParams& CImageComponent::GetComponentImageMouseIn() const
{
	return *m_stateip.GetImage( CImageable::MOUSEON);
}

void CImageComponent::RemoveComponentImageMouseIn()
{
	m_stateip.RemoveImage( CImageable::MOUSEON);
}

void CImageComponent::SetComponentImageDisable(const ImageParams& ip)
{
	m_stateip.SetImage( ip, CImageable::DISABLE);
}

const ImageParams& CImageComponent::GetComponentImageDisable() const
{
	return *m_stateip.GetImage( CImageable::DISABLE);
}

void CImageComponent::RemoveComponentImageDisable()
{
	m_stateip.RemoveImage( CImageable::DISABLE);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CAbstractAnimateImageComponent::CAbstractAnimateImageComponent()
: m_nInterval( 500)
{
	m_nCurrentIndex = - 1;
	m_bStopAtFrame = false;
	CTimer::NewTimer( *this, m_nInterval, m_nTimeID);
}

CAbstractAnimateImageComponent::~CAbstractAnimateImageComponent()
{
}

void CAbstractAnimateImageComponent::SetInterval( int ms)
{
	m_nInterval = ms;
}

int CAbstractAnimateImageComponent::GetInterval() const
{
	return m_nInterval;
}

int CAbstractAnimateImageComponent::GetCurrentIndex() const
{
	return m_nCurrentIndex;
}

void CAbstractAnimateImageComponent::OnTimer( const TIMERID& nid)
{
	if( nid == m_nTimeID)
	{
		CTimer::ResetTimer( *this, m_nTimeID, m_nInterval);

		int nCount = GetCount();
		if( nCount <= 0 || !this->GetParent())
		{
			m_nCurrentIndex = -1;
			return;
		}		

		if ( m_bStopAtFrame)
			return;

		m_nCurrentIndex ++;
		if( m_nCurrentIndex < 0 || m_nCurrentIndex >= nCount)
			m_nCurrentIndex = 0;

		INVALIDATE();
	}
}

void CAbstractAnimateImageComponent::StopAt(int frame)
{
	if ( frame < 0 || frame >= GetCount())
	{
		m_bStopAtFrame = false;
		return;
	}

	m_bStopAtFrame = true;
	m_nCurrentIndex = frame;
	INVALIDATE();
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CAnimateImageComponent::CAnimateImageComponent()
	: m_aiv( new INLINECLASS::CAnimateImageVector())
{
}

CAnimateImageComponent::~CAnimateImageComponent()
{
	delete m_aiv;
}

int CAnimateImageComponent::GetCount() const
{
	return (int)m_aiv->size();
}

const ImageParams& CAnimateImageComponent::GetAt( int nIndex) const
{
	assert( nIndex >= 0 && nIndex < GetCount());
	return m_aiv->at( nIndex);
}

ImageParams& CAnimateImageComponent::GetAt( int nIndex)
{
	assert( nIndex >= 0 && nIndex < GetCount());
	return m_aiv->at( nIndex);
}

void CAnimateImageComponent::RemoveAt( int nIndex)
{
	if( nIndex >= 0 && nIndex < GetCount())
		m_aiv->erase( m_aiv->begin() + nIndex);
}

void CAnimateImageComponent::RemoveAll()
{
	m_aiv->clear();
}

void CAnimateImageComponent::Insert( const ImageParams& ip)
{
	m_aiv->push_back( ip);
}

void CAnimateImageComponent::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	int nCurrentIndex = GetCurrentIndex();
	if( nCurrentIndex < 0 || nCurrentIndex >= GetCount())
		return;
	ImageParams& ip = GetAt( nCurrentIndex);
	draw.DrawPicture( ip, l + cx, t + cy, cw, ch, cx, cy, cw, ch);
}

void CAnimateImageComponent::OnComponentShow()
{
	for ( INLINECLASS::CAnimateImageVector::iterator it = m_aiv->begin(); it != m_aiv->end(); ++ it)
	{
		CImageTextureManager::GetInstance().InsertImage( (*it).nBitmap);
	}
	CAbstractAnimateImageComponent::OnComponentShow();
}

void CAnimateImageComponent::OnComponentHide()
{
	for ( INLINECLASS::CAnimateImageVector::iterator it = m_aiv->begin(); it != m_aiv->end(); ++ it)
	{
		CImageTextureManager::GetInstance().RemoveImage( (*it).nBitmap);
	}
	CAbstractAnimateImageComponent::OnComponentHide();
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CTileImageComponent::CTileImageComponent() : m_bDrawDown( false), m_bMouseIn( false)
{
}

CTileImageComponent::~CTileImageComponent()
{
}

bool CTileImageComponent::IsMouseDown() const
{
	return m_bDrawDown;
}

bool CTileImageComponent::IsMouseIn() const
{
	return m_bMouseIn;
}

void CTileImageComponent::Draw( CDraw& draw, int l, int t, int cx, int cy, int cw, int ch)
{
	CComponent::Draw( draw, l, t, cx, cy, cw, ch);
	if( IsEnable() && m_bDrawDown && m_bMouseIn)
	{
		m_ifMouseDown.Draw( draw, l, t, cx, cy, cw, ch);
	}
	else
	{
		if ( m_bMouseIn )
			m_ifMouseIn.Draw( draw, l, t, cx, cy, cw, ch);
		else
			m_ifNormal.Draw( draw, l, t, cx, cy, cw, ch);
	}
}

void CTileImageComponent::OnMouseEnter()
{
	if ( IsViewOnly())
		return;

	if( IsEnable())
		m_bMouseIn = true;
	CComponent::OnMouseEnter();
}

void CTileImageComponent::OnMouseLeave()
{
	if ( IsViewOnly())
		return;

	m_bMouseIn = false;
	m_bDrawDown = false;
	CComponent::OnMouseLeave();
}

void CTileImageComponent::OnComponentShow()
{
	m_ifNormal.RegisterImages();
	m_ifMouseIn.RegisterImages();
	m_ifMouseDown.RegisterImages();
	CComponent::OnComponentShow();
}

void CTileImageComponent::OnComponentHide()
{
	m_ifNormal.FreeImages();
	m_ifMouseIn.FreeImages();
	m_ifMouseDown.FreeImages();
	CComponent::OnComponentHide();
}

bool CTileImageComponent::OnLeftButtonUp( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	m_bDrawDown = false;
	m_bMouseIn = IsAbsPointIn( x, y);
	if ( IsEnable())
	{
		INVALIDATE();
	}
	return CComponent::OnLeftButtonUp( x, y, key);
}

bool CTileImageComponent::OnLeftButtonDown( int x, int y, int key)
{
	if ( IsViewOnly())
		return false;

	if( IsEnable())
	{
		m_bDrawDown = true;
		m_bMouseIn = true;
		INVALIDATE();
	}
	return CComponent::OnLeftButtonDown( x, y, key);
}

void CTileImageComponent::SetViewAsMouseIn( bool bIn)
{
	m_bMouseIn = bIn;
	INVALIDATE();
}

void CTileImageComponent::SetViewAsMouseDonw( bool bDown)
{
	m_bDrawDown = bDown;
	INVALIDATE();
}

CImagesFill& CTileImageComponent::GetTileImageNormal()
{
	return m_ifNormal;
}

CImagesFill& CTileImageComponent::GetTileImageMouseIn()
{
	return m_ifMouseIn;
}

CImagesFill& CTileImageComponent::GetTileImageMouseDown()
{
	return m_ifMouseDown;
}

} //namespace GXWINDOW { 