#ifndef __Nuclear_FONTTEXTURE_H__
#define __Nuclear_FONTTEXTURE_H__

#if _MSC_VER > 1000
#pragma once
#endif

#include <bitset>
#include "nurenderer.h"

#include "nuport.h"
#include <deque>

namespace Nuclear
{
	typedef int FontTextureHandle;
	const FontTextureHandle INVALID_FONTTEXTURE_HANDLE = 0;

	class CFontTextureInfo;
	class CFontTexture;

	class CFontTexPool
	{
		friend class CFontManager;
		friend class CFontTexture;
		friend class CFontTextureInfo;
		friend class CTextBlock;

	public:
		typedef std::map<FontTextureHandle, CFontTexture*> CFontTextureMap;

	protected:
		static const int MIN_FONT_TEX_HEIGHT = 16;			// 纹理块中行的最小单位, 所分配出的字体纹理高度为该值的整数倍
		static const int RENDER_TEX_LEVEL = 4;				// 纹理大小级别数量
		static const int RENDER_TEX_SIZE[RENDER_TEX_LEVEL];	// 目前使用级别纹理宽度为: 32, 64, 128, 256, 长度超出纹理宽度的, 将利用多行纹理进行拼接
		static const int MAX_RENDER_TEX_SIZE = 256;
		static const int MAX_FONT_TEX_PER_RENDER_TEX = MAX_RENDER_TEX_SIZE / MIN_FONT_TEX_HEIGHT;

		typedef std::bitset<MAX_FONT_TEX_PER_RENDER_TEX>	ALLOC_FLAG;	// 纹理分配标志

		struct TextureRes
		{
			PictureHandle		hPicture;
			ALLOC_FLAG			allocFlag;
			unsigned char		textureLevel;

			TextureRes() : allocFlag( 0) {}
			TextureRes( PictureHandle _hPic) : hPicture( _hPic), allocFlag( 0) {}
			void ReleaseTextureRes( int start, int blocks)	// 只是在将纹理块相应位置标记设为可用
			{
				ALLOC_FLAG flag;
				for ( int i = start; i < start + blocks; i ++)
					flag.set( i);
				flag.flip();
				allocFlag &= flag;
			}
		};
		typedef std::deque<TextureRes>	TEX_RES_REQ;

	public:
		CFontTexPool( Renderer*	pRenderer);
		virtual ~CFontTexPool();

	public:
		void Release();
		FontTextureHandle Request( Renderer& renderer, int fontSize, int textWidth);
		void Release( FontTextureHandle hFontTexture);
		CFontTexture* GetFontTexture( FontTextureHandle hFontTexture);

	private:
		TextureRes* AllocRenderTex( Renderer& renderer, int texLevel);
		CFontTextureInfo AllocFontTex( TextureRes& texRes, int blocks, int texLevel);

		// segmentNum 纹理片段数, 对于申请长度超过最大级(当前为256)的, 需要拆分到多个纹理片段中存储, 由字体高度决定, 获取字体高度所需的行数(一行高度是 MIN_FONT_TEX_HEIGHT 个像素)
		void CalcFontSize(int textWidth, int textHeight, int& texLevel, int& segmentNum, int& blocks, int& lastBlockWidth, int& lastBlockLevel);

		int GenerateID();
		void gc();

	private:
		Renderer*		m_pRenderer;
		TEX_RES_REQ		m_texReses;
		TextureRes*		m_lastTexRes;	// 最近使用的纹理资源
		CFontTextureMap	m_fonttexture;
	};

	class CFontTextureInfo
	{
		friend class CFontTexture;
		friend class CTextBlock;

		CFontTexPool::TextureRes*	m_res;			// 申请到的纹理
		unsigned char				m_startBlock;	// 文本在纹理中的起始位置(以block为单位, 每个block宽度为纹理宽度, 高度为MIN_FONT_TEX_HEIGHT)
		unsigned char				m_blockNum;		// 在该纹理中连续占用的block数	

	public:
		CFontTextureInfo();
		CFontTextureInfo(  CFontTexPool::TextureRes* res, unsigned char startBlock, int blockNum);
		virtual ~CFontTextureInfo();

	public:
		void ReleaseTextureRes();
	
		CFontTexPool::TextureRes* GetTextureRes();

		bool IsValid() { return NULL != m_res; }
	};

	typedef std::vector<CFontTextureInfo> CFontTextureInfoVector;	// 一行文本的纹理可能分布在多块纹理中
	class CFontTexture
	{
		friend class CFontTexPool;
		friend class CFontManager;

		CFontTextureInfoVector	m_resInfos;
		int						m_textWidth;
		int						m_textHeight;

	public:
		CFontTexture();
		CFontTexture(  CFontTextureInfoVector& fontTexInfos);
		virtual ~CFontTexture();

	public:
		int	GetTextureWidth( int n);			// 获取所用级别的纹理宽度
		int	GetTextureHeight();					// 获取一行的纹理高度, 取决于字体高度
		int	GetTextWidth();						// 获取文本宽度
		int	GetTextHeight();					// 获取文本高度

		int GetTextureInfoCount() const;
		int GetTextureLevel();						// 获取纹理级别
		CFontTextureInfo& GetTextureInfo( int n);	// 获取某块文本纹理

		void Release();

		void SetTextSize( int w, int h);

	protected:

		bool SetPictureData( Renderer& renderer, const void *data, int size, const RECT* pRect);
	};

}

#endif