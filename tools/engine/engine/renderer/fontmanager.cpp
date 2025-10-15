#include "stdafx.h"
#include "MinMax.h"
#include "renderer.h"
#include "fonttexture.h"
#include "textblock.h"
#include "../common/fileiomanager.h"
#include "fontmanager.h"
#include "../common/log.h"
#include "..\..\pfs\pfs2.h"

namespace Nuclear
{

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CBitmapCache::CBitmapCache() : m_size( NULL)
{
	
}

CBitmapCache::CBitmapCache( FT_Size size) : m_size( size)
{
	Initialize();
}

CBitmapCache::~CBitmapCache()
{
	Release();
}

void CBitmapCache::ActiveFT_Size()
{
	FT_Activate_Size( m_size);
}

FT_Face CBitmapCache::GetFT_Face()
{
	return m_size->face;
}

int CBitmapCache::GetCharWidth( TCHAR ch)
{
	BITMAP_MAP::iterator it = m_bitmaps.find( ch);
	if ( it != m_bitmaps.end())
	{
		++ it->second.life;
		return it->second.width;
	}

	if ( BitmapWrap* pbw = CreateCharBitmap( ch))
		return pbw->width;

	return 0;
}

int CBitmapCache::GetFontHeight()
{
	return m_height;
}

int CBitmapCache::GetFontUnderlinePosition()
{
	return m_underlinePosition;
}

int CBitmapCache::GetFontUnderlineThickness()
{
	return m_underlineThickness;
}

void* CBitmapCache::GetBitmap( TCHAR ch)
{
	BITMAP_MAP::iterator it = m_bitmaps.find( ch);
	if ( it != m_bitmaps.end())
	{
		it->second.life ++;
		return (void*)it->second.bitmap;
	}

	if ( BitmapWrap* pbw = CreateCharBitmap( ch))
		return (void*)pbw->bitmap;

	return NULL;
}

void CBitmapCache::Release()
{
	for ( BITMAP_MAP::iterator i = m_bitmaps.begin(); i != m_bitmaps.end(); ++i)
	{
		BitmapWrap& bw = i->second;
		delete[] bw.bitmap;
	}
	m_bitmaps.clear();
}

int CBitmapCache::GetBaseLine()
{
	int nBaseLine = 0;
	const wchar_t szTest[] = L"abcdefghijklmnopqrstuvwxyz1234567890_这是一个测试获取基准线的字符串";

	ActiveFT_Size();

	FT_Face& face = m_size->face;

	for ( int i = 0; i < ( int)wcslen( szTest); i ++)
	{
		// 装载字形图像到字形槽
		if ( FT_Load_Char( face, szTest[i], FT_LOAD_RENDER ))
			continue;

		FT_Glyph glyph;
		FT_Get_Glyph( face->glyph, &glyph );

		FT_BBox  bbox;
		FT_Glyph_Get_CBox( glyph , FT_GLYPH_BBOX_PIXELS , &bbox );

		if ( nBaseLine < bbox.yMax)
			nBaseLine = bbox.yMax;
	}

	return nBaseLine;
}

void CBitmapCache::Initialize()
{
	ActiveFT_Size();

	FT_Face& face = m_size->face;
	//m_height = m_size->metrics.y_ppem; 
	m_height = ( int)( 0.5f + ( float)m_size->metrics.y_ppem * face->height / face->units_per_EM);
	m_baselinePosition = ( int)( 0.5f + ( float)m_size->metrics.y_ppem * face->ascender / face->units_per_EM);
	//m_baselinePosition = GetBaseLine();
	m_underlinePosition = m_baselinePosition - ( int)( 0.5f + ( float)m_size->metrics.y_ppem * face->underline_position / face->units_per_EM);
	m_underlineThickness = max( 1, ( int)( 0.5f + ( float)m_size->metrics.y_ppem * face->underline_thickness / face->units_per_EM));
}

unsigned char GetCharPixel_Gray( const unsigned char* buffer, int pitch, int width, int row, int col)
{
	return buffer[ row * width + col];
}

unsigned char GetCharPixel_Bit( const unsigned char* buffer, int pitch, int width, int row, int col)
{
	return buffer[ row * pitch + ( col >> 3)] & (0x80 >> ( col % 8)) ? 255 : 0;
}

CBitmapCache::BitmapWrap* CBitmapCache::CreateCharBitmap( TCHAR ch)
{
	ActiveFT_Size();

	FT_Face& face = m_size->face;

	// 装载字形图像到字形槽
	if ( FT_Load_Char( face, ch, FT_LOAD_RENDER ))
		return NULL;

	FT_GlyphSlot slot = face->glyph;
	FT_Bitmap& bitmap = slot->bitmap;

	int w = slot->metrics.horiAdvance >> 6;
	int h = m_height;

	std::pair<BITMAP_MAP::iterator, bool> istate = m_bitmaps.insert( std::make_pair( ch, BitmapWrap()));
	BitmapWrap& bitmapWrap = istate.first->second;
	bitmapWrap.bitmap = new PixelData[ w * h];
	bitmapWrap.width = w;
	bitmapWrap.height = h;
	bitmapWrap.life = 1;

	memset( bitmapWrap.bitmap, 0, w * h * sizeof( PixelData));
	int posX = max( 0, slot->bitmap_left);
	int posY = max( 0, m_baselinePosition - slot->bitmap_top);

	unsigned char ( *GetCharPixel)( const unsigned char*, int, int, int, int);
	GetCharPixel = FT_PIXEL_MODE_MONO == bitmap.pixel_mode ? &GetCharPixel_Bit : &GetCharPixel_Gray;

	for ( int i = 0; i < bitmap.rows; i ++)
	{
		for ( int j = 0; j < bitmap.width; j ++)
		{
			if ( posX + j >= w || posY + i >= h)
				continue;

			unsigned char srcGray = GetCharPixel( bitmap.buffer, bitmap.pitch, bitmap.width, i, j);
			bitmapWrap.bitmap[ ( posY + i) * w + posX + j] = ( srcGray << 24) | 0x00ffffff;	// 将字形图像转换为带透明度的白色图像, 实际文字颜色由渲染时进行顶点颜色混合
		}
	}

	return &bitmapWrap;
}

void CBitmapCache::gc()
{
	for ( BITMAP_MAP::iterator i = m_bitmaps.begin(); i != m_bitmaps.end();)
	{
		BitmapWrap& bw = i->second;
		if ( 0 == bw.life)
		{
			delete[] bw.bitmap;
			i = m_bitmaps.erase( i);
		}
		else
		{
			-- bw.life;
			++ i;
		}
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


std::string WStringToString( std::wstring& wstr)
{
	int nCount = WideCharToMultiByte( CP_ACP, 0, wstr.c_str(), -1, NULL, NULL, NULL, NULL);
	char* szBuffer = new char[nCount];

	WideCharToMultiByte( CP_ACP, 0, wstr.c_str(), -1, szBuffer, nCount, NULL, NULL);
	std::string str( szBuffer, nCount - 1);
	delete[] szBuffer;
	return str;
}


CFontManager::CFontManager() : m_pFontTexturePool( NULL), m_pRenderer( NULL), m_pFileIOMan( NULL), m_ftLibrary( NULL)
{

}

CFontManager::~CFontManager()
{
	Release();
	delete m_pFontTexturePool;

	for (FontFileCatche::iterator it = m_fontFileCatch.begin(), ite = m_fontFileCatch.end(); it != ite; ++ it)
	{
		delete[] it->second;
	}
	m_fontFileCatch.clear();
}

void CFontManager::Init( Renderer* pRenderer, CFileIOManager *pFileIOMan)
{
	m_pRenderer = pRenderer;
	m_pFileIOMan = pFileIOMan;
	m_pFontTexturePool = new CFontTexPool( pRenderer);
	InitFreetype();
}

bool CFontManager::InitFreetype()
{
	if ( FT_Init_FreeType( &m_ftLibrary ))
		return false;

	return true;
}

void CFontManager::Release()
{
	for ( TEXTBLOCK_MAP::iterator it = m_textblocks.begin(); it != m_textblocks.end(); ++ it)
	{
		it->second->Release();
		delete it->second;
	}
	m_textblocks.clear();

	for ( BITMAPCACHE_MAP::iterator it = m_caches.begin(); it != m_caches.end(); ++ it)
	{
		it->second.Release();
	}
	m_caches.clear();

	for ( FACE_MAP::iterator it = m_faces.begin(); it != m_faces.end(); ++ it)
	{
		FT_Done_Face( it->second);
	}
	m_faces.clear();

	if ( m_ftLibrary)
	{
		FT_Done_FreeType(m_ftLibrary);
		m_ftLibrary = NULL;
	}
	m_pFontTexturePool->Release();
}

bool CFontManager::AddFontType( int fontType, LPCTSTR fontFile, int fontSize)
{
	XPASSERT( fontType >= 0 && L"字体号fontType必须大于等于0!");

	if ( NULL == m_pFileIOMan)
		return false;

	if (!fontFile)
	{
		XPASSERT( 0 && L"必须指定字体文件!");
		return false;
	}

	FT_Face face = NULL;

	FACE_MAP::iterator it = m_faces.find( fontFile);

	if ( it != m_faces.end())
		face = it->second;
	else
	{
		PFS::CFile file;
		if (!file.Open( fontFile, PFS::FM_EXCL, PFS::FA_RDONLY))
			return false;

		size_t size = static_cast<size_t>(file.GetSize());
		unsigned char* buffer = new unsigned char[size];
		file.ReadAll(buffer, size);
		file.Close();

		if ( FT_New_Memory_Face(m_ftLibrary, buffer, size, 0, &face))
		{
			delete[] buffer;
			return false;
		}
		m_fontFileCatch[fontFile] = buffer;

		m_faces.insert( std::make_pair( fontFile, face));
	}

	FT_Size size;
	if ( FT_New_Size( face, &size))
		return false;

	FT_Activate_Size( size);
	FT_Set_Pixel_Sizes( face, 0, fontSize);

	m_caches[fontType] = CBitmapCache( size);
	return true;
}


TextBlockHandle CFontManager::CreateTextBlock( int fontType, LPCTSTR szText)
{
	// TODO: 目前CTextBlock只支持单行文本, 多行文本需对\n做分析, 并相应修改render

	int textWidth = GetStringWidth( szText, fontType);
	int fontSize = GetFontTypeHeight( fontType);
	if ( !(textWidth && fontSize) || !m_pRenderer)
		return INVALID_TEXTBLOCK_HANDLE;

	FontTextureHandle fontTex = m_pFontTexturePool->Request( *m_pRenderer, fontSize, textWidth);
	if ( INVALID_FONTTEXTURE_HANDLE == fontTex)
		return INVALID_TEXTBLOCK_HANDLE;

	CTextBlock* pBlock = new CTextBlock(this);
	pBlock->InsertOneLineTexture( fontTex);
	pBlock->SetFontType( fontType);
	pBlock->SetText( szText);
	
	TextBlockHandle htb = GenId();
	m_textblocks[ htb] = pBlock;
	return htb;
}

TextBlockHandle CFontManager::CreateTextBlock( int fontType, int maxTextWidth)
{
	int fontSize = GetFontTypeHeight( fontType);
	if ( !( maxTextWidth && fontSize) || !m_pRenderer)
		return INVALID_TEXTBLOCK_HANDLE;

	FontTextureHandle fontTex = m_pFontTexturePool->Request( *m_pRenderer, fontSize, maxTextWidth);
	if ( INVALID_FONTTEXTURE_HANDLE == fontTex)
		return INVALID_TEXTBLOCK_HANDLE;

	CTextBlock* pBlock = new CTextBlock(this);
	pBlock->InsertOneLineTexture( fontTex);
	pBlock->SetFontType( fontType);
	
	TextBlockHandle htb = GenId();
	m_textblocks[ htb] = pBlock;
	return htb;
}

ITextBlock* CFontManager::GetTextBlock( TextBlockHandle htb)
{
	TEXTBLOCK_MAP::iterator it = m_textblocks.find( htb);
	if ( it != m_textblocks.end())
	{
		return (ITextBlock*)it->second;
	}
	return NULL;
}

void CFontManager::ReleaseTextBlock( TextBlockHandle htb)
{
	TEXTBLOCK_MAP::iterator it = m_textblocks.find( htb);
	if ( it != m_textblocks.end())
	{
		it->second->Release();
		delete it->second;
		m_textblocks.erase( htb);
	}
}

int CFontManager::FillBitmap( FontTextureHandle tex, int x, int y, TCHAR ch, int fontType, DWORD textColor, DWORD bkColor, bool bUnderline)
{
	BITMAPCACHE_MAP::iterator it = m_caches.find( fontType);
	if ( it == m_caches.end())		// 找不到fontType指定字体
		return -1;

	CBitmapCache& cache = it->second;
	int	w = cache.GetCharWidth( ch);
	int h = cache.GetFontHeight();
	DWORD* pBitmap = ( DWORD*)cache.GetBitmap( ch);

	if ( !pBitmap)
		return -2;

	CFontTexture* pTex = m_pFontTexturePool->GetFontTexture( tex);
	int texSize = CFontTexPool::RENDER_TEX_SIZE[ pTex->GetTextureLevel()];

	if ( x % texSize + w <= texSize) // 字形图在同一行纹理中, 每行纹理的具体高度由字体大小决定, 且是CFontTexPool::MIN_FONT_TEX_HEIGHT的倍数
	{
		RECT src, dest;
		src.left = 0;  src.top = 0;  src.right = w;  src.bottom = h;
		dest.left = x; dest.top = y; dest.right = x + w; dest.bottom = y + h;
		if ( !CopyPixels( pTex, &dest, pBitmap, &src, w))
			return -3;

		if ( bUnderline && L'@' != ch) // 字符 @ 不做下划线效果
		{
			dest.left = x; dest.top = y + cache.GetFontUnderlinePosition();
			dest.right = x + w;  dest.bottom = dest.top + cache.GetFontUnderlineThickness();
			FillRect( pTex, &dest, textColor | 0x00ffffff);
		}
	}
	else	// 一个字形分布在两行纹理中
	{
		int w2 = ( x + w) % texSize;
		int w1 = w - w2;

		{
			RECT src, dest;
			src.left = 0;  src.top = 0;  src.right = w1;  src.bottom = h;
			dest.left = x; dest.top = y; dest.right = x + w1; dest.bottom = y + h;
			if ( !CopyPixels( pTex, &dest, pBitmap, &src, w))
				return -3;

			if ( bUnderline && L'@' != ch)
			{
				dest.left = x; dest.top = y + cache.GetFontUnderlinePosition();
				dest.right = x + w1;  dest.bottom = dest.top + cache.GetFontUnderlineThickness();
				FillRect( pTex, &dest, textColor | 0x00ffffff);
			}
		}

		{
			RECT src, dest;
			src.left = w1;  src.top = 0;  src.right = w1 + w2;  src.bottom = h;
			dest.left = x + w1; dest.top = y; dest.right = dest.left + w2; dest.bottom = y + h;
			if ( !CopyPixels( pTex, &dest, pBitmap, &src, w))
				return -3;

			if ( bUnderline && L'@' != ch)
			{
				dest.left = x + w1; dest.top = y + cache.GetFontUnderlinePosition();
				dest.right = dest.left + w2;  dest.bottom = dest.top + cache.GetFontUnderlineThickness();
				FillRect( pTex, &dest, textColor | 0x00ffffff);
			}
		}
	}

	return w;
}

bool CFontManager::CopyPixels( CFontTexture* pTex, const RECT* dest, DWORD* pSrc, const RECT* src, int srcWidth)
{
	int w = src->right - src->left;
	int h = src->bottom - src->top;
	DWORD* data = new DWORD[ w * h];

	for ( int i = src->top; i < src->bottom; ++ i)
	{
		memcpy( data + i * w, pSrc + i * srcWidth + src->left, w * 4);
	}
	bool rs = pTex->SetPictureData( *m_pRenderer, (void*)data, w * h * 4, dest);

	delete []data;
	
	return rs;
}

bool CFontManager::FillRect( CFontTexture* pTex, const RECT* dest, DWORD color)
{
	int w = dest->right - dest->left;
	int h = dest->bottom - dest->top;
	DWORD* data = new DWORD[ w * h];

	for ( int i = 0; i < w * h; ++ i)
	{
		data[i] = color;
	}
	bool rs = pTex->SetPictureData( *m_pRenderer, data, w * h * 4, dest);

	delete []data;

	return rs;
}


int CFontManager::GetCharWidth( const TCHAR ch, int fontType)
{
	BITMAPCACHE_MAP::iterator it = m_caches.find( fontType);
	if ( it != m_caches.end())
		return it->second.GetCharWidth( ch);

	return 0;
}

int CFontManager::GetStringWidth( LPCTSTR szText, int fontType)
{
	if ( !szText)
		return 0;

	int w = 0;
	BITMAPCACHE_MAP::iterator it = m_caches.find( fontType);
	if ( it != m_caches.end())
	{	int len = ( int)wcslen( szText);
		for ( int i = 0; i < len; i ++)
		{
			w += it->second.GetCharWidth( szText[i]);
		}
	}
	return w;
}

int CFontManager::GetFontTypeHeight( int fontType)
{
	BITMAPCACHE_MAP::iterator it = m_caches.find( fontType);
	if ( it != m_caches.end())
		return it->second.GetFontHeight();
	return 0;
}

int CFontManager::GetFontTypeUnderlinePosition( int fontType)
{
	BITMAPCACHE_MAP::iterator it = m_caches.find( fontType);
	if ( it != m_caches.end())
		return it->second.GetFontUnderlinePosition();
	return 0;
}

int CFontManager::GetFontTypeUnderlineThickness( int fontType)
{
	BITMAPCACHE_MAP::iterator it = m_caches.find( fontType);
	if ( it != m_caches.end())
		return it->second.GetFontUnderlineThickness();
	return 0;
}

int CFontManager::GenId()
{
	static int id = 0;
	return ++ id;
}

void CFontManager::gc()
{
	for ( BITMAPCACHE_MAP::iterator it = m_caches.begin(); it != m_caches.end(); ++ it)
	{
		it->second.gc();
	}
	if ( m_pFontTexturePool)
		m_pFontTexturePool->gc();
}


TextBlockHandle CFontManager::NewText( LPCTSTR text, int fonttype, DWORD color, DWORD gradualcolor, DWORD bkColor, bool bUnderline)
{
	if ( !text)
		return INVALID_TEXTBLOCK_HANDLE;

	int w = GetStringWidth( text, fonttype);
	TextBlockHandle htb  = CreateTextBlock( fonttype, w);
	if ( INVALID_TEXTBLOCK_HANDLE != htb)
	{
		ITextBlock& tb = *GetTextBlock( htb);
		tb.SetFontColor( color, gradualcolor);
		tb.SetBkColor( bkColor);
		tb.SetUnderline( bUnderline);
		tb.SetText( text);
	}
	return htb;
}

bool CFontManager::DrawText( TextBlockHandle htb, float x1, float y1, const FRECT* srcrect)
{
	if ( INVALID_TEXTBLOCK_HANDLE != htb)
	{
		ITextBlock& tb = *GetTextBlock( htb);
		tb.Render( *m_pRenderer, x1, y1, srcrect);
		return true;
	}
	return false;
}


}