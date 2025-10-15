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
		PSF_BITMAP		= 0x0000,	//! �Ѿ���������ؼ�
		PSF_DDS			= 0x0001,	//! dds���ؼ�
		PSF_NATIVE_TGA	= 0x0101,	//! ԭʼ��tga�ļ�
		PSF_NATIVE_DDS	= 0x0102,	//! ԭʼ��dds�ļ�
		PSF_NATIVE_PNG	= 0x0103,	//! ԭʼ��png�ļ�
		PSF_NATIVE_BMP	= 0x0104,	//! ԭʼ��bmp�ļ�
		PSF_NATIVE_JPG	= 0x0105,	//! ԭʼ��jpg�ļ�
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
			// 09.03.16 ����һ�ΰ汾��,ͳһͼƬ��׺,ȥ��ͼƬ�ڵ�������Ϣ
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

		// ���� m_PictureDesc.format����ĸ�ʽ���н��롣
		// ������ PSF_NATIVE_TGA/PSF_NATIVE_BMP/PSF_DDS/PSF_BITMAP��ͼ���ʽ��
		void DecodeAndFillBlock( const unsigned char* pBitBlock, unsigned int size, 
			int width, int height, int x_offset, int y_offset );

		// ���ݲ���format����ĸ�ʽ���н��롣������PSF_NATIVE_DDSͼ���ʽ��������Ҫ���ν��롣
		void DecodeAndFillBlock( const unsigned char* pBitBlock, unsigned int size, 
			int width, int height, int x_offset, int y_offset, int format );

		void FillImageData( const unsigned char* pSrcImageData, unsigned int size );

		void DumpAsXML( std::wostream& os ) const;

		const unsigned char* GetBitmapData() const
		{
			return m_pBitArray;
		}

	protected:
		// ��ֹ����/����
		CPicture(const CPicture& rOther){};
		const CPicture& operator = (const CPicture& rOther){};
		unsigned char*  m_pBitArray;
	};
}