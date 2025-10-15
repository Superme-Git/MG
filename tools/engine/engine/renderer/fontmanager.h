#ifndef __Nuclear_FONTMANAGER_H
#define __Nuclear_FONTMANAGER_H

#if _MSC_VER > 1000
#pragma once
#endif
#include <ft2build.h>
#include <freetype/ftsizes.h>
#include <freetype/ftglyph.h>
#include FT_FREETYPE_H

#include "ifontmanager.h"
#include "fonttexture.h"

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
	typedef stdext::hash_map<TCHAR, BitmapWrap> BITMAP_MAP;

	FT_Size		m_size;					// freetype�е������С����
	int			m_height;				// �ı��߶�(������Ϊ��λ, �Ұ����������·��Ŀհ׼�϶, ���Ըø߶Ȼ�������õĳߴ��Դ�), ��m_size�������
	int			m_baselinePosition;		// �����׼�ߴ�ֱ����
	int			m_underlinePosition;	// �����»��ߴ�ֱ����
	int			m_underlineThickness;	// �����»��ߴ�ϸ��
	BITMAP_MAP	m_bitmaps;				// �ַ�ͼ�񻺴�

public:
	CBitmapCache();
	CBitmapCache( FT_Size size);
	virtual ~CBitmapCache();

public:
	void ActiveFT_Size();				// ����m_sizeָ��������ߴ�
	FT_Face GetFT_Face();				// ��ȡfreetype����face
	int GetCharWidth( TCHAR ch);		// ��ȡ�ַ����
	int GetFontHeight();				// ��ȡ����߶�
	int GetFontUnderlinePosition();		// ��ȡ�»��ߴ�ֱ����
	int GetFontUnderlineThickness();	// ��ȡ�»��ߴ�ϸ��

	void* GetBitmap( TCHAR ch);			// ��ȡ�ַ�ͼ������

	void Release();						// �ͷ�������Դ

private:
	void Initialize();							// ����freetype�ṩ�Ĳ�����������ʵ�ʸ߶ȡ���׼��λ�á��»���λ��
	BitmapWrap* CreateCharBitmap( wchar_t ch);	// ʹ��freetype�����ַ�ͼ��

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

	Renderer*			m_pRenderer;
	CFontTexPool*		m_pFontTexturePool;
	CFileIOManager*		m_pFileIOMan;
	FT_Library			m_ftLibrary;
	BITMAPCACHE_MAP		m_caches;	
	FACE_MAP			m_faces;
	TEXTBLOCK_MAP		m_textblocks;
	FontFileCatche		m_fontFileCatch;
public:
	CFontManager();
	virtual ~CFontManager();

	virtual bool InitFreetype( );
	virtual void Release();
	virtual bool AddFontType( int fontType, LPCTSTR fontFile, int fontSize);	// ���һ��ָ����С������

	virtual TextBlockHandle CreateTextBlock( int fontType, LPCTSTR szText);		// �����ַ�����ָ����������һ����������, Ŀǰֻ֧�ֵ����ı�
	virtual TextBlockHandle CreateTextBlock( int fontType, int maxTextWidth);	// ����ָ����Ⱥ���������һ����������, ��Զֻ֧�ֵ����ı�
	virtual void ReleaseTextBlock( TextBlockHandle hTex);
	virtual ITextBlock* GetTextBlock( TextBlockHandle htb);

	virtual TextBlockHandle NewText( LPCTSTR text, int fonttype, DWORD color, DWORD gradualcolor, DWORD bkColor = 0x00000000, bool bUnderline = false);
	virtual bool DrawText( TextBlockHandle htb, float x1, float y1, const FRECT* srcrect);

	// @return >=0: ���� -1: �Ҳ�������, -2: ��ȡ����λͼʧ��, -3: �������
	int FillBitmap( FontTextureHandle tex, int x, int y, TCHAR ch, int fontType, DWORD textColor, DWORD bkColor, bool bUnderline = false);

	virtual int GetCharWidth( const TCHAR ch, int fontType);
	virtual int GetStringWidth( LPCTSTR szText, int fontType);
	virtual int GetFontTypeHeight( int fontType);
	virtual int GetFontTypeUnderlinePosition( int fontType);
	virtual int GetFontTypeUnderlineThickness( int fontType);

	void Init( Renderer* pRenderer, CFileIOManager *pFileIOMan);

	CFontTexPool* GetFontTexturePool() { return m_pFontTexturePool; }

private:
	inline bool CopyPixels( CFontTexture* pTex, const RECT* dest, DWORD* pSrc, const RECT* src, int srcWidth);
	inline bool FillRect( CFontTexture* pTex, const RECT* dest, DWORD color);

	int GenId();

	void gc();

};

}

#endif