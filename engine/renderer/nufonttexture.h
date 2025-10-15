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
		static const int MIN_FONT_TEX_HEIGHT = 16;			// ��������е���С��λ, �����������������߶�Ϊ��ֵ��������
		static const int RENDER_TEX_LEVEL = 4;				// �����С��������
		static const int RENDER_TEX_SIZE[RENDER_TEX_LEVEL];	// Ŀǰʹ�ü���������Ϊ: 32, 64, 128, 256, ���ȳ��������ȵ�, �����ö����������ƴ��
		static const int MAX_RENDER_TEX_SIZE = 256;
		static const int MAX_FONT_TEX_PER_RENDER_TEX = MAX_RENDER_TEX_SIZE / MIN_FONT_TEX_HEIGHT;

		typedef std::bitset<MAX_FONT_TEX_PER_RENDER_TEX>	ALLOC_FLAG;	// ��������־

		struct TextureRes
		{
			PictureHandle		hPicture;
			ALLOC_FLAG			allocFlag;
			unsigned char		textureLevel;

			TextureRes() : allocFlag( 0) {}
			TextureRes( PictureHandle _hPic) : hPicture( _hPic), allocFlag( 0) {}
			void ReleaseTextureRes( int start, int blocks)	// ֻ���ڽ��������Ӧλ�ñ����Ϊ����
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

		// segmentNum ����Ƭ����, �������볤�ȳ������(��ǰΪ256)��, ��Ҫ��ֵ��������Ƭ���д洢, ������߶Ⱦ���, ��ȡ����߶����������(һ�и߶��� MIN_FONT_TEX_HEIGHT ������)
		void CalcFontSize(int textWidth, int textHeight, int& texLevel, int& segmentNum, int& blocks, int& lastBlockWidth, int& lastBlockLevel);

		int GenerateID();
		void gc();

	private:
		Renderer*		m_pRenderer;
		TEX_RES_REQ		m_texReses;
		TextureRes*		m_lastTexRes;	// ���ʹ�õ�������Դ
		CFontTextureMap	m_fonttexture;
	};

	class CFontTextureInfo
	{
		friend class CFontTexture;
		friend class CTextBlock;

		CFontTexPool::TextureRes*	m_res;			// ���뵽������
		unsigned char				m_startBlock;	// �ı��������е���ʼλ��(��blockΪ��λ, ÿ��block���Ϊ������, �߶�ΪMIN_FONT_TEX_HEIGHT)
		unsigned char				m_blockNum;		// �ڸ�����������ռ�õ�block��	

	public:
		CFontTextureInfo();
		CFontTextureInfo(  CFontTexPool::TextureRes* res, unsigned char startBlock, int blockNum);
		virtual ~CFontTextureInfo();

	public:
		void ReleaseTextureRes();
	
		CFontTexPool::TextureRes* GetTextureRes();

		bool IsValid() { return NULL != m_res; }
	};

	typedef std::vector<CFontTextureInfo> CFontTextureInfoVector;	// һ���ı���������ֲܷ��ڶ��������
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
		int	GetTextureWidth( int n);			// ��ȡ���ü����������
		int	GetTextureHeight();					// ��ȡһ�е�����߶�, ȡ��������߶�
		int	GetTextWidth();						// ��ȡ�ı����
		int	GetTextHeight();					// ��ȡ�ı��߶�

		int GetTextureInfoCount() const;
		int GetTextureLevel();						// ��ȡ������
		CFontTextureInfo& GetTextureInfo( int n);	// ��ȡĳ���ı�����

		void Release();

		void SetTextSize( int w, int h);

	protected:

		bool SetPictureData( Renderer& renderer, const void *data, int size, const RECT* pRect);
	};

}

#endif