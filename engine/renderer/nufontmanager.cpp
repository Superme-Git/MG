#include "nurenderer.h"
#include "nufonttexture.h"
#include "nutextblock.h"
#include "../nuiengine.h"
#include "utils/Utils.h"
#include "nufontmanager.h"
#include "../common/nulog.h"
#include "ljfmext.h"
#include "utils/StringUtil.h"
#include "utils/FileUtil.h"
#include "platform/platform_types.h"
#include "../../dependencies/LJXML/Include/LJXML.hpp"
#include <fstream>

namespace Nuclear
{
	#define INTER_GLYPH_PAD_SPACE 2
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	CBitmapCache::CBitmapCache() : m_size(NULL), m_defslibrary(NULL), m_defsface(NULL), m_defssize(NULL)
	{
	
	}

	CBitmapCache::CBitmapCache(FT_Size size) : m_size(size), m_defslibrary(NULL), m_defsface(NULL), m_defssize(NULL)
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
	void CBitmapCache::SetDefaultFont(LPCTSTR defFontFile, int defFontSize, int defType)
	{
		m_defaultFontFile = SHARE_Wstring2String(defFontFile);
		m_defaultFontSize = defFontSize;
		m_defaultFontType = defType;
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
		if (m_defssize != NULL)
		{
			FT_Done_Size(m_defssize);
			m_defssize = NULL;
		}
		if (m_defsface != NULL)
		{
			FT_Done_Face(m_defsface);
			m_defsface = NULL;
		}
		if (m_defslibrary != NULL)
		{
			FT_Done_FreeType(m_defslibrary);
			m_defslibrary = NULL;
		}
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
			// ◊∞‘ÿ◊÷–ŒÕºœÒµΩ◊÷–Œ≤€
			if ( FT_Load_Char( face, szTest[i], FT_LOAD_RENDER | FT_LOAD_FORCE_AUTOHINT ))
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
		m_underlineThickness = std::max( 1, ( int)( 0.5f + ( float)m_size->metrics.y_ppem * face->underline_thickness / face->units_per_EM));
	}

	unsigned char GetCharPixel_Gray( const unsigned char* buffer, int pitch, int width, int row, int col)
	{
		return buffer[ row * width + col];
	}

	unsigned char GetCharPixel_Bit( const unsigned char* buffer, int pitch, int width, int row, int col)
	{
		return buffer[ row * pitch + ( col >> 3)] & (0x80 >> ( col % 8)) ? 255 : 0;
	}

	CBitmapCache::BitmapWrap* CBitmapCache::CreateCharBitmap(TCHAR ch)
	{
		ActiveFT_Size();

		FT_Face& face = m_size->face;

		// ◊∞‘ÿ◊÷–ŒÕºœÒµΩ◊÷–Œ≤€
		if (FT_Load_Char(face, ch, FT_LOAD_RENDER | FT_LOAD_FORCE_AUTOHINT | FT_LOAD_TARGET_NORMAL))
			return NULL;

		FT_GlyphSlot slot = face->glyph;
		FT_Bitmap* bitmap = &slot->bitmap;
		int w = slot->metrics.horiAdvance >> 6;
		int h = m_height;
		int picW = bitmap->width;
		int picH = bitmap->rows;
		unsigned char* pData = bitmap->buffer;
		int posX = std::max(0, slot->bitmap_left);
		int posY = std::max(0, m_baselinePosition - slot->bitmap_top);
		unsigned char(*GetCharPixel)(const unsigned char*, int, int, int, int);
		GetCharPixel = FT_PIXEL_MODE_MONO == bitmap->pixel_mode ? &GetCharPixel_Bit : &GetCharPixel_Gray;

		int copyType = 1;
		if (bitmap->buffer == NULL && m_defaultFontFile.length() > 0)
		{
			if (m_defaultFontType == 1)
			{
				if (m_defslibrary == NULL)
				{
					FT_Init_FreeType(&m_defslibrary);
				}
				if (m_defsface == NULL)
				{
					FT_New_Face(m_defslibrary, m_defaultFontFile.c_str(), 0, &m_defsface);
					if (FT_New_Size(m_defsface, &m_defssize))
						return NULL;
					FT_Activate_Size(m_defssize);
					FT_Set_Pixel_Sizes(m_defsface, 0, m_defaultFontSize);
				}

				FT_Activate_Size(m_defssize);
				if (FT_Load_Char(m_defsface, ch, FT_LOAD_RENDER | FT_LOAD_FORCE_AUTOHINT | FT_LOAD_TARGET_NORMAL))
					return NULL;
				slot = m_defsface->glyph;
				bitmap = &slot->bitmap;
				w = slot->metrics.horiAdvance >> 6;
				h = m_height;
				picW = bitmap->width;
				picH = bitmap->rows;
				pData = bitmap->buffer;
				posX = std::max(0, slot->bitmap_left);
				posY = std::max(0, m_baselinePosition - slot->bitmap_top);
				GetCharPixel = FT_PIXEL_MODE_MONO == bitmap->pixel_mode ? &GetCharPixel_Bit : &GetCharPixel_Gray;
			}
			else if (m_defaultFontType == 2)
			{
				std::wstring wws;
				wws = ch;
				std::string tmp = SHARE_Wstring2String(wws);
				GetEngine()->GetRenderer()->GetTextData(tmp.c_str(), m_defaultFontFile, m_defaultFontSize, pData, picW, picH);
				w = m_defaultFontSize;
				h = m_defaultFontSize;
				posX = (picW - m_defaultFontSize) / 2;
				posY = (picH - m_defaultFontSize) / 2;
				copyType = 2;
			}
			else
				return NULL;
		}
		std::pair<BITMAP_MAP::iterator, bool> istate = m_bitmaps.insert( std::make_pair( ch, BitmapWrap()));
		BitmapWrap& bitmapWrap = istate.first->second;
		bitmapWrap.bitmap = new PixelData[w * h];
		bitmapWrap.width = w;
		bitmapWrap.height = h;
		bitmapWrap.life = 1;
		memset( bitmapWrap.bitmap, 0, w * h * sizeof( PixelData));

		if (copyType == 1)
		{
			for (int i = 0; i < picH; i++)
			{
				if (posY + i < 0 || posY + i >= h)	continue;
				for (int j = 0; j < picW; j++)
				{
					if (posX + j < 0 || posX + j >= w)	continue;
					unsigned char srcGray = GetCharPixel(bitmap->buffer, bitmap->pitch, bitmap->width, i, j);
					bitmapWrap.bitmap[(posY + i) * w + posX + j] = (srcGray << 24) | 0x00ffffff;	// Ω´◊÷–ŒÕºœÒ◊™ªªŒ™¥¯Õ∏√˜∂»µƒ∞◊…´ÕºœÒ,  µº Œƒ◊÷—’…´”…‰÷»æ ±Ω¯––∂•µ„—’…´ªÏ∫œ
				}
			}
		}
		else if (copyType == 2)
		{
			for (int i = 0; i < h; i++)
			{
				int ri = i + posY;
				if (ri < 0 || ri >= picH)	continue;
				for (int j = 0; j < w; j++)
				{
					int rj = j + posX;
					if (rj < 0 || rj >= picW)	continue;
					bitmapWrap.bitmap[i* w + j] = *((unsigned int*)pData + (ri)* w + rj);
				}
			}
			if (pData != NULL)
			{
				delete[] pData;
				pData = NULL;
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
				 m_bitmaps.erase( i++);
			}
			else
			{
				-- bw.life;
				++ i;
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

	void CFontManager::Init( Renderer* pRenderer, NuclearFileIOManager *pFileIOMan)
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
    
		for (unsigned int i = 0; i < m_vFontArg.size(); ++i) {
			delete m_vFontArg[i].pFontStream;
			delete m_vFontArg[i].pOpenFontArg;
		}
		m_vFontArg.clear();

		if ( m_ftLibrary)
		{
			FT_Done_FreeType(m_ftLibrary);
			m_ftLibrary = NULL;
		}
		m_pFontTexturePool->Release();
	}
    
    static unsigned long free_type_io( FT_Stream stream, unsigned long   offset, unsigned char*  buffer, unsigned long   count )
    {
        assert(stream->pathname.pointer);
		{
            LJFM::LJFMF* pFile = (LJFM::LJFMF*)stream->pathname.pointer;
            pFile->Seek(offset, LJFM::FSM_SET);
			if (buffer)
				size_t rd = pFile->Read(buffer, count);
		}
        
        return count;
    }
    
    static void free_type_close( FT_Stream  stream )
    {
        assert(stream->pathname.pointer);
		{
			LJFM::LJFMF* pFile = (LJFM::LJFMF*)stream->pathname.pointer;
			pFile->Close();
			delete pFile;
		}
        return;
    }
    
	bool CFontManager::AddFontType(int fontType, LPCTSTR fontFile, int fontSize, LPCTSTR defFontFile, int defFontSize, int defFontType)
	{
		XPASSERT(fontType >= 0 && L"◊÷ÃÂ∫≈fontType±ÿ–Î¥Û”⁄µ»”⁄0!");

		if (NULL == m_pFileIOMan)
			return false;

		if (!fontFile)
		{
			XPASSERT(0 && L"±ÿ–Î÷∏∂®◊÷ÃÂŒƒº˛!");
			return false;
		}

		FT_Face face = NULL;

		FACE_MAP::iterator it = m_faces.find((wchar_t*)fontFile);

		if (it != m_faces.end())
			face = it->second;
		else
		{
			int face_index = 0;
			int language = 0;
#ifndef ANDROID
			std::string filename = SHARE_Wstring2String((wchar_t*)fontFile);
			{
#if (defined WIN7_32)
				filename = "../..";
				filename += SHARE_Wstring2String((wchar_t*)fontFile);
#elif (defined WINAPI_FAMILY && WINAPI_FAMILY == WINAPI_FAMILY_PHONE_APP)
				filename = CFileUtil::MakePath(CFileUtil::GetCacheDir().c_str(), ws2s((wchar_t*)fontFile).c_str());
#else
				if(filename == "/res/ui/fonts/DFYuanW7-GB2312.ttf")
				{
					char* fileBuf = NULL;
					std::streamsize ss = 0;
					if (GetBuffFromFile(L"/cfg/language.xml", fileBuf, ss) != 0)
					{
						return false;
					}

					std::wstring fileData;
					LJXMLStringHelper::EncodeLJ codeType;
					if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, fileData, codeType))
					{
						delete[] fileBuf;
						return false;
					}

					delete[] fileBuf;

                    LJXML::LJXML_Doc<LJXML::Char> doc;
                    LJXML::Char* charData = doc.LoadFromString(fileData);
                    if (!doc.first_node())
                    {
                    }
                    LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
                    language = _wtoi(root->first_attribute(L"language")->value());
                    
					if (language == 0)
					{
						face_index = CFileUtil::IsCurLanguageSimpleCh()?1:0;
#ifdef __i386__
						filename = "/System/Library/Fonts/STHeiti Medium.ttc";
#else
						filename = "/System/Library/Fonts/Cache/STHeiti-Medium.ttc";
#endif
						std::ifstream* datafile = new std::ifstream(filename.c_str(), std::ios::binary|std::ios::ate);
						if(datafile->fail())
						{
#ifdef __i386__
							filename = "/System/Library/Fonts/STHeiti Light.ttc";
#else
							filename = "/System/Library/Fonts/Cache/STHeiti-Light.ttc";
#endif
							std::ifstream* datafileL = new std::ifstream(filename.c_str(), std::ios::binary|std::ios::ate);
							if(datafileL->fail())
							{
								filename = CFileUtil::MakePath(CFileUtil::GetCacheDir().c_str(), ws2s(fontFile).c_str()).c_str();
							}

							datafileL->close();
							delete datafileL;
						}

						datafile->close();
						delete  datafile;
					}
					else
					{
						filename = CFileUtil::MakePath(CFileUtil::GetCacheDir().c_str(), ws2s(fontFile).c_str()).c_str();
					}
				}
				else
				{
					filename = CFileUtil::MakePath(CFileUtil::GetCacheDir().c_str(), ws2s(fontFile).c_str()).c_str();
				}
#endif
			}
#else
			std::string filename = SHARE_Wstring2String(fontFile);
			{
				if (filename == "/res/ui/fonts/DFYuanW7-GB2312.ttf")
				{
					LJFM::LJFMF xmlFile;
					std::wstring xmlpath = L"/cfg/language.xml";
					if (!xmlFile.Open(xmlpath, LJFM::FM_EXCL, LJFM::FA_RDONLY))
					{
						xmlFile.Close();
					}
					else
					{
						char* fileBuf = NULL;
						std::streamsize ss = 0;
						if (GetBuffFromFile(xmlpath, fileBuf, ss) != 0)
						{
							return false;
						}

						std::wstring fileData;
						LJXMLStringHelper::EncodeLJ codeType;
						if (!LJXMLStringHelper::reallyLoadFromMemory((unsigned char*)fileBuf, ss, fileData, codeType))
						{
							delete[] fileBuf;
							return false;
						}

						delete[] fileBuf;

						LJXML::LJXML_Doc<LJXML::Char> doc;
						LJXML::Char* charData = doc.LoadFromString(fileData);
						if (!doc.first_node())
						{
						}
						LJXML::LJXML_Node<LJXML::Char>* root = (LJXML::LJXML_Node<LJXML::Char>*)doc.first_node();
						language = _wtoi(root->first_attribute(L"language")->value());// GetAttributeInteger(L"language");
					}
					if (language == 0)
					{
						filename = "/system/fonts/DroidSansFallback.ttf";
						std::ifstream* datafileL = new std::ifstream(filename.c_str(), std::ios::binary|std::ios::ate);
						if(datafileL->fail())
						{
							filename = CFileUtil::MakePath(CFileUtil::GetRootDir().c_str(), ws2s(fontFile).c_str()).c_str();
						}
						datafileL->close();
						delete datafileL;
					}
					else
					{
						filename = CFileUtil::MakePath(CFileUtil::GetRootDir().c_str(), ws2s(fontFile).c_str()).c_str();
					}
				}
				else
				{
					filename = CFileUtil::MakePath(CFileUtil::GetRootDir().c_str(), ws2s(fontFile).c_str()).c_str();
				}
			}
#endif

			{
				LJFM::LJFMF file;
				if (!file.Open( fontFile, LJFM::FM_EXCL, LJFM::FA_RDONLY))
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
			}

			m_faces.insert(std::make_pair((wchar_t*)fontFile, face));
		}

		FT_Size size;
		if ( FT_New_Size( face, &size))
		{
			return false;
		}

		FT_Activate_Size( size);
		FT_Set_Pixel_Sizes( face, 0, fontSize);

		m_caches[fontType] = CBitmapCache( size);
		m_caches[fontType].SetDefaultFont(defFontFile, defFontSize, defFontType);
		return true;
	}


	TextBlockHandle CFontManager::CreateTextBlock( int fontType, LPCTSTR szText)
	{

		int textWidth = GetStringWidth( szText, fontType);
		int fontSize = GetFontTypeHeight( fontType);
		if ( !(textWidth && fontSize) || !m_pRenderer)
			return INVALID_TEXTBLOCK_HANDLE;

		FontTextureHandle fontTex = m_pFontTexturePool->Request( *m_pRenderer, fontSize + INTER_GLYPH_PAD_SPACE, textWidth );
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
		if ( it == m_caches.end())
			return -1;

		CBitmapCache& cache = it->second;
		int	w = cache.GetCharWidth( ch);
		int h = cache.GetFontHeight();
		DWORD* pBitmap = ( DWORD*)cache.GetBitmap( ch);

		if ( !pBitmap)
			return -2;

		CFontTexture* pTex = m_pFontTexturePool->GetFontTexture( tex);
		int texSize = CFontTexPool::RENDER_TEX_SIZE[ pTex->GetTextureLevel()];

		if ( x % texSize + w <= texSize)
		{
			RECT src, dest;
			src.left = 0;  src.top = 0;  src.right = w;  src.bottom = h;
			dest.left = x; dest.top = y; dest.right = x + w; dest.bottom = y + h;
			if ( !CopyPixels( pTex, &dest, pBitmap, &src, w))
				return -3;

			if ( bUnderline && L'@' != ch)
			{
				dest.left = x; dest.top = y + cache.GetFontUnderlinePosition();
				dest.right = x + w;  dest.bottom = dest.top + cache.GetFontUnderlineThickness();
				FillRect( pTex, &dest, textColor | 0x00ffffff);
			}
		}
		else
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
	int CFontManager::FillBitmapColor(FontTextureHandle tex, int x, int y, int w, int h, DWORD color)
	{
		CFontTexture* pTex = m_pFontTexturePool->GetFontTexture(tex);
		if (pTex == NULL)	return 0;
		int texSize = CFontTexPool::RENDER_TEX_SIZE[pTex->GetTextureLevel()];

		if (x % texSize + w > texSize)
			w = texSize - x % texSize;
		if (w == 0)	return 0;
		if (x % texSize + w <= texSize)
		{
			RECT dest;
			dest.left = x; dest.top = y; dest.right = x + w; dest.bottom = y + h;
			FillRect(pTex, &dest, color);
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

	int CFontManager::GetStringWidth(LPCTSTR szText, int fontType)
	{
		if ( !szText)
			return 0;

		int w = 0;
		BITMAPCACHE_MAP::iterator it = m_caches.find( fontType);
		if ( it != m_caches.end())
		{
	#ifndef ANDROID
	#if (defined WIN7_32)
			int len = (int)wcslen((wchar_t*)szText);
	#else
			int len = (int)wcslen(szText);
	#endif
	#else
			int len = ( int)wcslen_x( szText);
	#endif
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

	TextBlockHandle CFontManager::NewTextex(std::wstring text, int fonttype, DWORD color, DWORD gradualcolor, DWORD bkColor, bool bUnderline)
	{
		if (!text.c_str())
			return INVALID_TEXTBLOCK_HANDLE;

		int w = GetStringWidth(text.c_str(), fonttype);
		TextBlockHandle htb = CreateTextBlock(fonttype, w);
		if (INVALID_TEXTBLOCK_HANDLE != htb)
		{
			ITextBlock& tb = *GetTextBlock(htb);
			tb.SetFontColor(color, gradualcolor);
			tb.SetBkColor(bkColor);
			tb.SetUnderline(bUnderline);
			tb.SetText(text.c_str());
		}
		return htb;
	}

	bool CFontManager::DrawText( TextBlockHandle htb, float x1, float y1, const NuclearFRectt* srcrect)
	{
		if (INVALID_TEXTBLOCK_HANDLE != htb)
		{
			ITextBlock& tb = *GetTextBlock(htb);
			tb.Render(*m_pRenderer, x1, y1, srcrect);
			return true;
		}
		return false;
	}
}