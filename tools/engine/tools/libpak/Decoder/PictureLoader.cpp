///=============================================================================
/// Product   : MHZX
/// Project   : DockChat
/// Module    : PictureLoader.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	P0X,B0X,TGA,BMP,JPG等文件的加载器
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-4-17 Created.
///=============================================================================
#include "StdAfx.h"
#include "PictureLoader.h"
#include "FileObjects.h"
#include "RenderHelper.h"
#include "TargaDecoder.h"
#include "memfile.h"

#ifndef EOF
#define EOF (-1) // see stdio.h
#endif

namespace HELPER
{
	typedef char			INT8;
	typedef unsigned char	UINT8;
	typedef short			INT16;
	typedef unsigned short	UINT16;
	typedef long			INT32;
	typedef unsigned long	UINT32;

	typedef struct tagPIXEL24
	{
		UINT8 blue;
		UINT8 green;
		UINT8 red;
	} PIXEL24;

	typedef struct tagPIXEL32
	{
		UINT8 blue;
		UINT8 green;
		UINT8 red;
		UINT8 alpha;
	} PIXEL32;

#define logprintf(args)

	enum PixelFormat
	{
		PIX_FMT_R5G6B5,
		PIX_FMT_A4R4G4B4,	
		PIX_FMT_R8G8B8,
		PIX_FMT_A8R8G8B8,
		PIX_FMT_X8R8G8B8,	
		PIX_FMT_A8,			// 8bit gray　目前不支持

		PIX_FMT_A1R5G5B5,
		PIX_FMT_X1R5G5B5,	

		//以下目前不支持
		PIX_FMT_DXT1,
		PIX_FMT_DXT2,
		PIX_FMT_DXT3,
		PIX_FMT_DXT4,
		PIX_FMT_DXT5,
	};

	//
	//每个像素的格式顺序是：
	//R5G6B5: R、G、B的顺序是从高到低
	//A4R4G4B4：R、G、B的顺序是从高到低
	//R8G8B8：R、G、B的顺序是从高到低
	//A8R8G8B8: 从高到低的顺序是A,B,G,R (pic文件) A,R,G,B(b0x,p0x文件)
	//X8R8G8B8: 从高到低的顺序是X,B,G,R (pic文件) A,R,G,B(b0x,p0x文件)
	//A1R5G5B5: A、R、G、B的顺序是从高到低
	//X1R5G5B5: X、R、G、B的顺序是从高到低
	//

#pragma pack(push)
#pragma pack(4)

	struct PicHeader
	{
		int format;
		short width;
		short height;
		int blockNum;
	};

	struct BlockDesc
	{
		short imageWidth;
		short imageHeight;
		short offsetX;
		short offsetY;
		unsigned fileSize;
	};

#pragma pack(pop)


	/* For expanding 5-bit pixel values to 8-bit with best rounding */

	static const UINT8 c5to8bits[32] = {
		0, 8, 16, 25, 33, 41, 49, 58,
		66, 74, 82, 90, 99, 107, 115, 123,
		132, 140, 148, 156, 165, 173, 181, 189,
		197, 206, 214, 222, 230, 239, 247, 255
	};

	typedef void (*pfn_get_row)( MEMFILE*, int, PIXEL32* );

	//
	// A, R、G、B的顺序是从高到低
	//
	// 结果要是 从高到低 A, B, G, R
	// 或者说 从低到高 R, G, B, A
	//
	/* This version is for reading 16-bit pixels */
	static void get_4444bit_row ( MEMFILE* mf, int iWidth, PIXEL32* pPixel )
	{
		int t;
		int col;
		for (col = iWidth; col > 0; col--)
		{
			t = MEMFILE_getc(mf);
			t += MEMFILE_getc(mf) << 8;
			/* We expand 5 bit data to 8 bit sample width.
			* The format of the 16-bit (LSB first) input word is
			* AAAARRRRGGGGBBBB
			*/
			pPixel->blue = (UINT8) (t & 0xF) * 17; // B
			t >>= 4;
			pPixel->green = (UINT8) (t & 0xF) * 17; // G
			t >>= 4;
			pPixel->red = (UINT8) (t & 0xF) * 17; // R
			t >>= 4;
			pPixel->alpha = (UINT8) (t & 0xF) * 17; // A

			pPixel ++;
		}
	}

	//
	// 从高到低的顺序是 R G B,
	// 结果要是 从高到低 B, G, R
	// 或者说 从低到高 R, G, B
	//
	/* This version is for reading 16-bit pixels */
	static void get_565bit_row ( MEMFILE* mf, int iWidth, PIXEL32* pPixel )
	{
		int t;
		int col;
		for (col = iWidth; col > 0; col--)
		{
			t = MEMFILE_getc(mf);
			t += MEMFILE_getc(mf) << 8;
			/* We expand 5 bit data to 8 bit sample width.
			* The format of the 16-bit (LSB first) input word is
			* RRRRRGGGGGGBBBBB
			*/
			pPixel->blue = (UINT8) c5to8bits[t & 0x1F]; // B
			t >>= 5;
			pPixel->green = (UINT8) ((t & 0x3F)*255.0/63.0);// G
			t >>= 6;
			pPixel->red = (UINT8) c5to8bits[t & 0x1F]; // R
			pPixel->alpha = 255;
			pPixel ++;
		}
	}
	//
	// 从高到低的顺序是 A R G B,
	// 结果要是 从高到低 A, B, G, R
	// 或者说 从低到高 A, R, G, B
	//
	/* This version is for reading 16-bit pixels */
	static void get_1555bit_row ( MEMFILE* mf, int iWidth, PIXEL32* pPixel )
	{
		int t;
		int col;
		for (col = iWidth; col > 0; col--)
		{
			t = MEMFILE_getc(mf);
			t += MEMFILE_getc(mf) << 8;
			/* We expand 5 bit data to 8 bit sample width.
			* The format of the 16-bit (LSB first) input word is
			* ARRRRRGGGGGBBBBB
			*/
			pPixel->blue = (UINT8) c5to8bits[t & 0x1F]; // B
			t >>= 5;
			pPixel->green = (UINT8) c5to8bits[t & 0x1F];// G
			t >>= 5;
			pPixel->red = (UINT8) c5to8bits[t & 0x1F]; // R
			t >>= 5;
			pPixel->alpha = ((t != 0) ? 255 : 0);				// A
			pPixel ++;
		}
	}

	//
	// 从高到低的顺序是 R G B,
	// 结果要是 从高到低 B, G, R
	// 或者说 从低到高 R, G, B
	//
	static void get_24bit_row ( MEMFILE* mf, int iWidth, PIXEL32* pPixel )
		/* This version is for reading 24-bit pixels */
	{
		int col;
		for (col = iWidth; col > 0; col--)
		{
			// B G R
			{ pPixel->blue = MEMFILE_getc(mf); }
			{ pPixel->green = MEMFILE_getc(mf); }
			{ pPixel->red = MEMFILE_getc(mf); }
			{ pPixel->alpha = 255; }
			pPixel++;
		}
	}

	/*
	* Picture also defines a 32-bit pixel format with order B,G,R,A.
	* We presently ignore the attribute byte, so the code for reading
	* these pixels is identical to the 24-bit routine above.
	* This works because the actual pixel length is only known to read_pixel.
	*/
	/* This version is for reading 32-bit pixels */

	//
	// 从高到低的顺序是A, B, G, R
	// 结果要是 从高到低 A, B, G, R
	// 或者说 从低到高 R, G, B, A
	//
	static void get_32bit_row ( MEMFILE* mf, int iWidth, PIXEL32* pPixel )
	{
		int col;
		for (col = iWidth; col > 0; col--)
		{
			// R G B A
			{ pPixel->red = MEMFILE_getc(mf); }
			{ pPixel->green = MEMFILE_getc(mf); }
			{ pPixel->blue = MEMFILE_getc(mf); }
			{ pPixel->alpha = MEMFILE_getc(mf); }
			pPixel++;
		}
	}

	//
	// 从高到低的顺序是A, R, G, B
	// 结果要是 从高到低 A, B, G, R
	// 或者说 从低到高 R, G, B, A
	//
	static void get_32bit_row_new( MEMFILE* mf, int iWidth, PIXEL32* pPixel )
	{
		// speed up
		MEMFILE_Read( mf, pPixel, iWidth*sizeof(PIXEL32));
	}

	static BITMAPINFO* texReadP0X( LPCTSTR filename, HBITMAP* pBitmap, void** ppPixels, BOOL bPremultiply)
	{
		PicHeader phead;
		MEMFILE* mf;
		BITMAPINFO* qt_pBitmapInfo = NULL;
		HBITMAP qt_hBITMAP = NULL;
		void* qt_pPixels = NULL;
		HDC qt_screenHDC = NULL;

		logprintf (("-----------loading (%s)-------------------\n", filename ));

		mf = MEMFILE_Load(filename);
		if( mf == NULL )
		{
			logprintf (("Could not open : %s\n", filename ));
			goto cleanup;
		}

		if (MEMFILE_Read(mf, &phead, sizeof( phead )) != sizeof (phead))
		{
			logprintf (("Invalid PIC file"));
			goto cleanup;
		}

		{
			long bufferWidth = phead.width;
			long bufferHeight = phead.height;
			long blockNum = phead.blockNum;

			long bufferSize;
			int y;

			if((bufferWidth <= 0 ) || (bufferHeight <= 0 ))
			{
				logprintf (("Invalid PIC file (3), bad size"));
				goto cleanup;
			}

			if ( blockNum != 1 )
			{
				logprintf (("Unsupported PIC file. blockNum != 1"));
				goto cleanup;
			}

			BlockDesc blockDesc;
			if (MEMFILE_Read(mf, &blockDesc, sizeof( blockDesc )) != sizeof (blockDesc))
			{
				logprintf (("Invalid PIC file!"));
				goto cleanup;
			}

			bufferSize = bufferWidth * bufferHeight * sizeof (PIXEL32);

			qt_pBitmapInfo = (BITMAPINFO*)HeapAlloc (GetProcessHeap(), 0, sizeof(BITMAPINFO) );
			if (!qt_pBitmapInfo)
			{
				goto cleanup;
			}

			qt_pBitmapInfo->bmiHeader.biSize = sizeof (BITMAPINFOHEADER);
			qt_pBitmapInfo->bmiHeader.biPlanes = 1;
			qt_pBitmapInfo->bmiHeader.biBitCount = 32; // because then we don't have to worry about row alignment
			qt_pBitmapInfo->bmiHeader.biCompression = BI_RGB;
			qt_pBitmapInfo->bmiHeader.biXPelsPerMeter = 0;
			qt_pBitmapInfo->bmiHeader.biYPelsPerMeter = 0;
			qt_pBitmapInfo->bmiHeader.biClrUsed = 0;
			qt_pBitmapInfo->bmiHeader.biClrImportant = 0;
			qt_pBitmapInfo->bmiHeader.biWidth = bufferWidth;
			qt_pBitmapInfo->bmiHeader.biHeight = -bufferHeight;
			qt_pBitmapInfo->bmiHeader.biSizeImage = bufferSize;

			qt_hBITMAP = ::CreateDIBSection (
				qt_screenHDC,
				qt_pBitmapInfo,
				DIB_RGB_COLORS,
				(LPVOID *)&qt_pPixels,
				NULL,
				0);

			if (!qt_pPixels)
			{
				goto cleanup;
			}

			if( ppPixels != NULL )
			{
				*ppPixels = qt_pPixels;
			}

			*pBitmap = qt_hBITMAP;

			pfn_get_row get_row = (pfn_get_row)NULL;

			switch ( phead.format )
			{
			case PIX_FMT_A8R8G8B8: 
			case PIX_FMT_X8R8G8B8 :
				get_row = get_32bit_row_new; break;
			case PIX_FMT_R8G8B8:
				get_row = get_24bit_row; break;
			case PIX_FMT_A4R4G4B4:
				get_row = get_4444bit_row; break;
			case PIX_FMT_R5G6B5:
				get_row = get_565bit_row; break;
			case PIX_FMT_A1R5G5B5 :
			case PIX_FMT_X1R5G5B5 : 
				get_row = get_1555bit_row;break;
			default:
				logprintf (("--unsupported yet!--\n"));
				goto cleanup;
			}

			for(y = 0; y < bufferHeight; y++)
			{
				//PIXEL32* pPixel = ((PIXEL32*)qt_pPixels) + (bufferHeight - y - 1) * bufferWidth;
				PIXEL32* pPixel = ((PIXEL32*)qt_pPixels) + y * bufferWidth;
				get_row( mf, bufferWidth, pPixel );
			}

			if( bPremultiply )
			{
				MHZX::CRenderHelper::PremultiplyImageBuffer(qt_pPixels, bufferWidth, bufferHeight);
			}
		}

		logprintf (("--done--\n"));

		if (mf) MEMFILE_Close(mf);
		return qt_pBitmapInfo;

cleanup:
		if (qt_hBITMAP) DeleteObject (qt_hBITMAP);
		if (qt_pBitmapInfo) HeapFree (GetProcessHeap(), 0 , qt_pBitmapInfo);
		if (mf) MEMFILE_Close(mf);

		logprintf (("--failed--\n"));

		return NULL;
	}

	//
	// 注: *ppPixels 存放的是原始的纹理数据,没有进行颠倒处理
	//
	BOOL LoadP0XData(__in LPCWSTR lpszFileName, 
		__out_bcount(*pWidth**pHeight*4) LPVOID* ppPixels, 
		__out  unsigned long* pWidth, __out  unsigned long* pHeight, __out_opt unsigned int* lpuiBitCount)
	{

		PicHeader phead;
		MEMFILE* mf;
		UINT8* qt_pPixels = NULL;

		logprintf (("-----------loading (%s)-------------------\n", filename ));

		mf = MEMFILE_Load(lpszFileName);
		if( mf == NULL )
		{
			logprintf (("Could not open : %s\n", filename ));
			goto cleanup;
		}

		if (MEMFILE_Read(mf, &phead, sizeof( phead )) != sizeof (phead))
		{
			logprintf (("Invalid PIC file"));
			goto cleanup;
		}

		{
			long bufferWidth = phead.width;
			long bufferHeight = phead.height;
			long blockNum = phead.blockNum;
			long bufferSize;

			if((bufferWidth <= 0 ) || (bufferHeight <= 0 ))
			{
				logprintf (("Invalid PIC file (3), bad size"));
				goto cleanup;
			}

			if ( blockNum != 1 )
			{
				logprintf (("Unsupported PIC file. blockNum != 1"));
				goto cleanup;
			}

			BlockDesc blockDesc;
			if (MEMFILE_Read(mf, &blockDesc, sizeof( blockDesc )) != sizeof (blockDesc))
			{
				logprintf (("Invalid PIC file!"));
				goto cleanup;
			}

			pfn_get_row get_row = (pfn_get_row)NULL;
			switch ( phead.format )
			{
			case PIX_FMT_A8R8G8B8: 
			case PIX_FMT_X8R8G8B8 :
				get_row = get_32bit_row_new; break;
			case PIX_FMT_R8G8B8:
				get_row = get_24bit_row; break;
			case PIX_FMT_A4R4G4B4:
				get_row = get_4444bit_row; break;
			case PIX_FMT_R5G6B5:
				get_row = get_565bit_row; break;
			case PIX_FMT_A1R5G5B5 :
			case PIX_FMT_X1R5G5B5 : 
				get_row = get_1555bit_row;break;
			default:
				logprintf (("--unsupported yet!--\n"));
				goto cleanup;
			}

			bufferSize = bufferWidth * bufferHeight * sizeof (PIXEL32);
			*pWidth = bufferWidth;
			*pHeight = bufferHeight;

			qt_pPixels = (UINT8*)::HeapAlloc( ::GetProcessHeap(), 0, bufferSize );
			if (!qt_pPixels)
			{
				goto cleanup;
			}

			for(int y = 0; y < bufferHeight; y++)
			{
				PIXEL32* pPixel = ((PIXEL32*)qt_pPixels) + y*bufferWidth;
				get_row( mf, bufferWidth, pPixel );
			}
		}

		if (mf) MEMFILE_Close(mf);
		*ppPixels = qt_pPixels;
		if( lpuiBitCount != NULL )
		{
			*lpuiBitCount = 32;
		}
		logprintf (("--done--\n"));
		return TRUE;

cleanup:
		if (mf) MEMFILE_Close(mf);
		logprintf (("--failed--\n"));
		return FALSE;
	}

} // namespace HELPER

//////////////////////////////////////////////////////////////////////////
//
// CPictureLoader
//
//////////////////////////////////////////////////////////////////////////
namespace HELPER
{

HBITMAP CPictureLoader::LoadBitmap(LPCWSTR lpszFileName, BITMAPINFO** ppBitmapInfo, BOOL bPremultiply)
{
	if( ppBitmapInfo != NULL )
		*ppBitmapInfo = NULL;

	CFile theFile;
	if( !theFile.Open( lpszFileName, CFile::FM_EXCL, CFile::FA_RDONLY) )
	{
		return NULL;
	}

	CFile* pFile = &theFile;
	BITMAPFILEHEADER fileHeader = {0};
	size_t ret = pFile->Read( &fileHeader, sizeof( BITMAPFILEHEADER ) );
	if ( ret != sizeof( BITMAPFILEHEADER ) )
	{
		return NULL;
	}

	// Check file type
	if( fileHeader.bfType != 'MB' || fileHeader.bfOffBits <  sizeof( BITMAPFILEHEADER ))
	{
		return NULL;
	}

	int nBitmapInfoSize = fileHeader.bfOffBits - sizeof(BITMAPFILEHEADER);
	if ( nBitmapInfoSize < sizeof(BITMAPINFOHEADER) )
	{
		// 无效的Bitmap文件或不支持的Bitmap文件格式
		return NULL;
	}

	BITMAPINFO*	pBitmapInfo = (BITMAPINFO*)HeapAlloc(GetProcessHeap(), 0, nBitmapInfoSize  );
	if ( pBitmapInfo == NULL )
	{
		return NULL;
	}

	// Read info header
	BITMAPINFOHEADER& infoHeader = pBitmapInfo->bmiHeader;
	ret = pFile->Read( pBitmapInfo, nBitmapInfoSize );
	if ( ret != nBitmapInfoSize )
	{
		return NULL;
	}

	int strideAbs = 0;
	if( infoHeader.biBitCount == 32 )
		strideAbs = infoHeader.biWidth * 4;
	else
		strideAbs = (infoHeader.biWidth * infoHeader.biBitCount + 31 ) >> 5 << 2; 

	//UINT sizeOfBits = infoHeader.biSizeImage;
	UINT sizeOfBits = infoHeader.biHeight * strideAbs;
	//sizeOfBits = fileHeader.bfSize - fileHeader.bfOffBits;
	if ( sizeOfBits == 0 )
	{
		// empty bitmap
		return NULL;
	}

	LPVOID bitsArray = NULL;
	HBITMAP hBitmap = ::CreateDIBSection (
		NULL,
		pBitmapInfo,
		DIB_RGB_COLORS,
		&bitsArray,
		NULL,
		0);
	if ( hBitmap == NULL )
	{
		return NULL;
	}

	// Read bits to the array
	pFile->Seek(fileHeader.bfOffBits, CFile::FSM_SET );
	if( sizeOfBits != pFile->Read( bitsArray, sizeOfBits ) )
	{
		DeleteObject(hBitmap);
		hBitmap = NULL;
	}

	if( ppBitmapInfo != NULL )
		*ppBitmapInfo = pBitmapInfo;

	if( infoHeader.biBitCount == 32 && bPremultiply )
	{
		MHZX::CRenderHelper::PremultiplyImageBuffer( bitsArray, infoHeader.biWidth, infoHeader.biHeight );
	}

	return hBitmap;
}

//////////////////////////////////////////////////////////////////////////
//
// 从文件中读取位图数据
//
bool CPictureLoader::LoadBitmapData(__in const wchar_t*	lpszFileName, 
									__out_bcount(*lpulWidth**lpulHeight*4) void** lppvPixels,
									__out unsigned long*	lpulWidth,
									__out unsigned long*	lpulHeight,
									__out_opt unsigned int*	lpuiBitCount)
{
	CFile theFile;
	if( !theFile.Open( lpszFileName, CFile::FM_EXCL, CFile::FA_RDONLY) )
	{
		return NULL;
	}

	CFile* pFile = &theFile;
	BITMAPFILEHEADER fileHeader = {0};
	size_t ret = pFile->Read( &fileHeader, sizeof( BITMAPFILEHEADER ) );
	if ( ret != sizeof( BITMAPFILEHEADER ) )
	{
		return false;
	}

	// Check file type
	if( fileHeader.bfType != 'MB' || fileHeader.bfOffBits <  sizeof( BITMAPFILEHEADER ))
	{
		return false;
	}

	LONG nBitmapInfoSize = fileHeader.bfOffBits - sizeof(BITMAPFILEHEADER);
	if ( nBitmapInfoSize < sizeof(BITMAPINFOHEADER) )
	{
		// 无效的Bitmap文件或不支持的Bitmap文件格式
		return false;
	}

	// Read info header
	BITMAPINFOHEADER infoHeader;
	ret = pFile->Read( &infoHeader, sizeof(BITMAPINFOHEADER) );
	if ( ret != sizeof(BITMAPINFOHEADER) )
	{
		return false;
	}

	if( nBitmapInfoSize - sizeof(BITMAPINFOHEADER) > 0 )
	{
		// 跳过多余的数据
		pFile->Seek( (LONG)nBitmapInfoSize - sizeof(BITMAPINFOHEADER), CFile::FSM_CUR );
	}

	int strideAbs = 0;
	if( infoHeader.biBitCount == 32 )
		strideAbs = infoHeader.biWidth * 4;
	else
		strideAbs = (infoHeader.biWidth * infoHeader.biBitCount + 31 ) >> 5 << 2; 

	UINT sizeOfBits = infoHeader.biHeight * strideAbs;
	if ( sizeOfBits == 0 )
	{
		// empty bitmap
		return false;
	}

	unsigned char* bitsArray = (unsigned char*)HeapAlloc(GetProcessHeap(), 0, sizeOfBits);
	if( bitsArray == NULL )
	{
		return false;
	}

	// Read bits to the array
	pFile->Seek(fileHeader.bfOffBits, CFile::FSM_SET );

	// 位图图像数据需要进行上下倒置处理
	unsigned char* pRow = bitsArray + (infoHeader.biHeight-1)*strideAbs;
	LONG lTotleLines = infoHeader.biHeight;
	while ( lTotleLines -- )
	{
		if( strideAbs != pFile->Read( pRow, strideAbs ) )
		{
			HeapFree(GetProcessHeap(), 0, bitsArray);
			return FALSE;
		}

		pRow -= strideAbs;
	}

	*lppvPixels = bitsArray;

	if( lpulWidth != NULL ) *lpulWidth = infoHeader.biWidth;
	if( lpulHeight != NULL ) *lpulHeight = infoHeader.biHeight;
	if( lpuiBitCount != NULL ) *lpuiBitCount = infoHeader.biBitCount;

	return true;
}

HBITMAP CPictureLoader::LoadPicture(LPCWSTR lpszFileName, BITMAPINFO** ppBitmapInfo, BOOL bPremultiply )
{
	HBITMAP hBmp = NULL;
	BITMAPINFO*	pBitmapInfo = HELPER::texReadP0X( lpszFileName, &hBmp, NULL, bPremultiply );
	if( ppBitmapInfo != NULL )
	{
		*ppBitmapInfo = pBitmapInfo;
	}
	return hBmp;
}

HBITMAP CPictureLoader::LoadTarga(LPCWSTR lpszFileName, BITMAPINFO** ppBitmapInfo, BOOL bPremultiply)
{
	HBITMAP hBmp = NULL;
	BITMAPINFO*	pBitmapInfo = HELPER::texReadTGA( lpszFileName, &hBmp, bPremultiply );
	if( ppBitmapInfo != NULL )
	{
		*ppBitmapInfo = pBitmapInfo;
	}

	return hBmp;
}

void CPictureLoader::FreeBitmapInfo( BITMAPINFO* pBitmapInfo )
{
	if ( pBitmapInfo != NULL ) 
	{
		HeapFree (GetProcessHeap(), 0 , pBitmapInfo);
	}
}

bool CPictureLoader::LoadPictureData(__in LPCWSTR lpszFileName, 
									 __out_bcount(*lpulWidth**lpulHeight*4) void** lppvPixels,
									 __out unsigned long* lpulWidth,
									 __out unsigned long* lpulHeight,
									 __out_opt unsigned int* lpuiBitCount)
{

	BOOL bRet = HELPER::LoadP0XData(lpszFileName, lppvPixels, lpulWidth, lpulHeight, lpuiBitCount);
	return !!bRet;
}

bool CPictureLoader::LoadTargaData(__in const wchar_t* lpszFileName,
								   __out_bcount(*lpulWidth**lpulHeight*4) void** lppvPixels,
								   __out unsigned long* lpulWidth,
								   __out unsigned long* lpulHeight,
								   __out_opt unsigned int* lpuiBitCount)
{
	BOOL bRet = HELPER::LoadTGAData( lpszFileName, lppvPixels, lpulWidth, lpulHeight, lpuiBitCount);
	return !!bRet;
}

void CPictureLoader::FreePictureData( LPVOID pPixels )
{
	// 释放由 LoadBitmapData()/LoadP0XData()/LoadTargaData() 分配的内存
	::HeapFree( ::GetProcessHeap(), 0, pPixels );
}

} // namespace HELPER