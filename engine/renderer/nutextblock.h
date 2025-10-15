#ifndef __Nuclear_TEXTBLOCK_H__
#define __Nuclear_TEXTBLOCK_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <string>

#include "nuifontmanager.h"
#include "nufontmanager.h"

namespace Nuclear
{
// Pixels to put between glyphs
#define INTER_GLYPH_PAD_SPACE 2

class CTextBlock : public ITextBlock
{
	friend class CFontManager;

	enum EFFECT_TYPE { FET_NONE = 0, FET_SHADOW, FET_STROKE, };
	typedef std::vector<FontTextureHandle> FONTTEX_VEC;

	CFontManager *		m_pFontMan;

	std::wstring		m_text;
	int					m_fontType;
	DWORD				m_fontColor;
	DWORD				m_fontGradualColor;
	DWORD				m_bkColor;
	DWORD				m_effectColor;
	EFFECT_TYPE			m_effectType;
	bool				m_bUnderline;
	FONTTEX_VEC			m_fontTexes;
public:
	CTextBlock(CFontManager *pFontMan);
	virtual ~CTextBlock();

public:
	virtual bool SetText( LPCTSTR text);
	virtual LPCTSTR GetText();
	virtual int	 GetFontType();
	virtual DWORD GetFontColor();
	virtual DWORD GetGradualColor();
	virtual DWORD GetBkColor();

	virtual void SetFontType( int fontType);
	virtual bool SetFontColor( DWORD fontColor);
	virtual bool SetFontColor( DWORD fontColor, DWORD gradualColor);
	virtual bool SetBkColor( DWORD bkColor);

	virtual int	GetTextWidth();
	virtual int GetTextHeight();

	virtual void CancelFontEffect();

	virtual void SetShadowColor( DWORD shadowColor);
	virtual DWORD GetShadowColor();

	virtual void SetStrokeColor( DWORD strokeColor);
	virtual DWORD GetStrokeColor();

	virtual bool SetUnderline( bool bUnderline);
	virtual bool IsUnderline();

	virtual void Render( Renderer& renderer, float x, float y, const NuclearFRectt* src);
	virtual void Render( Renderer* renderer, const NuclearFPoint& textPos, const NuclearFPoint& fScale, const NuclearFRectt* src);

protected:
	virtual void InsertOneLineTexture( FontTextureHandle hFontTexture);
	
	virtual void Release();

private:
	int  FillBitmap();
	void Render( Renderer* renderer, float x, float y, const NuclearFRectt* src, DWORD fontColor, DWORD gradualColor, DWORD bkColor);
	void ShadowRender( Renderer* renderer, float x, float y, const NuclearFRectt* src, DWORD fontColor, DWORD gradualColor, DWORD bkColor, DWORD shadowColor);
	void StrokeRender( Renderer* renderer, float x, float y, const NuclearFRectt* src, DWORD fontColor, DWORD gradualColor, DWORD bkColor, DWORD strokeColor);

};

}

#endif