#include "stdafx.h"
#include <stdio.h>
#include "memfile.h"

typedef long int32;
typedef short int16;
typedef char int8;
typedef unsigned char UINT8;
typedef unsigned short UINT16;
typedef unsigned int UINT32;

//
//	ppm文件的开始为三行ASCII码文本
//	第一行是:   P6
//			第二行是图像的大小,先是列象素数,后是行象素数,中间有一空格,
//			例如320X240的图像,次行为: 320 240
//			第三行是一个介于1和65525的整数,当然也是文本的,用来表示
//			每一个象素的一个分量(如红,绿,蓝)用几个比特表示, 1-255
//			表示一个分量用8比特表示.
//			每一行的结尾一换行或回车或两者都用表示.
//	
//			三行文本之后就是图像的数据流,从左到右,从上到下.
//	
//			如一个320X240的RGB图象,每分量用8比特表示,
//	文件如下:
//	P6
//	320 240
//	255

// P6\n%d %d\n255\n
typedef struct PpmHeader
{
	UINT8	subtype;// 2, 3, 5, 6
	UINT32	width;
	UINT32	height;
	UINT16	maxval; // 1 ~ 65535
} PpmHeader;

typedef struct Pixel24
{
	uint8 blue;
	uint8 green;
	uint8 red;
	//uint8 dummy;
} pixel24;

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

int pbm_getc (MEMFILE * infile)
/* Read next char, skipping over any comments */
/* A comment/newline sequence is returned as a newline */
{
	register int ch;

	ch = MEMFILE_getc(infile);
	if (ch == '#') {
		do {
			ch = MEMFILE_getc(infile);
		} while (ch != '\n' && ch != EOF);
	}
	return ch;
}

UINT32 read_pbm_integer (MEMFILE * infile)
/* Read an unsigned decimal integer from the PPM file */
/* Swallows one trailing character after the integer */
/* Note that on a 16-bit-int machine, only values up to 64k can be read. */
/* This should not be a problem in practice. */
{
	register int ch;
	register unsigned int val;

	/* Skip any leading whitespace */
	do {
		ch = pbm_getc(infile);
		if (ch == EOF)
		{
			return -1;
		}
	} while (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r');

	if (ch < '0' || ch > '9')
	{
		// ERROR
		return 0;
	}

	val = ch - '0';
	while ((ch = pbm_getc(infile)) >= '0' && ch <= '9') {
		val *= 10;
		val += ch - '0';
	}
	return val;
}

static void get_24bit_row ( MEMFILE* mf, int iWidth, pixel24*  pPixel )
/* This version is for reading 24-bit pixels */
{
	int col;
	for (col = iWidth; col > 0; col--)
	{
		// R G B
		{ pPixel->red	= MEMFILE_getc(mf); } 
		{ pPixel->green = MEMFILE_getc(mf); }
		{ pPixel->blue	= MEMFILE_getc(mf);  }
		//{ pPixel->dummy = 255; }
		pPixel++;
	}
}

BITMAPINFO* texReadPPM( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels)
{
	PpmHeader		phead;
	BITMAPINFO*		qt_pBitmapInfo = NULL;
	HBITMAP			qt_hBITMAP = NULL;
	char*			qt_pPixels = NULL;
	HDC				qt_screenHDC = NULL;
	MEMFILE*		mf = NULL;

	logprintf (("-----------loading (%s)-------------------\n", filename ));

	mf = MEMFILE_Load( filename );

	if ( mf == NULL )
	{
		logprintf (("Invalid PPM file"));
		return NULL;
	}

	if ( MEMFILE_getc(mf) != 'P' )
	{
		logprintf (("Invalid PPM file"));
		goto cleanup;
	}

	phead.subtype = MEMFILE_getc(mf);
	/* detect unsupported variants (ie, PBM) before trying to read header */
	switch (phead.subtype)
	{
	case '2':			/* it's a text-format PGM file */
	case '3':			/* it's a text-format PPM file */
	case '5':			/* it's a raw-format PGM file */
		logprintf (("--unsupported yet!--\n"));
		goto cleanup;
	case '6':			/* it's a raw-format PPM file */
		break;
	default:
		logprintf (("Invalid PPM file"));
		goto cleanup;
	}

	phead.width = read_pbm_integer(mf);
	phead.height = read_pbm_integer(mf);
	phead.maxval = read_pbm_integer(mf);

	if (phead.width <= 0 || phead.height  <= 0 || phead.maxval <= 0) /* error check */
	{
		logprintf (("Invalid PPM file (6), bad size"));
		goto cleanup;
	}

	{
		long bufferWidth  = phead.width;
		long bufferHeight = phead.height;
		long bufferSize;
		int  y;

		int strideAbs = (bufferWidth * 3 + 3) & 0xFFFFFFFC; // row alignment with 4 bytes
		bufferSize = strideAbs * bufferHeight;
		
		qt_pBitmapInfo = (BITMAPINFO*)HeapAlloc (GetProcessHeap(), 0, sizeof (BITMAPINFO));
		if (!qt_pBitmapInfo)
		{
			goto cleanup;
		}

		qt_pBitmapInfo->bmiHeader.biSize          = sizeof (BITMAPINFOHEADER);
		qt_pBitmapInfo->bmiHeader.biPlanes        = 1;
		qt_pBitmapInfo->bmiHeader.biBitCount      = 24;
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
			*ppPixels = qt_pPixels;
		*pBitmap = qt_hBITMAP;
		
		if ( phead.subtype == '6' )
		{
			for(y = 0; y < bufferHeight; y++)
			{
				pixel24* pPixel = (pixel24*)(qt_pPixels + (bufferHeight-y-1) * strideAbs);
				get_24bit_row( mf, bufferWidth, pPixel );
			}
		}
		else
		{
			logprintf (("--unsupported yet!--\n"));
			goto cleanup;
		}
	}

	logprintf (("--done--\n"));

	if( mf ) MEMFILE_Close( mf );
	return qt_pBitmapInfo;

cleanup:
	if (qt_hBITMAP) DeleteObject (qt_hBITMAP);
	if (qt_pBitmapInfo) HeapFree (GetProcessHeap(), 0 , qt_pBitmapInfo);
	if( mf )MEMFILE_Close( mf );
	logprintf (("--failed--\n"));
	return NULL;

}
