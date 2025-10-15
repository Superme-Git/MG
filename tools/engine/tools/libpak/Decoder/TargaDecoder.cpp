///=============================================================================
/// Product   : MHZX
/// Project   : DockChat
/// Module    : TargaDecoder.cpp
/// Copyright : (c) 2009 Locojoy, Inc. All rights reserved.
/// Creator   : yanghaibo
///=============================================================================
/// Description :
/// 	Targa(tga)图像数据/文件解码器
///=============================================================================
/// Revision History :
/// 	V1.0 - 2009-4-22 Created.
///=============================================================================
#include "StdAfx.h"
#include "TargaDecoder.h"
#include "stdio.h" // for EOF, SEEK_CUR macros
#include "memfile.h"
#include "RenderHelper.h"

#define logprintf(args)

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
	typedef struct
	{
		UINT8	blue;
		UINT8	green;
		UINT8	red;
	} PIXEL24;

#pragma pack( 4 )
	typedef struct
	{
		UINT8	blue;
		UINT8	green;
		UINT8	red;
		UINT8	alpha;
	} PIXEL32;

	typedef struct BlockO24BitPixels
	{
		long	 width;
		long	 height;
		PIXEL32	*rgb;
	} BlockO24BitPixels;

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
#pragma pack( pop )

	static bool _LoadUncompressedTGA(
		BlockO24BitPixels *blockPtr,
		int channels,
		MEMFILE *mf,
		BOOL bVFlip )
	{
		PIXEL32* bufferStart;
		PIXEL32* buffer;
		long	 h, w;
		long	 bufferSize;
		long	 bufferWidth;
		long	 bufferHeight;
		long	 lineStride;

		bufferWidth  = blockPtr->width;
		bufferHeight = blockPtr->height;
		bufferSize   = bufferWidth * bufferHeight;
		bufferStart  = blockPtr->rgb;

		if (bVFlip)
		{
			// read bottom-up
			bufferStart    += bufferSize - bufferWidth;
			lineStride      = -bufferWidth;
		}
		else
		{
			lineStride      = bufferWidth;
		}

		switch (channels)
		{
		case 3:
			{
				// No Alpha / Uncompressed
				for (h = 0; h < bufferHeight; h++)
				{
					buffer = bufferStart;
					for (w = 0; w < bufferWidth; w++)
					{
						// 读取blue,green,red前3个通道
						MEMFILE_Read(mf, buffer, sizeof(PIXEL24) );
						buffer->alpha = 255;
						buffer++;
					}
					bufferStart += lineStride;
				}
			}
			break;
		case 4:
			{
				// Alpha / Uncompressed
				for (h = 0; h < bufferHeight; h++)
				{
					buffer = bufferStart;
					for (w = 0; w < bufferWidth; w++)
					{
						// 读取blue,green,red,alpha4个通道
						MEMFILE_Read(mf, buffer, sizeof(PIXEL32) );
						buffer ++;
					}
					bufferStart += lineStride;
				}
			}
			break;
		default:
			return false;
		}
		return true;

	} // _LoadUncompressedTGA

	BITMAPINFO* texReadTGA( LPCWSTR lpszFileName, HBITMAP* pBitmap, BOOL bPremultiply)
	{
		MEMFILE*        infile = NULL;	// file pointer to infile
		TGAHeader		tgaHeaderX;
		TGAHeader*      tgaHeader = &tgaHeaderX;
		BITMAPINFO*		qt_pBitmapInfo = NULL;
		HBITMAP			qt_hBITMAP = NULL;
		char*			qt_pPixels = NULL;
		int				channels = 0;

		BlockO24BitPixels   blockX;
		BlockO24BitPixels*  blockPtr = &blockX;
		long			bufferWidth;
		long			bufferHeight;

		infile = MEMFILE_Load(lpszFileName);
		if( infile == NULL )
		{
			logprintf (("Could not open : %s\n", lpszFileName ));
			goto cleanup;
		}

		MEMFILE_Read(infile, tgaHeader, sizeof (TGAHeader));
		if( tgaHeader->ID != 0 )
		{
			MEMFILE_Seek (infile, tgaHeader->ID, SEEK_CUR); // Skip User Info
		}

		if (tgaHeader->bpp == 32)
		{
			channels = 4;
		}
		else if (tgaHeader->bpp == 24)
		{
			channels = 3;
		}
		else
		{
			logprintf (("unsupported bits per pixel (%s)\n", lpszFileName));
			goto cleanup;
		}

		bufferWidth  = tgaHeader->width;
		bufferHeight = tgaHeader->height;

		long bufferSize = bufferWidth * bufferHeight * sizeof (PIXEL32);

		qt_pBitmapInfo = (BITMAPINFO*)HeapAlloc (GetProcessHeap(), 0, sizeof(BITMAPINFO));
		if (!qt_pBitmapInfo)
		{
			goto cleanup;
		}

		qt_pBitmapInfo->bmiHeader.biSize          = sizeof (BITMAPINFOHEADER);
		qt_pBitmapInfo->bmiHeader.biPlanes        = 1;
		qt_pBitmapInfo->bmiHeader.biBitCount      = 32;	
		// we make this 32 bits because then we don't have to worry about aligning each row to a DWORD
		qt_pBitmapInfo->bmiHeader.biCompression   = BI_RGB;
		qt_pBitmapInfo->bmiHeader.biXPelsPerMeter = 0;
		qt_pBitmapInfo->bmiHeader.biYPelsPerMeter = 0;
		qt_pBitmapInfo->bmiHeader.biClrUsed       = 0;
		qt_pBitmapInfo->bmiHeader.biClrImportant  = 0;
		qt_pBitmapInfo->bmiHeader.biWidth         = bufferWidth;
		qt_pBitmapInfo->bmiHeader.biHeight        = bufferHeight;
		qt_pBitmapInfo->bmiHeader.biSizeImage     = bufferSize;

		qt_hBITMAP = ::CreateDIBSection (
			NULL,
			qt_pBitmapInfo,
			DIB_RGB_COLORS,
			(LPVOID *)&qt_pPixels,
			NULL,
			0);

		if (!qt_pPixels)
		{
			goto cleanup;
		}

		*pBitmap = qt_hBITMAP;

		blockPtr->rgb      = (PIXEL32 *)qt_pPixels;
		blockPtr->width    = bufferWidth;
		blockPtr->height   = bufferHeight;

		switch (tgaHeader->itype)
		{
		case 2: // uncompressed true color
			if (((tgaHeader->bpp != 24) && (tgaHeader->bpp != 32)))
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load uncompressed tga (%s)\n", lpszFileName));
				if( !_LoadUncompressedTGA( blockPtr, channels, infile, !!(tgaHeader->idesc & TGA_IDESC_VFLIP) ) )
				{
					goto cleanup;
				}
			}
			break;
		default:
			logprintf (("Unsupported TGA file type\n"));
			goto cleanup;
		}

		MEMFILE_Close (infile);

		logprintf (("--success--\n"));

		if( bPremultiply )
		{
			MHZX::CRenderHelper::PremultiplyImageBuffer( qt_pPixels, bufferWidth, bufferHeight );
		}

		return qt_pBitmapInfo;

cleanup:
		logprintf (("--failed--\n"));
		if (qt_hBITMAP) DeleteObject (qt_hBITMAP);
		if (qt_pBitmapInfo) HeapFree (GetProcessHeap(), 0 , qt_pBitmapInfo);
		if (infile) MEMFILE_Close(infile);

		return NULL;

	} // texReadTGA


	// 加载TGA数据,并读取TGA的宽度信息和高度信息
	// 该TGA数据可用于生成bgra32位格式的bitmap,但高度要取负值
	// 返回的*lppvPixels数据需要用FreeTGAData()释放
	BOOL LoadTGAData(LPCTSTR lpszFileName, LPVOID* lppvPixels, ULONG* pulWidth, ULONG* pulHeight, UINT* puiBitCount)
	{
		MEMFILE*        infile = NULL;	// file pointer to infile
		TGAHeader		tgaHeaderX;
		TGAHeader*      tgaHeader = &tgaHeaderX;
		int				channels = 0;

		BlockO24BitPixels   blockX;
		BlockO24BitPixels*  blockPtr = &blockX;
		unsigned long		bufferWidth;
		unsigned long		bufferHeight;

		if (lppvPixels == NULL)
		{
			assert(!"无效的参数!");
			return FALSE;
		}

		infile = MEMFILE_Load(lpszFileName);
		if( infile == NULL )
		{
			logprintf (("Could not open : %s\n", lpszFileName ));
			goto cleanup;
		}

		MEMFILE_Read(infile, tgaHeader, sizeof (TGAHeader));
		if( tgaHeader->ID != 0 )
		{
			MEMFILE_Seek (infile, tgaHeader->ID, SEEK_CUR); // Skip User Info
		}

		if (tgaHeader->bpp == 32)
		{
			channels = 4;
		}
		else if (tgaHeader->bpp == 24)
		{
			channels = 3;
		}
		else
		{
			logprintf (("unsupported bits per pixel (%s)\n", lpszFileName));
			goto cleanup;
		}

		bufferWidth  = tgaHeader->width;
		bufferHeight = tgaHeader->height;
		if( pulWidth != NULL )
		{
			*pulWidth = bufferWidth;
		}

		if( pulHeight != NULL )
		{
			*pulHeight = bufferHeight;
		}

		long bufferSize = bufferWidth * bufferHeight * sizeof (PIXEL32);

		*lppvPixels = HeapAlloc (GetProcessHeap(), 0, bufferSize);
		if (*lppvPixels == NULL)
		{
			goto cleanup;
		}

		blockPtr->rgb      = (PIXEL32 *)*lppvPixels;
		blockPtr->width    = bufferWidth;
		blockPtr->height   = bufferHeight;

		switch (tgaHeader->itype)
		{
		case 2: // uncompressed true color
			if (((tgaHeader->bpp != 24) && (tgaHeader->bpp != 32)))
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load uncompressed tga (%s)\n", lpszFileName));
				
				// 注: 这里需要读入原始的数据
				if( !_LoadUncompressedTGA( blockPtr, channels, infile, !(tgaHeader->idesc & TGA_IDESC_VFLIP) ) )
				{
					goto cleanup;
				}
			}
			break;
		default:
			logprintf (("Unsupported TGA file type\n"));
			goto cleanup;
		}

		MEMFILE_Close (infile);
		logprintf (("--success--\n"));
		if( puiBitCount != NULL ) *puiBitCount = 32; // 这里都返回32bit的图像数据
		return TRUE;

cleanup:
		if (infile) MEMFILE_Close(infile);
		logprintf (("--failed--\n"));
		return FALSE;
	}// LoadTGAData




	BOOL LoadTGAData(PFS::CBaseFile& rFile, LPVOID* lppvPixels, 
		ULONG* pulWidth, ULONG* pulHeight, UINT* puiBitCount)
	{

		MEMFILE*        infile = NULL;	// file pointer to infile
		TGAHeader		tgaHeaderX;
		TGAHeader*      tgaHeader = &tgaHeaderX;
		int				channels = 0;

		BlockO24BitPixels   blockX;
		BlockO24BitPixels*  blockPtr = &blockX;
		unsigned long		bufferWidth;
		unsigned long		bufferHeight;

		if (lppvPixels == NULL)
		{
			assert(!"无效的参数!");
			return FALSE;
		}

		infile = MEMFILE_Load(rFile);
		if( infile == NULL )
		{
			logprintf (("Could not open BaseFile : %p\n", &rFile ));
			goto cleanup;
		}

		MEMFILE_Read(infile, tgaHeader, sizeof (TGAHeader));
		if( tgaHeader->ID != 0 )
		{
			MEMFILE_Seek (infile, tgaHeader->ID, SEEK_CUR); // Skip User Info
		}

		if (tgaHeader->bpp == 32)
		{
			channels = 4;
		}
		else if (tgaHeader->bpp == 24)
		{
			channels = 3;
		}
		else
		{
			logprintf (("unsupported bits per pixel (%p)\n", &rFile));
			goto cleanup;
		}

		bufferWidth  = tgaHeader->width;
		bufferHeight = tgaHeader->height;
		if( pulWidth != NULL )
		{
			*pulWidth = bufferWidth;
		}

		if( pulHeight != NULL )
		{
			*pulHeight = bufferHeight;
		}

		long bufferSize = bufferWidth * bufferHeight * sizeof (PIXEL32);

		*lppvPixels = HeapAlloc (GetProcessHeap(), 0, bufferSize);
		if (*lppvPixels == NULL)
		{
			goto cleanup;
		}

		blockPtr->rgb      = (PIXEL32 *)*lppvPixels;
		blockPtr->width    = bufferWidth;
		blockPtr->height   = bufferHeight;

		switch (tgaHeader->itype)
		{
		case 2: // uncompressed true color
			if (((tgaHeader->bpp != 24) && (tgaHeader->bpp != 32)))
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load uncompressed tga (%p)\n", &rFile));
				
				// 注: 这里需要读入原始的数据
				if( !_LoadUncompressedTGA( blockPtr, channels, infile, !(tgaHeader->idesc & TGA_IDESC_VFLIP) ) )
				{
					goto cleanup;
				}
			}
			break;
		default:
			logprintf (("Unsupported TGA file type\n"));
			goto cleanup;
		}

		MEMFILE_Close (infile);
		logprintf (("--success--\n"));
		if( puiBitCount != NULL ) *puiBitCount = 32; // 这里都返回32bit的图像数据
		return TRUE;

cleanup:
		if (infile) MEMFILE_Close(infile);
		logprintf (("--failed--\n"));
		return FALSE;
	}

	// 释放由 LoadBitmapData()/LoadP0XData()/LoadTargaData() 分配的内存
	void FreePictureData( LPVOID pPixels )
	{
		// 释放由 LoadBitmapData()/LoadP0XData()/LoadTargaData() 分配的内存
		::HeapFree( ::GetProcessHeap(), 0, pPixels );
	}
} // namespace HELPER