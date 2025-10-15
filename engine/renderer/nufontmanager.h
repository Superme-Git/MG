#ifndef __Nuclear_FONTMANAGER_H__
#define __Nuclear_FONTMANAGER_H__

#if _MSC_VER > 1000
#pragma once
#endif
#if (defined WIN32) || (defined _WIN32)
#else
//#include <ext/hash_map>
#endif
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WP8)
#define generic GenericFromFreeTypeLibrary
#define internal InternalFromFreeTypeLibrary
#endif
#include <ft2build.h>
#include <freetype/ftsizes.h>
#include <freetype/ftglyph.h>
#include FT_FREETYPE_H
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WP8)
#undef generic
#undef internal
#endif
#include "nuifontmanager.h"
#include "nufonttexture.h"

#include "nuport.h"

namespace Nuclear
{

	class CTextBlock;
	class CFontManager;

	class CBitmapCache
	{
		friend class CFontManager;

		typedef DWORD PixelData;
		struct BitmapWrap	// 用于缓存一个字符图像
		{
			PixelData*		bitmap;
			unsigned int	life;

			int				width;
			int				height;

			BitmapWrap()
				: bitmap( NULL)
			{}
		};
		typedef std::map<TCHAR, BitmapWrap> BITMAP_MAP;

		FT_Size		m_size;					// freetype中的字体大小参数
		int			m_height;				// 文本高度(以像素为单位, 且包含文字上下方的空白间隙, 所以该高度会比所设置的尺寸略大), 由m_size计算而来
		int			m_baselinePosition;		// 字体基准线垂直坐标
		int			m_underlinePosition;	// 字体下划线垂直坐标
		int			m_underlineThickness;	// 字体下划线粗细度
		BITMAP_MAP	m_bitmaps;				// 字符图像缓存
		std::string		m_defaultFontFile;
		int			m_defaultFontSize;
		int			m_defaultFontType;		//1 读取文件  2系统字体
		FT_Library	m_defslibrary;
		FT_Face		m_defsface;
		FT_Size		m_defssize;
	public:
		CBitmapCache();
		CBitmapCache( FT_Size size);
		virtual ~CBitmapCache();

	public:
		void SetDefaultFont(LPCTSTR defFontFile, int defFontSize, int defType);				// 激活m_size指定的字体尺寸
		void ActiveFT_Size();				// 激活m_size指定的字体尺寸
		FT_Face GetFT_Face();				// 获取freetype字体face
		int GetCharWidth(TCHAR ch);			// 获取字符宽度
		int GetFontHeight();				// 获取字体高度
		int GetFontUnderlinePosition();		// 获取下划线垂直坐标
		int GetFontUnderlineThickness();	// 获取下划线粗细度

		void* GetBitmap(TCHAR ch);			// 获取字符图像数据

		void Release();						// 释放所有资源

	private:
		void Initialize();						// 根据freetype提供的参数计算字体实际高度、基准线位置、下划线位置
		BitmapWrap* CreateCharBitmap(TCHAR ch);	// 使用freetype创建字符图像

		// 用枚举的方法来估计基准线
		int GetBaseLine(); 

	protected:

		void gc();							// 资源回收
	};

	class CFontManager : public IFontManager
	{
		//friend class DX9Renderer;
		// 文字阴影或描边效果的宽度
		static const int offset_pixel_x = 1;
		static const int offset_pixel_y = 1;

		typedef std::map<int, CBitmapCache> BITMAPCACHE_MAP;
		typedef std::map<std::wstring, FT_Face> FACE_MAP;
		typedef std::map<TextBlockHandle, CTextBlock*> TEXTBLOCK_MAP;
		typedef std::map<std::wstring, unsigned char*> FontFileCatche;
    
		struct FontArg
		{
			FT_Open_Args* pOpenFontArg;
			FT_StreamRec* pFontStream;
		};
		typedef std::vector<FontArg>    FontArgArray;

		Renderer*			m_pRenderer;
		CFontTexPool*		m_pFontTexturePool;
		NuclearFileIOManager*		m_pFileIOMan;
		FT_Library			m_ftLibrary;
		BITMAPCACHE_MAP		m_caches;	
		FACE_MAP			m_faces;
		TEXTBLOCK_MAP		m_textblocks;
		FontFileCatche		m_fontFileCatch;
		FontArgArray        m_vFontArg;

	public:
		CFontManager();
		virtual ~CFontManager();

		virtual bool InitFreetype( );
		virtual void Release();
		virtual bool AddFontType(int fontType, LPCTSTR fontFile, int fontSize, LPCTSTR defFontFile, int defFontSize, int defFontType);	// 添加一个指定大小的字体

		virtual TextBlockHandle CreateTextBlock( int fontType, LPCTSTR szText);		// 根据字符串和指定字体申请一块字体纹理, 目前只支持单行文本
		virtual TextBlockHandle CreateTextBlock( int fontType, int maxTextWidth);	// 根据指定宽度和字体申请一块字体纹理, 永远只支持单行文本
		virtual void ReleaseTextBlock( TextBlockHandle hTex);
		virtual ITextBlock* GetTextBlock( TextBlockHandle htb);

		virtual TextBlockHandle NewText( LPCTSTR text, int fonttype, DWORD color, DWORD gradualcolor, DWORD bkColor = 0x00000000, bool bUnderline = false);
		virtual TextBlockHandle NewTextex( std::wstring text, int fonttype, DWORD color, DWORD gradualcolor, DWORD bkColor = 0x00000000, bool bUnderline = false);
		virtual bool DrawText( TextBlockHandle htb, float x1, float y1, const NuclearFRectt* srcrect);

		// @return >=0: 正常 -1: 找不到字体, -2: 获取字形位图失败, -3: 不可填充
		int FillBitmap( FontTextureHandle tex, int x, int y, TCHAR ch, int fontType, DWORD textColor, DWORD bkColor, bool bUnderline = false);
		int FillBitmapColor(FontTextureHandle tex, int x, int y, int w, int h, DWORD color);

		virtual int GetCharWidth( const TCHAR ch, int fontType);
		virtual int GetStringWidth( LPCTSTR szText, int fontType);
		virtual int GetFontTypeHeight( int fontType);
		virtual int GetFontTypeUnderlinePosition( int fontType);
		virtual int GetFontTypeUnderlineThickness( int fontType);

		void Init( Renderer* pRenderer, NuclearFileIOManager *pFileIOMan);

		CFontTexPool* GetFontTexturePool() { return m_pFontTexturePool; }
    
	private:
		inline bool CopyPixels( CFontTexture* pTex, const RECT* dest, DWORD* pSrc, const RECT* src, int srcWidth);
		inline bool FillRect( CFontTexture* pTex, const RECT* dest, DWORD color);

		int GenId();

		void gc();
	};

}

#endif