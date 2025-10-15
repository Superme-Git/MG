/*************************************************************************
 *                                                                       *
 *                              READTGA.CPP                              *
 *                                                                       *
 *************************************************************************

                          Copyright 1996 Echidna

   DESCRIPTION


   PROGRAMMERS


   FUNCTIONS

   TABS : 5 9

   HISTORY
		03/28/96 : Created.

	The Echidna Copyright
	
	Copyright 1991-2003 Echidna, Inc. All rights reserved.
	
	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are
	met:
	
	* Redistributions of source code must retain the above copyright notice,
	  this list of conditions and the following disclaimer.
	
	* Redistributions in binary form must reproduce the above copyright
	  notice, this list of conditions and the following disclaimer in the
	  documentation and/or other materials provided with the distribution.
	
	THIS SOFTWARE IS PROVIDED BY Echidna ``AS IS'' AND ANY EXPRESS OR IMPLIED
	WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
	MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN
	NO EVENT SHALL Echidna OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
	INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
	NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
	DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
	THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
	THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
	
	The views and conclusions contained in the software and documentation are
	those of the authors and should not be interpreted as representing
	official policies, either expressed or implied, of Echidna or
	Echidna, Inc.

 *************************************************************************/

/**************************** I N C L U D E S ****************************/

#include "stdafx.h"
#include "memfile.h"

/**************************** C O N S T A N T S ***************************/

// most tga files default to being read bottom first
// set this to 1 to leave them that way

#define READ_UPSIDEDOWN 1
#define USE_LOGFILE     0
#define SAVE_TARGAS     0

/******************************** T Y P E S *******************************/

typedef unsigned char uint8;

typedef struct
{
    uint8	blue;
    uint8	green;
    uint8	red;
	uint8	dummy;
}
pixel24;

typedef struct BlockO24BitPixels
{
	long	 width;
	long	 height;
	pixel24	*rgb;
} BlockO24BitPixels;

// descriptor: 00vhaaaa 
// h horizontal flip 
// v vertical flip 
// a alpha bits 
#define TGA_IDESC_HFLIP	0x10
#define	TGA_IDESC_VFLIP	0x20

typedef struct TGAHeader
{
	uint8	ID;				// Byte 0
	uint8	ctype;			// Byte 1
	uint8	itype;			// Byte 2
	uint8	mincolorl;		// Byte 3
	uint8	mincolorh;		// Byte 4
	uint8	colorsl;		// Byte 5
	uint8	colorsh;		// Byte 6
	uint8	colorsize;		// Byte 7
	uint8	originxl;		// Byte 8
	uint8	originxh;		// Byte 9
	uint8	originyl;		// Byte 10
	uint8	originyh;		// Byte 11
	uint8	widthl;			// Byte 12
	uint8	widthh;			// Byte 13
	uint8	heightl;		// Byte 14
	uint8	heighth;		// Byte 15
	uint8	bpp;			// Byte 16
	uint8	idesc;			// Byte 17
} TGAHeader;

/****************************** G L O B A L S *****************************/


/******************************* M A C R O S ******************************/


/***************************** R O U T I N E S ****************************/

#if USE_LOGFILE
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

/*********************************************************************
 *
 * loadUncompressedTGA
 *
 * SYNOPSIS
 *		void  loadUncompressedTGA (BlockO32BitPixels *blockPtr, unsigned char *fileBufferPtr)
 *
 * PURPOSE
 *		
 *
 * INPUT
 *
 *
 * EFFECTS
 *
 *
 * RETURN VALUE
 *
 *
 * HISTORY
 *
 *
 * SEE ALSO
 *
*/
static bool loadUncompressedTGA (BlockO24BitPixels *blockPtr, int channels, MEMFILE *mf, pixel24* colorMap, uint8 idesc)
{

	pixel24* bufferStart;
	pixel24* buffer;

	long	 i;
	long	 loop;
	long	 lineSize;
	long	 bufferSize;
	long	 bufferWidth;
	long	 bufferHeight;
	long	 lineMod;

	bufferWidth  = blockPtr->width;
	bufferHeight = blockPtr->height;
	lineSize     = bufferWidth;
	bufferSize   = lineSize * bufferHeight;

    #if READ_UPSIDEDOWN	
		if (idesc & TGA_IDESC_VFLIP)
		{
	    	bufferStart  = blockPtr->rgb + bufferSize - lineSize;
			lineMod      = -lineSize;
		}
		else
		{
	    	bufferStart  = blockPtr->rgb;
			lineMod      = lineSize;
		}
    #else
		if (idesc & TGA_IDESC_VFLIP)
		{
	    	bufferStart  = blockPtr->rgb;
			lineMod      = lineSize;
		}
		else
		{
	    	bufferStart  = blockPtr->rgb + bufferSize - lineSize;
			lineMod      = -lineSize;
		}
    #endif

	buffer = bufferStart;

	switch (channels)
	{
	case 1:
		if (colorMap)
		{
			// No Alpha / Uncompressed
			
			for (loop = 0; loop < bufferHeight; loop++)
			{
				buffer = bufferStart;
				
				for (i = 0; i < bufferWidth; i++)
				{
					uint8	k;
	
					k = MEMFILE_getc(mf);
	
					buffer->red   = colorMap[k].red;
					buffer->green = colorMap[k].green;
					buffer->blue  = colorMap[k].blue;
					// buffer->alpha = 255;
					buffer++;
				}
				
				#if READ_UPSIDEDOWN	
					bufferStart += lineMod;
				#else
					bufferStart -= lineMod;
				#endif
			}
		}
		else
		{
			// No Alpha / Uncompressed
			
			for (loop = 0; loop < bufferHeight; loop++)
			{
				buffer = bufferStart;
				
				for (i = 0; i < bufferWidth; i++)
				{
					uint8	k;
	
					k = MEMFILE_getc(mf);
	
					buffer->red   = k;
					buffer->green = k;
					buffer->blue  = k;
					// buffer->alpha = 255;
					buffer++;
				}
				
				#if READ_UPSIDEDOWN	
					bufferStart += lineMod;
				#else
					bufferStart -= lineMod;
				#endif
			}
		}
		break;
	case 3:
		{
			// No Alpha / Uncompressed
			
			for (loop = 0; loop < bufferHeight; loop++)
			{
				buffer = bufferStart;
				
				for (i = 0; i < bufferWidth; i++)
				{
					uint8	r,g,b;
	
					b = MEMFILE_getc(mf);
					g = MEMFILE_getc(mf);
					r = MEMFILE_getc(mf);
	
					buffer->red   = r;
					buffer->green = g;
					buffer->blue  = b;
					// buffer->alpha = 255;
					buffer++;
				}
				
				#if READ_UPSIDEDOWN	
					bufferStart += lineMod;
				#else
					bufferStart -= lineMod;
				#endif
			}
		}
		break;
	case 4:
		{
			// Alpha / Uncompressed
			
			for (loop = 0; loop < bufferHeight; loop++)
			{
				buffer = bufferStart;
				
				for (i = 0; i < bufferWidth; i++)
				{
					uint8	r,g,b,a;
	
					b = MEMFILE_getc(mf);
					g = MEMFILE_getc(mf);
					r = MEMFILE_getc(mf);
					a = MEMFILE_getc(mf);
	
					buffer->red   = r;
					buffer->green = g;
					buffer->blue  = b;
					// buffer->alpha = a;
					buffer++;
				}
				#if READ_UPSIDEDOWN	
					bufferStart += lineMod;
				#else
					bufferStart -= lineMod;
				#endif
			}
		}
		break;
	default:
		return false;
	}
	return true;
}
// loadUncompressedTGA

/*********************************************************************
 *
 * flipBuffer
 *
 * SYNOPSIS
 *		void  flipBuffer (void *buffer, long rowSize, long rows)
 *
 * PURPOSE
 *		
 *
 * INPUT
 *
 *
 * EFFECTS
 *
 *
 * RETURN VALUE
 *
 *
 * HISTORY
 *
 *
 * SEE ALSO
 *
*/
bool flipBuffer (void *buffer, long rowSize, long rows)
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
}
// flipBuffer

/*********************************************************************
 *
 * loadCompressedTGA
 *
 * SYNOPSIS
 *		void  loadCompressedTGA (BlockO32BitPixels *blockPtr, uint8 *fileBufferPtr)
 *
 * PURPOSE
 *		
 *
 * INPUT
 *
 *
 * EFFECTS
 *
 *
 * RETURN VALUE
 *
 *
 * HISTORY
 *
 *
 * SEE ALSO
 *
*/
bool loadCompressedTGA (BlockO24BitPixels *blockPtr, int channels, MEMFILE *mf, pixel24* colorMap, uint8 idesc)
{
	uint8			a;
	uint8			r;
	uint8			g;
	uint8			b;
	uint8			i;
	uint8			count;
	pixel24*		buffer;

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
				// buffer->alpha = a;
				buffer++;
			}
		}
		else
		{
			// dump data

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
				// buffer->alpha = a;
				buffer++;
			}
		}
	}

    #if READ_UPSIDEDOWN
		if (idesc & TGA_IDESC_VFLIP)
		{
    		flipBuffer (blockPtr->rgb, blockPtr->width * sizeof (pixel24), blockPtr->height);
		}
		else
		{
		}
		return true;
    #else
		if (idesc & TGA_IDESC_VFLIP)
		{
		}
		else
		{
    		flipBuffer (blockPtr->rgb, blockPtr->width * sizeof (pixel24), blockPtr->height);
		}
    #endif
}
// loadCompressedTGA


/*********************************************************************
 *
 * loadTGA32Bit
 *
 * SYNOPSIS
 *		void loadTGA32Bit (BlockO32BitPixels *blockPtr, char *fileName)
 *
 * PURPOSE
 *		
 *
 * INPUT
 *
 *
 * EFFECTS
 *
 *
 * RETURN VALUE
 *
 *
 * HISTORY
 *
 *
 * SEE ALSO
 *
*/
#define MAX_COLORMAP 256

BITMAPINFO* texReadTGA( LPCTSTR filename, HBITMAP* pBitmap, char** ppPixels)
{
    MEMFILE*        infile = NULL;         // file pointer to infile
	TGAHeader		tgaHeaderX;
	TGAHeader*      tgaHeader = &tgaHeaderX;

    BITMAPINFO*		qt_pBitmapInfo = NULL;
    HBITMAP			qt_hBITMAP = NULL;
    char*			qt_pPixels = NULL;
    HDC				qt_screenHDC = NULL;
	int				channels = 0;

    BlockO24BitPixels   blockX;
    BlockO24BitPixels*  blockPtr = &blockX;
	
	pixel24 colorMap[MAX_COLORMAP];
	bool			fColorMap = false;
    	
	long			bufferWidth;
	long			bufferHeight;

    infile = MEMFILE_Load(filename);
    if( infile == NULL ) {
         logprintf (("Could not open : %s\n", filename ));
         goto cleanup;
    }

	MEMFILE_Read(infile, tgaHeader, sizeof (TGAHeader));
	
//	if (tgaHeader->idesc & 0xf0)
//	{
//		logprintf (("Invalid TGA file\n"));
//		goto cleanup;
//	}

	#if 0
	{
		printf ("\n");
		printf ("\t\ttgaHeader->ID          = %d\n", tgaHeader->ID);
		printf ("\t\ttgaHeader->ctype       = %d\n", tgaHeader->ctype);
		printf ("\t\ttgaHeader->itype       = %d\n", tgaHeader->itype);
		printf ("\t\ttgaHeader->mincolorl   = %d\n", tgaHeader->mincolorl);
		printf ("\t\ttgaHeader->mincolorh   = %d\n", tgaHeader->mincolorh);
		printf ("\t\ttgaHeader->colorsl     = %d\n", tgaHeader->colorsl);
		printf ("\t\ttgaHeader->colorsh     = %d\n", tgaHeader->colorsh);
		printf ("\t\ttgaHeader->colorsize   = %d\n", tgaHeader->colorsize);
		printf ("\t\ttgaHeader->originxl    = %d\n", tgaHeader->originxl);
		printf ("\t\ttgaHeader->originxh    = %d\n", tgaHeader->originxh);
		printf ("\t\ttgaHeader->originyl    = %d\n", tgaHeader->originyl);
		printf ("\t\ttgaHeader->originyh    = %d\n", tgaHeader->originyh);
		printf ("\t\ttgaHeader->widthl      = %d\n", tgaHeader->widthl);
		printf ("\t\ttgaHeader->widthh      = %d\n", tgaHeader->widthh);
		printf ("\t\ttgaHeader->heightl     = %d\n", tgaHeader->heightl);
		printf ("\t\ttgaHeader->heighth     = %d\n", tgaHeader->heighth);
		printf ("\t\ttgaHeader->bpp         = %d\n", tgaHeader->bpp);
		printf ("\t\ttgaHeader->idesc       = %d\n", tgaHeader->idesc);
		printf ("\n");
	}
	#endif

	MEMFILE_Seek (infile, tgaHeader->ID, SEEK_CUR); // Skip User Info
	
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
	
		int	firstColor = ((long)tgaHeader->mincolorl + (long)tgaHeader->mincolorh * 256L);
		int	numColors  = ((long)tgaHeader->colorsl   + (long)tgaHeader->colorsh   * 256L);
		
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
				}
				break;
			case 32:
				for (colorNdx = 0; colorNdx < numColors; ++colorNdx)
				{
					uint8 r,g,b;
					b = MEMFILE_getc(infile);
					g = MEMFILE_getc(infile);
					r = MEMFILE_getc(infile);
					MEMFILE_getc(infile);
					
					colorMap[firstColor + colorNdx].red   = r;
					colorMap[firstColor + colorNdx].green = g;
					colorMap[firstColor + colorNdx].blue  = b;
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
		logprintf (("unsupported bits per pixel (%s)\n", filename));
		goto cleanup;
	}

		
	bufferWidth  = ((long)tgaHeader->widthl  + (long)tgaHeader->widthh * 256L);
	bufferHeight = ((long)tgaHeader->heightl + (long)tgaHeader->heighth * 256L);
	
    long	bufferSize;

	#if 0	
	bufferWidth = 31;
	bufferHeight = 32;
	tgaHeader->itype = 0;
	#endif

    bufferSize = bufferWidth * bufferHeight * sizeof (pixel24);

    qt_pBitmapInfo = (BITMAPINFO*)HeapAlloc (GetProcessHeap(), 0 , sizeof (BITMAPINFOHEADER) + bufferSize);
    if (!qt_pBitmapInfo)
    {
        goto cleanup;
    }

    qt_pBitmapInfo->bmiHeader.biSize          = sizeof (BITMAPINFOHEADER);
    qt_pBitmapInfo->bmiHeader.biPlanes        = 1;
    qt_pBitmapInfo->bmiHeader.biBitCount      = 32;		// we make this 32 bits because then we don't have to worry about aligning each row to a DWORD
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

    *pBitmap = qt_hBITMAP;
    *ppPixels = qt_pPixels;

    blockPtr->rgb      = (pixel24 *)qt_pPixels;;
    blockPtr->width    = bufferWidth;
    blockPtr->height   = bufferHeight;
	
	switch (tgaHeader->itype)
	{
	#if 0
	case 0: // test
		{
			memset (blockPtr->rgb, 0, bufferSize);
			for (int x = 0; x < bufferWidth; ++x)
			{
				blockPtr->rgb[0                  * bufferWidth + x].red = 255;
				blockPtr->rgb[1                  * bufferWidth + x].red = 255;
				blockPtr->rgb[(bufferHeight - 2) * bufferWidth + x].red = 255;
				blockPtr->rgb[(bufferHeight - 1) * bufferWidth + x].red = 255;
			}
			for (int y = 0; y < bufferHeight; ++y)
			{
				blockPtr->rgb[0               + bufferWidth * y].green = 255;
				blockPtr->rgb[1               + bufferWidth * y].green = 255;
				blockPtr->rgb[bufferWidth - 2 + bufferWidth * y].green = 255;
				blockPtr->rgb[bufferWidth - 1 + bufferWidth * y].green = 255;
			}
		}
		break;
	#endif
	case 1: // uncompressed color map
		if (tgaHeader->bpp != 8)
		{
			logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
			goto cleanup;
		}
		else
		{
            logprintf (("load uncompressed tga (%s)\n", filename));
			if (!loadUncompressedTGA (blockPtr, channels, infile, colorMap, tgaHeader->idesc))
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
            logprintf (("load uncompressed tga (%s)\n", filename));
			if (!loadUncompressedTGA (blockPtr, channels, infile, NULL, tgaHeader->idesc))
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
			if (!loadUncompressedTGA (blockPtr, channels, infile, NULL, tgaHeader->idesc))
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
            logprintf (("load compressed tga (%s)\n", filename));
			if (!loadCompressedTGA (blockPtr, channels, infile, colorMap, tgaHeader->idesc))
			{
				goto cleanup;
			}
		}
		break;
	case 10: // rle compressed black & white
		if (((tgaHeader->bpp != 24) && (tgaHeader->bpp != 32)))
		{
			logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
			goto cleanup;
		}
		else
		{
            logprintf (("load compressed tga (%s)\n", filename));
			if (!loadCompressedTGA (blockPtr, channels, infile, NULL, tgaHeader->idesc))
			{
				goto cleanup;
			}
		}
		break;
	case 11: // rle compressed black & white
		if (tgaHeader->bpp != 8)
		{
			logprintf (("Unsupported bit depth (%d)\n", tgaHeader->bpp));
			goto cleanup;
		}
		else
		{
            logprintf (("load compressed tga (%s)\n", filename));
			if (!loadCompressedTGA (blockPtr, channels, infile, NULL, tgaHeader->idesc))
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

    #if SAVE_TARGAS
    {
        char name[MAX_PATH];
        char *s = filename + strlen (filename);

        while (s > filename && s[-1] != '\\') --s;
        if (*s == '\\') ++s;

        strcpy (name, "c:\\temp\\test\\");
        strcat (name, s);

        bool saveTGA24Bit (const char* filename, BlockO24BitPixels *bop);

        logprintf (("save tga (%s)\n", name));
        saveTGA24Bit (name, blockPtr);
    }
    #endif

    return qt_pBitmapInfo;

cleanup:
	if (qt_hBITMAP) DeleteObject (qt_hBITMAP);
    if (qt_pBitmapInfo) HeapFree (GetProcessHeap(), 0 , qt_pBitmapInfo);
    if (infile) MEMFILE_Close(infile);

    logprintf (("--failed--\n"));

    return NULL;
}



/*************************************************************************
                              saveTGA24Bit
 *************************************************************************

   SYNOPSIS
		int saveTGA24Bit (int fh, BlockO24BitPixels *bop)

   PURPOSE


   INPUT
		fh  :
		bop :

   OUTPUT
		None

   EFFECTS
		None

   RETURNS


   SEE ALSO


   TODO
 	* Don't use CHK functions cause they exit


   HISTORY
		07/15/96 : Created.

 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
#if SAVE_TARGAS
bool saveTGA24Bit (const char* filename, BlockO24BitPixels *bop)
{
	static TGAHeader tgaHeader =
	{
	0,	//	uint8	ID;				// Byte 0
	0,	//	uint8	ctype;			// Byte 1
	2,	//	uint8	itype;			// Byte 2
	0,	//	uint8	mincolorl;		// Byte 3
	0,	//	uint8	mincolorh;		// Byte 4
	0,	//	uint8	colorsl;		// Byte 5
	0,	//	uint8	colorsh;		// Byte 6
	0,	//	uint8	colorsize;		// Byte 7
	0,	//	uint8	originxl;		// Byte 8
	0,	//	uint8	originxh;		// Byte 9
	0,	//	uint8	originyl;		// Byte 10
	0,	//	uint8	originyh;		// Byte 11
	0,	//	uint8	widthl;			// Byte 12
	0,	//	uint8	widthh;			// Byte 13
	0,	//	uint8	heightl;		// Byte 14
	0,	//	uint8	heighth;		// Byte 15
	24,	//	uint8	bpp;			// Byte 16
	0,	//	uint8	idesc;			// Byte 17
	};
    DWORD numWritten;
	HANDLE	fh;
	uint8 *BRGBuffer;

	fh = CreateFile (filename, GENERIC_WRITE, 0, NULL, CREATE_ALWAYS, 0, NULL);
	if (fh == INVALID_HANDLE_VALUE)
	{
		logprintf (("can't open file for writing (%s)\n", filename));
		return false;
    }

	BRGBuffer = (uint8 *)HeapAlloc (GetProcessHeap(), 0 , bop->width * bop->height * 3);
	if (!BRGBuffer)
	{
		logprintf (("Out of memory saving tga"));
		return false;
	}

	tgaHeader.widthl = (uint8)(bop->width % 256);
	tgaHeader.widthh = (uint8)(bop->width / 256);

	tgaHeader.heightl = (uint8)(bop->height % 256);
	tgaHeader.heighth = (uint8)(bop->height / 256);

    WriteFile (fh, &tgaHeader, sizeof (tgaHeader), &numWritten, NULL);

	{
		pixel24	*s = bop->rgb;
		uint8	*d = BRGBuffer;
		long	 count = bop->width * bop->height;

		while (count)
		{
			*d++ = s->blue;
			*d++ = s->green;
			*d++ = s->red;
			//*d++ = s->alpha;

			s++;
			count--;
		}
	}

    // if we read them upsidedown then don't flip them for saving
    #if READ_UPSIDEDOWN
    #else
    	flipBuffer (BRGBuffer, bop->width * 3, bop->height);
    #endif
    WriteFile (fh, BRGBuffer, bop->width * bop->height * 3, &numWritten, NULL);
	HeapFree (GetProcessHeap(), 0 , BRGBuffer);
    CloseHandle (fh);

	return TRUE;

}
#endif


