#include "BMPEncoder.h"
#include "../../../pfs/pfs2.h"
#include "../Picture.h"
#include "ImageEncoderHelper.h"
#include <iostream>
namespace MHZX
{
	CBMPEncoder::CBMPEncoder(void)
	{
		wcscpy_s( szExtensionBuffer, L".bmp" );
	}

	CBMPEncoder::~CBMPEncoder(void)
	{
	}

	void CBMPEncoder::SetDefaultFileExtension(const wchar_t* lpszNewExt)
	{
		wcscpy_s( szExtensionBuffer, lpszNewExt );
	}

	const wchar_t* CBMPEncoder::GetDefaultFileExtension() const
	{
		return szExtensionBuffer;
	}

	bool CBMPEncoder::Open()
	{
		return true;
	}
	
	bool CBMPEncoder::Encode(
		const unsigned char* pBits, 
		unsigned int nWidth,
		unsigned int nHeight,
		unsigned int nBitCount,
		bool bHasAlphaChannel,
		PFS::CBaseFile& rFile,
		unsigned int& rNewFormat )
	{
		BITMAPFILEHEADER fileHeader = {0};
		BITMAPINFOHEADER infoHeader = {0};
		infoHeader.biBitCount = nBitCount;
		infoHeader.biWidth = nWidth;
		infoHeader.biHeight = nHeight;		// // INVERT IMAGE
		infoHeader.biPlanes = 1;
		infoHeader.biSize = sizeof(BITMAPINFOHEADER); // 40.
		infoHeader.biXPelsPerMeter = 2834; // 72bit/inch
		infoHeader.biYPelsPerMeter = 2834; // 72bit/inch
		
		int strideAbs = 0;
		if( infoHeader.biBitCount == 32 )
			strideAbs = infoHeader.biWidth * 4;
		else
			strideAbs = (infoHeader.biWidth * infoHeader.biBitCount + 31 ) >> 5 << 2; 
		UINT sizeOfBits = abs( infoHeader.biHeight ) * strideAbs;
		infoHeader.biSizeImage = sizeOfBits;
		infoHeader.biCompression = BI_RGB;
		
		fileHeader.bfType = 'MB';
		fileHeader.bfOffBits = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER);
		fileHeader.bfSize = fileHeader.bfOffBits + sizeOfBits;
		rFile.Write( &fileHeader, sizeof(BITMAPFILEHEADER) );
		rFile.Write( &infoHeader, sizeof(BITMAPINFOHEADER) ) ;

		bool bRet = false;
		int stride = -strideAbs;
		unsigned char* pLine = (unsigned char*)pBits + sizeOfBits + stride;
		int height = infoHeader.biHeight;
		for( ; height -- ; )
		{
			bRet = ( rFile.Write( pLine, strideAbs ) == strideAbs );
			if( !bRet )
			{
				std::wcerr<<L"Write Bitmap File Failed."<<std::endl;
				break;
			}
			pLine += stride;
		}

		// TODO: check bHasAlphaChannel
		rNewFormat = MAKELONG(PIX_FMT_A8R8G8B8, PSF_NATIVE_BMP); // reset format& storage
		return bRet;
	}
	
	// V3 Bitmap
	bool EncodeV3(
		CPicture& rPicture,
		PFS::CBaseFile& rFile,
		unsigned int& rNewFormat )
	{
		BITMAPFILEHEADER fileHeader = {0};
		BITMAPINFOHEADER infoHeader = {0};
		infoHeader.biBitCount = 32;
		infoHeader.biWidth = rPicture.m_PictureDesc.width;
		infoHeader.biHeight = rPicture.m_PictureDesc.height; // INVERT IMAGE
		infoHeader.biPlanes = 1;
		infoHeader.biSize = sizeof(BITMAPINFOHEADER); // 40.
		infoHeader.biXPelsPerMeter = 2834; // 72bit/inch
		infoHeader.biYPelsPerMeter = 2834; // 72bit/inch
		int strideAbs = 0;
		if( infoHeader.biBitCount == 32 )
			strideAbs = infoHeader.biWidth * 4;
		else
			strideAbs = (infoHeader.biWidth * infoHeader.biBitCount + 31 ) >> 5 << 2; 
		UINT sizeOfBits = abs( infoHeader.biHeight ) * strideAbs;
		infoHeader.biSizeImage = sizeOfBits;
		infoHeader.biCompression = BI_RGB;
		fileHeader.bfType = 'MB';
		fileHeader.bfOffBits = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER);
		fileHeader.bfSize = fileHeader.bfOffBits + sizeOfBits ;
		rFile.Write( &fileHeader, sizeof(BITMAPFILEHEADER) );
		rFile.Write( &infoHeader, sizeof(BITMAPINFOHEADER) ) ;

		// 
		// 原来打算通过设置infoHeader.biHeight为-height的方式，直接写倒置的bitmap，
		// 而不必翻转bitmapDataArray，后来发现此种方式似乎与Bitmap规范不兼容。
		// 至少在FastStone Image Viewer 3.2上会不能正常渲染。
		// 无奈，只好修改之。
		// 刘东学
		// 2011-07-13
		int stride = -strideAbs;
		unsigned char* pLine = (unsigned char*)rPicture.GetBitmapData() + sizeOfBits + stride;
		int height = infoHeader.biHeight;
		bool bRet = false;
		for( ; height -- ; )
		{
			bRet = ( rFile.Write( pLine, strideAbs ) == strideAbs );
			if( !bRet )
			{
				std::wcerr<<L"Write Bitmap File Failed."<<std::endl;
				break;
			}
			pLine += stride;
		}

		rNewFormat = MAKELONG(PIX_FMT_A8R8G8B8, PSF_NATIVE_BMP); // reset format& storage

		return bRet;
	}

	// V4
	bool EncodeV4(
		CPicture& rPicture,
		PFS::CBaseFile& rFile,
		unsigned int& rNewFormat )
	{
		BITMAPFILEHEADER fileHeader = {0}; // 14bytes
		BITMAPV4HEADER infoHeader = {0};
		infoHeader.bV4BitCount = 32;
		infoHeader.bV4Width = rPicture.m_PictureDesc.width;
		infoHeader.bV4Height = rPicture.m_PictureDesc.height; // INVERT IMAGE
		infoHeader.bV4Planes = 1;
		infoHeader.bV4Size = sizeof(BITMAPV4HEADER); // 108 bytes
		infoHeader.bV4XPelsPerMeter = 2834; // 72bit/inch
		infoHeader.bV4YPelsPerMeter = 2834; // 72bit/inch
		infoHeader.bV4V4Compression = BI_BITFIELDS; // 3
		infoHeader.bV4AlphaMask = 0xFF000000U;
		infoHeader.bV4RedMask = 0x00FF0000U;
		infoHeader.bV4GreenMask = 0x0000FF00U;
		infoHeader.bV4BlueMask = 0x000000FFU;
		infoHeader.bV4CSType = LCS_sRGB; // 'sRGB' but not 1
		// ImageMagick bug! 
		// #define LCS_sRGB 0x01;

		// LCS_CALIBRATED_RGB; // 0
		// LCS_WINDOWS_COLOR_SPACE; // 'Win ' 

		//infoHeader.bV4Endpoints.ciexyzRed.ciexyzX = 0x4000000;
		//infoHeader.bV4Endpoints.ciexyzRed.ciexyzY = 0x4000000;
		//infoHeader.bV4Endpoints.ciexyzRed.ciexyzZ = 0x4000000;
		//infoHeader.bV4Endpoints.ciexyzGreen.ciexyzX = 0x4000000;
		//infoHeader.bV4Endpoints.ciexyzGreen.ciexyzY = 0x4000000;
		//infoHeader.bV4Endpoints.ciexyzGreen.ciexyzZ = 0x4000000;
		//infoHeader.bV4Endpoints.ciexyzBlue.ciexyzX = 0x4000000;
		//infoHeader.bV4Endpoints.ciexyzBlue.ciexyzY = 0x4000000;
		//infoHeader.bV4Endpoints.ciexyzBlue.ciexyzZ = 0x4000000;
		//infoHeader.bV4GammaRed = 0x10000;
		//infoHeader.bV4GammaGreen = 0x10000;
		//infoHeader.bV4GammaBlue = 0x10000;

		int strideAbs = 0;
		if( infoHeader.bV4BitCount == 32 )
			strideAbs = infoHeader.bV4Width * 4;
		else
			strideAbs = (infoHeader.bV4Width * infoHeader.bV4BitCount + 31 ) >> 5 << 2; 
		UINT sizeOfBits = abs( infoHeader.bV4Height ) * strideAbs;
		infoHeader.bV4SizeImage = sizeOfBits;
		fileHeader.bfType = 'MB';
		fileHeader.bfOffBits = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPV4HEADER);
		fileHeader.bfSize = fileHeader.bfOffBits + sizeOfBits ;
		rFile.Write( &fileHeader, sizeof(BITMAPFILEHEADER) );
		rFile.Write( &infoHeader, sizeof(BITMAPV4HEADER) ) ;

		// 
		// 原来打算通过设置infoHeader.biHeight为-height的方式，直接写倒置的bitmap，
		// 而不必翻转bitmapDataArray，后来发现此种方式似乎与Bitmap规范不兼容。
		// 至少在FastStone Image Viewer 3.2上会不能正常渲染。
		// 无奈，只好修改之。
		// 刘东学
		// 2011-07-13
		int stride = -strideAbs;
		unsigned char* pLine = (unsigned char*)rPicture.GetBitmapData() + sizeOfBits + stride;
		int height = infoHeader.bV4Height;
		bool bRet = false;
		for( ; height -- ; )
		{
			bRet = ( rFile.Write( pLine, strideAbs ) == strideAbs );
			if( !bRet )
			{
				std::wcerr<<L"Write Bitmap File Failed."<<std::endl;
				break;
			}
			pLine += stride;
		}

		rNewFormat = MAKELONG(PIX_FMT_A8R8G8B8, PSF_NATIVE_BMP); // reset format& storage

		return bRet;
	}

	bool CBMPEncoder::Encode(
		CPicture& rPicture,
		PFS::CBaseFile& rFile,
		unsigned int& rNewFormat )
	{
		return EncodeV4( rPicture, rFile, rNewFormat );
	}

	void CBMPEncoder::Close()
	{
	}
} // namespace MHZX