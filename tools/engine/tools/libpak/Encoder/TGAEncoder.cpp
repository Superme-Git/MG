#include "TGAEncoder.h"
#include "../../../pfs/pfs2.h"
#include "../Picture.h"
#include "ImageEncoderHelper.h"

namespace HELPER
{
	typedef unsigned char UINT8;
	typedef unsigned short UINT16;

	// descriptor: 00vhaaaa 
	// h horizontal flip 
	// v vertical flip 
	// a alpha bits 
#define TGA_IDESC_HFLIP	0x10
#define	TGA_IDESC_VFLIP	0x20

#pragma pack( push )
#pragma pack( 1 )
	typedef struct TGAHeader
	{
		UINT8	ID;				// Byte 0
		UINT8	ctype;			// Byte 1
		UINT8	itype;			// Byte 2
		UINT16	mincolor;		// Byte 3, 4
		UINT16	colors;			// Byte 5, 6
		UINT8	colorsize;		// Byte 7
		UINT16	originx;		// Byte 8, 9
		UINT16	originy;		// Byte 10, 11
		UINT16	width;			// Byte 12,13
		UINT16	height;			// Byte 14,15
		UINT8	bpp;			// Byte 16
		UINT8	idesc;			// Byte 17
	} TGAHeader;
	// if ctype is 1, itype would be also 1. colors, and colorsize should also be filled. 
	// itype 1: colormapped RGB, 2: uncompressed RGB, 3 : uncompressed gray-scale
		
#pragma pack( pop )

} // namespace HELPER


namespace MHZX
{

	CTGAEncoder::CTGAEncoder(void)
	{	
		wcscpy_s( szExtensionBuffer, L".tga" );
	}

	CTGAEncoder::~CTGAEncoder(void)
	{
	}
	
	void CTGAEncoder::SetDefaultFileExtension(const wchar_t* lpszNewExt)
	{
		wcscpy_s( szExtensionBuffer, lpszNewExt );
	}

	const wchar_t* CTGAEncoder::GetDefaultFileExtension() const
	{
		return szExtensionBuffer;
	}

	bool CTGAEncoder::Open()
	{
		return true;
	}

	bool CTGAEncoder::Encode(
		const unsigned char* pBits, 
		unsigned int nWidth,
		unsigned int nHeight,
		unsigned int nBitCount, // todo
		bool bHasAlphaChannel,
		PFS::CBaseFile& rFile,
		unsigned int& rNewFormat )
	{
		HELPER::TGAHeader header = {0};
		header.itype = 2; // 1: colormapped RGB, 2: uncompressed RGB, 3 : uncompressed gray-scale
		header.originx = 0;
		header.originy = 0;
		header.width = nWidth;
		header.height = nHeight;
		header.bpp = nBitCount;				// bits per pixel.
		header.idesc = TGA_IDESC_VFLIP;// Top-down, non-interlaced

		if( rFile.Write( &header, sizeof(header) ) != sizeof(header) )
		{
			return false;
		}

		int bufferSize = nWidth * nHeight * nBitCount / 8;

		if(rFile.Write(  pBits, bufferSize ) != bufferSize )
		{
			return false;
		}

		rNewFormat = MAKELONG(PIX_FMT_A8R8G8B8, PSF_NATIVE_TGA); // reset format& storage
		return true;
	}
		
	bool CTGAEncoder::Encode(
		CPicture& rPicture,
		PFS::CBaseFile& rFile,
		unsigned int& rNewFormat )
	{
		HELPER::TGAHeader header = {0};
		header.itype = 2; // 1: colormapped RGB, 2: uncompressed RGB, 3 : uncompressed gray-scale
		header.originx = rPicture.m_PictureDesc.x_offset;
		header.originy = rPicture.m_PictureDesc.y_offset;
		header.width = rPicture.m_PictureDesc.width;
		header.height = rPicture.m_PictureDesc.height;
		header.bpp = 32;				// bits per pixel.
		header.idesc = TGA_IDESC_VFLIP;// Top-down, non-interlaced

		if( rFile.Write( &header, sizeof(header) ) != sizeof(header) )
		{
			return false;
		}

		int bufferSize = rPicture.m_PictureDesc.width * rPicture.m_PictureDesc.height * 4;
		if(rFile.Write(  rPicture.GetBitmapData(), bufferSize ) != bufferSize )
		{
			return false;
		}

		rNewFormat = MAKELONG(PIX_FMT_A8R8G8B8, PSF_NATIVE_TGA); // reset format& storage

		return true;
	}


	void CTGAEncoder::Close()
	{

	}


} // namespace MHZX