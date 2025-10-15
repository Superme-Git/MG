//#include "stdafx.h"
#include "nurenderer.h"
#include "nufonttexture.h"
#include "nufontmanager.h"
#include "nutextblock.h"
#if (defined WIN32) || (defined _WIN32)
#else
//#include <ext/hash_map>
#endif
namespace Nuclear
{

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


CTextBlock::CTextBlock(CFontManager *pFontMan)
	: m_pFontMan(pFontMan), m_fontType( -1), m_fontColor( 0xffffffff), 
	m_bkColor( 0x00000000), m_effectColor( 0x00000000), m_effectType( FET_NONE), m_bUnderline( false)
{
}

CTextBlock::~CTextBlock()
{
	Release();
}

bool CTextBlock::SetText( LPCTSTR text)
{
#if (defined WIN7_32)
	m_text.assign((wchar_t*)text);
#else
	m_text.assign(text);
#endif

	if ( m_fontTexes.empty() || INVALID_FONTTEXTURE_HANDLE == m_fontTexes[0])
		return false;

	return FillBitmap() > 0;
}

LPCTSTR CTextBlock::GetText()
{
	return LPCTSTR(m_text.c_str());
}

int	 CTextBlock::GetFontType()
{
	return m_fontType;
}

DWORD CTextBlock::GetFontColor()
{
	return m_fontColor;
}

DWORD CTextBlock::GetGradualColor()
{
	return m_fontGradualColor;
}

DWORD CTextBlock::GetBkColor()
{
	return m_bkColor;
}

void CTextBlock::SetFontType( int fontType)
{
	m_fontType = fontType;
}

bool CTextBlock::SetFontColor( DWORD fontColor)
{
	m_fontColor = fontColor;
	m_fontGradualColor = fontColor;
	return true;
}

bool CTextBlock::SetFontColor( DWORD fontColor, DWORD gradualColor)
{
	m_fontColor = fontColor;
	m_fontGradualColor = gradualColor;
	return true;
}

bool CTextBlock::SetBkColor( DWORD bkColor)
{
	m_bkColor = bkColor;
	return true;
}

int	CTextBlock::GetTextWidth()
{
#if (defined WIN7_32)
	return m_pFontMan->GetStringWidth(LPCTSTR(m_text.c_str()), m_fontType);
#else
	return m_pFontMan->GetStringWidth(m_text.c_str(), m_fontType);
#endif
}

int CTextBlock::GetTextHeight()
{
	return m_pFontMan->GetFontTypeHeight( m_fontType);
}

void CTextBlock::CancelFontEffect()
{
	m_effectType = FET_NONE;
}

void CTextBlock::SetShadowColor( DWORD shadowColor)
{
	m_effectColor = shadowColor;
	if (0xff000000 & m_effectColor)
		m_effectType = FET_SHADOW;
	else
		m_effectType = FET_NONE;
}

DWORD CTextBlock::GetShadowColor()
{
	return m_effectColor;
}

void CTextBlock::SetStrokeColor( DWORD strokeColor)
{
	m_effectColor = strokeColor;
	if ( 0xff000000 & m_effectColor)
		m_effectType = FET_STROKE;
	else
		m_effectType = FET_NONE;
}

DWORD CTextBlock::GetStrokeColor()
{
	return m_effectColor;
}

bool CTextBlock::SetUnderline( bool bUnderline)
{
	if ( m_bUnderline == bUnderline)
		return true;

	m_bUnderline = bUnderline;
	if ( m_text.empty())
		return true;

	return FillBitmap() > 0;
}

bool CTextBlock::IsUnderline()
{
	return m_bUnderline;
}

void CTextBlock::InsertOneLineTexture( FontTextureHandle hFontTexture)
{
	m_fontTexes.push_back( hFontTexture);
}

void CTextBlock::Render( Renderer* renderer, const NuclearFPoint& textPos, const NuclearFPoint& fScale, const NuclearFRectt* src)
{
	NuclearFRectt textRect = NULL != src ? *src : NuclearFRectt( .0f, .0f, (float)GetTextWidth(), (float)GetTextHeight());

	NuclearColor color[4] = { m_fontColor, m_fontColor, m_fontGradualColor, m_fontGradualColor};
	DrawPictureParam param;
	NuclearFRectt rect, src_area;
	param.pColor = color;
	param.colorCount = 4;

	int	nTotalWidth = GetTextWidth();
	int nTotalHeight = GetTextHeight();
	float fLeftx = textPos.x - fScale.x* nTotalWidth/2.0f;
	float fTopy = textPos.y - fScale.y* nTotalHeight/2.0f;

	for( FONTTEX_VEC::iterator it = m_fontTexes.begin(); it != m_fontTexes.end(); ++it)
	{
		NuclearFRectt full_area( textRect.left, textRect.top, textRect.right, textRect.bottom);	// “™‰÷»æµƒ’˚ÃÂŒƒ◊÷«¯”Ú(Ω´Œƒ±æŒ∆¿Ì ”Œ™“ª’˚øÈ)
		CFontTexture* tex = m_pFontMan->GetFontTexturePool()->GetFontTexture( *it);
		if(tex)
		{
			int text_w = tex->GetTextWidth();		// Œƒ±æŒ∆¿Ì◊‹øÌ∂»
			int text_h = tex->GetTextHeight();		// √ø––Œƒ±æŒ∆¿Ì µº ∏ﬂ∂»
			if ( textRect.top >= text_h)
				return;

			int passlen = 0;
			for ( int i = 0; i < tex->GetTextureInfoCount() && full_area.left < full_area.right; i ++)
			{
				CFontTextureInfo& fti = tex->GetTextureInfo( i);
				if ( !fti.IsValid())	
					return;

				int texture_w = tex->GetTextureWidth( i);	// √øøÈŒ∆¿ÌøÌ∂»
				passlen += texture_w;
				if ( full_area.left >= passlen)
					continue;

				src_area.left = full_area.left - passlen + texture_w;
				src_area.top = fti.m_startBlock * CFontTexPool::MIN_FONT_TEX_HEIGHT + full_area.top + INTER_GLYPH_PAD_SPACE;
				src_area.right = src_area.left + std::min( full_area.Width(), std::min( text_w - full_area.left, passlen - full_area.left));
				src_area.bottom = src_area.top + std::min( text_h - full_area.top, full_area.Height());

				rect.left = fLeftx;
				rect.top = fTopy;
				rect.right = fLeftx + fScale.x* src_area.Width();
				rect.bottom = fTopy + fScale.y* src_area.Height();
				param.pRect = &rect;

				int sw = (int)src_area.Width();
				src_area /= static_cast<float>( texture_w);
				param.pSrcrect = &src_area;

				param.handle = fti.GetTextureRes()->hPicture;

				renderer->DrawPicture( param);

				full_area.left += sw;
				fLeftx += fScale.x* sw;
			}
		}
	}
}

void CTextBlock::Render( Renderer& renderer, float x, float y, const NuclearFRectt* src)
{
	NuclearFRectt textRect = NULL != src ? *src : NuclearFRectt( .0f, .0f, (float)GetTextWidth(), (float)GetTextHeight());

	if ( m_effectType == FET_STROKE)
		StrokeRender( &renderer, x, y, &textRect, m_fontColor, m_fontGradualColor, m_bkColor, m_effectColor);
	else if ( m_effectType == FET_SHADOW)
		ShadowRender( &renderer, x, y, &textRect, m_fontColor, m_fontGradualColor, m_bkColor, m_effectColor);
	else
		Render( &renderer, x, y, &textRect, m_fontColor, m_fontGradualColor, m_bkColor);
}

void CTextBlock::Render( Renderer* renderer, float x, float y, const NuclearFRectt* src, DWORD fontColor, DWORD gradualColor, DWORD bkColor)
{
	NuclearColor color[4] = { fontColor, fontColor, gradualColor, gradualColor};
	DrawPictureParam param;
	NuclearFRectt rect, src_area;
	param.pColor = color;
	param.colorCount = 4;

	for ( FONTTEX_VEC::iterator it = m_fontTexes.begin(); it != m_fontTexes.end(); ++it)
	{
		NuclearFRectt full_area( src->left, src->top, src->right, src->bottom);	// “™‰÷»æµƒ’˚ÃÂŒƒ◊÷«¯”Ú(Ω´Œƒ±æŒ∆¿Ì ”Œ™“ª’˚øÈ)
		CFontTexture* tex = m_pFontMan->GetFontTexturePool()->GetFontTexture( *it);
		if ( tex)
		{
			int text_w = tex->GetTextWidth();		// Œƒ±æŒ∆¿Ì◊‹øÌ∂»
			int text_h = tex->GetTextHeight();		// √ø––Œƒ±æŒ∆¿Ì µº ∏ﬂ∂»
			if ( src->top >= text_h)
				return;

			int passlen = 0;
			for ( int i = 0; i < tex->GetTextureInfoCount() && full_area.left < full_area.right; i ++)
			{
				CFontTextureInfo& fti = tex->GetTextureInfo( i);
				if ( !fti.IsValid())	
					return;

				int texture_w = tex->GetTextureWidth( i);	// √øøÈŒ∆¿ÌøÌ∂»
				passlen += texture_w;
				if ( full_area.left >= passlen)
					continue;

				src_area.left = full_area.left - passlen + texture_w;
				src_area.top = fti.m_startBlock * CFontTexPool::MIN_FONT_TEX_HEIGHT + full_area.top + INTER_GLYPH_PAD_SPACE;
				src_area.right = src_area.left + std::min( full_area.Width(), std::min( text_w - full_area.left, passlen - full_area.left));
				src_area.bottom = src_area.top + std::min( text_h - full_area.top, full_area.Height());

				if ( 0xff000000 & bkColor)
					renderer->DrawBox( NuclearFRectt( x, y, x + src_area.Width(), y + src_area.Height()), bkColor);
				
				rect.left = x;								rect.top = y;
				rect.right = x + src_area.Width();			rect.bottom = y + src_area.Height();
				param.pRect = &rect;

				int sw = (int)src_area.Width();
				src_area /= static_cast<float>( texture_w);
				param.pSrcrect = &src_area;

				param.handle = fti.GetTextureRes()->hPicture;

				renderer->DrawPicture( param);

				full_area.left += sw;
				x += sw;
			}
		}
	}
}

void CTextBlock::ShadowRender( Renderer* renderer, float x, float y, const NuclearFRectt* src, DWORD fontColor, DWORD gradualColor, DWORD bkColor, DWORD shadowColor)
{
	NuclearColor color[4] = { fontColor, fontColor, gradualColor, gradualColor};
	DrawPictureParam param;

	for ( FONTTEX_VEC::iterator it = m_fontTexes.begin(); it != m_fontTexes.end(); ++it)
	{
		NuclearFRectt full_area( src->left, src->top, src->right, src->bottom);	// “™‰÷»æµƒ’˚ÃÂŒƒ◊÷«¯”Ú(Ω´Œƒ±æŒ∆¿Ì ”Œ™“ª’˚øÈ)
		CFontTexture* tex = m_pFontMan->GetFontTexturePool()->GetFontTexture( *it);
		if ( tex)
		{
			int text_w = tex->GetTextWidth();		// Œƒ±æŒ∆¿Ì◊‹øÌ∂»
			int text_h = tex->GetTextHeight();		// √ø––Œƒ±æŒ∆¿Ì µº ∏ﬂ∂»
			if ( src->top >= text_h)
				return;

			int passlen = 0;
			for ( int i = 0; i < tex->GetTextureInfoCount() && full_area.left < full_area.right; i ++)
			{
				CFontTextureInfo& fti = tex->GetTextureInfo( i);
				if ( !fti.IsValid())	
					return;

				int texture_w = tex->GetTextureWidth( i);	// √øøÈŒ∆¿ÌøÌ∂»
				passlen += texture_w;
				if ( full_area.left >= passlen)
					continue;

				NuclearFRectt src_area;
				src_area.left = full_area.left - passlen + texture_w;
				src_area.top = fti.m_startBlock * CFontTexPool::MIN_FONT_TEX_HEIGHT + full_area.top + INTER_GLYPH_PAD_SPACE;
				src_area.right = src_area.left + std::min( full_area.Width(), std::min( text_w - full_area.left, passlen - full_area.left));
				src_area.bottom = src_area.top + std::min( text_h - full_area.top, full_area.Height());

				if ( 0xff000000 & bkColor)
					renderer->DrawBox( NuclearFRectt( x, y, x + src_area.Width(), y + src_area.Height()), bkColor);

				NuclearFRectt rect( x + 1.0f, y + 1.0f, x + src_area.Width() + 1.0f, y + src_area.Height() + 1.0f);
				int sw = (int)src_area.Width();
				src_area /= static_cast<float>( texture_w);

				param.handle = fti.GetTextureRes()->hPicture;
				param.pRect = &rect;
				param.pSrcrect = &src_area;

				param.pColor = reinterpret_cast<const NuclearColor*>(&shadowColor);
				param.colorCount = 1;
				renderer->DrawPicture( param);

				rect -= NuclearFPoint( 1.0f, 1.0f);
				param.pColor = color;
				param.colorCount = 4;
				renderer->DrawPicture( param);

				full_area.left += sw;
				x += sw;
			}
		}
	}

}

void CTextBlock::StrokeRender( Renderer* renderer, float x, float y, const NuclearFRectt* src, DWORD fontColor, DWORD gradualColor, DWORD bkColor, DWORD strokeColor)
{
	NuclearColor color[4] = { fontColor, fontColor, gradualColor, gradualColor};

	for ( FONTTEX_VEC::iterator it = m_fontTexes.begin(); it != m_fontTexes.end(); ++it)
	{
		NuclearFRectt full_area( src->left, src->top, src->right, src->bottom);	// “™‰÷»æµƒ’˚ÃÂŒƒ◊÷«¯”Ú(Ω´Œƒ±æŒ∆¿Ì ”Œ™“ª’˚øÈ)
		CFontTexture* tex = m_pFontMan->GetFontTexturePool()->GetFontTexture( *it);
		if ( tex)
		{
			int text_w = tex->GetTextWidth();		// Œƒ±æŒ∆¿Ì◊‹øÌ∂»
			int text_h = tex->GetTextHeight();		// √ø––Œƒ±æŒ∆¿Ì µº ∏ﬂ∂»
			if ( src->top >= text_h)
				return;

			std::vector<PictureHandle> HPics;
			std::vector<NuclearFRectt> areas;
			std::vector<NuclearFRectt> dests;
			std::vector<float> xpos;

			int passlen = 0;
			for ( int i = 0; i < tex->GetTextureInfoCount() && full_area.left < full_area.right; i ++)
			{
				CFontTextureInfo& fti = tex->GetTextureInfo( i);
				if ( !fti.IsValid())	
					return;

				int texture_w = tex->GetTextureWidth( i);	// √øøÈŒ∆¿ÌøÌ∂»
				passlen += texture_w;
				if ( full_area.left >= passlen)
					continue;

				NuclearFRectt src_area;
				src_area.left = full_area.left - passlen + texture_w;
				src_area.top = fti.m_startBlock * CFontTexPool::MIN_FONT_TEX_HEIGHT + full_area.top + INTER_GLYPH_PAD_SPACE;
				src_area.right = src_area.left + std::min( full_area.Width(), std::min( text_w - full_area.left, passlen - full_area.left));
				src_area.bottom = src_area.top + std::min( text_h - full_area.top, full_area.Height());

				if ( 0xff000000 & bkColor)
					renderer->DrawBox( NuclearFRectt( x, y, x + src_area.Width(), y + src_area.Height()), bkColor);

				PictureHandle hPic = fti.GetTextureRes()->hPicture;
				NuclearFRectt rect( x, y, x + src_area.Width(), y + src_area.Height());
				NuclearFRectt rectstroke;
				int sw = (int)src_area.Width();
				src_area /= static_cast<float>( texture_w);

				//--------“‘œ¬π˝≥Ã”√XPRE_FONTOUTLINE¥˙ÃÊ–ß¬ ”–“ª∂®Ã·…˝£¨–ßπ˚”–“ª∂®œ¬Ωµ:(
				NuclearFRectt rectstrokes[8];
				DrawBatchPictureParam params;
				params.handle = hPic;
				params.colorCount = 1;
				params.pColor = reinterpret_cast<const NuclearColor*>(&strokeColor);
				params.rectCount = 8;
				params.pRect = rectstrokes;
				params.pSrcrect = &src_area;
				
				rectstrokes[0] = rect + NuclearFPoint( 1.0f, .0f);
				rectstrokes[1] = rect + NuclearFPoint( 1.0f, 1.0f);
				rectstrokes[2] = rect + NuclearFPoint( .0f, 1.0f);
				rectstrokes[3] = rect + NuclearFPoint( -1.0f, 1.0f);
				rectstrokes[4] = rect + NuclearFPoint( -1.0f, .0f);
				rectstrokes[5] = rect + NuclearFPoint( -1.0f, -1.0f);
				rectstrokes[6] = rect + NuclearFPoint( .0f, -1.0f);
				rectstrokes[7] = rect + NuclearFPoint( 1.0f, -1.0f);

				renderer->DrawBatchPicture( params);
				//--------------

				HPics.push_back( hPic);
				dests.push_back( rect);
				areas.push_back( src_area);
				xpos.push_back( x);

				full_area.left += sw;
				x += sw;
			}

//			sNuclearFontOutlineParam OutlineParam;
			DrawPictureParam param;
			param.pColor = color;
			param.colorCount = 4;
			for ( int i = 0; i < (int)HPics.size(); ++ i)
			{
				param.handle = HPics[i];
				param.pRect = &dests[i];
				param.pSrcrect = &areas[i];
 
//  				renderer->SelectRenderEffect(XPRE_FONTOUTLINE);
//  				NuclearPictureInfo picinfo;
//  				renderer->GetPictureInfo(HPics[i], picinfo);
//  				OutlineParam.dwOutlineColor = strokeColor;
//  				OutlineParam.nTextureHeight = picinfo.m_nPicHeight;
//  				OutlineParam.nTextureWidth = picinfo.m_nPicWidth;
//  				renderer->SetShaderParam(XPRE_FONTOUTLINE, &OutlineParam);
//  
//  				renderer->DrawPicture( param);
//  				renderer->SelectRenderEffect(XPRE_NULL);
				renderer->DrawPicture( param);
			}
		}
	}	
}

int CTextBlock::FillBitmap()
{
	int w = 0;

	for ( FONTTEX_VEC::iterator it = m_fontTexes.begin(); it != m_fontTexes.end(); ++ it)
	{
		int x = 0;
		if ( !m_text.empty())
		{
#if (defined WIN7_32)
			LPCTSTR text = LPCTSTR(m_text.c_str());
#else
			LPCTSTR text = m_text.c_str();
#endif
			int fontSize = m_pFontMan->GetFontTypeHeight(m_fontType);
			//CFontTexPool* pTexPool = m_pFontMan->GetFontTexturePool();
			//if (pTexPool != NULL)
			//{
			//	CFontTexture* pTex = pTexPool->GetFontTexture(*it);
			//	if (pTex != NULL)
			//	{
			//		int texSize = CFontTexPool::RENDER_TEX_SIZE[pTex->GetTextureLevel()];
			//		//m_pFontMan->FillBitmapColor(*it, 0, 0, texSize, fontSize+INTER_GLYPH_PAD_SPACE, 0);
			//	}
			//}			
			while ( *text)
			{
				x += m_pFontMan->FillBitmap( *it, x, INTER_GLYPH_PAD_SPACE, *text, m_fontType, m_fontColor, m_bkColor, m_bUnderline);
				text ++;
			}
			m_pFontMan->FillBitmapColor(*it, x, INTER_GLYPH_PAD_SPACE, INTER_GLYPH_PAD_SPACE, fontSize, 0);
		}
		CFontTexture* pft = m_pFontMan->GetFontTexturePool()->GetFontTexture( *it);
		pft->SetTextSize( x, pft->GetTextHeight());
		w += x;
	}

	return w;
}

void CTextBlock::Release()
{
	CFontTexPool& texpool = *m_pFontMan->GetFontTexturePool();

	for ( FONTTEX_VEC::iterator it = m_fontTexes.begin(); it != m_fontTexes.end(); ++ it)
	{
		texpool.Release( *it);
	}
	m_fontTexes.clear();

	m_fontColor = 0xffffffff; 
	m_fontGradualColor = 0xffffffff;
	m_bkColor = 0x00000000;
	m_effectColor = 0x00000000;
	m_effectType = FET_NONE;
	m_bUnderline = false;
	m_text.clear();
}

}