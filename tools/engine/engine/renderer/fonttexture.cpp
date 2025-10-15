#include "stdafx.h"
#include "fonttexture.h"

namespace Nuclear
{


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

const int CFontTexPool::RENDER_TEX_SIZE[] = { 32, 64, 128, 256};

CFontTexPool::CFontTexPool( Renderer* pRenderer) : m_pRenderer( pRenderer)
{

}

CFontTexPool::~CFontTexPool()
{
	Release();
}


CFontTexPool::TextureRes* CFontTexPool::AllocRenderTex( Renderer& renderer, int texLevel)
{
	if ( texLevel < 0 || texLevel >= RENDER_TEX_LEVEL)
		return NULL;

	PictureHandle hPic = renderer.NewPicture( RENDER_TEX_SIZE[ texLevel], RENDER_TEX_SIZE[ texLevel], NULL, XPTEXFMT_A8R8G8B8);
	if ( INVALID_PICTURE_HANDLE == hPic)
		return NULL;

	m_texReses.push_back( TextureRes( hPic));
	TextureRes& texRes = m_texReses.back();
	texRes.textureLevel = texLevel;
	return &texRes;
}

CFontTextureInfo CFontTexPool::AllocFontTex( TextureRes& texRes, int blocks, int texLevel)
{
	if ( texLevel < 0 || texLevel >= RENDER_TEX_LEVEL)
		return CFontTextureInfo();

	int lines = RENDER_TEX_SIZE[texLevel] / MIN_FONT_TEX_HEIGHT;
	if ( blocks < 0 || blocks > lines)
		return CFontTextureInfo();

	ALLOC_FLAG requestFlag( 0);
	for ( int i = 0; i < blocks; i ++)
		requestFlag.set( i);

	for ( int i = 0; i <= lines - blocks; i ++)
	{
		if ( ( requestFlag & texRes.allocFlag) == 0)
		{
			texRes.allocFlag |= requestFlag;
			m_lastTexRes = &texRes;
			return CFontTextureInfo( &texRes, i, blocks);
		}
		requestFlag <<= 1;
	}

	return CFontTextureInfo();
}

void CFontTexPool::CalcFontSize(int textWidth, int textHeight, int& texLevel, int& segmentNum, int& blocks, int& lastBlockWidth, int& lastBlockLevel)
{
	texLevel = 0;
	while ( texLevel < RENDER_TEX_LEVEL - 1 && (textWidth >= RENDER_TEX_SIZE[ texLevel] || textHeight > RENDER_TEX_SIZE[ texLevel]))
		texLevel++;
	
	lastBlockWidth = textWidth % RENDER_TEX_SIZE[ texLevel];
	segmentNum = textWidth / RENDER_TEX_SIZE[ texLevel] + ( lastBlockWidth == 0 ? 0 : 1);
	blocks = textHeight / MIN_FONT_TEX_HEIGHT + ( textHeight % MIN_FONT_TEX_HEIGHT == 0 ? 0 : 1);

	lastBlockLevel = 0;
	while ( lastBlockLevel < RENDER_TEX_LEVEL - 1 && ( lastBlockWidth > RENDER_TEX_SIZE[ lastBlockLevel] || textHeight > RENDER_TEX_SIZE[ lastBlockLevel]))
	{
		lastBlockLevel++;
	}
}

FontTextureHandle CFontTexPool::Request( Renderer& renderer, int fontSize, int textWidth)
{
	if ( fontSize <= 0 || fontSize > RENDER_TEX_SIZE[ RENDER_TEX_LEVEL - 1])
		return INVALID_PICTURE_HANDLE;

	int texLevel = 0;
	int segmentNum = 0;
	int blocks = 0;
	int lastBlockWidth = 0;
	int lastBlockLevel = 0;
	CalcFontSize( textWidth, fontSize, texLevel, segmentNum, blocks, lastBlockWidth, lastBlockLevel);

	CFontTextureInfoVector t_fontTexInfos;
	while ( segmentNum > 0)
	{
		int level = texLevel;
		if ( segmentNum == 0)
			level = lastBlockWidth == 0 ? texLevel : lastBlockLevel;

		if ( m_texReses.empty())	// 从未分配过纹理
		{
			TextureRes* pTexRes = AllocRenderTex( renderer, level);
			if ( !pTexRes)
				return INVALID_FONTTEXTURE_HANDLE;

			CFontTextureInfo fontTexInfo = AllocFontTex( *pTexRes, blocks, level);
			if ( fontTexInfo.IsValid())
			{
				t_fontTexInfos.push_back( fontTexInfo);
				-- segmentNum;
				continue;
			}
		}
		else if ( m_lastTexRes)					// 存在一个最近使用过的纹理资源
		{
			if ( m_lastTexRes->textureLevel == level)
			{
				CFontTextureInfo fontTexInfo =  AllocFontTex( *m_lastTexRes, blocks, level);
				if ( fontTexInfo.IsValid())
				{
					t_fontTexInfos.push_back( fontTexInfo);
					-- segmentNum;
					continue;
				}
			}
		}

		bool bFound = false;
		for ( TEX_RES_REQ::iterator it = m_texReses.begin(); it != m_texReses.end(); ++ it) // 已经分配过纹理, 寻找一个合适的空闲位置
		{
			TextureRes& texRes = *it;
			if ( level != texRes.textureLevel)
				continue;

			CFontTextureInfo fontTexInfo = AllocFontTex( texRes, blocks, level);
			if ( fontTexInfo.IsValid())
			{
				t_fontTexInfos.push_back( fontTexInfo);
				bFound = true;
				break;
			}
		}

		if ( !bFound) // 查找失败, 重新申请一个新纹理
		{
			TextureRes* pTexRes = AllocRenderTex( renderer, level);
			if ( !pTexRes) // 申请不到纹理
				return INVALID_PICTURE_HANDLE;

			CFontTextureInfo fontTexInfo = AllocFontTex( *pTexRes, blocks, level);
			if ( fontTexInfo.IsValid())
				t_fontTexInfos.push_back( fontTexInfo);
		}

		-- segmentNum;
	}

	if ( t_fontTexInfos.empty())
		return INVALID_PICTURE_HANDLE;
	else
	{
		if ( CFontTexture* pft = new CFontTexture( t_fontTexInfos))
		{
			pft->SetTextSize( textWidth, fontSize);
			int h = GenerateID();
			m_fonttexture[ h] = pft;
			return h;
		}
	}

	return INVALID_FONTTEXTURE_HANDLE;
}

void CFontTexPool::Release( FontTextureHandle hFontTexture)
{
	std::map<FontTextureHandle, CFontTexture*>::iterator it = m_fonttexture.find( hFontTexture);
	if ( it != m_fonttexture.end())
	{
		CFontTexture* pft = it->second;
		pft->Release();
		delete pft;
		m_fonttexture.erase( it);
	}
}

void CFontTexPool::Release()
{
	for ( std::map<FontTextureHandle, CFontTexture*>::iterator it = m_fonttexture.begin(); it != m_fonttexture.end(); ++ it)
	{
		CFontTexture* pft = it->second;
		pft->Release();
		delete pft;
	}
	m_fonttexture.clear();
	this->gc();
}

void CFontTexPool::gc()
{
	for ( TEX_RES_REQ::iterator it = m_texReses.begin(); it != m_texReses.end(); )
	{
		TextureRes& tr = *it;
		if ( 0 == tr.allocFlag.to_ulong())
		{
			m_pRenderer->FreePicture( tr.hPicture);
			it = m_texReses.erase( it);
		}
		else
			++ it;
	}
}

CFontTexture* CFontTexPool::GetFontTexture( FontTextureHandle hFontTexture)
{
	std::map<FontTextureHandle, CFontTexture*>::iterator it = m_fonttexture.find( hFontTexture);
	if ( it != m_fonttexture.end())
	{
		return it->second;
	}
	return NULL;
}

int CFontTexPool::GenerateID()
{
	static int id = 0;
	return ++ id;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CFontTextureInfo::CFontTextureInfo() : m_res( NULL)
{

}

CFontTextureInfo::CFontTextureInfo(  CFontTexPool::TextureRes* res, unsigned char startBlock, int blockNum)
: m_res( res),  m_startBlock( startBlock), m_blockNum( blockNum)
{

}

CFontTextureInfo::~CFontTextureInfo()
{

}

void CFontTextureInfo::ReleaseTextureRes()
{
	if ( m_res)
		m_res->ReleaseTextureRes( m_startBlock, m_blockNum);
}

CFontTexPool::TextureRes* CFontTextureInfo::GetTextureRes()
{
	return m_res;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

CFontTexture::CFontTexture()
{

}

CFontTexture::CFontTexture(  CFontTextureInfoVector& fontTexInfos) : m_resInfos( fontTexInfos.begin(), fontTexInfos.end())
{

}

CFontTexture::~CFontTexture()
{
	Release();
}


int CFontTexture::GetTextureLevel()
{
	return m_resInfos[0].GetTextureRes()->textureLevel;
}

int CFontTexture::GetTextureInfoCount() const
{
	return (int)m_resInfos.size();
}

CFontTextureInfo& CFontTexture::GetTextureInfo( int n)
{
	return m_resInfos[n];
}

void CFontTexture::Release()
{
	for ( CFontTextureInfoVector::iterator it = m_resInfos.begin(); it != m_resInfos.end(); ++ it)
		( *it).ReleaseTextureRes();

	m_resInfos.clear();
}

int	CFontTexture::GetTextureWidth( int n)
{
	return CFontTexPool::RENDER_TEX_SIZE[ m_resInfos[n].GetTextureRes()->textureLevel];
}

int	CFontTexture::GetTextureHeight()
{
	return ( m_textHeight / CFontTexPool::MIN_FONT_TEX_HEIGHT + ( m_textHeight % CFontTexPool::MIN_FONT_TEX_HEIGHT == 0 ? 0 : 1)) * CFontTexPool::MIN_FONT_TEX_HEIGHT;
}

int	CFontTexture::GetTextWidth()
{
	return m_textWidth;
}

int	CFontTexture::GetTextHeight()
{
	return m_textHeight;
}

void CFontTexture::SetTextSize( int w, int h)
{
	m_textWidth = w;
	m_textHeight = h;
}

bool CFontTexture::SetPictureData( Renderer& renderer, const void *data, int size, const RECT* pRect)
{
	if ( !pRect || pRect->top < 0 || pRect->left < 0)
		return false;

	int src_x = pRect->left;
	for ( CFontTextureInfoVector::iterator it = m_resInfos.begin(); it != m_resInfos.end() && src_x >= 0; ++ it)
	{
		CFontTextureInfo& fti = *it;
		if ( !fti.IsValid())
			continue;

		int nTexWidth = CFontTexPool::RENDER_TEX_SIZE[ fti.GetTextureRes()->textureLevel];
		int src_y = fti.m_startBlock * CFontTexPool::MIN_FONT_TEX_HEIGHT + pRect->top;
		if ( src_x < nTexWidth && src_x + pRect->right - pRect->left <= nTexWidth && src_y + pRect->bottom - pRect->top <= nTexWidth &&  pRect->bottom - pRect->top <= nTexWidth)
		{
			CRECT rect(src_x, src_y, src_x + pRect->right - pRect->left, src_y + pRect->bottom - pRect->top);
			renderer.SetPictureData( fti.GetTextureRes()->hPicture, data, size, &rect);
		}
		src_x -= nTexWidth;
	}

	return true;
}

}