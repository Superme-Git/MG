#include "BlockDecoder.h"
#include "../Picture.h"
#include "DDSDecoder.h"
#include <iostream>

namespace MHZX
{
	//
	// pImageOriAddr - the address of the image origin pixel.
	// bpp - bytes per pixel of the image.
	// 
	static inline unsigned char * PixelAddr(
		unsigned char* pImageOriAddr, int bpp, int width, int x, int y)
	{
		return pImageOriAddr + width * bpp * y + x * bpp;
	}

	static const unsigned char c5to8bits[32] =
	{
		0,   8,  16,  25,  33,  41,  49,  58,
		66,  74,  82,  90,  99, 107, 115, 123,
		132, 140, 148, 156, 165, 173, 181, 189,
		197, 206, 214, 222, 230, 239, 247, 255
	};

	class CBlockDecoderA8R8G8B8 : public IBlockDecoder
	{
	public:
		virtual void Decode (
			unsigned char* pDestPicture, int pictureWidth, int pictureHeight,
			const unsigned char* pSrcBlock, unsigned int /*size*/, 
			int width, int height, int x_offset, int y_offset )
		{
			int blockpitch = width * 4;
			int linepitch = pictureWidth * 4;
			unsigned char * pDestAddr = PixelAddr(pDestPicture, 4, pictureWidth, x_offset, y_offset);

			if( blockpitch == linepitch )
			{
				memcpy( pDestAddr, pSrcBlock, blockpitch * height ); 
			}
			else
			{
				for( int i = 0; i < height; i ++ )
				{
					memcpy( pDestAddr, pSrcBlock, blockpitch ); 
					pDestAddr  += linepitch;
					pSrcBlock += blockpitch;
				}
			}
		}
	};

	typedef CBlockDecoderA8R8G8B8 CBlockDecoderX8R8G8B8;

	class CBlockDecoderR8G8B8 : public IBlockDecoder
	{
	public:
		virtual void Decode (
			unsigned char* pDestPicture, int pictureWidth, int pictureHeight,
			const unsigned char* pSrcBlock, unsigned int /*size*/, 
			int width, int height, int x_offset, int y_offset )
		{
			unsigned char * pDestAddr = NULL;
			
			for( int i = 0; i < height; i ++ )
			{
				pDestAddr = PixelAddr(pDestPicture, 4, pictureWidth, x_offset, y_offset + i);
				for( int j = 0; j < width; j ++ )
				{
					pDestAddr[0] = *pSrcBlock++;		// B
					pDestAddr[1] = *pSrcBlock++;		// G
					pDestAddr[2] = *pSrcBlock++;		// R
					pDestAddr[3] = 255;					// A
					pDestAddr += 4;
				}
			}
		}
	};
	
	class CBlockDecoderA8 : public IBlockDecoder
	{
	public:
		virtual void Decode (
			unsigned char* pDestPicture, int pictureWidth, int pictureHeight,
			const unsigned char* pSrcBlock, unsigned int /*size*/, 
			int width, int height, int x_offset, int y_offset )
		{
			unsigned char * pDestAddr = NULL;
			
			for( int i = 0; i < height; i ++ )
			{
				pDestAddr = PixelAddr(pDestPicture, 4, pictureWidth, x_offset, y_offset + i);
				for( int j = 0; j < width; j ++ )
				{
					*(unsigned int*)pDestAddr = 0;
					pDestAddr[3] = *pSrcBlock; // A8
					pDestAddr		+= 4;
					pSrcBlock		+= 4;
				}
			}
		}
	};

	class CBlockDecoderA4R4G4B4 : public IBlockDecoder
	{
	public:
		virtual void Decode (
			unsigned char* pDestPicture, int pictureWidth, int pictureHeight,
			const unsigned char* pSrcBlock, unsigned int /*size*/, 
			int width, int height, int x_offset, int y_offset )
		{
			unsigned char * pDestAddr = NULL;
			
			for( int i = 0; i < height; i ++ )
			{
				pDestAddr = PixelAddr(pDestPicture, 4, pictureWidth, x_offset, y_offset + i);
				for( int j = 0; j < width; j ++ )
				{
					pDestAddr[0] = (*pSrcBlock&0x0F) + ( (*pSrcBlock&0x0F) << 4 ) ;	// B
					pDestAddr[1] = (*pSrcBlock&0xF0) + ( (*pSrcBlock&0xF0) >> 4 );	// G 
					pSrcBlock ++;	
					pDestAddr[2] = (*pSrcBlock&0x0F) + ( (*pSrcBlock&0x0F) << 4 ) ;	// R
					pDestAddr[3] = (*pSrcBlock&0xF0) + ( (*pSrcBlock&0xF0) >> 4 );	// A
					pSrcBlock ++;
					pDestAddr += 4;
				}
			}
		}
	};

	class CBlockDecoderA1R5G5B5 : public IBlockDecoder
	{
	public:
		virtual void Decode (
			unsigned char* pDestPicture, int pictureWidth, int pictureHeight,
			const unsigned char* pSrcBlock, unsigned int /*size*/, 
			int width, int height, int x_offset, int y_offset )
		{
			unsigned char * pDestAddr = NULL;			
			const unsigned int* pSrcAddr16 = (const unsigned int*)pSrcBlock; 

			for( int i = 0; i < height; i ++ )
			{
				pDestAddr = PixelAddr(pDestPicture, 4, pictureWidth, x_offset, y_offset + i);
				for( int j = 0; j < width; j ++ )
				{
					*pDestAddr++ = c5to8bits[ (*pSrcAddr16&0x001F ) ];		// B5
					*pDestAddr++ = c5to8bits[ (*pSrcAddr16&0x03E0 ) >> 5 ];	// G5 
					*pDestAddr++ = c5to8bits[ (*pSrcAddr16&0x7C00 ) >> 10 ];	// R5
					*pDestAddr++ = (*pSrcAddr16&0x8000) ? 0xFF : 0 ;			// A1
					pSrcAddr16 ++;
				}
			}
		}
	};

	typedef CBlockDecoderA1R5G5B5 CBlockDecoderX1R5G5B5;
	
	class CBlockDecoderR5G6B5 : public IBlockDecoder
	{
		typedef struct
		{
			unsigned char blue;
			unsigned char green;
			unsigned char red;
			unsigned char alpha;
		} pixel32;
	public:
		virtual void Decode (
			unsigned char* pDestPicture, int pictureWidth, int pictureHeight,
			const unsigned char* pSrcBlock, unsigned int /*size*/, 
			int width, int height, int x_offset, int y_offset )
		{
			unsigned char * pDestAddr = NULL;
			
			//const unsigned int* pSrcAddr16 = (const unsigned int*)pSrcBlock;
			
			//pDestAddr = pDestPicture + (pictureHeight - y_offset - 1)* pictureWidth*4 + x_offset * 4;
			pDestAddr = pDestPicture + y_offset* pictureWidth*4 + x_offset * 4;

			for( int i = 0; i < height; i ++ )
			{
				//pDestAddr = PixelAddr(pDestPicture, 4, pictureWidth, x_offset, y_offset + i);
				//pixel32* pPixel = ((pixel32*)pDestAddr) - i * pictureWidth;
				pixel32* pPixel = ((pixel32*)pDestAddr) + i*pictureWidth;
				for( int j = width; j > 0; j-- )
				{
					int t = *pSrcBlock++;
					t += (*pSrcBlock++)<<8;
					pPixel->blue = c5to8bits[t & 0x1F];		// B5
					// 255.0/63.0 = 4.047619047619047619047619047619
					t >>= 5;
					pPixel->green =  (unsigned char)((t & 0x3F)*255.0/63.0);	// G6 
					t >>= 6;
					pPixel->red = c5to8bits[t & 0x1F];	// R5
					pPixel->alpha = 0xFF;										// A always 255
					pPixel++;
					//pSrcAddr16 ++;
				}
			}
		}
	};

	class CBlockDecoderDDS : public IBlockDecoder
	{
	public:
		virtual void Decode (
			unsigned char* pDestPicture, int pictureWidth, int pictureHeight,
			const unsigned char* pSrcBlock, unsigned int size,
			int width, int height, int x_offset, int y_offset )
		{	
			DECODER::CDDSDecoder _decoder;
			if( !_decoder.Init() )
			{
				std::wcerr<< L"初始化DDS Decoder失败！" << std::endl;
				return;
			}

			MIX::CBitmap bmp;
			if( !_decoder.Decode( pSrcBlock, size, bmp) )
			{
				std::wcerr<< L"DDS Decode失败！" << std::endl;
				return;
			}

			unsigned nWidth = bmp.GetWidth();
			unsigned nHeight = bmp.GetHeight(); 
			if( width != nWidth )
			{
				std::wcerr << L" block width is not same as decoded. " << std::endl;
				std::wcerr << L" block.width : " << width << std::endl;
				std::wcerr << L" bitmap width : " << nWidth << std::endl;

				return;
			}

			if( height != nHeight )
			{
				std::wcerr << L" block height is not same as decoded. " << std::endl;
				std::wcerr << L" block height : " << height << std::endl;
				std::wcerr << L" bitmap height : " << nHeight << std::endl;

				return ;
			}

			// 调用其它解码器再对MIX::CBitmap进行解码。
			IBlockDecoder* pAdapter =  CBlockDecoderFactory::CreateBlockDecoder( bmp.GetFormat()  );
			if (pAdapter == NULL)
			{
				std::wcerr << L"No find decoder for image of format: " << bmp.GetFormat() << std::endl;
				return;
			}

			pAdapter->Decode( pDestPicture, pictureWidth, pictureHeight,
				 bmp.GetPixels(), bmp.GetSize(),
				 width, height, x_offset, y_offset );
		}
	};

	template < class BaseClass >
	class CStaticSingletonTemplate : public BaseClass
	{
	public:
		static CStaticSingletonTemplate<BaseClass> & GetInstance()
		{
			static CStaticSingletonTemplate<BaseClass> instance;
			return instance;
		}
		virtual void Release()
		{
			// Do not need to delete static instance.
		}
	};

	IBlockDecoder* CBlockDecoderFactory::CreateBlockDecoder(int format)
	{
		switch (format)
		{
		case PIX_FMT_R5G6B5:
			return &CStaticSingletonTemplate<CBlockDecoderR5G6B5>::GetInstance();
		case PIX_FMT_A4R4G4B4:
			return &CStaticSingletonTemplate<CBlockDecoderA4R4G4B4>::GetInstance();
		case PIX_FMT_R8G8B8:
			return &CStaticSingletonTemplate<CBlockDecoderR8G8B8>::GetInstance();
		case PIX_FMT_A8R8G8B8:
		case PIX_FMT_X8R8G8B8:
			return &CStaticSingletonTemplate<CBlockDecoderA8R8G8B8>::GetInstance();
		case PIX_FMT_A8:
			return &CStaticSingletonTemplate<CBlockDecoderA8>::GetInstance();
		case PIX_FMT_A1R5G5B5:
		case PIX_FMT_X1R5G5B5:
			return &CStaticSingletonTemplate<CBlockDecoderA1R5G5B5>::GetInstance();
		case PIX_FMT_DXT1:
		case PIX_FMT_DXT1a:
		case PIX_FMT_DXT3:
		case PIX_FMT_DXT5:
		case PIX_FMT_DXT5n:
			return &CStaticSingletonTemplate<CBlockDecoderDDS>::GetInstance();
		default:
			std::wcerr<< L"不支持的图像格式！format: " << format << std::endl;
			assert(!"不支持的图像格式！");
			return NULL;
		}
	}

}