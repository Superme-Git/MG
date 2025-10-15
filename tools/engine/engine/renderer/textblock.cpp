#include "stdafx.h"
#include "renderer.h"
#include "fonttexture.h"
#include "fontmanager.h"
#include "textblock.h"
#include <hash_map>

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
	m_text.assign( text);

	if ( m_fontTexes.empty() || INVALID_FONTTEXTURE_HANDLE == m_fontTexes[0])
		return false;

	return FillBitmap() > 0;
}

LPCTSTR CTextBlock::GetText()
{
	return m_text.c_str();
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
	return m_pFontMan->GetStringWidth( m_text.c_str(), m_fontType);
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
	if ( 0xff000000 & m_effectColor)
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

void CTextBlock::Render( Renderer* renderer, const FPOINT& textPos, const FPOINT& fScale, const FRECT* src)
{
	FRECT textRect = NULL != src ? *src : FRECT( .0f, .0f, (float)GetTextWidth(), (float)GetTextHeight());

	XPCOLOR color[4] = { m_fontColor, m_fontColor, m_fontGradualColor, m_fontGradualColor};
	DrawPictureParam param;
	FRECT rect, src_area;
	param.pColor = color;
	param.colorCount = 4;

	int	nTotalWidth = GetTextWidth();
	int nTotalHeight = GetTextHeight();
	float fLeftx = textPos.x - fScale.x* nTotalWidth/2.0f;
	float fTopy = textPos.y - fScale.y* nTotalHeight/2.0f;

	for( FONTTEX_VEC::iterator it = m_fontTexes.begin(); it != m_fontTexes.end(); ++it)
	{
		FRECT full_area( textRect.left, textRect.top, textRect.right, textRect.bottom);	// 要渲染的整体文字区域(将文本纹理视为一整块)
		CFontTexture* tex = m_pFontMan->GetFontTexturePool()->GetFontTexture( *it);
		if(tex)
		{
			int text_w = tex->GetTextWidth();		// 文本纹理总宽度
			int text_h = tex->GetTextHeight();		// 每行文本纹理实际高度
			if ( textRect.top >= text_h)
				return;

			int passlen = 0;
			for ( int i = 0; i < tex->GetTextureInfoCount() && full_area.left < full_area.right; i ++)
			{
				CFontTextureInfo& fti = tex->GetTextureInfo( i);
				if ( !fti.IsValid())	
					return;

				int texture_w = tex->GetTextureWidth( i);	// 每块纹理宽度
				passlen += texture_w;
				if ( full_area.left >= passlen)
					continue;

				src_area.left = full_area.left - passlen + texture_w;
				src_area.top = fti.m_startBlock * CFontTexPool::MIN_FONT_TEX_HEIGHT + full_area.top;
				src_area.right = src_area.left + min( full_area.Width(), min( text_w - full_area.left, passlen - full_area.left));
				src_area.bottom = src_area.top + min( text_h - full_area.top, full_area.Height());

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

void CTextBlock::Render( Renderer& renderer, float x, float y, const FRECT* src)
{
	FRECT textRect = NULL != src ? *src : FRECT( .0f, .0f, (float)GetTextWidth(), (float)GetTextHeight());

	if ( m_effectType == FET_STROKE)
		StrokeRender( &renderer, x, y, &textRect, m_fontColor, m_fontGradualColor, m_bkColor, m_effectColor);
	else if ( m_effectType == FET_SHADOW)
		ShadowRender( &renderer, x, y, &textRect, m_fontColor, m_fontGradualColor, m_bkColor, m_effectColor);
	else
		Render( &renderer, x, y, &textRect, m_fontColor, m_fontGradualColor, m_bkColor);
}

void CTextBlock::Render( Renderer* renderer, float x, float y, const FRECT* src, DWORD fontColor, DWORD gradualColor, DWORD bkColor)
{
	XPCOLOR color[4] = { fontColor, fontColor, gradualColor, gradualColor};
	DrawPictureParam param;
	FRECT rect, src_area;
	param.pColor = color;
	param.colorCount = 4;

	for ( FONTTEX_VEC::iterator it = m_fontTexes.begin(); it != m_fontTexes.end(); ++it)
	{
		FRECT full_area( src->left, src->top, src->right, src->bottom);	// 要渲染的整体文字区域(将文本纹理视为一整块)
		CFontTexture* tex = m_pFontMan->GetFontTexturePool()->GetFontTexture( *it);
		if ( tex)
		{
			int text_w = tex->GetTextWidth();		// 文本纹理总宽度
			int text_h = tex->GetTextHeight();		// 每行文本纹理实际高度
			if ( src->top >= text_h)
				return;

			int passlen = 0;
			for ( int i = 0; i < tex->GetTextureInfoCount() && full_area.left < full_area.right; i ++)
			{
				CFontTextureInfo& fti = tex->GetTextureInfo( i);
				if ( !fti.IsValid())	
					return;

				int texture_w = tex->GetTextureWidth( i);	// 每块纹理宽度
				passlen += texture_w;
				if ( full_area.left >= passlen)
					continue;

				src_area.left = full_area.left - passlen + texture_w;
				src_area.top = fti.m_startBlock * CFontTexPool::MIN_FONT_TEX_HEIGHT + full_area.top;
				src_area.right = src_area.left + min( full_area.Width(), min( text_w - full_area.left, passlen - full_area.left));
				src_area.bottom = src_area.top + min( text_h - full_area.top, full_area.Height());

				if ( 0xff000000 & bkColor)
					renderer->DrawBox( FRECT( x, y, x + src_area.Width(), y + src_area.Height()), bkColor);
				
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

void CTextBlock::ShadowRender( Renderer* renderer, float x, float y, const FRECT* src, DWORD fontColor, DWORD gradualColor, DWORD bkColor, DWORD shadowColor)
{
	XPCOLOR color[4] = { fontColor, fontColor, gradualColor, gradualColor};
	DrawPictureParam param;

	for ( FONTTEX_VEC::iterator it = m_fontTexes.begin(); it != m_fontTexes.end(); ++it)
	{
		FRECT full_area( src->left, src->top, src->right, src->bottom);	// 要渲染的整体文字区域(将文本纹理视为一整块)
		CFontTexture* tex = m_pFontMan->GetFontTexturePool()->GetFontTexture( *it);
		if ( tex)
		{
			int text_w = tex->GetTextWidth();		// 文本纹理总宽度
			int text_h = tex->GetTextHeight();		// 每行文本纹理实际高度
			if ( src->top >= text_h)
				return;

			int passlen = 0;
			for ( int i = 0; i < tex->GetTextureInfoCount() && full_area.left < full_area.right; i ++)
			{
				CFontTextureInfo& fti = tex->GetTextureInfo( i);
				if ( !fti.IsValid())	
					return;

				int texture_w = tex->GetTextureWidth( i);	// 每块纹理宽度
				passlen += texture_w;
				if ( full_area.left >= passlen)
					continue;

				FRECT src_area;
				src_area.left = full_area.left - passlen + texture_w;
				src_area.top = fti.m_startBlock * CFontTexPool::MIN_FONT_TEX_HEIGHT + full_area.top;
				src_area.right = src_area.left + min( full_area.Width(), min( text_w - full_area.left, passlen - full_area.left));
				src_area.bottom = src_area.top + min( text_h - full_area.top, full_area.Height());

				if ( 0xff000000 & bkColor)
					renderer->DrawBox( FRECT( x, y, x + src_area.Width(), y + src_area.Height()), bkColor);

				FRECT rect( x + 1.0f, y + 1.0f, x + src_area.Width() + 1.0f, y + src_area.Height() + 1.0f);
				int sw = (int)src_area.Width();
				src_area /= static_cast<float>( texture_w);

				param.handle = fti.GetTextureRes()->hPicture;
				param.pRect = &rect;
				param.pSrcrect = &src_area;

				param.pColor = reinterpret_cast<const XPCOLOR*>(&shadowColor);
				param.colorCount = 1;
				renderer->DrawPicture( param);

				rect -= FPOINT( 1.0f, 1.0f);
				param.pColor = color;
				param.colorCount = 4;
				renderer->DrawPicture( param);

				full_area.left += sw;
				x += sw;
			}
		}
	}

}

void CTextBlock::StrokeRender( Renderer* renderer, float x, float y, const FRECT* src, DWORD fontColor, DWORD gradualColor, DWORD bkColor, DWORD strokeColor)
{
	XPCOLOR color[4] = { fontColor, fontColor, gradualColor, gradualColor};

	for ( FONTTEX_VEC::iterator it = m_fontTexes.begin(); it != m_fontTexes.end(); ++it)
	{
		FRECT full_area( src->left, src->top, src->right, src->bottom);	// 要渲染的整体文字区域(将文本纹理视为一整块)
		CFontTexture* tex = m_pFontMan->GetFontTexturePool()->GetFontTexture( *it);
		if ( tex)
		{
			int text_w = tex->GetTextWidth();		// 文本纹理总宽度
			int text_h = tex->GetTextHeight();		// 每行文本纹理实际高度
			if ( src->top >= text_h)
				return;

			std::vector<PictureHandle> HPics;
			std::vector<FRECT> areas;
			std::vector<FRECT> dests;
			std::vector<float> xpos;

			int passlen = 0;
			for ( int i = 0; i < tex->GetTextureInfoCount() && full_area.left < full_area.right; i ++)
			{
				CFontTextureInfo& fti = tex->GetTextureInfo( i);
				if ( !fti.IsValid())	
					return;

				int texture_w = tex->GetTextureWidth( i);	// 每块纹理宽度
				passlen += texture_w;
				if ( full_area.left >= passlen)
					continue;

				FRECT src_area;
				src_area.left = full_area.left - passlen + texture_w;
				src_area.top = fti.m_startBlock * CFontTexPool::MIN_FONT_TEX_HEIGHT + full_area.top;
				src_area.right = src_area.left + min( full_area.Width(), min( text_w - full_area.left, passlen - full_area.left));
				src_area.bottom = src_area.top + min( text_h - full_area.top, full_area.Height());

				if ( 0xff000000 & bkColor)
					renderer->DrawBox( FRECT( x, y, x + src_area.Width(), y + src_area.Height()), bkColor);

				PictureHandle hPic = fti.GetTextureRes()->hPicture;
				FRECT rect( x, y, x + src_area.Width(), y + src_area.Height());
				FRECT rectstroke;
				int sw = (int)src_area.Width();
				src_area /= static_cast<float>( texture_w);

				//--------以下过程用XPRE_FONTOUTLINE代替效率有一定提升，效果有一定下降:(
				FRECT rectstrokes[8];
				DrawBatchPictureParam params;
				params.handle = hPic;
				params.colorCount = 1;
				params.pColor = reinterpret_cast<const XPCOLOR*>(&strokeColor);
				params.rectCount = 8;
				params.pRect = rectstrokes;
				params.pSrcrect = &src_area;
				
				rectstrokes[0] = rect + FPOINT( 1.0f, .0f);
				rectstrokes[1] = rect + FPOINT( 1.0f, 1.0f);
				rectstrokes[2] = rect + FPOINT( .0f, 1.0f);
				rectstrokes[3] = rect + FPOINT( -1.0f, 1.0f);
				rectstrokes[4] = rect + FPOINT( -1.0f, .0f);
				rectstrokes[5] = rect + FPOINT( -1.0f, -1.0f);
				rectstrokes[6] = rect + FPOINT( .0f, -1.0f);
				rectstrokes[7] = rect + FPOINT( 1.0f, -1.0f);

				renderer->DrawBatchPicture( params);
				//--------------

				HPics.push_back( hPic);
				dests.push_back( rect);
				areas.push_back( src_area);
				xpos.push_back( x);

				full_area.left += sw;
				x += sw;
			}

//			XPFontOutlineParam OutlineParam;
			DrawPictureParam param;
			param.pColor = color;
			param.colorCount = 4;
			for ( int i = 0; i < (int)HPics.size(); ++ i)
			{
				param.handle = HPics[i];
				param.pRect = &dests[i];
				param.pSrcrect = &areas[i];
 
//  				renderer->SelectRenderEffect(XPRE_FONTOUTLINE);
//  				PictureInfo picinfo;
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
			LPCTSTR text = m_text.c_str();
			while ( *text)
			{
				x += m_pFontMan->FillBitmap( *it, x, 0, *text, m_fontType, m_fontColor, m_bkColor, m_bUnderline);
				text ++;
			}
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