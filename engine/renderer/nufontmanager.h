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
		struct BitmapWrap	// ���ڻ���һ���ַ�ͼ��
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

		FT_Size		m_size;					// freetype�е������С����
		int			m_height;				// �ı��߶�(������Ϊ��λ, �Ұ����������·��Ŀհ׼�϶, ���Ըø߶Ȼ�������õĳߴ��Դ�), ��m_size�������
		int			m_baselinePosition;		// �����׼�ߴ�ֱ����
		int			m_underlinePosition;	// �����»��ߴ�ֱ����
		int			m_underlineThickness;	// �����»��ߴ�ϸ��
		BITMAP_MAP	m_bitmaps;				// �ַ�ͼ�񻺴�
		std::string		m_defaultFontFile;
		int			m_defaultFontSize;
		int			m_defaultFontType;		//1 ��ȡ�ļ�  2ϵͳ����
		FT_Library	m_defslibrary;
		FT_Face		m_defsface;
		FT_Size		m_defssize;
	public:
		CBitmapCache();
		CBitmapCache( FT_Size size);
		virtual ~CBitmapCache();

	public:
		void SetDefaultFont(LPCTSTR defFontFile, int defFontSize, int defType);				// ����m_sizeָ��������ߴ�
		void ActiveFT_Size();				// ����m_sizeָ��������ߴ�
		FT_Face GetFT_Face();				// ��ȡfreetype����face
		int GetCharWidth(TCHAR ch);			// ��ȡ�ַ����
		int GetFontHeight();				// ��ȡ����߶�
		int GetFontUnderlinePosition();		// ��ȡ�»��ߴ�ֱ����
		int GetFontUnderlineThickness();	// ��ȡ�»��ߴ�ϸ��

		void* GetBitmap(TCHAR ch);			// ��ȡ�ַ�ͼ������

		void Release();						// �ͷ�������Դ

	private:
		void Initialize();						// ����freetype�ṩ�Ĳ�����������ʵ�ʸ߶ȡ���׼��λ�á��»���λ��
		BitmapWrap* CreateCharBitmap(TCHAR ch);	// ʹ��freetype�����ַ�ͼ��

		// ��ö�ٵķ��������ƻ�׼��
		int GetBaseLine(); 

	protected:

		void gc();							// ��Դ����
	};

	class CFontManager : public IFontManager
	{
		//friend class DX9Renderer;
		// ������Ӱ�����Ч���Ŀ��
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
		virtual bool AddFontType(int fontType, LPCTSTR fontFile, int fontSize, LPCTSTR defFontFile, int defFontSize, int defFontType);	// ���һ��ָ����С������

		virtual TextBlockHandle CreateTextBlock( int fontType, LPCTSTR szText);		// �����ַ�����ָ����������һ����������, Ŀǰֻ֧�ֵ����ı�
		virtual TextBlockHandle CreateTextBlock( int fontType, int maxTextWidth);	// ����ָ����Ⱥ���������һ����������, ��Զֻ֧�ֵ����ı�
		virtual void ReleaseTextBlock( TextBlockHandle hTex);
		virtual ITextBlock* GetTextBlock( TextBlockHandle htb);

		virtual TextBlockHandle NewText( LPCTSTR text, int fonttype, DWORD color, DWORD gradualcolor, DWORD bkColor = 0x00000000, bool bUnderline = false);
		virtual TextBlockHandle NewTextex( std::wstring text, int fonttype, DWORD color, DWORD gradualcolor, DWORD bkColor = 0x00000000, bool bUnderline = false);
		virtual bool DrawText( TextBlockHandle htb, float x1, float y1, const NuclearFRectt* srcrect);

		// @return >=0: ���� -1: �Ҳ�������, -2: ��ȡ����λͼʧ��, -3: �������
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