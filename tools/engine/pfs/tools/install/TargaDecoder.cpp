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
#include <assert.h>
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
#define MAX_COLORMAP 256

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
	} PIXEL32, pixel32;

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
		pixel32 * colorMap, 
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
		case 1:
			if (colorMap)
			{
				// No Alpha / Uncompressed
				
				for (long loop = 0; loop < bufferHeight; loop++)
				{
					buffer = bufferStart;
					
					for (long i = 0; i < bufferWidth; i++)
					{
						uint8	k;
		
						k = MEMFILE_getc(mf);
		
						buffer->red   = colorMap[k].red;
						buffer->green = colorMap[k].green;
						buffer->blue  = colorMap[k].blue;
						buffer->alpha = 255;// colorMap[k].alpha;
						buffer++;
					}
					
					bufferStart += lineStride;
				}
			}
			else
			{
				// No Alpha / Uncompressed
				
				for (long loop = 0; loop < bufferHeight; loop++)
				{
					buffer = bufferStart;
					
					for (long i = 0; i < bufferWidth; i++)
					{
						uint8	k;
		
						k = MEMFILE_getc(mf);
		
						buffer->red   = k;
						buffer->green = k;
						buffer->blue  = k;
						buffer->alpha = 255;
						buffer++;
					}
					bufferStart += lineStride;
				}
			}
			break;
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


	static bool _flipBuffer (void *buffer, long rowSize, long rows)
	{

		uint8	*temp;
		uint8	*firstRow;
		uint8	*lastRow;
				
		long	 count;
		
		temp = (uint8 *)HeapAlloc (GetProcessHeap(), 0 , rowSize);
		if (!temp)
		{
			logprintf (("Out of memory loading tga\n"));
			return false;
		}
		
		firstRow = (uint8 *) buffer;
		lastRow  = firstRow + rowSize * (rows - 1);
		
		count = (rows + 1) / 2;
		
		while (count)
		{
			memcpy (temp, lastRow, rowSize);
			memcpy (lastRow, firstRow, rowSize);
			memcpy (firstRow, temp, rowSize);
			
			firstRow += rowSize;
			lastRow  -= rowSize;
			
			count--;
		}
		
		HeapFree (GetProcessHeap(), 0 , temp);

		return true;
	} // _flipBuffer

	static bool _LoadCompressedTGA (BlockO24BitPixels *blockPtr, int channels, MEMFILE *mf, pixel32* colorMap, BOOL bVFlip)
	{
		uint8			a;
		uint8			r;
		uint8			g;
		uint8			b;
		uint8			i;
		uint8			count;
		pixel32*		buffer;

		long			pixelCount;
		long			totalPixels;
		long			bufferWidth;
		long			bufferHeight;

		bufferWidth  = blockPtr->width;
		bufferHeight = blockPtr->height;
		totalPixels  = bufferWidth * bufferHeight;
		buffer       = blockPtr->rgb;
		pixelCount   = 0;

		while (pixelCount < totalPixels)
		{
			i = MEMFILE_getc(mf);
			count = (0x7F & i) + 1;
			
			pixelCount += count;
			
			if (pixelCount > totalPixels)
			{
				logprintf (("Pixel overflow in targa decompression\n"));
				return false;
			}
			
			if (i & 0x80)
			{
				a = 255;
				// run data
				switch (channels)
				{
				case 1:
					if (colorMap)
					{
						uint8 k;
						k = MEMFILE_getc(mf);
						
						r = colorMap[k].red;
						g = colorMap[k].green;
						b = colorMap[k].blue;
					}
					else
					{
						b = MEMFILE_getc(mf);
						g = b;
						r = b;
					}
					break;
				case 3:
					b = MEMFILE_getc(mf);
					g = MEMFILE_getc(mf);
					r = MEMFILE_getc(mf);
					break;
				case 4:
					b = MEMFILE_getc(mf);
					g = MEMFILE_getc(mf);
					r = MEMFILE_getc(mf);
					a = MEMFILE_getc(mf);
					break;
				}

				while (count--)
				{
					buffer->red   = r;
					buffer->green = g;
					buffer->blue  = b;
					buffer->alpha = a;
					buffer++;
				}
			}
			else
			{
				// dump data

				a = 255;

				while (count--)
				{
					switch (channels)
					{
					case 1:
						if (colorMap)
						{
							uint8 k;
							k = MEMFILE_getc(mf);
							
							r = colorMap[k].red;
							g = colorMap[k].green;
							b = colorMap[k].blue;
						}
						else
						{
							b = MEMFILE_getc(mf);
							g = b;
							r = b;
						}
						break;
					case 3:
						b = MEMFILE_getc(mf);
						g = MEMFILE_getc(mf);
						r = MEMFILE_getc(mf);
						break;
					case 4:
						b = MEMFILE_getc(mf);
						g = MEMFILE_getc(mf);
						r = MEMFILE_getc(mf);
						a = MEMFILE_getc(mf);
						break;
					}

					buffer->red   = r;
					buffer->green = g;
					buffer->blue  = b;
					buffer->alpha = a;
					buffer++;
				}
			}
		}

		if (bVFlip)
		{
			_flipBuffer (blockPtr->rgb, blockPtr->width * sizeof (pixel32), blockPtr->height);
		}

		return true;
	} // _LoadCompressedTGA

	static BITMAPINFO* _ReadTGAData( MEMFILE* infile, HBITMAP* pBitmap, BOOL bPremultiply)
	{
		TGAHeader		tgaHeaderX;
		TGAHeader*      tgaHeader = &tgaHeaderX;
		BITMAPINFO*		qt_pBitmapInfo = NULL;
		HBITMAP			qt_hBITMAP = NULL;
		char*			qt_pPixels = NULL;
		int				channels = 0;

		
		BlockO24BitPixels   blockX;
		BlockO24BitPixels*  blockPtr = &blockX;
		
		pixel32 colorMap[MAX_COLORMAP];
		bool			fColorMap = false;

		long			bufferWidth;
		long			bufferHeight;

		assert( infile != NULL );
		
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
		else if (tgaHeader->bpp == 8)
		{
			channels = 1;
			
			fColorMap = true;
		
			int	firstColor = (long)tgaHeader->mincolor;//((long)tgaHeader->mincolorl + (long)tgaHeader->mincolorh * 256L);
			int	numColors  = (long)tgaHeader->colors;//((long)tgaHeader->colorsl   + (long)tgaHeader->colorsh   * 256L);
			
			if (numColors > 0 && firstColor + numColors <= 256)
			{
				int colorNdx;
				memset (colorMap, 0, sizeof (colorMap));
				
				switch (tgaHeader->colorsize)
				{
				case 24:
					for (colorNdx = 0; colorNdx < numColors; ++colorNdx)
					{
						uint8 r,g,b;
						
						b = MEMFILE_getc(infile);
						g = MEMFILE_getc(infile);
						r = MEMFILE_getc(infile);
						
						colorMap[firstColor + colorNdx].red   = r;
						colorMap[firstColor + colorNdx].green = g;
						colorMap[firstColor + colorNdx].blue  = b;
						colorMap[firstColor + colorNdx].alpha = 255;
					}
					break;
				case 32:
					for (colorNdx = 0; colorNdx < numColors; ++colorNdx)
					{
						uint8 r,g,b, a;
						b = MEMFILE_getc(infile);
						g = MEMFILE_getc(infile);
						r = MEMFILE_getc(infile);
						a = MEMFILE_getc(infile);
						
						colorMap[firstColor + colorNdx].red   = r;
						colorMap[firstColor + colorNdx].green = g;
						colorMap[firstColor + colorNdx].blue  = b;
						colorMap[firstColor + colorNdx].alpha = a;
					}
					break;
				default:
					logprintf (("unhandled color map size (%s)\n", filename));
					goto cleanup;
				}
			}
			else if (numColors)
			{
				logprintf (("too many colors in color map (%s)\n", filename));
				goto cleanup;
			}
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
		bool bVFlip = !!(tgaHeader->idesc & TGA_IDESC_VFLIP);

		switch (tgaHeader->itype)
		{
		case 1: // uncompressed color map
			if (tgaHeader->bpp != 8)
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load uncompressed tga (%s)\n", filename));
				if (!_LoadUncompressedTGA (blockPtr, channels, infile, colorMap, bVFlip))
				{
					goto cleanup;
				}
			}
			break;
		case 2: // uncompressed true color
			if (((tgaHeader->bpp != 24) && (tgaHeader->bpp != 32)))
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load uncompressed tga (%s)\n", lpszFileName));
				if( !_LoadUncompressedTGA( blockPtr, channels, infile, NULL, bVFlip ) )
				{
					goto cleanup;
				}
			}
			break;
		case 3: // uncompressed black & white
			if (tgaHeader->bpp != 8)
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load uncompressed tga (%s)\n", filename));
				if (!_LoadUncompressedTGA (blockPtr, channels, infile, NULL, bVFlip ))
				{
					goto cleanup;
				}
			}
			break;
		case 9: // rle compressed color map
			if (tgaHeader->bpp != 8)
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				if (!_LoadCompressedTGA (blockPtr, channels, infile, colorMap, bVFlip ))
				{
					goto cleanup;
				}
			}
			break;
		case 10: // rle compressed true color
			if (((tgaHeader->bpp != 24) && (tgaHeader->bpp != 32)))
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load compressed tga (%s)\n", filename));
				if (!_LoadCompressedTGA (blockPtr, channels, infile, NULL, bVFlip))
				{
					goto cleanup;
				}
			}
			break;
		case 11: // rle compressed 256-color
			if (tgaHeader->bpp != 8)
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load compressed tga (%s)\n", filename));
				if (!_LoadCompressedTGA (blockPtr, channels, infile, NULL, bVFlip))
				{
					goto cleanup;
				}
			}
			break;
		default:
			logprintf (("Unsupported TGA file type\n"));
			goto cleanup;
		}

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
		return NULL;
	}

	BITMAPINFO* texReadTGA( LPCWSTR lpszFileName, HBITMAP* pBitmap, BOOL bPremultiply)
	{
		MEMFILE* infile = MEMFILE_Load(lpszFileName);
		if( infile == NULL )
		{
			logprintf (("Could not open : %s\n", lpszFileName ));
			return NULL;
		}
	
		BITMAPINFO* pBmpInfo = _ReadTGAData( infile, pBitmap, bPremultiply );

		MEMFILE_Close (infile);		

		return pBmpInfo;

	} // texReadTGA

	BITMAPINFO* texReadTGAData( LPCVOID lpvFileData, size_t nSize, HBITMAP* pBitmap, BOOL bPremultiply)
	{
		MEMFILE* infile = MEMFILE_OpenData(lpvFileData, (long)nSize );
		if( infile == NULL )
		{
			logprintf (("Could not open : %s\n", lpszFileName ));
			return NULL;
		}

		BITMAPINFO* pBmpInfo = _ReadTGAData( infile, pBitmap, bPremultiply );

		MEMFILE_Close (infile);		

		return pBmpInfo;

	} // texReadTGAData

	static BOOL _LoadTGA( MEMFILE* infile, LPVOID* lppvPixels, ULONG* pulWidth, ULONG* pulHeight, UINT* puiBitCount )
	{
		TGAHeader		tgaHeaderX;
		TGAHeader*      tgaHeader = &tgaHeaderX;
		int				channels = 0;

		BlockO24BitPixels   blockX;
		BlockO24BitPixels*  blockPtr = &blockX;
		
		pixel32 colorMap[MAX_COLORMAP];
		bool			fColorMap = false;

		unsigned long		bufferWidth;
		unsigned long		bufferHeight;

		assert( lppvPixels != NULL );
		assert( infile != NULL );

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

		bool bVFlip = !!(tgaHeader->idesc & TGA_IDESC_VFLIP);
		switch (tgaHeader->itype)
		{
		case 1: // uncompressed color map
			if (tgaHeader->bpp != 8)
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load uncompressed tga (%s)\n", filename));
				if (!_LoadUncompressedTGA (blockPtr, channels, infile, colorMap, bVFlip))
				{
					goto cleanup;
				}
			}
			break;
		case 2: // uncompressed true color
			if (((tgaHeader->bpp != 24) && (tgaHeader->bpp != 32)))
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load uncompressed tga (%s)\n", lpszFileName));
				if( !_LoadUncompressedTGA( blockPtr, channels, infile, NULL, bVFlip ) )
				{
					goto cleanup;
				}
			}
			break;
		case 3: // uncompressed black & white
			if (tgaHeader->bpp != 8)
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load uncompressed tga (%s)\n", filename));
				if (!_LoadUncompressedTGA (blockPtr, channels, infile, NULL, bVFlip ))
				{
					goto cleanup;
				}
			}
			break;
		case 9: // rle compressed color map
			if (tgaHeader->bpp != 8)
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				if (!_LoadCompressedTGA (blockPtr, channels, infile, colorMap, bVFlip ))
				{
					goto cleanup;
				}
			}
			break;
		case 10: // rle compressed true color
			if (((tgaHeader->bpp != 24) && (tgaHeader->bpp != 32)))
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load compressed tga (%s)\n", filename));
				if (!_LoadCompressedTGA (blockPtr, channels, infile, NULL, bVFlip))
				{
					goto cleanup;
				}
			}
			break;
		case 11: // rle compressed 256-color
			if (tgaHeader->bpp != 8)
			{
				logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
				goto cleanup;
			}
			else
			{
				logprintf (("load compressed tga (%s)\n", filename));
				if (!_LoadCompressedTGA (blockPtr, channels, infile, NULL, bVFlip))
				{
					goto cleanup;
				}
			}
			break;
		default:
			logprintf (("Unsupported TGA file type\n"));
			goto cleanup;
		}

		logprintf (("--success--\n"));
		if( puiBitCount != NULL ) *puiBitCount = 32; // 这里都返回32bit的图像数据
		return TRUE;

cleanup:
		logprintf (("--failed--\n"));
		return FALSE;
	}

	
	// 加载TGA数据,并读取TGA的宽度信息和高度信息
	// 该TGA数据可用于生成bgra32位格式的bitmap,但高度要取负值
	// 返回的*lppvPixels数据需要用FreeTGAData()释放
	BOOL LoadTGAData(LPCTSTR lpszFileName, LPVOID* lppvPixels, ULONG* pulWidth, ULONG* pulHeight, UINT* puiBitCount)
	{
		if (lppvPixels == NULL)
		{
			assert(!"无效的参数!");
			return FALSE;
		}

		MEMFILE*  infile = MEMFILE_Load(lpszFileName);
		if( infile == NULL )
		{
			logprintf (("Could not open : %s\n", lpszFileName ));
			return FALSE;
		}

		BOOL bRet = _LoadTGA(infile, lppvPixels, pulWidth, pulHeight, puiBitCount );
		
		MEMFILE_Close(infile);

		return bRet;

	} // LoadTGAData

} // namespace HELPER