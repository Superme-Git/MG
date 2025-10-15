#include "../../../contrib/lpng1234/png.h"
#include "../../../pfs/pfs2.h"
#include "PNGDecoder.h"
#include "../Picture.h"

static void PNGAPI _PNGReadDataProxy(png_structp png_ptr, png_bytep data, png_size_t length)
{
	PFS::CBaseFile * pFile = static_cast<PFS::CBaseFile*>(png_get_io_ptr(png_ptr));
	if( pFile->Read( data, length ) != length)
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

/* Read a PNG file.  You may want to return an error code if the read
 * fails (depending upon the failure).  There are two "prototypes" given
 * here - one where we are given the filename, and we need to open the
 * file, and the other where we are given an open file (possibly with
 * some or all of the magic bytes read - see comments above).
 */
static bool _PngDecode(PFS::CBaseFile& rFile, MHZX::CPicture& rPicture)  /* file is already open */
{
	png_structp png_ptr;
	png_infop info_ptr;
	png_uint_32 width, height;
	int bit_depth, color_type, interlace_type;
	unsigned int sig_read = 0;

	/* Create and initialize the png_struct with the desired error handler
	* functions.  If you want to use the default stderr and longjump method,
	* you can supply NULL for the last three parameters.  We also supply the
	* the compiler header file version, so that we know if the application
	* was compiled with a compatible version of the library.  REQUIRED
	*/
	png_ptr = png_create_read_struct(
		PNG_LIBPNG_VER_STRING, NULL,
		_PNGErrorCallback, _PNGWarnCallback);

	if (png_ptr == NULL)
	{
		return (false);
	}

	/* Allocate/initialize the memory for image information.  REQUIRED. */
	info_ptr = png_create_info_struct(png_ptr);
	if (info_ptr == NULL)
	{
		png_destroy_read_struct(&png_ptr, png_infopp_NULL, png_infopp_NULL);
		return (false);
	}

	/* Set error handling if you are using the setjmp/longjmp method (this is
	* the normal method of doing things with libpng).  REQUIRED unless you
	* set up your own error handlers in the png_create_read_struct() earlier.
	*/

	if (setjmp(png_jmpbuf(png_ptr)))
	{
		/* Free all of the memory associated with the png_ptr and info_ptr */
		png_destroy_read_struct(&png_ptr, &info_ptr, png_infopp_NULL);
		
		/* If we get here, we had a problem reading the file */
		return (false);
	}

	/* If you are using replacement read functions, instead of calling
	* png_init_io() here you would call:
	*/
	png_set_read_fn(png_ptr, (void *)&rFile, &_PNGReadDataProxy);
	/* where user_io_ptr is a structure you want available to the callbacks */

	/* If we have already read some of the signature */
	png_set_sig_bytes(png_ptr, sig_read);

	#ifdef hilevel
	/*
	* If you have enough memory to read in the entire image at once,
	* and you need to specify only transforms that can be controlled
	* with one of the PNG_TRANSFORM_* bits (this presently excludes
	* dithering, filling, setting background, and doing gamma
	* adjustment), then you can read the entire image (including
	* pixels) into the info structure with this call:
	*/
	png_read_png(png_ptr, info_ptr, png_transforms, png_voidp_NULL);
	#else
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
	//png_set_packing(png_ptr);

	/* Change the order of packed pixels to least significant bit first
	* (not useful if you are using png_set_packing). */
	//png_set_packswap(png_ptr);

	/* Expand paletted colors into true RGB triplets */
	//if (color_type == PNG_COLOR_TYPE_PALETTE)
	//	png_set_palette_to_rgb(png_ptr);

	/* Expand grayscale images to the full 8 bits from 1, 2, or 4 bits/pixel */
	//if (color_type == PNG_COLOR_TYPE_GRAY && bit_depth < 8)
	//	png_set_expand_gray_1_2_4_to_8(png_ptr);

	/* Expand paletted or RGB images with transparency to full alpha channels
	* so the data will be available as RGBA quartets.
	*/
	//if (png_get_valid(png_ptr, info_ptr, PNG_INFO_tRNS))
	//	png_set_tRNS_to_alpha(png_ptr);

	/* invert monochrome files to have 0 as white and 1 as black */
	//png_set_invert_mono(png_ptr);

	/* flip the RGB pixels to BGR (or RGBA to BGRA) */
	if (color_type & PNG_COLOR_MASK_COLOR)
		png_set_bgr(png_ptr);

	///* swap the RGBA or GA data to ARGB or AG (or BGRA to ABGR) */
	// png_set_swap_alpha(png_ptr);

	/* swap bytes of 16 bit files to least significant byte first */
	// png_set_swap(png_ptr);

	/* Add filler (or alpha) byte (before/after each RGB triplet) */
	// png_set_filler(png_ptr, 0xff, PNG_FILLER_AFTER);

	/* Turn on interlace handling.  REQUIRED if you are not using
	* png_read_image().  To see how to handle interlacing passes,
	* see the png_read_row() method below:
	*/
	int number_passes = png_set_interlace_handling(png_ptr);

	/* Optional call to gamma correct and add the background to the palette
	* and update info structure.  REQUIRED if you are expecting libpng to
	* update the palette for you (ie you selected such a transform above).
	*/
	// png_read_update_info(png_ptr, info_ptr);

	/* Allocate the memory to hold the image using the fields of info_ptr. */

	/* The easiest way to read the image: */

	png_uint_32 rowsize = info_ptr->rowbytes;

	png_bytep row_buffer = (png_bytep)png_malloc( png_ptr, rowsize);
	
	rPicture.AllocBitmap();

	//rPicture.m_PictureDesc 

	/* Now it's time to read the image.  One of these methods is REQUIRED */
	for (png_uint_32 y = 0; y < height; y += 1)
	{
		png_read_row(png_ptr, row_buffer, NULL);
		rPicture.DecodeAndFillBlock( row_buffer, rowsize, width, 1, 0, y );
	}

	png_free( png_ptr, row_buffer);

	/* read rest of file, and get additional chunks in info_ptr - REQUIRED */
	png_read_end(png_ptr, info_ptr);
	#endif hilevel

	/* At this point you have read the entire image */

	/* clean up after the read, and free any memory allocated - REQUIRED */
	png_destroy_read_struct(&png_ptr, &info_ptr, png_infopp_NULL);

	/* close the file */

	/* that's it */
	return (true);
}

namespace MHZX
{
	bool CPNGDecoder::Decode(PFS::CBaseFile &rFile, CPicture &rPicture)
	{
		if( !_PngDecode(rFile,  rPicture)  )
		{
			// TODO:
			return false;
		}
	
		return true;
	}
}