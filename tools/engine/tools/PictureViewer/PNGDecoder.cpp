#include "StdAfx.h"
#include "PNGDecoder.h"
#include "../../contrib/lpng1234/png.h"

#ifdef _DEBUG
#pragma comment( lib, "../../lib/libpngd.lib")
#else
#pragma comment( lib, "../../lib/libpng.lib")
#endif

#ifndef png_jmpbuf
#  define png_jmpbuf(png_ptr) ((png_ptr)->jmpbuf)
#endif

static void PNGAPI _PNGReadDataProxy(png_structp png_ptr, png_bytep data, png_size_t length)
{
	CFile * pFile = static_cast<CFile*>(png_get_io_ptr(png_ptr));
	if( pFile->Read( data, (UINT)length ) != length)
	{
		// TODO:
		longjmp(png_ptr->jmpbuf, 1);
	}
}

static void _PNGWarnCallback(png_structp png_ptr, png_const_charp msg)
{
	printf("read png warning: %s\n", msg);
	fflush(stdout);
}

static void _PNGErrorCallback(png_structp png_ptr, png_const_charp msg)
{
	fprintf(stderr, "read png error: %s\n", msg);
	fflush(stderr);
	longjmp(png_ptr->jmpbuf, 1);
}

BITMAPINFO* texReadPNG( LPCTSTR filename, HBITMAP* pBitmap, unsigned char** ppPixels)
{
	png_structp png_ptr;
	png_infop info_ptr;
	png_uint_32 width, height;
	int bit_depth, color_type, interlace_type;

	CFile theFile;
	CFile *pFile = &theFile;
	CFileException theException;
	CFileException *pException = &theException;
	if( !pFile->Open(filename, GENERIC_READ, pException ) )
	{
		return NULL;
	}

	png_voidp user_error_ptr = NULL;

	/* Create and initialize the png_struct with the desired error handler
	* functions.  If you want to use the default stderr and longjump method,
	* you can supply NULL for the last three parameters.  We also supply the
	* the compiler header file version, so that we know if the application
	* was compiled with a compatible version of the library.  REQUIRED
	*/
	png_ptr = png_create_read_struct(PNG_LIBPNG_VER_STRING,
		user_error_ptr, _PNGErrorCallback, _PNGWarnCallback);
	if (png_ptr == NULL)
	{
		pFile->Close();
		return (NULL);
	}

	/* Allocate/initialize the memory for image information.  REQUIRED. */
	info_ptr = png_create_info_struct(png_ptr);
	if (info_ptr == NULL)
	{
		pFile->Close();
		png_destroy_read_struct(&png_ptr, png_infopp_NULL, png_infopp_NULL);
		return (NULL);
	}

	/* Set error handling if you are using the setjmp/longjmp method (this is
	* the normal method of doing things with libpng).  REQUIRED unless you
	* set up your own error handlers in the png_create_read_struct() earlier.
	*/
	if (setjmp(png_jmpbuf(png_ptr)))
	{
		/* Free all of the memory associated with the png_ptr and info_ptr */
		png_destroy_read_struct(&png_ptr, &info_ptr, png_infopp_NULL);
		pFile->Close();
		/* If we get here, we had a problem reading the file */
		return (NULL);
	}

	png_set_read_fn(png_ptr, (void *)pFile, _PNGReadDataProxy);
	/* where user_io_ptr is a structure you want available to the callbacks */

	/* OK, you're doing it the hard way, with the lower-level functions */

	/* The call to png_read_info() gives us all of the information from the
	* PNG file before the first IDAT (image data chunk).  REQUIRED
	*/
	png_read_info(png_ptr, info_ptr);

	png_get_IHDR(png_ptr, info_ptr, &width, &height, &bit_depth, &color_type,
		&interlace_type, int_p_NULL, int_p_NULL);

	/* Extract multiple pixels with bit depths of 1, 2, and 4 from a single
	* byte into separate bytes (useful for paletted and grayscale images).
	*/
	png_set_packing(png_ptr);

	/* Change the order of packed pixels to least significant bit first
	* (not useful if you are using png_set_packing). */
	png_set_packswap(png_ptr);

	if (color_type & PNG_COLOR_MASK_COLOR)
	{
		/* Use blue, green, red order for pixels. */
		png_set_bgr(png_ptr);
	}

	/* Expand paletted colors into true RGB triplets */
	if (color_type == PNG_COLOR_TYPE_PALETTE)
		png_set_palette_to_rgb(png_ptr);

	/* Expand grayscale images to the full 8 bits from 1, 2, or 4 bits/pixel */
	if (color_type == PNG_COLOR_TYPE_GRAY && bit_depth < 8)
		png_set_expand_gray_1_2_4_to_8(png_ptr);

	/* Expand paletted or RGB images with transparency to full alpha channels
	* so the data will be available as RGBA quartets.
	*/
	if (png_get_valid(png_ptr, info_ptr, PNG_INFO_tRNS))
		png_set_tRNS_to_alpha(png_ptr);

	BITMAPINFO* bitmapInfo = NULL;

	BYTE* pPixels = NULL;
	{
		long bufferWidth = width;
		long bufferHeight = height;
		long bufferSize;

		if((bufferWidth <= 0 ) || (bufferHeight <= 0 ))
		{
			goto cleanup;
		}

		bufferSize = bufferWidth * bufferHeight * 4;

		bitmapInfo = (BITMAPINFO*)HeapAlloc (GetProcessHeap(), 0, sizeof(BITMAPINFO));
		if (!bitmapInfo)
		{
			goto cleanup;
		}

		bitmapInfo->bmiHeader.biSize = sizeof (BITMAPINFOHEADER);
		bitmapInfo->bmiHeader.biPlanes = 1;
		bitmapInfo->bmiHeader.biBitCount = 32; // because then we don't have to worry about row alignment
		bitmapInfo->bmiHeader.biCompression = BI_RGB;
		bitmapInfo->bmiHeader.biXPelsPerMeter = 0;
		bitmapInfo->bmiHeader.biYPelsPerMeter = 0;
		bitmapInfo->bmiHeader.biClrUsed = 0;
		bitmapInfo->bmiHeader.biClrImportant = 0;
		bitmapInfo->bmiHeader.biWidth = bufferWidth;
		bitmapInfo->bmiHeader.biHeight = bufferHeight;
		bitmapInfo->bmiHeader.biSizeImage = bufferSize;

		HBITMAP hBITMAP = ::CreateDIBSection( NULL, bitmapInfo, DIB_RGB_COLORS, (LPVOID *)&pPixels, NULL, 0);
		if ( hBITMAP == NULL || pPixels == NULL )
		{
			HeapFree(GetProcessHeap(), 0, bitmapInfo );
			goto cleanup;
		}

		*pBitmap = hBITMAP;

		if( ppPixels  != NULL )
			*ppPixels = pPixels;
	}

	png_uint_32 rowbytes = png_get_rowbytes(png_ptr, info_ptr);

	/* Read the image one or more scanlines at a time */
	/* The other way to read images - deal with interlacing: */
	for (png_uint_32 y = 0; y < height; y++)
	{
		png_bytep prow =  pPixels + rowbytes * (height-y-1);
		png_read_rows(png_ptr, &prow, png_bytepp_NULL, 1);
	}

	/* read rest of file, and get additional chunks in info_ptr - REQUIRED */
	png_read_end(png_ptr, info_ptr);

	/* At this point you have read the entire image */

cleanup:

	/* clean up after the read, and free any memory allocated - REQUIRED */
	png_destroy_read_struct(&png_ptr, &info_ptr, png_infopp_NULL);

	/* close the file */
	pFile->Close();

	/* that's it */
	return bitmapInfo;
}
