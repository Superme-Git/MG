/*
 * READPIC.C
 *
 * COPYRIGHT (c) 1992 Echidna.
 * PROGRAMMER : Gregg A. Tavares
 *    VERSION : 00.000
 *    CREATED : 09/30/94
 *   MODIFIED : 12/22/94
 *       TABS : 05 09
 *
 *	     \|///-_
 *	     \oo///_
 *	-----w/-w------
 *	 E C H I D N A
 *	---------------
 *
 * DESCRIPTION
 *		
 *
 * HISTORY
 *
*/


#include "stdafx.h"
#include <stdio.h>
#include "memfile.h"
//#include "readpic.h"

typedef long int32;
typedef short int16;
typedef char int8;
typedef unsigned char UINT8;
typedef unsigned short UINT16;

#define MSBFToNative32Bit(a)    \
	(((((UINT32)(a)) & 0xFF000000L) >> 24L) |   \
	((((UINT32)(a)) & 0x00FF0000L) >>  8L) |  \
	((((UINT32)(a)) & 0x0000FF00L) <<  8L) |  \
	((((UINT32)(a)) & 0x000000FFL) << 24L) )

#define MSBFToNative16Bit(a)    \
	(((((UINT16)(a)) & 0x00FF) << 8) | \
	((((UINT16)(a)) & 0xFF00) >> 8) )


/**************************** C O N S T A N T S ***************************/

#define PIC_UNCOMPRESSED     0x00
#define PIC_MIXED_RUN_LENGTH 0x02

#define PIC_CHANNEL_RED_BIT   0x80
#define PIC_CHANNEL_GREEN_BIT 0x40
#define PIC_CHANNEL_BLUE_BIT  0x20
#define PIC_CHANNEL_ALPHA_BIT 0x10

/******************************** T Y P E S *******************************/

typedef struct PICHeader
{
	int32 magic;
	int32 version;
	char  comment[80];
	char  id[4];
	int16 width;
	int16 height;
	int32 ratio;
	int16 fields;
	int16 pad;
} PICHeader;

typedef struct PICChannel
{
	int8 chained;
	int8 size;
	int8 type;
	int8 channel;
}
PICChannel;

typedef struct
{
	uint8 blue;
	uint8 green;
	uint8 red;
	uint8 alpha;
}
pixel24;

#if 0

#define logprintf(args)	logprintf2 args

void logprintf2 (const char *fmt, ...)
{
	FILE* logfp;

	va_list ap;	/* points to each unnamed arg in turn */

	logfp = fopen ("c:\\gatlog.txt", "a");
	if (logfp)
	{
		va_start (ap,fmt); /* make ap point to 1st unnamed arg */
		vfprintf (logfp, fmt, ap);
		va_end (ap);	/* clean up when done */

		fclose (logfp);
	}
}
#else
#define logprintf(args)


#endif

enum PixelFormat
{
	PIX_FMT_R5G6B5,
	PIX_FMT_A4R4G4B4,
	PIX_FMT_R8G8B8,
	PIX_FMT_A8R8G8B8,
	PIX_FMT_X8R8G8B8,

	//以下目前不支持
	PIX_FMT_A8, // 8bit gray
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
//PIX_FMT_A8R8G8B8: 从高到低的顺序是A,B,G,R (pic文件) A,R,G,B(b0x,p0x文件)
//PIX_FMT_X8R8G8B8: 从高到低的顺序是X,B,G,R (pic文件) A,R,G,B(b0x,p0x文件)
//

#pragma pack(push)
#pragma pack(4)


struct PicHeaderSmall
{
	int format;
	short width;
	short height;
};


struct PicHeader
{
	int format;
	short width;
	short height;
	int blockNum;
};

struct BlockDesc
{
	short	imageWidth;
	short	imageHeight;
	short	offsetX;
	short	offsetY;
	unsigned fileSize;
};

#pragma pack(pop)



/* For expanding 5-bit pixel values to 8-bit with best rounding */

static const UINT8 c5to8bits[32] = {
	0,   8,  16,  25,  33,  41,  49,  58,
	66,  74,  82,  90,  99, 107, 115, 123,
	132, 140, 148, 156, 165, 173, 181, 189,
	197, 206, 214, 222, 230, 239, 247, 255
};

typedef void (*pfn_get_row)( MEMFILE*, int, pixel24* );

//
// A, R、G、B的顺序是从高到低
//
// 结果要是 从高到低 A, B, G, R 
// 或者说 从低到高 R, G, B, A
//
/* This version is for reading 16-bit pixels */
static void get_4444bit_row ( MEMFILE* mf, int iWidth, pixel24*  pPixel )
{	
	int t;
	int col;
	for (col = iWidth; col > 0; col--)
	{
		t = MEMFILE_getc(mf);
		t += MEMFILE_getc(mf) << 8;
		/* We expand 5 bit data to 8 bit sample width.
		* The format of the 16-bit (LSB first) input word is
		*     AAAARRRRGGGGBBBB
		*/
		pPixel->blue = (unsigned char) (t & 0xF) * 17; // B
		t >>= 4;
		pPixel->green = (unsigned char) (t & 0xF) * 17; // G
		t >>= 4;
		pPixel->red = (unsigned char) (t & 0xF) * 17; // R
		t >>= 4;
		pPixel->alpha = (unsigned char) (t & 0xF) * 17; // A

		pPixel ++;
	}
}


//
// 从高到低的顺序是 R G B,
// 结果要是 从高到低 B, G, R 
// 或者说 从低到高 R, G, B 
//
/* This version is for reading 16-bit pixels */
static void get_565bit_row ( MEMFILE* mf, int iWidth, pixel24*  pPixel )
{
	int t;
	int col;
	for (col = iWidth; col > 0; col--)
	{
		t = MEMFILE_getc(mf);
		t += MEMFILE_getc(mf) << 8;
		/* We expand 5 bit data to 8 bit sample width.
		* The format of the 16-bit (LSB first) input word is
		*     RRRRRGGGGGGBBBBB
		*/
		pPixel->blue = (unsigned char) c5to8bits[t & 0x1F]; // B
		t >>= 5;
		pPixel->green = (unsigned char) ((t & 0x3F)*255.0/63.0);// G
		t >>= 6;
		pPixel->red = (unsigned char) c5to8bits[t & 0x1F]; // R
		pPixel->alpha = 255;
		pPixel ++;
	}
}

//
// 从高到低的顺序是 R G B,
// 结果要是 从高到低 B, G, R 
// 或者说 从低到高 R, G, B 
//
static void get_24bit_row ( MEMFILE* mf, int iWidth, pixel24*  pPixel )
/* This version is for reading 24-bit pixels */
{
	int col;
	for (col = iWidth; col > 0; col--)
	{
		// B G R
		{ pPixel->blue   = MEMFILE_getc(mf); } 
		{ pPixel->green = MEMFILE_getc(mf); }
		{ pPixel->red  = MEMFILE_getc(mf); }
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
static void get_32bit_row ( MEMFILE* mf, int iWidth, pixel24*  pPixel )
{
	int col;
	for (col = iWidth; col > 0; col--)
	{
		// R G B A
		{ pPixel->red   = MEMFILE_getc(mf); } 
		{ pPixel->green = MEMFILE_getc(mf); }
		{ pPixel->blue  = MEMFILE_getc(mf); }
		{ pPixel->alpha = MEMFILE_getc(mf); }
		pPixel++;
	}
}

//
// 从高到低的顺序是A, R, G, B 
// 结果要是 从高到低 A, B, G, R 
// 或者说 从低到高 R, G, B, A
//
static void get_32bit_row_new( MEMFILE* mf, int iWidth, pixel24*  pPixel )
{
	// speed up
	MEMFILE_Read( mf, pPixel, iWidth*sizeof(pixel24));
}


BITMAPINFO* texReadPIC( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels)
{
	PicHeaderSmall	phead;
	MEMFILE*		mf;
	BITMAPINFO*		qt_pBitmapInfo = NULL;
	HBITMAP			qt_hBITMAP = NULL;
	char*			qt_pPixels = NULL;
	HDC				qt_screenHDC = NULL;


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
		long bufferWidth  = phead.width;
		long bufferHeight = phead.height;
		long bufferSize;
		int  y;

		if((bufferWidth <= 0 ) || (bufferHeight <= 0 ))
		{
			logprintf (("Invalid PIC file (3), bad size"));
			goto cleanup;
		}

		bufferSize = bufferWidth * bufferHeight * sizeof (pixel24);

		qt_pBitmapInfo = (BITMAPINFO*)HeapAlloc (GetProcessHeap(), 0 , sizeof (BITMAPINFOHEADER) + bufferSize);
		if (!qt_pBitmapInfo)
		{
			goto cleanup;
		}

		qt_pBitmapInfo->bmiHeader.biSize          = sizeof (BITMAPINFOHEADER);
		qt_pBitmapInfo->bmiHeader.biPlanes        = 1;
		qt_pBitmapInfo->bmiHeader.biBitCount      = 32; // because then we don't have to worry about row alignment
		qt_pBitmapInfo->bmiHeader.biCompression   = BI_RGB;
		qt_pBitmapInfo->bmiHeader.biXPelsPerMeter = 0;
		qt_pBitmapInfo->bmiHeader.biYPelsPerMeter = 0;
		qt_pBitmapInfo->bmiHeader.biClrUsed       = 0;
		qt_pBitmapInfo->bmiHeader.biClrImportant  = 0;
		qt_pBitmapInfo->bmiHeader.biWidth         = bufferWidth;
		qt_pBitmapInfo->bmiHeader.biHeight        = bufferHeight;
		qt_pBitmapInfo->bmiHeader.biSizeImage     = bufferSize;

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

		pfn_get_row	get_row = (pfn_get_row)NULL;

		if ( phead.format == PIX_FMT_A8R8G8B8 ||
			phead.format == PIX_FMT_X8R8G8B8 )
		{
			get_row = get_32bit_row;
		}
		else if ( phead.format == PIX_FMT_R8G8B8 )
		{
			get_row = get_24bit_row;
		}
		else if ( phead.format == PIX_FMT_A4R4G4B4 )
		{
			get_row = get_4444bit_row;
		}
		else if ( phead.format == PIX_FMT_R5G6B5 )
		{	
			get_row = get_565bit_row;
		}
		else
		{
			logprintf (("--unsupported yet!--\n"));
			goto cleanup;
		}

		for(y = 0; y < bufferHeight; y++)
		{
			pixel24*  pPixel = ((pixel24*)qt_pPixels) + y/*(bufferHeight - y - 1)*/ * bufferWidth;
			get_row( mf, bufferWidth, pPixel );
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

BITMAPINFO* texReadP0X( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels)
{
	PicHeader		phead;
	MEMFILE*		mf;
	BITMAPINFO*		qt_pBitmapInfo = NULL;
	HBITMAP			qt_hBITMAP = NULL;
	char*			qt_pPixels = NULL;
	HDC				qt_screenHDC = NULL;


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
		long bufferWidth  = phead.width;
		long bufferHeight = phead.height;
		long blockNum = phead.blockNum;

		long bufferSize;
		int  y;

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
			logprintf (("Invalid PIC file, reach EOF when reading BlockDesc"));
			goto cleanup;
		}

		bufferSize = bufferWidth * bufferHeight * sizeof (pixel24);

		qt_pBitmapInfo = (BITMAPINFO*)HeapAlloc (GetProcessHeap(), 0 , sizeof (BITMAPINFOHEADER) + bufferSize);
		if (!qt_pBitmapInfo)
		{
			goto cleanup;
		}

		qt_pBitmapInfo->bmiHeader.biSize          = sizeof (BITMAPINFOHEADER);
		qt_pBitmapInfo->bmiHeader.biPlanes        = 1;
		qt_pBitmapInfo->bmiHeader.biBitCount      = 32; // because then we don't have to worry about row alignment
		qt_pBitmapInfo->bmiHeader.biCompression   = BI_RGB;
		qt_pBitmapInfo->bmiHeader.biXPelsPerMeter = 0;
		qt_pBitmapInfo->bmiHeader.biYPelsPerMeter = 0;
		qt_pBitmapInfo->bmiHeader.biClrUsed       = 0;
		qt_pBitmapInfo->bmiHeader.biClrImportant  = 0;
		qt_pBitmapInfo->bmiHeader.biWidth         = bufferWidth;
		qt_pBitmapInfo->bmiHeader.biHeight        = bufferHeight;
		qt_pBitmapInfo->bmiHeader.biSizeImage     = bufferSize;

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

		pfn_get_row	get_row = (pfn_get_row)NULL;

		if ( phead.format == PIX_FMT_A8R8G8B8 ||
			phead.format == PIX_FMT_X8R8G8B8 )
		{
			get_row = get_32bit_row_new;
		}
		else if ( phead.format == PIX_FMT_R8G8B8 )
		{
			get_row = get_24bit_row;
		}
		else if ( phead.format == PIX_FMT_A4R4G4B4 )
		{
			get_row = get_4444bit_row;
		}
		else if ( phead.format == PIX_FMT_R5G6B5 )
		{	
			get_row = get_565bit_row;
		}
		else
		{
			logprintf (("--unsupported yet!--\n"));
			goto cleanup;
		}

		for(y = 0; y < bufferHeight; y++)
		{
			pixel24*  pPixel = ((pixel24*)qt_pPixels) + y/*(bufferHeight - y - 1)*/ * bufferWidth;
			get_row( mf, bufferWidth, pPixel );
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

BITMAPINFO* texReadB0X( LPCTSTR fileName, HBITMAP* pBitmap, char** ppPixels)
{
	PicHeader		phead;
	MEMFILE*		mf;
	BITMAPINFO*		qt_pBitmapInfo = NULL;
	HBITMAP			qt_hBITMAP = NULL;
	char*			qt_pPixels = NULL;
	HDC				qt_screenHDC = NULL;
	BlockDesc*		blockDescs = NULL;

	if ( !(mf = MEMFILE_Load(  fileName) ) )
		return NULL;

	if ( MEMFILE_Read( mf, &phead, sizeof( PicHeader)) != sizeof( PicHeader))		// 读头
	{
		logprintf (("Invalid PIC file"));
		goto cleanup;
	}

	// 格式校验
	if ( phead.format < 0 || phead.format > PIX_FMT_X8R8G8B8)
	{
		logprintf (("Invalid PIC file format"));
		goto cleanup;
	}

	long bufferWidth  = phead.width;
	long bufferHeight = phead.height;
	long blockNum = phead.blockNum;

	if((bufferWidth <= 0 ) || (bufferHeight <= 0 ))
	{
		logprintf (("Invalid PIC file (3), bad size"));
		goto cleanup;
	}

	if ( blockNum < 1 )
	{
		logprintf (("Invalid block Num."));
		goto cleanup;
	}

	blockDescs = new BlockDesc[phead.blockNum];					// 读块描述
	if ( blockDescs == NULL )
		goto cleanup;

	long blockDescSize = sizeof( BlockDesc) * phead.blockNum;
	if ( MEMFILE_Read( mf, blockDescs,  blockDescSize) != blockDescSize)
		goto cleanup;

	long bufferSize = phead.width * phead.height * sizeof(pixel24);

	// 分配缓冲区
	qt_pBitmapInfo = (BITMAPINFO*)HeapAlloc (GetProcessHeap(), 0 , sizeof (BITMAPINFOHEADER) + bufferSize);
	if (!qt_pBitmapInfo)
	{
		goto cleanup;
	}

	qt_pBitmapInfo->bmiHeader.biSize          = sizeof (BITMAPINFOHEADER);
	qt_pBitmapInfo->bmiHeader.biPlanes        = 1;
	qt_pBitmapInfo->bmiHeader.biBitCount      = 32; // because then we don't have to worry about row alignment
	qt_pBitmapInfo->bmiHeader.biCompression   = BI_RGB;
	qt_pBitmapInfo->bmiHeader.biXPelsPerMeter = 0;
	qt_pBitmapInfo->bmiHeader.biYPelsPerMeter = 0;
	qt_pBitmapInfo->bmiHeader.biClrUsed       = 0;
	qt_pBitmapInfo->bmiHeader.biClrImportant  = 0;
	qt_pBitmapInfo->bmiHeader.biWidth         = bufferWidth;
	qt_pBitmapInfo->bmiHeader.biHeight        = bufferHeight;
	qt_pBitmapInfo->bmiHeader.biSizeImage     = bufferSize;

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
	char* bitmapBuffer = qt_pPixels;

	int bypp = 4;
	int picLineSize = phead.width * bypp;

	pfn_get_row	get_row = (pfn_get_row)NULL;

	if ( phead.format == PIX_FMT_A8R8G8B8 ||
		phead.format == PIX_FMT_X8R8G8B8 )
	{
		get_row = get_32bit_row_new;
	}
	else if ( phead.format == PIX_FMT_R8G8B8 )
	{
		get_row = get_24bit_row;
	}
	else if ( phead.format == PIX_FMT_A4R4G4B4 )
	{
		get_row = get_4444bit_row;
	}
	else if ( phead.format == PIX_FMT_R5G6B5 )
	{	
		get_row = get_565bit_row;
	}
	else
	{
		logprintf (("--unsupported yet!--\n"));
		goto cleanup;
	}

	for ( int i = 0; i < phead.blockNum; ++i)
	{
		BlockDesc& desc = blockDescs[i];

		char* blockPtr = bitmapBuffer + (/*bufferHeight-1-*/desc.offsetY)* picLineSize + desc.offsetX * bypp;
		for(long y = 0; y < desc.imageHeight; y++)
		{
			pixel24*  pPixel = ((pixel24*)blockPtr) + (/*-*/y) * bufferWidth;
			get_row( mf, desc.imageWidth, pPixel );
		}
	}

	logprintf (("--done--\n"));
	if ( blockDescs != NULL )
	{
		delete[] blockDescs;
		blockDescs = NULL;
	}

	if (mf) MEMFILE_Close(mf);
	return qt_pBitmapInfo;


cleanup:
	if ( blockDescs != NULL )
	{
		delete[] blockDescs;
		blockDescs = NULL;
	}

	if (qt_hBITMAP) DeleteObject (qt_hBITMAP);
	if (qt_pBitmapInfo) HeapFree (GetProcessHeap(), 0 , qt_pBitmapInfo);
	if (mf) MEMFILE_Close(mf);

	logprintf (("--failed--\n"));

	return NULL;
}


BITMAPINFO* texReadPIC2( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels)
{
	PICHeader   phead;
	PICChannel  channel[4];
	int         i;
	int			alphaChannel = 0;
	MEMFILE*		mf;
	BITMAPINFO*		qt_pBitmapInfo = NULL;
	HBITMAP			qt_hBITMAP = NULL;
	char*			qt_pPixels = NULL;
	HDC				qt_screenHDC = NULL;

	logprintf (("-----------loading (%s)-------------------\n", filename ));

	mf = MEMFILE_Load(filename);
	if( mf == NULL ) {
		logprintf (("Could not open : %s\n", filename ));
		goto cleanup;
	}
	if (MEMFILE_Read(mf, &phead, sizeof( phead )) != sizeof (phead))
	{
		logprintf (("Invalid PIC file"));
		goto cleanup;
	}

	phead.magic   = MSBFToNative32Bit (phead.magic);
	phead.version = MSBFToNative32Bit (phead.version);
	phead.width   = MSBFToNative16Bit (phead.width);
	phead.height  = MSBFToNative16Bit (phead.height);
	phead.ratio   = MSBFToNative32Bit (phead.ratio);
	phead.fields  = MSBFToNative16Bit (phead.fields);

	for(i = 0; i < 4; i++)
	{
		if ( MEMFILE_Read(mf, &channel[i], sizeof(PICChannel)) !=  sizeof (PICChannel))
		{
			logprintf (("Invalid PIC file (2)"));
			goto cleanup;
		}

#if 1
		logprintf(("\n"));
		logprintf(("\t\tchannel[%d].chained = %d\n", i, channel[i].chained));
		logprintf(("\t\tchannel[%d].size    = %d\n", i, channel[i].size));
		logprintf(("\t\tchannel[%d].type    = %d\n", i, channel[i].type));
		logprintf(("\t\tchannel[%d].channel = %d\n", i, channel[i].channel));
		logprintf(("\n"));
#endif

		if (!channel[i].chained)
		{
			break;
		}
	}

	if (i == 4)
	{
		logprintf (("Too Many Channels"));
		goto cleanup;
	}

	{
		long bufferWidth  = phead.width;
		long bufferHeight = phead.height;
		long bufferSize;
		int  c;
		int  y;
		pixel24* pEOB;

		if((bufferWidth <= 0 ) || (bufferHeight <= 0 ))
		{
			logprintf (("Invalid PIC file (3), bad size"));
			goto cleanup;
		}

		bufferSize = bufferWidth * bufferHeight * sizeof (pixel24);

		qt_pBitmapInfo = (BITMAPINFO*)HeapAlloc (GetProcessHeap(), 0 , sizeof (BITMAPINFOHEADER) + bufferSize);
		if (!qt_pBitmapInfo)
		{
			goto cleanup;
		}

		qt_pBitmapInfo->bmiHeader.biSize          = sizeof (BITMAPINFOHEADER);
		qt_pBitmapInfo->bmiHeader.biPlanes        = 1;
		qt_pBitmapInfo->bmiHeader.biBitCount      = 32; // because then we don't have to worry about row alignment
		qt_pBitmapInfo->bmiHeader.biCompression   = BI_RGB;
		qt_pBitmapInfo->bmiHeader.biXPelsPerMeter = 0;
		qt_pBitmapInfo->bmiHeader.biYPelsPerMeter = 0;
		qt_pBitmapInfo->bmiHeader.biClrUsed       = 0;
		qt_pBitmapInfo->bmiHeader.biClrImportant  = 0;
		qt_pBitmapInfo->bmiHeader.biWidth         = bufferWidth;
		qt_pBitmapInfo->bmiHeader.biHeight        = bufferHeight;
		qt_pBitmapInfo->bmiHeader.biSizeImage     = bufferSize;

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

		if ( ppPixels != NULL )
		{
			*ppPixels = qt_pPixels;
		}
		*pBitmap = qt_hBITMAP;

		pEOB = ((pixel24*)qt_pPixels) + bufferWidth * bufferHeight;

		for(y = 0; y < bufferHeight; y++)
		{
			for(c = 0; c < i + 1; c++)
			{
				pixel24*  pPixel;
				uint8     channels = channel[c].channel;

				pPixel = ((pixel24*)qt_pPixels) + (bufferHeight - y - 1) * bufferWidth;

				if (channels == 0)
				{
					logprintf(("bad channel flags\n"));
					goto cleanup;
				}

				switch(channel[c].type)
				{
				case PIC_UNCOMPRESSED:
					{
						long x;

						for(x = 0; x < bufferWidth; x++)
						{
							if (channels & PIC_CHANNEL_RED_BIT)   { pPixel->red   = MEMFILE_getc(mf); }
							if (channels & PIC_CHANNEL_GREEN_BIT) { pPixel->green = MEMFILE_getc(mf); }
							if (channels & PIC_CHANNEL_BLUE_BIT)  { pPixel->blue  = MEMFILE_getc(mf); }
							if (channels & PIC_CHANNEL_ALPHA_BIT) { pPixel->alpha = MEMFILE_getc(mf); }
							pPixel++;
						}
					}
					break;
				case PIC_MIXED_RUN_LENGTH:
					{
						long x = 0;

						while (x < bufferWidth)
						{
							long count = MEMFILE_getc(mf);

							if(count > 128)
							{
								uint8 r,g,b, a;

								if (channels & PIC_CHANNEL_RED_BIT)   { r = MEMFILE_getc(mf); }
								if (channels & PIC_CHANNEL_GREEN_BIT) { g = MEMFILE_getc(mf); }
								if (channels & PIC_CHANNEL_BLUE_BIT)  { b = MEMFILE_getc(mf); }
								if (channels & PIC_CHANNEL_ALPHA_BIT) { a = MEMFILE_getc(mf); }

								count -= 127;

								if (pPixel + count > pEOB)
								{
									logprintf (("error reading file"));
									goto cleanup;
								}

								while (count > 0)
								{
									if (channels & PIC_CHANNEL_RED_BIT)   { pPixel->red   = r; }
									if (channels & PIC_CHANNEL_GREEN_BIT) { pPixel->green = g; }
									if (channels & PIC_CHANNEL_BLUE_BIT)  { pPixel->blue  = b; }
									if (channels & PIC_CHANNEL_ALPHA_BIT) { pPixel->alpha = a; }

									pPixel++;
									x++;
									count--;
								}
							}
							else if (count == 128)
							{
								uint8 r,g,b,a;

								count  = MEMFILE_getc(mf) * 256;
								count += MEMFILE_getc(mf);

								if (channels & PIC_CHANNEL_RED_BIT)   { r = MEMFILE_getc(mf); }
								if (channels & PIC_CHANNEL_GREEN_BIT) { g = MEMFILE_getc(mf); }
								if (channels & PIC_CHANNEL_BLUE_BIT)  { b = MEMFILE_getc(mf); }
								if (channels & PIC_CHANNEL_ALPHA_BIT) { a = MEMFILE_getc(mf); }

								if (pPixel + count > pEOB)
								{
									logprintf (("error reading file (2)"));
									goto cleanup;
								}

								while (count > 0)
								{
									if (channels & PIC_CHANNEL_RED_BIT)   { pPixel->red   = r; }
									if (channels & PIC_CHANNEL_GREEN_BIT) { pPixel->green = g; }
									if (channels & PIC_CHANNEL_BLUE_BIT)  { pPixel->blue  = b; }
									if (channels & PIC_CHANNEL_ALPHA_BIT) { pPixel->alpha = a; }

									pPixel++;
									x++;
									count--;
								}
							}
							else /* if (count < 128) */
							{
								count++;

								if (pPixel + count > pEOB)
								{
									logprintf (("error reading file (3)"));
									goto cleanup;
								}

								while (count > 0)
								{
									if (channels & PIC_CHANNEL_RED_BIT)   { pPixel->red   = MEMFILE_getc(mf); }
									if (channels & PIC_CHANNEL_GREEN_BIT) { pPixel->green = MEMFILE_getc(mf); }
									if (channels & PIC_CHANNEL_BLUE_BIT)  { pPixel->blue  = MEMFILE_getc(mf); }
									if (channels & PIC_CHANNEL_ALPHA_BIT) { pPixel->alpha = MEMFILE_getc(mf); }

									pPixel++;
									x++;
									count--;
								}
							}
						}
					}
					break;
				default:
					logprintf (("error reading file (3)"));
					goto cleanup;
				}
			}
		}
	}

#if 0
	{
		for (int yy = 0; yy < 16; yy++)
		{
			pixel24* pPixel = ((pixel24*)qt_pPixels) + yy * qt_pBitmapInfo->bmiHeader.biWidth;

			logprintf (("%02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x %02x%02x%02x\n",
				pPixel[ 0].red, pPixel[ 0].green, pPixel[ 0].blue,
				pPixel[ 1].red, pPixel[ 1].green, pPixel[ 1].blue,
				pPixel[ 2].red, pPixel[ 2].green, pPixel[ 2].blue,
				pPixel[ 3].red, pPixel[ 3].green, pPixel[ 3].blue,
				pPixel[ 4].red, pPixel[ 4].green, pPixel[ 4].blue,
				pPixel[ 5].red, pPixel[ 5].green, pPixel[ 5].blue,
				pPixel[ 6].red, pPixel[ 6].green, pPixel[ 6].blue,
				pPixel[ 7].red, pPixel[ 7].green, pPixel[ 7].blue,
				pPixel[ 8].red, pPixel[ 8].green, pPixel[ 8].blue,
				pPixel[ 9].red, pPixel[ 9].green, pPixel[ 9].blue,
				pPixel[10].red, pPixel[10].green, pPixel[10].blue,
				pPixel[11].red, pPixel[11].green, pPixel[11].blue,
				pPixel[12].red, pPixel[12].green, pPixel[12].blue,
				pPixel[13].red, pPixel[13].green, pPixel[13].blue,
				pPixel[14].red, pPixel[14].green, pPixel[14].blue,
				pPixel[15].red, pPixel[15].green, pPixel[15].blue));
		}
	}
#endif
#if 0
	{
		extern void saveTGA(const char* filename, const char* extra, BITMAPINFO* qt_pBitmapInfo, char* qt_pPixels);

		saveTGA (filename, "_rd", qt_pBitmapInfo, qt_pPixels);
	}
#endif

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

