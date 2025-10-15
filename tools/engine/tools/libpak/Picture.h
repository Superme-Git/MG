#pragma once
#include <string>
#include <vector>

namespace MHZX
{
#pragma pack(2)
	struct PicHeader
	{
		int format;
		short width;
		short height;
		int blockNum;
	};

	struct BlockDesc
	{
		short	width;
		short	height;
		short	x_offset;
		short	y_offset;
		unsigned int size;

		BlockDesc()
		{
			width = 0;
			height = 0;
			x_offset = 0;
			y_offset = 0;
			size = 0;
		}
	};

#pragma pack()

	enum PixelFormat
	{
		PIX_FMT_UNKNOWN = -1,
		PIX_FMT_R5G6B5,
		PIX_FMT_A4R4G4B4,
		PIX_FMT_R8G8B8,
		PIX_FMT_A8R8G8B8,
		PIX_FMT_X8R8G8B8,
		PIX_FMT_A8,
		PIX_FMT_A1R5G5B5,
		PIX_FMT_X1R5G5B5,

		PIX_FMT_DXT1,
		PIX_FMT_DXT1a,
		PIX_FMT_DXT3,
		PIX_FMT_DXT5,
		PIX_FMT_DXT5n,
	};
	
	enum PicStorageFlag
	{
		PSF_BITMAP		= 0x0000,	//! 已经整理的像素集
		PSF_DDS			= 0x0001,	//! dds像素集
		PSF_NATIVE_TGA	= 0x0101,	//! 原始的tga文件
		PSF_NATIVE_DDS	= 0x0102,	//! 原始的dds文件
		PSF_NATIVE_PNG	= 0x0103,	//! 原始的png文件
		PSF_NATIVE_BMP	= 0x0104,	//! 原始的bmp文件
		PSF_NATIVE_JPG	= 0x0105,	//! 原始的jpg文件
	};

	struct PictureDesc
	{
		int version;
		short width;
		short height;
		short x_offset;
		short y_offset;
		int format;
		int storage;
		std::wstring filename;

		PictureDesc()
		{
			 //version = 1 
			// version = 2
			// 09.03.16 升级一次版本号,统一图片后缀,去掉图片内的描述信息
			version = 2;
			width = 0;
			height = 0;
			x_offset = 0;
			y_offset = 0;
			format = PIX_FMT_UNKNOWN;
			storage = 0;
		}
	};

	class CPicture
	{
	public:
		CPicture(void);
		~CPicture(void);
		
		PictureDesc m_PictureDesc;
		std::vector<BlockDesc> m_vBlocks;

		bool AllocBitmap( );

		void FreeBitmap();

		// 根据 m_PictureDesc.format定义的格式进行解码。
		// 适用于 PSF_NATIVE_TGA/PSF_NATIVE_BMP/PSF_DDS/PSF_BITMAP等图像格式。
		void DecodeAndFillBlock( const unsigned char* pBitBlock, unsigned int size, 
			int width, int height, int x_offset, int y_offset );

		// 根据参数format定义的格式进行解码。适用于PSF_NATIVE_DDS图像格式，因其需要二次解码。
		void DecodeAndFillBlock( const unsigned char* pBitBlock, unsigned int size, 
			int width, int height, int x_offset, int y_offset, int format );

		void FillImageData( const unsigned char* pSrcImageData, unsigned int size );

		void DumpAsXML( std::wostream& os ) const;

		const unsigned char* GetBitmapData() const
		{
			return m_pBitArray;
		}

	protected:
		// 禁止复制/拷贝
		CPicture(const CPicture& rOther){};
		const CPicture& operator = (const CPicture& rOther){};
		unsigned char*  m_pBitArray;
	};
}